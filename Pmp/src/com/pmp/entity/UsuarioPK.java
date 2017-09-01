/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author RDR
 */
@Embeddable
public class UsuarioPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "MATRICULA")
    private String matricula;
    @Basic(optional = false)
    @Column(name = "PERFIL_ID_PERFIL")
    private long perfilIdPerfil;

    public UsuarioPK() {
    }

    public UsuarioPK(String matricula, long perfilIdPerfil) {
        this.matricula = matricula;
        this.perfilIdPerfil = perfilIdPerfil;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public long getPerfilIdPerfil() {
        return perfilIdPerfil;
    }

    public void setPerfilIdPerfil(long perfilIdPerfil) {
        this.perfilIdPerfil = perfilIdPerfil;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matricula != null ? matricula.hashCode() : 0);
        hash += (int) perfilIdPerfil;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioPK)) {
            return false;
        }
        UsuarioPK other = (UsuarioPK) object;
        if ((this.matricula == null && other.matricula != null) || (this.matricula != null && !this.matricula.equals(other.matricula))) {
            return false;
        }
        if (this.perfilIdPerfil != other.perfilIdPerfil) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.UsuarioPK[ matricula=" + matricula + ", perfilIdPerfil=" + perfilIdPerfil + " ]";
    }
    
}
