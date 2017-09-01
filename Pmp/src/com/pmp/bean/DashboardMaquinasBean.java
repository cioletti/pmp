package com.pmp.bean;

public class DashboardMaquinasBean {
	private String operacao;
	private Integer totalRevisaoVencidas;
	private Integer totalMenor100;
	private Integer totalContratos;
	private Integer totalHorimetroZerado;
	
	public String getOperacao() {
		return operacao;
	}
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	public Integer getTotalRevisaoVencidas() {
		return totalRevisaoVencidas;
	}
	public void setTotalRevisaoVencidas(Integer totalRevisaoVencidas) {
		this.totalRevisaoVencidas = totalRevisaoVencidas;
	}
	public Integer getTotalMenor100() {
		return totalMenor100;
	}
	public void setTotalMenor100(Integer totalMenor100) {
		this.totalMenor100 = totalMenor100;
	}
	public Integer getTotalContratos() {
		return totalContratos;
	}
	public void setTotalContratos(Integer totalContratos) {
		this.totalContratos = totalContratos;
	}
	public Integer getTotalHorimetroZerado() {
		return totalHorimetroZerado;
	}
	public void setTotalHorimetroZerado(Integer totalHorimetroZerado) {
		this.totalHorimetroZerado = totalHorimetroZerado;
	}
	public DashboardMaquinasBean() {
		super();
		this.operacao = "";
		this.totalRevisaoVencidas = 0;
		this.totalMenor100 = 0;
		this.totalContratos = 0;
		this.totalHorimetroZerado = 0;
	}
	
	
	
	
	
}
