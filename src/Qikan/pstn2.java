package Qikan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.SQLHelper;

public class pstn2 {
	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		int ii = 10;
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; rv:22.0) Gecko/20100101 Firefox/22.0";
		// System.out.println(BootURL);
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(3000).get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return document;
	}

	public static void main(String[] args) {
		String url = "";

		String Did = "";
		String data = "";

		String title = "";
		String paguer = "";
		String down = "";
		String author = "";
		String source = "";
		String time = "";
		String content = "";
		String keyword = "";
		String page = "";
		String Abstract = "";

		for (int i = 1; i < 3; i++) {
			url = "http://www.ptsn.net.cn/article_new/sr_result.php?categories_id=bfe2aae3-6576-a58b-431d-44b1be6b2429&fromDate=&toDate=&selectStr=&page_currentPage="
					+ i;
			Document document1 = getDoc(url);
			Elements elements1 = document1.select("table tr:nth-child(4) tr");
			// System.out.println(elements1.size());
			for (Element element : elements1) {
				try {
					if (!element.html().contains("<a"))
						continue;
					title = element.select("td:nth-child(2)").text();
					paguer = "http://www.ptsn.net.cn"
							+ element.select("td:nth-child(2) a").attr("href");
					Did = element.select("td:nth-child(3)").text();
					data = element.select("td:nth-child(4)").text();
					Document document2 = null;
					Elements elements2 = null;
					try {
						document2 = getDoc(paguer);
						elements2 = document2.select(".FormTABLE tr");
					} catch (NullPointerException en) {
						en.printStackTrace();
					}
					for (Element element2 : elements2) {

						try {
							if (element2.text().contains("作者"))
								author = element2.select("td").text();
							if (element2.text().contains("来源"))
								source = element2.select("td").text();
							if (element2.text().contains("发布时间"))
								time = element2.select("td").text();
							if (element2.text().contains("关键词"))
								keyword = element2.select("td").text();
							if (element2.text().contains("页数"))
								page = element2.select("td").text();
							if (element2.text().contains("摘要"))
								Abstract = element2.select("td").text();
							if (element2.text().contains("全文下载"))
								down = "http://www.ptsn.net.cn"
										+ element2.select("td td[width=98%] a")
												.first().attr("href");
							if (element2.text().contains("荣誉展示"))
								down = "http://www.ptsn.net.cn"
										+ element2.select("td td[width=98%] a")
												.first().attr("href");

							/*
							 * System.out.println(author);
							 * System.out.println(source);
							 * System.out.println(time);
							 * System.out.println(content);
							 * System.out.println(down);
							 */
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					Object[] objects = { down, author, source, time,
							 title, paguer, Did,data,page,Abstract,keyword};
					String str = "insert into pstn (download_url,author,source,time,title,paguer,Did,data,page,Abstract,keyword) values (?,?,?,?,?,?,?,?,?,?,?)";
					SQLHelper.updateBySQL(str, objects);
					System.out.println(title + "...........添加完成！");
					System.out.println();
					System.out.println(author);
					System.out.println(source);
					System.out.println(time);
					System.out.println(page);
					System.out.println(Abstract);
					System.out.println(keyword);
					System.out.println(down);

					System.out.println(title);
					System.out.println(paguer);
					System.out.println(Did);
					System.out.println(data);
					System.out.println();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
