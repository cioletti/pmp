package com.pmp.business;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.IdClass;
import javax.persistence.Query;

import org.hibernate.ejb.criteria.ValueHandlerFactory.LongValueHandler;

import com.pmp.bean.AgendamentoBean;
import com.pmp.bean.DataHeaderBean;
import com.pmp.bean.PecasDbsBean;
import com.pmp.bean.StatusAgendamentoBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.entity.PmpAgendamento;
import com.pmp.entity.PmpAgendamentoPendente;
import com.pmp.entity.PmpConfigOperacional;
import com.pmp.entity.PmpContHorasStandard;
import com.pmp.entity.PmpContHorasStandardPlus;
import com.pmp.entity.PmpContrato;
import com.pmp.entity.PmpDescontoMultVac;
import com.pmp.entity.PmpFluxoDatas;
import com.pmp.entity.PmpOsOperacional;
import com.pmp.entity.PmpPecas;
import com.pmp.entity.PmpStatusAgendamento;
import com.pmp.entity.TwFuncionario;
import com.pmp.util.ConectionDbs;
import com.pmp.util.DateHelper;
import com.pmp.util.EmailHelper;
import com.pmp.util.IConstantAccess;
import com.pmp.util.JpaUtil;
import com.pmp.util.ValorMonetarioHelper;

public class AgendamentoBusiness {
	private String ID_FUNCIONARIO;
	private String FILIAL;
	private UsuarioBean usuarioBean;
	String semana[] = {"Domingo","Segunda-feira","Terça-feira","Quarta-feira","Quinta-feira","Sexta-feira","Sábado","Domingo"}; 
	
	public AgendamentoBusiness() {
	}
	
	public AgendamentoBusiness(UsuarioBean bean) {
		ID_FUNCIONARIO = bean.getMatricula();
		FILIAL = bean.getFilial();
		this.usuarioBean = bean;
	}
	
