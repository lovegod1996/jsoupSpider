package biaozhun;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.ReadConfig;
import utils.SQLHelper;

public class Fetch_biaozhun {

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
	
		String title="";
		String keyword="";
		String huge="";
		String rank="";
		String number="";
		String scan="";
		String type="";
		String safe="";
		String sort="";
		String impower="";
		String language="";
		String name="";
		String name_en="";
		String replace="";
		String Abstract="";
		String dow="";
		String down="";
		String str = "Update " + tableName + " set mark=+5 where id=" + id;
		Document document1 = null;
		Elements elements1 = null;
		try{
		 document1=getDoc(paguer);
		 if (document1 == null) {
			System.out.println(paguer+"  无法获取！！");
			SQLHelper.updateBySQL(str);
				return;
			}
		 
	elements1=document1.select(".soft_info");
		}catch(Exception en){
			en.printStackTrace();
		}
	for (Element element : elements1) {
		try{
			title=element.select("h1").text();
			keyword=element.select("h4").text().replace("关键字：", "");
			Abstract=element.select(".soft_menuabout").first().text();
			dow="http://www.biaozhunw.com"+element.select(".soft_menuabout>li>a").attr("href");
			
			Elements elements2=element.select(".soft_info_all li");
			for (Element element2 : elements2) {
				try{
					if(element2.text().contains("文档大小："))
						huge=element2.select("span").text();
					if(element2.text().contains("资源评级："))
						rank=element2.select("span").text();
					if(element2.text().contains("标准编号："))
						number=element2.select("span").text();
					if(element2.text().contains("浏览次数："))
						scan=element2.select("span").text();
					if(element2.text().contains("文件类型："))
						type=element2.select("span").text();
					if(element2.text().contains("安全检测："))
						safe=element2.select("span").text();
					if(element2.text().contains("标准类别："))
						sort=element2.select("span").text();
					if(element2.text().contains("授权形式："))
						impower=element2.select("span").text();
					if(element2.text().contains("资料语言："))
						language=element2.select("span").text();
					if(element2.text().contains("标准名称："))
						name=element2.select("span").text();
					if(element2.text().contains("英文名称："))
					   name_en=element2.select("span").text();
					if(element2.text().contains("替代标准："))
						replace=element2.select("span").text();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			Document document3=null;
			Elements elements3=null;
			try{
				document3=getDoc(dow);
				elements3=document3.select("#content");
			}catch(NullPointerException en){
				en.printStackTrace();
			}
			for (Element element3 : elements3) {
				try{
					down=element3.select("a").attr("href");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			Object[] objects = { title, keyword, huge, rank, number,
					scan, type, safe, sort, impower, language, name,
					name_en, replace,Abstract,down};
			String sql = "UPDATE "
					+ tableName
					+ " set title=?,keyword=?,huge=?,rank=?,number=?,scan=?,type=?,safe=?,sort=?,impower=?,language=?,name=?,name_en=?,replace_=?,Abstract=?,download_url=?,mark=200 where id="
					+ id;
			SQLHelper.updateBySQL(sql, objects);
			System.out.println(paguer + "...........补充完整！");
			System.out.println();
		System.out.println(scan);
			
			System.out.println(huge);
			System.out.println(impower);
			System.out.println(Abstract);
			System.out.println(dow);
			System.out.println(down);
			
			System.out.println(title);
			System.out.println(keyword);
		}catch(Exception e){
			System.out.println(paguer+"  无法获取！！");
		//	SQLHelper.updateBySQL(str);
			e.printStackTrace();
		}
	}
	}
}
