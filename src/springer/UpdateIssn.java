package springer;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.ReadConfig;
import utils.SQLHelper;

public class UpdateIssn {
	private static ArrayList<HashMap<String, Object>> rows;
	private static String tableName = ReadConfig.tablename;

	private static synchronized HashMap<String, Object> getType() {
		if (rows == null || rows.size() <= 0) {
			String str = "";
			str = "select id,pageurl from " + tableName
					+ " where mark<200 "+ReadConfig.orderby+" limit 1000"; // 每次取出500个没有补充详细的链接
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
		String DeleteUrl="delete from "+tableName+" where pageurl='"+BootURL+"'";
		// System.out.println(BootURL);
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(10000).get();
			} catch (Exception e) {
				System.out.println(BootURL);
				e.printStackTrace();
				SQLHelper.deleteBySQL(DeleteUrl);
		       System.err.println("已删除，网页地址错误！！");
			}
			if (document != null)
				return document;
		}
		return document;
	}

	public static void fetch() {

		HashMap<String, Object> row = getType();
		String pageurl = row.get("pageurl").toString(); // 从集合中抽取paguer
		int id = Integer.parseInt(row.get("id").toString()); // 将id转化为int类型
	
		String down="";
		String pissn="";
		String setMark="Update " + tableName + " set mark=+10 where id=" + id;
		String DeleteUrl="delete from "+tableName+" where pageurl='"+pageurl+"'";
		String down_url=null;
	Document docment=null;
	try{
		docment=getDoc(pageurl);
	}catch(Exception en){
		en.printStackTrace();
		SQLHelper.updateBySQL(setMark);
		
	}
	Elements elements=docment.select("#article");
	for (Element element : elements) {
		try{
			down_url=element.select("#abstract-actions-download-article-pdf-link").attr("href").trim();
			if(down_url==""){
				SQLHelper.deleteBySQL(DeleteUrl);
				System.err.println("删除此记录，无下载信息....");
				continue;
			}
			
			pissn=element.select("#abstract-about-issn").text();
		
			
			
			Object[] objs = {pissn};
			//String str = "insert into springer_hdx (download_url,Abstract,journal,date,doi,pissn,oissn,publisher,topics,keyword,authors,affili,references_) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			String sql = "UPDATE "
					+ tableName
					+ " set pissn=?,mark=250 where id="
					+ id;
			SQLHelper.updateBySQL(sql, objs); // 保存
			System.out.println(pissn + "更新完成....");
			//System.out.println(down_url);
			//System.out.println(doi);
			//System.out.println(down);
			//System.out.println(Abstract);
			//System.out.println(pageurl);
		}catch(Exception e){
			e.printStackTrace();
			SQLHelper.updateBySQL(setMark);
		}
	}
	
	}
}
