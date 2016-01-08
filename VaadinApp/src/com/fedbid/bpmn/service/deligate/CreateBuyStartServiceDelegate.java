package com.fedbid.bpmn.service.deligate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class CreateBuyStartServiceDelegate implements JavaDelegate{

	@Override
	public void execute(DelegateExecution exec) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(" ================= repId in StartServiceDelegate == "+exec.getVariable("repId"));
		System.out.println(" ================= orgId in StartServiceDelegate == "+exec.getVariable("orgId"));
	}

}
