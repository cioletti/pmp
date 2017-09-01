package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.ConfigurarCustomizacaoBean;
import com.pmp.bean.TipoCustomizacaoBean;
import com.pmp.entity.ArvInspecao;
import com.pmp.entity.PmpConfigCustomizacao;
import com.pmp.entity.PmpFamilia;
import com.pmp.entity.PmpSiglaCustomizacao;
import com.pmp.entity.PmpTipoCustomizacao;
import com.pmp.util.JpaUtil;

public class CustomizacaoBusiness {

	public List<TipoCustomizacaoBean> findAllTipoCustomizacao(Long idModelo) {
		List<TipoCustomizacaoBean> tipoCustomizacaoList = new ArrayList<TipoCustomizacaoBean>();
		EntityManager manager = null;

		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("From PmpTipoCustomizacao where idModelo.idArv =:idModelo order by descricao");
			query.setParameter("idModelo", idModelo);
			List<PmpTipoCustomizacao> result = query.getResultList();
			for (PmpTipoCustomizacao tipoCustomizacao: result) {
				TipoCustomizacaoBean bean = new TipoCustomizacaoBean();
				bean.setId(tipoCustomizacao.getId());
				bean.setDescricao(tipoCustomizacao.getDescricao());
				bean.setIdModelo(tipoCustomizacao.getIdModelo().getIdArv());
				tipoCustomizacaoList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return tipoCustomizacaoList;
	}
	

	public TipoCustomizacaoBean saveOrUpdate(TipoCustomizacaoBean bean) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpTipoCustomizacao tipoCustomizacao = null;
			if (bean.getId() == null || bean.getId() == 0) {
				tipoCustomizacao = new PmpTipoCustomizacao();
				tipoCustomizacao.setDescricao(bean.getDescricao().toUpperCase());
				tipoCustomizacao.setIdModelo(manager.find(ArvInspecao.class, bean.getIdModelo()));
				manager.persist(tipoCustomizacao);
			} else {
				tipoCustomizacao = manager.find(PmpTipoCustomizacao.class, bean.getId());
				tipoCustomizacao.setDescricao(bean.getDescricao().toUpperCase());
				tipoCustomizacao.setIdModelo(manager.find(ArvInspecao.class, bean.getId()));
				manager.merge(tipoCustomizacao);
			}
			manager.getTransaction().commit();
			bean.setId(tipoCustomizacao.getId());
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

	public Boolean removerTipoCustomizacao(Long idTipoCustomizacao) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpTipoCustomizacao.class, idTipoCustomizacao));
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
	
	public List<ConfigurarCustomizacaoBean> findAllConfigCustomizacao(Long idModelo) {
		List<ConfigurarCustomizacaoBean> configCostomizacaoList = new ArrayList<ConfigurarCustomizacaoBean>();
		EntityManager manager = null;

		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("From PmpConfigCustomizacao where idModelo.idArv =:idModelo order by idTipoCustomizacao.descricao");
			query.setParameter("idModelo", idModelo);
			List<PmpConfigCustomizacao> result = query.getResultList();
			for (PmpConfigCustomizacao configCustomizacao : result) {
				ConfigurarCustomizacaoBean bean = new ConfigurarCustomizacaoBean();
				bean.setId(configCustomizacao.getId());
				bean.setDescricao(configCustomizacao.getIdTipoCustomizacao().getDescricao());
				bean.setIdModelo(configCustomizacao.getIdModelo().getIdArv());
				bean.setIdTipoCustomizacao(configCustomizacao.getIdTipoCustomizacao().getId());
				bean.setIdFamilia(configCustomizacao.getIdFamilia().getId());
				query = manager.createNativeQuery("select SIGLA_CUSTOMIZACAO from PMP_SIGLA_CUSTOMIZACAO where ID_CONFIG_CUSTOMIZACAO =:id_conf_customizacao");
				query.setParameter("id_conf_customizacao", configCustomizacao.getId());
				if (query.getResultList().size() > 0) {
					bean.setSiglaCustList(query.getResultList());
				}
				configCostomizacaoList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return configCostomizacaoList;
	}
	
	public ConfigurarCustomizacaoBean saveOrUpdate(ConfigurarCustomizacaoBean bean) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpConfigCustomizacao configCustomizacao= null;
			ArvInspecao arvInspecao = manager.find(ArvInspecao.class, bean.getIdModelo());
			PmpTipoCustomizacao tipoCustomizacao = manager.find(PmpTipoCustomizacao.class, bean.getIdTipoCustomizacao());
			PmpFamilia familia = manager.find(PmpFamilia.class, bean.getIdFamilia());
			
			if (bean.getId() == null || bean.getId() == 0) {
				configCustomizacao = new PmpConfigCustomizacao();
				configCustomizacao.setIdModelo(arvInspecao);
				configCustomizacao.setIdTipoCustomizacao(tipoCustomizacao);
				configCustomizacao.setIdFamilia(familia);
				manager.persist(configCustomizacao);
				//query = manager.createNativeQuery("");

				if(bean.getSiglaCustList().size() > 0){
					for(String sigla : bean.getSiglaCustList()){
						PmpSiglaCustomizacao cust  = new PmpSiglaCustomizacao();
						cust.setIdConfigCustumizacao(manager.find(PmpConfigCustomizacao.class,configCustomizacao.getId()));
						cust.setSiglaCustomizacao(sigla);
						manager.persist(cust);
					}
				}
				
			} else {
				configCustomizacao = manager.find(PmpConfigCustomizacao.class, bean.getId());
				configCustomizacao.setIdModelo(arvInspecao);
				configCustomizacao.setIdTipoCustomizacao(tipoCustomizacao);
				configCustomizacao.setIdFamilia(familia);
				manager.merge(configCustomizacao);
				
				Query query = manager.createNativeQuery("delete PMP_SIGLA_CUSTOMIZACAO where ID_CONFIG_CUSTOMIZACAO =:idConfCustomizacao");
				query.setParameter("idConfCustomizacao", configCustomizacao.getId());
				query.executeUpdate();
				if(bean.getSiglaCustList().size() > 0){
					for(String sigla : bean.getSiglaCustList()){
						PmpSiglaCustomizacao cust  = new PmpSiglaCustomizacao();
						cust.setIdConfigCustumizacao(manager.find(PmpConfigCustomizacao.class, configCustomizacao.getId()));
						cust.setSiglaCustomizacao(sigla);
						manager.persist(cust);
					}
				}
				
			}
			manager.getTransaction().commit();
			bean.setId(configCustomizacao.getId());
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
	
	public Boolean removerConfigCustomizacao(Long idConfigcustomizacao) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpConfigCustomizacao.class, idConfigcustomizacao));
			
			Query query = manager.createNativeQuery("delete PMP_SIGLA_CUSTOMIZACAO where ID_CONFIG_CUSTOMIZACAO =:idConfigcustomizacao");
			query.setParameter("idConfigcustomizacao", idConfigcustomizacao);
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
