/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_MANUTENCAO")

public class PmpManutencao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
   private Long id;
    @Basic(optional = false)
    @Column(name = "BGRP")
    private String bgrp;
    @Basic(optional = false)
    @Column(name = "BEQMSN")
    private String beqmsn;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "UNCS")
    private BigDecimal uncs;
    @Column(name = "UNLS")
    private BigDecimal unls;
    @Column(name = "DLRQTY")
    private Long dlrqty;
    @Basic(optional = false)
    @Column(name = "PANO20")
    private String pano20;
    @Column(name = "BECTYC")
    private String bectyc;
    @Basic(optional = false)
    @Column(name = "CPTCD")
    private String cptcd;
    @Column(name = "DS18")
    private String ds18;
    @Column(name = "SOS")
    private String sos;
    @Column(name = "OCPTMD")
    private String ocptmd;
    @Column(name = "OJBLOC")
    private String ojbloc;
    @Column(name = "JWKAPP")
    private String jwkapp;
    

    public PmpManutencao() {
    }

    public PmpManutencao(Long id) {
        this.id = id;
    }

    public PmpManutencao(Long id, String bgrp, String beqmsn, String pano20, String cptcd) {
        this.id = id;
        this.bgrp = bgrp;
        this.beqmsn = beqmsn;
        this.pano20 = pano20;
        this.cptcd = cptcd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBgrp() {
        return bgrp;
    }

    public void setBgrp(String bgrp) {
        this.bgrp = bgrp;
    }

    public String getBeqmsn() {
        return beqmsn;
    }

    public void setBeqmsn(String beqmsn) {
        this.beqmsn = beqmsn;
    }

    public BigDecimal getUncs() {
        return uncs;
    }

    public void setUncs(BigDecimal uncs) {
        this.uncs = uncs;
    }

    public BigDecimal getUnls() {
        return unls;
    }

    public void setUnls(BigDecimal unls) {
        this.unls = unls;
    }

    public Long getDlrqty() {
        return dlrqty;
    }

    public void setDlrqty(Long dlrqty) {
        this.dlrqty = dlrqty;
    }

    public String getPano20() {
        return pano20;
    }

    public void setPano20(String pano20) {
        this.pano20 = pano20;
    }

    public String getBectyc() {
        return bectyc;
    }

    public void setBectyc(String bectyc) {
        this.bectyc = bectyc;
    }

    public String getCptcd() {
        return cptcd;
    }

    public void setCptcd(String cptcd) {
        this.cptcd = cptcd;
    }

    public String getDs18() {
        return ds18;
    }

    public void setDs18(String ds18) {
        this.ds18 = ds18;
    }

    public String getSos() {
        return sos;
    }

    public void setSos(String sos) {
        this.sos = sos;
    }

    public String getOcptmd() {
        return ocptmd;
    }

    public void setOcptmd(String ocptmd) {
        this.ocptmd = ocptmd;
    }

    public String getOjbloc() {
        return ojbloc;
    }

    public void setOjbloc(String ojbloc) {
        this.ojbloc = ojbloc;
    }

    public String getJwapp() {
		return jwkapp;
	}

	public void setJwapp(String jwapp) {
		this.jwkapp = jwapp;
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
        if (!(object instanceof PmpManutencao)) {
            return false;
        }
        PmpManutencao other = (PmpManutencao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PmpManutencao[ id=" + id + " ]";
    }
    
}
