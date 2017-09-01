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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "ARV_INSPECAO")
@NamedQueries({
    @NamedQuery(name = "ArvInspecao.findAll", query = "SELECT a FROM ArvInspecao a")})
public class ArvInspecao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @Column(name = "id_Arv", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idArv;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "SIM")
    private Character sim;
    @Column(name = "NAO")
    private Character nao;
    @Column(name = "ID_PAI_ROOT")
    private Long idPaiRoot;
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "SOS")
    private String sos;
    @Column(name = "SMCS")
    private String smcs;
    @Column(name = "TIPO_MANUTENCAO")
    private String tipoManutencao;
    @JoinColumn(name = "ID_FAMILIA", referencedColumnName = "ID")
    @ManyToOne
    private PmpFamilia idFamilia;
    @OneToMany(mappedBy = "idPai")
    private Collection<ArvInspecao> arvInspecaoCollection;
    @JoinColumn(name = "ID_PAI", referencedColumnName = "ID_ARV")
    @ManyToOne
    private ArvInspecao idPai;

    public ArvInspecao() {
    }

    public ArvInspecao(Long idArv) {
        this.idArv = idArv;
    }

    public Long getIdArv() {
        return idArv;
    }

    public void setIdArv(Long idArv) {
        this.idArv = idArv;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Character getSim() {
        return sim;
    }

    public void setSim(Character sim) {
        this.sim = sim;
    }

    public Character getNao() {
        return nao;
    }

    public void setNao(Character nao) {
        this.nao = nao;
    }

    public Long getIdPaiRoot() {
        return idPaiRoot;
    }

    public void setIdPaiRoot(Long idPaiRoot) {
        this.idPaiRoot = idPaiRoot;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoManutencao() {
        return tipoManutencao;
    }

    public void setTipoManutencao(String tipoManutencao) {
        this.tipoManutencao = tipoManutencao;
    }

    public PmpFamilia getIdFamilia() {
        return idFamilia;
    }

    public void setIdFamilia(PmpFamilia idFamilia) {
        this.idFamilia = idFamilia;
    }

    public Collection<ArvInspecao> getArvInspecaoCollection() {
        return arvInspecaoCollection;
    }

    public void setArvInspecaoCollection(Collection<ArvInspecao> arvInspecaoCollection) {
        this.arvInspecaoCollection = arvInspecaoCollection;
    }

    public ArvInspecao getIdPai() {
        return idPai;
    }

    public void setIdPai(ArvInspecao idPai) {
        this.idPai = idPai;
    }

    public String getSos() {
		return sos;
	}

	public void setSos(String sos) {
		this.sos = sos;
	}

	public String getSmcs() {
		return smcs;
	}

	public void setSmcs(String smcs) {
		this.smcs = smcs;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idArv != null ? idArv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ArvInspecao)) {
            return false;
        }
        ArvInspecao other = (ArvInspecao) object;
        if ((this.idArv == null && other.idArv != null) || (this.idArv != null && !this.idArv.equals(other.idArv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.ArvInspecao[ idArv=" + idArv + " ]";
    }
    
}
