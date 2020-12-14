package cn.katasea.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 访问Access数据库类
 *
 * @author CFQ
 */
public class AccessOptUtil {

	public static Connection connectAccessFile() {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			String dbur1 = "jdbc:ucanaccess://./config/db.accdb";
			return DriverManager.getConnection(dbur1, Global.NULLSTRING, Global.NULLSTRING);
//			InitialContext ctx = new InitialContext();
//			DataSource datasource = (DataSource) ctx.lookup(dbur1);
//			return  datasource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


}
