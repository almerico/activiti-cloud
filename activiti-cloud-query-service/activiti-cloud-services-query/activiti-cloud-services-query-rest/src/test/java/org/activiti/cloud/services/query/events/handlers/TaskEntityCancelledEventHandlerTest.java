/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.cloud.services.query.events.handlers;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.events.TaskRuntimeEvent;
import org.activiti.api.task.model.impl.TaskImpl;
import org.activiti.cloud.api.task.model.events.CloudTaskCancelledEvent;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskCancelledEventImpl;
import org.activiti.cloud.services.query.app.repository.TaskRepository;
import org.activiti.cloud.services.query.model.QueryException;
import org.activiti.cloud.services.query.model.TaskEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.activiti.cloud.services.query.events.handlers.TaskBuilder.aTask;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link TaskCancelledEventHandler}
 */
@ExtendWith(MockitoExtension.class)
public class TaskEntityCancelledEventHandlerTest {

    @InjectMocks
    private TaskCancelledEventHandler handler;

    @Mock
    private TaskRepository taskRepository;

    @Test
    public void handleShouldUpdateTaskStatusToCancelled() {
        //given
        CloudTaskCancelledEvent event = buildTaskCancelledEvent();
        String taskId = event.getEntity().getId();
        TaskEntity taskEntity = aTask().withId(taskId).build();
        given(taskRepository.findById(taskId)).willReturn(Optional.of(taskEntity));

        //when
        handler.handle(event);

        //then
        verify(taskRepository).save(taskEntity);
        verify(taskEntity).setStatus(Task.TaskStatus.CANCELLED);
        verify(taskEntity).setLastModified(any(Date.class));
    }

    private CloudTaskCancelledEvent buildTaskCancelledEvent() {
        TaskImpl task = new TaskImpl(UUID.randomUUID().toString(),
                                     "to be cancelled",
                                     Task.TaskStatus.CANCELLED);
        return new CloudTaskCancelledEventImpl(task);
    }

    @Test
    public void handleShouldThrowExceptionWhenTaskNotFound() {
        //GIVEN
        CloudTaskCancelledEvent event = buildTaskCancelledEvent();
        String taskId = event.getEntity().getId();
        given(taskRepository.findById(taskId)).willReturn(Optional.empty());

        //WHEN
        QueryException e = assertThrows(QueryException.class,
                                        () -> handler.handle(event));

        //THEN
        assertThat(e).hasMessageContaining("Unable to find task with id: " + taskId);
    }

    @Test
    public void getHandledEventShouldReturnTaskCancelledEvent() {
        //when
        String handledEvent = handler.getHandledEvent();

        //then
        assertThat(handledEvent).isEqualTo(TaskRuntimeEvent.TaskEvents.TASK_CANCELLED.name());
    }
}
