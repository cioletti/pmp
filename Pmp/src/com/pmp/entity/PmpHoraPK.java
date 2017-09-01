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
public class PmpHoraPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "JBCD")
    private String jbcd;
    @Basic(optional = false)
    @Column(name = "CPTCD")
    private String cptcd;
    @Basic(optional = false)
    @Column(name = "BEQMSN")
    private String beqmsn;
    @Basic(optional = false)
    @Column(name = "BGRP")
    private String bgrp;

    public PmpHoraPK() {
    }

    public PmpHoraPK(String jbcd, String cptcd, String beqmsn, String bgrp) {
        this.jbcd = jbcd;
        this.cptcd = cptcd;
        this.beqmsn = beqmsn;
        this.bgrp = bgrp;
    }

    public String getJbcd() {
        return jbcd;
    }

    public void setJbcd(String jbcd) {
        this.jbcd = jbcd;
    }

    public String getCptcd() {
        return cptcd;
    }

    public void setCptcd(String cptcd) {
        this.cptcd = cptcd;
    }

    public String getBeqmsn() {
        return beqmsn;
    }

    public void setBeqmsn(String beqmsn) {
        this.beqmsn = beqmsn;
    }

    public String getBgrp() {
        return bgrp;
    }

    public void setBgrp(String bgrp) {
        this.bgrp = bgrp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jbcd != null ? jbcd.hashCode() : 0);
        hash += (cptcd != null ? cptcd.hashCode() : 0);
        hash += (beqmsn != null ? beqmsn.hashCode() : 0);
        hash += (bgrp != null ? bgrp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PmpHoraPK)) {
            return false;
        }
        PmpHoraPK other = (PmpHoraPK) object;
        if ((this.jbcd == null && other.jbcd != null) || (this.jbcd != null && !this.jbcd.equals(other.jbcd))) {
            return false;
        }
        if ((this.cptcd == null && other.cptcd != null) || (this.cptcd != null && !this.cptcd.equals(other.cptcd))) {
            return false;
        }
        if ((this.beqmsn == null && other.beqmsn != null) || (this.beqmsn != null && !this.beqmsn.equals(other.beqmsn))) {
            return false;
        }
        if ((this.bgrp == null && other.bgrp != null) || (this.bgrp != null && !this.bgrp.equals(other.bgrp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpHoraPK[ jbcd=" + jbcd + ", cptcd=" + cptcd + ", beqmsn=" + beqmsn + ", bgrp=" + bgrp + " ]";
    }
    
}
