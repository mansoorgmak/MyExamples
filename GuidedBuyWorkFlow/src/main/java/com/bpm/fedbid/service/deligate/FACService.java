package com.bpm.fedbid.service.deligate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.bpm.fedbid.common.CommonUtil;

public class FACService  implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("--------------- In "+this.getClass().getName());
		CommonUtil util = new CommonUtil();
		util.addNavigationSteps(execution, 4, "facBuyInfo", "FAC Buy Information");
	}

}
