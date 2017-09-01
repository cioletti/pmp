package com.pmp.bean;

public class TipoOleoBean {
	private Long id;
	private String fabricante;
	private String viscosidade;
	private String nomeComercial;
	public String getFabricante() {
		return fabricante;
	}
	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}
	public String getViscosidade() {
		return viscosidade;
	}
	public void setViscosidade(String viscosidade) {
		this.viscosidade = viscosidade;
	}
	public String getNomeComercial() {
		return nomeComercial;
	}
	public void setNomeComercial(String nomeComercial) {
		this.nomeComercial = nomeComercial;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	
}
