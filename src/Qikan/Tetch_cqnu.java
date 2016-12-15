package Qikan;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.ReadConfig;
import utils.SQLHelper;

public class Tetch_cqnu {

	private static ArrayList<HashMap<String, Object>> rows;
	private static String tableName = ReadConfig.tablename;

	private static synchronized HashMap<String, Object> getType() {
		if (rows == null || rows.size() <= 0) {
			String str = "";
			str = "select id,pageurl from " + tableName
					+ " where mark<5 limit 500"; // 每次取出500个没有补充详细的链接
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
		String pageurl = row.get("pageurl").toString(); // 从集合中抽取paguer
		int id = Integer.parseInt(row.get("id").toString()); // 将id转化为int类型
		String str = "Update " + tableName + " set mark=mark+1 where id=" + id;
		String title_en = "";
		String author_en = "";
		String keywords = "";
		String keywords_en = "";
		String LbFLH = "";
		String TbWXBSM = "";
		String college = "";
		String college_en = "";
		String Abstract = "";
		String reference = "";
		String issn = "";
		String cn = "";
		String juan = "";
		String date = "";
		String column = "";
		Document document = null;
		try {
			document = getDoc(pageurl);
			if (document == null) {
				SQLHelper.updateBySQL(str);
			}
		} catch (NullPointerException en) {
			en.printStackTrace();
		}
		Elements elements = document.select("#container");
		for (Element element : elements) {
			try {
				title_en=element.select("#LbTitleE").text();
				issn = element.select("#LbISSN").text();
				cn = element.select("#LbCN").text();
				juan = element.select("#lbvolumne").text();
				column = element.select("#LbCoulmn").text();
				date = element.select("#LbIssueDate").text();
				author_en = element.select("#LbAuthorE").text();
				college = element.select("#LbUnitC").text();
				college_en = element.select("#LbUnitE").text();
				keywords = element.select("#LbKeyC").text();
				keywords_en = element.select("#LbKeyE").text();
				LbFLH=element.select("#LbFLH").text();
				TbWXBSM=element.select("#TbWXBSM").text();
				Abstract=element.select("#LbZY").text();
			
				Object[] objects = { issn, cn, juan,
						column, date, author_en, college, college_en,
						keywords, keywords_en, LbFLH, TbWXBSM,Abstract,title_en };
				String sql = "UPDATE "
						+ tableName
						+ " set issn=?,cn=?,juan=?,column_=?,date=?,author_en=?,college=?,college_en=?,keyword=?,keyword_en=?,LbFLH=?,TbWXBSM=?,abstract=?,title_en=?, mark=200 where id="
						+ id;
				SQLHelper.updateBySQL(sql, objects);
				System.out.println(pageurl + "  >  >  补充完整！ >  >OK!! ");
				//System.out.println(title_en);
				/*System.out.println(issn);
				System.out.println(cn);
				System.out.println(juan);
				System.out.println(column);
				System.out.println(date);
				System.out.println(author_en);
				System.out.println(college);
				System.out.println(college_en);
				System.out.println(keywords);
				System.out.println(keywords_en);
				System.out.println(LbFLH);
				System.out.println(TbWXBSM);
				System.out.println(Abstract);*/
				System.out.println();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
