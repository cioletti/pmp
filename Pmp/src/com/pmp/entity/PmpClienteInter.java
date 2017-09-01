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
 * @author RDR
 */
@Entity
@Table(name = "PMP_CLIENTE_INTER")

public class PmpClienteInter implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id 
    @Column(name = "ID", nullable = false)
    @SequenceGenerator(name = "SEQ", sequenceName = "PMP_CLIENTE_INTER_SEQ" )
    @GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ") 
    private Long id;
    @Column(name = "FK_FILIAL")
    private Long fkFilial;
    @Column(name = "CUSTOMER_NUM")
    private String customerNum;
    @Column(name = "SEARCH_KEY")
    private String searchKey;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "COD")
    private Long cod;
    @Column(name = "FILIAL_DESC")
    private String filialDesc;

    public PmpClienteInter() {
    }

    public PmpClienteInter(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFkFilial() {
        return fkFilial;
    }

    public void setFkFilial(Long fkFilial) {
        this.fkFilial = fkFilial;
    }

    public String getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(String customerNum) {
        this.customerNum = customerNum;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getCod() {
        return cod;
    }

    public void setCod(Long cod) {
        this.cod = cod;
    }

    public String getFilialDesc() {
        return filialDesc;
    }

    public void setFilialDesc(String filialDesc) {
        this.filialDesc = filialDesc;
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
        if (!(object instanceof PmpClienteInter)) {
            return false;
        }
        PmpClienteInter other = (PmpClienteInter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpClienteInter[ id=" + id + " ]";
    }
    
}
