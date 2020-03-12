package org.activiti.cloud.services.rest.assemblers;

import org.activiti.api.process.model.ProcessInstance;
import org.activiti.cloud.api.process.model.CloudProcessInstance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessInstanceResourceAssemblerTest {

    @InjectMocks
    private ProcessInstanceResourceAssembler resourceAssembler;

    @Mock
    private ToCloudProcessInstanceConverter toCloudProcessInstanceConverter;

    @Test
    public void toResourceShouldReturnResourceWithSelfLinkContainingResourceId() {
        //given
        CloudProcessInstance cloudModel = mock(CloudProcessInstance.class);
        given(cloudModel.getId()).willReturn("my-identifier");

        ProcessInstance model = mock(ProcessInstance.class);
        given(toCloudProcessInstanceConverter.from(model)).willReturn(cloudModel);

        //when
        Resource<CloudProcessInstance> resource = resourceAssembler.toResource(model);

        //then
        Link selfResourceLink = resource.getLink("self");
        assertThat(selfResourceLink).isNotNull();
        assertThat(selfResourceLink.getHref()).contains("my-identifier");
    }
}
