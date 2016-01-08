package com.fedbid.bpmn.service.deligate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class CreateBuyFBOServiceDelegate implements JavaDelegate{
	@Override
	public void execute(DelegateExecution exec) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(" ================= fbo in FBOServiceDelegate == "+exec.getVariable("fbo"));
		System.out.println(" ================= fac in FBOServiceDelegate == "+exec.getVariable("fac"));
	}
}
