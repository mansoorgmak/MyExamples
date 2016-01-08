package com.fedbid.bpmn.web.forms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.fedbid.bpmn.util.CommonUtil;
import com.vaadin.Application;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class CreateBuyStartForm extends CommonBuyForm{
	public static String FORM_KEY = "createBuyStartForm";
	private static UserForm.FORM_TYPE formType = UserForm.FORM_TYPE.START_FORM;
	
	private TextField repId;
	private TextField orgId;
	
	public void init(){
		
		HorizontalLayout viewLayout = new HorizontalLayout();
		VerticalLayout loginPanel = new VerticalLayout();
		loginPanel.setSpacing(true);
		loginPanel.setWidth("300px");

		Label header = new Label("Create Buy Start");
		header.addStyleName(Reindeer.LABEL_H1);
		loginPanel.addComponent(header);

		repId = new TextField("Rep Id");
		repId.setWidth("100%");
		loginPanel.addComponent(repId);

		orgId = new TextField("Org Id");
		orgId.setWidth("100%");
		loginPanel.addComponent(orgId);

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
		restartButton.addListener(createRestartButtonListener());
		//buttons.addComponent(restartButton);

		viewLayout = new HorizontalLayout();
		viewLayout.addComponent(loginPanel);
		viewLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
		viewLayout.setSizeFull();
		viewLayout.addStyleName(Reindeer.LAYOUT_BLACK);

		repId.focus();
				
		Window mainWindow = new Window("Create Buy Starts", viewLayout);
		this.getApplication().setMainWindow(mainWindow);
	}
	
		
	@SuppressWarnings("serial")
	private Button.ClickListener createRestartButtonListener() {
		return new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println("===================================================== button createRestartButtonListener");
				//viewLayout.getApplication().close();
			}
		};
	}
	
	private Button.ClickListener continueButtonListener(){
		final UserForm userForm = this;
		final Application application = this.getApplication();
		return new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				String processDefId = CommonUtil.getProcessDefinitionId();
				setProcessDefinitionId(processDefId);
				try{
					CommonUtil.submitFormData(processDefId, getFormData(userForm),  formType);
					CommonBuyForm userForm = CommonUtil.getForm("");
					if(userForm != null){
						userForm.setApplication(application);
						userForm.setProcessDefinitionId(processDefId);
						userForm.init();
					}
				}catch(Exception exp){
					exp.printStackTrace();
				}
			}
		};
	}
	
	
	public Map<String, String> getFormData(UserForm userForm){
		Map<String, String> map = new HashMap();
		CreateBuyStartForm startForm = (CreateBuyStartForm)userForm;
		map.put("repId", startForm.repId.getValue()+"");
		map.put("orgId", startForm.orgId.getValue()+"");
		return map;
	}
	
	public CommonBuyForm populateForm(UserForm userForm, List<FormProperty> list){
		CreateBuyStartForm startForm = (CreateBuyStartForm)userForm;
		if(list != null){			
			for(FormProperty formProperty : list){
				if(formProperty.getId().equals("repId")){
					startForm.repId.setValue(CommonUtil.unNuller(formProperty.getValue()));					
				}else if(formProperty.getId().equals("orgId")){
					startForm.orgId.setValue(CommonUtil.unNuller(formProperty.getValue()));
				}
			}
		}
		
		return startForm;
	}

	
}
