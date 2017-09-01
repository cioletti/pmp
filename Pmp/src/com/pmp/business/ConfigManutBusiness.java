package com.pmp.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.BusinessGroupBean;
import com.pmp.bean.ConfigManutencaoBean;
import com.pmp.bean.ConfigManutencaoHorasBean;
import com.pmp.bean.ModeloBean;
import com.pmp.bean.PrefixoBean;
import com.pmp.bean.RangerBean;
import com.pmp.bean.StandardJobBean;
import com.pmp.bean.TipoFrequenciaBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.entity.PmpAgendamento;
import com.pmp.entity.PmpConfigHorasStandard;
import com.pmp.entity.PmpConfigManutencao;
import com.pmp.entity.PmpConfigOperacional;
import com.pmp.entity.PmpConfiguracaoPrecos;
import com.pmp.entity.PmpContHorasStandard;
import com.pmp.entity.PmpContMesesStandard;
import com.pmp.entity.PmpContrato;
import com.pmp.entity.PmpFamilia;
import com.pmp.entity.PmpFrequenciaPadrao;
import com.pmp.entity.PmpOsOperacional;
import com.pmp.entity.PmpStatusAgendamento;
import com.pmp.entity.PmpTipoFrequencia;
import com.pmp.entity.PmpTipoTracao;
import com.pmp.util.JpaUtil;

public class ConfigManutBusiness {
    private String ID_FUNCIONARIO;
	private static final String SQL_FIND_ALL_BUSINESS_GROUPS = "select distinct(bgrp) from pmp_manutencao";
	private static final String SQL_FIND_ALL_PREFIXOS = "select distinct SUBSTR (m.beqmns,1,4) from pmp_manutencao m order by SUBSTR (m.beqmns,1,4)";
	private static final String SQL_FIND_ALL_MODELOS = "select distinct eqmfmd from pmp_hora where eqmfmd is not null order by eqmfmd ";
	private UsuarioBean usuarioBean;
	public ConfigManutBusiness(UsuarioBean bean) {
		this.usuarioBean = bean;
		ID_FUNCIONARIO = bean.getMatricula();
	}
	
