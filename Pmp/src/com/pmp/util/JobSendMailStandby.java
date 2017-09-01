package com.pmp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pmp.entity.PmpConfigOperacional;
import com.pmp.entity.PmpContMesesStandard;
import com.pmp.entity.PmpContrato;
import com.pmp.entity.PmpMotivosNaoFecContrato;
import com.pmp.entity.PmpStatusContrato;

public class JobSendMailStandby implements Job {

	private static final String MANUTENCAO_PMP = "Manutencao PMP";
	private static int DIAS_PROXIMA_REVISAO = 10;
	
	private static final String EMAIL_RDR = "cioletti.rodrigo@gmail.com";
	private static final String EMAIL_TI = "ti.monitoramento@marcosa.com.br";

	//private static final String EMAIL_RDR = "sandroabrantes@yahoo.com.br";
	//private static final String EMAIL_TI = "sandroabrantes@yahoo.com.br";

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
		Properties prop = new Properties();
		EntityManager manager = null;

		try{
			prop.load(in);
			String url = prop.getProperty("cancelar.url");
			//boolean isConnection = true;
			//while(isConnection){
				try {
					manager = JpaUtil.getInstance();
					

					Query query = manager.createNativeQuery("select distinct pl.numero_serie, chs.id_contrato, chs.mes_manutencao, (TO_DATE(((chs.mes_manutencao * chs.frequencia) + c.data_aceite), 'DD/MM/YYYY') - (TO_DATE(SYSDATE, 'DD/MM/YYYY')) ) dias " +
							 "from pmp_contrato c, pmp_maquina_pl pl, " +
							 "(select s.id_Contrato, min(s.mes_manutencao) as mes_manutencao, s.frequencia from pmp_cont_meses_standard s where s.is_executado = 'N' " + 
							 "group by  s.id_Contrato, s.frequencia) chs " +
							 "where c.numero_serie = pl.numero_serie " +
							 "and chs.id_contrato = c.id " +
							 "and c.data_aceite is not null " +
					 "and c.has_send_email = 'S' " +
					 "and c.id in (select op.id_contrato from pmp_config_operacional op)");
					
					List<Object[]> list = query.getResultList();
					EmailHelper emailHelper = new EmailHelper();
					
					for (Object[] objects : list) {
						try {
							String numero_serie = (String)objects[0];
							Long idContrato = ((BigDecimal)objects[1]).longValue();
							BigDecimal mes_manutencao = (BigDecimal)objects[2];
							Long revisao = ((BigDecimal)objects[3]).longValue();
							query = manager.createQuery("From PmpConfigOperacional where idContrato.id =:idContrato");
							query.setParameter("idContrato", idContrato);
							PmpConfigOperacional operacional = (PmpConfigOperacional)query.getSingleResult();
							
							//contratos que a próxma revisao e menor que 10 dias
							if(revisao <= DIAS_PROXIMA_REVISAO){
								query = manager.createNativeQuery("select EPLSNM, EMAIL  from tw_funcionario usu, adm_perfil_sistema_usuario psu"+
										" where psu.id_sistema = (select sis.id from adm_sistema sis where sis.sigla = 'PMP')"+
										" and psu.id_perfil = (select p.id from adm_perfil p where p.sigla = 'ADM' and p.tipo_sistema = 'PMP')"+
										" and usu.epidno = psu.id_tw_usuario"+
								" and usu.stn1 =:filial" );
								query.setParameter("filial", operacional.getFilial());
								
								List<Object[]> objList = query.getResultList();
								
								for (Object[] pair : objList) {
									
									String msg = ((String)pair[0])+" informamos que o equipamento de serie "+numero_serie+", contrato " + operacional.getIdContrato().getNumeroContrato() + " falta menos que 10 dias para a revisao de " + mes_manutencao + " mes(es)\n" +
									" por favor efetue o agendamento!";
									emailHelper.sendSimpleMail(msg, MANUTENCAO_PMP, (String)pair[1]);
								}
							}
							query = manager.createQuery("From PmpContMesesStandard where idContrato.id =:idContrato");
							query.setParameter("idContrato", idContrato);
							
							//Contratos que faltam apenas uma revisao
							List<PmpContMesesStandard> chsList = query.getResultList();
							
							if(chsList.size() == 1){
								query = manager.createNativeQuery("select EPLSNM, EMAIL, p.sigla   from tw_funcionario usu, adm_perfil_sistema_usuario psu, adm_perfil p"+
										" where psu.id_sistema = (select sis.id from adm_sistema sis where sis.sigla = 'PMP')"+
										" and psu.id_perfil in (select p.id from adm_perfil p where p.sigla = 'ADM' or p.sigla = 'CON'  and p.tipo_sistema = 'PMP')"+
										" and usu.EPIDNO = psu.id_tw_usuario"+
										" and usu.stn1 =:filial" +
								" and p.id = psu.id_perfil");
								query.setParameter("filial", operacional.getFilial());
								List<Object[]> objList = query.getResultList();
								for (Object[] pair : objList) {
									String msg = "";
									if(((String)pair[2]).equals("CON")){
										msg = ((String)pair[0])+" informamos que o equipamento de serie "+numero_serie+", modelo,"+operacional.getIdContrato().getModelo()+" contrato "+operacional.getIdContrato().getNumeroContrato()+" falta apenas a revisao de " + mes_manutencao + " meses" +
										" para terminar. Por favor entre em contato com o cliente "+operacional.getIdContrato().getRazaoSocial()+" para renovar. Para nao receber mais esse e-mail, click no link abaixo.\n" +
										url+"?idContrato="+idContrato;
									}else{
										msg = ((String)pair[0])+" informamos que o equipamento de serie "+numero_serie+", contrato "+operacional.getIdContrato().getNumeroContrato()+" falta apenas a revisao de "+mes_manutencao+" meses\n" +
										" para terminar. Por favor entre em contato com o cliente para renovar.";
									}
									emailHelper.sendSimpleMail(msg, MANUTENCAO_PMP, (String)pair[1]);
								}
							}
						} catch (Exception e) {
							File file = File.createTempFile("log", ".txt", new File("."));
							 try {  
						            PrintStream ps = new PrintStream(file);  
						            e.printStackTrace(ps);  
						        } catch (FileNotFoundException e1) {  
						            e1.printStackTrace();  
						        }  
							emailHelper.sendSimpleMail("Erro ao executar o monitoramento do sistema de PMP", "ERRO MONITORAMENTO", EMAIL_RDR, file);
						}
					}
					//enviar e-mail para os horímetros com mais de sete dias sem atualizar
					query = manager.createNativeQuery("select round(sysdate - max(m.data_atualizacao)) , c.id, c.id_status_contrato, op.filial, m.numero_serie, c.numero_contrato from "+
							" pmp_maquina_pl m, pmp_contrato c, pmp_config_operacional op" +
							" where c.numero_serie = m.numero_serie"+
							" and c.id_status_contrato = (select id from pmp_status_contrato where sigla = 'CA')"+
							" and c.id in (select distinct hs.id_contrato from pmp_cont_meses_standard hs where hs.is_executado = 'N' and c.id = hs.id_contrato)"+
							" and c.id = op.id_contrato" +
							" group by m.numero_serie, c.id, c.id_status_contrato, op.filial, m.numero_serie, c.numero_contrato" +
					" order by max(m.data_atualizacao)");
					List<Object[]> result = query.getResultList();
					for (Object[] objects : result) {
						try {
							if(((BigDecimal)objects[0]).intValue() >= 7){
								query = manager.createNativeQuery("select EPLSNM, EMAIL, p.sigla   from tw_funcionario usu, adm_perfil_sistema_usuario psu, adm_perfil p"+
										" where psu.id_sistema = (select sis.id from adm_sistema sis where sis.sigla = 'PMP')"+
										" and psu.id_perfil in (select p.id from adm_perfil p where p.sigla = 'ADM' and p.tipo_sistema = 'PMP')"+
										" and usu.EPIDNO = psu.id_tw_usuario"+
										" and usu.stn1 =:filial" +
								" and p.id = psu.id_perfil");
								query.setParameter("filial", ((BigDecimal)objects[3]).intValue());
								List<Object[]> objList = query.getResultList();
								for (Object[] pair : objList) {
									String msg = "";

									msg = ((String)pair[0])+" informamos que o equipamento de serie "+(String)objects[4]+", contrato "+(String)objects[5]+" ja tem mais sete dias que o seu horimetro\n" +
									" foi atualizado.";
									
									emailHelper.sendSimpleMail(msg, MANUTENCAO_PMP, (String)pair[1]);

								}
							}
						} catch (Exception e) {
							File file = File.createTempFile("log", ".txt", new File("."));
							 try {  
						            PrintStream ps = new PrintStream(file);  
						            e.printStackTrace(ps);  
						        } catch (FileNotFoundException e1) {  
						            e1.printStackTrace();  
						        }  
							emailHelper.sendSimpleMail("Erro ao executar o monitoramento do sistema de PMP", "ERRO MONITORAMENTO", EMAIL_RDR, file);
						}
					}
					
					//enviar e-mail notificando os operacionais para entrar em contato com os clientes informando o que foi realizado no servico
					query = manager.createNativeQuery("select p.cliente, p.numero_os, p.modelo, p.serie, p.smu, p.tecnico, to_number(p.filial) filial, to_char(p.emissao,'DD/MM/RRRR') emissao, p.contato, p.telefone, p.equipamento from os_palm p"+
													" where round(SYSDATE - p.emissao) = 2");
					List<Object[]> resultMobile = query.getResultList();
					for (Object[] objects : resultMobile) {
						
							try {
								query = manager.createNativeQuery("select EPLSNM, EMAIL, p.sigla   from tw_funcionario usu, adm_perfil_sistema_usuario psu, adm_perfil p"+
										" where psu.id_sistema = (select sis.id from adm_sistema sis where sis.sigla = 'PMP')"+
										" and psu.id_perfil in (select p.id from adm_perfil p where p.sigla = 'ADM' and p.tipo_sistema = 'PMP')"+
										" and usu.EPIDNO = psu.id_tw_usuario"+
										" and usu.stn1 =:filial" +
								" and p.id = psu.id_perfil");
								query.setParameter("filial", ((BigDecimal)objects[6]).intValue());
								List<Object[]> objList = query.getResultList();
								for (Object[] pair : objList) {
									String msg = "";

									msg = ((String)pair[0])+", favor entrar em contato com o cliente "+(String)objects[0]+", telefone "+(String)objects[2]+" para informar os servicos que foram realizados\n" +
									" no seu equipamento de modelo"+(String)objects[2]+", serie "+(String)objects[3]+", horimetro "+(String)objects[4]+", na data de "+(String)objects[7]+", pelo tecnico "+(String)objects[5]+".";
									emailHelper.sendSimpleMail(msg, MANUTENCAO_PMP, (String)pair[1]);

								}
							} catch (Exception e) {
								File file = File.createTempFile("log", ".txt", new File("."));
								 try {  
							            PrintStream ps = new PrintStream(file);  
							            e.printStackTrace(ps);  
							        } catch (FileNotFoundException e1) {  
							            e1.printStackTrace();  
							        } 
								 
								emailHelper.sendSimpleMail("Erro ao executar o monitoramento do sistema de PMP", "ERRO MONITORAMENTO", EMAIL_RDR, file);
							}
						}
					
					
					query = manager.createNativeQuery("select distinct c.id, tw.eplsnm, tw.email, p.validade_contrato, c.numero_contrato, c.numero_serie " +
							" from pmp_contrato c, tw_funcionario tw, pmp_configuracao_precos p, pmp_config_manutencao m"+
							" where c.id_status_contrato = (select s.id from pmp_status_contrato s where s.sigla = 'CEN')"+
							" and tw.epidno = c.id_funcionario"+
							" and c.id_config_manutencao = m.id"+
					" and round(sysdate - c.data_criacao) >= p.validade_contrato");
					result = query.getResultList();
					manager.getTransaction().begin();
					for (Object[] objects : result) {
						query = manager.createQuery("From PmpStatusContrato where sigla = 'CNA'");
						PmpStatusContrato pmpStatusContrato = (PmpStatusContrato)query.getSingleResult();
						query = manager.createQuery("From PmpMotivosNaoFecContrato where sigla = 'VA'");
						PmpMotivosNaoFecContrato fecContrato = (PmpMotivosNaoFecContrato)query.getSingleResult();
						PmpContrato contrato = manager.find(PmpContrato.class, ((BigDecimal)objects[0]).longValue());
						contrato.setDataRejeicao(new Date());
						contrato.setIdMotivoNaoFecContrato(fecContrato);
						contrato.setIdStatusContrato(pmpStatusContrato);
						String msg = ((String)objects[1])+" informamos que o equipamento de serie "+(String)objects[5]+", proposta "+(String)objects[4]+" foi cancelado pelo motivo de validade da mesma.";
						
						emailHelper.sendSimpleMail(msg, MANUTENCAO_PMP, (String)objects[2]);
					}
					manager.getTransaction().commit();
					//isConnection = false;
					emailHelper.sendSimpleMail("Servico de envio de e-mail executado", MANUTENCAO_PMP, EMAIL_RDR);
				} catch (Exception e1) {
					if(manager != null && manager.getTransaction().isActive()){
						manager.getTransaction().rollback();
					}
					File file = File.createTempFile("log", ".txt", new File("."));
					 try {  
				            PrintStream ps = new PrintStream(file);  
				            e1.printStackTrace(ps);  
				        } catch (FileNotFoundException e) {  
				            e.printStackTrace();  
				        }  
				    EmailHelper emailHelper = new EmailHelper();
					emailHelper.sendSimpleMail("Erro ao executar o monitoramento do sistema de PMP", "ERRO MONITORAMENTO", EMAIL_TI);
					emailHelper.sendSimpleMail("Erro ao executar o monitoramento do sistema de PMP", "ERRO MONITORAMENTO", EMAIL_RDR, file);
				}
			//}
		}catch (Exception e) {
			e.printStackTrace();
		}
		manager.close();
	}


}
