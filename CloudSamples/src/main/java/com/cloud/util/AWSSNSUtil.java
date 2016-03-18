package com.fedbid.core.util;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.amazonaws.services.sns.AmazonSNSAsyncClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

public class AWSSNSUtil {
	private static final FedBidLog log = FedBidLogFactory.getInstance(AWSSNSUtil.class);
	
	private String topicArn;

	public String getTopicArn() {
		return topicArn;
	}

	public void setTopicArn(String topicArn) {
		this.topicArn = topicArn;
	}

	public void publishMessage(String message, String subject) throws FedBidSNSException{
		try{
			
			log.info(" ======= publishing message: "+message+" with subject: "+subject+" to topic ARN: "+this.getTopicArn());
			
			if(StringUtil.isNullOrBlank(this.getTopicArn())){
				throw new FedBidSNSException(" Invalid Topic ARN!");
			}
			
			if(StringUtil.isNullOrBlank(message)){
				throw new FedBidSNSException(" Invalid Message to publish!");
			}
			
			if(StringUtil.isNullOrBlank(subject)){
				throw new FedBidSNSException(" Invalid Message Subject to publish!");
			}	
			
			//SNS blocking client
			//AmazonSNSClient snsClient = new AmazonSNSClient();
			
			//SNS Non blocking client
			AmazonSNSAsyncClient snsClient = new AmazonSNSAsyncClient();			
			PublishRequest publishRequest = new PublishRequest();
			publishRequest.setMessage(message);
			publishRequest.setTopicArn(this.getTopicArn());
			publishRequest.setSubject(subject);
			
			Future<PublishResult> futureResult = snsClient.publishAsync(publishRequest);	
			
			if(futureResult.get() != null){
				log.info("Published Message Id = "+futureResult.get().getMessageId());
			}else{
				log.info("message: "+message+" with subject: "+subject+" to topic: "+this.getTopicArn()+" was not published");
			}
		}catch(ExecutionException exp){
			log.error("ExecutionException while publishing SNS ", exp);
			throw new FedBidSNSException(exp);
		}catch(InterruptedException exp){
			log.error("InterruptedException while publishing SNS ", exp);
			throw new FedBidSNSException(exp);
		}catch(FedBidSNSException exp){
			throw exp;
		}
	}
	
	
	public static void main(String k[]){		
		try{
			AWSSNSUtil util = new AWSSNSUtil();
			util.setTopicArn("arn:aws:sns:us-east-1:620642927803:calendar-event-sns-dev");
			util.publishMessage("Testing UnInterrupted message from Mansoor Gooty at Date Time "+ DateUtil.convertDateToDataTimeString(new Date()), "adfasdf345353");		
			System.out.println("============== published ============ ");
		}catch(FedBidSNSException exp){
			exp.printStackTrace();
		}
	}
	
}
