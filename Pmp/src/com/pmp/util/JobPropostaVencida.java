package com.pmp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobPropostaVencida implements Job {

	private static final String MANUTENCAO_PMP = "Proposta Vencida";
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		EntityManager manager = null;
		EmailHelper emailHelper = new EmailHelper();
		try{
			
				try {
					manager = JpaUtil.getInstance();
					

					Query query = manager.createNativeQuery(" select distinct p.validade_contrato-DATEDIFF( DD,c.data_criacao,GETDATE()) as dias, tw.eplsnm,  c.numero_contrato, c.numero_serie , tw.email, c.RAZAO_SOCIAL"+  
							" from pmp_contrato c, tw_funcionario tw, pmp_configuracao_precos p, pmp_config_manutencao m"+
							" where c.id_status_contrato = (select s.id from pmp_status_contrato s where s.sigla = 'CEN')"+
							" and tw.epidno = c.id_funcionario"+
							" and c.id_config_manutencao = m.id"+
							" and DATEDIFF( DD,c.data_criacao,GETDATE()) >= (p.validade_contrato - 2)"+   
							" and tw.email is not null   ");
					
					List<Object[]> obj = query.getResultList();
					for (Object[] objects : obj) {
						BigDecimal dias = (BigDecimal) objects[0];
						String nome = (String)objects[1];
						String numeroContrato = (String)objects[2];
						String numero_serie = (String)objects[3];
						String email = (String)objects[4];
						String nomeCliente = (String)objects[5];
						
						emailHelper.sendSimpleMail(nome +" gostariamos de informar que o equipamento de serie "+numero_serie+", contrato "+numeroContrato+" do cliente "+nomeCliente+" falta apenas "+dias.intValue()+" dia(s) para a proposta expirar.\nCaso o cliente não queira fechar essa proposta, favor entrar no sistema de PMP e informar o motivo do nao fechamento do contrato.", MANUTENCAO_PMP, email);
					}
					emailHelper.sendSimpleMail("Serviço de proposta vencida executado com sucesso!", MANUTENCAO_PMP, "rodrigo@rdrsistemas.com.br");
				} catch (Exception e1) {
					File file = File.createTempFile("log", ".txt", new File("."));
					 try {  
				            PrintStream ps = new PrintStream(file);  
				            e1.printStackTrace(ps);  
				        } catch (FileNotFoundException e) {  
				            e.printStackTrace();  
				        }  
					emailHelper.sendSimpleMail("Erro ao executar serviço de proposta vencida PMP", "ERRO proposta vencida", "rodrigo@rdrsistemas.com.br", file);
				}
			//}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
	}


}
