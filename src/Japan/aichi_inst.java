package Japan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

public class aichi_inst {
	
	
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
	
	public static void Update(String data, String title, String down,
			String huge, String depart,String image) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "INSERT INTO aichi"
					+ "(data,title,downoad_url,image,depart,huge)" + "VALUES(?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, data);
			ps.setString(2, title);
			ps.setString(3, down);
			ps.setString(4, image);
			ps.setString(5, depart);
			ps.setString(6, huge);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}
	
	public static void main(String[] args) {
		String url="http://www.aichi-inst.jp/research/report/";
		Document docuemnt1=getDoc(url);
		Elements elements1=docuemnt1.select(".entryArea>div");
     Elements elements2=null;
		String data="";
		String[] tit=null;
      String title="";
      String down="";
      String huge="";
      String depart="";
      String image="";
		for (Element element : elements1) {
		try{
			data=element.select("h3").text();
			elements2=elements1.select(".reportList>li");
			for (Element element1 : elements2) {
				try{
					tit=element1.select("span>a").text().split("（PDF:");
					down=element1.select("span>a").attr("href");
					title=tit[0];
					huge=tit[1].replace("）", "");
					depart=element1.select("img").attr("alt");
					image=element1.select("img").attr("src");
					
					Update(data, title, down, huge, depart, image);
					System.out.println(title);
					System.out.println(down);
					System.out.println(huge);
					System.out.println(depart);
					System.out.println(image);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			System.out.println(data);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	}

}
