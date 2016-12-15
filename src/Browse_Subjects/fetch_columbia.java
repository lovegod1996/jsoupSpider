package Browse_Subjects;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.ReadConfig;
import utils.SQLHelper;

public class fetch_columbia {
	private static ArrayList<HashMap<String, Object>> rows;
	private static String tableName = ReadConfig.tablename;

	private static synchronized HashMap<String, Object> getType() {
		if (rows == null || rows.size() <= 0) {
			String str = "";
			str = "select id,paguer from " + tableName
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
		String down = "";
		String auther = "";
		String date = "";
		String type = "";
		String URL = "";
		String description = "";
		String keywords = "";
		String notes = "";
		String advisor = "";
		String citation = "";
		String views="";
		String doi="";
		String volume="";
		String book="";
		HashMap<String, Object> row = getType();
		String paguer = row.get("paguer").toString(); // 从集合中抽取titleURL
		int id = Integer.parseInt(row.get("id").toString()); // 将id转化为int类型
		Document document1 = null;
		Elements elements1 = null;
		String str = "Update " + tableName + " set mark=10 where id=" + id;
		try {
			document1 = getDoc(paguer);
			if (document1 == null) {
				System.out.println("无法获取！！");
				SQLHelper.updateBySQL(str);
				return;
			}
			elements1 = document1.select("#main");
		} catch (NullPointerException en) {
			en.printStackTrace();
		}
		for (Element element : elements1) {
			try {
				down = "http://academiccommons.columbia.edu"
						+ element.select("[class=title]>a").attr("href").trim();
				auther = element.select("[itemprop=creator]").text();
				date = element.select("[itemprop=datePublished]").text();
				type = element.select("[itemprop=genre]").text();
				// String
				// department=element.select(".defList clearfix>dd").get(4).text();
				URL = element.select("[itemprop=url]").text();
				description = element.select("[itemprop=description]").text();
				keywords = element.select("[itemprop=keywords]").text();
				if (element.select("#document div>dl:nth-child(1)>dt").get(2).text()
						.contains("Thesis Advisor(s):"))
					advisor = element.select("#document div>dl:nth-child(1)>dd").get(2).text();
				if (element.select("#document div>dl:nth-child(1)>dt").get(7).text().contains("Notes:"))
					notes = element.select("#document div>dl:nth-child(1)>dd").get(7).text();
				citation = element.select("#document div>dl:nth-child(3)>dd").get(1).text();
			views=element.select("#document div>dl:nth-child(2)>dd").text();
			if (element.select("#document div>dl:nth-child(1)>dt").last().text().contains("Publisher DOI:"))
				doi = element.select("#document div>dl:nth-child(1)>dd").last().text();
			if (element.select("#document div>dl:nth-child(1)>dt").get(5).text().contains("Volume:"))
				volume = element.select("#document div>dl:nth-child(1)>dd").get(5).text();
			if (element.select("#document div>dl:nth-child(1)>dt").get(6).text().contains("Book/Journal Title:"))
				book = element.select("#document div>dl:nth-child(1)>dd").get(6).text();
			
			
			Object[] objects = { down, auther, date, type,
					URL, description, keywords, advisor, notes, doi,views,citation,volume,book};
			String sql = "UPDATE "
					+ tableName
					+ " set download_url=?,auther=?,date=?,type=?,URL=?,description=?,keyword=?,advisor=?,notes=?,doi=?,views=?,citation=?,volume=?,book=?,mark=200 where id="
					+ id;
			SQLHelper.updateBySQL(sql, objects);
			System.out.println(paguer + "...........补充完整！");
			System.out.println();
			
			
			/*System.out.println(down);
			System.out.println(auther);
			System.out.println(date);
			System.out.println(type);
			System.out.println(URL);
			System.out.println(description);
			System.out.println(keywords);
			System.out.println(advisor);
			System.out.println(notes);
			System.out.println(doi);
			System.out.println(views);
			System.out.println(citation);
			System.out.println(volume);
			System.out.println(book);
			System.out.println();*/
			} catch (Exception e) {
				System.out.println("无法获取！！");
				SQLHelper.updateBySQL(str);
				e.printStackTrace();
			}
		}
	}

}
