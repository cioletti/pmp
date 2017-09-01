package com.pmp.bean;

public class IndicadorGarantiaBean {

	private Long id;
	private String tipo;
	private String ind;
	private String descricao;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getInd() {
		return ind;
	}
	public void setInd(String ind) {
		this.ind = ind;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
