package com.pmp.util;

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
 * Application Lifecycle Listener implementation class CronImportStandardJob
 *
 */
public class CronImportStandardJob implements ServletContextListener {
	 private static Scheduler schedPmp;
	 private static Scheduler schedSendMail;
	 private static Scheduler schedSendMailOuroVerde;
	 private static Scheduler schedSendMailProximaRevisao;
	 //private static Scheduler schedSendMailStandby;
	private static Scheduler schedPropostaVencida;
	 //private static Scheduler schedEnviarRelatorio;
	 //private static Scheduler schedImportNotaFiscal;
	 private static Scheduler schedFindNumDoc;
	 private static Scheduler schedFindNumOsAgendamento;
	 private static Scheduler schedFindNumOsContrato;
	// private static Scheduler schedFindOrcamento;
	 private static Scheduler schedFindLocalizacaoVeiculo;
	 private static Scheduler schedDeslocamentoVeiculoDia;
	 private static Scheduler schedDeslocamentoVeiculoNoite;
	// private static Scheduler schedSendSmartConnection;
	 private static Scheduler schedHorasAproval;
	 private static Scheduler schedHorasAjudanteAproval;
	 private static Scheduler schedSendNotesDbs;
	 private static Scheduler schedJobNotaFiscal;
	 private static Scheduler schedJobKillProcess;
	 private static Scheduler schedJobSendDateDbs;
	 private static Scheduler schedJobSendOilDbs;
	 private static Scheduler schedJobVerificarPecasOilDbs;
	 private static Scheduler JobImportarFuncionario;
	 private static Scheduler JobLocalizarMapaPl;
	 private static Scheduler JobDashboardContratos;
	 private static Scheduler JobSenCredito;
	 
