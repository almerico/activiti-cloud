package org.activiti.cloud.services.core.commands;

import org.activiti.api.process.model.payloads.SignalPayload;
import org.activiti.api.process.runtime.ProcessAdminRuntime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignalCmdExecutorTest {

    @InjectMocks
    private SignalCmdExecutor signalCmdExecutor;

    @Mock
    private ProcessAdminRuntime processAdminRuntime;

    @Test
    public void signalProcessInstancesCmdExecutorTest() {
        SignalPayload signalPayload = new SignalPayload("x",
                                                        null);

        assertThat(signalCmdExecutor.getHandledType()).isEqualTo(SignalPayload.class.getName());

        signalCmdExecutor.execute(signalPayload);

        verify(processAdminRuntime).signal(signalPayload);
    }

}
