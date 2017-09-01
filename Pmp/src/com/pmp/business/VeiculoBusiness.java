package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.type.descriptor.java.UUIDTypeDescriptor.ToBytesTransformer;

import com.pmp.bean.DetalhesVeiculosBean;
import com.pmp.bean.FilialBean;
import com.pmp.bean.OperacionalBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.entity.PmpConfigManutencao;
import com.pmp.entity.PmpConfiguracaoPrecos;
import com.pmp.entity.PmpDetalhesVeiculos;
import com.pmp.entity.TwFilial;
import com.pmp.entity.TwFuncionario;
import com.pmp.util.JpaUtil;

public class VeiculoBusiness {

	public List<DetalhesVeiculosBean> findallDetalhes(){
		EntityManager manager = null;
		List<DetalhesVeiculosBean> listaDetalhesVeiculos = new ArrayList<DetalhesVeiculosBean>();
		try {
			manager = JpaUtil.getInstance();

			String SQL="from PmpDetalhesVeiculos";

			Query query = manager.createQuery(SQL);

			List<PmpDetalhesVeiculos> result = query.getResultList();
			if(query.getResultList().size() >0){
				for (PmpDetalhesVeiculos detalhes : result) {
					DetalhesVeiculosBean bean = new DetalhesVeiculosBean();
					TwFilial filial = manager.find(TwFilial.class, detalhes.getIdFilial());
					bean.setFilial(filial.getStnm());
					if(detalhes.getResponsavel() == null){
						bean.setResponsavelNome("SEM TÉCNICO");
						bean.setResponsavel(null);
					}else{
						bean.setResponsavelNome(detalhes.getResponsavel().getEplsnm());
						bean.setResponsavel(detalhes.getResponsavel());
					}
					bean.setFilial(filial.getStnm());
					bean.toBean(bean, detalhes);
					listaDetalhesVeiculos.add(bean);
				}
			}
			return listaDetalhesVeiculos;

		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
			return null;
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}

	}

	public DetalhesVeiculosBean saveOrUpdate(DetalhesVeiculosBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpDetalhesVeiculos detalhes = null;
			if( bean.getId() == null || bean.getId() == 0){
				detalhes = new PmpDetalhesVeiculos();
				TwFuncionario func = manager.find(TwFuncionario.class, bean.getResponsavelId());
				bean.fromBean(bean, detalhes);
				if(bean.getResponsavelId().equals("-1000")){
					detalhes.setResponsavel(null);
					bean.setResponsavelNome("SEM TECNICO");
				}else{
					detalhes.setResponsavel(func);
					func.setPlacaVeiculo(bean.getPlaca());
					func.setCodigoVeiculo(bean.getFrota());
					if(bean.getFrota().charAt(0) == 'C'){
						func.setTipoVeiculo("T");
					}else{
						func.setTipoVeiculo("C");
					}
				}
				manager.persist(detalhes);

			}else{
				detalhes = manager.find(PmpDetalhesVeiculos.class, bean.getId());
				TwFuncionario func = manager.find(TwFuncionario.class, bean.getResponsavelId());
				if(bean.getResponsavelId().equals("-1000")){
					detalhes.setResponsavel(null);
					bean.setResponsavelNome("SEM TECNICO");
				}else{
					detalhes.setResponsavel(func);
					func.setPlacaVeiculo(bean.getPlaca().toUpperCase());
					func.setCodigoVeiculo(bean.getFrota().toUpperCase());
					if(bean.getFrota().charAt(0) == 'C' || bean.getFrota().charAt(0) == 'c'){
						func.setTipoVeiculo("T");
					}else{
						func.setTipoVeiculo("C");
					}
					manager.merge(func);
				}
				bean.fromBean(bean, detalhes);
				manager.merge(detalhes);
				
			}
			manager.getTransaction().commit();
			bean.setId(detalhes.getId().longValue());
			return bean;
		}catch (Exception e) {
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

	public List<UsuarioBean> findAllTecnicosVeiculos() {
		EntityManager manager = null;
		List<UsuarioBean> beans = new ArrayList<UsuarioBean>();

		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("FROM TwFuncionario f, AdmPerfilSistemaUsuario as p" +
					" WHERE p.idPerfil.id = (select id from AdmPerfil where tipoSistema = 'PMP' and sigla = 'USUTEC')" +
					" and p.idTwUsuario.epidno = f.epidno"+
					" and f.eplsnm not in ('PARTNER', 'CUSTOMER')" +
					" order by f.eplsnm");

			List<Object[]> list = (List<Object[]>)query.getResultList();

			UsuarioBean objUsuario = new UsuarioBean();				
			objUsuario.setNome("SEM TÉCNICO");
			objUsuario.setMatricula("-1000");
			objUsuario.setFilial(null);
			beans.add(objUsuario);
			for(Object[] pair : list){
				TwFuncionario bean = (TwFuncionario)pair[0];
				objUsuario = new UsuarioBean();				
				objUsuario.setNome(bean.getEplsnm());
				objUsuario.setMatricula(bean.getEpidno());
				objUsuario.setFilial(bean.getStn1());
				objUsuario.setCodVeiculo(bean.getCodigoVeiculo());
				objUsuario.setPlaca(bean.getPlacaVeiculo());
				beans.add(objUsuario);				
			}		

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
		return beans;

	}

	public Boolean excluirVeiculo(DetalhesVeiculosBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpDetalhesVeiculos.class, bean.getId().longValue()));
			manager.getTransaction().commit();	
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}	
		}



	}


}
