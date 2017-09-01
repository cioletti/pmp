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
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_FILE_ET")

public class PmpFileEt implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id 
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    
    private Long id;
    @Lob
    @Column(name = "FILE_ET")
    private Serializable fileEt;
    @Column(name = "ID_OS_PALM")
    private Long idOsPalm;

    public PmpFileEt() {
    }

    public PmpFileEt(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Serializable getFileEt() {
        return fileEt;
    }

    public void setFileEt(Serializable fileEt) {
        this.fileEt = fileEt;
    }

    public Long getIdOsPalm() {
        return idOsPalm;
    }

    public void setIdOsPalm(Long idOsPalm) {
        this.idOsPalm = idOsPalm;
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
        if (!(object instanceof PmpFileEt)) {
            return false;
        }
        PmpFileEt other = (PmpFileEt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpFileEt[ id=" + id + " ]";
    }
    
}
