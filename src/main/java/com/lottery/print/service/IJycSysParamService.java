package com.lottery.print.service;

import org.apache.ibatis.annotations.Param;

/** * @author  作者 : 李永普
 * @date 创建时间：2015-11-27 下午2:19:06 
 * @version  
 * @parameter  
 * @since  
 * @return  */
public interface IJycSysParamService {
   public String querySystemParamValueByName(@Param("name")String name);
}
