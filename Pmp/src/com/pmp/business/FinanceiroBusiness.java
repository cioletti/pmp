package com.pmp.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.FinanceiroBean;
import com.pmp.bean.StatusOsBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.entity.GerarSequencia;
import com.pmp.entity.PmpContHorasStandard;
import com.pmp.entity.PmpContHorasStandardPlus;
import com.pmp.entity.PmpContrato;
import com.pmp.entity.PmpFinanceiro;
import com.pmp.entity.PmpStatusOs;
import com.pmp.entity.TwFilial;
import com.pmp.util.ConectionDbs;
import com.pmp.util.EmailHelper;
import com.pmp.util.IConstantAccess;
import com.pmp.util.JpaUtil;
import com.pmp.util.UtilGestaoEquipamentosPesa;
import com.pmp.util.ValorMonetarioHelper;

public class FinanceiroBusiness {
	
	private UsuarioBean usuarioBean;
	public FinanceiroBusiness(UsuarioBean usuarioBean) {
		this.usuarioBean = usuarioBean;
	}
	
	public Boolean enviarFinanceiro(FinanceiroBean bean){
		EntityManager manager = null;
		Connection con = null;
		 Statement prstmt = null;
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createQuery("from PmpStatusOs where sigla = 'AFI'");
			PmpStatusOs idStatusOs = (PmpStatusOs)query.getSingleResult(); 
			
			PmpContrato contrato = manager.find(PmpContrato.class, bean.getIdContrato());
			
			
			
			PmpFinanceiro financeiro = null;
			if(contrato.getIdClassificacaoContrato().getSigla().equals("PLUS")){
				query = manager.createQuery("from PmpFinanceiro where codCliente =:codCliente and dataLiberacao is null and dataRejeicao is null and idContHorasStandardPlus.id.id = "+bean.getIdContHorasStandard());
			}else{
				query = manager.createQuery("from PmpFinanceiro where codCliente =:codCliente and dataLiberacao is null and dataRejeicao is null and idContHorasStandard.id = "+bean.getIdContHorasStandard());
			}
			query.setParameter("codCliente", contrato.getCodigoCliente());
			String status = "";
			String sequencia = "";
			if(query.getResultList().size() > 0){
				financeiro = (PmpFinanceiro)query.getSingleResult();
				if(financeiro.getStatus().equals("R")){
					status = "R";
					sequencia = financeiro.getCrdDbs();
					financeiro = new PmpFinanceiro();
				}
			}else{
				financeiro = new PmpFinanceiro();
			}
			bean.toBean(financeiro);
			
			financeiro.setCondPag(bean.getSiglaCondicaoPagamento());
			financeiro.setMotivoSolicitacao(bean.getSiglaMotivoRequisicao());
			
			PmpContHorasStandard contHorasStandard = null;
			PmpContHorasStandardPlus contHorasStandardPlus = null;
			if(contrato.getIdClassificacaoContrato().getSigla().equals("PLUS")){
				contHorasStandardPlus = manager.find(PmpContHorasStandardPlus.class, bean.getIdContHorasStandard());
				manager.getTransaction().begin();
				contHorasStandardPlus.setIdStatusOs(idStatusOs);
				manager.merge(contHorasStandardPlus);
				manager.getTransaction().commit();
				financeiro.setIdContHorasStandardPlus(contHorasStandardPlus);
				//financeiro.getIdContHorasStandardPlus().setIdStatusOs(idStatusOs);
			}else{
				contHorasStandard = manager.find(PmpContHorasStandard.class, bean.getIdContHorasStandard());
				manager.getTransaction().begin();
				contHorasStandard.setIdStatusOs(idStatusOs);
				manager.merge(contHorasStandard);
				manager.getTransaction().commit();
				financeiro.setIdContHorasStandard(contHorasStandard);
				//financeiro.getIdContHorasStandard().setIdStatusOs(idStatusOs);
			}
			
			
//			if(contHorasStandard.getIdOsOperacional() != null){
//				bean.setNumeroOs(contHorasStandard.getIdOsOperacional().getNumOs());
//			}
			String SQL = "select FILIAL from PMP_CLIENTE_PL where SERIE = '"+contrato.getNumeroSerie()+"'";
			
			query = manager.createNativeQuery(SQL);
			Long filial = null;
			if(query.getResultList().size() > 0){
				filial = ((BigDecimal)query.getSingleResult()).longValue();
			}
			if(filial != null && filial == 99){
				filial = 0L;
			}
			
			TwFilial twFilial =null;
			if(filial != null){
				twFilial = manager.find(TwFilial.class, filial);
			}else{
				twFilial = manager.find(TwFilial.class, Long.valueOf(this.usuarioBean.getFilial()));
			}
			financeiro.setIdFuncionarioCriador(this.usuarioBean.getMatricula());
			
			financeiro.setFilial(Long.valueOf(Long.valueOf(this.usuarioBean.getFilial())));
			financeiro.setNomeFilial(twFilial.getStnm());
			
			financeiro.setCodCliente(contrato.getCodigoCliente());
			financeiro.setCliente(contrato.getRazaoSocial());
			
			
			String valor = bean.getVlrEstimado().replace(".", "");
			valor = valor.replace(",", ".");
			financeiro.setValorEstimado(BigDecimal.valueOf(Double.valueOf(valor)));
			
			String depositoValor = bean.getDepositoCliente().replace(".", "");
			depositoValor = depositoValor.replace(",", ".");
			financeiro.setDepositoCliente(BigDecimal.valueOf(Double.valueOf(depositoValor)));
			
			String VALORREQ = valor.replace(".", "");
			for (int i = VALORREQ.length();  i < 13; i++) {
				VALORREQ = "0"+VALORREQ;
			}
			String VALORDDEP =  bean.getDepositoCliente().replace(",", "");
			VALORDDEP = VALORDDEP.replace(".", "");
			for (int i = VALORDDEP.length();  i < 13; i++) {
				VALORDDEP = "0"+VALORDDEP;
			}
			con = ConectionDbs.getConnecton();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			prstmt = con.createStatement();
			
			if(((financeiro.getId() == null || financeiro.getId() == 0 ) && !status.equals("R")) || sequencia == null || sequencia.equals("")){
				GerarSequencia gerarSequencia = new GerarSequencia();
				manager.getTransaction().begin();
				gerarSequencia.setDescricao("TESTE");
				manager.persist(gerarSequencia);
				manager.getTransaction().commit();
				sequencia = gerarSequencia.getId().toString();
				
				manager.getTransaction().begin();
				manager.remove(gerarSequencia);
				manager.getTransaction().commit();
				
				int auxSequencia =  10 - ("CRD"+sequencia).length();
				
				for (int i = 0;  i < auxSequencia; i++) {
					sequencia = "0"+sequencia;
				}
				sequencia = "CRD"+sequencia;
//				manager.getTransaction().begin();
//				manager.merge(financeiro);
//				manager.getTransaction().commit();
				SQL = "INSERT into "+IConstantAccess.PESASAPARQ+".SOLCRDOS0 (CODEMP, NROREQ, CUNO, DTAREQ, INTUSR, MOTREQ, VALORREQ, VALORDDEP, CONDPAG, REOPEN1, REOPEN2) values ("+
				"'PESA','"+sequencia+"','"+contrato.getCodigoCliente()+"','"+dateFormat.format(new Date())+"','"+this.usuarioBean.getEstimateBy()+"','"+bean.getSiglaMotivoRequisicao()+"',"+BigDecimal.valueOf(Double.valueOf(valor))+","+BigDecimal.valueOf(Double.valueOf(depositoValor))+",'"+bean.getSiglaCondicaoPagamento()+"',"+
				"'"+((bean.getObservacao().length() > 50)?bean.getObservacao().substring(0,50):bean.getObservacao().substring(0,bean.getObservacao().length()))+"','"+((bean.getObservacao().length() > 50)?bean.getObservacao().substring(50,bean.getObservacao().length()):"")+"')";
				prstmt.executeUpdate(SQL);
			}else{
				//sequencia = financeiro.getCrdDbs();
				SQL = "UPDATE "+IConstantAccess.PESASAPARQ+".SOLCRDOS0 SET CODEMP = 'PESA'," +
						" CUNO = '"+contrato.getCodigoCliente()+"'," +
						" INTUSR = '"+this.usuarioBean.getEstimateBy()+"'," +
						" MOTREQ = '"+bean.getSiglaMotivoRequisicao()+"'," +
						" VALORREQ = "+BigDecimal.valueOf(Double.valueOf(valor))+"," +
						" VALORDDEP = "+BigDecimal.valueOf(Double.valueOf(depositoValor))+"," +
						" CONDPAG = '"+bean.getSiglaCondicaoPagamento()+"'," +
						" DTAREQ = '" +dateFormat.format(new Date())+"',"+
						" REOPEN1 = '" +((bean.getObservacao().length() > 50)?bean.getObservacao().substring(0,50):bean.getObservacao().substring(0,bean.getObservacao().length()))+"',"+
						" REOPEN2 = '" +((bean.getObservacao().length() > 50)?bean.getObservacao().substring(50,bean.getObservacao().length()):"")+"'," +
						" CRDSOL = 'R'," +
						" CRDAPR = ''," +
						" CRDRPV = ''"+
						" where NROREQ = '"+sequencia+"'" ;
				prstmt.executeUpdate(SQL);
				
			}
			manager.getTransaction().begin();
			financeiro.setCrdDbs(sequencia);
			financeiro.setIsFindCrdDbs("N");
			manager.merge(financeiro);
			manager.getTransaction().commit();
			this.sendEmailFinanceiro(bean.getObservacao()+"<br> Série:"+contrato.getNumeroSerie()+"<br> Código Cliente:"+financeiro.getCodCliente()+"<br> Cliente:"+financeiro.getCliente(), "Solicitação de Liberação Serviço PMP", contrato.getNumeroSerie());
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
	public List<StatusOsBean> findAllStatus(){
		EntityManager manager = null;
		
		List<StatusOsBean> result = new ArrayList<StatusOsBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("from PmpStatusOs");
			List<PmpStatusOs> statusOsList = query.getResultList();
			for (PmpStatusOs pmpStatusOs : statusOsList) {
				StatusOsBean bean = new StatusOsBean();
				bean.setId(pmpStatusOs.getId());
				bean.setDescricao(pmpStatusOs.getDescricao());
				bean.setSigla(pmpStatusOs.getSigla());
				result.add(bean);
			}
			
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
		return result;
	}	
	
	public boolean removerArquivoFinanceiro(Long idArquivo, String nomeArquivo) {
		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
		Properties prop = new Properties();

		try {
			prop.load(in);
			String dirFotos = prop.getProperty("dir.gestaoArquivos");

			File dir = new File(dirFotos + idArquivo);

			deleteFile(dir, nomeArquivo);
			return true;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void deleteFile(File dir, String nomeArquivo) {
		try {
			File file = new File(dir, nomeArquivo);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public Collection<String> findAllArquivos(Long idArquivo) {
		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
		Properties prop = new Properties();		

		Collection<String> listForm = new ArrayList<String>();

		try {
			prop.load(in);
			String dirFotos = prop.getProperty("dir.gestaoArquivos");		
			File dir = new File(dirFotos + idArquivo);

			if (dir.isDirectory()) {
				String[] children = dir.list();

				for (int i = 0; i < children.length; i++) { 
					listForm.add(children[i]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return listForm;
	}
	
	private boolean criarDiretorio(Long id) {
		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
		Properties prop = new Properties();
		
		try {
			prop.load(in);
			String dirFotos = prop.getProperty("dir.gestaoArquivos");
			File dir = new File(dirFotos + id);
			Boolean criou = dir.mkdirs();
			
			return criou;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Boolean fazerUploadEmDiretorioFinanceiro(Long idContHorasStandard, byte[] bytes, String nomeArquivo) throws Exception {
		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
		Properties prop = new Properties();

		try {
			prop.load(in);
			String diretorioFotos = prop.getProperty("dir.gestaoArquivos") + idContHorasStandard;

			if(!diretorioExiste(diretorioFotos)) {
				criarDiretorio(idContHorasStandard);
			}

			String caminhoCompletoArquivo = prop.getProperty("dir.gestaoArquivos") + idContHorasStandard+ "/" + UtilGestaoEquipamentosPesa.replaceCaracterEspecial(nomeArquivo);

			File arquivo = new File(caminhoCompletoArquivo);
			FileOutputStream fos = new FileOutputStream(arquivo);

			fos.write(bytes);
			fos.flush();
			fos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private Boolean diretorioExiste(String dir) {
		if ((new File(dir)).exists()) {
			return true;
		}
		return false;
	}
	
	public String verificarCreditoCliente(Long idContHorasStandard){
		EntityManager manager = null;
		Connection con = null;
		PreparedStatement prstmtCredito = null;
		PreparedStatement prstmtCreditoSap = null;
		ResultSet rsCredito = null;
		ResultSet rsCreditoSap = null;
		try {
			manager = JpaUtil.getInstance();
			con = ConectionDbs.getConnecton();
			PmpContHorasStandard standard = null;;
			PmpContHorasStandardPlus standardPlus = null;
			try {
				standard = manager.find(PmpContHorasStandard.class, idContHorasStandard);
			} catch (Exception e) {
			}
			try {
				standardPlus = manager.find(PmpContHorasStandardPlus.class, idContHorasStandard);
			} catch (Exception e) {
			}

			PmpContrato contrato = null;
			if(standard != null){
				contrato = standard.getIdContrato();
			}else{
				contrato = standardPlus.getIdContrato();
			}
			
			if(contrato.getIdTipoContrato().equals("REN")){
				return null;
			}
			
			String QUERY = "select * from PMP_CONTRATO_CUSTOMIZACAO ccus, PMP_TIPO_CUSTOMIZACAO tc, PMP_CONTRATO con"+
							"	where tc.DESCRICAO like 'COM MDA%'"+
							"	and ccus.ID_TIPO_CUSTOMIZACAO = tc.ID"+
							"	and con.ID = ccus.ID_CONTRATO"+
							"	and con.NUMERO_SERIE = '"+contrato.getNumeroSerie()+"'";
			Query query = manager.createNativeQuery(QUERY);
			if(query.getResultList().size() > 0){
				return null;
			}
			
			BigDecimal vlrEstimado = null;
			if(contrato.getIdClassificacaoContrato().getSigla().equals("PLUS")){
				String SQL = "select VALOR_ESTIMADO from PMP_FINANCEIRO where ID = ("+
						" select MAX(id) from PMP_FINANCEIRO where ID_CONT_HORAS_STANDARD_PLUS = "+standardPlus.getId()+" and DATA_LIBERACAO is not null and STATUS = 'L')";
				query = manager.createNativeQuery(SQL);
				if(query.getResultList().size() > 0){
					vlrEstimado = (BigDecimal)query.getSingleResult();
				}
			}else{
				String SQL = "select valor_liberado from PMP_FINANCEIRO where ID = ("+
						" select MAX(id) from PMP_FINANCEIRO where ID_CONT_HORAS_STANDARD = "+standard.getId()+" and DATA_LIBERACAO is not null and STATUS = 'L')";
				query = manager.createNativeQuery(SQL);
				if(query.getResultList().size() > 0){
					vlrEstimado = (BigDecimal)query.getSingleResult();
				}
			}

			BigDecimal total = BigDecimal.ZERO;
			if(vlrEstimado == null){
				Double LIMITE = 0D;
				String SQL = "";
				if(standard != null){
					SQL = "select t.CUNO, t.CRLMT, t.TERMCD from "+IConstantAccess.LIB_DBS+".cipname0 t where t.CUNO = '"+standard.getIdContrato().getCodigoCliente()+"'";
				}else if(standardPlus != null){
					SQL = "select t.CUNO, t.CRLMT, t.TERMCD from "+IConstantAccess.LIB_DBS+".cipname0 t where t.CUNO = '"+standardPlus.getIdContrato().getCodigoCliente()+"'";
				}
				prstmtCredito = con.prepareStatement(SQL);
				rsCredito = prstmtCredito.executeQuery();
				if(rsCredito.next()){
					//if(rsCredito.getString("TERMCD").equals("1")){
						if(standard != null){
							SQL = "select t.LIMITE from "+IConstantAccess.PESASAPARQ+".SAPCRDLMT0 t where t.CUNO = '"+standard.getIdContrato().getCodigoCliente()+"'";
							total = standard.getCusto();
						}else if(standardPlus != null){
							SQL = "select t.LIMITE from "+IConstantAccess.PESASAPARQ+".SAPCRDLMT0 t where t.CUNO = '"+standardPlus.getIdContrato().getCodigoCliente()+"'";
							total = standardPlus.getCusto();
						}	
						prstmtCreditoSap = con.prepareStatement(SQL);
						rsCreditoSap = prstmtCreditoSap.executeQuery();
						if(rsCreditoSap.next()){
							LIMITE = Double.valueOf(String.valueOf(rsCreditoSap.getString("LIMITE")));
						}
						if(total != null && LIMITE < total.doubleValue() || rsCredito.getString("TERMCD").equals("1")){
							return "O limite de crédito é menor que o valor da manutenção!\nLimite = "+ValorMonetarioHelper.formata("###,###,##0.00", LIMITE)+"\nValor manutenção = "+ValorMonetarioHelper.formata("###,###,##0.00", total.doubleValue());
						}else{
							return null;
						}
					//}
				}
			}else{
				if(standard != null){
					total = standard.getCusto();
					if(standard.getIdStatusOs().getSigla().equals("RF")){
						return "Rejeitado";
					}
				}else if(standardPlus != null){
					total = standardPlus.getCusto();
					if(standardPlus.getIdStatusOs().getSigla().equals("RF")){
						return "Rejeitado";
					}
				}
				if(total != null && vlrEstimado.doubleValue() < total.doubleValue()){
					return "O limite de crédito é menor que o valor da manutenção!\nLimite = "+ValorMonetarioHelper.formata("###,###,##0.00", vlrEstimado.doubleValue())+"\nValor manutenção = "+ValorMonetarioHelper.formata("###,###,##0.00", total.doubleValue());
				}else{
					return null;
				}
			}


		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}	
		}
		return null;
	}
	public FinanceiroBean findObsFinanceiro(Long idContHorasStandard){
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat dateFormatBanco = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			PmpContHorasStandard standard = null;;
			PmpContHorasStandardPlus standardPlus = null;
			FinanceiroBean bean = new FinanceiroBean();
			try {
				standard = manager.find(PmpContHorasStandard.class, idContHorasStandard);
			} catch (Exception e) {
			}
			try {
				standardPlus = manager.find(PmpContHorasStandardPlus.class, idContHorasStandard);
			} catch (Exception e) {
			}
			
			PmpContrato contrato = null;
			if(standard != null){
				contrato = standard.getIdContrato();
				if(standard.getIdStatusOs() != null){
					bean.setIdStatusFinanceiro(standard.getIdStatusOs().getId());
				}
				if(standard.getCusto() != null){
					bean.setValorManutencaoStd(ValorMonetarioHelper.formata("###,###,##0.00", standard.getCusto().doubleValue()));
				}
			}else{
				contrato = standardPlus.getIdContrato();
				if(standardPlus.getIdStatusOs() != null){
					bean.setIdStatusFinanceiro(standardPlus.getIdStatusOs().getId());
				}
				if(standardPlus.getCusto() != null){
					bean.setValorManutencaoStd(ValorMonetarioHelper.formata("###,###,##0.00", standardPlus.getCusto().doubleValue()));
				}
			}
			
			
			Query query = manager.createNativeQuery("select CONVERT(varchar(4000), OBSERVACAO) observacao, " +
					"CONVERT(varchar(4000), OBSERVACAO_LIBERACAO) OBSERVACAO_LIBERACAO," +
					" DATA_LIBERACAO," +
					" VALOR_LIBERADO," +
					" (select eplsnm from tw_funcionario where epidno = id_funcionario) nomeFunconarioLiberacao," +
					" DATA_REJEICAO,"+
					" VALOR_ESTIMADO,"+
					" DEPOSITO_CLIENTE," +
					" CRD_DBS," +
					" COND_PAG," +
					" MOTIVO_SOLICITACAO"+
					"  from PMP_FINANCEIRO " +
					" where ID = (select MAX(ID) from PMP_FINANCEIRO" +
					" where (ID_CONT_HORAS_STANDARD = "+idContHorasStandard+" or ID_CONT_HORAS_STANDARD_PLUS = "+idContHorasStandard+"))" );
			if(query.getResultList().size() > 0){
				Object[] pair = (Object[])query.getSingleResult();
				

				
				if(standard != null){
					contrato = standard.getIdContrato();
					bean.setIdStatusFinanceiro(standard.getIdStatusOs().getId());
					if(standard.getCusto() != null){
						bean.setValorManutencaoStd(ValorMonetarioHelper.formata("###,###,##0.00", standard.getCusto().doubleValue()));
					}
				}else{
					contrato = standardPlus.getIdContrato();
					bean.setIdStatusFinanceiro(standardPlus.getIdStatusOs().getId());
					if(standardPlus.getCusto() != null){
						bean.setValorManutencaoStd(ValorMonetarioHelper.formata("###,###,##0.00", standardPlus.getCusto().doubleValue()));
					}
				}
				
				bean.setStatus(standard.getIdStatusOs().getSigla());
				
				bean.setObservacao((String)pair[0]);
				bean.setObservacaoLiberacao((String)pair[1]);
				if(pair[2] != null){
					bean.setDataLiberacao(dateFormat.format(dateFormatBanco.parse((String)pair[2])));
				}
				if(pair[3] != null){
					bean.setValorLiberado(ValorMonetarioHelper.formata("###,###,##0.00", ((BigDecimal)pair[3]).doubleValue()));
				}
				bean.setFuncionarioLiberacao((String)pair[4]);
				if(pair[5] != null){
					bean.setDataRejeicao(dateFormat.format(dateFormatBanco.parse((String)pair[5])));
				}
				if(pair[6] != null){
					bean.setVlrEstimado(ValorMonetarioHelper.formata("###,###,##0.00", ((BigDecimal)pair[6]).doubleValue()));
				}
				
				if(pair[7] != null){
					bean.setDepositoCliente(ValorMonetarioHelper.formata("###,###,##0.00", ((BigDecimal)pair[7]).doubleValue()));
				}
				bean.setCrdDbs((String)pair[8]);
				bean.setSiglaCondicaoPagamento((String)pair[9]);
				bean.setSiglaMotivoRequisicao((String)pair[10]);
			}
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
	public List<FinanceiroBean> findLiberacaoCredito(String pesquisa){
		EntityManager manager = null;
		List<FinanceiroBean> result = new ArrayList<FinanceiroBean>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormatBR = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createNativeQuery("select f.ID, func.EPLSNM, f.NUMERO_OS, f.COD_CLIENTE, f.CLIENTE, f.DATA dataEnvio,f.NOME_FILIAL, convert(varchar(4000),f.OBSERVACAO) OBSERVACAO, f.ID_CONT_HORAS_STANDARD_PLUS, f.ID_CONT_HORAS_STANDARD, f.VALOR_ESTIMADO, f.FUNCIONARIO_RESPONSAVEL " +
					" from PMP_FINANCEIRO f, TW_FUNCIONARIO func" + 
					" where f.ID_FUNCIONARIO_CRIADOR = func.EPIDNO" +
					" and (f.CLIENTE like '%"+pesquisa+"%' or f.COD_CLIENTE like '%"+pesquisa+"%' or f.NUMERO_OS like '%"+pesquisa+"%')"+
					" and f.DATA_LIBERACAO is null"+
					" and f.data_rejeicao is null" );
			
			if(query.getResultList().size() > 0){
				List<Object[]> pairList = query.getResultList();
				for (Object[] pair : pairList) {
					FinanceiroBean bean = new FinanceiroBean();
					bean.setId(((BigDecimal)pair[0]).longValue());
					bean.setNomeFuncionario((String)pair[1]);
					bean.setNumeroOs((String)pair[2]);
					bean.setCodCliente((String)pair[3]);
					bean.setCliente((String)pair[4]);
					bean.setData(dateFormatBR.format(dateFormat.parse((String)pair[5])));
					bean.setNomeFilial((String)pair[6]);
					bean.setObservacao((String)pair[7]);
					if(pair[8] !=null){
						bean.setIdContHorasStandard(((BigDecimal)pair[8]).longValue());
					}else{
						bean.setIdContHorasStandard(((BigDecimal)pair[9]).longValue());
					}
					if(pair[10] != null){
						bean.setVlrEstimado(ValorMonetarioHelper.formata("###,###,##0.00",((BigDecimal)pair[10]).longValue()));
					}
					bean.setNomeFuncionarioResponsavel((String)pair[11]);
					result.add(bean);
				}
			}
			
			
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
		return result;
	}	
	
	public Boolean aprovarFinanceiro(Long idFinanceiro, String obs, String depositoCliente, String TERMCD){
		EntityManager manager = null;
		ResultSet rsCreditoSap = null;
		PreparedStatement prstmtCreditoSap = null;
		Connection con = null;
		try {
			manager = JpaUtil.getInstance();
			con = ConectionDbs.getConnecton(); 
			PmpFinanceiro financeiro = manager.find(PmpFinanceiro.class, idFinanceiro);
			Query query = manager.createQuery("from PmpStatusOs where sigla = 'LF'");
			PmpStatusOs pmpFinanceiro = (PmpStatusOs)query.getSingleResult();
			
			
			manager.getTransaction().begin();
			financeiro.setDataLiberacao(new Date());
			String serie = "";
			if(financeiro.getIdContHorasStandard() != null){
				financeiro.getIdContHorasStandard().setIdStatusOs(pmpFinanceiro);
				serie = financeiro.getIdContHorasStandard().getIdContrato().getNumeroSerie();
			}else{
				financeiro.getIdContHorasStandardPlus().setIdStatusOs(pmpFinanceiro);
				serie = financeiro.getIdContHorasStandardPlus().getIdContrato().getNumeroSerie();
			}
			financeiro.setObservacaoLiberacao(obs);
			financeiro.setIdFuncionario(this.usuarioBean.getMatricula());
			financeiro.setStatus("L");
			financeiro.setTermsCod(TERMCD);
			financeiro.setIsFindCrdDbs("S");
						
			//String SQL = "select t.LIMITE from "+IConstantAccess.PESASAPARQ+".SAPCRDLMT0 t where t.CUNO = '"+financeiro.getCodCliente()+"'";
			String SQL = "select valorreq from "+IConstantAccess.PESASAPARQ+".SOLCRDOS0 where nroreq = '"+financeiro.getCrdDbs()+"'";
			prstmtCreditoSap = con.prepareStatement(SQL);
			rsCreditoSap = prstmtCreditoSap.executeQuery();
			if(rsCreditoSap.next()){
				financeiro.setValorLiberado(BigDecimal.valueOf(Double.valueOf(String.valueOf(rsCreditoSap.getString("valorreq")))));
			}
			
			manager.merge(financeiro);
			manager.getTransaction().commit();
			this.sendMailOperacional(financeiro.getIdFuncionarioCriador(), "Cliente/Código: "+financeiro.getCliente()+"/"+financeiro.getCodCliente()+" Série : "+serie+"." +obs);
			return true;
		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			try {
				if(manager != null && manager.isOpen()){
					manager.close();
				}	
				if(con != null){
					con.close();
					prstmtCreditoSap.close();
					prstmtCreditoSap.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	public Boolean rejeitarFinanceiro(Long idFinanceiro, String obs){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			PmpFinanceiro financeiro = manager.find(PmpFinanceiro.class, idFinanceiro);
			Query query = manager.createQuery("from PmpStatusOs where sigla = 'RF'");
			PmpStatusOs pmpFinanceiro = (PmpStatusOs)query.getSingleResult();
			
			
			manager.getTransaction().begin();
			financeiro.setDataRejeicao(new Date());
			String serie = "";
			
			if(financeiro.getIdContHorasStandard() != null){
				PmpContHorasStandard contHorasStandard = financeiro.getIdContHorasStandard();
				contHorasStandard.setIdStatusOs(pmpFinanceiro);
				manager.merge(contHorasStandard);
				
				serie = financeiro.getIdContHorasStandard().getIdContrato().getNumeroSerie();
				
			}else{
				PmpContHorasStandardPlus contHorasStandard = financeiro.getIdContHorasStandardPlus();
				contHorasStandard.setIdStatusOs(pmpFinanceiro);
				manager.merge(contHorasStandard);
				serie = financeiro.getIdContHorasStandardPlus().getIdContrato().getNumeroSerie();
			}
			financeiro.setObservacaoLiberacao(obs);
			financeiro.setIdFuncionario(this.usuarioBean.getMatricula());
			financeiro.setStatus("R");
			financeiro.setIsFindCrdDbs("S");
			
			manager.merge(financeiro);
			manager.getTransaction().commit();
			this.sendMailOperacional(financeiro.getIdFuncionarioCriador(), "Cliente/Código: "+financeiro.getCliente()+"/"+financeiro.getCodCliente()+" Série : "+serie+"." +obs);
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
	public void sendMailOperacional(String epidno, String msg){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createNativeQuery("select EMAIL from TW_FUNCIONARIO where EPIDNO = '"+epidno+"'");
			String email = (String)query.getSingleResult();
			new EmailHelper().sendSimpleMail(msg, "Análise Financeira de serviço PMP", email);
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
	}
	
	public void sendEmailFinanceiro(String obs, String subject, String serie){
//		String SQL = "select f.EPLSNM, f.EMAIL from TW_FUNCIONARIO f, ADM_PERFIL_SISTEMA_USUARIO psu"+
//		"	where f.EPIDNO = psu.ID_TW_USUARIO"+
//		"	and psu.ID_PERFIL IN (select ID from ADM_PERFIL where TIPO_SISTEMA = 'SC' and sigla in ('FIN'))";
//		EntityManager manager = null;
//		try {
//			manager = JpaUtil.getInstance();
//			Query query = manager.createNativeQuery(SQL);
//			List<Object[]> emailList = query.getResultList();
//			for (Object[] pair : emailList) {
				new EmailHelper().sendSimpleMail(obs, subject, geEmail(serie));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if(manager != null && manager.isOpen()){
//				manager.close();
//			}	
//		}	
	}
	
	/**
	 * 
	 * @param serie
	 * @return
	 */
	public String geEmail(String serie){
		EntityManager manager = null;
		
		try {
			String SQL = "select FILIAL from PMP_CLIENTE_PL where SERIE = '"+serie+"'";
			manager = JpaUtil.getInstance();
			Query query = manager.createNativeQuery(SQL);
			Long filial = null;
			if(query.getResultList().size() > 0){
				filial = ((BigDecimal)query.getSingleResult()).longValue();
			}
			if(filial != null){
				if(filial == 0 || filial == 14 || filial == 13 || filial == 19 || filial == 7){
					return "galo_giovanni@pesa.com.br";
				}
				if(filial == 11 || filial == 10 || filial == 15){
					return "galo_giovanni@pesa.com.br";
				}
//				if(filial == 39 || filial == 40 || filial == 41 || filial == 42 || filial == 35 || filial == 36 || filial == 37){
//					return "";
//				}
				if(filial == 30 || filial == 33 || filial == 32){
					return "galo_giovanni@pesa.com.br";
				}
				if(filial == 25 || filial == 21 || filial == 23 || filial == 27){
					return "galo_giovanni@pesa.com.br";
				}
				
			}
			
		} catch (Exception e) {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			new EmailHelper().sendSimpleMail("Erro "+writer.toString(), "Erro ao enviar e-mail Financeiro Pmp", "rodrigo@rdrsistemas.com.br");
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return "";
	}
}
