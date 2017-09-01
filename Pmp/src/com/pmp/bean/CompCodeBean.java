package com.pmp.bean;

import com.pmp.entity.PmpCentroDeCusto;
import com.pmp.entity.PmpCompCodePartner;

public class CompCodeBean {
    private String descricao;
    private Long id;
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public void toBean(PmpCompCodePartner entity){
		entity.setDescricao(getDescricao());
	}
	public static void main(String[] args) {
	
	}

}
