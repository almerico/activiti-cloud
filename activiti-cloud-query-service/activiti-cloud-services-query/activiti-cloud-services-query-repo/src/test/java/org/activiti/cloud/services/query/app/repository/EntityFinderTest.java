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

package org.activiti.cloud.services.query.app.repository;

import java.util.Optional;

import com.querydsl.core.types.Predicate;
import org.activiti.cloud.services.query.model.ProcessInstanceEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntityFinderTest {

    @InjectMocks
    private EntityFinder entityFinder;

    @Mock
    private ProcessInstanceRepository repository;

    @Test
    public void findByIdShouldReturnResultWhenIsPresent() throws Exception {
        //given
        String processInstanceId = "5";
        ProcessInstanceEntity processInstanceEntity = mock(ProcessInstanceEntity.class);
        given(repository.findById(processInstanceId)).willReturn(Optional.of(processInstanceEntity));

        //when
        ProcessInstanceEntity retrieveProcessInstanceEntity = entityFinder.findById(repository,
                                                                                    processInstanceId,
                                                                                    "error");

        //then
        assertThat(retrieveProcessInstanceEntity).isEqualTo(processInstanceEntity);
    }

    @Test
    public void findByIdShouldThrowExceptionWhenNotPresent() throws Exception {
        //GIVEM
        String processInstanceId = "5";
        given(repository.findById(processInstanceId)).willReturn(Optional.empty());

        //WHEN
        IllegalStateException e = assertThrows(IllegalStateException.class,
                                               () -> entityFinder.findById(repository, processInstanceId, "Error"));

        //THEN
        assertThat(e).hasMessageContaining("Error");
    }

    @Test
    public void findOneShouldReturnResultWhenIsPresent() throws Exception {
        //given
        Predicate predicate = mock(Predicate.class);
        ProcessInstanceEntity processInstanceEntity = mock(ProcessInstanceEntity.class);
        given(repository.findOne(predicate)).willReturn(Optional.of(processInstanceEntity));

        //when
        ProcessInstanceEntity retrievedProcessInstanceEntity = entityFinder.findOne(repository,
                                                                                    predicate,
                                                                                    "error");

        //then
        assertThat(retrievedProcessInstanceEntity).isEqualTo(processInstanceEntity);
    }

    @Test
    public void findOneShouldThrowExceptionWhenNotPresent() throws Exception {
        //GIVEN
        Predicate predicate = mock(Predicate.class);
        given(repository.findOne(predicate)).willReturn(Optional.empty());

        //WHEN
        IllegalStateException e = assertThrows(IllegalStateException.class,
                                               () -> entityFinder.findOne(repository, predicate, "Error"));

        //THEN
        assertThat(e).hasMessageContaining("Error");
    }

}
