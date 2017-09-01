package com.pmp.bean;

public class ConfigManutencaoMesesBean implements Comparable{

	private Long id;
	private Long idConfigManutencao;
	private Long meses;
	private Long mesManutencao;
	private String standardJob;
	private Boolean isSelected;
	private Long frequencia;
	private String isExecutado;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMeses() {
		return meses;
	}
	public void setMeses(Long meses) {
		this.meses = meses;
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
	public Long getMesManutencao() {
		return mesManutencao;
	}
	public void setMesManutencao(Long mesManutencao) {
		this.mesManutencao = mesManutencao;
	}
	public int compareTo(Object o) {
		return this.getMesManutencao().compareTo(((ConfigManutencaoMesesBean)o).getMesManutencao());
	}
	
}
