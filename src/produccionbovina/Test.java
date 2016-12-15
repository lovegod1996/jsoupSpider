package produccionbovina;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.SQLHelper;

public class Test {

	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		int ii = 10;
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0";
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(100000).get();
				// System.out.println(document);
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		return document;
	}
	
	public void Analyze(String collUrl, String collection, String pageurl,
			String seccoll) {
		try {
			Document document = null;
			Elements elements = null;
			Elements elements1 = null;
			String title = "";
			String tit[] = null;
			String author = "";
			String page = "";
			String down = "";
			String cambio = "";

			try {
				document = getDoc(pageurl);
			//	System.out.println(document);
				elements = document.select(".WordSection1>div[align=center]");
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(elements.size());
			elements.remove(elements.size() - 1);
			System.out.println(elements.size());
			try {
				elements1 = elements.select(".MsoNormalTable tr");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String[] pa = pageurl.split("/");
			String ml = pa[pa.length - 1];

			String url = pageurl.replace(ml, "");
			for (Element element : elements1) {
				try {
					if (!element.text().contains("PDF"))
						continue;
					tit = element.select("td:nth-child(2)").text().split("-");
					title = tit[0];
					author = tit[1];
					page = element.select("td:nth-child(3)").text();
					down = url
							+ element.select("td:nth-child(4) a").attr("href")
									.trim();
					cambio = element.select("td:nth-child(5)").text();

					Object[] objs = { collection, seccoll, pageurl, title,
							author, page, cambio, down, collUrl };
					String str = "insert into produccionbovina (collection,sertitle,pageurl,title,author,page,cambio,download_url,URL) values (?,?,?,?,?,?,?,?,?)";
					 SQLHelper.updateBySQL(str, objs); // 保存
					System.out.println(pageurl + "........正在抓取");

					System.out.println(title);
					System.out.println(author);
					System.out.println(page);
					System.out.println(down);
					System.out.println(cambio);
					System.out.println();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
		String collUrl="";
		String collection="";
		String seccoll="";
		
		String pageurl="http://www.produccionbovina.com.ar/informacion_tecnica/manejo_del_alimento/00-manejo_del_alimento_suplementacion_carga_animal.htm";
		
		Test t=new Test();
		t.Analyze(collUrl, collection, pageurl, seccoll);
	}

}
