package com.mmadu.registration.providers;

import com.mmadu.registration.exceptions.FormFieldsGenerationException;
import com.mmadu.registration.models.RegistrationFieldModifiedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.UnicastProcessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static com.mmadu.registration.models.RegistrationFieldModifiedEvent.ALL_DOMAIN;

@Component
public class DomainRegistrationFormFieldsManager {
    private int sampleTimeInSeconds = 5;
    private DomainService domainService;
    private FormFieldsGenerator formFieldsGenerator;

    private UnicastProcessor<RegistrationFieldModifiedEvent> processor = UnicastProcessor.create();
    private FluxSink<RegistrationFieldModifiedEvent> sink = processor.serialize().sink();
    private File templateDirectory;

    private Resource templatesDirectoryResource;

    @Value("${mmadu.registration.templates}")
    public void setTemplatesDirectoryResource(Resource templatesDirectoryResource) {
        this.templatesDirectoryResource = templatesDirectoryResource;
    }

    @Value("${mmadu.registration.fields-sample-time:5}")
    public void setSampleTimeInSeconds(int sampleTimeInSeconds) {
        this.sampleTimeInSeconds = sampleTimeInSeconds;
    }

    @Autowired
    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    @Autowired
    public void setFormFieldsGenerator(FormFieldsGenerator formFieldsGenerator) {
        this.formFieldsGenerator = formFieldsGenerator;
    }

    public void startMonitoring() throws Exception {
        File templateDirectoryRoot = templatesDirectoryResource.getFile();
        if (templateDirectoryRoot.exists() && !templateDirectoryRoot.isDirectory()) {
            throw new IllegalArgumentException("Template directory resource is not a directory");
        }
        templateDirectory = new File(templateDirectoryRoot, "domain");
        if (!templateDirectory.exists()) {
            boolean successful = templateDirectory.mkdirs();
            if (!successful) {
                throw new IllegalStateException("could not create directory " + templateDirectory.getAbsolutePath());
            }
        }
        generateFormFieldsForAllDomains();
        subscribeToEvent();
    }

    private void generateFormFieldsForAllDomains() {
        List<String> domainIds = domainService.getDomainIds();
        domainIds.stream()
                .forEach(this::generateFormFieldsForDomain);
    }

    private void generateFormFieldsForDomain(String domainId) {
        String formFields = formFieldsGenerator.generateFormFieldsForDomain(domainId);
        try {
            File file = new File(templateDirectory, "register-" + domainId + ".html");
            FileCopyUtils.copy(formFields, new PrintWriter(new FileOutputStream(file)));
        } catch (IOException ex) {
            throw new FormFieldsGenerationException("could not write form fields to file", ex);
        }
    }

    void subscribeToEvent() {
        processor
                .timestamp()
                .distinctUntilChanged(tupule -> tupule.getT2().getDomain() +
                        tupule.getT1() / (sampleTimeInSeconds * 1000))
                .map(tupule -> tupule.getT2())
                .subscribe(this::handleEvent);
    }

    @EventListener(classes = RegistrationFieldModifiedEvent.class)
    public void sendEventToProcessor(RegistrationFieldModifiedEvent event) {
        sink.next(event);
    }

    void handleEvent(RegistrationFieldModifiedEvent event) {
        if (event.getDomain().equals(ALL_DOMAIN)) {
            generateFormFieldsForAllDomains();
        } else {
            generateFormFieldsForDomain(event.getDomain());
        }
    }
}
