package org.activiti.cloud.services.rest.assemblers;

import org.activiti.api.runtime.model.impl.ProcessDefinitionImpl;
import org.activiti.cloud.api.process.model.CloudProcessDefinition;
import org.activiti.cloud.api.process.model.impl.CloudProcessDefinitionImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProcessDefinitionResourceAssemblerTest {

    @InjectMocks
    private ProcessDefinitionResourceAssembler resourceAssembler;

    @Mock
    private ToCloudProcessDefinitionConverter converter;

    @Test
    public void toResourceShouldReturnResourceWithSelfLinkContainingResourceId() {
        ProcessDefinitionImpl processDefinition = new ProcessDefinitionImpl();
        processDefinition.setId("my-identifier");
        given(converter.from(processDefinition)).willReturn(new CloudProcessDefinitionImpl(processDefinition));

        Resource<CloudProcessDefinition> processDefinitionResource = resourceAssembler.toResource(processDefinition);

        Link selfResourceLink = processDefinitionResource.getLink("self");

        assertThat(selfResourceLink).isNotNull();
        assertThat(selfResourceLink.getHref()).contains("my-identifier");
    }

}
