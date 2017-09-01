/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_HORA")
@NamedQueries({
    @NamedQuery(name = "PmpHora.findAll", query = "SELECT p FROM PmpHora p")})
public class PmpHora implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PmpHoraPK pmpHoraPK;
    @Column(name = "EQMFCD")
    private String eqmfcd;
    @Column(name = "FRSDHR")
    private String frsdhr;
    @Column(name = "EQMFMD")
    private String eqmfmd;
    @Column(name = "TIPO_PM")
    private String tipoPm;

    public PmpHora() {
    }

    public PmpHora(PmpHoraPK pmpHoraPK) {
        this.pmpHoraPK = pmpHoraPK;
    }

    public PmpHora(String jbcd, String cptcd, String beqmsn, String bgrp) {
        this.pmpHoraPK = new PmpHoraPK(jbcd, cptcd, beqmsn, bgrp);
    }

    public PmpHoraPK getPmpHoraPK() {
        return pmpHoraPK;
    }

    public void setPmpHoraPK(PmpHoraPK pmpHoraPK) {
        this.pmpHoraPK = pmpHoraPK;
    }

    public String getEqmfcd() {
        return eqmfcd;
    }

    public void setEqmfcd(String eqmfcd) {
        this.eqmfcd = eqmfcd;
    }

    public String getFrsdhr() {
        return frsdhr;
    }

    public void setFrsdhr(String frsdhr) {
        this.frsdhr = frsdhr;
    }

    public String getEqmfmd() {
        return eqmfmd;
    }

    public void setEqmfmd(String eqmfmd) {
        this.eqmfmd = eqmfmd;
    }

    public String getTipoPm() {
		return tipoPm;
	}

	public void setTipoPm(String tipoPm) {
		this.tipoPm = tipoPm;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (pmpHoraPK != null ? pmpHoraPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PmpHora)) {
            return false;
        }
        PmpHora other = (PmpHora) object;
        if ((this.pmpHoraPK == null && other.pmpHoraPK != null) || (this.pmpHoraPK != null && !this.pmpHoraPK.equals(other.pmpHoraPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpHora[ pmpHoraPK=" + pmpHoraPK + " ]";
    }
    
}
