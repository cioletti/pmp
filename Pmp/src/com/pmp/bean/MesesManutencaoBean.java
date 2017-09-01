package com.pmp.bean;

public class MesesManutencaoBean {
	
	private Long id;
	private Long idFamilia;
	private String familiaStr;
	private Long idModelo;
	private String modeloStr;
	private Long mesesManutencao;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdFamilia() {
		return idFamilia;
	}
	public void setIdFamilia(Long idFamilia) {
		this.idFamilia = idFamilia;
	}
	public Long getIdModelo() {
		return idModelo;
	}
	public void setIdModelo(Long idModelo) {
		this.idModelo = idModelo;
	}
	public Long getMesesManutencao() {
		return mesesManutencao;
	}
	public void setMesesManutencao(Long mesesManutencao) {
		this.mesesManutencao = mesesManutencao;
	}
	public String getFamiliaStr() {
		return familiaStr;
	}
	public void setFamiliaStr(String familiaStr) {
		this.familiaStr = familiaStr;
	}
	public String getModeloStr() {
		return modeloStr;
	}
	public void setModeloStr(String modeloStr) {
		this.modeloStr = modeloStr;
	}

}
