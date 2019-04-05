package wxEntity;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class TextMessage extends BaseMessage{

	@XStreamAlias("Content")
	private String content;
	
	public String getContext() {
		return content;
	}

	public void setContext(String content) {
		this.content = content;
	}

	public TextMessage(Map<String, String> requestMap) {
		super(requestMap);
		this.content = requestMap.get("Content");
		this.setMsgType("text");
	}
}
