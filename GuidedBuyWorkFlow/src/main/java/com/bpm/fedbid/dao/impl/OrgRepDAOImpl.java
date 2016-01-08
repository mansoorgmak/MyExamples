package com.bpm.fedbid.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.bpm.fedbid.common.CommonUtil;
import com.bpm.fedbid.dao.OrgRepDAO;
import com.bpm.fedbid.dto.OrgAttributesTO;
import com.bpm.fedbid.dto.SettingTO;

public class OrgRepDAOImpl implements OrgRepDAO{
	private DataSource umpDataSource;

	public DataSource getUmpDataSource() {
		return umpDataSource;
	}

	public void setUmpDataSource(DataSource umpDataSource) {
		this.umpDataSource = umpDataSource;
	}
	
	private Connection getConnection() throws Exception{
		Connection conn = this.umpDataSource.getConnection();
		System.out.println("==================================== UMP Connection "+conn);
		return conn;
		/*
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@//db01.dev.fbcloud.net:1521/umpdev1","coreapp","coreapp");
		return conn;
		*/
	}
	
	private void closeConnection(Connection conn){
		try{
			conn.close();
			conn = null;
		}catch(Exception exp){
			exp.printStackTrace();
		}
	}
	
	private void closeResultSet(ResultSet rs) throws Exception{
		rs.close();
		rs = null;
	}
	
	private void closeStatement(PreparedStatement pstmt) throws Exception{
		pstmt.close();
		pstmt = null;
	}
	
	public List<SettingTO> getOrgSettings(Integer orgId){
		
		String sql = "select * from org_settings where org_id=?";
		List list = new ArrayList();
		try{
			
			System.out.println("--------------------------- org id in DAO ----------- "+orgId);
			
			Connection conn = this.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, orgId);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				SettingTO settingTO = new SettingTO();
				settingTO.setOrgId(orgId);
				settingTO.setSettingId(rs.getInt("org_settings_id"));
				settingTO.setValueId(rs.getInt("org_setting_value_id"));
				list.add(settingTO);
			}
			
			closeResultSet(rs);
			closeStatement(pstmt);			
			closeConnection(conn);
		}catch(Exception exp){
			exp.printStackTrace();
		}
		return list;
	}
	
	public OrgAttributesTO getOrgAttributes(Integer orgId){
		OrgAttributesTO orgAttributesTO = new OrgAttributesTO();
		String sql = "select can_attach, fbo_enabled,allow_sa_attach,exclude_buyer_poc,bid_delivery_days_type_id,recovery_acct,default_system_sc_id,fac_flag,show_selected_bid_price, show_selected_seller,enable_option_buys from org where org_id=?";
		try{
			
			System.out.println("--------------------------- org id in DAO ----------- "+orgId);
			
			Connection conn = this.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, orgId);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				orgAttributesTO.setAllowSaAttach(CommonUtil.getBooleanValue(rs.getInt("allow_sa_attach")));
				orgAttributesTO.setBidDeliveryDaysTypeId(CommonUtil.getBooleanValue(rs.getInt("allow_sa_attach")));
				orgAttributesTO.setCanAttach(CommonUtil.getBooleanValue(rs.getInt("allow_sa_attach")));
				orgAttributesTO.setDefaultSystemScId(rs.getInt("allow_sa_attach"));
				orgAttributesTO.setEnableOptionBuys(CommonUtil.getBooleanValue(rs.getInt("allow_sa_attach")));
				orgAttributesTO.setExcludeBuyerPoc(CommonUtil.getBooleanValue(rs.getInt("allow_sa_attach")));
				orgAttributesTO.setFacFlag(CommonUtil.getBooleanValue(rs.getInt("allow_sa_attach")));
				orgAttributesTO.setFboEnabled(CommonUtil.getBooleanValue(rs.getInt("allow_sa_attach")));
				orgAttributesTO.setRecoveryAcct(CommonUtil.getBooleanValue(rs.getInt("allow_sa_attach")));
				orgAttributesTO.setShowSelectedBidPrice(CommonUtil.getBooleanValue(rs.getInt("allow_sa_attach")));
				orgAttributesTO.setShowSelectedSller(CommonUtil.getBooleanValue(rs.getInt("allow_sa_attach")));				
			}			
			closeResultSet(rs);
			closeStatement(pstmt);			
			closeConnection(conn);
		}catch(Exception exp){
			exp.printStackTrace();
		}
		
		return orgAttributesTO;
	}
	
}
