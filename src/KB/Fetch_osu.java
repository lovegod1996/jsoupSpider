package KB;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.ReadConfig;
import utils.SQLHelper;

public class Fetch_osu {
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
			if (document != null)
				return document;
		}
		return document;
	}

	public static void fetch() {

		HashMap<String, Object> row = getType();
		String paguer = row.get("paguer").toString(); // 从集合中抽取paguer
		int id = Integer.parseInt(row.get("id").toString()); // 将id转化为int类型
		String down = "";
		String Keyword = "";
		String issue = "";
		String report = "";
		String description = "";
		String publisher = "";
		String Abstract = "";
		String Rights = "";
		String citation = "";
		String ISSN = "";
		String size = "";
		String pdfDescription = "";
		Document document1 = null;
		Elements elements1 = null;
		String str = "Update " + tableName + " set mark=10 where id=" + id;
		try {
			document1 = getDoc(paguer);
			if (document1 == null) {
				SQLHelper.updateBySQL(str);
				return;
			}
			elements1 = document1
					.select("#aspect_artifactbrowser_ItemViewer_div_item-view");
		} catch (Exception e) {

			e.printStackTrace();
		}
		Elements elements2 = null;
		Elements elements3 = null;
		for (Element element : elements1) {
			try {
				if (!element.text().contains("PDF")) {
					System.out.println("数据错误！" + paguer);
					SQLHelper.updateBySQL(str);
					continue;

					/*
					 * } else if (element.text().contains("HTML") ||
					 * element.text().contains("Microsoft Word") &&
					 * !element.text().contains("PDF")) {
					 * System.out.println("数据错误！" + paguer);
					 * 
					 * SQLHelper.updateBySQL(str);
					 */
				}
				// System.out.println(element);
				elements3 = elements1.select(".ds-table>tbody>tr");
				for (Element element2 : elements3) {
					// System.out.println(element2);
					if (element2.text().contains("PDF")
							&& element2.text().contains("Event webpage")) {
						down = "http://kb.osu.edu"
								+ element2.select("td:nth-child(1)>a").attr(
										"href");
						size = element2.select("td:nth-child(2)").text();
						try {
							pdfDescription = element2.select("td:nth-child(5)")
									.text();
						} catch (Exception e) {
						}
					} else if (!element2.text().contains("HTML")) {
						down = "http://kb.osu.edu"
								+ element2.select("td:nth-child(1)>a").attr(
										"href");
						size = element2.select("td:nth-child(2)").text();
						try {
							pdfDescription = element2.select("td:nth-child(5)")
									.text();
						} catch (Exception e) {
						}
					}

					// System.out.println("下载：  "+down);

					/*
					 * else if(!element2.text().contains("HTML")) down =
					 * "http://kb.osu.edu" + element.select("td:nth-child(1)>a")
					 * .attr("href");
					 */
				}

				elements2 = element.select(".ds-includeSet-table>tbody>tr");
				for (Element element1 : elements2) {
					try {
						if (element1.html().contains("Keywords:"))
							Keyword = element1.select("td:nth-child(2)").text();
						if (element1.html().contains("Issue Date:"))
							issue = element1.select("td:nth-child(2)").text();
						if (element1.html().contains("Series/Report no.:"))
							report = element1.select("td:nth-child(2)").text();
						if (element1.html().contains("Description:"))
							description = element1.select("td:nth-child(2)")
									.text();
						if (element1.html().contains("Publisher:"))
							publisher = element1.select("td:nth-child(2)")
									.text();
						if (element1.html().contains("Abstract:"))
							Abstract = element1.select("td:nth-child(2)")
									.text();
						if (element1.html().contains("Rights:"))
							Rights = element1.select("td:nth-child(2)").text();
						if (element1.html().contains("Citation:"))
							citation = element1.select("td:nth-child(2)")
									.text();
						if (element1.html().contains("ISSN:"))
							ISSN = element1.select("td:nth-child(2)").text();
						// System.out.println(element1);
						/*
						 * System.out.println("关键词：   "+Keyword);
						 * System.out.println("日期：  "+issue);
						 * System.out.println("报道   ： "+report);
						 * System.out.println("描述：  "+description);
						 * System.out.println("出版 ： "+publisher);
						 * System.out.println("摘要：  "+Abstract);
						 * System.out.println("方式：  "+Rights);
						 * System.out.println("条件：  "+citation);
						 * System.out.println("区域：   "+ISSN);
						 */

					} catch (Exception e) {

						e.printStackTrace();
					}
				}
				Object[] objects = { Keyword, issue, report, description,
						publisher, Abstract, Rights, citation, ISSN, down,size,pdfDescription};
				String sql = "UPDATE "
						+ tableName
						+ " set Keyword=?,issue=?,report=?,description=?,publisher=?,Abstract=?,Rights=?,citation=?,ISSN=?,download_url=?,size=?,pdfDescription=?,mark=200 where id="
						+ id;
				SQLHelper.updateBySQL(sql, objects);
				System.out.println(paguer + "...........补充完整！");
				System.out.println();

				/*
				 * System.out.println("关键词：   "+Keyword);
				 * System.out.println("日期：  "+issue);
				 * System.out.println("报道   ： "+report);
				 * System.out.println("描述：  "+description);
				 * System.out.println("出版 ： "+publisher);
				 * System.out.println("摘要：  "+Abstract);
				 * System.out.println("方式：  "+Rights);
				 * System.out.println("条件：  "+citation);
				 * System.out.println("区域：   "+ISSN); System.out.println();
				 */
				//System.out.println("下载：  " + down);
				//System.out.println(size);
				//System.out.println(pdfDescription);
			} catch (Exception e) {

				// e.printStackTrace();
			}
		}
	}
}
