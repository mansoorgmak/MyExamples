package com.cloud.email.notification;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class EmailBounceAndComplaintListener {
	Log log = LogFactory.getLog(EmailBounceAndComplaintListener.class);
	public void trackBounceAndComplaintEmail(String input){
		log.info(" ******************* trackBounceAndComplaintEmail ********************** "+input);
	}
}
