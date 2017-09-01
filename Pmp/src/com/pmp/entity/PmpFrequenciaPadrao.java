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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_FREQUENCIA_PADRAO")

public class PmpFrequenciaPadrao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "HORAS")
    private Long horas;
    @Column(name = "STANDARD")
    private String standard;
    @Column(name = "SEQUENCIA")
    private Long sequencia;
    @Column(name = "JOB_CODE")
    private Long jobCode;
    @JoinColumn(name = "ID_TIPO_FREQUENCIA", referencedColumnName = "ID")
    @ManyToOne
    private PmpTipoFrequencia idTipoFrequencia;
    @Column(name = "HORAS_REVISAO")
    private Long horasRevisao;

    public PmpFrequenciaPadrao() {
    }

    public PmpFrequenciaPadrao(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHoras() {
        return horas;
    }

    public void setHoras(Long horas) {
        this.horas = horas;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public Long getSequencia() {
        return sequencia;
    }

    public void setSequencia(Long sequencia) {
        this.sequencia = sequencia;
    }

    public Long getJobCode() {
        return jobCode;
    }

    public void setJobCode(Long jobCode) {
        this.jobCode = jobCode;
    }

    public PmpTipoFrequencia getIdTipoFrequencia() {
        return idTipoFrequencia;
    }

    public void setIdTipoFrequencia(PmpTipoFrequencia idTipoFrequencia) {
        this.idTipoFrequencia = idTipoFrequencia;
    }

    public Long getHorasRevisao() {
		return horasRevisao;
	}

	public void setHorasRevisao(Long horasRevisao) {
		this.horasRevisao = horasRevisao;
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
        if (!(object instanceof PmpFrequenciaPadrao)) {
            return false;
        }
        PmpFrequenciaPadrao other = (PmpFrequenciaPadrao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpFrequenciaPadrao[ id=" + id + " ]";
    }
    
}
