package biaozhun;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.SQLHelper;

public class Biaozhunw {

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

	public static void main(String[] args) {
		String collection = "";
		String Url = "";
		String First = "";
		String URL = "";
		String paguer = "";
		String url = "http://www.biaozhunw.com/Index.html";
		Document document1 = getDoc(url);
		Elements elements1 = document1.select(".subnav li");
		for (Element element : elements1) {
			try {
				collection = element.select("a").text();
				Url = "http://www.biaozhunw.com" // 获取集合的链接
						+ element.select("a").attr("href");
				Document document2 = null;
				Elements elements2 = null;
				try {
					document2 = getDoc(Url);
					elements2 = document2.select("#pe100_page_infolist");
				} catch (NullPointerException en) {
					en.printStackTrace();
				}
				for (Element element2 : elements2) {
					try {
						First = element2.select("a").first().attr("href"); // 获取首页地址
						String[] id = First.split("_");
						String lang = id[1].replace(".html", ""); // 获取总共页数
						int num = Integer.parseInt(lang);
						for (int i = 1; i <= num; i++) {
							URL = Url + "List_" + i + ".html"; // 获取页面的链接
							// System.out.println(URL);
							Document document3 = null;
							Elements elements3 = null;
							try {
								document3 = getDoc(URL);
								elements3 = document3.select(".soft_list li");
							} catch (NullPointerException en) {
								en.printStackTrace();
							}
							for (Element element3 : elements3) {
								try {
									paguer = "http://www.biaozhunw.com"
											+ element3.select("a").attr("href"); // 获取详细页面链接

									Object[] objs = { collection, Url, paguer };
									String str = "insert into biaozhun (collection,url,paguer) values (?,?,?)";
									SQLHelper.updateBySQL(str, objs); // 保存
									System.out.println(paguer);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println(collection);
				System.out.println(Url);
				System.out.println();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
