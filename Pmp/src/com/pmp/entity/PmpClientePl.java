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
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_CLIENTE_PL")
public class PmpClientePl implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SERIE")
    private String serie;
    @Column(name = "COD_CLIENTE")
    private String codCliente;
    @Column(name = "NOME_CLIENTE")
    private String nomeCliente;
    @Column(name = "FILIAL")
    private Long filial;
    @Column(name = "LBCC")
    private String lbcc;

    public PmpClientePl() {
    }

    public PmpClientePl(String serie) {
        this.serie = serie;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Long getFilial() {
        return filial;
    }

    public void setFilial(Long filial) {
        this.filial = filial;
    }

    public String getLbcc() {
		return lbcc;
	}

	public void setLbcc(String lbcc) {
		this.lbcc = lbcc;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (serie != null ? serie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PmpClientePl)) {
            return false;
        }
        PmpClientePl other = (PmpClientePl) object;
        if ((this.serie == null && other.serie != null) || (this.serie != null && !this.serie.equals(other.serie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.treinamento.entity.PmpClientePl[ serie=" + serie + " ]";
    }
    
}
