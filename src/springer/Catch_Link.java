package springer;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.ReadConfig;
import utils.SQLHelper;

public class Catch_Link {
	private static ArrayList<HashMap<String, Object>> rows;
	private static String tableName = ReadConfig.tablename;

	private static synchronized HashMap<String, Object> getType() {
		if (rows == null || rows.size() <= 0) {
			String str = "";
			str = "select id,words from " + tableName + " where mark <10 "
					+ ReadConfig.orderby + " limit 500"; // 每次取出500个没有补充详细的链接
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
			if (document != null)
				return document;
		}
		return document;
	}

	public static void fetch() {

		HashMap<String, Object> row = getType();
		String words = row.get("words").toString(); // 从集合中抽取paguer
		int id = Integer.parseInt(row.get("id").toString()); // 将id转化为int类型

		String Test_Url = "http://link.springer.com/search?query=" + words
				+ "&facet-content-type=Article&showAll=false";
		Document docu = null;
		try {
			docu = getDoc(Test_Url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Elements elemen = docu
				.select(".number-of-search-results-and-search-terms");
		String Size = elemen.select("strong").first().toString()
				.replace("<strong>", "").replace("</strong>", "")
				.replace(",", "");
		int PageSize = Integer.parseInt(Size) / 20;
		int page = 1;
		if (PageSize > 1000) {
			page = 999;
		} else {
			page = PageSize;
		}
		for (int i = 1; i <= page; i++) {
			String url = "http://link.springer.com/search/page/" + i
					+ "?facet-content-type=Article&showAll=false&query="
					+ words;
			Document document = null;
			try {
				document = getDoc(url);
			} catch (NullPointerException en) {
				en.printStackTrace();
			}
			Elements elements = document.select("#results-list>li");
			for (Element element : elements) {
				try {
					String title = element.select("h2>a").text();
					String pageurl = "http://link.springer.com"
							+ element.select("h2>a").attr("href").trim();
					//System.out.println(title);
					
					Object[] objs = { title, pageurl };
					String str = "insert into springer_hdx (title,pageurl) values (?,?)";
					 SQLHelper.updateBySQL(str, objs); // 保存
					System.out.println(pageurl + "正在抓取....");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		String str2 = "UPDATE " + tableName + " set mark=100 where id=" + id;
		SQLHelper.updateBySQL(str2);
	}
}
