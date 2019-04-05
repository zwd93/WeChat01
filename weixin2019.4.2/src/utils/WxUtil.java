package utils;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;




public class WxUtil {
	/**
	 * 向指定的地址发送get请求
	 * 
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		try {
			URL urlObj = new URL(url);
			// 开链接
			URLConnection connection = urlObj.openConnection();
			InputStream is = connection.getInputStream();
			byte[] b = new byte[1024];
			int len;
			StringBuilder sb = new StringBuilder();
			while ((len = is.read(b)) != -1) {
				sb.append(new String(b, 0, len));
			}
			return sb.toString();

		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 向指定的地址发送一个post请求，带着json数据
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	public static String post(String url, String data) {
		try {
			URL urlObj = new URL(url);
			URLConnection connection = urlObj.openConnection();
			// 要发送数据出去，必须设置为可发送数据状态
			connection.setDoOutput(true);

			OutputStream os = connection.getOutputStream();
			// 写出数据
			os.write(data.getBytes(Charset.forName("utf-8")));
			os.close();
			// 获取输入流
			InputStream is = connection.getInputStream();
			byte[] b = new byte[1024];
			int len;
			StringBuilder sb = new StringBuilder();
			while ((len = is.read(b)) != -1) {
				sb.append(new String(b, 0, len));
			}
			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 解析XML数据包，将键值对放入Map集合中
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request) throws Exception{
		//将解析结果存储在HashMap中
		Map<String,String> map = new HashMap<String,String>();
		
		//从request中取得输入流
		InputStream inputStream = request.getInputStream();
		//读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		
		//得到XML元素根元素
		Element root = document.getRootElement();
		//得到根元素的所有子节点
		List<Element> elementsList = root.elements();
		
		//遍历所有子节点
		for (Element e : elementsList) {
			map.put(e.getName(), e.getStringValue());
		}
		
		//释放资源
		inputStream.close();
		inputStream = null;
		
		return map;
	}
	
	/**
	 * 将xml数据转为JSON数据字符串
	 * 
	 * @param xml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static String xmlToJSON(String xml) {
		XMLSerializer xmlSerializer = new XMLSerializer();
		// 将xml转为json（注：如果是元素的属性，会在json里的key前加一个@标识）
		JSON jsonObject = xmlSerializer.read(xml);
		return jsonObject.toString();
	}
}
