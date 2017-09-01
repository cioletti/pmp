package com.pmp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.json.simple.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pmp.bean.FilialBean;
import com.pmp.bean.PlMaquinaBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.business.LocalizacaoMaquinaBusiness;
import com.pmp.entity.PmpConfigOperacional;
import com.pmp.entity.PmpContHorasStandard;
import com.pmp.entity.PmpContrato;
import com.pmp.entity.PmpMotivosNaoFecContrato;
import com.pmp.entity.PmpStatusContrato;

public class JobSendMailOuroVerde implements Job {

	private static final String MANUTENCAO_PMP = "Manutenção PMP";
	private static int HORAS_PROXIMA_REVISAO = 50;
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		EntityManager manager = null;

		try{
			//boolean isConnection = true;
			//while(isConnection){
					manager = JpaUtil.getInstance();
					EmailHelper emailHelper = new EmailHelper();
					//envia e-mail informando que a próxima revisão está próxima ou por vencimento de horímetro ou por tempo
					Query query = manager.createNativeQuery("select ID, NUMERO_SERIE from PMP_CONTRATO where CODIGO_CLIENTE = '0001554' and is_ativo is null");
					List<Object[]> filiais = query.getResultList();
					for (Object[] pair : filiais) {
						query = manager.createNativeQuery("select  MAX(horimetro) horimetro from PMP_MAPA_PL where SERIE in('"+(String)pair[1]+"') and horimetro is not null");
						if(query.getResultList().size() > 0){
							String horimetro = (String)query.getSingleResult();
							query = manager.createNativeQuery("select HORAS_MANUTENCAO from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = "+(BigDecimal)pair[0]+" and HORAS_MANUTENCAO > "+horimetro);
							List<BigDecimal> horasManutencaoList = (List<BigDecimal>)query.getResultList();
							for (BigDecimal horasManutencao : horasManutencaoList) {

								if((horasManutencao.longValue() -  Long.valueOf(horimetro)) <= 100 && (horasManutencao.longValue() -  Long.valueOf(horimetro)) > 0){

									PmpContrato contrato =manager.find(PmpContrato.class, ((BigDecimal)pair[0]).longValue());
									String msg = contrato.getRazaoSocial()+" informamos que o equipamento com:<br> horímetro: "+horimetro+" <br>serie: "+contrato.getNumeroSerie()+" <br>contrato: "+contrato.getNumeroContrato()+"<br> Entrou no período crítico, para realizar a revisão de "+horasManutencao+" horas.";
									if(contrato.getEmailContatoComercial() != null){
										emailHelper.sendSimpleMail(msg, MANUTENCAO_PMP, contrato.getEmailContatoComercial());
									}
									if(contrato.getEmailContatoServicos() != null){
										emailHelper.sendSimpleMail(msg, MANUTENCAO_PMP, contrato.getEmailContatoServicos());
									}
									//emailHelper.sendSimpleMail(msg, MANUTENCAO_PMP, "rodrigo@rdrsistemas.com.br");
									//emailHelper.sendSimpleMail(msg, MANUTENCAO_PMP, "correa_maiky@pesa.com.br");
									emailHelper.sendSimpleMail(msg, MANUTENCAO_PMP, "moura_leonardo@pesa.com.br");
									//emailHelper.sendSimpleMail(msg, MANUTENCAO_PMP, "Costa_Flademir@pesa.com.br");
									if(contrato.getEmailChecklist() != null){
										String [] emails = contrato.getEmailChecklist().split(";");
										for (String email : emails) {
											msg = contrato.getRazaoSocial()+" informamos que o equipamento com:<br> horímetro: "+horimetro+" <br>serie: "+contrato.getNumeroSerie()+" <br>contrato: "+contrato.getNumeroContrato()+"<br> Entrou no período crítico, para realizar a revisão de "+horasManutencao+" horas.";
											emailHelper.sendSimpleMail(msg, MANUTENCAO_PMP, email);
											//emailHelper.sendSimpleMail(msg, MANUTENCAO_PMP, "cioletti.rodrigo@gmail.com");

										}
									}
								}
							}
						}
					}
					
				
			//}
		}catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}

		}
}
