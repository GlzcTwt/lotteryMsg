package com.lottery.print.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lottery.print.dao.IMerchantInfoDAO;
import com.lottery.print.model.TbMerchantInfo;
import com.lottery.print.service.IMerchantInfoService;

@Service
public class MerchantInfoServiceImpl implements IMerchantInfoService {
	@Autowired
	public IMerchantInfoDAO merchantInfoDAO;

	public TbMerchantInfo getMerchantInfoById(int id) {
		return merchantInfoDAO.getById(id);
	}

}
