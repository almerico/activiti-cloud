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

import javax.persistence.EntityManager;

import org.activiti.api.process.model.IntegrationContext;
import org.activiti.api.process.model.events.IntegrationEvent.IntegrationEvents;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.process.model.events.CloudIntegrationErrorReceivedEvent;
import org.activiti.cloud.services.query.app.repository.IntegrationContextRepository;
import org.activiti.cloud.services.query.model.IntegrationContextEntity;
import org.activiti.cloud.services.query.model.IntegrationContextEntity.IntegrationContextStatus;
import org.activiti.cloud.services.query.model.QueryException;

public class IntegrationErrorReceivedEventHandler implements QueryEventHandler {

    private final IntegrationContextRepository repository;
    private final EntityManager entityManager;

    public IntegrationErrorReceivedEventHandler(IntegrationContextRepository repository,
                                                EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    public void handle(CloudRuntimeEvent<?, ?> event) {
        CloudIntegrationErrorReceivedEvent integrationEvent = CloudIntegrationErrorReceivedEvent.class.cast(event);

        IntegrationContext integrationContext = integrationEvent.getEntity();

        IntegrationContextEntity entity = repository.findByProcessInstanceIdAndClientId(integrationContext.getProcessInstanceId(),
                                                                                        integrationContext.getClientId());

        // Let's create entity if does not exists
        if(entity == null) {
            entity = new IntegrationContextEntity(event.getServiceName(),
                                                        event.getServiceFullName(),
                                                        event.getServiceVersion(),
                                                        event.getAppName(),
                                                        event.getAppVersion());
            // Let use event id to persist activity id
            entity.setId(event.getId());
            entity.setClientId(integrationContext.getClientId());
            entity.setClientName(integrationContext.getClientName());
            entity.setClientType(integrationContext.getClientType());
            entity.setConnectorType(integrationContext.getConnectorType());
            entity.setProcessDefinitionId(integrationContext.getProcessDefinitionId());
            entity.setProcessInstanceId(integrationContext.getProcessInstanceId());
            entity.setProcessDefinitionKey(integrationContext.getProcessDefinitionKey());
            entity.setProcessDefinitionVersion(integrationContext.getProcessDefinitionVersion());
            entity.setBusinessKey(integrationContext.getBusinessKey());
        }

        entity.setErrorDate(new Date(integrationEvent.getTimestamp()));
        entity.setStatus(IntegrationContextStatus.ERROR_RECEIVED);
        entity.setErrorMessage(integrationEvent.getErrorMessage());
        entity.setErrorClassName(integrationEvent.getErrorClassName());
        entity.setStackTraceElements(integrationEvent.getStackTraceElements());

        persistIntoDatabase(event,
                            entity);

    }

    private void persistIntoDatabase(CloudRuntimeEvent<?, ?> event,
                                     IntegrationContextEntity entity) {
        try {
            repository.save(entity);
        } catch (Exception cause) {
            throw new QueryException("Error handling CloudIntegrationErrorReceivedEvent[" + event + "]",
                                     cause);
        }
    }

    @Override
    public String getHandledEvent() {
        return IntegrationEvents.INTEGRATION_ERROR_RECEIVED.name();
    }
}
