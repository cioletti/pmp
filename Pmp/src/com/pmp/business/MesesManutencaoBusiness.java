package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.MesesManutencaoBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.entity.ArvInspecao;
import com.pmp.entity.PmpFamilia;
import com.pmp.entity.PmpMesesManutencao;
import com.pmp.util.JpaUtil;


public class MesesManutencaoBusiness {

	private UsuarioBean usuarioBean;

	public MesesManutencaoBusiness(UsuarioBean usuarioBean) {
		this.usuarioBean = usuarioBean;
	}

	public MesesManutencaoBean saveOrUpdate(MesesManutencaoBean bean) {
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();

			PmpMesesManutencao manutencao = null;


			ArvInspecao inspecao = manager.find(ArvInspecao.class, bean.getIdModelo());
			PmpFamilia familia = manager.find(PmpFamilia.class, bean.getIdFamilia()); 

			if(bean != null && bean.getId() == 0){

				Query query = manager.createQuery("FROM PmpMesesManutencao WHERE idFamilia.id =:idFamilia AND idModelo.idArv =:idModelo");
				query.setParameter("idFamilia", bean.getIdFamilia());
				query.setParameter("idModelo", bean.getIdModelo());

				if(query.getResultList() != null && query.getResultList().size() > 0){
					return null;
				}

				manutencao = new PmpMesesManutencao();
				manutencao.setIdModelo(inspecao);
				manutencao.setIdFamilia(familia);
				manutencao.setMesesManutencao(bean.getMesesManutencao());

				manager.persist(manutencao);				

			}else{
				manutencao = manager.find(PmpMesesManutencao.class, bean.getId());
				manutencao.setIdModelo(inspecao);
				manutencao.setIdFamilia(familia);
				manutencao.setMesesManutencao(bean.getMesesManutencao());

				manager.merge(manutencao);
			}

			manager.getTransaction().commit();
			bean.setId(manutencao.getId());

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

	public List<MesesManutencaoBean> findAllMesesManutencao(Long idFamilia, Long idModelo) {
		List<MesesManutencaoBean> list = new ArrayList<MesesManutencaoBean>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();


			Query query = manager.createQuery("FROM PmpMesesManutencao where idFamilia = "+idFamilia+" and idModelo = "+idModelo+" ORDER BY mesesManutencao");
			List<PmpMesesManutencao> result = query.getResultList();

			for (PmpMesesManutencao manutencaoBean : result) {
				MesesManutencaoBean bean = new MesesManutencaoBean();

				bean.setId(manutencaoBean.getId());
				bean.setIdFamilia(manutencaoBean.getIdFamilia().getId());
				bean.setFamiliaStr(manutencaoBean.getIdFamilia().getDescricao());
				bean.setIdModelo(manutencaoBean.getIdModelo().getIdArv());
				bean.setModeloStr(manutencaoBean.getIdModelo().getDescricao());
				bean.setMesesManutencao(manutencaoBean.getMesesManutencao());

				list.add(bean);				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}

		return list;
	}

	public Boolean removerMesesManutencao(MesesManutencaoBean bean) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpMesesManutencao.class, bean.getId()));
			manager.getTransaction().commit();
			return true;

		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();				
			}				
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return false;
	}
}

