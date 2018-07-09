package com.lottery.print.service;

import java.util.List;
import java.util.Map;

import com.lottery.print.model.JycMsgLog;

public interface IJycMsgLogService {

	public int saveTradeRecords(JycMsgLog entity);

	public List<JycMsgLog> queryForTradeRecordsCallBackList(int endCount);

	public JycMsgLog getTbTradeRecordsById(Long id);

	public List<JycMsgLog> queryForTbTradeRecordsSendList(Map param);

	public int updateTbTradeRecordsSendState(JycMsgLog tbTradeRecords);

	public int updateTradeRecordsCallBack(JycMsgLog ttr);

	public int updateTbTradeRecordsLockState(JycMsgLog tbTradeRecords);

}
