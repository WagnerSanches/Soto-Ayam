package defacom;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class dbprocess {

	database DB;
	ResultSet rs;
	ArrayList<String[]> listData;
	int rownum, colnum;
	String classErrorName;

	public dbprocess(int server) {
		DB = new database(server);
		listData = new ArrayList<String[]>();
		classErrorName = dbprocess.class.getName();
	}

	public void closeDB() {
		DB.closeResultSet(rs);
		DB.closingDatabase();
	}

	public ArrayList<String[]> getArrayData(String query) {
		listData.removeAll(listData);
		try {
			rs = (ResultSet) DB.getResultData(query);
			colnum = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				String[] row = new String[colnum];
				for (int i = 0; i < colnum; i++) {
					row[i] = rs.getString(i + 1);
				}
				listData.add(row);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return listData;
	}

	public String[] getRowSetData(String query) {
		String[] row = null;
		try {
			rs = (ResultSet) DB.getResultData(query);
			colnum = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				row = new String[colnum];
				for (int i = 0; i < colnum; i++) {
					row[i] = rs.getString(i + 1);
				}
			}
		} catch (Exception e) {
		}
		return row;
	}

	public Object[] getColumnSetData(String query) {
		ArrayList<Object> list = new ArrayList<Object>();
		try {
			rs = (ResultSet) DB.getResultData(query);
			while (rs.next()) {
				Object row = rs.getString(1);
				list.add(row);
			}
		} catch (Exception e) {
		}
		return (Object[]) list.toArray();
	}

	public String getSingleData(String query) {
		String result = null;
		try {
			rs = (ResultSet) DB.getResultData(query);
			if (!rs.first()) {
				result = null;
			} else if (rs.getObject(1) == null) {
				result = "0";
			} else {
				result = rs.getObject(1).toString();
			}
		} catch (SQLException ex) {
		}
		return result;
	}

	public boolean setData(String query) {
		try {
			(DB.getStatement()).executeUpdate(query);
			return true;
		} catch (SQLException ex) {
			System.err.println(ex);
			return false;
		}
	}

	public Connection getConnection() {
		return (Connection) DB.getConnection();
	}

	public void setUsedClass(String name) {
		this.classErrorName = name;
	}

}
