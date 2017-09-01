package com.pmp.bean;

import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class RevisaoPecasBean {
	
	private String horas;
	private JRBeanCollectionDataSource pecas;
	public String getHoras() {
		return horas;
	}
	public void setHoras(String horas) {
		this.horas = horas;
	}
	public JRBeanCollectionDataSource getPecas() {
		return pecas;
	}
	public void setPecas(JRBeanCollectionDataSource pecas) {
		this.pecas = pecas;
	}


	

}
