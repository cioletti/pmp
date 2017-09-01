package com.pmp.bean;

import java.util.List;

import com.pmp.entity.PmpConfigManutencao;

public class ConfigManutencaoBean {

	private Integer id;
	private String modelo;
	private String prefixo;
	private String businessGroup;
	private String beginRanger;
	private String endRanger;
	private String rangerDescription;
	private List<StandardJobBean> standardJobList;
	private List<RangerBean> rangerList;
	private List<BusinessGroupBean> businessGroupList;
	private List<PrefixoBean> prefixoList;
	private List<ModeloBean> modeloList;
	private String contExcessao;
	private String tipoPreco;
	private String descTipoPreco;
	private String descPreco;
	private Long idPrecoConfig;
	private Long idFamilia;
	private Long idTipoTracao;
	private String possuiArCondicionado;
	
	public String getBeginRanger() {
		return beginRanger;
	}

	public void setBeginRanger(String beginRanger) {
		this.beginRanger = beginRanger;
	}

	public String getEndRanger() {
		return endRanger;
	}

	public void setEndRanger(String endRanger) {
		this.endRanger = endRanger;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getPrefixo() {
		return prefixo;
	}

	public void setPrefixo(String prefixo) {
		this.prefixo = prefixo;
	}

	public String getBusinessGroup() {
		return businessGroup;
	}

	public void setBusinessGroup(String businessGroup) {
		this.businessGroup = businessGroup;
	}

	public List<StandardJobBean> getStandardJobList() {
		return standardJobList;
	}

	public void setStandardJobList(List<StandardJobBean> standardJobList) {
		this.standardJobList = standardJobList;
	}

	public String getContExcessao() {
		return contExcessao;
	}

	public void setContExcessao(String contExcessao) {
		this.contExcessao = contExcessao;
	}

	public String getTipoPreco() {
		return tipoPreco;
	}

	public void setTipoPreco(String tipoPreco) {
		this.tipoPreco = tipoPreco;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<RangerBean> getRangerList() {
		return rangerList;
	}

	public void setRangerList(List<RangerBean> rangerList) {
		this.rangerList = rangerList;
	}

	public List<BusinessGroupBean> getBusinessGroupList() {
		return businessGroupList;
	}

	public void setBusinessGroupList(List<BusinessGroupBean> businessGroupList) {
		this.businessGroupList = businessGroupList;
	}

	public List<PrefixoBean> getPrefixoList() {
		return prefixoList;
	}

	public void setPrefixoList(List<PrefixoBean> prefixoList) {
		this.prefixoList = prefixoList;
	}

	public String getRangerDescription() {
		return rangerDescription;
	}

	public void setRangerDescription(String rangerDescription) {
		this.rangerDescription = rangerDescription;
	}

	public List<ModeloBean> getModeloList() {
		return modeloList;
	}

	public void setModeloList(List<ModeloBean> modeloList) {
		this.modeloList = modeloList;
	}

	public Long getIdPrecoConfig() {
		return idPrecoConfig;
	}

	public void setIdPrecoConfig(Long idPrecoConfig) {
		this.idPrecoConfig = idPrecoConfig;
	}

	public Long getIdFamilia() {
		return idFamilia;
	}

	public void setIdFamilia(Long idFamilia) {
		this.idFamilia = idFamilia;
	}

	public Long getIdTipoTracao() {
		return idTipoTracao;
	}

	public void setIdTipoTracao(Long idTipoTracao) {
		this.idTipoTracao = idTipoTracao;
	}

	public String getPossuiArCondicionado() {
		return possuiArCondicionado;
	}

	public void setPossuiArCondicionado(String possuiArCondicionado) {
		this.possuiArCondicionado = possuiArCondicionado;
	}

	public String getDescTipoPreco() {
		return descTipoPreco;
	}

	public void setDescTipoPreco(String descTipoPreco) {
		this.descTipoPreco = descTipoPreco;
	}

	public String getDescPreco() {
		return descPreco;
	}

	public void setDescPreco(String descPreco) {
		this.descPreco = descPreco;
	}

	public PmpConfigManutencao toBean(ConfigManutencaoBean bean) {
		PmpConfigManutencao manut = new PmpConfigManutencao();
		manut.setModelo(bean.getModelo());
		manut.setPrefixo(bean.getPrefixo());
		manut.setBgrp(bean.getBusinessGroup());
		manut.setBeginrange(bean.getBeginRanger());
		manut.setEndrange(bean.getEndRanger());
		manut.setContExcessao(bean.getContExcessao());
		manut.setTipoPreco(bean.getTipoPreco());
		
		return manut;
	}

	public void toBean(ConfigManutencaoBean bean, PmpConfigManutencao manut) {
		manut.setModelo(bean.getModelo());
		manut.setPrefixo(bean.getPrefixo());
		manut.setBgrp(bean.getBusinessGroup());
		manut.setBeginrange(bean.getBeginRanger());
		manut.setEndrange(bean.getEndRanger());
		manut.setContExcessao(bean.getContExcessao());
		manut.setTipoPreco(bean.getTipoPreco());

	}

	public void fromBean(PmpConfigManutencao manut) {
		setId(manut.getId().intValue());
		setModelo(manut.getModelo());
		setPrefixo(manut.getPrefixo());
		setBusinessGroup(manut.getBgrp());
		setBeginRanger(manut.getBeginrange());
		setEndRanger(manut.getEndrange());
		setContExcessao(manut.getContExcessao());
		setRangerDescription(manut.getBeginrange() + " - "
				+ manut.getEndrange());

	}
}
