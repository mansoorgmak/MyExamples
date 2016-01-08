package com.fedbid.bpmn.util;


import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;

import com.fedbid.bpmn.web.forms.CreateBuyStartForm;
import com.vaadin.Application;
import com.vaadin.ui.Window;

public class DemoApplication extends Application{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		setTheme("VaadinActivitiDemo");
		createAndShowLoginWindow();
	}
	
	private void createAndShowLoginWindow() {
		//loginView = new LoginViewImpl();
		//loginView.addListener(this);
		
		CreateBuyStartForm startForm = new CreateBuyStartForm();
		startForm.setApplication(this);
		startForm.init();
		//Window mainWindow = new Window("Create Buy", startForm.init());
		//setMainWindow(mainWindow);
	}
	
		
	public void showLoginFailed() {
		//viewLayout.getWindow().showNotification("Login failed. Please try again.",Notification.TYPE_HUMANIZED_MESSAGE);
	}

	
	public void clearForm() {
		/*
		username.setValue("");
		password.setValue("");
		username.focus();
		*/
	}
	
	public void attemptLogin(String username, String password) {
		/*
		if (getIdentityService().checkPassword(username, password)) {
			getIdentityService().setAuthenticatedUserId(username);
			fireViewEvent(new UserLoggedInEvent(getView(), username));
		} else {
			getView().clearForm();
			getView().showLoginFailed();
		}
		*/
		
		RepositoryService service = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
				
	}
}
