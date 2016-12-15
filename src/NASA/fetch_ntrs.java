package NASA;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.ReadConfig;
import utils.SQLHelper;

public class fetch_ntrs {
	private static ArrayList<HashMap<String, Object>> rows;
	private static String tableName = ReadConfig.tablename;

	private static synchronized HashMap<String, Object> getType() {
		if (rows == null || rows.size() <= 0) {
			String str = "";
			str = "select id,paguer from " + tableName
					+ " where mark<10 limit 500"; // 每次取出500个没有补充详细的链接
			rows = SQLHelper.selectBySQL(str);
			if (rows.size() <= 0) {
				System.out.println("==========未取到链接=======");
				System.exit(0);
			}
		}
		HashMap<String, Object> row = rows.get(0);
		rows.remove(0);
		return row;
	}

	public static void main(String[] args) {
		for (int i = 0; i < ReadConfig.thread; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (;;) {
						fetch();
					}
				}
			}).start();

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

	public static void fetch() {
		HashMap<String, Object> row = getType();
		String paguer = row.get("paguer").toString(); // 从集合中抽取titleURL
		int id = Integer.parseInt(row.get("id").toString()); // 将id转化为int类型

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
		String down = "";
		String str = "Update " + tableName + " set mark=10 where id=" + id;
		Document document = null;
		Elements elements = null;

		try {
			document = getDoc(paguer);
			if (document == null) {
				System.out.println("无法获取！！");
				SQLHelper.updateBySQL(str);
				return;
			}
			elements = document.select("#doctable tr");
		} catch (Exception en) {
			en.printStackTrace();
		}
		for (Element element : elements) {
			try {
				if (element.html().contains("External Online Source:"))
					online = element.select("td:nth-child(2)").attr("href");
				if (element.html().contains("External Online Source:"))
					source = element.select("td:nth-child(2)>a").attr("href");
				if (element.html().contains("Author and Affiliation:"))
					auther = element.select("td:nth-child(2)").text();
				if (element.html().contains("Abstract:"))
					Abstract = element.select("td:nth-child(2)").text();
				if (element.html().contains("Publication Date:"))
					date = element.select("td:nth-child(2)").text();
				if (element.html().contains("Document ID:"))
					Did = element.select("td:nth-child(2)").text();
				if (element.html().contains("Subject Category:"))
					subject = element.select("td:nth-child(2)").text();
				if (element.html().contains("Report/Patent Number:"))
					report = element.select("td:nth-child(2)").text();
				if (element.html().contains("Document Type:"))
					Dtype = element.select("td:nth-child(2)").text();
				if (element.html().contains("Contract/Grant/Task Num:"))
					contract = element.select("td:nth-child(2)").text();
				if (element.html().contains("Meeting Sponsor:"))
					sponsor = element.select("td:nth-child(2)").text();
				if (element.html().contains("Financial Sponsor:"))
					financial = element.select("td:nth-child(2)").text();
				if (element.html().contains("Organization Source:"))
					source = element.select("td:nth-child(2)").text();
				if (element.html().contains("Description:"))
					description = element.select("td:nth-child(2)").text();
				if (element.html().contains("Distribution Limits:"))
					limits = element.select("td:nth-child(2)").text();
				if (element.html().contains("Rights:"))
					right = element.select("td:nth-child(2)").text();
				if (element.html().contains("NASA Terms:"))
					terms = element.select("td:nth-child(2)").text();
				if (element.html().contains("NIX (Document) ID:"))
					Did = element.select("td:nth-child(2)").text();
				if (element.html().contains("Other Descriptors:"))
					other = element.select("td:nth-child(2)").text();
				if (element.html().contains("Meeting Information:"))
					meet = element.select("td:nth-child(2)").text();
				if (element.html().contains("Miscellaneous Notes:"))
					note = element.select("td:nth-child(2)").text();
				if (element.html().contains("NTRS Full-Text:"))
					down = element.select("td:nth-child(2) a").attr("href");

			} catch (Exception e) {
				System.out.println("无法获取！！");
				SQLHelper.updateBySQL(str);
				e.printStackTrace();
			}
		}
		Object[] objects = { down, limits, description, financial, sponsor,
				contract, online, source, auther, Abstract, date, Did, subject,
				report, right,terms,other,meet,note,Dtype};
		String sql = "UPDATE "
				+ tableName
				+ " set download_url=?,limits=?,description=?,financial=?,sponsor=?,contract=?,online=?,source=?,auther=?,Abstract=?,date=?,Did=?,subject=?,Report=?,Right_=?,terms=?,other=?,meet=?,note=?,Dtype=?,mark=200 where id="
				+ id;
		SQLHelper.updateBySQL(sql, objects);
		System.out.println(paguer + "...........补充完整！");
		System.out.println();

		/*System.out.println("下载：  " + down);
		System.out.println(limits);
		System.out.println(description);
		System.out.println(financial);
		System.out.println(sponsor);
		System.out.println(contract);
		System.out.println("在线  " + online);
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
		System.out.println();*/

	}
}
