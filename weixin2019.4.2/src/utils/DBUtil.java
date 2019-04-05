package utils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.alibaba.druid.pool.DruidDataSource;


public class DBUtil {
	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	//引入druid连接池jar
	private static  DruidDataSource dataSource;

	static {
		try {
			InputStream is = DBUtil.class.getResourceAsStream("/jdbc.properties");
		    Properties pro = new Properties();
		    pro.load(is);
		    
		    //MySQL数据库
		    driver =pro.getProperty("driver"); 
		    url =pro.getProperty("url"); 
		    user =pro.getProperty("user"); 
		    password =pro.getProperty("password"); 
		    
		   /* //Oracle数据库
			String driver = pro.getProperty("oracleDriver");
			String url = pro.getProperty("oracleUrl");
			String user = pro.getProperty("oracleUser");
			String password = pro.getProperty("oraclePassword");*/
		    
		    //创建数据源
		    dataSource = new DruidDataSource();
		    dataSource.setDriverClassName(driver);
		    dataSource.setUrl(url);
		    dataSource.setUsername(user);
		    dataSource.setPassword(password);
		    
		    dataSource.setMaxActive(20);
		    dataSource.setInitialSize(0);
		    dataSource.setMaxWait(6000);
		    dataSource.setMinIdle(1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取数据库连接对象：连接数据库
	 * @return
	 */
	public static Connection getConn() {
		try {
			return dataSource.getConnection();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 封装通用的更新操作：对所有的添加、删除、修改使用该方法同一实现
	 * @param sql：insert、update、delete语句
	 * @param params：动态数组
	 * @return
	 */
	public static boolean exeUpdate(String sql,Object ...params) {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConn();
			ps = conn.prepareStatement(sql);
			
			for(int i =0 ;i<params.length;i++) {
				ps.setObject(i+1, params[i]);
			}
			int result = ps.executeUpdate();
			if(result > 0) return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(ps != null) {ps.close();}
				if(conn != null) {conn.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		return false;
	}
	
	/**
	 * 封装多条查询结果
	 * @param t
	 * @param sql
	 * @param params
	 * @return
	 */
	public static <T> List<T> queryList(Class<T> t, String sql, Object... params) {
		List<T> list = new ArrayList<T>();//1.6以上可以写：List<T> list = new ArrayList<>();
		T obj = null;
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConn();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}
			rs = ps.executeQuery();
			// 获取插叙结果集中的元数据(获取列类型，数量以及长度等信息)
			ResultSetMetaData rsmd = rs.getMetaData();
			// 声明一个map集合，用于临时存储查询到的一条数据（key：列名；value：列值）
			Map<String, Object> map = new HashMap<String, Object>();
			// 遍历结果集
			while (rs.next()) {
				// 防止缓存上一条数据
				map.clear();
				// 遍历所有的列
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					// 获取列名
					String cname = rsmd.getColumnLabel(i + 1);
					//获取列类型的int表示形式，以及列类型名称
					// 获取列值
					Object value = rs.getObject(cname);
					// 将列明与列值存储到map中
					map.put(cname, value);
				}
				// 利用反射将map中的数据注入到Java对象中，并将对象存入集合
				if (!map.isEmpty()) {
					// 获取map集合键集(列名集合)
					Set<String> columnNames = map.keySet();
					// 创建对象
					obj = t.newInstance();
					for (String column : columnNames) {
						// 根据键获取值
						Object value = map.get(column);
						//当数据对象不为空时，才注入数据到属性中
						if(value!=null){	
							// 获取属性对象
							Field f = t.getDeclaredField(column);
							// 设置属性为可访问状态
							f.setAccessible(true);
							// 为属性设置
							f.set(obj, value);
						}
					}
					list.add(obj);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) {rs.close();}
				if(ps != null) {ps.close();}
				if(conn != null) {conn.close();}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * 封装单条查询结果
	 * @param t
	 * @param sql
	 * @param params
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static <T> T queryOne(Class<T> t, String sql, Object... params) {
		T obj = null;
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConn();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}
		    rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			//ORM操作（对象关系映射）
			if (rs.next()) {
				// 创建一个指定类型的实例对象(必须包含默认构造器)
				obj = t.newInstance();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					//获取指定列的列名称
					String cname = rsmd.getColumnLabel(i + 1);
					//获取列值
					Object value = rs.getObject(cname);
					if(value!=null){//1.6以上可以写成：if(Objects.nonNull(value)){							
						//根据列名称获取Java类的属性名(要求表中的列名称必须与类中的属性名保持一致)
						Field field = t.getDeclaredField(cname);
						//将字段设置为可访问状态
						field.setAccessible(true);
						//为字段设置属性值
						field.set(obj, value);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) {rs.close();}
				if(ps != null) {ps.close();}
				if(conn != null) {conn.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		return obj;
	}
}
