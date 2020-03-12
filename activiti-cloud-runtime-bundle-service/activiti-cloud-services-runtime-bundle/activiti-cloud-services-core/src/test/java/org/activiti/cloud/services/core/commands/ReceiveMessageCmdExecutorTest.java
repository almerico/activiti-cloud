package org.activiti.cloud.services.core.commands;

import java.util.Collections;

import org.activiti.api.process.model.payloads.ReceiveMessagePayload;
import org.activiti.api.process.runtime.ProcessAdminRuntime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReceiveMessageCmdExecutorTest {

    @InjectMocks
    private ReceiveMessageCmdExecutor subject;

    @Mock
    private ProcessAdminRuntime processAdminRuntime;

    @Test
    public void signalProcessInstancesCmdExecutorTest() {
        ReceiveMessagePayload payload = new ReceiveMessagePayload("messageName",
                                                                  "correlationKey",
                                                                  Collections.emptyMap());

        assertThat(subject.getHandledType()).isEqualTo(ReceiveMessagePayload.class.getName());

        subject.execute(payload);

        verify(processAdminRuntime).receive(payload);
    }

}
