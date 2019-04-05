package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.WxService;
import utils.WxUtil;

/**
 * Servlet implementation class WxServlet
 */
@WebServlet("/wx")
public class WxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
	    signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
		timestamp	时间戳
		nonce	        随机数
		echostr	        随机字符串	 
		*/
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		
		System.out.println(signature);
		System.out.println(timestamp);
		System.out.println(nonce);
		System.out.println(echostr);
		
		//验证请求来自微信
		if(WxService.check(timestamp,nonce,signature)){
			System.out.println("接入成功！");
			PrintWriter out = response.getWriter();
			//原样返回echostr参数
			out.print(echostr);
			out.flush();
			out.close();
		}
		else{
			System.out.println("接入失败！");
		}
	}
	
	/**
	 * 接收消息和事件推送的
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("post！");

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		//处理消息和事件
		String responseMessage = WxService.processRequest(request);
//		String responseMessage="<xml>\r\n" + 
//				"  <ToUserName>o8WIn57l7aU_km1WGlYo64CPB0rM</ToUserName>\r\n" + 
//				"  <FromUserName>gh_a1f14e680125</FromUserName>\r\n" + 
//				"  <CreateTime>1554281301</CreateTime>\r\n" + 
//				"  <MsgType>text</MsgType>\r\n" + 
//				"  <Content>欢迎关注我的微信公众号！</Content>\r\n" + 
//				"</xml>";
		//回复消息
		PrintWriter out = response.getWriter();
		out.print(responseMessage);
		out.flush();
		out.close();
	}
}
