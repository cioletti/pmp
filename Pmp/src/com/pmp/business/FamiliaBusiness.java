package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.FamiliaBean;
import com.pmp.entity.PmpFamilia;
import com.pmp.util.JpaUtil;

public class FamiliaBusiness {

	public List<FamiliaBean> findAllFamilia(String contExcessao) {
		List<FamiliaBean> familiaList = new ArrayList<FamiliaBean>();
		EntityManager manager = null;

		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("From PmpFamilia where id in( select cm.idFamilia.id from PmpConfigManutencao cm where cm.contExcessao = '"+contExcessao+"' and cm.isGeradorStandby = 'N' and cm.isAtivo is null) order by descricao");
			List<PmpFamilia> result = query.getResultList();
			for (PmpFamilia familia : result) {
				FamiliaBean bean = new FamiliaBean();
				bean.setId(familia.getId());
				bean.setDescricao(familia.getDescricao());
				familiaList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return familiaList;
	}
	public List<FamiliaBean> findAllFamiliaPromocao(String contExcessao) {
		List<FamiliaBean> familiaList = new ArrayList<FamiliaBean>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createQuery("From PmpFamilia where id in( select cm.idFamilia.id from PmpConfigManutencao cm where cm.contExcessao = '"+contExcessao+"' and cm.isGeradorStandby = 'N' and cm.isAtivo is null) and descricao in ('RETROESCAVADEIRA') order by descricao");
			List<PmpFamilia> result = query.getResultList();
			for (PmpFamilia familia : result) {
				FamiliaBean bean = new FamiliaBean();
				bean.setId(familia.getId());
				bean.setDescricao(familia.getDescricao());
				familiaList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return familiaList;
	}
	
	public List<FamiliaBean> findAllFamiliaGerador(String contExcessao, String descricao) {
		List<FamiliaBean> familiaList = new ArrayList<FamiliaBean>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createQuery("From PmpFamilia where id in( select cm.idFamilia.id from PmpConfigManutencao cm where cm.contExcessao = '"+contExcessao+"' and cm.isGeradorStandby = 'S' and cm.isAtivo is null) order by descricao");
			List<PmpFamilia> result = query.getResultList();
			for (PmpFamilia familia : result) {
				FamiliaBean bean = new FamiliaBean();
				bean.setId(familia.getId());
				bean.setDescricao(familia.getDescricao());
				familiaList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return familiaList;
	}
	
	public List<FamiliaBean> findAllFamilia() {
		List<FamiliaBean> familiaList = new ArrayList<FamiliaBean>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createQuery("FROM PmpFamilia ORDER BY descricao");
			List<PmpFamilia> result = query.getResultList();
			for (PmpFamilia familia : result) {
				FamiliaBean bean = new FamiliaBean();
				bean.setId(familia.getId());
				bean.setDescricao(familia.getDescricao());
				familiaList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return familiaList;
	}
	
	public List<FamiliaBean> findAllFamiliaGerador(String descricao) {
		List<FamiliaBean> familiaList = new ArrayList<FamiliaBean>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createQuery("FROM PmpFamilia WHERE descricao LIKE '%" + descricao.toUpperCase() + "%' ORDER BY descricao");
			List<PmpFamilia> result = query.getResultList();
			for (PmpFamilia familia : result) {
				FamiliaBean bean = new FamiliaBean();
				bean.setId(familia.getId());
				bean.setDescricao(familia.getDescricao());
				familiaList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return familiaList;
	}

	public FamiliaBean saveOrUpdate(FamiliaBean bean) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpFamilia familia = null;
			if (bean.getId() == null || bean.getId() == 0) {
				familia = new PmpFamilia();
				familia.setDescricao(bean.getDescricao().toUpperCase());
				manager.persist(familia);
			} else {
				familia = manager.find(PmpFamilia.class, bean.getId());
				familia.setDescricao(bean.getDescricao().toUpperCase());
				manager.merge(familia);
			}
			manager.getTransaction().commit();
			bean.setId(familia.getId());
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

	public Boolean remover(FamiliaBean bean) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpFamilia.class, bean.getId()));
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
