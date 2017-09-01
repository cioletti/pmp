package com.pmp.bean;

import java.io.Serializable;

import com.pmp.entity.TwFilial;

public class FilialBean implements Serializable {

	private static final long serialVersionUID = 2155358431269549665L;
	private Long stno;
	private String stnm;
	private String descricao;
	private String cnpj;
	private String razaoSocial;
	private String endereco;
	private String cep;

	public Long getStno() {
		return stno;
	}

	public void setStno(Long stno) {
		this.stno = stno;
	}

	public String getStnm() {
		return stnm;
	}

	public void setStnm(String stnm) {
		this.stnm = stnm;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public void fromBean(TwFilial entity) {
		setStno(entity.getStno());
		setStnm(entity.getStnm());
	}

	public TwFilial toBean() {
		TwFilial fil = new TwFilial();
		fil.setStnm(getStnm());
		return fil;
	}
}
