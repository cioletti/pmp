package com.pmp.util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pmp.business.LocalizacaoMaquinaBusiness;

public class JobFindMaquinasMapa implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {
			LocalizacaoMaquinaBusiness business = new LocalizacaoMaquinaBusiness();
			business.findAllMaquinaMapaPl("", null);
		}catch (Exception e) {
			
			EmailHelper emailHelper = new EmailHelper();
        	emailHelper.sendSimpleMail("Erro ao recuperar Máquinas do Mapa!", "Erro ao Buscar Máquinas do MAPA", "rodrigo@rdrsistemas.com.br");
			e.printStackTrace();
		}finally{
			
			
		}
	}

}
