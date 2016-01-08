package com.bpm.fedbid.dto;

import java.io.Serializable;

public class SettingTO implements Serializable{
	private Integer orgId;
	private Integer settingId;
	private Integer valueId;
	
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getSettingId() {
		return settingId;
	}
	public void setSettingId(Integer settingId) {
		this.settingId = settingId;
	}
	public Integer getValueId() {
		return valueId;
	}
	public void setValueId(Integer valueId) {
		this.valueId = valueId;
	}
}
