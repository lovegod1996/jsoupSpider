package Browse_Subjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

class Ticket implements Runnable// extends Thread
{

	public static void Update(String bigtitle, String link, String title,
			String paguer, String auther, String type, String URL,
			String description, String keywords, String down) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "INSERT INTO columbia_edu1"
					+ "(bigtitle,link,title,paguer,auther,type,URL,description,keyword,download_url)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, bigtitle);
			ps.setString(2, link);
			ps.setString(3, title);
			ps.setString(4, paguer);
			ps.setString(5, auther);
			ps.setString(6, type);
			ps.setString(7, URL);
			ps.setString(8, description);
			ps.setString(9, keywords);
			ps.setString(10, down);

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

	int m = 1;
	private int tick = 100;
	Object obj = new Object();

	public void run() {
		while (true) {
			synchronized (obj) {
				if (tick > 0) {
					try {
						Thread.sleep(10);
					} catch (Exception e) {
						e.printStackTrace();
					}
					String url = "http://academiccommons.columbia.edu/catalog/browse/subjects";
					// System.out.println(Thread.currentThread().getName()+"sale : "
					// + tick--);
					Document document1 = getDoc(url);
					// System.out.println(doc);:nth-child(1)
					Elements els = document1
							.select("#browse-box>.span-7>ul>li");
					System.out.println(els.size());
					for (Element element : els) {
						try {
							String bigtitle = element.select("a").text();
							String link = "http://academiccommons.columbia.edu"
									+ element.select("a").attr("href").trim();

							for (int i = 1; i < 215; i++) {
								String url2 = link + "&page=" + i;
								Elements elements2 = null;
								Document document2 = null;
								try {
									document2 = getDoc(url2);
									elements2 = document2
											.select(".results-set-list>div>div");
								} catch (NullPointerException en) {
									en.printStackTrace();
								}
								for (Element element2 : elements2) {
									try {
										String title = element2.select(
												".index_title>a").text();
										String paguer = "http://academiccommons.columbia.edu"
												+ element2.select(
														".index_title>a").attr(
														"href");

										Document document3 = null;
										Elements elements3 = null;
										try {
											document3 = getDoc(paguer);
											elements3 = document3
													.select("#main");
										} catch (NullPointerException en) {
										}
										for (Element element3 : elements3) {
											try {
												String auther = element3
														.select("[itemprop=creator]")
														.text();
												String date = element3
														.select("[itemprop=datePublished]")
														.text();
												String type = element3.select(
														"[itemprop=genre]")
														.text();
												// String
												// department=element3.select(".defList clearfix>dd").get(4).text();
												String URL = element3.select(
														"[itemprop=url]")
														.text();
												String description = element3
														.select("[itemprop=description]")
														.text();
												String keywords = element3
														.select("[itemprop=keywords]")
														.text();

												String down = "http://academiccommons.columbia.edu"
														+ element3
																.select("[class=title]>a")
																.attr("href")
																.trim();

												Update(bigtitle, link, title,
														paguer, auther, type,
														URL, description,
														keywords, down);

												System.out.println("作者："
														+ auther);
												System.out
														.println("日期：" + date);
												System.out.println("类型：  "
														+ type);
												// System.out.println("部门："+department);
												System.out.println("永久地址："
														+ URL);
												System.out.println("文摘："
														+ description);
												System.out.println("主题："
														+ keywords);
												System.out.println("下载链接："
														+ down);
												System.out.println(m++);
												System.out.println();
											} catch (Exception e) {
												e.printStackTrace();
											}
										}

										System.out.println("标题：    " + title);
										System.out.println("标题链接：   " + paguer);
										System.out.println();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

							System.out.println("大标题：   " + bigtitle);
							System.out.println("大标题链接：   " + link);
							System.out.println();
						} catch (Exception e) {
						}
					}

				}
			}
		}
	}
}

public class columbia_edu {

	public static void main(String[] args) {
		Ticket t = new Ticket();
		/*
		 * Thread t1 =new Thread(t);//创建了一个线程 Thread t2 =new Thread(t); Thread
		 * t3 =new Thread(t); Thread t4 =new Thread(t);
		 * 
		 * t1.start(); t2.start(); t3.start(); t4.start();
		 */
		for (int j = 0; j <20; j++) {
			Thread t1 = new Thread(t);

			t1.start();

		}

	}

}
