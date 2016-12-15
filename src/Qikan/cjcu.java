package Qikan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

public class cjcu {

	public static void Update(String title, String titleURL, String auther,
			String juan, String page, String down, String college,
			String college_en, String title_en, String auther_en,
			String Abstract, String Abstract_en, String keyword,
			String keyword_en, String doi, String date, String jijin,
			String tongxun, String email, String reference, String pacs,
			String image) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "INSERT INTO url31"
					+ "(title,titleURL,author,college,title_en,author_en,college_en,abstract,keyword,abstract_en,keyword_en,juan,page,download_url,doi,date,jijin,tongxun,email,cankaowenxian,ZTFLH,image)"
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
			ps.setString(19, email);
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
		String url = "http://www.cjcu.jlu.edu.cn/CN/article/showOldVolumn.do";
		Document document1 = getDoc(url);
		Elements elements1 = document1
				.select("table table >tbody>tr:nth-child(6) tr>td");
		for (Element element : elements1) {
			try {
				if (!element.text().contains("No"))
					continue;
				String link = element.select("a").attr("href");
				String paguer = null;
				if (!link.equals("")) {
					paguer = "http://www.cjcu.jlu.edu.cn/CN"
							+ element.select("a").attr("href").trim()
									.replace("..", "");
				} else {
					paguer = null;
				}
				String date = element.select("b").text();

				Document document2 = null;
				Elements elements2 = null;
				try {
					document2 = getDoc(paguer);
					elements2 = document2.select("tbody form>table>tbody>tr");
				} catch (NullPointerException en) {
					en.printStackTrace();
				}
				System.out.println(elements2.size());
				for (Element element2 : elements2) {
					try {
						if (!elements2.html().contains("table"))
							continue;
						String auther = "";
						try {
							auther = element2
									.select("table:nth-child(2) tr:nth-child(1)>td")
									.first().ownText();
						} catch (Exception e) {
						}
						String title = element2.select(
								"table:nth-child(2) tr:nth-child(1)>td>a")
								.text();
						String titleURL = "http://www.cjcu.jlu.edu.cn/CN"
								+ element2
										.select("table:nth-child(2) tr:nth-child(1)>td>a")
										.attr("href").trim().replace("..", "");
						String image = "";
						try {
							// image="http://www.cjcu.jlu.edu.cn"+element2.select("table:nth-child(2) tr:nth-child(1)>td>img").attr("src").replace("../..",
							// "");
							if (!element2
									.select("table:nth-child(2) tr:nth-child(1)>td")
									.html().contains("img")) {
								image = "";
							} else {
								image = "http://www.cjcu.jlu.edu.cn"
										+ element2
												.select("table:nth-child(2) tr:nth-child(1)>td>img")
												.attr("src")
												.replace("../..", "");
							}

						} catch (Exception e) {
						}
						String juan = "";
						String page = "";
						String[] ju = null;
						String[] pa = null;
						ju = element2
								.select("table:nth-child(2) tr:nth-child(2)>td")
								.first().ownText().split(":");

						juan = ju[0];
						pa = ju[1].split("\\[");
						page = pa[0];
						String doi = element2
								.select("table:nth-child(2) tr:nth-child(2)>td>a")
								.text().replace("摘要 HTML PDF ", "");
						String[] Do = element2
								.select("table:nth-child(2) tr:nth-child(2)>td>a:nth-child(4)")
								.attr("onclick").trim().split(",");
						String[] p = Do[0].split("\\(");
						String pd = p[1].replaceAll("'", "");
						String dow = Do[1].replace(");", "");
						String down = "http://www.cjcu.jlu.edu.cn/CN/article/downloadArticleFile.do?attachType="
								+ pd + "&id=" + dow;
						Document document3 = null;
						Elements elements3 = null;
						try {
							document3 = getDoc(titleURL);
							elements3 = document3.select("html>body");
						} catch (NullPointerException en) {
						}
						String college = "";
						String title_en = "";
						String auther_en ="";
						String college_en ="";
						for (Element element3 : elements3) {
							try { // http://www.cjcu.jlu.edu.cn/CN/article/downloadArticleFile.do?attachType=PDF&amp;id="id";
								 
								String pacs = "";
								String jijin = "";
								String[] tong = null;
								String tongxun = "";
								String email = "";
								String reference = "";
								if (element3
										.select("#abstract_tab_content>[width=98%]>tbody>tr")
										.html()
										.contains("<strong>ZTFLH:</strong>"))

								{
									 college = element3
												.select("body>table:nth-child(2) table:nth-child(2)>tbody>tr:nth-child(5)")
												.text();
										 title_en = element3
												.select("body>table:nth-child(2) table:nth-child(2)>tbody>tr:nth-child(7)")
												.text();
										auther_en = element3
												.select("body>table:nth-child(2) table:nth-child(2)>tbody>tr:nth-child(8)")
												.text();
										college_en = element3
												.select("body>table:nth-child(2) table:nth-child(2)>tbody>tr:nth-child(9)")
												.text();
									
									String Abstract = element3
											.select("#abstract_tab_content>[width=98%] tr:nth-child(2)>td:nth-child(1)")
											.text().replace("摘要", "");
									String keyword = element3
											.select("#abstract_tab_content>[width=98%]>tbody>tr")
											.get(2).text().replace("关键词 ：", "");
									String Abstract_en = element3
											.select("#abstract_tab_content>[width=98%]>tbody>tr")
											.get(3).text()
											.replace("Abstract：", "");
									String keyword_en = element3
											.select("#abstract_tab_content>[width=98%]>tbody>tr")
											.get(4).text()
											.replace("Key words：", "");
									if (!element3
											.select("#abstract_tab_content>[width=98%]>tbody>tr")
											.html()
											.contains("<strong>基金资助:</strong>")) {
										try {
											pacs = element3
													.select("#abstract_tab_content>[width=98%]>tbody>tr")
													.get(6).text()
													.replace("ZTFLH:", "");
											tong = element3
													.select("#abstract_tab_content>[width=98%]>tbody>tr")
													.get(7).text()
													.replace("通讯作者: ", "")
													.replace("作者简介:", "")
													.split("E-mail:");
											tongxun = tong[0];
											email = tong[1];
										} catch (Exception e) {
										}
									} else {
										try {
											pacs = element3
													.select("#abstract_tab_content>[width=98%]>tbody>tr")
													.get(6).text()
													.replace("ZTFLH:", "");
											jijin = element3
													.select("#abstract_tab_content>[width=98%]>tbody>tr")
													.get(7).text()
													.replace("基金资助:", "");
											tong = element3
													.select("#abstract_tab_content>[width=98%]>tbody>tr")
													.get(8).text()
													.replace("通讯作者: ", "")
													.replace("作者简介:", "")
													.split("E-mail:");
											tongxun = tong[0];
											email = tong[1];
										} catch (Exception e) {
										}
									}

									Elements elements4 = elements3
											.select("#reference_tab_content table>tbody>tr");
									if (elements4 != null)
										for (Element element4 : elements4) {
											reference += element4.text()
													+ "<br>";
										}

									Update(title, titleURL, auther, juan, page,
											down, college, college_en,
											title_en, auther_en, Abstract,
											Abstract_en, keyword, keyword_en,
											doi, date, jijin, tongxun, email,
											reference, pacs, image);
									// System.out.println("摘要：  "+Abstract);
									System.out.println("关键词：  " + keyword);
									// System.out.println("摘要（英）：" +
									// Abstract_en);
									System.out.println("关键词（英）：" + keyword_en);
									System.out.println("基金： " + jijin);
									System.out.println("通讯： " + tongxun);
									System.out.println("邮箱：" + email);
									System.out.println("位置：" + pacs);
									// System.out.println(reference);
									System.out.println(college);
									System.out.println(college_en);
									System.out.println(title_en);
									System.out.println(auther_en);
								} else if (element3
										.select("#abstract_tab_content>[width=100%]>tbody>tr")
										.html()
										.contains("<strong>Fund</strong>")) {
									 college = element3
												.select("body>table:nth-child(3)>tbody>tr:nth-child(6)")
												.text();
										 title_en = element3
												.select("body>table:nth-child(3)>tbody>tr:nth-child(8)")
												.text();
										auther_en = element3
												.select("body>table:nth-child(3)>tbody>tr:nth-child(9)")
												.text();
										college_en = element3
												.select("body>table:nth-child(3)>tbody>tr:nth-child(10)")
												.text();
									
									
									String Abstract = element3
											.select("#abstract_tab_content>[width=100%] tr:nth-child(2)>td:nth-child(1)")
											.text().replace("摘要", "");
									String keyword = element3
											.select("#abstract_tab_content>[width=100%]>tbody>tr")
											.get(2).text().replace("关键词 ：", "");
									String Abstract_en = element3
											.select("#abstract_tab_content>[width=100%]>tbody>tr")
											.get(3).text()
											.replace("Abstract：", "");
									String keyword_en = element3
											.select("#abstract_tab_content>[width=100%]>tbody>tr")
											.get(4).text()
											.replace("Key words：", "");
									try{
									jijin = element3
											.select("#abstract_tab_content>[width=100%]>tbody>tr")
											.get(6).text().replace("Fund:", "");
									
									tongxun = element3
											.select("#abstract_tab_content>[width=100%]>tbody>tr")
											.get(7)
											.text()
											.replace("Corresponding Authors:",
													"");
									}catch(Exception e){}
									Elements elements4 = elements3
											.select("#reference_tab_content table>tbody>tr");
									if (elements4 != null)
										for (Element element4 : elements4) {
											reference += element4.text()
													+ "<br>";
										}
									Update(title, titleURL, auther, juan, page,
											down, college, college_en,
											title_en, auther_en, Abstract,
											Abstract_en, keyword, keyword_en,
											doi, date, jijin, tongxun, email,
											reference, pacs, image);
									// System.out.println("摘要：  "+Abstract);
									System.out.println("关键词：  " + keyword);
									// System.out.println("摘要（英）：" +
									// Abstract_en);
									System.out.println("关键词（英）：" + keyword_en);
									System.out.println("基金： " + jijin);
									System.out.println("通讯： " + tongxun);
									System.out.println("邮箱：" + email);
									System.out.println("位置：" + pacs);
									// System.out.println(reference);
									System.out.println(college);
									System.out.println(college_en);
									System.out.println(title_en);
									System.out.println(auther_en);
								} else if(!element3.select("#abstract_tab_content>[width=100%]>tbody>tr").text().contains("基金资助"))
								{
									 college = element3
												.select("body>table:nth-child(3)>tbody>tr:nth-child(6)")
												.text();
										 title_en = element3
												.select("body>table:nth-child(3)>tbody>tr:nth-child(8)")
												.text();
										auther_en = element3
												.select("body>table:nth-child(3)>tbody>tr:nth-child(9)")
												.text();
										college_en = element3
												.select("body>table:nth-child(3)>tbody>tr:nth-child(10)")
												.text();
									String Abstract = element3
											.select("#abstract_tab_content>[width=100%] tr:nth-child(2)>td:nth-child(1)")
											.text().replace("摘要", "");
									String keyword = element3
											.select("#abstract_tab_content>[width=100%]>tbody>tr")
											.get(2).text().replace("关键词 ：", "");
									String Abstract_en = element3
											.select("#abstract_tab_content>[width=100%]>tbody>tr")
											.get(3).text()
											.replace("Abstract：", "");
									String keyword_en = element3
											.select("#abstract_tab_content>[width=100%]>tbody>tr")
											.get(4).text()
											.replace("Keywords：", "");
									try{
									jijin = element3
											.select("#abstract_tab_content>[width=100%]>tbody>tr")
											.get(6).text().replace("基金资助:", "");
									tong = element3
											.select("#abstract_tab_content>[width=100%]>tbody>tr")
											.get(7).text()
											.replace("通讯作者: ", "")
											.split("E-mail:");
									tongxun = tong[0];
									email = tong[1];
									}catch(Exception e){}
									Elements elements4 = elements3
											.select("#reference_tab_content table>tbody>tr");
									if (elements4 != null)
										for (Element element4 : elements4) {
											reference += element4.text()
													+ "<br>";
										}
									Update(title, titleURL, auther, juan, page,
											down, college, college_en,
											title_en, auther_en, Abstract,
											Abstract_en, keyword, keyword_en,
											doi, date, jijin, tongxun, email,
											reference, pacs, image);
									// System.out.println("摘要：  "+Abstract);
									System.out.println("关键词：  " + keyword);
									// System.out.println("摘要（英）：" +
									// Abstract_en);
									System.out.println("关键词（英）：" + keyword_en);
									System.out.println("基金： " + jijin);
									System.out.println("通讯： " + tongxun);
									System.out.println("邮箱：" + email);
									System.out.println("位置：" + pacs);
									// System.out.println(reference);
									System.out.println(college);
									System.out.println(college_en);
									System.out.println(title_en);
									System.out.println(auther_en);
								}else{
									college = element3
											.select("body>table:nth-child(2) table:nth-child(2)>tbody>tr:nth-child(5)")
											.text();
									 title_en = element3
											.select("body>table:nth-child(2) table:nth-child(2)>tbody>tr:nth-child(7)")
											.text();
									auther_en = element3
											.select("body>table:nth-child(2) table:nth-child(2)>tbody>tr:nth-child(8)")
											.text();
									college_en = element3
											.select("body>table:nth-child(2) table:nth-child(2)>tbody>tr:nth-child(9)")
											.text();
								String Abstract = element3
										.select("#abstract_tab_content>[width=98%] tr:nth-child(2)>td:nth-child(1)")
										.text().replace("摘要", "");
								String keyword = element3
										.select("#abstract_tab_content>[width=98%]>tbody>tr")
										.get(2).text().replace("关键词 ：", "");
								String Abstract_en = element3
										.select("#abstract_tab_content>[width=98%]>tbody>tr")
										.get(3).text()
										.replace("Abstract：", "");
								String keyword_en = element3
										.select("#abstract_tab_content>[width=98%]>tbody>tr")
										.get(4).text()
										.replace("Keywords：", "");
								try{
								jijin = element3
										.select("#abstract_tab_content>[width=98%]>tbody>tr")
										.get(6).text().replace("基金资助:", "");
								tong = element3
										.select("#abstract_tab_content>[width=98%]>tbody>tr")
										.get(7).text()
										.replace("通讯作者: ", "")
										.split("E-mail:");
								tongxun = tong[0];
								email = tong[1];
								}catch(Exception e){}
								Elements elements4 = elements3
										.select("#reference_tab_content table>tbody>tr");
								if (elements4 != null)
									for (Element element4 : elements4) {
										reference += element4.text()
												+ "<br>";
									}
								Update(title, titleURL, auther, juan, page,
										down, college, college_en,
										title_en, auther_en, Abstract,
										Abstract_en, keyword, keyword_en,
										doi, date, jijin, tongxun, email,
										reference, pacs, image);
								// System.out.println("摘要：  "+Abstract);
								System.out.println("关键词：  " + keyword);
								// System.out.println("摘要（英）：" +
								// Abstract_en);
								System.out.println("关键词（英）：" + keyword_en);
								System.out.println("基金： " + jijin);
								System.out.println("通讯： " + tongxun);
								System.out.println("邮箱：" + email);
								System.out.println("位置：" + pacs);
								// System.out.println(reference);
								System.out.println(college);
								System.out.println(college_en);
								System.out.println(title_en);
								System.out.println(auther_en);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						System.out.println(auther);
						System.out.println(title);
						System.out.println(titleURL);
						System.out.println(juan);
						System.out.println(page);
						System.out.println(doi);
						System.out.println(down);
						System.out.println(pd);
						System.out.println(image);
						System.out.println();
						System.out.println(m++);
						System.out.println();
					} catch (Exception e) {
						// e.printStackTrace();
					}

				}

				System.out.println(paguer);
				System.out.println(date);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
