package com.fedbid.bpmn.web.forms;

import java.util.List;
import java.util.Map;

import org.activiti.engine.form.FormProperty;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class ConfirmationForm extends CommonBuyForm{

	@Override
	public void init() {
		HorizontalLayout viewLayout = new HorizontalLayout();
		VerticalLayout loginPanel = new VerticalLayout();
		loginPanel.setSpacing(true);
		loginPanel.setWidth("300px");

		Label header = new Label("That's All For Today !!!");
		header.addStyleName(Reindeer.LABEL_H1);
		loginPanel.addComponent(header);

		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		loginPanel.addComponent(buttons);
		loginPanel.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);	

		
		viewLayout = new HorizontalLayout();
		viewLayout.addComponent(loginPanel);
		viewLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
		viewLayout.setSizeFull();
		viewLayout.addStyleName(Reindeer.LAYOUT_BLACK);

										
		Window previousWindow = this.getApplication().getMainWindow();
		this.getApplication().removeWindow(previousWindow);
		Window mainWindow = new Window("Confirmation", viewLayout);		
		this.getApplication().addWindow(mainWindow);//setMainWindow(mainWindow);
	}

	@Override
	public Map<String, String> getFormData(UserForm userForm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonBuyForm populateForm(UserForm userForm, List<FormProperty> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
