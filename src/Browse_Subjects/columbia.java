package Browse_Subjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

public class columbia {
	public static void Update(String bigtitle, String link, String title,
			String paguer) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "INSERT INTO columbia_edu"
					+ "(bigtitle,link,title,paguer)" + "VALUES(?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, bigtitle);
			ps.setString(2, link);
			ps.setString(3, title);
			ps.setString(4, paguer);
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
		String userAgent = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36";
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(100000).get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return document;
	}

	public static void main(String[] args) {
		String bigtitle = "";
		String link = "";
		String page = "";
		int num = 0;
		String title = "";
		String paguer = "";

		String url = "http://academiccommons.columbia.edu/catalog/browse/subjects";
		Document document1 = getDoc(url);
		// System.out.println(doc);:nth-child(1)
		Elements els = document1.select("#browse-box>.span-7>ul>li");
		System.out.println(els.size());
		for (Element element : els) {
			try {
				bigtitle = element.select("a").text();
				link = "http://academiccommons.columbia.edu"
						+ element.select("a").attr("href").trim();
				page = element.select("li").first().ownText().replace("(", "")
						.replace(")", "");
				num = Integer.parseInt(page);
				int id = num / 10 + 2;
				for (int i = 1; i < id; i++) {
					String url2 = link + "&page=" + i;
					Elements elements2 = null;
					Document document2 = null;
					try {
						document2 = getDoc(url2);
						elements2 = document2
								.select(".results-set-list>div>div");
					} catch (NullPointerException en) {
						en.printStackTrace();
					}
					for (Element element2 : elements2) {
						try {
							title = element2.select(".index_title>a").text();
							paguer = "http://academiccommons.columbia.edu"
									+ element2.select(".index_title>a").attr(
											"href");
							Update(bigtitle, link, title, paguer);
							System.out.println(title);
							System.out.println(paguer);
							System.out.println();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
				System.out.println("大标题： "+bigtitle);
				System.out.println("大链接：  "+link);
				/*
				 * System.out.println(num); System.out.println(id);
				 */

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
