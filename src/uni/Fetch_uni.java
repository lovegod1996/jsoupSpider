package uni;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.ReadConfig;
import utils.SQLHelper;

public class Fetch_uni {
	private static ArrayList<HashMap<String, Object>> rows;
	private static String tableName = ReadConfig.tablename;

	private static synchronized HashMap<String, Object> getType() {
		if (rows == null || rows.size() <= 0) {
			String str = "";
			str = "select id,pageurl from " + tableName
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
		String pageurl = row.get("pageurl").toString(); // 从集合中抽取titleURL
		int id = Integer.parseInt(row.get("id").toString()); // 将id转化为int类型
		
		
		String type="";
		String URL="";
		String data="";
		String summary ="";
		String Published="";
		String Pubmed="";
		String DOI ="";
		String Examination ="";
		String note="";
		String Subject="";
		String Keywords="";
		String License="";
		String Checksum="";
		String down="";
		String Collection="";
		String author="";
		String str = "Update " + tableName + " set mark=10 where id=" + id;
		Document document = null;
		Elements elements = null;
		try {
			document = getDoc(pageurl);
			if (document == null) {
				System.out.println("无法获取！！");
				SQLHelper.updateBySQL(str);
				return;
			}
		} catch (Exception en) {
			en.printStackTrace();
		}
		elements=document.select("#aspect_artifactbrowser_ItemViewer_div_item-view");
		
		Elements elements1=elements.select(".item-summary-view-metadata tr");
	//	System.out.println(elements1.size());
		for (Element element : elements1) {
			try{
				//System.out.println(element);
				//System.out.println();
				if(element.html().contains("Publikationstyp:"))
					type=element.select("td:nth-child(2)").text();
				if(element.html().contains("URI"))
					URL=element.select("td:nth-child(2)").text();
				if(element.html().contains("Autor/innen"))
					author=element.select("td:nth-child(2)").text();
				if(element.html().contains("Erscheinungsjahr/-datum"))
					data=element.select("td:nth-child(2)").text();
				if(element.html().contains("Zusammenfassung in einer weiteren Sprache"))
					summary=element.select("td:nth-child(2)").text();
				if(element.html().contains("Zusammenfassung"))
					summary=element.select("td:nth-child(2)").text();
				if(element.html().contains("Pr&uuml;fungsdatum (bei Dissertationen)"))
					Examination=element.select("td:nth-child(2)").text();
				if(element.html().contains("Hochschulschriftenvermerk"))
					note=element.select("td:nth-child(2)").text();
				if(element.html().contains("Fachgebiet (DDC):"))
					Subject=element.select("td:nth-child(2)").text();
				if(element.html().contains("Schlagw&ouml;rter"))
					Keywords=element.select("td:nth-child(2)").text();
				if(element.html().contains("Link zur Lizenz"))
					License=element.select("td:nth-child(2)>a").attr("href");
				if(element.html().contains("Erschienen in"))
					Published=element.select("td:nth-child(2)").text();
				if(element.html().contains("Pubmed ID"))
					Pubmed=element.select("td:nth-child(2)").text();
				if(element.html().contains("DOI"))
					DOI=element.select("td:nth-child(2)").text();
			}catch(Exception e){
				System.out.println("无法获取！！");
				SQLHelper.updateBySQL(str);
				e.printStackTrace();
			}
		}
		Elements elements2=elements.select(".file-list");
		for (Element element1 : elements2) {
			try{
				down="http://kops.uni-konstanz.de"+element1.select(".thumbnail-wrapper>a").attr("href");
				Checksum=element1.select(".file-list>div:nth-child(2)>span:nth-child(2)").text().replace("MD5:", "");
			}catch(Exception e){
				System.out.println("无法获取！！");
				SQLHelper.updateBySQL(str);
				e.printStackTrace();
			}
		}
		Collection=elements.select(".ds-referenceSet-list").text();
		
		
		
		
		Object[] objects = { type, URL, data, summary, Published,
				Pubmed, DOI, Examination, note, Subject, Keywords, License, Checksum,
				down, Collection,author};
		String sql = "UPDATE "
				+ tableName
				+ " set type=?,URL=?,data=?,summary=?,Published=?,Pubmed=?,DOI=?,Examination=?,note=?,Subject=?,Keywords=?,License=?,Checksum=?,download_url=?,Collection=?,author_=?,mark=200 where id="
				+ id;
		SQLHelper.updateBySQL(sql, objects);
		System.out.println(pageurl + "...........补充完整！");
		System.out.println();

		//System.out.println(Checksum);
		/*System.out.println(Collection);
		System.out.println(down);
		System.out.println(Checksum);
		System.out.println(type);
		System.out.println(author);
		System.out.println(URL);
		System.out.println(data);
		System.out.println(DOI);
		System.out.println(summary);
		System.out.println(Published);
		System.out.println(Keywords);
		System.out.println(Examination);
		System.out.println(pageurl);
		System.out.println("啊舒服撒股份撒旦");*/
	}
}
