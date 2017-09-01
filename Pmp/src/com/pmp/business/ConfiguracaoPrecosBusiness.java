package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.ConfiguracaoPrecosBean;
import com.pmp.bean.TipoTracaoBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.entity.PmpConfiguracaoPrecos;
import com.pmp.entity.PmpTipoTracao;
import com.pmp.util.JpaUtil;

public class ConfiguracaoPrecosBusiness {
	private String ID_FUNCIONARIO;

	public ConfiguracaoPrecosBusiness(UsuarioBean bean) {
		ID_FUNCIONARIO = bean.getMatricula();
	}
	public ConfiguracaoPrecosBean saveOrUpdate(ConfiguracaoPrecosBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpConfiguracaoPrecos precos = new PmpConfiguracaoPrecos();
			if( bean.getId() == null || bean.getId() == 0){
				bean.toBean(bean, precos);
				precos.setIdFuncionario(ID_FUNCIONARIO);
				manager.persist(precos);
			}else{
				precos = manager.find(PmpConfiguracaoPrecos.class, bean.getId());
				bean.toBean(bean, precos);
				manager.merge(precos);
			}
			manager.getTransaction().commit();
			bean.setId(precos.getId().longValue());
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
	
	public List<ConfiguracaoPrecosBean> findAllConfigPrecos(){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			List<ConfiguracaoPrecosBean> result = new ArrayList<ConfiguracaoPrecosBean>();
			Query query = manager.createQuery("From PmpConfiguracaoPrecos ORDER BY descricao");
			List<PmpConfiguracaoPrecos> precos = (List<PmpConfiguracaoPrecos>)query.getResultList();
			for (PmpConfiguracaoPrecos pmpConfiguracaoPrecos : precos) {
				ConfiguracaoPrecosBean bean = new ConfiguracaoPrecosBean();
				bean.fromBean(pmpConfiguracaoPrecos);
				result.add(bean);
			}
			return result;
		} catch (Exception e) {			
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return new ArrayList<ConfiguracaoPrecosBean>();
	}
	public List<TipoTracaoBean> findAllTipoTracao(){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			List<TipoTracaoBean> result = new ArrayList<TipoTracaoBean>();
			Query query = manager.createQuery("From PmpTipoTracao ORDER BY descricao");
			List<PmpTipoTracao> tipoTracaoList = (List<PmpTipoTracao>)query.getResultList();
			TipoTracaoBean bean = new TipoTracaoBean();
			bean.setId(0L);
			bean.setDescricao("Slecione");
			result.add(bean);
			for (PmpTipoTracao pmpTipoTracao : tipoTracaoList) {
				bean = new TipoTracaoBean();
				bean.fromBean(pmpTipoTracao);
				result.add(bean);
			}
			return result;
		} catch (Exception e) {			
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return new ArrayList<TipoTracaoBean>();
	}
	
	public Boolean remover(ConfiguracaoPrecosBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpConfiguracaoPrecos.class, bean.getId()));
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