	 //private static Scheduler schedLimparLog;
    /**
     * Default constructor. 
     */
    public CronImportStandardJob() {
        
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent context) {
    	SchedulerFactory sf=new StdSchedulerFactory();
    
  	    try {
  	    	schedPmp = sf.getScheduler();
			JobDetail jd=new JobDetail("job3","group3",JobStandardJob.class);
			CronTrigger ct=new CronTrigger("cronTrigger2","group3","0 0 3 * * ?");
			schedPmp.scheduleJob(jd,ct);
			schedPmp.start();
			
			schedSendMail = sf.getScheduler();
			JobDetail jdConnection=new JobDetail("jobSendMail","groupSendMail",JobSendMail.class);
			CronTrigger ctConnection=new CronTrigger("cronTriggerSendMail","groupSendMail","0 40 5 * * ?");
			schedSendMail.scheduleJob(jdConnection,ctConnection);
			schedSendMail.start();

			schedSendMailOuroVerde = sf.getScheduler();
			JobDetail jdConnectionOuroVerde=new JobDetail("jobSendMailOuroVerde","groupSendMailOuroVerde",JobSendMailOuroVerde.class);
			CronTrigger ctConnectionOuroVerde=new CronTrigger("cronTriggerSendMailOuroVerde","groupSendMailOuroVerde","0 20 4 * * ?");
			schedSendMailOuroVerde.scheduleJob(jdConnectionOuroVerde,ctConnectionOuroVerde);
			schedSendMailOuroVerde.start();
//			
//			schedSendMailStandby = sf.getScheduler();
//			JobDetail jdConnectionStandby=new JobDetail("jobSendMailStandby","groupSendMailStandby",JobSendMailStandby.class);
//			CronTrigger ctConnectionStandby=new CronTrigger("cronTriggerSendMailStandby","groupSendMailStandby","0 0 4 * * ?");
//			schedSendMailStandby.scheduleJob(jdConnectionStandby,ctConnectionStandby);
//			schedSendMailStandby.start();
//
			schedPropostaVencida = sf.getScheduler();
			JobDetail jdPropostaVencidaConnection=new JobDetail("jobPropostaVencida","groupPropostaVencida",JobPropostaVencida.class);
			CronTrigger ctPropostaVencidaConnection=new CronTrigger("cronTriggerPropostaVencida","groupPropostaVencida","0 0 6 * * ?");
			schedPropostaVencida.scheduleJob(jdPropostaVencidaConnection,ctPropostaVencidaConnection);
			schedPropostaVencida.start();
//			
//			schedEnviarRelatorio = sf.getScheduler();
//			JobDetail jdEnviarRelatorio=new JobDetail("jobEnviarRelatorio","groupEnviarRelatorio",JobEnviarRelatorio.class);
//			CronTrigger ctEnviarRelatorioConnection=new CronTrigger("cronTriggerEnviarRelatorio","groupPropostaVencida","0 0 6 ? * MON");
//			schedEnviarRelatorio.scheduleJob(jdEnviarRelatorio,ctEnviarRelatorioConnection);
//			schedEnviarRelatorio.start();
//
//			schedImportNotaFiscal = sf.getScheduler();
//			JobDetail jdImportarNotaFiscal=new JobDetail("jobImportarNotaFiscal","groupImportarNotaFiscal",JobImportarNotaFiscal.class);
//			CronTrigger ctImportNotaFiscalConnection=new CronTrigger("cronTriggerImportarNotaFiscal","groupImportarNotaFiscal","0 0 4 * * ?");
//			schedEnviarRelatorio.scheduleJob(jdImportarNotaFiscal,ctImportNotaFiscalConnection);
//			schedEnviarRelatorio.start();
//			
			schedFindNumDoc = sf.getScheduler();
			JobDetail jdFindNumDoc=new JobDetail("jobFindNumDoc","groupFindNumDoc",JobVerificaPecasDbsPmp.class);
			CronTrigger ctFindNumDoc=new CronTrigger("cronTriggerFindNumDoc","groupFindNumDoc","10 0/1 * * * ?");
			schedFindNumDoc.scheduleJob(jdFindNumDoc,ctFindNumDoc);
			schedFindNumDoc.start();
			
			schedJobSendOilDbs = sf.getScheduler();
			JobDetail jdOil=new JobDetail("jobOil","groupOil",JobSendOilDbs.class);
			CronTrigger ctOil=new CronTrigger("cronTriggerOil","groupOil","30 15,30,45 * * * ?");
			//CronTrigger ctOil=new CronTrigger("cronTriggerOil","groupOil","0 26 * * * ?");
			schedJobSendOilDbs.scheduleJob(jdOil,ctOil);
			schedJobSendOilDbs.start();

			schedJobVerificarPecasOilDbs = sf.getScheduler();
			JobDetail jdPecasOil=new JobDetail("jobPecasOil","groupPecasOil",JobVerificarPecasOilDbs.class);
			CronTrigger ctPecasOil=new CronTrigger("cronTriggerOil","groupPecasOil","50 * * * * ?");
			//CronTrigger ctPecasOil=new CronTrigger("cronTriggerOil","groupPecasOil","0 8 * * * ?");
			schedJobVerificarPecasOilDbs.scheduleJob(jdPecasOil,ctPecasOil);
			schedJobVerificarPecasOilDbs.start();

			schedFindNumOsAgendamento = sf.getScheduler();
			JobDetail jdFindNumOsAgend=new JobDetail("jobFindNumOsAgend","groupFindNumOsAgend",FindOsEstimadaAgendamento.class);
			CronTrigger ctFindNumOsAgend=new CronTrigger("cronTriggerFindNumOsAgend","groupFindNumOsAgend","20 0/4 * * * ?");
			//CronTrigger ctFindNumOsAgend=new CronTrigger("cronTriggerFindNumOsAgend","groupFindNumOsAgend","0 48 * * * ?");
			schedFindNumOsAgendamento.scheduleJob(jdFindNumOsAgend,ctFindNumOsAgend);
			schedFindNumOsAgendamento.start();

			schedFindNumOsContrato = sf.getScheduler();
			JobDetail jdFindNumOsContrato=new JobDetail("jobFindNumOsContrato","groupFindNumOsContrato",FindOsEstimadaContrato.class);
			CronTrigger ctFindNumOsContrato=new CronTrigger("cronTriggerFindNumOsAgend","groupFindNumOsContrato","0 0/4 * * * ?");
			schedFindNumOsContrato.scheduleJob(jdFindNumOsContrato,ctFindNumOsContrato);
			schedFindNumOsContrato.start();

			/*DESCOMENTAR QUANDO O PMP COMEÇAR A RODAR EM PRODUÇÃO E A SUBSTITUIÇÃO TRIBUTÁRIA ESTIVER PRONTA*/
//			schedFindOrcamento = sf.getScheduler();
//			JobDetail jdFindOrcamento=new JobDetail("jobFindOrcamento","groupFindOrcamento",JobFindOrcamento.class);
//			CronTrigger ctFindOrcamento=new CronTrigger("cronTriggerFindOrcamento","groupFindOrcamento","0 0/1 * * * ?");
//			schedFindOrcamento.scheduleJob(jdFindOrcamento,ctFindOrcamento);
//			schedFindOrcamento.start();

//			schedFindLocalizacaoVeiculo = sf.getScheduler();
//			JobDetail jdFindLocalizacaoVeiculo=new JobDetail("jobFindLocalizacaoVeiculo","groupFindLocalizacaoVeiculo",JobFindLocalizacaoVeiculo.class);
//			CronTrigger ctFindLocalizacaoVeiculo = new CronTrigger("cronTriggerFindLocalizacaoVeiculo","groupFindLocalizacaoVeiculo","0 0/10 * * * ?");
//			//CronTrigger ctFindLocalizacaoVeiculo = new CronTrigger("cronTriggerFindLocalizacaoVeiculo","groupFindLocalizacaoVeiculo","0 37 15 * * ?");
//			schedFindLocalizacaoVeiculo.scheduleJob(jdFindLocalizacaoVeiculo,ctFindLocalizacaoVeiculo);
//			
//			schedDeslocamentoVeiculoDia = sf.getScheduler();
//			JobDetail jdDeslocamentoVeiculoDia=new JobDetail("jobDeslocamentoVeiculoDia","groupDeslocamentoVeiculoDia",JobDeslocamentoDiaVeiculo.class);
//			CronTrigger ctDeslocamentoVeiculoDia = new CronTrigger("cronTriggerDeslocamentoVeiculoDia","groupDeslocamentoVeiculoDia","0 5,10,15 0 * * ?");
//			//CronTrigger ctFindLocalizacaoVeiculo = new CronTrigger("cronTriggerFindLocalizacaoVeiculo","groupFindLocalizacaoVeiculo","0 37 15 * * ?");
//			schedDeslocamentoVeiculoDia.scheduleJob(jdDeslocamentoVeiculoDia,ctDeslocamentoVeiculoDia);
//			
//			schedDeslocamentoVeiculoNoite = sf.getScheduler();
//			JobDetail jdDeslocamentoVeiculoNoite=new JobDetail("jobDeslocamentoVeiculoNoite","groupDeslocamentoVeiculoNoite",JobDeslocamentoNoiteVeiculo.class);
//			CronTrigger ctDeslocamentoVeiculoNoite = new CronTrigger("cronTriggerDeslocamentoVeiculoNoite","groupDeslocamentoVeiculoNoite","0 45,50,55 23 * * ?");
//			//CronTrigger ctFindLocalizacaoVeiculo = new CronTrigger("cronTriggerFindLocalizacaoVeiculo","groupFindLocalizacaoVeiculo","0 37 15 * * ?");
//			schedDeslocamentoVeiculoNoite.scheduleJob(jdDeslocamentoVeiculoNoite,ctDeslocamentoVeiculoNoite);

//			/*DESCOMENTAR QUANDO O PMP COMEÇAR A RODAR EM PRODUÇÃO E O SMART CONNECTION ESTIVER FUNCIONANDO*/
//			schedSendSmartConnection = sf.getScheduler();
//			JobDetail jdschedSendSmartConnection=new JobDetail("jobschedSendSmartConnection","groupschedSendSmartConnection",JobSendBacklogSmartConnection.class);
//			CronTrigger ctschedSendSmartConnection = new CronTrigger("cronTriggerschedSendSmartConnection","groupschedSendSmartConnection","0 0/15 * * * ?");
//			schedSendSmartConnection.scheduleJob(jdschedSendSmartConnection,ctschedSendSmartConnection);
//			schedSendSmartConnection.start();

			/*DESCOMENTAR QUANDO O PMP COMEÇAR A RODAR EM PRODUÇÃO*/
			schedHorasAproval = sf.getScheduler();
			JobDetail jdschedSchedHorasAproval=new JobDetail("jobschedSchedHorasAproval","groupSchedHorasAproval",JobHorasAproval.class);
			CronTrigger ctschedSchedHorasAproval = new CronTrigger("cronTriggerSchedHorasAproval","groupSchedHorasAproval","0 0/35 * * * ?");
			//CronTrigger ctschedSchedHorasAproval = new CronTrigger("cronTriggerSchedHorasAproval","groupSchedHorasAproval","0 39 22 * * ?");
			schedHorasAproval.scheduleJob(jdschedSchedHorasAproval,ctschedSchedHorasAproval);
			schedHorasAproval.start();

			schedHorasAjudanteAproval = sf.getScheduler();
			JobDetail jdschedSchedHorasAjudanteAproval=new JobDetail("jobschedSchedHorasAjudanteAproval","groupSchedHorasAjudanteAproval",JobHorasAjudanteAproval.class);
			CronTrigger ctschedSchedHorasAjudanteAproval = new CronTrigger("cronTriggerSchedHorasAjudanteAproval","groupSchedHorasAjudanteAproval","0 10,45 * * * ?");
			//CronTrigger ctschedSchedHorasAproval = new CronTrigger("cronTriggerSchedHorasAproval","groupSchedHorasAproval","0 44 15 * * ?");
			schedHorasAjudanteAproval.scheduleJob(jdschedSchedHorasAjudanteAproval,ctschedSchedHorasAjudanteAproval);
			schedHorasAjudanteAproval.start();
			
			schedSendNotesDbs = sf.getScheduler();
			JobDetail jdschedSchedSendNotesDbs=new JobDetail("jobschedSendNotesDbs","groupSchedSendNotesDbs",JobSendNotesDbs.class);
			CronTrigger ctschedSchedSendNotesDbs = new CronTrigger("cronTriggerSchedSendNotesDbs","groupSchedSendNotesDbs","0 0 19,21 * * ?");
			//CronTrigger ctschedSchedHorasAproval = new CronTrigger("cronTriggerSchedHorasAproval","groupSchedHorasAproval","0 44 15 * * ?");
			schedSendNotesDbs.scheduleJob(jdschedSchedSendNotesDbs,ctschedSchedSendNotesDbs);
			schedSendNotesDbs.start();

			schedJobNotaFiscal = sf.getScheduler();
			JobDetail jdschedSchedJobNotaFiscalDbs=new JobDetail("jobschedJobNotaFiscalDbs","groupSchedJobNotaFiscalDbs",JobNotaFiscal.class);
			CronTrigger ctschedSchedJobNotaFiscalDbs = new CronTrigger("cronTriggerSchedJobNotaFiscalDbs","groupSchedJobNotaFiscalDbs","30 30 20,6 * * ?");
			//CronTrigger ctschedSchedJobNotaFiscalDbs = new CronTrigger("cronTriggerSchedJobNotaFiscalDbs","groupSchedJobNotaFiscalDbs","0 59 13 * * ?");
			//CronTrigger ctschedSchedHorasAproval = new CronTrigger("cronTriggerSchedHorasAproval","groupSchedHorasAproval","0 44 15 * * ?");
			schedJobNotaFiscal.scheduleJob(jdschedSchedJobNotaFiscalDbs,ctschedSchedJobNotaFiscalDbs);
			schedJobNotaFiscal.start();

			schedJobKillProcess = sf.getScheduler();
			JobDetail jdschedSchedJobKillProcess=new JobDetail("jobschedJobKillProcess","groupSchedJobKillProcess",JobKillProcess.class);
			CronTrigger ctschedSchedJobKillProcess = new CronTrigger("cronTriggerSchedJobKillProcess","groupSchedJobKillProcess","0 0/1 * * * ?");
			//CronTrigger ctschedSchedHorasAproval = new CronTrigger("cronTriggerSchedHorasAproval","groupSchedHorasAproval","0 44 15 * * ?");
			schedJobKillProcess.scheduleJob(jdschedSchedJobKillProcess,ctschedSchedJobKillProcess);
			schedJobKillProcess.start();

//			schedLimparLog = sf.getScheduler();
//			JobDetail jdschedSchedLimparLog=new JobDetail("jobschedLimparLog","groupSchedLimparLog",JobLimparLog.class);
//			CronTrigger ctschedSchedLimparLog = new CronTrigger("cronTriggerSchedLimparLog","groupSchedLimparLog","0 0/30 * * * ?");
//			//CronTrigger ctschedSchedHorasAproval = new CronTrigger("cronTriggerSchedHorasAproval","groupSchedHorasAproval","0 44 15 * * ?");
//			schedLimparLog.scheduleJob(jdschedSchedLimparLog,ctschedSchedLimparLog);
//			schedLimparLog.start();
			
			schedJobSendDateDbs = sf.getScheduler();
			JobDetail jdschedSchedJobSendDateDbs=new JobDetail("jobschedJobSendDateDbs","groupSchedJobSendDateDbs",JobSendDateDbs.class);
			CronTrigger ctschedSchedJobSendDateDbs = new CronTrigger("cronTriggerSchedJobSendDateDbs","groupSchedJobSendDateDbs","0 30 3,4,5 * * ?");
			//CronTrigger ctschedSchedHorasAproval = new CronTrigger("cronTriggerSchedHorasAproval","groupSchedHorasAproval","0 44 15 * * ?");
			schedJobSendDateDbs.scheduleJob(jdschedSchedJobSendDateDbs,ctschedSchedJobSendDateDbs);
			schedJobSendDateDbs.start();
			
			schedSendMailProximaRevisao = sf.getScheduler();
			JobDetail jdSendMailProximaRevisao=new JobDetail("jobSendMailProximaRevisao","groupSendMailProximaRevisao",JobSendMailProximaRevisao.class);
			CronTrigger ctSendMailProximaRevisao=new CronTrigger("cronTriggerSendMailProximaRevisao","groupSendMailProximaRevisao","0 20 4 * * ?");
			schedSendMailProximaRevisao.scheduleJob(jdSendMailProximaRevisao,ctSendMailProximaRevisao);
			schedSendMailProximaRevisao.start();

			JobImportarFuncionario = sf.getScheduler();
			JobDetail jdJobImportarFuncionario=new JobDetail("jobJobImportarFuncionario","groupJobImportarFuncionario",JobImportarFuncionario.class);
			CronTrigger ctJobImportarFuncionario=new CronTrigger("cronJobImportarFuncionario","groupJobImportarFuncionario","0 30 23,20,4 * * ?");
			//CronTrigger ctJobImportarFuncionario=new CronTrigger("cronJobImportarFuncionario","groupJobImportarFuncionario","0 53 * * * ?");
			JobImportarFuncionario.scheduleJob(jdJobImportarFuncionario,ctJobImportarFuncionario);
			JobImportarFuncionario.start();

			JobLocalizarMapaPl = sf.getScheduler();
			JobDetail jdJobLocalizarMapaPl=new JobDetail("jobJobLocalizarMapaPl","groupJobLocalizarMapaPl",JobFindMaquinasMapa.class);
			CronTrigger ctJobLocalizarMapaPl=new CronTrigger("cronJobLocalizarMapaPl","groupJobLocalizarMapaPl","0 0 * * * ?");
			JobLocalizarMapaPl.scheduleJob(jdJobLocalizarMapaPl,ctJobLocalizarMapaPl);
			JobLocalizarMapaPl.start();

			
			JobDashboardContratos = sf.getScheduler();
			JobDetail jdJobDashboardContratos=new JobDetail("jobJobDashboardContratos","groupJobDashboardContratos",JobDashboardContratos.class);
			CronTrigger ctJobDashboardContratos=new CronTrigger("cronJobDashboardContratos","groupJobDashboardContratos","0 0 23 * * ?");
			JobDashboardContratos.scheduleJob(jdJobDashboardContratos,ctJobDashboardContratos);
			JobDashboardContratos.start();

			JobSenCredito = sf.getScheduler();
			JobDetail jdJobSenCredito = new JobDetail("jobSenCredito","groupJobSenCredito",JobSolicitacaoCredito.class);
			//CronTrigger ctJobSenCredito = new CronTrigger("cronJobSenCredito","groupJobSenCredito","0 0/5 * * * ?");
			CronTrigger ctJobSenCredito = new CronTrigger("cronJobSenCredito","groupJobSenCredito","0 0/3 * * * ?");
			//CronTrigger ctJobSenCredito = new CronTrigger("cronJobSenCredito","groupJobSenCredito","0 50 * * * ?");
			JobSenCredito.scheduleJob(jdJobSenCredito,ctJobSenCredito);
			JobSenCredito.start();
			
			
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
        	schedPmp.shutdown();
			schedSendMail.shutdown();
			schedSendMailOuroVerde.shutdown();
			schedSendMailProximaRevisao.shutdown();
			//schedSendMailStandby.shutdown();
			schedPropostaVencida.shutdown();
			//schedEnviarRelatorio.shutdown();
			//schedEnviarRelatorio.shutdown();
			//schedImportNotaFiscal.shutdown();
			schedFindNumDoc.shutdown();
			schedFindNumOsAgendamento.shutdown();
			schedFindNumOsContrato.shutdown();
			schedFindLocalizacaoVeiculo.shutdown();
			schedDeslocamentoVeiculoNoite.shutdown();
			schedDeslocamentoVeiculoDia.shutdown();
//			schedFindOrcamento.shutdown();
			//schedSendSmartConnection.shutdown();
			schedHorasAproval.shutdown();
			schedHorasAjudanteAproval.shutdown();
			schedSendNotesDbs.shutdown();
			schedJobNotaFiscal.shutdown();
			schedJobKillProcess.shutdown();
			schedJobSendDateDbs.shutdown();
			schedJobSendOilDbs.shutdown();
			schedJobVerificarPecasOilDbs.shutdown();
			JobImportarFuncionario.shutdown();
			JobLocalizarMapaPl.shutdown();
			JobDashboardContratos.shutdown();
			JobSenCredito.shutdown();
			//schedLimparLog.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
    }
	
}
