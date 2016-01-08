package com.bpm.fedbid.service.deligate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class FourthServiceDelegate implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		execution.setVariable("testReturnParam4", "Testing the app4");
		System.out.println("new param value ================== "+execution.getVariable("newparam1"));
		execution.setVariable("newlyAddedVariable", execution.getVariable("newparam1"));
	}
	
}
