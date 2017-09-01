package com.pmp.read;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pmp.business.AgendamentoBusiness;

public class SaveAgendamentoPendenteJob implements Job {


	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		AgendamentoBusiness business = new AgendamentoBusiness();
		business.saveAgendamentosPendentes(null);

	}
	
	
	

}
