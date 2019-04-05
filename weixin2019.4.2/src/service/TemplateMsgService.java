package service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;

import net.sf.json.JSONObject;
import utils.WxUtil;
import wxEntity.Data;
import wxEntity.First;
import wxEntity.LoginCity;
import wxEntity.LoginName;
import wxEntity.LoginTime;
import wxEntity.Remark;
import wxEntity.TemlpateMessage;
import wxEntity.TextMessage;
import wxEntity.Weiuser;



public class TemplateMsgService {

	public void sendTemplateMessage(Weiuser weiuser){
		try {
			//加载模板
			InputStreamReader is = new InputStreamReader(getClass().getResourceAsStream("/template.properties"),"UTF-8");
			
			Properties pro = new Properties();
			pro.load(is);
			//获取AccessToken
			String at = WxService.getAccessToken();
			//从模板中获取post的参数url
			String url = pro.getProperty("post_url");
			url = url.replace("ACCESS_TOKEN", at);
			
			//first
			Map<String, String> map_first = new HashMap<>();
			map_first.put("value", pro.getProperty("first_value"));
			map_first.put("color", pro.getProperty("first_color"));
			First first = new First(map_first);
			
			//loginName
			Map<String, String> map_loginName = new HashMap<>();
			map_loginName.put("value", weiuser.getNickname());
			map_loginName.put("color", pro.getProperty("loginName_color"));
			LoginName loginName = new LoginName(map_loginName);
			
			//loginCity
			Map<String, String> map_loginCity = new HashMap<>();
			map_loginCity.put("value", weiuser.getCity());
			map_loginCity.put("color", pro.getProperty("loginCity_color"));
			LoginCity loginCity = new LoginCity(map_loginCity);
			
			//loginTime
			Map<String, String> map_loginTime = new HashMap<>();
			SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
			map_loginTime.put("value",dateFormat.format(new Date()));
			map_loginTime.put("color", pro.getProperty("loginTime_color"));
			LoginTime loginTime = new LoginTime(map_loginTime);
			
			//remark
			Map<String, String> map_remark = new HashMap<>();
			map_remark.put("value", pro.getProperty("remark_value"));
			map_remark.put("color", pro.getProperty("remark_color"));
			Remark remark = new Remark(map_remark);
			
			Data data = new Data(first, loginName, loginCity, loginTime, remark);
			
			//touser
			String touser = weiuser.getOpenid();

			// template_id
			String template_id = pro.getProperty("template_id");

			// 点击后跳转的链接
			String click_url = pro.getProperty("click_url");
			
			TemlpateMessage temlpateMessage = new TemlpateMessage(touser, template_id, click_url, data);
			
			//直接将对象转为json
			JSONObject jsonObject = JSONObject.fromObject(temlpateMessage);
			System.out.println(jsonObject.toString());
			
			
			XStream stream = new XStream(new Xpp3Driver(new NoNameCoder()));
			// 处理需要@XStream注解的类
			stream.processAnnotations(TemlpateMessage.class);
			stream.processAnnotations(Data.class);
			stream.processAnnotations(First.class);
			stream.processAnnotations(LoginName.class);
			stream.processAnnotations(LoginCity.class);
			stream.processAnnotations(LoginTime.class);
			stream.processAnnotations(Remark.class);
			//将object转为xml
			String xml = stream.toXML(temlpateMessage);
			//将xml转为json
			String dataMsg = WxUtil.xmlToJSON(xml);
			
			WxUtil.post(url, dataMsg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
