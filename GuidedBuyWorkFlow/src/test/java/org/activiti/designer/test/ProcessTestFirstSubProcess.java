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

public class ProcessTestFirstSubProcess {

	private String filename = "C:/Users/mag684/workspace/GuidedBuyWorkFlow/src/main/resources/diagrams/FirstSubProcess.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("firstSubProcess.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("firstSubProcess", variableMap);
		
		Object obj = runtimeService.getVariable(processInstance.getId(), "subPReturnMap");
		System.out.println("Return Map 1 ---------- "+obj);
		
		ExecutionQuery executionQuery = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).activityId("subreceivetask1");
		Execution execution	= executionQuery.singleResult();		
		runtimeService.setVariable(execution.getId(), "newparam111", "New Parameter Value1111------------------");
		runtimeService.signal(execution.getId());
		System.out.println("Process ended here "+execution.getId() + "--------- "+execution.isSuspended() + execution.getProcessInstanceId());
		
		
		
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
	}
}