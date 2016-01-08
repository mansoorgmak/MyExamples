package com.bpm.fedbid.common;

public enum BuyFlowSteps {
	BUY_BASIC_INFO(1, "buy_BasicInfo", "Buy Basic Information Page"),
	BUY_FBO_INFO(2, "buy_FBOInfo", "Buy fbo Information Page"),
	BUY_CONTRACT_INFO(3, "buy_ContractInfo", "Buy contract info page");
	
	
	private int sequence;
	private String stepId;
	private String stepName;
		
	BuyFlowSteps(int sequence, String stepId, String stepName){
		this.sequence = sequence;
		this.stepId = stepId;
		this.stepName = stepName;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

}
