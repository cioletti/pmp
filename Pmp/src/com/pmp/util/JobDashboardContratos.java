package com.pmp.util;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pmp.bean.DashboardBean;
import com.pmp.bean.FilialBean;
import com.pmp.business.DashboardBusiness;
import com.pmp.business.OsBusiness;
import com.pmp.entity.PmpDashboardContratos;

public class JobDashboardContratos implements Job{

	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		EntityManager manager = null;
//		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
//		Properties prop = new Properties();
		try {
			Calendar calendar = Calendar.getInstance();
			if(calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
				OsBusiness businessFilial = new OsBusiness();
				Collection<FilialBean> filais = businessFilial.findAllFiliais();

				manager = JpaUtil.getInstance();
				DashboardBusiness business = new DashboardBusiness();
				for (FilialBean filialBean : filais) {
					List<DashboardBean> result = business.findAllIndicadoresCliente(filialBean.getStno());
					int count = 0;
					for (DashboardBean dashboardBean : result) {
						String [] mesAno = dashboardBean.getNomeMes7().split("/");
						PmpDashboardContratos contratos = new PmpDashboardContratos(); 
						contratos.setQuantidade(Long.valueOf(dashboardBean.getMes7()));
						contratos.setMes(Long.valueOf(mesAno[0]));
						contratos.setAno(Long.valueOf(mesAno[1]));
						contratos.setData(new Date());
						contratos.setFilial(filialBean.getStno());
						if(count == 0){
							contratos.setSigla("MDA");
						} else if(count == 1){
							contratos.setSigla("REN");
						} else if(count == 2){
							contratos.setSigla("PART");
						} else if(count == 3){
							contratos.setSigla("CUS");
						} else if(count == 4){
							contratos.setSigla("PRE");
						} else if(count == 5){
							contratos.setSigla("PLUS");
						} 
						manager.getTransaction().begin();
						manager.persist(contratos);
						manager.getTransaction().commit();
						if(count == 5){
							break;
						}
						count++;
					}
				}
			}

		}catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			EmailHelper emailHelper = new EmailHelper();
			emailHelper.sendSimpleMail("Erro ao rodar rotina de dashboard contratos", "Erro na rotina da dashboard contratos", "rodrigo@rdrsistemas.com.br");
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
			
		}
	}
	
}
