package com.pmp.bean;

import java.util.List;

public class ValorManutencaoBean {
	
	private List<ConfigManutencaoHorasBean> configManutencaoHorasBeanList;
	private String beginRanger;
	private String endRanger;
	private String prefixo;
	private String cptcd;
	private String bgrp;
	private String tipoPreco;
	private String modelo;
	
	
	public List<ConfigManutencaoHorasBean> getConfigManutencaoHorasBeanList() {
		return configManutencaoHorasBeanList;
	}
	public void setConfigManutencaoHorasBeanList(
			List<ConfigManutencaoHorasBean> configManutencaoHorasBeanList) {
		this.configManutencaoHorasBeanList = configManutencaoHorasBeanList;
	}
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
	public String getPrefixo() {
		return prefixo;
	}
	public void setPrefixo(String prefixo) {
		this.prefixo = prefixo;
	}
	public String getCptcd() {
		return cptcd;
	}
	public void setCptcd(String cptcd) {
		this.cptcd = cptcd;
	}
	public String getBgrp() {
		return bgrp;
	}
	public void setBgrp(String bgrp) {
		this.bgrp = bgrp;
	}
	public String getTipoPreco() {
		return tipoPreco;
	}
	public void setTipoPreco(String tipoPreco) {
		this.tipoPreco = tipoPreco;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	

}
