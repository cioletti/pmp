package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.DescontoPDPSpotBean;
import com.pmp.bean.DescontoPdpBean;
import com.pmp.entity.PmpDescontoPdp;
import com.pmp.entity.PmpDescontoPdpSpot;
import com.pmp.util.JpaUtil;

public class DescontoPdpSpotBusiness {

	public List<DescontoPDPSpotBean> findAllDescontoPdp(){
		List<DescontoPDPSpotBean> dp = new ArrayList<DescontoPDPSpotBean>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("From PmpDescontoPdpSpot");
			List<PmpDescontoPdpSpot> result = query.getResultList();
			for (PmpDescontoPdpSpot descontoPdp : result) {
				DescontoPDPSpotBean bean = new DescontoPDPSpotBean();
				bean.fromBean(descontoPdp);
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
	
	public DescontoPDPSpotBean saveOrUpdate(DescontoPDPSpotBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpDescontoPdpSpot desconto = null;
			if(bean.getId() == null || bean.getId() == 0){
				desconto = new PmpDescontoPdpSpot();
				bean.toBean(desconto);
				manager.persist(desconto);
			}else{
				desconto = manager.find(PmpDescontoPdpSpot.class, bean.getId());
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
	
	public Boolean remover(DescontoPDPSpotBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpDescontoPdpSpot.class, bean.getId()));
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
