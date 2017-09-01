/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pmp.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_MAQUINA_PL")

public class PmpMaquinaPl implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
     
    @Id 
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NUMERO_SERIE")
    private String numeroSerie;
    @Column(name = "HORIMETRO")
    private Integer horimetro;
    @Column(name = "LATITUDE")
    private String latitude;
    @Column(name = "LONGITUDE")
    private String longitude;
    @Column(name = "DATA_ATUALIZACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;
    @Column(name = "MODELO")
    private String modelo;    
    @Column(name = "MAKE")
    private String make;
    @Column(name = "MODULE_CODE")
    private String moduleCode;
    @Column(name = "MODULE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date moduleTime;
    @Column(name = "RECEIVED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedTime;
    @Column(name = "MESSAGE_ID")
    private Long messageId;
    @Column(name = "MASTER_MSG_ID")
    private Long masterMsgId;
    @Column(name = "NICKNAME")
    private String nickname;

    public PmpMaquinaPl() {
    }

    public PmpMaquinaPl(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Integer getHorimetro() {
        return horimetro;
    }

    public void setHorimetro(Integer horimetro) {
        this.horimetro = horimetro;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public Date getModuleTime() {
        return moduleTime;
    }

    public void setModuleTime(Date moduleTime) {
        this.moduleTime = moduleTime;
    }

    public Date getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getMasterMsgId() {
        return masterMsgId;
    }

    public void setMasterMsgId(Long masterMsgId) {
        this.masterMsgId = masterMsgId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
        if (!(object instanceof PmpMaquinaPl)) {
            return false;
        }
        PmpMaquinaPl other = (PmpMaquinaPl) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pmp.entity.PmpMaquinaPl[ id=" + id + " ]";
    }
    
}
