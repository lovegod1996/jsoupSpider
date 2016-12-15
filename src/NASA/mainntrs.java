package NASA;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import Jsoup.JdbcUtils;

import com.mysql.jdbc.PreparedStatement;

public class mainntrs {
	public static void Update(Data data) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "UPDATE ntrs SET"
					+ " Did=?,auther=?,Abstract=?,date=?,source=?,`subject`=?,Report=?,Dtype=?,meet=?,sponsor=?,contract=?,financial=?,description=?,limits=?,Right_=?,terms=?,other=?,note=?,`online`=?,mark=200 "
					+ " WHERE paguer='"+data.getpaguer()+"'";
			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, data.getDid());
			ps.setString(2, data.getauther());
			ps.setString(3, data.getAbstract());
			ps.setString(4, data.getdate());
			ps.setString(5, data.getsource());
			ps.setString(6, data.getsubject());
			ps.setString(7, data.getreport());
			ps.setString(8, data.getDtype());
			ps.setString(9, data.getmeet());
			ps.setString(10, data.getsponsor());
			ps.setString(11, data.getcontract());
			ps.setString(12, data.getfinancial());
			ps.setString(13, data.getdescription());
			ps.setString(14, data.getlimits());
			ps.setString(15, data.getright());
			ps.setString(16, data.getterms());
			ps.setString(17, data.getother());
			ps.setString(18, data.getnote());
			ps.setString(19, data.getonline());
			System.out.println(sql);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			ERROR(data.getpaguer());
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}

	private static void ERROR(String url) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "UPDATE ntrs SET mark=mark+1 WHERE paguer='" + url
					+ "'";
			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}

	public static void main1(String paguer, Data data) throws IOException {
		Document doc = null;
		Elements els = null;
		int k = 0;
		try {
			doc = getDoc(paguer);
			els = doc.select("#doctable tr");
			Detail(els, data);
		} catch (Exception en) {
			en.printStackTrace();
			if (k < 15)
				doc = getDoc(paguer);
			else
				return;
		} finally {
			k++;
		}
		data.setDid("");
		data.setauther("");
		data.setAbstract("");
		data.setdate("");
		data.setsource("");
		data.setsubject("");
		data.setreport("");
		data.setDtype("");
		data.setmeet("");
		data.setsponsor("");
		data.setcontract("");
		data.setfinancial("");
		data.setdescription("");
		data.setlimits("");
		data.setright("");
		data.setterms("");
		data.setother("");
		data.setnote("");
		data.setonline("");
	}

	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		String userAgent = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36";
		try {
			document = Jsoup.connect(BootURL).userAgent(userAgent)
					.timeout(100000).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;

	}

	public static void Detail(Elements els, Data data) {
		String title = "";
		String paguer = "";
		
		String Did = "";
		
		String auther = "";
		String Abstract = "";
		String date = "";
		
		String source = null;
		String subject = "";
		String report = "";
		String Dtype = "";
		String meet = "";
		String sponsor = "";
		String contract = "";
		String financial = "";
		String description = "";
		String limits = "";
		String right = "";
		String terms = "";
		String other = "";
		String note = "";
		String online = "";
		for (Element element4 : els) {
			try {
				if (element4.html().contains("External Online Source:"))
					online = element4.select("td:nth-child(2)").attr("href");
				if (element4.html().contains("External Online Source:"))
					source = element4.select("td:nth-child(2)>a").attr("href");
				if (element4.html().contains("Author and Affiliation:"))
					auther = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("Abstract:"))
					Abstract = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("Publication Date:"))
					date = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("Document ID:"))
					Did = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("Subject Category:"))
					subject = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("Report/Patent Number:"))
					report = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("Document Type:"))
					Dtype = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("Contract/Grant/Task Num:"))
					contract = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("Meeting Sponsor:"))
					sponsor = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("Financial Sponsor:"))
					financial = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("Organization Source:"))
					source = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("Description:"))
					description = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("Distribution Limits:"))
					limits = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("Rights:"))
					right = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("NASA Terms:"))
					terms = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("NIX (Document) ID:"))
					Did = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("Other Descriptors:"))
					other = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("Meeting Information:"))
					meet = element4.select("td:nth-child(2)").text();
				if (element4.html().contains("Miscellaneous Notes:"))
					note = element4.select("td:nth-child(2)").text();
				
				else
					continue;
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		data.setDid(Did);
		data.setauther(auther);
		data.setAbstract(Abstract);
		data.setdate(date);
		data.setsource(source);
		data.setsubject(subject);
		data.setreport(report);
		data.setDtype(Dtype);
		data.setmeet(meet);
		data.setsponsor(sponsor);
		data.setcontract(contract);
		data.setfinancial(financial);
		data.setdescription(description);
		data.setlimits(limits);
		data.setright(right);
		data.setterms(terms);
		data.setother(other);
		data.setnote(note);
		data.setonline(online);

		 System.out.println(limits);
		  System.out.println(description);
		  System.out.println(financial);
		  System.out.println(sponsor);
		  System.out.println(contract);
		  System.out.println(online);
		  System.out.println(source);
		  System.out.println(auther);
		  System.out.println(Abstract);
		  System.out.println(date);
		  System.out.println(Did);
		  System.out.println(subject);
		  System.out.println(report);
		  System.out.println(right);
		  System.out.println(terms);
		  System.out.println(other);
		  System.out.println(meet);
		  System.out.println(note);
		  System.out.println(Dtype);
		
		  Update(data);
		
		

	}

}
