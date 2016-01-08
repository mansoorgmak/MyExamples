package com.bpm.fedbid.dto;

import java.io.Serializable;

public class NavigationStepTO implements Serializable{
	private Integer sequenceId;
	private String navigationStepId;
	private String navigationStep;
	
	public NavigationStepTO(Integer sequenceId, String navigationStepId, String navigationStep){
		this.sequenceId = sequenceId;
		this.navigationStepId = navigationStepId;
		this.navigationStep = navigationStep;
	}
	
	public Integer getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}
	public String getNavigationStepId() {
		return navigationStepId;
	}
	public void setNavigationStepId(String navigationStepId) {
		this.navigationStepId = navigationStepId;
	}
	public String getNavigationStep() {
		return navigationStep;
	}
	public void setNavigationStep(String navigationStep) {
		this.navigationStep = navigationStep;
	}

	@Override
	public String toString() {
		return "NavigationStepTO [sequenceId=" + sequenceId + ", navigationStepId=" + navigationStepId
				+ ", navigationStep=" + navigationStep + "]";
	}
	
	
	
}
