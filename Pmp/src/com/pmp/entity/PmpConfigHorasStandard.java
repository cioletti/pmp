/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_CONFIG_HORAS_STANDARD")

public class PmpConfigHorasStandard implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id 
    @Column(name = "ID", nullable = false)
    @SequenceGenerator(name = "SEQ", sequenceName = "PMP_CONF_HORAS_ST_SEQ" )
    @GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ") 
    private Long id;
    @Column(name = "HORAS")
    private Long horas;
    @Column(name = "HORAS_REVISAO")
    private Long horasRevisao;
    @Column(name = "STANDARD_JOB_CPTCD")
    private String standardJobCptcd;
    @JoinColumn(name = "ID_CONFIG_MANUTENCAO", referencedColumnName = "ID")
    @ManyToOne
    private PmpConfigManutencao idConfigManutencao;

    public PmpConfigHorasStandard() {
    }

    public PmpConfigHorasStandard(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHoras() {
        return horas;
    }

    public void setHoras(Long horas) {
        this.horas = horas;
    }

    public String getStandardJobCptcd() {
        return standardJobCptcd;
    }

    public void setStandardJobCptcd(String standardJobCptcd) {
        this.standardJobCptcd = standardJobCptcd;
    }

    public PmpConfigManutencao getIdConfigManutencao() {
        return idConfigManutencao;
    }

    public void setIdConfigManutencao(PmpConfigManutencao idConfigManutencao) {
        this.idConfigManutencao = idConfigManutencao;
    }


	public Long getHorasRevisao() {
		return horasRevisao;
	}

	public void setHorasRevisao(Long horasRevisao) {
		this.horasRevisao = horasRevisao;
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
        if (!(object instanceof PmpConfigHorasStandard)) {
            return false;
        }
        PmpConfigHorasStandard other = (PmpConfigHorasStandard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpConfigHorasStandard[ id=" + id + " ]";
    }
    
}
