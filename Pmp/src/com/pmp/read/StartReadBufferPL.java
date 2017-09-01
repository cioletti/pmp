package com.pmp.read;

import java.text.ParseException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Application Lifecycle Listener implementation class StartReadBufferPL
 *
 */
public class StartReadBufferPL implements ServletContextListener {
	private static Scheduler sched;
	private static Scheduler schedAgendamento;
    /**
     * Default constructor. 
     */
    public StartReadBufferPL() {
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	SchedulerFactory sf=new StdSchedulerFactory();
  	    try {
			sched = sf.getScheduler();
			JobDetail jd=new JobDetail("jobPl","groupPl",ReadMaquinaPlJob.class);
			//CronTrigger ct=new CronTrigger("cronTriggerPl","groupPl","50 38 9 * * ?");
			//CronTrigger ct=new CronTrigger("cronTriggerPl","groupPl","0 15 4,7,10,13,16,19 * * ?");
			CronTrigger ct=new CronTrigger("cronTriggerPl","groupPl","0 15 2,19 * * ?");
			//CronTrigger ct=new CronTrigger("cronTriggerPl","groupPl","0 23 17 * * ?");
			//CronTrigger ct=new CronTrigger("cronTriggerPl","groupPl","0 57 * * * ?");
			sched.scheduleJob(jd,ct);
			sched.start();
			
			
			schedAgendamento = sf.getScheduler();
			JobDetail jdAgendamento=new JobDetail("jobPlAgendamento","groupPlAgendamento",SaveAgendamentoPendenteJob.class);
			//CronTrigger ct=new CronTrigger("cronTriggerPl","groupPl","50 38 9 * * ?");
			//CronTrigger ct=new CronTrigger("cronTriggerPl","groupPl","0 15 4,7,10,13,16,19 * * ?");
			CronTrigger ctAgendamento =new CronTrigger("cronTriggerPlAgendamento","groupPlAgendamento","0 0 4,12,23 * * ?");
			//CronTrigger ctAgendamento =new CronTrigger("cronTriggerPlAgendamento","groupPlAgendamento","0 14 * * * ?");
			//CronTrigger ct=new CronTrigger("cronTriggerPl","groupPl","0 23 17 * * ?");
			//CronTrigger ct=new CronTrigger("cronTriggerPl","groupPl","0 57 * * * ?");
			schedAgendamento.scheduleJob(jdAgendamento,ctAgendamento);
			schedAgendamento.start();
			
			
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	try {
			sched.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
    }
    
   
	
}
