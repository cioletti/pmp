package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.ClienteInterBean;
import com.pmp.entity.PmpClienteInter;
import com.pmp.util.JpaUtil;

public class ClienteInterBusiness {

	public List<ClienteInterBean> findAllClienteInter(){
		List<ClienteInterBean> ci = new ArrayList<ClienteInterBean>();

		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("From PmpClienteInter order by descricao");
			List<PmpClienteInter> result = query.getResultList();
			for (PmpClienteInter clienteInter : result) {
				ClienteInterBean bean = new ClienteInterBean();
				bean.formBean(clienteInter);
				ci.add(bean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return ci;
	}
	
	public List<ClienteInterBean> findClienteInterBySearchKey(String searchKey){
		List<ClienteInterBean> ci = new ArrayList<ClienteInterBean>();

		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("From PmpClienteInter Where searchKey Like :searchKey");
			query.setParameter("searchKey", "%" + searchKey + "%");
			
			List<PmpClienteInter> result = query.getResultList();
			for (PmpClienteInter clienteInter : result) {
				ClienteInterBean bean = new ClienteInterBean();
				bean.formBean(clienteInter);
				ci.add(bean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return ci;
	}
	
	public ClienteInterBean saveOrUpdate(ClienteInterBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpClienteInter cliente = null;
			if(bean.getId() == null || bean.getId() == 0){
				cliente = new PmpClienteInter();
				bean.toBean(cliente);
				manager.persist(cliente);
			}else{
				cliente = manager.find(PmpClienteInter.class, bean.getId());
				bean.toBean(cliente);
				manager.merge(cliente);
			}
			manager.getTransaction().commit();
			bean.setId(cliente.getId());
			return bean;
		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return null;
	}
	
	public Boolean remover(ClienteInterBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpClienteInter.class, bean.getId()));
			manager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return false;
	}
}
