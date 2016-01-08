package com.bpm.fedbid.service.deligate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class TestService implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(" ======================================= exp id"+execution.getVariable("fname")+" sal = "+execution.getVariable("lname"));
	}

}
