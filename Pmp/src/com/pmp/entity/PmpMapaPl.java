/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pmp.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Rodrigo
 */
@Entity
@Table(name = "PMP_MAPA_PL")
@NamedQueries({
    @NamedQuery(name = "PmpMapaPl.findAll", query = "SELECT p FROM PmpMapaPl p")})
public class PmpMapaPl implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SERIE")
    private String serie;
    @Column(name = "LATITUDE")
    private String latitude;
    @Column(name = "LOGITUDE")
    private String logitude;
    @Column(name = "CODIGO_CLIENTE")
    private String codigoCliente;
    @Column(name = "NOME_CLIENTE")
    private String nomeCliente;
    @Column(name = "NOME_FILIAL")
    private String nomeFilial;
    @Column(name = "MODELO")
    private String modelo;
    @Column(name = "HORIMETRO")
    private String horimetro;
    @Column(name = "DATA_ATUALIZACAO")
    private String dataAtualização;
    @Column(name = "COR")
    private String cor;
    @Column(name = "FILIAL")
    private Long filial;
    @Column(name = "ID_CONTRATO")
    private Long idContrato;

    public PmpMapaPl() {
    }

    public PmpMapaPl(String serie) {
        this.serie = serie;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLogitude() {
        return logitude;
    }

    public void setLogitude(String logitude) {
        this.logitude = logitude;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getNomeFilial() {
        return nomeFilial;
    }

    public void setNomeFilial(String nomeFilial) {
        this.nomeFilial = nomeFilial;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getHorimetro() {
        return horimetro;
    }

    public void setHorimetro(String horimetro) {
        this.horimetro = horimetro;
    }

    public String getDataAtualização() {
        return dataAtualização;
    }

    public void setDataAtualização(String dataAtualização) {
        this.dataAtualização = dataAtualização;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Long getFilial() {
        return filial;
    }

    public void setFilial(Long filial) {
        this.filial = filial;
    }

    public Long getIdContrato() {
		return idContrato;
	}

	public void setIdContrato(Long idContrato) {
		this.idContrato = idContrato;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (serie != null ? serie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PmpMapaPl)) {
            return false;
        }
        PmpMapaPl other = (PmpMapaPl) object;
        if ((this.serie == null && other.serie != null) || (this.serie != null && !this.serie.equals(other.serie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestaoequipamentos.PmpMapaPl[ serie=" + serie + " ]";
    }
    
}
