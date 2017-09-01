/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Rodrigo
 */
@Entity
@Table(name = "ADM_DESLOCAMENTO")
public class AdmDeslocamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "PLACA")
    private String placa;
    @Column(name = "DATA_REPORT_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataReportInicio;
    @Column(name = "METROS_INICIO")
    private Long metrosInicio;
    @Column(name = "METROS_FIM")
    private Long metrosFim;
    @Column(name = "DATA_REPORT_FIM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataReportFim;
    @Column(name = "DATA_ATUALIZACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;

    public AdmDeslocamento() {
    }

    public AdmDeslocamento(String placa) {
        this.placa = placa;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Date getDataReportInicio() {
        return dataReportInicio;
    }

    public void setDataReportInicio(Date dataReportInicio) {
        this.dataReportInicio = dataReportInicio;
    }

    public Long getMetrosInicio() {
        return metrosInicio;
    }

    public void setMetrosInicio(Long metrosInicio) {
        this.metrosInicio = metrosInicio;
    }

    public Long getMetrosFim() {
        return metrosFim;
    }

    public void setMetrosFim(Long metrosFim) {
        this.metrosFim = metrosFim;
    }

    public Date getDataReportFim() {
        return dataReportFim;
    }

    public void setDataReportFim(Date dataReportFim) {
        this.dataReportFim = dataReportFim;
    }

    public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (placa != null ? placa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdmDeslocamento)) {
            return false;
        }
        AdmDeslocamento other = (AdmDeslocamento) object;
        if ((this.placa == null && other.placa != null) || (this.placa != null && !this.placa.equals(other.placa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AdmDeslocamento[ placa=" + placa + " ]";
    }
    
}
