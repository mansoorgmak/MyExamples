package com.bpm.fedbid.service.deligate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.bpm.fedbid.common.CommonConstants;
import com.bpm.fedbid.common.SprintContextUtil;
import com.bpm.fedbid.dao.OrgRepDAO;
import com.bpm.fedbid.dao.impl.OrgRepDAOImpl;
import com.bpm.fedbid.dto.OrgAttributesTO;
import com.bpm.fedbid.dto.SettingTO;

public class BuyNavigationInitInfoService implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("--------------- In "+this.getClass().getName());
		//OrgRepDAO orgRepDAO = new OrgRepDAOImpl();
		OrgRepDAO orgRepDAO = (OrgRepDAO)SprintContextUtil.getContext().getBean("orgRepDAO");
		Map<String, String> buyNavigationMap = new HashMap();
		//Map<String, List<NavigationStepTO> buyNavigationMap = new HashMap();
		//buyNavigationMap.put(CommonConstants.NAVIGATION_STEPS, new ArrayList());
		buyNavigationMap.put(CommonConstants.NAVIGATION_STEPS, "");
		try{
			Object obj = execution.getVariable("orgId");
			List<SettingTO> list = orgRepDAO.getOrgSettings(new Integer(obj.toString()));
			execution.createVariableLocal(CommonConstants.ORG_SETTINGS_LIST, list);
			//EngineServices engineServices = execution.getEngineServices();		
			execution.setVariable(CommonConstants.BUY_NAVIGATION_MAP, buyNavigationMap);
			OrgAttributesTO orgAttributes = orgRepDAO.getOrgAttributes(new Integer(obj.toString()));
			System.out.println("fbo enabled ==== "+orgAttributes.isFboEnabled());
			execution.createVariableLocal(CommonConstants.ORG_ATTRIBUTES,orgAttributes);
			
		}catch(Exception exp){
			System.out.println("-----------exp---------------------");
			exp.printStackTrace();
		}
		
		
	}

}
