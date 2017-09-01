/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "ADM_USUARIO")

public class AdmUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "LOGIN")
    private String login;
    @Column(name = "SENHA")
    private String senha;
    @Id
    @Basic(optional = false)
    @Column(name = "MATRICULA")
    private String matricula;
    @Column(name = "FONE")
    private String fone;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "SMS")
    private String sms;
    @Column(name = "CAMPO")
    private String campo;
    @Column(name = "FILIAL")
    private String filial;
    @Column(name = "PLACA")
    private String placa;
    @Column(name = "USERAG")
    private String userag;
    @Column(name = "USERMOB")
    private String usermob;
    @Column(name = "WEB")
    private String web;
    @Column(name = "CARGO")
    private String cargo;
    @OneToMany(mappedBy = "idUsuario")
    private Collection<AdmPerfilSistemaUsuario> admPerfilSistemaUsuarioCollection;

    public AdmUsuario() {
    }

    public AdmUsuario(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getFilial() {
        return filial;
    }

    public void setFilial(String filial) {
        this.filial = filial;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getUserag() {
        return userag;
    }

    public void setUserag(String userag) {
        this.userag = userag;
    }

    public String getUsermob() {
        return usermob;
    }

    public void setUsermob(String usermob) {
        this.usermob = usermob;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public Collection<AdmPerfilSistemaUsuario> getAdmPerfilSistemaUsuarioCollection() {
        return admPerfilSistemaUsuarioCollection;
    }

    public void setAdmPerfilSistemaUsuarioCollection(Collection<AdmPerfilSistemaUsuario> admPerfilSistemaUsuarioCollection) {
        this.admPerfilSistemaUsuarioCollection = admPerfilSistemaUsuarioCollection;
    }

    public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (matricula != null ? matricula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdmUsuario)) {
            return false;
        }
        AdmUsuario other = (AdmUsuario) object;
        if ((this.matricula == null && other.matricula != null) || (this.matricula != null && !this.matricula.equals(other.matricula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.controlPanel.entity.AdmUsuario[ matricula=" + matricula + " ]";
    }
    
}
