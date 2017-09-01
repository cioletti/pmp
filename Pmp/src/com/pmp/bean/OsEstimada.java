package com.pmp.bean;

public class OsEstimada {

	private String msg;
	private String filial;
	private String jobControl;
	private String codigoCliente;
	private String estimateBy;
	private String make;
	private String numeroSerie;
	private String segmento;
	private String jobCode;
	private String componenteCode;
	private String numContrato;
	private ValidarCentroDeCustoContaContabilBean vcc;
	private AgendamentoBean agendamentoBean;
	private String endereco;
	private Long horimetro;
	

	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getFilial() {
		return filial;
	}
	public void setFilial(String filial) {
		this.filial = filial;
	}
	public String getJobControl() {
		return jobControl;
	}
	public void setJobControl(String jobControl) {
		this.jobControl = jobControl;
	}
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getNumeroSerie() {
		return numeroSerie;
	}
	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}
	public String getSegmento() {
		return segmento;
	}
	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}
	public String getJobCode() {
		return jobCode;
	}
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	public String getComponenteCode() {
		return componenteCode;
	}
	public void setComponenteCode(String componenteCode) {
		this.componenteCode = componenteCode;
	}
	public ValidarCentroDeCustoContaContabilBean getVcc() {
		return vcc;
	}
	public void setVcc(ValidarCentroDeCustoContaContabilBean vcc) {
		this.vcc = vcc;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getEstimateBy() {
		return estimateBy;
	}
	public void setEstimateBy(String estimateBy) {
		this.estimateBy = estimateBy;
	}
	public AgendamentoBean getAgendamentoBean() {
		return agendamentoBean;
	}
	public void setAgendamentoBean(AgendamentoBean agendamentoBean) {
		this.agendamentoBean = agendamentoBean;
	}
	public String getNumContrato() {
		return numContrato;
	}
	public void setNumContrato(String numContrato) {
		this.numContrato = numContrato;
	}
	public Long getHorimetro() {
		return horimetro;
	}
	public void setHorimetro(Long horimetro) {
		this.horimetro = horimetro;
	}
}
