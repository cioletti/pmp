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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "ADM_SISTEMA")
@NamedQueries({
    @NamedQuery(name = "AdmSistema.findAll", query = "SELECT a FROM AdmSistema a")})
public class AdmSistema implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "SIGLA")
    private String sigla;
    @Column(name = "CONTEXT")
    private String context;
    @Column(name = "IMG")
    private String img;
    @OneToMany(mappedBy = "idSistema")
    private Collection<AdmPerfilSistemaUsuario> admPerfilSistemaUsuarioCollection;

    public AdmSistema() {
    }

    public AdmSistema(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Collection<AdmPerfilSistemaUsuario> getAdmPerfilSistemaUsuarioCollection() {
        return admPerfilSistemaUsuarioCollection;
    }

    public void setAdmPerfilSistemaUsuarioCollection(Collection<AdmPerfilSistemaUsuario> admPerfilSistemaUsuarioCollection) {
        this.admPerfilSistemaUsuarioCollection = admPerfilSistemaUsuarioCollection;
    }

    public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
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
        if (!(object instanceof AdmSistema)) {
            return false;
        }
        AdmSistema other = (AdmSistema) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.controlPanel.entity.AdmSistema[ id=" + id + " ]";
    }
    
}
