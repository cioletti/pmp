package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.DescontoPdpBean;
import com.pmp.entity.PmpDescontoPdp;
import com.pmp.util.JpaUtil;

public class DescontoPdpBusiness {

	public List<DescontoPdpBean> findAllDescontoPdp(){
		List<DescontoPdpBean> dp = new ArrayList<DescontoPdpBean>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("From PmpDescontoPdp");
			List<PmpDescontoPdp> result = query.getResultList();
			for (PmpDescontoPdp descontoPdp : result) {
				DescontoPdpBean bean = new DescontoPdpBean();
				bean.formBean(descontoPdp);
				dp.add(bean);
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return dp;
	}
	
	public DescontoPdpBean saveOrUpdate(DescontoPdpBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpDescontoPdp desconto = null;
			if(bean.getId() == null || bean.getId() == 0){
				desconto = new PmpDescontoPdp();
				bean.toBean(desconto);
				manager.persist(desconto);
			}else{
				desconto = manager.find(PmpDescontoPdp.class, bean.getId());
				bean.toBean(desconto);
				manager.merge(desconto);
			}
			manager.getTransaction().commit();
			bean.setId(desconto.getId());
			return bean;
		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return null;
	}
	
	public Boolean remover(DescontoPdpBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpDescontoPdp.class, bean.getId()));
			manager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return false;
	}
}
