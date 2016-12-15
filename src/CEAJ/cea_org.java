package CEAJ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

public class cea_org {
	public static void Update(String title, String titleURL, String auther,
			String juan, String page, String down, String college,
			String college_en, String title_en, String auther_en,
			String Abstract, String Abstract_en, String Keyword, String key) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "INSERT INTO url21"
					+ "(title,titleURL,author,college,title_en,author_en,college_en,abstract,keyword,abstract_en,keyword_en,juan,page,download_url)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, title);
			ps.setString(2, titleURL);
			ps.setString(3, auther);
			ps.setString(4, college);
			ps.setString(5, title_en);
			ps.setString(6, auther_en);
			ps.setString(7, college_en);
			ps.setString(8, Abstract);
			ps.setString(9, Keyword);
			ps.setString(10, Abstract_en);
			ps.setString(11, key);
			ps.setString(12, juan);
			ps.setString(13, page);
			ps.setString(14, down);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}

	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		int ii = 10;
		String userAgent = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36";
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(100000).get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return document;
	}

	public static void main(String[] args) {
		int m = 1;
		String url = "http://cea.ceaj.org/CN/article/showOldVolumn.do";
		Document document1 = getDoc(url);
		Elements elements1 = document1.select("table table >tbody>tr:nth-child(6) tr>td>a");
		for (Element element : elements1) {
			try {
				if(!element.text().contains("No"))
					continue;
				String link = element.select("a").attr("href");
				String paguer = null;
				if (!link.equals("")) {
					paguer = "http://cea.ceaj.org/CN"
							+ element.select("a").attr("href").trim()
									.replace("..", "");
				} else {
					paguer = null;
				}
				
				String url2 = paguer;
				Document document2 = null;
				Elements elements2 = null;

				try {
					document2 = getDoc(url2);
					elements2 = document2
							.select("[width=98%]>tbody>tr>td>table:not([height=122])");
				} catch (NullPointerException en) {
					en.printStackTrace();
				}
				Elements elements3 = elements2
						.select("tbody>tr:nth-child(2)>td>table");
				for (Element element2 : elements3) {
					try {
						String auther = element2.select("tr:nth-child(1)>td")
								.last().text();
						String title = element2.select("tr:nth-child(2)>td")
								.last().text();
						String[] ju = element2
								.select("tr:nth-child(3)>td:nth-child(3)")
								.first().ownText().split(":");
						String juan = ju[0];
						String j = ju[1];
						String[] L = j.split("\\[");
						String page = L[0];
						String R = L[1];
						String titleURL = "http://cea.ceaj.org/CN"
								+ element2
										.select("tr:nth-child(3)>td:nth-child(3)>a")
										.first().attr("href").replace("..", "");
						String down = "http://cea.ceaj.org/CN"
								+ element2
										.select("tr:nth-child(3)>td:nth-child(3)>a")
										.last().attr("href").replace("..", "");

						String url3 = titleURL;
						Document document3 = null;
						;
						Elements elements4 = null;
						try {
							document3 = getDoc(url3);
							elements4 = document3
									.select("body>table:nth-child(2)>tbody");
						} catch (NullPointerException en) {
							en.printStackTrace();
						}
						// System.out.println(elements4);
						for (Element element3 : elements4) {
							try {
								String college = element3
										.select("table:nth-child(2)>tbody>tr:nth-child(5)>td")
										.text();
								String title_en = element3
										.select("table:nth-child(2)>tbody>tr:nth-child(7)>td")
										.text();
								String college_en = element3
										.select("table:nth-child(2)>tbody>tr:nth-child(9)>td")
										.text();
								String auther_en = element3
										.select("table:nth-child(2)>tbody>tr:nth-child(8)>td")
										.text();
								String Abstract = element3
										.select("#abstract_tab_content>[width=100%]>tbody>tr:nth-child(2)>td")
										.get(0).text().replace("摘要", "");
								String Keyword = element3
										.select("#abstract_tab_content>[width=100%]>tbody>tr")
										.get(2).text().replace("关键词：", "");
								String Abstract_en = element3
										.select("#abstract_tab_content>[width=100%]>tbody>tr")
										.get(3).text().replace("Abstract：", "");
								String key = element3
										.select("#abstract_tab_content>[width=100%]>tbody>tr")
										.get(4).text()
										.replace("Key words：", "");

								Update(title, titleURL, auther, juan, page,
										down, college, college_en, title_en,
										auther_en, Abstract, Abstract_en,
										Keyword, key);
								System.out.println("学校： " + college);
								System.out.println(college_en);
								System.out.println("标题（英）： " + title_en);
								System.out.println("作者（英）： " + auther_en);
								System.out.println("描述： " + Abstract);
								System.out.println(Abstract_en);
								System.out.println("关键词： " + Keyword);
								System.out.println("关:   " + key);
								System.out.println(m++);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						System.out.println("作者： " + auther);
						System.out.println("标题：  " + title);
						System.out.println("卷：  " + juan);
						System.out.println("页：" + page);
						System.out.println("链接： " + titleURL);
						System.out.println("PDF：" + down);
						System.out.println();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("链接：  " + paguer);
		
				System.out.println();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
