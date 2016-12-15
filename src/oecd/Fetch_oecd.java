package oecd;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import utils.ReadConfig;
import utils.SQLHelper;

public class Fetch_oecd {

	private static ArrayList<HashMap<String, Object>> rows;
	private static String tableName = ReadConfig.tablename;

	private static synchronized HashMap<String, Object> getType() {
		if (rows == null || rows.size() <= 0) {
			String str = "";
			str = "select id,pageurl from " + tableName
					+ " where mark<10 limit 500"; // 每次取出500个没有补充详细的链接
			rows = SQLHelper.selectBySQL(str);
			if (rows.size() <= 0) {
				System.out.println("==========未取到链接=======");
				System.exit(0);
			}
		}
		HashMap<String, Object> row = rows.get(0);
		rows.remove(0);
		return row;
	}

	public static void main(String[] args) {
		for (int i = 0; i < ReadConfig.thread; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (;;) {
						fetch();
					}
				}
			}).start();

		}
	}

	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		int ii = 10;
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; rv:22.0) Gecko/20100101 Firefox/22.0";
		// System.out.println(BootURL);
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(10000).get();
			} catch (Exception e) {
				System.out.println(BootURL);
				e.printStackTrace();
			}

		}
		return document;
	}

	public static void fetch() {
		HashMap<String, Object> row = getType();
		String pageurl = row.get("pageurl").toString(); // 从集合中抽取titleURL
		int id = Integer.parseInt(row.get("id").toString()); // 将id转化为int类型
		
		
		String str = "Update " + tableName + " set mark=10 where id=" + id;
		Document document = null;
		Elements elements = null;

		try {
			document = getDoc(pageurl);
			if (document == null) {
				System.out.println("无法获取！！");
				SQLHelper.updateBySQL(str);
				return;
			}
		} catch (Exception en) {
			en.printStackTrace();
		}
		System.out.println(pageurl);
	}
}
