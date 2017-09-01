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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author sandro
 */
@Entity
@Table(name = "TR_CARGO")
@NamedQueries({
    @NamedQuery(name = "TrCargo.findAll", query = "SELECT t FROM TrCargo t")})
public class TrCargo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;
    @Column(name = "DESCRICAO")
    private String descricao;     
    @Column(name = "PROFICIENCIA")
    private String proficiencia;
    @Column(name = "GRADE")
    private Integer grade;


    public TrCargo() {
    }

    public TrCargo(Long id) {
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



	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}


	public String getProficiencia() {
		return proficiencia;
	}

	public void setProficiencia(String proficiencia) {
		this.proficiencia = proficiencia;
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
        if (!(object instanceof TrCargo)) {
            return false;
        }
        TrCargo other = (TrCargo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.treinamento.entity.TrCargo[ id=" + id + " ]";
    }
    
}
