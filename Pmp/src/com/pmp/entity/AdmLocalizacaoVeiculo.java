/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Rodrigo
 */
@Entity
@Table(name = "ADM_LOCALIZACAO_VEICULO")

public class AdmLocalizacaoVeiculo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "DATA_REPORT")
    private String dataReport;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "LATITUDE")
    private String latitude;
    @Column(name = "LONGITUDE")
    private String longitude;
    @Column(name = "LOCALIZACAO")
    private String localizacao;
    @Column(name = "PLACA")
    private String placa;
    @Column(name = "VELOCIDADE")
    private Long velocidade;

    public AdmLocalizacaoVeiculo() {
    }

    public AdmLocalizacaoVeiculo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataReport() {
        return dataReport;
    }

    public void setDataReport(String dataReport) {
        this.dataReport = dataReport;
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

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Long getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(Long velocidade) {
        this.velocidade = velocidade;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdmLocalizacaoVeiculo)) {
            return false;
        }
        AdmLocalizacaoVeiculo other = (AdmLocalizacaoVeiculo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.AdmLocalizacaoVeiculo[ id=" + id + " ]";
    }
    
}
