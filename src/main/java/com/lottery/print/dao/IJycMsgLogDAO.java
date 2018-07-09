package com.lottery.print.dao;

import java.util.List;
import java.util.Map;

import com.lottery.print.model.JycMsgLog;

public interface IJycMsgLogDAO extends BaseDAO<JycMsgLog> {

	public List<JycMsgLog> queryForTbTradeRecordsSendList(Map param);

	public int updateTbTradeRecordsSendState(JycMsgLog tbTradeRecords);

	public int updateTbTradeRecordsLockState(JycMsgLog tbTradeRecords);

	public List<JycMsgLog> getCallBackList(int endCount);

	public int updateTradeRecordsCallBack(JycMsgLog ttr);

}
