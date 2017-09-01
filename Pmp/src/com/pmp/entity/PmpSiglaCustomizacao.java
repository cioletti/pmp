/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Rodrigo
 */
@Entity
@Table(name = "PMP_SIGLA_CUSTOMIZACAO")

public class PmpSiglaCustomizacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "SIGLA_CUSTOMIZACAO")
    private String siglaCustomizacao;
    @JoinColumn(name = "ID_CONFIG_CUSTOMIZACAO", referencedColumnName = "ID")
    @ManyToOne
    private PmpConfigCustomizacao idConfigCustumizacao;

    public PmpSiglaCustomizacao() {
    }

    public PmpSiglaCustomizacao(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiglaCustomizacao() {
        return siglaCustomizacao;
    }

    public void setSiglaCustomizacao(String siglaCustomizacao) {
        this.siglaCustomizacao = siglaCustomizacao;
    }

    public PmpConfigCustomizacao getIdConfigCustumizacao() {
        return idConfigCustumizacao;
    }

    public void setIdConfigCustumizacao(PmpConfigCustomizacao idConfigCustumizacao) {
        this.idConfigCustumizacao = idConfigCustumizacao;
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
        if (!(object instanceof PmpSiglaCustomizacao)) {
            return false;
        }
        PmpSiglaCustomizacao other = (PmpSiglaCustomizacao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpSiglaCustumizacao[ id=" + id + " ]";
    }
    
}
