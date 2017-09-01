package com.pmp.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pmp.bean.CriticidadeHorasBean;
import com.pmp.business.AprovalHelper;

public class JobHorasAjudanteAproval implements Job{

	private List<CriticidadeHorasBean> result = new ArrayList<CriticidadeHorasBean>();
	SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
	SimpleDateFormat sdfDateAtual = new SimpleDateFormat("yyyyMMdd");
	DecimalFormat df = new DecimalFormat("0.##");
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		EntityManager manager = null;
		AprovalHelper.popularArrayCriticidadeHS(result);
		try {
			manager = JpaUtil.getInstance();
		
			
			ExportDbs dbs = new ExportDbs();
			Query query = manager.createNativeQuery("SELECT aph.ID"+//0
													      ",convert(datetime, aph.DATA, 111) as DATA"+//1
													      ",aph.ORIGEM"+//2
													      ",aph.DESTINO"+//3
													      ",aph.HORA_SAIDA"+//4
													      ",aph.HORA_CHEGADA"+//5
													      ",aph.HORAS_VIAGEM"+//6
													      ",aph.HORA_INICIO_SERVICO"+//7
													      ",aph.HORA_TERMINO_SERVICO"+//8
													      ",aph.HORAS_TRABALHO"+//9
													      ",aph.KM_INICIAL"+//10
													      ",aph.KM_FINAL"+//11
													      ",aph.ID_AGENDAMENTO"+//12
													      ",func.LOGIN"+//13
													      ",aph.ID_OS_PALM"+//14
													      ",aph.HAS_SEND_DBS" +//15
													      ",ag.NUM_OS" +//16
													      ",ag.FILIAL" +//17
													      ",func.EPLSNM" +//18
													  " FROM PMP_APROPRIACAO_HORAS aph, PMP_AGENDAMENTO ag, TW_FUNCIONARIO func"+
													  " where aph.HAS_SEND_AJUDANTE_DBS is null" +
													  " and ag.ID = aph.ID_AGENDAMENTO" +
													  " and (aph.HORAS_VIAGEM <> '0:00' or aph.HORAS_TRABALHO <> '0:00')"+
													  " and ag.ID_AJUDANTE = func.EPIDNO");		
//			Query query = manager.createNativeQuery("select h.id, h.id_agendamento from of_horas_agendamento h " +
//					" where to_char(h.data_ini, 'DD/MM/YYYY') = '07/07/2012'"+ 
//					" and h.data_fim is null"+
//					" and to_char(h.data_ini, 'HH') < 12"+
//					" and h.id_agendamento in (select id from of_agendamento t"+
//					" where t.id_status_atividade = (select id from of_status_atividade where sigla = 'EXE'))");		
			List<Object[]> atividadeList = query.getResultList(); 	
			String pair = "";
			for(Object[] obj: atividadeList){
				dbs.removerAproval((String)obj[18], (String)obj[16], sdfDateAtual.format((Date)obj[1]));
			}
			for(Object[] obj: atividadeList){
				
				if(!((String)obj[6]).equals("0:00")){
					manager.getTransaction().begin();
					//PmpApropriacaoHoras horas = manager.find(PmpApropriacaoHoras.class, Long.valueOf(19));
					String [] horasViagemAux = ((String)obj[6]).split(":");
					String horasViagemDecimal = "";
					DecimalFormat df = new DecimalFormat("0.00");
					if(Integer.valueOf(horasViagemAux[1]) > 0){
						Double horasMinutos = Double.valueOf(horasViagemAux[1])/60;
						horasMinutos = Double.valueOf(horasViagemAux[0]) + horasMinutos;
						horasViagemDecimal = df.format(horasMinutos).replace(",", ".");
					}else if(Integer.valueOf(horasViagemAux[0]) > 0){
						horasViagemDecimal = horasViagemAux[0] +"."+horasViagemAux[1];
					}

					horasViagemAux = ((String)obj[4]).split(":");
					if(!(Integer.valueOf(horasViagemAux[0]) == 8) && AprovalHelper.verificarCritidadeHoraExtra(Integer.valueOf(horasViagemAux[0]), result) != null){
						if(this.inserirIntervaloHorasViagem(manager, ((BigDecimal)obj[0]).longValue())){
							query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_AJUDANTE_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
							query.executeUpdate();
							manager.getTransaction().commit();
						}else{
							query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_AJUDANTE_DBS = null where ID ="+((BigDecimal)obj[0]).longValue());
							query.executeUpdate();
							manager.getTransaction().commit();
							continue;
						}
					}else{
						String horaSaidaViagemDecimal = "0.00";

						if(Integer.valueOf(horasViagemAux[1]) > 0){
							Double horasMinutos = Double.valueOf(horasViagemAux[1])/60;
							horasMinutos = Double.valueOf(horasViagemAux[0]) + horasMinutos;
							horaSaidaViagemDecimal = df.format(horasMinutos).replace(",", ".");
						} else if(Integer.valueOf(horasViagemAux[0]) > 0){
							horaSaidaViagemDecimal = horasViagemAux[0] +"."+horasViagemAux[1];
						}

						horasViagemAux = ((String)obj[5]).split(":");
						if(!(Integer.valueOf(horasViagemAux[0]) == 18 && Integer.valueOf(horasViagemAux[1]) == 0) &&  AprovalHelper.verificarCritidadeHoraExtra(Integer.valueOf(horasViagemAux[0]), result) != null){
							if(this.inserirIntervaloHorasViagem(manager, ((BigDecimal)obj[0]).longValue())){
								query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_AJUDANTE_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
								query.executeUpdate();
								manager.getTransaction().commit();
							}else{
								query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_AJUDANTE_DBS = null where ID ="+((BigDecimal)obj[0]).longValue());
								query.executeUpdate();
								manager.getTransaction().commit();
								continue;
							}
						} else {
							String horaChegadaViagemDecimal = "0.00";

							if(Integer.valueOf(horasViagemAux[1]) > 0){
								Double horasMinutos = Double.valueOf(horasViagemAux[1])/60;
								horasMinutos = Double.valueOf(horasViagemAux[0]) + horasMinutos;
								horaChegadaViagemDecimal = df.format(horasMinutos).replace(",", ".");
							}else if(Integer.valueOf(horasViagemAux[0]) > 0){
								horaChegadaViagemDecimal = horasViagemAux[0] +"."+horasViagemAux[1];
							}
							Long labseq = ConectionDbs.generateSequence((String)obj[16]);


							try {
								pair = "'"+obj[16]+"','01','',"+
								"'MAV','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+((String)obj[4]).replace(":", "")+"','"+
								((String)obj[5]).replace(":", "")+"','"+horasViagemDecimal+"','"+horaSaidaViagemDecimal+"','"+horaChegadaViagemDecimal+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
								((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV',''";
								dbs.inserAproval(pair);
								//horas.setHasSendDbs("S");
								query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_AJUDANTE_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
								query.executeUpdate();
								manager.getTransaction().commit();
							} catch (Exception e) {
								query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_AJUDANTE_DBS = null where ID ="+((BigDecimal)obj[0]).longValue());
								query.executeUpdate();
								manager.getTransaction().commit();
								if(manager != null && manager.getTransaction().isActive()){
									manager.getTransaction().rollback();
								}
								e.printStackTrace();
								new EmailHelper().sendSimpleMail("Erro ao enviar horas de viagem para o DBS "+pair, "ERRO Horas Viagem", "rodrigo@rdrsistemas.com.br");
								continue;
							}
						}
					}
				}

				if(!((String)obj[9]).equals("0:00")){
					manager.getTransaction().begin();
					//PmpApropriacaoHoras horas = manager.find(PmpApropriacaoHoras.class, ((BigDecimal)obj[0]).longValue());
					String [] horasViagemAux = ((String)obj[9]).split(":");
					String horasViagemDecimal = "";
					DecimalFormat df = new DecimalFormat("0.00");
					if(Integer.valueOf(horasViagemAux[1]) > 0){
						Double horasMinutos = Double.valueOf(horasViagemAux[1])/60;
						horasMinutos = Double.valueOf(horasViagemAux[0]) + horasMinutos;
						horasViagemDecimal = df.format(horasMinutos).replace(",", ".");
					}else if(Integer.valueOf(horasViagemAux[0]) > 0){
						horasViagemDecimal = horasViagemAux[0] +"."+horasViagemAux[1];
					}
					
					horasViagemAux = ((String)obj[7]).split(":");
					if(!(Integer.valueOf(horasViagemAux[0]) == 8) && AprovalHelper.verificarCritidadeHoraExtra(Integer.valueOf(horasViagemAux[0]), result) != null){
						if(this.inserirIntervaloHorasTrabalho(manager, ((BigDecimal)obj[0]).longValue())){
							query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_AJUDANTE_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
							query.executeUpdate();
							manager.getTransaction().commit();
						}else{
							query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_AJUDANTE_DBS = null where ID ="+((BigDecimal)obj[0]).longValue());
							query.executeUpdate();
							manager.getTransaction().commit();
							return;
						}
					}else{
						String horaSaidaViagemDecimal = "0.00";

						if(Integer.valueOf(horasViagemAux[1]) > 0){
							Double horasMinutos = Double.valueOf(horasViagemAux[1])/60;
							horasMinutos = Double.valueOf(horasViagemAux[0]) + horasMinutos;
							horaSaidaViagemDecimal = df.format(horasMinutos).replace(",", ".");
						} else if(Integer.valueOf(horasViagemAux[0]) > 0){
							horaSaidaViagemDecimal = horasViagemAux[0] +"."+horasViagemAux[1];
						}

						horasViagemAux = ((String)obj[8]).split(":");
						if(!((Integer.valueOf(horasViagemAux[0]) == 18 && Integer.valueOf(horasViagemAux[1]) == 0) || Integer.valueOf(horasViagemAux[0]) == 5) &&  AprovalHelper.verificarCritidadeHoraExtra(Integer.valueOf(horasViagemAux[0]), result) != null){
							if(this.inserirIntervaloHorasTrabalho(manager, ((BigDecimal)obj[0]).longValue())){
								query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_AJUDANTE_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
								query.executeUpdate();
								manager.getTransaction().commit();
							}else{
								query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_AJUDANTE_DBS = null where ID ="+((BigDecimal)obj[0]).longValue());
								query.executeUpdate();
								manager.getTransaction().commit();
								return;
							}
						} else {
							String horaChegadaViagemDecimal = "0.00";

							if(Integer.valueOf(horasViagemAux[1]) > 0){
								Double horasMinutos = Double.valueOf(horasViagemAux[1])/60;
								horasMinutos = Double.valueOf(horasViagemAux[0]) + horasMinutos;
								horaChegadaViagemDecimal = df.format(horasMinutos).replace(",", ".");
							}else if(Integer.valueOf(horasViagemAux[0]) > 0){
								horaChegadaViagemDecimal = horasViagemAux[0] +"."+horasViagemAux[1];
							}


							try {
								Long labseq = ConectionDbs.generateSequence((String)obj[16]);
								pair = "'"+obj[16]+"','01','',"+
								"'MAN','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+((String)obj[7]).replace(":", "")+"','"+
								((String)obj[8]).replace(":", "")+"','"+horasViagemDecimal+"','"+horaSaidaViagemDecimal+"','"+horaChegadaViagemDecimal+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
								((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV',''";
								dbs.inserAproval(pair);
								//horas.setHasSendDbs("S");
								query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_AJUDANTE_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
								query.executeUpdate();
								manager.getTransaction().commit();
							} catch (Exception e) {
								query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_AJUDANTE_DBS = null where ID ="+((BigDecimal)obj[0]).longValue());
								query.executeUpdate();
								manager.getTransaction().commit();
								if(manager != null && manager.getTransaction().isActive()){
									manager.getTransaction().rollback();
								}
								e.printStackTrace();
								new EmailHelper().sendSimpleMail("Erro ao enviar horas trabalhadas para o DBS "+pair, "ERRO Horas Viagem", "rodrigo@rdrsistemas.com.br");
								continue;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
			EmailHelper emailHelper = new EmailHelper();
        	emailHelper.sendSimpleMail("Erro ao enviar horas para o aproval!", "Erro horas Aproval", "rodrigo@rdrsistemas.com.br");
		} finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
	}
		
	/**
	 * Insere as horas de viagem
	 * @param manager
	 * @param idAph
	 * @return
	 */
	private boolean inserirIntervaloHorasViagem(EntityManager manager, Long idAph){
		boolean isSend = false;
		String LABOTI = "";
		String pair = "";
		try{
			ExportDbs dbs = new ExportDbs();
			DecimalFormat df = new DecimalFormat("0.00");
			Query query = manager.createNativeQuery("select aph.ID"+//0
					",convert(datetime, aph.DATA, 111) as DATA"+//1
					",aph.ORIGEM"+//2
					",aph.DESTINO"+//3
					",aph.HORA_SAIDA"+//4
					",aph.HORA_CHEGADA"+//5
					",aph.HORAS_VIAGEM"+//6
					",aph.HORA_INICIO_SERVICO"+//7
					",aph.HORA_TERMINO_SERVICO"+//8
					",aph.HORAS_TRABALHO"+//9
					",aph.KM_INICIAL"+//10
					",aph.KM_FINAL"+//11
					",aph.ID_AGENDAMENTO"+//12
					",func.LOGIN"+//13
					",aph.ID_OS_PALM"+//14
					",aph.HAS_SEND_DBS" +//15
					",ag.NUM_OS" +//16
					",ag.FILIAL" +//17
					",func.EPLSNM"+//18 
					" from PMP_APROPRIACAO_HORAS aph, PMP_AGENDAMENTO ag, TW_FUNCIONARIO func"+
					//" where DATEPART (hour,(CONVERT(datetime, convert(varchar(10),ap.data, 103) + ' '+HORA_SAIDA, 103)))  >= 18"+
					//" and DATEPART (hour,(CONVERT(datetime, convert(varchar(10),ap.data, 103) + ' '+HORA_SAIDA, 103)))  < 22" +
					" where aph.id =:id" +
					" and ag.ID = aph.ID_AGENDAMENTO" +
			" and ag.ID_AJUDANTE = func.EPIDNO");
			query.setParameter("id", idAph);
			List<Object[]> atividadeList = query.getResultList(); 	
			for(Object[] obj: atividadeList){

				if(!((String)obj[6]).equals("0:00")){
					//manager.getTransaction().begin();
					//PmpApropriacaoHoras horas = manager.find(PmpApropriacaoHoras.class, Long.valueOf(19));
					//					String [] horasViagemAux = ((String)obj[6]).split(":");
					//					String horasViagemDecimal = "";
					//					if(Integer.valueOf(horasViagemAux[1]) > 0){
					//						Double horasMinutos = Double.valueOf(horasViagemAux[1])/60;
					//						horasMinutos = Double.valueOf(horasViagemAux[0]) + horasMinutos;
					//						horasViagemDecimal = df.format(horasMinutos).replace(",", ".");
					//					}else if(Integer.valueOf(horasViagemAux[0]) > 0){
					//						horasViagemDecimal = horasViagemAux[0] +"."+horasViagemAux[1];
					//					}

					String [] horasInicioViagemAux = ((String)obj[4]).split(":");
					String horaSaidaViagemDecimal = "0.00";


					Integer horaInicio = Integer.valueOf(horasInicioViagemAux[0]);


					if(Integer.valueOf(horasInicioViagemAux[1]) > 0){
						Double horasMinutos = Double.valueOf(horasInicioViagemAux[1])/60;
						horasMinutos = Double.valueOf(horasInicioViagemAux[0]) + horasMinutos;
						horaSaidaViagemDecimal = df.format(horasMinutos).replace(",", ".");
					} else if(Integer.valueOf(horasInicioViagemAux[0]) > 0){
						horaSaidaViagemDecimal = horasInicioViagemAux[0] +"."+horasInicioViagemAux[1];
					}

					String [] horasFimViagemAux = ((String)obj[5]).split(":");
					String horaChegadaViagemDecimal = "0.00";

					if(Integer.valueOf(horasFimViagemAux[1]) > 0){
						Double horasMinutos = Double.valueOf(horasFimViagemAux[1])/60;
						horasMinutos = Double.valueOf(horasFimViagemAux[0]) + horasMinutos;
						horaChegadaViagemDecimal = df.format(horasMinutos).replace(",", ".");
					}else if(Integer.valueOf(horasFimViagemAux[0]) > 0){
						horaChegadaViagemDecimal = horasFimViagemAux[0] +"."+horasFimViagemAux[1];
					}

					Integer horaFim = Integer.valueOf(horasFimViagemAux[0]);

					Double ultimaHoraLevadaDBS = Double.valueOf(horaSaidaViagemDecimal);
					for(int i = horaInicio; i <= horaFim; i++){
						CriticidadeHorasBean bean = AprovalHelper.verificarCritidadeHoraExtra(i,result);
						if(bean != null){
							if(bean.getIsDivisorHoraExtra()){
								if(i == horaFim){
									if(i == 18 && horaInicio != horaFim){
										LABOTI = "";
									} else if(i == 8){
										LABOTI = "";
										if(i > horaInicio){
											LABOTI = "1";
										}
									}else{
										LABOTI = bean.getLABOTIINFERIOR();
									}
									Double auxHoraSaidaViagemDecimal = 0d;
									if(horaInicio != horaFim){
										auxHoraSaidaViagemDecimal = Double.valueOf(i+".00") - ultimaHoraLevadaDBS;
									}else{
										auxHoraSaidaViagemDecimal = Double.valueOf(horaChegadaViagemDecimal) - ultimaHoraLevadaDBS;
									}
									
									//Double auxHoraSaidaViagemDecimal = Double.valueOf(i+".00") - ultimaHoraLevadaDBS;
									Long labseq = ConectionDbs.generateSequence((String)obj[16]);
									
									if(horaInicio != horaFim){
										pair = "'"+obj[16]+"','01','',"+
										"'MAV','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+DateHelper.converteHoraMinuto(ultimaHoraLevadaDBS.floatValue())+"','"+
										df.format(Double.valueOf(i+".00")).replace(",", "")+"','"+df.format(auxHoraSaidaViagemDecimal).replaceAll(",", ".")+"','"+df.format(ultimaHoraLevadaDBS).replaceAll(",", ".")+"','"+df.format(Double.valueOf(i+".00")).replaceAll(",", ".")+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
										((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
										
									}else{
										pair = "'"+obj[16]+"','01','',"+
										"'MAV','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+DateHelper.converteHoraMinuto(Double.valueOf(horaSaidaViagemDecimal).floatValue())+"','"+
										DateHelper.converteHoraMinuto(Double.valueOf(horaChegadaViagemDecimal).floatValue())+"','"+df.format(auxHoraSaidaViagemDecimal).replaceAll(",", ".")+"','"+df.format(Double.valueOf(horaSaidaViagemDecimal)).replaceAll(",", ".")+"','"+df.format(Double.valueOf(horaChegadaViagemDecimal)).replaceAll(",", ".").replaceAll(",", ".")+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
										((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
									}
									
									dbs.inserAproval(pair);
									//horas.setHasSendDbs("S");
									//						query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
									//						query.executeUpdate();
									//						manager.getTransaction().commit();
									isSend = true;
									ultimaHoraLevadaDBS = Double.valueOf(i+".00");
									if(Integer.valueOf(horaChegadaViagemDecimal.split("\\.")[1]) > 0 && horaInicio != horaFim){
										LABOTI = bean.getLABOTIINFERIOR();
										//LABOTI = bean.getLABOTIINFERIOR();
										if (i == 22 || i == 5 || i == 8){
											LABOTI = bean.getLABOTISUPERIOR();
										}
										
										auxHoraSaidaViagemDecimal = Double.valueOf(horaChegadaViagemDecimal) - Double.valueOf(i+".00");
										labseq = ConectionDbs.generateSequence((String)obj[16]);
										pair = "'"+obj[16]+"','01','',"+
										"'MAV','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+DateHelper.converteHoraMinuto(ultimaHoraLevadaDBS.floatValue())+"','"+
										((String)obj[5]).replace(":", "")+"','"+df.format(auxHoraSaidaViagemDecimal).replaceAll(",", ".")+"','"+df.format(Double.valueOf(i+".00")).replaceAll(",", ".")+"','"+horaChegadaViagemDecimal+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
										((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
										dbs.inserAproval(pair);
										//horas.setHasSendDbs("S");
										//						query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
										//						query.executeUpdate();
										//						manager.getTransaction().commit();
										isSend = true;
									}


								} else {
									if(i == 18){
										LABOTI = "";
									}else{
										LABOTI = bean.getLABOTIINFERIOR();
									}
									
									
									Double auxHoraSaidaViagemDecimal = Double.valueOf(i+".00") - ultimaHoraLevadaDBS;
									if(auxHoraSaidaViagemDecimal > 0){
										Long labseq = ConectionDbs.generateSequence((String)obj[16]);
										pair = "'"+obj[16]+"','01','',"+
										"'MAV','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+DateHelper.converteHoraMinuto(ultimaHoraLevadaDBS.floatValue())+"','"+
										df.format(Double.valueOf(i+".00")).replace(",", "")+"','"+df.format(auxHoraSaidaViagemDecimal).replaceAll(",", ".")+"','"+df.format(ultimaHoraLevadaDBS).replaceAll(",", ".")+"','"+df.format(Double.valueOf(i+".00")).replaceAll(",", ".")+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
										((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
										dbs.inserAproval(pair);
										//horas.setHasSendDbs("S");
										//						query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
										//						query.executeUpdate();
										//						manager.getTransaction().commit();
										isSend = true;
										ultimaHoraLevadaDBS = Double.valueOf(i+".00");
									}
								}
							}else if(i == horaFim){
								LABOTI = bean.getLABOTIINFERIOR();
								Double auxHoraSaidaViagemDecimal = Double.valueOf(horaChegadaViagemDecimal) -  ultimaHoraLevadaDBS;
								Long labseq = ConectionDbs.generateSequence((String)obj[16]);
								pair = "'"+obj[16]+"','01','',"+
								"'MAV','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+DateHelper.converteHoraMinuto(ultimaHoraLevadaDBS.floatValue())+"','"+
								((String)obj[5]).replace(":", "")+"','"+df.format(auxHoraSaidaViagemDecimal).replaceAll(",", ".")+"','"+df.format(ultimaHoraLevadaDBS).replaceAll(",", ".")+"','"+horaChegadaViagemDecimal+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
								((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
								dbs.inserAproval(pair);
								//horas.setHasSendDbs("S");
								//						query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
								//						query.executeUpdate();
								//						manager.getTransaction().commit();
								isSend = true;
							}
						}else if(i == horaFim){
							LABOTI = "";
							Double auxHoraSaidaViagemDecimal = Double.valueOf(horaChegadaViagemDecimal) -  ultimaHoraLevadaDBS;
							Long labseq = ConectionDbs.generateSequence((String)obj[16]);
							pair = "'"+obj[16]+"','01','',"+
							"'MAV','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+DateHelper.converteHoraMinuto(ultimaHoraLevadaDBS.floatValue())+"','"+
							((String)obj[5]).replace(":", "")+"','"+df.format(auxHoraSaidaViagemDecimal).replaceAll(",", ".")+"','"+df.format(ultimaHoraLevadaDBS).replaceAll(",", ".")+"','"+horaChegadaViagemDecimal+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
							((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
							dbs.inserAproval(pair);
							//horas.setHasSendDbs("S");
							//						query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
							//						query.executeUpdate();
							//						manager.getTransaction().commit();
							isSend = true;
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			isSend = false;
			new EmailHelper().sendSimpleMail("Erro ao enviar horas de viagem crítica para o DBS "+pair, "ERRO Horas Viagem", "rodrigo@rdrsistemas.com.br");
		}
		return isSend;
	}
	
	/**
	 * Insere as horas de trabalho
	 * @param manager
	 * @param idAph
	 * @return
	 */
	private boolean inserirIntervaloHorasTrabalho(EntityManager manager, Long idAph){		
		boolean isSend = false;
		String LABOTI = "";
		String pair = "";
		DecimalFormat df = new DecimalFormat("0.00");
		try{
			ExportDbs dbs = new ExportDbs();
			Query query = manager.createNativeQuery("select aph.ID"+//0
					",convert(datetime, aph.DATA, 111) as DATA"+//1
					",aph.ORIGEM"+//2
					",aph.DESTINO"+//3
					",aph.HORA_SAIDA"+//4
					",aph.HORA_CHEGADA"+//5
					",aph.HORAS_VIAGEM"+//6
					",aph.HORA_INICIO_SERVICO"+//7
					",aph.HORA_TERMINO_SERVICO"+//8
					",aph.HORAS_TRABALHO"+//9
					",aph.KM_INICIAL"+//10
					",aph.KM_FINAL"+//11
					",aph.ID_AGENDAMENTO"+//12
					",func.LOGIN"+//13
					",aph.ID_OS_PALM"+//14
					",aph.HAS_SEND_DBS" +//15
					",ag.NUM_OS" +//16
					",ag.FILIAL" +//17
					",func.EPLSNM"+//18 
					" from PMP_APROPRIACAO_HORAS aph, PMP_AGENDAMENTO ag, TW_FUNCIONARIO func"+
					//" where DATEPART (hour,(CONVERT(datetime, convert(varchar(10),ap.data, 103) + ' '+HORA_SAIDA, 103)))  >= 18"+
					//" and DATEPART (hour,(CONVERT(datetime, convert(varchar(10),ap.data, 103) + ' '+HORA_SAIDA, 103)))  < 22" +
					" where aph.id =:id" +
					" and ag.ID = aph.ID_AGENDAMENTO" +
			" and ag.ID_AJUDANTE = func.EPIDNO");
			query.setParameter("id", idAph);
			List<Object[]> atividadeList = query.getResultList(); 	

			for(Object[] obj: atividadeList){
				if(!((String)obj[9]).equals("0:00")){
					//manager.getTransaction().begin();
					//PmpApropriacaoHoras horas = manager.find(PmpApropriacaoHoras.class, ((BigDecimal)obj[0]).longValue());
					//					String [] horasViagemAux = ((String)obj[9]).split(":");
					//					String horasViagemDecimal = "";
					//					DecimalFormat df = new DecimalFormat("0.00");
					//					if(Integer.valueOf(horasViagemAux[1]) > 0){
					//						Double horasMinutos = Double.valueOf(horasViagemAux[1])/60;
					//						horasMinutos = Double.valueOf(horasViagemAux[0]) + horasMinutos;
					//						horasViagemDecimal = df.format(horasMinutos).replace(",", ".");
					//					}else if(Integer.valueOf(horasViagemAux[0]) > 0){
					//						horasViagemDecimal = horasViagemAux[0] +"."+horasViagemAux[1];
					//					}

					String [] horasViagemAux = ((String)obj[7]).split(":");
					String horaSaidaViagemDecimal = "0.00";

					Integer horaInicio = Integer.valueOf(horasViagemAux[0]);

					if(Integer.valueOf(horasViagemAux[1]) > 0){
						Double horasMinutos = Double.valueOf(horasViagemAux[1])/60;
						horasMinutos = Double.valueOf(horasViagemAux[0]) + horasMinutos;
						horaSaidaViagemDecimal = df.format(horasMinutos).replace(",", ".");
					} else if(Integer.valueOf(horasViagemAux[0]) > 0){
						horaSaidaViagemDecimal = horasViagemAux[0] +"."+horasViagemAux[1];
					}

					horasViagemAux = ((String)obj[8]).split(":");
					String horaChegadaViagemDecimal = "0.00";

					if(Integer.valueOf(horasViagemAux[1]) > 0){
						Double horasMinutos = Double.valueOf(horasViagemAux[1])/60;
						horasMinutos = Double.valueOf(horasViagemAux[0]) + horasMinutos;
						horaChegadaViagemDecimal = df.format(horasMinutos).replace(",", ".");
					}else if(Integer.valueOf(horasViagemAux[0]) > 0){
						horaChegadaViagemDecimal = horasViagemAux[0] +"."+horasViagemAux[1];
					}

					Integer horaFim = Integer.valueOf(horasViagemAux[0]);


					Double ultimaHoraLevadaDBS = Double.valueOf(horaSaidaViagemDecimal);
					for(int i = horaInicio; i <= horaFim; i++){
						CriticidadeHorasBean bean = AprovalHelper.verificarCritidadeHoraExtra(i,result);
						if(bean != null){
							if(bean.getIsDivisorHoraExtra()){
								if(i == horaFim){
									
									if(i == 18 && horaInicio != horaFim){
										LABOTI = "";
									} else if(i == 8){
										LABOTI = "";
										if(i > horaInicio){
											LABOTI = "1";
										}
									}else{
										LABOTI = bean.getLABOTIINFERIOR();
									}
									Double auxHoraSaidaViagemDecimal = 0d;
									if(horaInicio != horaFim){
										auxHoraSaidaViagemDecimal = Double.valueOf(i+".00") - ultimaHoraLevadaDBS;
									}else{
										auxHoraSaidaViagemDecimal = Double.valueOf(horaChegadaViagemDecimal) - ultimaHoraLevadaDBS;
									}
									Long labseq = ConectionDbs.generateSequence((String)obj[16]);
//									pair = "'"+obj[16]+"','01','',"+
//									"'MAV','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+((String)obj[7]).replace(":", "")+"','"+
//									((String)obj[8]).replace(":", "")+"','"+df.format(auxHoraSaidaViagemDecimal)+"','"+df.format(ultimaHoraLevadaDBS)+"','"+df.format(Double.valueOf(horaFim+".00"))+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
//									((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
//									dbs.inserAproval(pair);
									if(horaInicio != horaFim){
										pair = "'"+obj[16]+"','01','',"+
										"'MAN','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+DateHelper.converteHoraMinuto(ultimaHoraLevadaDBS.floatValue())+"','"+
										df.format(Double.valueOf(i+".00")).replace(",", "")+"','"+df.format(auxHoraSaidaViagemDecimal).replaceAll(",", ".")+"','"+df.format(ultimaHoraLevadaDBS).replaceAll(",", ".")+"','"+df.format(Double.valueOf(i+".00")).replaceAll(",", ".")+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
										((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
									}else{
										pair = "'"+obj[16]+"','01','',"+
										"'MAN','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+DateHelper.converteHoraMinuto(Double.valueOf(horaSaidaViagemDecimal).floatValue())+"','"+
										DateHelper.converteHoraMinuto(Double.valueOf(horaChegadaViagemDecimal).floatValue())+"','"+df.format(auxHoraSaidaViagemDecimal).replaceAll(",", ".")+"','"+df.format(Double.valueOf(horaSaidaViagemDecimal)).replaceAll(",", ".")+"','"+df.format(Double.valueOf(horaChegadaViagemDecimal)).replaceAll(",", ".").replaceAll(",", ".")+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
										((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
									}
									dbs.inserAproval(pair);
									
									//horas.setHasSendDbs("S");
									//						query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
									//						query.executeUpdate();
									//						manager.getTransaction().commit();
									isSend = true;
									ultimaHoraLevadaDBS = Double.valueOf(i+".00");
									if(Integer.valueOf(horaChegadaViagemDecimal.split("\\.")[1]) > 0 && horaInicio != horaFim){
										LABOTI = bean.getLABOTIINFERIOR();
										if (i == 22 || i == 5 || i == 8){
											LABOTI = bean.getLABOTISUPERIOR();
										}
										auxHoraSaidaViagemDecimal = Double.valueOf(horaChegadaViagemDecimal) - Double.valueOf(i+".00");
										labseq = ConectionDbs.generateSequence((String)obj[16]);
//										pair = "'"+obj[16]+"','01','',"+
//										"'MAV','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+((String)obj[7]).replace(":", "")+"','"+
//										((String)obj[8]).replace(":", "")+"','"+df.format(auxHoraSaidaViagemDecimal)+"','"+df.format(Double.valueOf(horaFim+".00"))+"','"+horaChegadaViagemDecimal+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
//										((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
										
										pair = "'"+obj[16]+"','01','',"+
										"'MAN','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+DateHelper.converteHoraMinuto(ultimaHoraLevadaDBS.floatValue())+"','"+
										((String)obj[8]).replace(":", "")+"','"+df.format(auxHoraSaidaViagemDecimal).replaceAll(",", ".")+"','"+df.format(Double.valueOf(i+".00")).replaceAll(",", ".")+"','"+horaChegadaViagemDecimal+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
										((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
										dbs.inserAproval(pair);
										//horas.setHasSendDbs("S");
										//						query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
										//						query.executeUpdate();
										//						manager.getTransaction().commit();
										isSend = true;
									}


								} else {
									if(i == 18){
										LABOTI = "";
									}else{
										LABOTI = bean.getLABOTIINFERIOR();
									}
									//Double auxHoraSaidaViagemDecimal = Double.valueOf(horaFim+".00") - ultimaHoraLevadaDBS;
//									pair = "'"+obj[16]+"','01','',"+
//									"'MAV','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+((String)obj[7]).replace(":", "")+"','"+
//									((String)obj[8]).replace(":", "")+"','"+df.format(auxHoraSaidaViagemDecimal)+"','"+df.format(ultimaHoraLevadaDBS)+"','"+df.format(Double.valueOf(horaFim+".00"))+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
//									((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
//									dbs.inserAproval(pair);
//									//horas.setHasSendDbs("S");
//									//						query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
//									//						query.executeUpdate();
//									//						manager.getTransaction().commit();
//									isSend = true;
//									ultimaHoraLevadaDBS = Double.valueOf(horaFim+".00");
									Double auxHoraSaidaViagemDecimal = Double.valueOf(i+".00") - ultimaHoraLevadaDBS;
									if(auxHoraSaidaViagemDecimal > 0){
										Long labseq = ConectionDbs.generateSequence((String)obj[16]);
										pair = "'"+obj[16]+"','01','',"+
										"'MAN','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+DateHelper.converteHoraMinuto(ultimaHoraLevadaDBS.floatValue())+"','"+
										df.format(Double.valueOf(i+".00")).replace(",", "")+"','"+df.format(auxHoraSaidaViagemDecimal).replaceAll(",", ".")+"','"+df.format(ultimaHoraLevadaDBS).replaceAll(",", ".")+"','"+df.format(Double.valueOf(i+".00")).replaceAll(",", ".")+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
										((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
										dbs.inserAproval(pair);
										//horas.setHasSendDbs("S");
										//						query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
										//						query.executeUpdate();
										//						manager.getTransaction().commit();
										isSend = true;
										ultimaHoraLevadaDBS = Double.valueOf(i+".00");
									}
								}
							}else if(i == horaFim){
								LABOTI = bean.getLABOTIINFERIOR();
//								Double auxHoraSaidaViagemDecimal = Double.valueOf(horaChegadaViagemDecimal) -  ultimaHoraLevadaDBS;
//								Long labseq = ConectionDbs.generateSequence((String)obj[16]);
//								pair = "'"+obj[16]+"','01','',"+
//								"'MAV','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+((String)obj[7]).replace(":", "")+"','"+
//								((String)obj[8]).replace(":", "")+"','"+df.format(auxHoraSaidaViagemDecimal)+"','"+df.format(ultimaHoraLevadaDBS)+"','"+horaChegadaViagemDecimal+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
//								((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
//								dbs.inserAproval(pair);
								
								Double auxHoraSaidaViagemDecimal = Double.valueOf(horaChegadaViagemDecimal) -  ultimaHoraLevadaDBS;
								Long labseq = ConectionDbs.generateSequence((String)obj[16]);
								pair = "'"+obj[16]+"','01','',"+
								"'MAN','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+DateHelper.converteHoraMinuto(ultimaHoraLevadaDBS.floatValue())+"','"+
								((String)obj[8]).replace(":", "")+"','"+df.format(auxHoraSaidaViagemDecimal).replaceAll(",", ".")+"','"+df.format(ultimaHoraLevadaDBS).replaceAll(",", ".")+"','"+horaChegadaViagemDecimal+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
								((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
								dbs.inserAproval(pair);
								//horas.setHasSendDbs("S");
								//						query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
								//						query.executeUpdate();
								//						manager.getTransaction().commit();
								isSend = true;
							}
						}else if(i == horaFim){
							LABOTI = "";
//							Double auxHoraSaidaViagemDecimal = Double.valueOf(horaChegadaViagemDecimal) -  ultimaHoraLevadaDBS;
//							Long labseq = ConectionDbs.generateSequence((String)obj[16]);
//							pair = "'"+obj[16]+"','01','',"+
//							"'MAV','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+((String)obj[7]).replace(":", "")+"','"+
//							((String)obj[8]).replace(":", "")+"','"+df.format(auxHoraSaidaViagemDecimal)+"','"+df.format(ultimaHoraLevadaDBS)+"','"+horaChegadaViagemDecimal+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
//							((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
//							dbs.inserAproval(pair);
							
							Double auxHoraSaidaViagemDecimal = Double.valueOf(horaChegadaViagemDecimal) -  ultimaHoraLevadaDBS;
							Long labseq = ConectionDbs.generateSequence((String)obj[16]);
							pair = "'"+obj[16]+"','01','',"+
							"'MAN','"+((String)obj[13]).toUpperCase()+"','"+((String)obj[13]).toUpperCase()+"','"+((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"','"+"1"+"','"+sdfDateAtual.format((Date)obj[1])+"','"+DateHelper.converteHoraMinuto(ultimaHoraLevadaDBS.floatValue())+"','"+
							((String)obj[8]).replace(":", "")+"','"+df.format(auxHoraSaidaViagemDecimal).replaceAll(",", ".")+"','"+df.format(ultimaHoraLevadaDBS).replaceAll(",", ".")+"','"+horaChegadaViagemDecimal+"','"+IConstantAccess.SISTEMA+"','N','W','"+(String)obj[18]+"','"+labseq+"',"+"'"+
							((((BigDecimal)obj[17]).intValue() < 10)?"0"+((BigDecimal)obj[17]).intValue():((BigDecimal)obj[17]).intValue())+"SV','"+LABOTI+"'";
							dbs.inserAproval(pair);
							//horas.setHasSendDbs("S");
							//						query = manager.createNativeQuery("update PMP_APROPRIACAO_HORAS set HAS_SEND_DBS = 'S' where ID ="+((BigDecimal)obj[0]).longValue());
							//						query.executeUpdate();
							//						manager.getTransaction().commit();
							isSend = true;
						}
					}
				}
			}


		}catch (Exception e) {
			e.printStackTrace();
			isSend = false;
			new EmailHelper().sendSimpleMail("Erro ao enviar horas trabalhadas para o DBS "+pair, "ERRO Horas Viagem", "rodrigo@rdrsistemas.com.br");
		}
		return isSend;
	}
	
	public static void main(String[] args) {
		System.out.println(Math.round(5.4f));
	}
	
//	private CriticidadeHorasBean verificarCritidadeHoraExtra(int horas){
//		for (CriticidadeHorasBean bean : result) {
//			if(bean.getHoras() == horas){
//				return bean;
//			}
//		}
//		return null;
//	}  
	
			
	
}
