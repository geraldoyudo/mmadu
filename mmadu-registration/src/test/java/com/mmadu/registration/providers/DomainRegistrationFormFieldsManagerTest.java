package com.mmadu.registration.providers;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileReader;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class DomainRegistrationFormFieldsManagerTest {
    public static final String USER_HOME = System.getProperty("user.home");
    @Value("${mmadu.registration.templates}")
    private String templatesFolder;

    @Mock
    private FormFieldsGenerator formFieldsGenerator;
    @Mock
    private DomainService domainService;
    @InjectMocks
    private DomainRegistrationFormFieldsManager formFieldsManager = new DomainRegistrationFormFieldsManager();

    private static File file;

    @BeforeClass
    public static void setUpClass() {
        file = new File(USER_HOME + "/mmadu-test/templates/domain");
    }

    @Before
    public void setUp() {
        formFieldsManager.setTemplatesFolder(USER_HOME + "/mmadu-test/templates");
        doReturn(asList("1")).when(domainService).getDomainIds();
        doReturn("fields-1").when(formFieldsGenerator).generateFormFieldsForDomain("1");
    }

    @Test
    public void onStartUpManagerShouldCreateNestedFolders() throws Exception {
        formFieldsManager.startMonitoring();
        assertThat(FileCopyUtils.copyToString(new FileReader(new File(file, "register-1.html"))),
                equalTo("fields-1"));
    }

    @AfterClass
    public static void tearDown() {
        File file = new File(System.getProperty("user.home") + "/mmadu-test");
        file.delete();
    }

}