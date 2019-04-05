package wxEntity;

public class AccessToken {
	private String accessToken;
	private long expireTime;//过期时间
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	
	public AccessToken(String accessToken, String expireIn) {
		super();
		this.accessToken = accessToken;
		this.expireTime = System.currentTimeMillis()+Integer.parseInt(expireIn)*1000;
	}
	
	/**
	 * 判断token是否过期
	 * @return
	 */
	public boolean isExpire() {
		return System.currentTimeMillis() > expireTime; 
	}
}
