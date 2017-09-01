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
@Table(name = "PMP_CONTRATO_CUSTOMIZACAO_MONITORAMENTO")

public class PmpContratoCustomizacaoMonitoramento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "ID_TIPO_CUSTOMIZACAO", referencedColumnName = "ID")
    @ManyToOne
    private PmpTipoCustomizacao idTipoCustomizacao;
    @JoinColumn(name = "ID_CONTRATO", referencedColumnName = "ID")
    @ManyToOne
    private PmpContratoMonitoramento idContrato;

    public PmpContratoCustomizacaoMonitoramento() {
    }

    public PmpContratoCustomizacaoMonitoramento(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PmpTipoCustomizacao getIdTipoCustomizacao() {
        return idTipoCustomizacao;
    }

    public void setIdTipoCustomizacao(PmpTipoCustomizacao idTipoCustomizacao) {
        this.idTipoCustomizacao = idTipoCustomizacao;
    }

    public PmpContratoMonitoramento getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(PmpContratoMonitoramento idContrato) {
        this.idContrato = idContrato;
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
        if (!(object instanceof PmpContratoCustomizacaoMonitoramento)) {
            return false;
        }
        PmpContratoCustomizacaoMonitoramento other = (PmpContratoCustomizacaoMonitoramento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpContratoCustomizacao[ id=" + id + " ]";
    }
    
}
