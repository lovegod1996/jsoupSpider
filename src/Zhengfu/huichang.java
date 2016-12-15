package Zhengfu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

public class huichang {

	public static void Update(String collection, String title, String paguer,
			String data, String content,  String table, String URL,String date) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "insert into huichang (collection,title,paguer,data,content,table_,URL,date) values(?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, collection);
			ps.setString(2, title);
			ps.setString(3, paguer);
			ps.setString(4, data);
			ps.setString(5, content);
			ps.setString(6, table);
			ps.setString(7, URL);
			ps.setString(8, date);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}

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

		}
		return document;
	}

	public static void main(String[] args) {
		String title = "";
		String paguer = "";
		String data = "";
		String collection = "";
		String content = "";
		
		String table = "";
		String URL="";
		String date="";
		
		
			String url = "http://www.huichang.gov.cn/hcgov/zwzx/gsgg/";
			Document document1 = getDoc(url);
			Elements elements1 = document1.select("#list_news>ul>li");
			collection = "公告公示";
			for (Element element : elements1) {
				try {
					title = element.select("a").text();
					paguer = "http://www.huichang.gov.cn/hcgov/zwzx/gsgg/"
							+ element.select("a").attr("href").replace("", "");
					data = element.select("span").text();
					//System.out.println(paguer);
					Document document2 = null;
					Elements elements2 = null;
					
					try {
						document2 = getDoc(paguer);
						elements2 = document2.select(".news-content");
					} catch (NullPointerException en) {
						en.printStackTrace();
					}
					for (Element element2 : elements2) {
						try {
							URL = element2.select(".news-status>span:nth-child(2)").text();
							
							date= element2.select(".news-status>span:nth-child(4)").text().replace("发布时间:", "");
							content = element2.select("#info_content>p").html();
							table = element2.select("#info_content>table").html();
						
					Update(collection, title, paguer, data, content, table, url, date);
						System.out.println(URL);
						System.out.println(date);
						System.out.println(content);
						System.out.println(table);
						System.out.println(collection);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					System.out.println(title);
					System.out.println(paguer);
					System.out.println(data);
					System.out.println();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
	}

}
