package com.fedbid.bpmn.service.deligate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class CreateBuyGeneralServiceDelegate implements JavaDelegate{

	@Override
	public void execute(DelegateExecution exec) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(" ================= buyDesc in GeneralServiceDelegate == "+exec.getVariable("buyDesc"));
		System.out.println(" ================= solNum in GeneralServiceDelegate == "+exec.getVariable("solNumber"));
	}

}