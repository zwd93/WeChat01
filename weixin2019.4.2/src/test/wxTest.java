package test;

import java.util.List;

import org.junit.Test;

import dao.WeiUserDao;
import service.UserInfoService;
import service.WxService;
import utils.DBUtil;
import wxEntity.Weiuser;

public class wxTest {

	@Test
	public void test() {
		Weiuser weiuser = new Weiuser();
		weiuser.setOpenid("23");
		weiuser.setSex(1);
		//String sql = "insert into weiuser(openid,sex,city) values(?,?,?);";
		WeiUserDao weiUserDao = new WeiUserDao();
		Weiuser weiusers = weiUserDao.selectWeiUserByOpenID(weiuser.getOpenid());
		if( weiusers==null) {
			System.out.println("插入");
		}
		else {
			System.out.println("更新");
		}
		System.out.println("hah ");
		//weiUserDao.updateWeiUser(weiuser);
//		String string = "{\"subscribe\":1,\"openid\":\"o8WIn57l7aU_km1WGlYo64CPB0rM\",\"nickname\":\"女子甚好\",\"sex\":0,\"language\":\"zh_CN\",\"city\":\"\",\"province\":\"\",\"country\":\"\",\"headimgurl\":\"http:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/Q3auHgzwzM7qaGZEuxhEjicYDVgCVp8l4mtCB9d69cRot1EMPrjVT6jSxyBLRjn72ib21nM1SalLtW8s2amcjcVHRghtb2hGJ6RpvNA0JXkeE\\/132\",\"subscribe_time\":1554279407,\"remark\":\"\",\"groupid\":0,\"tagid_list\":[],\"subscribe_scene\":\"ADD_SCENE_QR_CODE\",\"qr_scene\":0,\"qr_scene_str\":\"\"}\r\n" + 
//				"";
//		Weiuser weiuser = UserInfoService.jsonToBean(string);
//		System.out.println(weiuser);
	}
}