	public List<DataHeaderBean> findAllHeaderList(String data){
		List<DataHeaderBean> result = new ArrayList<DataHeaderBean>();
		try {
			Calendar calendar = new GregorianCalendar();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			if(data == null){
				calendar.setTime(new Date());
			}else{
				try {
					calendar.setTime(sdf.parse(data));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			for(int i = 0; i < 7; i++){
				DataHeaderBean bean = new DataHeaderBean();
				bean.setData(calendar.getTime());
				bean.setDescricao(semana[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
				bean.setDateString(sdf.format(calendar.getTime()));
				result.add(bean);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<UsuarioBean> findAllTecnico(List<DataHeaderBean> dataHeaderList){
		List<UsuarioBean> usuList = new ArrayList<UsuarioBean>();
		EntityManager manager = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormatBR = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createNativeQuery("select EPLSNM, EPIDNO from tw_funcionario t, adm_perfil_sistema_usuario ps"+
												" where t.epidno = ps.id_tw_usuario"+
												" and t.stn1 =:filial"+
												" and ps.id_sistema = (select sis.id from adm_sistema sis where sis.sigla = 'PMP')"+
												" and ps.id_perfil = (select p.id from adm_perfil p where p.sigla = 'USUTEC' and p.tipo_sistema = 'PMP')" +
												" order by EPLSNM");
			query.setParameter("filial", Integer.valueOf(FILIAL));
			List<Object[]> result = query.getResultList();
			for (Object [] pair : result) {
				UsuarioBean bean = new UsuarioBean();
				bean.setNome((((String)pair[0]).length() > 30)?((String)pair[0]).substring(0,30):((String)pair[0]));
				bean.setMatricula((String)pair[1]);
				List<AgendamentoBean> agendamentoList = new ArrayList<AgendamentoBean>();
				for (DataHeaderBean dataHeaderBean : dataHeaderList) {
					query = manager.createNativeQuery("select a.id, a.id_status_agendamento, a.id_cong_operacional, a.id_funcionario, a.data_agendamento, a.horas_revisao, s.sigla, a.num_os, a.id_cont_horas_standard, CONVERT(varchar(1000), a.obs) as obs, a.data_faturamento, a.id_ajudante," +
							" (select EPLSNM from TW_FUNCIONARIO where EPIDNO = a.ID_AJUDANTE) as nome_ajudante, (select cast(palm.obs as varchar(255)) from os_palm palm where palm.id_agendamento = a.id) as obsCheckList, CONVERT(varchar(4000),a.obs_tecnico) obsTecnico, a.id_cont_horas_standard_plus" +
							" from pmp_agendamento a, pmp_status_agendamento s" +
							" where CONVERT(varchar(10), a.data_agendamento, 103) = '"+dataHeaderBean.getDateString()+"' " +
							" and a.id_funcionario = :id_funcionario " +
							" and a.id_status_agendamento = s.id " +
							" and a.filial ='"+FILIAL+"'");
					query.setParameter("id_funcionario", (String)pair[1]);
//					System.out.println((String)pair[1]);
//					System.out.println("select a.id, a.id_status_agendamento, a.id_cong_operacional, a.id_funcionario, a.data_agendamento, a.horas_revisao, s.sigla, a.num_os, a.id_cont_horas_standard, CONVERT(varchar(1000), a.obs) as obs, a.data_faturamento, a.id_ajudante," +
//							" (select EPLSNM from TW_FUNCIONARIO where EPIDNO = a.ID_AJUDANTE) as nome_ajudante, (select cast(palm.obs as varchar(255)) from os_palm palm where palm.id_agendamento = a.id) as obsCheckList, CONVERT(varchar(4000),a.obs_tecnico) obsTecnico, a.id_cont_horas_standard_plus" +
//							" from pmp_agendamento a, pmp_status_agendamento s" +
//							" where CONVERT(varchar(10), a.data_agendamento, 103) = '"+dataHeaderBean.getDateString()+"' " +
//							" and a.id_funcionario = :id_funcionario " +
//							" and a.id_status_agendamento = s.id " +
//							" and a.filial ='"+FILIAL+"'");
					List<Object[]> objectList = (List<Object[]>)query.getResultList();
					AgendamentoBean agendamentoBeanInterno = new AgendamentoBean();
					for (Object[] objects : objectList) {
						AgendamentoBean agendamentoBean = new AgendamentoBean();
						
						if(objects[2] != null){	
							PmpConfigOperacional operacional = manager.find(PmpConfigOperacional.class, ((BigDecimal)objects[2]).longValue());
//							if(((String)objects[7]).equals("A022119")){
//								System.out.println("");
//							}
							agendamentoBean.setObsOs(operacional.getObs());
							agendamentoBean.setNumSerie(operacional.getIdContrato().getNumeroSerie());
							agendamentoBean.setModelo(operacional.getIdContrato().getModelo());
							agendamentoBean.setContato(operacional.getContato());
							agendamentoBean.setLocal(operacional.getLocal());
							agendamentoBean.setTelefone(operacional.getTelefoneContato());
							agendamentoBean.setEmailContato(operacional.getIdContrato().getEmailContatoComercial());
							agendamentoBean.setRazaoSocial(operacional.getIdContrato().getRazaoSocial());
							agendamentoBean.setIdConfOperacional((objects[2] != null)?((BigDecimal)objects[2]).longValue():null);
							agendamentoBean.setHorasRevisao((objects[5] != null)? ((BigDecimal)objects[5]).longValue(): null);
							if(objects[8] != null){
								PmpContHorasStandard standard = manager.find(PmpContHorasStandard.class, ((BigDecimal)objects[8]).longValue());
								agendamentoBean.setIdContHorasStandard(standard.getId());
								//agendamentoBean.setHorasTrabalhadas(this.findHorasRevisao(standard.getId()).doubleValue());
							}else if(objects[15] != null){
								PmpContHorasStandardPlus standard = manager.find(PmpContHorasStandardPlus.class, ((BigDecimal)objects[15]).longValue());
								agendamentoBean.setIdContHorasStandard(standard.getId());
							}
//							query = manager.createQuery("From PmpPecas where idAgendamento.id = :idAgendamento");
//							query.setParameter("idAgendamento", ((BigDecimal)objects[0]).longValue());
//							List<PmpPecas> pecas = query.getResultList();
//							List<PecasDbsBean> pecasList = new ArrayList<PecasDbsBean>();
//							for (PmpPecas pmpPecas : pecas) {
//								PecasDbsBean dbsBean = new PecasDbsBean();
//								dbsBean.setCodigo(pmpPecas.getCodigo());
//								dbsBean.setNomePeca(pmpPecas.getNomePeca());
//								dbsBean.setNumPeca(pmpPecas.getNumPeca());
//								dbsBean.setQtd(pmpPecas.getQtd().intValue());
//								pecasList.add(dbsBean);
//							}
//							agendamentoBean.setPecasList(pecasList);
						}
						
						agendamentoBean.setId(((BigDecimal)objects[0]).longValue());
						agendamentoBean.setIdStatusAgendamento(((BigDecimal)objects[1]).longValue());
						agendamentoBean.setIdFuncionario((String)objects[3]);
						
						agendamentoBean.setDataAgendamento(dateFormatBR.format(dateFormat.parse((String)objects[4])));
						agendamentoBean.setSiglaStatus((String)objects[6]);
						agendamentoBean.setNumOs((String)objects[7]);
						agendamentoBean.setObs((String)objects[9]);
						if((String)objects[10] != null){
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							
							Date d = sdf2.parse((String)objects[10]);
							String dataFaturamento = sdf.format(d);
							agendamentoBean.setDataFaturamento(dataFaturamento);			
							
						}
						agendamentoBean.setIdAjudante((String)objects[11]);
						agendamentoBean.setNomeAjudante((String)objects[12]);
						agendamentoBean.setObsCheckList((String)objects[13]);
						agendamentoBean.setObsTecnico((String)objects[14]);
						agendamentoBeanInterno.getAgendamentoList().add(agendamentoBean);
					}
					agendamentoList.add(agendamentoBeanInterno);
				}
				bean.setAgendamentoList(agendamentoList);
				usuList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		List<UsuarioBean> listCustomerPartner = new ArrayList<UsuarioBean>();
		for (int i = 0; i < usuList.size(); i++) {
			UsuarioBean usuarioBean = usuList.get(i);
			if(usuarioBean.getNome().equals("CUSTOMER") || usuarioBean.getNome().equals("PARTNER")){
				listCustomerPartner.add(usuarioBean);
				usuList.remove(i);
				i--;
			}
		}
		usuList.addAll(listCustomerPartner);
		return usuList;
	}
	
	public String verificarFilialTecnico(AgendamentoBean bean){
		EntityManager manager = null;
		try{
			manager = JpaUtil.getInstance();
			
			TwFuncionario funcionario = manager.find(TwFuncionario.class, bean.getIdFuncionario());
			
			
			PmpConfigOperacional operacional = manager.find(PmpConfigOperacional.class, bean.getIdConfOperacional());
			
			if(!operacional.getFilial().equals(new Long(funcionario.getStn1())) || !this.usuarioBean.getFilial().equals(funcionario.getStn1())){
				return "O técnico não pertence a filial que a OS foi criada, ou o destino a filial de destino da máquina não está na filial que a OS foi criada, favor atualizar a tela!";
			}
			
		}catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			try{
				if(manager != null && manager.isOpen()){
					manager.close();
				}
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}
	
	public List<UsuarioBean> saveOrUpdate(AgendamentoBean bean, List<DataHeaderBean> dataHeaderList){
		EntityManager manager = null;
		Connection con = null;
		Statement prstmtDataUpdate = null;
		ResultSet rs = null;
		try{
			con = com.pmp.util.ConectionDbs.getConnecton(); 
			manager = JpaUtil.getInstance();
			
			PmpConfigOperacional operacional = manager.find(PmpConfigOperacional.class, bean.getIdConfOperacional());
			
			
			if(operacional.getIdContrato().getIdTipoContrato().getSigla().equals("VPG")){
				prstmtDataUpdate = con.createStatement();
				String SQL = "select TRIM(w.ACTI) ACTI, TRIM(w.OPNDT8) OPNDT8 from "+IConstantAccess.LIB_DBS+".WOPHDRS0 w  where TRIM(w.WONO) = '"+operacional.getNumOs()+"'"; 
        		rs = prstmtDataUpdate.executeQuery(SQL);
        		if(rs.next()){
        			String ACTI = rs.getString("ACTI");
        			if(!ACTI.equals("I")){
        				return null;
        			}
        		}
			}
			
			manager.getTransaction().begin();
			operacional.setContato(bean.getContato()!=null?bean.getContato().toUpperCase():null);
			operacional.setLocal(bean.getLocal()!= null?bean.getLocal().toUpperCase():null);
			operacional.setTelefoneContato(bean.getTelefone());
			if(bean.getEmailContato() != null){
				operacional.getIdContrato().setEmailContatoComercial(bean.getEmailContato().toUpperCase());
			}
			PmpStatusAgendamento statusAgendamento = manager.find(PmpStatusAgendamento.class, bean.getIdStatusAgendamento());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			if(!DateHelper.verificarDatasVencimento(sdf.parse(bean.getDataAgendamento())) && !operacional.getIdContrato().getIdClassificacaoContrato().getSigla().equals("PART") && !operacional.getIdContrato().getIdClassificacaoContrato().getSigla().equals("CUS")){
				return null;
			}		
			// Sun Jul 07 00:00:00 BRT 2013
			Calendar calendar = Calendar.getInstance();
			Calendar calBegin = new GregorianCalendar();
			calBegin.setTime(sdf.parse(bean.getDataAgendamento()));
			calBegin.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
			calBegin.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
			calBegin.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
					
			
			PmpAgendamento agendamento = new PmpAgendamento();
			if(bean.getId() != null && bean.getId() > 0){
				agendamento = manager.find(PmpAgendamento.class, bean.getId());
				Query query = manager.createNativeQuery("delete from pmp_pecas where id_agendamento = :id_agendamento");
				query.setParameter("id_agendamento", bean.getId());
				query.executeUpdate();
			}
			agendamento.setObsTecnico(bean.getObsTecnico());
			agendamento.setIdAjudante(bean.getIdAjudante());
			//agendamento.setHorasAgendadas(BigDecimal.valueOf(Double.valueOf(bean.getHorasTrabalhadas())));
			Calendar calEnd = new GregorianCalendar();
			calEnd.setTime(sdf.parse(bean.getDataAgendamentoFinal()));
			calEnd.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
			calEnd.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
			calEnd.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
			
			//System.out.println(calBegin.getTime());
			String complemento = "";
			
			PmpContrato contrato = manager.find(PmpContrato.class, operacional.getIdContrato().getId());
			if(operacional.getIdContrato().getIdClassificacaoContrato().getSigla().equals("PLUS")){
				agendamento.setHorasAgendadas(BigDecimal.valueOf(IConstantAccess.horasManutPlus));
				PmpContHorasStandardPlus standard = manager.find(PmpContHorasStandardPlus.class, bean.getIdContHorasStandard());
				if(standard !=null && standard.getIdContrato() != null){

					Query query = manager.createNativeQuery("select COUNT(*) from PMP_CONTRATO c, PMP_CONFIG_OPERACIONAL op, PMP_CONT_HORAS_STANDARD_PLUS hs"+
							" where c.id = "+standard.getIdContrato().getId()+
							" and c.ID = op.ID_CONTRATO"+
							" and hs.ID_CONTRATO = c.id"+
							" and c.is_ativo is null"+
					" and IS_EXECUTADO = 'N'");

					Integer result = (Integer)query.getSingleResult();

					if(result == 1){
						contrato.setIsAtivo("I");
						contrato.setDataFinalizacao(new Date());
						manager.merge(contrato);					
					}					
				}
//				if(standard.getIdContrato().getIdConfigTracao() != null){
//					complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+standard.getIdContrato().getIdConfigTracao().getId()+") or ocptmd is null)"
//					+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+standard.getIdContrato().getIdConfigTracao().getId()+") or ojbloc is null)";
//				}
				
				while(calBegin.getTimeInMillis() <= calEnd.getTimeInMillis()){
					agendamento.setDataAgendamento(calBegin.getTime());
					agendamento.setIdCongOperacional(operacional);
					agendamento.setIdFuncSupervisor(ID_FUNCIONARIO);
					agendamento.setIdFuncionario(bean.getIdFuncionario());
					agendamento.setIdStatusAgendamento(statusAgendamento);
					agendamento.setTipoPm(standard.getTipoPm());
					if(statusAgendamento.getSigla().equals("FIN")){
						standard.setIsExecutado("S");
					}else{
						standard.setIsExecutado("N");
					}
					agendamento.setNumOs(bean.getNumOs());
					agendamento.setHorasRevisao(bean.getHorasRevisao());
					agendamento.setFilial(Long.valueOf(FILIAL));
					agendamento.setIdContHorasStandardPlus(standard);
					if(bean.getId() != null && bean.getId() > 0){
						manager.merge(agendamento);
					}else{
						manager.persist(agendamento);
					}
					for (int i = 0; i < bean.getPecasList().size(); i++) {
						PecasDbsBean pecasBean = (PecasDbsBean)bean.getPecasList().get(i);
						PmpPecas pecas = new PmpPecas();
						pecas.setCodigo(pecasBean.getCodigo());
						pecas.setIdAgendamento(agendamento);
						pecas.setNomePeca(pecasBean.getNomePeca());
						pecas.setNumPeca(pecasBean.getNumPeca());
						pecas.setQtd(pecasBean.getQtd().longValue());
						manager.persist(pecas);
					}
					calBegin.add(Calendar.DAY_OF_MONTH, 1);

					//manager.persist(agendamento);
				}
			}else{
				agendamento.setHorasAgendadas(BigDecimal.valueOf(Double.valueOf(bean.getHorasTrabalhadas())));
				PmpContHorasStandard standard = manager.find(PmpContHorasStandard.class, bean.getIdContHorasStandard());
				try {
					if(standard.getIsMultVac() != null && standard.getIsMultVac().equals("S")){
						Query query = manager.createQuery("from PmpDescontoMultVac");
						if(query.getResultList().size() > 0){
							PmpDescontoMultVac multVac = (PmpDescontoMultVac)query.getResultList().get(0);
							if(multVac.getDescontoMultiVac() > 0 && BigDecimal.valueOf(Double.valueOf(bean.getHorasTrabalhadas())).longValue() > 0){
								BigDecimal descontoMultVac = BigDecimal.valueOf(multVac.getDescontoMultiVac()).divide(BigDecimal.valueOf(100));
								BigDecimal  totalHHManutencao = BigDecimal.valueOf(Double.valueOf(bean.getHorasTrabalhadas()));
								totalHHManutencao = totalHHManutencao.subtract(totalHHManutencao.multiply(descontoMultVac));
								totalHHManutencao = totalHHManutencao.setScale(2,RoundingMode.HALF_UP);
								agendamento.setHorasAgendadas(totalHHManutencao);
							}
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}		

				if(standard !=null && standard.getIdContrato() != null){

					Query query = manager.createNativeQuery("select COUNT(*) from PMP_CONTRATO c, PMP_CONFIG_OPERACIONAL op, PMP_CONT_HORAS_STANDARD hs"+
							" where c.id = "+standard.getIdContrato().getId()+
							" and c.ID = op.ID_CONTRATO"+
							" and hs.ID_CONTRATO = c.id"+
							" and c.is_ativo is null"+
					" and IS_EXECUTADO = 'N'");

					Integer result = (Integer)query.getSingleResult();

					if(result == 1){
						contrato.setIsAtivo("I");
						manager.merge(contrato);					
					}					
				}
				if(standard.getIdContrato().getIdConfigTracao() != null){
					complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+standard.getIdContrato().getIdConfigTracao().getId()+") or ocptmd is null)";
					complemento += " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+standard.getIdContrato().getIdConfigTracao().getId()+") or JWKAPP is null)"
					+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+standard.getIdContrato().getIdConfigTracao().getId()+") or ojbloc is null)";
				}

				Query query = manager.createNativeQuery("select m.dlrqty, m.PANO20, m.DS18 from pmp_manutencao m"+
						" where m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and m.bgrp = 'PM'"+
						" and substring(m.beqmsn,1,4) = '"+standard.getIdContrato().getPrefixo()+"'"+
						complemento+
						" and substring(m.beqmsn,5,10) between '"+standard.getIdContrato().getBeginRanger().substring(4, 9)+"' and '"+standard.getIdContrato().getEndRanger().substring(4, 9)+"'");

				List<Object[]> pecasList = query.getResultList();

				if(pecasList.size() == bean.getPecasList().size()){
					for (int i = 0; i < bean.getPecasList().size(); i++) {
						PecasDbsBean pecasBean = (PecasDbsBean)bean.getPecasList().get(i);
						boolean isEquals = false;
						for (Object[] pair : pecasList) {
							if(pecasBean.getQtd().intValue() == ((BigDecimal)pair[0]).intValue()  && pecasBean.getNumPeca().trim().equals((String)pair[1]) ){
								isEquals = true;
							}
						}
						if(!isEquals){
							//new EmailHelper().sendSimpleMail("Verifique urgente a "+bean.getNumOs()+" da revisao de "+bean.getHorasRevisao()+", pois, existe diferenca entre as pecas da revisao do Stander Job\n e as da OS", "Urgente PMP", "rodrigo@rdrsistemas.com.br");
							//new EmailHelper().sendSimpleMail("Verifique urgente a "+bean.getNumOs()+" da revisao de "+bean.getHorasRevisao()+", pois, existe diferenca entre as pecas da revisao do Stander Job\n e as da OS", "Urgente PMP", "correa_maiky@pesa.com.br");
							new EmailHelper().sendSimpleMail("Verifique urgente a "+bean.getNumOs()+" da revisao de "+bean.getHorasRevisao()+", pois, existe diferenca entre as pecas da revisao do Stander Job\n e as da OS", "Urgente PMP", "moura_leonardo@pesa.com.br");
							//new EmailHelper().sendSimpleMail("Verifique urgente a "+bean.getNumOs()+" da revisao de "+bean.getHorasRevisao()+", pois, existe diferenca entre as pecas da revisao do Stander Job\n e as da OS", "Urgente PMP", "costa_flademir@pesa.com.br");
							//new EmailHelper().sendSimpleMail("Verifique urgente a "+bean.getNumOs()+" da revisao de "+bean.getHorasRevisao()+", pois, existe diferenca entre as pecas da revisao do Stander Job\n e as da OS", "Urgente PMP", "salomao.freitas@marcosa.com.br");
							break;
						}
					}
				}else{
					//new EmailHelper().sendSimpleMail("Verifique urgente a "+bean.getNumOs()+" da revisao de"+bean.getHorasRevisao()+", pois, existe diferenca entre as pecas da revisao do Stander Job\n e as da OS", "Urgente PMP", "rodrigo@rdrsistemas.com.br");
					//new EmailHelper().sendSimpleMail("Verifique urgente a "+bean.getNumOs()+" da revisao de "+bean.getHorasRevisao()+", pois, existe diferenca entre as pecas da revisao do Stander Job\n e as da OS", "Urgente PMP", "correa_maiky@pesa.com.br");
					new EmailHelper().sendSimpleMail("Verifique urgente a "+bean.getNumOs()+" da revisao de "+bean.getHorasRevisao()+", pois, existe diferenca entre as pecas da revisao do Stander Job\n e as da OS", "Urgente PMP", "moura_leonardo@pesa.com.br");
					//new EmailHelper().sendSimpleMail("Verifique urgente a "+bean.getNumOs()+" da revisao de"+bean.getHorasRevisao()+", pois, existe diferenca entre as pecas da revisao do Stander Job\n e as da OS", "Urgente PMP", "costa_flademir@pesa.com.br");
					//new EmailHelper().sendSimpleMail("Verifique urgente a "+bean.getNumOs()+" da revisao de"+bean.getHorasRevisao()+", pois, existe diferenca entre as pecas da revisao do Stander Job\n e as da OS", "Urgente PMP", "salomao.freitas@marcosa.com.br");
				}
			
				while(calBegin.getTimeInMillis() <= calEnd.getTimeInMillis()){
					agendamento.setDataAgendamento(calBegin.getTime());
					agendamento.setIdCongOperacional(operacional);
					agendamento.setIdFuncSupervisor(ID_FUNCIONARIO);
					agendamento.setIdFuncionario(bean.getIdFuncionario());
					agendamento.setIdStatusAgendamento(statusAgendamento);
					agendamento.setTipoPm(standard.getTipoPm());
					if(statusAgendamento.getSigla().equals("FIN")){
						standard.setIsExecutado("S");
					}else{
						standard.setIsExecutado("N");
					}
					agendamento.setNumOs(bean.getNumOs());
					agendamento.setHorasRevisao(bean.getHorasRevisao());
					agendamento.setFilial(Long.valueOf(FILIAL));
					agendamento.setIdContHorasStandard(standard);
					if(bean.getId() != null && bean.getId() > 0){
						manager.merge(agendamento);
					}else{
						manager.persist(agendamento);
					}
					for (int i = 0; i < bean.getPecasList().size(); i++) {
						PecasDbsBean pecasBean = (PecasDbsBean)bean.getPecasList().get(i);
						PmpPecas pecas = new PmpPecas();
						pecas.setCodigo(pecasBean.getCodigo());
						pecas.setIdAgendamento(agendamento);
						pecas.setNomePeca(pecasBean.getNomePeca());
						pecas.setNumPeca(pecasBean.getNumPeca());
						pecas.setQtd(pecasBean.getQtd().longValue());
						manager.persist(pecas);
					}
					calBegin.add(Calendar.DAY_OF_MONTH, 1);

					//manager.persist(agendamento);
				}
				query = manager.createQuery("From PmpOsOperacional where numOs =:numOs");
				query.setParameter("numOs", bean.getNumOs());
				PmpOsOperacional osOperacional = (PmpOsOperacional)query.getSingleResult();
				try {
					String SQL = "select lbamt, frsthr from " +IConstantAccess.AMBIENTE_DBS+".USPSGSM0 where wonosm = '"+osOperacional.getId().toString()+"-A'";
					prstmtDataUpdate = con.createStatement();
					rs = prstmtDataUpdate.executeQuery(SQL);
					rs.next();
					String totalMO = rs.getString("lbamt");
					String horas = rs.getString("frsthr");
					String pair = "'"+osOperacional.getId().toString()+"-A','"+osOperacional.getNumeroSegmento()+"','"+osOperacional.getCscc()+"','"+osOperacional.getJobCode()+"','"+osOperacional.getCompCode()+"','"+osOperacional.getInd()+"','F','"+horas+"','F','"+totalMO+"','00001'";
					
					 SQL = "delete from " +IConstantAccess.AMBIENTE_DBS+".USPSGSM0 where wonosm = '"+osOperacional.getId().toString()+"-A'";
					 prstmtDataUpdate = con.createStatement();
					 prstmtDataUpdate.executeUpdate(SQL);
					if((contrato.getIdClassificacaoContrato().getSigla().equals("PART") && standard.getIsPartner().equals("S")) || contrato.getIdClassificacaoContrato().getSigla().equals("CUS") || contrato.getIdClassificacaoContrato().getSigla().equals("CUSLIGHT")){
						pair += ",'ENVP'";
						SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPSGSM0 (wonosm, wosgno, cscc, jbcd, cptcd, ind, shpfld, frsthr, lbrate, lbamt, qty, mecr) values("+pair+")";
						prstmtDataUpdate = con.createStatement();
						prstmtDataUpdate.executeUpdate(SQL);
					} else {
						TwFuncionario funcionario = manager.find(TwFuncionario.class, bean.getIdFuncionario());
						pair += ",'"+funcionario.getLogin()+"'";
						SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPSGSM0 (wonosm, wosgno, cscc, jbcd, cptcd, ind, shpfld, frsthr, lbrate, lbamt, qty, mecr) values("+pair+")";
						
						prstmtDataUpdate = con.createStatement();
						prstmtDataUpdate.executeUpdate(SQL);
					}
				} catch (Exception e) {
					StringWriter writer = new StringWriter();
					e.printStackTrace(new PrintWriter(writer));
					EmailHelper emailHelper = new EmailHelper();
		        	emailHelper.sendSimpleMail("OS "+bean.getNumOs()+" Erro ao enviar mecanico responsavel! "+writer.toString(), "Erro ao inserir MECÂNICO RESPONSÁVEL "+osOperacional.getId().toString(), "rodrigo@rdrsistemas.com.br");
					e.printStackTrace();
				}
			}
			//bean.setId(agendamento.getId().longValue());

			if(statusAgendamento.getSigla().equals("EA") || statusAgendamento.getSigla().equals("FIN")){
				PmpFluxoDatas fluxoDatas = new PmpFluxoDatas(); 
				String data = null;
				String coluna = null;

				if(statusAgendamento.getSigla().equals("EA")){
					data = sdf.format(agendamento.getDataAgendamento());
					coluna = "005";
				}else{					
					Date dateAux = new Date();					
					data = sdf.format(dateAux);
					coluna = "006";					
				}
				fluxoDatas.setColuna(coluna);
				fluxoDatas.setData(data);
				fluxoDatas.setIdAgendamento(agendamento);
				manager.merge(fluxoDatas);
				
//				query = manager.createNativeQuery("select ESTIMATEBY from tw_funcionario where EPIDNO = '"+agendamento.getIdFuncionario()+"'");
//
//				String estimateBy = (String)query.getSingleResult();			
//
//				prstmtDataUpdate = setDateFluxoOSDBS(data, con, agendamento.getNumOs(), coluna, estimateBy);

			}
			
			manager.getTransaction().commit();
			TwFuncionario funcionario = manager.find(TwFuncionario.class, bean.getIdFuncionario()); 
			if(statusAgendamento.getSigla().equals("EA")){
				new EmailHelper().sendSimpleMail("Informamos que a OS "+bean.getNumOs()+", cliente "+contrato.getRazaoSocial()+", contato "+contrato.getContatoServicos()+", telefone "+contrato.getTelefoneServicos()+" foi enviada!\nFavor aceitar ou recusar.", "OS Enviada Sistema Campo", funcionario.getEmail());
			}
			this.saveAgendamentosPendentes(contrato.getId());
			
		}catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			try{
				if(manager != null && manager.isOpen()){
					manager.close();
				}
				if(prstmtDataUpdate != null){					
					prstmtDataUpdate.close();
				}
				if(rs != null){
					rs.close();
				}
				if(con != null){
					con.close();
				}

			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return this.findAllTecnico(dataHeaderList);
	}
		
	public PreparedStatement setDateFluxoOSDBS(String dataPrevisao,
			Connection con, String numeroOs, String coluna, String estimateBy) throws SQLException {
		PreparedStatement prstmt_ = null;
		ResultSet rs = null;
		try {
			String SQL = "select TRIM(W.NTDA) NTDA, substring(W.NTDA, 0, 15) as campoReplace from "+IConstantAccess.LIB_DBS+".WOPNOTE0 W where TRIM(W.WONO) = '"+numeroOs+"' AND TRIM(W.NTLNO1) = '"+coluna+"' AND TRIM(W.WOSGNO) = ''";
			prstmt_ = con.prepareStatement(SQL);
			rs = prstmt_.executeQuery();
			if(rs.next()){
				String NTDA = rs.getString("NTDA");
				NTDA = NTDA.substring(0, 43);
				String campoReplace = rs.getString("campoReplace");
				String dataAux = NTDA.replace(campoReplace, dataPrevisao+" "+((estimateBy != null) ? estimateBy : "   "));
				SQL = "update "+IConstantAccess.LIB_DBS+".WOPNOTE0 W set W.NTDA = '"+dataAux+"' where TRIM(W.WONO) = '"+numeroOs+"' AND TRIM(W.NTLNO1) = '"+coluna+"' AND TRIM(W.WOSGNO) = ''";
				prstmt_ = con.prepareStatement(SQL);
				prstmt_.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			rs.close();
			prstmt_.close();
		}
		return prstmt_;
	}	
	public PreparedStatement setDateFluxoOSDBSNaoAutomatico(String dataPrevisao,
			Connection con, String numeroOs, String coluna, String estimateBy) throws Exception {
		PreparedStatement prstmt_ = null;
		ResultSet rs = null;
		try {
			String SQL = "select TRIM(W.NTDA) NTDA, substring(W.NTDA, 0, 15) as campoReplace from "+IConstantAccess.LIB_DBS+".WOPNOTE0 W where TRIM(W.WONO) = '"+numeroOs+"' AND TRIM(W.NTLNO1) = '"+coluna+"' AND TRIM(W.WOSGNO) = ''";
			prstmt_ = con.prepareStatement(SQL);
			rs = prstmt_.executeQuery();
			if(rs.next()){
				String NTDA = rs.getString("NTDA");
				NTDA = NTDA.substring(0, 43);
				String campoReplace = rs.getString("campoReplace");
				String dataAux = NTDA.replace(campoReplace, dataPrevisao+" BLQ");
				dataAux += ((this.usuarioBean.getEstimateBy() != null) ? this.usuarioBean.getEstimateBy() : "   ");
				//String dataAux = NTDA.replace(campoReplace, dataPrevisao+" "+((estimateBy != null) ? estimateBy : "   "));
				SQL = "update "+IConstantAccess.LIB_DBS+".WOPNOTE0 W set W.NTDA = '"+dataAux+"' where TRIM(W.WONO) = '"+numeroOs+"' AND TRIM(W.NTLNO1) = '"+coluna+"' AND TRIM(W.WOSGNO) = ''";
				prstmt_ = con.prepareStatement(SQL);
				prstmt_.executeUpdate();
			}
		} catch (Exception e) {
			throw e;
		} finally{
			rs.close();
			prstmt_.close();
		}
		return prstmt_;
	}	
	public PreparedStatement setDateFluxoOSDBSEncerrarAutomatica(String dataPrevisao,
			Connection con, String numeroOs, String coluna, String estimateBy) throws Exception {
		PreparedStatement prstmt_ = null;
		ResultSet rs = null;
		try {
			String SQL = "select TRIM(W.NTDA) NTDA, substring(W.NTDA, 0, 15) as campoReplace from "+IConstantAccess.LIB_DBS+".WOPNOTE0 W where TRIM(W.WONO) = '"+numeroOs+"' AND TRIM(W.NTLNO1) = '"+coluna+"' AND TRIM(W.WOSGNO) = ''";
			prstmt_ = con.prepareStatement(SQL);
			rs = prstmt_.executeQuery();
			if(rs.next()){
				String NTDA = rs.getString("NTDA");
				NTDA = NTDA.substring(0, 43);
				String campoReplace = rs.getString("campoReplace");
				String dataAux = NTDA.replace(campoReplace, dataPrevisao+" AUT");
				dataAux += ((estimateBy != null) ? estimateBy : "   ");
				SQL = "update "+IConstantAccess.LIB_DBS+".WOPNOTE0 W set W.NTDA = '"+dataAux+"' where TRIM(W.WONO) = '"+numeroOs+"' AND TRIM(W.NTLNO1) = '"+coluna+"' AND TRIM(W.WOSGNO) = ''";
				prstmt_ = con.prepareStatement(SQL);
				prstmt_.executeUpdate();
			}
		} catch (Exception e) {
			throw e;
		} finally{
			rs.close();
			prstmt_.close();
		}
		return prstmt_;
	}	
	
	public PreparedStatement setNotesOSDBS(Connection con, String numeroOs, String coluna, String texto) throws Exception {
		PreparedStatement prstmt_ = null;
		try {
				String SQL = "update "+IConstantAccess.LIB_DBS+".WOPNOTE0 W set W.NTDA = '"+texto+"' where TRIM(W.WONO) = '"+numeroOs+"' AND TRIM(W.NTLNO1) = '"+coluna+"' AND TRIM(W.WOSGNO) = ''";
				prstmt_ = con.prepareStatement(SQL);
				prstmt_.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally{
			prstmt_.close();
		}
		return prstmt_;
	}	
	
	public List<AgendamentoBean> findAllOsDisponiveis(Boolean isEdit){
		EntityManager manager = null;
		List<AgendamentoBean> result = new ArrayList<AgendamentoBean>();
		try{
			manager = JpaUtil.getInstance();
			String SQL = "select op.num_os, co.contato, co.local, co.telefone_contato, co.id_contrato, co.id, CONVERT(varchar(1000),co.obs)," +
													"  (select cast(palm.OBS as varchar(255)) from PMP_AGENDAMENTO ag, OS_PALM palm where palm.NUMERO_OS = op.num_os and palm.ID_AGENDAMENTO = ag.ID) as obsCheckList, c.numero_serie, c.EMAIL_CONTATO_COMERCIAL," +
													" 'PRE' classificacao" +
													"   from pmp_cont_horas_standard hs,  pmp_os_operacional op, pmp_config_operacional co, pmp_contrato c" +
													"   where hs.is_executado = 'N' "+
													"	and hs.id_os_operacional is not null" +
													"	and co.id_contrato = c.id" +
													//"   and hs.id not in (select ag.id_cont_horas_standard from pmp_agendamento ag )" +
													"   and hs.id_os_operacional = op.id" +
													"   and co.id_contrato = hs.id_contrato" +
													"   and op.filial =:filial" +
													"   and c.id_classificacao_contrato = (select id from pmp_classificacao_contrato where sigla = 'PRE')"+ 
													"   union "+
			"select op.num_os, co.contato, co.local, co.telefone_contato, co.id_contrato, co.id, CONVERT(varchar(1000),co.obs)," +
													"  (select cast(palm.OBS as varchar(255)) from PMP_AGENDAMENTO ag, OS_PALM palm where palm.NUMERO_OS = op.num_os and palm.ID_AGENDAMENTO = ag.ID) as obsCheckList, c.numero_serie, c.EMAIL_CONTATO_COMERCIAL," +
													" 'CUS' classificacao" +
													"   from pmp_cont_horas_standard hs,  pmp_os_operacional op, pmp_config_operacional co, pmp_contrato c" +
													"   where hs.is_executado = 'N' "+
													"	and hs.id_os_operacional is not null" +
													"	and co.id_contrato = c.id" +
													//"   and hs.id not in (select ag.id_cont_horas_standard from pmp_agendamento ag )" +
													"   and hs.id_os_operacional = op.id" +
													"   and co.id_contrato = hs.id_contrato" +
													"   and op.filial =:filial" +
													"   and c.id_classificacao_contrato in (select id from pmp_classificacao_contrato where sigla in ('CUS', 'CUSLIGHT'))" +
													"   and hs.is_ta = 'S'"+ 
													"   union "+
			"select op.num_os, co.contato, co.local, co.telefone_contato, co.id_contrato, co.id, CONVERT(varchar(1000),co.obs)," +
													"  (select cast(palm.OBS as varchar(255)) from PMP_AGENDAMENTO ag, OS_PALM palm where palm.NUMERO_OS = op.num_os and palm.ID_AGENDAMENTO = ag.ID) as obsCheckList, c.numero_serie, c.EMAIL_CONTATO_COMERCIAL," +
													" 'PART' classificacao" +
													"   from pmp_cont_horas_standard hs,  pmp_os_operacional op, pmp_config_operacional co, pmp_contrato c" +
													"   where hs.is_executado = 'N' "+
													"	and hs.id_os_operacional is not null" +
													"	and co.id_contrato = c.id" +
													//"   and hs.id not in (select ag.id_cont_horas_standard from pmp_agendamento ag )" +
													"   and hs.id_os_operacional = op.id" +
													"   and co.id_contrato = hs.id_contrato" +
													"   and op.filial =:filial" +
													"   and c.id_classificacao_contrato = (select id from pmp_classificacao_contrato where sigla = 'PART')" +
													"   and hs.is_partner = 'N'"+
													"   union "+
													"select op.num_os, co.contato, co.local, co.telefone_contato, co.id_contrato, co.id, CONVERT(varchar(1000),co.obs)," +
													"  (select cast(palm.OBS as varchar(255)) from PMP_AGENDAMENTO ag, OS_PALM palm where palm.NUMERO_OS = op.num_os and palm.ID_AGENDAMENTO = ag.ID) as obsCheckList, c.numero_serie, c.EMAIL_CONTATO_COMERCIAL," +
													" 'PLUS' classificacao" +
													"   from pmp_cont_horas_standard_plus hs,  pmp_os_operacional op, pmp_config_operacional co, pmp_contrato c" +
													"   where hs.is_executado = 'N' "+
													"	and hs.id_os_operacional is not null" +
													"	and co.id_contrato = c.id" +
													//"   and hs.id not in (select ag.id_cont_horas_standard from pmp_agendamento ag )" +
													"   and hs.id_os_operacional = op.id" +
													"   and co.id_contrato = hs.id_contrato" +
													"   and op.filial =:filial" +
													"   and c.id_classificacao_contrato = (select id from pmp_classificacao_contrato where sigla = 'PLUS')"+
													"   order by op.num_os";
													
//													" select op.num_os, co.contato, co.local, co.telefone_contato, co.id_contrato, co.id, CONVERT(varchar(1000),co.obs)," +
//													"  (select cast(palm.OBS as varchar(255)) from PMP_AGENDAMENTO ag, OS_PALM palm where palm.NUMERO_OS = op.num_os and palm.ID_AGENDAMENTO = ag.ID) as obsCheckList, c.numero_serie," +
//													"   'PLUS' classificacao, c.EMAIL_CONTATO_COMERCIAL" +
//													"   from pmp_cont_horas_standard_plus hs,  pmp_os_operacional op, pmp_config_operacional co, pmp_contrato c" +
//													"   where hs.is_executado = 'N' "+
//													"	and hs.id_os_operacional is not null" +
//													"	and co.id_contrato = c.id" +
//													//"   and hs.id not in (select ag.id_cont_horas_standard from pmp_agendamento ag )" +
//													"   and hs.id_os_operacional = op.id" +
//													"   and co.id_contrato = hs.id_contrato" +
//													"   and op.filial =:filial" +
//													"   and c.id_classificacao_contrato = (select id from pmp_classificacao_contrato where sigla = 'PLUS')" +
//													"   and hs.is_ta is null"+ 
//													"   order by op.num_os";
			
			Query query = manager.createNativeQuery(SQL);
			query.setParameter("filial", Integer.valueOf(FILIAL));
			List<Object[]> pairs = (List<Object[]>)query.getResultList();
	
			for (Object[] objects : pairs) {
				if(!isEdit){
					SQL = "select * from PMP_AGENDAMENTO "+
					"	where ID_STATUS_AGENDAMENTO in (select ID from PMP_STATUS_AGENDAMENTO where SIGLA in ('EA', 'FIN', 'AT'))"+
					"	and NUM_OS = '"+(String)objects[0]+"'";
					query = manager.createNativeQuery(SQL);
					if(query.getResultList().size() > 0){
						continue;
					}
				}
				
				AgendamentoBean bean = new AgendamentoBean();
				bean.setNumOs((String)objects[0]);
				bean.setContato((String)objects[1]);
				bean.setLocal((String)objects[2]);
				bean.setTelefone((String)objects[3]);
				bean.setIdConfOperacional(((BigDecimal)objects[5]).longValue());
				bean.setObsOs((String)objects[6]);
				bean.setNumSerie((String)objects[8]);
				bean.setEmailContato((String)objects[9]);
				if(!((String)objects[10]).equals("PLUS")){
					Map<String, Double> map = this.findHorasProximaRevisao(((BigDecimal)objects[4]).longValue(), manager);
					bean.setHorasRevisao(map.get("horas").longValue());
					bean.setIdContHorasStandard(map.get("id").longValue());
					bean.setHorasTrabalhadas(map.get("horasTrabalhadas"));
				}else if(((String)objects[10]).equals("PLUS")){
					Map<String, Double> map = this.findHorasProximaRevisaoPlus(((BigDecimal)objects[4]).longValue(), manager);
					bean.setIdContHorasStandard(map.get("id").longValue());
					bean.setHorasTrabalhadas(IConstantAccess.horasManutPlus.doubleValue());
				}
				bean.setObsCheckList((String)objects[7]);
				bean.setIsOrderOs("S");
				result.add(bean);
			}
			Collections.sort(result, new AgendamentoBean());
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	public List<AgendamentoBean> findAllOsDisponiveisCustomer(Boolean isEdit){
		EntityManager manager = null;
		List<AgendamentoBean> result = new ArrayList<AgendamentoBean>();
		try{
			manager = JpaUtil.getInstance();
			String SQL = "select op.num_os, co.contato, co.local, co.telefone_contato, co.id_contrato, co.id, CONVERT(varchar(1000),co.obs)," +
			"  (select cast(palm.OBS as varchar(255)) from PMP_AGENDAMENTO ag, OS_PALM palm where palm.NUMERO_OS = op.num_os and palm.ID_AGENDAMENTO = ag.ID) as obsCheckList, c.numero_serie," +
			"   'CUS' classificacao, c.EMAIL_CONTATO_COMERCIAL" +
			"   from pmp_cont_horas_standard hs,  pmp_os_operacional op, pmp_config_operacional co, pmp_contrato c" +
			"   where hs.is_executado = 'N' "+
			"	and hs.id_os_operacional is not null" +
			"	and co.id_contrato = c.id" +
			//"   and hs.id not in (select ag.id_cont_horas_standard from pmp_agendamento ag )" +
			"   and hs.id_os_operacional = op.id" +
			"   and co.id_contrato = hs.id_contrato" +
			"   and op.filial =:filial" +
			"   and c.id_classificacao_contrato in (select id from pmp_classificacao_contrato where sigla in ('CUS', 'CUSLIGHT'))" +
			"   and hs.is_ta is null";
			
			
			
			Query query = manager.createNativeQuery(SQL);
			query.setParameter("filial", Integer.valueOf(FILIAL));
			List<Object[]> pairs = (List<Object[]>)query.getResultList();
			for (Object[] objects : pairs) {
//				SQL = "select * from PMP_AGENDAMENTO "+
//				"	where ID_STATUS_AGENDAMENTO in (select ID from PMP_STATUS_AGENDAMENTO where SIGLA in ('EA', 'FIN', 'AT'))"+
//				"	and NUM_OS = '"+(String)objects[0]+"'";
//				query = manager.createNativeQuery(SQL);
//				if(query.getResultList().size() > 0){
//					continue;
//				}
				
				if(!isEdit){
					SQL = "select * from PMP_AGENDAMENTO "+
					"	where ID_STATUS_AGENDAMENTO in (select ID from PMP_STATUS_AGENDAMENTO where SIGLA in ('EA', 'FIN', 'AT'))"+
					"	and NUM_OS = '"+(String)objects[0]+"'";
					query = manager.createNativeQuery(SQL);
					if(query.getResultList().size() > 0){
						continue;
					}
				}
				
				AgendamentoBean bean = new AgendamentoBean();
				bean.setNumOs((String)objects[0]);
				bean.setContato((String)objects[1]);
				bean.setLocal((String)objects[2]);
				bean.setTelefone((String)objects[3]);
				bean.setIdConfOperacional(((BigDecimal)objects[5]).longValue());
				bean.setObsOs((String)objects[6]);
				bean.setNumSerie((String)objects[8]);
				bean.setEmailContato((String)objects[10]);
				//if(((String)objects[9]).equals("CUS")){
					Map<String, Double> map = this.findHorasProximaRevisao(((BigDecimal)objects[4]).longValue(), manager);
					bean.setHorasRevisao(map.get("horas").longValue());
					bean.setIdContHorasStandard(map.get("id").longValue());
					bean.setHorasTrabalhadas(map.get("horasTrabalhadas"));
//				}else{
//					Map<String, Double> map = this.findHorasProximaRevisaoPlus(((BigDecimal)objects[4]).longValue(), manager);
//					bean.setIdContHorasStandard(map.get("id").longValue());
//					bean.setHorasTrabalhadas(IConstantAccess.horasManutPlus.doubleValue());
//				}
				bean.setObsCheckList((String)objects[7]);
				result.add(bean);
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	public List<AgendamentoBean> findAllOsDisponiveisPartner(Boolean isEdit){
		EntityManager manager = null;
		List<AgendamentoBean> result = new ArrayList<AgendamentoBean>();
		try{
			manager = JpaUtil.getInstance();
			String SQL = "select op.num_os, co.contato, co.local, co.telefone_contato, co.id_contrato, co.id, CONVERT(varchar(1000),co.obs)," +
			"  (select cast(palm.OBS as varchar(255)) from PMP_AGENDAMENTO ag, OS_PALM palm where palm.NUMERO_OS = op.num_os and palm.ID_AGENDAMENTO = ag.ID) as obsCheckList, c.numero_serie, c.EMAIL_CONTATO_COMERCIAL," +
			"   hs.is_partner, c.ID_FAMILIA, hs.STANDARD_JOB_CPTCD" +
			"   from pmp_cont_horas_standard hs,  pmp_os_operacional op, pmp_config_operacional co, pmp_contrato c" +
			"   where hs.is_executado = 'N' "+
			"	and hs.id_os_operacional is not null" +
			"	and co.id_contrato = c.id" +
			//"   and hs.id not in (select ag.id_cont_horas_standard from pmp_agendamento ag )" +
			"   and hs.id_os_operacional = op.id" +
			"   and co.id_contrato = hs.id_contrato" +
			"   and op.filial =:filial" +
			"   and c.id_classificacao_contrato = (select id from pmp_classificacao_contrato where sigla = 'PART')" +
			"   and hs.is_partner = 'S'"+ 
			"   order by op.num_os";
			
			
			Query query = manager.createNativeQuery(SQL);
			query.setParameter("filial", Integer.valueOf(FILIAL));
			List<Object[]> pairs = (List<Object[]>)query.getResultList();
			for (Object[] objects : pairs) {
//				SQL = "select * from PMP_AGENDAMENTO "+
//				"	where ID_STATUS_AGENDAMENTO in (select ID from PMP_STATUS_AGENDAMENTO where SIGLA in ('EA', 'FIN', 'AT'))"+
//				"	and NUM_OS = '"+(String)objects[0]+"'";
//				query = manager.createNativeQuery(SQL);
//				if(query.getResultList().size() > 0){
//					continue;
//				}
				if(!isEdit){
					SQL = "select * from PMP_AGENDAMENTO "+
					"	where ID_STATUS_AGENDAMENTO in (select ID from PMP_STATUS_AGENDAMENTO where SIGLA in ('EA', 'FIN', 'AT'))"+
					"	and NUM_OS = '"+(String)objects[0]+"'";
					query = manager.createNativeQuery(SQL);
					if(query.getResultList().size() > 0){
						continue;
					}
				}
				AgendamentoBean bean = new AgendamentoBean();
				bean.setNumOs((String)objects[0]);
				bean.setContato((String)objects[1]);
				bean.setLocal((String)objects[2]);
				bean.setTelefone((String)objects[3]);
				bean.setIdConfOperacional(((BigDecimal)objects[5]).longValue());
				bean.setObsOs((String)objects[6]);
				bean.setNumSerie((String)objects[8]);
				bean.setEmailContato((String)objects[9]);
				Map<String, Double> map = this.findHorasProximaRevisao(((BigDecimal)objects[4]).longValue(), manager);
				bean.setHorasRevisao(map.get("horas").longValue());
				bean.setIdContHorasStandard(map.get("id").longValue());
				bean.setHorasTrabalhadas(map.get("horasTrabalhadas"));
				bean.setObsCheckList((String)objects[7]);
				if(((String)objects[10]).equals("S")){
				   result.add(bean);
				}else if((((String)objects[12]).equals("752A") || ((String)objects[12]).equals("752B")) &&  (((BigDecimal)objects[11]).longValue() == 41 || ((BigDecimal)objects[11]).longValue() == 84)){
					 result.add(bean);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	private Map<String, Double> findHorasProximaRevisao(Long idContrato,EntityManager manager){
		Query query = manager.createNativeQuery("select hs.horas_manutencao, hs.id from pmp_contrato con, pmp_cont_horas_standard hs"+
				"	where con.id = :idContrato"+
				"	and hs.id_contrato = con.id"+
				"	and  hs.is_executado = 'N'"+
				"	order by hs.horas_manutencao");
		query.setParameter("idContrato", idContrato);
		List<Object[]> result = (List<Object[]>)query.getResultList();
		Map<String, Double> map = new HashMap<String, Double>();
		if(result.size() > 0){
			Object[] pair = result.get(0);
			map.put("horas", ((BigDecimal)pair[0]).doubleValue());
			map.put("id", ((BigDecimal)pair[1]).doubleValue());
			map.put("horasTrabalhadas", this.findHorasRevisao(((BigDecimal)pair[1]).longValue()).doubleValue());
		}
		return map;
	}
	private Map<String, Double> findHorasProximaRevisaoPlus(Long idContrato,EntityManager manager){
		Query query = manager.createNativeQuery("select min(hs.id) from pmp_cont_horas_standard_plus hs"+
				"	where hs.id_contrato = :idContrato"+
				"	and  hs.is_executado = 'N'");
		query.setParameter("idContrato", idContrato);
		List result = query.getResultList();
		Map<String, Double> map = new HashMap<String, Double>();
		if(result.size() > 0){
			BigDecimal pair = (BigDecimal)query.getSingleResult();
			//map.put("horas", ((BigDecimal)pair[0]).doubleValue());
			map.put("id", pair.doubleValue());
			//map.put("horasTrabalhadas", this.findHorasRevisao(((BigDecimal)pair[1]).longValue()).doubleValue());
		}
		return map;
	}
	
	/**
	 * Recupera as horas necessárias para realizar a revisão;
	 * @param idAgendamento
	 * @return
	 */
	public BigDecimal findHorasRevisao(Long idContHorasStandard){
			
			EntityManager manager = null;
			BigDecimal totalHHManutencao = BigDecimal.ZERO;
			try {
				manager = JpaUtil.getInstance();
				PmpContHorasStandard hs = manager.find(PmpContHorasStandard.class, idContHorasStandard);
				//total horas para a manutenção
				Query query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
						" where h.cptcd = '"+hs.getStandardJobCptcd()+"'"+
						" and h.bgrp = '"+hs.getIdContrato().getBgrp()+"'"+
						" and substring(h.beqmsn,1,4) = '"+hs.getIdContrato().getPrefixo()+"'"+
						" and substring(h.beqmsn,5,10) between '"+hs.getIdContrato().getBeginRanger().substring(4, 9)+"' and '"+hs.getIdContrato().getEndRanger().substring(4, 9)+"'");
				totalHHManutencao = (BigDecimal)query.getSingleResult();
				if(totalHHManutencao == null){
					totalHHManutencao = BigDecimal.ZERO;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(manager != null && manager.isOpen()){
					manager.close();
				}
			}
			return totalHHManutencao;
		}
	
	public List<PecasDbsBean> findPecas(String numberOs){
		ResultSet rs = null;
		PreparedStatement prstmt_ = null;
		Connection con = null;

//		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
//		Properties prop = new Properties();
		List<PecasDbsBean> pecasList = new ArrayList<PecasDbsBean>();
		try {
//			prop.load(in);
//			String url = prop.getProperty("dbs.url");
//			String user = prop.getProperty("dbs.user");
//			String password =prop.getProperty("dbs.password");
//			Class.forName(prop.getProperty("dbs.driver")).newInstance();

			//						String url = "jdbc:as400://192.168.128.146";
			//						String user = "XUPU15PSS";
			//						String password = "marcosa";
			//						Class.forName("com.ibm.as400.access.AS400JDBCDriver").newInstance();
			//con = DriverManager.getConnection(url, user, password); 
			con = ConectionDbs.getConnecton();
			String SQL  = "SELECT   a.pano20, a.ds18, a.rfdcno, a.orqy"+
			" FROM     "+IConstantAccess.LIB_DBS+".woppart0 a"+
			" WHERE    wono='"+numberOs+"'  ";
			prstmt_ = con.prepareStatement(SQL);
			rs = prstmt_.executeQuery();

			while(rs.next()){
				PecasDbsBean bean = new PecasDbsBean();
				bean.setCodigo(rs.getString("rfdcno"));
				bean.setNomePeca(rs.getString("ds18"));
				bean.setNumPeca(rs.getString("pano20"));
				bean.setQtd(rs.getInt("orqy"));


				pecasList.add(bean);

			}	

		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(con != null){
					con.close();
				}
				if(prstmt_ != null){
					prstmt_.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return pecasList;
	}
	
	public List<PecasDbsBean> findPecasAgendamento(Long idAgendamento){
		EntityManager manager = null;
		List<PecasDbsBean> pecasList = new ArrayList<PecasDbsBean>();
		try{
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("From PmpPecas where idAgendamento.id = :idAgendamento");
			query.setParameter("idAgendamento", idAgendamento);
			List<PmpPecas> pecas = query.getResultList();
			for (PmpPecas pmpPecas : pecas) {
				PecasDbsBean dbsBean = new PecasDbsBean();
				dbsBean.setCodigo(pmpPecas.getCodigo());
				dbsBean.setNomePeca(pmpPecas.getNomePeca());
				dbsBean.setNumPeca(pmpPecas.getNumPeca());
				dbsBean.setQtd(pmpPecas.getQtd().intValue());
				pecasList.add(dbsBean);
			}
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(manager != null && manager.isOpen()){
					manager.close();
				}
			}
			return pecasList;
		}
	public List<StatusAgendamentoBean> findAllStatusAgendamento(){
		EntityManager manager = null;
		List<StatusAgendamentoBean> result = new ArrayList<StatusAgendamentoBean>();
		try{
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("From PmpStatusAgendamento where sigla <> 'OBS'");
			List<PmpStatusAgendamento> list = query.getResultList();
			for (PmpStatusAgendamento pmpStatusAgendamento : list) {
				StatusAgendamentoBean bean = new StatusAgendamentoBean();
				bean.setId(pmpStatusAgendamento.getId().longValue());
				bean.setDescricao(pmpStatusAgendamento.getDescricao());
				bean.setSigla(pmpStatusAgendamento.getSigla());
				result.add(bean);
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	public List<UsuarioBean> remover(Long idAgendamento, List<DataHeaderBean> dataHeaderList){
		EntityManager manager = null;
		try{
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpAgendamento agendamento = manager.find(PmpAgendamento.class, idAgendamento);
			
			if(agendamento.getIsFindTecnico() != null && agendamento.getIsFindTecnico().equals("S")){
				return null;
			}
			if(agendamento.getIdContHorasStandard() != null){
				agendamento.getIdContHorasStandard().setIsExecutado("N");
			}else if(agendamento.getIdContHorasStandardPlus() != null){
				agendamento.getIdContHorasStandardPlus().setIsExecutado("N");
			}
//			PmpContHorasStandard standard = manager.find(PmpContHorasStandard.class, agendamento.getIdContHorasStandard().getId());
//			standard.setIsExecutado("N");
//			manager.merge(standard);
			manager.remove(agendamento);
			manager.getTransaction().commit();
			if(agendamento.getIdContHorasStandard() != null){
				this.saveAgendamentosPendentes(agendamento.getIdContHorasStandard().getIdContrato().getId());
			}else{
				this.saveAgendamentosPendentes(agendamento.getIdContHorasStandardPlus().getIdContrato().getId());
			}
		}catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
		   e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return this.findAllTecnico(dataHeaderList);
	}
	
//	public List<AgendamentoBean> findAllAgendamentosPendentes(Integer inicial, Integer numRegistros, String tipoAgendamento, String campoPesquisa){
//		EntityManager manager = null;
//		List<AgendamentoBean> result = new ArrayList<AgendamentoBean>();
//		try{
//			manager = JpaUtil.getInstance();
////			Query query = manager.createNativeQuery("select C.id,C.Numero_Contrato, C.modelo, C.numero_Serie as serie, " +
////					" ( select op.num_os from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as num_os, " +
////					//" op.num_os,"+
////					" (select horimetro from pmp_maquina_pl pl where pl.numero_serie = C.Numero_Serie and pl.horimetro is not null and pl.id = (select max(id) from pmp_maquina_pl where numero_serie = pl.numero_serie and horimetro is not null and horimetro > 0) ) as horimetro, " +
////					"  (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N') as horas_manutencao," +
////					" ((select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N')  - (select max(horimetro) from pmp_maquina_pl pl where pl.numero_serie = C.Numero_Serie " +
////					"  and pl.horimetro is not null and pl.data_atualizacao = (select max(data_atualizacao) " +
////					"   from pmp_maquina_pl" +
////					" where numero_serie = pl.numero_serie and horimetro is not null and HORIMETRO > 0) )) horas_pendentes, c.codigo_cliente," +
////					" ( select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.Id)  as idContOperacional," +
////					" ( select count(ag.id) from Pmp_Agendamento ag where  ag.id_cont_horas_standard = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N') and id_Contrato = C.id)) statusAgendamento," +
////					" (select convert(varchar(10),max(pl.data_atualizacao),103) from pmp_maquina_pl pl where pl.numero_serie = c.numero_serie)," +
////					" (select hs.standard_job_cptcd from Pmp_Cont_Horas_Standard hs"+
////					" where horas_manutencao = (select  min(horas_manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N')"+
////					" and hs.id_contrato = C.id) standard_job_cptcd, " +
////					" c.filial origem," +
////					" (select t.sigla from pmp_tipo_contrato t where t.id = c.id_tipo_contrato), co.filial destino, c.razao_social, co.local," +
////					" ( select op.num_doc from pmp_os_operacional op where  op.id = (select st.id_os_operacional from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as num_doc," +
////					" ( select op.msg from pmp_os_operacional op where  op.id = (select st.id_os_operacional from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as msg," +
////					" ( select op.COD_ERRO_OS_DBS from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as COD_ERRO_OS_DBS,"+
////					" ( select op.COD_ERRO_DOC_DBS from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as COD_ERRO_DOC_DBS,"+
////					" ( select op.id from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as idOsOperacional," +
////					"  tc.sigla as sigla, convert(varchar(4000), co.obs) as obs"+
////					"  from pmp_contrato c, pmp_config_operacional co, pmp_tipo_contrato tc" +
////					" where c.id_Status_Contrato = (select s.id from Pmp_Status_Contrato s where s.sigla = 'CA') " +
////					" and  c.id in((select  distinct id_Contrato  from Pmp_Cont_Horas_Standard hs where id_Contrato = c.id and is_Executado = 'N')) " +
////					" and co.num_os is not null " +
////					" and co.id_contrato = c.id" +
////					" and c.id_tipo_contrato = tc.id" +
////					//" and op.id_Config_Operacional = co.id" +
////					" and co.filial = "+Integer.valueOf(FILIAL)+
////					//Caso a OS puder ser agendada mais de uma vez comentar essa linha
////					//" and co.id not in (select ag.id_cong_operacional from pmp_agendamento ag)"+
////					" order by horas_pendentes");
//			
//			String SQL = "select C.id,"+//0
//														" C.Numero_Contrato,"+//1 
//														" C.modelo, " +//2
//														" C.numero_Serie as serie,"+//3 
//														" c.codigo_cliente,"+//4				  
//														" c.filial origem,"+//5
//														" tc.sigla,"+//6
//														" co.filial destino,"+//7 
//														" c.razao_social,"+ //8
//														" co.local,"+//9		
//														//" tc.sigla as sigla,"+//10 
//														" convert(varchar(4000), co.obs) as obs," +
////														"  ((select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N')  - (select max(horimetro) from pmp_maquina_pl pl where pl.numero_serie = C.Numero_Serie"+ 
////														"  and pl.horimetro is not null and pl.id = (select max(id)"+ 
////														"   from pmp_maquina_pl"+
////														" where numero_serie = pl.numero_serie and horimetro is not null and HORIMETRO > 0) )) horas_pendentes," +//11
//														" '' horas_pendentes," +//11
//														" c.EMAIL_CONTATO_COMERCIAL, " +//12
//														" tc.sigla siglaTipoContrato,"+//13
//														//"  (select sigla from PMP_TIPO_CONTRATO where ID = c.ID_TIPO_CONTRATO) siglaTipoContrato," +//13
//														//" (select IS_PARTNER from pmp_cont_horas_standard where IS_EXECUTADO = 'N' and ID_CONTRATO = c.id"+
//														//"	and HORAS_MANUTENCAO = (select MIN(HORAS_MANUTENCAO) from pmp_cont_horas_standard where IS_EXECUTADO = 'N' and ID_CONTRATO = c.id)) isPartner, "+//14
//														" '' isPartner," +//15
//														" cla.sigla siglaClassificacaoContrato," +//15
//														"  c.POSSUI_HORAS_MES, "+//16
//														"  c.MEDIA_HORAS_MES, "+//17
//														"  c.TELEFONE_COMERCIAL, "+//18
//														"  c.TELEFONE_SERVICOS, "+//19
//														"  c.CONTATO_SERVICOS, "+//20
//														"  c.EMAIL_CONTATO_SERVICOS, "+//21
//														"  (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = c.id and is_Executado = 'N') horas_manutencao, "+//22
//														"  co.CONTATO as CONTATO_OPERACIONAL, "+//23
//														"  co.TELEFONE_CONTATO AS TELEFONE_OPERACIONAL, "+//24
//														"  c.id AS ID_CONTRATO "+//25
//														" from pmp_contrato c, pmp_config_operacional co, pmp_tipo_contrato tc, pmp_classificacao_contrato cla"+
//														" where c.id_Status_Contrato = (select s.id from Pmp_Status_Contrato s where s.sigla = 'CA')"+ 
//														" and  c.id in((select  distinct id_Contrato  from Pmp_Cont_Horas_Standard hs where id_Contrato = c.id and is_Executado = 'N'))"+ 
//														" and co.num_os is not null "+
//														" and co.id_contrato = c.id"+
//														" and cla.id = c.id_classificacao_contrato "+
//														" and c.id_tipo_contrato = tc.id"+
//														" and co.filial = "+Integer.valueOf(FILIAL);
//			if(tipoAgendamento.equals("TA")){
//				SQL += " and  (select count(ag.id) from Pmp_Agendamento ag where ag.ID_STATUS_AGENDAMENTO in (select ID from PMP_STATUS_AGENDAMENTO where SIGLA in ('EA','AT')) and  ag.id_cont_horas_standard = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = c.id and is_Executado = 'N') and id_Contrato = c.id)) > 0";
//			}
//			if(tipoAgendamento.equals("TNA")){
//				SQL += " and  (select count(ag.id) from Pmp_Agendamento ag where  ag.id_cont_horas_standard = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = c.id and is_Executado = 'N') and id_Contrato = c.id)) = 0";
//			}
//			if(campoPesquisa.trim().length() > 0){
//				SQL += " and (C.numero_Serie like '%"+campoPesquisa.toUpperCase()+"%' or C.modelo like '%"+campoPesquisa.toUpperCase()+"%' or C.Numero_Contrato like '%"+campoPesquisa.toUpperCase()+"%' or C.numero_Serie like '%"+campoPesquisa.toUpperCase()+"%' or c.razao_social like '%"+campoPesquisa.toUpperCase()+"%')";
//			}
//
//			SQL += " order by horas_pendentes";
//			Query query = manager.createNativeQuery(SQL);
//			Integer tamanhoTotalLista = query.getResultList().size();
//			query.setFirstResult(inicial);
//			query.setMaxResults(numRegistros);
//
//			
//			List<Object[]> resultQuery = query.getResultList();			
//			for (Object[] objects : resultQuery) {
//				Long idContrato = ((BigDecimal)objects[25]).longValue();
//				PmpContrato contrato = manager.find(PmpContrato.class, idContrato);
//				//query = manager.createNativeQuery("select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = "+((BigDecimal)objects[0]).longValue()+" and is_Executado = 'N'");
//				query = manager.createNativeQuery("select horimetro from pmp_agendamento ag, os_palm palm where ag.id_cont_horas_standard = ("+
//						" select max(id) from pmp_cont_horas_standard where id_contrato = "+((BigDecimal)objects[0]).longValue()+" and is_executado = 'S' )"+
//				" and palm.id_agendamento = ag.id");
//				Long horimetroUltimaRevisao = 0L;
//				if(query.getResultList().size() > 0){
//					horimetroUltimaRevisao = ((BigDecimal)query.getSingleResult()).longValue();
//				}
//				BigDecimal horasManutencao = (BigDecimal)objects[22]; 
//				
//				
//				query = manager.createNativeQuery("select st.id, st.standard_job_cptcd, st.IS_PARTNER, frequencia from pmp_cont_horas_standard st where st.horas_manutencao = "+horasManutencao+" and id_Contrato = "+((BigDecimal)objects[0]).longValue());
//				BigDecimal idContHorasStandard = BigDecimal.ZERO;
//				String standerJob = null;
//				String isPartner = null;
//				Long horimetroProximaRevisao = 0L;
//				if(query.getResultList().size() > 0){
//					Object[] pair = (Object[])query.getSingleResult();
//					idContHorasStandard = (BigDecimal)pair[0];
//					standerJob = (String)pair[1];
//					isPartner = (String)pair[2];
//					horimetroProximaRevisao = horimetroUltimaRevisao + ((BigDecimal)pair[3]).longValue();
//					
//				}
//				
//				
//				
////				query = manager.createNativeQuery("select op.num_os from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = "+idContHorasStandard);
////				String numeroOs = null;
////				if(query.getResultList().size() > 0){
////					numeroOs = (String)query.getSingleResult();
////				}
//				if(horimetroUltimaRevisao == 0){
//					horimetroProximaRevisao = horasManutencao.longValue();
//				}
//				Long horimetroFaltante = 0L;
//				query = manager.createNativeQuery("select distinct horimetro from pmp_maquina_pl pl where pl.numero_serie = '"+(String)objects[3]+"'  and pl.horimetro is not null and pl.horimetro = (select max(horimetro) from pmp_maquina_pl where numero_serie = pl.numero_serie and horimetro is not null and horimetro > 0)");
//				BigDecimal horimetro = BigDecimal.ZERO;
//				if(query.getResultList().size() > 0){
//					horimetro = (BigDecimal)query.getSingleResult();
//					horimetroFaltante = horimetroProximaRevisao - horimetro.longValue();
//				}
//				if(horimetroUltimaRevisao == 0){
//					if(contrato.getIdClassificacaoContrato().equals("CUS")
//							|| contrato.getIdClassificacaoContrato().equals("PART")
//							|| contrato.getIdClassificacaoContrato().equals("CUSLIGHT"))
//						horimetroUltimaRevisao = contrato.getHorimetroUltimaRevisao();
//				}else{
//					horimetroUltimaRevisao = horimetro.longValue();
//				}
//				query = manager.createNativeQuery(" select CONVERT(varchar(10), ag.data_agendamento, 103) from Pmp_Agendamento ag where ag.ID_STATUS_AGENDAMENTO in (select ID from PMP_STATUS_AGENDAMENTO where SIGLA in ('EA','AT')) and  ag.id_cont_horas_standard = "+idContHorasStandard);
//				String dataAgendamento = "";
//				if(query.getResultList().size() > 0){
//					List<String> strings = (List<String>)query.getResultList();
//					for (String data : strings) {
//						dataAgendamento += data+" ";
//					}
//					
//				}
//
//				//query = manager.createNativeQuery("select convert(varchar(10),max(pl.data_atualizacao),103) from pmp_maquina_pl pl where pl.numero_serie = '"+(String)objects[3]+"' and pl.horimetro is not null and pl.horimetro > 0");
////				String dataAtualizacaoHorimentro = null;
////				if(query.getResultList().size() > 0){
////					dataAtualizacaoHorimentro = (String)query.getSingleResult();
////				}
//
////				query = manager.createNativeQuery("select hs.standard_job_cptcd from Pmp_Cont_Horas_Standard hs  where horas_manutencao = "+horasManutencao+" and hs.id_contrato = "+((BigDecimal)objects[0]).longValue());
////				String standerJob = null;
////				if(query.getResultList().size() > 0){
////					standerJob = (String)query.getSingleResult();
////				}
//				query = manager.createNativeQuery("select op.num_doc, op.msg, op.COD_ERRO_OS_DBS, op.COD_ERRO_DOC_DBS, op.id, op.num_os  from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = "+idContHorasStandard);
//				String numDoc = null;
//				String msg = null;
//				String codErroOsDbs = null;
//				String codErroDocDbs = null;
//				BigDecimal idOsOperacional = null;
//				String numeroOs = null;
//				if(query.getResultList().size() > 0){
//					Object[] pair = (Object[])query.getSingleResult();
//					numDoc = (String)pair[0];
//					msg = (String)pair[1];
//					codErroOsDbs = (String)pair[2];
//					codErroDocDbs = (String)pair[3];
//					idOsOperacional = (BigDecimal)pair[4];
//					numeroOs = (String)pair[5];
//				}
////				if("H305322".equals(numeroOs))
////				{
////					System.out.println("entrei");
////				}
////				query = manager.createNativeQuery("select op.msg from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = "+horasManutencao+" and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
////				String msg = null;
////				if(query.getResultList().size() > 0){
////					msg = (String)query.getSingleResult();
////				}
////				query = manager.createNativeQuery("select op.COD_ERRO_OS_DBS from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = "+horasManutencao+" and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
////				String codErroOsDbs = null;
////				if(query.getResultList().size() > 0){
////					codErroOsDbs = (String)query.getSingleResult();
////				}
////				query = manager.createNativeQuery("select op.COD_ERRO_DOC_DBS from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = "+horasManutencao+" and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
////				String codErroDocDbs = null;
////				if(query.getResultList().size() > 0){
////					codErroDocDbs = (String)query.getSingleResult();
////				}
////				query = manager.createNativeQuery("select id from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = "+horasManutencao+" and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
////				BigDecimal idOsOperacional = null;
////				if(query.getResultList().size() > 0){
////					idOsOperacional = (BigDecimal)query.getSingleResult();
////				}
//				
//				query = manager.createNativeQuery("select so.DESCRICAO from Pmp_Cont_Horas_Standard hs, PMP_STATUS_OS so  where hs.id = "+idContHorasStandard+" and so.ID = hs.ID_STATUS_OS ");
//				String financeiro = "";
//				if(query.getResultList().size() > 0){
//					financeiro = (String)query.getSingleResult();
//				}
//				
//				
//				AgendamentoBean bean = new AgendamentoBean();
//				bean.setHorimetroUltimaRevisao(horimetroUltimaRevisao.toString());
//				bean.setHorimetroProximaRevisao(horimetroProximaRevisao.toString());
//				bean.setHorimetroFaltantes(horimetroFaltante.toString());
//				bean.setIdContrato(((BigDecimal)objects[0]).longValue());
//				bean.setNumContrato((String)objects[1]);
//				bean.setModelo((String)objects[2]);
//				bean.setNumSerie((String)objects[3]);
//				bean.setNumOs(numeroOs);
//				if(horimetro != null){
//					bean.setHorimetro(horimetro.longValue());
//				}
//				try {
//					//bean.setHorasRevisao(((BigDecimal)objects[6]).longValue());
//					bean.setHorasRevisao(horasManutencao.longValue());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				try {
//					if(horasManutencao != null && horimetro != null){
//						bean.setHorasPendentes(horasManutencao.longValue() - horimetro.longValue());
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				bean.setCodigoCliente((String)objects[4]);
//				bean.setIdContHorasStandard(idContHorasStandard.longValue());
//				if(financeiro.length() == 0){
//					bean.setStatusAgendamento((dataAgendamento.equals(""))?"Não Agendado":dataAgendamento);
//				}else{
//					bean.setStatusAgendamento((dataAgendamento.equals(""))?"Não Agendado / "+financeiro:dataAgendamento+" / "+financeiro);
//
//				}
//				//bean.setDataAtualizacaoHorimetro(dataAtualizacaoHorimentro);
//				bean.setStandardJob(standerJob);
//				bean.setFilial(((BigDecimal)objects[5]).toString());
//				bean.setSiglaTipoContrato((String)objects[6]);
//				if(((String)objects[13]).equals("CAN") || ((String)objects[13]).equals("VPG")){
//					bean.setFilialDestino(((BigDecimal)objects[5]).toString());
//				}else{
//					bean.setFilialDestino(((BigDecimal)objects[7]).toString());
//				}
//				bean.setRazaoSocial((String)objects[8]);
//				bean.setLocal((String)objects[9]);
//				bean.setNumDoc(numDoc);
//				bean.setMsg(msg);
//				bean.setCodErroOsDbs(codErroOsDbs);
//				bean.setCodErroDocDbs(codErroDocDbs);
//				if(idOsOperacional != null){
//					bean.setIdOsOperacional(idOsOperacional.longValue());
//				}
//				//bean.setSiglaTipoContrato((String)objects[23]);
//				bean.setTotalRegistros(tamanhoTotalLista);
//				bean.setObsOs("Contato Operacional:"+(String)objects[23]+"\nTelefone Operacional:"+(String)objects[24]+"\nContato:"+(String)objects[20]+"\nTelefone Comercial:"+(String)objects[18]+"\nTelefone de serviços:"+(String)objects[19]+"\nEmail:"+(String)objects[21]+"\nLocalização:"+(String)objects[9]+"\n\nOBS OS:"+(String)objects[10]);
//				bean.setEmailContato((String)objects[12]);
//				bean.setIsPartner(isPartner);
//				bean.setSiglaClassificacaoContrato((String)objects[15]);
////				try {
////					if(objects[16] != null && ((String)objects[16]).equals("S")){
////						if(horasManutencao != null && horimetro != null){
////							bean.setHorasPendentes(horasManutencao.longValue() - horimetro.longValue());
////							Double mediaDiasProximaRevisao = ((horasManutencao.doubleValue() - horimetro.doubleValue())/((BigDecimal)objects[17]).doubleValue())*30;
////							query = manager.createNativeQuery(" select DATEDIFF ( day , max(palm.EMISSAO), GETDATE())   from Pmp_Agendamento ag, pmp_os_palm palm where  ag.id_cont_horas_standard in (select st.id from pmp_cont_horas_standard st where id_Contrato = "+((BigDecimal)objects[0]).longValue()+") and palm.ID_AGENDAMENTO = ag.ID");
////							if(query.getResultList().size() > 0 && query.getResultList().get(0) != null){
////								Integer dias = (Integer)query.getSingleResult();
////								mediaDiasProximaRevisao = mediaDiasProximaRevisao - dias;
////							}else{
////								query = manager.createNativeQuery(" select DATEDIFF ( day , max(c.data_aceite), GETDATE())   from Pmp_contrato  c where  c.id = "+((BigDecimal)objects[0]).longValue());
////								Integer dias = (Integer)query.getSingleResult();
////								mediaDiasProximaRevisao = mediaDiasProximaRevisao -dias;
////							}
////							Long diasProximaRevisao = Math.round(mediaDiasProximaRevisao);
////							bean.setMediaDiasProximaRevisao(diasProximaRevisao.toString());
////						}
////					}
////				} catch (Exception e) {
////					e.printStackTrace();
////				}
//				
//				result.add(bean);
//			}
//		}catch (Exception e) {
//		   e.printStackTrace();
//		} finally {
//			if(manager != null && manager.isOpen()){
//				manager.close();
//			}
//		}
//		return result;
//}
	public List<AgendamentoBean> findAllAgendamentosPendentes(Integer inicial, Integer numRegistros, String tipoAgendamento, String campoPesquisa){
		EntityManager manager = null;
		List<AgendamentoBean> result = new ArrayList<AgendamentoBean>();
		try{
			manager = JpaUtil.getInstance();
//			Query query = manager.createNativeQuery("select C.id,C.Numero_Contrato, C.modelo, C.numero_Serie as serie, " +
//					" ( select op.num_os from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as num_os, " +
//					//" op.num_os,"+
//					" (select horimetro from pmp_maquina_pl pl where pl.numero_serie = C.Numero_Serie and pl.horimetro is not null and pl.id = (select max(id) from pmp_maquina_pl where numero_serie = pl.numero_serie and horimetro is not null and horimetro > 0) ) as horimetro, " +
//					"  (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N') as horas_manutencao," +
//					" ((select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N')  - (select max(horimetro) from pmp_maquina_pl pl where pl.numero_serie = C.Numero_Serie " +
//					"  and pl.horimetro is not null and pl.data_atualizacao = (select max(data_atualizacao) " +
//					"   from pmp_maquina_pl" +
//					" where numero_serie = pl.numero_serie and horimetro is not null and HORIMETRO > 0) )) horas_pendentes, c.codigo_cliente," +
//					" ( select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.Id)  as idContOperacional," +
//					" ( select count(ag.id) from Pmp_Agendamento ag where  ag.id_cont_horas_standard = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N') and id_Contrato = C.id)) statusAgendamento," +
//					" (select convert(varchar(10),max(pl.data_atualizacao),103) from pmp_maquina_pl pl where pl.numero_serie = c.numero_serie)," +
//					" (select hs.standard_job_cptcd from Pmp_Cont_Horas_Standard hs"+
//					" where horas_manutencao = (select  min(horas_manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N')"+
//					" and hs.id_contrato = C.id) standard_job_cptcd, " +
//					" c.filial origem," +
//					" (select t.sigla from pmp_tipo_contrato t where t.id = c.id_tipo_contrato), co.filial destino, c.razao_social, co.local," +
//					" ( select op.num_doc from pmp_os_operacional op where  op.id = (select st.id_os_operacional from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as num_doc," +
//					" ( select op.msg from pmp_os_operacional op where  op.id = (select st.id_os_operacional from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as msg," +
//					" ( select op.COD_ERRO_OS_DBS from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as COD_ERRO_OS_DBS,"+
//					" ( select op.COD_ERRO_DOC_DBS from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as COD_ERRO_DOC_DBS,"+
//					" ( select op.id from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as idOsOperacional," +
//					"  tc.sigla as sigla, convert(varchar(4000), co.obs) as obs"+
//					"  from pmp_contrato c, pmp_config_operacional co, pmp_tipo_contrato tc" +
//					" where c.id_Status_Contrato = (select s.id from Pmp_Status_Contrato s where s.sigla = 'CA') " +
//					" and  c.id in((select  distinct id_Contrato  from Pmp_Cont_Horas_Standard hs where id_Contrato = c.id and is_Executado = 'N')) " +
//					" and co.num_os is not null " +
//					" and co.id_contrato = c.id" +
//					" and c.id_tipo_contrato = tc.id" +
//					//" and op.id_Config_Operacional = co.id" +
//					" and co.filial = "+Integer.valueOf(FILIAL)+
//					//Caso a OS puder ser agendada mais de uma vez comentar essa linha
//					//" and co.id not in (select ag.id_cong_operacional from pmp_agendamento ag)"+
//					" order by horas_pendentes");
			
			String SQL = "select C.id,"+//0
														" C.Numero_Contrato,"+//1 
														" C.modelo, " +//2
														" C.numero_Serie as serie,"+//3 
														" c.codigo_cliente,"+//4				  
														" c.filial origem,"+//5
														" '' vazio,"+//6
														" cp.filial destino,"+//7 
														" c.razao_social,"+ //8
														" cp.local,"+//9		
														//" tc.sigla as sigla,"+//10 
														" '' as obs," +
//														"  ((select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N')  - (select max(horimetro) from pmp_maquina_pl pl where pl.numero_serie = C.Numero_Serie"+ 
//														"  and pl.horimetro is not null and pl.id = (select max(id)"+ 
//														"   from pmp_maquina_pl"+
//														" where numero_serie = pl.numero_serie and horimetro is not null and HORIMETRO > 0) )) horas_pendentes," +//11
														" '' horas_pendentes," +//11
														" c.EMAIL_CONTATO_COMERCIAL, " +//12
														" cp.sigla_Tipo_Contrato,"+//13
														//"  (select sigla from PMP_TIPO_CONTRATO where ID = c.ID_TIPO_CONTRATO) siglaTipoContrato," +//13
														//" (select IS_PARTNER from pmp_cont_horas_standard where IS_EXECUTADO = 'N' and ID_CONTRATO = c.id"+
														//"	and HORAS_MANUTENCAO = (select MIN(HORAS_MANUTENCAO) from pmp_cont_horas_standard where IS_EXECUTADO = 'N' and ID_CONTRATO = c.id)) isPartner, "+//14
														" '' isPartner," +//15
														" cp.sigla_Classificacao_Contrato," +//15
														"  c.POSSUI_HORAS_MES, "+//16
														"  c.MEDIA_HORAS_MES, "+//17
														"  c.TELEFONE_COMERCIAL, "+//18
														"  c.TELEFONE_SERVICOS, "+//19
														"  c.CONTATO_SERVICOS, "+//20
														"  c.EMAIL_CONTATO_SERVICOS, "+//21
														"  (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = c.id and is_Executado = 'N') horas_manutencao, "+//22
														"  cp.CONTATO as CONTATO_OPERACIONAL, "+//23
														"  cp.TELEFONE_CONTATO AS TELEFONE_OPERACIONAL, "+//24
														"  cp.HORIMETRO_ULTIMA_REVISAO, " +//25
														"  cp.HORIMETRO_PROXIMA_REVISAO, "+//26
														"  c.HORIMETRO_ULTIMA_REVISAO,"+//27
														"  cp.horimetro,"+//28
														"  cp.HORIMETRO_FALTANTE,"+//29
														"  convert(varchar(4000),cp.OBS) as OBS_OS"+//30
											
														" from pmp_contrato c, PMP_AGENDAMENTO_PENDENTE cp"+
														" where c.id_Status_Contrato = (select s.id from Pmp_Status_Contrato s where s.sigla = 'CA')"+ 
														" and  c.id in((select  distinct id_Contrato  from Pmp_Cont_Horas_Standard hs where id_Contrato = c.id and is_Executado = 'N'))"+ 
														" and c.id = cp.id_contrato "+
														" and cp.filial_destino = "+Integer.valueOf(FILIAL);
			if(tipoAgendamento.equals("TA")){
				SQL += " and  (select count(ag.id) from Pmp_Agendamento ag where ag.ID_STATUS_AGENDAMENTO in (select ID from PMP_STATUS_AGENDAMENTO where SIGLA in ('EA','AT')) and  ag.id_cont_horas_standard = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = c.id and is_Executado = 'N') and id_Contrato = c.id)) > 0";
			}
			if(tipoAgendamento.equals("TNA")){
				SQL += " and  (select count(ag.id) from Pmp_Agendamento ag where  ag.id_cont_horas_standard = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = c.id and is_Executado = 'N') and id_Contrato = c.id)) = 0";
			}
			if(campoPesquisa.trim().length() > 0){
				SQL += " and (C.numero_Serie like '%"+campoPesquisa.toUpperCase()+"%' or C.modelo like '%"+campoPesquisa.toUpperCase()+"%' or C.Numero_Contrato like '%"+campoPesquisa.toUpperCase()+"%' or C.numero_Serie like '%"+campoPesquisa.toUpperCase()+"%' or c.razao_social like '%"+campoPesquisa.toUpperCase()+"%')";
			}

			SQL += " order by horas_pendentes";
			Query query = manager.createNativeQuery(SQL);
			Integer tamanhoTotalLista = query.getResultList().size();
			query.setFirstResult(inicial);
			query.setMaxResults(numRegistros);

			
			List<Object[]> resultQuery = query.getResultList();			
			for (Object[] objects : resultQuery) {
				Long idContrato = ((BigDecimal)objects[0]).longValue();
				//PmpContrato contrato = manager.find(PmpContrato.class, idContrato);
				//query = manager.createNativeQuery("select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = "+((BigDecimal)objects[0]).longValue()+" and is_Executado = 'N'");
//				query = manager.createNativeQuery("select horimetro from pmp_agendamento ag, os_palm palm where ag.id_cont_horas_standard = ("+
//						" select max(id) from pmp_cont_horas_standard where id_contrato = "+((BigDecimal)objects[0]).longValue()+" and is_executado = 'S' )"+
//				" and palm.id_agendamento = ag.id");
//				if(query.getResultList().size() > 0){
//					horimetroUltimaRevisao = ((BigDecimal)query.getSingleResult()).longValue();
//				}
				
				Long horimetroUltimaRevisao = 0L;
				if(objects[25] != null ){
					horimetroUltimaRevisao = new Long((String)objects[25]);
				}
				
				BigDecimal horasManutencao = (BigDecimal)objects[22]; 
				
				
				query = manager.createNativeQuery("select st.id, st.standard_job_cptcd, st.IS_PARTNER, frequencia, op.num_doc, op.msg, op.COD_ERRO_OS_DBS, op.COD_ERRO_DOC_DBS, op.id, op.num_os " +
						" from pmp_os_operacional op right join pmp_cont_horas_standard st   on  op.ID_CONT_HORAS_STANDARD = st.id and op.ID_CONT_HORAS_STANDARD = st.id " +
						" where st.horas_manutencao = "+horasManutencao+" and id_Contrato = "+((BigDecimal)objects[0]).longValue());
				BigDecimal idContHorasStandard = BigDecimal.ZERO;
				String standerJob = null;
				String isPartner = null;
				Long horimetroProximaRevisao = 0L;
				
				String numDoc = null;
				String msg = null;
				String codErroOsDbs = null;
				String codErroDocDbs = null;
				BigDecimal idOsOperacional = null;
				String numeroOs = null;
				
				if(query.getResultList().size() > 0){
					Object[] pair = (Object[])query.getSingleResult();
					idContHorasStandard = (BigDecimal)pair[0];
					standerJob = (String)pair[1];
					isPartner = (String)pair[2];
					horimetroProximaRevisao = horimetroUltimaRevisao + ((BigDecimal)pair[3]).longValue();
					
					
					numDoc = (String)pair[4];
					msg = (String)pair[5];
					codErroOsDbs = (String)pair[6];
					codErroDocDbs = (String)pair[7];
					idOsOperacional = (BigDecimal)pair[8];
					numeroOs = (String)pair[9];
					
				}
				
				
				
//				query = manager.createNativeQuery("select op.num_os from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = "+idContHorasStandard);
//				String numeroOs = null;
//				if(query.getResultList().size() > 0){
//					numeroOs = (String)query.getSingleResult();
//				}
				Long horimetroFaltante = new Long((String)objects[29]);
				//query = manager.createNativeQuery("select distinct horimetro from pmp_maquina_pl pl where pl.numero_serie = '"+(String)objects[3]+"'  and pl.horimetro is not null and pl.horimetro = (select max(horimetro) from pmp_maquina_pl where numero_serie = pl.numero_serie and horimetro is not null and horimetro > 0)");
				BigDecimal horimetro = (BigDecimal)objects[28];
//				if(query.getResultList().size() > 0){
//					horimetro = (BigDecimal)query.getSingleResult();
//					horimetroFaltante = horimetroProximaRevisao - ((BigDecimal)objects[28]).longValue();
//				}
				if(horimetroUltimaRevisao == 0){
					horimetroProximaRevisao = horasManutencao.longValue();
					horimetroUltimaRevisao = horimetro.longValue();
				}
				
				
				if(((String)objects[15]).equals("CUS")
						|| ((String)objects[15]).equals("PART")
						|| ((String)objects[15]).equals("CUSLIGHT")){
					if(objects[27] != null){
						horimetroUltimaRevisao = ((BigDecimal)objects[27]).longValue();
					}
				}
				query = manager.createNativeQuery(" select CONVERT(varchar(10), ag.data_agendamento, 103) from Pmp_Agendamento ag where ag.ID_STATUS_AGENDAMENTO in (select ID from PMP_STATUS_AGENDAMENTO where SIGLA in ('EA','AT')) and  ag.id_cont_horas_standard = "+idContHorasStandard);
				String dataAgendamento = "";
				if(query.getResultList().size() > 0){
					List<String> strings = (List<String>)query.getResultList();
					for (String data : strings) {
						dataAgendamento += data+" ";
					}
					
				}

				//query = manager.createNativeQuery("select convert(varchar(10),max(pl.data_atualizacao),103) from pmp_maquina_pl pl where pl.numero_serie = '"+(String)objects[3]+"' and pl.horimetro is not null and pl.horimetro > 0");
//				String dataAtualizacaoHorimentro = null;
//				if(query.getResultList().size() > 0){
//					dataAtualizacaoHorimentro = (String)query.getSingleResult();
//				}

//				query = manager.createNativeQuery("select hs.standard_job_cptcd from Pmp_Cont_Horas_Standard hs  where horas_manutencao = "+horasManutencao+" and hs.id_contrato = "+((BigDecimal)objects[0]).longValue());
//				String standerJob = null;
//				if(query.getResultList().size() > 0){
//					standerJob = (String)query.getSingleResult();
//				}
//				query = manager.createNativeQuery("select op.num_doc, op.msg, op.COD_ERRO_OS_DBS, op.COD_ERRO_DOC_DBS, op.id, op.num_os  from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = "+idContHorasStandard);
//				String numDoc = null;
//				String msg = null;
//				String codErroOsDbs = null;
//				String codErroDocDbs = null;
//				BigDecimal idOsOperacional = null;
//				String numeroOs = null;
//				if(query.getResultList().size() > 0){
//					Object[] pair = (Object[])query.getSingleResult();
//					numDoc = (String)pair[0];
//					msg = (String)pair[1];
//					codErroOsDbs = (String)pair[2];
//					codErroDocDbs = (String)pair[3];
//					idOsOperacional = (BigDecimal)pair[4];
//					numeroOs = (String)pair[5];
//				}
//				if("H305322".equals(numeroOs))
//				{
//					System.out.println("entrei");
//				}
//				query = manager.createNativeQuery("select op.msg from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = "+horasManutencao+" and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
//				String msg = null;
//				if(query.getResultList().size() > 0){
//					msg = (String)query.getSingleResult();
//				}
//				query = manager.createNativeQuery("select op.COD_ERRO_OS_DBS from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = "+horasManutencao+" and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
//				String codErroOsDbs = null;
//				if(query.getResultList().size() > 0){
//					codErroOsDbs = (String)query.getSingleResult();
//				}
//				query = manager.createNativeQuery("select op.COD_ERRO_DOC_DBS from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = "+horasManutencao+" and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
//				String codErroDocDbs = null;
//				if(query.getResultList().size() > 0){
//					codErroDocDbs = (String)query.getSingleResult();
//				}
//				query = manager.createNativeQuery("select id from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = "+horasManutencao+" and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
//				BigDecimal idOsOperacional = null;
//				if(query.getResultList().size() > 0){
//					idOsOperacional = (BigDecimal)query.getSingleResult();
//				}
				
				query = manager.createNativeQuery("select so.DESCRICAO from Pmp_Cont_Horas_Standard hs, PMP_STATUS_OS so  where hs.id = "+idContHorasStandard+" and so.ID = hs.ID_STATUS_OS ");
				String financeiro = "";
				if(query.getResultList().size() > 0){
					financeiro = (String)query.getSingleResult();
				}
				
				
				AgendamentoBean bean = new AgendamentoBean();
				bean.setHorimetroUltimaRevisao(horimetroUltimaRevisao.toString());
				bean.setHorimetroProximaRevisao(horimetroProximaRevisao.toString());
				bean.setHorimetroFaltantes(horimetroFaltante.toString());
				bean.setIdContrato(((BigDecimal)objects[0]).longValue());
				bean.setNumContrato((String)objects[1]);
				bean.setModelo((String)objects[2]);
				bean.setNumSerie((String)objects[3]);
				bean.setNumOs(numeroOs);
				if(horimetro != null){
					bean.setHorimetro(horimetro.longValue());
				}
				try {
					//bean.setHorasRevisao(((BigDecimal)objects[6]).longValue());
					bean.setHorasRevisao(horasManutencao.longValue());
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if(horasManutencao != null && horimetro != null){
						bean.setHorasPendentes(horasManutencao.longValue() - horimetro.longValue());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				bean.setCodigoCliente((String)objects[4]);
				bean.setIdContHorasStandard(idContHorasStandard.longValue());
				if(financeiro.length() == 0){
					bean.setStatusAgendamento((dataAgendamento.equals(""))?"Não Agendado":dataAgendamento);
				}else{
					bean.setStatusAgendamento((dataAgendamento.equals(""))?"Não Agendado / "+financeiro:dataAgendamento+" / "+financeiro);

				}
				//bean.setDataAtualizacaoHorimetro(dataAtualizacaoHorimentro);
				bean.setStandardJob(standerJob);
				bean.setFilial(((BigDecimal)objects[5]).toString());
				bean.setSiglaTipoContrato((String)objects[13]);
				if(((String)objects[13]).equals("CAN") || ((String)objects[13]).equals("VPG")){
					bean.setFilialDestino(((BigDecimal)objects[5]).toString());
				}else{
					bean.setFilialDestino(((BigDecimal)objects[7]).toString());
				}
				bean.setRazaoSocial((String)objects[8]);
				bean.setLocal((String)objects[9]);
				bean.setNumDoc(numDoc);
				bean.setMsg(msg);
				bean.setCodErroOsDbs(codErroOsDbs);
				bean.setCodErroDocDbs(codErroDocDbs);
				if(idOsOperacional != null){
					bean.setIdOsOperacional(idOsOperacional.longValue());
				}
				//bean.setSiglaTipoContrato((String)objects[23]);
				bean.setTotalRegistros(tamanhoTotalLista);
				bean.setObsOs((String)objects[30]);
				bean.setEmailContato((String)objects[12]);
				bean.setIsPartner(isPartner);
				bean.setSiglaClassificacaoContrato((String)objects[15]);
//				try {
//					if(objects[16] != null && ((String)objects[16]).equals("S")){
//						if(horasManutencao != null && horimetro != null){
//							bean.setHorasPendentes(horasManutencao.longValue() - horimetro.longValue());
//							Double mediaDiasProximaRevisao = ((horasManutencao.doubleValue() - horimetro.doubleValue())/((BigDecimal)objects[17]).doubleValue())*30;
//							query = manager.createNativeQuery(" select DATEDIFF ( day , max(palm.EMISSAO), GETDATE())   from Pmp_Agendamento ag, pmp_os_palm palm where  ag.id_cont_horas_standard in (select st.id from pmp_cont_horas_standard st where id_Contrato = "+((BigDecimal)objects[0]).longValue()+") and palm.ID_AGENDAMENTO = ag.ID");
//							if(query.getResultList().size() > 0 && query.getResultList().get(0) != null){
//								Integer dias = (Integer)query.getSingleResult();
//								mediaDiasProximaRevisao = mediaDiasProximaRevisao - dias;
//							}else{
//								query = manager.createNativeQuery(" select DATEDIFF ( day , max(c.data_aceite), GETDATE())   from Pmp_contrato  c where  c.id = "+((BigDecimal)objects[0]).longValue());
//								Integer dias = (Integer)query.getSingleResult();
//								mediaDiasProximaRevisao = mediaDiasProximaRevisao -dias;
//							}
//							Long diasProximaRevisao = Math.round(mediaDiasProximaRevisao);
//							bean.setMediaDiasProximaRevisao(diasProximaRevisao.toString());
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				
				result.add(bean);
			}
		}catch (Exception e) {
		   e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
		
		public void saveAgendamentosPendentes(Long idContrato){
			EntityManager manager = null;
			//List<AgendamentoBean> result = new ArrayList<AgendamentoBean>();
			try{
				manager = JpaUtil.getInstance();
//				Query query = manager.createNativeQuery("select C.id,C.Numero_Contrato, C.modelo, C.numero_Serie as serie, " +
//						" ( select op.num_os from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as num_os, " +
//						//" op.num_os,"+
//						" (select horimetro from pmp_maquina_pl pl where pl.numero_serie = C.Numero_Serie and pl.horimetro is not null and pl.id = (select max(id) from pmp_maquina_pl where numero_serie = pl.numero_serie and horimetro is not null and horimetro > 0) ) as horimetro, " +
//						"  (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N') as horas_manutencao," +
//						" ((select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N')  - (select max(horimetro) from pmp_maquina_pl pl where pl.numero_serie = C.Numero_Serie " +
//						"  and pl.horimetro is not null and pl.data_atualizacao = (select max(data_atualizacao) " +
//						"   from pmp_maquina_pl" +
//						" where numero_serie = pl.numero_serie and horimetro is not null and HORIMETRO > 0) )) horas_pendentes, c.codigo_cliente," +
//						" ( select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.Id)  as idContOperacional," +
//						" ( select count(ag.id) from Pmp_Agendamento ag where  ag.id_cont_horas_standard = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N') and id_Contrato = C.id)) statusAgendamento," +
//						" (select convert(varchar(10),max(pl.data_atualizacao),103) from pmp_maquina_pl pl where pl.numero_serie = c.numero_serie)," +
//						" (select hs.standard_job_cptcd from Pmp_Cont_Horas_Standard hs"+
//						" where horas_manutencao = (select  min(horas_manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N')"+
//						" and hs.id_contrato = C.id) standard_job_cptcd, " +
//						" c.filial origem," +
//						" (select t.sigla from pmp_tipo_contrato t where t.id = c.id_tipo_contrato), co.filial destino, c.razao_social, co.local," +
//						" ( select op.num_doc from pmp_os_operacional op where  op.id = (select st.id_os_operacional from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as num_doc," +
//						" ( select op.msg from pmp_os_operacional op where  op.id = (select st.id_os_operacional from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as msg," +
//						" ( select op.COD_ERRO_OS_DBS from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as COD_ERRO_OS_DBS,"+
//						" ( select op.COD_ERRO_DOC_DBS from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as COD_ERRO_DOC_DBS,"+
//						" ( select op.id from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as idOsOperacional," +
//						"  tc.sigla as sigla, convert(varchar(4000), co.obs) as obs"+
//						"  from pmp_contrato c, pmp_config_operacional co, pmp_tipo_contrato tc" +
//						" where c.id_Status_Contrato = (select s.id from Pmp_Status_Contrato s where s.sigla = 'CA') " +
//						" and  c.id in((select  distinct id_Contrato  from Pmp_Cont_Horas_Standard hs where id_Contrato = c.id and is_Executado = 'N')) " +
//						" and co.num_os is not null " +
//						" and co.id_contrato = c.id" +
//						" and c.id_tipo_contrato = tc.id" +
//						//" and op.id_Config_Operacional = co.id" +
//						" and co.filial = "+Integer.valueOf(FILIAL)+
//						//Caso a OS puder ser agendada mais de uma vez comentar essa linha
//						//" and co.id not in (select ag.id_cong_operacional from pmp_agendamento ag)"+
//						" order by horas_pendentes");
				
				String SQL = "select C.id,"+//0
															" C.Numero_Contrato,"+//1 
															" C.modelo, " +//2
															" C.numero_Serie as serie,"+//3 
															" c.codigo_cliente,"+//4				  
															" c.filial origem,"+//5
															" tc.sigla,"+//6
															" co.filial destino,"+//7 
															" c.razao_social,"+ //8
															" co.local,"+//9		
															//" tc.sigla as sigla,"+//10 
															" convert(varchar(4000), co.obs) as obs," +
//															"  ((select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N')  - (select max(horimetro) from pmp_maquina_pl pl where pl.numero_serie = C.Numero_Serie"+ 
//															"  and pl.horimetro is not null and pl.id = (select max(id)"+ 
//															"   from pmp_maquina_pl"+
//															" where numero_serie = pl.numero_serie and horimetro is not null and HORIMETRO > 0) )) horas_pendentes," +//11
															" '' horas_pendentes," +//11
															" c.EMAIL_CONTATO_COMERCIAL, " +//12
															" tc.sigla siglaTipoContrato,"+//13
															//"  (select sigla from PMP_TIPO_CONTRATO where ID = c.ID_TIPO_CONTRATO) siglaTipoContrato," +//13
															//" (select IS_PARTNER from pmp_cont_horas_standard where IS_EXECUTADO = 'N' and ID_CONTRATO = c.id"+
															//"	and HORAS_MANUTENCAO = (select MIN(HORAS_MANUTENCAO) from pmp_cont_horas_standard where IS_EXECUTADO = 'N' and ID_CONTRATO = c.id)) isPartner, "+//14
															" '' isPartner," +//15
															" cla.sigla siglaClassificacaoContrato," +//15
															"  c.POSSUI_HORAS_MES, "+//16
															"  c.MEDIA_HORAS_MES, "+//17
															"  c.TELEFONE_COMERCIAL, "+//18
															"  c.TELEFONE_SERVICOS, "+//19
															"  c.CONTATO_SERVICOS, "+//20
															"  c.EMAIL_CONTATO_SERVICOS, "+//21
															"  (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = c.id and is_Executado = 'N') horas_manutencao, "+//22
															"  co.CONTATO as CONTATO_OPERACIONAL, "+//23
															"  co.TELEFONE_CONTATO AS TELEFONE_OPERACIONAL "+//24
												
															" from pmp_contrato c, pmp_config_operacional co, pmp_tipo_contrato tc, pmp_classificacao_contrato cla"+
															" where c.id_Status_Contrato = (select s.id from Pmp_Status_Contrato s where s.sigla = 'CA')"+ 
															" and  c.id in((select  distinct id_Contrato  from Pmp_Cont_Horas_Standard hs where id_Contrato = c.id and is_Executado = 'N'))"+ 
															" and co.num_os is not null "+
															" and co.id_contrato = c.id"+
															" and cla.id = c.id_classificacao_contrato "+
															" and c.id_tipo_contrato = tc.id";
				
				if(idContrato != null && idContrato > 0){
					SQL += " and C.id = "+idContrato;
				}

				SQL += " order by horas_pendentes";
				Query query = manager.createNativeQuery(SQL);
				Integer tamanhoTotalLista = query.getResultList().size();
				

				
				List<Object[]> resultQuery = query.getResultList();		
				//int count = 0;
				for (Object[] objects : resultQuery) {
				//	System.out.println(count);
				//	count++;
					if(idContrato == null){
						idContrato = ((BigDecimal)objects[0]).longValue();
					}
					PmpContrato contrato = manager.find(PmpContrato.class, idContrato);
					//query = manager.createNativeQuery("select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = "+((BigDecimal)objects[0]).longValue()+" and is_Executado = 'N'");
					query = manager.createNativeQuery("select horimetro from pmp_agendamento ag, os_palm palm where ag.id_cont_horas_standard = ("+
							" select max(id) from pmp_cont_horas_standard where id_contrato = "+((BigDecimal)objects[0]).longValue()+" and is_executado = 'S' )"+
					" and palm.id_agendamento = ag.id");
					Long horimetroUltimaRevisao = 0L;
					if(query.getResultList().size() > 0){
						horimetroUltimaRevisao = ((BigDecimal)query.getSingleResult()).longValue();
					}
					BigDecimal horasManutencao = (BigDecimal)objects[22]; 
					
					
					query = manager.createNativeQuery("select st.id, st.standard_job_cptcd, st.IS_PARTNER, frequencia from pmp_cont_horas_standard st where st.horas_manutencao = "+horasManutencao+" and id_Contrato = "+((BigDecimal)objects[0]).longValue());
					BigDecimal idContHorasStandard = BigDecimal.ZERO;
					String standerJob = null;
					String isPartner = null;
					Long horimetroProximaRevisao = 0L;
					if(query.getResultList().size() > 0){
						Object[] pair = (Object[])query.getSingleResult();
						idContHorasStandard = (BigDecimal)pair[0];
						standerJob = (String)pair[1];
						isPartner = (String)pair[2];
						horimetroProximaRevisao = horimetroUltimaRevisao + ((BigDecimal)pair[3]).longValue();
						
					}
					
					
					
//					query = manager.createNativeQuery("select op.num_os from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = "+idContHorasStandard);
//					String numeroOs = null;
//					if(query.getResultList().size() > 0){
//						numeroOs = (String)query.getSingleResult();
//					}
					if(horimetroUltimaRevisao == 0){
						horimetroProximaRevisao = horasManutencao.longValue();
					}
					Long horimetroFaltante = 0L;
					query = manager.createNativeQuery("select distinct horimetro from pmp_maquina_pl pl where pl.numero_serie = '"+(String)objects[3]+"'  and pl.horimetro is not null and pl.horimetro = (select max(horimetro) from pmp_maquina_pl where numero_serie = pl.numero_serie and horimetro is not null and horimetro > 0)");
					BigDecimal horimetro = BigDecimal.ZERO;
					if(query.getResultList().size() > 0){
						horimetro = (BigDecimal)query.getSingleResult();
						horimetroFaltante = horimetroProximaRevisao - horimetro.longValue();
					}
					
//					if(horimetroUltimaRevisao == 0){
//						horimetroUltimaRevisao = horimetro.longValue();
//					}
					if(contrato.getIdClassificacaoContrato().equals("CUS")
							|| contrato.getIdClassificacaoContrato().equals("PART")
							|| contrato.getIdClassificacaoContrato().equals("CUSLIGHT")){

						horimetroUltimaRevisao = contrato.getHorimetroUltimaRevisao();
					}
					query = manager.createNativeQuery(" select CONVERT(varchar(10), ag.data_agendamento, 103) from Pmp_Agendamento ag where ag.ID_STATUS_AGENDAMENTO in (select ID from PMP_STATUS_AGENDAMENTO where SIGLA in ('EA','AT')) and  ag.id_cont_horas_standard = "+idContHorasStandard);
					String dataAgendamento = "";
					if(query.getResultList().size() > 0){
						List<String> strings = (List<String>)query.getResultList();
						for (String data : strings) {
							dataAgendamento += data+" ";
						}
						
					}

					//query = manager.createNativeQuery("select convert(varchar(10),max(pl.data_atualizacao),103) from pmp_maquina_pl pl where pl.numero_serie = '"+(String)objects[3]+"' and pl.horimetro is not null and pl.horimetro > 0");
//					String dataAtualizacaoHorimentro = null;
//					if(query.getResultList().size() > 0){
//						dataAtualizacaoHorimentro = (String)query.getSingleResult();
//					}

//					query = manager.createNativeQuery("select hs.standard_job_cptcd from Pmp_Cont_Horas_Standard hs  where horas_manutencao = "+horasManutencao+" and hs.id_contrato = "+((BigDecimal)objects[0]).longValue());
//					String standerJob = null;
//					if(query.getResultList().size() > 0){
//						standerJob = (String)query.getSingleResult();
//					}
					query = manager.createNativeQuery("select op.num_doc, op.msg, op.COD_ERRO_OS_DBS, op.COD_ERRO_DOC_DBS, op.id, op.num_os  from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = "+idContHorasStandard);
//					String numDoc = null;
//					String msg = null;
//					String codErroOsDbs = null;
//					String codErroDocDbs = null;
					BigDecimal idOsOperacional = null;
					String numeroOs = null;
					if(query.getResultList().size() > 0){
						Object[] pair = (Object[])query.getSingleResult();
//						numDoc = (String)pair[0];
//						msg = (String)pair[1];
//						codErroOsDbs = (String)pair[2];
//						codErroDocDbs = (String)pair[3];
						idOsOperacional = (BigDecimal)pair[4];
						numeroOs = (String)pair[5];
					}
//					if("H305322".equals(numeroOs))
//					{
//						System.out.println("entrei");
//					}
//					query = manager.createNativeQuery("select op.msg from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = "+horasManutencao+" and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
//					String msg = null;
//					if(query.getResultList().size() > 0){
//						msg = (String)query.getSingleResult();
//					}
//					query = manager.createNativeQuery("select op.COD_ERRO_OS_DBS from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = "+horasManutencao+" and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
//					String codErroOsDbs = null;
//					if(query.getResultList().size() > 0){
//						codErroOsDbs = (String)query.getSingleResult();
//					}
//					query = manager.createNativeQuery("select op.COD_ERRO_DOC_DBS from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = "+horasManutencao+" and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
//					String codErroDocDbs = null;
//					if(query.getResultList().size() > 0){
//						codErroDocDbs = (String)query.getSingleResult();
//					}
//					query = manager.createNativeQuery("select id from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = "+horasManutencao+" and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
//					BigDecimal idOsOperacional = null;
//					if(query.getResultList().size() > 0){
//						idOsOperacional = (BigDecimal)query.getSingleResult();
//					}
					
//					query = manager.createNativeQuery("select so.DESCRICAO from Pmp_Cont_Horas_Standard hs, PMP_STATUS_OS so  where hs.id = "+idContHorasStandard+" and so.ID = hs.ID_STATUS_OS ");
//					String financeiro = "";
//					if(query.getResultList().size() > 0){
//						financeiro = (String)query.getSingleResult();
//					}
					
					
					PmpAgendamentoPendente bean = new PmpAgendamentoPendente();
					bean.setDataAtualizacao(new Date());
					bean.setHorimetroUltimaRevisao(horimetroUltimaRevisao.toString());
					bean.setHorimetroProximaRevisao(horimetroProximaRevisao.toString());
					bean.setHorimetroFaltante(horimetroFaltante.toString());
					bean.setIdContrato(((BigDecimal)objects[0]).longValue());
					bean.setNumeroContrato((String)objects[1]);
					bean.setModelo((String)objects[2]);
					bean.setSerie((String)objects[3]);
					bean.setNumOs(numeroOs);
					if(horimetro != null){
						bean.setHorimetro(horimetro.longValue());
					}
					try {
						//bean.setHorasRevisao(((BigDecimal)objects[6]).longValue());
						bean.setHorasRevisao(horasManutencao.longValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if(horasManutencao != null && horimetro != null){
							bean.setHorasPendentes(horasManutencao.longValue() - horimetro.longValue());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					bean.setCodigoCliente((String)objects[4]);
					bean.setIdContHorasStandard(idContHorasStandard.longValue());
//					if(financeiro.length() == 0){
//						bean.setStatusAgendamento((dataAgendamento.equals(""))?"Não Agendado":dataAgendamento);
//					}else{
//						bean.setStatusAgendamento((dataAgendamento.equals(""))?"Não Agendado / "+financeiro:dataAgendamento+" / "+financeiro);
//
//					}
					//bean.setDataAtualizacaoHorimetro(dataAtualizacaoHorimentro);
					//bean.setStandardJob(standerJob);
					bean.setFilial(((BigDecimal)objects[5]).longValue());
					bean.setSiglaTipoContrato((String)objects[6]);
//					if(((String)objects[13]).equals("CAN") || ((String)objects[13]).equals("VPG")){
//						bean.setFilialDestino(((BigDecimal)objects[5]).toString());
//					}else{
						bean.setFilialDestino(((BigDecimal)objects[7]).toString());
					//}
					bean.setRazaoSocial((String)objects[8]);
					bean.setLocal((String)objects[9]);
//					bean.setNumDoc(numDoc);
//					bean.setMsg(msg);
//					bean.setCodErroOsDbs(codErroOsDbs);
//					bean.setCodErroDocDbs(codErroDocDbs);
					if(idOsOperacional != null){
						bean.setIdOsOperacional(idOsOperacional.longValue());
					}
					//bean.setSiglaTipoContrato((String)objects[23]);
//					bean.setTotalRegistros(tamanhoTotalLista);
					bean.setObs("Contato Operacional:"+(String)objects[23]+"\nTelefone Operacional:"+(String)objects[24]+"\nContato:"+(String)objects[20]+"\nTelefone Comercial:"+(String)objects[18]+"\nTelefone de serviços:"+(String)objects[19]+"\nEmail:"+(String)objects[21]+"\nLocalização:"+(String)objects[9]+"\n\nOBS OS:"+(String)objects[10]);
//					bean.setEmailContato((String)objects[12]);
//					bean.setIsPartner(isPartner);
					bean.setSiglaClassificacaoContrato((String)objects[15]);
					bean.setContato((String)objects[23]);
					bean.setTelefoneContato((String)objects[24]);
//					try {
//						if(objects[16] != null && ((String)objects[16]).equals("S")){
//							if(horasManutencao != null && horimetro != null){
//								bean.setHorasPendentes(horasManutencao.longValue() - horimetro.longValue());
//								Double mediaDiasProximaRevisao = ((horasManutencao.doubleValue() - horimetro.doubleValue())/((BigDecimal)objects[17]).doubleValue())*30;
//								query = manager.createNativeQuery(" select DATEDIFF ( day , max(palm.EMISSAO), GETDATE())   from Pmp_Agendamento ag, pmp_os_palm palm where  ag.id_cont_horas_standard in (select st.id from pmp_cont_horas_standard st where id_Contrato = "+((BigDecimal)objects[0]).longValue()+") and palm.ID_AGENDAMENTO = ag.ID");
//								if(query.getResultList().size() > 0 && query.getResultList().get(0) != null){
//									Integer dias = (Integer)query.getSingleResult();
//									mediaDiasProximaRevisao = mediaDiasProximaRevisao - dias;
//								}else{
//									query = manager.createNativeQuery(" select DATEDIFF ( day , max(c.data_aceite), GETDATE())   from Pmp_contrato  c where  c.id = "+((BigDecimal)objects[0]).longValue());
//									Integer dias = (Integer)query.getSingleResult();
//									mediaDiasProximaRevisao = mediaDiasProximaRevisao -dias;
//								}
//								Long diasProximaRevisao = Math.round(mediaDiasProximaRevisao);
//								bean.setMediaDiasProximaRevisao(diasProximaRevisao.toString());
//							}
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
					
					//result.add(bean);
					manager.getTransaction().begin();
					manager.merge(bean);
					manager.getTransaction().commit();
				}
			}catch (Exception e) {
			   e.printStackTrace();
			} finally {
				if(manager != null && manager.isOpen()){
					manager.close();
				}
			}
			//return result;
	}
	public List<AgendamentoBean> findAllAgendamentosPendentesPlus(Integer inicial, Integer numRegistros, String tipoAgendamento, String campoPesquisa){
		EntityManager manager = null;
		List<AgendamentoBean> result = new ArrayList<AgendamentoBean>();
		try{
			manager = JpaUtil.getInstance();
//			Query query = manager.createNativeQuery("select C.id,C.Numero_Contrato, C.modelo, C.numero_Serie as serie, " +
//					" ( select op.num_os from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as num_os, " +
//					//" op.num_os,"+
//					" (select horimetro from pmp_maquina_pl pl where pl.numero_serie = C.Numero_Serie and pl.horimetro is not null and pl.id = (select max(id) from pmp_maquina_pl where numero_serie = pl.numero_serie and horimetro is not null and horimetro > 0) ) as horimetro, " +
//					"  (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N') as horas_manutencao," +
//					" ((select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N')  - (select max(horimetro) from pmp_maquina_pl pl where pl.numero_serie = C.Numero_Serie " +
//					"  and pl.horimetro is not null and pl.data_atualizacao = (select max(data_atualizacao) " +
//					"   from pmp_maquina_pl" +
//					" where numero_serie = pl.numero_serie and horimetro is not null and HORIMETRO > 0) )) horas_pendentes, c.codigo_cliente," +
//					" ( select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.Id)  as idContOperacional," +
//					" ( select count(ag.id) from Pmp_Agendamento ag where  ag.id_cont_horas_standard = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N') and id_Contrato = C.id)) statusAgendamento," +
//					" (select convert(varchar(10),max(pl.data_atualizacao),103) from pmp_maquina_pl pl where pl.numero_serie = c.numero_serie)," +
//					" (select hs.standard_job_cptcd from Pmp_Cont_Horas_Standard hs"+
//					" where horas_manutencao = (select  min(horas_manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N')"+
//					" and hs.id_contrato = C.id) standard_job_cptcd, " +
//					" c.filial origem," +
//					" (select t.sigla from pmp_tipo_contrato t where t.id = c.id_tipo_contrato), co.filial destino, c.razao_social, co.local," +
//					" ( select op.num_doc from pmp_os_operacional op where  op.id = (select st.id_os_operacional from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as num_doc," +
//					" ( select op.msg from pmp_os_operacional op where  op.id = (select st.id_os_operacional from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as msg," +
//					" ( select op.COD_ERRO_OS_DBS from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as COD_ERRO_OS_DBS,"+
//					" ( select op.COD_ERRO_DOC_DBS from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as COD_ERRO_DOC_DBS,"+
//					" ( select op.id from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD = (select st.id from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as idOsOperacional," +
//					"  tc.sigla as sigla, convert(varchar(4000), co.obs) as obs"+
//					"  from pmp_contrato c, pmp_config_operacional co, pmp_tipo_contrato tc" +
//					" where c.id_Status_Contrato = (select s.id from Pmp_Status_Contrato s where s.sigla = 'CA') " +
//					" and  c.id in((select  distinct id_Contrato  from Pmp_Cont_Horas_Standard hs where id_Contrato = c.id and is_Executado = 'N')) " +
//					" and co.num_os is not null " +
//					" and co.id_contrato = c.id" +
//					" and c.id_tipo_contrato = tc.id" +
//					//" and op.id_Config_Operacional = co.id" +
//					" and co.filial = "+Integer.valueOf(FILIAL)+
//					//Caso a OS puder ser agendada mais de uma vez comentar essa linha
//					//" and co.id not in (select ag.id_cong_operacional from pmp_agendamento ag)"+
//					" order by horas_pendentes");
			
			String SQL = "select C.id,"+//0
			" C.Numero_Contrato,"+//1 
			" C.modelo, " +//2
			" C.numero_Serie as serie,"+//3 
			" c.codigo_cliente,"+//4				  
			" c.filial origem,"+//5
			" tc.sigla siglaTipoContrato,"+//6
			" co.filial destino,"+//7 
			" c.razao_social,"+ //8
			" co.local,"+//9		
			//" tc.sigla as sigla,"+
			" convert(varchar(4000), co.obs) as obs," +//10
			"  0 horas_pendentes," +//11
			" c.EMAIL_CONTATO_COMERCIAL, "+//12
			//" (select datediff(day, getdate(), data_revisao) from Pmp_Cont_Horas_Standard where id = (select  min (id)  from Pmp_Cont_Horas_Standard hs where id_Contrato = c.id and is_Executado = 'N')) diasPendentes,"+//13
			" cla.sigla siglaClassificacaoContrato," +//14
			"  (select sigla from PMP_TIPO_CONTRATO where ID = c.ID_TIPO_CONTRATO) siglaTipoContrato, "+//15
			"  c.TELEFONE_COMERCIAL, "+//16
			"  c.TELEFONE_SERVICOS, "+//17
			"  c.CONTATO_SERVICOS, "+//18
			"  c.EMAIL_CONTATO_SERVICOS "+//19
			" from pmp_contrato c, pmp_config_operacional co, pmp_tipo_contrato tc, pmp_classificacao_contrato cla"+
			" where c.id_Status_Contrato = (select s.id from Pmp_Status_Contrato s where s.sigla = 'CA')"+ 
			" and  c.id in((select  distinct id_Contrato  from Pmp_Cont_Horas_Standard_Plus hs where id_Contrato = c.id and is_Executado = 'N'))"+ 
			" and co.num_os is not null "+
			" and cla.id = c.id_classificacao_contrato "+
			" and co.id_contrato = c.id"+
			" and c.id_tipo_contrato = tc.id"+
			" and co.filial = "+Integer.valueOf(FILIAL);
			if(tipoAgendamento.equals("TA")){
				SQL += " and  (select count(ag.id) from Pmp_Agendamento ag where ag.ID_STATUS_AGENDAMENTO in (select ID from PMP_STATUS_AGENDAMENTO where SIGLA in ('EA','AT')) and  ag.id_cont_horas_standard_plus = (select st.id from pmp_cont_horas_standard_plus st where st.id = (select  min(id) from Pmp_Cont_Horas_Standard_Plus  where id_Contrato = c.id and is_Executado = 'N') and id_Contrato = c.id)) > 0";
			}
			if(tipoAgendamento.equals("TNA")){
				SQL += " and  (select count(ag.id) from Pmp_Agendamento ag where  ag.id_cont_horas_standard_plus = (select st.id from pmp_cont_horas_standard_Plus st where st.horas_manutencao = (select  min(id) from Pmp_Cont_Horas_Standard_Plus  where id_Contrato = c.id and is_Executado = 'N') and id_Contrato = c.id)) = 0";
			}
			if(campoPesquisa.trim().length() > 0){
				SQL += " and (C.numero_Serie like '%"+campoPesquisa.toUpperCase()+"%' or C.modelo like '%"+campoPesquisa.toUpperCase()+"%' or C.Numero_Contrato like '%"+campoPesquisa.toUpperCase()+"%' or C.numero_Serie like '%"+campoPesquisa.toUpperCase()+"%' or c.razao_social like '%"+campoPesquisa.toUpperCase()+"%')";
			}
			
			SQL += " order by horas_pendentes";
			Query query = manager.createNativeQuery(SQL);
			Integer tamanhoTotalLista = query.getResultList().size();
			query.setFirstResult(inicial);
			query.setMaxResults(numRegistros);
			
			
			List<Object[]> resultQuery = query.getResultList();			
			for (Object[] objects : resultQuery) {

				
				query = manager.createNativeQuery("select op.num_os from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD_PLUS = (select  min(id) from Pmp_Cont_Horas_Standard_Plus  where is_Executado = 'N' and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
				String numeroOs = null;
				if(query.getResultList().size() > 0){
					numeroOs = (String)query.getSingleResult();
				}
				query = manager.createNativeQuery("select distinct horimetro from pmp_maquina_pl pl where pl.numero_serie = '"+(String)objects[3]+"'  and pl.horimetro is not null and pl.horimetro = (select max(horimetro) from pmp_maquina_pl where numero_serie = pl.numero_serie and horimetro is not null and horimetro > 0)");
				BigDecimal horimetro = BigDecimal.ZERO;
				if(query.getResultList().size() > 0){
					horimetro = (BigDecimal)query.getSingleResult();
				}
				query = manager.createNativeQuery("select  min(id) from Pmp_Cont_Horas_Standard_Plus  where is_Executado = 'N' and id_Contrato = "+((BigDecimal)objects[0]).longValue());
				BigDecimal idContHorasStandard = BigDecimal.ZERO;
				if(query.getResultList().size() > 0){
					idContHorasStandard = (BigDecimal)query.getSingleResult();
				}
				query = manager.createNativeQuery(" select CONVERT(varchar(10), ag.data_agendamento, 103) from Pmp_Agendamento ag where ag.ID_STATUS_AGENDAMENTO in (select ID from PMP_STATUS_AGENDAMENTO where SIGLA in ('EA','AT')) and  ag.id_cont_horas_standard_plus = (select  min(id) from Pmp_Cont_Horas_Standard_Plus  where is_Executado = 'N' and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
				String dataAgendamento = "";
				if(query.getResultList().size() > 0){
					List<String> strings = (List<String>)query.getResultList();
					for (String data : strings) {
						dataAgendamento += data+" ";
					}
					
				}
				
				//query = manager.createNativeQuery("select convert(varchar(10),max(pl.data_atualizacao),103) from pmp_maquina_pl pl where pl.numero_serie = '"+(String)objects[3]+"' and pl.horimetro is not null and pl.horimetro > 0");
//				String dataAtualizacaoHorimentro = null;
//				if(query.getResultList().size() > 0){
//					dataAtualizacaoHorimentro = (String)query.getSingleResult();
//				}
				
				query = manager.createNativeQuery("select hs.standard_job_cptcd from Pmp_Cont_Horas_Standard_Plus hs  where hs.id = (select  min(id) from Pmp_Cont_Horas_Standard_Plus  where is_Executado = 'N' and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
				String standerJob = null;
				if(query.getResultList().size() > 0){
					standerJob = (String)query.getSingleResult();
				}
				query = manager.createNativeQuery("select op.num_doc from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD_PLUS = (select  min(id) from Pmp_Cont_Horas_Standard_Plus  where is_Executado = 'N' and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
				String numDoc = null;
				if(query.getResultList().size() > 0){
					numDoc = (String)query.getSingleResult();
				}
				query = manager.createNativeQuery("select op.msg from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD_PLUS = (select  min(id) from Pmp_Cont_Horas_Standard_Plus  where is_Executado = 'N' and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
				String msg = null;
				if(query.getResultList().size() > 0){
					msg = (String)query.getSingleResult();
				}
				query = manager.createNativeQuery("select op.COD_ERRO_OS_DBS from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD_PLUS = (select  min(id) from Pmp_Cont_Horas_Standard_Plus  where is_Executado = 'N' and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
				String codErroOsDbs = null;
				if(query.getResultList().size() > 0){
					codErroOsDbs = (String)query.getSingleResult();
				}
				query = manager.createNativeQuery("select op.COD_ERRO_DOC_DBS from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD_PLUS = (select  min(id) from Pmp_Cont_Horas_Standard_Plus  where is_Executado = 'N' and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
				String codErroDocDbs = null;
				if(query.getResultList().size() > 0){
					codErroDocDbs = (String)query.getSingleResult();
				}
				query = manager.createNativeQuery("select id from pmp_os_operacional op where  op.ID_CONT_HORAS_STANDARD_PLUS = (select  min(id) from Pmp_Cont_Horas_Standard_Plus  where is_Executado = 'N' and id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
				BigDecimal idOsOperacional = null;
				if(query.getResultList().size() > 0){
					idOsOperacional = (BigDecimal)query.getSingleResult();
				}
				
				query = manager.createNativeQuery("select so.DESCRICAO from Pmp_Cont_Horas_Standard_Plus hs, PMP_STATUS_OS so  where hs.id = (select  min(id) from Pmp_Cont_Horas_Standard_Plus  where is_Executado = 'N' and id_Contrato = "+((BigDecimal)objects[0]).longValue()+") and so.ID = hs.ID_STATUS_OS");
				String financeiro = "";
				if(query.getResultList().size() > 0){
					financeiro = (String)query.getSingleResult();
				}
				AgendamentoBean bean = new AgendamentoBean();
				bean.setIdContrato(((BigDecimal)objects[0]).longValue());
				bean.setNumContrato((String)objects[1]);
				bean.setModelo((String)objects[2]);
				bean.setNumSerie((String)objects[3]);
				bean.setNumOs(numeroOs);
				//bean.setDiasProximaRevisao((Integer)objects[13]);
				bean.setSiglaClassificacaoContrato((String)objects[14]);
				if(horimetro != null){
					bean.setHorimetro(horimetro.longValue());
				}
				
//				try {
//					//bean.setHorasRevisao(((BigDecimal)objects[6]).longValue());
//					bean.setHorasRevisao(horasManutencao.longValue());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				try {
//					if(horasManutencao != null && horimetro != null){
//						bean.setHorasPendentes(horasManutencao.longValue() - horimetro.longValue());
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				bean.setCodigoCliente((String)objects[4]);
				bean.setIdContHorasStandard(idContHorasStandard.longValue());
				if(financeiro.length() == 0){
					bean.setStatusAgendamento((dataAgendamento.equals(""))?"Não Agendado":dataAgendamento);
				}else{
					bean.setStatusAgendamento((dataAgendamento.equals(""))?"Não Agendado / "+financeiro:dataAgendamento+" / "+financeiro);

				}
				//bean.setDataAtualizacaoHorimetro(dataAtualizacaoHorimentro);
				bean.setStandardJob(standerJob);
				bean.setFilial(((BigDecimal)objects[5]).toString());
				bean.setSiglaTipoContrato((String)objects[6]);
				if(((String)objects[15]).equals("CAN") || ((String)objects[15]).equals("VPG")){
					bean.setFilialDestino(((BigDecimal)objects[5]).toString());
				}else{
					bean.setFilialDestino(((BigDecimal)objects[7]).toString());
				}
				bean.setRazaoSocial((String)objects[8]);
				bean.setLocal((String)objects[9]);
				bean.setNumDoc(numDoc);
				bean.setMsg(msg);
				bean.setCodErroOsDbs(codErroOsDbs);
				bean.setCodErroDocDbs(codErroDocDbs);
				if(idOsOperacional != null){
					bean.setIdOsOperacional(idOsOperacional.longValue());
				}
				//bean.setSiglaTipoContrato((String)objects[23]);
				bean.setTotalRegistros(tamanhoTotalLista);
				bean.setObsOs("Contato:"+(String)objects[18]+"\nTelefone Comercial:"+(String)objects[16]+"\nTelefone de serviços:"+(String)objects[17]+"\nEmail:"+(String)objects[19]+"\nLocalização:"+(String)objects[9]+"\n\nOBS OS:"+(String)objects[10]);
				//bean.setObsOs((String)objects[10]);
				bean.setEmailContato((String)objects[12]);
				result.add(bean);
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	public Boolean saveObsAgendamento(AgendamentoBean bean) {
		
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			
			PmpAgendamento agendamento = null;
			
			if(bean.getId() == null || bean.getId() == 0){				
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar calBegin = new GregorianCalendar();
			calBegin.setTime(sdf.parse(bean.getDataAgendamento()));
			
			agendamento = new PmpAgendamento();
			
			Query query = manager.createQuery("FROM PmpStatusAgendamento WHERE sigla = 'OBS'");
			
			PmpStatusAgendamento statusAgendamento = (PmpStatusAgendamento)query.getSingleResult();
			
			agendamento.setIdStatusAgendamento(statusAgendamento);
			agendamento.setIdFuncSupervisor(ID_FUNCIONARIO);
			agendamento.setIdFuncionario(bean.getIdFuncionario());
			agendamento.setDataAgendamento(calBegin.getTime());
			agendamento.setFilial(Long.valueOf(FILIAL));
			agendamento.setNumOs("OBSERVAÇÃO");
			agendamento.setObs(bean.getObs());
			manager.persist(agendamento);			
			}else{
				agendamento = manager.find(PmpAgendamento.class, bean.getId());
				agendamento.setObs(bean.getObs());
				manager.merge(agendamento);
			}
			
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
	
	public Boolean removerAgendamentoObs(AgendamentoBean bean) {

		EntityManager manager = null;

		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpAgendamento agendamento = manager.find(PmpAgendamento.class, bean.getId());
			manager.remove(agendamento);
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
	
	public Boolean saveDataFaturamento(AgendamentoBean bean, String encerrarAutomatica) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		EntityManager manager = null;
		Connection con = null;
		Statement prstmtDataUpdate = null;
		ResultSet rs = null;
		PreparedStatement prstmt_ = null;
		Statement prstmt = null;
        String cuno = "";
        Double VLROSP = null;
        Double VLROSS = null;
		try {

			manager = JpaUtil.getInstance();
			con = ConectionDbs.getConnecton();

			String SQL = "select s.LBAMT mo, s.WIPPAS pecas from LIBU17.WOPHDRS0 w, LIBU17.WOPSEGS0 s"+ 
					  	 "	where w.wono = '"+bean.getNumOs()+"'"+
						 "	and s.WONO = w.WONO";
			prstmt_ = con.prepareStatement(SQL);
			rs = prstmt_.executeQuery();
			String valorMaoDeObra = "";
			String valorPecas = "";
			String valorTotal = "";
			if(rs.next()){
				if(rs.getString("mo") != null && rs.getString("pecas") != null){
					valorMaoDeObra = ValorMonetarioHelper.formata("###,###,##0.00",rs.getDouble("mo"));
					valorPecas = ValorMonetarioHelper.formata("###,###,##0.00",rs.getDouble("pecas"));
					valorTotal = ValorMonetarioHelper.formata("###,###,##0.00",rs.getDouble("mo")+rs.getDouble("pecas"));
				}
			}


			//Query query = manager.createNativeQuery("SELECT ESTIMATEBY FROM tw_funcionario WHERE EPIDNO = '"+agendamento.getIdFuncionario()+"'");

			String estimateBy = this.usuarioBean.getEstimateBy();
			if(encerrarAutomatica.equals("N")){
				prstmtDataUpdate = setDateFluxoOSDBSNaoAutomatico(bean.getDataFaturamento(), con, bean.getNumOs(), "007", estimateBy);
			}else{
				prstmtDataUpdate = setDateFluxoOSDBSEncerrarAutomatica(bean.getDataFaturamento(), con, bean.getNumOs(), "007", estimateBy);
			}
			manager.getTransaction().begin();

			PmpAgendamento agendamento = manager.find(PmpAgendamento.class, bean.getId());
			agendamento.setDataFaturamento(sdf.parse(bean.getDataFaturamento()));
			agendamento.setEncerrarAutomatica(encerrarAutomatica);
			manager.merge(agendamento);
			manager.getTransaction().commit();
			
			
			Boolean lbccExterno = this.verificarLBCC(bean.getNumOs());
			
		if(lbccExterno == null){

			manager.getTransaction().begin();
			agendamento.setDataFaturamento(null);
			agendamento.setEncerrarAutomatica(null);
			manager.merge(agendamento);
			manager.getTransaction().commit();
		}
			if(lbccExterno == true){
				Long filial = null;
				if(agendamento.getIdContHorasStandard() != null && agendamento.getIdContHorasStandard().getIdContrato().getIdTipoContrato().getSigla().equals("VEN")||
						agendamento.getIdContHorasStandard().getIdContrato().getIdTipoContrato().getSigla().equals("VPG")
						|| agendamento.getIdContHorasStandard().getIdContrato().getIdTipoContrato().getSigla().equals("CAN")){
					filial = agendamento.getIdContHorasStandard().getIdOsOperacional().getFilial();
					String nomeContato = "NOME DO CONTATO ........:"+agendamento.getIdContHorasStandard().getIdContrato().getContatoServicos();
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "010", (nomeContato.length() > 50)?nomeContato.substring(0, 50):nomeContato);
					String telefoneContato = "TELEFONE DO CONTATO ....:"+agendamento.getIdContHorasStandard().getIdContrato().getTelefoneServicos();
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "011", (telefoneContato.length() > 50)?telefoneContato.substring(0, 50):telefoneContato);
					String emailContato = "EMAIL CONTATO..:"+agendamento.getIdContHorasStandard().getIdContrato().getEmailContatoServicos();
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "012", (emailContato.length() > 50)?emailContato.substring(0, 50):emailContato);
					String maoDeObra = "VALOR MOBRA/MISC/DESLOC.: R$"+valorMaoDeObra;
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "014", (maoDeObra.length() > 50)?maoDeObra.substring(0, 50):maoDeObra);
					String pecas = "VALOR DE PECAS .........: R$"+valorPecas+" %"+agendamento.getIdContHorasStandard().getIdContrato().getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas();
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "015", (pecas.length() > 50)?pecas.substring(0, 50):pecas);
					String total = "VALOR TOTAL ............: R$"+valorTotal;
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "016", (total.length() > 50)?total.substring(0, 50):total);
					String condicaoPagamento = "CONDICAO DE PAGAMENTO ..:"+bean.getCondicaoPagamentoPecas()+" "+bean.getCondicaoPagamentoServicos();
					if(agendamento.getIdContHorasStandard().getIdContrato().getIdTipoContrato().getSigla().equals("VEN")){
						prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "017", (condicaoPagamento.length() > 50)?condicaoPagamento.substring(0, 50):condicaoPagamento);
					}else{
						condicaoPagamento = "CONDICAO DE PAGAMENTO ..: "+agendamento.getIdContHorasStandard().getIdContrato().getQtdParcelas()+" parcela(s)";
						prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "017", (condicaoPagamento.length() > 50)?condicaoPagamento.substring(0, 50):condicaoPagamento);
					}
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "018", "PROPOSTA NUMERO ........: "+agendamento.getIdContHorasStandard().getIdContrato().getNumeroContrato());
					nomeContato =  "AUTORIZADO POR .........: "+agendamento.getIdContHorasStandard().getIdContrato().getContatoServicos();
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "019", ((nomeContato.length() > 50)?nomeContato.substring(0, 50):nomeContato));
					//TwFuncionario funcionario = manager.find(TwFuncionario.class, agendamento.getIdContHorasStandard().getIdOsOperacional().getIdFuncionarioCriadorOs());
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "021", "NOTES PREENCHIDO POR .........: "+estimateBy);

				} else if(agendamento.getIdContHorasStandardPlus() != null){
					filial = agendamento.getIdContHorasStandardPlus().getIdOsOperacional().getFilial();
					String nomeContato = "NOME DO CONTATO ........:"+agendamento.getIdContHorasStandardPlus().getIdContrato().getContatoServicos();
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "010", (nomeContato.length() > 50)?nomeContato.substring(0, 50):nomeContato);
					String telefoneContato = "TELEFONE DO CONTATO ....:"+agendamento.getIdContHorasStandardPlus().getIdContrato().getTelefoneServicos();
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "011", (telefoneContato.length() > 50)?telefoneContato.substring(0, 50):telefoneContato);
					String emailContato = "EMAIL CONTATO..:"+agendamento.getIdContHorasStandardPlus().getIdContrato().getEmailContatoServicos();
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "012", (emailContato.length() > 50)?emailContato.substring(0, 50):emailContato);
					String maoDeObra = "VALOR MOBRA/MISC/DESLOC.: R$"+valorMaoDeObra;
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "014", (maoDeObra.length() > 50)?maoDeObra.substring(0, 50):maoDeObra);
					String pecas = "VALOR DE PECAS .........: R$"+valorPecas+" %"+agendamento.getIdContHorasStandard().getIdContrato().getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas();;
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "015", (pecas.length() > 50)?pecas.substring(0, 50):pecas);
					String total = "VALOR TOTAL ............: R$"+valorTotal;
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "016", (total.length() > 50)?total.substring(0, 50):total);
					String condicaoPagamento = "CONDICAO DE PAGAMENTO ..:"+bean.getCondicaoPagamentoPecas()+" "+bean.getCondicaoPagamentoServicos();
					if(agendamento.getIdContHorasStandardPlus().getIdContrato().getIdTipoContrato().getSigla().equals("VEN")){
						prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "017", (condicaoPagamento.length() > 50)?condicaoPagamento.substring(0, 50):condicaoPagamento);
					}else{
						condicaoPagamento = "CONDICAO DE PAGAMENTO ..: "+agendamento.getIdContHorasStandardPlus().getIdContrato().getQtdParcelas()+" parcela(s)";
						prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "017", (condicaoPagamento.length() > 50)?condicaoPagamento.substring(0, 50):condicaoPagamento);
					}
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "018", "PROPOSTA NUMERO ........: "+agendamento.getIdContHorasStandardPlus().getIdContrato().getNumeroContrato());
					nomeContato =  "AUTORIZADO POR .........: "+agendamento.getIdContHorasStandard().getIdContrato().getContatoServicos();
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "019", ((nomeContato.length() > 50)?nomeContato.substring(0, 50):nomeContato));
					prstmtDataUpdate = setNotesOSDBS(con, bean.getNumOs(), "021", "NOTES PREENCHIDO POR .........: "+estimateBy);
				}



				SQL = "select cuno from "+IConstantAccess.LIB_DBS+".wophdrs0 where wono = '"+bean.getNumOs()+"'";
				prstmt = con.createStatement();
				rs = prstmt.executeQuery(SQL);
				if(rs.next()){
					cuno = rs.getString("cuno");
				}

				//VLROSP = null; 
				SQL = "select sum(WIPPAS) WIPPAS from "+IConstantAccess.LIB_DBS+".wopsegs0 where wono = '"+bean.getNumOs()+"'";
				prstmt = con.createStatement();
				rs = prstmt.executeQuery(SQL);
				if(rs.next()){
					VLROSP = rs.getDouble("WIPPAS");
				}


				// VLROSS = null; 

				SQL = "select sum(lbamt) lbamt from "+IConstantAccess.LIB_DBS+".wopsegs0 where wono = '"+bean.getNumOs()+"'";
				prstmt = con.createStatement();
				rs = prstmt.executeQuery(SQL);
				if(rs.next()){
					VLROSS = rs.getDouble("lbamt");
				}



				SimpleDateFormat formatFaturamento = new SimpleDateFormat("dd.MM.yyyy");
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date dataFat = format.parse(bean.getDataFaturamento());

				if(VLROSP == 0 && VLROSS == 0){
					SQL = "insert into "+IConstantAccess.PESASAPARQ+".SAPINFWONO (CODEMP, WONO, CUNO, STNO, DTAFAT, "+
					" ENCAUT) values ('PESA', '"+bean.getNumOs()+"', '"+cuno+"', '"+filial+"'," +
					" '"+formatFaturamento.format(dataFat)+"','"+encerrarAutomatica+"')";
					prstmt = con.createStatement();
					prstmt.executeUpdate(SQL);
				}else{
					String autPor = (bean.getAutPor().length() > 20)?bean.getAutPor().substring(0,20):bean.getAutPor();
					SQL = "insert into "+IConstantAccess.PESASAPARQ+".SAPINFWONO (CODEMP, WONO, CUNO, STNO, PROPOSTA, AUTORIZADO, ORDCOMP, ORDCOMS,"+ 
					" ESTCREDP, ESTCREDS, CPAGP, CPAGS, DSCPCTP, DSCPCTS, DSCVLRP, DSCVLRS, VLRORCP, VLRORCS, VLROSP, VLROSS, DTAFAT,"+
					" ENCAUT, MANAUT, OBSPEC, OBSSER, CONDESP) values ('PESA', '"+bean.getNumOs()+"', '"+cuno+"', '"+filial+"'," +
					" '"+bean.getPropNumero()+"','"+autPor+"', '"+bean.getOrdemCompraPeca()+"','"+bean.getOrdemCompraServico()+"'," +
					" '"+bean.getEstabelecimentoCredorPecas()+"','"+bean.getEstabelecimentoCredorServicos()+"', '"+bean.getCondicaoPagamentoPecas()+"'," +
					" '"+bean.getCondicaoPagamentoServicos().replace(".","").replace(",", ".")+"', '"+bean.getDescPorcPecas().replace(".","").replace(",", ".")+"', '"+bean.getDescPorcServicos().replace(".","").replace(",", ".")+"'," +
					" '"+bean.getDescValorPecas().replace(".","").replace(",", ".")+"', '"+bean.getDescValorServicos().replace(".","").replace(",", ".")+"', '"+bean.getValorPecas().replace(".","").replace(",", ".")+"', '"+bean.getMoMiscDesl().replace(".","").replace(",", ".")+"'," +
					" '"+VLROSP+"', '"+VLROSS+"', '"+formatFaturamento.format(dataFat)+"','"+encerrarAutomatica+"','"+bean.getAutomaticaFaturada()+"'," +
					" '"+bean.getObsPeca()+"', '"+bean.getObsServico()+"', '"+((bean.getCondEspecial() != null)?bean.getCondEspecial():"")+"')";
					prstmt = con.createStatement();
					prstmt.executeUpdate(SQL);
				}
			}
			return true;
			
		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			manager.getTransaction().begin();

			PmpAgendamento agendamento = manager.find(PmpAgendamento.class, bean.getId());
			agendamento.setDataFaturamento(null);
			agendamento.setEncerrarAutomatica(null);
			manager.merge(agendamento);
			manager.getTransaction().commit();
			StringWriter writer = new StringWriter();
		   e.printStackTrace(new PrintWriter(writer));
		   new EmailHelper().sendSimpleMail(writer.toString(), "ERRO AO ENVIAR DATA DE FATURAMENTO", "rodrigo@rdrsistemas.com.br");
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
			try {
				if(prstmtDataUpdate != null){
					prstmtDataUpdate.close();
				}
				if(con != null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		return false;
	}
	
	public List<AgendamentoBean> pesquisarOsNaoRealizada(String numOs){
		List<AgendamentoBean> result = new ArrayList<AgendamentoBean>();

		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			
			String SQL = "select os.NUM_OS, hs.HORAS_MANUTENCAO from PMP_OS_OPERACIONAL os, PMP_CONT_HORAS_STANDARD hs "+
					" where NUM_OS like '%"+numOs+"%'"+
					" and os.ID = hs.ID_OS_OPERACIONAL"+
					" and hs.IS_EXECUTADO = 'N'";
			
			
			Query query = manager.createNativeQuery(SQL);
			List<Object[]> list = query.getResultList();
			for (Object[] pmpAgendamento : list) {
				AgendamentoBean bean = new AgendamentoBean();
				bean.setNumOs((String)pmpAgendamento[0]);
				bean.setHorasRevisao(((BigDecimal)pmpAgendamento[1]).longValue());
				result.add(bean);
			}

		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		System.out.println("NOME DO CONTATO ........:"+calendar.get(Calendar.MINUTE));
	}

	public Boolean removerAgendamento(AgendamentoBean bean){
		EntityManager manager = null;
		try{
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			String SQL = "update "+ 
					" PMP_CONT_HORAS_STANDARD "+ 
					" set ID_OS_OPERACIONAL = null "+
					" where ID_OS_OPERACIONAL = (select id from PMP_OS_OPERACIONAL where NUM_OS =:numOs)";
			
            Query query = manager.createNativeQuery(SQL);
            query.setParameter("numOs", bean.getNumOs());
            query.executeUpdate();

            SQL ="delete from PMP_AGENDAMENTO where NUM_OS =:numOs ";
            query = manager.createNativeQuery(SQL);
            query.setParameter("numOs", bean.getNumOs());
            query.executeUpdate();
            
            SQL ="delete from PMP_OS_OPERACIONAL where NUM_OS =:numOs ";
            query = manager.createNativeQuery(SQL);
            query.setParameter("numOs", bean.getNumOs());
            query.executeUpdate();
            
			manager.getTransaction().commit();
			PmpAgendamento agendamento = manager.find(PmpAgendamento.class, bean.getId());
			if(agendamento.getIdContHorasStandard() != null){
				this.saveAgendamentosPendentes(agendamento.getIdContHorasStandard().getIdContrato().getId());
			}else{
				this.saveAgendamentosPendentes(agendamento.getIdContHorasStandardPlus().getIdContrato().getId());
			}
			return true;
		}catch (Exception e) {
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
	
	public AgendamentoBean findValorTelaDataFaturamento(AgendamentoBean  bean){
		EntityManager manager = null;
		Connection conn = null;
		Statement prstmtDataUpdate = null;
		ResultSet rs = null;
		PreparedStatement prstmt_ = null;
		Statement prstmt = null;
		try {
			conn = ConectionDbs.getConnecton();
			manager = JpaUtil.getInstance();
			String SQL = "	select c.NUMERO_CONTRATO, c.FILIAL credorPeca, cop.FILIAL credorServico, (select JBCDDS from JBCD where JBCD = op.JOB_CODE) +' '+"+
				" (select CPTCDD from CPTCD where CPTCD = op.COMP_CODE) +' PM '+CONVERT(varchar(4000), ag.HORAS_REVISAO), c.CONTATO_SERVICOS "+
			" from PMP_AGENDAMENTO ag, PMP_OS_OPERACIONAL op, "+
			" PMP_CONT_HORAS_STANDARD hs, PMP_CONFIG_OPERACIONAL cop, PMP_CONTRATO c"+
			" where ag.ID = "+bean.getId()+
			" and ag.ID_CONT_HORAS_STANDARD = hs.ID"+
			" and hs.ID_OS_OPERACIONAL = op.ID"+
			" and cop.ID = op.ID_CONFIG_OPERACIONAL"+
			" and c.ID = hs.ID_CONTRATO";	
			Query query = manager.createNativeQuery(SQL);
			List<Object[]> pair = query.getResultList();
			for (Object[] objects : pair) {
				String NUMERO_CONTRATO = (String)objects[0];
				Long credorPeca =  ((BigDecimal)objects[1]).longValue();
				Long credorServico = ((BigDecimal)objects[2]).longValue();
				String jobCode = (String)objects[3];
				String autPor = (String)objects[4];
				bean.setNumContrato(NUMERO_CONTRATO);
				bean.setCredorPeca(credorPeca);
				bean.setCredorServico(credorServico);
				//bean.setCompCode(compCode);
				bean.setJobCode(jobCode);
				bean.setAutPor(autPor);
			}

			SQL = "select s.LBAMT mo, s.WIPPAS pecas from LIBU17.WOPHDRS0 w, LIBU17.WOPSEGS0 s"+ 
			"	where w.wono = '"+bean.getNumOs()+"'"+
			"	and s.WONO = w.WONO";
			prstmt_ = conn.prepareStatement(SQL);
			rs = prstmt_.executeQuery();
			String valorMaoDeObra = "";
			String valorPecas = "";
			String valorTotal = "";
			if(rs.next()){
				if(rs.getString("mo") != null && rs.getString("pecas") != null){
					valorMaoDeObra = ValorMonetarioHelper.formata("###,###,##0.00",rs.getDouble("mo"));
					valorPecas = ValorMonetarioHelper.formata("###,###,##0.00",rs.getDouble("pecas"));
					valorTotal = ValorMonetarioHelper.formata("###,###,##0.00",rs.getDouble("mo")+rs.getDouble("pecas"));
				}
			}
			bean.setValorPecas(valorPecas);
			bean.setMoMiscDesl(valorMaoDeObra);
			bean.setValorTotal(valorTotal);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return bean;


	}
	public Boolean verificarLBCC(String numeroOs){

		Statement prstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = ConectionDbs.getConnecton();
			
			String cuno = "";
			String SQL = "select cuno from "+IConstantAccess.LIB_DBS+".wophdrs0 where wono = '"+numeroOs+"'";
			prstmt = con.createStatement();
			rs = prstmt.executeQuery(SQL);
			if(rs.next()){
				cuno = rs.getString("cuno");
			}
			
			SQL = "select LBCC from "+IConstantAccess.LIB_DBS+".CIPNAME0 where cuno = '"+cuno+"'";
			rs = prstmt.executeQuery(SQL);
			if(rs.next()){
				String lbcc = rs.getString("LBCC");
				if(lbcc.equals("10")){
					return true;
				}
			}
			
			return false;
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(con != null){
					con.close();
				}
				if(prstmt != null){
					prstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		return null;
	}
}

