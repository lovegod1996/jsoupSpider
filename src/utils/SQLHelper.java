/**  
 * @author yokoboy
 * @date 2013-9-9
 */
package utils;

import java.sql.*;
import java.util.*;

import com.mysql.jdbc.PacketTooBigException;


/**
 * @author yokoboy
 * @date 2013-9-9
 */
public class SQLHelper {

	public static int insertBySQL(String sqlString) {
		return updateBySQL(sqlString);
	}

	public static int insertBySQL(String sqlString, Object[] parms ) {
		return updateBySQL(sqlString, parms);
	}
	public static int insertBySQL(String sqlString, Object[] parms, int[] types) {
		return updateBySQL(sqlString, parms, types);
	}

	public static int insertBySQL(String sqlString, CompletePrepareStatementI cps) {
		return updateBySQL(sqlString, cps);
	}

	public static ArrayList<HashMap<String, Object>> selectBySQL(String sqlString) {
		ArrayList<HashMap<String, Object>> rows = new ArrayList<HashMap<String, Object>>();
		Connection conn = DbUtil.getConnection();
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		ResultSetMetaData metaData = null;
		try {
			conn.setAutoCommit(false);
			prepareStatement = conn.prepareStatement(sqlString);
			resultSet = prepareStatement.executeQuery();
			metaData = resultSet.getMetaData();    //获取此 ResultSet 对象的列的编号、类型和属性。 
			int columnCount = metaData.getColumnCount();
			for (; resultSet.next();) {
				HashMap<String, Object> row = new HashMap<String, Object>();
				for (int i = 0; i < columnCount; i++) {
					String columnName = metaData.getColumnName(i + 1);
					row.put(columnName, resultSet.getObject(i + 1));
				}
				rows.add(row);
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				//System.out.println(rows);
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtil.closeSource(resultSet, prepareStatement, conn);
		}
		return rows;
	}
   
	public static int selectColumnCount(String sqlString) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		int columnCount = 0;
		try {
			conn.setAutoCommit(false);
			prepareStatement = conn.prepareStatement(sqlString);
			resultSet = prepareStatement.executeQuery();
			for (; resultSet.next();) {
				columnCount++;
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtil.closeSource(resultSet, prepareStatement, conn);
		}
		return columnCount;
	}
	
	public static ArrayList<HashMap<String, Object>> selectBySQL(String sqlString, Object[] parms, int[] types) {
		if (parms == null || types == null || parms.length != types.length) {
			throw new RuntimeException("我也不知道这是什么异常！");
		}
		ArrayList<HashMap<String, Object>> rows = new ArrayList<HashMap<String, Object>>();
		Connection conn = DbUtil.getConnection();
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		ResultSetMetaData metaData = null;
		try {
			conn.setAutoCommit(false);
			prepareStatement = conn.prepareStatement(sqlString);
			for (int len = parms.length, i = 0; i < len; i++) {
				prepareStatement.setObject(i + 1, parms[i], types[i]);
			}
			resultSet = prepareStatement.executeQuery();
			metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (; resultSet.next();) {
				HashMap<String, Object> row = new HashMap<String, Object>();
				for (int i = 0; i < columnCount; i++) {
					String columnName = metaData.getColumnName(i + 1);
					row.put(columnName, resultSet.getObject(i + 1));
				}
				rows.add(row);
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtil.closeSource(resultSet, prepareStatement, conn);
		}
		return rows;
	}

	public static int updateBySQL(String sqlString) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement prepareStatement = null;
		int result = 0;
		try {
			conn.setAutoCommit(false);
			prepareStatement = conn.prepareStatement(sqlString);
			result = prepareStatement.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtil.closeSource(null, prepareStatement, conn);
		}
		return result;
	}
	public static int deleteBySQL(String sqlString) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement prepareStatement = null;
		int result = 0;
		try {
			conn.setAutoCommit(false);
			prepareStatement = conn.prepareStatement(sqlString);
			result = prepareStatement.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtil.closeSource(null, prepareStatement, conn);
		}
		return result;
	}

	public static int updateBySQL(String sqlString, Object[] parms ,String tablename ,int id) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement prepareStatement = null;
		int result = 0;
		try {
			conn.setAutoCommit(false);
			prepareStatement = conn.prepareStatement(sqlString);
			for (int len = parms.length, i = 0; i < len; i++) {
				prepareStatement.setObject(i + 1, parms[i]);
			}
			result = prepareStatement.executeUpdate();
			conn.commit();
		}catch(PacketTooBigException pt){
			//pt.printStackTrace();
			String setMark="Update " + tablename + " set mark=10 where id=" + id;
			updateBySQL(setMark);
			System.err.println("网页数据包过大，不建议获取(已标记)、、、");
			//System.err.println("数据包过大，不建议获取~~~");
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("--------------------[数据重复]");
			throw new RuntimeException(e);
		} finally {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtil.closeSource(null, prepareStatement, conn);
		}
		return result;
	}
	
	public static int updateBySQL(String sqlString, Object[] parms) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement prepareStatement = null;
		int result = 0;
		try {
			conn.setAutoCommit(false);
			prepareStatement = conn.prepareStatement(sqlString);
			for (int len = parms.length, i = 0; i < len; i++) {
				prepareStatement.setObject(i + 1, parms[i]);
			}
			result = prepareStatement.executeUpdate();
			conn.commit();
		}catch(PacketTooBigException pt){
			System.err.println("网页数据包过大，不建议获取、、、");
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("--------------------[数据重复]");
			throw new RuntimeException(e);
		} finally {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtil.closeSource(null, prepareStatement, conn);
		}
		return result;
	}
	
	
	
	public static int updateBySQL(String sqlString, Object[] parms, int[] types) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement prepareStatement = null;
		int result = 0;
		try {
			conn.setAutoCommit(false);
			prepareStatement = conn.prepareStatement(sqlString);
			for (int len = parms.length, i = 0; i < len; i++) {
				prepareStatement.setObject(i + 1, parms[i], types[i]);
			}
			result = prepareStatement.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtil.closeSource(null, prepareStatement, conn);
		}
		return result;
	}

	public static int updateBySQL(String sqlString, CompletePrepareStatementI cps) {
		Connection conn = DbUtil.getConnection();
		PreparedStatement prepareStatement = null;
		int result = 0;
		try {
			conn.setAutoCommit(false);
			prepareStatement = conn.prepareStatement(sqlString);
			prepareStatement = cps.completeIt(prepareStatement);
			result = prepareStatement.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtil.closeSource(null, prepareStatement, conn);
		}
		return result;
	}
	
}
