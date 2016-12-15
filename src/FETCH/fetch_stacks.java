package FETCH;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.ReadConfig;
import utils.SQLHelper;

public class fetch_stacks {
	private static ArrayList<HashMap<String, Object>> rows;
	private static String tableName = ReadConfig.tablename;

	private static synchronized HashMap<String, Object> getType() {
		if (rows == null || rows.size() <= 0) {
			String str = "";
			str = "select id,sonpaguer from " + tableName
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
		String sonpaguer = row.get("sonpaguer").toString(); // 从集合中抽取paguer
		int id = Integer.parseInt(row.get("id").toString()); // 将id转化为int类型

		String corporate = "";
		String published = "";
		String type = "";
		String conference = "";
		String series = "";
		String keyword = "";
		String description = "";
		String File = null;
		String SupportingFile = null;
		String str = "Update " + tableName + " set mark=10 where id=" + id;
		Document document1 = getDoc(sonpaguer);
		if (document1 == null) {
			SQLHelper.updateBySQL(str);
			return;
		}
		
		Elements elements1 = document1.select("#document-left-panel");

		String Supporting = elements1.select("#datastreams>table>tbody").text();
		File = elements1.select("#datastreams>table>tbody a").attr("href");
		if (File!= null) {
			SupportingFile = "";
		} else {
			SupportingFile = "http://stacks.cdc.gov" + File;
		}
		Elements elements2 = elements1.select("#upper-details-container tr");

		for (Element element : elements2) {
			try {
				if (element.text().contains("Corporate Authors:"))
					corporate = element.select("td:nth-child(2)").text();
				if (element.text().contains("Published Date:"))
					published = element.select("td:nth-child(2)").text();
				if (element.text().contains("File Type:"))
					type = element.select("td:nth-child(2)").text();
				if (element.text().contains("Conference Authors:"))
					conference = element.select("td:nth-child(2)").text();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Elements elements3 = elements1.select("#lower-details-container");
		for (Element element1 : elements3) {
			try {
				if (element1.text().contains("Series:")) {
					series = element1.select("div:nth-child(2)").text();
					keyword = element1.select("#mesh-keywords").text();
					description = element1.select("div").last().text();

				} else {
					keyword = element1.select("#mesh-keywords").text();
					description = element1.select("div").last().text();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		Object[] objects = { Supporting, SupportingFile, corporate, published,
				type, conference, series, keyword, description};
		String sql = "UPDATE "
				+ tableName
				+ " set Supporting=?,SupportingFile=?,Corporate=?,published=?,type=?,conference=?,series=?,Keyword=?,description=?,mark=200 where id="
				+ id;
		SQLHelper.updateBySQL(sql, objects);
		System.out.println(sonpaguer + "...........补充完整！");
		System.out.println();

		
		/*System.out.println("支持： " + Supporting);
		System.out.println("支持文件： " + SupportingFile);
		System.out.println("作者1： " + corporate);
		System.out.println("出版：   " + published);
		System.out.println("大小：  " + type);
		System.out.println("作者2： " + conference);
		System.out.println("关系：  " + series);
		System.out.println("关键词： " + keyword);
		System.out.println("描述：   " + description);
		System.out.println();*/

	}
}
