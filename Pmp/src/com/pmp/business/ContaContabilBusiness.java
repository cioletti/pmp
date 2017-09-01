package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.ContaContabilBean;
import com.pmp.entity.PmpContaContabil;
import com.pmp.util.JpaUtil;

public class ContaContabilBusiness {

	public List<ContaContabilBean> findAllContaContabil(){
		List<ContaContabilBean> cc = new ArrayList<ContaContabilBean>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("From PmpContaContabil");
			List<PmpContaContabil> result = query.getResultList();
			for (PmpContaContabil contaContabil : result) {
				ContaContabilBean bean = new ContaContabilBean();
				bean.formBean(contaContabil);
				cc.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return cc;
	}
	
	public ContaContabilBean saveOrUpdate(ContaContabilBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpContaContabil conta = null;
			if(bean.getId() == null || bean.getId() == 0){
				conta = new PmpContaContabil();
				bean.toBean(conta);
				manager.persist(conta);
			}else{
				conta = manager.find(PmpContaContabil.class, bean.getId());
				bean.toBean(conta);
				manager.merge(conta);
			}
			manager.getTransaction().commit();
			bean.setId(conta.getId());
			return bean;
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
		return null;
	}
	
	public Boolean remover(ContaContabilBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpContaContabil.class, bean.getId()));
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
}
