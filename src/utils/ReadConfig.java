/**  
 * @author yokoboy
 * @date 2013-9-25
 */
package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author yokoboy
 * @date 2013-9-25
 */
public class ReadConfig {

	public static String filedir;
	public static int thread;
	public static int tableCount;
	public static int tableNumberStart;
	public static boolean isDebug;
	public static boolean isProxy;
	public static boolean isuseGoAgent;
	public static String proxyIp;
	public static int proxyPort;
	public static String cookie;
	public static String tablename;
	public static String ip_source;  //测试ip的源表
	public static String ip_target;  //ip的目标表
	public static String test_url;  //测试ip的链接
    public static String orderby;
    public static String CreateTable_url;
    public static String insertNumber;
    public static String insertfilepath;
    public static boolean isonetable;
    public static String username;
    public static String password;
	static {
		Properties properties = new Properties();
		File file = new File("./config.properties");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			properties.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
			throw new Error("读取配置文件失败！");
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		filedir = properties.getProperty("filedir");
		thread = Integer.parseInt(properties.getProperty("thread"));
		tableCount = Integer.parseInt(properties.getProperty("tableCount"));
		tableNumberStart = Integer.parseInt(properties.getProperty("tableNumberStart"));
		isDebug = properties.get("debug").equals("true") ? true : false;
		isProxy = properties.get("isProxy").equals("true") ? true : false;
		isuseGoAgent = properties.get("isuseGoAgent").equals("true") ? true : false;
		proxyIp = properties.get("proxyIp").toString();
		proxyPort = Integer.parseInt(properties.get("proxyPort").toString());
		cookie = properties.get("cookie").toString();
		tablename = properties.get("tablename").toString();
		ip_source = properties.get("ip_source").toString();
		ip_target = properties.get("ip_target").toString();
		test_url = properties.get("test_url").toString();
		orderby=properties.get("orderby").toString();
		CreateTable_url=properties.get("CreateTable_url").toString();
		insertNumber=properties.get("insertNumber").toString();
		insertfilepath=properties.get("insertFilepath").toString();
		isonetable = properties.get("isOneTable").equals("true") ? true : false;
		username=properties.get("username").toString();
		password = properties.get("password").toString();
	}

	public static void showConfig() {
		System.out.println("============================================");
/*		System.out.println("文件地址： " + filedir);
		System.out.println("线程数：  "+ thread);
		System.out.println("debug: " + isDebug);
		System.out.println("isProxy: " + isProxy);
		System.out.println("isuseGoAgent: " + isuseGoAgent);
		System.out.println("IP: " + proxyIp);
		System.out.println("Port: " + proxyPort);
		System.out.println("cookie: " + cookie);
		System.out.println("tableName: " + tablename);
		System.out.println("ip_source: " + ip_source);*/
		System.out.println("导入一张表: " + isonetable);
		System.out.println("导入一张表表名 " + tablename);
		System.out.println("导入源数据地址: " + insertfilepath);
		System.out.println("============================================");
	}

	public static void main(String[] args) {
		showConfig();
	}

}
