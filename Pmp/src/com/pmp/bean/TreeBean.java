package com.pmp.bean;

import java.util.List;

import com.pmp.entity.ArvInspecao;

public class TreeBean {

	private Long idArv;
	private String descricao;
	private String tipoManutencao;
	private String sos;
	private Long idPai;
	private Long idPaiRoot;
	private List<TreeBean> children;
	private String smcs;
	
	public Long getIdArv() {
		return idArv;
	}
	public void setIdArv(Long idArv) {
		this.idArv = idArv;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Long getIdPai() {
		return idPai;
	}
	public void setIdPai(Long idPai) {
		this.idPai = idPai;
	}
	public Long getIdPaiRoot() {
		return idPaiRoot;
	}
	public void setIdPaiRoot(Long idPaiRoot) {
		this.idPaiRoot = idPaiRoot;
	}

	public List<TreeBean> getChildren() {
		return children;
	}
	public void setChildren(List<TreeBean> children) {
		this.children = children;
	}
	public String getTipoManutencao() {
		return tipoManutencao;
	}
	public void setTipoManutencao(String tipoManutencao) {
		this.tipoManutencao = tipoManutencao;
	}
	public String getSos() {
		return sos;
	}
	public void setSos(String sos) {
		this.sos = sos;
	}
	public String getSmcs() {
		return smcs;
	}
	public void setSmcs(String smcs) {
		this.smcs = smcs;
	}
	public void fromBean(ArvInspecao bean){
		setIdArv(bean.getIdArv());
		setDescricao(bean.getDescricao());
		setTipoManutencao(bean.getTipoManutencao());
		setSos(bean.getSos());
		setIdPai((bean.getIdPai() != null)?bean.getIdPai().getIdArv():null);
		setIdPaiRoot(bean.getIdPaiRoot());
		setSmcs(bean.getSmcs());
	}
}
