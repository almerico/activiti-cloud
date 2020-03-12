package org.activiti.cloud.services.core.commands;

import java.util.Collections;

import org.activiti.api.process.model.payloads.StartMessagePayload;
import org.activiti.api.process.runtime.ProcessAdminRuntime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StartMessageCmdExecutorTest {

    @InjectMocks
    private StartMessageCmdExecutor subject;

    @Mock
    private ProcessAdminRuntime processAdminRuntime;

    @Test
    public void signalProcessInstancesCmdExecutorTest() {
        StartMessagePayload payload = new StartMessagePayload("messageName",
                                                              "businessKey",
                                                              Collections.emptyMap());

        assertThat(subject.getHandledType()).isEqualTo(StartMessagePayload.class.getName());

        subject.execute(payload);

        verify(processAdminRuntime).start(payload);
    }

}
