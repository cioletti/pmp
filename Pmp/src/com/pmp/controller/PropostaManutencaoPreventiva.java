package com.pmp.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import com.pmp.bean.ApropriacaoHoras;
import com.pmp.bean.ConfigurarCustomizacaoBean;
import com.pmp.bean.IntervencaoBean;
import com.pmp.bean.PecaBean;
import com.pmp.bean.RevisaoPecasBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.business.ContratoBusiness;
import com.pmp.entity.PmpAgendamento;
import com.pmp.entity.PmpConfigOperacional;
import com.pmp.entity.PmpContHorasStandard;
import com.pmp.entity.PmpContrato;
import com.pmp.entity.PmpContratoCustomizacao;
import com.pmp.entity.PmpDescontoPdp;
import com.pmp.entity.PmpTipoCustomizacao;
import com.pmp.entity.TwFilial;
import com.pmp.util.ConectionDbs;
import com.pmp.util.IConstantAccess;
import com.pmp.util.JpaUtil;
import com.pmp.util.ValorMonetarioHelper;

/**
 * Servlet implementation class PropostaManutencaoPreventiva
 */
public class PropostaManutencaoPreventiva extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PropostaManutencaoPreventiva() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idContHorasStandard = request.getParameter("idContHorasStandard");
		

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		java.sql.PreparedStatement stm = null;
		EntityManager manager = null;
		try {
			JasperReport jasperReport = null;  
			JasperPrint pdfProposta = null;    
			manager = JpaUtil.getInstance();

			//Obtem o caminho do .jasper  
			ServletContext servletContext = super.getServletContext(); 
			String caminhoJasper = "";
			String pathSubreport = "";
			//UsuarioBean bean = (UsuarioBean) request.getSession().getAttribute("usuario");

			caminhoJasper = servletContext.getRealPath("WEB-INF/porpostaManutencaoNaoRealizada/PropostaPmp.jasper"); 
			pathSubreport = servletContext.getRealPath("WEB-INF/porpostaManutencaoNaoRealizada/")+"/";
			Query query = manager.createQuery("From PmpContHorasStandard c where c.id=:id order by horasManutencao asc");
			query.setParameter("id",Long.valueOf(idContHorasStandard));
			
			PmpContHorasStandard standard = (PmpContHorasStandard)query.getSingleResult(); 
			PmpContrato contrato = standard.getIdContrato();
			TwFilial filial = manager.find(TwFilial.class, contrato.getFilial().longValue());
			
			
			
//			query = manager.createQuery("From PmpContratoCustomizacao c where c.idContrato.id =:idContrato");
//			query.setParameter("idContrato",contrato.getId());
//			
//			
//			List<PmpContratoCustomizacao> customizacaoList =  query.getResultList();
			
			
//				for (PmpContratoCustomizacao bean : customizacaoList) {
//						query = manager.createNativeQuery("select sc.sigla_customizacao from PMP_SIGLA_CUSTOMIZACAO sc, PMP_CONFIG_CUSTOMIZACAO cc"+
//													" where cc.ID_TIPO_CUSTOMIZACAO =:ID_TIPO_CUSTOMIZACAO"+
//													" and sc.id_config_customizacao = cc.id");
//						query.setParameter("ID_TIPO_CUSTOMIZACAO", bean.getIdTipoCustomizacao().getId());
//						List<String> sgTipoCustList = query.getResultList();
//						for (String string : sgTipoCustList) {
//							siglaCustomizacao += "'"+string+"',";
//						}
//				}
//			
//			if(siglaCustomizacao.length() > 0){
//				siglaCustomizacao = siglaCustomizacao.substring(0, siglaCustomizacao.length() - 1);
//			}else{
//				siglaCustomizacao = "'-null'";
//			}
			
			
			String complementoSigla = "";
			String complemento = "";
			String siglaCustomizacao = "";
//			String complementoCustPart = "";
//			if(contrato.getIdClassificacaoContrato().getSigla().equals("CUS") || contrato.getIdClassificacaoContrato().getSigla().equals("CUSLIGHT") || contrato.getIdClassificacaoContrato().getSigla().equals("PART")){
//				complementoCustPart = " and m.OJBLOC <> 'CST'";
//			}
			query = manager
					.createNativeQuery("select ID_TIPO_CUSTOMIZACAO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_TIPO_CUSTOMIZACAO tc"
							+ " where cc.ID_CONTRATO = "
							+ contrato.getId()+ " and cc.ID_TIPO_CUSTOMIZACAO = tc.ID");

			if (query.getResultList().size() > 0) {
				List<BigDecimal> idTipoCustomizacaoList = query.getResultList();
				for (BigDecimal idTipoCustomizacao : idTipoCustomizacaoList) {
					query = manager
							.createNativeQuery("select sc.sigla_customizacao from PMP_SIGLA_CUSTOMIZACAO sc, PMP_CONFIG_CUSTOMIZACAO cc"
									+ " where cc.ID_TIPO_CUSTOMIZACAO =:ID_TIPO_CUSTOMIZACAO"
									+ " and sc.id_config_customizacao = cc.id");
					query.setParameter("ID_TIPO_CUSTOMIZACAO",idTipoCustomizacao.intValue());
					List<String> sgTipoCustList = query.getResultList();
					for (String string : sgTipoCustList) {
						siglaCustomizacao += "'" + string + "',";
					}
				}
			}
			
			if(siglaCustomizacao.length() > 0){
				siglaCustomizacao = siglaCustomizacao.substring(0, siglaCustomizacao.length() -1);
			}else{
				siglaCustomizacao = "'-1'";
			}

			
			query = manager
					.createNativeQuery("select COUNT(*) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = "+contrato.getId());

			Integer totalRevisao =(Integer)query.getSingleResult();
			Double valorParcelas = 0D;
			if(contrato.getValorSugerido() != null && contrato.getValorSugerido().doubleValue() < contrato.getValoContrato().doubleValue()){
				valorParcelas = contrato.getValoContrato().doubleValue() - contrato.getValorSugerido().doubleValue();
				if(totalRevisao.intValue() > 0){
					valorParcelas = valorParcelas / totalRevisao.intValue() ;
				}
			}
			
//			
			
			Double custo = 0D;
			ContratoBusiness business = new ContratoBusiness();
			
			String retirarPeca = "";
			String sosDPDP400 = "";
			String sosSemDPDP400 = "";
			String sosDPDP000 = "";
			String sosSemDPDP000 = "";
				if(contrato.getIdClassificacaoContrato().getSigla().equals("PRE")){
					sosDPDP400 = " and m.sos <> '400' ";
					sosSemDPDP400 = " and m.sos = '400' ";
					sosDPDP000 = " and m.sos <> '000' and m.sos <> '400' ";
					sosSemDPDP000 = " and m.sos = '000' ";
					
					retirarPeca +=  " and ojbloc not in ("+siglaCustomizacao+") ";
					retirarPeca +=  " and ocptmd not in ("+siglaCustomizacao+") ";
					retirarPeca +=  " and JWKAPP not in ("+siglaCustomizacao+") ";
					
					custo = business.calcularValorRevisaoPremium(contrato, standard, manager, siglaCustomizacao);
					BigDecimal valorParcelasDiluido = BigDecimal.valueOf(valorParcelas).divide(BigDecimal.valueOf(2));
					
					standard.setCustoPecas(standard.getCustoPecas().add((valorParcelasDiluido)));
					standard.setCustoMo(standard.getCustoMo().add((valorParcelasDiluido)));
					
				} else if(contrato.getIdClassificacaoContrato().getSigla().equals("CUS")){
					retirarPeca = " and m.OJBLOC <> 'CST'";
					retirarPeca +=  " and ojbloc not in ("+siglaCustomizacao+") ";
					retirarPeca +=  " and ocptmd not in ("+siglaCustomizacao+") ";
					retirarPeca +=  " and JWKAPP not in ("+siglaCustomizacao+") ";
					
					sosDPDP400 = " and m.sos <> '400' ";
					sosSemDPDP400 = " and m.sos = '400' ";
					sosDPDP000 = " and m.sos <> '000' ";
					sosSemDPDP000 = " and m.sos = '000' ";
					
					
					custo = business.calcularValorRevisaoCustomer(contrato, standard, manager, siglaCustomizacao);
					standard.setCustoPecas(standard.getCustoPecas().add(BigDecimal.valueOf(valorParcelas)));
				} else if(contrato.getIdClassificacaoContrato().getSigla().equals("PART")){
					if(!standard.getIsPartner().equals("N")){
						query = manager.createNativeQuery("select descricao from Pmp_Comp_Code_Partner");
						List<String> descricao = (List<String>)query.getResultList();
						for (String cptcd : descricao) {
							if(standard.getStandardJobCptcd().equals(cptcd)){
								retirarPeca = " and m.OJBLOC <> 'CST'";
							}
						}
						retirarPeca +=  " and ojbloc not in ("+siglaCustomizacao+") ";
						retirarPeca +=  " and ocptmd not in ("+siglaCustomizacao+") ";
						retirarPeca +=  " and JWKAPP not in ("+siglaCustomizacao+") ";
					}
					
					sosDPDP400 = " and m.sos <> '400' ";
					sosSemDPDP400 = " and m.sos = '400' ";
					sosDPDP000 = " and m.sos <> '000' ";
					sosSemDPDP000 = " and m.sos = '000' ";
					
					custo = business.calcularValorRevisaoPartner(contrato, standard, manager, siglaCustomizacao, retirarPeca);
					if(standard.getIsPartner() != null &&  standard.getIsPartner().equals("S")){
						standard.setCustoPecas(standard.getCustoPecas().add(BigDecimal.valueOf(valorParcelas)));
					}else{
						BigDecimal valorParcelasDiluido = BigDecimal.valueOf(valorParcelas).divide(BigDecimal.valueOf(2));

						standard.setCustoPecas(standard.getCustoPecas().add((valorParcelasDiluido)));
						standard.setCustoMo(standard.getCustoMo().add((valorParcelasDiluido)));
					}

				}else if(contrato.getIdClassificacaoContrato().getSigla().equals("CUSLIGHT")){
					custo = business.calcularValorRevisaoCustomerLight(contrato, standard, manager, siglaCustomizacao);
					
					sosDPDP400 = " and m.sos <> '400' and m.sos <> '050' ";
					sosSemDPDP400 = " and m.sos = '400' ";
					sosDPDP000 = " and m.sos <> '000' and m.sos <> '050' and m.sos <> '400'";
					sosSemDPDP000 = " and m.sos = '000' and m.sos <> '050' ";
					
					
					retirarPeca = " and m.OJBLOC <> 'CST'";
					retirarPeca +=  " and ojbloc not in ("+siglaCustomizacao+") ";
					retirarPeca +=  " and ocptmd not in ("+siglaCustomizacao+") ";
					retirarPeca +=  " and JWKAPP not in ("+siglaCustomizacao+") ";
				}
				if(custo != null){
					standard.setCusto(BigDecimal.valueOf(custo + valorParcelas));
				}
			//standard.setCusto(BigDecimal.valueOf(custo));
			
			
			

			if (siglaCustomizacao.length() > 0) {
				complementoSigla = " and ojbloc not in (" + siglaCustomizacao
						+ ")";
				complementoSigla += " and ocptmd not in (" + siglaCustomizacao
						+ ")";
				complementoSigla += " and JWKAPP not in (" + siglaCustomizacao
				+ ")";
			}
			if (contrato.getIdConfigTracao() != null) {
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "
						+ contrato.getIdConfigTracao().getId()
						+ ") or ocptmd is null)"
						+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "
						+ contrato.getIdConfigTracao().getId()
						+ ") or ojbloc is null)"
						+ " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or JWKAPP is null)";
			}
			List<RevisaoPecasBean> revisaoPecasList = new ArrayList<RevisaoPecasBean>();
			List<PecaBean> pecasBeanList = new ArrayList<PecaBean>();
//			query = manager
//					.createNativeQuery(" select convert( varchar(10),HORAS_MANUTENCAO) +' HORAS', STANDARD_JOB_CPTCD, HORAS_MANUTENCAO from PMP_CONT_HORAS_STANDARD"
//							+ " where ID_CONTRATO     = "
//							+ contrato.getId()
//							+ " order by HORAS_MANUTENCAO  ");
//			if (query.getResultList().size() > 0) {
//				List<Object[]> result = query.getResultList();
//				for (Object[] horasCompcode : result) {
//					if (contrato.getIdTipoContrato() != null
//							&& contrato.getIdTipoContrato().getSigla()
//									.equals("VEN")
//							&& contrato.getIdStatusContrato().getSigla()
//									.equals("CA")) {
//						if (contrato.getPrintRevisaoPosPago().longValue() <  ((BigDecimal)horasCompcode[2]).longValue()) {
//							break;
//						}
//					}
			BigDecimal custoPecas = BigDecimal.ZERO;
			BigDecimal descontoPecas = standard.getIdContrato().getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp();
			BigDecimal descontoPecasPdpSpot = standard.getIdContrato().getDescontoPdpSpot();
					
			query = manager.createQuery("from PmpDescontoPdp");
			List<PmpDescontoPdp> pdpList = query.getResultList();
			String siglaDescontoPdp = "";
			for (PmpDescontoPdp pmpDescontoPdp : pdpList) {
				siglaDescontoPdp += "'"+pmpDescontoPdp.getDescricao()+"',";
			}
			if(siglaDescontoPdp.length() > 0){
				siglaDescontoPdp = siglaDescontoPdp.substring(0, siglaDescontoPdp.length() -1);
			}
					RevisaoPecasBean revisaoPecas = new RevisaoPecasBean();
					revisaoPecas.setHoras(standard.getHorasManutencao().toString());
					query = manager
							.createNativeQuery("select m.PANO20, m.DLRQTY, m.DS18, m.UNLS from pmp_manutencao m"
									+ " where m.cptcd = '"
									+ standard.getStandardJobCptcd()
									+ "'"
									+ " and m.bgrp = 'PM'"
									//+ " and m.sos <> '050'"
									//+ " and m.sos <> '400' "
									+ "  and substring(m.beqmsn,1,4) = '"
									+ contrato.getPrefixo()
									+ "'"
									+ sosDPDP400
									+ retirarPeca
									+ complemento
									+ complementoSigla
									+ " and substring(m.beqmsn,5,10) between '"
									+ contrato.getBeginRanger().substring(4, 9)
									+ "' and '"
									+ contrato.getEndRanger().substring(4, 9)+"'" +
									" and m.BECTYC in ("+siglaDescontoPdp+")");
					if (query.getResultList().size() > 0) {
						List<Object[]> resultPecas = query.getResultList();
						for (Object[] pecas : resultPecas) {
							
							PecaBean peca = new PecaBean();
							peca.setPano20((String) pecas[0]);
							peca.setDlrqty(((BigDecimal)pecas[1]).intValue());
							peca.setDs18((String) pecas[2]);
							peca.setPreco((BigDecimal)pecas[3]);
							pecasBeanList.add(peca);
							
							if(descontoPecas.equals(0.00)){
								custoPecas = custoPecas.add(peca.getPreco());
							}else{
								BigDecimal valorPeca = peca.getPreco();
								if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
									peca.setPreco(valorPeca.subtract(valorPeca.multiply(descontoPecas.divide(BigDecimal.valueOf(100.0)))));
								}else if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
									peca.setPreco(valorPeca.subtract(valorPeca.multiply(descontoPecasPdpSpot.divide(BigDecimal.valueOf(100.0)))));
								}else{
									peca.setPreco(valorPeca);
								}
								custoPecas = custoPecas.add( peca.getPreco().multiply(BigDecimal.valueOf(peca.getDlrqty())) );
							}
						}
					}
					
					query = manager
					.createNativeQuery("select m.PANO20, m.DLRQTY, m.DS18, m.UNLS from pmp_manutencao m"
							+ " where m.cptcd = '"
							+ standard.getStandardJobCptcd()
							+ "'"
							//+ " and m.sos <> '050'"
							//+ " and m.sos = '400' "
							+ " and m.bgrp = 'PM'"
							+ "  and substring(m.beqmsn,1,4) = '"
							+ contrato.getPrefixo()
							+ "'"
							+ sosSemDPDP400
							+ retirarPeca
							+ complemento
							+ complementoSigla
							+ " and substring(m.beqmsn,5,10) between '"
							+ contrato.getBeginRanger().substring(4, 9)
							+ "' and '"
							+ contrato.getEndRanger().substring(4, 9)+"'" );
							//" and (m.BECTYC not in ("+siglaDescontoPdp+") or m.bectyc is null)");
			if (query.getResultList().size() > 0) {
				List<Object[]> resultPecas = query.getResultList();
				for (Object[] pecas : resultPecas) {
					
					PecaBean peca = new PecaBean();
					peca.setPano20((String) pecas[0]);
					peca.setDlrqty(((BigDecimal)pecas[1]).intValue());
					peca.setDs18((String) pecas[2]);
					peca.setPreco((BigDecimal)pecas[3]);
					pecasBeanList.add(peca);
					custoPecas = custoPecas.add(peca.getPreco().multiply(BigDecimal.valueOf(peca.getDlrqty())));
				}
			}
					
						query = manager
								.createNativeQuery("select m.PANO20, m.DLRQTY, m.DS18, m.UNLS from pmp_manutencao m"
										+ " where m.cptcd = '"
										+ standard.getStandardJobCptcd()
										+ "'"
										//+ " and m.sos <> '000'"
										//+ " and m.sos <> '050' "
										+ " and m.bgrp = 'PM'"
										+ "  and substring(m.beqmsn,1,4) = '"
										+ contrato.getPrefixo()
										+ "'"
										+ sosDPDP000
										+ retirarPeca
										+ complemento
										+ complementoSigla
										+ " and substring(m.beqmsn,5,10) between '"
										+ contrato.getBeginRanger().substring(4, 9)
										+ "' and '"
										+ contrato.getEndRanger().substring(4, 9)+"'" +
										" and (m.BECTYC not in ("+siglaDescontoPdp+") or m.bectyc is null)");
						if (query.getResultList().size() > 0) {
							List<Object[]> resultPecas = query.getResultList();
							for (Object[] pecas : resultPecas) {
								
								PecaBean peca = new PecaBean();
								peca.setPano20((String) pecas[0]);
								peca.setDlrqty(((BigDecimal)pecas[1]).intValue());
								peca.setDs18((String) pecas[2]);
								peca.setPreco((BigDecimal)pecas[3]);
								pecasBeanList.add(peca);
								custoPecas = custoPecas.add(peca.getPreco().multiply(BigDecimal.valueOf(peca.getDlrqty())));
							}
						}
						query = manager
						.createNativeQuery("select m.PANO20, m.DLRQTY, m.DS18, m.UNLS from pmp_manutencao m"
								+ " where m.cptcd = '"
								+ standard.getStandardJobCptcd()
								+ "'"
								//+ " and m.sos = '000'"
								//+ " and m.sos <> '050' "
								+ " and m.bgrp = 'PM'"
								+ "  and substring(m.beqmsn,1,4) = '"
								+ contrato.getPrefixo()
								+ "'"
								+ sosSemDPDP000
								+ retirarPeca
								+ complemento
								+ complementoSigla
								+ " and substring(m.beqmsn,5,10) between '"
								+ contrato.getBeginRanger().substring(4, 9)
								+ "' and '"
								+ contrato.getEndRanger().substring(4, 9)+"'" +
								" and (m.BECTYC not in ("+siglaDescontoPdp+") or m.bectyc is null)");
						if (query.getResultList().size() > 0) {
							List<Object[]> resultPecas = query.getResultList();
							for (Object[] pecas : resultPecas) {
								
								PecaBean peca = new PecaBean();
								peca.setPano20((String) pecas[0]);
								peca.setDlrqty(((BigDecimal)pecas[1]).intValue());
								peca.setDs18((String) pecas[2]);
								if(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas() != null && contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue() > 0){
									if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
										Double precoPeca = ((BigDecimal)pecas[3]).longValue() - ((((BigDecimal)pecas[3]).longValue() * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue()))/100);
										peca.setPreco(BigDecimal.valueOf(precoPeca) );//desconto PDP
									}
								}else{
									peca.setPreco((BigDecimal)pecas[3]);
								}
								pecasBeanList.add(peca);
								custoPecas = custoPecas.add(peca.getPreco().multiply(BigDecimal.valueOf(peca.getDlrqty())));
							}
						}
					//JRBeanCollectionDataSource pecasList =  new JRBeanCollectionDataSource(pecasBeanList);
					//revisaoPecas.setPecas(pecasList);
					
					//revisaoPecasList.add(revisaoPecas);
				//}	
			//}
			
			JRBeanCollectionDataSource pecasListJRBaen = new JRBeanCollectionDataSource(pecasBeanList);
			

			try {  
				jasperReport = (JasperReport) JRLoader.loadObject( caminhoJasper );  
			} catch (Exception jre) {  
				jre.printStackTrace();  
			} finally{
				if(conn != null){
					//stm.close();
					conn.close();
				}
			}

			//Double custoManutencao = 0d;
			//BigDecimal custoPecas = BigDecimal.ZERO;
			//TwFilial twFilial = new TwFilial();

