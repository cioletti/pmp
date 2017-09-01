package com.pmp.bean;

public class PecasDbsBean {
	private String codigo;
	private Integer qtd;
	private String numPeca;
	private String nomePeca;
	
	public PecasDbsBean() {
		
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Integer getQtd() {
		return qtd;
	}
	public void setQtd(Integer qtd) {
		this.qtd = qtd;
	}
	public String getNumPeca() {
		return numPeca;
	}
	public void setNumPeca(String numPeca) {
		this.numPeca = numPeca;
	}
	public String getNomePeca() {
		return nomePeca;
	}
	public void setNomePeca(String nomePeca) {
		this.nomePeca = nomePeca;
	}
}
