package Qikan;


import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.ReadConfig;
import utils.SQLHelper;

public class Fetch {
	private static ArrayList<HashMap<String, Object>> rows;
	private static String tableName = ReadConfig.tablename;

	private static synchronized HashMap<String, Object> getType() {
		if (rows == null || rows.size() <= 0) {
			String str = "";
			str = "select id,titleURL from " + tableName
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
		//System.out.println(BootURL);
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(10000).get();
			} catch (Exception e) {
				System.out.println(BootURL);
				e.printStackTrace();
			}
			if(document!=null)
				return document;
		}
		return document;
	}

	public static void fetch() {
		String college = "";
		String title_en = "";
		String auther_en = "";
		String college_en = "";
		String reference = "";
		String Abstract = "";
		String keyword = "";
		String Abstract_en = "";
		String keyword_en = "";
		String pacs = "";
		String jijin = "";
		String doi = "";
		String tongxun = "";
		String email = "";
		String authordetail = "";
		HashMap<String, Object> row = getType();
		String titleURL = row.get("titleURL").toString(); // 从集合中抽取titleURL
		int id = Integer.parseInt(row.get("id").toString()); // 将id转化为int类型
		try {
			Document docuemnt = null;
			Elements elements = null;
			try {
				docuemnt = getDoc(titleURL);
				//System.out.println(docuemnt);
				elements = docuemnt
						.select("body>table[width=1002]>tbody>tr>td");
			} catch (NullPointerException en) {
				System.out.println(titleURL);
				en.printStackTrace();
			}
			for (Element element : elements) {
				college = element.select(
						"td>table:nth-child(2)>tbody>tr:nth-child(5)")
						.text();
				title_en = element.select(
						"td>table:nth-child(2)>tbody>tr:nth-child(7)")
						.text();
				auther_en = element.select(
						"td>table:nth-child(2)>tbody>tr:nth-child(8)")
						.text();
				try {
					college_en = element.select(
							"td>table:nth-child(2)>tbody>tr:nth-child(9)")
							.text();
				} catch (Exception e) {
				}
				Elements elements4 = element
						.select("#reference_tab_content table>tbody>tr");
				if (elements4 != null)
					for (Element element4 : elements4) {
						reference += element4.text() + "<br>";
					}
				Elements elements5 = element
						.select("#abstract_tab_content table>tbody>tr");
				for (Element element5 : elements5) {
					try {
						if (element5.html().contains("摘要"))
							Abstract = element5.select("p").text();
						if (element5.html().contains("关键词"))
							keyword = element5.select("a").text();
						if (element5.html().contains("Abstract"))
							Abstract_en = element5.select("span>p").text();
						if (element5.html().contains("中图分类号:"))
							pacs = element5.select("td:nth-child(2)")
									.text();
						if (element5.html().contains("基金资助:"))
							jijin = element5.select("td").first().ownText();
						if (element5.html().contains("作者简介"))
							authordetail = element5.select("span").first()
									.ownText().replace(": ", "");
						if (element5.html().contains("通讯作者:"))
							tongxun = element5.select("span").first()
									.ownText();
						if (element5.html().contains("Key words"))
							keyword_en = element5.select("a").text();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				Object[] objects = { college, title_en, auther_en,
						college_en, Abstract, keyword, Abstract_en, pacs,
						jijin, authordetail, tongxun, keyword_en,reference };
				String sql = "UPDATE "
						+ tableName
						+ " set college=?,title_en=?,author_en=?,college_en=?,Abstract=?,keyword=?,Abstract_en=?,ZTFLH=?,jijin=?,authordetail=?,tongxun=?,keyword_en=?,cankaowenxian=?,mark=200 where id="
						+ id;
				SQLHelper.updateBySQL(sql, objects);
				System.out.println(titleURL + "补充完整！");

				/*System.out.println(keyword);
				System.out.println(Abstract_en);
				System.out.println(pacs);
				System.out.println(jijin);
				System.out.println(authordetail);
				System.out.println(tongxun);
				System.out.println(Abstract);
				System.out.println(title_en);
				System.out.println(auther_en);
				System.out.println(college_en);
				System.out.println(college);
				System.out.println(reference);*/

			}
		} catch (Exception e) {
			 String str = "Update " + tableName + " set mark=mark+1 where id="
					 + id;
			e.printStackTrace();
		}

	}

}
