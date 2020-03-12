package org.activiti.cloud.services.core.commands;

import org.activiti.api.task.model.payloads.ReleaseTaskPayload;
import org.activiti.api.task.runtime.TaskAdminRuntime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReleaseTaskCmdExecutorTest {

    @InjectMocks
    private ReleaseTaskCmdExecutor releaseTaskCmdExecutor;

    @Mock
    private TaskAdminRuntime taskAdminRuntime;

    @Test
    public void releaseTaskCmdExecutorTest() {
        //given
        ReleaseTaskPayload releaseTaskPayload = new ReleaseTaskPayload("taskId");
        assertThat(releaseTaskCmdExecutor.getHandledType()).isEqualTo(ReleaseTaskPayload.class.getName());

        //when
        releaseTaskCmdExecutor.execute(releaseTaskPayload);

        //then
        verify(taskAdminRuntime).release(releaseTaskPayload);
    }

}
