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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Rodrigo
 */
@Entity
@Table(name = "PMP_SIGLA_AC")
@NamedQueries({
    @NamedQuery(name = "PmpSiglaAc.findAll", query = "SELECT p FROM PmpSiglaAc p")})
public class PmpSiglaAc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "SIGLA_AC")
    private String siglaAc;
    @Column(name = "ID_CONFIG_TRACAO")
    private Long idConfigTracao;

    public PmpSiglaAc() {
    }

    public PmpSiglaAc(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiglaAc() {
        return siglaAc;
    }

    public void setSiglaAc(String siglaAc) {
        this.siglaAc = siglaAc;
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
        if (!(object instanceof PmpSiglaAc)) {
            return false;
        }
        PmpSiglaAc other = (PmpSiglaAc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PmpSiglaAc[ id=" + id + " ]";
    }
    
}
