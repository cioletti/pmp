package com.pmp.bean;

import java.io.Serializable;

public class IntervencaoBean implements Serializable {

	private static final long serialVersionUID = 158664875654687805L;
	private Integer horimetro;
	private Integer intervancaoRealizada;
	private String custo;
	private String isTa;
	public Integer getHorimetro() {
		return horimetro;
	}
	public void setHorimetro(Integer horimetro) {
		this.horimetro = horimetro;
	}
	public Integer getIntervancaoRealizada() {
		return intervancaoRealizada;
	}
	public void setIntervancaoRealizada(Integer intervancaoRealizada) {
		this.intervancaoRealizada = intervancaoRealizada;
	}
	public String getCusto() {
		return custo;
	}
	public void setCusto(String custo) {
		this.custo = custo;
	}
	public String getIsTa() {
		return isTa;
	}
	public void setIsTa(String isTa) {
		this.isTa = isTa;
	}
}
