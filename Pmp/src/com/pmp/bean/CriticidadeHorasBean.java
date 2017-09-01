package com.pmp.bean;

public class CriticidadeHorasBean {
	
	private Integer horas;
	private String LABOTISUPERIOR;
	private String LABOTIINFERIOR;
	public Integer getHoras() {
		return horas;
	}
	public void setHoras(Integer horas) {
		this.horas = horas;
	}
	public String getLABOTISUPERIOR() {
		return LABOTISUPERIOR;
	}
	public void setLABOTISUPERIOR(String lABOTISUPERIOR) {
		LABOTISUPERIOR = lABOTISUPERIOR;
	}
	public String getLABOTIINFERIOR() {
		return LABOTIINFERIOR;
	}
	public void setLABOTIINFERIOR(String lABOTIINFERIOR) {
		LABOTIINFERIOR = lABOTIINFERIOR;
	}
	public Boolean getIsDivisorHoraExtra() {
		return isDivisorHoraExtra;
	}
	public void setIsDivisorHoraExtra(Boolean isDivisorHoraExtra) {
		this.isDivisorHoraExtra = isDivisorHoraExtra;
	}
	private Boolean isDivisorHoraExtra;

}
