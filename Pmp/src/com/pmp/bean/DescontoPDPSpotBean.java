package com.pmp.bean;

import java.math.BigDecimal;

import com.pmp.entity.PmpDescontoPdpSpot;
import com.pmp.util.ValorMonetarioHelper;

public class DescontoPDPSpotBean {
	private Long id;
	private String descricao;
	private String valor;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public void fromBean(PmpDescontoPdpSpot descontoPdp){
		setId(descontoPdp.getId().longValue());
		setDescricao(descontoPdp.getDescricao());
		setValor(String.valueOf(ValorMonetarioHelper.formata("###,###,##0.00", Double.valueOf(String.valueOf(descontoPdp.getValor())))));
	}
	
	public void toBean(PmpDescontoPdpSpot entity){
		entity.setDescricao(getDescricao());
		entity.setValor(BigDecimal.valueOf(Double.valueOf(getValor().replace(".", "").replace(",", "."))));
	}
}
