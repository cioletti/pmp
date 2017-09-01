package com.pmp.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ConnectionStratec {
	private static java.sql.Connection con = null;
	//private final static String user = "pesa";
	//private final static String password = "pesa";

	public static java.sql.Connection getConnection() {
		try {
			Context ctx = new InitialContext();
			//Recupera o DataSource
			DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/stratec");

			//Cria a conexão através do DataSource
			con = ds.getConnection();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro em Connection.getConnection(): " + e.getMessage());
		}

		return con;
	}
}
