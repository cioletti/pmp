package com.pmp.bean;

import java.util.Date;

import com.pmp.entity.PmpCentroDeCusto;
import com.pmp.entity.PmpMaquinaPl;

public class MaquinaPlBean {

	private Long id;
	private String numeroSerie;
	private Number horimetro;
	private String latitude;
	private String longitude;
	private Date dataAtualizacao;
	private String modelo;
	private Long idContrato;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNumeroSerie() {
		return numeroSerie;
	}
	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}
	public Number getHorimetro() {
		return horimetro;
	}
	public void setHorimetro(Number horimetro) {
		this.horimetro = horimetro;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}
	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}	
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public Long getIdContrato() {
		return idContrato;
	}
	public void setIdContrato(Long idContrato) {
		this.idContrato = idContrato;
	}
	public void formBean(PmpMaquinaPl maquinaPl){
		setId(maquinaPl.getId().longValue());
		setNumeroSerie(maquinaPl.getNumeroSerie());
		setHorimetro(maquinaPl.getHorimetro());
		setLatitude(maquinaPl.getLatitude());
		setLongitude(maquinaPl.getLongitude());
		setDataAtualizacao(maquinaPl.getDataAtualizacao());	
		setModelo(maquinaPl.getModelo());	
	}
	
	public void toBean(PmpMaquinaPl entity){
		entity.setNumeroSerie(getNumeroSerie());
		entity.setHorimetro(getHorimetro().intValue());
		entity.setLatitude(getLatitude());
		entity.setLongitude(getLongitude());
		entity.setDataAtualizacao(getDataAtualizacao());
		entity.setModelo(getModelo());
	}
}
