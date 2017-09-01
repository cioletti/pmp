/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author RDR
 */
@Entity
@Table(name = "PMP_DETALHES_VEICULOS")
@XmlRootElement
public class PmpDetalhesVeiculos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "FROTA")
    private String frota;
    @Column(name = "ID_FILIAL")
    private Long idFilial;
    @Column(name = "CHASSI")
    private String chassi;
    @Column(name = "PLACA")
    private String placa;
    @Column(name = "MODELO")
    private String modelo;
    @Column(name = "RENAVAN")
    private String renavan;
    @Column(name = "ANO_VEICULO")
    private String anoVeiculo;
    @Column(name = "DATA_COMPRA")
    private String dataCompra;
    @Column(name = "KM_ATUAL_DATA")
    private String kmAtualData;
    @Column(name = "VIA_FACIL")
    private String viaFacil;
    @Column(name = "GOODCAR")
    private String goodcar;
    @Column(name = "VISTORIA_CRONO")
    private String vistoriaCrono;
    @Column(name = "PI")
    private Long pi;
    @Column(name = "TROCA_PNEU_DIANTEIRO")
    private String trocaPneuDianteiro;
    @Column(name = "TROCA_PNEU_TRASEIRO")
    private String trocaPneuTraseiro;
    @Column(name = "NOTEBOOK")
    private String notebook;
    @JoinColumn(name = "ID_TW_FUNCIONARIO", referencedColumnName = "EPIDNO")
    @ManyToOne
    private TwFuncionario responsavel;

    public PmpDetalhesVeiculos() {
    }

    public PmpDetalhesVeiculos(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrota() {
        return frota;
    }

    public void setFrota(String frota) {
        this.frota = frota;
    }

    public Long getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Long idFilial) {
        this.idFilial = idFilial;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getRenavan() {
        return renavan;
    }

    public void setRenavan(String renavan) {
        this.renavan = renavan;
    }

    public String getAnoVeiculo() {
        return anoVeiculo;
    }

    public void setAnoVeiculo(String anoVeiculo) {
        this.anoVeiculo = anoVeiculo;
    }

    public String getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(String dataCompra) {
        this.dataCompra = dataCompra;
    }

    public String getKmAtualData() {
        return kmAtualData;
    }

    public void setKmAtualData(String kmAtualData) {
        this.kmAtualData = kmAtualData;
    }

    public String getViaFacil() {
        return viaFacil;
    }

    public void setViaFacil(String viaFacil) {
        this.viaFacil = viaFacil;
    }

    public String getGoodcar() {
        return goodcar;
    }

    public void setGoodcar(String goodcar) {
        this.goodcar = goodcar;
    }

    public String getVistoriaCrono() {
        return vistoriaCrono;
    }

    public void setVistoriaCrono(String vistoriaCrono) {
        this.vistoriaCrono = vistoriaCrono;
    }

    public Long getPi() {
        return pi;
    }

    public void setPi(Long pi) {
        this.pi = pi;
    }

    public String getTrocaPneuDianteiro() {
        return trocaPneuDianteiro;
    }

    public void setTrocaPneuDianteiro(String trocaPneuDianteiro) {
        this.trocaPneuDianteiro = trocaPneuDianteiro;
    }

    public String getTrocaPneuTraseiro() {
        return trocaPneuTraseiro;
    }

    public void setTrocaPneuTraseiro(String trocaPneuTraseiro) {
        this.trocaPneuTraseiro = trocaPneuTraseiro;
    }

    public String getNotebook() {
        return notebook;
    }

    public void setNotebook(String notebook) {
        this.notebook = notebook;
    }

    public TwFuncionario getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(TwFuncionario responsavel) {
        this.responsavel = responsavel;
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
        if (!(object instanceof PmpDetalhesVeiculos)) {
            return false;
        }
        PmpDetalhesVeiculos other = (PmpDetalhesVeiculos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.crm.entity.PmpDetalhesVeiculos[ id=" + id + " ]";
    }
    
}
