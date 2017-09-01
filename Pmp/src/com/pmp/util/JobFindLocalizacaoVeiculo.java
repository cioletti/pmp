package com.pmp.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import webservicelinkultimas.Posicao;
import webservicelinkultimas.Retorno;
import webservicelinkultimas.WsUltimas;
import webservicelinkultimas.WsUltimasPortType;
import webservicelinkultimas.WsUltimas_Impl;

import com.pmp.entity.AdmLocalizacaoVeiculo;

public class JobFindLocalizacaoVeiculo implements Job{
	final int id = 78; // colocar 0 id - 15 = Teste
	final String pass = "872b578e9e7c8c3b9601681950e161c3"; // colocar a senha
	final static SimpleDateFormat sdfF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt", "BR"));
	final static SimpleDateFormat sdfP = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt", "BR"));
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		EntityManager manager = null;
		Query query = null;

		try {
			manager = JpaUtil.getInstance();

			WsUltimas service = new WsUltimas_Impl();
            WsUltimasPortType port = service.getWsUltimasPort();
            
            Posicao[] posicao = null;
            Retorno retorno = null;
            
            retorno = port.recebeUltimas("pesa", "LINK1234");
            posicao = retorno.getResposta();

            if(posicao.length == 0){
            	new EmailHelper().sendSimpleMail("Nenhum veiculo localizado", "ERRO NENHUM VEICULO LOCALIZADO", "rodrigo@rdrsistemas.com.br");
            	return;
            }
			//InterfaceExternaServicePortTypeProxy proxy = new InterfaceExternaServicePortTypeProxy(); 
			sdfP.setTimeZone(TimeZone.getTimeZone("GMT-0"));
			sdfF.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
			Long count = 0L;

			try
			{
//				RetornoInterfaceExterna ie = proxy.obterUltimaPosicao(id, pass);
//				Evento[] evts = ie.getEventos();
//				if ( evts != null && evts.length != 0 ){
					manager.getTransaction().begin();
					query = manager.createNativeQuery("delete from ADM_LOCALIZACAO_VEICULO");
					query.executeUpdate();
					 for(int i=0;i < posicao.length; i++) {
						count++;
//						if ( posicao == null ){
//							//System.out.println(count + " --->> vazio...");
//							continue;
//						}
						try{
							Date dataequipamento = sdfP.parse(posicao[i].getData_hora_gravacao());
							// gravar em algum lugar
							AdmLocalizacaoVeiculo admLocalizacaoVeiculo = new AdmLocalizacaoVeiculo();
							admLocalizacaoVeiculo.setDataReport(sdfF.format(dataequipamento));
							admLocalizacaoVeiculo.setId(count);
							admLocalizacaoVeiculo.setLatitude(((Float)posicao[i].getLatitude()).toString());
							admLocalizacaoVeiculo.setLongitude(((Float)posicao[i].getLongitude()).toString());
							admLocalizacaoVeiculo.setLocalizacao(posicao[i].getLogradouro());
							admLocalizacaoVeiculo.setPlaca(posicao[i].getPlaca().replace("-", ""));
							admLocalizacaoVeiculo.setVelocidade(Long.valueOf(posicao[i].getVelocidade()));
							manager.persist(admLocalizacaoVeiculo);
							// as datas est‹o em UTC
							//System.out.println(count + " --->> " + evt.getPlaca() + "," + sdfF.format(dataequipamento) + "," + evt.getVelocidade()+"."+evt.getLatitude());
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
					manager.getTransaction().commit();
				//}
			}
			catch (Exception e)
			{
				if(manager != null && manager.getTransaction().isActive()){
					manager.getTransaction().rollback();
				}
				EmailHelper emailHelper = new EmailHelper();
	        	emailHelper.sendSimpleMail("Erro ao executar rotina de localizar veículo!", "Erro rotina de localizar veículo", "rodrigo@rdrsistemas.com.br");
				e.printStackTrace();
			}

		}catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			EmailHelper emailHelper = new EmailHelper();
        	emailHelper.sendSimpleMail("Erro ao executar rotina de localizar veículo!", "Erro rotina de localizar veículo", "rodrigo@rdrsistemas.com.br");
			e.printStackTrace();
		} finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			JobFindLocalizacaoVeiculo veiculo = new JobFindLocalizacaoVeiculo();
			veiculo.execute(null);
		} catch (JobExecutionException e) {
			e.printStackTrace();
		}
	}
}
