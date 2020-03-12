package org.activiti.cloud.api.process.model.impl.events;

import org.activiti.api.process.model.IntegrationContext;
import org.activiti.cloud.api.process.model.impl.IntegrationRequestImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class IntegrationRequestImplTest {

    @Mock
    private IntegrationContext integrationContext;

    @Test
    public void should_always_haveAppVersionSet(){
        given(integrationContext.getAppVersion()).willReturn("1");
        IntegrationRequestImpl integrationRequest = new IntegrationRequestImpl(integrationContext);
        assertThat(integrationRequest.getAppVersion()).isEqualTo("1");
    }

}
