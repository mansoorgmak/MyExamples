package com.bpm.fedbid.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SprintContextUtil implements ApplicationContextAware{

	private static ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		// TODO Auto-generated method stub
		applicationContext = context;
	}
	
	public static ApplicationContext getContext(){
		return applicationContext;
	}

}
