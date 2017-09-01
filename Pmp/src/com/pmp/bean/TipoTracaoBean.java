package com.pmp.bean;

import com.pmp.entity.PmpTipoTracao;

public class TipoTracaoBean {

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
	public void fromBean(PmpTipoTracao tipoTracao){
		setId(tipoTracao.getId().longValue());
		setDescricao(tipoTracao.getDescricao());
		setIdModelo(tipoTracao.getIdModelo());
	}
	
	public void toBean(PmpTipoTracao entity){
		entity.setDescricao(getDescricao());
		//entity.setSigla(getSigla());
	}
}
