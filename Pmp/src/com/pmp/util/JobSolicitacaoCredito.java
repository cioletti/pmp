package com.pmp.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pmp.bean.UsuarioBean;
import com.pmp.business.FinanceiroBusiness;
import com.pmp.entity.PmpFinanceiro;

public class JobSolicitacaoCredito implements Job{


	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		ResultSet rs = null;
		PreparedStatement prstmt = null;
		Connection con = null;
		EntityManager manager = null;
		try {

			con = ConectionDbs.getConnecton(); 
			try {
				manager = JpaUtil.getInstance();
				Query query =  manager.createQuery("from PmpFinanceiro where isFindCrdDbs =	'N'");
				//Query query =  manager.createQuery("from PmpFinanceiro where id = 14852");
				List<PmpFinanceiro> financeiroList = (List<PmpFinanceiro>)query.getResultList();
				UsuarioBean bean = new UsuarioBean();
				bean.setMatricula("CRD");
				FinanceiroBusiness business = new FinanceiroBusiness(bean);
				for (PmpFinanceiro scFinanceiro : financeiroList) {
					String SQL = "select MOTIVO1||MOTIVO2 as MOTIVO, NROREQ, CRDRPV, CRDAPR, TERMCD  from PESASAPARQ.SOLCRDOS0 where NROREQ = '"+scFinanceiro.getCrdDbs()+"' and CRDSOL = 'Y'";
					prstmt = con.prepareStatement(SQL);
					rs = prstmt.executeQuery();
					rs.next();
					String MOTIVO = rs.getString("MOTIVO");
					String CRDRPV = rs.getString("CRDRPV");
					String CRDAPR = rs.getString("CRDAPR");
					
					String TERMCD = rs.getString("TERMCD");
					
					if(CRDRPV != null && CRDRPV.equals("Y")){
						business.rejeitarFinanceiro(scFinanceiro.getId(), MOTIVO);
					}
					if(CRDAPR != null && CRDAPR.equals("Y")){
						business.aprovarFinanceiro(scFinanceiro.getId(), MOTIVO, null, TERMCD);
					}
					
				}
			} catch (Exception e1) {
				if(manager != null && manager.getTransaction().isActive()){
					manager.getTransaction().rollback();
				}
				StringWriter writer = new StringWriter();
				e1.printStackTrace(new PrintWriter(writer));
				EmailHelper emailHelper = new EmailHelper();
				emailHelper.sendSimpleMail("Erro ao executar ao executar rotina de CRD "+writer.toString(), "ERRO SIMIS", "rodrigo@rdrsistemas.com.br");
				//e1.printStackTrace();
			}
			//}
		}catch (Exception e) {
			e.printStackTrace();
			EmailHelper emailHelper = new EmailHelper();
			emailHelper.sendSimpleMail("Erro ao executar ao executar rotina de CRD", "ERRO SIMIS", "rodrigo@rdrsistemas.com.br");
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}	
			try {
				//rs.close();
				//prstmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
