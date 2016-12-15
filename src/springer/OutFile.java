package springer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import utils.DbUtil;
import utils.ReadConfig;

public class OutFile {
	private static ArrayList<String> tables = new ArrayList<String>();

	public static ArrayList<String> GetTables() {

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
	GetTables();
	String tableName=null;
	for(int i=0;i<tables.size();i++){
		tableName=tables.get(i).toString();
		System.out.print("正要导出  "+tableName);
		//String OutSql="select * from "+tableName+" into outfile '"+ tableName+".sql'";
		//导出数据库内所有的表
		//String outFilesql="SELECT * INTO OUTFILE 'E:\\"+tableName+".txt' FIELDS TERMINATED BY ',' FROM "+tableName;
		String outFilesql="SELECT * INTO OUTFILE '"+ReadConfig.filedir+"//"+tableName+".txt' FIELDS TERMINATED BY ',' FROM "+tableName;
		System.out.println(outFilesql);
		execute(outFilesql);
		System.out.println("  这是第"+(i+1)+"个!");
		System.out.println(tableName+"  导出完成！");
	}
}
	
	
}
