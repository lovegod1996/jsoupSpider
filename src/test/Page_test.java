package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.SQLHelper;

public class Page_test {

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
	public static boolean judge(String isFree1 ,String isFree2,String isFree3){
		if (isFree1.contains("￥") // 看看是否需要下载劵
				|| !isFree2.replaceAll("[^\\x00-\\xff]", "").trim()
						.equals("0") || isFree3.contains("￥")) {
			// System.out.println(" 收费 --> " + data.getTitleURL());
			return false;
		} else
			return true;
	}
	
	public static void main(String[] args) {
		String URL = "http://wenku.baidu.com/o/saoffcn?tab=1&od=1&view=0&pay=0&cid=0&pn=260";
		Document document1 = null;
		try {
			document1 = getDoc(URL);
			// System.out.println(document1);
		} catch (NullPointerException en) {
			en.printStackTrace();
		}
		Elements elemnts1 = document1.select(".doc-detail tr");
		System.out.println(elemnts1.size());
		if (elemnts1 == null) {
			Elements elemnts2 = document1.select(".doc-msg li");
			for (Element element : elemnts2) {
				try {
					String title = element.select(".title").text();
					String pageurl = "http://wenku.baidu.com"
							+ element.select(".title a").attr("href").trim()
							+ ".html";
					String date = element.select(".upload-time").text();
                    Document document2=null;
                    try{
                    	document2=getDoc(pageurl);
                    }catch(NullPointerException en){
                    	en.printStackTrace();
                    }
                    String isFree1 = document2.select(".goods-price").text();// ￥1.00
            		String isFree2 = document2.select(".btn-download").text();// 1下载劵
            		String isFree3 = document2.select(".discribe-text>p:nth-child(2)").text();// ppt要钱这种：http://wenku.baidu.com/view/bc61079d6c175f0e7dd1377d.html
            		
            		judge(isFree1, isFree2, isFree3);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else {
			for (Element element1 : elemnts1) {
				// System.out.println(element1);
				System.out.println();
				try {
					if (element1.select("td").size() == 0)
						continue;
					String pageurl = "http://wenku.baidu.com"
							+ element1.select("td:nth-child(1) a").attr("href")
									.trim() + ".html";
					String title = element1.select("td:nth-child(1) a").attr(
							"title");
					String pay = element1.select("td:nth-child(2)").text();
					String downs = element1.select("td:nth-child(4)").text();
					String date = element1.select("td:nth-child(6)").text();
					String docid = pageurl.replace(
							"http://wenku.baidu.com/view/", "").replace(
							".html", "");

					if (pay.compareTo("免费") == 0) {
						/*
						 * if (judgeDate(date)) { Object[] objs = { title,
						 * pageurl, date, downs, author, docid }; String str =
						 * "insert into wenku_hdx (title,pageurl,date,downloads,author,docid) values (?,?,?,?,?,?)"
						 * ; SQLHelper.updateBySQL(str, objs); // 保存
						 * System.out.println(docid + "正在抓取...."); } else {
						 * System.out.println(docid + "超过时间，不能下载....."); }
						 */
					} else {
						System.out.println(docid + "需要付费，不能下载.....");
					}

					System.out.println(pageurl);
					System.out.println(title);
					System.out.println(pay);
					System.out.println(downs);
					System.out.println(date);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}
}