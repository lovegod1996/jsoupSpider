package baiduwenku;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import utils.ReadConfig;
import utils.SQLHelper;

public class Sunyigao_amend {
	private static ArrayList<HashMap<String, Object>> rows;
	private static String tableName = ReadConfig.tablename;

	private static synchronized HashMap<String, Object> getType() {
		if (rows == null || rows.size() <= 0) {
			String str = "";
			str = "select id,doc_id from " + tableName
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
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36";
		// System.out.println(BootURL);
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(10000).get();
//				public static void Write(String fileName, String content, Boolean flag) {
			/*	try {
					FileWriter writer = new FileWriter("D:/1.html", false);
					writer.write(document.toString());
					writer.close();
				} catch (IOException e) {
//					return;
				}*/
//				}
			} catch (Exception e) {
				System.out.println(BootURL);
				e.printStackTrace();
			}

		}
		return document;
	}

	public static void fetch() {
		HashMap<String, Object> row = getType();
		String doc_id = row.get("doc_id").toString(); // 从集合中抽取titleURL
		int id = Integer.parseInt(row.get("id").toString()); // 将id转化为int类型
		String pageurl="http://wenku.baidu.com/view/"+doc_id+".html";
		Document document=getDoc(pageurl);
		
		String isFree1 = document.select(".goods-price").text();// ￥1.00
		String isFree2 = document.select(".btn-download").text();// 1下载劵
		String isFree3 = document.select(".discribe-text>p:nth-child(2)").text();// ppt要钱这种：http://wenku.baidu.com/view/bc61079d6c175f0e7dd1377d.html
		
		
		
		
		String test1=document.select(".doc-value").text();
		System.out.println(test1);
		String test2=document.select(".owner-value").text();
		System.out.println(test2);
		
		if(judge(isFree1, isFree2, isFree3)){
			System.out.println(doc_id+"可以下载.......");
		}
		else{
			String str="delete from "+tableName+" where doc_id ='"+doc_id+"'";
			 SQLHelper.updateBySQL(str);
			 System.out.println(doc_id+"不可下载，正在删除.....");
		}
		
		System.out.println();
	}
	public static boolean judge(String isFree1 ,String isFree2,String isFree3){
		if (isFree1.contains("￥") // 看看是否需要下载劵
				|| !isFree2.replaceAll("[^\\x00-\\xff]", "").trim()
						.equals("0") || isFree3.contains("￥")) {
			// System.out.println(" 收费 --> " + data.getTitleURL());
			return false;
		} else
			return true;
	}
}
