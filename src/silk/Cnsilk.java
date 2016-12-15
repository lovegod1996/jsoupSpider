package silk;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.SQLHelper;

public class Cnsilk {

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
		String Url = "";
		String title = "";
		String paguer = "";
int m=0;
		String url = "http://www.cnsilk.cn/oa/dlistnum.aspx";
		Document document1 = getDoc(url);
		Elements elements1 = document1
				.select("#ctl00_cphContect_Mydatalsit>tbody>tr td:nth-child(2) tr>td");
		// System.out.println(element1);
		for (Element element : elements1) {
			try {
				if (!element.html().contains("a"))
					continue;
				Url = "http://www.cnsilk.cn/oa/"
						+ element.select("a").attr("href")
								.replace("期", "%C6%DA").replace("年", "%C4%EA");
				Document document2 = null;
				Elements elements2 = null;
				try {
					document2 = getDoc(Url);
					elements2 = document2.select("#ctl00_cphContect_Dglist tr");
				} catch (NullPointerException en) {
					en.printStackTrace();
				}
				Elements elements3 = elements2.select(".title");
				for (Element element2 : elements3) {
					try {
						paguer = "http://www.cnsilk.cn"
								+ element2.select("a").attr("href").trim();
						title = element2.select("a").text();
						/*System.out.println(paguer);
						System.out.println(title);
						System.out.println();*/

						Object[] objs = { Url, title, paguer };
						String str = "insert into cnsilk (Url,title,paguer) values (?,?,?)";
					//	SQLHelper.updateBySQL(str, objs); // 保存
						System.out.println(paguer+"........正在抓取");
						System.out.println(m++);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			//	System.out.println(Url);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
