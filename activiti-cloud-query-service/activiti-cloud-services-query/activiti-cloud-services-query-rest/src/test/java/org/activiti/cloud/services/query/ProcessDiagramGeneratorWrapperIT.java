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

package org.activiti.cloud.services.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.cloud.services.query.rest.ProcessInstanceDiagramController;
import org.activiti.image.exception.ActivitiImageException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

/**
 * Integration tests for ProcessDiagramGeneratorWrapper
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource("classpath:test-process-diagram.properties")
public class ProcessDiagramGeneratorWrapperIT {

    private static final String DEFAULT_DIAGRAM_FONT_NAME = "Serif";

    @SpyBean
    private ProcessDiagramGeneratorWrapper processDiagramGenerator;

    @SpyBean
    private ProcessInstanceDiagramController processInstanceDiagramController;

    @Value("classpath:/processes/SimpleProcess.bpmn20.xml")
    private Resource simpleProcess;

    @Value("classpath:/processes/SubProcessTest.fixSystemFailureProcess.bpmn20.xml")
    private Resource subProcessTest;

    /**
     * Test for generating a valid process diagram
     * <p>
     * 1. parse bpmn with existing diagram
     * 2. generate diagram for the corresponding BPMN model
     * 3. Expected: the diagram is not empty
     */
    @Test
    public void testGenerateProcessDiagram() throws Exception {
        //GIVEN
        BpmnModel bpmnModel = processDiagramGenerator.parseBpmnModelXml(simpleProcess.getInputStream());
        assertThat(bpmnModel.hasDiagramInterchangeInfo()).isTrue();

        //WHEN
        byte[] diagram = processDiagramGenerator.generateDiagram(bpmnModel);

        //THEN
        assertThat(diagram).isNotEmpty();
    }

    /**
     * Test for generating diagram a process without diagram
     * <p>
     * 1. parse bpmn without diagram
     * 2. generate diagram for the corresponding BPMN model
     * 3. Expected: the diagram is not empty
     */
    @Test
    public void testGenerateDiagramForProcessWithNoGraphicInfo() throws Exception {
        //GIVEN
        BpmnModel bpmnModel = processDiagramGenerator.parseBpmnModelXml(subProcessTest.getInputStream());
        assertThat(bpmnModel.hasDiagramInterchangeInfo()).isFalse();

        //WHEN
        byte[] diagram = processDiagramGenerator.generateDiagram(bpmnModel);

        //THEN
        assertThat(diagram).isNotEmpty();
    }

    /**
     * Test for generating diagram a process without diagram when there is no image for the default diagram
     * <p>
     * 1. parse a bpmn without diagram
     * 2. generate diagram for the corresponding BPMN model
     * 3. Expected: the diagram is not empty
     */
    @Test
    public void testGenerateDiagramForProcessWithNoGraphicInfoAndNoDefaultImage() throws Exception {
        //GIVEN
        BpmnModel bpmnModel = processDiagramGenerator.parseBpmnModelXml(subProcessTest.getInputStream());
        assertThat(bpmnModel.hasDiagramInterchangeInfo()).isFalse();

        when(processDiagramGenerator.isGenerateDefaultDiagram())
                .thenReturn(true);
        when(processDiagramGenerator.getDefaultDiagramImageFileName())
                .thenReturn("");

        //WHEN
        byte[] diagram = processDiagramGenerator.generateDiagram(bpmnModel);

        //THEN
        assertThat(diagram).isNotEmpty();
    }

    /**
     * Test for generating diagram a process without diagram when there is no image for the default diagram
     * <p>
     * 1. deploy a process without diagram
     * 2. start the process
     * 3. generate diagram for the corresponding BPMN model
     * 4. Expected: ActivitiException is thrown while generating diagram
     */
    @Test
    public void testGenerateDiagramForProcessWithNoGraphicInfoAndInvalidDefaultImage() throws Exception {
        //GIVEN
        BpmnModel bpmnModel = processDiagramGenerator.parseBpmnModelXml(subProcessTest.getInputStream());
        assertThat(bpmnModel.hasDiagramInterchangeInfo()).isFalse();

        when(processDiagramGenerator.isGenerateDefaultDiagram())
                .thenReturn(true);
        when(processDiagramGenerator.getDefaultDiagramImageFileName())
                .thenReturn("invalid-file-name");

        //WHEN
        ActivitiImageException e = assertThrows(ActivitiImageException.class,
                                                () -> processDiagramGenerator.generateDiagram(bpmnModel));

        //THEN
        assertThat(e).hasMessageContaining("Error occurred while getting default diagram image from file");
    }

    /**
     * Test for generating diagram a process with invalid diagram
     * <p>
     * 1. generate diagram for an invalid BPMN model
     * 2. Expected: ActivitiException is thrown while generating diagram
     */
    @Test
    public void testGenerateDiagramForProcessWithInvalidGraphicInfo() throws Exception {
        //GIVEN
        BpmnModel bpmnModel = new BpmnModel();
        bpmnModel.addGraphicInfo("key",
                                 null);
        assertThat(bpmnModel.hasDiagramInterchangeInfo()).isTrue();

        //WHEN
        RuntimeException e = assertThrows(RuntimeException.class,
                                          () -> processDiagramGenerator.generateDiagram(bpmnModel));

        //THEN
        assertThat(e).hasMessageContaining("Error occurred while getting process diagram");
    }

    /**
     * Test the diagram custom font.
     * <p>
     * 1. Get the diagram activity font name when the custom font is 'Lucida' in properties file
     * 2. Get the diagram label font name when the custom font is 'InvalidFont' in properties file
     * 3. Get the diagram annotation font name when there is no custom font name specified in properties file
     * 4. Expected:
     * - the diagram activity font name is the custom one from properties file
     * - the diagram label font name is the engine default one (Arial)
     * - the diagram annotation font name is the engine default one (Arial)
     */
    @Test
    public void testProcessDiagramFonts() {
        //GIVEN
        //test-process-diagram.properties:
        //activiti.engine.diagram.activity.font=Lucida
        //activiti.engine.diagram.label.font=InvalidFont
        when(processDiagramGenerator.getAvailableFonts())
                .thenReturn(new String[]{"Lucida", "Serif"});

        //WHEN
        String activityFont = processDiagramGenerator.getActivityFontName();
        String labelFont = processDiagramGenerator.getLabelFontName();
        String annotationFont = processDiagramGenerator.getAnnotationFontName();

        //THEN
        assertThat(activityFont).isEqualTo("Lucida");
        assertThat(labelFont).isEqualTo(DEFAULT_DIAGRAM_FONT_NAME);
        assertThat(annotationFont).isEqualTo(DEFAULT_DIAGRAM_FONT_NAME);
    }

    /**
     * Test the diagram custom font when the only available font on the system is the default one ('Arial').
     * <p>
     * Expected: The only used font is the default, no matter what custom font are specified
     */
    @Test
    public void testProcessDiagramFontsWhenWithAvailableFonts() {
        //GIVEN
        when(processDiagramGenerator.getAvailableFonts())
                .thenReturn(new String[]{DEFAULT_DIAGRAM_FONT_NAME});

        //WHEN
        String activityFont = processDiagramGenerator.getActivityFontName();
        String labelFont = processDiagramGenerator.getLabelFontName();
        String annotationFont = processDiagramGenerator.getAnnotationFontName();

        //THEN
        assertThat(activityFont).isEqualTo(DEFAULT_DIAGRAM_FONT_NAME);
        assertThat(labelFont).isEqualTo(DEFAULT_DIAGRAM_FONT_NAME);
        assertThat(annotationFont).isEqualTo(DEFAULT_DIAGRAM_FONT_NAME);
    }
}
