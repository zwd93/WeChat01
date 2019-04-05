package dao;

import java.util.List;

import utils.DBUtil;
import wxEntity.Weiuser;

public class WeiUserDao {

	//插入微信用户
	public boolean insertWeiUser(Weiuser weiuser) {
		String sql = "insert into weiuser(openid,nickname,sex,city,country,province) values(?,?,?,?,?,?);";
		return DBUtil.exeUpdate(sql,weiuser.getOpenid(),weiuser.getNickname(),weiuser.getSex(),weiuser.getCity(),weiuser.getCountry(),weiuser.getProvince());
	}
	//根据openid查找用户
	public Weiuser selectWeiUserByOpenID(String openid) {
		String sql = "select * from weiuser where openid=?";
		return DBUtil.queryOne(Weiuser.class, sql, openid);
	}
	//更新微信用户
	public boolean updateWeiUser(Weiuser weiuser) {
		String sql = "update weiuser set nickname=?,sex=?,city=?,country=?,province=? where openid=?";
		return DBUtil.exeUpdate(sql, weiuser.getNickname(),weiuser.getSex(),weiuser.getCity(),
				       weiuser.getCountry(),weiuser.getProvince(),weiuser.getOpenid());
	}
	
}
