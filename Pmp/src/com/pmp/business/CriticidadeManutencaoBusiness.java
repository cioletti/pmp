package com.pmp.business;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.pmp.bean.CriticidadeManutencaoBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.entity.PmpCriticidadeManutencao;
import com.pmp.util.JpaUtil;


public class CriticidadeManutencaoBusiness {

	private UsuarioBean usuarioBean;

	public CriticidadeManutencaoBusiness(UsuarioBean usuarioBean) {
		this.usuarioBean = usuarioBean;
	}

	public CriticidadeManutencaoBean saveOrUpdate(CriticidadeManutencaoBean bean) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();

			PmpCriticidadeManutencao manutencao = null;
			
			Query query = manager.createQuery("FROM PmpCriticidadeManutencao WHERE nivel <> '"+bean.getNivel()+"' AND (minPorcetagem BETWEEN "+bean.getMinPorcetagem()+" AND "
					+bean.getMaxPorcetagem()+" OR maxPorcetagem BETWEEN "+bean.getMinPorcetagem()+" AND "+bean.getMaxPorcetagem()+")");
			
			if(query.getResultList() != null && query.getResultList().size() > 0){
				bean.setMsg("Já existe porcetagem do horímetro cadastrado dentro do valor informado.");
				return bean;
			}						
			
			if(bean != null && bean.getId() == 0){				
				
				manutencao = new PmpCriticidadeManutencao();
				manutencao.setNivel(bean.getNivel());
				manutencao.setMinPorcetagem(bean.getMinPorcetagem());
				manutencao.setMaxPorcetagem(bean.getMaxPorcetagem());

				manager.persist(manutencao);				

			}else{
				manutencao = manager.find(PmpCriticidadeManutencao.class, bean.getId());
				manutencao.setNivel(bean.getNivel());
				manutencao.setMinPorcetagem(bean.getMinPorcetagem());
				manutencao.setMaxPorcetagem(bean.getMaxPorcetagem());

				manager.merge(manutencao);
			}

			manager.getTransaction().commit();
			bean.setId(manutencao.getId());
			
			bean.setMsg("Operação realizada com sucesso.");
			return bean;			

		}catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();				
			}
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}

		return null;
	}

	public CriticidadeManutencaoBean findCriticidadeBy(String criticidade) {
		CriticidadeManutencaoBean bean = new CriticidadeManutencaoBean();
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createQuery("FROM PmpCriticidadeManutencao WHERE nivel =:criticidade");
			query.setParameter("criticidade", criticidade);
			
			PmpCriticidadeManutencao result = (PmpCriticidadeManutencao)query.getSingleResult();
			
			if(result != null){
				bean.setId(result.getId());
				bean.setMinPorcetagem(result.getMinPorcetagem());
				bean.setMaxPorcetagem(result.getMaxPorcetagem());
				bean.setNivel(result.getNivel());				
			}
			
			return bean;
		}catch (NoResultException e) {
			return bean;
					
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
	}

}

