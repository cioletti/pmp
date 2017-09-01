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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author RDR Sistemas
 */
@Entity
@Table(name = "PMP_SMU_LOC")

public class PmpSmuLoc implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id 
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    private Long id;
    @Column(name = "BUFFER")
    private Long buffer;

    public PmpSmuLoc() {
    }

    public PmpSmuLoc(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuffer() {
        return buffer;
    }

    public void setBuffer(Long buffer) {
        this.buffer = buffer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PmpSmuLoc)) {
            return false;
        }
        PmpSmuLoc other = (PmpSmuLoc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpSmuLoc[ id=" + id + " ]";
    }
    
}
