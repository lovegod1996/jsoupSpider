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

public class DataUpdate {
	

	public static void main(String[] args) {
		
		  Connection con = DbUtil.getConnection();
		   try {
		   DatabaseMetaData meta = con.getMetaData();
		   ResultSet rs = meta.getTables(null, null, null,
		     new String[] { "TABLE" });
		   while (rs.next()) {
		     System.out.println("表名：" + rs.getString(3));
		     String tableName=rs.getString(3);
		     String UpdateSql="update "+tableName+" set  mark=300 where mark=310";
		     SQLHelper.updateBySQL(UpdateSql);
		     System.out.println("~ ~ ~ "+tableName+" O V E R  !");
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
	

	
	}

	

}
