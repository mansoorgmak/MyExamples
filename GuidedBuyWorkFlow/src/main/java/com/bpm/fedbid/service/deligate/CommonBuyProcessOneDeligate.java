package com.bpm.fedbid.service.deligate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class CommonBuyProcessOneDeligate implements JavaDelegate{

	@Override
	public void execute(DelegateExecution args) throws Exception {
		// TODO Auto-generated method stub
		
		args.setVariable("buyerId","12345678");
		
	}

}
