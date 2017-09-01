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
import javax.persistence.Table;

/**
 *
 * @author Rodrigo
 */
@Entity
@Table(name = "PMP_SIGLA_TRACAO")

public class PmpSiglaTracao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "SIGLA_TRACAO")
    private String siglaTracao;
    @Column(name = "ID_CONFIG_TRACAO")
    private Long idConfigTracao;

    public PmpSiglaTracao() {
    }

    public PmpSiglaTracao(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiglaTracao() {
        return siglaTracao;
    }

    public void setSiglaTracao(String siglaTracao) {
        this.siglaTracao = siglaTracao;
    }

    public Long getIdConfigTracao() {
        return idConfigTracao;
    }

    public void setIdConfigTracao(Long idConfigTracao) {
        this.idConfigTracao = idConfigTracao;
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
        if (!(object instanceof PmpSiglaTracao)) {
            return false;
        }
        PmpSiglaTracao other = (PmpSiglaTracao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PmpSiglaTracao[ id=" + id + " ]";
    }
    
}
