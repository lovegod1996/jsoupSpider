package Qikan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class test {
	
	
	public static void main(String[] args) {
		String url="http://www.crter.org/CN/abstract/abstract9807.shtml";
		Document document=getDoc(url);
		System.out.println(document);
	}
	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		int ii = 10;
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; rv:22.0) Gecko/20100101 Firefox/22.0";
		System.out.println(BootURL);
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(3000).get();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return document;
		}
		return document;
	}

}
