/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_OS_OPERACIONAL")

public class PmpOsOperacional implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NUM_OS")
    private String numOs;
    @Column(name = "DATA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Column(name = "FILIAL")
    private Long filial;
    @Column(name = "NUM_DOC")
    private String numDoc;
    @Column(name = "MSG")
    private String msg;
    @Column(name = "COD_ERRO_OS_DBS")
    private String codErroOsDbs;
    @Column(name = "COD_ERRO_DOC_DBS")
    private String codErroDocDbs;
    @Column(name = "NUMERO_SEGMENTO")
    private String numeroSegmento;
    @Column(name = "JOB_CODE")
    private String jobCode;
    @Column(name = "COMP_CODE")
    private String compCode;
    @Column(name = "CSCC")
    private String cscc;
    @Column(name = "IND")
    private String ind;
    @Column(name = "ID_FUNCIONARIO_CRIADOR_OS")
    private String idFuncionarioCriadorOs;
    @JoinColumn(name = "ID_CONT_HORAS_STANDARD", referencedColumnName = "ID")
    @ManyToOne
    private PmpContHorasStandard idContHorasStandard;
    @JoinColumn(name = "ID_CONFIG_OPERACIONAL", referencedColumnName = "ID")
    @ManyToOne
    private PmpConfigOperacional idConfigOperacional;
    @OneToMany(mappedBy = "idOsOperacional")
    private Collection<PmpContHorasStandard> pmpContHorasStandardCollection;
    @JoinColumn(name = "ID_CONT_HORAS_STANDARD_PLUS", referencedColumnName = "ID")
    @ManyToOne
    private PmpContHorasStandardPlus idContHorasStandardPlus;
    
	public PmpOsOperacional() {
		
	}
    public PmpOsOperacional(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumOs() {
        return numOs;
    }

    public void setNumOs(String numOs) {
        this.numOs = numOs;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Long getFilial() {
        return filial;
    }

    public void setFilial(Long filial) {
        this.filial = filial;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCodErroOsDbs() {
        return codErroOsDbs;
    }

    public void setCodErroOsDbs(String codErroOsDbs) {
        this.codErroOsDbs = codErroOsDbs;
    }

    public String getCodErroDocDbs() {
        return codErroDocDbs;
    }

    public void setCodErroDocDbs(String codErroDocDbs) {
        this.codErroDocDbs = codErroDocDbs;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getCscc() {
        return cscc;
    }

    public void setCscc(String cscc) {
        this.cscc = cscc;
    }

    public String getInd() {
        return ind;
    }

    public void setInd(String ind) {
        this.ind = ind;
    }

    public PmpContHorasStandard getIdContHorasStandard() {
        return idContHorasStandard;
    }

    public void setIdContHorasStandard(PmpContHorasStandard idContHorasStandard) {
        this.idContHorasStandard = idContHorasStandard;
    }

    public PmpConfigOperacional getIdConfigOperacional() {
        return idConfigOperacional;
    }

    public void setIdConfigOperacional(PmpConfigOperacional idConfigOperacional) {
        this.idConfigOperacional = idConfigOperacional;
    }

    public Collection<PmpContHorasStandard> getPmpContHorasStandardCollection() {
        return pmpContHorasStandardCollection;
    }

    public void setPmpContHorasStandardCollection(Collection<PmpContHorasStandard> pmpContHorasStandardCollection) {
        this.pmpContHorasStandardCollection = pmpContHorasStandardCollection;
    }

	public String getNumeroSegmento() {
		return numeroSegmento;
	}

	public void setNumeroSegmento(String numeroSegmento) {
		this.numeroSegmento = numeroSegmento;
	}

	public String getIdFuncionarioCriadorOs() {
		return idFuncionarioCriadorOs;
	}

	public void setIdFuncionarioCriadorOs(String idFuncionarioCriadorOs) {
		this.idFuncionarioCriadorOs = idFuncionarioCriadorOs;
	}

	public PmpContHorasStandardPlus getIdContHorasStandardPlus() {
		return idContHorasStandardPlus;
	}

	public void setIdContHorasStandardPlus(
			PmpContHorasStandardPlus idContHorasStandardPlus) {
		this.idContHorasStandardPlus = idContHorasStandardPlus;
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
        if (!(object instanceof PmpOsOperacional)) {
            return false;
        }
        PmpOsOperacional other = (PmpOsOperacional) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PmpOsOperacional[ id=" + id + " ]";
    }
    
}
