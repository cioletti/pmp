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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_CONT_MESES_STANDARD")
@NamedQueries({
    @NamedQuery(name = "PmpContMesesStandard.findAll", query = "SELECT p FROM PmpContMesesStandard p")})
public class PmpContMesesStandard implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @Column(name = "ID", nullable = false)
    @SequenceGenerator(name = "SEQ", sequenceName = "PMP_CONT_MESES_STANDARD_SEQ" )
    @GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ") 
    private Long id;
    @Column(name = "MES")
    private Long mes;
    @Column(name = "STANDARD_JOB_CPTCD")
    private String standardJobCptcd;
    @Column(name = "FREQUENCIA")
    private Long frequencia;
    @Basic(optional = false)
    @Column(name = "IS_EXECUTADO")
    private String isExecutado;
    @Column(name = "MES_MANUTENCAO")
    private Long mesManutencao;
    @JoinColumn(name = "ID_OS_OPERACIONAL", referencedColumnName = "ID")
    @ManyToOne
    private PmpOsOperacional idOsOperacional;
    @JoinColumn(name = "ID_CONTRATO", referencedColumnName = "ID")
    @ManyToOne
    private PmpContrato idContrato;

    public PmpContMesesStandard() {
    }

    public PmpContMesesStandard(Long id) {
        this.id = id;
    }

    public PmpContMesesStandard(Long id, String isExecutado) {
        this.id = id;
        this.isExecutado = isExecutado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMes() {
        return mes;
    }

    public void setMes(Long mes) {
        this.mes = mes;
    }

    public String getStandardJobCptcd() {
        return standardJobCptcd;
    }

    public void setStandardJobCptcd(String standardJobCptcd) {
        this.standardJobCptcd = standardJobCptcd;
    }

    public Long getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(Long frequencia) {
        this.frequencia = frequencia;
    }

    public String getIsExecutado() {
        return isExecutado;
    }

    public void setIsExecutado(String isExecutado) {
        this.isExecutado = isExecutado;
    }

    public Long getMesManutencao() {
        return mesManutencao;
    }

    public void setMesManutencao(Long mesManutencao) {
        this.mesManutencao = mesManutencao;
    }

    public PmpOsOperacional getIdOsOperacional() {
        return idOsOperacional;
    }

    public void setIdOsOperacional(PmpOsOperacional idOsOperacional) {
        this.idOsOperacional = idOsOperacional;
    }

    public PmpContrato getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(PmpContrato idContrato) {
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
        if (!(object instanceof PmpContMesesStandard)) {
            return false;
        }
        PmpContMesesStandard other = (PmpContMesesStandard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpContMesesStandard[ id=" + id + " ]";
    }
    
}
