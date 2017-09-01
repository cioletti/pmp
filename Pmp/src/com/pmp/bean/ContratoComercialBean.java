package com.pmp.bean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.pmp.entity.PmpContrato;
import com.pmp.entity.PmpContratoMonitoramento;
import com.pmp.util.ValorMonetarioHelper;

public class ContratoComercialBean {

	private Long id;
	private String numeroContrato;
	private String contratoConcessao;
	private String familia;
	private String modelo;
	private String prefixo;
	private String businessGroup;
	private String numeroSerie;
	private String beginRanger;
	private String endRanger;
	private Integer horimetro;
	private String TA;
	private Integer qtdParcelas;
	private Long idTipoContrato;
	private Long statusContrato;
	private String nomeCliente;
	private String cidade;
	private String endereco;
	private String bairro;
	private String razaoSocial;
	private String cnpj;
	private String inscEstadual;
	private String inscMunicipal;
	private String contribuinte;
	private String procurador;
	private String cpf;
	private String contatoComercial;
	private String telComercial;
	private String emailComercial;
	private String faxComercial;
	private String contatoServicos;
	private String telServicos;
	private String emailServicos;
	private String faxServicos;
	private List<RangerBean> rangerList;
	private List<BusinessGroupBean> businessGroupList;
	private List<PrefixoBean> prefixoList;
	private List<ModeloBean> modeloList;
	private List<TipoContratoBean> tipoContrato;
	private List<StatusContratoBean> statusContratoList;
	private List<ConfigurarTracaoBean> configTracaoList;
	private ClienteBean cliente;
	private String financiado;
	private List<ConfigManutencaoHorasBean> configManutencaoHorasBeanList;
	private List<ConfigManutencaoMesesBean> configManutencaoMesesBeanList;
	private String contExcessao;
	private String condExcepcional;
	private String descricaoStatusContrato;
	private String descricaoTipoContrato;
	private Long idMotNaoFecContrato;
	private String hasSendEmail;
    private String valorContrato;
    private String valorSugerido;
    private String codigoCliente;
    private String dataCriacao;
    private String uf;
    private String cep;
    private String siglaStatusContrato;
    private String numOs;
    private String serie;
    private String valorContratoConcessao;
    private Boolean isDeletar;
    private String msg;
    private String nomeFuncionario;
    private Double distanciaGerador;
    private Boolean isGeradorStandby;
    private Long idFamilia;
    private String isFindSubstuicaoTributaria;
    private String numDocDbs;
    private String codErroDbs;
    private String msgErroDbs;
    private String fabricante;
    private Long idConfigTracao;
    private BigDecimal printRevisaoPosPago;
    private String funcionarioIndicado;
    private String matriculaIndicado;
    private List<ConfigurarCustomizacaoBean> tipoCustomizacaoList;
    private Long idConfigPreco;
    private String emailCheckList;
	private List<ConfigManutencaoBean> precoEspecialList;
	private String siglaClassificacaoContrato;
	private String isTerritorio;
	private Long periodoGarantia;
	private Long dataPreEntrega;
	private Long anosContrato;
	private Long mediaHorasMes;
	private String possuiHorasMes;
	private String isSpot;
	private String descPdpSpot;
	private String horasDeslocamentoSpot;
    
    
	public Long getAnosContrato() {
		return anosContrato;
	}
	public void setAnosContrato(Long anosContrato) {
		this.anosContrato = anosContrato;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNumeroContrato() {
		return numeroContrato;
	}
	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}
	public String getContratoConcessao() {
		return contratoConcessao;
	}
	public void setContratoConcessao(String contratoConcessao) {
		this.contratoConcessao = contratoConcessao;
	}
	public String getFamilia() {
		return familia;
	}
	public void setFamilia(String familia) {
		this.familia = familia;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getPrefixo() {
		return prefixo;
	}
	public void setPrefixo(String prefixo) {
		this.prefixo = prefixo;
	}
	public String getBusinessGroup() {
		return businessGroup;
	}
	public void setBusinessGroup(String businessGroup) {
		this.businessGroup = businessGroup;
	}
	public String getNumeroSerie() {
		return numeroSerie;
	}
	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}
	public String getBeginRanger() {
		return beginRanger;
	}
	public void setBeginRanger(String beginRanger) {
		this.beginRanger = beginRanger;
	}
	public String getEndRanger() {
		return endRanger;
	}
	public void setEndRanger(String endRanger) {
		this.endRanger = endRanger;
	}
	public Integer getHorimetro() {
		return horimetro;
	}
	public void setHorimetro(Integer horimetro) {
		this.horimetro = horimetro;
	}
	public String getTA() {
		return TA;
	}
	public void setTA(String ta) {
		TA = ta;
	}
	public Integer getQtdParcelas() {
		return qtdParcelas;
	}
	public void setQtdParcelas(Integer qtdParcelas) {
		this.qtdParcelas = qtdParcelas;
	}
	public Long getIdTipoContrato() {
		return idTipoContrato;
	}
	public void setIdTipoContrato(Long idTipoContrato) {
		this.idTipoContrato = idTipoContrato;
	}
	public Long getStatusContrato() {
		return statusContrato;
	}
	public void setStatusContrato(Long statusContrato) {
		this.statusContrato = statusContrato;
	}
	public String getNomeCliente() {
		return nomeCliente;
	}
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getRazaoSocial() {
		return razaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getInscEstadual() {
		return inscEstadual;
	}
	public void setInscEstadual(String inscEstadual) {
		this.inscEstadual = inscEstadual;
	}
	public String getInscMunicipal() {
		return inscMunicipal;
	}
	public void setInscMunicipal(String inscMunicipal) {
		this.inscMunicipal = inscMunicipal;
	}
	public String getContribuinte() {
		return contribuinte;
	}
	public void setContribuinte(String contribuinte) {
		this.contribuinte = contribuinte;
	}
	public String getProcurador() {
		return procurador;
	}
	public void setProcurador(String procurador) {
		this.procurador = procurador;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getContatoComercial() {
		return contatoComercial;
	}
	public void setContatoComercial(String contatoComercial) {
		this.contatoComercial = contatoComercial;
	}
	public String getTelComercial() {
		return telComercial;
	}
	public void setTelComercial(String telComercial) {
		this.telComercial = telComercial;
	}
	public String getEmailComercial() {
		return emailComercial;
	}
	public void setEmailComercial(String emailComercial) {
		this.emailComercial = emailComercial;
	}
	public String getFaxComercial() {
		return faxComercial;
	}
	public void setFaxComercial(String faxComercial) {
		this.faxComercial = faxComercial;
	}
	public String getContatoServicos() {
		return contatoServicos;
	}
	public void setContatoServicos(String contatoServicos) {
		this.contatoServicos = contatoServicos;
	}
	public String getTelServicos() {
		return telServicos;
	}
	public void setTelServicos(String telServicos) {
		this.telServicos = telServicos;
	}
	public String getEmailServicos() {
		return emailServicos;
	}
	public void setEmailServicos(String emailServicos) {
		this.emailServicos = emailServicos;
	}
	public String getFaxServicos() {
		return faxServicos;
	}
	public void setFaxServicos(String faxServicos) {
		this.faxServicos = faxServicos;
	}
	public List<RangerBean> getRangerList() {
		return rangerList;
	}
	public void setRangerList(List<RangerBean> rangerList) {
		this.rangerList = rangerList;
	}
	public List<BusinessGroupBean> getBusinessGroupList() {
		return businessGroupList;
	}
	public void setBusinessGroupList(List<BusinessGroupBean> businessGroupList) {
		this.businessGroupList = businessGroupList;
	}
	public List<PrefixoBean> getPrefixoList() {
		return prefixoList;
	}
	public void setPrefixoList(List<PrefixoBean> prefixoList) {
		this.prefixoList = prefixoList;
	}
	public List<ModeloBean> getModeloList() {
		return modeloList;
	}
	public void setModeloList(List<ModeloBean> modeloList) {
		this.modeloList = modeloList;
	}
	public List<TipoContratoBean> getTipoContrato() {
		return tipoContrato;
	}
	public void setTipoContrato(List<TipoContratoBean> tipoContrato) {
		this.tipoContrato = tipoContrato;
	}
	public List<StatusContratoBean> getStatusContratoList() {
		return statusContratoList;
	}
	public void setStatusContratoList(List<StatusContratoBean> statusContratoList) {
		this.statusContratoList = statusContratoList;
	}
	public ClienteBean getCliente() {
		return cliente;
	}
	public void setCliente(ClienteBean cliente) {
		this.cliente = cliente;
	}
	public String getValorContrato() {
		return valorContrato;
	}
	public void setValorContrato(String valorContrato) {
		this.valorContrato = valorContrato;
	}
	public String getFinanciado() {
		return financiado;
	}
	public void setFinanciado(String financiado) {
		this.financiado = financiado;
	}
	public List<ConfigManutencaoHorasBean> getConfigManutencaoHorasBeanList() {
		return configManutencaoHorasBeanList;
	}
	public void setConfigManutencaoHorasBeanList(
			List<ConfigManutencaoHorasBean> configManutencaoHorasBeanList) {
		this.configManutencaoHorasBeanList = configManutencaoHorasBeanList;
	}
	
	public List<ConfigManutencaoMesesBean> getConfigManutencaoMesesBeanList() {
		return configManutencaoMesesBeanList;
	}
	public void setConfigManutencaoMesesBeanList(
			List<ConfigManutencaoMesesBean> configManutencaoMesesBeanList) {
		this.configManutencaoMesesBeanList = configManutencaoMesesBeanList;
	}
	public void setIsGeradorStandby(Boolean isGeradorStandby) {
		this.isGeradorStandby = isGeradorStandby;
	}
	public String getContExcessao() {
		return contExcessao;
	}
	public void setContExcessao(String contExcessao) {
		this.contExcessao = contExcessao;
	}
	public String getCondExcepcional() {
		return condExcepcional;
	}
	public void setCondExcepcional(String condExcepcional) {
		this.condExcepcional = condExcepcional;
	}
	public String getDescricaoStatusContrato() {
		return descricaoStatusContrato;
	}
	public void setDescricaoStatusContrato(String descricaoStatusContrato) {
		this.descricaoStatusContrato = descricaoStatusContrato;
	}
	public String getDescricaoTipoContrato() {
		return descricaoTipoContrato;
	}
	public void setDescricaoTipoContrato(String descricaoTipoContrato) {
		this.descricaoTipoContrato = descricaoTipoContrato;
	}
	public Long getIdMotNaoFecContrato() {
		return idMotNaoFecContrato;
	}
	public void setIdMotNaoFecContrato(Long idMotNaoFecContrato) {
		this.idMotNaoFecContrato = idMotNaoFecContrato;
	}
	public String getHasSendEmail() {
		return hasSendEmail;
	}
	public void setHasSendEmail(String hasSendEmail) {
		this.hasSendEmail = hasSendEmail;
	}
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(String dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getSiglaStatusContrato() {
		return siglaStatusContrato;
	}
	public void setSiglaStatusContrato(String siglaStatusContrato) {
		this.siglaStatusContrato = siglaStatusContrato;
	}
	public String getNumOs() {
		return numOs;
	}
	public void setNumOs(String numOs) {
		this.numOs = numOs;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getValorContratoConcessao() {
		return valorContratoConcessao;
	}
	public void setValorContratoConcessao(String valorContratoConcessao) {
		this.valorContratoConcessao = valorContratoConcessao;
	}
	public Boolean getIsDeletar() {
		return isDeletar;
	}
	public void setIsDeletar(Boolean isDeletar) {
		this.isDeletar = isDeletar;
	}
	public Boolean getIsGeradorStandby() {
		return isGeradorStandby;
	}
	public void setGeradorStandby(Boolean isGeradorStandby) {
		this.isGeradorStandby = isGeradorStandby;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getNomeFuncionario() {
		return nomeFuncionario;
	}
	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}
	public Double getDistanciaGerador() {
		return distanciaGerador;
	}
	public void setDistanciaGerador(Double distanciaGerador) {
		this.distanciaGerador = distanciaGerador;
	}

	public Long getIdFamilia() {
		return idFamilia;
	}
	public void setIdFamilia(Long idFamilia) {
		this.idFamilia = idFamilia;
	}
	public String getIsFindSubstuicaoTributaria() {
		return isFindSubstuicaoTributaria;
	}
	public void setIsFindSubstuicaoTributaria(String isFindSubstuicaoTributaria) {
		this.isFindSubstuicaoTributaria = isFindSubstuicaoTributaria;
	}
	public String getNumDocDbs() {
		return numDocDbs;
	}
	public void setNumDocDbs(String numDocDbs) {
		this.numDocDbs = numDocDbs;
	}
	public String getCodErroDbs() {
		return codErroDbs;
	}
	public void setCodErroDbs(String codErroDbs) {
		this.codErroDbs = codErroDbs;
	}
	public String getMsgErroDbs() {
		return msgErroDbs;
	}
	public void setMsgErroDbs(String msgErroDbs) {
		this.msgErroDbs = msgErroDbs;
	}
	public String getFabricante() {
		return fabricante;
	}
	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}
	public Long getIdConfigTracao() {
		return idConfigTracao;
	}
	public void setIdConfigTracao(Long idConfigTracao) {
		this.idConfigTracao = idConfigTracao;
	}
	public List<ConfigurarTracaoBean> getConfigTracaoList() {
		return configTracaoList;
	}
	public void setConfigTracaoList(List<ConfigurarTracaoBean> configTracaoList) {
		this.configTracaoList = configTracaoList;
	}
	public String getValorSugerido() {
		return valorSugerido;
	}
	public void setValorSugerido(String valorSugerido) {
		this.valorSugerido = valorSugerido;
	}
	public BigDecimal getPrintRevisaoPosPago() {
		return printRevisaoPosPago;
	}
	public void setPrintRevisaoPosPago(BigDecimal printRevisaoPosPago) {
		this.printRevisaoPosPago = printRevisaoPosPago;
	}
	public String getFuncionarioIndicado() {
		return funcionarioIndicado;
	}
	public void setFuncionarioIndicado(String funcionarioIndicado) {
		this.funcionarioIndicado = funcionarioIndicado;
	}
	public String getMatriculaIndicado() {
		return matriculaIndicado;
	}
	public void setMatriculaIndicado(String matriculaIndicado) {
		this.matriculaIndicado = matriculaIndicado;
	}
	public List<ConfigurarCustomizacaoBean> getTipoCustomizacaoList() {
		return tipoCustomizacaoList;
	}
	public void setTipoCustomizacaoList(
			List<ConfigurarCustomizacaoBean> tipoCustomizacaoList) {
		this.tipoCustomizacaoList = tipoCustomizacaoList;
	}
	public Long getIdConfigPreco() {
		return idConfigPreco;
	}
	public void setIdConfigPreco(Long idConfigPreco) {
		this.idConfigPreco = idConfigPreco;
	}
	public String getEmailCheckList() {
		return emailCheckList;
	}
	public void setEmailCheckList(String emailCheckList) {
		this.emailCheckList = emailCheckList;
	}
	public List<ConfigManutencaoBean> getPrecoEspecialList() {
		return precoEspecialList;
	}
	public void setPrecoEspecialList(List<ConfigManutencaoBean> precoEspecialList) {
		this.precoEspecialList = precoEspecialList;
	}
	public String getSiglaClassificacaoContrato() {
		return siglaClassificacaoContrato;
	}
	public void setSiglaClassificacaoContrato(String siglaClassificacaoContrato) {
		this.siglaClassificacaoContrato = siglaClassificacaoContrato;
	}
	public String getIsTerritorio() {
		return isTerritorio;
	}
	public void setIsTerritorio(String isTerritorio) {
		this.isTerritorio = isTerritorio;
	}
	public Long getPeriodoGarantia() {
		return periodoGarantia;
	}
	public void setPeriodoGarantia(Long periodoGarantia) {
		this.periodoGarantia = periodoGarantia;
	}
	public Long getDataPreEntrega() {
		return dataPreEntrega;
	}
	public void setDataPreEntrega(Long dataPreEntrega) {
		this.dataPreEntrega = dataPreEntrega;
	}
	public Long getMediaHorasMes() {
		return mediaHorasMes;
	}
	public void setMediaHorasMes(Long mediaHorasMes) {
		this.mediaHorasMes = mediaHorasMes;
	}
	public String getPossuiHorasMes() {
		return possuiHorasMes;
	}
	public void setPossuiHorasMes(String possuiHorasMes) {
		this.possuiHorasMes = possuiHorasMes;
	}
	public String getIsSpot() {
		return isSpot;
	}
	public void setIsSpot(String isSpot) {
		this.isSpot = isSpot;
	}
	public String getHorasDeslocamentoSpot() {
		return horasDeslocamentoSpot;
	}
	public void setHorasDeslocamentoSpot(String horasDeslocamentoSpot) {
		this.horasDeslocamentoSpot = horasDeslocamentoSpot;
	}
	public String getDescPdpSpot() {
		return descPdpSpot;
	}
	public void setDescPdpSpot(String descPdpSpot) {
		this.descPdpSpot = descPdpSpot;
	}
	public void toBean(PmpContrato contrato){
		contrato.setAnoContrato(getAnosContrato());
		contrato.setNomeCliente(getNomeCliente());
		contrato.setTa(getTA());
		contrato.setBgrp(getBusinessGroup());
		contrato.setHorimetro(new BigInteger(getHorimetro().toString()));
		
		if (getDistanciaGerador() != null) {
			contrato.setDistanciaGerador(new BigDecimal(getDistanciaGerador().toString()));
		}

		contrato.setNumeroSerie(getNumeroSerie());
		contrato.setModelo(getModelo());
		contrato.setTelefoneComercial(getTelComercial());
		contrato.setRazaoSocial(getRazaoSocial());
		contrato.setContatoComercial(getContatoComercial());
		contrato.setFamilia(getFamilia());
		contrato.setIdFamilia(getIdFamilia());
		contrato.setCidade(getCidade());
		contrato.setEndereco(getEndereco());
		contrato.setBairro(getBairro());
		contrato.setCnpj(getCnpj());
		contrato.setInscEstadual(getInscEstadual());
		contrato.setInscMunicipal(getInscMunicipal());
		contrato.setContribuinte(getContribuinte());
		contrato.setProcurador(getProcurador());
		contrato.setCpf(getCpf());
		contrato.setEmailContatoComercial(getEmailComercial());
		contrato.setContatoServicos(getContatoServicos());
		contrato.setEmailContatoServicos(getEmailServicos());
		contrato.setFinanciado(getFinanciado());
		contrato.setQtdParcelas((getQtdParcelas()!= null)?getQtdParcelas().longValue():null);
		contrato.setTelefoneServicos(getTelServicos());
		contrato.setFaxComercial(getFaxComercial());
		contrato.setFaxServicos(getFaxServicos());
		contrato.setBeginRanger(getBeginRanger());
		contrato.setEndRanger(getEndRanger());
		contrato.setDataCriacao((contrato.getDataCriacao() == null)?new Date():contrato.getDataCriacao());
		contrato.setPrefixo(getPrefixo());
		contrato.setCondicaoExcepcional(getCondExcepcional());
		contrato.setContExcessao(getContExcessao());
		contrato.setUf(getUf());
		contrato.setFabricante(getFabricante());
		contrato.setCep(getCep());
		contrato.setHasSendEmail("S");
		contrato.setMediaHorasMes(getMediaHorasMes());
		contrato.setPossuiHorasMes(getPossuiHorasMes());
		if(getValorContrato() != null && !"".equals(getValorContrato())){
			contrato.setValoContrato((BigDecimal.valueOf(Double.valueOf(getValorContrato().replace(".", "").replace(",", ".")))));
		}
		if(getValorSugerido() != null && !"".equals(getValorSugerido())){
			contrato.setValorSugerido((BigDecimal.valueOf(Double.valueOf(getValorSugerido().replace(".", "").replace(",", ".")))));
		}
		if(getValorContratoConcessao() != null && !"".equals(getValorContratoConcessao())){
			contrato.setValoConcessao((BigDecimal.valueOf(Double.valueOf(getValorContratoConcessao().replace(".", "").replace(",", ".")))));
		}
		contrato.setCodigoCliente(getCodigoCliente());
		contrato.setEmailChecklist(getEmailCheckList());
		contrato.setIsSpot(getIsSpot());
		if(getDescPdpSpot() != null && !"".equals(getDescPdpSpot())){
			contrato.setDescontoPdpSpot((BigDecimal.valueOf(Double.valueOf(getDescPdpSpot().replace(".", "").replace(",", ".")))));
			contrato.setQtdHorasDeslSpot(getHorasDeslocamentoSpot());
		}
		
	}
	public void toBean(PmpContratoMonitoramento contrato){
		contrato.setNomeCliente(getNomeCliente());
		contrato.setTa(getTA());
		contrato.setBgrp(getBusinessGroup());
		contrato.setHorimetro(new BigInteger(getHorimetro().toString()));
		
		if (getDistanciaGerador() != null) {
			contrato.setDistanciaGerador(new BigDecimal(getDistanciaGerador().toString()));
		}
		
		contrato.setNumeroSerie(getNumeroSerie());
		contrato.setModelo(getModelo());
		contrato.setTelefoneComercial(getTelComercial());
		contrato.setRazaoSocial(getRazaoSocial());
		contrato.setContatoComercial(getContatoComercial());
		contrato.setFamilia(getFamilia());
		contrato.setIdFamilia(getIdFamilia());
		contrato.setCidade(getCidade());
		contrato.setEndereco(getEndereco());
		contrato.setBairro(getBairro());
		contrato.setCnpj(getCnpj());
		contrato.setInscEstadual(getInscEstadual());
		contrato.setInscMunicipal(getInscMunicipal());
		contrato.setContribuinte(getContribuinte());
		contrato.setProcurador(getProcurador());
		contrato.setCpf(getCpf());
		contrato.setEmailContatoComercial(getEmailComercial());
		contrato.setContatoServicos(getContatoServicos());
		contrato.setEmailContatoServicos(getEmailServicos());
		contrato.setFinanciado(getFinanciado());
		contrato.setQtdParcelas((getQtdParcelas()!= null)?getQtdParcelas().longValue():null);
		contrato.setTelefoneServicos(getTelServicos());
		contrato.setFaxComercial(getFaxComercial());
		contrato.setFaxServicos(getFaxServicos());
		contrato.setBeginRanger(getBeginRanger());
		contrato.setEndRanger(getEndRanger());
		contrato.setDataCriacao((contrato.getDataCriacao() == null)?new Date():contrato.getDataCriacao());
		contrato.setPrefixo(getPrefixo());
		contrato.setCondicaoExcepcional(getCondExcepcional());
		contrato.setContExcessao(getContExcessao());
		contrato.setUf(getUf());
		contrato.setFabricante(getFabricante());
		contrato.setCep(getCep());
		contrato.setHasSendEmail("S");
		if(getValorContrato() != null && !"".equals(getValorContrato())){
			contrato.setValoContrato((BigDecimal.valueOf(Double.valueOf(getValorContrato().replace(".", "").replace(",", ".")))));
		}
		if(getValorSugerido() != null && !"".equals(getValorSugerido())){
			contrato.setValorSugerido((BigDecimal.valueOf(Double.valueOf(getValorSugerido().replace(".", "").replace(",", ".")))));
		}
		if(getValorContratoConcessao() != null && !"".equals(getValorContratoConcessao())){
			contrato.setValoConcessao((BigDecimal.valueOf(Double.valueOf(getValorContratoConcessao().replace(".", "").replace(",", ".")))));
		}
		contrato.setCodigoCliente(getCodigoCliente());
		contrato.setEmailChecklist(getEmailCheckList());
	}
	public void fromBean(PmpContrato contrato, UsuarioBean usuarioBean){
		setId(contrato.getId());
		setNomeCliente(contrato.getNomeCliente());
		setTA(contrato.getTa());
		setBusinessGroup(contrato.getBgrp());
		setHorimetro(contrato.getHorimetro().intValue());
		setNumeroSerie(contrato.getNumeroSerie());
		setModelo(contrato.getModelo());
		setTelComercial(contrato.getTelefoneComercial());
		setRazaoSocial(contrato.getRazaoSocial());
		setContatoComercial(contrato.getContatoComercial());
		setFamilia(contrato.getFamilia());
		setIdFamilia(contrato.getIdFamilia());
		setCidade(contrato.getCidade());
		setEndereco(contrato.getEndereco());
		setBairro(contrato.getBairro());
		setCnpj(contrato.getCnpj());
		setInscEstadual(contrato.getInscEstadual());
		setInscMunicipal(contrato.getInscMunicipal());
		setContribuinte(contrato.getContribuinte());
		setProcurador(contrato.getProcurador());
		setCpf(contrato.getCpf());
		setEmailComercial(contrato.getEmailContatoComercial());
		setContatoServicos(contrato.getContatoServicos());
		setEmailServicos(contrato.getEmailContatoServicos());
		setFinanciado(contrato.getFinanciado());
		setQtdParcelas(contrato.getQtdParcelas().intValue());
		setTelServicos(contrato.getTelefoneServicos());
		setFaxComercial(contrato.getFaxComercial());
		setFaxServicos(contrato.getFaxServicos());
		setBeginRanger(contrato.getBeginRanger());
		setEndRanger(contrato.getEndRanger());
		setPrefixo(contrato.getPrefixo());
		setCondExcepcional(contrato.getCondicaoExcepcional());
		setContExcessao(contrato.getContExcessao());
		setDescricaoStatusContrato(contrato.getIdStatusContrato().getDescricao());
		setDescricaoTipoContrato(contrato.getIdTipoContrato().getDescricao());
		setIdTipoContrato(contrato.getIdTipoContrato().getId());
		setStatusContrato(contrato.getIdStatusContrato().getId());
		setNumeroContrato(contrato.getNumeroContrato());
		setIdMotNaoFecContrato((contrato.getIdMotivoNaoFecContrato() != null)?contrato.getIdMotivoNaoFecContrato().getId():0);
		setHasSendEmail(contrato.getHasSendEmail());
		setUf(contrato.getUf());
		setFabricante(contrato.getFabricante());
		setCep(contrato.getCep());
		setHasSendEmail(contrato.getHasSendEmail());
		if(contrato.getValoContrato() != null){
			setValorContrato((String.valueOf(ValorMonetarioHelper.formata("###,###,##0.00", Double.valueOf(String.valueOf(contrato.getValoContrato()))))));
		}
		if(contrato.getValoConcessao() != null){
			setValorContratoConcessao((String.valueOf(ValorMonetarioHelper.formata("###,###,##0.00", Double.valueOf(String.valueOf(contrato.getValoConcessao()))))));
		}
		setCodigoCliente(contrato.getCodigoCliente());
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		setDataCriacao(format.format(contrato.getDataCriacao()));
		setSiglaStatusContrato(contrato.getIdStatusContrato().getSigla());
		setSerie(contrato.getNumeroSerie());
		setIsDeletar((usuarioBean.getIsAdm()));
		setIsFindSubstuicaoTributaria(contrato.getIsFindSubTributaria());
		setCodErroDbs(contrato.getCodErroDbs());
		setMsgErroDbs(contrato.getMsgErroDbs());
		setNumDocDbs(contrato.getNumDocDbs());
		if(contrato.getIdConfigTracao() != null){
			setIdConfigTracao(contrato.getIdConfigTracao().getId());
		}
		if(contrato.getIdConfigManutencao().getIdConfiguracaoPreco() != null){
			setIdConfigPreco(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getId());
		}
		setEmailCheckList(contrato.getEmailChecklist());
		setPrintRevisaoPosPago(contrato.getPrintRevisaoPosPago());
		setIsTerritorio(contrato.getIsTerritorio());
		setSiglaClassificacaoContrato(contrato.getIdClassificacaoContrato().getSigla());
		setMediaHorasMes(contrato.getMediaHorasMes());
		setPossuiHorasMes(contrato.getPossuiHorasMes());
		setIsSpot(contrato.getIsSpot());
		if(contrato.getDescontoPdpSpot() != null){
			setDescPdpSpot((String.valueOf(ValorMonetarioHelper.formata("###,###,##0.00", Double.valueOf(String.valueOf(contrato.getDescontoPdpSpot()))))));
			setHorasDeslocamentoSpot(contrato.getQtdHorasDeslSpot());
		}
	}
	
}
