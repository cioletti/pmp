/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author RODRIGO
 */
@Entity
@Table(name = "PMP_AGENDAMENTO_PENDENTE")
public class PmpAgendamentoPendente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CONTRATO")
    private Long idContrato;
    
    @Column(name = "SERIE")
    private String serie;
    @Column(name = "HORIMETRO_ULTIMA_REVISAO")
    private String horimetroUltimaRevisao;
    @Column(name = "HORIMETRO_PROXIMA_REVISAO")
    private String horimetroProximaRevisao;
    @Column(name = "HORIMETRO_FALTANTE")
    private String horimetroFaltante;
    
    @Column(name = "NUMERO_CONTRATO")
    private String numeroContrato;
    @Column(name = "MODELO")
    private String modelo;
    @Column(name = "NUM_OS")
    private String numOs;
    @Column(name = "HORIMETRO")
    private Long horimetro;
    @Column(name = "FILIAL")
    private Long filial;
    @Column(name = "HORAS_REVISAO")
    private Long horasRevisao;
    @Column(name = "HORAS_PENDENTES")
    private Long horasPendentes;
    @Column(name = "CODIGO_CLIENTE")
    private String codigoCliente;
    @Column(name = "ID_CONT_HORAS_STANDARD")
    private Long idContHorasStandard;
    @Column(name = "DATA_AGENDAMENTO")
    private String dataAgendamento;
    @Column(name = "STANDER_JOB")
    private String standerJob;
    @Lob
    @Column(name = "SIGLA_TIPO_CONTRATO")
    private String siglaTipoContrato;
    @Column(name = "FILIAL_DESTINO")
    private String filialDestino;
    @Column(name = "RAZAO_SOCIAL")
    private String razaoSocial;
    @Column(name = "LOCAL")
    private String local;
    @Column(name = "SIGLA_CLASSIFICACAO_CONTRATO")
    private String siglaClassificacaoContrato;
    
    

    @Column(name = "ID_OS_OPERACIONAL")
    private Long idOsOperacional;
    
    @Column(name = "CONTATO")
    private String contato;
    
    @Column(name = "TELEFONE_CONTATO")
    private String telefoneContato;
    
    
    @Column(name = "OBS")
    private String obs;
    
    
    
    @Column(name = "DATA_ATUALIZACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;
    public PmpAgendamentoPendente() {
    }

    public PmpAgendamentoPendente(String serie) {
        this.serie = serie;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getHorimetroUltimaRevisao() {
        return horimetroUltimaRevisao;
    }

    public void setHorimetroUltimaRevisao(String horimetroUltimaRevisao) {
        this.horimetroUltimaRevisao = horimetroUltimaRevisao;
    }

    public String getHorimetroProximaRevisao() {
        return horimetroProximaRevisao;
    }

    public void setHorimetroProximaRevisao(String horimetroProximaRevisao) {
        this.horimetroProximaRevisao = horimetroProximaRevisao;
    }

    public String getHorimetroFaltante() {
        return horimetroFaltante;
    }

    public void setHorimetroFaltante(String horimetroFaltante) {
        this.horimetroFaltante = horimetroFaltante;
    }

    public Long getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Long idContrato) {
        this.idContrato = idContrato;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumOs() {
        return numOs;
    }

    public void setNumOs(String numOs) {
        this.numOs = numOs;
    }

    public Long getHorimetro() {
        return horimetro;
    }

    public void setHorimetro(Long horimetro) {
        this.horimetro = horimetro;
    }

    public Long getFilial() {
        return filial;
    }

    public void setFilial(Long filial) {
        this.filial = filial;
    }

    public Long getHorasRevisao() {
        return horasRevisao;
    }

    public void setHorasRevisao(Long horasRevisao) {
        this.horasRevisao = horasRevisao;
    }

    public Long getHorasPendentes() {
        return horasPendentes;
    }

    public void setHorasPendentes(Long horasPendentes) {
        this.horasPendentes = horasPendentes;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public Long getIdContHorasStandard() {
        return idContHorasStandard;
    }

    public void setIdContHorasStandard(Long idContHorasStandard) {
        this.idContHorasStandard = idContHorasStandard;
    }

    public String getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(String dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public String getStanderJob() {
        return standerJob;
    }

    public void setStanderJob(String standerJob) {
        this.standerJob = standerJob;
    }

    public String getSiglaTipoContrato() {
        return siglaTipoContrato;
    }

    public void setSiglaTipoContrato(String siglaTipoContrato) {
        this.siglaTipoContrato = siglaTipoContrato;
    }

    public String getFilialDestino() {
        return filialDestino;
    }

    public void setFilialDestino(String filialDestino) {
        this.filialDestino = filialDestino;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getSiglaClassificacaoContrato() {
        return siglaClassificacaoContrato;
    }

    public void setSiglaClassificacaoContrato(String siglaClassificacaoContrato) {
        this.siglaClassificacaoContrato = siglaClassificacaoContrato;
    }

    public Long getIdOsOperacional() {
		return idOsOperacional;
	}

	public void setIdOsOperacional(Long idOsOperacional) {
		this.idOsOperacional = idOsOperacional;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public String getTelefoneContato() {
		return telefoneContato;
	}

	public void setTelefoneContato(String telefoneContato) {
		this.telefoneContato = telefoneContato;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (serie != null ? serie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PmpAgendamentoPendente)) {
            return false;
        }
        PmpAgendamentoPendente other = (PmpAgendamentoPendente) object;
        if ((this.serie == null && other.serie != null) || (this.serie != null && !this.serie.equals(other.serie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.PmpAgendamentoPendente[ serie=" + serie + " ]";
    }
    
}
