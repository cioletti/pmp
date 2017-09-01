package com.pmp.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
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
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.pmp.bean.IntervencaoBean;
import com.pmp.bean.PecaBean;
import com.pmp.bean.RevisaoPecasBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.entity.PmpContHorasStandard;
import com.pmp.entity.PmpContMesesStandard;
import com.pmp.entity.PmpContrato;
import com.pmp.entity.PmpStatusContrato;
import com.pmp.entity.PmpTipoContrato;
import com.pmp.entity.TwFilial;
import com.pmp.entity.TwFuncionario;
import com.pmp.util.JpaUtil;
import com.pmp.util.ValorMonetarioHelper;

/**
 * Servlet implementation class ReportPdf
 */
public class ReportPdf extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReportPdf() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		EntityManager manager = null;
		PmpContrato contrato = null;
		try {
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class,
					Long.valueOf(request.getParameter("idContrato")));
		} catch (Exception e) {
			manager.close();
		}

		if (contrato.getIdConfigManutencao().getIsGeradorStandby().equals("N")) {
			processRequest(request, response);
		} else {
			processRequestGerador(request, response);
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String idContrato = request.getParameter("idContrato");
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			PmpContrato contrato = manager.find(PmpContrato.class,
					Long.valueOf(idContrato));
			if (contrato.getIdClassificacaoContrato().getSigla().equals("PLUS")) {
				this.generateContratoPlus(request, response);
			} else {
				this.generateContratoPMREN(request, response);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} finally {
			if (manager != null && manager.isOpen()) {
				manager.close();
			}
		}

	}

	protected void processRequestGerador(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String idContrato = request.getParameter("idContrato");
		// if(idContrato != null){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			PmpContrato contrato = manager.find(PmpContrato.class,
					Long.valueOf(idContrato));
			// PmpStatusContrato statusContrato =
			// manager.find(PmpStatusContrato.class,
			// contrato.getIdStatusContrato().getId());
			// PmpTipoContrato tipoContrato =
			// manager.find(PmpTipoContrato.class,
			// contrato.getIdTipoContrato().getId());

			generateContratoPMRENGerador(request, response);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (manager != null && manager.isOpen()) {
				manager.close();
			}
		}

		// if(statusContrato.getSigla().equals("CEN")){
		// if(("CON").equals(tipoContrato.getSigla()) ||
		// ("VEN").equals(tipoContrato.getSigla()) ||
		// ("REN").equals(tipoContrato.getSigla()) ||
		// ("VEPM").equals(tipoContrato.getSigla())){
		// if(contrato.getBgrp().equals("PM") ||
		// contrato.getBgrp().equals("KIT3")){
		// generatePropostaGerador(request, response);
		// }else if(contrato.getBgrp().equals("KIT2")){
		// generatePropostaKIT2Gerador(request, response);
		// }
		// }
		// }else if(statusContrato.getSigla().equals("CA")){
		// if(contrato.getBgrp().equals("PM")){
		// generateContratoPMRENGerador(request, response);
		// }else if(contrato.getBgrp().equals("KIT3")){
		// generateContratoKIT3Gerador(request, response);
		// }else if(contrato.getBgrp().equals("KIT2")){
		// generateContratoKIT2Gerador(request, response);
		// }
		// }
		// }else{
		// this.generateContratoJuridico(request, response);
		// }

	}

	// para pm e kit3
	private void generateProposta(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			FileNotFoundException {
		String idContrato = request.getParameter("idContrato");

		JasperReport jasperReport = null;
		byte[] pdfProposta = null;
		EntityManager manager = JpaUtil.getInstance();
		PmpContrato contrato = manager.find(PmpContrato.class,
				Long.valueOf(idContrato));
		// Obtem o caminho do .jasper
		ServletContext servletContext = super.getServletContext();
		String caminhoJasper = servletContext
				.getRealPath("WEB-INF/proposta/proposta_pmp.jasper");
		String pathSubreport = servletContext.getRealPath("WEB-INF/proposta/")
				+ "/";

		// Carrega o arquivo com o jasperReport
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(caminhoJasper);
		} catch (Exception jre) {
			jre.printStackTrace();
		}
		TwFuncionario funcionario = new TwFuncionario();

		// Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		// BigDecimal custoPecas = BigDecimal.ZERO;
		TwFilial twFilial = new TwFilial();
		try {
			// contrato = manager.find(PmpContrato.class,
			// Long.valueOf(idContrato));
			twFilial = manager.find(TwFilial.class, contrato.getFilial()
					.longValue());
			Long frequencia = 0l;
			// if(!contrato.getIdTipoContrato().getSigla().equals("REN")){
			// BigDecimal custoNordeste =
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager
					.createQuery("From PmpContHorasStandard where idContrato.id = :id");
			query.setParameter("id", contrato.getId());
			List<PmpContHorasStandard> horasStandardList = query
					.getResultList();
			frequencia = horasStandardList.get(0).getFrequencia();
			totalHorasManutencao = frequencia * horasStandardList.size();
			// for (PmpContHorasStandard pmpContHorasStandard :
			// horasStandardList) {
			// //Query para peças com desconto PDP
			// query =
			// manager.createNativeQuery("select sum(m.unls) from pmp_manutencao m"+
			// " where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
			// " and m.bgrp = '"+contrato.getBgrp()+"'"+
			// " and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
			// " and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4,
			// 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
			// " and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
			// custoPecas = (BigDecimal)query.getSingleResult();
			// Double custoNordesteTotal = custoPecas.doubleValue() +
			// ((custoPecas.doubleValue() *
			// custoNordeste.doubleValue())/100);//aplicar custo nordeste
			// custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal *
			// (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto
			// PDP
			// custoManutencao = custoManutencao + custoNordesteTotal;
			//
			// //Query sem desconto pdp
			// query =
			// manager.createNativeQuery("select sum(m.unls) from pmp_manutencao m"+
			// " where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
			// " and m.bgrp = '"+contrato.getBgrp()+"'"+
			// " and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
			// " and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4,
			// 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
			// " and m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)");
			// custoPecas = (BigDecimal)query.getSingleResult();
			// custoNordesteTotal = custoPecas.doubleValue() +
			// ((custoPecas.doubleValue() *
			// custoNordeste.doubleValue())/100);//aplicar custo nordeste
			// custoManutencao = custoManutencao + custoNordesteTotal;
			// }
			//
			// totalHorasManutencao = frequencia * horasStandardList.size();
			// //custo do TA
			// if(contrato.getTa().equals("S")){
			// Double taCusto =
			// (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue()
			// *
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
			// taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;//
			// total de revisoes de 2000 horas * custo do TA
			// custoManutencao = custoManutencao + taCusto;
			// }
			//
			//
			// //total horas para a manutenção
			// query =
			// manager.createNativeQuery("select sum (to_number(replace(h.frsdhr,'.',','))) from pmp_hora h"+
			// " where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId()+")"+
			// " and h.bgrp = '"+contrato.getBgrp()+"'"+
			// " and substr(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
			// " and substr(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4,
			// 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
			// BigDecimal totalHHManutencao =
			// (BigDecimal)query.getSingleResult();
			// custoManutencao = custoManutencao +
			// (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue()
			// * totalHHManutencao.doubleValue());//valor de hh
			// custoManutencao = custoManutencao +
			// ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue()
			// *
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())*horasStandardList.size());//valor
			// de km vezes a quantidade de manutenções
			// funcionario = (TwFuncionario)manager.find(TwFuncionario.class,
			// contrato.getIdFuncionario());
			//
			// custoManutencao = jurosContrato(contrato, custoManutencao);
			//
			// }

			funcionario = (TwFuncionario) manager.find(TwFuncionario.class,
					contrato.getIdFuncionario());

			String horasManut = "";
			query = manager
					.createQuery(" From PmpContHorasStandard where idContrato.id =:id");
			query.setParameter("id", contrato.getId());
			List<PmpContHorasStandard> list = query.getResultList();
			for (PmpContHorasStandard pmpContHorasStandard : list) {
				horasManut += pmpContHorasStandard.getHorasManutencao() + "H/";
			}

			String complemento = "Temos a satisfação em apresentar nossa proposta de prestação de serviço para realizar as"
					+ " manutenções preventivas do equipamento "
					+ contrato.getModelo()
					+ " série "
					+ contrato.getNumeroSerie()
					+ " no período de "
					+ horasManut.substring(0, horasManut.length() - 1)
					+ " horas do equipamento conforme escopo:";

			Map parametros = new HashMap();
			parametros.put("SUBREPORT_DIR", pathSubreport);

			parametros.put("empresa", contrato.getRazaoSocial());
			parametros.put("nomeCliente", contrato.getProcurador());
			parametros
					.put("telefoneComercial", contrato.getTelefoneComercial());
			parametros.put("validadeProposta", contrato.getIdConfigManutencao()
					.getIdConfiguracaoPreco().getValidadeContrato().toString());
			parametros.put("complemento", complemento);
			if (contrato.getValoContrato() != null) {
				parametros
						.put("valorProposta", ValorMonetarioHelper.formata(
								"###,###.00", contrato.getValoContrato()
										.doubleValue()));
			} else {
				parametros.put("valorProposta", "");
			}
			if (contrato.getIdTipoContrato().getSigla().equals("CON")) {
				parametros.put("condicaoPagamento", "CONCESSÃO");
			} else {
				parametros.put(
						"condicaoPagamento",
						contrato.getQtdParcelas() == 1 ? "à vista" : contrato
								.getQtdParcelas() + " parcelas");
			}
			parametros.put("nomeConsultor", funcionario.getEplsnm());
			parametros.put("foneConsultor", funcionario.getTelefone());
			parametros.put("cargo", funcionario.getCargo());
			SimpleDateFormat format = new SimpleDateFormat(
					"d 'de' MMMM , 'de' yyyy");
			parametros.put("filialData", twFilial.getStnm().trim() + ", "
					+ format.format(new Date()));
			if (contrato.getTa().equals("S")) {
				parametros
						.put("ta",
								"<style isBold=\"true\" pdfFontName=\"Helvetica-Bold\">-</style> Teste de Desempenho em ciclos de 2.000 horas (TA´s), quando contratada");
			} else {
				parametros.put("ta", null);
			}
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("cabecalho_contrato.PNG");

			File img = File.createTempFile("img", "gif", new File("."));

			OutputStream out = new FileOutputStream(img);
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0)
				out.write(buf, 0, len);
			out.close();
			inputStream.close();
			parametros.put("logo", img);

			// Gera o pdf para exibicao..
			try {
				pdfProposta = JasperRunManager.runReportToPdf(jasperReport,
						parametros, new JREmptyDataSource());
			} catch (Exception jre) {
				jre.printStackTrace();
			}

			// Parametros para nao fazer cache e o que será exibido..
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=proposta.pdf");

			// Envia para o navegador o pdf..
			ServletOutputStream servletOutputStream = response
					.getOutputStream();
			servletOutputStream.write(pdfProposta);
			servletOutputStream.flush();
			servletOutputStream.close();
			img.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// para pm e kit3
	private void generatePropostaGerador(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			FileNotFoundException {
		String idContrato = request.getParameter("idContrato");

		JasperReport jasperReport = null;
		byte[] pdfProposta = null;
		EntityManager manager = JpaUtil.getInstance();
		PmpContrato contrato = manager.find(PmpContrato.class,
				Long.valueOf(idContrato));
		// Obtem o caminho do .jasper
		ServletContext servletContext = super.getServletContext();
		String caminhoJasper = servletContext
				.getRealPath("WEB-INF/proposta/proposta_pmp.jasper");
		String pathSubreport = servletContext.getRealPath("WEB-INF/proposta/")
				+ "/";

		// Carrega o arquivo com o jasperReport
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(caminhoJasper);
		} catch (Exception jre) {
			jre.printStackTrace();
		}
		TwFuncionario funcionario = new TwFuncionario();

		// Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		// BigDecimal custoPecas = BigDecimal.ZERO;
		TwFilial twFilial = new TwFilial();
		try {
			// contrato = manager.find(PmpContrato.class,
			// Long.valueOf(idContrato));
			twFilial = manager.find(TwFilial.class, contrato.getFilial()
					.longValue());
			Long frequencia = 0l;
			// if(!contrato.getIdTipoContrato().getSigla().equals("REN")){
			// BigDecimal custoNordeste =
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager
					.createQuery("From PmpContMesesStandard where idContrato.id = :id");
			query.setParameter("id", contrato.getId());
			List<PmpContMesesStandard> horasStandardList = query
					.getResultList();
			frequencia = horasStandardList.get(0).getFrequencia();
			totalHorasManutencao = frequencia * horasStandardList.size();

			funcionario = (TwFuncionario) manager.find(TwFuncionario.class,
					contrato.getIdFuncionario());

			String horasManut = "";
			query = manager
					.createQuery(" From PmpContMesesStandard where idContrato.id =:id");
			query.setParameter("id", contrato.getId());
			List<PmpContMesesStandard> list = query.getResultList();
			for (PmpContMesesStandard pmpContHorasStandard : list) {
				horasManut += pmpContHorasStandard.getMesManutencao() + "M/";
			}

			String complemento = "Temos a satisfação em apresentar nossa proposta de prestação de serviço para realizar as"
					+ " manutenções preventivas do equipamento "
					+ contrato.getModelo()
					+ " série "
					+ contrato.getNumeroSerie()
					+ " no período de "
					+ horasManut.substring(0, horasManut.length() - 1)
					+ " horas do equipamento conforme escopo:";

			Map parametros = new HashMap();
			parametros.put("SUBREPORT_DIR", pathSubreport);

			parametros.put("empresa", contrato.getRazaoSocial());
			parametros.put("nomeCliente", contrato.getProcurador());
			parametros
					.put("telefoneComercial", contrato.getTelefoneComercial());
			parametros.put("validadeProposta", contrato.getIdConfigManutencao()
					.getIdConfiguracaoPreco().getValidadeContrato().toString());
			parametros.put("complemento", complemento);
			parametros.put("valorProposta", ValorMonetarioHelper.formata(
					"###,###.00", contrato.getValoContrato().doubleValue()));
			if (contrato.getIdTipoContrato().getSigla().equals("CON")) {
				parametros.put("condicaoPagamento", "CONCESSÃO");
			} else {
				parametros.put(
						"condicaoPagamento",
						contrato.getQtdParcelas() == 1 ? "à vista" : contrato
								.getQtdParcelas() + " parcelas");
			}
			parametros.put("nomeConsultor", funcionario.getEplsnm());
			parametros.put("foneConsultor", funcionario.getTelefone());
			parametros.put("cargo", funcionario.getCargo());
			SimpleDateFormat format = new SimpleDateFormat(
					"d 'de' MMMM , 'de' yyyy");
			parametros.put("filialData", twFilial.getStnm().trim() + ", "
					+ format.format(new Date()));
			if (contrato.getTa().equals("S")) {
				parametros
						.put("ta",
								"<style isBold=\"true\" pdfFontName=\"Helvetica-Bold\">-</style> Teste de Desempenho em ciclos de 2.000 horas (TA´s), quando contratada");
			} else {
				parametros.put("ta", null);
			}
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("cabecalho_contrato.PNG");

			File img = File.createTempFile("img", "gif", new File("."));

			OutputStream out = new FileOutputStream(img);
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0)
				out.write(buf, 0, len);
			out.close();
			inputStream.close();
			parametros.put("logo", img);

			// Gera o pdf para exibicao..
			try {
				pdfProposta = JasperRunManager.runReportToPdf(jasperReport,
						parametros, new JREmptyDataSource());
			} catch (Exception jre) {
				jre.printStackTrace();
			}

			// Parametros para nao fazer cache e o que será exibido..
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=proposta.pdf");

			// Envia para o navegador o pdf..
			ServletOutputStream servletOutputStream = response
					.getOutputStream();
			servletOutputStream.write(pdfProposta);
			servletOutputStream.flush();
			servletOutputStream.close();
			img.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generatePropostaKIT2(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			FileNotFoundException {
		String idContrato = request.getParameter("idContrato");

		JasperReport jasperReport = null;
		byte[] pdfProposta = null;

		// Obtem o caminho do .jasper
		ServletContext servletContext = super.getServletContext();
		String caminhoJasper = servletContext
				.getRealPath("WEB-INF/proposta/proposta_pmp.jasper");
		String pathSubreport = servletContext.getRealPath("WEB-INF/proposta/")
				+ "/";

		// Carrega o arquivo com o jasperReport
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(caminhoJasper);
		} catch (Exception jre) {
			jre.printStackTrace();
		}
		EntityManager manager = null;
		TwFuncionario funcionario = new TwFuncionario();
		PmpContrato contrato = new PmpContrato();
		// Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		// BigDecimal custoPecas = BigDecimal.ZERO;
		TwFilial twFilial = new TwFilial();
		manager = JpaUtil.getInstance();
		try {
			contrato = manager
					.find(PmpContrato.class, Long.valueOf(idContrato));
			twFilial = manager.find(TwFilial.class, contrato.getFilial()
					.longValue());
			Long frequencia = 0l;
			// if(!contrato.getIdTipoContrato().getSigla().equals("REN")){
			// BigDecimal custoNordeste =
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager
					.createQuery("From PmpContHorasStandard where idContrato.id = :id");
			query.setParameter("id", contrato.getId());
			List<PmpContHorasStandard> horasStandardList = query
					.getResultList();
			frequencia = horasStandardList.get(0).getFrequencia();
			totalHorasManutencao = frequencia * horasStandardList.size();
			// for (PmpContHorasStandard pmpContHorasStandard :
			// horasStandardList) {
			// frequencia = pmpContHorasStandard.getFrequencia();
			// //Query para peças com desconto PDP
			// query =
			// manager.createNativeQuery("select sum(m.unls) from pmp_manutencao m"+
			// " where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
			// " and m.bgrp = '"+contrato.getBgrp()+"'"+
			// " and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
			// " and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4,
			// 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
			// " and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
			// custoPecas = (BigDecimal)query.getSingleResult();
			// Double custoNordesteTotal = custoPecas.doubleValue() +
			// ((custoPecas.doubleValue() *
			// custoNordeste.doubleValue())/100);//aplicar custo nordeste
			// custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal *
			// (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto
			// PDP
			// custoManutencao = custoManutencao + custoNordesteTotal;
			//
			// //Query sem desconto pdp
			// query =
			// manager.createNativeQuery("select sum(m.unls) from pmp_manutencao m"+
			// " where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
			// " and m.bgrp = '"+contrato.getBgrp()+"'"+
			// " and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
			// " and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4,
			// 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
			// " and m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)");
			// custoPecas = (BigDecimal)query.getSingleResult();
			// custoNordesteTotal = custoPecas.doubleValue() +
			// ((custoPecas.doubleValue() *
			// custoNordeste.doubleValue())/100);//aplicar custo nordeste
			// custoManutencao = custoManutencao + custoNordesteTotal;
			// }
			//
			// totalHorasManutencao = frequencia * horasStandardList.size();
			// //custo do TA
			// if(contrato.getTa().equals("S")){
			// Double taCusto =
			// (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue()
			// *
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
			// taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;//
			// total de revisoes de 2000 horas * custo do TA
			// custoManutencao = custoManutencao + taCusto;
			// }
			//
			//
			// //total horas para a manutenção
			// query =
			// manager.createNativeQuery("select sum (to_number(replace(h.frsdhr,'.',','))) from pmp_hora h"+
			// " where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId()+")"+
			// " and h.bgrp = '"+contrato.getBgrp()+"'"+
			// " and substr(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
			// " and substr(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4,
			// 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'" +
			// " and h.cptcd = 7504");
			// BigDecimal totalHHManutencao =
			// (BigDecimal)query.getSingleResult();
			// custoManutencao = custoManutencao +
			// (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue()
			// * totalHHManutencao.doubleValue());//valor de hh
			// custoManutencao = custoManutencao +
			// ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue()
			// *
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())*1);//valor
			// de km vezes a quantidade de manutenções
			// funcionario = (TwFuncionario)manager.find(TwFuncionario.class,
			// contrato.getIdFuncionario());
			//
			// custoManutencao = jurosContrato(contrato, custoManutencao);
			//
			// }

			funcionario = (TwFuncionario) manager.find(TwFuncionario.class,
					contrato.getIdFuncionario());

			String horasManut = "";
			query = manager
					.createQuery(" From PmpContHorasStandard where idContrato.id =:id");
			query.setParameter("id", contrato.getId());
			List<PmpContHorasStandard> list = query.getResultList();
			for (PmpContHorasStandard pmpContHorasStandard : list) {
				horasManut += pmpContHorasStandard.getHorasManutencao() + "H/";
			}

			String complemento = "Temos a satisfação em apresentar nossa proposta de prestação de serviço para realizar as"
					+ " manutenções preventivas do equipamento "
					+ contrato.getModelo()
					+ " série "
					+ contrato.getPrefixo()
					+ contrato.getNumeroSerie()
					+ " no período de  "
					+ horasManut.substring(0, horasManut.length() - 2)
					+ " horas do equipamento conforme escopo:";

			Map parametros = new HashMap();
			parametros.put("SUBREPORT_DIR", pathSubreport);

			parametros.put("empresa", contrato.getRazaoSocial());
			parametros.put("nomeCliente", contrato.getNomeCliente());
			parametros
					.put("telefoneComercial", contrato.getTelefoneComercial());
			parametros.put("validadeProposta", contrato.getIdConfigManutencao()
					.getIdConfiguracaoPreco().getValidadeContrato().toString());
			parametros.put("complemento", complemento);
			parametros.put("valorProposta", ValorMonetarioHelper.formata(
					"###,###.00", contrato.getValoContrato().doubleValue()));
			parametros.put(
					"condicaoPagamento",
					contrato.getQtdParcelas() == 1 ? "à vista" : contrato
							.getQtdParcelas() + " parcelas");
			parametros.put("nomeConsultor", funcionario.getEplsnm());
			parametros.put("foneConsultor", funcionario.getTelefone());
			SimpleDateFormat format = new SimpleDateFormat(
					"d 'de' MMMM , 'de' yyyy");
			parametros.put("filialData", twFilial.getStnm().trim() + ", "
					+ format.format(new Date()));

			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("cabecalho_contrato.PNG");

			File img = File.createTempFile("img", "gif", new File("."));

			OutputStream out = new FileOutputStream(img);
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0)
				out.write(buf, 0, len);
			out.close();
			inputStream.close();
			parametros.put("logo", img);

			// Gera o pdf para exibicao..
			try {
				pdfProposta = JasperRunManager.runReportToPdf(jasperReport,
						parametros, new JREmptyDataSource());
			} catch (Exception jre) {
				jre.printStackTrace();
			}

			// Parametros para nao fazer cache e o que será exibido..
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=proposta.pdf");

			// Envia para o navegador o pdf..
			ServletOutputStream servletOutputStream = response
					.getOutputStream();
			servletOutputStream.write(pdfProposta);
			servletOutputStream.flush();
			servletOutputStream.close();
			img.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generatePropostaKIT2Gerador(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			FileNotFoundException {
		String idContrato = request.getParameter("idContrato");

		JasperReport jasperReport = null;
		byte[] pdfProposta = null;

		// Obtem o caminho do .jasper
		ServletContext servletContext = super.getServletContext();
		String caminhoJasper = servletContext
				.getRealPath("WEB-INF/proposta/proposta_pmp.jasper");
		String pathSubreport = servletContext.getRealPath("WEB-INF/proposta/")
				+ "/";

		// Carrega o arquivo com o jasperReport
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(caminhoJasper);
		} catch (Exception jre) {
			jre.printStackTrace();
		}
		EntityManager manager = null;
		TwFuncionario funcionario = new TwFuncionario();
		PmpContrato contrato = new PmpContrato();
		// Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		// BigDecimal custoPecas = BigDecimal.ZERO;
		TwFilial twFilial = new TwFilial();
		manager = JpaUtil.getInstance();
		try {
			contrato = manager
					.find(PmpContrato.class, Long.valueOf(idContrato));
			twFilial = manager.find(TwFilial.class, contrato.getFilial()
					.longValue());
			Long frequencia = 0l;
			// if(!contrato.getIdTipoContrato().getSigla().equals("REN")){
			// BigDecimal custoNordeste =
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager
					.createQuery("From PmpContMesesStandard where idContrato.id = :id");
			query.setParameter("id", contrato.getId());
			List<PmpContMesesStandard> mesesStandardList = query
					.getResultList();
			frequencia = mesesStandardList.get(0).getFrequencia();
			totalHorasManutencao = frequencia * mesesStandardList.size();

			funcionario = (TwFuncionario) manager.find(TwFuncionario.class,
					contrato.getIdFuncionario());

			String horasManut = "";
			query = manager
					.createQuery(" From PmpContMesesStandard where idContrato.id =:id");
			query.setParameter("id", contrato.getId());
			List<PmpContMesesStandard> list = query.getResultList();
			for (PmpContMesesStandard pmpContMesesStandard : list) {
				horasManut += pmpContMesesStandard.getMesManutencao() + "H/";
			}

			String complemento = "Temos a satisfação em apresentar nossa proposta de prestação de serviço para realizar as"
					+ " manutenções preventivas do equipamento "
					+ contrato.getModelo()
					+ " série "
					+ contrato.getPrefixo()
					+ contrato.getNumeroSerie()
					+ " no período de  "
					+ horasManut.substring(0, horasManut.length() - 2)
					+ " horas do equipamento conforme escopo:";

			Map parametros = new HashMap();
			parametros.put("SUBREPORT_DIR", pathSubreport);

			parametros.put("empresa", contrato.getRazaoSocial());
			parametros.put("nomeCliente", contrato.getNomeCliente());
			parametros
					.put("telefoneComercial", contrato.getTelefoneComercial());
			parametros.put("validadeProposta", contrato.getIdConfigManutencao()
					.getIdConfiguracaoPreco().getValidadeContrato().toString());
			parametros.put("complemento", complemento);
			parametros.put("valorProposta", ValorMonetarioHelper.formata(
					"###,###.00", contrato.getValoContrato().doubleValue()));
			parametros.put(
					"condicaoPagamento",
					contrato.getQtdParcelas() == 1 ? "à vista" : contrato
							.getQtdParcelas() + " parcelas");
			parametros.put("nomeConsultor", funcionario.getEplsnm());
			parametros.put("foneConsultor", funcionario.getTelefone());
			SimpleDateFormat format = new SimpleDateFormat(
					"d 'de' MMMM , 'de' yyyy");
			parametros.put("filialData", twFilial.getStnm().trim() + ", "
					+ format.format(new Date()));

			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("cabecalho_contrato.PNG");

			File img = File.createTempFile("img", "gif", new File("."));

			OutputStream out = new FileOutputStream(img);
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0)
				out.write(buf, 0, len);
			out.close();
			inputStream.close();
			parametros.put("logo", img);

			// Gera o pdf para exibicao..
			try {
				pdfProposta = JasperRunManager.runReportToPdf(jasperReport,
						parametros, new JREmptyDataSource());
			} catch (Exception jre) {
				jre.printStackTrace();
			}

			// Parametros para nao fazer cache e o que será exibido..
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=proposta.pdf");

			// Envia para o navegador o pdf..
			ServletOutputStream servletOutputStream = response
					.getOutputStream();
			servletOutputStream.write(pdfProposta);
			servletOutputStream.flush();
			servletOutputStream.close();
			img.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Double jurosContrato(PmpContrato contrato, Double custoManutencao) {
		for (int i = 0; i < contrato.getQtdParcelas(); i++) {
			custoManutencao = custoManutencao
					+ ((custoManutencao * contrato.getIdConfigManutencao()
							.getIdConfiguracaoPreco().getJurosVenda()
							.doubleValue()) / 100);
		}
		return custoManutencao;
	}

	private void generateContratoPMREN(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			FileNotFoundException {
		String idContrato = request.getParameter("idContrato");
		// String isAdministrador = request.getParameter("isAdministrador");
		// String tipoContrato = request.getParameter("tipoContrato");

		JasperReport jasperReport = null;
		byte[] pdfProposta = null;
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			PmpContrato contrato = manager.find(PmpContrato.class,
					Long.valueOf(idContrato));
			// Obtem o caminho do .jasper
			ServletContext servletContext = super.getServletContext();
			String caminhoJasper = "";
			String pathSubreport = "";
			UsuarioBean bean = (UsuarioBean) request.getSession().getAttribute(
					"usuario");
			if (bean == null) {
				response.sendRedirect("/ControlPanelPesa/ControlPanel.html");
				return;
			}

			// if(isAdministrador.equals("ADM")){
			// caminhoJasper =
			// servletContext.getRealPath("WEB-INF/contrato_pm_juridico/pmp_contrato.jasper");
			// pathSubreport =
			// servletContext.getRealPath("WEB-INF/contrato_pm_juridico/")+"/";
			// }else{
			
			if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){ 
				caminhoJasper = servletContext
				.getRealPath("WEB-INF/contrato_pm_consultor_spot/pmp_contrato.jasper");
				pathSubreport = servletContext
				.getRealPath("WEB-INF/contrato_pm_consultor_spot/")
				+ "/";
			}else if (contrato.getIdClassificacaoContrato().getSigla().equals("PRE")) {
				if (contrato.getIdTipoContrato().getSigla().equals("VEN")) {
					caminhoJasper = servletContext
							.getRealPath("WEB-INF/contrato_pm_consultor_pos_pago/pmp_contrato.jasper");
					pathSubreport = servletContext
							.getRealPath("WEB-INF/contrato_pm_consultor_pos_pago/")
							+ "/";
				}else if (contrato.getIdTipoContrato().getSigla()
						.equals("VPG")
						|| contrato.getIdTipoContrato().getSigla()
								.equals("REN")
						|| contrato.getIdTipoContrato().getSigla()
								.equals("CAN")
						|| contrato.getIdTipoContrato().getSigla()
								.equals("VEPM")) {
					caminhoJasper = servletContext
							.getRealPath("WEB-INF/contrato_pm_consultor_pre_pago/pmp_contrato.jasper");
					pathSubreport = servletContext
							.getRealPath("WEB-INF/contrato_pm_consultor_pre_pago/")
							+ "/";
					// }
				} else if (contrato.getIdTipoContrato().getSigla()
						.equals("CON")) {
					caminhoJasper = servletContext
							.getRealPath("WEB-INF/contrato_pm_consultor_concessao/pmp_contrato.jasper");
					pathSubreport = servletContext
							.getRealPath("WEB-INF/contrato_pm_consultor_concessao/")
							+ "/";
				}
			} else {
				if (contrato.getIdClassificacaoContrato().getSigla()
						.equals("CUS")) {
					if (contrato.getIsTerritorio().equals("S")) {
						if (contrato.getIdTipoContrato().getSigla().equals("VPG")) {
							caminhoJasper = servletContext
							.getRealPath("WEB-INF/contrato_pm_consultor_customer/pmp_contrato.jasper");
							pathSubreport = servletContext
							.getRealPath("WEB-INF/contrato_pm_consultor_customer/")
							+ "/";
						}else{
							caminhoJasper = servletContext
							.getRealPath("WEB-INF/contrato_pm_consultor_customer_pos/pmp_contrato.jasper");
							pathSubreport = servletContext
							.getRealPath("WEB-INF/contrato_pm_consultor_customer_pos/")
							+ "/";
						}
					} else {
						caminhoJasper = servletContext
								.getRealPath("WEB-INF/contrato_pm_consultor_custumer_fora/pmp_contrato.jasper");
						pathSubreport = servletContext
								.getRealPath("WEB-INF/contrato_pm_consultor_custumer_fora/")
								+ "/";
					}
					
				} else {
					if (contrato.getIdClassificacaoContrato().getSigla()
							.equals("PART") && contrato.getIdTipoContrato().getSigla().equals("VEN")) {
						caminhoJasper = servletContext
								.getRealPath("WEB-INF/contrato_pm_consultor_partner_pos/pmp_contrato.jasper");
						pathSubreport = servletContext
								.getRealPath("WEB-INF/contrato_pm_consultor_partner_pos/")
								+ "/";
						
					} else 	if(contrato.getIdClassificacaoContrato().getSigla()
							.equals("PART")){
						caminhoJasper = servletContext
						.getRealPath("WEB-INF/contrato_pm_consultor_partner/pmp_contrato.jasper");
						pathSubreport = servletContext
						.getRealPath("WEB-INF/contrato_pm_consultor_partner/")
						+ "/";
						
						
					} else {
						if (contrato.getIdClassificacaoContrato().getSigla()
								.equals("CUSLIGHT")) {
							if (contrato.getIsTerritorio().equals("S")) {
								if (contrato.getIdTipoContrato().getSigla().equals("VPG")) {
								caminhoJasper = servletContext
										.getRealPath("WEB-INF/contrato_pm_consultor_customer_light_dentro_pre/pmp_contrato.jasper");
								pathSubreport = servletContext
										.getRealPath("WEB-INF/contrato_pm_consultor_customer_light_dentro_pre/")
										+ "/";
								}else{
									caminhoJasper = servletContext
									.getRealPath("WEB-INF/contrato_pm_consultor_customer_light_dentro_pos/pmp_contrato.jasper");
							pathSubreport = servletContext
									.getRealPath("WEB-INF/contrato_pm_consultor_customer_light_dentro_pos/")
									+ "/";
								}
							} else {
								caminhoJasper = servletContext
										.getRealPath("WEB-INF/contrato_pm_consultor_customer_light_fora/pmp_contrato.jasper");
								pathSubreport = servletContext
										.getRealPath("WEB-INF/contrato_pm_consultor_customer_light_fora/")
										+ "/";
							}
						} 
					}
				}
			}
			
			// Carrega o arquivo com o jasperReport
			try {
				jasperReport = (JasperReport) JRLoader
						.loadObject(caminhoJasper);
			} catch (Exception jre) {
				jre.printStackTrace();
			}

			// TwFuncionario funcionario = new TwFuncionario();

			// Double custoManutencao = 0d;
			Long totalHorasManutencao = 0l;
			// BigDecimal custoPecas = BigDecimal.ZERO;
			TwFilial twFilial = new TwFilial();

			twFilial = manager.find(TwFilial.class, contrato.getFilial()
					.longValue());
			// Long frequencia = 0l;
			// if(!contrato.getIdTipoContrato().getSigla().equals("REN")){
			// BigDecimal custoNordeste =
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager
					.createQuery("From PmpContHorasStandard where idContrato.id = :id order by horasManutencao");
			query.setParameter("id", contrato.getId());
			List<PmpContHorasStandard> horasStandardList = query
					.getResultList();
			List<IntervencaoBean> intervencaoList = new ArrayList<IntervencaoBean>();
			int numVisitas = 0;
			Long primeiraRevisao = 0L; 
			Long ultimaRevisao = 0L; 
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				if (contrato.getIdTipoContrato() != null
						&& contrato.getIdTipoContrato().getSigla()
								.equals("VEN")
						&& contrato.getIdStatusContrato().getSigla()
								.equals("CA")) {
					if (contrato.getPrintRevisaoPosPago().longValue() < pmpContHorasStandard
							.getHorasManutencao()) {
						break;
					}
				}
				if(numVisitas == 0){
					primeiraRevisao = pmpContHorasStandard
					.getHorasManutencao();
				}
				numVisitas++;
				ultimaRevisao = pmpContHorasStandard
						.getHorasManutencao();
				IntervencaoBean intervencaoBean = new IntervencaoBean();
				intervencaoBean.setHorimetro(pmpContHorasStandard
						.getHorasManutencao().intValue());
				intervencaoBean.setIntervancaoRealizada(pmpContHorasStandard
						.getHorasRevisao().intValue());
				intervencaoBean.setIsTa(pmpContHorasStandard.getIsTa());
				if (pmpContHorasStandard.getCusto() != null) {
					intervencaoBean.setCusto(ValorMonetarioHelper.formata(
							"###,###.00", pmpContHorasStandard.getCusto()
									.doubleValue()));
				}
				intervencaoList.add(intervencaoBean);
			}
			totalHorasManutencao = ultimaRevisao - primeiraRevisao;
			JRBeanCollectionDataSource intervencaoListJRBaen = new JRBeanCollectionDataSource(
					intervencaoList);

			// frequencia = horasStandardList.get(0).getFrequencia();
			// totalHorasManutencao = frequencia * horasStandardList.size();
			// for (PmpContHorasStandard pmpContHorasStandard :
			// horasStandardList) {
			// frequencia = pmpContHorasStandard.getFrequencia();
			// //Query para peças com desconto PDP
			// query =
			// manager.createNativeQuery("select sum(m.unls) from pmp_manutencao m"+
			// " where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
			// " and m.bgrp = '"+contrato.getBgrp()+"'"+
			// " and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
			// " and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4,
			// 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
			// " and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
			// custoPecas = (BigDecimal)query.getSingleResult();
			// Double custoNordesteTotal = custoPecas.doubleValue() +
			// ((custoPecas.doubleValue() *
			// custoNordeste.doubleValue())/100);//aplicar custo nordeste
			// custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal *
			// (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto
			// PDP
			// custoManutencao = custoManutencao + custoNordesteTotal;
			//
			// //Query sem desconto pdp
			// query =
			// manager.createNativeQuery("select sum(m.unls) from pmp_manutencao m"+
			// " where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
			// " and m.bgrp = '"+contrato.getBgrp()+"'"+
			// " and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
			// " and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4,
			// 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
			// " and m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)");
			// custoPecas = (BigDecimal)query.getSingleResult();
			// custoNordesteTotal = custoPecas.doubleValue() +
			// ((custoPecas.doubleValue() *
			// custoNordeste.doubleValue())/100);//aplicar custo nordeste
			// custoManutencao = custoManutencao + custoNordesteTotal;
			// }
			// //int totalManutencoes = 0;
			//
			// totalHorasManutencao = frequencia * horasStandardList.size();
			//
			// //custo do TA
			// if(contrato.getTa().equals("S")){
			// Double taCusto =
			// (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue()
			// *
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
			// taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;//
			// total de revisoes de 2000 horas * custo do TA
			// custoManutencao = custoManutencao + taCusto;
			// }
			//
			//
			// //total horas para a manutenção
			// query =
			// manager.createNativeQuery("select sum (to_number(replace(h.frsdhr,'.',','))) from pmp_hora h"+
			// " where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId()+")"+
			// " and h.bgrp = '"+contrato.getBgrp()+"'"+
			// " and substr(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
			// " and substr(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4,
			// 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
			// BigDecimal totalHHManutencao =
			// (BigDecimal)query.getSingleResult();
			// custoManutencao = custoManutencao +
			// (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue()
			// * totalHHManutencao.doubleValue());//valor de hh
			// custoManutencao = custoManutencao +
			// ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue()
			// *
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())*horasStandardList.size());//valor
			// de km vezes a quantidade de manutenções
			// funcionario = (TwFuncionario)manager.find(TwFuncionario.class,
			// contrato.getIdFuncionario());
			//
			// custoManutencao = jurosContrato(contrato, custoManutencao);
			//
			// }

			// }catch (Exception e) {
			// e.printStackTrace();
			// }
			// try{

			String horasManut = "";
			// query =
			// manager.createQuery(" From PmpContHorasStandard where idContrato.id =:id");
			// query.setParameter("id", contrato.getId());
			// List<PmpContHorasStandard> list = query.getResultList();
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				horasManut += pmpContHorasStandard.getHorasManutencao() + "H/";
			}

			String complementoSigla = "";
			String complemento = "";
			String siglaCustomizacao = "";
			String complementoCustPart = "";
				if(contrato.getIdClassificacaoContrato().getSigla().equals("CUS") || contrato.getIdClassificacaoContrato().getSigla().equals("CUSLIGHT") || contrato.getIdClassificacaoContrato().getSigla().equals("PART")){
					complementoCustPart = " and m.OJBLOC <> 'CST'";
				}

			query = manager
					.createNativeQuery("select ID_TIPO_CUSTOMIZACAO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_TIPO_CUSTOMIZACAO tc"
							+ " where cc.ID_CONTRATO = "
							+ idContrato+ " and cc.ID_TIPO_CUSTOMIZACAO = tc.ID");

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

			if (siglaCustomizacao.length() > 0) {
				complementoSigla = " and ojbloc not in (" + siglaCustomizacao.substring(0, siglaCustomizacao.length() -1)
						+ ")";
				complementoSigla += " and ocptmd not in (" + siglaCustomizacao.substring(0, siglaCustomizacao.length() -1)
						+ ")";
				complementoSigla += " and JWKAPP not in (" + siglaCustomizacao.substring(0, siglaCustomizacao.length() -1)
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
			List<PecaBean> pecasBeanList;
			query = manager
					.createNativeQuery(" select convert( varchar(10),HORAS_MANUTENCAO) +' HORAS', STANDARD_JOB_CPTCD, HORAS_MANUTENCAO, IS_PARTNER from PMP_CONT_HORAS_STANDARD"
							+ " where ID_CONTRATO     = "
							+ idContrato
							+ " order by HORAS_MANUTENCAO  ");
			if (query.getResultList().size() > 0) {
				List<Object[]> result = query.getResultList();
				for (Object[] horasCompcode : result) {
					if (contrato.getIdTipoContrato() != null
							&& contrato.getIdTipoContrato().getSigla()
									.equals("VEN")
							&& contrato.getIdStatusContrato().getSigla()
									.equals("CA")) {
						if (contrato.getPrintRevisaoPosPago().longValue() <  ((BigDecimal)horasCompcode[2]).longValue()) {
							break;
						}
					}
					if(contrato.getIdClassificacaoContrato().getSigla().equals("PART")
							&& ((horasCompcode[3] == null || !((String)horasCompcode[3]).equals("N")))){
						
						query = manager.createNativeQuery("select descricao from Pmp_Comp_Code_Partner");
						List<String> descricao = (List<String>)query.getResultList();
						for (String cptcd : descricao) {
							if((((String)horasCompcode[1]).equals(cptcd))){
								complementoCustPart = " and m.OJBLOC <> 'CST'";
							}
						}
						//complementoCustPart = " and m.OJBLOC <> 'CST'";
					}else{
						if(contrato.getIdClassificacaoContrato().getSigla().equals("PART")){
							if((horasCompcode[3] == null || ((String)horasCompcode[3]).equals("N"))){
								//complementoCustPart = " and m.OJBLOC <> 'CST'";
								complementoCustPart = "";
							}
						}
					}
					pecasBeanList = new ArrayList<PecaBean>();
					RevisaoPecasBean revisaoPecas = new RevisaoPecasBean();
					revisaoPecas.setHoras((String) horasCompcode[0]);
					String SQL = "select m.PANO20, m.DLRQTY, m.DS18 from pmp_manutencao m"
							+ " where m.cptcd = '"
							+ (String) horasCompcode[1]
							+ "'"
							+ " and m.bgrp = 'PM'"
							+ "  and substring(m.beqmsn,1,4) = '"
							+ contrato.getPrefixo()
							+ "'"
							+ complemento
							+ complementoSigla
							+ complementoCustPart
							+ " and substring(m.beqmsn,5,10) between '"
							+ contrato.getBeginRanger().substring(4, 9)
							+ "' and '"
							+ contrato.getEndRanger().substring(4, 9)+"'";
					if(contrato.getIdClassificacaoContrato().getSigla()
							.equals("CUSLIGHT")){
						SQL += " and m.sos <> '050' ";
					}
					query = manager
							.createNativeQuery(SQL);
					if (query.getResultList().size() > 0) {
						List<Object[]> resultPecas = query.getResultList();
						for (Object[] pecas : resultPecas) {
							
							PecaBean peca = new PecaBean();
							peca.setPano20((String) pecas[0]);
							peca.setDlrqty(((BigDecimal)pecas[1]).intValue());
							peca.setDs18((String) pecas[2]);
							pecasBeanList.add(peca);
						}
					JRBeanCollectionDataSource pecasList =  new JRBeanCollectionDataSource(pecasBeanList);
					revisaoPecas.setPecas(pecasList);
					}	

					revisaoPecasList.add(revisaoPecas);
				}	
			}
			
			JRBeanCollectionDataSource revisaoPecasDataSource =  new JRBeanCollectionDataSource(revisaoPecasList);
			
			

			Map parametros = new HashMap();

			parametros.put("COMPLEMENTO_TITULO", contrato
					.getIdClassificacaoContrato().getDescricao()
					+ " - "
					+ contrato.getIdTipoContrato().getDescricao());

		
			parametros.put("SUBREPORT_DIR", pathSubreport);
			parametros.put("NUMERO_CONTRATO", contrato.getNumeroContrato());
			parametros.put("REGIONAL", contrato.getRegional());
			parametros.put("HORAS_VIGENCIA", totalHorasManutencao.intValue());
			parametros.put("NUM_VISITAS", numVisitas);
			parametros.put("INTERVENCAO_LIST", intervencaoListJRBaen);
			parametros.put("FABRICANTE", contrato.getFabricante());

			parametros.put("FILIAL", twFilial.getStnm().trim());
			parametros.put("RAZAO_SOCIAL", contrato.getRazaoSocial() + " / "
					+ contrato.getCodigoCliente());
			parametros.put("ENDERECO", contrato.getEndereco() + " / "
					+ contrato.getBairro());
			parametros.put("CIDADE",
					contrato.getCidade() + " / " + contrato.getUf());
			parametros.put("CEP", contrato.getCep());
			parametros.put("CNPJ", contrato.getCnpj());
			parametros.put("PROCURADOR", contrato.getProcurador());
			parametros.put("INSC_ESTADUAL", contrato.getInscEstadual());
			parametros.put("INSC_MUNICIPAL", contrato.getInscMunicipal());
			parametros.put("INSC_MUNICIPAL", contrato.getInscMunicipal());

			parametros.put("CONTRIBUINTE", contrato.getContribuinte());
			parametros.put("TELEFONE_COMERCIAL",
					contrato.getTelefoneComercial());
			parametros.put("CPF", contrato.getCpf());
			parametros.put("CONTATO_COMERCIAL", contrato.getContatoComercial());
			parametros.put("EMAIL_CONTATO_COMERCIAL",
					contrato.getEmailContatoComercial());
			parametros.put("FAX_COMERCIAL", contrato.getFaxComercial());
			parametros.put("CONTATO_SERVICOS", contrato.getContatoServicos());
			parametros.put("EMAIL_CONTATO_SERVICOS",
					contrato.getEmailContatoServicos());

			parametros.put("TELEFONE_SERVICOS", contrato.getTelefoneServicos());
			parametros.put("FAX_SERVICOS", contrato.getFaxServicos());
			parametros.put("MODELO", contrato.getModelo());
			parametros.put("FAMILIA", contrato.getFamilia());
			parametros.put("MUM_SERIE", contrato.getNumeroSerie());
			parametros.put("HORIMETRO", contrato.getHorimetro().toString());
			parametros.put("ADMINISTRATOR", Boolean.valueOf(true));
			parametros.put("DATA_ACEITE", contrato.getDataAceite());
			TwFuncionario funcionario = manager.find(TwFuncionario.class,
					contrato.getIdFuncionario());
			parametros.put("NOME_CONSULTOR", funcionario.getEplsnm());
			parametros.put("MATRICULA_CONSULTOR", funcionario.getEpidno());
			String parcelas = "";
			if (contrato.getQtdParcelas().equals(1L)) {
				parcelas = contrato.getQtdParcelas().toString();
			} else {
				parcelas = contrato.getQtdParcelas().toString();
			}
			if (contrato.getIdTipoContrato().getSigla().equals("CON")) {
				parcelas = "CONCESSÃO";
			}
			parametros.put("QTD_PARCELAS", parcelas);
			parametros.put("CONDICAO_EXCEPCIONAL",
					contrato.getCondicaoExcepcional());
			if (contrato.getValoContrato() != null) {
				parametros.put(
						"PRECO",
						"R$ "
								+ ValorMonetarioHelper.formata("###,###.00",
										contrato.getValoContrato()
												.doubleValue()));
				parametros.put(
						"VALOR_PARCELA",
						"R$ "
								+ ValorMonetarioHelper.formata("###,###.00",
										(contrato.getValoContrato()
												.doubleValue() / contrato
												.getQtdParcelas())));
				parametros.put(
						"CUSTO_HORA",
						"R$ "
								+ ValorMonetarioHelper.formata("###,###.00",
										(contrato.getValoContrato()
												.doubleValue() / totalHorasManutencao)));

			} else {
				parametros.put("PRECO", "");
				parametros.put("VALOR_PARCELA", "");
			}
			if (contrato.getTa().equals("S")) {
				parametros.put("HORAS_CONTRATADA",
						horasManut.substring(0, horasManut.length() - 1));
			} else {
				parametros.put("HORAS_CONTRATADAS_SEM_TA",
						horasManut.substring(0, horasManut.length() - 1));
			}

			if(contrato.getCodigoCliente().equals("0016853") || contrato.getCodigoCliente().equals("0016854") || contrato.getCodigoCliente().equals("0020685")
					|| contrato.getCodigoCliente().equals("0021835") || contrato.getCodigoCliente().equals("0057216") || contrato.getCodigoCliente().equals("0578053")
					|| contrato.getCodigoCliente().equals("0668516") || contrato.getCodigoCliente().equals("1008080") || contrato.getCodigoCliente().equals("1008081")
					|| contrato.getCodigoCliente().equals("1008079") || contrato.getCodigoCliente().equals("1013931") || contrato.getCodigoCliente().equals("0884531")
					|| contrato.getCodigoCliente().equals("1019028")){
				
				parametros.put("revisaoPecasDataSource", new JRBeanCollectionDataSource(new ArrayList()));
				parametros.put("PRINT_PECAS", "N");
			}else{
				parametros.put("revisaoPecasDataSource", revisaoPecasDataSource);
				parametros.put("PRINT_PECAS", "S");
				
			}
			// InputStream inputStream =
			// getClass().getClassLoader().getResourceAsStream("logo.jpg");

			// File img=File.createTempFile("img", "jpg",new File("."));
			//
			// OutputStream out=new FileOutputStream(img);
			// byte buf[]=new byte[1024];
			// int len;
			// while((len=inputStream.read(buf))>0)
			// out.write(buf,0,len);
			// out.close();
			// inputStream.close();
			// parametros.put("logo", img);

			// Gera o pdf para exibicao..
			try {
				pdfProposta = JasperRunManager.runReportToPdf(jasperReport,
						parametros, new JREmptyDataSource());
			} catch (Exception jre) {
				jre.printStackTrace();
			}

			// Parametros para nao fazer cache e o que será exibido..
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=contrato_pmp.pdf");

			// Envia para o navegador o pdf..
			ServletOutputStream servletOutputStream = response
					.getOutputStream();
			servletOutputStream.write(pdfProposta);
			servletOutputStream.flush();
			servletOutputStream.close();
			// img.delete();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (manager != null && manager.isOpen()) {
				manager.close();
			}
		}
	}

	private void generateContratoPlus(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			FileNotFoundException {
		String idContrato = request.getParameter("idContrato");
		// String isAdministrador = request.getParameter("isAdministrador");
		// String tipoContrato = request.getParameter("tipoContrato");

		JasperReport jasperReport = null;
		byte[] pdfProposta = null;
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			PmpContrato contrato = manager.find(PmpContrato.class,
					Long.valueOf(idContrato));
			// Obtem o caminho do .jasper
			ServletContext servletContext = super.getServletContext();
			String caminhoJasper = "";
			String pathSubreport = "";
			UsuarioBean bean = (UsuarioBean) request.getSession().getAttribute(
					"usuario");
			if (bean == null) {
				response.sendRedirect("/ControlPanelPesa/ControlPanel.html");
				return;
			}
			caminhoJasper = servletContext
					.getRealPath("WEB-INF/contrato_pm_consultor_plus/pmp_contrato.jasper");
			pathSubreport = servletContext
					.getRealPath("WEB-INF/contrato_pm_consultor_plus/") + "/";
			// Carrega o arquivo com o jasperReport
			try {
				jasperReport = (JasperReport) JRLoader
						.loadObject(caminhoJasper);
			} catch (Exception jre) {
				jre.printStackTrace();
			}
			// BigDecimal custoPecas = BigDecimal.ZERO;
			TwFilial twFilial = new TwFilial();

			twFilial = manager.find(TwFilial.class, contrato.getFilial()
					.longValue());

			Map parametros = new HashMap();

			parametros.put("COMPLEMENTO_TITULO", contrato
					.getIdClassificacaoContrato().getDescricao()
					+ " - "
					+ contrato.getIdTipoContrato().getDescricao());
			parametros.put("VALIDADE", contrato.getAnoContrato().intValue());

			parametros.put("SUBREPORT_DIR", pathSubreport);
			parametros.put("NUMERO_CONTRATO", contrato.getNumeroContrato());
			parametros.put("REGIONAL", contrato.getRegional());
			parametros.put("FABRICANTE", contrato.getFabricante());

			parametros.put("FILIAL", twFilial.getStnm().trim());
			parametros.put("RAZAO_SOCIAL", contrato.getRazaoSocial() + " / "
					+ contrato.getCodigoCliente());
			parametros.put("ENDERECO", contrato.getEndereco() + " / "
					+ contrato.getBairro());
			parametros.put("CIDADE",
					contrato.getCidade() + " / " + contrato.getUf());
			parametros.put("CEP", contrato.getCep());
			parametros.put("CNPJ", contrato.getCnpj());
			parametros.put("PROCURADOR", contrato.getProcurador());
			parametros.put("INSC_ESTADUAL", contrato.getInscEstadual());
			parametros.put("INSC_MUNICIPAL", contrato.getInscMunicipal());
			parametros.put("INSC_MUNICIPAL", contrato.getInscMunicipal());

			parametros.put("CONTRIBUINTE", contrato.getContribuinte());
			parametros.put("TELEFONE_COMERCIAL",
					contrato.getTelefoneComercial());
			parametros.put("CPF", contrato.getCpf());
			parametros.put("CONTATO_COMERCIAL", contrato.getContatoComercial());
			parametros.put("EMAIL_CONTATO_COMERCIAL",
					contrato.getEmailContatoComercial());
			parametros.put("FAX_COMERCIAL", contrato.getFaxComercial());
			parametros.put("CONTATO_SERVICOS", contrato.getContatoServicos());
			parametros.put("EMAIL_CONTATO_SERVICOS",
					contrato.getEmailContatoServicos());

			parametros.put("TELEFONE_SERVICOS", contrato.getTelefoneServicos());
			parametros.put("FAX_SERVICOS", contrato.getFaxServicos());
			parametros.put("MODELO", contrato.getModelo());
			parametros.put("FAMILIA", contrato.getFamilia());
			parametros.put("MUM_SERIE", contrato.getNumeroSerie());
			parametros.put("HORIMETRO", contrato.getHorimetro().toString());
			parametros.put("ADMINISTRATOR", Boolean.valueOf(true));
			parametros.put("DATA_ACEITE", contrato.getDataAceite());
			TwFuncionario funcionario = manager.find(TwFuncionario.class,
					contrato.getIdFuncionario());
			parametros.put("NOME_CONSULTOR", funcionario.getEplsnm());
			parametros.put("MATRICULA_CONSULTOR", funcionario.getEpidno());
			String parcelas = "";
			if (contrato.getQtdParcelas().equals(1L)) {
				parcelas = contrato.getQtdParcelas().toString();
			} else {
				parcelas = contrato.getQtdParcelas().toString();
			}
			if (contrato.getIdTipoContrato().getSigla().equals("CON")) {
				parcelas = "CONCESSÃO";
			}
			parametros.put("QTD_PARCELAS", parcelas);
			parametros.put("CONDICAO_EXCEPCIONAL",
					contrato.getCondicaoExcepcional());
			if (contrato.getValoContrato() != null) {
				parametros.put(
						"PRECO",
						"R$ "
								+ ValorMonetarioHelper.formata("###,###.00",
										contrato.getValoContrato()
												.doubleValue()));
				parametros.put(
						"VALOR_PARCELA",
						"R$ "
								+ ValorMonetarioHelper.formata("###,###.00",
										(contrato.getValoContrato()
												.doubleValue() / contrato
												.getQtdParcelas())));

			} else {
				parametros.put("PRECO", "");
				parametros.put("VALOR_PARCELA", "");
			}

			// Gera o pdf para exibicao..
			try {
				pdfProposta = JasperRunManager.runReportToPdf(jasperReport,
						parametros, new JREmptyDataSource());
			} catch (Exception jre) {
				jre.printStackTrace();
			}

			// Parametros para nao fazer cache e o que será exibido..
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=contrato_pmp.pdf");

			// Envia para o navegador o pdf..
			ServletOutputStream servletOutputStream = response
					.getOutputStream();
			servletOutputStream.write(pdfProposta);
			servletOutputStream.flush();
			servletOutputStream.close();
			// img.delete();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (manager != null && manager.isOpen()) {
				manager.close();
			}
		}
	}

	private void generateContratoPMRENGerador(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			FileNotFoundException {
		String idContrato = request.getParameter("idContrato");
		// String isAdministrador = request.getParameter("isAdministrador");
		// String tipoContrato = request.getParameter("tipoContrato");

		JasperReport jasperReport = null;
		byte[] pdfProposta = null;
		EntityManager manager = JpaUtil.getInstance();
		PmpContrato contrato = manager.find(PmpContrato.class,
				Long.valueOf(idContrato));
		// Obtem o caminho do .jasper
		ServletContext servletContext = super.getServletContext();
		String caminhoJasper = "";
		String pathSubreport = "";
		UsuarioBean bean = (UsuarioBean) request.getSession().getAttribute(
				"usuario");
		if (bean == null) {
			response.sendRedirect("/ControlPanel/ControlPanel.html");
			return;
		}

		// if(isAdministrador.equals("ADM")){
		// caminhoJasper =
		// servletContext.getRealPath("WEB-INF/contrato_pm_juridico/pmp_contrato.jasper");
		// pathSubreport =
		// servletContext.getRealPath("WEB-INF/contrato_pm_juridico/")+"/";
		// }else{
		caminhoJasper = servletContext
				.getRealPath("WEB-INF/contrato_pm_consultor/pmp_contrato.jasper");
		pathSubreport = servletContext
				.getRealPath("WEB-INF/contrato_pm_consultor/") + "/";

		// }
		// Carrega o arquivo com o jasperReport
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(caminhoJasper);
		} catch (Exception jre) {
			jre.printStackTrace();
		}

		// TwFuncionario funcionario = new TwFuncionario();

		// Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		// BigDecimal custoPecas = BigDecimal.ZERO;
		TwFilial twFilial = new TwFilial();
		try {

			twFilial = manager.find(TwFilial.class, contrato.getFilial()
					.longValue());
			Long frequencia = 0l;
			// if(!contrato.getIdTipoContrato().getSigla().equals("REN")){
			// BigDecimal custoNordeste =
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager
					.createQuery("From PmpContMesesStandard where idContrato.id = :id");
			query.setParameter("id", contrato.getId());
			List<PmpContMesesStandard> horasStandardList = query
					.getResultList();
			frequencia = horasStandardList.get(0).getFrequencia();
			totalHorasManutencao = frequencia * horasStandardList.size();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			String horasManut = "";
			Query query = manager
					.createQuery(" From PmpContMesesStandard where idContrato.id =:id");
			query.setParameter("id", contrato.getId());
			List<PmpContMesesStandard> list = query.getResultList();
			for (PmpContMesesStandard pmpContMesesStandard : list) {
				horasManut += pmpContMesesStandard.getMesManutencao() + "M/";
			}

			Map parametros = new HashMap();
			parametros.put("SUBREPORT_DIR", pathSubreport);
			parametros.put("NUMERO_CONTRATO", contrato.getNumeroContrato());
			parametros.put("REGIONAL", contrato.getRegional());

			parametros.put("FILIAL", twFilial.getStnm().trim());
			parametros.put("RAZAO_SOCIAL", contrato.getRazaoSocial() + " / "
					+ contrato.getCodigoCliente());
			parametros.put("ENDERECO", contrato.getEndereco() + " / "
					+ contrato.getBairro());
			parametros.put("CIDADE",
					contrato.getCidade() + " / " + contrato.getUf());
			parametros.put("CEP", contrato.getCep());
			parametros.put("CNPJ", contrato.getCnpj());
			parametros.put("PROCURADOR", contrato.getProcurador());
			parametros.put("INSC_ESTADUAL", contrato.getInscEstadual());
			parametros.put("INSC_MUNICIPAL", contrato.getInscMunicipal());
			parametros.put("INSC_MUNICIPAL", contrato.getInscMunicipal());

			parametros.put("CONTRIBUINTE", contrato.getContribuinte());
			parametros.put("TELEFONE_COMERCIAL",
					contrato.getTelefoneComercial());
			parametros.put("CPF", contrato.getCpf());
			parametros.put("CONTATO_COMERCIAL", contrato.getContatoComercial());
			parametros.put("EMAIL_CONTATO_COMERCIAL",
					contrato.getEmailContatoComercial());
			parametros.put("FAX_COMERCIAL", contrato.getFaxComercial());
			parametros.put("CONTATO_SERVICOS", contrato.getContatoServicos());
			parametros.put("EMAIL_CONTATO_SERVICOS",
					contrato.getEmailContatoServicos());

			parametros.put("TELEFONE_SERVICOS", contrato.getTelefoneServicos());
			parametros.put("FAX_SERVICOS", contrato.getFaxServicos());
			parametros.put("MODELO", contrato.getModelo());
			parametros.put("FAMILIA", contrato.getFamilia());
			parametros.put("MUM_SERIE", contrato.getNumeroSerie());
			parametros.put("HORIMETRO", contrato.getHorimetro().toString());
			parametros.put("ADMINISTRATOR", Boolean.valueOf(true));
			parametros.put("DATA_ACEITE", contrato.getDataAceite());
			TwFuncionario funcionario = manager.find(TwFuncionario.class,
					contrato.getIdFuncionario());
			parametros.put("NOME_CONSULTOR", funcionario.getEplsnm());
			parametros.put("MATRICULA_CONSULTOR", funcionario.getEpidno());
			String parcelas = "";
			if (contrato.getQtdParcelas().equals(1L)) {
				parcelas = contrato.getQtdParcelas() + " À vista";
			} else {
				parcelas = contrato.getQtdParcelas()
						+ " parcelas mensais, iguais e sucessivas";
			}
			if (contrato.getIdTipoContrato().getSigla().equals("CON")) {
				parcelas = "CONCESSÃO";
			}
			parametros.put("QTD_PARCELAS", parcelas);
			parametros.put("CONDICAO_EXCEPCIONAL",
					contrato.getCondicaoExcepcional());
			parametros.put(
					"PRECO",
					"R$ "
							+ ValorMonetarioHelper.formata("###,###.00",
									contrato.getValoContrato().doubleValue()));
			if (contrato.getTa().equals("S")) {
				parametros.put("HORAS_CONTRATADA",
						horasManut.substring(0, horasManut.length() - 1));
			} else {
				parametros.put("HORAS_CONTRATADAS_SEM_TA",
						horasManut.substring(0, horasManut.length() - 1));
			}
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("cabecalho_contrato.PNG");

			File img = File.createTempFile("img", "gif", new File("."));

			OutputStream out = new FileOutputStream(img);
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0)
				out.write(buf, 0, len);
			out.close();
			inputStream.close();
			parametros.put("logo", img);

			// Gera o pdf para exibicao..
			try {
				pdfProposta = JasperRunManager.runReportToPdf(jasperReport,
						parametros, new JREmptyDataSource());
			} catch (Exception jre) {
				jre.printStackTrace();
			}

			// Parametros para nao fazer cache e o que será exibido..
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=proposta.pdf");

			// Envia para o navegador o pdf..
			ServletOutputStream servletOutputStream = response
					.getOutputStream();
			servletOutputStream.write(pdfProposta);
			servletOutputStream.flush();
			servletOutputStream.close();
			img.delete();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (manager != null && manager.isOpen()) {
				manager.close();
			}
		}
	}

	private void generateContratoJuridico(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			FileNotFoundException {

		JasperReport jasperReport = null;
		byte[] pdfProposta = null;

		// Obtem o caminho do .jasper
		ServletContext servletContext = super.getServletContext();
		String caminhoJasper = "";
		String pathSubreport = "";
		UsuarioBean bean = (UsuarioBean) request.getSession().getAttribute(
				"usuario");
		if (bean == null) {
			response.sendRedirect("/ControlPanel/ControlPanel.html");
			return;
		}

		caminhoJasper = servletContext
				.getRealPath("WEB-INF/contrato_pm_juridico/pmp_contrato.jasper");
		pathSubreport = servletContext
				.getRealPath("WEB-INF/contrato_pm_juridico/") + "/";

		// }
		// Carrega o arquivo com o jasperReport
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(caminhoJasper);
		} catch (Exception jre) {
			jre.printStackTrace();
		}

		try {
			Map parametros = new HashMap();
			parametros.put("SUBREPORT_DIR", pathSubreport);

			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("cabecalho_contrato.PNG");

			File img = File.createTempFile("img", "gif", new File("."));

			OutputStream out = new FileOutputStream(img);
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0)
				out.write(buf, 0, len);
			out.close();
			inputStream.close();
			parametros.put("logo", img);

			// Gera o pdf para exibicao..
			try {
				pdfProposta = JasperRunManager.runReportToPdf(jasperReport,
						parametros, new JREmptyDataSource());
			} catch (Exception jre) {
				jre.printStackTrace();
			}

			// Parametros para nao fazer cache e o que será exibido..
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=proposta.pdf");

			// Envia para o navegador o pdf..
			ServletOutputStream servletOutputStream = response
					.getOutputStream();
			servletOutputStream.write(pdfProposta);
			servletOutputStream.flush();
			servletOutputStream.close();
			img.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateContratoKIT3(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			FileNotFoundException {
		String idContrato = request.getParameter("idContrato");

		JasperReport jasperReport = null;
		byte[] pdfProposta = null;

		// Obtem o caminho do .jasper
		ServletContext servletContext = super.getServletContext();
		String caminhoJasper = servletContext
				.getRealPath("WEB-INF/comtrato_pm_3/pmp_contrato_3.jasper");
		String pathSubreport = servletContext
				.getRealPath("WEB-INF/comtrato_pm_3/") + "/";

		// Carrega o arquivo com o jasperReport
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(caminhoJasper);
		} catch (Exception jre) {
			jre.printStackTrace();
		}
		EntityManager manager = null;
		// TwFuncionario funcionario = new TwFuncionario();
		PmpContrato contrato = new PmpContrato();
		// Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		// BigDecimal custoPecas = BigDecimal.ZERO;
		TwFilial twFilial = new TwFilial();
		try {
			manager = JpaUtil.getInstance();
			contrato = manager
					.find(PmpContrato.class, Long.valueOf(idContrato));
			twFilial = manager.find(TwFilial.class, contrato.getFilial()
					.longValue());
			Long frequencia = 0l;
			// if(!contrato.getIdTipoContrato().getSigla().equals("REN")){
			// BigDecimal custoNordeste =
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager
					.createQuery("From PmpContHorasStandard where idContrato.id = :id");
			query.setParameter("id", contrato.getId());
			List<PmpContHorasStandard> horasStandardList = query
					.getResultList();
			frequencia = horasStandardList.get(0).getFrequencia();
			totalHorasManutencao = frequencia * horasStandardList.size();
			// for (PmpContHorasStandard pmpContHorasStandard :
			// horasStandardList) {
			// frequencia = pmpContHorasStandard.getFrequencia();
			// //Query para peças com desconto PDP
			// query =
			// manager.createNativeQuery("select sum(m.unls) from pmp_manutencao m"+
			// " where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
			// " and m.bgrp = '"+contrato.getBgrp()+"'"+
			// " and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
			// " and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4,
			// 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
			// " and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
			// custoPecas = (BigDecimal)query.getSingleResult();
			// Double custoNordesteTotal = custoPecas.doubleValue() +
			// ((custoPecas.doubleValue() *
			// custoNordeste.doubleValue())/100);//aplicar custo nordeste
			// custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal *
			// (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto
			// PDP
			// custoManutencao = custoManutencao + custoNordesteTotal;
			//
			// //Query sem desconto pdp
			// query =
			// manager.createNativeQuery("select sum(m.unls) from pmp_manutencao m"+
			// " where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
			// " and m.bgrp = '"+contrato.getBgrp()+"'"+
			// " and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
			// " and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4,
			// 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
			// " and m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)");
			// custoPecas = (BigDecimal)query.getSingleResult();
			// custoNordesteTotal = custoPecas.doubleValue() +
			// ((custoPecas.doubleValue() *
			// custoNordeste.doubleValue())/100);//aplicar custo nordeste
			// custoManutencao = custoManutencao + custoNordesteTotal;
			// }
			// //int totalManutencoes = 0;
			//
			// totalHorasManutencao = frequencia * horasStandardList.size();
			//
			// //custo do TA
			// if(contrato.getTa().equals("S")){
			// Double taCusto =
			// (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue()
			// *
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
			// taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;//
			// total de revisoes de 2000 horas * custo do TA
			// custoManutencao = custoManutencao + taCusto;
			// }
			//
			//
			// //total horas para a manutenção
			// query =
			// manager.createNativeQuery("select sum (to_number(replace(h.frsdhr,'.',','))) from pmp_hora h"+
			// " where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId()+")"+
			// " and h.bgrp = '"+contrato.getBgrp()+"'"+
			// " and substr(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
			// " and substr(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4,
			// 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
			// BigDecimal totalHHManutencao =
			// (BigDecimal)query.getSingleResult();
			// custoManutencao = custoManutencao +
			// (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue()
			// * totalHHManutencao.doubleValue());//valor de hh
			// custoManutencao = custoManutencao +
			// ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue()
			// *
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())*horasStandardList.size());//valor
			// de km vezes a quantidade de manutenções
			// funcionario = (TwFuncionario)manager.find(TwFuncionario.class,
			// contrato.getIdFuncionario());
			//
			// custoManutencao = jurosContrato(contrato, custoManutencao);
			//
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			Map parametros = new HashMap();
			parametros.put("SUBREPORT_DIR", pathSubreport);
			parametros.put("NUMERO_CONTRATO", contrato.getNumeroContrato());
			parametros.put("REGIONAL", contrato.getRegional());

			parametros.put("FILIAL", twFilial.getStnm().trim());
			parametros.put("RAZAO_SOCIAL", contrato.getRazaoSocial());
			parametros.put("CNPJ", contrato.getCnpj());
			parametros.put("PROCURADOR", contrato.getProcurador());
			parametros.put("INSC_ESTADUAL", contrato.getInscEstadual());
			parametros.put("INSC_MUNICIPAL", contrato.getInscMunicipal());
			parametros.put("INSC_MUNICIPAL", contrato.getInscMunicipal());

			parametros.put("CONTRIBUINTE", contrato.getContribuinte());
			parametros.put("TELEFONE_COMERCIAL",
					contrato.getTelefoneComercial());
			parametros.put("CPF", contrato.getCpf());
			parametros.put("CONTATO_COMERCIAL", contrato.getContatoComercial());
			parametros.put("EMAIL_CONTATO_COMERCIAL",
					contrato.getEmailContatoComercial());
			parametros.put("FAX_COMERCIAL", contrato.getFaxComercial());
			parametros.put("CONTATO_SERVICOS", contrato.getContatoServicos());
			parametros.put("EMAIL_CONTATO_SERVICOS",
					contrato.getEmailContatoServicos());

			parametros.put("TELEFONE_SERVICOS", contrato.getTelefoneServicos());
			parametros.put("FAX_SERVICOS", contrato.getFaxServicos());
			parametros.put("MODELO", contrato.getModelo());
			parametros.put("FAMILIA", contrato.getModelo());
			parametros.put("MUM_SERIE", contrato.getNumeroSerie());
			parametros.put("HORIMETRO", contrato.getHorimetro().toString());
			parametros.put("HORAS_CONTRATADA", totalHorasManutencao.toString()
					+ " Horas");

			parametros.put("DATA_ACEITE", contrato.getDataAceite());
			parametros
					.put("QTD_PARCELAS", contrato.getQtdParcelas().toString());
			parametros.put("CONDICAO_EXCEPCIONAL",
					contrato.getCondicaoExcepcional());
			parametros.put(
					"PRECO",
					"R$ "
							+ ValorMonetarioHelper.formata("###,###.00",
									contrato.getValoContrato().doubleValue()));

			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("cabecalho_contrato.PNG");

			File img = File.createTempFile("img", "gif", new File("."));

			OutputStream out = new FileOutputStream(img);
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0)
				out.write(buf, 0, len);
			out.close();
			inputStream.close();
			parametros.put("logo", img);

			// Gera o pdf para exibicao..
			try {
				pdfProposta = JasperRunManager.runReportToPdf(jasperReport,
						parametros, new JREmptyDataSource());
			} catch (Exception jre) {
				jre.printStackTrace();
			}

			// Parametros para nao fazer cache e o que será exibido..
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=proposta.pdf");

			// Envia para o navegador o pdf..
			ServletOutputStream servletOutputStream = response
					.getOutputStream();
			servletOutputStream.write(pdfProposta);
			servletOutputStream.flush();
			servletOutputStream.close();
			img.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateContratoKIT3Gerador(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			FileNotFoundException {
		String idContrato = request.getParameter("idContrato");

		JasperReport jasperReport = null;
		byte[] pdfProposta = null;

		// Obtem o caminho do .jasper
		ServletContext servletContext = super.getServletContext();
		String caminhoJasper = servletContext
				.getRealPath("WEB-INF/comtrato_pm_3/pmp_contrato_3.jasper");
		String pathSubreport = servletContext
				.getRealPath("WEB-INF/comtrato_pm_3/") + "/";

		// Carrega o arquivo com o jasperReport
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(caminhoJasper);
		} catch (Exception jre) {
			jre.printStackTrace();
		}
		EntityManager manager = null;
		// TwFuncionario funcionario = new TwFuncionario();
		PmpContrato contrato = new PmpContrato();
		// Double custoManutencao = 0d;
		Long totalMesesManutencao = 0l;
		// BigDecimal custoPecas = BigDecimal.ZERO;
		TwFilial twFilial = new TwFilial();
		try {
			manager = JpaUtil.getInstance();
			contrato = manager
					.find(PmpContrato.class, Long.valueOf(idContrato));
			twFilial = manager.find(TwFilial.class, contrato.getFilial()
					.longValue());
			Long frequencia = 0l;
			// if(!contrato.getIdTipoContrato().getSigla().equals("REN")){
			// BigDecimal custoNordeste =
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager
					.createQuery("From PmpContMesesStandard where idContrato.id = :id");
			query.setParameter("id", contrato.getId());
			List<PmpContMesesStandard> mesesStandardList = query
					.getResultList();
			frequencia = mesesStandardList.get(0).getFrequencia();
			totalMesesManutencao = frequencia * mesesStandardList.size();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			Map parametros = new HashMap();
			parametros.put("SUBREPORT_DIR", pathSubreport);
			parametros.put("NUMERO_CONTRATO", contrato.getNumeroContrato());
			parametros.put("REGIONAL", contrato.getRegional());

			parametros.put("FILIAL", twFilial.getStnm().trim());
			parametros.put("RAZAO_SOCIAL", contrato.getRazaoSocial());
			parametros.put("CNPJ", contrato.getCnpj());
			parametros.put("PROCURADOR", contrato.getProcurador());
			parametros.put("INSC_ESTADUAL", contrato.getInscEstadual());
			parametros.put("INSC_MUNICIPAL", contrato.getInscMunicipal());
			parametros.put("INSC_MUNICIPAL", contrato.getInscMunicipal());

			parametros.put("CONTRIBUINTE", contrato.getContribuinte());
			parametros.put("TELEFONE_COMERCIAL",
					contrato.getTelefoneComercial());
			parametros.put("CPF", contrato.getCpf());
			parametros.put("CONTATO_COMERCIAL", contrato.getContatoComercial());
			parametros.put("EMAIL_CONTATO_COMERCIAL",
					contrato.getEmailContatoComercial());
			parametros.put("FAX_COMERCIAL", contrato.getFaxComercial());
			parametros.put("CONTATO_SERVICOS", contrato.getContatoServicos());
			parametros.put("EMAIL_CONTATO_SERVICOS",
					contrato.getEmailContatoServicos());

			parametros.put("TELEFONE_SERVICOS", contrato.getTelefoneServicos());
			parametros.put("FAX_SERVICOS", contrato.getFaxServicos());
			parametros.put("MODELO", contrato.getModelo());
			parametros.put("FAMILIA", contrato.getModelo());
			parametros.put("MUM_SERIE", contrato.getNumeroSerie());
			parametros.put("HORIMETRO", contrato.getHorimetro().toString());
			parametros.put("HORAS_CONTRATADA", totalMesesManutencao.toString()
					+ " Horas");

			parametros.put("DATA_ACEITE", contrato.getDataAceite());
			parametros
					.put("QTD_PARCELAS", contrato.getQtdParcelas().toString());
			parametros.put("CONDICAO_EXCEPCIONAL",
					contrato.getCondicaoExcepcional());
			parametros.put(
					"PRECO",
					"R$ "
							+ ValorMonetarioHelper.formata("###,###.00",
									contrato.getValoContrato().doubleValue()));

			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("cabecalho_contrato.PNG");

			File img = File.createTempFile("img", "gif", new File("."));

			OutputStream out = new FileOutputStream(img);
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0)
				out.write(buf, 0, len);
			out.close();
			inputStream.close();
			parametros.put("logo", img);

			// Gera o pdf para exibicao..
			try {
				pdfProposta = JasperRunManager.runReportToPdf(jasperReport,
						parametros, new JREmptyDataSource());
			} catch (Exception jre) {
				jre.printStackTrace();
			}

			// Parametros para nao fazer cache e o que será exibido..
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=proposta.pdf");

			// Envia para o navegador o pdf..
			ServletOutputStream servletOutputStream = response
					.getOutputStream();
			servletOutputStream.write(pdfProposta);
			servletOutputStream.flush();
			servletOutputStream.close();
			img.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateContratoKIT2(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			FileNotFoundException {
		String idContrato = request.getParameter("idContrato");

		JasperReport jasperReport = null;
		byte[] pdfProposta = null;

		// Obtem o caminho do .jasper
		ServletContext servletContext = super.getServletContext();
		String caminhoJasper = servletContext
				.getRealPath("WEB-INF/comtrato_pm_2/pmp_contrato_2.jasper");
		String pathSubreport = servletContext
				.getRealPath("WEB-INF/comtrato_pm_2/") + "/";

		// Carrega o arquivo com o jasperReport
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(caminhoJasper);
		} catch (Exception jre) {
			jre.printStackTrace();
		}
		EntityManager manager = null;
		// TwFuncionario funcionario = new TwFuncionario();
		PmpContrato contrato = new PmpContrato();
		// Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		// BigDecimal custoPecas = BigDecimal.ZERO;
		TwFilial twFilial = new TwFilial();
		try {
			manager = JpaUtil.getInstance();
			contrato = manager
					.find(PmpContrato.class, Long.valueOf(idContrato));
			twFilial = manager.find(TwFilial.class, contrato.getFilial()
					.longValue());
			Long frequencia = 0l;
			// if(!contrato.getIdTipoContrato().getSigla().equals("REN")){
			// BigDecimal custoNordeste =
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager
					.createQuery("From PmpContHorasStandard where idContrato.id = :id");
			query.setParameter("id", contrato.getId());
			List<PmpContHorasStandard> horasStandardList = query
					.getResultList();
			frequencia = horasStandardList.get(0).getFrequencia();
			totalHorasManutencao = frequencia * horasStandardList.size();
			// for (PmpContHorasStandard pmpContHorasStandard :
			// horasStandardList) {
			// frequencia = pmpContHorasStandard.getFrequencia();
			// //Query para peças com desconto PDP
			// query =
			// manager.createNativeQuery("select sum(m.unls) from pmp_manutencao m"+
			// " where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
			// " and m.bgrp = '"+contrato.getBgrp()+"'"+
			// " and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
			// " and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4,
			// 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
			// " and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
			// custoPecas = (BigDecimal)query.getSingleResult();
			// Double custoNordesteTotal = custoPecas.doubleValue() +
			// ((custoPecas.doubleValue() *
			// custoNordeste.doubleValue())/100);//aplicar custo nordeste
			// custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal *
			// (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto
			// PDP
			// custoManutencao = custoManutencao + custoNordesteTotal;
			//
			// //Query sem desconto pdp
			// query =
			// manager.createNativeQuery("select sum(m.unls) from pmp_manutencao m"+
			// " where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
			// " and m.bgrp = '"+contrato.getBgrp()+"'"+
			// " and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
			// " and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4,
			// 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
			// " and m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)");
			// custoPecas = (BigDecimal)query.getSingleResult();
			// custoNordesteTotal = custoPecas.doubleValue() +
			// ((custoPecas.doubleValue() *
			// custoNordeste.doubleValue())/100);//aplicar custo nordeste
			// custoManutencao = custoManutencao + custoNordesteTotal;
			// }
			// //int totalManutencoes = 0;
			//
			// totalHorasManutencao = frequencia * horasStandardList.size();
			//
			// //custo do TA
			// if(contrato.getTa().equals("S")){
			// Double taCusto =
			// (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue()
			// *
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
			// taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;//
			// total de revisoes de 2000 horas * custo do TA
			// custoManutencao = custoManutencao + taCusto;
			// }
			//
			//
			// //total horas para a manutenção
			// query =
			// manager.createNativeQuery("select sum (to_number(replace(h.frsdhr,'.',','))) from pmp_hora h"+
			// " where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId()+")"+
			// " and h.bgrp = '"+contrato.getBgrp()+"'"+
			// " and substr(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
			// " and substr(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4,
			// 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'" +
			// " and h.cptcd = 7504");
			// BigDecimal totalHHManutencao =
			// (BigDecimal)query.getSingleResult();
			// custoManutencao = custoManutencao +
			// (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue()
			// * totalHHManutencao.doubleValue());//valor de hh
			// custoManutencao = custoManutencao +
			// ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue()
			// *
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())*1);//valor
			// de km vezes a quantidade de manutenções
			// funcionario = (TwFuncionario)manager.find(TwFuncionario.class,
			// contrato.getIdFuncionario());
			//
			// custoManutencao = jurosContrato(contrato, custoManutencao);
			//
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			Map parametros = new HashMap();
			parametros.put("SUBREPORT_DIR", pathSubreport);
			parametros.put("NUMERO_CONTRATO", contrato.getNumeroContrato());
			parametros.put("REGIONAL", contrato.getRegional());

			parametros.put("FILIAL", twFilial.getStnm().trim());
			parametros.put("RAZAO_SOCIAL", contrato.getRazaoSocial());
			parametros.put("CNPJ", contrato.getCnpj());
			parametros.put("PROCURADOR", contrato.getProcurador());
			parametros.put("INSC_ESTADUAL", contrato.getInscEstadual());
			parametros.put("INSC_MUNICIPAL", contrato.getInscMunicipal());
			parametros.put("INSC_MUNICIPAL", contrato.getInscMunicipal());

			parametros.put("CONTRIBUINTE", contrato.getContribuinte());
			parametros.put("TELEFONE_COMERCIAL",
					contrato.getTelefoneComercial());
			parametros.put("CPF", contrato.getCpf());
			parametros.put("CONTATO_COMERCIAL", contrato.getContatoComercial());
			parametros.put("EMAIL_CONTATO_COMERCIAL",
					contrato.getEmailContatoComercial());
			parametros.put("FAX_COMERCIAL", contrato.getFaxComercial());
			parametros.put("CONTATO_SERVICOS", contrato.getContatoServicos());
			parametros.put("EMAIL_CONTATO_SERVICOS",
					contrato.getEmailContatoServicos());

			parametros.put("TELEFONE_SERVICOS", contrato.getTelefoneServicos());
			parametros.put("FAX_SERVICOS", contrato.getFaxServicos());
			parametros.put("MODELO", contrato.getModelo());
			parametros.put("FAMILIA", contrato.getModelo());
			parametros.put("MUM_SERIE", contrato.getNumeroSerie());
			parametros.put("HORIMETRO", contrato.getHorimetro().toString());
			parametros.put("HORAS_CONTRATADA", totalHorasManutencao.toString()
					+ " Horas");

			parametros.put("DATA_ACEITE", contrato.getDataAceite());
			parametros
					.put("QTD_PARCELAS", contrato.getQtdParcelas().toString());
			parametros.put("CONDICAO_EXCEPCIONAL",
					contrato.getCondicaoExcepcional());
			parametros.put(
					"PRECO",
					"R$ "
							+ ValorMonetarioHelper.formata("###,###.00",
									contrato.getValoContrato().doubleValue()));

			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("cabecalho_contrato.PNG");

			File img = File.createTempFile("img", "gif", new File("."));

			OutputStream out = new FileOutputStream(img);
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0)
				out.write(buf, 0, len);
			out.close();
			inputStream.close();
			parametros.put("logo", img);

			// Gera o pdf para exibicao..
			try {
				pdfProposta = JasperRunManager.runReportToPdf(jasperReport,
						parametros, new JREmptyDataSource());
			} catch (Exception jre) {
				jre.printStackTrace();
			}

			// Parametros para nao fazer cache e o que será exibido..
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=proposta.pdf");

			// Envia para o navegador o pdf..
			ServletOutputStream servletOutputStream = response
					.getOutputStream();
			servletOutputStream.write(pdfProposta);
			servletOutputStream.flush();
			servletOutputStream.close();
			img.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateContratoKIT2Gerador(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			FileNotFoundException {
		String idContrato = request.getParameter("idContrato");

		JasperReport jasperReport = null;
		byte[] pdfProposta = null;

		// Obtem o caminho do .jasper
		ServletContext servletContext = super.getServletContext();
		String caminhoJasper = servletContext
				.getRealPath("WEB-INF/comtrato_pm_2/pmp_contrato_2.jasper");
		String pathSubreport = servletContext
				.getRealPath("WEB-INF/comtrato_pm_2/") + "/";

		// Carrega o arquivo com o jasperReport
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(caminhoJasper);
		} catch (Exception jre) {
			jre.printStackTrace();
		}
		EntityManager manager = null;
		// TwFuncionario funcionario = new TwFuncionario();
		PmpContrato contrato = new PmpContrato();
		// Double custoManutencao = 0d;
		Long totalMesesManutencao = 0l;
		// BigDecimal custoPecas = BigDecimal.ZERO;
		TwFilial twFilial = new TwFilial();
		try {
			manager = JpaUtil.getInstance();
			contrato = manager
					.find(PmpContrato.class, Long.valueOf(idContrato));
			twFilial = manager.find(TwFilial.class, contrato.getFilial()
					.longValue());
			Long frequencia = 0l;
			// if(!contrato.getIdTipoContrato().getSigla().equals("REN")){
			// BigDecimal custoNordeste =
			// contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager
					.createQuery("From PmpContMesesStandard where idContrato.id = :id");
			query.setParameter("id", contrato.getId());
			List<PmpContMesesStandard> mesesStandardList = query
					.getResultList();
			frequencia = mesesStandardList.get(0).getFrequencia();
			totalMesesManutencao = frequencia * mesesStandardList.size();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			Map parametros = new HashMap();
			parametros.put("SUBREPORT_DIR", pathSubreport);
			parametros.put("NUMERO_CONTRATO", contrato.getNumeroContrato());
			parametros.put("REGIONAL", contrato.getRegional());

			parametros.put("FILIAL", twFilial.getStnm().trim());
			parametros.put("RAZAO_SOCIAL", contrato.getRazaoSocial());
			parametros.put("CNPJ", contrato.getCnpj());
			parametros.put("PROCURADOR", contrato.getProcurador());
			parametros.put("INSC_ESTADUAL", contrato.getInscEstadual());
			parametros.put("INSC_MUNICIPAL", contrato.getInscMunicipal());
			parametros.put("INSC_MUNICIPAL", contrato.getInscMunicipal());

			parametros.put("CONTRIBUINTE", contrato.getContribuinte());
			parametros.put("TELEFONE_COMERCIAL",
					contrato.getTelefoneComercial());
			parametros.put("CPF", contrato.getCpf());
			parametros.put("CONTATO_COMERCIAL", contrato.getContatoComercial());
			parametros.put("EMAIL_CONTATO_COMERCIAL",
					contrato.getEmailContatoComercial());
			parametros.put("FAX_COMERCIAL", contrato.getFaxComercial());
			parametros.put("CONTATO_SERVICOS", contrato.getContatoServicos());
			parametros.put("EMAIL_CONTATO_SERVICOS",
					contrato.getEmailContatoServicos());

			parametros.put("TELEFONE_SERVICOS", contrato.getTelefoneServicos());
			parametros.put("FAX_SERVICOS", contrato.getFaxServicos());
			parametros.put("MODELO", contrato.getModelo());
			parametros.put("FAMILIA", contrato.getModelo());
			parametros.put("MUM_SERIE", contrato.getNumeroSerie());
			parametros.put("HORIMETRO", contrato.getHorimetro().toString());
			parametros.put("HORAS_CONTRATADA", totalMesesManutencao.toString()
					+ " Horas");

			parametros.put("DATA_ACEITE", contrato.getDataAceite());
			parametros
					.put("QTD_PARCELAS", contrato.getQtdParcelas().toString());
			parametros.put("CONDICAO_EXCEPCIONAL",
					contrato.getCondicaoExcepcional());
			parametros.put(
					"PRECO",
					"R$ "
							+ ValorMonetarioHelper.formata("###,###.00",
									contrato.getValoContrato().doubleValue()));

			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("cabecalho_contrato.PNG");

			File img = File.createTempFile("img", "gif", new File("."));

			OutputStream out = new FileOutputStream(img);
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0)
				out.write(buf, 0, len);
			out.close();
			inputStream.close();
			parametros.put("logo", img);

			// Gera o pdf para exibicao..
			try {
				pdfProposta = JasperRunManager.runReportToPdf(jasperReport,
						parametros, new JREmptyDataSource());
			} catch (Exception jre) {
				jre.printStackTrace();
			}

			// Parametros para nao fazer cache e o que será exibido..
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"attachment; filename=proposta.pdf");

			// Envia para o navegador o pdf..
			ServletOutputStream servletOutputStream = response
					.getOutputStream();
			servletOutputStream.write(pdfProposta);
			servletOutputStream.flush();
			servletOutputStream.close();
			img.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
