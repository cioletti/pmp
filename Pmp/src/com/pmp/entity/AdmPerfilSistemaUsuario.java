/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "ADM_PERFIL_SISTEMA_USUARIO")

public class AdmPerfilSistemaUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id 
    @Column(name = "ID", nullable = false)
    @SequenceGenerator(name = "SEQ", sequenceName = "ADM_PERFIL_SISTEMA_USUARIO_SEQ" )
    @GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ") 
     private Long id;
    @JoinColumn(name = "ID_TW_USUARIO", referencedColumnName = "EPIDNO")
    @ManyToOne
    private TwFuncionario idTwUsuario;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "MATRICULA")
    @ManyToOne
    private AdmUsuario idUsuario;
    @JoinColumn(name = "ID_SISTEMA", referencedColumnName = "ID")
    @ManyToOne
    private AdmSistema idSistema;
    @JoinColumn(name = "ID_PERFIL", referencedColumnName = "ID")
    @ManyToOne
    private AdmPerfil idPerfil;

    public AdmPerfilSistemaUsuario() {
    }

    public AdmPerfilSistemaUsuario(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdmUsuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(AdmUsuario idUsuario) {
        this.idUsuario = idUsuario;
    }

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

    public TwFuncionario getIdTwUsuario() {
		return idTwUsuario;
	}

	public void setIdTwUsuario(TwFuncionario idTwUsuario) {
		this.idTwUsuario = idTwUsuario;
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
        if (!(object instanceof AdmPerfilSistemaUsuario)) {
            return false;
        }
        AdmPerfilSistemaUsuario other = (AdmPerfilSistemaUsuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.controlPanel.entity.AdmPerfilSistemaUsuario[ id=" + id + " ]";
    }
    
}
