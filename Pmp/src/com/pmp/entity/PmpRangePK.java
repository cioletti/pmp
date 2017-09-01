/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author RDR
 */
@Embeddable
public class PmpRangePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "BEGSN2")
    private String begsn2;
    @Basic(optional = false)
    @Column(name = "ENDSN2")
    private String endsn2;
    @Basic(optional = false)
    @Column(name = "EQMFM2")
    private String eqmfm2;

    public PmpRangePK() {
    }

    public PmpRangePK(String begsn2, String endsn2, String eqmfm2) {
        this.begsn2 = begsn2;
        this.endsn2 = endsn2;
        this.eqmfm2 = eqmfm2;
    }

    public String getBegsn2() {
        return begsn2;
    }

    public void setBegsn2(String begsn2) {
        this.begsn2 = begsn2;
    }

    public String getEndsn2() {
        return endsn2;
    }

    public void setEndsn2(String endsn2) {
        this.endsn2 = endsn2;
    }

    public String getEqmfm2() {
        return eqmfm2;
    }

    public void setEqmfm2(String eqmfm2) {
        this.eqmfm2 = eqmfm2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (begsn2 != null ? begsn2.hashCode() : 0);
        hash += (endsn2 != null ? endsn2.hashCode() : 0);
        hash += (eqmfm2 != null ? eqmfm2.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PmpRangePK)) {
            return false;
        }
        PmpRangePK other = (PmpRangePK) object;
        if ((this.begsn2 == null && other.begsn2 != null) || (this.begsn2 != null && !this.begsn2.equals(other.begsn2))) {
            return false;
        }
        if ((this.endsn2 == null && other.endsn2 != null) || (this.endsn2 != null && !this.endsn2.equals(other.endsn2))) {
            return false;
        }
        if ((this.eqmfm2 == null && other.eqmfm2 != null) || (this.eqmfm2 != null && !this.eqmfm2.equals(other.eqmfm2))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpRangePK[ begsn2=" + begsn2 + ", endsn2=" + endsn2 + ", eqmfm2=" + eqmfm2 + " ]";
    }
    
}
