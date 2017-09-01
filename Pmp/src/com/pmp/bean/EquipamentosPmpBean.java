package com.pmp.bean;

import java.math.BigDecimal;
import java.util.Date;

public class EquipamentosPmpBean {

	private String MODELO;
	private String NUMERO_SERIE;
	private String DIA;
	private String MES;
	private String ANO;
	private String NUMEROCONTRATO;
	private String CODIGOCLIENTE;
	private String RAZAOSOCIAL;
	private String PL;
	private String MESFIM;
	private String ANOFIM;
	private String NUMOS;
	private BigDecimal VALOR;
	private BigDecimal PARCELAS;
	private Date DATAACEITE;
	private String CONSULTOR;
	private BigDecimal FILIAL;
	private String TIPOCONTRATO;
	private String TA;
	private String ORIGEM;
	private String DESTINO;
	private BigDecimal REVPENDENTES;
	private BigDecimal HORIMETRO;
	private Date AT_HORIMETRO;
	private String TIPO_CONTRATO;
	private String DIAS_PENDENTES;
	private String PROXIMA_REVISAO;
	private String HORIMETRO_PENDENTE;
	
	private String HORIMETRO_ULTIMA_REVISAO;
	private String HORIMETRO_PROXIMA_REVISAO;
	private String HORIMETRO_FALTANTE;
	
	
	public String getDIAS_PENDENTES() {
		return DIAS_PENDENTES;
	}
	public void setDIAS_PENDENTES(String dIAS_PENDENTES) {
		DIAS_PENDENTES = dIAS_PENDENTES;
	}
	public String getPROXIMA_REVISAO() {
		return PROXIMA_REVISAO;
	}
	public void setPROXIMA_REVISAO(String pROXIMA_REVISAO) {
		PROXIMA_REVISAO = pROXIMA_REVISAO;
	}
	public String getHORIMETRO_PENDENTE() {
		return HORIMETRO_PENDENTE;
	}
	public void setHORIMETRO_PENDENTE(String hORIMETRO_PENDENTE) {
		HORIMETRO_PENDENTE = hORIMETRO_PENDENTE;
	}
	public String getMODELO() {
		return MODELO;
	}
	public void setMODELO(String modelo) {
		MODELO = modelo;
	}
	public String getDIA() {
		return DIA;
	}
	public void setDIA(String dia) {
		DIA = dia;
	}
	public String getMES() {
		return MES;
	}
	public void setMES(String mes) {
		MES = mes;
	}
	public String getANO() {
		return ANO;
	}
	public void setANO(String ano) {
		ANO = ano;
	}
	public String getNUMEROCONTRATO() {
		return NUMEROCONTRATO;
	}
	public void setNUMEROCONTRATO(String numerocontrato) {
		NUMEROCONTRATO = numerocontrato;
	}
	public String getRAZAOSOCIAL() {
		return RAZAOSOCIAL;
	}
	public void setRAZAOSOCIAL(String razaosocial) {
		RAZAOSOCIAL = razaosocial;
	}
	public String getPL() {
		return PL;
	}
	public void setPL(String pl) {
		PL = pl;
	}
	public String getMESFIM() {
		return MESFIM;
	}
	public void setMESFIM(String mesfim) {
		MESFIM = mesfim;
	}
	public String getANOFIM() {
		return ANOFIM;
	}
	public void setANOFIM(String anofim) {
		ANOFIM = anofim;
	}
	public BigDecimal getVALOR() {
		return VALOR;
	}
	public void setVALOR(BigDecimal valor) {
		VALOR = valor;
	}
	public BigDecimal getPARCELAS() {
		return PARCELAS;
	}
	public void setPARCELAS(BigDecimal parcelas) {
		PARCELAS = parcelas;
	}
	public Date getDATAACEITE() {
		return DATAACEITE;
	}
	public void setDATAACEITE(Date dataaceite) {
		DATAACEITE = dataaceite;
	}
	public String getCODIGOCLIENTE() {
		return CODIGOCLIENTE;
	}
	public void setCODIGOCLIENTE(String codigocliente) {
		CODIGOCLIENTE = codigocliente;
	}
	public String getNUMOS() {
		return NUMOS;
	}
	public void setNUMOS(String numos) {
		NUMOS = numos;
	}
	public String getNUMERO_SERIE() {
		return NUMERO_SERIE;
	}
	public void setNUMERO_SERIE(String numero_serie) {
		NUMERO_SERIE = numero_serie;
	}
	public String getCONSULTOR() {
		return CONSULTOR;
	}
	public void setCONSULTOR(String consultor) {
		CONSULTOR = consultor;
	}
	public BigDecimal getFILIAL() {
		return FILIAL;
	}
	public void setFILIAL(BigDecimal filial) {
		FILIAL = filial;
	}
	public String getTIPOCONTRATO() {
		return TIPOCONTRATO;
	}
	public void setTIPOCONTRATO(String tipocontrato) {
		TIPOCONTRATO = tipocontrato;
	}
	public String getTA() {
		return TA;
	}
	public void setTA(String ta) {
		TA = ta;
	}
	public String getORIGEM() {
		return ORIGEM;
	}
	public void setORIGEM(String origem) {
		ORIGEM = origem;
	}
	public String getDESTINO() {
		return DESTINO;
	}
	public void setDESTINO(String destino) {
		DESTINO = destino;
	}
	public BigDecimal getREVPENDENTES() {
		return REVPENDENTES;
	}
	public void setREVPENDENTES(BigDecimal revpendentes) {
		REVPENDENTES = revpendentes;
	}
	public BigDecimal getHORIMETRO() {
		return HORIMETRO;
	}
	public void setHORIMETRO(BigDecimal hORIMETRO) {
		HORIMETRO = hORIMETRO;
	}
	public Date getAT_HORIMETRO() {
		return AT_HORIMETRO;
	}
	public void setAT_HORIMETRO(Date aT_HORIMETRO) {
		AT_HORIMETRO = aT_HORIMETRO;
	}
	public String getTIPO_CONTRATO() {
		return TIPO_CONTRATO;
	}
	public void setTIPO_CONTRATO(String tIPO_CONTRATO) {
		TIPO_CONTRATO = tIPO_CONTRATO;
	}
	public String getHORIMETRO_ULTIMA_REVISAO() {
		return HORIMETRO_ULTIMA_REVISAO;
	}
	public void setHORIMETRO_ULTIMA_REVISAO(String hORIMETRO_ULTIMA_REVISAO) {
		HORIMETRO_ULTIMA_REVISAO = hORIMETRO_ULTIMA_REVISAO;
	}
	public String getHORIMETRO_PROXIMA_REVISAO() {
		return HORIMETRO_PROXIMA_REVISAO;
	}
	public void setHORIMETRO_PROXIMA_REVISAO(String hORIMETRO_PROXIMA_REVISAO) {
		HORIMETRO_PROXIMA_REVISAO = hORIMETRO_PROXIMA_REVISAO;
	}
	public String getHORIMETRO_FALTANTE() {
		return HORIMETRO_FALTANTE;
	}
	public void setHORIMETRO_FALTANTE(String hORIMETRO_FALTANTE) {
		HORIMETRO_FALTANTE = hORIMETRO_FALTANTE;
	}
	

}
