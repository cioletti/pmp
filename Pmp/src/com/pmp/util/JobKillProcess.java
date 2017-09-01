package com.pmp.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobKillProcess implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
        EntityManager manager = null;
        try {
        	manager = JpaUtil.getInstance();

        	Query query = manager.createNativeQuery("select s.spid"+
        									  " from sys.sysprocesses  s where s.loginame = 'control_dse'"+
        									  " and (DATEDIFF(SECOND, CONVERT(date,s.last_batch),s.last_batch)) - (DATEDIFF(SECOND, CONVERT(date,s.login_time),s.login_time)) > 240"+
        									  " and s.blocked <> 0");
//        	Query query = manager.createNativeQuery("SELECT spid FROM MASTER.DBO.SYSPROCESSES BLOCKING WHERE BLOCKING.BLOCKED = 0 AND EXISTS"+
//        											" (SELECT 1 FROM MASTER.DBO.SYSPROCESSES BLOCKED WHERE BLOCKED.BLOCKED = BLOCKING.SPID );");
        	List<Short> spidList = query.getResultList();
        	for (Short spid : spidList) {
				try {
					manager.getTransaction().begin();
					query = manager.createNativeQuery("kill "+spid);
					query.executeUpdate();
					manager.getTransaction().commit();
				} catch (Exception e) {
					if(manager != null && manager.getTransaction().isActive()){
		        		manager.getTransaction().rollback();
		        	}
				}
        	}
        } catch (Exception e) {
        	if(manager != null && manager.getTransaction().isActive()){
        		manager.getTransaction().rollback();
        	}
        	EmailHelper emailHelper = new EmailHelper();
        	emailHelper.sendSimpleMail("Erro ao matar processo!", "Erro ao matar processo!", "rodrigo@rdrsistemas.com.br");
        	e.printStackTrace();

        }finally{
        	if(manager != null && manager.isOpen()){
        		manager.close();
        	}

        }

	}

}
