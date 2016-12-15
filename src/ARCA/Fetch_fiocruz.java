package ARCA;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.ReadConfig;
import utils.SQLHelper;

public class Fetch_fiocruz {
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
			if (document != null)
				return document;
		}
		return document;
	}

	public static void fetch() {

		HashMap<String, Object> row = getType();
		String paguer = row.get("paguer").toString(); // 从集合中抽取paguer
		int id = Integer.parseInt(row.get("id").toString()); // 将id转化为int类型
		// System.out.println(paguer);
		Document document = null;
		Elements elements1 = null;
		Elements elements2 = null;
		String down = "";
		String titleAbout = "";
		String autherAbout = "";
		String Contact = "";
		String Abstract = "";
		String other = "";
		String reference = "";
		String keyWord = "";
		String doi = "";
		String issn = "";
		String colletion = "";
		String source = "";
		String Description = "";
		String depart = "";

		try {
			document = getDoc(paguer);
			elements1 = document.select(".pageContents");
		} catch (Exception e) {
			System.out.println("获取失败！！");
			e.printStackTrace();
		}
		down = elements1.select("center>object").attr("data");
		elements2 = elements1.select("center>table>tbody>tr");
		// System.out.println(elements2);
		for (Element element : elements2) {
			try {
				// System.out.println(element);

				if (element.html().contains("Título Alternativo:"))
					titleAbout = element.select("td:nth-child(2)").text();
				if (element.html().contains("Other titles:"))
					titleAbout = titleAbout
							+ element.select("td:nth-child(2)").text();

				if (element.html().contains("Abstract:"))
					Abstract = element.select("td:nth-child(2)").text();
				if (element.html().contains("Resumo:"))
					Abstract = element.select("td:nth-child(2)").text();
				if (element.html().contains("Resumen: "))
					Abstract = Abstract
							+ element.select("td:nth-child(2)").text();
				if (element.html().contains("Abstract in Spanish: "))
					Abstract = Abstract
							+ element.select("td:nth-child(2)").text();

				if (element.html().contains("Source:"))
					source = source + element.select("td:nth-child(2)").text();

				if (element.html().contains("Department:"))
					depart = depart + element.select("td:nth-child(2)").text();
				if (element.html().contains("Thesis Institution:"))
					depart = depart + element.select("td:nth-child(2)").text();
				if (element.html().contains("Thesis Place:"))
					depart = depart + element.select("td:nth-child(2)").text();

				if (element.html().contains("Palavras-Chave:"))
					other = element.select("td:nth-child(2)").text();
				if (element.html().contains("Descritores DeCS:"))
					other = element.select("td:nth-child(2)").text();
				if (element.html().contains("Palabras-Clave:"))
					other = element.select("td:nth-child(2)").text();
				if (element.html().contains("Source:"))
					other = element.select("td:nth-child(2)").text();

				if (element.html().contains("Affilliation:"))
					Contact = element.select("td:nth-child(2)").text();

				if (element.html().contains("Keywords:"))
					keyWord = element.select("td:nth-child(2)").text();
				if (element.html().contains("Keywords in English:"))
					keyWord = element.select("td:nth-child(2)").text();
				if (element.html().contains("Keywords in Portuguese:"))
					keyWord = keyWord
							+ element.select("td:nth-child(2)").text();

				if (element.html().contains("Description:"))
					Description = Description
							+ element.select("td:nth-child(2)").text();
				if (element.html().contains("Adviser:"))
					Description = Description
							+ element.select("td:nth-child(2)").text();

				if (element.html().contains("DOI:"))
					doi = element.select("td:nth-child(2)").text();

				if (element.html().contains("ISSN:"))
					issn = issn + element.select("td:nth-child(2)").text();
				if (element.html().contains("Issue Date:"))
					issn = issn + element.select("td:nth-child(2)").text();

				if (element.html().contains("Aparece nas coleções:"))
					colletion = element.select("td:nth-child(2)").text();
				if (element.html().contains("Appears in Collections:"))
					colletion = element.select("td:nth-child(2)").text();

			} catch (Exception e) {
				String str = "Update " + tableName
						+ " set mark=mark+1 where id=" + id;
				 SQLHelper.updateBySQL(str);
				e.printStackTrace();
			}
		}
		Object[] objects = { Abstract, titleAbout, Description, Contact, other,
				depart,doi, keyWord, issn, down, colletion,source };
		String sql = "UPDATE "
				+ tableName
				+ " set Abstract=?,titleAbout=?,Description=?,Contact=?,other=?,depart=?,doi=?,Keyword=?,ISSN=?,download_url=?,colletion=?,source=?,mark=200 where id="
				+ id;
		SQLHelper.updateBySQL(sql, objects);
		System.out.println(paguer + "补充完整！");
		System.out.println();
		/*
		 * System.out.println("描述Abstract   " + Abstract);
		 * System.out.println("题目相关titleAbout  " + titleAbout);
		 * System.out.println("作者相关autherAbout   " + autherAbout);
		 * System.out.println("联系Contact    " + Contact);
		 * System.out.println("其他other    " + other);
		 * System.out.println("文献reference" + reference);
		 * System.out.println("DOI  " + doi);
		 * System.out.println("关键词keyWord    " + keyWord);
		 * System.out.println("ISSN    " + issn);
		 */
		/*System.out.println("描述Abstract   " + Abstract);
		System.out.println("题目相关titleAbout  " + titleAbout);
		System.out.println("联系Contact    " + Contact);
		System.out.println("其他other    " + other);
		System.out.println("DOI  " + doi);
		System.out.println("关键词keyWord    " + keyWord);
		System.out.println("ISSN    " + issn);
		System.out.println("集合  " + colletion);
		System.out.println("描绘  " + Description);
		System.out.println("部门   " + depart);
		System.out.println("源  " + source);
		System.out.println("下载       " + down);*/

	}

}
