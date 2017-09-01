package com.pmp.bean;

import java.io.Serializable;

public class MultiVacBean implements Serializable {

	private static final long serialVersionUID = 2155358431269549665L;
	private Long valorDesconto;
	
	public Long getValorDesconto() {
		return valorDesconto;
	}
	public void setValorDesconto(Long valorDesconto) {
		this.valorDesconto = valorDesconto;
	}
	
}
