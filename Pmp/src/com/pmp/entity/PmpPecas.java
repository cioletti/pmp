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
 * @author RDR
 */
@Entity
@Table(name = "PMP_PECAS")
public class PmpPecas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "QTD")
    private Long qtd;
    @Column(name = "NUM_PECA")
    private String numPeca;
    @Column(name = "NOME_PECA")
    private String nomePeca;
    @JoinColumn(name = "ID_AGENDAMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PmpAgendamento idAgendamento;

    public PmpPecas() {
    }

    public PmpPecas(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getQtd() {
        return qtd;
    }

    public void setQtd(Long qtd) {
        this.qtd = qtd;
    }

    public String getNumPeca() {
        return numPeca;
    }

    public void setNumPeca(String numPeca) {
        this.numPeca = numPeca;
    }

    public String getNomePeca() {
        return nomePeca;
    }

    public void setNomePeca(String nomePeca) {
        this.nomePeca = nomePeca;
    }

    public PmpAgendamento getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(PmpAgendamento idAgendamento) {
        this.idAgendamento = idAgendamento;
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
        if (!(object instanceof PmpPecas)) {
            return false;
        }
        PmpPecas other = (PmpPecas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpPecas[ id=" + id + " ]";
    }
    
}
