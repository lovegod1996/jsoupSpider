package test;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.GetDocument;
import utils.SQLHelper;

public class Test2 {

	public static Document getDoc1(String BootURL) { // 获取box
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
	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		int ii = 3;
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; rv:22.0) Gecko/20100101 Firefox/22.0";
		// System.out.println(BootURL);
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(30000).get();
			} catch (HttpStatusException htp) {
				// htp.printStackTrace();
				ii = 0;
				System.err.println("已删除，网页地址错误！！");
			} catch (Exception e) {
				System.err.println("访问失败，标记一次");
				System.out.println(BootURL);
				e.printStackTrace();
			}
		}
		System.out.println("=========读取成功========");
		return document;
	}
	public static void main(String[] args) {
		String URL = "https://www.kicksup.com/";
		Document doc = getDoc(URL);
		Elements elements = doc.select("#leftPan>.one>li");
//		System.out.println(doc);
		String url=null,url_t=null;;
		for(Element element:elements)
		{
			url_t=element.select("a").text();
			if(url_t.contains("Home"))
				url="https://www.kicksup.com/";
			else
				url=element.select("a").attr("href");
			System.out.println(url);
			System.out.println(url_t);
			Document doc1 =GetDocument.connect(url);
			Elements elements3 = doc1.select("#bodyPan a");
			
			String title=null,page_url=null;
			for(Element element3 :elements3)
			{
				title=element3.text();
				page_url="https://www.kicksup.com"+element3.attr("href");
				System.out.println(title);
				System.out.println(page_url);
				String download_url=null,a=null,con=null;
				Document doc11 = getDoc(page_url);
				if(doc11.html().contains("PDF found:")){
					download_url=doc11.select("a").first().attr("href");
					
				}
				else if(doc11.html().contains("PDF Preview")){
					download_url="http://www.kicksup.com"+doc11.select("#bodyPan a").get(1).attr("href").replace(".htm", ".pdf");
					a=doc11.select("#bodyPan a").get(1).text();
					con=doc11.select("#bodyPan").text();
//					System.out.println("***"+download_url);
//					System.out.println("----"+a);
				}
				System.out.println("***"+download_url);
				System.out.println("----"+a);
				
				Object[] objects = {url,url_t,page_url,title,download_url,con};  
				String sql = "INSERT INTO kicksup (url,url_t,page_url,title,download_url,con) VALUES (?,?,?,?,?,?)";  //预编译
				try{
				SQLHelper.insertBySQL(sql, objects);  //调用公共类的插入方法
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
	}

}
