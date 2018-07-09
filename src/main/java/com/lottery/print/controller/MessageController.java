package com.lottery.print.controller;

import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import com.lottery.print.model.TbMerchantInfo;
import com.lottery.print.model.JycMsgLog;
import com.lottery.print.service.IMerchantInfoService;
import com.lottery.print.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.lottery.print.model.RequestEntity;
import com.lottery.print.model.RetsultEntity;
import com.lottery.print.service.IJycMsgLogService;
import com.lottery.print.util.FunctionUtil;
import com.lottery.print.util.SendJson;

/**
 * 
 * @创建时间 2014年3月4日 下午2:05:45
 * @author fanghua
 * 
 */
@Controller
@RequestMapping("/")
public class MessageController {
	protected final Logger logger = Logger.getLogger(getClass());
	@Autowired
	private IMerchantInfoService merchantInfoService;
	@Autowired
	private IJycMsgLogService tradeRecordsService;
	private JycMsgLog entity;

	/**
	 * 1、验证商户 2、验证签名 3、记录短信表
	 * 
	 * @param
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("swMess")
	public String index(@RequestParam(value = "input") String inputStr,
			HttpServletResponse response) throws Exception {
		RetsultEntity retEntity = new RetsultEntity();
		// String inputStr = new String(request.getParameter("input").getBytes(
		// "iso-8859-1"), "utf-8");
		try {
			logger.info("inputStr|" + inputStr);
			RequestEntity reqEntity = JSON.parseObject(inputStr,
					RequestEntity.class);

			// 验证发送号码和内容是否正常
			if (null == reqEntity.getMessNum()
					|| "".equals(reqEntity.getMessNum())
					|| null == reqEntity.getMessContent()
					|| "".equals(reqEntity.getMessContent())) {
				logger.info("发送号码或内容为空num|" + reqEntity.getMessNum()
						+ ",content|" + reqEntity.getMessContent());
				retEntity.setRetState("9991");
				retEntity.setRetMess("参数格式错误");
				SendJson.sendJson(response, retEntity);
				return null;
			}
			logger.info("messContent|" + reqEntity.getMessContent());
			String messContent = URLDecoder.decode(reqEntity.getMessContent(),
					"utf-8");
			reqEntity.setMessContent(messContent);
			logger.info("messContent|" + messContent);

			boolean isTrue = true;
			if (reqEntity.getMessType() > 0 && reqEntity.getMessType() == 1) {
				// 验证电话号码是否正确
				isTrue = StringUtil.isMobile(reqEntity.getMessNum());

			} else if (reqEntity.getMessType() > 0
					&& reqEntity.getMessType() == 2) {
				// 验证邮箱是否正确
				isTrue = StringUtil.isEmail(reqEntity.getMessNum());

			} else {
				logger.info("发送消息类型不对type|" + reqEntity.getMessType());
				retEntity.setRetState("9991");
				retEntity.setRetMess("参数格式错误");
				SendJson.sendJson(response, retEntity);
				return null;
			}
			if (!isTrue) {
				logger.info("发送号码格式不对num|" + reqEntity.getMessNum());
				retEntity.setRetState("9991");
				retEntity.setRetMess("参数格式错误");
				SendJson.sendJson(response, retEntity);
				return null;
			}
			TbMerchantInfo merchantInfo = merchantInfoService
					.getMerchantInfoById(Integer.parseInt(reqEntity
							.getMerchId()));
			//没有查到商户
			if (merchantInfo == null) {
				logger.info("发送号码格式不对num|" + reqEntity.getMessNum());
				retEntity.setRetState("9999");
				retEntity.setRetMess("商户不存在");
				SendJson.sendJson(response, retEntity);
				return null;
			}
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append(reqEntity.getMerchId());
			strBuffer.append(reqEntity.getMessType());
			strBuffer.append(reqEntity.getMessNum());
			strBuffer.append(reqEntity.getMessOrderId());
			strBuffer.append(reqEntity.getTimes());
			strBuffer.append(merchantInfo.getMerchKey());
			String sign = FunctionUtil
					.getSupEncryptionStr(strBuffer.toString());
			// 验证签名
			if (!sign.equalsIgnoreCase(reqEntity.getSign())) {
				logger.info("签名验证错误sign|" + sign);
				retEntity.setRetState("9999");
				retEntity.setRetMess("签名验证错误");
				SendJson.sendJson(response, retEntity);
				return null;
			}

			// 记录到短信消息表中,以后改用MQ消息对列
			entity = new JycMsgLog();
			entity.setMerchId(merchantInfo.getMerchId());
			entity.setMessType(reqEntity.getMessType());
			entity.setMessNumber(reqEntity.getMessNum());
			entity.setMessContent(reqEntity.getMessContent());
			entity.setTimeInsert(new Date());
			entity.setCallUrl(merchantInfo.getMerchCallUrl());
			entity.setCallTime(null);
			entity.setCallCount(0);
			entity.setMessTitle(reqEntity.getMessTitle());
			entity.setTradeState(1);
			entity.setCallState(10);
			entity.setMerchOrderId(reqEntity.getMessOrderId());
			int count = tradeRecordsService.saveTradeRecords(entity);
			if (count > 0) {
				retEntity.setRetState("0000");
				retEntity.setRetMess("接收成功");
				retEntity.setTradeOrderId(entity.getId()+"");
				retEntity.setTimes(reqEntity.getTimes());
				SendJson.sendJson(response, retEntity);
			} else {
				retEntity.setRetState("9999");
				retEntity.setRetMess("系统异常");
				SendJson.sendJson(response, retEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("参数格式异常");
			retEntity.setRetState("9999");
			retEntity.setRetMess("参数格式错误");
			SendJson.sendJson(response, retEntity);
		}
		return null;
	}

	public JycMsgLog getEntity() {
		return entity;
	}

	public void setEntity(JycMsgLog entity) {
		this.entity = entity;
	}

	public static void main(String[] args) {
		RequestEntity req = new RequestEntity();
		req.setMerchId("100001");
		req.setMessContent("请快回来吃饭!");
		req.setMessNum("13512345678");
		req.setMessType(1);
		req.setTimes("20140311121212");
		req.setSign("1233");
		req.setMessOrderId("20140311321");
		System.out.println(JSON.toJSON(req));
	}

}
