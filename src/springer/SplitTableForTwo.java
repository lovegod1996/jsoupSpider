package springer;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import utils.DbUtil;
import utils.ReadConfig;
import utils.SQLHelper;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class SplitTableForTwo {
	private static int biao = ReadConfig.tableNumberStart;
	private static String table = "";
	private static String TableName = "";
	private static ArrayList<String> tables = new ArrayList<String>();

	public static void CreateTable() {
		if (tables.size() > 0) {
			table = tables.get(0).toString();
			String Cratetable = "";
			TableName = table + "_" +biao;
			Cratetable = "CREATE TABLE "
					+ TableName
					+ " (id int(11) NOT NULL AUTO_INCREMENT,"
					+ "pageurl varchar(255) DEFAULT NULL,"
					+ " title longtext,"
					+ " about_ varchar(255) DEFAULT NULL,"
					+ " typ varchar(255) DEFAULT NULL,"
					+ "  additional varchar(255) DEFAULT NULL,"
					+ " somedate varchar(255) DEFAULT NULL,"
					+ " download_url varchar(255) DEFAULT NULL,"
					+ "  Abstract longtext,"
					+ "  journal varchar(255) DEFAULT NULL,"
					+ "  date varchar(255) DEFAULT NULL,"
					+ "  doi varchar(255) DEFAULT NULL,"
					+ "  pissn varchar(255) DEFAULT NULL,"
					+ "  oissn varchar(255) DEFAULT NULL,"
					+ "  publisher varchar(255) DEFAULT NULL,"
					+ "  topics longtext,"
					+ "  keyword longtext,"
					+ "  authors longtext,"
					+ "  affili longtext,"
					+ "  references_ longtext,"
					+ "  path varchar(255) DEFAULT NULL,"
					+ "  mark int(11) DEFAULT '0',"
					+ "  PRIMARY KEY (id),"
					+ "  UNIQUE KEY pageurl (pageurl)"
					+ "	) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
			try {
				execute(Cratetable);
				System.out.println("创建表 " + TableName + " 成功！！");
			} catch (Exception e) {
				System.err.println("创建表失败！！");
			}
		}
	}

	public static void execute(String sql) {
		Connection conn = null;
		Statement st = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection(
					ReadConfig.CreateTable_url, "root", "root");
			st = (Statement) conn.createStatement();
			st.execute(sql);
			// st.executeUpdate(sql);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// String str="select * from "+ReadConfig.tablename;
		// int count=SQLHelper.selectColumnCount(str);
		// System.out.println(count);

		java.sql.Connection con = DbUtil.getConnection();
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
		System.out.println(tables.size());
         for(int i=0;i<tables.size();i++){
        	 split();
         }
		

	}
	public static void split(){
		int count = ReadConfig.tableCount;
		while (count > 0) {
			biao++;
			CreateTable();
            if(biao==ReadConfig.tableCount)
            	biao=0;
			
			String into = "insert into " + TableName + " select * from "
					+ table + " limit " + ReadConfig.insertNumber;
			try {
				SQLHelper.insertBySQL(into);
				// execute(into);
				System.out.println(TableName + "插入成功！！！！");
			} catch (Exception e) {
				System.err.println(TableName + "插入失败！！！");
			}
			String delete = "delete from " + table + " limit "
					+ ReadConfig.insertNumber;
			try {
				SQLHelper.deleteBySQL(delete);
				System.out.println(table+ "前"
						+ ReadConfig.insertNumber + "条删除成功！！！");
			} catch (Exception e) {
				System.err.println("删除失败！！！");
			}
			count--;
		}
		tables.remove(0);
	}
}
