package wxEntity;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("first")
public class First extends DataBase{
	public First(Map<String, String> map) {
		super(map);
	}
}