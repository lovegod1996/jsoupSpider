package Catch_ip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

public class youdaili {

	public static void Update(String ip_port, String location, String lastTime) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "insert into ips  (ip_port,location,lastTime) values(?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, ip_port);
			ps.setString(2, location);
			ps.setString(3, lastTime);
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
		String[] i = null;
		String[] ip = null;
		String ip_port = "";
		String location = "";
		String lastTime = "";
		String url = "http://www.youdaili.net/Daili/http/3568.html";
		Document docuemnt = getDoc(url);
		Elements elements = docuemnt.select(".cont_font");
		for (Element element : elements) {
			try {
				i = element.select("p").first().ownText().split("@");
				for (int m = 0; m < 150; m++) {
					ip = i[m].split(" ");
					ip_port = ip[ip.length - 1];
					Update(ip_port, location, lastTime);
					System.out.println(ip_port);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
