package springer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.SQLHelper;

public class Link_Springer {

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
		// TODO Auto-generated method stub
		String[] letter = new String[] { "b", "c", "d", "f", "g", "h"};
		for (int j = 0; j < letter.length; j++)
			for (int i = 1; i <= 999; i++) {
				String url = "http://link.springer.com/search/page/" + i
						+ "?facet-content-type=%22Article%22&query="
						+ letter[j];
				Document document=null;
				try{
					document=getDoc(url);
				}catch(NullPointerException en){
					en.printStackTrace();
				}
				Elements elements=document.select("#results-list>li");
			for (Element element : elements) {
				try{
					String title=element.select("h2>a").text();
					String pageurl="http://link.springer.com"+element.select("h2>a").attr("href").trim();
					//System.out.println(title);
					
					Object[] objs = { title, pageurl};
					String str = "insert into springer_hdx (title,pageurl) values (?,?)";
			    SQLHelper.updateBySQL(str, objs); // 保存
					System.out.println(pageurl + "正在抓取....");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			}
	}

}
