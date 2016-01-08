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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;

public class ProductCategoryForm extends CommonBuyForm{
	
	private static UserForm.FORM_TYPE formType = UserForm.FORM_TYPE.TASK_FORM;
	public static String FORM_KEY = "productCategoryForm";
	private TextField categoryId;
	private TextField subCategoryId;
	
	
	@Override
	public void init() {
		HorizontalLayout viewLayout = new HorizontalLayout();
		VerticalLayout loginPanel = new VerticalLayout();
		loginPanel.setSpacing(true);
		loginPanel.setWidth("300px");

		Label header = new Label("Create Buy Product Category");
		header.addStyleName(Reindeer.LABEL_H1);
		loginPanel.addComponent(header);

		categoryId = new TextField("Product Category");
		categoryId.setWidth("100%");
		loginPanel.addComponent(categoryId);

		subCategoryId = new TextField("Product Sub Category");
		subCategoryId.setWidth("100%");
		loginPanel.addComponent(subCategoryId);

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

		categoryId.focus();
								
		Window previousWindow = this.getApplication().getMainWindow();
		this.getApplication().removeWindow(previousWindow);
		Window mainWindow = new Window("Create Buy General", viewLayout);		
		this.getApplication().addWindow(mainWindow);//setMainWindow(mainWindow);
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
					}else{
							userForm = new ConfirmationForm();
							userForm.setApplication(application);
							userForm.init();						
					}
				}catch(Exception exp){
					exp.printStackTrace();
				}
			}
		};
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
	
	public Map<String, String> getBackFormData(UserForm userForm) {
		Map<String, String> map = new HashMap();
		ProductCategoryForm form = (ProductCategoryForm)userForm;
		map.put("categoryId", form.categoryId.getValue()+"");
		map.put("subCategoryId", form.subCategoryId.getValue()+"");
		map.put("pfworbw", "Back");
		return map;
	}
	
	@Override
	public Map<String, String> getFormData(UserForm userForm) {
		Map<String, String> map = new HashMap();
		ProductCategoryForm form = (ProductCategoryForm)userForm;
		map.put("categoryId", form.categoryId.getValue()+"");
		map.put("subCategoryId", form.subCategoryId.getValue()+"");
		map.put("pfworbw", "Forward");
		return map;
	}
	@Override
	public CommonBuyForm populateForm(UserForm userForm, List<FormProperty> list) {
		ProductCategoryForm form = (ProductCategoryForm)userForm;
		if(list != null){			
			for(FormProperty formProperty : list){
				if(formProperty.getId().equals("categoryId")){
					if(categoryId != null){
					form.categoryId.setValue(CommonUtil.unNuller(formProperty.getValue()));	
					}
				}else if(formProperty.getId().equals("subCategoryId")){
					if(subCategoryId != null){
						form.subCategoryId.setValue(CommonUtil.unNuller(formProperty.getValue()));
					}
				}
			}
		}		
		
		return form;
	}
		
}
