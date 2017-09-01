/*
 * To change this template, choose Tools | Templates
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
 * @author RDR
 */
@Entity
@Table(name = "PMP_CRITICIDADE_MANUTENCAO")

public class PmpCriticidadeManutencao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    private Long id;
    @Column(name = "NIVEL")
    private String nivel;
    @Column(name = "MIN_PORCETAGEM")
    private Long minPorcetagem;
    @Column(name = "MAX_PORCETAGEM")
    private Long maxPorcetagem;

    public PmpCriticidadeManutencao() {
    }

   

   

    public Long getId() {
		return id;
	}





	public void setId(Long id) {
		this.id = id;
	}





	public String getNivel() {
		return nivel;
	}





	public void setNivel(String nivel) {
		this.nivel = nivel;
	}





	public Long getMinPorcetagem() {
		return minPorcetagem;
	}





	public void setMinPorcetagem(Long minPorcetagem) {
		this.minPorcetagem = minPorcetagem;
	}





	public Long getMaxPorcetagem() {
		return maxPorcetagem;
	}





	public void setMaxPorcetagem(Long maxPorcetagem) {
		this.maxPorcetagem = maxPorcetagem;
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
        if (!(object instanceof PmpCriticidadeManutencao)) {
            return false;
        }
        PmpCriticidadeManutencao other = (PmpCriticidadeManutencao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpCriticidadeManutencao[ id=" + id + " ]";
    }
    
}
