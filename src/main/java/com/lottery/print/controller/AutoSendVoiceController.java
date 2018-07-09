package com.lottery.print.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lottery.print.model.JycMsgLog;
import com.lottery.print.service.ISendMessageService;
import com.lottery.print.service.IJycMsgLogService;
import com.lottery.print.util.PropertiesLoader;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Administrator
 *
 */
public class AutoSendVoiceController {
	protected final Logger logger = Logger.getLogger(getClass());
	@Autowired
	private ISendMessageService sendMessageService;
	@Autowired
	private IJycMsgLogService tradeRecordsService;

	/**
	 *  1、查询需要发送的消息记录 2、根据消息类型进行分组发送 3、修改发送状态
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void SendVoice() {
		int counts = new Integer(PropertiesLoader.getProperty("query.count"));
		int runningFlag = new Integer(
				PropertiesLoader.getProperty("running.flag"));
		try {
			ExecutorService exec = Executors.newFixedThreadPool(1);

			Map map = new HashMap();
			map.put("tradeState", 1);
			map.put("messType", 3);
			map.put("queryCount", counts);
			List<JycMsgLog> list = tradeRecordsService
					.queryForTbTradeRecordsSendList(map);
			logger.info("取到发送的消息条数|" + list.size());

			for (JycMsgLog tbr : list) {
				// 修改记录表加锁
				tbr.setTradeState(2);
				tradeRecordsService.updateTbTradeRecordsLockState(tbr);
				logger.info("发送消息记录加锁成功|" + tbr.getId() + ",toName|"
						+ tbr.getMessNumber());
				exec.execute(new Done(sendMessageService, tbr));
			}

			exec.shutdown();

			Long times = System.currentTimeMillis();
			logger.info("等待子线程处理开始|" + times);

			if (runningFlag == 0) {
				// 等待所有子线程结束，才退出主线程
				while (!exec.isTerminated()) {
					if (System.currentTimeMillis() - times > 40000) {
						exec.shutdownNow();
						break;
					}
				}

			}
			Long ctime = System.currentTimeMillis() - times;
			logger.info("取到发送的消息结束,等待子线程处理结束|" + System.currentTimeMillis()
					+ ",总用|" + ctime);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class Done extends Thread {
		private ISendMessageService sendMessageService;
		private JycMsgLog tbr;

		Done(ISendMessageService sendMessageService,
				JycMsgLog tbTradeRecords) {
			this.sendMessageService = sendMessageService;
			this.tbr = tbTradeRecords;
		}

		public void run() {

			try {
				logger.info("调用发送消息开始|" + tbr.getId() + ",toName|"
						+ tbr.getMessNumber());
				boolean ret = sendMessageService.sendMessage(tbr);
				logger.info("调用发送消息结束|" + tbr.getId() + ",toName|"
						+ tbr.getMessNumber() + ",ret|" + ret);

			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				logger.info("finally调用发送消息结束|" + tbr.getId() + ",toName|"
						+ tbr.getMessNumber());
			}
		}

	}

}