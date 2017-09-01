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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_CONFIG_OPERACIONAL_MONITORAMENTO")
public class PmpConfigOperacionalMonitoramento implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id 
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NUM_OS")
    private String numOs;
    @Column(name = "ID_FUNC_SUPERVISOR")
    private String idFuncSupervisor;
    @Column(name = "CONTATO")
    private String contato;
    @Column(name = "LOCAL")
    private String local;
    @Column(name = "TELEFONE_CONTATO")
    private String telefoneContato;
    @Column(name = "COD_ERRO_DBS")
    private String codErroDbs;
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
    @Column(name = "OBS")
    private String obs;
    @JoinColumn(name = "ID_CONTRATO", referencedColumnName = "ID")
    @ManyToOne
    private PmpContratoMonitoramento idContrato;
    @Column(name = "FILIAL")
    private Long filial;

    public PmpConfigOperacionalMonitoramento() {
    }

    public PmpConfigOperacionalMonitoramento(Long id) {
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

    public String getIdFuncSupervisor() {
        return idFuncSupervisor;
    }

    public void setIdFuncSupervisor(String idFuncSupervisor) {
        this.idFuncSupervisor = idFuncSupervisor;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getTelefoneContato() {
        return telefoneContato;
    }

    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }

    public PmpContratoMonitoramento getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(PmpContratoMonitoramento idContrato) {
        this.idContrato = idContrato;
    }

    public String getCodErroDbs() {
		return codErroDbs;
	}

	public void setCodErroDbs(String codErroDbs) {
		this.codErroDbs = codErroDbs;
	}

	public Long getFilial() {
		return filial;
	}

	public void setFilial(Long filial) {
		this.filial = filial;
	}

	public String getNumeroSegmento() {
		return numeroSegmento;
	}

	public void setNumeroSegmento(String numeroSegmento) {
		this.numeroSegmento = numeroSegmento;
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

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
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
        if (!(object instanceof PmpConfigOperacionalMonitoramento)) {
            return false;
        }
        PmpConfigOperacionalMonitoramento other = (PmpConfigOperacionalMonitoramento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpConfigOperacional[ id=" + id + " ]";
    }
    
}
