/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pmp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "PMP_FINANCEIRO")
@NamedQueries({
    @NamedQuery(name = "PmpFinanceiro.findAll", query = "SELECT p FROM PmpFinanceiro p")})
public class PmpFinanceiro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "CLIENTE")
    private String cliente;
    @Column(name = "COD_CLIENTE")
    private String codCliente;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CREDITO_LIBERADO")
    private BigDecimal creditoLiberado;
    @Column(name = "ID_FUNCIONARIO")
    private String idFuncionario;
    @Column(name = "STATUS")
    private String status;
    @Lob
    @Column(name = "OBSERVACAO")
    private String observacao;
    @Column(name = "NUMERO_OS")
    private String numeroOs;
    @Column(name = "ID_FUNCIONARIO_CRIADOR")
    private String idFuncionarioCriador;
    @Column(name = "DATA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Column(name = "DATA_LIBERACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataLiberacao;
    @Column(name = "FILIAL")
    private Long filial;
    @Column(name = "NOME_FILIAL")
    private String nomeFilial;
    @Lob
    @Column(name = "OBSERVACAO_LIBERACAO")
    private String observacaoLiberacao;
    @Column(name = "VALOR_LIBERADO")
    private BigDecimal valorLiberado;
    @Column(name = "DATA_REJEICAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRejeicao;
    @JoinColumn(name = "ID_CONT_HORAS_STANDARD", referencedColumnName = "ID")
    @ManyToOne
    private PmpContHorasStandard idContHorasStandard;
    @JoinColumn(name = "ID_CONT_HORAS_STANDARD_PLUS", referencedColumnName = "ID")
    @ManyToOne
    private PmpContHorasStandardPlus idContHorasStandardPlus;
    @Column(name = "VALOR_ESTIMADO")
    private BigDecimal valorEstimado;
    @Column(name = "ID_FUNCIONARIO_RESPONSAVEL")
    private String idFuncionarioResponsavel;
    @Column(name = "FUNCIONARIO_RESPONSAVEL")
    private BigDecimal funcionarioResponsavel;
    
    @Column(name = "DEPOSITO_CLIENTE")
    private BigDecimal depositoCliente;
    
    
    @Column(name = "CRD_DBS")
    private String crdDbs;
    
    @Column(name = "IS_FIND_CRD_DBS")
    private String isFindCrdDbs;
    
    @Column(name = "COND_PAG")
    private String condPag;
    
    @Column(name = "TERMS_COD")
    private String termsCod;
    
    @Column(name = "MOTIVO_SOLICITACAO")
    private String motivoSolicitacao;

    public PmpFinanceiro() {
    }

    public PmpFinanceiro(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public BigDecimal getCreditoLiberado() {
        return creditoLiberado;
    }

    public void setCreditoLiberado(BigDecimal creditoLiberado) {
        this.creditoLiberado = creditoLiberado;
    }

    public String getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(String idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getNumeroOs() {
        return numeroOs;
    }

    public void setNumeroOs(String numeroOs) {
        this.numeroOs = numeroOs;
    }

    public String getIdFuncionarioCriador() {
        return idFuncionarioCriador;
    }

    public void setIdFuncionarioCriador(String idFuncionarioCriador) {
        this.idFuncionarioCriador = idFuncionarioCriador;
    }

    public Long getFilial() {
        return filial;
    }

    public void setFilial(Long filial) {
        this.filial = filial;
    }

    public String getNomeFilial() {
        return nomeFilial;
    }

    public void setNomeFilial(String nomeFilial) {
        this.nomeFilial = nomeFilial;
    }

    public String getObservacaoLiberacao() {
        return observacaoLiberacao;
    }

    public void setObservacaoLiberacao(String observacaoLiberacao) {
        this.observacaoLiberacao = observacaoLiberacao;
    }

    public BigDecimal getValorLiberado() {
        return valorLiberado;
    }

    public void setValorLiberado(BigDecimal valorLiberado) {
        this.valorLiberado = valorLiberado;
    }

    public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataLiberacao() {
		return dataLiberacao;
	}

	public void setDataLiberacao(Date dataLiberacao) {
		this.dataLiberacao = dataLiberacao;
	}

	public Date getDataRejeicao() {
		return dataRejeicao;
	}

	public void setDataRejeicao(Date dataRejeicao) {
		this.dataRejeicao = dataRejeicao;
	}

	public BigDecimal getValorEstimado() {
		return valorEstimado;
	}

	public void setValorEstimado(BigDecimal valorEstimado) {
		this.valorEstimado = valorEstimado;
	}

	public PmpContHorasStandard getIdContHorasStandard() {
		return idContHorasStandard;
	}

	public void setIdContHorasStandard(PmpContHorasStandard idContHorasStandard) {
		this.idContHorasStandard = idContHorasStandard;
	}

	public PmpContHorasStandardPlus getIdContHorasStandardPlus() {
		return idContHorasStandardPlus;
	}

	public void setIdContHorasStandardPlus(PmpContHorasStandardPlus idContHorasStandardPlus) {
		this.idContHorasStandardPlus = idContHorasStandardPlus;
	}

	public String getIdFuncionarioResponsavel() {
		return idFuncionarioResponsavel;
	}

	public void setIdFuncionarioResponsavel(String idFuncionarioResponsavel) {
		this.idFuncionarioResponsavel = idFuncionarioResponsavel;
	}

	public BigDecimal getFuncionarioResponsavel() {
		return funcionarioResponsavel;
	}

	public void setFuncionarioResponsavel(BigDecimal funcionarioResponsavel) {
		this.funcionarioResponsavel = funcionarioResponsavel;
	}

	public String getCrdDbs() {
		return crdDbs;
	}

	public void setCrdDbs(String crdDbs) {
		this.crdDbs = crdDbs;
	}

	public String getIsFindCrdDbs() {
		return isFindCrdDbs;
	}

	public void setIsFindCrdDbs(String isFindCrdDbs) {
		this.isFindCrdDbs = isFindCrdDbs;
	}

	public String getCondPag() {
		return condPag;
	}

	public void setCondPag(String condPag) {
		this.condPag = condPag;
	}

	public String getTermsCod() {
		return termsCod;
	}

	public void setTermsCod(String termsCod) {
		this.termsCod = termsCod;
	}

	public String getMotivoSolicitacao() {
		return motivoSolicitacao;
	}

	public void setMotivoSolicitacao(String motivoSolicitacao) {
		this.motivoSolicitacao = motivoSolicitacao;
	}

	public BigDecimal getDepositoCliente() {
		return depositoCliente;
	}

	public void setDepositoCliente(BigDecimal depositoCliente) {
		this.depositoCliente = depositoCliente;
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
        if (!(object instanceof PmpFinanceiro)) {
            return false;
        }
        PmpFinanceiro other = (PmpFinanceiro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication4.PmpFinanceiro[ id=" + id + " ]";
    }
    
}
