package naldc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import Jsoup.JdbcUtils;

public class CatchDetails {
	private static List<Data> linksSet = new ArrayList<Data>();

	private static synchronized Data getUrl() {
		if (linksSet.size() <= 0) {
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				conn = JdbcUtils.getConnection();
				conn.setAutoCommit(false);
				String stl = "SELECT sonpaguer FROM naldc_nal2 WHERE URL is null AND mark < 5 LIMIT 1000";
				ps = conn.prepareStatement(stl);
				rs = ps.executeQuery();

				while (rs.next()) {
					Data data = new Data();
					String urlString = rs.getString("sonpaguer");
					data.setsonpaguer(urlString);
					linksSet.add(data);
				}
				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JdbcUtils.free(rs, ps, conn);
			}
		}
		return linksSet.remove(0);
	}

	public static void main(String[] args) {

		for (int i = 0; i < 30; i++) {
			new Thread(new Runnable() {
				public void run() {
					for (;;) {
						Data data = getUrl();
						new naldc();
						try {
							naldc.main1(data.getsonpaguer(), data);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	}
}