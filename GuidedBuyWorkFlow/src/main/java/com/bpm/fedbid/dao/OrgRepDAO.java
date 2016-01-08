package com.bpm.fedbid.dao;

import java.util.List;

import com.bpm.fedbid.dto.OrgAttributesTO;
import com.bpm.fedbid.dto.SettingTO;

public interface OrgRepDAO {
	public List<SettingTO> getOrgSettings(Integer orgId);
	public OrgAttributesTO getOrgAttributes(Integer orgId);
}
