package com.pmp.bean;

import java.io.Serializable;
import java.math.BigInteger;

import com.pmp.entity.PmpRegraDeNegocio;

public class RegraDeNegocioBean implements Serializable {

	private static final long serialVersionUID = 8515908170819560165L;

	private Long id;
	private String descricao;
	private String filial;

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

	public String getFilial() {
		return filial;
	}

	public void setFilial(String filial) {
		this.filial = filial;
	}
	
	public void formBean(PmpRegraDeNegocio entity){
		setId(entity.getId());
		setDescricao(entity.getDescricao());
		setFilial(entity.getFilial().toString());
	}
	
	public void toBean(PmpRegraDeNegocio  entity){
		entity.setDescricao(getDescricao());
		entity.setFilial(Integer.valueOf(getFilial()));
	}
	

}
