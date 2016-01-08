package org.activiti.designer.test;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestGuidedBuyFlowForOrg {

	private String filename = "C:/Users/mag684/workspace/GuidedBuyWorkFlow/src/main/resources/diagrams/GuidedBuyFlowForOrg.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("guidedBuyFlowForOrg.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		variableMap.put("orgId", "11328");
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("guidedBuyFlowForOrg", variableMap);
		
		
		Object obj = runtimeService.getVariable(processInstance.getId(), "returnMap");
				
		System.out.println("Return Map 1 ---------- "+obj);
		
		System.out.println("processInstance.getId() == "+processInstance.getId());
		
		ExecutionQuery executionQuery = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).activityId("receivetask1");
		Execution execution	= executionQuery.singleResult();		
		runtimeService.setVariable(execution.getId(), "newparam1", "New Parameter Value1------------------");
		runtimeService.signal(execution.getId());
		System.out.println("Process ended here "+execution.getId() + "--------- "+execution.isSuspended() + execution.getProcessInstanceId());
		
		System.out.println("processInstance.getId() == "+processInstance.getId());
		
		obj = runtimeService.getVariable(processInstance.getId(), "returnMap");		
		System.out.println("Return Map 2---------- "+obj);
		
		execution = runtimeService.createExecutionQuery()
				  .processInstanceId(processInstance.getId())
				  .activityId("receivetask2")
				  .singleResult();
				runtimeService.signal(execution.getId());
	}
}