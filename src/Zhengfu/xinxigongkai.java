package Zhengfu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

public class xinxigongkai {
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
		//for (int i = 1; i < 10; i++) {

		//String url =
		// "http://xxgk.huichang.gov.cn/fgwj/qtygwj/index_"+i+".htm";
		// String url = "http://xxgk.huichang.gov.cn/xzzf/zfyj/";
		//String url = "http://xxgk.huichang.gov.cn/cjxx/zfcgyztb/";
		//String url = "http://xxgk.huichang.gov.cn/cjxx/xzsyxsf/";
		//String url = "http://xxgk.huichang.gov.cn/cjxx/zxjf/";
		//String url = "http://xxgk.huichang.gov.cn/cjxx/ss/";
		//String url = "http://xxgk.huichang.gov.cn/cjxx/jr/";
		//String url = "http://xxgk.huichang.gov.cn/cjxx/bx/";
		//String url = "http://xxgk.huichang.gov.cn/cjxx/zftzxm/";
		//String url = "http://xxgk.huichang.gov.cn/cjxx/zsxm/";
		//String url = "http://xxgk.huichang.gov.cn/gddt/tjsj/";
		//String url = "http://xxgk.huichang.gov.cn/fzgh/fzgh/";
	//	String url = "http://xxgk.huichang.gov.cn/fzgh/gzjh/";
	   //  String url = "http://xxgk.huichang.gov.cn/fgwj/qtygwj/";
		String url = "http://xxgk.huichang.gov.cn/gkxx/ld/ldxx/index_1.htm";
		Document document1 = getDoc(url);
		Elements elements1 = document1
				.select("table[width=100%]>tbody>[class=cn4]");
		// System.out.println(elements1);
		for (Element element : elements1) {
			try {
				id = element.select("td:nth-child(1)").text();
				collection = element.select("td:nth-child(2)").text();
				tit = element.select("td:nth-child(3)").html().split("\\','");
				data = element.select("td:nth-child(4)").text();
				way = element.select("td:nth-child(5)").text();
				limits = element.select("td:nth-child(6)").text();
				title = tit[2].replace("')</script>", "");

				paguer = "http://xxgk.huichang.gov.cn/gkxx/ld/ldxx/"+ tit[1];
				//paguer=url+ tit[1];
				// title=element.select("td:nth-child(3)>a").text();
				// paguer="http://xxgk.huichang.gov.cn/xgxxwj/"+element.select("td:nth-child(3)>a").attr("href");
				da = element.select("td:nth-child(4)").text().split("-");
				L = da[0];
				R = da[1];
				Document document2 = null;
				Elements elements2 = null;
				try {
					document2 = getDoc(paguer);
					// elements2 = document2.select(".table");
					elements2 = document2
							.select("table[width=1003]:nth-child(4)");
				} catch (NullPointerException en) {
					en.printStackTrace();
				}
//System.out.println(elements2);
				for (Element element2 : elements2) {
					try {
						/*
						 * head =
						 * element2.select("table[width=97%]:nth-child(1)")
						 * .html();
						 */
						head = element2
								.select(".table>table[width=97%]:nth-child(1)")
								.html();
						// content = element2.select("td[class=cn4]>p").html();
						content = element2
								.select(".table [class=cn4] p")
								.html();

						/*file = "http://xxgk.huichang.gov.cn/gkxx/ld/ldjj/"+ L + R + "/" +element2.select("[bgcolor=#EEEEEE] a")
								.attr("href");
						filename = element2.select("[bgcolor=#EEEEEE] a").text();*/
					table = element2.select(".table [class=cn4] div").html();
						/*
						 * if (element2.select("table[width=97%]:nth-child(3)")
						 * .html().contains("a")) {
						 * 
						 * "http://xxgk.huichang.gov.cn/bmgkxx/xzfb/cjxx/cjyjs/"
						 * + L + R + "/" +
						 * 
						 * file = element2.select(
						 * "table[width=97%]:nth-child(3) a").attr( "href");
						 * filename = element2.select(
						 * "table[width=97%]:nth-child(3) a").text(); }
						 */

					
			Update(collection, title, paguer, data, content,
					table, URL, date, head, file, filename, id, way,
						limits);
					

						//System.out.println(head);
						//System.out.println(content);
						//System.out.println(table);
						System.out.println(file);
						System.out.println(filename);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				System.out.println(id);
				System.out.println(collection);
				System.out.println(title);
				System.out.println(paguer);
				System.out.println(data);
				/*
				 * System.out.println(L); System.out.println(R);
				 */
				System.out.println(way);
				System.out.println(limits);
				System.out.println();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		 }
	//}
}
