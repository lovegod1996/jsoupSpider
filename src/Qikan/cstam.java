package Qikan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

class Ticket implements Runnable// extends Thread
{

	public static void Update(String title, String titleURL, String auther,
			String juan, String page, String down, String college,
			String college_en, String title_en, String auther_en,
			String Abstract, String Abstract_en, String keyword,
			String keyword_en, String doi, String date, String jijin,
			String reference, String pacs, String jianjie) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "INSERT INTO url28"
					+ "(title,titleURL,author,college,title_en,author_en,college_en,abstract,keyword,abstract_en,keyword_en,juan,page,download_url,doi,date,jijin,cankaowenxian,ZTFLH,authordetail)"
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
			ps.setString(18, reference);
			ps.setString(19, pacs);
			ps.setString(20, jianjie);
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

	private int tick = 1;
	Object obj = new Object();

	public void run() {
		while (true) {
			synchronized (obj) {
				if (tick > 0) {
					try {
						Thread.sleep(10);
					} catch (Exception e) {
					}
					String url = "http://www.applmathmech.cn/CN/article/showOldVolumn.do";
					Document document1 = getDoc(url);
					Elements elemenets1 = document1
							.select("table table >tbody>tr:nth-child(6) tr>td");
					for (Element element : elemenets1) {
						try {
							if (!element.text().contains("No"))
								continue;
							String link = element.select("a").attr("href");
							String paguer = null;
							if (!link.equals("")) {
								paguer = "http://www.applmathmech.cn/CN"
										+ element.select("a").attr("href")
												.trim().replace("..", "");
							} else {
								paguer = null;
							}
							String date = element.select("b").text();
							String url2 = paguer;
							Document document2 = null;
							Elements elements2 = null;
							try {
								document2 = getDoc(url2);
								elements2 = document2
										.select("table[class=td41] form table:nth-child(2) tr:nth-child(2)>td>table");
							} catch (NullPointerException en) {
								en.printStackTrace();
							}
							// System.out.println(elements2.size());
							for (Element element2 : elements2) {
								try {
									String auther = element2.select(
											"tr:nth-child(1)").text();
									String title = element2.select(
											"tr:nth-child(2)>td:nth-child(3)")
											.text();
									String[] ju = element2
											.select("tr:nth-child(3)>td:nth-child(3)")
											.first().ownText().split(": ");
									String juan = ju[0];
									String[] L = ju[1].split(" | ");
									String page = L[0];
									String doi = element2
											.select("tr:nth-child(3)>td:nth-child(3)>a:nth-child(1)")
											.text();
									String titleURL = "http://www.applmathmech.cn/CN"
											+ element2
													.select("tr:nth-child(3)>td:nth-child(3)>a")
													.get(1).attr("href")
													.replace("..", "");
									String down = null;
									if(element2.text().contains("RICH HTML"))
									{
									 down = "http://www.applmathmech.cn/CN/"
											+ element2
													.select("tr:nth-child(3)>td:nth-child(3)>a")
													.get(3).attr("href")
													.replace("../", "");
									}
									else{
										 down = "http://www.applmathmech.cn/CN/"
													+ element2
															.select("tr:nth-child(3)>td:nth-child(3)>a")
															.get(2).attr("href")
															.replace("../", "");
									}
									Document document3 = null;
									Elements elements3 = null;
									try {
										document3 = getDoc(titleURL);
										elements3 = document3
												.select("html>body");
									} catch (NullPointerException en) {
										en.printStackTrace();
									}
									for (Element element3 : elements3) {
										try {
											String college = element3
													.select("td>table:nth-child(2)  tr:nth-child(5)")
													.text();
											String title_en = element3
													.select("td>table:nth-child(2)  tr:nth-child(7)")
													.text();
											String auther_en = element3
													.select("td>table:nth-child(2)  tr:nth-child(8)")
													.text();
											String college_en = element3
													.select("td>table:nth-child(2)  tr:nth-child(9)")
													.text();
											String Abstract = element3
													.select("#abstract_tab_content>[width=98%] tr:nth-child(2)>td:nth-child(1)")
													.text().replace("摘要", "");
											String keyword ="";
											String keyword_en ="";
											String pacs ="";
											String jijin = null;
											String jianjie = null;
											String reference = "";
											String Abstract_en = "";
											if(!element3.select("#abstract_tab_content>[width=98%]").html().contains("<b>关键词 </b>"))
											{
												 Abstract_en = element3
														.select("#abstract_tab_content>[width=98%]>tbody>tr")
														.get(2).text()
														.replace("Abstract：", "");
												
												
												
												
											/*
											 * try { Pattern p = Pattern
											 * .compile(
											 * "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"
											 * ); Matcher m =
											 * p.matcher(jianjie); m.find();
											 * 
											 * if (Email.equals("")) Email =
											 * m.group(0); jianjie=
											 * jianjie.replace(m.group(0), "")
											 * .replace("E-mail:",
											 * "").replace("E-mail",
											 * "").replace("(通讯作者.  )",
											 * "").trim();
											 * 
											 * } catch (Exception e) { // TODO:
											 * handle exception }
											 */

											Elements elements4 = elements3
													.select("#reference_tab_content table>tbody>tr");
											if (elements4 != null)
												for (Element element4 : elements4) {
													reference += element4
															.text() + "<br>";
												}
										Update(title, titleURL, auther,
													juan, page, down, college,
													college_en, title_en,
													auther_en, Abstract,
													Abstract_en, keyword,
													keyword_en, doi, date,
													jijin, reference, pacs,
													jianjie);
											System.out.println(college);
											System.out.println(title_en);
											System.out.println(auther_en);
											System.out.println(college_en);
											System.out.println(Abstract);
											System.out.println(Abstract_en);
											System.out.println(keyword);
											System.out.println(keyword_en);
											System.out.println(pacs);
											System.out.println(jijin);
											// System.out.println(Email);
											System.out.println(jianjie);
											System.out.println(reference);
											}else{
												
												
												
												
												
												keyword = element3
														.select("#abstract_tab_content>[width=98%]>tbody>tr")
														.get(2).text()
														.replace("关键词 ：", "");
												Abstract_en = element3
														.select("#abstract_tab_content>[width=98%]>tbody>tr")
														.get(3).text()
														.replace("Abstract：", "");
												
												try{
												 keyword_en = element3
														.select("#abstract_tab_content>[width=98%]>tbody>tr")
														.get(4).text()
														.replace("Key words：", "");
												pacs = element3
														.select("#abstract_tab_content>[width=98%]>tbody>tr")
														.get(6).text()
														.replace("中图分类号:", "");
												}catch(Exception e){}
												
												/*
												 * String tongxun=""; String
												 * Email="";
												 */
												try {
													jijin = element3
															.select("#abstract_tab_content>[width=98%]>tbody>tr")
															.get(7).text()
															.replace("基金资助:", "");
													jianjie = element3
															.select("#abstract_tab_content>[width=98%]>tbody>tr")
															.get(8).text()
															.replace("作者简介:", "");
												} catch (Exception e) {
												}
												
												
												
												
												Elements elements4 = elements3
														.select("#reference_tab_content table>tbody>tr");
												if (elements4 != null)
													for (Element element4 : elements4) {
														reference += element4
																.text() + "<br>";
													}
											Update(title, titleURL, auther,
														juan, page, down, college,
														college_en, title_en,
														auther_en, Abstract,
														Abstract_en, keyword,
														keyword_en, doi, date,
														jijin, reference, pacs,
														jianjie);
												System.out.println(college);
												System.out.println(title_en);
												System.out.println(auther_en);
												System.out.println(college_en);
												System.out.println(Abstract);
												System.out.println(Abstract_en);
												System.out.println(keyword);
												System.out.println(keyword_en);
												System.out.println(pacs);
												System.out.println(jijin);
												// System.out.println(Email);
												System.out.println(jianjie);
												System.out.println(reference);
												
												
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
									System.out.println(auther);
									System.out.println(title);
									// System.out.println(ju);
									System.out.println(juan);
									System.out.println(page);
									System.out.println(doi);
									System.out.println(titleURL);
									System.out.println(down);
									System.out.println();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							System.out.println("链接： " + paguer);
							System.out.println("日期： " + date);
							System.out.println(tick--);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			}
		}
	}
}

public class cstam {

	public static void main(String[] args) {
		Ticket t = new Ticket();
		for (int i = 0; i < 20; i++) {
			// Ticket t=new Ticket();
			Thread t1 = new Thread(t);// 创建了一个线程
			t1.start();

		}
	}

}
