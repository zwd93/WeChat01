package service;

import java.util.List;

import dao.WeiUserDao;
import net.sf.json.JSONObject;
import utils.WxUtil;
import wxEntity.Weiuser;

public class UserInfoService {

	public Weiuser insertWeiUser(String openid) {
		//获取用户基本信息
		String userInfoJsonstr = this.getWeiUserInfo(openid);
		//将json转为Weiuser对象
		Weiuser weiuser = this.jsonToBean(userInfoJsonstr);
		//将该用户插入数据库
		if(this.insert(weiuser)) {
			System.out.println("插入成功！");
		}
		else {
			System.out.println("插入失败！");
		}
		return weiuser;
	}
	
	
	/**
	 * 获取用户基本信息:通过openid获取用户的基本信息
	 * @param openid
	 * @return json字符串
	 */
	private final static String GET_USERINFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	public  String getWeiUserInfo(String openid) {
		String url = GET_USERINFO_URL.replace("ACCESS_TOKEN", WxService.getAccessToken()).replace("OPENID", openid);
		String jsonstr = WxUtil.get(url);
		System.out.println("getWeiUserInfo" + jsonstr);
		return jsonstr;
	}
	
	/**
	 * 将json字符串转为Weiuser对象
	 * @param jsonStr
	 * @return
	 */
	public  Weiuser jsonToBean(String jsonStr) {
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		Weiuser weiuser =  (Weiuser) JSONObject.toBean(jsonObject, Weiuser.class);
		System.out.println("jsonToBean:" + weiuser);
		return weiuser;
	}
	
	/**
	 * 将Weiuser对象插入到数据库中
	 * @param weiuser
	 * @return
	 */
	public  boolean insert(Weiuser weiuser) {
		//判断该用户是否已经插入:已存在该对象则更新，否则插入
		WeiUserDao weiUserDao = new WeiUserDao();
		Weiuser weiusers = weiUserDao.selectWeiUserByOpenID(weiuser.getOpenid());
		if(weiusers == null) {
			return weiUserDao.insertWeiUser(weiuser);
		}
		else {
			return weiUserDao.updateWeiUser(weiuser);
		}
	}
}
