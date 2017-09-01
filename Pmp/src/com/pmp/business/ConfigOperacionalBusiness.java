package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.ConfigOperacionalBean;
import com.pmp.bean.ConfigurarCustomizacaoBean;
import com.pmp.bean.FilialBean;
import com.pmp.entity.PmpConfigOperacional;
import com.pmp.entity.PmpContrato;
import com.pmp.entity.PmpContratoCustomizacao;
import com.pmp.entity.PmpTipoCustomizacao;
import com.pmp.entity.TwFilial;
import com.pmp.util.EmailHelper;
import com.pmp.util.JpaUtil;

public class ConfigOperacionalBusiness {

	
	public Boolean alterarFilial(FilialBean filialBean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			TwFilial twFilial = manager.find(TwFilial.class, filialBean.getStno());
			twFilial.setCnpj(filialBean.getCnpj());
			twFilial.setRazaoSocial(filialBean.getRazaoSocial());
			twFilial.setEndereco(filialBean.getEndereco());
			twFilial.setCep(filialBean.getCep());
			manager.merge(twFilial);
			manager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			if(manager.getTransaction().isActive()){
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
	
	
	public List<FilialBean> findAllFilial(){
		EntityManager manager = null;
		List<FilialBean> filialList = new ArrayList<FilialBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("select stno, stnm, cnpj, razaoSocial, endereco, cep from TwFilial where stno not in (18, 38, 40, 32, 30) order by stnm");
			List<Object[]> result = query.getResultList();
			for (Object [] filial: result) {
				FilialBean bean = new FilialBean();
				bean.setStno((Long)filial[0]);
				bean.setStnm(filial[1].toString());
				bean.setCnpj((String)filial[2]);
				
				bean.setRazaoSocial((String)filial[3]);
				bean.setEndereco((String)filial[4]);
				bean.setCep((String)filial[5]);
				filialList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return filialList;
	}
	
	
	public ConfigOperacionalBean findConfigOperacional(Long idContrato){
		ConfigOperacionalBean co = new ConfigOperacionalBean();
		EntityManager manager = null;

		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("From PmpConfigOperacional Where idContrato.id = :idContrato");
			query.setParameter("idContrato", idContrato);
			List<PmpConfigOperacional> result = query.getResultList();
			if(result.size() > 0){				
				co.formBean(result.get(0));	
			}					
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return co;
	}
	
	public ConfigOperacionalBean saveOrUpdate(ConfigOperacionalBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpContrato pmpContrato = manager.find(PmpContrato.class,bean.getIdContrato());
			pmpContrato.setEmailContatoServicos(bean.getEmail());
			pmpContrato.setPrefixo(bean.getNumeroSerie().substring(0,4));
			pmpContrato.setNumeroSerie(bean.getNumeroSerie());
			pmpContrato.setBeginRanger(bean.getBeginRanger());
			pmpContrato.setEndRanger(bean.getEndRanger());
			PmpConfigOperacional configOperacional = null;
			
			if(bean.getId() == null || bean.getId() == 0){
				configOperacional = new PmpConfigOperacional();
				bean.toBean(configOperacional);
				configOperacional.setIdContrato(pmpContrato);
				manager.persist(configOperacional);
			}else{
				configOperacional = manager.find(PmpConfigOperacional.class, bean.getId());
				bean.toBean(configOperacional);
				configOperacional.setIdContrato(pmpContrato);
				manager.merge(configOperacional);
			}
			
			Query query = manager.createNativeQuery("delete from PMP_CONTRATO_CUSTOMIZACAO where id_contrato =:id_contrato");
			query.setParameter("id_contrato", pmpContrato.getId());
			query.executeUpdate();
			if(bean.getTipoCustomizacaoList() != null){
				for (ConfigurarCustomizacaoBean beanAux : bean.getTipoCustomizacaoList()) {
					if(beanAux.getIsSelected()){
						PmpContratoCustomizacao contratoCustomizacao = new PmpContratoCustomizacao();
						contratoCustomizacao.setIdContrato(pmpContrato);
						contratoCustomizacao.setIdTipoCustomizacao(manager.find(PmpTipoCustomizacao.class, beanAux.getIdTipoCustomizacao()));
						manager.persist(contratoCustomizacao);
					}
				}
			}
			
			
			manager.getTransaction().commit();
			AgendamentoBusiness business = new AgendamentoBusiness();
			business.saveAgendamentosPendentes(pmpContrato.getId());
			bean.setId(configOperacional.getId());
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
	
	public Boolean enviarObsCliente(String obsEmail, String email) {
		
		try {
			EmailHelper helper = new EmailHelper();
			if(helper.sendSimpleMail(""+",Gostaríamos de informar que:\n"+obsEmail, "Manutenção PMP", email)){
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println("0GCT00714".substring(0,4));
		System.out.println("0GCT00714".substring(4,"0GCT00714".length()));
	}



}
