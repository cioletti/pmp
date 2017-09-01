package com.pmp.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.pmp.bean.AgendamentoBean;
import com.pmp.bean.BusinessGroupBean;
import com.pmp.bean.CentroDeCustoBean;
import com.pmp.bean.ClassificacaoBean;
import com.pmp.bean.ClienteBean;
import com.pmp.bean.ClienteInterBean;
import com.pmp.bean.CompCodeBean;
import com.pmp.bean.CompartimentoBean;
import com.pmp.bean.ComponenteCodeBean;
import com.pmp.bean.CondicaoPagamentoBean;
import com.pmp.bean.ConfigManutencaoBean;
import com.pmp.bean.ConfigManutencaoHorasBean;
import com.pmp.bean.ConfigManutencaoMesesBean;
import com.pmp.bean.ConfigOperacionalBean;
import com.pmp.bean.ConfiguracaoPrecosBean;
import com.pmp.bean.ConfigurarCustomizacaoBean;
import com.pmp.bean.ConfigurarTracaoBean;
import com.pmp.bean.ContaContabilBean;
import com.pmp.bean.ContratoComercialBean;
import com.pmp.bean.CriticidadeManutencaoBean;
import com.pmp.bean.DashboardBean;
import com.pmp.bean.DashboardMaquinasBean;
import com.pmp.bean.DataHeaderBean;
import com.pmp.bean.DescontoPDPSpotBean;
import com.pmp.bean.DescontoPdpBean;
import com.pmp.bean.DetalhesVeiculosBean;
import com.pmp.bean.FamiliaBean;
import com.pmp.bean.FilialBean;
import com.pmp.bean.FinanceiroBean;
import com.pmp.bean.IndicadorGarantiaBean;
import com.pmp.bean.InspecaoPmpBean;
import com.pmp.bean.InspecaoPmpTreeBean;
import com.pmp.bean.JobCodeBean;
import com.pmp.bean.JobControlBean;
import com.pmp.bean.MaquinaPlBean;
import com.pmp.bean.MesesManutencaoBean;
import com.pmp.bean.MinutaBean;
import com.pmp.bean.ModeloBean;
import com.pmp.bean.MotNaoFecContratoBean;
import com.pmp.bean.MultiVacBean;
import com.pmp.bean.NotaFiscalBean;
import com.pmp.bean.OperacionalBean;
import com.pmp.bean.OsEstimada;
import com.pmp.bean.PecaBean;
import com.pmp.bean.PecasDbsBean;
import com.pmp.bean.PlMaquinaBean;
import com.pmp.bean.PrecoBean;
import com.pmp.bean.PrefixoBean;
import com.pmp.bean.RangerBean;
import com.pmp.bean.RegraDeNegocioBean;
import com.pmp.bean.StatusAgendamentoBean;
import com.pmp.bean.StatusContratoBean;
import com.pmp.bean.StatusOsBean;
import com.pmp.bean.TipoContratoBean;
import com.pmp.bean.TipoCustomizacaoBean;
import com.pmp.bean.TipoFrequenciaBean;
import com.pmp.bean.TipoOleoBean;
import com.pmp.bean.TipoPm;
import com.pmp.bean.TipoTracaoBean;
import com.pmp.bean.TotalAgendamentoChartBean;
import com.pmp.bean.TreeBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.bean.ValidarCentroDeCustoContaContabilBean;
import com.pmp.bean.ValorManutencaoBean;
import com.pmp.business.AgendamentoBusiness;
import com.pmp.business.CentroDeCustoBusiness;
import com.pmp.business.CentroDeCustoContaContabilBusiness;
import com.pmp.business.ClienteInterBusiness;
import com.pmp.business.CompCodeBusiness;
import com.pmp.business.CompartimentoBusiness;
import com.pmp.business.ConfigManutBusiness;
import com.pmp.business.ConfigOperacionalBusiness;
import com.pmp.business.ConfiguracaoPrecosBusiness;
import com.pmp.business.ContaContabilBusiness;
import com.pmp.business.ContratoBusiness;
import com.pmp.business.CriticidadeManutencaoBusiness;
import com.pmp.business.CustomizacaoBusiness;
import com.pmp.business.DashboardBusiness;
import com.pmp.business.DescontoMultiVacBusiness;
import com.pmp.business.DescontoPdpBusiness;
import com.pmp.business.DescontoPdpSpotBusiness;
import com.pmp.business.FamiliaBusiness;
import com.pmp.business.FinanceiroBusiness;
import com.pmp.business.InspencaoPmpBusiness;
import com.pmp.business.InspencaoPmpTreeBusiness;
import com.pmp.business.MaquinaPlBusiness;
import com.pmp.business.MesesManutencaoBusiness;
import com.pmp.business.OperacionalBusiness;
import com.pmp.business.OsBusiness;
import com.pmp.business.PlMaquinaBusiness;
import com.pmp.business.RegraDeNegocioBusiness;
import com.pmp.business.TipoOleoBusiness;
import com.pmp.business.TracaoBusiness;
import com.pmp.business.TreeBusiness;
import com.pmp.business.UsuarioBusiness;
import com.pmp.business.ValorManutencaoBusiness;
import com.pmp.business.VeiculoBusiness;
import com.pmp.entity.PmpFileEt;
import com.pmp.read.ReadMaquinaPlJob;
import com.pmp.util.ExceptionLogin;

import flex.messaging.FlexContext;
import flex.messaging.io.ArrayList;

public class ServiceController { 

	private UsuarioBean usuarioBean;

	public ServiceController() throws Exception {
		usuarioBean = (UsuarioBean) FlexContext.getFlexSession().getAttribute("usuario");
		
	}

	public String getUrlLogintServer() throws Exception {
		String url = FlexContext.getHttpRequest().getProtocol().split("/")[0]
				.concat("://").concat(FlexContext.getHttpRequest().getServerName()).concat(
				":").concat(
				String.valueOf(FlexContext.getHttpRequest().getServerPort()))
				.concat("/ControlPanelPesa");
		 return url;
	}
	public String getUrlReport() throws Exception {
		String url = FlexContext.getHttpRequest().getProtocol().split("/")[0]
   				.concat("://").concat(FlexContext.getHttpRequest().getServerName()).concat(
   				":").concat(
   				String.valueOf(FlexContext.getHttpRequest().getServerPort()))
   				.concat("/Pmp");
                                           		
		return url;
	}
	public String getUrlImg() throws Exception {
		String url = FlexContext.getHttpRequest().getProtocol().split("/")[0]
                       .concat("://").concat(FlexContext.getHttpRequest().getServerName()).concat(
                       ":").concat(
                    		   String.valueOf(FlexContext.getHttpRequest().getServerPort())).concat(FlexContext.getHttpRequest().getContextPath()).concat("/");
		return url;
	}
	
	public String findUrlMapa() throws Exception {
		String url = FlexContext.getHttpRequest().getProtocol().split("/")[0]
                       .concat("://").concat(FlexContext.getHttpRequest().getServerName()).concat(
                       ":").concat(
                    		   String.valueOf(FlexContext.getHttpRequest().getServerPort())).concat(FlexContext.getHttpRequest().getContextPath());
		return url;
	}

	private void validarUsuario() throws Exception {
		try {
			if (usuarioBean == null) {
				throw new Exception();
			}
		} catch (Exception e) {
			ExceptionLogin exception = new ExceptionLogin("false");
			throw exception;
		}
	}

