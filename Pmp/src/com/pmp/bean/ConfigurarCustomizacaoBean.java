package com.pmp.bean;

import java.util.ArrayList;
import java.util.List;

import com.pmp.entity.PmpTipoTracao;



public class ConfigurarCustomizacaoBean {

	private Long id;
	private Long idModelo;
	private Long idFamilia;
	private String descricao;
	private Long idTipoCustomizacao;
	private Boolean isSelected;
	public Boolean getIsSelected() {
		return isSelected;
	}


	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	private List<String> siglaCustList;
	
	
	public ConfigurarCustomizacaoBean() {
		siglaCustList = new ArrayList<String>();
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdModelo() {
		return idModelo;
	}

	public void setIdModelo(Long idModelo) {
		this.idModelo = idModelo;
	}

	public Long getIdFamilia() {
		return idFamilia;
	}

	public void setIdFamilia(Long idFamilia) {
		this.idFamilia = idFamilia;
	}

	public Long getIdTipoCustomizacao() {
		return idTipoCustomizacao;
	}


	public void setIdTipoCustomizacao(Long idTipoCustomizacao) {
		this.idTipoCustomizacao = idTipoCustomizacao;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public List<String> getSiglaCustList() {
		return siglaCustList;
	}


	public void setSiglaCustList(List<String> siglaCustList) {
		this.siglaCustList = siglaCustList;
	}


	public void fromBean(PmpTipoTracao tipoTracao){
		setId(tipoTracao.getId().longValue());
		setIdModelo(tipoTracao.getIdModelo());
	}
	
	public void toBean(PmpTipoTracao entity){
		//entity.setSigla(getSigla());
	}
}
