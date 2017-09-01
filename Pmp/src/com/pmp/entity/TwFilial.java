/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author sandro
 */
@Entity
@Table(name = "TW_FILIAL")
@NamedQueries({
    @NamedQuery(name = "TwFilial.findAll", query = "SELECT t FROM TwFilial t")})
public class TwFilial implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STNO")
    private Long stno;
    @Column(name = "STNM")
    private String stnm;
    @Column(name = "REGIONAL")
    private BigInteger regional;
    @Column(name = "DESCRICAO_STRATEC")
    private String descricaoStratec;
    @Column(name = "FILIAL_IMPORTACAO")
    private String filialImportacao;
    @Column(name = "CNPJ")
    private String cnpj;
    @Column(name = "RAZAO_SOCIAL")
    private String razaoSocial;
    @Column(name = "ENDERECO")
    private String endereco;
    @Column(name = "CEP")
    private String cep;
    

    public TwFilial() {
    }

    public TwFilial(Long stno) {
        this.stno = stno;
    }

    public Long getStno() {
        return stno;
    }

    public void setStno(Long stno) {
        this.stno = stno;
    }

    public String getStnm() {
        return stnm;
    }

    public void setStnm(String stnm) {
        this.stnm = stnm;
    }

    public BigInteger getRegional() {
        return regional;
    }

    public void setRegional(BigInteger regional) {
        this.regional = regional;
    }



    public String getDescricaoStratec() {
		return descricaoStratec;
	}

	public void setDescricaoStratec(String descricaoStratec) {
		this.descricaoStratec = descricaoStratec;
	}

	public String getFilialImportacao() {
		return filialImportacao;
	}

	public void setFilialImportacao(String filialImportacao) {
		this.filialImportacao = filialImportacao;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (stno != null ? stno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TwFilial)) {
            return false;
        }
        TwFilial other = (TwFilial) object;
        if ((this.stno == null && other.stno != null) || (this.stno != null && !this.stno.equals(other.stno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.treinamento.entity.TwFilial[ stno=" + stno + " ]";
    }
    
}
