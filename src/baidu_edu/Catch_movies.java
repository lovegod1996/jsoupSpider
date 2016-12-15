package baidu_edu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Catch_movies {

	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		int ii = 10;
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0";
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(100000).get();
				// System.out.println(document);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return document;
	}
	
	public static void main(String[] args) {
		String url="http://v.baidu.com/edu/index";
		String Collect="";
		String CollectUrl="";
		Document docment=getDoc(url);
		//System.out.println(docment);
		Elements elements=docment.select("#sideNavBarListWrap li");
		//System.out.println(elements);
		for (Element element : elements) {
			try{
				Collect=element.select("h3").text();
				CollectUrl=element.select("h3 a").attr("href");
				String str=java.net.URLEncoder.encode(CollectUrl).replace("%3A%2F%2F", "://").replaceAll("%2F", "/").replaceAll("%2B", "+");
				Document docment1=null;
				Elements elements1=null;
				try{
					docment1=getDoc(str);
					//System.out.println(docment1);
				}catch(NullPointerException en){
					en.printStackTrace();
				}
				elements1=docment1.select(".sub-left");
				//System.out.println(elements1);
				
				System.out.println(Collect);
				System.out.println(CollectUrl);
				System.out.println(str);
				System.out.println();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	
	}
	
	
	 public static String toUtf8String(String s) {
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < s.length(); i++) {
	            char c = s.charAt(i);
	            if (c >= 0 && c <= 255) {
	                sb.append(c);
	            } else {
	                byte[] b;
	                try {
	                    b = String.valueOf(c).getBytes("utf-8");
	                } catch (Exception ex) {
	                    System.out.println(ex);
	                    b = new byte[0];
	                }
	                for (int j = 0; j < b.length; j++) {
	                    int k = b[j];
	                    if (k < 0)
	                        k += 256;
	                    sb.append("%" + Integer.toHexString(k).toUpperCase());
	                }
	            }
	        }
	        return sb.toString();
	    }
}
