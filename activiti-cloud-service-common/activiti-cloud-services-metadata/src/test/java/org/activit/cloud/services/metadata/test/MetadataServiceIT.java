package org.activit.cloud.services.metadata.test;

import java.util.Map;

import org.activiti.cloud.services.metadata.MetadataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource("classpath:propstest.properties")
public class MetadataServiceIT {

    @Autowired
    private MetadataService metadataService;

    @Test
    public void shouldGetMetaData() {

        Map<String, String> metaData = metadataService.getMetadata();

        assertThat(metaData.keySet()).hasSize(5);
        assertThat(metaData.keySet()).contains("activiti-cloud-service-name");
        assertThat(metaData.keySet()).contains("activiti-cloud-service-version");
        assertThat(metaData.keySet()).contains("activiti-cloud-application-name");
        assertThat(metaData.keySet()).contains("activiti-cloud-application-version");
        assertThat(metaData.keySet()).contains("activiti-cloud-service-short-name");

        assertThat(metaData.values()).contains("1");
        assertThat(metaData.values()).contains("app");
    }

}
