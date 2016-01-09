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
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;

public class CreateBuyGeneralForm extends CommonBuyForm{
	
	private static UserForm.FORM_TYPE formType = UserForm.FORM_TYPE.TASK_FORM;
	public static String FORM_KEY = "createBuyGeneralForm";
	private TextField buyDesc;
	private TextField solNumber;
	
	
	@Override
	public void init() {
		HorizontalLayout viewLayout = new HorizontalLayout();
		VerticalLayout loginPanel = new VerticalLayout();
		loginPanel.setSpacing(true);
		loginPanel.setWidth("300px");

		Label header = new Label("Create Buy General");
		header.addStyleName(Reindeer.LABEL_H1);
		loginPanel.addComponent(header);

		buyDesc = new TextField("Buy Description");
		buyDesc.setWidth("100%");
		loginPanel.addComponent(buyDesc);

		solNumber = new TextField("Solicitation Number");
		solNumber.setWidth("100%");
		loginPanel.addComponent(solNumber);

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

		buyDesc.focus();
								
		Window previousWindow = this.getApplication().getMainWindow();
		this.getApplication().removeWindow(previousWindow);
		
		if(this.getFormDataList() != null && this.getFormDataList().size() > 0){
			this.populateForm(this, this.getFormDataList());
		}
		
		Window mainWindow = new Window("Create Buy General", viewLayout);		
		this.getApplication().addWindow(mainWindow);//setMainWindow(mainWindow);
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
					userForm.setApplication(application);
					userForm.setProcessDefinitionId(userForm.getProcessDefinitionId());
					userForm.init();
				}catch(Exception exp){
					exp.printStackTrace();
				}
			}
		};
	}
	
	
	@Override
	public Map<String, String> getFormData(UserForm userForm) {
		Map<String, String> map = new HashMap();
		CreateBuyGeneralForm form = (CreateBuyGeneralForm)userForm;
		map.put("buyDesc", form.buyDesc.getValue()+"");
		map.put("solNumber", form.solNumber.getValue()+"");
		return map;
	}
	
	@Override
	public CommonBuyForm populateForm(UserForm userForm, List<FormProperty> list) {
		CreateBuyGeneralForm form = (CreateBuyGeneralForm)userForm;
		if(list != null){			
			for(FormProperty formProperty : list){
				if(formProperty.getId().equals("buyDesc")){
					if(buyDesc != null){
						form.buyDesc.setValue(CommonUtil.unNuller(formProperty.getValue()));	
					}else{
						buyDesc = new TextField("Buy Description");
						form.buyDesc.setValue(CommonUtil.unNuller(formProperty.getValue()));	
					}
				}else if(formProperty.getId().equals("solNumber")){
					if(solNumber != null){
						form.solNumber.setValue(CommonUtil.unNuller(formProperty.getValue()));
					}
				}
			}
		}		
		
		return form;
	}
		
}
