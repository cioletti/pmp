package com.pmp.bean;

import java.io.Serializable;
import java.util.List;

import com.pmp.entity.PmpConfigOperacional;
import com.pmp.entity.PmpContaContabil;
import com.pmp.entity.PmpContrato;

public class ConfigOperacionalBean implements Serializable {

	private static final long serialVersionUID = 8515908170819560165L;

	private Long id;
	private Long idContrato;
	private ContratoComercialBean contrato;
	private String numOs;
	private String idFuncSupervisor;
	private String contato;
	private String local;
	private String telefoneContato;
	private Long filial;
	private String email;
	private String numeroSerie;
	private String obsCliente;
	private List<FilialBean> filialList;
	private List<ConfigurarCustomizacaoBean> tipoCustomizacaoList;
	
	private String beginRanger;
	private String endRanger;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdContrato() {
		return idContrato;
	}
	
	public void setIdContrato(Long idContrato) {
		this.idContrato = idContrato;
	}

	public void setContrato(ContratoComercialBean contrato) {
		this.contrato = contrato;
	}
	
	public ContratoComercialBean getContrato() {
		return contrato;
	}	

	public String getNumOs() {
		return numOs;
	}

	public void setNumOs(String numOs) {
		this.numOs = numOs;
	}

	public String getIdFuncSupervisor() {
		return idFuncSupervisor;
	}

	public void setIdFuncSupervisor(String idFuncSupervisor) {
		this.idFuncSupervisor = idFuncSupervisor;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getTelefoneContato() {
		return telefoneContato;
	}

	public void setTelefoneContato(String telefoneContato) {
		this.telefoneContato = telefoneContato;
	}

	public Long getFilial() {
		return filial;
	}

	public void setFilial(Long filial) {
		this.filial = filial;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	public String getObsCliente() {
		return obsCliente;
	}

	public void setObsCliente(String obsCliente) {
		this.obsCliente = obsCliente;
	}

	public String getBeginRanger() {
		return beginRanger;
	}

	public void setBeginRanger(String beginRanger) {
		this.beginRanger = beginRanger;
	}

	public String getEndRanger() {
		return endRanger;
	}

	public void setEndRanger(String endRanger) {
		this.endRanger = endRanger;
	}

	public List<ConfigurarCustomizacaoBean> getTipoCustomizacaoList() {
		return tipoCustomizacaoList;
	}

	public void setTipoCustomizacaoList(
			List<ConfigurarCustomizacaoBean> tipoCustomizacaoList) {
		this.tipoCustomizacaoList = tipoCustomizacaoList;
	}

	public void formBean(PmpConfigOperacional entity){
		setId(entity.getId());
		setIdContrato(entity.getIdContrato().getId().longValue());
		setIdFuncSupervisor(entity.getIdFuncSupervisor());
		setNumOs(entity.getNumOs());
		setContato(entity.getContato());
		setLocal(entity.getLocal());
		setTelefoneContato(entity.getTelefoneContato());
		setFilial(entity.getFilial());
		setEmail(entity.getIdContrato().getEmailContatoServicos());
		setNumeroSerie(entity.getIdContrato().getNumeroSerie());
		setObsCliente((entity.getObs() != null) ? entity.getObs() : ""); 
	}
	
	public void toBean(PmpConfigOperacional  entity){
		entity.setNumOs(getNumOs());
		entity.setIdFuncSupervisor(getIdFuncSupervisor());
		entity.setLocal(getLocal());
		entity.setContato(getContato());
		entity.setTelefoneContato(getTelefoneContato());
		entity.setFilial(getFilial());
		entity.setObs(getObsCliente());
	}

}
