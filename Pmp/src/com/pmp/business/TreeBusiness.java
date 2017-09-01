package com.pmp.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.DescontoPdpBean;
import com.pmp.bean.TipoPm;
import com.pmp.bean.TreeBean;
import com.pmp.entity.ArvInspecao;
import com.pmp.entity.PmpDescontoPdp;
import com.pmp.entity.PmpFamilia;
import com.pmp.util.JpaUtil;

public class TreeBusiness {
	
	private static final String TIPO_SISTEMA = "PM";
	
    
	
		public Long findModeloId(String modelo){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createNativeQuery("select id_arv from arv_inspecao where DESCRICAO =  '"+modelo+"'");
			
			return ((BigDecimal)query.getSingleResult()).longValue();
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
		
		
	public Boolean removerTree(Long idArv){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			Query query = manager.createNativeQuery("delete from arv_inspecao where id_pai_root = "+idArv);
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
	public TreeBean findAllTree(Integer idArv){
		EntityManager manager = null;
		
		TreeBean bean = new TreeBean();
		try {
			manager = JpaUtil.getInstance();
			ArvInspecao tree = manager.find(ArvInspecao.class, idArv.longValue());
			bean.fromBean(tree);
			Query query = manager.createQuery("From ArvInspecao where idPaiRoot = :idpairoot and idPai.idArv is not null and tipo =:tipo order by descricao");
			query.setParameter("idpairoot", tree.getIdArv());
			query.setParameter("tipo", TIPO_SISTEMA);
			List<ArvInspecao> result = query.getResultList();
			
			this.mountTree(result, bean);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	
	private TreeBean mountTree(List<ArvInspecao> list, TreeBean tree){
		List<TreeBean> treeList = new ArrayList<TreeBean>();
		for (ArvInspecao arvInspecao : list) {
			TreeBean children = new TreeBean();
			if(tree.getIdArv().equals(arvInspecao.getIdPai().getIdArv())){
				children.fromBean(arvInspecao);
				treeList.add(children);
				this.mountTree(list, children);
			}
		}
		tree.setChildren(treeList);
		return tree;
	}
	
	public List<TreeBean> findAllTreePai(String tipo, Long idFamilia){
		List<TreeBean> treeList = new ArrayList<TreeBean>();
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("From ArvInspecao where tipo = :tipo and idPai.idArv is null and idFamilia.id =:idFamilia order by descricao");
			query.setParameter("tipo", tipo);
			query.setParameter("idFamilia", idFamilia);
			List<ArvInspecao> list = query.getResultList();
			TreeBean bean = new TreeBean();
			bean.setIdArv(-1l);
			bean.setDescricao("Selecione");
			treeList.add(bean);
			for (ArvInspecao arvInspecao : list) {
				bean = new TreeBean();
				bean.fromBean(arvInspecao);
				treeList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return treeList;
	}
	
	public TreeBean saveNodo(TreeBean bean, Long idPaiRoot, Long idFamilia) {
		
		try {
//			EntityManager manager = JpaUtil.getInstance();
//			manager.getTransaction().begin();
//			Query query = manager.createNativeQuery("delete from arv_inspecao where id_pai_root = "+idPaiRoot+" and id_pai is not null");
//			query.executeUpdate();
//			manager.getTransaction().commit();
			
			if(bean.getIdArv() == null || bean.getIdArv() == 0){
				bean = this.save(bean, null, null, idFamilia);
			}else{
				bean = this.save(bean, null, bean.getIdArv(), idFamilia);
			}
			if(bean.getChildren() != null && bean.getChildren().size() > 0){
				this.saveRecursive(bean.getChildren(), bean.getIdArv(), bean.getIdArv(), idFamilia);
			}
			return bean;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public TreeBean saveNodoClone(Long idArv, Long idFamilia, String descricao) {
		
		try {
			TreeBean bean = this.findAllTree(Integer.valueOf(idArv.toString()));
			bean.setDescricao(descricao);
			
			bean = this.saveClone(bean, null, idArv, idFamilia);
			
			if(bean.getChildren() != null && bean.getChildren().size() > 0){
				this.saveRecursiveClone(bean.getChildren(), bean.getIdArv(), bean.getIdArv(), idFamilia);
			}
			return bean;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean removerNodo(Long idArv){
		EntityManager manager = null;
		try{
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();			
		
			Query query = manager.createNativeQuery("delete from arv_inspecao where id_arv = "+idArv);
			query.executeUpdate();
			query = manager.createNativeQuery("delete from arv_inspecao where id_pai = "+idArv);
			query.executeUpdate();
			
			manager.getTransaction().commit();
			return true;
			
		} catch (Exception e) {			
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return false;
	}
	
	private void saveRecursive(List<TreeBean> children, Long idPai, Long paiRoot, Long idFamilia){
		for (TreeBean bean : children) {
			//bean.setIdArv(null);
			bean = this.save(bean, idPai, paiRoot, idFamilia);
			if(bean.getChildren() != null && bean.getChildren().size() > 0){
				this.saveRecursive(bean.getChildren(), bean.getIdArv(), paiRoot, idFamilia);
			}
		}
	}
	private void saveRecursiveClone(List<TreeBean> children, Long idPai, Long paiRoot, Long idFamilia){
		for (TreeBean bean : children) {
			//bean.setIdArv(null);
			bean = this.saveClone(bean, idPai, paiRoot, idFamilia);
			if(bean.getChildren() != null && bean.getChildren().size() > 0){
				this.saveRecursiveClone(bean.getChildren(), bean.getIdArv(), paiRoot, idFamilia);
			}
		}
	}
	public TreeBean save(TreeBean bean, Long idPai, Long paiRoot, Long idFamilia) {
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			ArvInspecao pai = null;
			if(idPai != null){
				pai = manager.find(ArvInspecao.class, idPai);
			}
			PmpFamilia familia = manager.find(PmpFamilia.class, idFamilia);
			ArvInspecao inspecao = new ArvInspecao();
			if(bean.getIdArv() != null && bean.getIdArv() > 0){
				inspecao = manager.find(ArvInspecao.class, bean.getIdArv());
				inspecao.setTipo(TIPO_SISTEMA);
				inspecao.setDescricao(bean.getDescricao().toUpperCase());
				inspecao.setIdPai(pai);
				inspecao.setIdFamilia(familia);
				inspecao.setTipoManutencao(bean.getTipoManutencao());
				inspecao.setSos(bean.getSos());
				inspecao.setSmcs(bean.getSmcs());
				manager.merge(inspecao);
			}else{
				inspecao.setTipo(TIPO_SISTEMA);
				inspecao.setDescricao(bean.getDescricao().toUpperCase());
				inspecao.setIdPai(pai);
				inspecao.setIdFamilia(familia);
				inspecao.setTipoManutencao(bean.getTipoManutencao());
				inspecao.setSos(bean.getSos());
				inspecao.setSmcs(bean.getSmcs());
				manager.persist(inspecao);
			}
			if(pai == null){
				inspecao.setIdPaiRoot(inspecao.getIdArv());
			}else{
				inspecao.setIdPaiRoot(paiRoot);
				
			}
			manager.merge(inspecao);
			manager.getTransaction().commit();
			bean.fromBean(inspecao);
			return bean;
		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return null;
	}
	public TreeBean saveClone(TreeBean bean, Long idPai, Long paiRoot, Long idFamilia) {
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			ArvInspecao pai = null;
			if(idPai != null){
				pai = manager.find(ArvInspecao.class, idPai);
			}
			PmpFamilia familia = manager.find(PmpFamilia.class, idFamilia);
			ArvInspecao inspecao = new ArvInspecao();

			inspecao.setTipo(TIPO_SISTEMA);
			inspecao.setDescricao(bean.getDescricao().toUpperCase());
			inspecao.setIdPai(pai);
			inspecao.setIdFamilia(familia);
			inspecao.setTipoManutencao(bean.getTipoManutencao());
			inspecao.setSos(bean.getSos());
			inspecao.setSmcs(bean.getSmcs());
			manager.persist(inspecao);

			if(pai == null){
				inspecao.setIdPaiRoot(inspecao.getIdArv());
			}else{
				inspecao.setIdPaiRoot(paiRoot);

			}
			manager.merge(inspecao);
			manager.getTransaction().commit();
			bean.fromBean(inspecao);
			return bean;
		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return null;
	}
	
	public List<TipoPm> findAllTipoPm(){
		List<TipoPm> dp = new ArrayList<TipoPm>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createNativeQuery("select tipo_pm, ordem From PMP_TIPO_INSPECAO order by ordem");
			List<Object []> result = query.getResultList();
			for (Object [] pair : result) {
				TipoPm bean = new TipoPm();
				bean.setLabel((String)pair[0]);
				bean.setOrdem(((BigDecimal)pair[1]).longValue());
				bean.setValor((String)pair[0]);
				dp.add(bean);
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return dp;
	}
}
