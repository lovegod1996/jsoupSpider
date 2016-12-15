package naldc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;




import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JdbcUtils;

import com.mysql.jdbc.PreparedStatement;

public class naldc1 {
	public static void Update(String bigtitle, String paguer, String title,
			String sonpaguer, String theme, String auther, String time,
			String URL, String File, String Subject, String Publisher,
			String Abstract, String Year, String Collection, String Series,
			String Rights) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "INSERT INTO naldc_nal"
					+ "(bigtitle,paguer,title,sonpaguer,theme,auther,time,URL,download_url,Subject,Publisher,Abstract,Year,Collection,Series,Rights)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, bigtitle);
			ps.setString(2, paguer);
			ps.setString(3, title);
			ps.setString(4, sonpaguer);
			ps.setString(5, theme);
			ps.setString(6, auther);
			ps.setString(7, time);
			ps.setString(8, URL);
			ps.setString(9, File);
			ps.setString(10, Subject);
			ps.setString(11, Publisher);
			ps.setString(12, Abstract);
			ps.setString(13, Year);
			ps.setString(14, Collection);
			ps.setString(15, Series);
			ps.setString(16, Rights);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}

	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int m = 1;
		String url = "http://naldc.nal.usda.gov/naldc/collections.xhtml";
		Document doc = null;
		Elements els = null;
		int k = 0;
		try {
			doc = getDoc(url);
			els = doc.select("table>tbody>tr");
		} catch (NullPointerException en) {
			if (k < 15)
				doc = getDoc(url);
			else
				return;
		} finally {
			k++;
		}
		for (Element element : els) {
			try {
				Elements bigtitleElement = element.select(".index_title>a");
				String bigtitle = bigtitleElement.text();
				String paguer = "http://naldc.nal.usda.gov"
						+ bigtitleElement.attr("href").trim();
				String[] str = paguer.split("&");
				String type = "&" + str[1];
				int i;
				for (i = 0; i < 47323; i += 20) {
					try {

						String url1 = "http://naldc.nal.usda.gov/naldc/search.xhtml?start="
								+ i
								+ "&searchText=&searchField=&sortField="
								+ type;
						Document doc1 = null;
						Elements els1 = null;
						int j = 0;
						try {

							doc1 = getDoc(url1);
							els1 = doc1
									.select("#searchResultDocuments>#documents>div");
						} catch (NullPointerException en) {
							if (j < 15)
								doc1 = getDoc(url1);
							else
								break;
						} finally {
							j++;
						}
						if(doc1==null)
							break;
						for (Element element2 : els1) {
							try {
								Elements titleElement = element2
										.select("dl>.blacklight-extent_format_facet>a");
								String title = titleElement.text();
								String sonpaguer = "http://naldc.nal.usda.gov"
										+ titleElement.attr("href").trim();
								Elements autherElement = element2
										.select("[class*=blacklight-name_facet]>a");
								String auther = autherElement.text();

								Elements ThemeElement = element2
										.select(".blacklight-subject>a");
								String theme = ThemeElement.text();

								Elements timeElement = element2
										.select("dl>.blacklight-year_facet");
								String time = timeElement.text();

								String url2 = sonpaguer;
								Document doc2 = null;
								Elements els2 = null;
								int n = 0;
								try {
									doc2 = getDoc(url2);
									els2 = doc2
											.select("[id=catalogContentForm]>[class=defList]");
								} catch (NullPointerException en) {
									if (n < 13)
										doc2 = getDoc(url2);
									else
										break;
								} finally {
									n++;
								}
								if(doc2==null)
									break;
								for (Element element3 : els2) {
									try {
										Elements URLElement = element3
												.select(".blacklight-extent_format_facet>a");
										String URL = URLElement.attr("href");
										Elements FileElement = element3
												.select(".blacklight-model>a");
										String File = "http://naldc.nal.usda.gov"
												+ FileElement.attr("href");
										Elements SubjectElement = element3
												.select(".blacklight-subject>a");
										String Subject = SubjectElement.text();
										Element AbstractElement = element3
												.select("dd[class=blacklight-extent_format_facet]")
												.get(1);
										String Abstract = AbstractElement
												.text();
										Elements YearElement = element3
												.select(".blacklight-year_facet>a");
										String Year = YearElement.text();
										Elements CollectionElement = element3
												.select(".blacklight-collection>a");
										String Collection = CollectionElement
												.text();
										Element RightsElement = element3
												.select("dd[class=blacklight-extent_format_facet]")
												.last();
										String Rights = RightsElement.text();
										Element PublisherElement = element3
												.select("dd[class=blacklight-extent_format_facet]")
												.get(2);
										String Publisher = PublisherElement
												.text();
										Elements SeriesElement = element3
												.select(".blacklight-series>a");
										String Series = SeriesElement.text();

										System.out.println("永久地址： " + URL);
										System.out.println("文件：" + File);
										System.out.println("主题：  " + Subject);
										System.out.println("发布者：" + Publisher);
										System.out.println("文摘： " + Abstract);
										System.out.println("年：" + Year);
										System.out.println("集合：" + Collection);
										System.out.println("系列：" + Series);
										System.out.println("权利：" + Rights);
										System.out.println(m++);
										
										  Update(bigtitle, paguer, title,
										  sonpaguer, theme, auther, time, URL,
										  File, Subject, Publisher, Abstract,
										  Year, Collection, Series, Rights);
										 
									} catch (Exception e) {
										e.printStackTrace();
									}
									

								}

								System.out.println("标题：    " + title);
								System.out.println("标题链接 " + sonpaguer);
								System.out.println("主题：     " + theme);
								System.out.println("作者:    " + auther);
								System.out.println("时间：     " + time);

							} catch (Exception e) {
								e.printStackTrace();
							}

						}

						System.out.println("大标题：  " + bigtitle);
						System.out.println("大标题链接：" + paguer);
						System.out.println();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		String userAgent = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36";
		try {
			document = Jsoup.connect(BootURL).userAgent(userAgent)
					.timeout(100000).get();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return document;

	}

}
