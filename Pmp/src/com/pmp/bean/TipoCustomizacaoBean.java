package com.pmp.bean;

import com.pmp.entity.PmpTipoCustomizacao;

public class TipoCustomizacaoBean {

	private Long id;
	private String descricao;
	private String sigla;
	private long idModelo;
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

	public long getIdModelo() {
		return idModelo;
	}
	public void setIdModelo(long idModelo) {
		this.idModelo = idModelo;
	}
	public void fromBean(PmpTipoCustomizacao tipoCustomizacao){
		setId(tipoCustomizacao.getId().longValue());
		setDescricao(tipoCustomizacao.getDescricao());
		setIdModelo(tipoCustomizacao.getIdModelo().getIdArv().longValue());
	}
	
	public void toBean(PmpTipoCustomizacao entity){
		entity.setDescricao(getDescricao());
		//entity.setSigla(getSigla());
	}
}
