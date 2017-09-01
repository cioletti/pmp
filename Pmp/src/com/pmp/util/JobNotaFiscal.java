package com.pmp.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobNotaFiscal implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat dfUpdate = new SimpleDateFormat("yyyy-MM-dd");
		EntityManager manager = null;
		Connection conn = null;
		Statement prstmt = null;
		ResultSet rs = null;
		try {
			manager = JpaUtil.getInstance();
			conn = com.pmp.util.ConectionDbs.getConnecton();
			prstmt = conn.createStatement();

			Query query = manager.createNativeQuery("select op.NUM_OS, c.ID from PMP_CONTRATO c, PMP_CONFIG_OPERACIONAL op"+
													" where c.DATA_ACEITE is not null and (c.VALOR_NOTA is null or c.DATA_NOTA is null or c.DATA_FATURAMENTO is null or c.NUMERO_NOTA is null)"+
													" and c.ID = op.ID_CONTRATO"+
													" and c.ID_TIPO_CONTRATO in (select ID from PMP_TIPO_CONTRATO where SIGLA not in('VEN', 'CAN', 'REN'))"+
													" and op.NUM_OS is not null" +
													" and (c.VALOR_NOTA is null or c.DATA_NOTA is null)");
			
			List<Object[]> result = query.getResultList();
			for (Object[] objects : result) {
				rs = prstmt.executeQuery("select nf.VLRNTF, nf.DTASYS, wo.IVDT8, nf.NRONTF  from "+IConstantAccess.PESARDRTRIBUTACAO+".NF020F nf, "+IConstantAccess.LIB_DBS+".WOPHDRS0 wo"+ 
										 "	where nf.NRODOC = wo.WONO"+
										 "	and wo.wono = '"+objects[0]+"'"+
										 "	and nf.VLRNTF is not null order by wo.IVDT8 desc");
				if(rs.next()){
					String DTASYS = rs.getString("DTASYS");
					String IVDT8 = rs.getString("IVDT8");
					String NRONTF = rs.getString("NRONTF");
					manager.getTransaction().begin();
					query = manager.createNativeQuery("update PMP_CONTRATO set VALOR_NOTA = '"+rs.getDouble("VLRNTF")+"', DATA_NOTA = '"+dfUpdate.format(df.parse(DTASYS))+"', DATA_FATURAMENTO = '"+dfUpdate.format(df.parse(IVDT8))+"', NUMERO_NOTA ='"+NRONTF+"' where ID = "+(BigDecimal)objects[1]);
					query.executeUpdate();
					manager.getTransaction().commit();
				}
			}
			
			
			query = manager.createNativeQuery("select op.NUM_OS, hs.ID from PMP_CONT_HORAS_STANDARD hs, PMP_OS_OPERACIONAL op where hs.DATA_FATURAMENTO is null and hs.ID_OS_OPERACIONAL is not null and op.ID = hs.ID_OS_OPERACIONAL and op.NUM_OS is not null and op.id not in (select ID from PMP_OS_OPERACIONAL where NUM_OS like	'%PULAR%')");

			result = query.getResultList();

			for (Object[] objects : result) {
				rs = prstmt.executeQuery("select nf.VLRNTF, nf.DTASYS, wo.IVDT8, nf.NRONTF  from "+IConstantAccess.PESARDRTRIBUTACAO+".NF020F nf, "+IConstantAccess.LIB_DBS+".WOPHDRS0 wo"+ 
						"	where nf.NRODOC = wo.WONO"+
						"	and wo.wono = '"+objects[0]+"'"+
				"	and nf.VLRNTF is not null order by wo.IVDT8 desc");
				if(rs.next()){
					//String DTASYS = rs.getString("DTASYS");
					String IVDT8 = rs.getString("IVDT8");
					//String NRONTF = rs.getString("NRONTF");
					manager.getTransaction().begin();
					query = manager.createNativeQuery("update PMP_CONT_HORAS_STANDARD set DATA_FATURAMENTO = '"+dfUpdate.format(df.parse(IVDT8))+"' where ID =  "+(BigDecimal)objects[1]);
					query.executeUpdate();
					manager.getTransaction().commit();
				}
			}

		}catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
			if(conn != null){
				try {
					conn.close();
					rs.close();
					prstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
