package com.bpm.fedbid.service.deligate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.bpm.fedbid.common.CommonUtil;

public class BuyBasicInfoService implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		CommonUtil util = new CommonUtil();
		System.out.println("--------------- In "+this.getClass().getName());
		util.addNavigationSteps(execution, 1, "basicBuyInfo", "Basic Buy Information");
	}

}
