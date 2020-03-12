package org.activiti.cloud.services.core.commands;

import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.DeleteProcessPayload;
import org.activiti.api.process.runtime.ProcessAdminRuntime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteProcessInstanceCmdExecutorTest {

    @InjectMocks
    private DeleteProcessInstanceCmdExecutor subject;

    @Mock
    private ProcessAdminRuntime processAdminRuntime;

    @Test
    public void startProcessInstanceCmdExecutorTest() {
        DeleteProcessPayload payload = ProcessPayloadBuilder.delete()
                                                            .withProcessInstanceId("def key")
                                                            .build();

        ProcessInstance fakeProcessInstance = mock(ProcessInstance.class);

        given(processAdminRuntime.delete(payload)).willReturn(fakeProcessInstance);

        assertThat(subject.getHandledType()).isEqualTo(DeleteProcessPayload.class.getName());

        subject.execute(payload);

        verify(processAdminRuntime).delete(payload);

    }
}
