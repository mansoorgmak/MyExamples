package com.cloud.email.notification;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fedbid.cloud.dao.AmazonS3ClientDAO;


public class EmailSuccessDeliveryListener {
	Log log = LogFactory.getLog(EmailSuccessDeliveryListener.class);
	
	@Autowired
	private AmazonS3ClientDAO amazonS3ClientDAO;
	
	public void trackSuccessEmail(String input){
		log.info(" ******************* trackSuccessEmail ********************** "+input);
		amazonS3ClientDAO.writeSuccessMessage(input);
		log.info("--------------------- "+amazonS3ClientDAO);
	}
}
