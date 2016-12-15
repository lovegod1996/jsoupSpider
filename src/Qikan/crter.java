package Qikan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

public class crter {
	public static void Update(String title, String titleURL, String auther,
			String juan, String page, String down, String college,
			String college_en, String title_en, String auther_en,
			String Abstract, String Abstract_en, String keyword,
			String keyword_en, String doi, String date, String jijin,
			String tongxun, String authordetail, String reference, String pacs,
			String image) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "INSERT INTO url34"
					+ "(title,titleURL,author,college,title_en,author_en,college_en,abstract,keyword,abstract_en,keyword_en,juan,page,download_url,doi,date,jijin,tongxun,authordetail,cankaowenxian,ZTFLH,image)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, title);
			ps.setString(2, titleURL);
			ps.setString(3, auther);
			ps.setString(4, college);
			ps.setString(5, title_en);
			ps.setString(6, auther_en);
			ps.setString(7, college_en);
			ps.setString(8, Abstract);
			ps.setString(9, keyword);
			ps.setString(10, Abstract_en);
			ps.setString(11, keyword_en);
			ps.setString(12, juan);
			ps.setString(13, page);
			ps.setString(14, down);
			ps.setString(15, doi);
			ps.setString(16, date);
			ps.setString(17, jijin);
			ps.setString(18, tongxun);
			ps.setString(19, authordetail);
			ps.setString(20, reference);
			ps.setString(21, pacs);
			ps.setString(22, image);
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
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240";
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
		String url = "http://www.crter.org/CN/article/showOldVolumn.do";
		Document document1 = getDoc(url);
		Elements elements1 = document1
				.select("table table >tbody>tr:nth-child(5) tr>td");
		String date = "";
		String link = "";
		String paguer = "";
		String title = "";
		String titleURL = "";
		String auther = "";
		String juan = "";
		String[] ju = null;
		String[] y = null;
		String page = "";
		String image = null;
		String down = null;
		String college = "";
		String title_en = "";
		String auther_en = "";
		String college_en = "";
		String reference = "";
		String Abstract = "";
		String keyword = "";
		String Abstract_en = "";
		String keyword_en = "";
		String pacs = "";
		String jijin = "";
		String doi = "";
		String tongxun = "";
		String email = "";
		String authordetail = "";
		// System.out.println(elements1);
		for (Element element : elements1) {
			try {
				if (!element.text().contains("No"))
					continue;
				link = element.select("a").attr("href");

				if (!link.equals("")) {
					paguer = "http://www.crter.org/CN"
							+ element.select("a").attr("href").trim()
									.replace("..", "");
				} else {
					paguer = null;
				}
				date = element.select("b").text();

				Document document2 = null;
				Elements elements2 = null;
				try {
					document2 = getDoc(paguer);
					elements2 = document2
							.select("table:nth-child(3) tr:nth-child(1) table[class=td4]");
				} catch (NullPointerException en) {
					en.printStackTrace();
				}
				// System.out.println(elements2.size());
				for (Element element2 : elements2) {
					try {
						image = "http://www.crter.org"
								+ element2.select("td:nth-child(1) img")
										.attr("src").replace("../..", "");
						title = element2
								.select("td[width=585]>table:nth-child(1)>tbody>tr:nth-child(1)")
								.text();
						titleURL = "http://www.crter.org/CN"
								+ element2
										.select("td[width=585]>table:nth-child(1)>tbody>tr:nth-child(1) a")
										.attr("href").replace("..", "");
						ju = element2
								.select("td[width=585]>table:nth-child(1)>tbody>tr:nth-child(2)")
								.text().split("[J].");
						auther = ju[0].replace("[", "").replace(title, "");
						y = ju[1].split(":");
						juan = y[0].replace(". 中国组织工程研究，", "");
						page = y[1].replace(".", "");
						down = "http://www.crter.org/CN"
								+ element2
										.select("td[width=585]>table:nth-child(2) td[width=210] a")
										.attr("href").replace("..", "");

						/*Document document3 = null;
						Elements elements3 = null;
						try {
							document3 = getDoc(titleURL);
							elements3 = document3
									.select("body>table[width=1002]>tbody>tr>td");
						} catch (NullPointerException en) {
							en.printStackTrace();
						}
						for (Element element3 : elements3) {
							try {
								college = element3
										.select("td>table:nth-child(2)>tbody>tr:nth-child(5)")
										.text();
								title_en = element3
										.select("td>table:nth-child(2)>tbody>tr:nth-child(7)")
										.text();
								auther_en = element3
										.select("td>table:nth-child(2)>tbody>tr:nth-child(8)")
										.text();
								try {
									college_en = element3
											.select("td>table:nth-child(2)>tbody>tr:nth-child(9)")
											.text();
								} catch (Exception e) {
								}
								Elements elements4 = elements3
										.select("#reference_tab_content table>tbody>tr");
								if (elements4 != null)
									for (Element element4 : elements4) {
										reference += element4.text() + "<br>";
									}
								Elements elements5 = elements3
										.select("#abstract_tab_content table>tbody>tr");
								for (Element element5 : elements5) {
									try {
										if (element5.html().contains("摘要"))
											Abstract = element5.select("p")
													.text();
										if (element5.html().contains("关键词"))
											keyword = element5.select("a")
													.text();
										if (element5.html()
												.contains("Abstract"))
											Abstract_en = element5.select(
													"span>p").text();
										if (element5.html().contains("中图分类号:"))
											pacs = element5.select(
													"td:nth-child(2)").text();
										if (element5.html().contains("基金资助:"))
											jijin = element5.select("td")
													.first().ownText();
										if (element5.html().contains("作者简介"))
											authordetail = element5
													.select("span").first()
													.ownText()
													.replace(": ", "");
										if (element5.html().contains("通讯作者:"))
											tongxun = element5.select("span")
													.first().ownText();
										if (element5.html().contains(
												"Key words"))
											keyword_en = element5.select("a")
													.text();
									} catch (Exception e) {
										e.printStackTrace();
									}

								}
								Update(title, titleURL, auther, juan, page,
										down, college, college_en, title_en,
										auther_en, Abstract, Abstract_en,
										keyword, keyword_en, doi, date, jijin,
										tongxun, authordetail, reference, pacs,
										image);
								System.out.println(keyword);

								// System.out.println(Abstract_en);
								// System.out.println(pacs);
								
								 * System.out.println(jijin);
								 * System.out.println(authordetail);
								 * System.out.println(tongxun);
								 * System.out.println(Abstract);
								 * System.out.println(title_en);
								 * System.out.println(auther_en);
								 * System.out.println(college_en);
								 * System.out.println(college);
								 * System.out.println(reference);
								 
							} catch (Exception e) {
								e.printStackTrace();
							}

						}*/
						// System.out.println(title);
						Update(title, titleURL, auther, juan, page, down, college, college_en, title_en, auther_en, Abstract, Abstract_en, keyword, keyword_en, doi, date, jijin, tongxun, authordetail, reference, pacs, image);
						System.out.println(titleURL);
						
						  System.out.println(auther); System.out.println(juan);
						 System.out.println(page); System.out.println(down);
						 System.out.println(image);
						 
						System.out.println(m++);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println(paguer);
				System.out.println(date);
				System.out.println();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
