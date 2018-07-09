package com.lottery.print.service.impl;

import java.util.List;
import java.util.Map;

import com.lottery.print.model.JycMsgLog;
import com.lottery.print.service.IJycMsgLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lottery.print.dao.IJycMsgLogDAO;

@Service
public class JycMsgLogServiceImpl implements IJycMsgLogService {
	@Autowired
	public IJycMsgLogDAO tradeRecordsDAO;

	public int saveTradeRecords(JycMsgLog entity) {
		return tradeRecordsDAO.insert(entity);
	}

	public List<JycMsgLog> queryForTradeRecordsCallBackList(int endCount) {
		return tradeRecordsDAO.getCallBackList(endCount);
	}

	public JycMsgLog getTbTradeRecordsById(Long id) {
		return tradeRecordsDAO.getById(id);
	}

	public List<JycMsgLog> queryForTbTradeRecordsSendList(Map param) {
		return tradeRecordsDAO.queryForTbTradeRecordsSendList(param);
	}

	public int updateTbTradeRecordsSendState(JycMsgLog tbTradeRecords) {
		return tradeRecordsDAO.updateTbTradeRecordsSendState(tbTradeRecords);
	}

	public int updateTradeRecordsCallBack(JycMsgLog ttr) {
		return tradeRecordsDAO.updateTradeRecordsCallBack(ttr);
	}

	public int updateTbTradeRecordsLockState(JycMsgLog tbTradeRecords) {
		return tradeRecordsDAO.updateTbTradeRecordsLockState(tbTradeRecords);
	}

}
