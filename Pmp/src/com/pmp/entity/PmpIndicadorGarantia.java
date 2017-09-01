/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_INDICADOR_GARANTIA")
@NamedQueries({
    @NamedQuery(name = "PmpIndicadorGarantia.findAll", query = "SELECT p FROM PmpIndicadorGarantia p")})
public class PmpIndicadorGarantia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "TIPO")
    private String tipo;
    @Basic(optional = false)
    @Column(name = "IND")
    private String ind;
    @Basic(optional = false)
    @Column(name = "DESCRICAO")
    private String descricao;

    public PmpIndicadorGarantia() {
    }

    public PmpIndicadorGarantia(Long id) {
        this.id = id;
    }

    public PmpIndicadorGarantia(Long id, String tipo, String ind, String descricao) {
        this.id = id;
        this.tipo = tipo;
        this.ind = ind;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getInd() {
        return ind;
    }

    public void setInd(String ind) {
        this.ind = ind;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
        if (!(object instanceof PmpIndicadorGarantia)) {
            return false;
        }
        PmpIndicadorGarantia other = (PmpIndicadorGarantia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpIndicadorGarantia[ id=" + id + " ]";
    }
    
}