	public List<ModeloBean> findAllModelos() throws Exception {
		validarUsuario();
		ConfigManutBusiness business = new ConfigManutBusiness(usuarioBean);
		return business.findAllModelos();
	}
	
	public List<ModeloBean> findAllModelos(Long idFamilia) throws Exception {
		validarUsuario();
		ConfigManutBusiness business = new ConfigManutBusiness(usuarioBean);
		return business.findAllModelos(idFamilia);
	}
	
	public List<ModeloBean> findAllModelosEditBy(Long idFamilia) throws Exception {
		validarUsuario();
		ConfigManutBusiness business = new ConfigManutBusiness(usuarioBean);
		return business.findAllModelos(idFamilia);
	}

	public List<PrefixoBean> findAllPrefixos(String modelo) throws Exception {
		validarUsuario();
		ConfigManutBusiness business = new ConfigManutBusiness(usuarioBean);
		return business.findAllPrefixos(modelo);
	}
	
	public List<PrefixoBean> findAllPrefixosEditBy(String modelo) throws Exception {
		validarUsuario();
		ConfigManutBusiness business = new ConfigManutBusiness(usuarioBean);
		return business.findAllPrefixos(modelo);
	}

	public List<BusinessGroupBean> findAllBusinessGroup() throws Exception {
		validarUsuario();
		ConfigManutBusiness business = new ConfigManutBusiness(usuarioBean);
		return business.findAllBusinessGroup();
	}
	
	public List<BusinessGroupBean> findBusinessGroupPM() throws Exception {
		validarUsuario();
		ConfigManutBusiness business = new ConfigManutBusiness(usuarioBean);
		return business.findBusinessGroupPM();
	}

	public List<RangerBean> findRangerPmp(String modelo, String prefixo) throws Exception {
		validarUsuario();
		ConfigManutBusiness business = new ConfigManutBusiness(usuarioBean);
		return business.findRangerPmp(modelo, prefixo);
	}

	public ConfigManutencaoBean saveOrUpdate(ConfigManutencaoBean bean, String isGeradorStandby) throws Exception {
		validarUsuario();
		ConfigManutBusiness business = new ConfigManutBusiness(usuarioBean);
		return business.saveOrUpdate(bean, isGeradorStandby);
	}

	public MesesManutencaoBean saveOrUpdate (MesesManutencaoBean bean) throws Exception{
		validarUsuario();
		MesesManutencaoBusiness business = new MesesManutencaoBusiness(usuarioBean);
		return business.saveOrUpdate(bean);
	}
	
	public List<MesesManutencaoBean> findAllMesesManutencao (Long idFamilia, Long idModelo) throws Exception{
		validarUsuario();
		MesesManutencaoBusiness business = new MesesManutencaoBusiness(usuarioBean);
		return business.findAllMesesManutencao(idFamilia, idModelo);
	}
	
	public CriticidadeManutencaoBean findCriticidadeBy (String criticidade) throws Exception{
		validarUsuario();
		CriticidadeManutencaoBusiness business = new CriticidadeManutencaoBusiness(usuarioBean);
		return business.findCriticidadeBy(criticidade);
	}	
	
	public CriticidadeManutencaoBean saveOrUpdate (CriticidadeManutencaoBean bean) throws Exception {
		validarUsuario();
		CriticidadeManutencaoBusiness business = new CriticidadeManutencaoBusiness(usuarioBean);
		return business.saveOrUpdate(bean);
	}
	
	
	public Boolean removerMesesManutencao(MesesManutencaoBean bean) throws Exception {
		validarUsuario();
		MesesManutencaoBusiness business = new MesesManutencaoBusiness(usuarioBean);
		return business.removerMesesManutencao(bean);
	}

	public List<CentroDeCustoBean> findAllCentroDeCusto() throws Exception {
		validarUsuario();
		CentroDeCustoBusiness business = new CentroDeCustoBusiness();
		return business.findAllCentroDeCusto();
	}

	public CentroDeCustoBean saveOrUpdate(CentroDeCustoBean bean) throws Exception {
		validarUsuario();
		CentroDeCustoBusiness business = new CentroDeCustoBusiness();
		return business.saveOrUpdate(bean);
	}

	public Boolean remover(CentroDeCustoBean bean) throws Exception {
		validarUsuario();
		CentroDeCustoBusiness business = new CentroDeCustoBusiness();
		return business.remover(bean);
	}

	public List<ContaContabilBean> findAllContaContabil() throws Exception {
		validarUsuario();
		ContaContabilBusiness business = new ContaContabilBusiness();
		return business.findAllContaContabil();
	}

	public ContaContabilBean saveOrUpdate(ContaContabilBean bean) throws Exception {
		validarUsuario();
		ContaContabilBusiness business = new ContaContabilBusiness();
		return business.saveOrUpdate(bean);
	}

	public Boolean remover(ContaContabilBean bean) throws Exception {
		validarUsuario();
		ContaContabilBusiness business = new ContaContabilBusiness();
		return business.remover(bean);
	}

	public List<RegraDeNegocioBean> findAllRegraDeNegocio() throws Exception {
		validarUsuario();
		RegraDeNegocioBusiness business = new RegraDeNegocioBusiness();
		return business.findAllRegraDeNegocio();
	}

	public List<RegraDeNegocioBean> findRegraDeNegocioByFilial(String filial) throws Exception {
		validarUsuario();
		RegraDeNegocioBusiness business = new RegraDeNegocioBusiness();
		return business.findRegraDeNegocioByFilial(filial);
	}

	public RegraDeNegocioBean saveOrUpdate(RegraDeNegocioBean bean) throws Exception {
		validarUsuario();
		RegraDeNegocioBusiness business = new RegraDeNegocioBusiness();
		return business.saveOrUpdate(bean);
	}

	public Boolean remover(RegraDeNegocioBean bean) throws Exception {
		validarUsuario();
		RegraDeNegocioBusiness business = new RegraDeNegocioBusiness();
		return business.remover(bean);
	}

	public List<ClienteInterBean> findAllClienteInter() throws Exception {
		validarUsuario();
		ClienteInterBusiness business = new ClienteInterBusiness();
		return business.findAllClienteInter();
	}

	public List<ClienteInterBean> findClienteInterBySearchKey(String searchKey) throws Exception {
		validarUsuario();
		ClienteInterBusiness business = new ClienteInterBusiness();
		return business.findClienteInterBySearchKey(searchKey);		
	}
	
	public ClienteInterBean saveOrUpdate(ClienteInterBean bean) throws Exception {
		validarUsuario();
		ClienteInterBusiness business = new ClienteInterBusiness();
		return business.saveOrUpdate(bean);
	}

	public Boolean remover(ClienteInterBean bean) throws Exception {
		validarUsuario();
		ClienteInterBusiness business = new ClienteInterBusiness();
		return business.remover(bean);
	}
	
	public List<DescontoPdpBean> findAllDescontoPdp() throws Exception {
		validarUsuario();
		DescontoPdpBusiness business = new DescontoPdpBusiness();
		return business.findAllDescontoPdp();
	}

	public DescontoPdpBean saveOrUpdate(DescontoPdpBean bean) throws Exception {
		validarUsuario();
		DescontoPdpBusiness business = new DescontoPdpBusiness();
		return business.saveOrUpdate(bean);
	}

	public Boolean remover(DescontoPdpBean bean) throws Exception {
		validarUsuario();
		DescontoPdpBusiness business = new DescontoPdpBusiness();
		return business.remover(bean);
	}

