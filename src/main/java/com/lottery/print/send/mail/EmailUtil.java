/**
 * 
 */
package com.lottery.print.send.mail;

import org.apache.log4j.Logger;

import com.lottery.print.util.PropertiesLoader;

public class EmailUtil {

	protected final Logger logger = Logger.getLogger(getClass());

	/**
	 * 发送邮件工具类
	 * 
	 * @param toName
	 *            发件人
	 * @param title
	 *            标题
	 * @param body
	 *            内容
	 */
	public static void sendEmail(String toName, String title, String body) {
		Email email = new Email();
		// email.setHost("1876.cn"); // Email主机
		// email.setFrom("wangou<mail@txkm.com>");// 发件人
		// email.setUsername("mail@txkm.com");// 发件人用户名
		// email.setPassword("666666");// 发件人密码
		email.setHost(PropertiesLoader.getProperty("email.host")); // Email主机
		email.setFrom(PropertiesLoader.getProperty("email.from"));
		email.setUsername(PropertiesLoader.getProperty("email.username"));
		email.setPassword(PropertiesLoader.getProperty("email.password"));
		email.setTo(toName);// 收件人
		email.setSubject(title);// 标题
		email.setText(body);//  内容
		email.sendEmail();
	}

	/**
	 * 邮件发送真正使用的方法
	 * @param toName
	 * @param title
	 * @param body
	 */
	public static void sendHtmlEmail(String toName, String title, String body,int status) {
		Email email = new Email();
		if(status==1){//用云
			try {
				email.setTo(toName);// 收件人
				email.setSubject(title);// 标题
				email.setText(body);// 内容
				email.normalSend();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return;
		}
		email.setHost(PropertiesLoader.getProperty("email.host")); // Email主机
		email.setFrom(PropertiesLoader.getProperty("email.from"));
		email.setUsername(PropertiesLoader.getProperty("email.username"));
		email.setPassword(PropertiesLoader.getProperty("email.password"));
		email.setTo(toName);// 收件人
		email.setSubject(title);// 标题
		email.setText(body);// 内容
		email.sendHtmlEmail();
	}
}
