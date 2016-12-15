package oxford;

import java.sql.ResultSet;



import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;






import Jsoup.JdbcUtils;

import com.mysql.jdbc.PreparedStatement;

public class oxford {

	public static void Update(String bigtitle, String paguer, String title,
			String sonpaguer, String auther, String published, String series,
			String description, String type, String down) {
		java.sql.Connection conn = null;
		java.sql.PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = (java.sql.Connection) JdbcUtils.getConnection();
			String sql = "INSERT INTO Stacks_cdc"
					+ "(bigtitle,paguer,title,sonpaguer,auther,published,series,description,type,download_url)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?)";
			ps = ((java.sql.Connection) conn).prepareStatement(sql);
			ps.setString(1, bigtitle);
			ps.setString(2, paguer);
			ps.setString(3, title);
			ps.setString(4, sonpaguer);
			ps.setString(5, auther);
			ps.setString(6, published);
			ps.setString(7, series);
			ps.setString(8, description);
			ps.setString(9, type);
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
		int ii = 15;
		String userAgent = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36";
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(100000).get();
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		return document;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int m = 1;
		String url = "http://stacks.cdc.gov/browse/collections";
		Document doc;
		Elements els = null;
		int k = 0;
		try {
			doc = getDoc(url);
			els = doc.select("#collections-objects>div");
		} catch (NullPointerException en) {
			en.printStackTrace();

			if (k < 5)
				doc = getDoc(url);
			else
				return;
		} finally {
			k++;
		}

		for (Element element : els) {
			try {
				Elements bigtitleElement = element
						.select(".collections-title>a");
				String bigtitle = bigtitleElement.text();
				String paguer = "http://stacks.cdc.gov"
						+ bigtitleElement.attr("href").trim();
				int i;
				for (i = 0; i < 8320; i += 20) {
					String url1 = paguer + "&start=" + i;

					Document doc1 = null;
					Elements els1 = null;
					try {
						doc1 = getDoc(url1);
						els1 = doc1.select("#search-results-list>li");
					} catch (NullPointerException en) {
						en.printStackTrace();
					}
					if (doc1 == null)
						break;
					for (Element element2 : els1) {
						try {
							Elements titleElement = element2
									.select(".search-details>.object-title>a");
							String title = titleElement.text();
							String sonpaguer = "http://stacks.cdc.gov"
									+ titleElement.attr("href");
							Element autherElement = element2
									.select(".search-details>.search-item-info>.meta-data")
									.first();
							String auther = autherElement.text();
							Element publishedElement = element2
									.select(".search-details>.search-item-info>.meta-data")
									.get(1);
							String published = publishedElement.text();
							Element seriesElement = element2
									.select(".search-details>.search-item-info>.meta-data")
									.get(2);
							String series = seriesElement.text();

							Element descriptionElement = element2
									.select(".search-details>.search-item-info>.meta-data")
									.get(3);
							String description = descriptionElement.text();
							Element typeElement = element2
									.select(".search-details>.search-item-info>.meta-data")
									.last();
							String type = typeElement.text();

							String url2 = sonpaguer;
							Document doc2 = null;
							Elements els2 = null;
							try {
								doc2 = getDoc(url2);
								els2 = doc2.select("#large-thumb");
							} catch (NullPointerException en) {
								en.printStackTrace();
							}
							if (doc2 == null)
								break;
							for (Element element3 : els2) {
								try {
									Elements downElement = element3
											.select("[href*=/view/cdc]");
									String down = "http://stacks.cdc.gov"
											+ downElement.attr("href").trim();

									Update(bigtitle, paguer, title, sonpaguer,
											auther, published, series,
											description, type, down);

									System.out.println("文件链接：" + down);
									System.out.println(m++);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							System.out.println("标题：  " + title);
							System.out.println("标题链接： " + sonpaguer);
							System.out.println("作者： " + auther);
							System.out.println("发表： " + published);
							System.out.println("系列：" + series);
							System.out.println("描述：" + description);
							System.out.println("文件类型：" + type);
							System.out.println();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
				System.out.println("标题：   " + bigtitle);
				System.out.println("标题链接： " + paguer);
				System.out.println();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
