/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Rodrigo
 */
@Entity
@Table(name = "PMP_TIPO_CUSTOMIZACAO")

public class PmpTipoCustomizacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "DESCRICAO")
    private String descricao;
    @JoinColumn(name = "ID_MODELO", referencedColumnName = "ID_ARV")
    @ManyToOne
    private ArvInspecao idModelo;
    @OneToMany(mappedBy = "idTipoCustomizacao")
    private Collection<PmpContratoCustomizacao> pmpContratoCustomizacaoCollection;

    public PmpTipoCustomizacao() {
    }

    public PmpTipoCustomizacao(Long id) {
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

    public ArvInspecao getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(ArvInspecao idModelo) {
        this.idModelo = idModelo;
    }

    public Collection<PmpContratoCustomizacao> getPmpContratoCustomizacaoCollection() {
        return pmpContratoCustomizacaoCollection;
    }

    public void setPmpContratoCustomizacaoCollection(Collection<PmpContratoCustomizacao> pmpContratoCustomizacaoCollection) {
        this.pmpContratoCustomizacaoCollection = pmpContratoCustomizacaoCollection;
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
        if (!(object instanceof PmpTipoCustomizacao)) {
            return false;
        }
        PmpTipoCustomizacao other = (PmpTipoCustomizacao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpTipoCustomizacao[ id=" + id + " ]";
    }
    
}
