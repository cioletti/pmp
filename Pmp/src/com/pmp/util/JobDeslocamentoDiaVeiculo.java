package com.pmp.util;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import webservices.framew2.com.br.InterfaceExternaServicePortTypeProxy;
import br.com.framew2.webservices.xsd.Componentes;
import br.com.framew2.webservices.xsd.Evento;
import br.com.framew2.webservices.xsd.RetornoInterfaceExterna;

import com.pmp.entity.AdmDeslocamento;

public class JobDeslocamentoDiaVeiculo implements Job{
	final int id = 78; // colocar 0 id - 15 = Teste
	final String pass = "872b578e9e7c8c3b9601681950e161c3"; // colocar a senha
	final static SimpleDateFormat sdfF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", new Locale("pt", "BR"));
	final static SimpleDateFormat sdfP = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", new Locale("pt", "BR"));
	final static SimpleDateFormat sdfPDateBR = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		EntityManager manager = null;
		Query query = null;

		try {
			manager = JpaUtil.getInstance();

			InterfaceExternaServicePortTypeProxy proxy = new InterfaceExternaServicePortTypeProxy(); 
			sdfP.setTimeZone(TimeZone.getTimeZone("GMT-0"));
			sdfF.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
			Long count = 0L;

			try
			{
				RetornoInterfaceExterna ie = proxy.obterUltimaPosicao(id, pass);
				Evento[] evts = ie.getEventos();
				if ( evts != null && evts.length != 0 ){
					manager.getTransaction().begin();
					Calendar calendar = Calendar.getInstance(new Locale("pt", "BR"));
					query = manager.createNativeQuery("delete from ADM_DESLOCAMENTO where convert(varchar(10),DATA_ATUALIZACAO, 103) = '"+sdfPDateBR.format(calendar.getTime())+"'");
					query.executeUpdate();
					for ( Evento evt : evts ){
						count++;
						if ( evt == null ){
							//System.out.println(count + " --->> vazio...");
							continue;
						}
						try{
							AdmDeslocamento deslocamento = new AdmDeslocamento();
							Date dataequipamento = sdfP.parse(evt.getDataEquipamento());
							deslocamento.setDataReportInicio(dataequipamento);
							Componentes [] array = evt.getComponentes();
							for (Componentes componentes : array) {
								if(componentes.getCodigo() == 10){//código 0 ignição desligada. código 1 ignição ligada 10 odometro em metros GPS 11 tensão da bateria do veículo 12 bateria do seu equipamento 31 tempo de ignição ligação 32 tempo de desligada 35 velocidade média de viagem
									deslocamento.setMetrosInicio(componentes.getEstado().longValue());//em metros
									break;
								}
							}
							deslocamento.setPlaca(evt.getPlaca());
							deslocamento.setDataAtualizacao(calendar.getTime());
							manager.persist(deslocamento);
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
					manager.getTransaction().commit();
				}
			}
			catch (RemoteException e)
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
}
