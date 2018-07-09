package com.lottery.print.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.apache.log4j.Logger;

/**
 * 例子: BasicRequest br=new BasicRequest(); br.setUrlStr(UrlStr);
 * br.setParam(Param); br.setCookie(cookies); br.sendUrlPost();
 * 
 * @author Administrator
 * @version 0.3
 */
public class BasicRequest {
	protected final Logger logger = Logger.getLogger(getClass());
	private String retValue;

	private int connectTimeout = 5000;

	private int readTimeout = 10000;

	private String retCookie;

	private String coding = "UTF-8";

	private String urlStr;// 请求URL

	private String param;// 请求参数

	private String referer;// 请求来源

	private String cookie;// 请求cookie

	private int result;// 请求时返回的状态值

	private InputStream inputStream;

	private StringBuffer retBuffer;

	private static SSLContext sc;
	static {
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
					new java.security.SecureRandom());

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 用GET方式请求URL
	 * 
	 * @param UrlStr
	 *            请求的URL
	 * @param cookie
	 * @return 请求成功反回true,否则false
	 */
	public boolean sendUrlGet(String UrlStr, String cookie) {
		this.urlStr = UrlStr;
		this.cookie = cookie;

		return sendUrlGet();
	}

	/**
	 * 用GET方式请求URL
	 * 
	 * @param UrlStr
	 *            请求的URL
	 * @param cookie
	 * @param coding
	 *            返回编码格式
	 * @return 请求成功反回true,否则false
	 */
	public boolean sendUrlGet(String UrlStr, String cookie, String coding) {
		this.urlStr = UrlStr;
		this.cookie = cookie;
		this.coding = coding;
		return sendUrlGet();
	}

	public boolean sendUrlGet() {
		try {
			String str = "";
			if (param != null) {

				if (urlStr.contains("?")) {
					param = "&" + param;
				} else {
					param = "?" + param;
				}
			} else {
				param = "";
			}
			URL url = new URL(urlStr + param);
			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);

			logger.info("厂商回调url:" + url);
			((HttpURLConnection) connection).setRequestMethod("GET");
			if (cookie != null) {
				connection.setRequestProperty("Cookie", cookie);
			}
			if (referer != null) {
				connection.setRequestProperty("Referer", referer);
			}
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			retCookie = httpConnection.getHeaderField("Set-Cookie");
			if (retCookie != null && retCookie.indexOf("; Path=") != -1) {
				retCookie = retCookie
						.substring(0, retCookie.indexOf("; Path="));
			}
			logger.info("发送请求地址与参数：" + urlStr + param);
			result = httpConnection.getResponseCode();
			logger.info("发送请求地址与参数返回结果：" + result);
			if (result == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						connection.getInputStream(), coding));

				char[] cbuf = new char[31 * 1024];
				int len = 0;

