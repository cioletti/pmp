package com.pmp.bean;

import java.util.Date;

import com.pmp.entity.PmpFinanceiro;
import com.pmp.util.ValorMonetarioHelper;

public class FinanceiroBean {
	private Long id;
	private String cliente; 
	private String codCliente; 
	private String creditoCliente; 
	private String idFuncionario; 
	private String status; 
	private Long idOsOperacional;
	private String numeroOs;
	private String observacao;
	private String observacaoLiberacao;
	private String dataLiberacao;
	private String dataRejeicao;
	private String valorLiberado;
	private String funcionarioLiberacao;
	private String idFuncionarioCriador;
	private String data;
	private String nomeFilial;
	private String nomeFuncionario;
	private String vlrEstimado;
	private Long idContrato;
	private Long idContHorasStandard;
	private String nomeFuncionarioResponsavel;
	private Long idStatusFinanceiro;
	private String valorManutencaoStd;
	
	private String depositoCliente;
	
	private String siglaCondicaoPagamento;
	private String crdDbs;
	private String siglaMotivoRequisicao;
	
	public Long getIdOsOperacional() {
		return idOsOperacional;
	}
	public void setIdOsOperacional(Long idOsOperacional) {
		this.idOsOperacional = idOsOperacional;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getCodCliente() {
		return codCliente;
	}
	public void setCodCliente(String codCliente) {
		this.codCliente = codCliente;
	}
	public String getCreditoCliente() {
		return creditoCliente;
	}
	public void setCreditoCliente(String creditoCliente) {
		this.creditoCliente = creditoCliente;
	}
	public String getIdFuncionario() {
		return idFuncionario;
	}
	public void setIdFuncionario(String idFuncionario) {
		this.idFuncionario = idFuncionario;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNumeroOs() {
		return numeroOs;
	}
	public void setNumeroOs(String numeroOs) {
		this.numeroOs = numeroOs;
	} 
	
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public String getObservacaoLiberacao() {
		return observacaoLiberacao;
	}
	public void setObservacaoLiberacao(String observacaoLiberacao) {
		this.observacaoLiberacao = observacaoLiberacao;
	}
	public String getDataLiberacao() {
		return dataLiberacao;
	}
	public void setDataLiberacao(String dataLiberacao) {
		this.dataLiberacao = dataLiberacao;
	}
	public String getValorLiberado() {
		return valorLiberado;
	}
	public void setValorLiberado(String valorLiberado) {
		this.valorLiberado = valorLiberado;
	}
	public String getFuncionarioLiberacao() {
		return funcionarioLiberacao;
	}
	public void setFuncionarioLiberacao(String funcionarioLiberacao) {
		this.funcionarioLiberacao = funcionarioLiberacao;
	}
	public String getDataRejeicao() {
		return dataRejeicao;
	}
	public void setDataRejeicao(String dataRejeicao) {
		this.dataRejeicao = dataRejeicao;
	}
	public String getIdFuncionarioCriador() {
		return idFuncionarioCriador;
	}
	public void setIdFuncionarioCriador(String idFuncionarioCriador) {
		this.idFuncionarioCriador = idFuncionarioCriador;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getNomeFilial() {
		return nomeFilial;
	}
	public void setNomeFilial(String nomeFilial) {
		this.nomeFilial = nomeFilial;
	}
	public String getNomeFuncionario() {
		return nomeFuncionario;
	}
	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}
	public String getVlrEstimado() {
		return vlrEstimado;
	}
	public void setVlrEstimado(String vlrEstimado) {
		this.vlrEstimado = vlrEstimado;
	}
	public Long getIdContrato() {
		return idContrato;
	}
	public void setIdContrato(Long idContrato) {
		this.idContrato = idContrato;
	}
	public Long getIdContHorasStandard() {
		return idContHorasStandard;
	}
	public void setIdContHorasStandard(Long idContHorasStandard) {
		this.idContHorasStandard = idContHorasStandard;
	}
	public String getNomeFuncionarioResponsavel() {
		return nomeFuncionarioResponsavel;
	}
	public void setNomeFuncionarioResponsavel(String nomeFuncionarioResponsavel) {
		this.nomeFuncionarioResponsavel = nomeFuncionarioResponsavel;
	}
	public Long getIdStatusFinanceiro() {
		return idStatusFinanceiro;
	}
	public void setIdStatusFinanceiro(Long idStatusFinanceiro) {
		this.idStatusFinanceiro = idStatusFinanceiro;
	}
	public void fromBean(PmpFinanceiro entity) {
		setId(entity.getId());
		setCliente(entity.getCliente());
		setCodCliente(entity.getCodCliente());
		setCreditoCliente(ValorMonetarioHelper.formata("###,##0.00", entity.getCreditoLiberado().doubleValue()));
		setIdFuncionario(entity.getIdFuncionario());
		setStatus(getStatusStr(entity.getStatus()));
		//setIdOsOperacional(entity.getIdOsOperacional().getId());
		setNumeroOs(entity.getNumeroOs());
		setObservacao(entity.getObservacao());
	}

	private String getStatusStr(String status){
		if(status.equals("L")){
			return "Liberado";
		}else if(status.equals("A")){
			return "Ag. Liberação";
		}else{
			return "Rejeitado";
		}
	}
	public String getValorManutencaoStd() {
		return valorManutencaoStd;
	}
	public void setValorManutencaoStd(String valorManutencaoStd) {
		this.valorManutencaoStd = valorManutencaoStd;
	}
	public String getDepositoCliente() {
		return depositoCliente;
	}
	public void setDepositoCliente(String depositoCliente) {
		this.depositoCliente = depositoCliente;
	}
	public String getSiglaCondicaoPagamento() {
		return siglaCondicaoPagamento;
	}
	public void setSiglaCondicaoPagamento(String siglaCondicaoPagamento) {
		this.siglaCondicaoPagamento = siglaCondicaoPagamento;
	}
	public String getCrdDbs() {
		return crdDbs;
	}
	public void setCrdDbs(String crdDbs) {
		this.crdDbs = crdDbs;
	}
	public String getSiglaMotivoRequisicao() {
		return siglaMotivoRequisicao;
	}
	public void setSiglaMotivoRequisicao(String siglaMotivoRequisicao) {
		this.siglaMotivoRequisicao = siglaMotivoRequisicao;
	}
	public void toBean(PmpFinanceiro entity) {
		//ScFinanceiro entity = new ScFinanceiro();
		//entity.setId(getId());
		entity.setCliente(getCliente());
		entity.setCodCliente(getCodCliente());
		//entity.setCreditoLiberado( BigDecimal.valueOf(Long.valueOf(getCreditoCliente().replace(".", "").replace(",", "."))));
		//entity.setIdFuncionario(getIdFuncionario());
		entity.setStatus("A");
		//entity.setIdChamado(getIdChamado());
		entity.setNumeroOs(getNumeroOs());
		entity.setObservacao(getObservacao());
		entity.setData(new Date());
		//return entity;
	}
}
