package com.pmp.util;

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

import com.pmp.entity.PmpOsOperacional;

public class JobVerificaPecasDbsPmp implements Job {
	
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
        	
        	Query query = manager.createQuery("From PmpOsOperacional where numDoc is null and codErroDocDbs in ('100', '99')");
        	List<PmpOsOperacional> osOperacionalList = query.getResultList();
        	
        	for (PmpOsOperacional pmpOsOperacional : osOperacionalList) {
        		String SQL = ("select * from "+IConstantAccess.AMBIENTE_DBS+".USPPWSM0 where CODERR is not null and TRIM (PEDSM) = '"+pmpOsOperacional.getId()+"-P'");
        		rs = prstmt.executeQuery(SQL);
        		if(rs.next()){
        			String DESCERR = rs.getString("DESCERR").trim();
        			String CODERR = rs.getString("CODERR").trim();
            		String [] aux = rs.getString("PEDSM").trim().split("-");
            		if(aux.length > 1){
            			manager.getTransaction().begin();
            			if(rs.getString("CODERR").trim().equals("99")){
            				query = manager.createNativeQuery("update pmp_os_operacional set MSG = '"+DESCERR.trim()+"', COD_ERRO_DOC_DBS = '"+CODERR+"' where id = "+pmpOsOperacional.getId());
            				query.executeUpdate();
            			}else{
            				query = manager.createNativeQuery("update pmp_os_operacional set NUM_DOC = '"+rs.getString("PLDBS").trim()+"' , COD_ERRO_DOC_DBS = '"+CODERR+"', MSG = NULL where id = "+pmpOsOperacional.getId());
            				query.executeUpdate();
            				//Inserir as informações do notes da OS
            				
            				
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
        	
        	manager.getTransaction().begin();
        	query = manager.createNativeQuery("update PMP_OS_OPERACIONAL set COD_ERRO_DOC_DBS = '99',  msg = 'Erro tente enviar novamente!', NUM_DOC = null where NUM_DOC = ''");
        	query.executeUpdate();
        	manager.getTransaction().commit();
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
        			rs.close();
        			prstmt.close();
        			prstmtDelete.close();
        			con.close();
        		}
        	} catch (SQLException e) {
        		e.printStackTrace();
        	}

        }
		
	}
//		GestaoEquipamentosBusiness business = new GestaoEquipamentosBusiness(null);
//		business.saveErro();
		
	}


