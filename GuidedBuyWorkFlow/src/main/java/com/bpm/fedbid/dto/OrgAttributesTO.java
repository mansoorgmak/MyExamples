package com.bpm.fedbid.dto;

import java.io.Serializable;

public class OrgAttributesTO implements Serializable{
	private boolean canAttach, fboEnabled,allowSaAttach,excludeBuyerPoc,bidDeliveryDaysTypeId,recoveryAcct,facFlag,showSelectedBidPrice, showSelectedSller,enableOptionBuys;
	private int defaultSystemScId;

	public boolean isCanAttach() {
		return canAttach;
	}

	public void setCanAttach(boolean canAttach) {
		this.canAttach = canAttach;
	}

	public boolean isFboEnabled() {
		return fboEnabled;
	}

	public void setFboEnabled(boolean fboEnabled) {
		this.fboEnabled = fboEnabled;
	}

	public boolean isAllowSaAttach() {
		return allowSaAttach;
	}

	public void setAllowSaAttach(boolean allowSaAttach) {
		this.allowSaAttach = allowSaAttach;
	}

	public boolean isExcludeBuyerPoc() {
		return excludeBuyerPoc;
	}

	public void setExcludeBuyerPoc(boolean excludeBuyerPoc) {
		this.excludeBuyerPoc = excludeBuyerPoc;
	}

	public boolean isBidDeliveryDaysTypeId() {
		return bidDeliveryDaysTypeId;
	}

	public void setBidDeliveryDaysTypeId(boolean bidDeliveryDaysTypeId) {
		this.bidDeliveryDaysTypeId = bidDeliveryDaysTypeId;
	}

	public boolean isRecoveryAcct() {
		return recoveryAcct;
	}

	public void setRecoveryAcct(boolean recoveryAcct) {
		this.recoveryAcct = recoveryAcct;
	}

	public int getDefaultSystemScId() {
		return defaultSystemScId;
	}

	public void setDefaultSystemScId(int defaultSystemScId) {
		this.defaultSystemScId = defaultSystemScId;
	}

	public boolean isFacFlag() {
		return facFlag;
	}

	public void setFacFlag(boolean facFlag) {
		this.facFlag = facFlag;
	}

	public boolean isShowSelectedBidPrice() {
		return showSelectedBidPrice;
	}

	public void setShowSelectedBidPrice(boolean showSelectedBidPrice) {
		this.showSelectedBidPrice = showSelectedBidPrice;
	}

	public boolean isShowSelectedSller() {
		return showSelectedSller;
	}

	public void setShowSelectedSller(boolean showSelectedSller) {
		this.showSelectedSller = showSelectedSller;
	}

	public boolean isEnableOptionBuys() {
		return enableOptionBuys;
	}

	public void setEnableOptionBuys(boolean enableOptionBuys) {
		this.enableOptionBuys = enableOptionBuys;
	}
}
