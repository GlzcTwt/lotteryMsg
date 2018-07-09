package com.lottery.print.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.lottery.print.dao.IJycSysParamDao;
import com.lottery.print.send.mail.EmailUtil;
import com.lottery.print.service.ISendEmailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @ClassName: SendEmailServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author fanghua
 * @date 2014年3月17日 下午5:48:22
 * 
 */
@Service
public class SendEmailServiceImpl implements ISendEmailService {
	protected final Logger logger = Logger.getLogger(getClass());
	@Autowired
	IJycSysParamDao spd;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map SendEmail(String toName, String title, String content) {
		Map map = new HashMap();
		int state = 0;
		logger.info("发送邮件开始,toName|" + toName + ",title|" + title
				+ ",content|" + content);
		try {
			//sendCloud渠道开关
			String SCMail = spd.querySystemParamValueByName("sendCloud");
			//邮箱邮件渠道开关
			String EMail = spd.querySystemParamValueByName("iGENUSemail");
			if("on".equals(SCMail) && "on".equals(EMail)){//两个邮件渠道都开启
				int flag = new Random().nextInt(2);//随机选择邮件发送渠道
				if(flag == 0){//sendCloud发送邮件
					EmailUtil.sendHtmlEmail(toName, title, content,1);
					map.put("merchId", "100005");//SendCloud商业号，与tb_merchant_info表SendCloud邮件merch_Id一致
				}else{//java发送邮件
					EmailUtil.sendHtmlEmail(toName, title, content,0);
				}
			}else if("on".equals(SCMail) && !"on".equals(EMail)){//只用sendCloud发送邮件
				EmailUtil.sendHtmlEmail(toName, title, content,1);
				map.put("merchId", "100005");//SendCloud商业号，与tb_merchant_info表SendCloud邮件merch_Id一致
			}else if(!"on".equals(SCMail) && "on".equals(EMail)){//只用邮箱发送邮件
				EmailUtil.sendHtmlEmail(toName, title, content,0);
			}else{
				state = -10;
			}
		} catch (Exception e) {
			e.printStackTrace();
			state = -10;
		}
		map.put("state", state);
		map.put("extOrderId", "");
		logger.info("发送邮件结束,toName|" + toName + ",map|" + map);
		return map;
	}

}
