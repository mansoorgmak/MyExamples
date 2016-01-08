package com.fedbid.bpmn.service.deligate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class NaicsServiceDelegate implements JavaDelegate{

	@Override
	public void execute(DelegateExecution exec) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(" ================= naicsCatId in GeneralServiceDelegate == "+exec.getVariable("naicsCatId"));
		System.out.println(" ================= naicsSubCatId in GeneralServiceDelegate == "+exec.getVariable("naicsSubCatId"));
		System.out.println(" ================= naicsId in GeneralServiceDelegate == "+exec.getVariable("naicsId"));
	}

}