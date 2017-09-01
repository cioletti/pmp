package com.pmp.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pmp.entity.PmpConfigOperacional;
import com.pmp.entity.PmpContrato;

public class JobFindOrcamento implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		EntityManager manager = null;
//		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
//		Properties prop = new Properties();
		Connection con = null;
		Statement prstmt = null;
		Statement prstmt2 = null;
		ResultSet rs = null;
		try {
			manager = JpaUtil.getInstance();
//			prop.load(in);
//			String url = prop.getProperty("dbs.url");
//			String user = prop.getProperty("dbs.user");
//			String password =prop.getProperty("dbs.password");
//			Class.forName(prop.getProperty("dbs.driver")).newInstance();
			//verifica se o DBS está ativo
			con = ConectionDbs.getConnecton();
			prstmt = con.createStatement();
			prstmt2 = con.createStatement();
			String SQL = "select PEDSM, CODERR, PLDBS, DESCERR from "+IConstantAccess.AMBIENTE_DBS+".USPPHSM0 where CODERR is not null and SUBSTRING (PEDSM ,0,2) = 'C'";
			rs = prstmt.executeQuery(SQL);
			while(rs.next()){
				manager.getTransaction().begin();
				try {
					String CODERR = rs.getString("CODERR");
					String PEDSM = rs.getString("PEDSM").split("-")[1];
					PmpContrato contrato = manager.find(PmpContrato.class, Long.parseLong(PEDSM.trim()));
					if(Integer.parseInt(CODERR.trim()) == 0){
						contrato.setMsgErroDbs("OK");
						contrato.setNumDocDbs(rs.getString("PLDBS").trim());
						contrato.setCodErroDbs(rs.getString("CODERR").trim());
						contrato.setIsFindSubTributaria("S");
						//Envia para realizar substituição tributária na função rdrwono
					}else{
						contrato.setMsgErroDbs(rs.getString("DESCERR").trim());
						contrato.setCodErroDbs(rs.getString("CODERR").trim());
					}
					manager.persist(contrato);
					prstmt2.execute("delete from "+IConstantAccess.AMBIENTE_DBS+".USPPLSM0 where PEDSM = 'C-"+PEDSM+"'");
					prstmt2.execute("delete from "+IConstantAccess.AMBIENTE_DBS+".USPPHSM0 where PEDSM = 'C-"+PEDSM+"'");
				} catch (Exception e) {
					e.printStackTrace();
				}
				manager.getTransaction().commit();
				
			}
		
		}catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			EmailHelper emailHelper = new EmailHelper();
        	emailHelper.sendSimpleMail("Erro ao recuperar Orçamento JobFindOrcamento!", "Erro ao Buscar Orçamento", "rodrigo@rdrsistemas.com.br");
			e.printStackTrace();
		}finally{
			try {
				con.close();
				prstmt.close();
				prstmt2.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(manager != null && manager.isOpen()){
				manager.close();
			}
			
		}
	}

}
