package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestGuidedBuyNavigation {

	private String filename = "C:/Users/mag684/workspace/GuidedBuyWorkFlow/src/main/resources/diagrams/GuidedBuyNavigation.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("guidedBuyNavigation.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		variableMap.put("orgId", "11328");
		variableMap.put("repId", "20438");
		variableMap.put("marketSector", "FED");
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("guidedBuyNavigation", variableMap);
		
		Object obj = runtimeService.getVariable(processInstance.getId(), "navigationSteps");
		
		System.out.println("navigationSteps ---------- "+obj);
		System.out.println("--------------------- ********************* 1 ---------- ");
		
		
		ExecutionQuery executionQuery = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).activityId("receivetask1");
		Execution execution	= executionQuery.singleResult();		
		runtimeService.setVariable(execution.getId(), "displayQED", "Y");
		runtimeService.signal(execution.getId());
		
		
		obj = runtimeService.getVariable(processInstance.getId(), "navigationSteps");
		
		System.out.println("navigationSteps ---------- "+obj);
		System.out.println("--------------------- ********************* 2 ---------- ");
		
		executionQuery = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).activityId("receivetask2");
		execution	= executionQuery.singleResult();		
		runtimeService.setVariable(execution.getId(), "createSC", "Y");
		runtimeService.signal(execution.getId());
		
		
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "+ processInstance.getProcessDefinitionId());
	}
}