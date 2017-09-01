package com.pmp.bean;

public class CriticidadeManutencaoBean {
	
	private Long id;
	private String nivel;
	private Long minPorcetagem;
	private Long maxPorcetagem;
	private String msg;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public Long getMinPorcetagem() {
		return minPorcetagem;
	}
	public void setMinPorcetagem(Long minPorcetagem) {
		this.minPorcetagem = minPorcetagem;
	}
	public Long getMaxPorcetagem() {
		return maxPorcetagem;
	}
	public void setMaxPorcetagem(Long maxPorcetagem) {
		this.maxPorcetagem = maxPorcetagem;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
