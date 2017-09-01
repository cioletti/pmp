package com.pmp.bean;

import com.pmp.entity.PmpCentroDeCusto;

public class CentroDeCustoBean {

	private Long id;
	private String descricao;
	private String sigla;
	private String siglaDescricao;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public String getSiglaDescricao() {
		return siglaDescricao;
	}
	public void setSiglaDescricao(String siglaDescricao) {
		this.siglaDescricao = siglaDescricao;
	}
	public void formBean(PmpCentroDeCusto centroDeCusto){
		setId(centroDeCusto.getId().longValue());
		setDescricao(centroDeCusto.getDescricao());
		setSigla(centroDeCusto.getSigla());
		setSiglaDescricao(centroDeCusto.getSigla()+" "+centroDeCusto.getDescricao());
	}
	
	public void toBean(PmpCentroDeCusto entity){
		entity.setDescricao(getDescricao());
		entity.setSigla(getSigla());
	}
}
