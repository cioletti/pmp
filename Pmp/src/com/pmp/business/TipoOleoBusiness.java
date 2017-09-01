package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.TipoOleoBean;
import com.pmp.entity.PmpTipoOleo;
import com.pmp.util.JpaUtil;

public class TipoOleoBusiness {
   
	public TipoOleoBean saveTipoOleo(TipoOleoBean bean){
		PmpTipoOleo tipoOleo = null;
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			if(bean.getId() == null || bean.getId() == 0){
				tipoOleo = new PmpTipoOleo();
				//tipoOleo.setFabricante(bean.getFabricante().toUpperCase());
				tipoOleo.setViscosidade(bean.getViscosidade());
				//tipoOleo.setNomeComercial(bean.getNomeComercial().toUpperCase());
				manager.persist(tipoOleo);
			}else{
				tipoOleo = manager.find(PmpTipoOleo.class, bean.getId());
				//tipoOleo.setFabricante(bean.getFabricante().toUpperCase());
				tipoOleo.setViscosidade(bean.getViscosidade());
				//tipoOleo.setNomeComercial(bean.getNomeComercial().toUpperCase());
				manager.merge(tipoOleo);
			}
			manager.getTransaction().commit();
			bean.setId(tipoOleo.getId());
			
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
	public Boolean removerTipoOleo(Long idTipoOleo){
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpTipoOleo tipoOleo = manager.find(PmpTipoOleo.class, idTipoOleo);
			manager.remove(tipoOleo);
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
	
	public List<TipoOleoBean> findTipoOleo(String fabricante){
		List<TipoOleoBean> tpList = new ArrayList<TipoOleoBean>();
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("From PmpTipoOleo c where c.fabricante = :fabricante");
			query.setParameter("fabricante", fabricante);
			List<PmpTipoOleo> list = query.getResultList();
			for (PmpTipoOleo pmpTipoOleo : list) {
				TipoOleoBean bean = new TipoOleoBean();
				bean.setId(pmpTipoOleo.getId());
				bean.setFabricante(pmpTipoOleo.getFabricante());
				bean.setViscosidade(pmpTipoOleo.getViscosidade());
				bean.setNomeComercial(pmpTipoOleo.getNomeComercial());
				tpList.add(bean);
			}
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
		return tpList;
	}
	public List<TipoOleoBean> findAllTipoOleo(){
		List<TipoOleoBean> tpList = new ArrayList<TipoOleoBean>();
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("From PmpTipoOleo c order by c.viscosidade");
			List<PmpTipoOleo> list = query.getResultList();
			for (PmpTipoOleo pmpTipoOleo : list) {
				TipoOleoBean bean = new TipoOleoBean();
				bean.setId(pmpTipoOleo.getId());
				bean.setFabricante(pmpTipoOleo.getFabricante());
				bean.setViscosidade(pmpTipoOleo.getViscosidade());
				bean.setNomeComercial(pmpTipoOleo.getNomeComercial());
				tpList.add(bean);
			}
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
		return tpList;
	}
}
