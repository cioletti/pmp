package com.pmp.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pmp.entity.PmpConfigOperacional;
import com.pmp.entity.PmpContrato;
import com.pmp.entity.PmpOsOperacional;

public class JobVerificarPecasOilDbs implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Connection con = null;
        Statement prstmt = null;
        Statement prstmtDelete = null;
        EntityManager manager = null;
        ResultSet rs = null;
        try {
        	con = com.pmp.util.ConectionDbs.getConnecton(); 
        	prstmt = con.createStatement();
        	prstmtDelete = con.createStatement();
        	manager = JpaUtil.getInstance();
        	
        	Query query = manager.createNativeQuery("select IDOS_PALM, NUMERO_OS from OS_PALM where COD_ERRO_OIL_DBS in ('99', '100') and NUMERO_OS not in (select NUMERO_OS from pmp_oleo_inspecao)");
        	List<Object[]> osOperacionalList = query.getResultList();
        	
        	for (Object[] idOsPalm : osOperacionalList) {
        		String SQL = ("select * from "+IConstantAccess.AMBIENTE_DBS+".USPPWSM0 where CODERR is not null and CODERR <> '' and TRIM (PEDSM) = '"+(BigDecimal)idOsPalm[0]+"-F'");
        		rs = prstmt.executeQuery(SQL);
        		if(rs.next()){
        			String DESCERR = rs.getString("DESCERR").trim();
        			String CODERR = rs.getString("CODERR").trim();
            		String [] aux = rs.getString("PEDSM").trim().split("-");
            		if(aux.length > 1){
            			manager.getTransaction().begin();
            			if(rs.getString("CODERR").trim().equals("99")){
            				
            				query = manager.createNativeQuery("update OS_PALM set NUM_DOC_OIL = '"+DESCERR.trim()+"', COD_ERRO_OIL_DBS = '"+CODERR+"' where IDOS_PALM ="+idOsPalm[0]);
            				query.executeUpdate();
            				
            				
            			}else{
            				query = manager.createNativeQuery("update OS_PALM set NUM_DOC_OIL = '"+rs.getString("PLDBS").trim()+"', COD_ERRO_OIL_DBS = '"+CODERR+"' where IDOS_PALM ="+idOsPalm[0]);
            				query.executeUpdate();
            				if(CODERR.equals("00")){
            					//Inserir as informações do notes da OS
            					String SQLOLEO = "insert into pmp_oleo_inspecao (numero_os, data) values('"+(String)idOsPalm[1]+"', GETDATE())";
            					try {
            						query = manager.createNativeQuery(SQLOLEO);
            						query.executeUpdate();
            						//new EmailHelper().sendSimpleMail(SQLOLEO, "Óleo enviado com sucesso", "cioletti.rodrigo@gmail.com");
            					} catch (Exception e) {
            						e.printStackTrace();
            						new EmailHelper().sendSimpleMail(SQLOLEO, "ERRO AO ENVIAR ÓLEO", "cioletti.rodrigo@gmail.com");
            					}
            				}
            				
            			}
            			manager.getTransaction().commit();
            		}
            		String sql = ("delete FROM "+IConstantAccess.AMBIENTE_DBS+".USPPWSM0 where TRIM(PEDSM) = '"+rs.getString("PEDSM").trim()+"'");
        			prstmtDelete.executeUpdate(sql);
        			sql = ("delete FROM "+IConstantAccess.AMBIENTE_DBS+".USPPLSM0 where TRIM(PEDSM) = '"+rs.getString("PEDSM").trim()+"'");
        			prstmtDelete.executeUpdate(sql);
        			con.commit();
        		}
				
			}
        	
//        	manager.getTransaction().begin();
//        	query = manager.createNativeQuery("update PMP_OS_OPERACIONAL set COD_ERRO_DOC_DBS = '99',  msg = 'Erro tente enviar novamente!', NUM_DOC = null where NUM_DOC = ''");
//        	query.executeUpdate();
//        	manager.getTransaction().commit();
        } catch (Exception e) {
        	if(manager != null && manager.getTransaction().isActive()){
        		manager.getTransaction().commit();
        	}
        	EmailHelper emailHelper = new EmailHelper();
        	emailHelper.sendSimpleMail("Erro ao recuperar peças no DBS!", "Erro ao Buscar Peças DBS", "rodrigo@rdrsistemas.com.br");
        	e.printStackTrace();
        	
        }finally{
        	if(manager != null && manager.isOpen()){
        		manager.close();
        	}
        	try {
        		if(con != null){
        			con.close();
        			rs.close();
        			prstmt.close();
        			prstmtDelete.close();
        		}
        	} catch (SQLException e) {
        		e.printStackTrace();
        	}

        }
		
	}

}
