package Mediatum;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ub_tum {

	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		int ii = 1;
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

	public static void main(String[] args) {

		String paguer = "";

		/*
		 * String id="http://mediatum.ub.tum.de/node?id=1232484";
		 * paguer="http://mediatum.ub.tum.de/node?id=600000";
		 */

		/*
		 * while(true) { if(id==paguer) break; System.out.println(paguer);
		 * Document docuemnt1=getDoc(paguer); System.out.println(docuemnt1);
		 * Elements elements1=docuemnt1.select("#portal-column-content");
		 * //System.out.println(elements1); System.out.println("结束！！！"); for
		 * (Element element : elements1) { try{
		 * //paguer="http://mediatum.ub.tum.de"
		 * +element.select("#page-nav>table>tbody>tr>td:nth-child(3)>a:nth-child(1)"
		 * ).attr("href");
		 * 
		 * //System.out.println(paguer); }catch(Exception e){
		 * e.printStackTrace(); } } }
		 */
		Document document1 = null;
		Elements elements1 = null;
		for (int i = 600000; i < 1232484; i++) {
			paguer = "http://mediatum.ub.tum.de/node?id=" + i;
			try {
				document1 = getDoc(paguer);
				if(document1==null)
					continue;
				elements1 = document1.select("#object_main");
			} catch (NullPointerException en) {
				en.printStackTrace();
			}
		}
		
			
System.out.println(elements1.size());
	}

}
