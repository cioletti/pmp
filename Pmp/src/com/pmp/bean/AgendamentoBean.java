package com.pmp.bean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AgendamentoBean implements Comparator{ 
	private Long id;
	private Long idContrato;
	private String numSerie;
	private Long horimetro;
	private Long horasPendentes;
	private String codigoCliente;
	private String modelo;
	private Long idStatusAgendamento;
	private String idFuncionario;
	private Long idConfOperacional;
	private Long horasRevisao;
	private String dataAgendamento;
	private String dataAgendamentoFinal;
	private String siglaStatus;
	private String numOs;  
	private String local;
	private String contato;
	private String numContrato;
	private String telefone;
	private String statusAgendamento;
	private Long idContHorasStandard;
	private String dataAtualizacaoHorimetro;
	private String standardJob;
	private String filial;
	private String filialDestino;
	private String siglaTipoContrato;
	private String razaoSocial;
	private List<PecasDbsBean> pecasList;
	private List<AgendamentoBean> agendamentoList;
	private String numDoc;
	private String msg;
	private String codErroOsDbs;
	private String codErroDocDbs;
	private Long idOsOperacional;
	private String isFindTecnico;
	private String obs;
	private String obsOs;
	private Integer totalRegistros;
	private String dataFaturamento;
	private String idAjudante;
	private String nomeAjudante;
	private Double horasTrabalhadas;
	private String obsCheckList;
	private String isOrderOs;
	private String obsTecnico;
	private String emailContato;
	private Integer diasProximaRevisao;
	private String siglaClassificacaoContrato;
	private String isPartner;
	private String mediaDiasProximaRevisao;
	
	
	private String moMiscDesl;
	private String valorPecas;
	private String valorTotal;
	private String condicaoPagamento;
	private String propNumero;
	private String autPor;
	private String ordemCompra;
	private String preenchidoPor;
	private String obsNf;
	private String emissao;
	
	private String ordemCompraPeca;
	private String ordemCompraServico;
	
	private String estabelecimentoCredorPecas;
	private String estabelecimentoCredorServicos;
	
	private String condicaoPagamentoPecas;
	private String condicaoPagamentoServicos;
	
	private String descPorcPecas;
	private String descPorcServicos;
	
	private String descValorPecas;
	private String descValorServicos;
	
	private String automaticaFaturada;
	
	private String obsPeca;
	private String obsServico;
	private String condEspecial;
	
	private Long credorPeca;
	private Long credorServico;
	private String jobCode;
	private String compCode;
	private String horimetroUltimaRevisao;
	private String horimetroProximaRevisao;
	private String horimetroFaltantes;

	public String getIsPartner() {
		return isPartner;
	}
	public void setIsPartner(String isPartner) {
		this.isPartner = isPartner;
	}
	public Integer getTotalRegistros() {
		return totalRegistros;
	}
	public void setTotalRegistros(Integer totalRegistros) {
		this.totalRegistros = totalRegistros;
	}
	public AgendamentoBean() {
		agendamentoList = new ArrayList<AgendamentoBean>();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdStatusAgendamento() {
		return idStatusAgendamento;
	}
	public void setIdStatusAgendamento(Long idStatusAgendamento) {
		this.idStatusAgendamento = idStatusAgendamento;
	}
	public String getIdFuncionario() {
		return idFuncionario;
	}
	public void setIdFuncionario(String idFuncionario) {
		this.idFuncionario = idFuncionario;
	}
	public String getDataAgendamento() {
		return dataAgendamento;
	}
	public void setDataAgendamento(String dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}
	public String getSiglaStatus() {
		return siglaStatus;
	}
	public void setSiglaStatus(String siglaStatus) {
		this.siglaStatus = siglaStatus;
	}
	public Long getIdConfOperacional() {
		return idConfOperacional;
	}
	public void setIdConfOperacional(Long idConfOperacional) {
		this.idConfOperacional = idConfOperacional;
	}
	public Long getHorasRevisao() {
		return horasRevisao;
	}
	public void setHorasRevisao(Long horasRevisao) {
		this.horasRevisao = horasRevisao;
	}
	public String getNumOs() {
		return numOs;
	}
	public void setNumOs(String numOs) {
		this.numOs = numOs;
	}
	public List<AgendamentoBean> getAgendamentoList() {
		return agendamentoList;
	}
	public void setAgendamentoList(List<AgendamentoBean> agendamentoList) {
		this.agendamentoList = agendamentoList;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getDataAgendamentoFinal() {
		return dataAgendamentoFinal;
	}
	public void setDataAgendamentoFinal(String dataAgendamentoFinal) {
		this.dataAgendamentoFinal = dataAgendamentoFinal;
	}
	public List<PecasDbsBean> getPecasList() {
		return pecasList;
	}
	public void setPecasList(List<PecasDbsBean> pecasList) {
		this.pecasList = pecasList;
	}
	public String getContato() {
		return contato;
	}
	public void setContato(String contato) {
		this.contato = contato;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public Long getIdContHorasStandard() {
		return idContHorasStandard;
	}
	public void setIdContHorasStandard(Long idContHorasStandard) {
		this.idContHorasStandard = idContHorasStandard;
	}
	public Long getIdContrato() {
		return idContrato;
	}
	public void setIdContrato(Long idContrato) {
		this.idContrato = idContrato;
	}
	public String getNumSerie() {
		return numSerie;
	}
	public void setNumSerie(String numSerie) {
		this.numSerie = numSerie;
	}
	public Long getHorimetro() {
		return horimetro;
	}
	public void setHorimetro(Long horimetro) {
		this.horimetro = horimetro;
	}
	public Long getHorasPendentes() {
		return horasPendentes;
	}
	public void setHorasPendentes(Long horasPendentes) {
		this.horasPendentes = horasPendentes;
	}
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getNumContrato() {
		return numContrato;
	}
	public void setNumContrato(String numContrato) {
		this.numContrato = numContrato;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getStatusAgendamento() {
		return statusAgendamento;
	}
	public void setStatusAgendamento(String statusAgendamento) {
		this.statusAgendamento = statusAgendamento;
	}
	public String getDataAtualizacaoHorimetro() {
		return dataAtualizacaoHorimetro;
	}
	public void setDataAtualizacaoHorimetro(String dataAtualizacaoHorimetro) {
		this.dataAtualizacaoHorimetro = dataAtualizacaoHorimetro;
	}
	public String getStandardJob() {
		return standardJob;
	}
	public void setStandardJob(String standardJob) {
		this.standardJob = standardJob;
	}
	public String getFilial() {
		return filial;
	}
	public void setFilial(String filial) {
		this.filial = filial;
	}
	public String getSiglaTipoContrato() {
		return siglaTipoContrato;
	}
	public void setSiglaTipoContrato(String siglaTipoContrato) {
		this.siglaTipoContrato = siglaTipoContrato;
	}
	public String getFilialDestino() {
		return filialDestino;
	}
	public void setFilialDestino(String filialDestino) {
		this.filialDestino = filialDestino;
	}
	public String getRazaoSocial() {
		return razaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	public String getNumDoc() {
		return numDoc;
	}
	public void setNumDoc(String numDoc) {
		this.numDoc = numDoc;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCodErroOsDbs() {
		return codErroOsDbs;
	}
	public void setCodErroOsDbs(String codErroOsDbs) {
		this.codErroOsDbs = codErroOsDbs;
	}
	public String getCodErroDocDbs() {
		return codErroDocDbs;
	}
	public void setCodErroDocDbs(String codErroDocDbs) {
		this.codErroDocDbs = codErroDocDbs;
	}
	public Long getIdOsOperacional() {
		return idOsOperacional;
	}
	public void setIdOsOperacional(Long idOsOperacional) {
		this.idOsOperacional = idOsOperacional;
	}
	public String getIsFindTecnico() {
		return isFindTecnico;
	}
	public void setIsFindTecnico(String isFindTecnico) {
		this.isFindTecnico = isFindTecnico;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	public String getDataFaturamento() {
		return dataFaturamento;
	}
	public void setDataFaturamento(String dataFaturamento) {
		this.dataFaturamento = dataFaturamento;
	}
	public String getObsOs() {
		return obsOs;
	}
	public void setObsOs(String obsOs) {
		this.obsOs = obsOs;
	}
	public String getIdAjudante() {
		return idAjudante;
	}
	public void setIdAjudante(String idAjudante) {
		this.idAjudante = idAjudante;
	}
	public String getNomeAjudante() {
		return nomeAjudante;
	}
	public void setNomeAjudante(String nomeAjudante) {
		this.nomeAjudante = nomeAjudante;
	}
	public Double getHorasTrabalhadas() {
		return horasTrabalhadas;
	}
	public void setHorasTrabalhadas(Double horasTrabalhadas) {
		this.horasTrabalhadas = horasTrabalhadas;
	}
	public String getObsCheckList() {
		return obsCheckList;
	}
	public void setObsCheckList(String obsCheckList) {
		this.obsCheckList = obsCheckList;
	}
	public String getIsOrderOs() {
		return isOrderOs;
	}
	public void setIsOrderOs(String isOrderOs) {
		this.isOrderOs = isOrderOs;
	}
	public String getObsTecnico() {
		return obsTecnico;
	}
	public void setObsTecnico(String obsTecnico) {
		this.obsTecnico = obsTecnico;
	}

	public String getEmailContato() {
		return emailContato;
	}
	public void setEmailContato(String emailContato) {
		this.emailContato = emailContato;
	}
	public Integer getDiasProximaRevisao() {
		return diasProximaRevisao;
	}
	public void setDiasProximaRevisao(Integer diasProximaRevisao) {
		this.diasProximaRevisao = diasProximaRevisao;
	}
	public String getSiglaClassificacaoContrato() {
		return siglaClassificacaoContrato;
	}
	public void setSiglaClassificacaoContrato(String siglaClassificacaoContrato) {
		this.siglaClassificacaoContrato = siglaClassificacaoContrato;
	}
	public String getMediaDiasProximaRevisao() {
		return mediaDiasProximaRevisao;
	}
	public void setMediaDiasProximaRevisao(String mediaDiasProximaRevisao) {
		this.mediaDiasProximaRevisao = mediaDiasProximaRevisao;
	}
	public String getMoMiscDesl() {
		return moMiscDesl;
	}
	public void setMoMiscDesl(String moMiscDesl) {
		this.moMiscDesl = moMiscDesl;
	}
	public String getValorPecas() {
		return valorPecas;
	}
	public void setValorPecas(String valorPecas) {
		this.valorPecas = valorPecas;
	}
	public String getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}
	public String getCondicaoPagamento() {
		return condicaoPagamento;
	}
	public void setCondicaoPagamento(String condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}
	public String getPropNumero() {
		return propNumero;
	}
	public void setPropNumero(String propNumero) {
		this.propNumero = propNumero;
	}
	public String getAutPor() {
		return autPor;
	}
	public void setAutPor(String autPor) {
		this.autPor = autPor;
	}
	public String getOrdemCompra() {
		return ordemCompra;
	}
	public void setOrdemCompra(String ordemCompra) {
		this.ordemCompra = ordemCompra;
	}
	public String getPreenchidoPor() {
		return preenchidoPor;
	}
	public void setPreenchidoPor(String preenchidoPor) {
		this.preenchidoPor = preenchidoPor;
	}
	public String getObsNf() {
		return obsNf;
	}
	public void setObsNf(String obsNf) {
		this.obsNf = obsNf;
	}
	public String getEmissao() {
		return emissao;
	}
	public void setEmissao(String emissao) {
		this.emissao = emissao;
	}
	public String getOrdemCompraPeca() {
		return ordemCompraPeca;
	}
	public void setOrdemCompraPeca(String ordemCompraPeca) {
		this.ordemCompraPeca = ordemCompraPeca;
	}
	public String getOrdemCompraServico() {
		return ordemCompraServico;
	}
	public void setOrdemCompraServico(String ordemCompraServico) {
		this.ordemCompraServico = ordemCompraServico;
	}
	public String getEstabelecimentoCredorPecas() {
		return estabelecimentoCredorPecas;
	}
	public void setEstabelecimentoCredorPecas(String estabelecimentoCredorPecas) {
		this.estabelecimentoCredorPecas = estabelecimentoCredorPecas;
	}
	public String getEstabelecimentoCredorServicos() {
		return estabelecimentoCredorServicos;
	}
	public void setEstabelecimentoCredorServicos(
			String estabelecimentoCredorServicos) {
		this.estabelecimentoCredorServicos = estabelecimentoCredorServicos;
	}
	public String getCondicaoPagamentoPecas() {
		return condicaoPagamentoPecas;
	}
	public void setCondicaoPagamentoPecas(String condicaoPagamentoPecas) {
		this.condicaoPagamentoPecas = condicaoPagamentoPecas;
	}
	public String getCondicaoPagamentoServicos() {
		return condicaoPagamentoServicos;
	}
	public void setCondicaoPagamentoServicos(String condicaoPagamentoServicos) {
		this.condicaoPagamentoServicos = condicaoPagamentoServicos;
	}
	public String getDescPorcPecas() {
		return descPorcPecas;
	}
	public void setDescPorcPecas(String descPorcPecas) {
		this.descPorcPecas = descPorcPecas;
	}
	public String getDescPorcServicos() {
		return descPorcServicos;
	}
	public void setDescPorcServicos(String descPorcServicos) {
		this.descPorcServicos = descPorcServicos;
	}
	public String getDescValorPecas() {
		return descValorPecas;
	}
	public void setDescValorPecas(String descValorPecas) {
		this.descValorPecas = descValorPecas;
	}
	public String getDescValorServicos() {
		return descValorServicos;
	}
	public void setDescValorServicos(String descValorServicos) {
		this.descValorServicos = descValorServicos;
	}
	public String getObsPeca() {
		return obsPeca;
	}
	public void setObsPeca(String obsPeca) {
		this.obsPeca = obsPeca;
	}
	public String getObsServico() {
		return obsServico;
	}
	public void setObsServico(String obsServico) {
		this.obsServico = obsServico;
	}
	public String getCondEspecial() {
		return condEspecial;
	}
	public void setCondEspecial(String condEspecial) {
		this.condEspecial = condEspecial;
	}
	public String getAutomaticaFaturada() {
		return automaticaFaturada;
	}
	public void setAutomaticaFaturada(String automaticaFaturada) {
		this.automaticaFaturada = automaticaFaturada;
	}
	public Long getCredorPeca() {
		return credorPeca;
	}
	public void setCredorPeca(Long credorPeca) {
		this.credorPeca = credorPeca;
	}
	public Long getCredorServico() {
		return credorServico;
	}
	public void setCredorServico(Long credorServico) {
		this.credorServico = credorServico;
	}
	public String getJobCode() {
		return jobCode;
	}
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	public String getCompCode() {
		return compCode;
	}
	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}
	public String getHorimetroUltimaRevisao() {
		return horimetroUltimaRevisao;
	}
	public void setHorimetroUltimaRevisao(String horimetroUltimaRevisao) {
		this.horimetroUltimaRevisao = horimetroUltimaRevisao;
	}
	public String getHorimetroProximaRevisao() {
		return horimetroProximaRevisao;
	}
	public void setHorimetroProximaRevisao(String horimetroProximaRevisao) {
		this.horimetroProximaRevisao = horimetroProximaRevisao;
	}
	public String getHorimetroFaltantes() {
		return horimetroFaltantes;
	}
	public void setHorimetroFaltantes(String horimetroFaltantes) {
		this.horimetroFaltantes = horimetroFaltantes;
	}
	public int compare(Object o1, Object o2) {
		if(((AgendamentoBean)o1).getIsOrderOs() == null){
			return ((AgendamentoBean)o1).getHorasPendentes().compareTo(((AgendamentoBean)o2).getHorasPendentes());
		}else {
			return ((AgendamentoBean)o1).getNumOs().compareTo(((AgendamentoBean)o2).getNumOs());
		}
	}

}
