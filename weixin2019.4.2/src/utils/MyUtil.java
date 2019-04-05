package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

public class MyUtil {

	/**
	 * 将对像转为json数据
	 */
	@Test
	public void objToJson() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "zwd");
		map.put("age", "20");
		JSONObject jsonObject = JSONObject.fromObject(map);
		System.out.println(jsonObject.toString());
	}

	/**
	 * 将json转为对象
	 */
	@Test
	public void jsonToObj() {
		String objectStr = "{\"name\":\"JSON\",\"age\":\"24\",\"address\":\"北京市西城区\"}";

		// 使用JSONObject：这种转换只能是json中的键在object都有对应的元素才可以
		JSONObject jsonObject = JSONObject.fromObject(objectStr);
		Student stu = (Student) JSONObject.toBean(jsonObject, Student.class);

		System.out.println("stu:" + stu);
	}

	/**
	 * 获取json中键的值
	 */
	/**
	 * {
	 * "code":100,
	 * "data":{
	 *          "grdbl":100.0,
	 *          "bxl":646,
	 *          "fwl":0,
	 *          "mytsl":0
	 *        }
	 * }
	 */
	@Test
	public void jsonData() {
		String a="{\"code\":100,\"data\":{\"grdbl\":100.0,\"bxl\":646,\"fwl\":0,\"mytsl\":0}}";
		 
        JSONObject object=JSONObject.fromObject(a);

        System.out.println(object.get("code"));
        System.out.println(object.getJSONObject("data").get("grdbl"));
	    System.out.println(object.getJSONObject("data").get("bxl"));
	    System.out.println(object.getJSONObject("data").get("fwl"));
	    System.out.println(object.getJSONObject("data").get("mytsl"));
	}
	
	/**
	 * 将对象转为XML
	 */
	@Test
	public void objToXML() {
		XStream stream = new XStream(new Xpp3Driver(new NoNameCoder()));
		Student student = new Student();
		student.setName("zwd");
		student.setAge("20");
		student.setAddress("Wuhan");
		
		//标识@XStreamAlias("Student")..注解
		stream.processAnnotations(Student.class);
		String xml = stream.toXML(student);
		System.out.println(xml);
	}
	
	
	/**
	 * 解析XML数据，将键值对放到Map集合中
	 * @throws DocumentException 
	 */
	@Test
	public void xmlToObj() throws DocumentException {
		//将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String,String>();
		//创建解析器
		SAXReader reader = new SAXReader();
		
		//读取数据
		Document document = reader.read("src//utils//xmlData.xml");
	  
		/*//读取输入流
		Document document = reader.read(inputStream);
	   //从request中取得输入流
	    InputStream inputStream = request.getInputStream();*/
	
		//得到XML元素的根元素
		Element root = document.getRootElement();
		//得到根元素的所有节点
		List<Element> elementsList = root.elements();
		
		for (Element e : elementsList) {
			map.put(e.getName(), e.getStringValue());
		}
		
		System.out.println(map);
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


