package json;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
public class JsonTest {
public static void main(String[] args) {
	String pageUrl = "http://wenku.baidu.com/user/interface/getpgcpublicdoclist?range=0&order=0&pn=3&uname=%E5%AD%99%E9%80%B8%E8%B1%AA&uid=4829&rn=16&sep=0&_=1447334863753";
	try {
		Document document = Jsoup
				.connect(pageUrl)
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36")
				.ignoreContentType(true).timeout(3000).get();  
	//	System.out.println(document);
		String body = document.select("body").html().replaceAll("&quot;", "\"");
		System.out.println(body);
		 List<Map<String, String>> nodes = toMap(body);
		for (Map<String, String> map : nodes) {
			System.out.print("id:"+map.get("doc_id"));
			System.out.print("   title:"+map.get("title"));
			System.out.println("   uname:"+map.get("uname"));
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
}
public static  List<Map<String, String>> toMap(String jsonString) throws JSONException {

    JSONObject jsonObject = JSONObject.fromObject(jsonString);
    JSONObject jsonObject1 = JSONObject.fromObject(jsonObject.get("data"));
    JSONArray jsons = JSONArray.fromObject(jsonObject1.get("doc_list"));
    List<Map<String, String>>  nodes = new ArrayList<Map<String, String>>();
    for (Object o : jsons)
    {
        JSONObject jsonNode = JSONObject.fromObject(o);
        Map<String, String> treeNodes = new HashMap<String, String>();
        treeNodes.put("doc_id",jsonNode.getString("doc_id"));
        treeNodes.put("title",jsonNode.getString("title"));
        treeNodes.put("uname",jsonNode.getString("uname"));
        nodes.add(treeNodes);
    }
    return nodes;
}
}
