package com.fedbid.bpmn.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.fedbid.bpmn.web.forms.CommonBuyForm;
import com.fedbid.bpmn.web.forms.CreateBuyFBOForm;
import com.fedbid.bpmn.web.forms.CreateBuyGeneralForm;
import com.fedbid.bpmn.web.forms.CreateBuyStartForm;
import com.fedbid.bpmn.web.forms.NaicsForm;
import com.fedbid.bpmn.web.forms.ProductCategoryForm;
import com.fedbid.bpmn.web.forms.UserForm;

public class CommonUtil {
	public static String PROCESS_DEFINITION_ID = "PROCESS_DEFINITION_ID";
	public static String PROCESS_INSTANCE_ID = "PROCESS_INSTANCE_ID";
	public static String EXECUTION_ID = "EXECUTION_ID";
	public static String BUSINESS_KEY = "BUSINESS_KEY";
	public static String ACTIVITI_ID = "ACTIVITI_ID";
	public static String TENANT_ID = "TENANT_ID";
	public static String DEPLOYMENT_ID = "DEPLOYMENT_ID";
	public static String PROCESS_VERSION_ID = "PROCESS_VERSION_ID";
	public static String PROCESS_DEFINITION_KEY = "PROCESS_DEFINITION_KEY";
	
	private static Map<String, Class> map = new HashMap<String, Class>();
	private static Map<String, String> executionMap = new HashMap();
	
	static { 
		map.put(CreateBuyStartForm.FORM_KEY, CreateBuyStartForm.class);
		map.put(CreateBuyGeneralForm.FORM_KEY, CreateBuyGeneralForm.class);
		map.put(CreateBuyFBOForm.FORM_KEY, CreateBuyFBOForm.class);
		map.put(ProductCategoryForm.FORM_KEY, ProductCategoryForm.class);
		map.put(NaicsForm.FORM_KEY, NaicsForm.class);
	}
	
	public static Map<String, String> getExecutionMap() {
		return executionMap;
	}
	
	public static CommonBuyForm getUserForm(String formKey) throws Exception{
		return (CommonBuyForm)map.get(formKey).newInstance();
	}
	
	public static ProcessEngine getProcessEngine(){		
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		return processEngine;
	}
	
	public static String getProcessDefinitionId(){
		ProcessDefinition processDefinition = getProcessEngine().getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("createBuyProcess").singleResult();
		getExecutionMap().put(PROCESS_DEFINITION_ID, processDefinition.getId());
		getExecutionMap().put(DEPLOYMENT_ID, processDefinition.getDeploymentId());
		getExecutionMap().put(PROCESS_VERSION_ID, processDefinition.getVersion()+"");
		getExecutionMap().put(PROCESS_DEFINITION_KEY, processDefinition.getKey());
		return processDefinition.getId();
	}
	
	public static void submitFormData(String definitionId, Map<String, String> data, UserForm.FORM_TYPE formType) throws Exception{		
		FormService formService = getProcessEngine().getFormService();			
		if(formType.equals(UserForm.FORM_TYPE.START_FORM)){
			ProcessInstance processInstance = formService.submitStartFormData(definitionId, data);			
			getExecutionMap().put(PROCESS_INSTANCE_ID, processInstance.getProcessInstanceId());
			getExecutionMap().put(EXECUTION_ID, processInstance.getId());
			getExecutionMap().put(BUSINESS_KEY, processInstance.getBusinessKey());	
			String pdId = processInstance.getProcessDefinitionId();
		}else if(formType.equals(UserForm.FORM_TYPE.TASK_FORM)){
			formService.submitTaskFormData(definitionId, data);
		}else{
			throw new Exception("Error: Invalid Form Type !");
		}
		
	}
	
	public static CommonBuyForm getForm(String processDefinitionId) throws Exception{	
		ProcessEngine processEngine = getProcessEngine();
		FormService formService = getProcessEngine().getFormService();
		Map<String, String> processVariables = 	getExecutionMap();
		
		List<Task> tempList = getProcessEngine().getTaskService().createTaskQuery().list();
		
		List<Task> list = getProcessEngine().getTaskService()
				.createTaskQuery()
				.processDefinitionId(processVariables.get(PROCESS_DEFINITION_ID))
				.executionId(processVariables.get(EXECUTION_ID))
				.processInstanceId(processVariables.get(PROCESS_INSTANCE_ID)).list();
		
		processDefinitionId = (processDefinitionId != null) ? processDefinitionId.trim() : "";
		
		if("".equals(processDefinitionId)){		
			if(list != null && list.size() > 0){
				for(Task task : list){								
					try{
						TaskFormData taskFormData = formService.getTaskFormData(task.getId());
						String formKey = taskFormData.getFormKey();		
						CommonBuyForm userForm = getUserForm(formKey);
						userForm.setTaskId(task.getId());
						userForm.setFormDataList(taskFormData.getFormProperties());
						return userForm.populateForm(userForm, taskFormData.getFormProperties());
					}catch(Exception exp){
						exp.printStackTrace();
					}			
				}
			}
		}else{
			StartFormData startFormData = formService.getStartFormData(processDefinitionId);
			String formKey = startFormData.getFormKey();
			CommonBuyForm userForm = getUserForm(formKey);
			userForm.setProcessDefinitionId(processDefinitionId);
			return userForm.populateForm(userForm, startFormData.getFormProperties());
		}
		
		return null;
	}
	
	public static String unNuller(String param){
		param = (param != null) ? param.trim() : "";
		return param;
	}
}
