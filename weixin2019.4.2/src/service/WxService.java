package service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;

import dao.WeiUserDao;
import net.sf.json.JSONObject;
import utils.WxUtil;
import wxEntity.AccessToken;
import wxEntity.BaseMessage;
import wxEntity.TextMessage;
import wxEntity.Weiuser;

public class WxService {
	/**
	 * sha1加密
	 * 
	 * @param str
	 * @return
	 */
	private static String sha1(String str) {
		try {
			// 获取一个加密对象
			MessageDigest md = MessageDigest.getInstance("sha1");
			// 加密
			byte[] digest = md.digest(str.getBytes());
			// 处理加密结果
			char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				sb.append(chars[(b >> 4) & 15]);
				sb.append(chars[b & 15]);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 配置服务器时检验signature
	 * 
	 * @param timestamp
	 * @param nonce
	 * @param signature
	 * @return
	 */
	private static final String TOKEN = "bpa";

	public static boolean check(String timestamp, String nonce, String signature) {
		// 1. 将token、timestamp、nonce三个参数进行字典排序
		String[] strs = new String[] { TOKEN, timestamp, nonce };
		Arrays.sort(strs);
		// 2.将三个参数字符串拼接成一个字符串进行sha1加密
		String str = strs[0] + strs[1] + strs[2];
		String mysig = sha1(str);
		// 3.开发者获得加密后的字符串可与signature对比，标识该请求源于微信
		return mysig.equals(signature);
	}

	private static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String APPID = "wx8820c0d640b18647";
	private static final String APPSECRET = "2bb4d6c310395e17c47e75384ba80c43";
	// 用于存储token
	private static AccessToken at;

	/**
	 * 获取token
	 */
	private static void getToken() {
		String url = GET_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		String tokenStr = WxUtil.get(url);
		// System.out.println(tokenStr);//获取到的是一个JSON数据
		// 将JSON数据的值取出来，封装到AccessToken类中
		JSONObject jsonObject = JSONObject.fromObject(tokenStr);
		String token = jsonObject.getString("access_token");
		String expiresIn = jsonObject.getString("expires_in");
		// 创建token对象，并存起来
		at = new AccessToken(token, expiresIn);
	}

	/**
	 * 向外暴露的获取token的方法
	 * 
	 * @return
	 */
	public static String getAccessToken() {
		if (at == null || at.isExpire()) {
			getToken();
		}
		return at.getAccessToken();
	}

	/**
	 * 处理消息和事件
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String responseMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍后尝试！";

			// xml请求解析，调用工具类解析微信发送来的xml格式消息，解析的结果放在HashMap里
			Map<String, String> requestMap = WxUtil.xmlToMap(request);

			String msgType = requestMap.get("MsgType");

			// 处理事件推送的消息
			if (msgType.equals("event")) {// 接收事件推送
				responseMessage = dealScribe(requestMap);
			}
			return responseMessage;
		} catch (Exception e) {
		} finally {
			return responseMessage;
		}
	}

	/**
	 * 接收事件推送
	 * 
	 * @param requestMap
	 * @return
	 */
	public static String dealScribe(Map<String, String> requestMap) {

		System.out.println("dealScribe");
		Map<String, String> responseMap = new HashMap<String, String>();
		String responseMessage = null;
		
		// 获取事件推送的事件内容
		String eventType = requestMap.get("Event");
		
		if (eventType.equals("subscribe")) {// 关注微信公众号需要处理的事务
			System.out.println("处理关注事务！");
			//1.向数据库中插入该用户的信息
			Weiuser weiuser = new UserInfoService().insertWeiUser(requestMap.get("FromUserName"));
			
			// 2.用户关注公众号后向用户发送文本消息
			responseMessage = WxService.dealTextMessage(requestMap);
			
			new TemplateMsgService().sendTemplateMessage(weiuser);
			
		} else if (eventType.equals("unsubscribe")) {// 取消关注
		}
		return responseMessage;
	}
	
	
	/**
	 * 回复文本消息
	 * @param requestMap
	 * @return
	 */
	public static String dealTextMessage(Map<String, String> requestMap) {
		Map<String, String> responseMap = new HashMap<String,String>();
		String respContent = "欢迎关注我的微信公众号！";
		
		responseMap.put("ToUserName", requestMap.get("FromUserName"));
		responseMap.put("FromUserName", requestMap.get("ToUserName"));
		responseMap.put("CreateTime", String.valueOf(System.currentTimeMillis()/1000));
		responseMap.put("Content",respContent);
		
		TextMessage textMessage = new TextMessage(responseMap);
		
		//将Java对象转为xml
		XStream stream = new XStream(new Xpp3Driver(new NoNameCoder()));
		stream.processAnnotations(BaseMessage.class);
		stream.processAnnotations(TextMessage.class);
		
		String textMessageXml = stream.toXML(textMessage);
		System.out.println("dealTextMessage："+textMessageXml);
		return textMessageXml;
	}
}
