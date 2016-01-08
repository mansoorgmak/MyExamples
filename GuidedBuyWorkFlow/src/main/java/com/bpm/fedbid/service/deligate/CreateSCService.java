package com.bpm.fedbid.service.deligate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.bpm.fedbid.common.CommonConstants;
import com.bpm.fedbid.common.CommonUtil;
import com.bpm.fedbid.dto.NavigationStepTO;

public class CreateSCService   implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("--------------- In "+this.getClass().getName());
		System.out.println("Org Id in SC -------- "+execution.getVariable("orgId"));
		//Map<String, List<NavigationStepTO>> map = (Map)execution.getVariable(CommonConstants.BUY_NAVIGATION_MAP);
		Map<String, String> map = (Map)execution.getVariable(CommonConstants.BUY_NAVIGATION_MAP);
		if(map != null){
			//List<NavigationStepTO> list = map.get(CommonConstants.NAVIGATION_STEPS);
			String data  = map.get(CommonConstants.NAVIGATION_STEPS);
		}else{
			map = new HashMap();
			execution.setVariable(CommonConstants.BUY_NAVIGATION_MAP, map);
			map.put(CommonConstants.NAVIGATION_STEPS, "");
		}
		
		String data = map.get(CommonConstants.NAVIGATION_STEPS);
		System.out.println("List before sc step "+data);
		CommonUtil util = new CommonUtil();
		util.addNavigationSteps(execution, 8, "createSCInfo", "create SC Information");
		map = (Map)execution.getVariable(CommonConstants.BUY_NAVIGATION_MAP);
		data = map.get(CommonConstants.NAVIGATION_STEPS);
		System.out.println("List after SC step "+data);
	}

}
