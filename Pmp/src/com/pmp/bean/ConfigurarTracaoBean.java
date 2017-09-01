package com.pmp.bean;

import java.util.ArrayList;
import java.util.List;

import com.pmp.entity.PmpTipoTracao;



public class ConfigurarTracaoBean {

	private Long id;
	private Long idModelo;
	private Long idFamilia;
	private Long idTracao;
	private String possuiArCondicionado;
	private String descricaoAC;
	private String descricao;
	private List<String> siglaAcList;
	private List<String> siglaTracaoList;
	
	public ConfigurarTracaoBean() {
		siglaAcList = new ArrayList<String>();
		siglaTracaoList = new ArrayList<String>();
	}
	
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

	public Long getIdModelo() {
		return idModelo;
	}
	public void setIdModelo(Long idModelo) {
		this.idModelo = idModelo;
	}
	public Long getIdTracao() {
		return idTracao;
	}
	public void setIdTracao(Long idTracao) {
		this.idTracao = idTracao;
	}
	public String getPossuiArCondicionado() {
		return possuiArCondicionado;
	}
	public void setPossuiArCondicionado(String possuiArCondicionado) {
		this.possuiArCondicionado = possuiArCondicionado;
	}
	public String getDescricaoAC() {
		return descricaoAC;
	}
	public void setDescricaoAC(String descricaoAC) {
		this.descricaoAC = descricaoAC;
	}
	public List<String> getSiglaAcList() {
		return siglaAcList;
	}
	public void setSiglaAcList(List<String> siglaAcList) {
		this.siglaAcList = siglaAcList;
	}
	public List<String> getSiglaTracaoList() {
		return siglaTracaoList;
	}
	public void setSiglaTracaoList(List<String> siglaTracaoList) {
		this.siglaTracaoList = siglaTracaoList;
	}
	public Long getIdFamilia() {
		return idFamilia;
	}
	public void setIdFamilia(Long idFamilia) {
		this.idFamilia = idFamilia;
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
