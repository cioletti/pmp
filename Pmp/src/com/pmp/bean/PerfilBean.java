package com.pmp.bean;

import java.io.Serializable;

public class PerfilBean implements Serializable{

	private static final long serialVersionUID = 2718067050337989178L;
	private Integer id;
	private String descricao;
	private String sigla;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
}
