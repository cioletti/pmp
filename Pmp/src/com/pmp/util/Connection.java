package com.pmp.util;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class Connection {

	private static java.sql.Connection con = null;
	//private final static String user = "pesa";
	//private final static String password = "pesa";

	public static java.sql.Connection getConnection() {
		try {
			 Context ctx = new InitialContext();
		        //Recupera o DataSource
		        DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/pesa");
		 
		        //Cria a conexão através do DataSource
		         con = ds.getConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro em Connection.getConnection(): " + e.getMessage());
		}

		return con;
	}
	/**
	 * Rotina para atualizar a base de dados do PMP com o novo preço padrão da PESA
	 * para 2017
	 * @param argv
	 */
		public static void main(String[] argv) {
		    try {
				Class.forName("net.sourceforge.jtds.jdbc.Driver");

				java.sql.Connection con = DriverManager.getConnection("jdbc:jtds:sqlserver://172.18.1.15:1433/DSEControl","control_dse", "w8o#ay3*");

				 Statement prstmt = con.createStatement();
				 String SQL = "select ID, ID_ANTIGO from pmp_config_manutencao where id_configuracao_preco = 264";
		        	
				ResultSet rs = prstmt.executeQuery(SQL);
				

				while (rs.next()) {
				  BigDecimal idConfigManutencao = rs.getBigDecimal("ID");
				  BigDecimal idAntigo = rs.getBigDecimal("ID_ANTIGO");
				  SQL = "select horas, standard_job_cptcd,horas_revisao from Pmp_Config_Horas_Standard where ID_CONFIG_MANUTENCAO = "+idAntigo;
				  Statement prstmtLoop = con.createStatement();
				  ResultSet rsLoop = prstmtLoop.executeQuery(SQL);
				  while(rsLoop.next()){
					  String horas = rsLoop.getString("horas");
					  String standard_job_cptcd = rsLoop.getString("standard_job_cptcd"); 
					  String horas_revisao = rsLoop.getString("horas_revisao"); 
					  SQL = "insert into Pmp_Config_Horas_Standard (id_config_manutencao, horas, standard_job_cptcd, horas_revisao) values (" +
					  		idConfigManutencao+",'"+horas+"','"+standard_job_cptcd+"','"+horas_revisao+"')";
					  Statement prstmtInsert = con.createStatement();
					  
					  prstmtInsert.executeUpdate(SQL);
					  
				  }
				  
				  
				  
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				System.out.println("Acabei!!");
			}
			
		  }
}
