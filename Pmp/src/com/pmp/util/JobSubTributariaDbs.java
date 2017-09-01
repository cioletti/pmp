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

import com.pmp.entity.PmpContrato;

public class JobSubTributariaDbs implements Job {
	
	public synchronized void execute(JobExecutionContext arg0) throws JobExecutionException {
		
        Connection con = null;
        Statement prstmt = null;

        EntityManager manager = null;
        ResultSet rs = null;

        try {
        	manager = JpaUtil.getInstance();
        	con = ConectionDbs.getConnecton(); 
        	prstmt = con.createStatement();
        	
        	String SQL = "select TRIM(WONO) from "+IConstantAccess.PESARDRTRIBUTACAO+".RDRWONO";
        	rs = prstmt.executeQuery(SQL);
        	String pair = "";
        	while (rs.next()) {
        		pair += "'"+rs.getString("WONO")+"',";
        	}
        	pair = (pair.length() > 0)?pair.substring(0, pair.length()-1):"''";

        	manager.getTransaction().begin();
        	Query query = manager.createQuery(" from PmpContrato  where isFindSubTributaria = 'P' and numeroOs NOT IN ("+pair+")");
        	List<PmpContrato> numOsList = (List<PmpContrato>)query.getResultList();

        	for (PmpContrato contrato : numOsList) {
        		contrato.setIsFindSubTributaria("S");
        		
        		SQL = "select parts.TOTSEL PUNI, parts.ORQY QTD, parts.IPI IPI, parts.ICMSUB ICMSUB, parts.VLRTOT TOTALTRIBUTOS," +
				" trim(parts.PANO20) PANO20, trim(parts.SOS1) SOS1, trim(parts.DESCRICAO) DESCRICAO from "+IConstantAccess.PESARDRTRIBUTACAO+".RDRPARTS parts where parts.RFDCNO = '"+contrato.getNumDocDbs()+"'";
				//prstmt = con.prepareStatement(SQL);
				rs = prstmt.executeQuery(SQL);
				while(rs.next()){
					//manager.getTransaction().begin();
					query = manager.createNativeQuery("update PMP_PECAS_CONFIG_OPERACIONAL  set VLSUB ="+BigDecimal.valueOf(rs.getDouble("TOTALTRIBUTOS"))+", VLSSUB ="+BigDecimal.valueOf(rs.getDouble("PUNI"))+
							" where NUM_PECA = '"+rs.getString("PANO20")+"'" +
									" and qtd = "+Long.valueOf(rs.getString("QTD").trim())+" " +
									" and sos1 ='"+rs.getString("SOS1")+"'"+
									" and ID_CONTRATO ="+contrato.getId());
//					query.setParameter("ipi", BigDecimal.valueOf(rs.getDouble("IPI")));
//					query.setParameter("icmsub", BigDecimal.valueOf(rs.getDouble("ICMSUB")));
//					query.setParameter("totalTributos", BigDecimal.valueOf(rs.getDouble("TOTALTRIBUTOS")));
					//query.setParameter("partNo", rs.getString("PANO20"));
					//query.setParameter("sos1", rs.getString("SOS1"));
					//query.setParameter("partName", rs.getString("DESCRICAO"));
					//query.setParameter("qtd", Long.valueOf(rs.getString("QTD").trim()));
					query.executeUpdate();
				}
        	}
        	manager.getTransaction().commit();
        } catch (Exception e) {
        	if(manager != null && manager.getTransaction().isActive()){
        		manager.getTransaction().rollback();
        	}
        	e.printStackTrace();

        }finally{
        	try {
        		if(manager != null && manager.isOpen()){
        			manager.close();
        		}
				rs.close();
				prstmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	
        }
		
	}
//		GestaoEquipamentosBusiness business = new GestaoEquipamentosBusiness(null);
//		business.saveErro();
		
	}


