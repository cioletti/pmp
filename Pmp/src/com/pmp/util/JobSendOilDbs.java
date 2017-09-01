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
import com.pmp.entity.PmpPecasOsOperacional;
import com.pmp.entity.TwFuncionario;

public class JobSendOilDbs implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		EntityManager manager = null;
//		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
//		Properties prop = new Properties();
		Connection con = null;
		Statement prstmt = null;
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
			String SQL = "select distinct p.IDOS_PALM, p.NUMERO_OS, id_funcionario from OS_PALM p, PMP_TIPO_OLEO_CHECKLIST tc where p.HAS_SEND_OIL_DBS is null and tc.ID_OS_PALM = p.IDOS_PALM" +
					" union " +
					" select distinct p.IDOS_PALM, p.NUMERO_OS, id_funcionario from OS_PALM p, PMP_TIPO_OLEO_CHECKLIST tc where p.HAS_SEND_OIL_DBS = 'S' and NUM_DOC_OIL = '' and tc.ID_OS_PALM = p.IDOS_PALM";			
			Query query = manager.createNativeQuery(SQL);
			List<Object[]> objectsList = query.getResultList();
			for (Object[] objects : objectsList) {
				manager.getTransaction().begin();
				BigDecimal idOsPalm = (BigDecimal)objects[0];
				String numOs = (String)objects[1];
				String idfuncionario = (String)objects[2];
				TwFuncionario funcionario = manager.find(TwFuncionario.class, idfuncionario);
				SQL = "select * from "+IConstantAccess.AMBIENTE_DBS+".USPPLSM0 where PEDSM = '"+idOsPalm+"-F' and CODERR = '00'";
				ResultSet rs = prstmt.executeQuery(SQL);
				if(rs.next()){
					SQL = "UPDATE OS_PALM set HAS_SEND_OIL_DBS = null, COD_ERRO_OIL_DBS = null where IDOS_PALM ="+idOsPalm;
					query = manager.createNativeQuery(SQL);
					query.executeUpdate();
					continue;
				}
				prstmt.executeUpdate("delete from "+IConstantAccess.AMBIENTE_DBS+".USPPLSM0 where PEDSM = '"+idOsPalm+"-F'");
				prstmt.executeUpdate("delete from "+IConstantAccess.AMBIENTE_DBS+".USPPWSM0 where PEDSM = '"+idOsPalm+"-F'");

				String tipoVeiculo = "";
				if(funcionario.getTipoVeiculo() != null){
					if(funcionario.getTipoVeiculo().equals("T")){
						tipoVeiculo = "PM";
					}else if(funcionario.getTipoVeiculo().equals("D")){
						tipoVeiculo = "DBL";
					}else if(funcionario.getTipoVeiculo().equals("U")){
						tipoVeiculo = "DUC";
					}
				}
				
				SQL = "select '"+tipoVeiculo+"'+SUBSTRING ( f.CODIGO_VEICULO ,len(f.CODIGO_VEICULO) -1 , len(f.CODIGO_VEICULO))+SUBSTRING ( tp.DESCRICAO_TIPO_OLEO ,0 , 3) PANO20, tp.QTD_LITROS, f.LOGIN from OS_PALM palm, PMP_TIPO_OLEO_CHECKLIST tp, TW_FUNCIONARIO f"+
				"	where tp.ID_OS_PALM = palm.IDOS_PALM"+
				"	and palm.ID_FUNCIONARIO = f.EPIDNO"+
				"	and tp.ID_OS_PALM =:ID_OS_PALM  " +
				"   and ('PM'+SUBSTRING ( f.CODIGO_VEICULO ,len(f.CODIGO_VEICULO) -1 , len(f.CODIGO_VEICULO))+SUBSTRING ( tp.DESCRICAO_TIPO_OLEO ,0 , 3)) is not null";
				query = manager.createNativeQuery(SQL);
				query.setParameter("ID_OS_PALM", idOsPalm);
				List<Object[]> oilList = query.getResultList();
				if(oilList.size() > 0){
					for (Object[] pair : oilList) {
						String pano20 = (String)pair[0];
						BigDecimal qtd = (BigDecimal)pair[1];
						SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPPLSM0 (PEDSM, SOS, PANO20, QTDE) values('"+idOsPalm+"-F', '050', '"+pano20+"', '"+qtd+"')";
						prstmt.executeUpdate(SQL);
					}
					//manager.getTransaction().commit();

					SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPPWSM0 (PEDSM, WONOSM, SGNOSM, OPERSM) values('"+idOsPalm+"-F', '"+numOs+"', '01', '')";
					prstmt.executeUpdate(SQL);
					
					SQL = "UPDATE OS_PALM set HAS_SEND_OIL_DBS = 'S', COD_ERRO_OIL_DBS = '100' where IDOS_PALM ="+idOsPalm;
					query = manager.createNativeQuery(SQL);
					query.executeUpdate();
				}else {
					SQL = "UPDATE OS_PALM set HAS_SEND_OIL_DBS = 'N' where IDOS_PALM ="+idOsPalm;
					query = manager.createNativeQuery(SQL);
					query.executeUpdate();
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(manager != null && manager.isOpen()){
				manager.close();
			}
			
		}
	}

}
