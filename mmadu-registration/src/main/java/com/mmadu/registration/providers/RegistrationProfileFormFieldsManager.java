package com.mmadu.registration.providers;

import com.mmadu.registration.exceptions.FormFieldsGenerationException;
import com.mmadu.registration.models.RegistrationFieldModifiedEvent;
import com.mmadu.registration.services.RegistrationProfileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.UnicastProcessor;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static com.mmadu.registration.models.RegistrationFieldModifiedEvent.ALL_PROFILE;

@Component
public class RegistrationProfileFormFieldsManager {
    private int sampleTimeInSeconds = 5;
    private RegistrationProfileService registrationProfileService;
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
    public void setRegistrationProfileService(RegistrationProfileService registrationProfileService) {
        this.registrationProfileService = registrationProfileService;
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
            FileUtils.forceMkdir(templateDirectory);
        }
        generateFormFieldsForAllDomains();
        subscribeToEvent();
    }

    private void generateFormFieldsForAllDomains() {
        List<String> profileIds = registrationProfileService.getAllProfileIds();
        profileIds.stream()
                .forEach(this::generateFormFieldsForProfile);
    }

    private void generateFormFieldsForProfile(String profileId) {
        String formFields = formFieldsGenerator.generateFormFieldsForProfile(profileId);
        try {
            File file = new File(templateDirectory, "register-" + profileId + ".html");
            FileCopyUtils.copy(formFields, new PrintWriter(new FileOutputStream(file)));
        } catch (IOException ex) {
            throw new FormFieldsGenerationException("could not write form fields to file", ex);
        }
    }

    void subscribeToEvent() {
        processor
                .timestamp()
                .distinctUntilChanged(tupule -> tupule.getT2().getProfileId() +
                        tupule.getT1() / (sampleTimeInSeconds * 1000))
                .map(Tuple2::getT2)
                .subscribeOn(Schedulers.newBoundedElastic(4,2, "registration-form-generation"))
                .subscribe(this::handleEvent);
    }

    @EventListener(classes = RegistrationFieldModifiedEvent.class)
    public void sendEventToProcessor(RegistrationFieldModifiedEvent event) {
        sink.next(event);
    }

    void handleEvent(RegistrationFieldModifiedEvent event) {
        if (event.getProfileId().equals(ALL_PROFILE)) {
            generateFormFieldsForAllDomains();
        } else {
            generateFormFieldsForProfile(event.getProfileId());
        }
    }
}
