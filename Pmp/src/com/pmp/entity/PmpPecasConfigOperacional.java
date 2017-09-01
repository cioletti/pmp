/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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
 * @author Elizangela
 */
@Entity
@Table(name = "PMP_PECAS_CONFIG_OPERACIONAL")
public class PmpPecasConfigOperacional implements Serializable {
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
    @Column(name = "BECTYC")
    private String bectyc;
    @Column(name = "SOS")
    private String sos;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "VLSSUB")
    private BigDecimal vlssub;
    @Column(name = "VLSUB")
    private BigDecimal vlsub;
    @JoinColumn(name = "ID_CONTRATO", referencedColumnName = "ID")
    @ManyToOne
    private PmpContrato idContrato;
    @Column(name = "CPTCD")
    private String cptcd;

    public PmpPecasConfigOperacional() {
    }

    public PmpPecasConfigOperacional(Long id) {
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

    public String getBectyc() {
        return bectyc;
    }

    public void setBectyc(String bectyc) {
        this.bectyc = bectyc;
    }

    public String getSos() {
        return sos;
    }

    public void setSos(String sos) {
        this.sos = sos;
    }

    public BigDecimal getVlssub() {
        return vlssub;
    }

    public void setVlssub(BigDecimal vlssub) {
        this.vlssub = vlssub;
    }

    public BigDecimal getVlsub() {
        return vlsub;
    }

    public void setVlsub(BigDecimal vlsub) {
        this.vlsub = vlsub;
    }

    public PmpContrato getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(PmpContrato idContrato) {
        this.idContrato = idContrato;
    }

    public String getCptcd() {
		return cptcd;
	}

	public void setCptcd(String cptcd) {
		this.cptcd = cptcd;
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
        if (!(object instanceof PmpPecasConfigOperacional)) {
            return false;
        }
        PmpPecasConfigOperacional other = (PmpPecasConfigOperacional) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpPecasConfigOperacional[ id=" + id + " ]";
    }
    
}
