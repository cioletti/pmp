package com.pmp.bean;

public class ConfigManutencaoHorasBean implements Comparable{

	private Long id;
	private Long idConfigManutencao;
	private Long horas;
	private Long horasManutencao;
	private String standardJob;
	private Boolean isSelected;
	private Long frequencia;
	private String isExecutado;
	private Long horasRevisao;
	private String tipoInspecao;
	private String numContrato;
	private String isTa;
	private String isPartner;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getHoras() {
		return horas;
	}
	public void setHoras(Long horas) {
		this.horas = horas;
	}
	public String getStandardJob() {
		return standardJob;
	}
	public void setStandardJob(String standardJob) {
		this.standardJob = standardJob;
	}
	public Long getIdConfigManutencao() {
		return idConfigManutencao;
	}
	public void setIdConfigManutencao(Long idConfigManutencao) {
		this.idConfigManutencao = idConfigManutencao;
	}
	public Boolean getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
	public Long getFrequencia() {
		return frequencia;
	}
	public void setFrequencia(Long frequencia) {
		this.frequencia = frequencia;
	}
	public String getIsExecutado() {
		return isExecutado;
	}
	public void setIsExecutado(String isExecutado) {
		this.isExecutado = isExecutado;
	}
	public Long getHorasManutencao() {
		return horasManutencao;
	}
	public void setHorasManutencao(Long horasManutencao) {
		this.horasManutencao = horasManutencao;
	}
	public Long getHorasRevisao() {
		return horasRevisao;
	}
	public void setHorasRevisao(Long horasRevisao) {
		this.horasRevisao = horasRevisao;
	}
	public String getTipoInspecao() {
		return tipoInspecao;
	}
	public void setTipoInspecao(String tipoInspecao) {
		this.tipoInspecao = tipoInspecao;
	}
	public String getNumContrato() {
		return numContrato;
	}
	public void setNumContrato(String numContrato) {
		this.numContrato = numContrato;
	}
	public String getIsTa() {
		return isTa;
	}
	public void setIsTa(String isTa) {
		this.isTa = isTa;
	}
	public String getIsPartner() {
		return isPartner;
	}
	public void setIsPartner(String isPartner) {
		this.isPartner = isPartner;
	}
	public int compareTo(Object o) {
		return this.getHorasManutencao().compareTo(((ConfigManutencaoHorasBean)o).getHorasManutencao());
	}
	
}
