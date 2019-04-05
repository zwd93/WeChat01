package wxEntity;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class DataBase {
	@XStreamAlias("value")
	private String value;
	
	@XStreamAlias("color")
	private String color;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public DataBase(Map<String, String> map) {
		this.value = map.get("value");
		this.color = map.get("color");
	}	
}
