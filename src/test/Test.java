package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {


		public static void main(String[] args) {
			try {
				for (int i = 1; i <= 3; i++) { 
					Document Doc = getDoc("http://down.tech.sina.com.cn/list/3_1_" + i + ".html");
					Elements All = Doc.select(" h2 > a");
					for (Element One : All) {
						String url = "http://down.tech.sina.com.cn/" + One.attr("href");
						Document Doc1 = getDoc(url);
						String name = Doc1.select("div.b_cmon > h1").text();
						String download_btn = Doc1.select("p.r > a > img").attr("src"); 
						System.out.print(name);
						System.out.println("\t" + download_btn);
						Object[] obj = {name,download_btn};
					//	insertInfo(obj);
						}
					System.out.println("----------------------------");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public static Document getDoc(String BootURL) {	
			Document document = null;
			try {
				document = Jsoup.connect(BootURL).userAgent("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)").timeout(100000).get();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return document;
		}
		public static void insertInfo(Object[] obj)	throws  ClassNotFoundException{
			
			Connection conn=null;
		     PreparedStatement ps=null;    
		     String sql = "INSERT INTO software (name, download_btn) VALUES (?,?)";
			try {
				Class.forName("com.mysql.jdbc.Driver");
			    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo1023", "root", "12345678");
				ps =conn. prepareStatement(sql);
				ps.setString(1, obj[0].toString());
				ps.setString(2, obj[1].toString());
				ps.execute();
				ps.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	

}
