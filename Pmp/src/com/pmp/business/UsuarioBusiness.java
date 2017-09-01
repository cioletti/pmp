package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.pmp.bean.PerfilBean;
import com.pmp.bean.SistemaBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.entity.AdmPerfil;
import com.pmp.entity.AdmPerfilSistemaUsuario;
import com.pmp.entity.AdmSistema;
import com.pmp.entity.TwFilial;
import com.pmp.entity.TwFuncionario;
import com.pmp.util.JpaUtil;

public class UsuarioBusiness {
    private static final String EJBQL_ALTER_PASSWORD = "From TwFuncionario where login =:login and senha =:senha";
    
	public UsuarioBean loginUsuario(String login, String senha, String url){
		EntityManager manager = null;
		try {
			
				try {
					manager = JpaUtil.getInstance();

					Query query = manager.createQuery("From TwFuncionario u where u.login=:login and u.senha=:senha");
					query.setParameter("login", login);
					query.setParameter("senha", senha);
					TwFuncionario usuario = (TwFuncionario)query.getSingleResult();

					UsuarioBean usuarioBean = new UsuarioBean();
					usuarioBean.setEmail(usuario.getEmail());
					usuarioBean.setFilial(Integer.valueOf(usuario.getStn1()).toString());
					usuarioBean.setMatricula(usuario.getEpidno());
					usuarioBean.setTelefone(usuario.getTelefone());
					usuarioBean.setLogin(usuario.getLogin());
					usuarioBean.setSenha(usuario.getSenha());
					usuarioBean.setNome(usuario.getEplsnm().toUpperCase());
					usuarioBean.setEstimateBy(usuario.getEstimateBy());
					query = manager.createQuery("From AdmPerfilSistemaUsuario where idTwUsuario.epidno = '"+usuario.getEpidno().toUpperCase()+"'");
					List<AdmPerfilSistemaUsuario> sistemaUsuario = query.getResultList();
					usuarioBean.setIsAdm(Boolean.FALSE);
					for (AdmPerfilSistemaUsuario perfilSistemaUsuario : sistemaUsuario) {
						if(perfilSistemaUsuario.getIdSistema().getSigla().equals("PMP")){
							usuarioBean.setSiglaPerfil(perfilSistemaUsuario.getIdPerfil().getSigla());
							if(perfilSistemaUsuario.getIdPerfil().getSigla().equals("ADM")){
								usuarioBean.setIsAdm(Boolean.TRUE);
							}
						}
					}

					query = manager.createQuery("From AdmPerfilSistemaUsuario where idTwUsuario.epidno=:epidno");
					query.setParameter("epidno", usuarioBean.getMatricula());
					List<AdmPerfilSistemaUsuario> admPerfilSistemaUsuarioList = query.getResultList();
					List<SistemaBean> sistemaList = new ArrayList<SistemaBean>();
					for (AdmPerfilSistemaUsuario admPerfilSistemaUsuario : admPerfilSistemaUsuarioList) {
						SistemaBean bean = new SistemaBean();
						bean.setDescricao(admPerfilSistemaUsuario.getIdSistema().getDescricao());
						bean.setId(admPerfilSistemaUsuario.getIdSistema().getId().intValue());
						bean.setSigla(admPerfilSistemaUsuario.getIdSistema().getSigla());
						bean.setDescricaoPerfil(admPerfilSistemaUsuario.getIdPerfil().getDescricao());
						bean.setContext(admPerfilSistemaUsuario.getIdSistema().getContext());
						bean.setImg(admPerfilSistemaUsuario.getIdSistema().getImg());
						bean.setUrl(url+"/"+admPerfilSistemaUsuario.getIdSistema().getContext());
						PerfilBean perfilBean = new PerfilBean();
						perfilBean.setDescricao(admPerfilSistemaUsuario.getIdPerfil().getDescricao());
						perfilBean.setId(admPerfilSistemaUsuario.getIdPerfil().getId().intValue());
						perfilBean.setSigla(admPerfilSistemaUsuario.getIdPerfil().getSigla());
						bean.setPerfilBean(perfilBean);
						sistemaList.add(bean);				
					}
					usuarioBean.setSistemaList(sistemaList);

					
					return usuarioBean;
				}catch (NoResultException e) {
    			
    			}catch (Exception e1) {
    				e1.printStackTrace();
    			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return null;
	}

	/*
	 * Recupera os sistemas que o usuário não possui
	 */
	public List<SistemaBean> findAllSistemas(String idFuncionario){
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			Query query = null;
			if(idFuncionario != null){
				query = manager.createQuery("from  TwFuncionario  where id not in(select idSistema.id from  AdmPerfilSistemaUsuario  where idTwUsuario.epidno=:epidno)");
				query.setParameter("epidno", idFuncionario);
			}else{
				query = manager.createQuery("from  TwFuncionario");
			}
			List<AdmSistema> list = (List<AdmSistema>)query.getResultList();
			List<SistemaBean> result = new ArrayList<SistemaBean>();
			for (AdmSistema sistema : list) {
				SistemaBean bean = new SistemaBean();				
				bean.setId(sistema.getId().intValue());
				bean.setDescricao(sistema.getDescricao());
				bean.setSigla(sistema.getSigla());
				
				result.add(bean);				
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
	}

	public List<PerfilBean> findAllPerfil(String idFuncionario, String tipoSistema){
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			Query query = null;
			if(idFuncionario != null){
				query= manager.createQuery("from  AdmPerfil  where tipoSistema =:tipoSistema and id not in(select idPerfil.id from  AdmPerfilSistemaUsuario  where idTwUsuario.epidno=:epidno)");
				query.setParameter("tipoSistema", tipoSistema);
				query.setParameter("epidno", idFuncionario);
			}else{
				query= manager.createQuery("from  AdmPerfil  where tipoSistema =:tipoSistema");
				query.setParameter("tipoSistema", tipoSistema);
			}
			List<AdmPerfil> list = (List<AdmPerfil>)query.getResultList();
			List<PerfilBean> result = new ArrayList<PerfilBean>();
			for (AdmPerfil perfil : list) {
				PerfilBean bean = new PerfilBean();				
				bean.setId(perfil.getId().intValue());
				bean.setDescricao(perfil.getDescricao());
				bean.setSigla(perfil.getSigla());
				result.add(bean);				
			}
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
	}

	
	public UsuarioBean saveUser(UsuarioBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			TwFuncionario usuario = manager.find(TwFuncionario.class, bean.getMatricula().toUpperCase());
			if(usuario != null){
				usuario.setEplsnm(bean.getNome().toUpperCase());
				usuario.setEmail(bean.getEmail());
				usuario.setStn1(bean.getFilial().toString());
				usuario.setEpidno(bean.getMatricula().toUpperCase());
				usuario.setTelefone(bean.getTelefone());
				usuario.setLogin(bean.getLogin());
				usuario.setSenha(bean.getSenha());
				manager.merge(usuario);
			}else{
				usuario = new TwFuncionario();
				usuario.setEplsnm(bean.getNome().toUpperCase());
				usuario.setEmail(bean.getEmail());
				usuario.setStn1(bean.getFilial().toString());
				usuario.setEpidno(bean.getMatricula().toUpperCase());
				usuario.setTelefone(bean.getTelefone());
				usuario.setLogin(bean.getLogin());
				usuario.setSenha(bean.getSenha());
				manager.persist(usuario);
			}
			AdmPerfilSistemaUsuario sistemaUsuario = new AdmPerfilSistemaUsuario();
			sistemaUsuario.setIdPerfil(manager.find(AdmPerfil.class, bean.getIdPerfil()));
			sistemaUsuario.setIdSistema(manager.find(AdmSistema.class, bean.getIdSistema()));
			sistemaUsuario.setIdTwUsuario(usuario);
			manager.persist(sistemaUsuario);
			manager.getTransaction().commit();
			return findAllUsersByMatricula(bean.getMatricula());

		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
			return null;
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
	}

	public List<UsuarioBean> findAllUsersByName(String nome){
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			//manager.getTransaction().begin();
			Query query = manager.createQuery("From TwFuncionario, TwFilial where eplsnm like'"+nome.toUpperCase()+"%' and stn1 = stno");
			List<Object[]> list = query.getResultList();
			List<UsuarioBean> result = new ArrayList<UsuarioBean>();
			for (Object [] pair : list) {
				TwFuncionario usuario = (TwFuncionario)pair[0];
				TwFilial filial = (TwFilial)pair[1];
				UsuarioBean usuarioBean = new UsuarioBean();
				usuarioBean.setFilialStr(filial.getStnm());
				usuarioBean.setNome(usuario.getEplsnm());
				usuarioBean.setEmail(usuario.getEmail());
				usuarioBean.setFilial(Integer.valueOf(usuario.getStn1()).toString());
				usuarioBean.setMatricula(usuario.getEpidno());
				usuarioBean.setTelefone(usuario.getTelefone());
				usuarioBean.setLogin(usuario.getLogin());
				usuarioBean.setSenha(usuario.getSenha());
				query = manager.createQuery("From AdmPerfilSistemaUsuario u where idTwUsuario.epidno=:epidno");
				query.setParameter("epidno", usuarioBean.getMatricula());
				List<AdmPerfilSistemaUsuario> admPerfilSistemaUsuarioList = query.getResultList();
				List<SistemaBean> sistemaList = new ArrayList<SistemaBean>();
				for (AdmPerfilSistemaUsuario admPerfilSistemaUsuario : admPerfilSistemaUsuarioList) {
					SistemaBean bean = new SistemaBean();
					bean.setDescricao(admPerfilSistemaUsuario.getIdSistema().getDescricao());
					bean.setId(admPerfilSistemaUsuario.getIdSistema().getId().intValue());
					bean.setSigla(admPerfilSistemaUsuario.getIdSistema().getSigla());
					bean.setDescricaoPerfil(admPerfilSistemaUsuario.getIdPerfil().getDescricao());
					PerfilBean perfilBean = new PerfilBean();
					perfilBean.setDescricao(admPerfilSistemaUsuario.getIdPerfil().getDescricao());
					perfilBean.setId(admPerfilSistemaUsuario.getIdPerfil().getId().intValue());
					perfilBean.setSigla(admPerfilSistemaUsuario.getIdPerfil().getSigla());
					bean.setPerfilBean(perfilBean);
					sistemaList.add(bean);				
				}
				usuarioBean.setSistemaList(sistemaList);
				result.add(usuarioBean);
			}
			return result;
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
	public UsuarioBean findAllUsersByMatricula(String matricula){
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			//manager.getTransaction().begin();
			Query query = manager.createQuery("From TwFuncionario, TwFilial where epidno ='"+matricula.toUpperCase()+"' and stn1 = stno");
			List<Object[]> list = query.getResultList();
			List<UsuarioBean> result = new ArrayList<UsuarioBean>();
			Object [] pair  =  list.get(0);
			TwFuncionario usuario = (TwFuncionario)pair[0];
			TwFilial filial = (TwFilial)pair[1];
			UsuarioBean usuarioBean = new UsuarioBean();
			usuarioBean.setFilialStr(filial.getStnm());
			usuarioBean.setNome(usuario.getEplsnm());
			usuarioBean.setEmail(usuario.getEmail());
			usuarioBean.setFilial(Integer.valueOf(usuario.getStn1()).toString());
			usuarioBean.setMatricula(usuario.getEpidno());
			usuarioBean.setTelefone(usuario.getTelefone());
			usuarioBean.setLogin(usuario.getLogin());
			usuarioBean.setSenha(usuario.getSenha());
			query = manager.createQuery("From AdmPerfilSistemaUsuario u where idTwUsuario.epidno=:epidno");
			query.setParameter("epidno", usuarioBean.getMatricula());
			List<AdmPerfilSistemaUsuario> admPerfilSistemaUsuarioList = query.getResultList();
			List<SistemaBean> sistemaList = new ArrayList<SistemaBean>();
			for (AdmPerfilSistemaUsuario admPerfilSistemaUsuario : admPerfilSistemaUsuarioList) {
				SistemaBean bean = new SistemaBean();
				bean.setDescricao(admPerfilSistemaUsuario.getIdSistema().getDescricao());
				bean.setId(admPerfilSistemaUsuario.getIdSistema().getId().intValue());
				bean.setSigla(admPerfilSistemaUsuario.getIdSistema().getSigla());
				bean.setDescricaoPerfil(admPerfilSistemaUsuario.getIdPerfil().getDescricao());
				PerfilBean perfilBean = new PerfilBean();
				perfilBean.setDescricao(admPerfilSistemaUsuario.getIdPerfil().getDescricao());
				perfilBean.setId(admPerfilSistemaUsuario.getIdPerfil().getId().intValue());
				perfilBean.setSigla(admPerfilSistemaUsuario.getIdPerfil().getSigla());
				bean.setPerfilBean(perfilBean);
				sistemaList.add(bean);				
			}
			usuarioBean.setSistemaList(sistemaList);


			return usuarioBean;
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

	public String alterPassword(String login, String senhaAntiga, String senhaAtual){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			
			Query query = manager.createQuery(EJBQL_ALTER_PASSWORD);
			query.setParameter("login", login);
			query.setParameter("senha", senhaAntiga);
			
			TwFuncionario usuario = (TwFuncionario)query.getSingleResult();
			usuario.setSenha(senhaAtual);
			manager.getTransaction().commit();
			return "Senha Alterada com sucesso!";
			
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
	
	public Boolean removerUsuario(String matricula){
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			TwFuncionario usuario = manager.find(TwFuncionario.class, matricula);
			manager.remove(usuario);
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
	
	public Boolean removerPerfilSistema(String matricula, Integer idSistema, Integer idPerfil){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			Query query = manager.createNativeQuery("delete from adm_perfil_sistema_usuario where id_tw_usuario = '"+matricula+"' and id_perfil ="+idPerfil+" and id_sistema ="+idSistema);
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
//	public List<FilialBean> findAllFilial(){
//		List<FilialBean> beanList = new ArrayList<FilialBean>();
//		try {
//			EntityManager manager = null;
//			manager = JpaUtil.getInstance();
//			Query query = manager.createQuery("from TwFilial");
//			List<TwFilial> filialList = query.getResultList();
//			for (TwFilial twFilial : filialList) {
//				FilialBean bean = new FilialBean();
//				bean.setStno(twFilial.getStno());
//				bean.setDescricao(twFilial.getStnm().trim());
//				beanList.add(bean);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return beanList;
//	}
	public List<UsuarioBean> findAllFuncionariosByCampoPesquisa(String campoPesquisa, UsuarioBean usuarioBean) {
		EntityManager manager = null;
		List<UsuarioBean> beans = new ArrayList<UsuarioBean>();

		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createQuery("FROM TwFuncionario, AdmPerfilSistemaUsuario as p" +
					" WHERE (EPIDNO LIKE '%"+campoPesquisa+"%' or EPLSNM LIKE '%"+campoPesquisa+"%')" +
					" AND p.idPerfil.id = (select id from AdmPerfil where tipoSistema = 'PMP' and sigla = 'AJUD')" +
					" and p.idTwUsuario.epidno = epidno" +
					" AND STN1 = "+ Long.valueOf(usuarioBean.getFilial()));
			
			List<Object[]> list = (List<Object[]>)query.getResultList();
			
			for(Object[] pair : list){
				TwFuncionario bean = (TwFuncionario)pair[0];
				UsuarioBean objUsuario = new UsuarioBean();				
				objUsuario.setNome(bean.getEplsnm());
				objUsuario.setMatricula(bean.getEpidno());
				objUsuario.setFilial(bean.getStn1());
								
				beans.add(objUsuario);				
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}	
		}
		return beans;

	}
	/**
	 * Recupera os técnicos de uma filial
	 * @param campoPesquisa
	 * @return
	 */
	public List<UsuarioBean> findAllTecnicos(UsuarioBean usuarioBean) {
		EntityManager manager = null;
		List<UsuarioBean> beans = new ArrayList<UsuarioBean>();
		
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createQuery("FROM TwFuncionario f, AdmPerfilSistemaUsuario as p" +
					" WHERE p.idPerfil.id = (select id from AdmPerfil where tipoSistema = 'PMP' and sigla = 'USUTEC')" +
					" and p.idTwUsuario.epidno = f.epidno"+
					" and f.stn1 = "+ Long.valueOf(usuarioBean.getFilial()));
			
			List<Object[]> list = (List<Object[]>)query.getResultList();
			
			for(Object[] pair : list){
				TwFuncionario bean = (TwFuncionario)pair[0];
				UsuarioBean objUsuario = new UsuarioBean();				
				objUsuario.setNome(bean.getEplsnm());
				objUsuario.setMatricula(bean.getEpidno());
				objUsuario.setFilial(bean.getStn1());
				
				beans.add(objUsuario);				
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}	
		}
		return beans;
		
	}
	/**
	 * Recupera os técnicos da filial
	 * @param campoPesquisa
	 * @return
	 */
	public List<UsuarioBean> findAllFuncionarios(String campoPesquisa, UsuarioBean usuarioBean) {
		EntityManager manager = null;
		List<UsuarioBean> beans = new ArrayList<UsuarioBean>();
		
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createQuery("FROM TwFuncionario, AdmPerfilSistemaUsuario as p" +
					" WHERE (EPIDNO LIKE '%"+campoPesquisa+"%' or EPLSNM LIKE '%"+campoPesquisa+"%')" +
					//" AND p.idPerfil.id = (select id from AdmPerfil where tipoSistema = 'PMP' and sigla = 'AJUD')" +
			" and p.idTwUsuario.epidno = epidno");
			//" AND STN1 = "+ Long.valueOf(usuarioBean.getFilial()));
			
			List<Object[]> list = (List<Object[]>)query.getResultList();
			
			for(Object[] pair : list){
				TwFuncionario bean = (TwFuncionario)pair[0];
				UsuarioBean objUsuario = new UsuarioBean();				
				objUsuario.setNome(bean.getEplsnm());
				objUsuario.setMatricula(bean.getEpidno());
				objUsuario.setFilial(bean.getStn1());
				
				beans.add(objUsuario);				
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}	
		}
		return beans;
		
	}
}
