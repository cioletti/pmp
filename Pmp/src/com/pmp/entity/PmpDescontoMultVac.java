/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pmp.entity;

import java.io.Serializable;
import javax.persistence.Basic;
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
@Table(name = "PMP_DESCONTO_MULT_VAC")
public class PmpDescontoMultVac implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    private Long id;
    @Column(name = "DESCONTO_MULTI_VAC")
    private Long descontoMultiVac;

    public PmpDescontoMultVac() {
    }

    public PmpDescontoMultVac(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDescontoMultiVac() {
        return descontoMultiVac;
    }

    public void setDescontoMultiVac(Long descontoMultiVac) {
        this.descontoMultiVac = descontoMultiVac;
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
        if (!(object instanceof PmpDescontoMultVac)) {
            return false;
        }
        PmpDescontoMultVac other = (PmpDescontoMultVac) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestaoequipamentos.entity.PmpDescontoMultVac[ id=" + id + " ]";
    }
    
}
