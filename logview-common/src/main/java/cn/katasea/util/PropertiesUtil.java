package cn.katasea.util;

import java.io.*;
import java.util.*;

/**
 * @author linmr
 * @description: 用于操作properties
 * @date 2019/7/26
 */
public class PropertiesUtil {

	private String properiesName = "";

	public PropertiesUtil(String fileName) {
		this.properiesName = fileName;
	}

	/**
	 * 按key获取值
	 * @param key
	 * @return
	 */
	public String readProperty(String key) {
		String value = "";
		InputStream is = null;
		try {
			is = PropertiesUtil.class.getClassLoader().getResourceAsStream(properiesName);
			Properties p = new Properties();
			p.load(is);
			value = p.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	/**
	 * 获取整个配置信息
	 * @return
	 */
	public Properties getProperties() {
		Properties p = new Properties();
		InputStream is = null;
		try {
			is = PropertiesUtil.class.getClassLoader().getResourceAsStream(properiesName);
			p.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return p;
	}

	/**
	 * key-value写入配置文件
	 * @param key
	 * @param value
	 */
	public void writeProperty(String key, String value) {
		InputStream is = null;
		OutputStream os = null;
		Properties p = new Properties();
		try {
			is = new FileInputStream(PropertiesUtil.class.getClassLoader().getResource(properiesName).getPath());
			p.load(is);
			// 一定要在修改值之前关闭fis
			is.close();

			if(null!=value){
				p.setProperty(key, value);
				os = new FileOutputStream(PropertiesUtil.class.getClassLoader().getResource(properiesName).getPath());
				p.store(os, DateUtil.getCurrentDate()+" Update '" + key + "' value"+":"+value);
				os.flush();
				os.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != is)
					is.close();
				if (null != os)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		// sysConfig.properties(配置文件)
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		System.out.println("------1--------"+p.getProperties().get("call_db_url"));
		System.out.println("------2--------"+p.readProperty("call_db_url"));
		PropertiesUtil q = new PropertiesUtil("sysConfig.properties");
		q.writeProperty("call_db_url", "wang");
		System.out.println("-------3-------"+p.getProperties().get("call_db_url"));
		System.out.println("-------4-------"+p.readProperty("call_db_url"));
	}

}