	public List<ConfigManutencaoBean> findConfiguracao(String modelo, String isGeradorStandby) throws Exception {
		validarUsuario();
		ConfigManutBusiness business = new ConfigManutBusiness(usuarioBean);
		return business.findConfiguracao(modelo, isGeradorStandby);
	}

	public Boolean removerConfPmp(ConfigManutencaoBean bean) throws Exception {
		validarUsuario();
		ConfigManutBusiness business = new ConfigManutBusiness(usuarioBean);
		return business.removerConfPmp(bean);
	}

	public List<TipoFrequenciaBean> findAllFrequencia() throws Exception {
		validarUsuario();
		ConfigManutBusiness business = new ConfigManutBusiness(usuarioBean);
		return business.findAllFrequencia("");
	}
	
	public List<TipoFrequenciaBean> findAllFrequencia(String descricao) throws Exception {
		validarUsuario();
		ConfigManutBusiness business = new ConfigManutBusiness(usuarioBean);
		return business.findAllFrequencia(descricao);
	}

	public List<ConfiguracaoPrecosBean> findAllConfigPrecos() throws Exception {
		validarUsuario();
		ConfiguracaoPrecosBusiness business = new ConfiguracaoPrecosBusiness(usuarioBean);
		return business.findAllConfigPrecos();
	}

	public ConfiguracaoPrecosBean saveOrUpdate(ConfiguracaoPrecosBean bean) throws Exception {
		validarUsuario();
		ConfiguracaoPrecosBusiness business = new ConfiguracaoPrecosBusiness(usuarioBean);
		return business.saveOrUpdate(bean);
	}

	public Boolean remover(ConfiguracaoPrecosBean bean) throws Exception {
		validarUsuario();
		ConfiguracaoPrecosBusiness business = new ConfiguracaoPrecosBusiness(usuarioBean);
		return business.remover(bean);
	}

	public List<ModeloBean> findAllModelosContrato(String contExcessao, Long idFamilia, String isGerador) throws Exception {
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllModelosContrato(contExcessao, idFamilia, isGerador);
	}

	public List<PrefixoBean> findAllPrefixosContrato(String modelo, String contExcessao) throws Exception {
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllPrefixosContrato(modelo, contExcessao);
	}

	public List<BusinessGroupBean> findAllBusinessGroupContrato() throws Exception {
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllBusinessGroupContrato();
	}

	public List<RangerBean> findAllRangerContrato(String modelo, String prefixo,String contExcessao, Long idConfiguracaoPreco) throws Exception {
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllRangerContrato(modelo, prefixo, contExcessao, idConfiguracaoPreco);
	}

	public List<TipoContratoBean> findAllTipoContrato() throws Exception {
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllTipoContrato();
	}
	public List<TipoContratoBean> findAllTipoContratoRental() throws Exception {
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllTipoContratoRental();
	}
	public List<TipoContratoBean> findAllTipoContratoAntigo() throws Exception {
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllTipoContratoAntigo();
	}

	public List<StatusContratoBean> findAllStatusContrato() throws Exception {
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllStatusContrato();
	}

	public List<ClienteBean> findDataNomeCliente(String nomeCliente) throws Exception {
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findDataNomeCliente(nomeCliente);
	}

