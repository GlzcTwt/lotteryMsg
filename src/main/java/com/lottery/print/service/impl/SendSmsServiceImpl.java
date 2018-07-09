package com.lottery.print.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.lottery.print.send.sms.*;
import com.lottery.print.service.ISendSmsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lottery.print.service.IJycSysParamService;

/**
 * 
 * @ClassName: SendSmsServiceImpl
 * @Description: TODO(短信发送接口)
 * @author fanghua
 * @date 2014年3月17日 下午5:13:51
 * 
 */
@Service
public class SendSmsServiceImpl implements ISendSmsService {
	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private IJycSysParamService SystemParamService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map SendSms(String toName, String title, String content,
			Integer messType) {
		Map map = new HashMap();
		int state = 0;
		logger.info("发送短信消息开始,toName|" + toName + ",title|" + title
				+ ",content|" + content+",messType="+messType);
		try {
			int changzhuoSwitch = Integer.parseInt(this.SystemParamService.querySystemParamValueByName("sms_changzhuo_message_switch"));
			int changzhuoType = Integer.parseInt(this.SystemParamService.querySystemParamValueByName("sms_changzhuo_message_type"));
			int aiyinSwitch = Integer.parseInt(this.SystemParamService.querySystemParamValueByName("sms_aiying_message_switch"));
			int aiyinType = Integer.parseInt(this.SystemParamService.querySystemParamValueByName("sms_aiying_message_type"));
			if(1==messType){
				logger.info("发送验证码");
				if((changzhuoSwitch==1&&(changzhuoType==1||changzhuoType==0)) &&
						(aiyinSwitch==1&&(aiyinType==1||aiyinType==0))){//畅卓和爱音都应用
					logger.info("随机选一个");
					int flag = new Random().nextInt(2);//随机选择畅卓和爱音
					if(flag == 0){//畅卓短信发送
						SmsSendChangZhuoClient changz = SmsSendChangZhuoClient.getInstance();
						map = changz.sendMessage(toName, content);
						map.put("merchId", "100001");//畅卓短信商业号，与tb_merchant_info表畅卓短信merch_Id一致
					}else{//爱音短信发送
						SmsSendAiYinClient aiyin = SmsSendAiYinClient.getInstance();
						map = aiyin.sendMessage(toName, content);
						map.put("merchId", "100002");//爱音短信商业号，与tb_merchant_info表爱音短信merch_Id一致
					}
				}else if((changzhuoSwitch==1&&(changzhuoType==1||changzhuoType==0)) &&
						((aiyinSwitch==1&&aiyinType==2)||aiyinSwitch==0)){//仅用畅卓短信
					logger.info("仅用畅卓短信");

					SmsSendChangZhuoClient changz = SmsSendChangZhuoClient.getInstance();
					map = changz.sendMessage(toName, content);
					map.put("merchId", "100001");//畅卓短信商业号，与tb_merchant_info表畅卓短信merch_Id一致
				}else if((aiyinSwitch==1&&(aiyinType==1||aiyinType==0)) &&
						((changzhuoSwitch==1&&changzhuoType==2)||changzhuoSwitch==0)){//仅用爱音短信
					logger.info("仅用爱音短信");

					SmsSendAiYinClient aiyin = SmsSendAiYinClient.getInstance();
					map = aiyin.sendMessage(toName, content);
					map.put("merchId", "100002");//爱音短信商业号，与tb_merchant_info表爱音短信merch_Id一致
				}else{
					map.put("state", -10);
				}

			}else if (2==messType){
				logger.info("发送营销短信");
				if((changzhuoSwitch==1&&(changzhuoType==2||changzhuoType==0)) &&
						(aiyinSwitch==1&&(aiyinType==2||aiyinType==0))){//畅卓和爱音都应用
					logger.info("随机选一个");
					int flag = new Random().nextInt(2);//随机选择畅卓和爱音
					if(flag == 0){//畅卓短信发送
						SmsSendChangZhuoClient changz = SmsSendChangZhuoClient.getInstance();
						map = changz.sendMessage(toName, content);
						map.put("merchId", "100001");//畅卓短信商业号，与tb_merchant_info表畅卓短信merch_Id一致
					}else{//爱音短信发送
						SmsSendAiYinClient aiyin = SmsSendAiYinClient.getInstance();
						map = aiyin.sendMessage(toName, content);
						map.put("merchId", "100002");//爱音短信商业号，与tb_merchant_info表爱音短信merch_Id一致
					}
				}else if((changzhuoSwitch==1&&(changzhuoType==2||changzhuoType==0)) &&
						((aiyinSwitch==1&&aiyinType==1)||aiyinSwitch==0)){//仅用畅卓短信
					logger.info("仅用畅卓短信");

					SmsSendChangZhuoClient changz = SmsSendChangZhuoClient.getInstance();
					map = changz.sendMessage(toName, content);
					map.put("merchId", "100001");//畅卓短信商业号，与tb_merchant_info表畅卓短信merch_Id一致
				}else if((aiyinSwitch==1&&(aiyinType==2||aiyinType==0)) &&
						((changzhuoSwitch==1&&changzhuoType==1)||changzhuoSwitch==0)){//仅用爱音短信
					logger.info("仅用爱音短信");

					SmsSendAiYinClient aiyin = SmsSendAiYinClient.getInstance();
					map = aiyin.sendMessage(toName, content);
					map.put("merchId", "100002");//爱音短信商业号，与tb_merchant_info表爱音短信merch_Id一致
				}else{
					map.put("state", -10);
				}
			}
			
			/*百悟和亿美先屏蔽*/
//			if(baiwuSwitch==1 && emaySwitch==1){//百悟和亿美都应用
//				int flag = new Random().nextInt(2);//随机选择畅卓短信和亿美短信
//				if(flag == 0){//百悟短信发送
//					SmsSendBaiwClient baiw = SmsSendBaiwClient.getInstance();
//					baiw.setSMS_SIGN("【" + merchName + "】");
//					map = baiw.sendMessage(toName, content);
//					map.put("merchId", "100002");//畅卓短信商业号，与tb_merchant_info表百悟短信merch_Id一致
//				}else{//亿美短信发送
//					SmsSendEmayClient emay = SmsSendEmayClient.getInstance();
//					map = emay.sendMessage(toName, content);
//					map.put("merchId", "100004");//亿美短信商业号，与tb_merchant_info表亿美短信merch_Id一致
//				}
//			}else if(baiwuSwitch==1 && emaySwitch==0){//仅用百悟短信
//				SmsSendBaiwClient baiw = SmsSendBaiwClient.getInstance();
//				baiw.setSMS_SIGN("【" + merchName + "】");
//				map = baiw.sendMessage(toName, content);
//				map.put("merchId", "100002");//百悟短信商业号，与tb_merchant_info表百悟短信merch_Id一致
//			}else if(baiwuSwitch==0 && emaySwitch==1){//仅用亿美短信
//				SmsSendEmayClient emay = SmsSendEmayClient.getInstance();
//				map = emay.sendMessage(toName, content);
//				map.put("merchId", "100004");//亿美短信商业号，与tb_merchant_info表亿美短信merch_Id一致
//			}else{
//				map.put("state", -10);
//			}
		} catch (Exception e) {
			e.printStackTrace();
			state = -10;
			map.put("state", state);
		}
		logger.info("发送短信消息结束,toName|" + toName + ",map|" + map);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map SendVoice(String toName, String title, String content,
			String merchName) {
		Map map = new HashMap();
		int state = 0;
		logger.info("发送语音消息开始,toName|" + toName + ",title|" + title
				+ ",content|" + content);
		try {
			SmsVoiceBaiWuClient baiwVoice = SmsVoiceBaiWuClient.getInstance();
			map = baiwVoice.sendMessage(toName, content);
			map.put("merchId", "100003");//畅卓语音商业号，与tb_merchant_info表畅卓语音merch_Id一致
		} catch (Exception e) {
			e.printStackTrace();
			state = -10;
			map.put("state", state);
		}
		logger.info("发送语音消息结束,toName|" + toName + ",map|" + map);
		return map;
	}
}
