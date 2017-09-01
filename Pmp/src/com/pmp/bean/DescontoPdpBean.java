package com.pmp.bean;

import com.pmp.entity.PmpDescontoPdp;

public class DescontoPdpBean {

	private Long id;
	private String descricao;
	
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

	public void formBean(PmpDescontoPdp descontoPdp){
		setId(descontoPdp.getId().longValue());
		setDescricao(descontoPdp.getDescricao());
	}
	
	public void toBean(PmpDescontoPdp entity){
		entity.setDescricao(getDescricao());
	}
}
