package com.pmp.bean;

import java.util.List;

public class TipoFrequenciaBean {

	private Long id;
	private String descricao;
	private List<StandardJobBean> frequenciaList;
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
	public List<StandardJobBean> getFrequenciaList() {
		return frequenciaList;
	}
	public void setFrequenciaList(List<StandardJobBean> frequenciaList) {
		this.frequenciaList = frequenciaList;
	}
}
