/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Rodrigo
 */
@Entity
@Table(name = "PMP_NOTA_FISCAL")
@NamedQueries({
    @NamedQuery(name = "PmpNotaFiscal.findAll", query = "SELECT p FROM PmpNotaFiscal p")})
public class PmpNotaFiscal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @Column(name = "ID", nullable = false)
    @SequenceGenerator(name = "SEQ", sequenceName = "PMP_ID_NOTA_FISCAL_SEQ" )
    @GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ") 
    private Long id;
    @Column(name = "NGFIL")
    private Long ngfil;
    @Column(name = "NGNDOC")
    private String ngndoc;
    @Column(name = "NGNF")
    private Long ngnf;
    @Column(name = "NGDOCT")
    private String ngdoct;
    @Column(name = "NGSTAT")
    private String ngstat;
    @Column(name = "MGDTEDD")
    private String mgdtedd;
    @Column(name = "MGDTEMM")
    private String mgdtemm;
    @Column(name = "MGDTEYY")
    private String mgdteyy;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "VRTNF")
    private BigDecimal vrtnf;
    @Column(name = "NGCLI")
    private String ngcli;
    @Column(name = "NGRS")
    private String ngrs;
    @Column(name = "FEDORI")
    private String fedori;
    @Column(name = "NFUF")
    private String nfuf;
    @Column(name = "CONPAG")
    private Long conpag;
    @Column(name = "DSCCP")
    private String dsccp;
    @Column(name = "STN1")
    private Long stn1;

    public PmpNotaFiscal() {
    }

    public PmpNotaFiscal(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNgfil() {
        return ngfil;
    }

    public void setNgfil(Long ngfil) {
        this.ngfil = ngfil;
    }

    public String getNgndoc() {
        return ngndoc;
    }

    public void setNgndoc(String ngndoc) {
        this.ngndoc = ngndoc;
    }

    public Long getNgnf() {
        return ngnf;
    }

    public void setNgnf(Long ngnf) {
        this.ngnf = ngnf;
    }

    public String getNgdoct() {
        return ngdoct;
    }

    public void setNgdoct(String ngdoct) {
        this.ngdoct = ngdoct;
    }

    public String getNgstat() {
        return ngstat;
    }

    public void setNgstat(String ngstat) {
        this.ngstat = ngstat;
    }

    public String getMgdtedd() {
        return mgdtedd;
    }

    public void setMgdtedd(String mgdtedd) {
        this.mgdtedd = mgdtedd;
    }

    public String getMgdtemm() {
        return mgdtemm;
    }

    public void setMgdtemm(String mgdtemm) {
        this.mgdtemm = mgdtemm;
    }

    public String getMgdteyy() {
        return mgdteyy;
    }

    public void setMgdteyy(String mgdteyy) {
        this.mgdteyy = mgdteyy;
    }

    public BigDecimal getVrtnf() {
        return vrtnf;
    }

    public void setVrtnf(BigDecimal vrtnf) {
        this.vrtnf = vrtnf;
    }

    public String getNgcli() {
        return ngcli;
    }

    public void setNgcli(String ngcli) {
        this.ngcli = ngcli;
    }

    public String getNgrs() {
        return ngrs;
    }

    public void setNgrs(String ngrs) {
        this.ngrs = ngrs;
    }

    public String getFedori() {
        return fedori;
    }

    public void setFedori(String fedori) {
        this.fedori = fedori;
    }

    public String getNfuf() {
        return nfuf;
    }

    public void setNfuf(String nfuf) {
        this.nfuf = nfuf;
    }

    public Long getConpag() {
        return conpag;
    }

    public void setConpag(Long conpag) {
        this.conpag = conpag;
    }

    public String getDsccp() {
        return dsccp;
    }

    public void setDsccp(String dsccp) {
        this.dsccp = dsccp;
    }

    public Long getStn1() {
        return stn1;
    }

    public void setStn1(Long stn1) {
        this.stn1 = stn1;
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
        if (!(object instanceof PmpNotaFiscal)) {
            return false;
        }
        PmpNotaFiscal other = (PmpNotaFiscal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpNotaFiscal[ id=" + id + " ]";
    }
    
}
