/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_STATUS_CONTRATO")
@NamedQueries({
    @NamedQuery(name = "PmpStatusContrato.findAll", query = "SELECT p FROM PmpStatusContrato p")})
public class PmpStatusContrato implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "SIGLA")
    private String sigla;
    @OneToMany(mappedBy = "idStatusContrato")
    private Collection<PmpContrato> pmpContratoCollection;

    public PmpStatusContrato() {
    }

    public PmpStatusContrato(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Collection<PmpContrato> getPmpContratoCollection() {
        return pmpContratoCollection;
    }

    public void setPmpContratoCollection(Collection<PmpContrato> pmpContratoCollection) {
        this.pmpContratoCollection = pmpContratoCollection;
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
        if (!(object instanceof PmpStatusContrato)) {
            return false;
        }
        PmpStatusContrato other = (PmpStatusContrato) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpStatusContrato[ id=" + id + " ]";
    }
    
}