	public ClienteBean findDataCliente(String cuno) throws Exception {
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findDataCliente(cuno);
	}
	public List<ConfigManutencaoHorasBean> findAllManutencaoHoras(String modelo, String prefixo, String beginrange, String endrange, String bgrp, String contExcessao, Long idConfigPreco) throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllManutencaoHoras(modelo, prefixo, beginrange, endrange, bgrp,contExcessao,idConfigPreco);
	}
	public List<ConfigManutencaoMesesBean> findAllManutencaoMeses(String modelo, String prefixo, String beginrange, String endrange, String bgrp) throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllManutencaoMeses(modelo, prefixo, beginrange, endrange, bgrp);
	}
	public ContratoComercialBean saveOrUpdate(ContratoComercialBean contratoComercialBean, String isSaveHorasPosPago) throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		if(!contratoComercialBean.getSiglaClassificacaoContrato().equals("PLUS")){
			return business.saveOrUpdate(contratoComercialBean, isSaveHorasPosPago);
		}
		return business.saveOrUpdatePlus(contratoComercialBean, isSaveHorasPosPago);
	}
	public List<ContratoComercialBean> findAllContratoComercialRental(String nomeCliente, Long idStatusContrato, String contExcessao) throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllContratoComercialRental(nomeCliente, idStatusContrato, contExcessao);
	}
	public List<ContratoComercialBean> findAllContratoComercialAntigo(String nomeCliente, Long idStatusContrato, String contExcessao) throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllContratoComercialAntigo(nomeCliente, idStatusContrato, contExcessao);
	}
	public List<ContratoComercialBean> findAllContratoComercial(String nomeCliente, Long idStatusContrato, String contExcessao) throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		List<ContratoComercialBean> comercialBeans = new ArrayList();
		comercialBeans = business.findAllContratoComercial(nomeCliente, idStatusContrato, "N", contExcessao);
		if(comercialBeans == null){
			comercialBeans = new ArrayList();
		}
		comercialBeans.addAll(business.findAllContratoComercialPlus(nomeCliente, idStatusContrato, "N", contExcessao));
		return comercialBeans;
	}
	public List<ContratoComercialBean> findAllContratoComercialPromocao(String nomeCliente, Long idStatusContrato, String contExcessao) throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		List<ContratoComercialBean> comercialBeans = new ArrayList();
		comercialBeans = business.findAllContratoComercialPromocao(nomeCliente, idStatusContrato, "N", contExcessao);
		if(comercialBeans == null){
			comercialBeans = new ArrayList();
		}
		comercialBeans.addAll(business.findAllContratoComercialPlusPromocao(nomeCliente, idStatusContrato, "N", contExcessao));
		return comercialBeans;
	}
	public ContratoComercialBean findAllContratoComercial(Long idContrato) throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllContratoComercial(idContrato);
	}
	public List<ContratoComercialBean> findAllContratoComercialSpot(String nomeCliente, Long idStatusContrato,  String contExcessao) throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllContratoComercialSpot(nomeCliente, idStatusContrato, "N", contExcessao);
	}
	public List<ContratoComercialBean> findAllContratoComercialGerador(String nomeCliente, Long idStatusContrato) throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllContratoComercialGerador(nomeCliente, idStatusContrato, "S");
	}
	public List<ContratoComercialBean> findAllContratoComercialAVM(String nomeCliente) throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllContratoComercialAVM(nomeCliente);
	}

	public String getUrlReportServer() {
		String url = FlexContext.getHttpRequest().getProtocol().split("/")[0].concat("://");
		return url.concat(FlexContext.getHttpRequest().getServerName()).concat(":")
		.concat(String.valueOf(FlexContext.getHttpRequest().getServerPort()))
		.concat(FlexContext.getHttpRequest().getContextPath()).concat("/ReportPdf");
	}
	public String getUrlServicoPmp() {
		String url = FlexContext.getHttpRequest().getProtocol().split("/")[0].concat("://");
		return url.concat(FlexContext.getHttpRequest().getServerName()).concat(":")
				.concat(String.valueOf(FlexContext.getHttpRequest().getServerPort()))
				.concat(FlexContext.getHttpRequest().getContextPath());
	}
	public List<PrecoBean> findAllParcelas(Long idContrato) throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllParcelas(idContrato);
	}
	public List<PrecoBean> findAllParcelasSubstituicaoTributaria(Long idContrato) throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllParcelasSubstituicaoTributaria(idContrato);
	}
	public List<PrecoBean> findAllParcelasGerador(Long idContrato) throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllParcelasGerador(idContrato);
	}
	public List<MotNaoFecContratoBean> findAllMotNaoFecContrato() throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllMotNaoFecContrato();
	}
	public List<IndicadorGarantiaBean> findAllIndicadorGarantia() throws Exception{
		validarUsuario();
		CentroDeCustoContaContabilBusiness business = new CentroDeCustoContaContabilBusiness(usuarioBean);
		return business.findAllIndicadorGarantia();
	}
	public Boolean validarCentroDeCustoContaContabil(ValidarCentroDeCustoContaContabilBean bean) throws Exception{
		validarUsuario();
		CentroDeCustoContaContabilBusiness business = new CentroDeCustoContaContabilBusiness(usuarioBean);
		return business.validarCentroDeCustoContaContabil(bean);
	}
	public List<DataHeaderBean> findAllHeaderList(String data) throws Exception{
		validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(usuarioBean);
		return business.findAllHeaderList(data);
	}
	public List<UsuarioBean> findAllTecnico(List<DataHeaderBean> dataHeaderList) throws Exception{
		validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(usuarioBean);
		return business.findAllTecnico(dataHeaderList);
	}
	public List<UsuarioBean> saveOrUpdate(AgendamentoBean bean, List<DataHeaderBean> dataHeaderList) throws Exception{
		validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(usuarioBean);
		return business.saveOrUpdate(bean,dataHeaderList);
	}
	public List<AgendamentoBean> findAllOsDisponiveis(String idFuncionario, Boolean isEdit) throws Exception{
		validarUsuario();
		UsuarioBusiness usuarioBusiness = new UsuarioBusiness();
		UsuarioBean bean = usuarioBusiness.findAllUsersByMatricula(idFuncionario);
		AgendamentoBusiness business = new AgendamentoBusiness(usuarioBean);
		if(bean.getNome().equalsIgnoreCase("CUSTOMER")){
			return business.findAllOsDisponiveisCustomer(isEdit);
		}else if(bean.getNome().equalsIgnoreCase("PARTNER")){
			return business.findAllOsDisponiveisPartner(isEdit);
		}else{
			return business.findAllOsDisponiveis(isEdit);
		}
	}
	public List<PecasDbsBean> findPecas(String numberOs) throws Exception{
		validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(usuarioBean);
		return business.findPecas(numberOs);
	}
	public List<StatusAgendamentoBean> findAllStatusAgendamento() throws Exception{
		validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(usuarioBean);
		return business.findAllStatusAgendamento();
	}
	public List<UsuarioBean> removerAgendamento(Long idAgendamento, List<DataHeaderBean> dataHeaderList) throws Exception{
		validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(usuarioBean);
		return business.remover(idAgendamento,dataHeaderList);
	}
	public List<DataHeaderBean> findAllHeaderNext(String data) throws Exception{
		validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(usuarioBean);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(format.parse(data));
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return business.findAllHeaderList(format.format(calendar.getTime()));
	}
	public List<DataHeaderBean> findAllHeaderPrevious(String data) throws Exception{
		validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(usuarioBean);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(format.parse(data));
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return business.findAllHeaderList(format.format(calendar.getTime()));
	}
	public List<ClienteInterBean> findAllClienteInterCC() throws Exception{
		validarUsuario();
		CentroDeCustoContaContabilBusiness business = new CentroDeCustoContaContabilBusiness(usuarioBean);
		return business.findAllClienteInterCC();
	}
	public Collection<FilialBean> findAllFiliais() throws Exception {
		validarUsuario();
		OsBusiness business = new OsBusiness(usuarioBean);
		return business.findAllFiliais();
	}
	public Boolean verificaOsFaturada(Integer idContrato) throws Exception {
		validarUsuario();
		OsBusiness business = new OsBusiness(usuarioBean);
		return business.verificaOsFaturada(idContrato);
	}
	public Collection<JobControlBean> findAllJobControl() throws Exception {
		validarUsuario();
		OsBusiness business = new OsBusiness(usuarioBean);
		return business.findAllJobControl();
	}
	public Collection<JobCodeBean> findAllJobCode() throws Exception {
		validarUsuario();
		OsBusiness business = new OsBusiness(usuarioBean);
		return business.findAllJobCode();
	}
	public List<ContratoComercialBean> 	findAllContratosAbertos() throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllContratosAbertos();
	}
	public Collection<ComponenteCodeBean> findAllCompCode(String caracter) throws Exception {
		validarUsuario();
		OsBusiness business = new OsBusiness(usuarioBean);
		return business.findAllCompCode(caracter);
	}
	public String findEstimateBy() {
		String [] array = usuarioBean.getNome().split(" ");
		String estimateBy = "";
		for (String string : array) {
			if(string.length() > 2){
				estimateBy += string.charAt(0);
			}
		}
		return estimateBy;
	}
	public OsEstimada newOsEstimada(OsEstimada bean) throws Exception{
		validarUsuario();
		OsBusiness business = new OsBusiness(usuarioBean);
		return business.newOsEstimada(bean);
	}
	
	public ConfigOperacionalBean findConfigOperacional(Long idContrato) throws Exception{
		validarUsuario();
		ConfigOperacionalBusiness business = new ConfigOperacionalBusiness();
		return business.findConfigOperacional(idContrato);
	}
	
	public ConfigOperacionalBean saveOrUpdate(ConfigOperacionalBean configOperacionalBean) throws Exception{
		validarUsuario();
		ConfigOperacionalBusiness business = new ConfigOperacionalBusiness();
		return business.saveOrUpdate(configOperacionalBean);
	}
	public TreeBean findAllTree(Integer idArv) throws Exception{
		validarUsuario();
		TreeBusiness business = new TreeBusiness();
		return business.findAllTree(idArv);
	}
	public List<TreeBean> findAllTreePai(String tipo, Long idFamilia) throws Exception{
		validarUsuario();
		TreeBusiness business = new TreeBusiness();
		return business.findAllTreePai(tipo, idFamilia);
	}
	public TreeBean saveNodo(TreeBean bean, Long idPaiRoot, Long idFamilia) throws Exception {
		validarUsuario();
		TreeBusiness business = new TreeBusiness();
		return business.saveNodo(bean, idPaiRoot, idFamilia);
	}
	public List<OperacionalBean> findAllOperacionalByFiltro(String filtroUmaPendente, String filtroPendentes, String filtroEncerradas, String horas50,
			String ordenarHorimetro, String ordenarData, Long inicial, Long numRegistros, String campoPesquisa){
		OperacionalBusiness business = new OperacionalBusiness(usuarioBean);
		List<OperacionalBean> operacionalBeans = business.findAllOperacionalByFiltro(filtroUmaPendente, filtroPendentes, filtroEncerradas,horas50, ordenarHorimetro, ordenarData, inicial, numRegistros, campoPesquisa);
		operacionalBeans.addAll(business.findAllOperacionalByFiltroPlus(filtroUmaPendente, filtroPendentes, filtroEncerradas,horas50, ordenarHorimetro, ordenarData, inicial, numRegistros, campoPesquisa));
		return operacionalBeans;
	}
	public List<OperacionalBean> findAllOperacionalByFiltroGerador(String filtroUmaPendente, String filtroPendentes, String filtroEncerradas, String horas50,
			String ordenarHorimetro, String ordenarData){
		OperacionalBusiness business = new OperacionalBusiness(usuarioBean);
		return business.findAllOperacionalByFiltroGerador(filtroUmaPendente, filtroPendentes, filtroEncerradas,horas50, ordenarHorimetro, ordenarData);
	}
	public List<FamiliaBean> findAllFamilia(String contExcessao) throws Exception {
		validarUsuario();
		FamiliaBusiness business = new FamiliaBusiness();
		return business.findAllFamilia(contExcessao);
	}
	public List<FamiliaBean> findAllFamiliaPromocao(String contExcessao) throws Exception {
			validarUsuario();
			FamiliaBusiness business = new FamiliaBusiness();
			return business.findAllFamiliaPromocao(contExcessao);
	}
	public List<FamiliaBean> findAllFamiliaGerador(String contExcessao, String descricao) throws Exception {
		validarUsuario();
		FamiliaBusiness business = new FamiliaBusiness();
		return business.findAllFamiliaGerador(contExcessao, descricao);
	}
	public List<FamiliaBean> findAllFamilia() throws Exception {
		validarUsuario();
		FamiliaBusiness business = new FamiliaBusiness();
		return business.findAllFamilia();
	}
	public List<FamiliaBean> findAllFamiliaGerador(String descricao) throws Exception {
		validarUsuario();
		FamiliaBusiness business = new FamiliaBusiness();
		return business.findAllFamiliaGerador(descricao);
	}
	public FamiliaBean saveOrUpdate(FamiliaBean bean) throws Exception {
		validarUsuario();
		FamiliaBusiness business = new FamiliaBusiness();
		return business.saveOrUpdate(bean);
	}
	public Boolean remover(FamiliaBean bean) throws Exception {
		validarUsuario();
		FamiliaBusiness business = new FamiliaBusiness();
		return business.remover(bean);
	}
	public Boolean removerTree(Long idArv) throws Exception{
		validarUsuario();
		TreeBusiness business = new TreeBusiness();
		return business.removerTree(idArv);
	}
	public TreeBean save(TreeBean bean, Long idPai, Long paiRoot, Long idFamilia) throws Exception {
		validarUsuario();
		TreeBusiness business = new TreeBusiness();
		return business.save(bean, idPai, paiRoot, idFamilia);
	}	
	public List<InspecaoPmpBean> findAllInspecaoPmp(String dtEmissao, String trocarPecas,String campoPesquisa){
		InspencaoPmpBusiness business = new InspencaoPmpBusiness();
		return business.findAllInspecaoPmp(dtEmissao,trocarPecas, usuarioBean.getFilial(), usuarioBean, campoPesquisa);
	}	
	public List<InspecaoPmpTreeBean> findAllInspencaoPmpTree(Long idOsPalm) throws Exception{
		validarUsuario();
		InspencaoPmpTreeBusiness business = new InspencaoPmpTreeBusiness();
		return business.findAllInspencaoPmpTree(idOsPalm);		
	}
	public Boolean alterarFilial(FilialBean filialBean) throws Exception{
		validarUsuario();
		ConfigOperacionalBusiness business = new ConfigOperacionalBusiness();
		return business.alterarFilial(filialBean);
	}
	public List<FilialBean> findAllFilial() throws Exception {
		//validarUsuario();
		ConfigOperacionalBusiness business = new ConfigOperacionalBusiness();
		return business.findAllFilial();
	}
	public  UsuarioBean verificarLogin() throws Exception{
		validarUsuario();
		return usuarioBean;
	}
	public void invalidarSessao(){
		FlexContext.getFlexSession().invalidate();
	}
	public MaquinaPlBean findMaquinaPl(String numeroSerie) throws Exception {
		validarUsuario();
		MaquinaPlBusiness business = new MaquinaPlBusiness();
		return business.findMaquinaPl(numeroSerie);
	}
	public MaquinaPlBean saveOrUpdateMaquinaPl(MaquinaPlBean bean) throws Exception {
		//validarUsuario();
		MaquinaPlBusiness business = new MaquinaPlBusiness();
		return business.saveOrUpdate(bean);
	}
	public String alterPassword(String senhaAntiga, String senhaAtual) throws Exception{
		validarUsuario();
		UsuarioBusiness business = new UsuarioBusiness();
		return business.alterPassword(usuarioBean.getLogin(), senhaAntiga, senhaAtual);
	}
	public boolean removerNodo(Long idArv) throws Exception{
		validarUsuario();
		TreeBusiness business = new TreeBusiness();
		return business.removerNodo(idArv);
	}
	
	public List<Integer> findAllFotos(Integer idOsPalmDt) throws Exception{
		validarUsuario();
		InspencaoPmpTreeBusiness business = new InspencaoPmpTreeBusiness();
		return business.findAllFotos(idOsPalmDt);
	}
	public List<AgendamentoBean> findAllAgendamentosPendentes(Integer inicial, Integer numRegistros, String tipoAgendamento, String campoPesquisa) throws Exception{
		validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(usuarioBean);
		List<AgendamentoBean> agendamentoBeans = business.findAllAgendamentosPendentes(inicial, numRegistros, tipoAgendamento, campoPesquisa);
		agendamentoBeans.addAll(business.findAllAgendamentosPendentesPlus(inicial, numRegistros, tipoAgendamento, campoPesquisa));
		return agendamentoBeans;
	}
	public List<PlMaquinaBean> findAllMaquinaPl() throws Exception{
		//validarUsuario();
		PlMaquinaBusiness business = new PlMaquinaBusiness();
		return business.findAllMaquinaPl();
	}
	public PlMaquinaBean findAllMaquinaPlByName(String numSerie) throws Exception{
		validarUsuario();
		PlMaquinaBusiness business = new PlMaquinaBusiness();
		return business.findAllMaquinaPl(numSerie);
	}
	public ContratoComercialBean saveOrUpdateContratoAntigo(ContratoComercialBean contratoComercialBean) throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.saveOrUpdateContratoAntigo(contratoComercialBean);
	}
	
	public OsEstimada newOsEstimadaContrato(OsEstimada bean) throws Exception{
		validarUsuario();
		OsBusiness business = new OsBusiness(usuarioBean);
		return business.newOsEstimadaContrato(bean);
	}
	public List<PlMaquinaBean> findAllPlMaquinaFilter(Long idFilial, List<ClienteBean> clienteList, String cliente, Boolean isCodCliente, Boolean isNomeCliente, Boolean isPmp) throws Exception{
		validarUsuario();
		PlMaquinaBusiness business = new PlMaquinaBusiness();
		return business.findAllPlMaquinaFilter(idFilial, clienteList, cliente, isCodCliente, isNomeCliente, isPmp);
	}
	public List<ClienteBean> findClienteFilialPL(Long idFilial) throws Exception{
		validarUsuario();
		PlMaquinaBusiness business = new PlMaquinaBusiness();
		return business.findClienteFilialPL(idFilial);
	}
	public TreeBean saveNodoClone(Long idArv, Long idFamilia, String descricao) throws Exception {
		validarUsuario();
		TreeBusiness business = new TreeBusiness();
		return business.saveNodoClone(idArv, idFamilia, descricao);
	}
	public Boolean validarNumSerie(String numSerie) throws Exception{
		validarUsuario();
		OsBusiness business = new OsBusiness(usuarioBean);
		return business.validarNumSerie(numSerie);
	}
	public CompartimentoBean saveCompratimento(CompartimentoBean bean) throws Exception{
		validarUsuario();
		CompartimentoBusiness business = new CompartimentoBusiness();
		return business.saveCompratimento(bean);
	}
	public Boolean removerCompartimento(Long idCompartimento) throws Exception{
		validarUsuario();
		CompartimentoBusiness business = new CompartimentoBusiness();
		return business.removerCompartimento(idCompartimento);
	}
	public List<CompartimentoBean> findCompartimento(String modelo) throws Exception{
		validarUsuario();
		CompartimentoBusiness business = new CompartimentoBusiness();
		return business.findCompartimento(modelo);
	}
	public TipoOleoBean saveTipoOleo(TipoOleoBean bean) throws Exception{
		validarUsuario();
		TipoOleoBusiness business = new TipoOleoBusiness();
		return business.saveTipoOleo(bean);
	}
	public Boolean removerTipoOleo(Long idTipoOleo) throws Exception{
		validarUsuario();
		TipoOleoBusiness business = new TipoOleoBusiness();
		return business.removerTipoOleo(idTipoOleo);
	}
	public List<TipoOleoBean> findTipoOleo(String fabricante) throws Exception{
		validarUsuario();
		TipoOleoBusiness business = new TipoOleoBusiness();
		return business.findTipoOleo(fabricante);
	}
	public Boolean fazerUpload(byte[] bytesArquivo, Long idOsPalm) throws Exception {
		this.validarUsuario();
		InspencaoPmpBusiness business = new InspencaoPmpBusiness();
		PmpFileEt et = new PmpFileEt();
		et.setFileEt(bytesArquivo);
		
		return business.salvarFileEt(et, idOsPalm);
	}
	public Boolean removerContrato(Long idContrato) throws Exception{
		this.validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.removerContrato(idContrato);
	}
	public List<TotalAgendamentoChartBean> findTotalAgendamento(Long filial) throws Exception {
		this.validarUsuario();
		OsBusiness business = new OsBusiness(usuarioBean);
		return business.findTotalAgendamento(filial);
	}
	public ContratoComercialBean validarRenovacaoContrato(ContratoComercialBean contratoComercialBean)throws Exception {
		this.validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.validarRenovacaoContrato(contratoComercialBean);
	}
	
	public List<PecaBean> findAllPecas(ValorManutencaoBean bean) throws Exception{
		validarUsuario();
		ValorManutencaoBusiness business = new ValorManutencaoBusiness();
		return business.findAllPecas(bean);
	}	
	
	public boolean removerPeca(PecaBean bean) throws Exception{
		validarUsuario();
		ValorManutencaoBusiness business = new ValorManutencaoBusiness();
		return business.removerPeca(bean);
	}
	public boolean saveOrUpdate (PecaBean bean) throws Exception{
		validarUsuario();
		ValorManutencaoBusiness business = new ValorManutencaoBusiness();
		return business.saveOrUpdate(bean);		
	}
	
