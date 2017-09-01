/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "USUARIO")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")})
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsuarioPK usuarioPK;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "LOGIN")
    private String login;
    @Column(name = "SENHA")
    private String senha;
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
//    @JoinColumn(name = "PERFIL_ID_PERFIL", referencedColumnName = "ID_PERFIL", insertable = false, updatable = false)
//    @ManyToOne(optional = false)
//    private Perfil perfil;
//    @JoinColumn(name = "ID_SISTEMA", referencedColumnName = "ID")
   // @ManyToOne
   // private AdmSistema idSistema;
   // @JoinColumn(name = "ID_PERFIL", referencedColumnName = "ID")
   // @ManyToOne
  //  private AdmPerfil idPerfil;

    public Usuario() {
    }

    public Usuario(UsuarioPK usuarioPK) {
        this.usuarioPK = usuarioPK;
    }

    public Usuario(String matricula, long perfilIdPerfil) {
        this.usuarioPK = new UsuarioPK(matricula, perfilIdPerfil);
    }

    public UsuarioPK getUsuarioPK() {
        return usuarioPK;
    }

    public void setUsuarioPK(UsuarioPK usuarioPK) {
        this.usuarioPK = usuarioPK;
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

//    public Perfil getPerfil() {
//        return perfil;
//    }
//
//    public void setPerfil(Perfil perfil) {
//        this.perfil = perfil;
//    }
    /**
    public AdmSistema getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(AdmSistema idSistema) {
        this.idSistema = idSistema;
    }

    public AdmPerfil getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(AdmPerfil idPerfil) {
        this.idPerfil = idPerfil;
    }
*/
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuarioPK != null ? usuarioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.usuarioPK == null && other.usuarioPK != null) || (this.usuarioPK != null && !this.usuarioPK.equals(other.usuarioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.Usuario[ usuarioPK=" + usuarioPK + " ]";
    }
    
}
