package com.pmp.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pmp.business.AgendamentoBusiness;
import com.pmp.entity.PmpFluxoDatas;

public class JobSendDateDbs implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
			EntityManager manager = null;
			Connection conn = null;
		
			try {
				manager = JpaUtil.getInstance();
				Query query = manager.createQuery("from PmpFluxoDatas where hasSendDbs is null");
				List<PmpFluxoDatas> result = query.getResultList();
				conn = com.pmp.util.ConectionDbs.getConnecton();

				for (PmpFluxoDatas fluxoDatas : result) {
					try {
						query = manager.createNativeQuery("select ESTIMATEBY from tw_funcionario where EPIDNO = '"+fluxoDatas.getIdAgendamento().getIdFuncionario()+"'");

						String estimateBy = (String)query.getSingleResult();			

						AgendamentoBusiness business = new AgendamentoBusiness();
						business.setDateFluxoOSDBS(fluxoDatas.getData(), conn, fluxoDatas.getIdAgendamento().getNumOs(), fluxoDatas.getColuna(), estimateBy);
						manager.getTransaction().begin();
						
						fluxoDatas.setHasSendDbs("S");
						manager.merge(fluxoDatas);
						
						manager.getTransaction().commit();
					} catch (Exception e) {
						if(manager != null && manager.getTransaction().isActive()){
							manager.getTransaction().rollback();
						}
						e.printStackTrace();
					}
					
				}
			} catch (Exception e) {
				if(manager != null && manager.getTransaction().isActive()){
					manager.getTransaction().rollback();
				}
			}finally{
				if(manager != null && manager.isOpen()){
					manager.close();
				}
				if(conn != null){
					try {
						conn.close();
					} catch (SQLException e) {					
						e.printStackTrace();
					}
				}
			}

		
	}
	
	public PreparedStatement setNotesFluxoOSDBS(String notes,
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
