package com.lottery.print.service.impl;

import com.lottery.print.dao.IJycSysParamDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lottery.print.service.IJycSysParamService;

/** * @author  作者 : 李永普
 * @date 创建时间：2015-11-27 下午2:20:03 
 * @version  
 * @parameter  
 * @since  
 * @return  */
@Service
public class JycSysParamServiceImpl implements IJycSysParamService {
	
	@Autowired
    private IJycSysParamDao systemParamDao;
    
	public String querySystemParamValueByName(@Param("name") String name) {
		return systemParamDao.querySystemParamValueByName(name);
	}
	
}
