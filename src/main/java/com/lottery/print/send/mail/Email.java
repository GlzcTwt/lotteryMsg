package com.lottery.print.send.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.lottery.print.util.PropertiesLoader;

/**
 * <pre>
 */
public class Email {
	private final Logger logger = Logger.getLogger(getClass());
	
	private String host;// Email主机

	private String from;// 发件人

	private String to;// 收件人

	private String username;// 发件人用户名

	private String password;// 发件人密码

	private String subject;// 标题

	private String text;// 内容

	private boolean isTrue = true;

	public boolean isTrue() {
		return isTrue;
	}

	public void setTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}

	public String getFrom() {
		return from;
	}

	/**
	 * 
	 * 设置发件人
	 * 
	 * @param from
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	public String getHost() {
		return host;
	}

	/**
	 * 
	 * 设置主机
	 * 
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * 设置密码
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getSubject() {
		return subject;
	}

	/**
	 * 
	 * 设置标题
	 * 
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	/**
	 * 
	 * 设置内容
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	public String getTo() {
		return to;
	}

	/**
	 * 
	 * 设置收件人
	 * 
	 * @param to
	 */
	public void setTo(String to) {
		this.to = to;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * 
	 * 设置用户名
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 
	 * 发送Email
	 * 
	 * @param inHost
	 *            主机
	 * @param inFrom
	 *            发件人
	 * @param inTo
	 *            收件人
	 * @param inUsername
	 *            用户名
	 * @param inPassword
	 *            密码
	 * @param inSubject
	 *            标题
	 * @param inText
	 *            内容
	 * @param inIsTrue
	 *            是否记录日志 true 是 false 否
	 */
	public static void sendEmail(String inHost, String inFrom, String inTo,
			String inUsername, String inPassword, String inSubject,
			String inText, boolean inIsTrue) {

		String host = inHost;
		String from = inFrom;
		String to = inTo;
		String username = inUsername;
		String password = inPassword;
		boolean isTrue = inIsTrue;

		Properties props = new Properties();

		// Setup mail server
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true"); // 这样才能通过验证

		// Get session
		Session session = Session.getDefaultInstance(props);

		// watch the mail commands go by to the mail server
		session.setDebug(isTrue);// 记录日志打开

		// Define message
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject(inSubject);
			message.setText(inText);
			// Send message
			message.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect(host, username, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void sendHtmlEmail(String inHost, String inFrom, String inTo,
			String inUsername, String inPassword, String inSubject,
			String inText, boolean inIsTrue) {

		String host = inHost;
		String from = inFrom;
		String to = inTo;
		String username = inUsername;
		String password = inPassword;
		boolean isTrue = inIsTrue;

		Properties props = new Properties();

		// Setup mail server
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true"); // 这样才能通过验证

		// Get session
		Session session = Session.getDefaultInstance(props);

		// watch the mail commands go by to the mail server
		session.setDebug(isTrue);// 记录日志打开

		// Define message
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject(inSubject);

			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(inText, "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// 将MiniMultipart对象设置为邮件内容
			message.setContent(mainPart);
			// Send message
			message.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect(host, username, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 发送Email方法
	 */
	public void sendHtmlEmail() {
		Properties props = new Properties();

		// Setup mail server
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true"); // 这样才能通过验证

		// Get session
		Session session = Session.getDefaultInstance(props);

		// watch the mail commands go by to the mail server
		session.setDebug(isTrue);// 记录日志打开

		// Define message
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject(subject);

			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(text, "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// 将MiniMultipart对象设置为邮件内容
			message.setContent(mainPart);

			// message.setText(text);
			// Send message
			message.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect(host, username, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @author caizhiqin
	 * 云模版发送
	 */
	public void templateSend() throws Exception{
		System.out.println("云发送");
		final String url = "http://sendcloud.sohu.com/webapi/mail.send.json";
	    HttpPost httpost = new HttpPost(url);
	    HttpClient httpclient = new DefaultHttpClient();
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("api_user", "Etongdai_test_SlhPz2"));
	    params.add(new BasicNameValuePair("api_key", "1RUPBROUshBNN2nK"));
	    params.add(new BasicNameValuePair("from", "service@sendcloud.im"));
	    params.add(new BasicNameValuePair("fromname", "etongdai"));
	    params.add(new BasicNameValuePair("to", "yangxinyu@etongdai.com"));
	    params.add(new BasicNameValuePair("subject", "来自SendCloud的第一封邮件！"));
	    params.add(new BasicNameValuePair("html", "你太棒了！你已成功的从SendCloud发送了一封测试邮件，接下来快登录前台去完善账户信息吧！"));	   
	    //params.add(new BasicNameValuePair("resp_email_id", "false"));
	    //params.add(new BasicNameValuePair("use_maillist", "false"));
	    params.add(new BasicNameValuePair("resp_email_id", "true"));
	    //params.add(new BasicNameValuePair("gzip_compress", "false"));
	    params.add(new BasicNameValuePair("template_invoke_name", "test_template"));

	    httpost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

	    HttpResponse response = httpclient.execute(httpost);
	    // response
	    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	        System.out.println(EntityUtils.toString(response.getEntity()));
	    } else {
	        System.err.println("error");
	    }
	    httpost.releaseConnection();
	}
	
	/**
	 * @author caizhiqin
	 * 云普通发送
	 */
	public void normalSend() throws Exception{
		final String url =PropertiesLoader.getProperty("sendcloud.url");// "http://sendcloud.sohu.com/webapi/mail.send.json";
		System.out.println("url====="+url);
	    HttpPost httpost = new HttpPost(url);
	    HttpClient httpclient = new DefaultHttpClient();
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("api_user", PropertiesLoader.getProperty("cloudUser")));
	    params.add(new BasicNameValuePair("api_key", PropertiesLoader.getProperty("cloudKey")));
	    params.add(new BasicNameValuePair("from", PropertiesLoader.getProperty("cloudFrom")));
	    params.add(new BasicNameValuePair("fromname", PropertiesLoader.getProperty("cloudFromName")));
	    params.add(new BasicNameValuePair("to", this.to));
	    params.add(new BasicNameValuePair("subject",this.subject));
	    params.add(new BasicNameValuePair("html",this.text));	   
	    httpost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
	    HttpResponse response = httpclient.execute(httpost);
	    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	    	System.out.println(EntityUtils.toString(response.getEntity()));
	        logger.info(EntityUtils.toString(response.getEntity()));
	    } else {
	    	logger.info("error");
	    }
	    httpost.releaseConnection();
	}

	public void sendEmail() {
		Properties props = new Properties();

		// Setup mail server
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true"); // 这样才能通过验证

		// Get session
		Session session = Session.getDefaultInstance(props);

		// watch the mail commands go by to the mail server
		session.setDebug(isTrue);// 记录日志打开

		// Define message
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject(subject);
			message.setText(text);
			// Send message
			message.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect(host, username, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
