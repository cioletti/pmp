/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_DATA_AGENDAMENTO")
@NamedQueries({
    @NamedQuery(name = "PmpDataAgendamento.findAll", query = "SELECT p FROM PmpDataAgendamento p")})
public class PmpDataAgendamento implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "DATA_AGENDAMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAgendamento;
    @JoinColumn(name = "ID_FK_AGENDAMENTO", referencedColumnName = "ID")
    @ManyToOne
    private PmpAgendamento idFkAgendamento;

    public PmpDataAgendamento() {
    }

    public PmpDataAgendamento(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Date getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(Date dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public PmpAgendamento getIdFkAgendamento() {
        return idFkAgendamento;
    }

    public void setIdFkAgendamento(PmpAgendamento idFkAgendamento) {
        this.idFkAgendamento = idFkAgendamento;
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
        if (!(object instanceof PmpDataAgendamento)) {
            return false;
        }
        PmpDataAgendamento other = (PmpDataAgendamento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpDataAgendamento[ id=" + id + " ]";
    }
    
}
