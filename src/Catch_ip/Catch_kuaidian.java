package Catch_ip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

public class Catch_kuaidian {

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
		String url="";
		String ip="";
		String  port="";
		String ip_port="";
		String location="";
		String lastTime="";
		for(int i=1;i<11;i++){
			 url="http://www.kuaidaili.com/proxylist/"+i+"/";
		Document document1=getDoc(url);
		Elements elements1=document1.select("#list tbody>tr");
		for (Element element : elements1) {
			try{
			     ip=element.select("td:nth-child(1)").text();
			     port=element.select("td:nth-child(2)").text();
			     location=element.select("td:nth-child(6)").text();
			     lastTime=element.select("td:nth-child(8)").text();
				 ip_port=ip+":"+port;
			     Update(ip_port, location, lastTime);
				 
			     System.out.println(ip);
			     System.out.println(port);
			     System.out.println(ip_port);
			     System.out.println(location);
			     System.out.println(lastTime);
			     System.out.println();
			}catch(Exception e){
				e.printStackTrace();
			}
		  }
		}

	}

}
