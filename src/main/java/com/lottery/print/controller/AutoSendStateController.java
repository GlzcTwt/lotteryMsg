package com.lottery.print.controller;

import java.util.Date;
import java.util.List;

import com.lottery.print.util.DateTool;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.lottery.print.model.RetsultEntity;
import com.lottery.print.model.JycMsgLog;
import com.lottery.print.service.IJycMsgLogService;
import com.lottery.print.util.BasicRequest;
import com.lottery.print.util.FunctionUtil;
import com.lottery.print.util.PropertiesLoader;

/**
 * 
 * @ClassName: AutoSendStateController
 * @Description: TODO(消息发送结果通知接口)
 * @author fanghua
 * @date 2014年3月18日 下午2:44:20
 * 
 */
public class AutoSendStateController {
	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IJycMsgLogService tradeRecordsService;

	/**
	 * 1、取数据需要发送的数据 2、调用接口发送 3、修改调用记录
	 */
	public void execute() {

		int counts = new Integer(
				PropertiesLoader.getProperty("call.query.count"));

		List<JycMsgLog> list = tradeRecordsService
				.queryForTradeRecordsCallBackList(counts);

		logger.info("通知记录数|" + list.size());
		for (JycMsgLog ttr : list) {
			logger.info("通知交易子线程开始|" + ttr.getId());
			new Done(tradeRecordsService, ttr).start();

		}

	}

	class Done extends Thread {
		private IJycMsgLogService tradeRecordsService;
		private JycMsgLog ttr;

		Done(IJycMsgLogService tradeRecordsService,
				JycMsgLog tbTradeRecords) {
			this.tradeRecordsService = tradeRecordsService;
			this.ttr = tbTradeRecords;
		}

		public void run() {
			RetsultEntity ret = new RetsultEntity();
			try {
				ret.setRetState("0000");
				ret.setRetMess("成功");
				ret.setTradeOrderId(ttr.getId()+"");
				ret.setMessOrderId(ttr.getMerchOrderId());
				ret.setTimes(DateTool.parseDates2(new Date()));

				StringBuffer strBuffer = new StringBuffer();
				strBuffer.append(ttr.getMerchId());
				strBuffer.append(ret.getMessOrderId());
				strBuffer.append(ret.getTradeOrderId());
				strBuffer.append(ret.getRetState());
				strBuffer.append(ret.getTimes());
				strBuffer.append(ttr.getMerchKey());
				String sign = FunctionUtil.getSupEncryptionStr(strBuffer
						.toString());
				ret.setSign(sign);
				String input = JSON.toJSONString(ret);
				BasicRequest br = new BasicRequest();
				br.setUrlStr(ttr.getCallUrl());
				br.setParam("input=" + input);
				br.sendUrlPost();
				String retStr = br.getRetValue().toUpperCase();
				logger.info("通知结果|" + ttr.getId() + ",返回内容|" + retStr);
				if (retStr.equals("OK")) {
					ttr.setCallState(30);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			ttr.setCallCount(ttr.getCallCount() + 1);
			ttr.setCallTime(new Date());

			if (ttr.getCallCount() > 5) {
				ttr.setCallState(-30);
			}
			if (null == ttr.getTimeFinish()) {
				ttr.setTimeFinish(new Date());
			}

			tradeRecordsService.updateTradeRecordsCallBack(ttr);

			logger.info("通知修改记录结束|" + ttr.getId());
		}

	}
}
