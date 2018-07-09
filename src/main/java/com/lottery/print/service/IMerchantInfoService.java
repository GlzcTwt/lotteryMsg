package com.lottery.print.service;

import com.lottery.print.model.TbMerchantInfo;
import org.springframework.stereotype.Service;

@Service
public interface IMerchantInfoService {

	public TbMerchantInfo getMerchantInfoById(int id);
	

}
