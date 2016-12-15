package baiduwenku;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.SQLHelper;

public class Gongzuozongjie_wenku {

	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		int ii = 10;
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0";
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(100000).get();
				// System.out.println(document);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return document;
	}

	public static boolean judgeDate(String date) {
		String[] str = date.split("-");
		int year = Integer.parseInt(str[0]);
		if (str[1].startsWith("0"))
			str[1].replace("0", "");
		int month = Integer.parseInt(str[1]);
		if (year >= 2013 || (year == 2012 && month > 5))
			return true;
		return false;
	}

	public static void main(String[] args) {
		String[] group = new String[] { "", "信用卡", "美国签证","出国旅游","澳大利亚签证","法国签证"};
		String[] stage = new String[] { "", "个人", "公司","简易","商用" };
		for (int m = 0; m < group.length; m++) {
			// for (int n = 0; n < stage.length; n++) {
			for (int j = 0; j <= 4; j++) {
				for (int i = 0; i <= 760; i += 10) {
					String pageurl = "";
					String title = "";
					String pay = "";
					String date = "";
					String author = "";
					String contents = "";
					String readers = "";
					String docid = "";

					String Url = "http://wenku.baidu.com/zhuanti/gongzuozhengming?dim0="+group[m]+"&dim1=&dim2=&lm=0&od="
							+ j
							+ "&pn="
							+ i;
					String url = java.net.URLEncoder.encode(Url)
							.replace("%3A%2F%2F", "://").replaceAll("%2F", "/")
							.replaceAll("%3D", "=").replaceAll("%3F", "?")
							.replace("%26", "&");
					Document docment = getDoc(url);
					Elements elements = docment.select(".doc-list>li");
					// System.out.println(elements.size());
					for (Element element : elements) {
						try {
							pageurl = element.select(".doc-info a")
									.attr("href").trim();
							title = element.select(".doc-info a").text();
							contents = element.select(
									".doc-info .doc-info-jianjie").text();
							pay = element
									.select(".doc-all-info>ul>li:nth-child(1)")
									.text().replaceAll("[^\\x00-\\xff]", "")
									.trim();
							int mon = Integer.parseInt(pay);
							date = element
									.select(".doc-all-info>ul>li:nth-child(2)")
									.text().replace("上传时间：", "");
							author = element.select(
									".doc-all-info>ul>li:nth-child(3)>span")
									.text();
							readers = element.select(".read-person-count")
									.text();
							docid = pageurl.replace(
									"http://wenku.baidu.com/view/", "")
									.replace(".html", "");

							if (mon == 0) {
								if (judgeDate(date)) {
									Object[] objs = { title, pageurl, date,
											contents, readers, author, docid };
									String str = "insert into wenku_hdx (title,pageurl,date,content,downloads,author,docid) values (?,?,?,?,?,?,?)";
							SQLHelper.updateBySQL(str, objs); // 保存
									System.out.println(docid + "正在抓取....");
								} else {
									System.out
											.println(docid + "超过时间，不能下载.....");
								}
							} else {
								System.out.println(docid + "需要付费，不能下载.....");
							}

							/*
							 * System.out.println(pageurl);
							 * System.out.println(title);
							 * System.out.println(pay);
							 * System.out.println(contents);
							 * System.out.println(date);
							 * System.out.println(author);
							 * System.out.println(readers);
							 * System.out.println(docid); System.out.println();
							 */
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					System.out.println("第" + i + "页");
				}

			}
		}
	}
 //}
}