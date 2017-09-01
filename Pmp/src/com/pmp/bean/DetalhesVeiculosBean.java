package com.pmp.bean;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.pmp.entity.PmpDetalhesVeiculos;
import com.pmp.entity.TwFuncionario;

public class DetalhesVeiculosBean {
    private Long id;
    private String frota;
    private Long idFilial;
    private String chassi;
    private String placa;
    private String modelo;
    private String renavan;
    private String anoVeiculo;
    private String dataCompra;
    private String kmAtualData;
    private String viaFacil;
    private String goodCar;
    private String vistoriaCrono;
    private Long pi;
    private String trocaPneuDianteiro;
    private String trocaPneuTraseiro;
    private String notebook;
    private TwFuncionario responsavel;
    private String responsavelNome;
    private String filial;
    private String responsavelId;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFrota() {
		return frota;
	}
	public void setFrota(String frota) {
		this.frota = frota;
	}
	public Long getIdFilial() {
		return idFilial;
	}
	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}
	public String getChassi() {
		return chassi;
	}
	public void setChassi(String chassi) {
		this.chassi = chassi;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getRenavan() {
		return renavan;
	}
	public void setRenavan(String renavan) {
		this.renavan = renavan;
	}
	public String getAnoVeiculo() {
		return anoVeiculo;
	}
	public void setAnoVeiculo(String anoVeiculo) {
		this.anoVeiculo = anoVeiculo;
	}
	public String getDataCompra() {
		return dataCompra;
	}
	public void setDataCompra(String dataCompra) {
		this.dataCompra = dataCompra;
	}
	public String getKmAtualData() {
		return kmAtualData;
	}
	public void setKmAtualData(String kmAtualData) {
		this.kmAtualData = kmAtualData;
	}
	public String getViaFacil() {
		return viaFacil;
	}
	public void setViaFacil(String viaFacil) {
		this.viaFacil = viaFacil;
	}
	public String getGoodcar() {
		return goodCar;
	}
	public void setGoodcar(String goodcar) {
		this.goodCar = goodcar;
	}
	public String getVistoriaCrono() {
		return vistoriaCrono;
	}
	public void setVistoriaCrono(String vistoriaCrono) {
		this.vistoriaCrono = vistoriaCrono;
	}
	public Long getPi() {
		return pi;
	}
	public void setPi(Long pi) {
		this.pi = pi;
	}
	public String getTrocaPneuDianteiro() {
		return trocaPneuDianteiro;
	}
	public void setTrocaPneuDianteiro(String trocaPneuDianteiro) {
		this.trocaPneuDianteiro = trocaPneuDianteiro;
	}
	public String getTrocaPneuTraseiro() {
		return trocaPneuTraseiro;
	}
	public void setTrocaPneuTraseiro(String trocaPneuTraseiro) {
		this.trocaPneuTraseiro = trocaPneuTraseiro;
	}
	public String getNotebook() {
		return notebook;
	}
	public void setNotebook(String notebook) {
		this.notebook = notebook;
	}
	public TwFuncionario getResponsavel() {
		return responsavel;
	}
	public void setResponsavel(TwFuncionario responsavel) {
		this.responsavel = responsavel;
	} 
	
	public String getFilial() {
		return filial;
	}
	public void setFilial(String filial) {
		this.filial = filial;
	}
	public String getResponsavelNome() {
		return responsavelNome;
	}
	public void setResponsavelNome(String responsavelNome) {
		this.responsavelNome = responsavelNome;
	}
	public String getResponsavelId() {
		return responsavelId;
	}
	public void setResponsavelId(String responsavelId) {
		this.responsavelId = responsavelId;
	}
	public void toBean(DetalhesVeiculosBean bean, PmpDetalhesVeiculos detalhes){
		bean.setId(detalhes.getId());
		bean.setFrota(detalhes.getFrota());
		bean.setModelo(detalhes.getModelo());
		bean.setRenavan(detalhes.getRenavan());
		bean.setAnoVeiculo(detalhes.getAnoVeiculo());
		bean.setDataCompra(detalhes.getDataCompra());
		bean.setKmAtualData(detalhes.getKmAtualData());
		bean.setViaFacil(detalhes.getViaFacil());
		bean.setGoodcar(detalhes.getGoodcar());
		bean.setVistoriaCrono(detalhes.getVistoriaCrono());
		bean.setPi(detalhes.getPi());
		bean.setTrocaPneuDianteiro(detalhes.getTrocaPneuDianteiro());
		bean.setTrocaPneuTraseiro(detalhes.getTrocaPneuTraseiro());
		bean.setNotebook(detalhes.getNotebook());
		bean.setIdFilial(detalhes.getIdFilial());
		if(detalhes.getResponsavel() == null){
			bean.setResponsavelId("-1000");
		}else{
			bean.setResponsavelId(detalhes.getResponsavel().getEpidno());
		}
		bean.setChassi(detalhes.getChassi());
		bean.setPlaca(detalhes.getPlaca());

	}
	
	public void fromBean(DetalhesVeiculosBean bean, PmpDetalhesVeiculos detalhes){
		detalhes.setFrota(bean.getFrota().toUpperCase());
		detalhes.setIdFilial(bean.getIdFilial());
		detalhes.setChassi(bean.getChassi());
		detalhes.setModelo(bean.getModelo());
		detalhes.setRenavan(bean.getRenavan());
		detalhes.setAnoVeiculo(bean.getAnoVeiculo());
		detalhes.setDataCompra(bean.getDataCompra());
		detalhes.setKmAtualData(bean.getKmAtualData());
		detalhes.setViaFacil(bean.getViaFacil());
		detalhes.setGoodcar(bean.getGoodcar());
		detalhes.setVistoriaCrono(bean.getVistoriaCrono());
		detalhes.setPi(bean.getPi());
		detalhes.setTrocaPneuDianteiro(bean.getTrocaPneuDianteiro());
		detalhes.setTrocaPneuTraseiro(bean.getTrocaPneuTraseiro());
		detalhes.setNotebook(bean.getNotebook());
		detalhes.setPlaca(bean.getPlaca().toUpperCase());
		
	}
	
    
}
