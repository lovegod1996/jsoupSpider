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

public class naldc {
	static int m = 1;

	public static void Update(Data data) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "UPDATE naldc_nal2 SET"
					+ " URL=?,download_url=?,Subject_=?,Publisher=?,Abstract=?,`Year`=?,Collection=?,Series=?,Rights=?,mark=200 "
					+ " WHERE sonpaguer=?";
			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, data.getURL());
			ps.setString(2, data.getdownload_url());
			ps.setString(3, data.getSubject());
			ps.setString(4, data.getPublisher());
			ps.setString(5, data.getAbstract());
			ps.setString(6, data.getYear());
			ps.setString(7, data.getCollection());
			ps.setString(8, data.getSeries());
			ps.setString(9, data.getRights());
			ps.setString(10, data.getsonpaguer());
			System.out.println(sql);
			System.err.println(data.equals(rs));
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			ERROR(data.getURL());
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}

	private static void ERROR(String url) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "UPDATE naldc_nal2 SET mark=mark+1 WHERE url='" + url
					+ "'";
			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}

	public static void main1(String url, Data data) throws IOException {
		Document doc = null;
		Elements els = null;
		int k = 0;
		try {
			doc = getDoc(url);
			els = doc.select("[id=catalogContentForm]>[class=defList]");
			Detail(els, data);
		} catch (NullPointerException en) {
			if (k < 15)
				doc = getDoc(url);
			else
				return;
		} finally {
			k++;
		}
		data.setURL("");
		data.setdownload_url("");
		data.setSubject("");
		data.setAbstract("");
		data.setYear("");
		data.setCollection("");
		data.setRights("");
		data.setPublisher("");
		data.setSeries("");
	}

	public static void Detail(Elements els, Data data) {
		int size = els.size();
		System.out.println(size);
		String URL = null;
		String File = null;
		String Subject = null;
		String Abstract = null;
		String Language = null;
		String Author = null;
		String Year = null;
		String Note = null;
		String Collection = null;
		String Source = null;
		String Rights = null;
		String Publisher = null;
		String Series = null;
		/*
		 * for (int i = 0; i < size;) { try { if
		 * (els.get(i).text().contains("Permanent")) URL = els.get(++i).text();
		 * if (els.get(++i).text().contains("File")) File =
		 * els.get(++i).attr("href"); if
		 * (els.get(++i).text().contains("Subject(s)")) Subject =
		 * els.get(++i).text(); else if
		 * (els.get(++i).text().contains("Abstrat")) Abstract =
		 * els.get(++i).text(); if (els.get(++i).text().contains("Language"))
		 * Language = els.get(++i).text(); else if
		 * (els.get(++i).text().contains("Author")) Author =
		 * els.get(++i).text(); if (els.get(++i).text().contains("Year")) Year =
		 * els.get(++i).text(); else if (els.get(++i).text().contains("Note"))
		 * Note = els.get(++i).text(); if
		 * (els.get(++i).text().contains("Collection")) Collection =
		 * els.get(++i).text(); else if (els.get(++i).text().contains("Source"))
		 * Source = els.get(++i).text(); if
		 * (els.get(++i).text().contains("Rights")) Rights =
		 * els.get(++i).text(); else if
		 * (els.get(++i).text().contains("Language")) Language =
		 * els.get(++i).text(); if (els.get(++i).text().contains("Publisher"))
		 * Publisher = els.get(++i).text(); if
		 * (els.get(++i).text().contains("Year")) Year = els.get(++i).text(); if
		 * (els.get(++i).text().contains("Collection")) Collection =
		 * els.get(++i).text(); if (els.get(++i).text().contains("Rights"))
		 * Rights = els.get(++i).text(); if
		 * (els.get(++i).text().contains("Series")) Series =
		 * els.get(++i).text();
		 */
		for (Element element3 : els) {
			try {

				Elements URLElement = element3
						.select(".blacklight-extent_format_facet>a");
				URL = URLElement.attr("href");
				Elements FileElement = element3.select(".blacklight-model>a");
				File = "http://naldc.nal.usda.gov" + FileElement.attr("href");
				Elements SubjectElement = element3
						.select(".blacklight-subject>a");
				Subject = SubjectElement.text().replace(" ", " ");
				Element AbstractElement = element3.select(
						"dd[class=blacklight-extent_format_facet]").get(1);
				Abstract = AbstractElement.text();
				Elements YearElement = element3
						.select(".blacklight-year_facet>a");
				Year = YearElement.text();
				Elements CollectionElement = element3
						.select(".blacklight-collection>a");
				Collection = CollectionElement.text();
				Element RightsElement = element3.select(
						"dd[class=blacklight-extent_format_facet]").last();
				Rights = RightsElement.text();
				Element PublisherElement = element3.select(
						"dd[class=blacklight-extent_format_facet]").get(2);
				Publisher = PublisherElement.text();
				Elements SeriesElement = element3
						.select(".blacklight-series>a");
				Series = SeriesElement.text();

				data.setURL(URL);
				data.setdownload_url(File);
				data.setSubject(Subject);
				data.setAbstract(Abstract);
				data.setYear(Year);
				data.setCollection(Collection);
				data.setRights(Rights);
				data.setPublisher(Publisher);
				data.setSeries(Series);

				System.out.println("永久地址： " + URL);
				System.out.println("文件：" + File);
				System.out.println("主题：  " + Subject);
				System.out.println("作者：" + Author);
				System.out.println("注：" + Note);
				System.out.println("发布者：" + Publisher);
				System.out.println("文摘： " + Abstract);
				System.out.println("源： " + Source);
				System.out.println("年：" + Year);
				System.out.println("集合：" + Collection);
				System.out.println("系列：" + Series);
				System.out.println("权利：" + Rights);
				System.out.println(m++);

				Update(data);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// public static void UpdateTitleURL(String title, String sonpaguer,
	// String time, String theme, String auther) {
	// Connection conn = null;
	// PreparedStatement ps = null;
	// ResultSet rs = null;
	// try {
	// conn = JdbcUtils.getConnection();
	// String sql = "INSERT INTO naldc_nal"
	// + "(title,sonpaguer,theme,time,auther)"
	// + "VALUES(?,?,?,?,?)";
	// ps = (PreparedStatement) conn.prepareStatement(sql);
	// ps.setString(1, title);
	// ps.setString(2, sonpaguer);
	// ps.setString(3, theme);
	// ps.setString(4, time);
	// ps.setString(5, auther);
	// ps.execute();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// JdbcUtils.free(rs, ps, conn);
	// }
	//
	// }

	public static Document getDoc(String BootURL) { // 获取box
		Document document = null;
		String userAgent = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36";
		try {
			document = Jsoup.connect(BootURL).userAgent(userAgent)
					.timeout(100000).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;

	}

}
