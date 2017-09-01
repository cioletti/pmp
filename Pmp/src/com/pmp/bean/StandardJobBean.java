package com.pmp.bean;

public class StandardJobBean implements Comparable{

	
	private Long id;
	private Long horas;
	private String standardJob;
	private Long sequencia;
	private Long jobCode;
	private Long horasRevisao;
	
	
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
	public Long getSequencia() {
		return sequencia;
	}
	public void setSequencia(Long sequencia) {
		this.sequencia = sequencia;
	}
	public Long getJobCode() {
		return jobCode;
	}
	public void setJobCode(Long jobCode) {
		this.jobCode = jobCode;
	}

	public String getStandardJob() {
		return standardJob;
	}
	public void setStandardJob(String standardJob) {
		this.standardJob = standardJob;
	}
	public Long getHorasRevisao() {
		return horasRevisao;
	}
	public void setHorasRevisao(Long horasRevisao) {
		this.horasRevisao = horasRevisao;
	}
	public int compareTo(Object o) {
		return this.horas.compareTo(((StandardJobBean)o).horas); 
	}


}