				while ((len = in.read(cbuf, 0, 1024 * 31)) != -1) {
					str += new String(cbuf, 0, len);
				}
				in.close();
				if (str != null)
					str = str.trim();// 去空格
				retValue = str;
				return true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean sendHttpsGet() {
		try {
			String str = "";
			if (param != null)
				param = "?" + param;
			else
				param = "";
			// SSLContext sc= SSLContext.getInstance("SSL");
			// sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new
			// java.security.SecureRandom());

			URL url = new URL(urlStr + param);
			// URLConnection connection = url.openConnection();
			HttpsURLConnection connection = (HttpsURLConnection) url
					.openConnection();
			connection.setSSLSocketFactory(sc.getSocketFactory());
			connection.setHostnameVerifier(new TrustAnyHostnameVerifier());
			// URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);

			((HttpURLConnection) connection).setRequestMethod("GET");
			if (cookie != null) {
				connection.setRequestProperty("Cookie", cookie);
			}
			if (referer != null) {
				connection.setRequestProperty("Referer", referer);
			}
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			retCookie = httpConnection.getHeaderField("Set-Cookie");
			if (retCookie != null && retCookie.indexOf("; Path=") != -1) {
				retCookie = retCookie
						.substring(0, retCookie.indexOf("; Path="));
			}
			result = httpConnection.getResponseCode();
			if (result == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						connection.getInputStream(), coding));

				char[] cbuf = new char[31 * 1024];
				int len = 0;

				while ((len = in.read(cbuf, 0, 1024 * 31)) != -1) {
					str += new String(cbuf, 0, len);
				}
				in.close();
				if (str != null)
					str = str.trim();// 去空格
				retValue = str;
				return true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 用POST方式请求URL
	 * 
	 * @param UrlStr
	 *            请求的URL
	 * @param Param
	 *            请求参数
	 * @param cookie
	 * @return 请求成功反回true,否则false
	 */
	public boolean sendUrlPost(String UrlStr, String Param, String cookie) {
		this.urlStr = UrlStr;
		this.param = Param;
		this.cookie = cookie;
		return sendUrlPost();
	}

	/**
	 * 用POST方式请求URL
	 * 
	 * @param UrlStr
	 *            请求的URL
	 * @param Param
	 *            请求参数
	 * @param cookie
	 * @param coding
	 *            返回编码格式
	 * @return 请求成功反回true,否则false
	 */
	public boolean sendUrlPost(String UrlStr, String Param, String cookie,
			String coding) {
		this.urlStr = UrlStr;
		this.param = Param;
		this.cookie = cookie;
		this.coding = coding;
		return sendUrlPost();
	}

	public boolean sendUrlPost() {
		if (urlStr == null || param == null)
			return false;
		try {
			String str = "";
			URL url = new URL(urlStr);
			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);
			((HttpURLConnection) connection).setRequestMethod("POST");
			if (cookie != null) {
				connection.setRequestProperty("Cookie", cookie);
			}
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length",
					String.valueOf(param.length()));
			if (referer != null) {
				connection.setRequestProperty("Referer", referer);
			}
			connection.connect();
			DataOutputStream dos = new DataOutputStream(
					connection.getOutputStream());
			dos.write((param).getBytes());
			dos.flush();
			dos.close();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			retCookie = httpConnection.getHeaderField("Set-Cookie");
			if (retCookie != null && retCookie.indexOf("; Path=") != -1) {
				retCookie = retCookie
						.substring(0, retCookie.indexOf("; Path="));
			}
			result = httpConnection.getResponseCode();
			// System.out.println("result="+result);

			if (result == 200 || result == 301 || result == 302) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						connection.getInputStream(), coding));
				char[] cbuf = new char[31 * 1024];
				int len = 0;

				while ((len = in.read(cbuf, 0, 1024 * 31)) != -1) {
					str += new String(cbuf, 0, len);
				}
				in.close();
				if (str != null)
					str = str.trim();// 去空格
				retValue = str;
				return true;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean sendHttpsPost() {
		if (urlStr == null || param == null)
			return false;
		try {
			// SSLContext sc= SSLContext.getInstance("SSL");
			// sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new
			// java.security.SecureRandom());
			String str = "";
			URL url = new URL(urlStr);
			// URLConnection connection = url.openConnection();
			HttpsURLConnection connection = (HttpsURLConnection) url
					.openConnection();
			connection.setSSLSocketFactory(sc.getSocketFactory());
			connection.setHostnameVerifier(new TrustAnyHostnameVerifier());
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);
			((HttpURLConnection) connection).setRequestMethod("POST");
			if (cookie != null) {
				connection.setRequestProperty("Cookie", cookie);
			}
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length",
					String.valueOf(param.length()));
			if (referer != null) {
				connection.setRequestProperty("Referer", referer);
			}
			connection.connect();
			DataOutputStream dos = new DataOutputStream(
					connection.getOutputStream());
			dos.write((param).getBytes());
			dos.flush();
			dos.close();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			retCookie = httpConnection.getHeaderField("Set-Cookie");
			if (retCookie != null && retCookie.indexOf("; Path=") != -1) {
				retCookie = retCookie
						.substring(0, retCookie.indexOf("; Path="));
			}
			result = httpConnection.getResponseCode();
			// System.out.println("result="+result);
			// BufferedReader ins = new BufferedReader(new
			// InputStreamReader(connection
			// .getInputStream(), "GB2312"));
			// String c;
			// while ((c = ins.readLine()) != null) {
			// System.out.println(c);
			// }
			if (result == 200 || result == 301 || result == 302) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						connection.getInputStream(), coding));
				char[] cbuf = new char[31 * 1024];
				int len = 0;

				while ((len = in.read(cbuf, 0, 1024 * 31)) != -1) {
					str += new String(cbuf, 0, len);
				}
				in.close();
				if (str != null)
					str = str.trim();// 去空格
				retValue = str;
				return true;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			// } catch (NoSuchAlgorithmException e) {
			// e.printStackTrace();
			// } catch (KeyManagementException e) {
			// e.printStackTrace();
		}
		return false;
	}

	public String strReplace(String s, String s1, String s2) {
		int i = 0;
		if ((i = s.indexOf(s1, i)) >= 0) {
			int j = s1.length();
			int k = s2.length();
			StringBuffer stringbuffer = new StringBuffer();
			stringbuffer.append(s.substring(0, i)).append(s2);
			i += j;
			int l;
			for (l = i; (i = s.indexOf(s1, i)) > 0; l = i) {
				stringbuffer.append(s.substring(l, i)).append(s2);
				i += j;
			}

			stringbuffer.append(s.substring(l));
			return stringbuffer.toString();
		} else {
			return s;
		}
	}

	public String getRetValue(String coding) {
		if (retValue != null) {
			try {
				retValue = new String(retValue.getBytes(this.coding), coding);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return retValue;
	}

	/**
	 * 返回页面的值
	 * 
	 * @return
	 */
	public String getRetValue() {
		return retValue;
	}

	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}

	public String subValue(String start, String end) {
		if (retValue != null && retValue.contains(start)) {
			String temp = null;
			int n = retValue.indexOf(start);
			int m = retValue.indexOf(end, n + start.length());
			if (m != -1) {
				temp = retValue.substring(n + start.length(), m);
			}
			return temp;
		} else {
			return null;
		}
	}

	/**
	 * 查找字符串在返回页面中出现的位子
	 * 
	 * @param res
	 * @return
	 */
	public int retIndexOf(String res) {
		if (retValue != null) {
			return retValue.indexOf(res);
		} else {
			return -1;
		}
	}

	/**
	 * 字符串在返回页面中是否有出现
	 * 
	 * @param sub
	 * @return
	 */
	public boolean retContains(String sub) {
		if (retValue != null) {
			return retValue.contains(sub);
		} else {
			return false;
		}
	}

	public String getRetCookie() {
		return retCookie;
	}

	public void setRetCookie(String retCookie) {
		this.retCookie = retCookie;
	}

	public String getCoding() {
		return coding;
	}

	public void setCoding(String coding) {
		this.coding = coding;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public String getUrlStr() {
		return urlStr;
	}

	/**
	 * 设置请求URL
	 * 
	 * @param urlStr
	 */
	public void setUrlStr(String urlStr) {
		this.urlStr = urlStr;
	}

	/**
	 * 请求参数
	 * 
	 * @return
	 */
	public String getParam() {
		return param;
	}

	/**
	 * 设置请求参数
	 * 
	 * @return
	 */
	public void setParam(String param) {
		this.param = param;
	}

	public String getReferer() {
		return referer;
	}

	/**
	 * 设置请求来源
	 * 
	 * @param referer
	 */
	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getCookie() {
		return cookie;
	}

	/**
	 * 设置请求cookie
	 * 
	 * @param cookie
	 */
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void closeInputStream() {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class TrustAnyTrustManager implements X509TrustManager {

	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[] {};
	}
}

class TrustAnyHostnameVerifier implements HostnameVerifier {
	public boolean verify(String hostname, SSLSession session) {
		return true;
	}
}