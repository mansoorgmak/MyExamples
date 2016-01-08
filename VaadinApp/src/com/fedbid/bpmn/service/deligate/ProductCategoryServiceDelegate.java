package com.fedbid.bpmn.service.deligate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class ProductCategoryServiceDelegate implements JavaDelegate{

	@Override
	public void execute(DelegateExecution exec) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(" ================= categoryId in GeneralServiceDelegate == "+exec.getVariable("categoryId"));
		System.out.println(" ================= subCategoryId in GeneralServiceDelegate == "+exec.getVariable("subCategoryId"));
	}

}
