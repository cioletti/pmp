package com.pmp.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConectionDbs {

	public static Connection getConnecton(){
		InputStream in = new ConectionDbs().getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
		Properties prop = new Properties();
		Connection con = null;
		int i = 0;
		while(i < 5){
			try {
				prop.load(in);
				String url = prop.getProperty("dbs.url");
				String user = prop.getProperty("dbs.user");
				String password =prop.getProperty("dbs.password");
				Class.forName(prop.getProperty("dbs.driver")).newInstance();

				//			String url = "jdbc:as400://192.168.128.146";
				//			String user = "XUPU15PSS";
				//			String password = "marcosa";
				//			Class.forName("com.ibm.as400.access.AS400JDBCDriver").newInstance();
				con = DriverManager.getConnection(url, user, password); 
				return con;
			}catch (Exception e) {
				e.printStackTrace();
			}
			i++;
		}
		return null;
	}
//	public static void main(String[] args) {
//		InputStream in = new ConectionDbs().getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
//		System.out.println(in);
//	}
	
	public static Long generateSequence(String numOs) throws Exception {
		String SQL = "SELECT trim(MAX(WOLLAPR6.LABSEQ)) sequenceAproval  FROM "+IConstantAccess.LIB_DBS+".WOLLAPR6 WOLLAPR6"
				+ " WHERE  TRIM(WOLLAPR6.LABWO) = '" + numOs + "'";

		// String LIB_DBS = "LIBT15";
		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		Long sequenceAproval = 0L;
		try {
			con = ConectionDbs.getConnecton();
			con.setAutoCommit(true);

			statement = con.createStatement();
			rs = statement.executeQuery(SQL);
			if (rs.next()) {
				sequenceAproval = Long.valueOf(rs
						.getLong("sequenceAproval"));
				sequenceAproval++;
			} else {
				sequenceAproval++;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			try {
				statement.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sequenceAproval;
	}
	
	public static void main(String[] args) {
		try {
			String url = "jdbc:jtds:sqlserver://189.1.162.22:1433/Pesa";
			String user = "Pesa";
			String password = "SenhaPesa";
			Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

			//			String url = "jdbc:as400://192.168.128.146";
			//			String user = "XUPU15PSS";
			//			String password = "marcosa";
			//			Class.forName("com.ibm.as400.access.AS400JDBCDriver").newInstance();
			Connection con = DriverManager.getConnection(url, user, password); 
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("select * from FaturamentoPMP");
			if(rs.next()){
				System.out.println(rs.getString(1));
				System.out.println(rs.getString(3));
				System.out.println(rs.getString(4));
				System.out.println(rs.getString(5));
				System.out.println(rs.getString(6));
			}
			rs.close();
			statement.close();
			con.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static PreparedStatement setNotesFluxoOSDBS(String notes,
			Connection con, String numeroOs, String coluna) throws SQLException {
		PreparedStatement prstmt_ = null;
		try {
			String SQL = "insert into "+IConstantAccess.LIB_DBS+".WOPNOTE0 (WONO, NTLNO1, NTDA, MASTRI) VALUES ('"+numeroOs+"', '"+coluna+"','"+notes.replaceAll("'", "")+"','I')";
			prstmt_ = con.prepareStatement(SQL);
			prstmt_.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prstmt_;
	}
}
