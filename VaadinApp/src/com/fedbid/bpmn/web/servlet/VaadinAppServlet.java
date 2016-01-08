package com.fedbid.bpmn.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import com.fedbid.bpmn.util.DemoApplication;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;

@WebServlet(urlPatterns="/*")
public class VaadinAppServlet  extends AbstractApplicationServlet {

	@Override
	protected Application getNewApplication(HttpServletRequest request) throws ServletException {
		// TODO Auto-generated method stub
		return new DemoApplication();
	}

	@Override
	protected Class<? extends Application> getApplicationClass() throws ClassNotFoundException {
		// TODO Auto-generated method stub
		return DemoApplication.class;
	}

}
