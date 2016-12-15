package Qikan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class jweb {

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

	public static void main(String[] args) {
		String url = "http://202.120.85.33/Jweb_wyllyj/CN/article/showOldVolumn.do";
		Document document1 = getDoc(url);
		Elements elements1 = document1
				.select("table table >tbody>tr:nth-child(6) tr>td");
		for (Element element : elements1) {
			try {
				if (!element.text().contains("No"))
					continue;
				String link = element.select("a").attr("href");
				String paguer = null;
				if (!link.equals("")) {
					paguer = "http://202.120.85.33/Jweb_wyllyj/CN"
							+ element.select("a").attr("href").trim()
									.replace("..", "");
				} else {
					paguer = null;
				}
				String date = element.select("b").text();

				Document document2 = null;
				Elements elements2 = null;
				try {
					document2 = getDoc(paguer);
					elements2 = document2
							.select("tbody form>table table>tbody>tr:nth-child(2) table");
				} catch (NullPointerException en) {
					en.printStackTrace();
				}
				System.out.println(elements2.size());
				for (Element element2 : elements2) {
					try {
						if (!elements2.html().contains("PDF"))
							continue;
						String auther = "";
						try {
							auther = element2.select(
									"tr:nth-child(1)>td:nth-child(3)").text();
						} catch (Exception e) {
						}
						String title = element2.select(
								"tr:nth-child(2)>td:nth-child(3)").text();
						String titleURL = "http://202.120.85.33/Jweb_wyllyj/CN"
								+ element2
										.select("tr:nth-child(3)>td:nth-child(3)>a:nth-child(1)")
										.attr("href").trim().replace("..", "");

						String juan = "";
						String page = "";
						String[] ju = null;
						String[] pa = null;

						ju = element2.select("tr:nth-child(3)>td:nth-child(3)")
								.first().ownText().split(":");
						juan = ju[0];
						pa = ju[1].split("\\[]");
						page = pa[0];
						String down = "http://202.120.85.33/Jweb_wyllyj/CN"
								+ element2
										.select("tr:nth-child(3)>td:nth-child(3)>a:nth-child(4)")
										.attr("href").replace("..", "");

						/*
						 * juan = ju[0]; pa = ju[1].split("\\["); page = pa[0];
						 * String doi = element2
						 * .select("table:nth-child(2) tr:nth-child(2)>td>a")
						 * .text().replace("摘要 HTML PDF ", ""); String[] Do =
						 * element2 .select(
						 * "table:nth-child(2) tr:nth-child(2)>td>a:nth-child(4)"
						 * ) .attr("onclick").trim().split(","); String[] p =
						 * Do[0].split("\\("); String pd = p[1].replaceAll("'",
						 * "");
						 */
						/*
						 * String dow = Do[1].replace(");", ""); String down =
						 * "http://www.cjcu.jlu.edu.cn/CN/article/downloadArticleFile.do?attachType="
						 * + pd + "&id=" + dow;
						 */

						System.out.println(title);
						System.out.println(titleURL);
						System.out.println(auther);
						System.out.println(juan);
						System.out.println(page);

						System.out.println(down);
						System.out.println();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println(paguer);
				System.out.println(date);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
