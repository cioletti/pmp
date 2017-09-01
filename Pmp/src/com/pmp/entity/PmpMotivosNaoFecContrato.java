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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_MOTIVOS_NAO_FEC_CONTRATO")
@NamedQueries({
    @NamedQuery(name = "PmpMotivosNaoFecContrato.findAll", query = "SELECT p FROM PmpMotivosNaoFecContrato p")})
public class PmpMotivosNaoFecContrato implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "SIGLA")
    private String sigla;
    @OneToMany(mappedBy = "idMotivoNaoFecContrato")
    private Collection<PmpContrato> pmpContratoCollection;

    public PmpMotivosNaoFecContrato() {
    }

    public PmpMotivosNaoFecContrato(Long id) {
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

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Collection<PmpContrato> getPmpContratoCollection() {
        return pmpContratoCollection;
    }

    public void setPmpContratoCollection(Collection<PmpContrato> pmpContratoCollection) {
        this.pmpContratoCollection = pmpContratoCollection;
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
        if (!(object instanceof PmpMotivosNaoFecContrato)) {
            return false;
        }
        PmpMotivosNaoFecContrato other = (PmpMotivosNaoFecContrato) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpMotivosNaoFecContrato[ id=" + id + " ]";
    }
    
}
