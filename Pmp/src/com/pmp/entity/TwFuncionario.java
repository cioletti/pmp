/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;



/**
 *
 * @author RDR
 */
@Entity
@Table(name = "TW_FUNCIONARIO")

public class TwFuncionario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "EPLSNM")
    private String eplsnm;
    @Id
    @Basic(optional = false)
    @Column(name = "EPIDNO")
    private String epidno;
    @Column(name = "SHFCD")
    private String shfcd;
    @Column(name = "EMPCL0")
    private String empcl0;
    @Column(name = "EMCC")
    private String emcc;
    @Column(name = "STN1")
    private String stn1;
    @Column(name = "EMPCL1")
    private String empcl1;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "TELEFONE")
    private String telefone;
    @Column(name = "CARGO")
    private String cargo;
    @Column(name = "LOGIN")
    private String login;
    @Column(name = "SENHA")
    private String senha;
    @Column(name = "CPF")
    private String cpf;
    @Column(name = "PLACA_VEICULO")
    private String placaVeiculo;
    @Column(name = "ESTIMATEBY")
    private String estimateBy;
    @Column(name = "TURNO")
    private Long turno;
    @Column(name = "TIPO_VEICULO")
    private String tipoVeiculo;
    @Column(name = "CODIGO_VEICULO")
    private String codigoVeiculo;
    @Column(name = "EPIDNO_SUPERVISOR")
    private String epidnoSupervisor;
    @Column(name = "ID_FORNECEDOR")
    private Long idFornecedor;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "twFuncionario1")
    private TwFuncionario twFuncionario;
    @JoinColumn(name = "EPIDNO", referencedColumnName = "EPIDNO", insertable = false, updatable = false)
    @OneToOne(optional = true)
    private TwFuncionario twFuncionario1;
    @JoinColumn(name = "ID_CARGO", referencedColumnName = "ID")
    @ManyToOne

    
    private TrCargo idTrCargo;
    @Column(name = "CENTRO_CUSTO")
    private String centroCusto;
    @Column(name = "DESCRICAO_CENTRO_CUSTO")
    private String descricaoCentroCusto;

    public TrCargo getIdTrCargo() {
		return idTrCargo;
	}

	public void setIdTrCargo(TrCargo idTrCargo) {
		this.idTrCargo = idTrCargo;
	}

	public TwFuncionario() {
    }

    public TwFuncionario(String epidno) {
        this.epidno = epidno;
    }

    public String getEplsnm() {
        return eplsnm;
    }

    public void setEplsnm(String eplsnm) {
        this.eplsnm = eplsnm;
    }

    public String getEpidno() {
        return epidno;
    }

    public void setEpidno(String epidno) {
        this.epidno = epidno;
    }

    public String getShfcd() {
        return shfcd;
    }

    public void setShfcd(String shfcd) {
        this.shfcd = shfcd;
    }

    public String getEmpcl0() {
        return empcl0;
    }

    public void setEmpcl0(String empcl0) {
        this.empcl0 = empcl0;
    }

    public String getEmcc() {
        return emcc;
    }

    public void setEmcc(String emcc) {
        this.emcc = emcc;
    }

    public String getStn1() {
        return stn1;
    }

    public void setStn1(String stn1) {
        this.stn1 = stn1;
    }

    public String getEmpcl1() {
        return empcl1;
    }

    public void setEmpcl1(String empcl1) {
        this.empcl1 = empcl1;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEpidnoSupervisor() {
        return epidnoSupervisor;
    }

    public void setEpidnoSupervisor(String epidnoSupervisor) {
        this.epidnoSupervisor = epidnoSupervisor;
    }

    public TwFuncionario getTwFuncionario() {
        return twFuncionario;
    }

    public void setTwFuncionario(TwFuncionario twFuncionario) {
        this.twFuncionario = twFuncionario;
    }

    public TwFuncionario getTwFuncionario1() {
        return twFuncionario1;
    }

    public void setTwFuncionario1(TwFuncionario twFuncionario1) {
        this.twFuncionario1 = twFuncionario1;
    }

    public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEstimateBy() {
		return estimateBy;
	}

	public void setEstimateBy(String estimateBy) {
		this.estimateBy = estimateBy;
	}

	public Long getTurno() {
		return turno;
	}

	public void setTurno(Long turno) {
		this.turno = turno;
	}

	public String getPlacaVeiculo() {
		return placaVeiculo;
	}

	public void setPlacaVeiculo(String placaVeiculo) {
		this.placaVeiculo = placaVeiculo;
	}


	public Long getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public String getTipoVeiculo() {
		return tipoVeiculo;
	}

	public void setTipoVeiculo(String tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
	}

	public String getCodigoVeiculo() {
		return codigoVeiculo;
	}

	public void setCodigoVeiculo(String codigoVeiculo) {
		this.codigoVeiculo = codigoVeiculo;
	}

	public String getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(String centroCusto) {
		this.centroCusto = centroCusto;
	}

	public String getDescricaoCentroCusto() {
		return descricaoCentroCusto;
	}

	public void setDescricaoCentroCusto(String descricaoCentroCusto) {
		this.descricaoCentroCusto = descricaoCentroCusto;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (epidno != null ? epidno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TwFuncionario)) {
            return false;
        }
        TwFuncionario other = (TwFuncionario) object;
        if ((this.epidno == null && other.epidno != null) || (this.epidno != null && !this.epidno.equals(other.epidno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.TwFuncionario[ epidno=" + epidno + " ]";
    }
    
}
