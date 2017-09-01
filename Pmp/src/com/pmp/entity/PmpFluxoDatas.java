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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Rodrigo
 */
@Entity
@Table(name = "PMP_FLUXO_DATAS")
public class PmpFluxoDatas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    private Long id;
    @Column(name = "DATA")
    private String data;
    @Column(name = "COLUNA")
    private String coluna;
    @Column(name = "HAS_SEND_DBS")
    private String hasSendDbs;
    @JoinColumn(name = "ID_AGENDAMENTO", referencedColumnName = "ID")
    @ManyToOne
    private PmpAgendamento idAgendamento;

    public PmpFluxoDatas() {
    }

    public PmpFluxoDatas(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getColuna() {
        return coluna;
    }

    public void setColuna(String coluna) {
        this.coluna = coluna;
    }

    public String getHasSendDbs() {
        return hasSendDbs;
    }

    public void setHasSendDbs(String hasSendDbs) {
        this.hasSendDbs = hasSendDbs;
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
        if (!(object instanceof PmpFluxoDatas)) {
            return false;
        }
        PmpFluxoDatas other = (PmpFluxoDatas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpFluxoDatas[ id=" + id + " ]";
    }
    
}
