package test;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Testpage {
	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		int ii = 3;
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; rv:22.0) Gecko/20100101 Firefox/22.0";
		// System.out.println(BootURL);
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(30000).get();
			} catch (HttpStatusException htp) {
				// htp.printStackTrace();
				ii = 0;
				System.err.println("已删除，网页地址错误！！");
			} catch (Exception e) {
				System.err.println("访问失败，标记一次");
				System.out.println(BootURL);
				e.printStackTrace();
			}
		}
		System.out.println("=========读取成功========");
		return document;
	}
	
	
	public static void main(String[] args) {
	/*	String url="http://api.map.baidu.com/geocoder?output=json&location=34.584418,113.682136&key=C66C0501D0280744759A6957C42543AE38F5D540";
	
	Document doc=getDoc(url);
	System.out.println(doc.select("body"));
	String body=doc.select("body").html().replaceAll("&quot;", "\"");
	System.out.println(body);
	
	JSONObject jsonObject=JSONObject.fromObject(body);
	JSONObject resultJson=JSONObject.fromObject(jsonObject.get("result"));
	String address=resultJson.getString("formatted_address");
	System.out.println(address);
	JSONObject detail=resultJson.getJSONObject("addressComponent");
	String city=detail.getString("city");
	String district=detail.getString("district");
	String province=detail.getString("province");
	String street=detail.getString("street");
	String streetnum=detail.getString("street_number");
	
	System.out.println(city+district+province+street+streetnum);*/
	
	
	String weatherurl="http://api.map.baidu.com/telematics/v3/weather?location=%E6%96%B0%E9%83%91&output=json&ak=MPDgj92wUYvRmyaUdQs1XwCf";
	Document document=getDoc(weatherurl);
	System.out.println(document.select("body"));
	String weatherbody=document.select("body").html().replaceAll("&quot;", "\"");
	System.out.println(weatherbody);
	
	JSONObject jsonweaObject=JSONObject.fromObject(weatherbody);
	JSONArray jarrywea=JSONArray.fromObject(jsonweaObject.get("results"));
	JSONObject jsondatawea=JSONObject.fromObject(jarrywea.get(0));
	JSONArray jarrywee=JSONArray.fromObject(jsondatawea.get("weather_data"));
	JSONObject jobweather=JSONObject.fromObject(jarrywee.get(0));
	System.err.println(jobweather);
	System.out.println(jarrywee);
	System.out.println(jobweather.getString("date"));
	System.out.println(jobweather.getString("dayPictureUrl"));
	System.out.println(jobweather.getString("nightPictureUrl"));
	System.out.println(jobweather.getString("weather"));
	System.out.println(jobweather.getString("wind"));
	System.out.println(jobweather.getString("temperature"));
	
	
	}
	
}
