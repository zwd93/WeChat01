package wxEntity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("data")
public class Data {

	private First first;
	private LoginName loginName;
	private LoginCity loginCity;
	private LoginTime loginTime;
	private Remark remark;
	public First getFirst() {
		return first;
	}
	public void setFirst(First first) {
		this.first = first;
	}
	public LoginName getLoginName() {
		return loginName;
	}
	public void setLoginName(LoginName loginName) {
		this.loginName = loginName;
	}
	public LoginCity getLoginCity() {
		return loginCity;
	}
	public void setLoginCity(LoginCity loginCity) {
		this.loginCity = loginCity;
	}
	public LoginTime getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(LoginTime loginTime) {
		this.loginTime = loginTime;
	}
	public Remark getRemark() {
		return remark;
	}
	public void setRemark(Remark remark) {
		this.remark = remark;
	}
	public Data(First first, LoginName loginName, LoginCity loginCity, LoginTime loginTime, Remark remark) {
		super();
		this.first = first;
		this.loginName = loginName;
		this.loginCity = loginCity;
		this.loginTime = loginTime;
		this.remark = remark;
	}
	
	
	
}
