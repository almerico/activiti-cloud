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

package org.activiti.cloud.services.audit.jpa.streams;

import java.util.HashMap;
import java.util.UUID;

import org.activiti.api.process.model.events.ProcessRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.model.shared.impl.events.CloudRuntimeEventImpl;
import org.activiti.cloud.services.audit.api.converters.APIEventToEntityConverters;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.jpa.events.AuditEventEntity;
import org.activiti.cloud.services.audit.jpa.repository.EventsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuditConsumerChannelHandlerImplTest {

    @InjectMocks
    private AuditConsumerChannelHandlerImpl handler;

    @Mock
    private EventsRepository eventsRepository;

    @Mock
    private APIEventToEntityConverters converters;

    @Test
    public void receiveEventShouldStoreEntity() {
        //given
        CloudRuntimeEvent cloudRuntimeEvent = mock(CloudRuntimeEventImpl.class);
        when(cloudRuntimeEvent.getEventType()).thenReturn(ProcessRuntimeEvent.ProcessEvents.PROCESS_CREATED);
        EventToEntityConverter converter = mock(EventToEntityConverter.class);
        when(converters.getConverterByEventTypeName(ProcessRuntimeEvent.ProcessEvents.PROCESS_CREATED.name())).thenReturn(converter);
        AuditEventEntity entity = mock(AuditEventEntity.class);
        when(converter.convertToEntity(cloudRuntimeEvent)).thenReturn(entity);

        CloudRuntimeEvent[] events = {cloudRuntimeEvent};

        //when
        handler.receiveCloudRuntimeEvent(new HashMap<String,Object>(){{put("id", UUID.randomUUID());}}, events);

        //then
        verify(eventsRepository).save(entity);
    }

    @Test
    public void messageIdShouldBeSet(){
        //given
        CloudRuntimeEvent cloudRuntimeEvent = mock(CloudRuntimeEventImpl.class);
        when(cloudRuntimeEvent.getEventType()).thenReturn(ProcessRuntimeEvent.ProcessEvents.PROCESS_CREATED);
        EventToEntityConverter converter = mock(EventToEntityConverter.class);
        when(converters.getConverterByEventTypeName(ProcessRuntimeEvent.ProcessEvents.PROCESS_CREATED.name())).thenReturn(converter);
        AuditEventEntity entity = mock(AuditEventEntity.class);
        when(converter.convertToEntity(cloudRuntimeEvent)).thenReturn(entity);

        CloudRuntimeEvent[] events = {cloudRuntimeEvent};

        HashMap <String,Object> headers = new HashMap<>();
        headers.put("id", UUID.randomUUID());

        //when
        handler.receiveCloudRuntimeEvent(headers, events);

        //then
        verify((CloudRuntimeEventImpl)cloudRuntimeEvent).setMessageId(headers.get("id").toString());
    }

}
