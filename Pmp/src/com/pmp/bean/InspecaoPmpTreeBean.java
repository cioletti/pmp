package com.pmp.bean;

import java.util.List;

import com.pmp.entity.ArvInspecao;

public class InspecaoPmpTreeBean {

	private Long idOsPalmDt;
	private String status;
	private String statusUrlImage;
	private String descricao;
	private String obs;
	private String tipoManutencao;
	private Long idIrdav;
	private String grupo;
	private String simNao;
	private String smcs;
	private String fotoUrlImage;
	private List<InspecaoPmpTreeBean> children;
	
	public Long getIdOsPalmD() {
		return idOsPalmDt;
	}
	public void setIdOsPalmDt(Long idOsPalmDt) {
		this.idOsPalmDt = idOsPalmDt;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public List<InspecaoPmpTreeBean> getChildren() {
		return children;
	}
	public void setChildren(List<InspecaoPmpTreeBean> children) {
		this.children = children;
	}
	public String getTipoManutencao() {
		return tipoManutencao;
	}
	public void setTipoManutencao(String tipoManutencao) {
		this.tipoManutencao = tipoManutencao;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	public Long getIdIrdav() {
		return idIrdav;
	}
	public void setIdIrdav(Long idIrdav) {
		this.idIrdav = idIrdav;
	}
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	public Long getIdOsPalmDt() {
		return idOsPalmDt;
	}
	public String getSimNao() {
		return simNao;
	}
	public void setSimNao(String simNao) {
		this.simNao = simNao;
	}
	public String getSmcs() {
		return smcs;
	}
	public void setSmcs(String smcs) {
		this.smcs = smcs;
	}
	public String getStatusUrlImage() {
		return statusUrlImage;
	}
	public void setStatusUrlImage(String statusUrlImage) {
		this.statusUrlImage = statusUrlImage;
	}
	public String getFotoUrlImage() {
		return fotoUrlImage;
	}
	public void setFotoUrlImage(String fotoUrlImage) {
		this.fotoUrlImage = fotoUrlImage;
	}
}
