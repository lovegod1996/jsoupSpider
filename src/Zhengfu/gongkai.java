package Zhengfu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

public class gongkai {
	public static void Update(String collection, String title, String paguer,
			String data, String content, String table, String URL, String date,
			String head, String file, String filename, String id, String way,
			String limits) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "insert into huichang (collection,title,paguer,data,content,table_,URL,date,head,file,filename,ID_,way,limits) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, collection);
			ps.setString(2, title);
			ps.setString(3, paguer);
			ps.setString(4, data);
			ps.setString(5, content);
			ps.setString(6, table);
			ps.setString(7, URL);
			ps.setString(8, date);
			ps.setString(9, head);
			ps.setString(10, file);
			ps.setString(11, filename);
			ps.setString(12, id);
			ps.setString(13, way);
			ps.setString(14, limits);
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
		String id = "";
		String collection = "";
		String title = "";
		String paguer = "";
		String[] tit = null;
		String[] da = null;
		String data = "";
		String way = "";
		String limits = "";
		String head = "";
		String content = "";
		String table = "";
		String file = "";
		String filename = "";
		String L = "";
		String R = "";
		String URL = "";
		String date = "";
		String link = "";
		String url = "";
		String page = "";
		String Link = "http://xxgk.huichang.gov.cn/xxgknb/";
		Document document = getDoc(Link);
		Elements elements1 = document.select("#in_list>ul>li");
		collection = "公开年报";
	
		for (Element element : elements1) {
			try {
				title = element.select("a").attr("title");
				paguer = element.select("a").attr("href");
				Document document1 = null;
				Elements elements3 = null;
				try {
					document1 = getDoc(paguer);

					elements3 = document1
							.select("table[width=1003]:nth-child(4)");
				} catch (NullPointerException en) {
					en.printStackTrace();
				}

				for (Element element2 : elements3) {
					try {

						head = element2.select(
								".table>table[width=97%]:nth-child(1)").html();
						
						content = element2.select(".table [class=cn4] p")
								.html();

						
						table = element2.select(".table [class=cn4] table")
								.html();

						 Update(collection, title, paguer, data, content,
						 table, URL, date, head, file, filename, id, way,
						 limits);

						// System.out.println(head);
						// System.out.println(content);
						// System.out.println(table);
						System.out.println(file);
						System.out.println(filename);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				System.out.println(title);
				System.out.println(paguer);
				System.out.println(collection);
				System.out.println();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		Elements elements2 = document.select("#in_zfnb>ul>li");
		for (Element element1 : elements2) {
			try {
				title = element1.select("a").attr("title");
				paguer = "http://xxgk.huichang.gov.cn/xxgknb/"
						+ element1.select("a").attr("href");

				Document document2 = null;
				Elements elements4 = null;
				try {
					document2 = getDoc(paguer);
					elements4 = document2
							.select(".table");
				} catch (NullPointerException en) {
					en.printStackTrace();
				}
				
				for (Element element3 : elements4) {
					try {
						
						
						
						content = element3.select("[class=cn4] p")
								.html();
						table = element3.select("[class=cn4] table")
								.html();

						 Update(collection, title, paguer, data, content,
						 table, URL, date, head, file, filename, id, way,
						 limits);

						
						// System.out.println(content);
					// System.out.println(table);
						System.out.println(file);
						System.out.println(filename);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				System.out.println(title);
				System.out.println(paguer);
				System.out.println(collection);
				System.out.println();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
