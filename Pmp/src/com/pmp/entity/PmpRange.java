/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import javax.persistence.Basic;
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
@Table(name = "PMP_RANGE")
@NamedQueries({
    @NamedQuery(name = "PmpRange.findAll", query = "SELECT p FROM PmpRange p")})
public class PmpRange implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PmpRangePK pmpRangePK;
    @Column(name = "EQMFCD")
    private String eqmfcd;
    @Basic(optional = false)
    @Column(name = "BSERNO")
    private String bserno;

    public PmpRange() {
    }

    public PmpRange(PmpRangePK pmpRangePK) {
        this.pmpRangePK = pmpRangePK;
    }

    public PmpRange(PmpRangePK pmpRangePK, String bserno) {
        this.pmpRangePK = pmpRangePK;
        this.bserno = bserno;
    }

    public PmpRange(String begsn2, String endsn2, String eqmfm2) {
        this.pmpRangePK = new PmpRangePK(begsn2, endsn2, eqmfm2);
    }

    public PmpRangePK getPmpRangePK() {
        return pmpRangePK;
    }

    public void setPmpRangePK(PmpRangePK pmpRangePK) {
        this.pmpRangePK = pmpRangePK;
    }

    public String getEqmfcd() {
        return eqmfcd;
    }

    public void setEqmfcd(String eqmfcd) {
        this.eqmfcd = eqmfcd;
    }

    public String getBserno() {
        return bserno;
    }

    public void setBserno(String bserno) {
        this.bserno = bserno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pmpRangePK != null ? pmpRangePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PmpRange)) {
            return false;
        }
        PmpRange other = (PmpRange) object;
        if ((this.pmpRangePK == null && other.pmpRangePK != null) || (this.pmpRangePK != null && !this.pmpRangePK.equals(other.pmpRangePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpRange[ pmpRangePK=" + pmpRangePK + " ]";
    }
    
}
