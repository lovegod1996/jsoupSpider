package utils;

import java.io.*;
import java.net.MalformedURLException;
import java.sql.*;
import java.util.*;

import javax.sql.*;

import org.apache.commons.dbcp.BasicDataSourceFactory;

public class DbUtil {

	private static DataSource dataSource = null;

	/**
	 * 鍒濆鍖栨暟鎹簮
	 */
	static {
		try {
			// 使用DBCP创建连接池 
			Properties prop = new Properties();
			try {
				File file = new File("./dbcpCfg.properties");
				FileInputStream fis = new FileInputStream(file);
				prop.load(fis);  //从输入流中读取属性列表（键和元素对）。
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// DBCP鐨勮繛鎺ユ睜宸ュ巶
			// 閫氳繃杩欎釜宸ュ巶锛屾垜浠幏寰椾竴涓牴鎹祫婧愭枃浠堕厤缃殑鏁版嵁搴撹繛鎺ユ睜
			dataSource = BasicDataSourceFactory.createDataSource(prop);  //使用工厂生成一个实例
			System.out.println("使用工厂生成一个实例");
		} catch (Exception e) {
			throw new Error("鏁版嵁搴撹繛鎺ュけ璐�");
		}
	}

	/**
	 * 鑾峰緱鏁版嵁搴撻摼鎺�
	 * 
	 * @author yokoboy
	 * @date 2013-9-9
	 */
	public static Connection getConnection() {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return connection;
	}

	/**
	 * 鍏抽棴鏁版嵁搴撻摼鎺�瀹為檯鏄斁鍏ヤ簡鏁版嵁搴撻摼鎺ユ睜)
	 * 
	 * @author yokoboy
	 * @date 2013-9-9
	 */
	public static void closeSource(ResultSet rs, Statement statement, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		getConnection();
	}
}