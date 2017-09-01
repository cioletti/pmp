package com.pmp.util;

import java.io.IOException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobLimparLog implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Runtime r = Runtime.getRuntime();  
		try {
			r.exec("rm /var/lib/tomcat6/*gif");
		} catch (IOException e) {
			new EmailHelper().sendSimpleMail("Erro na Limpeza de Imagens", "ERRO na Limpeza de Imagens", "rodrigo@rdrsistemas.com.br");
			e.printStackTrace();
		}
		try {
			r.exec("rm /var/lib/tomcat6/*GIF");
		} catch (IOException e) {
			new EmailHelper().sendSimpleMail("Erro na Limpeza de Imagens", "ERRO na Limpeza de Imagens", "rodrigo@rdrsistemas.com.br");
			e.printStackTrace();
		}
		try {
			r.exec("rm /var/lib/tomcat6/*jpg");
		} catch (IOException e) {
			new EmailHelper().sendSimpleMail("Erro na Limpeza de Imagens", "ERRO na Limpeza de Imagens", "rodrigo@rdrsistemas.com.br");
			e.printStackTrace();
		}
		try {
			r.exec("rm /var/lib/tomcat6/*xml");
		} catch (IOException e) {
			new EmailHelper().sendSimpleMail("Erro na Limpeza de Imagens", "ERRO na Limpeza de Imagens", "rodrigo@rdrsistemas.com.br");
			e.printStackTrace();
		}
		
	}

}
