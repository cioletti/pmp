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
@Table(name = "PMP_CONFIG_TRACAO")

public class PmpConfigTracao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "POSSUI_AR_CONDICIONADO")
    private String possuiArCondicionado;
    @JoinColumn(name = "ID_TRACAO", referencedColumnName = "ID")
    @ManyToOne
    private PmpTipoTracao idTracao;
    @JoinColumn(name = "ID_MODELO", referencedColumnName = "ID_ARV")
    @ManyToOne
    private ArvInspecao idModelo;
    @JoinColumn(name = "ID_FAMILIA", referencedColumnName = "ID")
    @ManyToOne
    private PmpFamilia idFamilia;

    public PmpConfigTracao() {
    }

    public PmpConfigTracao(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPossuiArCondicionado() {
        return possuiArCondicionado;
    }

    public void setPossuiArCondicionado(String possuiArCondicionado) {
        this.possuiArCondicionado = possuiArCondicionado;
    }

    public PmpTipoTracao getIdTracao() {
        return idTracao;
    }

    public void setIdTracao(PmpTipoTracao idTracao) {
        this.idTracao = idTracao;
    }

    public ArvInspecao getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(ArvInspecao idModelo) {
        this.idModelo = idModelo;
    }

    public PmpFamilia getIdFamilia() {
		return idFamilia;
	}

	public void setIdFamilia(PmpFamilia idFamilia) {
		this.idFamilia = idFamilia;
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
        if (!(object instanceof PmpConfigTracao)) {
            return false;
        }
        PmpConfigTracao other = (PmpConfigTracao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PmpConfigTracao[ id=" + id + " ]";
    }
    
}
