package com.bpm.fedbid.service.deligate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.bpm.fedbid.common.SprintContextUtil;
import com.bpm.fedbid.dao.OrgRepDAO;
import com.bpm.fedbid.dao.impl.OrgRepDAOImpl;

public class BuyFlowDeligate implements JavaDelegate{

		
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("------------------------ BuyFlowDeligate.execution started ----");
		execution.setVariable("step1", "Testing the app");
		
		System.out.println("input org id ================ "+execution.getVariable("orgId"));
		
		OrgRepDAO orgRepDAO = new OrgRepDAOImpl();
		try{
		Object obj = execution.getVariable("orgId");
		System.out.println(orgRepDAO.getOrgSettings(new Integer(obj.toString())));
		}catch(Exception exp){
			System.out.println("-----------exp---------------------");
			exp.printStackTrace();
		}
		
		/*
		OrgRepDAO orgRepDAO = (OrgRepDAO)SprintContextUtil.getContext().getBean("orgRepDAO");
		try{
		System.out.println(orgRepDAO.getOrgSettings(new Integer((String)execution.getVariable("orgId"))));
		}catch(Exception exp){
			exp.printStackTrace();
		}
		*/
	}

}
