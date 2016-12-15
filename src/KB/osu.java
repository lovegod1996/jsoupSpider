package KB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

public class osu {

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
			if (document != null)
				return document;
		}
		return document;
	}
	public static void Update( String data, String title,
			String paguer, String auther) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "INSERT INTO osu"
					+ "(title,data,paguer,auther)" + "VALUES(?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, title);
			ps.setString(2, data);
			ps.setString(3, paguer);
			ps.setString(4, auther);
			
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 66820; i += 20) {
			String url = "http://kb.osu.edu/dspace/browse?order=ASC&rpp=20&sort_by=1&etal=-1&offset="
					+ i + "&type=title";
			Document document1 = getDoc(url);
			Elements elements1 = document1.select(".ds-artifact-list>li");
			String title = "";
			String auther = "";
			String paguer = "";
			String data = "";

			for (Element element : elements1) {
				if (element.html().contains("artifact-preview"))
					continue;
				title = element.select(".artifact-title").text();
				paguer = "http://kb.osu.edu"
						+ element.select(".artifact-title>a").attr("href");
				auther = element.select(".author").text();
				data = element.select(".publisher-date").text();

				//Update(data, title, paguer, auther);
				System.out.println(title);
				System.out.println(paguer);
				System.out.println(auther);
				System.out.println(data);
				System.out.println();
				System.out.println();
			}
		}

	}

}
