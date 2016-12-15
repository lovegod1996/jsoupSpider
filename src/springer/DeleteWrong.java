package springer;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import utils.ReadConfig;
import utils.SQLHelper;

public class DeleteWrong {
	private static ArrayList<HashMap<String, Object>> rows;
	private static String tableName = ReadConfig.tablename;

	private static synchronized HashMap<String, Object> getType() {
		if (rows == null || rows.size() <= 0) {
			String str = "";
			str = "select id,download_url from " + tableName
					+ " where mark < 220 "+ReadConfig.orderby+" limit 1000"; // 每次取出500个没有补充详细的链接
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
			if (document != null)
				return document;
		}
		return document;
	}

	public static void fetch() {
		HashMap<String, Object> row = getType();
		String down =null;
		int id = Integer.parseInt(row.get("id").toString()); // 将id转化为int类型
		try{
		      down = row.get("download_url").toString(); // 从集合中抽取paguer
		      if(down.equals("http://link.springer.com")){
					String DeleteUrl="delete from "+tableName+" where download_url='"+down+"'";
					SQLHelper.deleteBySQL(DeleteUrl);
					System.err.println("删除此记录，无下载信息....");
				}else{
					System.out.println("此记录没有问题，id="+id);
				}
		}catch(Exception e){
			System.err.println("此记录错误！！id="+id);
			String Deleterl="delete from "+tableName+" where id='"+id+"'";
			SQLHelper.deleteBySQL(Deleterl);
		}
		
		
		
		
	}
}
