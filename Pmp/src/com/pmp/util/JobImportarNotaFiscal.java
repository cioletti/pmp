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

import com.pmp.entity.PmpNotaFiscal;

public class JobImportarNotaFiscal implements Job{

	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		EntityManager manager = null;
		Connection con = null;
		Statement smt = null;
		ResultSet rs = null;
		try{
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			Query query = manager.createNativeQuery(" select * from pmp_nota_fiscal n where n.ngndoc not in ("+
													" select c.num_os from pmp_contrato c"+
													" where c.id_status_contrato = (select sc.id from pmp_status_contrato sc where sc.sigla = 'CA')"+
													" and c.num_os is not null)");
			List<String> osList = query.getResultList();
			String numOs = "";
			for (int i = 0; i < osList.size(); i++) {
				numOs += "'"+osList.get(i)+"',";
			}
			if(numOs.length() > 0){
				numOs.substring(0,numOs.length() - 1);

				con = ConectionDbs.getConnecton();
				smt = con.createStatement();
				rs = smt.executeQuery("SELECT TRIM(NXPNFSE0.NGFIL) NGFIL, TRIM(NXPNFSE0.NGNDOC) NGNDOC, TRIM(NXPNFSE0.NGNF) NGNF, TRIM(NXPNFSE0.NGDOCT) NGDOCT, TRIM(NXPNFSE0.NGSTAT) NGSTAT, TRIM(NXPNFSE0.MGDTEDD) MGDTEDD, TRIM(NXPNFSE0.MGDTEMM) MGDTEMM, " +
						" TRIM(NXPNFSE0.MGDTEYY) MGDTEYY, TRIM(NXPNFSE0.VRTNF) VRTNF, TRIM(NXPNFSE0.NGCLI) NGCLI, TRIM(NXPNFSE0.NGRS) NGRS, TRIM(NXPNFSE0.FEDORI) FEDORI, TRIM(NXPNFSE0.NFUF) NFUF, TRIM(NXPNFSE0.CONPAG) CONPAG, TRIM(NXPNFSE0.DSCCP) DSCCP, " +
						" TRIM(CIPNAME0.STN1) STN1"+
						" FROM B108F034.LIBU15.CIPNAME0 CIPNAME0, B108F034.LIBU15.NXPNFSE0 NXPNFSE0"+
						" WHERE NXPNFSE0.NGCLI = CIPNAME0.CUNO AND ((NXPNFSE0.NGDOCT Like '%L') AND (NXPNFSE0.MGDTEYY=12) AND (NXPNFSE0.NGFIL In (0,20,40,71,80,81,82,83,84,85,88,90)) OR (NXPNFSE0.NGDOCT Like '%T') AND (NXPNFSE0.MGDTEYY=12) AND (NXPNFSE0.NGFIL In (0,20,40,80,71,81,82,83,84,85,88,90)))"+
						" AND TRIM(NXPNFSE0.NGNDOC) in ("+numOs+")"+
						" ORDER BY NXPNFSE0.NGFIL, NXPNFSE0.MGDTEMM, NXPNFSE0.MGDTEDD ");
				while(rs.next()){
					query = manager.createQuery("From PmpNotaFiscal nf where nf.ngndoc =:ngndoc");
					try {
						PmpNotaFiscal notaFiscal = (PmpNotaFiscal)query.getSingleResult();
						notaFiscal.setNgfil(rs.getLong("NGFIL"));
						notaFiscal.setNgndoc(rs.getString("NGNDOC").trim());
						notaFiscal.setNgnf(rs.getLong("NGNF"));
						notaFiscal.setNgdoct(rs.getString("NGDOCT"));
						notaFiscal.setNgstat(rs.getString("NGSTAT"));
						notaFiscal.setMgdtedd(rs.getString("MGDTEDD"));
						notaFiscal.setMgdtemm(rs.getString("MGDTEMM"));
						notaFiscal.setMgdteyy(rs.getString("MGDTEYY"));
						notaFiscal.setVrtnf(rs.getBigDecimal("VRTNF"));
						notaFiscal.setNgcli(rs.getString("NGCLI"));
						notaFiscal.setNgrs(rs.getString("NGRS"));
						notaFiscal.setFedori(rs.getString("FEDORI"));
						notaFiscal.setNfuf(rs.getString("NFUF"));
						notaFiscal.setConpag(rs.getLong("CONPAG"));
						notaFiscal.setDsccp(rs.getString("DSCCP"));
						notaFiscal.setStn1(rs.getLong("STN1"));
						manager.persist(notaFiscal);
						
					} catch (Exception e) {
						PmpNotaFiscal notaFiscal = new PmpNotaFiscal();
						notaFiscal.setNgfil(rs.getLong("NGFIL"));
						notaFiscal.setNgndoc(rs.getString("NGNDOC").trim());
						notaFiscal.setNgnf(rs.getLong("NGNF"));
						notaFiscal.setNgdoct(rs.getString("NGDOCT"));
						notaFiscal.setNgstat(rs.getString("NGSTAT"));
						notaFiscal.setMgdtedd(rs.getString("MGDTEDD"));
						notaFiscal.setMgdtemm(rs.getString("MGDTEMM"));
						notaFiscal.setMgdteyy(rs.getString("MGDTEYY"));
						notaFiscal.setVrtnf(rs.getBigDecimal("VRTNF"));
						notaFiscal.setNgcli(rs.getString("NGCLI"));
						notaFiscal.setNgrs(rs.getString("NGRS"));
						notaFiscal.setFedori(rs.getString("FEDORI"));
						notaFiscal.setNfuf(rs.getString("NFUF"));
						notaFiscal.setConpag(rs.getLong("CONPAG"));
						notaFiscal.setDsccp(rs.getString("DSCCP"));
						notaFiscal.setStn1(rs.getLong("STN1"));
						manager.persist(notaFiscal);
					}
				}
			}
			manager.getTransaction().commit();
		}catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			new EmailHelper().sendSimpleMail("Erro na importação de nota fiscal do DBS", "ERRO importação de nota fiscal", "cioletti.rodrigo@gmail.com");
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
			try {
				if(con != null){
					rs.close();
					smt.close();
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
