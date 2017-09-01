package com.pmp.business;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.MultiVacBean;
import com.pmp.entity.PmpDescontoMultVac;
import com.pmp.util.JpaUtil;

public class DescontoMultiVacBusiness {
	public boolean saveOrUpdateDescontoMultiVac (Long valorDesconto){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			
			PmpDescontoMultVac descontoMultVac = new PmpDescontoMultVac();
			
			Query query = manager.createQuery("from PmpDescontoMultVac");
			if(query.getResultList().size() > 0){
				descontoMultVac = (PmpDescontoMultVac)query.getSingleResult();
				descontoMultVac.setDescontoMultiVac(valorDesconto);
				manager.merge(descontoMultVac);
			}else{
				descontoMultVac.setDescontoMultiVac(valorDesconto);
				manager.persist(descontoMultVac);
			}
			manager.getTransaction().commit();
			
			return true;

		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return false;
	}
	
	public MultiVacBean findMultVac(){
		MultiVacBean bean = new MultiVacBean();
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("from PmpDescontoMultVac" );
			if(query.getResultList().size()>0){
				PmpDescontoMultVac descontoMultVac = (PmpDescontoMultVac)query.getSingleResult();
				bean.setValorDesconto(descontoMultVac.getDescontoMultiVac());
				return bean;
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return null;
	}
}
