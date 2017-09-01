package com.pmp.bean;

import java.io.Serializable;

import com.pmp.entity.PmpContaContabil;

public class ContaContabilBean implements Serializable {

	private static final long serialVersionUID = 8515908170819560165L;

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

	public void formBean(PmpContaContabil entity){
		setId(entity.getId());
		setDescricao(entity.getDescricao());
		setSigla(entity.getSigla());
		setSiglaDescricao(entity.getSigla()+" "+entity.getDescricao());
	}
	
	public void toBean(PmpContaContabil  entity){
		entity.setDescricao(getDescricao());
		entity.setSigla(getSigla());
	}

}
