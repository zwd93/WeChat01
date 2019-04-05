package wxEntity;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("remark")
public class Remark extends DataBase{
	public Remark(Map<String, String> map) {
		super(map);
	}
}
