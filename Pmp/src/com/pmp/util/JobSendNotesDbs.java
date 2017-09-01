package com.pmp.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobSendNotesDbs implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
			EntityManager manager = null;
			Connection conn = null;
			Statement prstmt = null;
			ResultSet rs = null;
			try {
				manager = JpaUtil.getInstance();
				

				Query query = manager.createNativeQuery("select ag.NUM_OS, ag.HORAS_REVISAO, CONVERT(varchar(10), palm.EMISSAO, 103) data,"+
													   "	palm.HORIMETRO, palm.TECNICO, f.LOGIN, op.LOCAL, palm.CLIENTE,c.CODIGO_CLIENTE,"+
													   "	(select EPLSNM from TW_FUNCIONARIO where EPIDNO = ag.ID_FUNC_SUPERVISOR) responsavel, palm.IDOS_PALM, palm.MODELO, palm.SERIE"+
													   "	from OS_PALM palm, PMP_AGENDAMENTO ag, TW_FUNCIONARIO f, PMP_CONFIG_OPERACIONAL op, PMP_CONTRATO c"+
													   "	where palm.ID_AGENDAMENTO = ag.ID"+
													   "	and palm.ID_FUNCIONARIO = f.EPIDNO"+
													   "	and op.ID = ag.ID_CONG_OPERACIONAL"+
													   "	and c.ID = op.ID_CONTRATO"+
													   "	and HAS_SEND_NOTES_DBS is null");

				
				List<Object[]> result = query.getResultList();
				
					
				conn = com.pmp.util.ConectionDbs.getConnecton();

				for (Object[] objects : result) {
					try {
						
						String numeroOs = (String)objects[0];
						int maxChar = 0;
						int auxLine = 0;
						String obsOS = "MANUTENCAO PREVENTIVA NO INTERVALO DE "+(BigDecimal)objects[1]+" HRS";
						for(int i = obsOS.length(); i < 50; i++){
							obsOS += " ";
						}
						obsOS += "DATA: "+(String)objects[2]+" HORIMETRO: "+(BigDecimal)objects[3];
						for(int i = obsOS.length(); i < 100; i++){
							obsOS += " ";
						}
						obsOS += "MECANICO: "+(String)objects[4]+" "+(String)objects[5];
						for(int i = obsOS.length(); i < 150; i++){
							obsOS += " ";
						}
						maxChar = obsOS.length();
						auxLine = obsOS.length();
						obsOS += "LOCAL DA MANUTENCAO: "+((String)objects[6]).trim();
						while((("LOCAL DA MANUTENCAO: "+((String)objects[6]).trim()).length()+auxLine) >= maxChar){
							maxChar+=50;
						}
						for(int i = obsOS.length(); i < maxChar; i++){
							obsOS += " ";
						}
						maxChar = obsOS.length();
						auxLine = obsOS.length();
						obsOS += "CLIENTE: "+((String)objects[7]+" "+(String)objects[8]).trim();
						while((("CLIENTE: "+((String)objects[7]+" "+(String)objects[8]).trim()).length() + auxLine) >= maxChar){
							maxChar+=50;
						}
						for(int i = obsOS.length(); i < maxChar; i++){
							obsOS += " ";
						}
						obsOS += "RESPONSAVEL: "+((String)objects[9]).trim();
						maxChar += 50;
						for(int i = obsOS.length(); i < maxChar; i++){
							obsOS += " ";
						}
						obsOS += "BACKLOG LOCALIZADO NA MAQUINA: "+(String)objects[11]+" "+(String)objects[12];
						
						query = manager.createNativeQuery("select arv.DESCRICAO +' : '+CONVERT(varchar(4000), dt.OBS) OBS from OS_PALM_DT dt, ARV_INSPECAO arv where dt.ID_IDARV = arv.ID_ARV and dt.OS_PALM_IDOS_PALM = "+(BigDecimal)objects[10]+" and STATUS = 'NC' and OBS is not null");
						List<String> backlogList = query .getResultList();
						for (String object : backlogList) {
							obsOS+= " "+object;
						}
						obsOS = UtilGestaoEquipamentosPesa.replaceCaracterEspecial(obsOS);
						String SQL = "";//"select trim(NTLNO1) NTLNO1 from "+IConstantAccess.LIB_DBS+".WOPNOTE0 where wono = '"+numeroOs+"' and trim(ntda) = 'SERVICOS A SEREM EXECUTADOS'";
						//prstmt = conn.createStatement();
						//rs = prstmt.executeQuery(SQL);
						//rs.next();
						//Determina o tipo de cliente
						//String NTLNO1 = rs.getString("NTLNO1");
						//String tipoCliente = "011".endsWith(NTLNO1)?"INT":"EXT";
						String tipoCliente = "EXT";
						if(obsOS.length() > 0){
							int initIndex = 0;
							int lengthIndex = 48;
							Integer linha = 24;
							if(tipoCliente.equals("INT")){
								SQL = "delete from "+IConstantAccess.LIB_DBS+".WOPNOTE0 n where TRIM(n.NTLNO1) > '011' and wono = '"+numeroOs+"'";
								linha = 24;
							}else{
								SQL = "delete from "+IConstantAccess.LIB_DBS+".WOPNOTE0 n where TRIM(n.NTLNO1) > '023' and wono = '"+numeroOs+"'";
								linha = 24;
							}
							prstmt = conn.createStatement();
							prstmt.executeUpdate(SQL);
							String aux = "";
							for (initIndex = 0; initIndex < obsOS.length() ; initIndex++) {
								aux += obsOS.charAt(initIndex);
								if(initIndex -1 == lengthIndex){
									prstmt = ConectionDbs.setNotesFluxoOSDBS(aux.toUpperCase(), conn, numeroOs, (linha < 100)?"0"+linha:linha+"");
									lengthIndex += 50;
									linha += 1;
									aux = "";
								}
							}
							if(aux.length() > 0){
								prstmt = ConectionDbs.setNotesFluxoOSDBS(aux.toUpperCase(), conn, numeroOs, (linha < 100)?"0"+linha:linha+"");
							}
						}
						manager.getTransaction().begin();
						query = manager.createNativeQuery("update os_palm set has_send_notes_dbs = 'S' where idos_palm = "+(BigDecimal)objects[10]);
						query.executeUpdate();
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
						if(prstmt != null){
							prstmt.close();
						}
						if(rs != null){
							rs.close();
						}
						conn.close();
					} catch (SQLException e) {					
						e.printStackTrace();
					}
				}
			}

		
	}
	
	

}