//	public List<FilialBean> findAllFilial()throws Exception {
//		this.validarUsuario();
//		UsuarioBusiness business = new UsuarioBusiness();
//		return business.findAllFilial();
//	}
	
	public void changeUser(Long idFilial){
		usuarioBean = (UsuarioBean) FlexContext.getFlexSession().getAttribute("usuario");
		usuarioBean.setFilial(idFilial.toString());
		FlexContext.getFlexSession().setAttribute("usuario", usuarioBean);
	}
	
	public Boolean sendPecasDbs(Long idContHorasStandard) throws Exception{
		validarUsuario();
		OsBusiness business = new OsBusiness(usuarioBean);
		return business.sendPecasDbs(idContHorasStandard);
	}
	public List<PecaBean> findAllOsOperacionalPecas(Long idContHorasStandard) throws Exception{
		validarUsuario();
		ValorManutencaoBusiness business = new ValorManutencaoBusiness();
		return business.findAllOsOperacionalPecas(idContHorasStandard);
	}
	public Boolean removerOsOperacionalPecas(Long idPecaOsOperacional) throws Exception{
		validarUsuario();
		ValorManutencaoBusiness business = new ValorManutencaoBusiness();
		return business.removerOsOperacionalPecas(idPecaOsOperacional);
	}
	public Long saveOrUpdateOsOperacionalPecas(Long idContHorasStandard, Long idPecaOsOperacional, String partNumber, Long qtd, String sos) throws Exception{
		validarUsuario();
		ValorManutencaoBusiness business = new ValorManutencaoBusiness();
		return business.saveOrUpdateOsOperacionalPecas(idContHorasStandard, idPecaOsOperacional, partNumber, qtd, sos);
	}
	
	public Boolean enviarObsCliente (String obsEmail, String email) throws Exception{
		validarUsuario();
		ConfigOperacionalBusiness business = new ConfigOperacionalBusiness();
		return business.enviarObsCliente (obsEmail, email);
	}
	
	public Boolean saveObsAgendamento (AgendamentoBean bean)throws Exception{
		validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(usuarioBean);
		return business.saveObsAgendamento(bean);
	}
	
	public Boolean removerAgendamentoObs (AgendamentoBean bean)throws Exception{
		validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(usuarioBean);
		return business.removerAgendamentoObs(bean);
	}
	
	public Boolean saveDataFaturamento(AgendamentoBean bean, String encerrarAutomatica)throws Exception{
		validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(usuarioBean);
		return business.saveDataFaturamento(bean, encerrarAutomatica);
	}
	public Boolean sincronizarStanderJob()throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.sincronizarStanderJob();
		
	}
	
	public List<TipoTracaoBean> findAllTipoTracao() throws Exception {
		validarUsuario();
		ConfiguracaoPrecosBusiness business = new ConfiguracaoPrecosBusiness(usuarioBean);
		return business.findAllTipoTracao();
	}
	public List<TipoTracaoBean> findAllTipoTracao(Long idModelo)  throws Exception {
		validarUsuario();
		TracaoBusiness business = new TracaoBusiness();
		return business.findAllTipoTracao(idModelo);
	}
	public TipoTracaoBean saveOrUpdate(TipoTracaoBean bean)  throws Exception {
		validarUsuario();
		TracaoBusiness business = new TracaoBusiness();
		return business.saveOrUpdate(bean);
	}
	public TipoCustomizacaoBean saveOrUpdate(TipoCustomizacaoBean bean)  throws Exception {
		validarUsuario();
		CustomizacaoBusiness business = new CustomizacaoBusiness();
		return business.saveOrUpdate(bean);
	}
	public List<TipoCustomizacaoBean> findAllTipoCustomizacao(Long idModelo)  throws Exception {
		validarUsuario();
		CustomizacaoBusiness business = new CustomizacaoBusiness();
		return business.findAllTipoCustomizacao(idModelo);
	}
	public Boolean removerTipoCustomizacao(Long idTipoCustomizacao)  throws Exception {
		validarUsuario();
		CustomizacaoBusiness business = new CustomizacaoBusiness();
		return business.removerTipoCustomizacao(idTipoCustomizacao);
	}
	public List<ConfigurarCustomizacaoBean> findAllConfigCustomizacao(Long idModelo)  throws Exception {
		validarUsuario();
		CustomizacaoBusiness business = new CustomizacaoBusiness();
		return business.findAllConfigCustomizacao(idModelo);
	}
	
	public List<ConfigurarCustomizacaoBean> findAllConfigCustomizacao(String modelo, Long idContrato)  throws Exception {
		validarUsuario();
		ContratoComercialBean bean = this.findAllContratoComercial(idContrato);
		List<ConfigurarCustomizacaoBean> result = new ContratoBusiness(this.usuarioBean).findAllTipoCustomizacao(modelo, bean.getIdFamilia(), idContrato);
		if(result == null || result.size() == 0){
			TreeBusiness treeBusiness = new TreeBusiness();
			result = this.findAllConfigCustomizacao(treeBusiness.findModeloId(modelo));
		}
		return result;
	}
	public Boolean removerConfigCustomizacao(Long idConfigCustomizacao)  throws Exception {
		validarUsuario();
		CustomizacaoBusiness business = new CustomizacaoBusiness();
		return business.removerConfigCustomizacao (idConfigCustomizacao);
	}
	
	public ConfigurarCustomizacaoBean saveOrUpdate(ConfigurarCustomizacaoBean bean)  throws Exception {
		validarUsuario();
		CustomizacaoBusiness business = new CustomizacaoBusiness();
		return business.saveOrUpdate(bean);
	}
	
	public Boolean removerTipoTracao(Long idTipoTracao)  throws Exception {
		validarUsuario();
		TracaoBusiness business = new TracaoBusiness();
		return business.removerTipoTracao(idTipoTracao);
	}
	public List<ConfigurarTracaoBean> findAllConfigTracao(Long idModelo)  throws Exception {
		validarUsuario();
		TracaoBusiness business = new TracaoBusiness();
		return business.findAllConfigTracao(idModelo);
	}
	public ConfigurarTracaoBean saveOrUpdate(ConfigurarTracaoBean bean)  throws Exception {
		validarUsuario();
		TracaoBusiness business = new TracaoBusiness();
		return business.saveOrUpdate(bean);
	}
	public Boolean removerConfigTracao(Long idConfigTracao)  throws Exception {
		validarUsuario();
		TracaoBusiness business = new TracaoBusiness();
		return business.removerConfigTracao(idConfigTracao);
	}
	public List<TipoPm> findAllTipoPm() throws Exception {
		validarUsuario();
		TreeBusiness business = new TreeBusiness();
		return business.findAllTipoPm();
	}
	public Boolean terminarContrato(Long idContrato)  throws Exception {
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(this.usuarioBean);
		return business.terminarContrato(idContrato);
	}
	
	public List<UsuarioBean> findAllFuncionariosByCampoPesquisa(String campoPesquisa) throws Exception{
		validarUsuario();
		UsuarioBusiness business = new UsuarioBusiness();
		return business.findAllFuncionariosByCampoPesquisa(campoPesquisa, this.usuarioBean);	
	}
	
	public BigDecimal findHorasRevisao(Long idContHorasStandard)throws Exception{
		validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(this.usuarioBean);
		return business.findHorasRevisao(idContHorasStandard); 
	}
	public String findFabricante(String prefixo)throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(this.usuarioBean);
		return business.findFabricante(prefixo); 
	}
	
	public List<DashboardBean> findAllIndicadores(Long filial){
		DashboardBusiness business = new DashboardBusiness();
		return business.findAllIndicadores(filial); 
	}
	public List<DashboardBean> findAllIndicadoresProdutividade(Long filial){
		DashboardBusiness business = new DashboardBusiness();
		return business.findAllIndicadoresProdutividade(filial); 
	}
	public List<DashboardBean> findAllIndicadoresCliente(Long filial){
		DashboardBusiness business = new DashboardBusiness();
		return business.findAllIndicadoresCliente(filial); 
	}
	public List<DashboardMaquinasBean> findAllIndicadoresMaquinas(Long filial){
		DashboardBusiness business = new DashboardBusiness();
		return business.findAllIndicadoresMaquinas(filial); 
	}
	public List<UsuarioBean> findAllFuncionarios(String campoPesquisa)throws Exception{
		validarUsuario();
		UsuarioBusiness business = new UsuarioBusiness();
		return business.findAllFuncionarios(campoPesquisa, this.usuarioBean);
	}
	public void readMaquinaPlJob()throws Exception{
		//validarUsuario();
		ReadMaquinaPlJob business = new ReadMaquinaPlJob();
		System.out.println("Executei a importação");
		business.execute(null);
		System.out.println("Terminei a importação");
	}
	
	public String findIdEquipamento(String numeroSerie)throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(this.usuarioBean);
		return business.findIdEquipamento(numeroSerie);
	}
	
	public List<ConfigManutencaoBean> findAllPrecoEspecial(String modelo, String prefixo, String contExcessao, Long idFamilia)throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(this.usuarioBean);
		return business.findAllPrecoEspecial(modelo, prefixo,contExcessao, idFamilia);
	}
	public List<ConfigManutencaoBean> findAllPrecoEspecialPromocao(String modelo, String prefixo, String contExcessao, Long idFamilia)throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(this.usuarioBean);
		return business.findAllPrecoEspecialPromocao(modelo, prefixo,contExcessao, idFamilia);
	}
	
	public List<AgendamentoBean> pesquisarOsNaoRealizada(String numOs)throws Exception{
		validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(this.usuarioBean);
		return business.pesquisarOsNaoRealizada(numOs);
	}
	
	public Boolean removerAgendamento(AgendamentoBean bean) throws Exception {
		validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(usuarioBean);
		return business.removerAgendamento(bean);
	}
	
	public ContratoComercialBean saveOrUpdateMonitoramento(ContratoComercialBean contratoComercialBean, String isSaveHorasPosPago) throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.saveOrUpdateMonitoramento(contratoComercialBean, isSaveHorasPosPago);
	}
	public List<ConfigManutencaoHorasBean> findAllHorasManutencao(String numContrato)throws Exception{
		validarUsuario();
		ConfigManutBusiness business = new ConfigManutBusiness(this.usuarioBean);
		return business.findAllHorasManutencao(numContrato);
	}
	public List<UsuarioBean> findAllTecnicos()throws Exception {
		validarUsuario();
		UsuarioBusiness business = new UsuarioBusiness();
		return business.findAllTecnicos(usuarioBean);
	}
	public Boolean pularRevisao(ConfigManutencaoHorasBean horasBean, UsuarioBean bean)throws Exception {
		validarUsuario();
		ConfigManutBusiness business = new ConfigManutBusiness(this.usuarioBean);
		return business.pularRevisao(horasBean, bean);
	}
	public List<CompCodeBean> findAllCompCode()throws Exception{
		validarUsuario();
		CompCodeBusiness business = new CompCodeBusiness(this.usuarioBean);
		return business.findAllCompCode();	
	}
	public Boolean removerCompCode(CompCodeBean bean)throws Exception{
		validarUsuario();
		CompCodeBusiness business = new CompCodeBusiness(this.usuarioBean);
		return business.removerCompCode(bean);	
	}
	public CompCodeBean saveOrUpdateCompCode(CompCodeBean bean)throws Exception{
		validarUsuario();
		CompCodeBusiness business = new CompCodeBusiness(this.usuarioBean);
		return business.saveOrUpdateCompCode(bean);	
	}
	public List<ClassificacaoBean> findAllClassificacao()throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllClassificacao();
	}
	public List<ClassificacaoBean> findAllClassificacaoVendas()throws Exception{
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.findAllClassificacaoVendas();
	}
	public List<MinutaBean> findAllMinuta(String pesquisa)throws Exception{
		validarUsuario();
		OsBusiness business = new OsBusiness(usuarioBean);
		return business.findAllMinuta(pesquisa);
	}
	public NotaFiscalBean findAllNotaFiscal(String numMinuta)throws Exception{
		validarUsuario();
		OsBusiness business = new OsBusiness(usuarioBean);
		return business.findAllNotaFiscal(numMinuta);
	}
	public List<TipoOleoBean> findAllTipoOleo()throws Exception{
		validarUsuario();
		TipoOleoBusiness business = new TipoOleoBusiness();
		return business.findAllTipoOleo();
	}
	
	public Boolean verificarOs(String numeroOs)throws Exception{
		validarUsuario();
		OsBusiness business = new OsBusiness(usuarioBean);
		return business.verificarOs(numeroOs);
	}
	public boolean saveOrUpdateDescontoMultiVac (Long valorDesconto)throws Exception{
		validarUsuario();
		DescontoMultiVacBusiness business = new DescontoMultiVacBusiness();
		return business.saveOrUpdateDescontoMultiVac(valorDesconto);
	}
	public MultiVacBean findMultVac()throws Exception{
		validarUsuario();
		DescontoMultiVacBusiness business = new DescontoMultiVacBusiness();
		return business.findMultVac();
	}
	
	public List<DetalhesVeiculosBean> findallDetalhes() throws Exception{
		validarUsuario();
		VeiculoBusiness business = new VeiculoBusiness();
		return business.findallDetalhes();
	}
	public List<UsuarioBean> findAllTecnicosVeiculos()throws Exception {
		validarUsuario();
		VeiculoBusiness business = new VeiculoBusiness();
		return business.findAllTecnicosVeiculos();
	}
	public DetalhesVeiculosBean saveOrUpdate(DetalhesVeiculosBean bean)throws Exception {
		validarUsuario();
		VeiculoBusiness business = new VeiculoBusiness();
		return business.saveOrUpdate(bean);
	}
	
	public Boolean excluirVeiculo(DetalhesVeiculosBean bean)throws Exception {
		validarUsuario();
		VeiculoBusiness business = new VeiculoBusiness();
		return business.excluirVeiculo(bean);
	}
	
	public Long validarRevisaoProxima(String numerContrato) throws Exception {
		validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);
		return business.validarRevisaoProxima(numerContrato);
	}
	public List<FinanceiroBean> findLiberacaoCredito(String pesquisa) throws Exception {
		this.validarUsuario();
		FinanceiroBusiness business = new FinanceiroBusiness(usuarioBean);			
		return business.findLiberacaoCredito(pesquisa);
	}
	public Boolean aprovarFinanceiro(Long idFinanceiro, String obs) throws Exception {
		this.validarUsuario();
		FinanceiroBusiness business = new FinanceiroBusiness(usuarioBean);			
		return business.aprovarFinanceiro(idFinanceiro, obs, null, null);
	}
	public Boolean rejeitarFinanceiro(Long idFinanceiro, String obs) throws Exception {
		this.validarUsuario();
		FinanceiroBusiness business = new FinanceiroBusiness(usuarioBean);			
		return business.rejeitarFinanceiro(idFinanceiro, obs);
	}
	public Boolean enviarFinanceiro(FinanceiroBean bean) throws Exception {
		this.validarUsuario();
		FinanceiroBusiness business = new FinanceiroBusiness(usuarioBean);			
		return business.enviarFinanceiro(bean);
	}
	public List<StatusOsBean> findAllStatus() throws Exception {
		this.validarUsuario();
		FinanceiroBusiness business = new FinanceiroBusiness(usuarioBean);			
		return business.findAllStatus();
	}
	public String verificarCreditoCliente(Long idContHorasStandard) throws Exception {
		this.validarUsuario();
		FinanceiroBusiness business = new FinanceiroBusiness(usuarioBean);			
		return business.verificarCreditoCliente(idContHorasStandard);
	}
	public FinanceiroBean findObsFinanceiro(Long idContHorasStandard) throws Exception {
		this.validarUsuario();
		FinanceiroBusiness business = new FinanceiroBusiness(usuarioBean);			
		return business.findObsFinanceiro(idContHorasStandard);
	}
	public Boolean fazerUploadEmDiretorioFinanceiro(Long idContHorasStandard, byte[] bytes, String nomeArquivo) throws Exception {
		this.validarUsuario();
		FinanceiroBusiness business = new FinanceiroBusiness(usuarioBean);			
		return business.fazerUploadEmDiretorioFinanceiro(idContHorasStandard, bytes, nomeArquivo);
	}
	public Boolean removerArquivoFinanceiro(Long idContHorasStandard, String nomeArquivo) throws Exception {
		this.validarUsuario();
		FinanceiroBusiness arquivosBusiness = new FinanceiroBusiness(usuarioBean);	
		return arquivosBusiness.removerArquivoFinanceiro(idContHorasStandard, nomeArquivo);
	}
	public Collection<String> findAllArquivos(Long idContHorasStandard) throws Exception {
		this.validarUsuario();
		FinanceiroBusiness arquivosBusiness = new FinanceiroBusiness(usuarioBean);	
		return arquivosBusiness.findAllArquivos(idContHorasStandard);
	}
	public String verificarChamadoCampo(String serie) throws Exception {
		this.validarUsuario();
		ContratoBusiness business = new ContratoBusiness(usuarioBean);	
		return business.verificarChamadoCampo(serie);
	}
	public List<CondicaoPagamentoBean> findAllCondicaoPagamento()throws Exception{
		validarUsuario();
		OsBusiness business = new OsBusiness();
		return business.findAllCondicaoPagamento();
	}
	
	public List<CondicaoPagamentoBean> findAllCondicaoPagamento(String campo)throws Exception{
		validarUsuario();
		OsBusiness business = new OsBusiness();
		return business.findAllCondicaoPagamento(campo);
	}
	
	public AgendamentoBean findValorTelaDataFaturamento(AgendamentoBean  bean)throws Exception{
		validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(this.usuarioBean);
		return business.findValorTelaDataFaturamento(bean);
		
	}
	
	public Boolean verificarLBCC(String numeroOs)throws Exception{
		this.validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(this.usuarioBean);	
		return business.verificarLBCC(numeroOs);
	}
	
	
	public String verificarFilialTecnico(AgendamentoBean bean) throws Exception{
		this.validarUsuario();
		AgendamentoBusiness business = new AgendamentoBusiness(this.usuarioBean);	
		return business.verificarFilialTecnico(bean);
	}
	public List<PecasDbsBean> findPecasAgendamento(Long idAgendamento)throws Exception{
			this.validarUsuario();
			AgendamentoBusiness business = new AgendamentoBusiness(this.usuarioBean);	
			return business.findPecasAgendamento(idAgendamento);
	}
	public String buscarSerie(){
		String numSerie = (String) FlexContext.getFlexSession().getAttribute("serie");
		String modelo = (String) FlexContext.getFlexSession().getAttribute("modelo");
		return numSerie+","+modelo;
	}
	
	
	public List<DescontoPDPSpotBean> findAllDescontoPdpSpot() throws Exception {
		validarUsuario();
		DescontoPdpSpotBusiness business = new DescontoPdpSpotBusiness();
		return business.findAllDescontoPdp();
	}

	public DescontoPDPSpotBean saveOrUpdatePdpSopt(DescontoPDPSpotBean bean) throws Exception {
		validarUsuario();
		DescontoPdpSpotBusiness business = new DescontoPdpSpotBusiness();
		return business.saveOrUpdate(bean);
	}

	public Boolean removerPdpSpot(DescontoPDPSpotBean bean) throws Exception {
		validarUsuario();
		DescontoPdpSpotBusiness business = new DescontoPdpSpotBusiness();
		return business.remover(bean);
	}

}
