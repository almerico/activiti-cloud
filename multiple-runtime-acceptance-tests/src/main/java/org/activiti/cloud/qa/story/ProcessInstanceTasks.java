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

package org.activiti.cloud.qa.story;

import static org.assertj.core.api.Assertions.assertThat;

import org.activiti.cloud.qa.steps.AuditSteps;
import org.activiti.cloud.qa.steps.QuerySteps;
import org.activiti.cloud.qa.steps.MultipleRuntimeBundleSteps;
import org.activiti.runtime.api.model.CloudProcessInstance;
import org.activiti.runtime.api.model.ProcessInstance;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import net.thucydides.core.annotations.Steps;

public class ProcessInstanceTasks {

    @Steps
    private MultipleRuntimeBundleSteps runtimeBundleSteps;

    @Steps
    private AuditSteps auditSteps;

    @Steps
    private QuerySteps querySteps;

    private CloudProcessInstance processInstance;

    @When("the user starts signal catch process on primary runtime and starts signal throw process on secondary runtime")
    public void startSignalCatchProcessInstance() {
        processInstance = runtimeBundleSteps.startProcess("SignalCatchEventProcess", true);
        assertThat(processInstance).isNotNull();
        runtimeBundleSteps.startProcess("SignalThrowEventProcess", false);
    }

    @Then("a signal was received and the signal catch process was completed")
    public void startSignalThrowProcessInstance() throws Exception {
        querySteps.checkProcessInstanceStatus(processInstance.getId(),
                                              ProcessInstance.ProcessInstanceStatus.COMPLETED);
    }
}
