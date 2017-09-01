package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.RegraDeNegocioBean;
import com.pmp.entity.PmpRegraDeNegocio;
import com.pmp.util.JpaUtil;

public class RegraDeNegocioBusiness {

	public List<RegraDeNegocioBean> findAllRegraDeNegocio() {
		List<RegraDeNegocioBean> rn = new ArrayList<RegraDeNegocioBean>();
		EntityManager manager = null;

		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("From PmpRegraDeNegocio");
			List<PmpRegraDeNegocio> result = query.getResultList();
			for (PmpRegraDeNegocio regraDeNegocio : result) {
				RegraDeNegocioBean bean = new RegraDeNegocioBean();
				bean.formBean(regraDeNegocio);
				rn.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return rn;
	}

	public List<RegraDeNegocioBean> findRegraDeNegocioByFilial(String filial) {
		List<RegraDeNegocioBean> rn = new ArrayList<RegraDeNegocioBean>();
		EntityManager manager = null;

		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("From PmpRegraDeNegocio Where filial = :filial");
			query.setParameter("filial", Integer.valueOf(filial));

			List<PmpRegraDeNegocio> result = query.getResultList();

			for (PmpRegraDeNegocio regraDeNegocio : result) {
				RegraDeNegocioBean bean = new RegraDeNegocioBean();
				bean.formBean(regraDeNegocio);
				rn.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return rn;
	}

	public RegraDeNegocioBean saveOrUpdate(RegraDeNegocioBean bean) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpRegraDeNegocio regra = null;
			if (bean.getId() == null || bean.getId() == 0) {
				regra = new PmpRegraDeNegocio();
				bean.toBean(regra);
				manager.persist(regra);
			} else {
				regra = manager.find(PmpRegraDeNegocio.class, bean.getId());
				bean.toBean(regra);
				manager.merge(regra);
			}
			manager.getTransaction().commit();
			bean.setId(regra.getId());
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

	public Boolean remover(RegraDeNegocioBean bean) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpRegraDeNegocio.class, bean.getId()));
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
