package Jsoup;

import java.sql.*;

public class JdbcUtils {

	// private static String src = "jdbc:mysql://localhost:3306/J_WenZhang";
	// private static String user = "root";
	// private static String password = "1545";

	private static String src = "jdbc:mysql://localhost:3306/oa";
	private static String user = "root";
	private static String password = "root";

	public JdbcUtils() {
	}

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(src, user, password);
	}

	public static void free(ResultSet rs, Statement st, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException exx) {
						exx.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		free(null, null, getConnection());
		System.out.println(System.currentTimeMillis() - startTime);
		startTime = System.currentTimeMillis();

		free(null, null, getConnection());
		System.out.println(System.currentTimeMillis() - startTime);
		startTime = System.currentTimeMillis();

		free(null, null, getConnection());
		System.out.println(System.currentTimeMillis() - startTime);
		startTime = System.currentTimeMillis();

	}
}
