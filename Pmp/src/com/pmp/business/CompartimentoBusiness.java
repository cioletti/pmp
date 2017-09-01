package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.CompartimentoBean;
import com.pmp.entity.PmpCompartimentos;
import com.pmp.util.JpaUtil;

public class CompartimentoBusiness {
   
	public CompartimentoBean saveCompratimento(CompartimentoBean bean){
		PmpCompartimentos compartimentos = null;
		EntityManager manager = null;

		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			if(bean.getId() == null || bean.getId() == 0){
				compartimentos = new PmpCompartimentos();
				compartimentos.setDescricao(bean.getDescricao());
				compartimentos.setModelo(bean.getModelo().toUpperCase());
				compartimentos.setCodigo(bean.getCodigo().toUpperCase());
				manager.persist(compartimentos);
			}else{
				compartimentos = manager.find(PmpCompartimentos.class, bean.getId());
				compartimentos.setDescricao(bean.getDescricao());
				compartimentos.setModelo(bean.getModelo().toUpperCase());
				compartimentos.setCodigo(bean.getCodigo().toUpperCase());
				manager.merge(compartimentos);
			}
			manager.getTransaction().commit();
			bean.setId(compartimentos.getId());
			
		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
			return null;
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	
	public Boolean removerCompartimento(Long idCompartimento){
		EntityManager manager = null;

		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpCompartimentos compartimentos = manager.find(PmpCompartimentos.class, idCompartimento);
			manager.remove(compartimentos);
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
	
	public List<CompartimentoBean> findCompartimento(String modelo){
		List<CompartimentoBean> compList = new ArrayList<CompartimentoBean>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("From PmpCompartimentos c where c.modelo = :modelo");
			query.setParameter("modelo", modelo);
			List<PmpCompartimentos> list = query.getResultList();
			for (PmpCompartimentos pmpCompartimentos : list) {
				CompartimentoBean bean = new CompartimentoBean();
				bean.setId(pmpCompartimentos.getId());
				bean.setDescricao(pmpCompartimentos.getDescricao());
				bean.setModelo(pmpCompartimentos.getModelo());
				bean.setCodigo(pmpCompartimentos.getCodigo());
				compList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return compList;
	}
}
