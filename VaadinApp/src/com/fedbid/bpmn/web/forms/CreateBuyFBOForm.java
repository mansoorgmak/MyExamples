package com.fedbid.bpmn.web.forms;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.form.FormProperty;

import com.fedbid.bpmn.util.CommonUtil;
import com.vaadin.Application;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;


public class CreateBuyFBOForm extends CommonBuyForm{
	private static UserForm.FORM_TYPE formType = UserForm.FORM_TYPE.TASK_FORM;
	public static String FORM_KEY = "createBuyFBOForm";
	private OptionGroup fbo;
	private OptionGroup fac;
	
	
	@Override
	public void init() {
		HorizontalLayout viewLayout = new HorizontalLayout();
		VerticalLayout loginPanel = new VerticalLayout();
		loginPanel.setSpacing(true);
		loginPanel.setWidth("300px");

		Label header = new Label("Create Buy FBO");
		header.addStyleName(Reindeer.LABEL_H1);
		loginPanel.addComponent(header);

		fbo = new OptionGroup("FBO Flag");
		fbo.setWidth("100%");
		fbo.addItem("Yes");
		fbo.addItem("No");
		fbo.setMultiSelect(false);
		loginPanel.addComponent(fbo);

		fac = new OptionGroup("FAC Flag");
		fac.setWidth("100%");
		fac.addItem("Yes");
		fac.addItem("No");
		fac.setMultiSelect(false);
		loginPanel.addComponent(fac);

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		loginPanel.addComponent(buttons);
		loginPanel.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);	

		Button continueButton = new Button("Continue");
		continueButton.setClickShortcut(KeyCode.ENTER);
		continueButton.addStyleName(Reindeer.BUTTON_DEFAULT);
		continueButton.addListener(continueButtonListener());
		buttons.addComponent(continueButton);

		Button restartButton = new Button("Back");
		restartButton
				.setDescription("This button is only here for debugging purposes");
		restartButton.addListener(backButtonListener());
		buttons.addComponent(restartButton);

		viewLayout = new HorizontalLayout();
		viewLayout.addComponent(loginPanel);
		viewLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
		viewLayout.setSizeFull();
		viewLayout.addStyleName(Reindeer.LAYOUT_BLACK);

		fbo.focus();
		
		Window previousWindow = this.getApplication().getMainWindow();
		this.getApplication().removeWindow(previousWindow);
		
		if(this.getFormDataList() != null && this.getFormDataList().size() > 0){
			this.populateForm(this, this.getFormDataList());
		}
		
		Window mainWindow = new Window("Create Buy FBO", viewLayout);
		this.getApplication().setMainWindow(mainWindow);
	}
	
	@SuppressWarnings("serial")
	private Button.ClickListener backButtonListener() {
		final CommonBuyForm userForm = this;
		final Application application = this.getApplication();
		return new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				//String processDefId = CommonUtil.getProcessDefinitionId();
				//setProcessDefinitionId(processDefId);
				try{
					CommonUtil.submitFormData(userForm.getTaskId(), getBackFormData(userForm),  formType);
					CommonBuyForm userForm = CommonUtil.getForm("");
					if(userForm != null){
						userForm.setApplication(application);
						userForm.setProcessDefinitionId(userForm.getProcessDefinitionId());
						userForm.init();						
					}
				}catch(Exception exp){
					exp.printStackTrace();
				}
			}
		};
	}
	
	private Button.ClickListener continueButtonListener(){
		final CommonBuyForm userForm = this;
		final Application application = this.getApplication();
		return new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				//String processDefId = CommonUtil.getProcessDefinitionId();
				//setProcessDefinitionId(processDefId);
				try{
					CommonUtil.submitFormData(userForm.getTaskId(), getFormData(userForm),  formType);
					CommonBuyForm userForm = CommonUtil.getForm("");
					if(userForm != null){
						userForm.setApplication(application);
						userForm.setProcessDefinitionId(userForm.getProcessDefinitionId());
						userForm.init();
					}
				}catch(Exception exp){
					exp.printStackTrace();
				}
			}
		};
	}

	@Override
	public Map<String, String> getFormData(UserForm userForm) {
		Map<String, String> map = new HashMap();
		CreateBuyFBOForm form = (CreateBuyFBOForm)userForm;
		map.put("fbo", form.fbo.getValue()+"");
		map.put("fac", form.fac.getValue()+"");
		map.put("fworbw", "Forward");
		return map;
	}
	
	
	public Map<String, String> getBackFormData(UserForm userForm) {
		Map<String, String> map = new HashMap();
		CreateBuyFBOForm form = (CreateBuyFBOForm)userForm;
		map.put("fbo", form.fbo.getValue()+"");
		map.put("fac", form.fac.getValue()+"");
		map.put("fworbw", "Back");
		return map;
	}
	
	@Override
	public CommonBuyForm populateForm(UserForm userForm, List<FormProperty> list) {
		CreateBuyFBOForm form = (CreateBuyFBOForm)userForm;
		if(list != null){			
			for(FormProperty formProperty : list){
				if(formProperty.getId().equals("fbo")){
					if(fbo != null){
						form.fbo.setValue(CommonUtil.unNuller(formProperty.getValue()));	
					}
				}else if(formProperty.getId().equals("fac")){
					if(fac != null){
						form.fac.setValue(CommonUtil.unNuller(formProperty.getValue()));
					}
				}
			}
		}	
		
		return form;
	}
}
