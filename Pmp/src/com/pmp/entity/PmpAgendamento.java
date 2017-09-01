/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_AGENDAMENTO")
public class PmpAgendamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ID_FUNCIONARIO")
    private String idFuncionario;
    @Column(name = "ID_FUNC_SUPERVISOR")
    private String idFuncSupervisor;
    @Column(name = "ID_AJUDANTE")
    private String idAjudante;
    @Column(name = "DATA_AGENDAMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAgendamento;
    @Column(name = "DATA_FATURAMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFaturamento;
    @Column(name = "HORAS_REVISAO")
    private Long horasRevisao;
    @Column(name = "NUM_OS")
    private String numOs;
    @Column(name = "FILIAL")
    private Long filial;
    @Column(name = "OBS")
    private String obs;
    @Column(name = "TIPO_PM")
    private String tipoPm;
    @Column(name = "IS_FIND_TECNICO")
    private String isFindTecnico;
    @JoinColumn(name = "ID_STATUS_AGENDAMENTO", referencedColumnName = "ID")
    @ManyToOne
    private PmpStatusAgendamento idStatusAgendamento;
    @JoinColumn(name = "ID_CONT_HORAS_STANDARD", referencedColumnName = "ID")
    @ManyToOne
    private PmpContHorasStandard idContHorasStandard;
    @JoinColumn(name = "ID_CONG_OPERACIONAL", referencedColumnName = "ID")
    @ManyToOne
    private PmpConfigOperacional idCongOperacional;
    @JoinColumn(name = "ID_CONT_HORAS_STANDARD_PLUS", referencedColumnName = "ID")
    @ManyToOne
    private PmpContHorasStandardPlus idContHorasStandardPlus;
    @Column(name = "HORAS_AGENDADA")
    private BigDecimal horasAgendada;
    @Column(name = "OBS_TECNICO")
    private String obsTecnico;
    @Column(name = "ENCERRAR_AUTOMATICA")
    private String encerrarAutomatica;

    public PmpAgendamento() {
    }

    public PmpAgendamento(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(String idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getIdFuncSupervisor() {
        return idFuncSupervisor;
    }

    public void setIdFuncSupervisor(String idFuncSupervisor) {
        this.idFuncSupervisor = idFuncSupervisor;
    }

    public Date getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(Date dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public Long getHorasRevisao() {
        return horasRevisao;
    }

    public void setHorasRevisao(Long horasRevisao) {
        this.horasRevisao = horasRevisao;
    }

    public String getNumOs() {
        return numOs;
    }

    public void setNumOs(String numOs) {
        this.numOs = numOs;
    }

    public PmpStatusAgendamento getIdStatusAgendamento() {
        return idStatusAgendamento;
    }

    public void setIdStatusAgendamento(PmpStatusAgendamento idStatusAgendamento) {
        this.idStatusAgendamento = idStatusAgendamento;
    }

    public PmpContHorasStandard getIdContHorasStandard() {
        return idContHorasStandard;
    }

    public void setIdContHorasStandard(PmpContHorasStandard idContHorasStandard) {
        this.idContHorasStandard = idContHorasStandard;
    }

    public PmpConfigOperacional getIdCongOperacional() {
        return idCongOperacional;
    }

    public void setIdCongOperacional(PmpConfigOperacional idCongOperacional) {
        this.idCongOperacional = idCongOperacional;
    }

    public Long getFilial() {
		return filial;
	}

	public void setFilial(Long filial) {
		this.filial = filial;
	}

	public String getIsFindTecnico() {
		return isFindTecnico;
	}

	public void setIsFindTecnico(String isFindTecnico) {
		this.isFindTecnico = isFindTecnico;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Date getDataFaturamento() {
		return dataFaturamento;
	}

	public void setDataFaturamento(Date dataFaturamento) {
		this.dataFaturamento = dataFaturamento;
	}

	public String getTipoPm() {
		return tipoPm;
	}

	public void setTipoPm(String tipoPm) {
		this.tipoPm = tipoPm;
	}

	public String getIdAjudante() {
		return idAjudante;
	}

	public void setIdAjudante(String idAjudante) {
		this.idAjudante = idAjudante;
	}


	public BigDecimal getHorasAgendada() {
		return horasAgendada;
	}

	public void setHorasAgendadas(BigDecimal horasAgendada) {
		this.horasAgendada = horasAgendada;
	}

	public String getObsTecnico() {
		return obsTecnico;
	}

	public void setObsTecnico(String obsTecnico) {
		this.obsTecnico = obsTecnico;
	}

	public PmpContHorasStandardPlus getIdContHorasStandardPlus() {
		return idContHorasStandardPlus;
	}

	public void setIdContHorasStandardPlus(
			PmpContHorasStandardPlus idContHorasStandardPlus) {
		this.idContHorasStandardPlus = idContHorasStandardPlus;
	}

	public void setHorasAgendada(BigDecimal horasAgendada) {
		this.horasAgendada = horasAgendada;
	}

	public String getEncerrarAutomatica() {
		return encerrarAutomatica;
	}

	public void setEncerrarAutomatica(String encerrarAutomatica) {
		this.encerrarAutomatica = encerrarAutomatica;
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
        if (!(object instanceof PmpAgendamento)) {
            return false;
        }
        PmpAgendamento other = (PmpAgendamento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpAgendamento[ id=" + id + " ]";
    }

}
