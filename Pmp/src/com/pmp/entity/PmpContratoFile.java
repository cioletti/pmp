/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_CONTRATO_FILE")
@NamedQueries({
    @NamedQuery(name = "PmpContratoFile.findAll", query = "SELECT p FROM PmpContratoFile p")})
public class PmpContratoFile implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Lob
    @Column(name = "CONTRATO_CLIENTE")
    private Serializable contratoCliente;
    @Lob
    @Column(name = "CONTRATO_JURIDICO")
    private Serializable contratoJuridico;
    @OneToMany(mappedBy = "idContratoFile")
    private Collection<PmpTipoContrato> pmpTipoContratoCollection;

    public PmpContratoFile() {
    }

    public PmpContratoFile(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Serializable getContratoCliente() {
        return contratoCliente;
    }

    public void setContratoCliente(Serializable contratoCliente) {
        this.contratoCliente = contratoCliente;
    }

    public Serializable getContratoJuridico() {
        return contratoJuridico;
    }

    public void setContratoJuridico(Serializable contratoJuridico) {
        this.contratoJuridico = contratoJuridico;
    }

    public Collection<PmpTipoContrato> getPmpTipoContratoCollection() {
        return pmpTipoContratoCollection;
    }

    public void setPmpTipoContratoCollection(Collection<PmpTipoContrato> pmpTipoContratoCollection) {
        this.pmpTipoContratoCollection = pmpTipoContratoCollection;
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
        if (!(object instanceof PmpContratoFile)) {
            return false;
        }
        PmpContratoFile other = (PmpContratoFile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpContratoFile[ id=" + id + " ]";
    }
    
}
