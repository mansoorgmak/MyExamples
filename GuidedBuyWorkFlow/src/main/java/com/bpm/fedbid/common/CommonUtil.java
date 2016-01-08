package com.bpm.fedbid.common;

import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;

import com.bpm.fedbid.dto.NavigationStepTO;

public class CommonUtil {
	
	public static boolean getBooleanValue(int value){
		if(value > 0){
			return true;
		}
		
		return false;
	}
	
	public void addNavigationSteps(DelegateExecution execution, Integer seqId, String navigationStepId, String navigationStepDesc){
		//Map<String, List<NavigationStepTO>> map = (Map)execution.getVariable(CommonConstants.BUY_NAVIGATION_MAP);
		Map<String, String> map = (Map)execution.getVariable(CommonConstants.BUY_NAVIGATION_MAP);
		//List<NavigationStepTO> list = map.get(CommonConstants.NAVIGATION_STEPS);
		String data = map.get(CommonConstants.NAVIGATION_STEPS);
		NavigationStepTO navigationStepTO = new NavigationStepTO(seqId, navigationStepId, navigationStepDesc);		
		//list.add(navigationStepTO);
		data = data + navigationStepTO.toString();
		
		map.put(CommonConstants.NAVIGATION_STEPS, data);
	}
	
}
