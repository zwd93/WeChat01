package wxEntity;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("loginName")
public class LoginName extends DataBase{
	public LoginName(Map<String, String> map) {
		super(map);
	}
}