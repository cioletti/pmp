/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_CONTRATO")
public class PmpContrato implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "CODIGO_CLIENTE")
    private String codigoCliente;
    @Column(name = "NOME_CLIENTE")
    private String nomeCliente;
    @Column(name = "TA")
    private String ta;
    @Column(name = "NUMERO_CONTRATO")
    private String numeroContrato;
    @Column(name = "BGRP")
    private String bgrp;
    @Column(name = "ID_FUNCIONARIO")
    private String idFuncionario;
    @Column(name = "HORIMETRO")
    private BigInteger horimetro;
    @Column(name = "NUMERO_SERIE")
    private String numeroSerie;
    @Column(name = "MODELO")
    private String modelo;
    @Column(name = "CIDADE")
    private String cidade;
    @Column(name = "ENDERECO")
    private String endereco;
    @Column(name = "BAIRRO")
    private String bairro;
    @Column(name = "TELEFONE_COMERCIAL")
    private String telefoneComercial;
    @Column(name = "DATA_CRIACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @Column(name = "DATA_REJEICAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRejeicao;
    @Column(name = "FILIAL")
    private Integer filial;
    @Column(name = "RAZAO_SOCIAL")
    private String razaoSocial;
    @Column(name = "REGIONAL")
    private String regional;
    @Column(name = "CNPJ")
    private String cnpj;
    @Column(name = "INSC_ESTADUAL")
    private String inscEstadual;
    @Column(name = "INSC_MUNICIPAL")
    private String inscMunicipal;
    @Column(name = "CONTRIBUINTE")
    private String contribuinte;
    @Column(name = "PROCURADOR")
    private String procurador;
    @Column(name = "HAS_SEND_EMAIL")
    private String hasSendEmail;
    @Column(name = "CPF")
    private String cpf;
    @Column(name = "CONTATO_COMERCIAL")
    private String contatoComercial;
    @Column(name = "EMAIL_CONTATO_COMERCIAL")
    private String emailContatoComercial;
    @Column(name = "CONTATO_SERVICOS")
    private String contatoServicos;
    @Column(name = "EMAIL_CONTATO_SERVICOS")
    private String emailContatoServicos;
    @Column(name = "FINANCIADO")
    private String financiado;
    @Column(name = "TELEFONE_SERVICOS")
    private String telefoneServicos;
    @Column(name = "FAX_COMERCIAL")
    private String faxComercial;
    @Column(name = "FAX_SERVICOS")
    private String faxServicos;
    @Column(name = "FAMILIA")
    private String familia;
    @Column(name = "ID_FAMILIA")
    private Long idFamilia;
    @Column(name = "DATA_ACEITE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAceite;
    @Column(name = "QTD_PARCELAS")
    private Long qtdParcelas;
    @Column(name = "CONDICAO_EXCEPCIONAL")
    private String condicaoExcepcional;
    @Column(name = "BEGIN_RANGER")
    private String beginRanger;
    @Column(name = "IS_ATIVO")
    private String isAtivo;
    @Column(name = "END_RANGER")
    private String endRanger;
    @Column(name = "PREFIXO")
    private String prefixo;
    @Column(name = "CONT_EXCESSAO")
    private String contExcessao;
    @Column(name = "NUM_OS")
    private String numOs;
    @Column(name = "UF")
    private String uf;
    @Column(name = "CEP")
    private String cep;
    @Column(name = "VALO_CONTRATO")
    private BigDecimal valoContrato;
    @Column(name = "VALOR_SUGERIDO")
    private BigDecimal valorSugerido;
    @Column(name = "VALOR_CONESSAO")
    private BigDecimal valoConcessao;
    @Column(name = "DISTANCIA_GERADOR")
    private BigDecimal distanciaGerador;
    @Column(name = "VALOR_CUSTO")
    private BigDecimal valorCusto;
    @JoinColumn(name = "ID_TIPO_CONTRATO", referencedColumnName = "ID")
    @ManyToOne
    private PmpTipoContrato idTipoContrato;
    @JoinColumn(name = "ID_STATUS_CONTRATO", referencedColumnName = "ID")
    @ManyToOne
    private PmpStatusContrato idStatusContrato;
    @JoinColumn(name = "ID_MOTIVO_NAO_FEC_CONTRATO", referencedColumnName = "ID")
    @ManyToOne
    private PmpMotivosNaoFecContrato idMotivoNaoFecContrato;
    @JoinColumn(name = "ID_CONFIG_MANUTENCAO", referencedColumnName = "ID")
    @ManyToOne
    private PmpConfigManutencao idConfigManutencao;
    @JoinColumn(name = "ID_CENTRO_DE_CUSTO", referencedColumnName = "ID")
    @ManyToOne
    private PmpCentroDeCusto idCentroDeCusto;
    @OneToMany(mappedBy = "idContrato")
    private Collection<PmpConfigOperacional> pmpConfigOperacionalCollection;
    @OneToMany(mappedBy = "idContrato")
    private Collection<PmpContHorasStandard> pmpContHorasStandardCollection;
    @JoinColumn(name = "ID_CONFIG_TRACAO", referencedColumnName = "ID")
    @ManyToOne
    private PmpConfigTracao idConfigTracao;
    @Column(name = "IS_FIND_SUB_TRIBUTARIA")
    private String isFindSubTributaria;
    @Column(name = "NUM_DOC_DBS")
    private String numDocDbs;
    @Column(name = "COD_ERRO_DBS")
    private String codErroDbs;
    @Column(name = "MSG_ERRO_DBS")
    private String msgErroDbs;
    @Column(name = "FABRICANTE")
    private String fabricante;
    @Column(name = "NOME_INDICACAO")
    private String nomeIndicacao;
    @Column(name = "ID_FUNCIONARIO_INDICACAO")
    private String idFuncionarioIndicacao;
    @Column(name = "PRINT_REVISAO_POS_PAGO")
    private BigDecimal printRevisaoPosPago;
    @Column(name = "COMISSAO_CONSULTOR")
    private BigDecimal comissaoConsultor;
    @Column(name = "COMISSAO_INDICACAO")
    private BigDecimal comissaoindicacao;
    @Column(name = "EMAIL_CHECKLIST")
    private String emailChecklist;
    @JoinColumn(name = "ID_CLASSIFICACAO_CONTRATO", referencedColumnName = "ID")
    @ManyToOne
    private PmpClassificacaoContrato idClassificacaoContrato;
    @Column(name = "IS_TERRITORIO")
    private String isTerritorio;
    @Column(name = "ANOS_CONTRATO")
    private Long anoContrato;
    @Column(name = "MEDIA_HORAS_MES")
    private Long mediaHorasMes;
    @Column(name = "POSSUI_HORAS_MES")
    private String possuiHorasMes; 
    @Column(name = "IS_SPOT")
    private String isSpot; 
    
    @Column(name = "DATA_FINALIZACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFinalizacao;
    
    @Column(name = "ID_FUNCIONARIO_FINALIZACAO")
    private String idFuncionarioFinalizacao;
    
    
    @Column(name = "HORIMETRO_ULTIMA_REVISAO")
    private Long horimetroUltimaRevisao;
    
    @Column(name = "DESCONTO_PDP_SPOT")
    private BigDecimal descontoPdpSpot;
    
    @Column(name = "QTD_HORAS_DESL_SPOT")
    private String qtdHorasDeslSpot;
    
    
    
    public PmpContrato() {
    }

    public PmpContrato(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getTa() {
        return ta;
    }

    public void setTa(String ta) {
        this.ta = ta;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public String getBgrp() {
        return bgrp;
    }

    public void setBgrp(String bgrp) {
        this.bgrp = bgrp;
    }

    public String getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(String idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public BigInteger getHorimetro() {
        return horimetro;
    }

    public void setHorimetro(BigInteger horimetro) {
        this.horimetro = horimetro;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getTelefoneComercial() {
        return telefoneComercial;
    }

    public void setTelefoneComercial(String telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataRejeicao() {
        return dataRejeicao;
    }

    public void setDataRejeicao(Date dataRejeicao) {
        this.dataRejeicao = dataRejeicao;
    }

    public Integer getFilial() {
        return filial;
    }

    public void setFilial(Integer filial) {
        this.filial = filial;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getRegional() {
        return regional;
    }

    public void setRegional(String regional) {
        this.regional = regional;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscEstadual() {
        return inscEstadual;
    }

    public void setInscEstadual(String inscEstadual) {
        this.inscEstadual = inscEstadual;
    }

    public String getInscMunicipal() {
        return inscMunicipal;
    }

    public void setInscMunicipal(String inscMunicipal) {
        this.inscMunicipal = inscMunicipal;
    }

    public String getContribuinte() {
        return contribuinte;
    }

    public void setContribuinte(String contribuinte) {
        this.contribuinte = contribuinte;
    }

    public String getProcurador() {
        return procurador;
    }

    public void setProcurador(String procurador) {
        this.procurador = procurador;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getContatoComercial() {
        return contatoComercial;
    }

    public void setContatoComercial(String contatoComercial) {
        this.contatoComercial = contatoComercial;
    }

    public String getEmailContatoComercial() {
        return emailContatoComercial;
    }

    public void setEmailContatoComercial(String emailContatoComercial) {
        this.emailContatoComercial = emailContatoComercial;
    }

    public String getContatoServicos() {
        return contatoServicos;
    }

    public void setContatoServicos(String contatoServicos) {
        this.contatoServicos = contatoServicos;
    }

    public String getEmailContatoServicos() {
        return emailContatoServicos;
    }

    public void setEmailContatoServicos(String emailContatoServicos) {
        this.emailContatoServicos = emailContatoServicos;
    }

    public String getFinanciado() {
        return financiado;
    }

    public void setFinanciado(String financiado) {
        this.financiado = financiado;
    }

    public String getTelefoneServicos() {
        return telefoneServicos;
    }

    public void setTelefoneServicos(String telefoneServicos) {
        this.telefoneServicos = telefoneServicos;
    }

    public String getFaxComercial() {
        return faxComercial;
    }

    public void setFaxComercial(String faxComercial) {
        this.faxComercial = faxComercial;
    }

    public String getFaxServicos() {
        return faxServicos;
    }

    public void setFaxServicos(String faxServicos) {
        this.faxServicos = faxServicos;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public Date getDataAceite() {
        return dataAceite;
    }

    public void setDataAceite(Date dataAceite) {
        this.dataAceite = dataAceite;
    }

    public Long getQtdParcelas() {
        return qtdParcelas;
    }

    public void setQtdParcelas(Long qtdParcelas) {
        this.qtdParcelas = qtdParcelas;
    }

    public String getCondicaoExcepcional() {
        return condicaoExcepcional;
    }

    public void setCondicaoExcepcional(String condicaoExcepcional) {
        this.condicaoExcepcional = condicaoExcepcional;
    }

    public String getBeginRanger() {
        return beginRanger;
    }

    public void setBeginRanger(String beginRanger) {
        this.beginRanger = beginRanger;
    }

    public String getEndRanger() {
        return endRanger;
    }

    public void setEndRanger(String endRanger) {
        this.endRanger = endRanger;
    }

    public BigDecimal getValoContrato() {
        return valoContrato;
    }

    public void setValoContrato(BigDecimal valoContrato) {
        this.valoContrato = valoContrato;
    }

    public PmpTipoContrato getIdTipoContrato() {
        return idTipoContrato;
    }

    public void setIdTipoContrato(PmpTipoContrato idTipoContrato) {
        this.idTipoContrato = idTipoContrato;
    }

    public PmpStatusContrato getIdStatusContrato() {
        return idStatusContrato;
    }

    public void setIdStatusContrato(PmpStatusContrato idStatusContrato) {
        this.idStatusContrato = idStatusContrato;
    }

    public PmpMotivosNaoFecContrato getIdMotivoNaoFecContrato() {
        return idMotivoNaoFecContrato;
    }

    public void setIdMotivoNaoFecContrato(PmpMotivosNaoFecContrato idMotivoNaoFecContrato) {
        this.idMotivoNaoFecContrato = idMotivoNaoFecContrato;
    }

    public PmpConfigManutencao getIdConfigManutencao() {
        return idConfigManutencao;
    }

    public void setIdConfigManutencao(PmpConfigManutencao idConfigManutencao) {
        this.idConfigManutencao = idConfigManutencao;
    }

    public PmpCentroDeCusto getIdCentroDeCusto() {
        return idCentroDeCusto;
    }

    public void setIdCentroDeCusto(PmpCentroDeCusto idCentroDeCusto) {
        this.idCentroDeCusto = idCentroDeCusto;
    }

    public Collection<PmpConfigOperacional> getPmpConfigOperacionalCollection() {
        return pmpConfigOperacionalCollection;
    }

    public void setPmpConfigOperacionalCollection(Collection<PmpConfigOperacional> pmpConfigOperacionalCollection) {
        this.pmpConfigOperacionalCollection = pmpConfigOperacionalCollection;
    }

    public Collection<PmpContHorasStandard> getPmpContHorasStandardCollection() {
        return pmpContHorasStandardCollection;
    }

    public void setPmpContHorasStandardCollection(Collection<PmpContHorasStandard> pmpContHorasStandardCollection) {
        this.pmpContHorasStandardCollection = pmpContHorasStandardCollection;
    }

    public String getPrefixo() {
		return prefixo;
	}

	public void setPrefixo(String prefixo) {
		this.prefixo = prefixo;
	}

	public String getContExcessao() {
		return contExcessao;
	}

	public void setContExcessao(String contExcessao) {
		this.contExcessao = contExcessao;
	}

	public String getHasSendEmail() {
		return hasSendEmail;
	}

	public void setHasSendEmail(String hasSendEmail) {
		this.hasSendEmail = hasSendEmail;
	}

	public String getNumOs() {
		return numOs;
	}

	public void setNumOs(String numOs) {
		this.numOs = numOs;
	}

	public BigDecimal getValorCusto() {
		return valorCusto;
	}

	public void setValorCusto(BigDecimal valorCusto) {
		this.valorCusto = valorCusto;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public BigDecimal getValoConcessao() {
		return valoConcessao;
	}

	public void setValoConcessao(BigDecimal valoConcessao) {
		this.valoConcessao = valoConcessao;
	}
	
	public BigDecimal getDistanciaGerador() {
		return distanciaGerador;
	}
	
	public void setDistanciaGerador(BigDecimal distanciaGerador) {
		this.distanciaGerador = distanciaGerador;
	}

	public Long getIdFamilia() {
		return idFamilia;
	}

	public void setIdFamilia(Long idFamilia) {
		this.idFamilia = idFamilia;
	}

	public String getIsAtivo() {
		return isAtivo;
	}

	public void setIsAtivo(String isAtivo) {
		this.isAtivo = isAtivo;
	}

	public String getIsFindSubTributaria() {
		return isFindSubTributaria;
	}

	public void setIsFindSubTributaria(String isFindSubTributaria) {
		this.isFindSubTributaria = isFindSubTributaria;
	}

	public String getNumDocDbs() {
		return numDocDbs;
	}

	public void setNumDocDbs(String numDocDbs) {
		this.numDocDbs = numDocDbs;
	}

	public String getCodErroDbs() {
		return codErroDbs;
	}

	public void setCodErroDbs(String codErroDbs) {
		this.codErroDbs = codErroDbs;
	}

	public String getMsgErroDbs() {
		return msgErroDbs;
	}

	public void setMsgErroDbs(String msgErroDbs) {
		this.msgErroDbs = msgErroDbs;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public PmpConfigTracao getIdConfigTracao() {
		return idConfigTracao;
	}

	public void setIdConfigTracao(PmpConfigTracao idConfigTracao) {
		this.idConfigTracao = idConfigTracao;
	}

	public BigDecimal getValorSugerido() {
		return valorSugerido;
	}

	public void setValorSugerido(BigDecimal valorSugerido) {
		this.valorSugerido = valorSugerido;
	}

	public String getNomeIndicacao() {
		return nomeIndicacao;
	}

	public void setNomeIndicacao(String nomeIndicacao) {
		this.nomeIndicacao = nomeIndicacao;
	}

	public String getIdFuncionarioIndicacao() {
		return idFuncionarioIndicacao;
	}

	public void setIdFuncionarioIndicacao(String idFuncionarioIndicacao) {
		this.idFuncionarioIndicacao = idFuncionarioIndicacao;
	}

	public BigDecimal getPrintRevisaoPosPago() {
		return printRevisaoPosPago;
	}

	public void setPrintRevisaoPosPago(BigDecimal printRevisaoPosPago) {
		this.printRevisaoPosPago = printRevisaoPosPago;
	}

	public BigDecimal getComissaoConsultor() {
		return comissaoConsultor;
	}

	public void setComissaoConsultor(BigDecimal comissaoConsultor) {
		this.comissaoConsultor = comissaoConsultor;
	}

	public BigDecimal getComissaoindicacao() {
		return comissaoindicacao;
	}

	public void setComissaoindicacao(BigDecimal comissaoindicacao) {
		this.comissaoindicacao = comissaoindicacao;
	}

	public String getEmailChecklist() {
		return emailChecklist;
	}

	public void setEmailChecklist(String emailChecklist) {
		this.emailChecklist = emailChecklist;
	}

	public PmpClassificacaoContrato getIdClassificacaoContrato() {
		return idClassificacaoContrato;
	}

	public void setIdClassificacaoContrato(PmpClassificacaoContrato idClassificacaoContrato) {
		this.idClassificacaoContrato = idClassificacaoContrato;
	}

	public String getIsTerritorio() {
		return isTerritorio;
	}

	public void setIsTerritorio(String isTerritorio) {
		this.isTerritorio = isTerritorio;
	}

	public Long getAnoContrato() {
		return anoContrato;
	}

	public void setAnoContrato(Long anoContrato) {
		this.anoContrato = anoContrato;
	}

	public Long getMediaHorasMes() {
		return mediaHorasMes;
	}

	public void setMediaHorasMes(Long mediaHorasMes) {
		this.mediaHorasMes = mediaHorasMes;
	}

	public String getPossuiHorasMes() {
		return possuiHorasMes;
	}

	public void setPossuiHorasMes(String possuiHorasMes) {
		this.possuiHorasMes = possuiHorasMes;
	}

	public String getIsSpot() {
		return isSpot;
	}

	public void setIsSpot(String isSpot) {
		this.isSpot = isSpot;
	}

	public Date getDataFinalizacao() {
		return dataFinalizacao;
	}

	public void setDataFinalizacao(Date dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	public String getIdFuncionarioFinalizacao() {
		return idFuncionarioFinalizacao;
	}

	public void setIdFuncionarioFinalizacao(String idFuncionarioFinalizacao) {
		this.idFuncionarioFinalizacao = idFuncionarioFinalizacao;
	}

	public Long getHorimetroUltimaRevisao() {
		return horimetroUltimaRevisao;
	}

	public void setHorimetroUltimaRevisao(Long horimetroUltimaRevisao) {
		this.horimetroUltimaRevisao = horimetroUltimaRevisao;
	}

	public BigDecimal getDescontoPdpSpot() {
		return descontoPdpSpot;
	}

	public void setDescontoPdpSpot(BigDecimal descontoPdpSpot) {
		this.descontoPdpSpot = descontoPdpSpot;
	}



	public String getQtdHorasDeslSpot() {
		return qtdHorasDeslSpot;
	}

	public void setQtdHorasDeslSpot(String qtdHorasDeslSpot) {
		this.qtdHorasDeslSpot = qtdHorasDeslSpot;
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
        if (!(object instanceof PmpContrato)) {
            return false;
        }
        PmpContrato other = (PmpContrato) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpContrato[ id=" + id + " ]";
    }
}