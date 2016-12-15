package ARCA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

public class fiocruz {
	public static void Update(String image, String data, String title,
			String paguer, String auther) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "INSERT INTO Arca"
					+ "(title,data,paguer,auther,image)" + "VALUES(?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, title);
			ps.setString(2, data);
			ps.setString(3, paguer);
			ps.setString(4, auther);
			ps.setString(5, image);
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

	public static void main(String[] args) {
		int m = 1;
		String image = null;
		String img = null;
		String title = "";
		String auther = "";
		String data = "";
		String paguer = "";
		String link = "";
		for (int i = 0; i < 8640; i += 10) {
			String url = "http://www.arca.fiocruz.br/simple-search?query=&sort_by=score&order=desc&rpp=10&etal=0&start="
					+ i;
			Document document1 = getDoc(url);
			Elements elements1 = document1.select(".discovery-result-results");
			Elements elements2 = null;
			for (Element element : elements1) {
				try {
					if (element.html().contains("<h3>coleções:</h3>")) {
						if (element.html().contains("<h3>Registros:</h3>"))
							continue;
						elements2 = element
								.select("table:nth-child(2)>tbody>tr");
						// System.out.println(elements2);
						for (Element element1 : elements2) {
							try {
								if (!element1.html().contains("<td"))
									continue;
								// System.out.println(element1);
								img = element1.select("[headers=t1] img").attr(
										"src");
								if (img == null)
									image = null;
								else
									image = "http://www.arca.fiocruz.br" + img;

								data = element1.select("[headers=t2]").text();
								title = element1.select("[headers=t3]").text();

								link = element1.select("[headers=t3]>a").attr(
										"href");
								if (link == null)
									paguer = null;
								else
									paguer = "http://www.arca.fiocruz.br"
											+ link;

								auther = element1.select("[headers=t4]").text();

								Update(image, data, title, paguer, auther);
								System.out.println(url);
								System.out.println(image);
								System.out.println(data);
								System.out.println(title);
								System.out.println(paguer);
								System.out.println(auther);
								System.out.println("m++");

							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} else {
						elements2 = element.select(".miscTable>tbody>tr");
						// System.out.println(elements2);
						for (Element element1 : elements2) {
							try {
								if (!element1.html().contains("<td"))
									continue;
								// System.out.println(element1);
								img = element1.select("[headers=t1] img").attr(
										"src");
								if (img == null)
									image = null;
								else
									image = "http://www.arca.fiocruz.br" + img;

								data = element1.select("[headers=t2]").text();
								title = element1.select("[headers=t3]").text();

								link = element1.select("[headers=t3]>a").attr(
										"href");
								if (link == null)
									paguer = null;
								else
									paguer = "http://www.arca.fiocruz.br"
											+ link;

								auther = element1.select("[headers=t4]").text();

								Update(image, data, title, paguer, auther);
								System.out.println(url);
								System.out.println(image);
								System.out.println(data);
								System.out.println(title);
								System.out.println(paguer);
								System.out.println(auther);
								System.out.println(m++);

							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
