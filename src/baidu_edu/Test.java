package baidu_edu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;



public class Test {

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
	
	public static void main(String[] args)  {

		String url="http://wenku.baidu.com/view/2930c6cb0029bd64793e2c4e.html?qq-pf-to=pcqq.c2c";
		Document docment=getDoc(url);
		System.out.println(docment);
		/*Elements element1=docment.select(".cond-list-wrap");
		System.out.println(element1);*/
	}

}
