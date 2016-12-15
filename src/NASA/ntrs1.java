package NASA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

public class ntrs1 {
	public static void Update(String collection, String Link, String title,
			String paguer) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "insert into ntrs  (collection,link,title,paguer) values(?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, collection);
			ps.setString(2, Link);
			ps.setString(3, title);
			ps.setString(4, paguer);

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
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; rv:30.0) Gecko/20100101 Firefox/30.0";
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(100000).get();
				// System.out.println(document);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return document;
	}

	public static void main(String[] args) {

		String url = "http://www.ntrs.nasa.gov/";
		Document document1 = getDoc(url);
		Elements elements1 = document1.select("#DimList_107>a");
		// System.out.println(elements1);
		Elements elements2 = null;
		Elements elements3 = null;

		String collection = null;
		String Link = null;
		String nu = null;
		String url2 = null;
		String title = "";
		String paguer = "";
		// String down = null;
		for (Element element : elements1) {
			if (element.select("div").html().contains("<span>›</span>"))
				continue;
			if (element.select("a").html().contains("[x]"))
				continue;
			collection = element.select("a").text();
			Link = "http://www.ntrs.nasa.gov/"
					+ element.select("a").attr("href").trim();
       //Link="http://www.ntrs.nasa.gov/search.jsp?N=4294129243";
			Document document2 = null;
			try {
				document2 = getDoc(Link);
				elements2 = document2.select(".tab_sheet");
			} catch (NullPointerException en) {
			}
			for (Element element2 : elements2) {
				try {
					nu = element2.select(".regularBox>div>strong").text()
							.replace(",", "");
					int num = Integer.parseInt(nu) + 10;
					int i = 0;
					for (i = 0; i < num; i += 10) {
						url2 = Link + "&No=" + i;
						Document document3 = null;
						try {
							document3 = getDoc(url2);
							elements3 = document3
									.select(".recordTable>tbody>tr");
						} catch (NullPointerException en) {
						}
						System.out.println(url2);

						for (Element element3 : elements3) {
							try {
								if(!element3.text().contains("NTRS Full-Text:"))
							     	continue;
									title = element3.select("a").first().text();
								    paguer = "http://www.ntrs.nasa.gov/"
										+ element3.select("a").first()
												.attr("href").trim();
							
								/*
								 * Document document4 = null; Elements elements4
								 * = null; try { document4 = getDoc(paguer);
								 * elements4 = document4
								 * .select("#doctable tr"); } catch
								 * (NullPointerException en) { }
								 * System.out.println(elements4.size()); for
								 * (Element element4 : elements4) { try { if
								 * (element4.html().contains(
								 * "External Online Source:")) online =
								 * element4.select( "td:nth-child(2)").attr(
								 * "href"); if (element4.html().contains(
								 * "External Online Source:")) source =
								 * element4.select( "td:nth-child(2)>a").attr(
								 * "href"); if (element4.html().contains(
								 * "Author and Affiliation:")) auther =
								 * element4.select( "td:nth-child(2)").text();
								 * if (element4.html().contains( "Abstract:"))
								 * Abstract = element4.select(
								 * "td:nth-child(2)").text(); if
								 * (element4.html().contains(
								 * "Publication Date:")) date = element4.select(
								 * "td:nth-child(2)").text(); if
								 * (element4.html().contains( "Document ID:"))
								 * Did = element4.select(
								 * "td:nth-child(2)").text(); if
								 * (element4.html().contains(
								 * "Subject Category:")) subject =
								 * element4.select( "td:nth-child(2)").text();
								 * if (element4.html().contains(
								 * "Report/Patent Number:")) report =
								 * element4.select( "td:nth-child(2)").text();
								 * if (element4.html().contains(
								 * "Document Type:")) Dtype = element4.select(
								 * "td:nth-child(2)").text(); if
								 * (element4.html().contains(
								 * "Contract/Grant/Task Num:")) contract =
								 * element4.select( "td:nth-child(2)").text();
								 * if (element4.html().contains(
								 * "Meeting Sponsor:")) sponsor =
								 * element4.select( "td:nth-child(2)").text();
								 * if (element4.html().contains(
								 * "Financial Sponsor:")) financial =
								 * element4.select( "td:nth-child(2)").text();
								 * if (element4.html().contains(
								 * "Organization Source:")) source =
								 * element4.select( "td:nth-child(2)").text();
								 * if (element4.html().contains(
								 * "Description:")) description =
								 * element4.select( "td:nth-child(2)").text();
								 * if (element4.html().contains(
								 * "Distribution Limits:")) limits =
								 * element4.select( "td:nth-child(2)").text();
								 * if (element4.html().contains("Rights:"))
								 * right = element4.select(
								 * "td:nth-child(2)").text(); if
								 * (element4.html().contains( "NASA Terms:"))
								 * terms = element4.select(
								 * "td:nth-child(2)").text(); if
								 * (element4.html().contains(
								 * "NIX (Document) ID:")) Did = element4.select(
								 * "td:nth-child(2)").text(); if
								 * (element4.html().contains(
								 * "Other Descriptors:")) other =
								 * element4.select( "td:nth-child(2)").text();
								 * if (element4.html().contains(
								 * "Meeting Information:")) meet =
								 * element4.select( "td:nth-child(2)").text();
								 * if (element4.html().contains(
								 * "Miscellaneous Notes:")) note =
								 * element4.select( "td:nth-child(2)").text();
								 * else continue;
								 * 
								 * 
								 * } catch (Exception e) { } }
								 * Update(collection, Link, title, paguer,
								 * limits, down, description, financial,
								 * sponsor, contract, online, source, auther,
								 * Abstract, date, subject, Did, report, right,
								 * terms, other, meet, note, Dtype);
								 * 
								 * System.out.println(limits);
								 * System.out.println(description);
								 * System.out.println(financial);
								 * System.out.println(sponsor);
								 * System.out.println(contract);
								 * System.out.println(online);
								 * System.out.println(source);
								 * System.out.println(auther);
								 * System.out.println(Abstract);
								 * System.out.println(date);
								 * System.out.println(Did);
								 * System.out.println(subject);
								 * System.out.println(report);
								 * System.out.println(right);
								 * System.out.println(terms);
								 * System.out.println(other);
								 * System.out.println(meet);
								 * System.out.println(note);
								 * System.out.println(Dtype);
								 */

						Update(collection, Link, title, paguer);

								// System.out.println(title);
								System.out.println(paguer);
								// System.out.println(down);
								// System.out.println(auther);
								System.out.println();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						System.out.println(num);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			System.out.println(collection);
			System.out.println(Link);

		}

	}
}
