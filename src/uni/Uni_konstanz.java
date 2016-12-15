package uni;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.SQLHelper;

public class Uni_konstanz {
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
		String url=null;
		String title="";
		String pageurl="";
		for (int i = 1; i <=1514; i++) {
			
			url="http://kops.uni-konstanz.de/discover?rpp=10&page="+i+"&group_by=none&etal=0&filtertype_0=has_full_text&filter_0=1&filter_relational_operator_0=equals";
		Document docment=null;
		Elements elements=null;
		try{
			docment=getDoc(url);
		}catch(NullPointerException en){
			en.printStackTrace();
		}
		elements=docment.select("#aspect_discovery_SimpleSearch_div_search-results li");
		for (Element element : elements) {
			try{
				if(element.select("img").size()==2)
					continue;
				title=element.select(".artifact-title>a").text();
				pageurl="http://kops.uni-konstanz.de"+element.select(".artifact-title>a").attr("href");
				
				Object[] objs = { 
						title, pageurl };
				String str = "insert into uni_konstanz (title,pageurl) values (?,?)";
					SQLHelper.updateBySQL(str, objs); // 保存
					System.out.println(pageurl + "........正在抓取");
				//System.out.println(title);
			//	System.out.println(pageurl);
				System.out.println();
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		}
	}
}
