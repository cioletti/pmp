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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_CONFIG_MANUTENCAO")

public class PmpConfigManutencao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "MODELO")
    private String modelo;
    @Column(name = "PREFIXO")
    private String prefixo;
    @Column(name = "BGRP")
    private String bgrp;
    @Column(name = "BEGINRANGE")
    private String beginrange;
    @Column(name = "ENDRANGE")
    private String endrange;
    @Column(name = "DATA_CONFIGURACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataConfiguracao;
    @Column(name = "ID_FUNCIONARIO")
    private String idFuncionario;
    @Column(name = "CONT_EXCESSAO")
    private String contExcessao;
    @Column(name = "TIPO_PRECO")
    private String tipoPreco;
    @Column(name = "IS_GERADOR_STANDBY")
    private String isGeradorStandby;
    @OneToMany(mappedBy = "idConfigManutencao")
    private Collection<PmpConfigHorasStandard> pmpConfigHorasStandardCollection;
    @OneToMany(mappedBy = "idConfigManutencao")
    private Collection<PmpContrato> pmpContratoCollection;
    @JoinColumn(name = "ID_CONFIGURACAO_PRECO", referencedColumnName = "ID")
    @ManyToOne
    private PmpConfiguracaoPrecos idConfiguracaoPreco;
    @JoinColumn(name = "ID_FAMILIA", referencedColumnName = "ID")
    @ManyToOne
    private PmpFamilia idFamilia;
    @Column(name = "IS_ATIVO")
    private String isAtivo;
    @Column(name = "ID_ANTIGO")
    private Long idAntigo;
    
    

    public String getIsAtivo() {
		return isAtivo;
	}

	public void setIsAtivo(String isAtivo) {
		this.isAtivo = isAtivo;
	}

	public PmpConfigManutencao() {
    }

    public PmpConfigManutencao(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPrefixo() {
        return prefixo;
    }

    public void setPrefixo(String prefixo) {
        this.prefixo = prefixo;
    }

    public String getBgrp() {
        return bgrp;
    }

    public void setBgrp(String bgrp) {
        this.bgrp = bgrp;
    }

    public String getBeginrange() {
        return beginrange;
    }

    public void setBeginrange(String beginrange) {
        this.beginrange = beginrange;
    }

    public String getEndrange() {
        return endrange;
    }

    public void setEndrange(String endrange) {
        this.endrange = endrange;
    }

    public Date getDataConfiguracao() {
        return dataConfiguracao;
    }

    public void setDataConfiguracao(Date dataConfiguracao) {
        this.dataConfiguracao = dataConfiguracao;
    }

    public String getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(String idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getContExcessao() {
        return contExcessao;
    }

    public void setContExcessao(String contExcessao) {
        this.contExcessao = contExcessao;
    }

    public String getTipoPreco() {
		return tipoPreco;
	}

	public void setTipoPreco(String tipoPreco) {
		this.tipoPreco = tipoPreco;
	}

	public String getIsGeradorStandby() {
		return isGeradorStandby;
	}

	public void setIsGeradorStandby(String isGeradorStandby) {
		this.isGeradorStandby = isGeradorStandby;
	}

	public Collection<PmpConfigHorasStandard> getPmpConfigHorasStandardCollection() {
        return pmpConfigHorasStandardCollection;
    }

    public void setPmpConfigHorasStandardCollection(Collection<PmpConfigHorasStandard> pmpConfigHorasStandardCollection) {
        this.pmpConfigHorasStandardCollection = pmpConfigHorasStandardCollection;
    }

    public Collection<PmpContrato> getPmpContratoCollection() {
        return pmpContratoCollection;
    }

    public void setPmpContratoCollection(Collection<PmpContrato> pmpContratoCollection) {
        this.pmpContratoCollection = pmpContratoCollection;
    }

    public PmpConfiguracaoPrecos getIdConfiguracaoPreco() {
        return idConfiguracaoPreco;
    }

    public void setIdConfiguracaoPreco(PmpConfiguracaoPrecos idConfiguracaoPreco) {
        this.idConfiguracaoPreco = idConfiguracaoPreco;
    }

    public PmpFamilia getIdFamilia() {
		return idFamilia;
	}

	public void setIdFamilia(PmpFamilia idFamilia) {
		this.idFamilia = idFamilia;
	}

	public Long getIdAntigo() {
		return idAntigo;
	}

	public void setIdAntigo(Long idAntigo) {
		this.idAntigo = idAntigo;
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
        if (!(object instanceof PmpConfigManutencao)) {
            return false;
        }
        PmpConfigManutencao other = (PmpConfigManutencao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpConfigManutencao[ id=" + id + " ]";
    }
}