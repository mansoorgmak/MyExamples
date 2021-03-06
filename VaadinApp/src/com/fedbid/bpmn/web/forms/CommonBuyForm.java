package com.fedbid.bpmn.web.forms;

import java.util.List;

import org.activiti.engine.form.FormProperty;

import com.fedbid.bpmn.util.CommonUtil;
import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public abstract class CommonBuyForm implements UserForm{
	private String processDefinitionId;
	private String taskId;
	private Application application;
	List<FormProperty> formDataList;

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public List<FormProperty> getFormDataList() {
		return formDataList;
	}

	public void setFormDataList(List<FormProperty> formDataList) {
		this.formDataList = formDataList;
	}

	
}
