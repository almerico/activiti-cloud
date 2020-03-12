package org.activit.cloud.services.metadata.test;

import java.util.HashMap;
import java.util.Map;

import org.activiti.cloud.services.metadata.MetadataProperties;
import org.activiti.cloud.services.metadata.MetadataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MetadataServiceTest {

    @InjectMocks
    private MetadataService metadataService;

    @Mock
    private MetadataProperties metadataProperties;

    @BeforeEach
    public void setUp() {
        HashMap<String,String> application = new HashMap<>();
        application.put("name","app");
        application.put("version","1");

        HashMap<String,String> service = new HashMap<>();
        service.put("name","rb");
        service.put("version","2");

        when(metadataProperties.getApplication()).thenReturn(application);
        when(metadataProperties.getService()).thenReturn(service);
    }

    @Test
    public void shouldGetMetaData() throws Exception {
        Map<String,String> metaData = metadataService.getMetadata();

        assertThat(metaData.keySet()).hasSize(4);
        assertThat(metaData.keySet()).contains("activiti-cloud-service-name");
        assertThat(metaData.keySet()).contains("activiti-cloud-service-version");
        assertThat(metaData.keySet()).contains("activiti-cloud-application-name");
        assertThat(metaData.keySet()).contains("activiti-cloud-application-version");

        assertThat(metaData.values()).contains("1");
        assertThat(metaData.values()).contains("app");
    }

}
