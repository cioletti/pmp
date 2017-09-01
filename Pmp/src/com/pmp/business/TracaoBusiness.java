package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.ConfigurarTracaoBean;
import com.pmp.bean.TipoTracaoBean;
import com.pmp.entity.ArvInspecao;
import com.pmp.entity.PmpConfigTracao;
import com.pmp.entity.PmpFamilia;
import com.pmp.entity.PmpSiglaAc;
import com.pmp.entity.PmpSiglaTracao;
import com.pmp.entity.PmpTipoTracao;
import com.pmp.util.JpaUtil;

public class TracaoBusiness {

	public List<TipoTracaoBean> findAllTipoTracao(Long idModelo) {
		List<TipoTracaoBean> tipoTracaoList = new ArrayList<TipoTracaoBean>();
		EntityManager manager = null;

		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("From PmpTipoTracao where idModelo =:idModelo");
			query.setParameter("idModelo", idModelo);
			List<PmpTipoTracao> result = query.getResultList();
			for (PmpTipoTracao tipoTracao : result) {
				TipoTracaoBean bean = new TipoTracaoBean();
				bean.setId(tipoTracao.getId());
				bean.setDescricao(tipoTracao.getDescricao());
				bean.setIdModelo(tipoTracao.getIdModelo());
				tipoTracaoList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return tipoTracaoList;
	}
	

	public TipoTracaoBean saveOrUpdate(TipoTracaoBean bean) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpTipoTracao tipoTracao = null;
			if (bean.getId() == null || bean.getId() == 0) {
				tipoTracao = new PmpTipoTracao();
				tipoTracao.setDescricao(bean.getDescricao().toUpperCase());
				tipoTracao.setIdModelo(bean.getIdModelo());
				manager.persist(tipoTracao);
			} else {
				tipoTracao = manager.find(PmpTipoTracao.class, bean.getId());
				tipoTracao.setDescricao(bean.getDescricao().toUpperCase());
				tipoTracao.setIdModelo(bean.getIdModelo());
				manager.merge(tipoTracao);
			}
			manager.getTransaction().commit();
			bean.setId(tipoTracao.getId());
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

	public Boolean removerTipoTracao(Long idTipoTracao) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpTipoTracao.class, idTipoTracao));
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
	
	public List<ConfigurarTracaoBean> findAllConfigTracao(Long idModelo) {
		List<ConfigurarTracaoBean> configTracaoList = new ArrayList<ConfigurarTracaoBean>();
		EntityManager manager = null;

		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("From PmpConfigTracao where idModelo.idArv =:idModelo");
			query.setParameter("idModelo", idModelo);
			List<PmpConfigTracao> result = query.getResultList();
			for (PmpConfigTracao configTracao : result) {
				ConfigurarTracaoBean bean = new ConfigurarTracaoBean();
				bean.setId(configTracao.getId());
				bean.setDescricao(configTracao.getIdTracao().getDescricao()+" - "+(configTracao.getPossuiArCondicionado().equals("S")?"Com Ar Condicionado":"Sem Ar Condicionado"));
				bean.setIdModelo(configTracao.getIdModelo().getIdArv());
				bean.setIdTracao(configTracao.getIdTracao().getId());
				bean.setIdFamilia(configTracao.getIdFamilia().getId());
				bean.setPossuiArCondicionado(configTracao.getPossuiArCondicionado());
				query = manager.createNativeQuery("select sigla_ac from pmp_sigla_ac where id_config_tracao =:id_config_tracao");
				query.setParameter("id_config_tracao", configTracao.getId());
				List<String> siglaList = query.getResultList();
				if (query.getResultList().size() > 0) {
					bean.setSiglaAcList(query.getResultList());
				}
				query = manager.createNativeQuery("select sigla_tracao from pmp_sigla_tracao where id_config_tracao =:id_config_tracao");
				query.setParameter("id_config_tracao", configTracao.getId());
				if (query.getResultList().size() > 0) {
					bean.setSiglaTracaoList(query.getResultList());
				}
				configTracaoList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return configTracaoList;
	}
	
	public ConfigurarTracaoBean saveOrUpdate(ConfigurarTracaoBean bean) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpConfigTracao configTracao = null;
			ArvInspecao arvInspecao = manager.find(ArvInspecao.class, bean.getIdModelo());
			PmpTipoTracao tipoTracao = manager.find(PmpTipoTracao.class, bean.getIdTracao());
			PmpFamilia familia = manager.find(PmpFamilia.class, bean.getIdFamilia());
			
			if (bean.getId() == null || bean.getId() == 0) {
				configTracao = new PmpConfigTracao();
				configTracao.setIdModelo(arvInspecao);
				configTracao.setIdTracao(tipoTracao);
				configTracao.setPossuiArCondicionado(bean.getPossuiArCondicionado());
				configTracao.setIdFamilia(familia);
				manager.persist(configTracao);
				//query = manager.createNativeQuery("");
				
				if(bean.getSiglaAcList().size() > 0){
					for(String sigla : bean.getSiglaAcList()){
						PmpSiglaAc ac = new PmpSiglaAc();
						ac.setIdConfigTracao(configTracao.getId());
						ac.setSiglaAc(sigla);
						manager.persist(ac);
					}
				}
				if(bean.getSiglaTracaoList().size() > 0){
					for(String sigla : bean.getSiglaTracaoList()){
						PmpSiglaTracao tr = new PmpSiglaTracao();
						tr.setIdConfigTracao(configTracao.getId());
						tr.setSiglaTracao(sigla);
						manager.persist(tr);
					}
				}
				
			} else {
				configTracao = manager.find(PmpConfigTracao.class, bean.getId());
				configTracao.setIdModelo(arvInspecao);
				configTracao.setIdTracao(tipoTracao);
				configTracao.setPossuiArCondicionado(bean.getPossuiArCondicionado());
				configTracao.setIdFamilia(familia);
				manager.merge(configTracao);
				
				Query query = manager.createNativeQuery("delete Pmp_Sigla_Ac where id_Config_Tracao =:idConfigTracao");
				query.setParameter("idConfigTracao", configTracao.getId());
				query.executeUpdate();
				if(bean.getSiglaAcList().size() > 0){
					for(String sigla : bean.getSiglaAcList()){
						PmpSiglaAc ac = new PmpSiglaAc();
						ac.setIdConfigTracao(configTracao.getId());
						ac.setSiglaAc(sigla);
						manager.persist(ac);
					}
				}
				query = manager.createNativeQuery("delete Pmp_Sigla_Tracao where id_Config_Tracao =:idConfigTracao");
				query.setParameter("idConfigTracao", configTracao.getId());
				query.executeUpdate();
				if(bean.getSiglaTracaoList().size() > 0){
					for(String sigla : bean.getSiglaTracaoList()){
						PmpSiglaTracao tr = new PmpSiglaTracao();
						tr.setIdConfigTracao(configTracao.getId());
						tr.setSiglaTracao(sigla);
						manager.persist(tr);
					}
				}
				
			}
			manager.getTransaction().commit();
			bean.setId(configTracao.getId());
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
	
	public Boolean removerConfigTracao(Long idConfigTracao) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpConfigTracao.class, idConfigTracao));
			
			Query query = manager.createNativeQuery("delete Pmp_Sigla_Ac where id_Config_Tracao =:idConfigTracao");
			query.setParameter("idConfigTracao", idConfigTracao);
			query.executeUpdate();
			
			query = manager.createNativeQuery("delete Pmp_Sigla_Tracao where id_Config_Tracao =:idConfigTracao");
			query.setParameter("idConfigTracao", idConfigTracao);
			query.executeUpdate();
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
