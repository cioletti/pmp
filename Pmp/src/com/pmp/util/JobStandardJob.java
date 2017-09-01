package com.pmp.util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pmp.business.ContratoBusiness;

public class JobStandardJob implements Job {
	
//	private boolean isLoadService = true;
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		ContratoBusiness business = new ContratoBusiness();
		business.buscarStanderJob();
		business.savePrecoZoho();
		//}
	
	}

	

}
