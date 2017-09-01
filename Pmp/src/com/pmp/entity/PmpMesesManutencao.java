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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "PMP_MESES_MANUTENCAO")
@NamedQueries({
    @NamedQuery(name = "PmpMesesManutencao.findAll", query = "SELECT a FROM PmpMesesManutencao a")})
public class PmpMesesManutencao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "ID_FAMILIA", referencedColumnName = "ID")
    @ManyToOne
    private PmpFamilia idFamilia;    
    @JoinColumn(name = "ID_MODELO", referencedColumnName = "ID_ARV")
    @ManyToOne
    private ArvInspecao idModelo;
    @Column(name = "MESES_MANUTENCAO")
    private Long mesesManutencao;
    

    public PmpMesesManutencao() {
    }

   

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public PmpFamilia getIdFamilia() {
		return idFamilia;
	}



	public void setIdFamilia(PmpFamilia idFamilia) {
		this.idFamilia = idFamilia;
	}



	
	public Long getMesesManutencao() {
		return mesesManutencao;
	}



	public void setMesesManutencao(Long mesesManutencao) {
		this.mesesManutencao = mesesManutencao;
	}



	public ArvInspecao getIdModelo() {
		return idModelo;
	}



	public void setIdModelo(ArvInspecao idModelo) {
		this.idModelo = idModelo;
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
        if (!(object instanceof PmpMesesManutencao)) {
            return false;
        }
        PmpMesesManutencao other = (PmpMesesManutencao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpMesesManutencao[ id=" + id + " ]";
    }
    
}
