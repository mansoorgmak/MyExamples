package com.fedbid.bpmn.web.forms;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.activiti.engine.form.FormProperty;

public interface UserForm extends Serializable{
	public static String FORM_KEY = "";
	public static enum FORM_TYPE {START_FORM, TASK_FORM};
	
	public void init();
	
	public Map<String, String> getFormData(UserForm userForm);
	
	public CommonBuyForm populateForm(UserForm userForm, List<FormProperty> list);
}
