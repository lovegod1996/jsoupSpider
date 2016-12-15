package springer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.DbUtil;
import utils.ReadConfig;
import utils.SQLHelper;

public class CatchPageSpringer {
	private static ArrayList<HashMap<String, Object>> rows;
	private static ArrayList<String> tables = new ArrayList<String>();
	private static String tableName = null;
	
	private static synchronized HashMap<String, Object> getType() {
		if (tables.size() > 0) {
			tableName = tables.get(0).toString();
			if (rows == null || rows.size() <= 0) {
				String str = "";
				str = "select id,pageurl from " + tableName
						+ " where  mark <10  " + ReadConfig.orderby
						+ " limit 500"; // 每次取出500个没有补充详细的链接
				rows = SQLHelper.selectBySQL(str);
				if (rows.size() <= 0) {
					System.out.println(tableName + "==========未取到链接=======");
					tables.remove(0);
					return null;
				}
			}
			HashMap<String, Object> row = rows.get(0);
			rows.remove(0);
			return row;
		} else {
			System.out.println("---exit----");
			System.exit(0);
			return null;
		}
	}
	
	public static ArrayList<String> GetTables(){
		
		Connection con = DbUtil.getConnection();
		try {
			DatabaseMetaData meta = con.getMetaData();
			ResultSet rs = meta.getTables(null, null, null,
					new String[] { "TABLE" });
			while (rs.next()) {
				System.out.println("表名：" + rs.getString(3));
				tables.add(rs.getString(3));
				System.out.println("------------------------------");
			}
			con.close();
		} catch (Exception e) {
			try {
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		return tables;
		
	}
	
	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		String setMark = "Update " + tableName + " set mark=mark+5 where pageurl= '"
				+ BootURL+"'";
		String DeleteUrl = "delete from " + tableName + " where pageurl='"
				+ BootURL + "'";
		int ii = 3;
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; rv:22.0) Gecko/20100101 Firefox/22.0";
		// System.out.println(BootURL);
		while (document == null && ii != 0) {
			ii--;
			try {
				document = Jsoup.connect(BootURL).userAgent(userAgent)
						.timeout(30000).get();
			} catch (HttpStatusException htp) {
				// htp.printStackTrace();
				ii = 0;
				SQLHelper.deleteBySQL(DeleteUrl);
				System.err.println("已删除，网页地址错误！！");
			} catch (Exception e) {
				System.err.println("访问失败，标记一次");
				System.out.println(BootURL);
				SQLHelper.updateBySQL(setMark);
				e.printStackTrace();
			}
		}
		if(document==null)
		System.out.println("=========读取失败========");
		return document;
	}
	public static void main(String[] args) {
		GetTables();
		for (int i = 0; i < ReadConfig.thread; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (;;) {
						fetch();
					}
				}
			}).start();

		}
	}
	
	public static void fetch() {
		try {
			HashMap<String, Object> row = getType();
			if (row == null) {
				row = getType();
			}

			String pageurl = null;
			pageurl = row.get("pageurl").toString(); // 从集合中抽取paguer
			int id = Integer.parseInt(row.get("id").toString()); // 将id转化为int类型
         // pageurl="http://link.springer.com/article/10.1186/s13046-016-0319-x";
			String down = null;
			String Abstract = null;
			String journal = null;
			String date = null;
			String doi = null;
			String pissn = null;
			String oissn = null;
			String publisher = null;
			String topics = null;
			String keyword = null;
			String authors = null;
			String affili = null;
			String references = "";
			String type = null;
			String about = null;
			String somedate = null;
			String additional = null;
			String setMark = "Update " + tableName + " set mark=mark+5 where id="
					+ id;
			String DeleteUrl = "delete from " + tableName + " where pageurl='"
					+ pageurl + "'";
			String down_url = null;
			Document docment = null;
			try {
				docment = getDoc(pageurl);
			} catch (Exception en) {
				en.printStackTrace();
				SQLHelper.updateBySQL(setMark);
			}
			try {
				Elements elements = docment.select("#article");
			if(elements.size()==0){
				try{
				System.err.println("网页改版了");
				elements=docment.select(".article-container");
			    down=elements.select(".button-dropdown__button").attr("href").trim();
			    authors=elements.select("[data-component=Authors]").text();
			 date=elements.select(".ArticleCitation_Year").text();
			doi=elements.select(".article-doi").text().replace("DOI: ", "");
			 Abstract=elements.select("#Abs1").text().replace("Abstract", "");
			 oissn=elements.select("#electronic-issn").text();
			 publisher=elements.select("#publisher-name").text();
			 keyword=elements.select(".KeywordGroup").text().replace("Keywords", "");
			 Elements elements1 = elements
						.select(".BibliographyWrapper>li");
				int count = 0;
				if (elements1 != null)
					for (Element element2 : elements1) {
						try {
							if (count > 10) {
								break;
							}
							references += elements1.text() + "<br>";
						} catch (OutOfMemoryError out) {
							System.err.println("数据太长了！！！");
							count++;
						}
					}
			 
				/*System.out.println(keyword);
			 System.out.println(oissn);
			 System.out.println(publisher);
			// System.out.println(references);
			 System.out.println(Abstract);
			 System.out.println(doi);
			 System.out.println(date);
			    System.out.println(authors);
			    System.out.println(down);*/
				Object[] objs = { down, Abstract, journal, date, doi,
						pissn, oissn, publisher, topics, keyword,
						authors, affili, references, type, about,
						somedate, additional };
			
				String sql = "UPDATE "
						+ tableName
						+ " set download_url=?, Abstract=?,journal=?,date=?,doi=?,pissn=?,oissn=?,publisher=?, topics=?,keyword=?, authors=?,affili=?,references_=? ,typ=?,about_=?,somedate=?,additional=?,mark=200  where id="
						+ id;
			
				SQLHelper.updateBySQL(sql, objs,tableName,id);
				System.out.println(tableName + "  " + id + "正在更新....");
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}else{

				for (Element element : elements) {
					try {
						down_url = element
								.select("#abstract-actions-download-article-pdf-link")
								.attr("href").trim();
						if (down_url == "") {
							SQLHelper.deleteBySQL(DeleteUrl);
							System.err.println(pageurl + "   删除此记录，无下载信息....");
							continue;
						}
						down = "http://link.springer.com" + down_url;
						Abstract = element.select(".Abstract>p").text();
						journal = element.select("#abstract-about-publication")
								.text()
								+ "  "
								+ element.select(
										"#abstract-about-article-citation-id")
										.text();
						date = element.select("#abstract-about-cover-date")
								.text();
						doi = element.select("#abstract-about-doi").text();
						pissn = element.select("#abstract-about-issn").text();
						oissn = element.select(
								"#abstract-about-electronic-issn").text();
						publisher = element.select("#abstract-about-publisher")
								.text();
						topics = element.select(".abstract-about-subject")
								.text();
						keyword = element.select("#abstract-about-keywords")
								.text();
						authors = element.select(".authors").text();
						affili = element.select(".authors").text();

						type = element.select("#enumeration>strong").text();
						about = element.select("#enumeration>p").get(0).text();
						somedate = element.select("#enumeration>p").get(1)
								.text();
						additional = element.select(
								"#abstract-about-additional-links").text();
						Elements elements1 = element
								.select(".BibliographyWrapper>li");
						
						int count = 0;
						if (elements1 != null)
							for (Element element2 : elements1) {
								try {
									if (count > 10) {
										break;
									}
									references += elements1.text() + "<br>";
								} catch (OutOfMemoryError out) {
								
									System.err.println("数据太长了！！！");
									count++;
								}
							}
						Object[] objs = { down, Abstract, journal, date, doi,
								pissn, oissn, publisher, topics, keyword,
								authors, affili, references, type, about,
								somedate, additional };
					
						String sql = "UPDATE "
								+ tableName
								+ " set download_url=?, Abstract=?,journal=?,date=?,doi=?,pissn=?,oissn=?,publisher=?, topics=?,keyword=?, authors=?,affili=?,references_=? ,typ=?,about_=?,somedate=?,additional=?,mark=200   where id="
								+ id;
					
						SQLHelper.updateBySQL(sql, objs,tableName,id);
						System.out.println(tableName + "  " + id + "正在更新....");
						
					} catch (Exception e) {
						e.printStackTrace();
						SQLHelper.updateBySQL(setMark);
					}
				}
			  }
			} catch (NullPointerException nu) {
				nu.printStackTrace();
				System.err.println("404  错误 引起~ ~ ~ ~");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("未取到链接");
		}
	}
}
