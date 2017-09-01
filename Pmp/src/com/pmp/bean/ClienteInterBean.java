package com.pmp.bean;

import com.pmp.entity.PmpClienteInter;

public class ClienteInterBean {

	private Long id;
	private Long fkFilial;
	private String customerNum;
	private String searchKey;
	private String descricao;
	private Long cod;
	private String filialDesc;	
	
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
		
	public Long getFkFilial() {
		return fkFilial;
	}
	public void setFkFilial(Long fkFilial) {
		this.fkFilial = fkFilial;
	}
	public String getCustomerNum() {
		return customerNum;
	}
	public void setCustomerNum(String customerNum) {
		this.customerNum = customerNum;
	}
	public String getSearchKey() {
		return searchKey;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	public Long getCod() {
		return cod;
	}
	public void setCod(Long cod) {
		this.cod = cod;
	}
	public String getFilialDesc() {
		return filialDesc;
	}
	public void setFilialDesc(String filialDesc) {
		this.filialDesc = filialDesc;
	}
	public void formBean(PmpClienteInter clienteInter){
		setId(clienteInter.getId().longValue());
		setFkFilial(clienteInter.getFkFilial());
		setDescricao(clienteInter.getDescricao());
		setSearchKey(clienteInter.getSearchKey());
		setCustomerNum(clienteInter.getCustomerNum());
		setCod(clienteInter.getCod());
		setFilialDesc(clienteInter.getFilialDesc());
	}
	
	public void toBean(PmpClienteInter entity){
		entity.setFkFilial(getFkFilial());
		entity.setDescricao(getDescricao());
		entity.setSearchKey(getSearchKey());
		entity.setCustomerNum(getCustomerNum());
		entity.setCod(getCod());
		entity.setFilialDesc(getFilialDesc());
	}
}