	public List<ModeloBean> findAllModelos(){
		List<ModeloBean> modelos = new ArrayList<ModeloBean>();
		EntityManager manager = null;

		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createNativeQuery(SQL_FIND_ALL_MODELOS);
			List<String> result = query.getResultList();
			for (String modelo : result) {
				ModeloBean bean = new ModeloBean();
				bean.setDescricao(modelo);
				modelos.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return modelos;
	}
	
	public List<ModeloBean> findAllModelos(Long idFamilia){
		List<ModeloBean> modelos = new ArrayList<ModeloBean>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createNativeQuery("SELECT ai.descricao, ai.id_arv " +
													"FROM arv_inspecao ai " +
													"WHERE ai.id_familia = " + idFamilia + " AND ai.id_pai IS NULL " +
													"ORDER BY ai.descricao");
			
			List<Object[]> result = (List<Object[]>) query.getResultList();
			for (Object [] modelo : result) {
				ModeloBean bean = new ModeloBean();
				bean.setDescricao(modelo[0].toString());
				bean.setIdModelo(Long.valueOf((modelo[1]).toString()));
				modelos.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return modelos;
	}
	
	public List<PrefixoBean> findAllPrefixos(String modelo){
		List<PrefixoBean> prefixos = new ArrayList<PrefixoBean>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createNativeQuery("select distinct SUBSTRING (h.bserno,1,4) from pmp_range h where h.eqmfm2 = '"+modelo+"'order by SUBSTRING (h.bserno,1,4)");
			List<String> result = query.getResultList();
			for (String prefixo : result) {
				PrefixoBean bean = new PrefixoBean();
				bean.setDescricao(prefixo);
				prefixos.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return prefixos;
	}
	
	public List<BusinessGroupBean> findAllBusinessGroup(){
		List<BusinessGroupBean> bgs = new ArrayList<BusinessGroupBean>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createNativeQuery(SQL_FIND_ALL_BUSINESS_GROUPS);
			List<String> result = query.getResultList();
			for (String bg : result) {
				BusinessGroupBean bean = new BusinessGroupBean();
				bean.setDescricao(bg);
				bgs.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bgs;
	}
	
	public List<BusinessGroupBean> findBusinessGroupPM(){
		List<BusinessGroupBean> bgs = new ArrayList<BusinessGroupBean>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createNativeQuery("SELECT DISTINCT(bgrp) FROM pmp_manutencao m WHERE m.bgrp = 'PM'");
			List<String> result = query.getResultList();
			for (String bg : result) {
				BusinessGroupBean bean = new BusinessGroupBean();
				bean.setDescricao(bg);
				bgs.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bgs;
	}
	
	public List<RangerBean> findRangerPmp(String modelo, String prefixo){
		List<RangerBean> ranges = new ArrayList<RangerBean>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createNativeQuery("SELECT  SHL.BEGSN2 AS BEGSN2, SHL.ENDSN2 AS ENDSN2 from pmp_range SHL WHERE LTrim(RTrim(SUBSTRING(SHL.BSERNO,1,4))) = '"+prefixo+"' AND SHL.EQMFM2 = '"+modelo+"'");
			List<Object[]> result = query.getResultList();
			for (Object[] objects : result) {
				RangerBean bean = new RangerBean();
				bean.setBeginRanger((String)objects[0]);
				bean.setEndRanger((String)objects[1]);
				bean.setDescricao((String)objects[0]+" - "+ (String)objects[1]);
				ranges.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return ranges;
	}
	
	public ConfigManutencaoBean saveOrUpdate(ConfigManutencaoBean bean, String isGeradorStandby) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpConfigManutencao manut = null;
			PmpConfiguracaoPrecos precos = manager.find(PmpConfiguracaoPrecos.class, bean.getIdPrecoConfig());
			PmpFamilia familia = manager.find(PmpFamilia.class, bean.getIdFamilia());
			PmpTipoTracao tipoTracao = null;
			if(bean.getIdTipoTracao() != null && bean.getIdTipoTracao() != 0){
				tipoTracao = manager.find(PmpTipoTracao.class, bean.getIdTipoTracao());
			}
			
			String SQL = "select ID, id_configuracao_preco from pmp_config_manutencao where BGRP = '"+bean.getBusinessGroup()+"'"+ 
					   " and PREFIXO = '"+bean.getPrefixo()+"'"+ 
					   " and MODELO = '"+bean.getModelo()+"'"+
					   " and BEGINRANGE = '"+bean.getBeginRanger()+"'"+
					   " and ENDRANGE = '"+bean.getEndRanger()+"'"+
					   " and CONT_EXCESSAO = '"+bean.getContExcessao()+"'" +
					   	" and IS_ATIVO is null ";
			if(bean.getContExcessao().equals("S")){
				SQL+=" and ID_CONFIGURACAO_PRECO = '"+bean.getIdPrecoConfig()+"'";
			}
			Query query = manager.createNativeQuery(SQL);
			if( bean.getId() == null || bean.getId() == 0){
				if(query.getResultList().size() > 0){
					return null;
				}
				
				manut = bean.toBean(bean);
				manut.setIdConfiguracaoPreco(precos);
				manut.setIdFuncionario(ID_FUNCIONARIO);
				manut.setIdFamilia(familia);
				manut.setDataConfiguracao(new Date());
				manut.setIsGeradorStandby(isGeradorStandby);
				manager.persist(manut);
			} else {
				if(query.getResultList().size() > 0){
					Object[] pair = (Object[])query.getSingleResult();
					if(bean.getId() != ((BigDecimal)pair[0]).intValue() &&
							bean.getIdPrecoConfig() == ((BigDecimal)pair[1]).longValue()){
					
						return null;
					
					}
				}
				manut = manager.find(PmpConfigManutencao.class, bean.getId().longValue());
				bean.toBean(bean, manut);
				manut.setIdFamilia(familia);
				manut.setIdConfiguracaoPreco(precos);
				manut.setIsGeradorStandby(isGeradorStandby);
				manager.merge(manut);
			}
			manager.getTransaction().commit();
			setStandardJobList(bean.getStandardJobList(), manut, manager);
			bean.setId(manut.getId().intValue());
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
	
	private void setStandardJobList(List<StandardJobBean> list, PmpConfigManutencao manut, EntityManager manager){
		manager.getTransaction().begin();
		Query query = manager.createNativeQuery("delete from pmp_config_horas_standard where id_config_manutencao = "+manut.getId());
		query.executeUpdate();
		for (int i = 0; i < list.size(); i++) {
			PmpConfigHorasStandard horas = new PmpConfigHorasStandard();
			StandardJobBean jobBean = list.get(i);
			horas.setHoras(jobBean.getHoras());
			horas.setStandardJobCptcd(jobBean.getStandardJob());
			horas.setHorasRevisao(jobBean.getHorasRevisao());
			horas.setIdConfigManutencao(manut);
			manager.merge(horas);
			jobBean.setId(horas.getId());
		}
		manager.getTransaction().commit();
	}
	
	public List<ConfigManutencaoBean> findConfiguracao(String modelo, String isGeradorStandby){
		EntityManager manager = null;
		List<ConfigManutencaoBean> configlist = new ArrayList<ConfigManutencaoBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("From PmpConfigManutencao cm where cm.isGeradorStandby = '" + isGeradorStandby + "' AND cm.modelo like ':modelo%' order by cm.beginrange".replaceAll(":modelo", modelo.toUpperCase()));
			List<PmpConfigManutencao> manutencaoList = query.getResultList();
			for (int i = 0; i < manutencaoList.size(); i++) {
				PmpConfigManutencao manutencao = manutencaoList.get(i);
				ConfigManutencaoBean manutencaoBean = new ConfigManutencaoBean();
				manutencaoBean.fromBean(manutencao);
				List<StandardJobBean> standardJobList = new ArrayList<StandardJobBean>();
				for (Iterator iterator = manutencao.getPmpConfigHorasStandardCollection().iterator(); iterator.hasNext();) {
					PmpConfigHorasStandard standardJobBean = (PmpConfigHorasStandard) iterator.next();
					StandardJobBean bean = new StandardJobBean();
					bean.setId(standardJobBean.getId());
					bean.setHoras(standardJobBean.getHoras());
					bean.setStandardJob(standardJobBean.getStandardJobCptcd());
					bean.setHorasRevisao(standardJobBean.getHorasRevisao());
					standardJobList.add(bean);
				}
				Collections.sort(standardJobList);
				manutencaoBean.setStandardJobList(standardJobList);
				manutencaoBean.setRangerList(this.findRangerPmp(manutencaoBean.getModelo(), manutencaoBean.getPrefixo()));
				manutencaoBean.setBusinessGroupList(this.findAllBusinessGroup());
				manutencaoBean.setPrefixoList(this.findAllPrefixos(manutencaoBean.getModelo()));
				manutencaoBean.setModeloList(this.findAllModelos(manutencao.getIdFamilia().getId()));
				manutencaoBean.setIdPrecoConfig((manutencao.getIdConfiguracaoPreco() != null)?manutencao.getIdConfiguracaoPreco().getId().longValue():null);
				manutencaoBean.setIdFamilia((manutencao.getIdFamilia() != null)?manutencao.getIdFamilia().getId():null);
				manutencaoBean.setTipoPreco(manutencao.getTipoPreco());
				manutencaoBean.setDescTipoPreco(manutencao.getContExcessao().equals("S")?"SIM":"NÃO");
				manutencaoBean.setDescPreco(manutencao.getIdConfiguracaoPreco().getDescricao());
				configlist.add(manutencaoBean);
			}
			
			return configlist;
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
		return configlist;
	}
	
	public Boolean removerConfPmp(ConfigManutencaoBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpConfigManutencao.class, bean.getId().longValue()));
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
	
	public List<TipoFrequenciaBean> findAllFrequencia(String descricao){
		EntityManager manager = null;
		List<TipoFrequenciaBean> result = new ArrayList<TipoFrequenciaBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query;
			
			if (descricao.equals("")) {
				query = manager.createQuery("FROM PmpTipoFrequencia WHERE UPPER(descricao) NOT LIKE '%STANDBY%'");
			} else {
				query = manager.createQuery("FROM PmpTipoFrequencia WHERE descricao LIKE '%" + descricao + "%'");
			}
			
			List<PmpTipoFrequencia> list = query.getResultList();
			for (int i = 0; i < list.size(); i++) {
				PmpTipoFrequencia tipo = list.get(i);
				TipoFrequenciaBean frequenciaBean = new TipoFrequenciaBean();
				frequenciaBean.setDescricao(tipo .getDescricao());
				frequenciaBean.setId(tipo .getId());		
				
				List<StandardJobBean> frequenciaList = new ArrayList<StandardJobBean>();
				for (Iterator iterator = tipo.getPmpFrequenciaPadraoCollection().iterator(); iterator.hasNext();) {
					PmpFrequenciaPadrao frequencia = (PmpFrequenciaPadrao) iterator.next();
					StandardJobBean bean = new StandardJobBean();
					bean.setId(frequencia.getId());
					bean.setHoras(frequencia.getHoras().longValue());
					bean.setJobCode(frequencia.getJobCode());
					bean.setSequencia(frequencia.getSequencia());
					bean.setStandardJob(frequencia.getStandard());
					bean.setHorasRevisao(frequencia.getHorasRevisao());
					frequenciaList.add(bean);
				}
				frequenciaBean.setFrequenciaList(frequenciaList);
				result.add(frequenciaBean);
			}
			return result;
		} catch (Exception e) {			
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	/**
	 * Recupera todas as revisões pendentes de contrato pós pago
	 * @param numContrato
	 * @return
	 */
	public List<ConfigManutencaoHorasBean> findAllHorasManutencao(String numContrato){
		EntityManager manager = null;
		List<ConfigManutencaoHorasBean> result = new ArrayList<ConfigManutencaoHorasBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("from PmpContrato c, PmpConfigOperacional op where c.numeroContrato =:numeroContrato and c.idTipoContrato.id in (select id from PmpTipoContrato where sigla in ('VEN', 'REN', 'CAN')) " +
					" and op.idContrato.id = c.id and op.filial =:filial and c.idStatusContrato.id = (select id from PmpStatusContrato where sigla = 'CA')");
			query.setParameter("numeroContrato", numContrato);
			query.setParameter("filial", Long.valueOf(this.usuarioBean.getFilial()));
			List<Object[]> contratoList = query.getResultList();
			if(contratoList.size() > 0){
				PmpContrato contrato = (PmpContrato)contratoList.get(0)[0];
				//verifica se tem alguma OS que não foi realizada
				query = manager.createQuery("from PmpContHorasStandard where idContrato.id =:idContrato and idOsOperacional.id is null and isExecutado = 'N' order by horasManutencao");
				query.setParameter("idContrato", contrato.getId());
				if(query.getResultList().size() == 0){
					return result;
				}
				query = manager.createQuery("from PmpContHorasStandard where idContrato.id =:idContrato and idOsOperacional.id is null and isExecutado = 'N' order by horasManutencao");
				query.setParameter("idContrato", contrato.getId());
				List<PmpContHorasStandard> contHorasStandards = query.getResultList();
				for (PmpContHorasStandard pmpContHorasStandard : contHorasStandards) {
					ConfigManutencaoHorasBean bean = new ConfigManutencaoHorasBean();
					bean.setId(pmpContHorasStandard.getId());
					bean.setHorasManutencao(pmpContHorasStandard.getHorasManutencao());
					bean.setNumContrato(numContrato);
					result.add(bean);
				}
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	public Boolean pularRevisao(ConfigManutencaoHorasBean horasBean, UsuarioBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			Query query = manager.createQuery("from PmpContrato c, PmpConfigOperacional op where c.id = op.idContrato.id and c.numeroContrato =:numeroContrato");
			query.setParameter("numeroContrato", horasBean.getNumContrato());
			Object[] pair = (Object[])query.getSingleResult();
			PmpConfigOperacional configOperacional = (PmpConfigOperacional)pair[1]; 
			PmpOsOperacional os = new PmpOsOperacional();
			os.setIdConfigOperacional(configOperacional);
			PmpContHorasStandard contHorasStandard = manager.find(PmpContHorasStandard.class, horasBean.getId()); 
			os.setIdContHorasStandard(contHorasStandard);
			os.setNumOs("PULAR "+horasBean.getHorasManutencao());
			os.setNumDoc("S/N");
			os.setFilial(Long.valueOf(this.usuarioBean.getFilial()));
			os.setCodErroOsDbs("00");
			os.setNumeroSegmento("01");
			os.setJobCode("540");
			os.setCompCode(contHorasStandard.getStandardJobCptcd());
			os.setCscc("SV");
			os.setInd("E");
			os.setCodErroDocDbs("00");
			manager.persist(os);
			contHorasStandard.setIsExecutado("S");
			contHorasStandard.setIdOsOperacional(os);
			manager.merge(contHorasStandard);
			PmpAgendamento ag = new PmpAgendamento();
			query = manager.createQuery("from PmpStatusAgendamento where sigla = 'FIN'");
			PmpStatusAgendamento pmpStatusAgendamento = (PmpStatusAgendamento)query.getSingleResult();
			ag.setIdStatusAgendamento(pmpStatusAgendamento);
			ag.setIdCongOperacional(configOperacional);
			ag.setIdFuncionario(bean.getMatricula());
			ag.setIdFuncSupervisor(this.usuarioBean.getMatricula());
			ag.setDataAgendamento(new Date());
			ag.setDataFaturamento(new Date());
			ag.setHorasAgendadas(BigDecimal.valueOf(horasBean.getHorasManutencao()));
			ag.setNumOs("PULAR "+horasBean.getHorasManutencao());
			ag.setIdContHorasStandard(contHorasStandard);
			ag.setFilial(Long.valueOf(this.usuarioBean.getFilial()));
			ag.setIsFindTecnico("S");
			ag.setObs("Revisão não realizada o resposável por pular essa revisão foi "+this.usuarioBean.getNome());
			ag.setTipoPm(contHorasStandard.getTipoPm());
			ag.setHorasAgendadas(BigDecimal.valueOf(0.00D));
			manager.persist(ag);
			query = manager.createQuery("from PmpContHorasStandard where idContrato.id =:idContrato and isExecutado = 'N'");
			query.setParameter("idContrato", contHorasStandard.getIdContrato().getId());
			if(query.getResultList().size() == 0){
				contHorasStandard.getIdContrato().setIsAtivo("I");
			}
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
