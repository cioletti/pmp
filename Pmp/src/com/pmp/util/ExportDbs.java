package com.pmp.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ExportDbs {
	
	
	public Boolean inserAproval(String pair) throws Exception{

		Connection con = null;
		Statement statement = null;


		try {

			con = ConectionDbs.getConnecton();
			con.setAutoCommit(true);
		
			String SQL = "insert into "+IConstantAccess.LIB_DBS+".WOLLAPR6 (LABWO, LABSEG, LABOP, LABCC, LABEMP, LABBAD, LABBRN, LABSHF, LABDA8, LABST, LABSTP, LABELT, LABRS, LABRSP, LABCLK, LBCHGI, EMPRNR, EMPNM, LABSEQ, LABAGP,LABOTI) values("+pair+")";
		
			statement = con.createStatement();
			statement.executeUpdate(SQL);
			//System.out.println(pair);
			return true;
		}catch (Exception e) {
			throw e;
		}finally{
			try {
				if(con != null){
					statement.close();
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		//return false;
	}
	public void removerAproval(String funcionario, String os, String data) throws Exception{
		
		Connection con = null;
		Statement statement = null;
		
		
		try {
			
			con = ConectionDbs.getConnecton();
			con.setAutoCommit(true);
			
			
			
			String SQL = "delete from "+IConstantAccess.LIB_DBS+".WOLLAPR6 where empnm = '"+funcionario+"' and labwo = '"+os+"' and labda8 = '"+data+"'";
			
			statement = con.createStatement();
			statement.executeUpdate(SQL);
			//System.out.println(SQL);
		}catch (Exception e) {
			throw e;
		}finally{
			try {
				if(con != null){
					statement.close();
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
}
