package com.pmp.business;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.CentroDeCustoBean;
import com.pmp.bean.CompCodeBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.entity.PmpCompCodePartner;
import com.pmp.util.JpaUtil;
public class CompCodeBusiness {

private UsuarioBean usuarioBean;

public CompCodeBusiness(UsuarioBean bean) {
	this.usuarioBean = bean;
}

public List<CompCodeBean> findAllCompCode(){
		List<CompCodeBean> cc = new ArrayList<CompCodeBean>();
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("From PmpCompCodePartner");
			List<PmpCompCodePartner> result = query.getResultList();
			for (PmpCompCodePartner compcode : result) {
				CompCodeBean bean = new CompCodeBean();
                bean.setId(Long.valueOf(compcode.getId()));
                bean.setDescricao(compcode.getDescricao());
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

	public CompCodeBean saveOrUpdateCompCode(CompCodeBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpCompCodePartner comp = null;
			if(bean.getId() == null || bean.getId() == 0){
				comp = new PmpCompCodePartner();
				bean.toBean(comp);
				manager.persist(comp);
			}else{
				comp = manager.find(PmpCompCodePartner.class, bean.getId());
				bean.toBean(comp);
				manager.merge(comp);
			}
			manager.getTransaction().commit();
			bean.setId(comp.getId());
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

	public Boolean removerCompCode(CompCodeBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpCompCodePartner.class, bean.getId()));
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
