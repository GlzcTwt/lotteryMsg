package com.lottery.print.dao;

import org.apache.ibatis.annotations.Param;

import com.lottery.print.model.JycSysParam;

public interface IJycSysParamDao extends BaseDAO<JycSysParam> {
	public String querySystemParamValueByName(@Param("name")String name);
}
