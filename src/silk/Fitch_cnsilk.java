package silk;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.ReadConfig;
import utils.SQLHelper;

public class Fitch_cnsilk {

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
		HashMap<String, Object> row = getType();
		String paguer = row.get("paguer").toString(); // 从集合中抽取titleURL
		int id = Integer.parseInt(row.get("id").toString()); // 将id转化为int类型

		String down = "";
		String[] loc = null;
		String issn = "";
		String cn = "";
		String juan = "";
		String period = "";
		String page = "";
		String collection = "";
		String data = "";
		String title_en = "";
		String scriptid = "";
		String auther = "";
		String unit = "";
		String auther_en = "";
		String unit_en = "";
		String Key = "";
		String Key_en = "";
		String flh = "";
		String doi = "";
		String wxbsm = "";
		String zy = "";
		String zy_en = "";
		String references = "";
		String[] me = null;
		String jijin = "";
		String sd = "";
		String cd = "";
		String AbAuthor = "";
		String tongxun = "";
		String email = "";
		String[] da = null;
		int m = 0;
		String str = "Update " + tableName + " set mark=+5 where id=" + id;

		Document docment1 = null;
		Elements elements1 = null;
		try {
			docment1 = getDoc(paguer);
			elements1 = docment1.select("#container");
		} catch (NullPointerException en) {
			en.printStackTrace();
		}
		for (Element element : elements1) {
			try {
				down = "http://www.cnsilk.cn/oa/"
						+ element.select("#pdf").attr("href").trim();
				loc = element.select("#maginfo>h2").text().split(":");
				issn = loc[1].replace("/CN", "");
				cn = loc[2].replace("]", "");
				juan = element.select("#lbvolumne").text();
				period = element.select("#LbIssue").text();
				page = element.select("#LbPageNum").text();
				collection = element.select("#LbCoulmn").text();
				data = element.select("#LbIssueDate").text();
				title_en = element.select("#LbTitleE").text();
				scriptid = element.select("#lbscriptid").text();
				auther = element.select("#LbAuthorC").text();
				unit = element.select("#LbUnitC").text();
				auther_en = element.select("#LbAuthorE").text();
				unit_en = element.select("#LbUnitE").text();
				Key = element.select("#LbKeyC").text();
				Key_en = element.select("#LbKeyE").text();
				flh = element.select("#LbFLH").text();
				doi = element.select("#LbDOI").text();
				wxbsm = element.select("#TbWXBSM").text();
				zy = element.select("#LbZY").text();
				zy_en = element.select("#LbZYE").text();
				references = element.select("#LbCK").html();

				m = element.select("#LbMemory>br").size();
				if (m == 1) {
					me = element.select("#LbMemory").html().split("<br />");
					if (!me[0].contains("修回日期")) {
						da = me[0].split("；");
						sd = da[0].replace("收稿日期：", "").replace("收稿日期:", "");
						cd = da[1].replace("修回日期：", "").replace("修回日期:", "");
						AbAuthor = me[1].replace("作者简介：", "").replace("作者简介:",
								"");
					} else {
						sd = me[0].replace("收稿日期：", "").replace("收稿日期:", "");
						AbAuthor = me[1].replace("作者简介：", "").replace("作者简介:",
								"");
					}
				} else if (m == 2) {
					me = element.select("#LbMemory").html().split("<br />");
					for (int i = 0; i < me.length; i++) {
						if (me[i].contains("基金项目"))
							jijin = me[i].replace("基金项目：", "").replace("基金项目:",
									"");
						if (me[i].contains("收稿日期"))
							sd = me[i].replace("收稿日期：", "")
									.replace("收稿日期:", "");
						if (me[i].contains("修回日期"))
							cd = me[i].replace("修回日期：", "")
									.replace("修回日期:", "");
						if (me[i].contains("修回日期")&&me[i].contains("收稿日期")) {
							da = me[i].split("；");
							sd = da[0].replace("收稿日期：", "").replace("收稿日期:", "");
							cd = da[1].replace("修回日期：", "").replace("修回日期:", "");
						}
						
						if (me[i].contains("作者简历"))
							AbAuthor = me[i].replace("作者简历：", "");
						if (me[i].contains("作者简介"))
							AbAuthor = me[i].replace("作者简介：", "").replace(
									"作者简介:", "");
					}
				} else if (m == 3) {
					me = element.select("#LbMemory").html().split("<br />");
					for (int i = 0; i < me.length; i++) {
						if (me[i].contains("基金项目"))
							jijin = me[i].replace("基金项目：", "").replace("基金项目:",
									"");
						if (me[i].contains("收稿日期"))
							sd = me[i].replace("收稿日期：", "")
									.replace("收稿日期:", "");
						if (me[i].contains("修回日期"))
							cd = me[i].replace("修回日期：", "")
									.replace("修回日期:", "");
						if (me[i].contains("作者简历"))
							AbAuthor = me[i].replace("作者简历：", "");
						if (me[i].contains("作者简介"))
							AbAuthor = me[i].replace("作者简介：", "").replace(
									"作者简介:", "");
						if (me[i].contains("通信作者")) {
							tongxun = me[i].replace("通信作者：", "").replace(
									me[i].substring(me[i].indexOf("<a ")), "");
							email = me[i]
									.replace(tongxun, "")
									.replace("通信作者：", "")
									.replace(
											me[i].substring(
													me[i].indexOf("<a "),
													me[i].indexOf(">")), "")
									.replace("</a>", "").replace(">", "");
						}
						if (me[i].contains("通讯作者")) {
							tongxun = me[i].replace("通讯作者：", "").replace(
									me[i].substring(me[i].indexOf("<a ")), "");
							email = me[i]
									.replace(tongxun, "")
									.replace("通讯作者：", "")
									.replace(
											me[i].substring(
													me[i].indexOf("<a "),
													me[i].indexOf(">")), "")
									.replace("</a>", "").replace(">", "");
						}
					}
				} else if (m == 4) {
					me = element.select("#LbMemory").html().split("<br />");
					for (int i = 0; i < me.length; i++) {
						if (me[i].contains("基金项目"))
							jijin = me[i].replace("基金项目：", "").replace("基金项目:",
									"");
						if (me[i].contains("收稿日期"))
							sd = me[i].replace("收稿日期：", "")
									.replace("收稿日期:", "");
						if (me[i].contains("修回日期"))
							cd = me[i].replace("修回日期：", "")
									.replace("修回日期:", "");
						if (me[i].contains("作者简历"))
							AbAuthor = me[i].replace("作者简历：", "");
						if (me[i].contains("作者简介"))
							AbAuthor = me[i].replace("作者简介：", "").replace(
									"作者简介:", "");
						if (me[i].contains("通信作者")) {
							tongxun = me[i].replace("通信作者：", "").replace(
									me[i].substring(me[i].indexOf("<a ")), "");
							email = me[i]
									.replace(tongxun, "")
									.replace("通信作者：", "")
									.replace(
											me[i].substring(
													me[i].indexOf("<a "),
													me[i].indexOf(">")), "")
									.replace("</a>", "").replace(">", "");
						}
						if (me[i].contains("通讯作者")) {
							tongxun = me[i].replace("通讯作者：", "").replace(
									me[i].substring(me[i].indexOf("<a ")), "");
							email = me[i]
									.replace(tongxun, "")
									.replace("通讯作者：", "")
									.replace(
											me[i].substring(
													me[i].indexOf("<a "),
													me[i].indexOf(">")), "")
									.replace("</a>", "").replace(">", "");
						}
					}
				} else if (m == 5) {
					me = element.select("#LbMemory").html().split("<br />");
					for (int i = 0; i < me.length; i++) {
						if (me[i].contains("基金项目"))
							jijin = me[i].replace("基金项目：", "");
						if (me[i].contains("收稿日期"))
							sd = me[i].replace("收稿日期：", "");
						if (me[i].contains("修回日期"))
							cd = me[i].replace("修回日期：", "");
						if (me[i].contains("作者简历"))
							AbAuthor = me[i].replace("作者简历：", "");
						if (me[i].contains("作者简介"))
							AbAuthor = me[i].replace("作者简介：", "");
						if (me[i].contains("通信作者")) {
							tongxun = me[i].replace("通信作者：", "").replace(
									me[i].substring(me[i].indexOf("<a ")), "");
							email = me[i]
									.replace(tongxun, "")
									.replace("通信作者：", "")
									.replace(
											me[i].substring(
													me[i].indexOf("<a "),
													me[i].indexOf(">")), "")
									.replace("</a>", "").replace(">", "");
						}
						if (me[i].contains("通讯作者")) {
							tongxun = me[i].replace("通讯作者：", "").replace(
									me[i].substring(me[i].indexOf("<a ")), "");
							email = me[i]
									.replace(tongxun, "")
									.replace("通讯作者：", "")
									.replace(
											me[i].substring(
													me[i].indexOf("<a "),
													me[i].indexOf(">")), "")
									.replace("</a>", "").replace(">", "");
						}
					}
				}
				/*
				 * me=element.select("#LbMemory").html().split("<br />");
				 * if(me[0].contains("收稿日期")) sd=me[0];
				 * if(me[0].contains("基金项目")) jijin=me[0];
				 * if(me[1].contains("修回日期")) cd=me[1];
				 * if(me[2].contains("作者简介")) AbAuthor=me[2];
				 * 
				 * if(me[3].contains("通信作者")) tongxun=me[3];
				 */
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}

		/*
		 * System.out.println(issn); System.out.println(cn);
		 * System.out.println(juan); System.out.println(period);
		 * System.out.println(page); System.out.println(collection);
		 * System.out.println(data); System.out.println(title_en);
		 * System.out.println(scriptid); System.out.println(auther);
		 * System.out.println(unit); System.out.println(auther_en);
		 * System.out.println(unit_en); System.out.println(Key);
		 * System.out.println(Key_en); System.out.println(flh);
		 * System.out.println(doi); System.out.println(wxbsm);
		 * System.out.println(zy); System.out.println(zy_en);
		 * System.out.println(references);
		 */
		/*System.out.println();
		System.out.println("收稿  " + sd);
		System.out.println("修回 " + cd);
		System.out.println("基金 " + jijin);
		System.out.println("作者简介 " + AbAuthor);
		System.out.println("通讯  " + tongxun);
		System.out.println("邮件  " + email);
		System.out.println(m);

		System.out.println(paguer);

		System.out.println(down);
		System.out.println();*/

		Object[] objects = { issn, cn, juan, period, page, collection, data,
				title_en, scriptid, auther, unit, auther_en, unit_en, Key,
				Key_en, flh, doi, wxbsm, zy, zy_en, references, sd, cd, jijin,
				AbAuthor, tongxun, email, down };
		String sql = "UPDATE "
				+ tableName
				+ " set issn=?,cn=?,juan=?,period=?,page=?,collection=?,data=?,title_en=?,scriptid=?,auther=?,unit=?,auther_en=?,unit_en=?,Key_=?,Key_en=?,flh=?,doi=?,wxbsm=?,zy=?,zy_en=?,References_=?,sd=?,cd=?,jijin=?,AbAuthor=?,tongxun=?,email=?,download_url=?,mark=200 where id="
				+ id;
		 SQLHelper.updateBySQL(sql, objects);
		 System.out.println(paguer + "...........补充完整！");
		System.out.println();
	}

}
