/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pmp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Rodrigo
 */
@Entity
@Table(name = "PMP_DASHBOARD_CONTRATOS")
@NamedQueries({
    @NamedQuery(name = "PmpDashboardContratos.findAll", query = "SELECT p FROM PmpDashboardContratos p")})
public class PmpDashboardContratos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    private Long id;
    @Column(name = "ANO")
    private Long ano;
    @Column(name = "MES")
    private Long mes;
    @Column(name = "QUANTIDADE")
    private Long quantidade;
    @Column(name = "DATA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Column(name = "SIGLA")
    private String sigla;
    @Column(name = "FILIAL")
    private Long filial;

    public Long getFilial() {
		return filial;
	}

	public void setFilial(Long filial) {
		this.filial = filial;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public PmpDashboardContratos() {
    }

    public PmpDashboardContratos(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAno() {
        return ano;
    }

    public void setAno(Long ano) {
        this.ano = ano;
    }

    public Long getMes() {
        return mes;
    }

    public void setMes(Long mes) {
        this.mes = mes;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
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
        if (!(object instanceof PmpDashboardContratos)) {
            return false;
        }
        PmpDashboardContratos other = (PmpDashboardContratos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpDashboardContratos[ id=" + id + " ]";
    }
    
}