//			BigDecimal custoPecas = BigDecimal.ZERO;
//			BigDecimal custoMo = BigDecimal.ZERO;

			BigDecimal horimetro = null;
			if(standard.getIdOsOperacional() != null){
				query = manager.createNativeQuery("select horimetro From Os_Palm c where c.NUMERO_OS=:numOs");
				query.setParameter("numOs", standard.getIdOsOperacional().getNumOs());
				if(query.getResultList().size() > 0){
					horimetro = (BigDecimal)query.getSingleResult();
				}
			}else{
				query = manager.createNativeQuery("select HORIMETRO from PMP_MAQUINA_PL where ID = (select MAX (id) from PMP_MAQUINA_PL where HORIMETRO is not null and NUMERO_SERIE = '"+contrato.getNumeroSerie()+"')");
				if(query.getResultList().size() > 0){
					horimetro = (BigDecimal)query.getSingleResult();
				}
			}

//			query = manager.createQuery("From PmpContHorasStandard c, PmpOsOperacional o where c.id = o.idContHorasStandard.id and o.numOs=:numOs");
//			query.setParameter("numOs", standard.getIdOsOperacional().getNumOs());
//			Object[] pair = (Object[])query.getSingleResult();
			List<IntervencaoBean> intervencaoList = new ArrayList<IntervencaoBean>();

			//PmpContHorasStandard pmpContHorasStandard = (PmpContHorasStandard) pair[0];
			IntervencaoBean intervencaoBean = new IntervencaoBean();
			intervencaoBean.setHorimetro(standard.getHorasManutencao().intValue());
			intervencaoBean.setIntervancaoRealizada(standard.getHorasRevisao().intValue());
			
			intervencaoBean.setIsTa(standard.getIsTa());
			if(standard.getCusto() != null){
				intervencaoBean.setCusto(ValorMonetarioHelper.formata("###,###.00", standard.getCusto().doubleValue()));
			}

			intervencaoList.add(intervencaoBean);
			JRBeanCollectionDataSource intervencaoListJRBaen = new JRBeanCollectionDataSource(intervencaoList);

			Map parametros = new HashMap(); 
			//parametros.put("horas", horas);
			
			parametros.put("FORNECEDOR", filial.getRazaoSocial()); 
			parametros.put("ENDERECO_FILIAL", filial.getEndereco()); 
			parametros.put("CEP_FILIAL", filial.getCep()); 
			parametros.put("CNPJ_FILIAL", filial.getCnpj()); 
			
			parametros.put("SUBREPORT_DIR", pathSubreport); 
			parametros.put("NUMERO_CONTRATO", contrato.getNumeroContrato());  
			parametros.put("INTERVENCAO_LIST", intervencaoListJRBaen);
			parametros.put("PECAS_LIST", pecasListJRBaen);
			parametros.put("FABRICANTE", contrato.getFabricante());
			parametros.put("RAZAO_SOCIAL", contrato.getRazaoSocial()+" / "+contrato.getCodigoCliente());  
			parametros.put("ENDERECO", contrato.getEndereco()+" / "+contrato.getBairro());  
			parametros.put("CIDADE", contrato.getCidade()+" / "+contrato.getUf());  
			parametros.put("CEP", contrato.getCep());  
			parametros.put("CNPJ", contrato.getCnpj());  
			parametros.put("CPF", contrato.getCpf());  
			parametros.put("CONTATO_SERVICOS", contrato.getContatoServicos());  
			parametros.put("EMAIL_CONTATO_SERVICOS", contrato.getEmailContatoServicos());  
			parametros.put("TELEFONE_SERVICOS", contrato.getTelefoneServicos());  
			parametros.put("MODELO", contrato.getModelo());  
			parametros.put("FAMILIA", contrato.getFamilia());  
			parametros.put("MUM_SERIE", contrato.getNumeroSerie()); 
			if(horimetro != null){
				parametros.put("HORIMETRO", horimetro.toString());  
			}
			if(contrato.getValoContrato() != null){
//				String arraySpot []= contrato.getQtdHorasDeslSpot().split(":");
//				BigDecimal minSpot = BigDecimal.valueOf((Double.valueOf(arraySpot[1])/60));
//				BigDecimal deslocamentoSpot = BigDecimal.valueOf(Double.valueOf(arraySpot[0]));
//				deslocamentoSpot.add(minSpot);
//				deslocamentoSpot.add(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHorasDesSpot());
//				if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
//					BigDecimal MO = BigDecimal.valueOf(standard.getCustoMo().doubleValue() - deslocamentoSpot.doubleValue());
//					parametros.put("CUSTO_MO",MO);
//					parametros.put("DESLOCAMENTO",deslocamentoSpot);
//				}else{
//				}
				parametros.put("PRECO", "R$ "+ValorMonetarioHelper.formata("###,###.00", contrato.getValoContrato().doubleValue()));
				parametros.put("VALOR_PARCELA", "R$ "+ValorMonetarioHelper.formata("###,###.00", (contrato.getValoContrato().doubleValue()/contrato.getQtdParcelas())));
				
			}else{
				parametros.put("PRECO", "");
				parametros.put("VALOR_PARCELA", "");
			}
			parametros.put("CUSTO_PECAS",custoPecas);
			parametros.put("CUSTO_MO",standard.getCustoMo());
			
			byte[] bytes = null;
			try {  
				//pdfProposta = JasperRunManager.runReportToPdf( jasperReport, parametros, new JREmptyDataSource()); 
				pdfProposta = JasperFillManager.fillReport(jasperReport, parametros,  new JREmptyDataSource());
				JROdtExporter docxExporter = new JROdtExporter();
				ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();  
				docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);  
				docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, pdfProposta);  
				//docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);  
				docxExporter.exportReport();
				bytes = xlsReport.toByteArray();
			} catch (Exception jre) {  
				jre.printStackTrace();  
			}  

			//Parametros para nao fazer cache e o que será exibido..  
			//response.setContentType( "application/pdf" );  
			response.addHeader("Content-disposition", "attachment; filename=orçamento.odt"); 

			//Envia para o navegador o pdf..  
			ServletOutputStream servletOutputStream = response.getOutputStream();  

			servletOutputStream.write( bytes );  
			servletOutputStream.flush();  
			servletOutputStream.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
			if(conn != null){
				try {
					rs.close();
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}
}

