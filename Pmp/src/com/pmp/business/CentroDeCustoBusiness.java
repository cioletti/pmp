package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.CentroDeCustoBean;
import com.pmp.entity.PmpCentroDeCusto;
import com.pmp.util.JpaUtil;

public class CentroDeCustoBusiness {

	public List<CentroDeCustoBean> findAllCentroDeCusto(){
		List<CentroDeCustoBean> cc = new ArrayList<CentroDeCustoBean>();
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("From PmpCentroDeCusto");
			List<PmpCentroDeCusto> result = query.getResultList();
			for (PmpCentroDeCusto centroDeCusto : result) {
				CentroDeCustoBean bean = new CentroDeCustoBean();
				bean.formBean(centroDeCusto);
				cc.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return cc;
	}

	public CentroDeCustoBean saveOrUpdate(CentroDeCustoBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpCentroDeCusto custo = null;
			if(bean.getId() == null || bean.getId() == 0){
				custo = new PmpCentroDeCusto();
				bean.toBean(custo);
				manager.persist(custo);
			}else{
				custo = manager.find(PmpCentroDeCusto.class, bean.getId());
				bean.toBean(custo);
				manager.merge(custo);
			}
			manager.getTransaction().commit();
			bean.setId(custo.getId());
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

	public Boolean remover(CentroDeCustoBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpCentroDeCusto.class, bean.getId()));
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
