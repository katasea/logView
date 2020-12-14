package cn.katasea.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 全局变量
 */
public class Global {
	//系统版本号
	public static final String VERSION = "Finchley.RELEASE";
	public static final String ENCODING = "UTF-8";
	public static final String JSON = "json";
	public static final String CHARSET = "utf-8";
	public static final String NULLSTRING = "";//""字符串
	public static final String AUTO_DB_PO_PACKAGE = "cn.katasea.bean.po";
	public static Map<String,String> isCreatedTable = new HashMap<>();//记录当年是否已经创建了表
	public static final DateFormat year_month = new SimpleDateFormat("yyyy-MM");//日期format
	public static final DateFormat year_month_day = new SimpleDateFormat("yyyy-MM-dd");//日期format
	public static final DateFormat year_month_day_no_ = new SimpleDateFormat("yyyyMMdd");//日期format
	public static final DateFormat year_month_day_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期format
	public static final DateFormat year_month_day_time_no_ = new SimpleDateFormat("yyyyMMddHHmmss");//日期format
	public static final DateFormat dfpath = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");//日期format
	public static final DateFormat dtimecode = new SimpleDateFormat("mm_sss");//日期format
	public static final int TIMEOUT = 30000;
	public static final String RESFROM_ROLE = "1";
	public static final String RESFROM_USER = "2";
	/**
	 * 创建UUID
	 *
	 * @return UUID
	 */
	public static String createUUID() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.toUpperCase();
		uuid = uuid.replaceAll("-", Global.NULLSTRING);
		return uuid;
	}

	/**
	 * 关闭JDBC
	 *
	 * @param conn Connection
	 * @param stmt Statement
	 * @param rs ResultSet
	 */
	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if(conn != null) {
				conn.close();
			}
			if(stmt != null) {
				stmt.close();
			}
			if(rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
