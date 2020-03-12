package org.activiti.cloud.services.core.commands;

import java.util.HashMap;
import java.util.Map;

import org.activiti.api.task.model.payloads.CompleteTaskPayload;
import org.activiti.api.task.runtime.TaskAdminRuntime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompleteTaskCmdExecutorTest {

    @InjectMocks
    private CompleteTaskCmdExecutor completeTaskCmdExecutor;

    @Mock
    private TaskAdminRuntime taskAdminRuntime;

    @Test
    public void completeTaskCmdExecutorTest() {
        Map<String, Object> variables = new HashMap<>();
        CompleteTaskPayload completeTaskPayload = new CompleteTaskPayload("taskId",
                                                                          variables);

        assertThat(completeTaskCmdExecutor.getHandledType()).isEqualTo(CompleteTaskPayload.class.getName());

        completeTaskCmdExecutor.execute(completeTaskPayload);

        verify(taskAdminRuntime).complete(completeTaskPayload);
    }
}
