package Qikan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.SQLHelper;

public class cqnu {

	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		int ii = 10;
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0";
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(100000).get();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("=========读取成功========");
		return document;
	}

	public static void main(String[] args) {
		String url = "http://cqnuj.cqnu.edu.cn/oa/dlistnum.aspx";
		Document document = getDoc(url);
		// System.out.println(document);
		Elements elements = document
				.select("#ctl00_cphContect_Mydatalsit table table td");
		// System.out.println(elements);
		for (Element element : elements) {
			try {
				if (element.text().equals(""))
					continue;
				String Url = "http://cqnuj.cqnu.edu.cn/oa/"
						+ element.select("a").attr("href").trim()
								.replace("期", "%C6%DA").replace("年", "%C4%EA")
								.replaceAll("第", "%B5%DA");

				Document document1 = getDoc(Url);
				// System.out.println(document1);
				Elements elements1 = document1
						.select("#ctl00_cphContect_Dglist tr");
				for (Element element2 : elements1) {
					try {
						String title = element2.select(".title a").text();
						String pageurl ="http://cqnuj.cqnu.edu.cn"+ element2.select(".title a")
								.attr("href").trim();
                         String page=element2.select(".title span").text();
                         String author=element2.select(".authors").text();
                         String down="http://cqnuj.cqnu.edu.cn"+element2.select(".digest>a[target=_blank]").attr("href").trim();
                         String doi=element2.select(".doi a").text();
                        
                         Object[] objs = { title, pageurl, page,
                        		 author, down, doi ,Url};
 						String str = "insert into cqnu (title,pageurl,page,author,download_url,doi,sourse) values (?,?,?,?,?,?,?)";
 						SQLHelper.updateBySQL(str, objs); // 保存
 					System.out.println(title + "正在抓取....   >  >  OK!!");
						
 					/*System.out.println(title);
						System.out.println(pageurl);
						System.out.println(page);
						System.out.println(author);
						System.out.println(down);
						System.out.println(doi);
						System.out.println();*/
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				//System.out.println(Url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
