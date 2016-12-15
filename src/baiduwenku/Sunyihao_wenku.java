package baiduwenku;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import utils.SQLHelper;

public class Sunyihao_wenku {

	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		int ii = 10;
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0";
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.ignoreContentType(true).timeout(100000).get();
				// System.out.println(document);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return document;
	}

	public static List<Map<String, String>> toMap(String jsonString)
			throws JSONException {

		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		JSONObject jsonObject1 = JSONObject.fromObject(jsonObject.get("data"));
		JSONArray jsons = JSONArray.fromObject(jsonObject1.get("doc_list"));
		List<Map<String, String>> nodes = new ArrayList<Map<String, String>>();
		for (Object o : jsons) {
			JSONObject jsonNode = JSONObject.fromObject(o);
			Map<String, String> treeNodes = new HashMap<String, String>();
			treeNodes.put("doc_id", jsonNode.getString("doc_id"));
			treeNodes.put("title", jsonNode.getString("title"));
			treeNodes.put("uname", jsonNode.getString("uname"));
			treeNodes.put("tag_str", jsonNode.getString("tag_str"));
			treeNodes.put("page", jsonNode.getString("page"));
			treeNodes.put("download_count",
					jsonNode.getString("download_count"));
			treeNodes.put("create_time", jsonNode.getString("create_time"));

			nodes.add(treeNodes);
		}
		return nodes;
	}

	public static boolean judgeDate(String date) {
		String[] str = date.split("-");
		int year = Integer.parseInt(str[0]);
		if (str[1].startsWith("0"))
			str[1].replace("0", "");
		int month = Integer.parseInt(str[1]);
		if (year >= 2013 || (year == 2012 && month > 5))
			return true;
		return false;
	}

	public static String ChangeDate(String num) {
		// 将lang型的时期转化为yyyy-MM-dd型
		Date date = new Date(Long.parseLong(num + "000"));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = df.format(date);
		// System.out.println(dateStr);
		return dateStr;

	}

	public static boolean judge(String isFree1, String isFree2, String isFree3) {
		if (isFree1.contains("￥") // 看看是否需要下载劵
				|| !isFree2.replaceAll("[^\\x00-\\xff]", "").trim().equals("0")
				|| isFree3.contains("￥")) {
			// System.out.println(" 收费 --> " + data.getTitleURL());
			return false;
		} else
			return true;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for (int i = 1; i <= 172; i++) {
			String Url = "http://wenku.baidu.com/user/interface/getpgcpublicdoclist?range=0&order=0&pn="+i+"&uname=hc_cxq&uid=3428225&rn=16&sep=0&_=1447733993518";
			String url = java.net.URLEncoder.encode(Url)
					.replace("%3A%2F%2F", "://").replaceAll("%2F", "/")
					.replaceAll("%3D", "=").replaceAll("%3F", "?")
					.replace("%26", "&");
			String body = getDoc(url).select("body").html()
					.replaceAll("&quot;", "\"");
			List<Map<String, String>> nodes = toMap(body);
			for (Map<String, String> map : nodes) {
				String doc_id = map.get("doc_id");
				String title = map.get("title");
				String author = map.get("uname");
				String tag_str = map.get("tag_str");
				String page = map.get("page");
				String download_count = map.get("download_count");
				String create_time = map.get("create_time");
				String date = ChangeDate(create_time);
				String pageurl = "http://wenku.baidu.com/view/" + doc_id
						+ ".html";

				/*
				 * 判断是否可以免费下载
				 */
				Document document = getDoc(pageurl);
				String isFree1 = document.select(".goods-price").text();// ￥1.00
				String isFree2 = document.select(".btn-download").text();// 1下载劵
				String isFree3 = document.select(
						".discribe-text>p:nth-child(2)").text();// ppt要钱这种：http://wenku.baidu.com/view/bc61079d6c175f0e7dd1377d.html

				if (judge(isFree1, isFree2, isFree3)) {
					if (judgeDate(date)) {
						Object[] objs = { title, pageurl, date, page,
								download_count, author, doc_id };
						String str = "insert into wenku_hdx (title,pageurl,date,pages,downloads,author,docid) values (?,?,?,?,?,?,?)";
					 SQLHelper.updateBySQL(str, objs); // 保存
						System.out.println(doc_id + "正在抓取....");
					} else {
						System.out.println(doc_id + "超过时间，不能下载....");
					}
				} else {
					System.out.println(doc_id + "需要付费，不能下载....");
				}

				/*
				 * Object[] objs = { doc_id, title, author, tag_str }; String
				 * str =
				 * "insert into sunyihao (doc_id,title,uname,tag_str) values (?,?,?,?)"
				 * ; // SQLHelper.updateBySQL(str, objs); // 保存
				 */}
			System.out.println("这是第" + i + "页");
		}
	}

}
