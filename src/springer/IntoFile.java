package springer;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

import utils.ReadConfig;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class IntoFile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
       String[] filelist=GetFileName(ReadConfig.insertfilepath);
      int count=0;
		if(ReadConfig.isonetable){
			System.out.println("您选择导入一张表");
			CreateTable(ReadConfig.tablename);
			 for (String string : filelist) {
				 String name=string.replace(".txt", "");
				 String intosql="LOAD DATA INFILE '"+ReadConfig.insertfilepath+"\\"+string+"' INTO TABLE "+ReadConfig.tablename+" FIELDS TERMINATED BY ',';  ";
				 execute(intosql);
				 System.out.println(string+"导入成功");
			 }
			
		}else{
			System.out.println("您选择导入多张表");
			for (String string : filelist) {
				String tablename=string.replace(".txt", "");
				CreateTable(tablename);
				 String intosql="LOAD DATA INFILE '"+ReadConfig.insertfilepath+"\\"+string+"' INTO TABLE "+tablename+" FIELDS TERMINATED BY ',';  ";
				 execute(intosql);
				 System.out.println(string+"导入成功");
			}
		}
	
	}
    
	public static void CreateTable(String tablename) {
		String Cratetable = "";
	
		Cratetable = "CREATE TABLE "
				+ tablename
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
				+ "	) ENGINE=InnoDB AUTO_INCREMENT=28461854 DEFAULT CHARSET=utf8;";
		try {
			execute(Cratetable);
			System.out.println("创建表 " + tablename + " 成功！！");
		} catch (Exception e) {
			System.err.println("创建表失败！！");
		}
	}

	public static void execute(String sql) {
		Connection conn = null;
		Statement st = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection(
					ReadConfig.CreateTable_url, ReadConfig.username, ReadConfig.password);
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
	
	public static String[] GetFileName(String filepath){
		File file = new File(filepath);
        String [] fileName = file.list();
        return fileName;
	}
	
}
