package JEIT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

public class ie_ac {
	public static void Update1(String title, String titleURL, String auther,
			String juan, String page, String down, String college,
			String college_en, String title_en, String auther_en,
			String Abstract, String Abstract_en, String keyword,
			String keyword_en, String doi, String date, String jijin,
			String tongxun, String email, String reference) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "INSERT INTO url24"
					+ "(title,titleURL,author,college,title_en,author_en,college_en,abstract,keyword,abstract_en,keyword_en,juan,page,download_url,doi,date,jijin,tongxun,email,cankaowenxian)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}
	public static void Update2(String title, String titleURL, String auther,
			String juan, String page, String down, String college,
			String college_en, String title_en, String auther_en,
			String Abstract, String Abstract_en, String keyword,
			String keyword_en, String doi, String date, String jijin,
			String tongxun, String email, String reference,String pacs,String jianjie) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "INSERT INTO url24"
					+ "(title,titleURL,author,college,title_en,author_en,college_en,abstract,keyword,abstract_en,keyword_en,juan,page,download_url,doi,date,jijin,tongxun,email,cankaowenxian,ZTFLH,authordetail)"
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
			ps.setString(22, jianjie);
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
		String url = "http://jeit.ie.ac.cn/CN/article/showOldVolumn.do";
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
					paguer = "http://jeit.ie.ac.cn/CN"
							+ element.select("a").attr("href").trim()
									.replace("..", "");
				} else {
					paguer = null;
				}
				String date = element.select("b").text();

				String url2 = paguer;
				Document document2 = null;
				Elements elements2 = null;
				try {
					document2 = getDoc(url2);
					elements2 = document2.select("tbody form>table>tbody>tr");
				} catch (NullPointerException en) {
					en.printStackTrace();
				}
				// System.out.println(elements2.size());
				for (Element element2 : elements2) {
					try {
						if (element.html().contains("table"))
							continue;
						String auther = element2
								.select("table:nth-child(2) tr:nth-child(1)>td")
								.first().ownText();
						String title = element2.select(
								"table:nth-child(2) tr:nth-child(1)>td>a")
								.text();
						String titleURL = "http://jeit.ie.ac.cn/CN"
								+ element2
										.select("table:nth-child(2) tr:nth-child(1)>td>a")
										.attr("href").trim().replace("..", "");
						String[] ju = element2
								.select("table:nth-child(2) tr:nth-child(2)>td")
								.first().ownText().split(":");
						String juan = ju[0];
						String[] pa = ju[1].split("\\[");
						String page = pa[0];
						String doi = element2
								.select("table:nth-child(2) tr:nth-child(2)>td>a")
								.text().replace("摘要 HTML PDF ", "");
						String down = "http://jeit.ie.ac.cn/CN"
								+ element2
										.select("table:nth-child(2) tr:nth-child(2)>td>a:nth-child(4)")
										.attr("href").replace("..", "");
						String url3 = titleURL;
						Document document3 = null;
						Elements elements3 = null;
						try {
							document3 = getDoc(url3);
							elements3 = document3.select("html>body");
						} catch (NullPointerException en) {
							en.printStackTrace();
						}
                        if(elements3.html().contains("<strong>收稿日期:</strong>"))
                        {
                        	for (Element element3 : elements3) {
								try{
									String college = element3
											.select("table:nth-child(2)  tr:nth-child(5)")
											.text();
									String title_en = element3
											.select("table:nth-child(2)  tr:nth-child(7)")
											.text();
									String auther_en = element3
											.select("table:nth-child(2)  tr:nth-child(8)")
											.text();
									String college_en = element3
											.select("table:nth-child(2)  tr:nth-child(9)")
											.text();
									String Abstract=element3.select("#abstract_tab_content>[width=98%] tr:nth-child(2)>td:nth-child(1)").text().replace("摘要", "");
									String keyword=element3.select("#abstract_tab_content>[width=98%]>tbody>tr").get(2).text().replace("关键词 ：","");
									String Abstract_en=element3.select("#abstract_tab_content>[width=98%]>tbody>tr").get(3).text().replace("Abstract：", "");
									String keyword_en=element3.select("#abstract_tab_content>[width=98%]>tbody>tr").get(4).text().replace("Key words：", "");
									String pacs=element3.select("#abstract_tab_content>[width=98%]>tbody>tr").get(6).text().replace("PACS:", "");
									String jijin=element3.select("#abstract_tab_content>[width=98%]>tbody>tr").get(7).text().replace("基金资助:", "");
									String[] tong = element3
											.select("#abstract_tab_content>[width=98%]>tbody>tr").get(8)
											.text()
											.replace("通讯作者: ", "")
											.split("E-mail:");
									String tongxun = tong[0];
									String email = tong[1];
									String jianjie=null;
									String reference="";
									try{
									jianjie=element3.select("#abstract_tab_content>[width=98%]>tbody>tr").get(9).text().replace("作者简介:", "");
									}catch(Exception e){}
									
									Elements elements4 = elements3
											.select("#reference_tab_content table>tbody>tr");
									if (elements4 != null)
										for (Element element4 : elements4) {
											reference += element4.text() + "<br>";
										}
									Update2(title, titleURL, auther, juan, page, down, college, college_en, title_en, auther_en, Abstract, Abstract_en, keyword, keyword_en, doi, date, jijin, tongxun, email, reference, pacs, jianjie);
									System.out.println("学校：  " + college);
									System.out.println("标题（英）:" + title_en);
									System.out.println("作者（英）：" + auther_en);
									System.out.println("学校（英）:" + college_en);
									System.out.println("摘要：  "+Abstract);
									System.out.println("关键词：  " + keyword);
									System.out.println("摘要（英）：" + Abstract_en);
									System.out.println("关键词（英）：" + keyword_en);
									System.out.println("基金： " + jijin);
									System.out.println("通讯： " + tongxun);
									System.out.println("邮箱：" + email);
									System.out.println("位置："+pacs);
									System.out.println("简介： "+jianjie);
									System.out.println("文献： "+reference);
									System.out.println(m++);
									
									
									
								}catch(Exception e){
									//e.printStackTrace();
								}
							}
                        	
                        	
                        	
                        	
                        	
                        	
                        }else{
						for (Element element3 : elements3) {
							try {
								String college = element3
										.select("body>table:nth-child(3)>tbody>tr:nth-child(6)")
										.text();
								String title_en = element3
										.select("body>table:nth-child(3) tr:nth-child(8)>td")
										.text();
								String auther_en = element3
										.select("body>table:nth-child(3) tr:nth-child(9)>td")
										.text();
								String college_en = element3
										.select("body>table:nth-child(3) tr:nth-child(10)>td")
										.text();
								String Abstract = element3
										.select("#abstract_tab_content>[width=100%] tr:nth-child(2)>td:nth-child(1)")
										.text().replace("摘要 ", "");
								
								String keyword = null;
								/*
								 * if(element3.select(
								 * "#abstract_tab_content>[width=100%]>tbody>tr>td>span"
								 * )!=null) {
								 */
								keyword = element3
										.select("#abstract_tab_content>[width=100%]>tbody>tr>td>span")
										.get(0).text().replace("关键词：", "");
								String Abstract_en = element3
										.select("#abstract_tab_content>[width=100%]>tbody>tr>td>span")
										.get(1).text().replace("Abstract：", "");
								String keyword_en = element3
										.select("#abstract_tab_content>[width=100%]>tbody>tr>td>span")
										.get(2).text().replace("Keywords：", "");
								String jijin = "";
								String tongxun = "";
								String email = "";
								String reference = "";
								try {
									jijin = element3
											.select("#abstract_tab_content>[width=100%]>tbody>tr")
											.get(6).text().replace("本文基金:", "");
									String[] tong = element3
											.select("#abstract_tab_content>[width=100%]>tbody>tr")
											.last().text()
											.replace("通讯作者: ", "")
											.split("Email:");
									tongxun = tong[0];
									email = tong[1];
								} catch (Exception e) {
									// e.printStackTrace();
								}
								Elements elements4 = elements3
										.select("#reference_tab_content table>tbody>tr");
								if (elements4 != null)
									for (Element element4 : elements4) {
										reference += element4.text() + "<br>";
									}
Update1(title, titleURL, auther, juan, page, down, college, college_en, title_en, auther_en, Abstract, Abstract_en, keyword, keyword_en, doi, date, jijin, tongxun, email, reference);
								System.out.println("学校：  " + college);
								System.out.println("标题（英）:" + title_en);
								System.out.println("作者（英）：" + auther_en);
								System.out.println("学校（英）:" + college_en);
								System.out.println("摘要：  " + Abstract);
								System.out.println("关键词：  " + keyword);
								System.out.println("摘要（英）：" + Abstract_en);
								System.out.println("关键词（英）：" + keyword_en);
								System.out.println("基金： " + jijin);
								System.out.println("通讯： " + tongxun);
								System.out.println("邮箱：" + email);
								System.out.println("参考文献：" + reference);
								System.out.println(m++);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
                        }
						System.out.println("作者：  " + auther);
						System.out.println("标题：  " + title);
						System.out.println("链接： " + titleURL);
						System.out.println("卷： " + juan);
						System.out.println("页： " + page);
						System.out.println("DOI： " + doi);
						System.out.println("下载： " + down);
                      
					} catch (Exception e) {
						// e.printStackTrace();
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
