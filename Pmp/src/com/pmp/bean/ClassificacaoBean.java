package com.pmp.bean;

import com.pmp.entity.PmpCentroDeCusto;
import com.pmp.entity.PmpClassificacaoContrato;

public class ClassificacaoBean {
	private Long id;
	private String descricao;
	private String sigla;
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
	public void formBean(PmpClassificacaoContrato bean){
		setId(bean.getId().longValue());
		setDescricao(bean.getDescricao());
		setSigla(bean.getSigla());
	}

}
