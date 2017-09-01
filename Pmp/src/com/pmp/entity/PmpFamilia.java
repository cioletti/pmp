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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_FAMILIA")
@NamedQueries({
    @NamedQuery(name = "PmpFamilia.findAll", query = "SELECT p FROM PmpFamilia p")})
public class PmpFamilia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    private Long id;
    @Column(name = "DESCRICAO")
    private String descricao;
    @OneToMany(mappedBy = "idFamilia")
    private Collection<ArvInspecao> arvInspecaoCollection;

    public PmpFamilia() {
    }

    public PmpFamilia(Long id) {
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

    public Collection<ArvInspecao> getArvInspecaoCollection() {
        return arvInspecaoCollection;
    }

    public void setArvInspecaoCollection(Collection<ArvInspecao> arvInspecaoCollection) {
        this.arvInspecaoCollection = arvInspecaoCollection;
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
        if (!(object instanceof PmpFamilia)) {
            return false;
        }
        PmpFamilia other = (PmpFamilia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpFamilia[ id=" + id + " ]";
    }
    
}
