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

import org.activiti.api.process.model.events.IntegrationEvent.IntegrationEvents;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.process.model.events.CloudIntegrationRequestedEvent;
import org.activiti.cloud.services.query.app.repository.BPMNActivityRepository;
import org.activiti.cloud.services.query.app.repository.IntegrationContextRepository;
import org.activiti.cloud.services.query.model.IntegrationContextEntity;
import org.activiti.cloud.services.query.model.IntegrationContextEntity.IntegrationContextStatus;

public class IntegrationRequestedEventHandler extends BaseIntegrationEventHandler implements QueryEventHandler {

    public IntegrationRequestedEventHandler(IntegrationContextRepository repository,
                                            BPMNActivityRepository bpmnActivityRepository) {
        super(repository,
              bpmnActivityRepository);
    }

    @Override
    public void handle(CloudRuntimeEvent<?, ?> event) {
        CloudIntegrationRequestedEvent integrationEvent = CloudIntegrationRequestedEvent.class.cast(event);

        IntegrationContextEntity entity = findOrCreateIntegrationContextEntity(integrationEvent);

        entity.setRequestDate(new Date(integrationEvent.getTimestamp()));
        entity.setStatus(IntegrationContextStatus.INTEGRATION_REQUESTED);
        entity.setInboundVariables(entity.getInboundVariables());

        persistIntoDatabase(event,
                            entity);

    }

    @Override
    public String getHandledEvent() {
        return IntegrationEvents.INTEGRATION_REQUESTED.name();
    }
}