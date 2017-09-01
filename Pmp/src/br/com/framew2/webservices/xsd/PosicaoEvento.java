/**
 * PosicaoEvento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.framew2.webservices.xsd;

public class PosicaoEvento  implements java.io.Serializable {
    private java.lang.Integer altitude;

    private br.com.framew2.webservices.xsd.Componentes[] componentes;

    private java.lang.String dataEquipamento;

    private java.lang.String dataGps;

    private java.lang.Double distanciaMetros;

    private java.lang.Integer evento;

    private java.lang.Short hdop;

    private java.lang.Double latitude;

    private java.lang.String localizacao;

    private java.lang.Double longitude;

    private java.lang.String numero;

    private java.lang.Short numeroSatelites;

    private java.lang.String placa;

    private java.lang.Short proa;

    private java.lang.Integer sequencia;

    private java.lang.Short validade;

    private java.lang.Short velocidade;

    public PosicaoEvento() {
    }

    public PosicaoEvento(
           java.lang.Integer altitude,
           br.com.framew2.webservices.xsd.Componentes[] componentes,
           java.lang.String dataEquipamento,
           java.lang.String dataGps,
           java.lang.Double distanciaMetros,
           java.lang.Integer evento,
           java.lang.Short hdop,
           java.lang.Double latitude,
           java.lang.String localizacao,
           java.lang.Double longitude,
           java.lang.String numero,
           java.lang.Short numeroSatelites,
           java.lang.String placa,
           java.lang.Short proa,
           java.lang.Integer sequencia,
           java.lang.Short validade,
           java.lang.Short velocidade) {
           this.altitude = altitude;
           this.componentes = componentes;
           this.dataEquipamento = dataEquipamento;
           this.dataGps = dataGps;
           this.distanciaMetros = distanciaMetros;
           this.evento = evento;
           this.hdop = hdop;
           this.latitude = latitude;
           this.localizacao = localizacao;
           this.longitude = longitude;
           this.numero = numero;
           this.numeroSatelites = numeroSatelites;
           this.placa = placa;
           this.proa = proa;
           this.sequencia = sequencia;
           this.validade = validade;
           this.velocidade = velocidade;
    }


    /**
     * Gets the altitude value for this PosicaoEvento.
     * 
     * @return altitude
     */
    public java.lang.Integer getAltitude() {
        return altitude;
    }


    /**
     * Sets the altitude value for this PosicaoEvento.
     * 
     * @param altitude
     */
    public void setAltitude(java.lang.Integer altitude) {
        this.altitude = altitude;
    }


    /**
     * Gets the componentes value for this PosicaoEvento.
     * 
     * @return componentes
     */
    public br.com.framew2.webservices.xsd.Componentes[] getComponentes() {
        return componentes;
    }


    /**
     * Sets the componentes value for this PosicaoEvento.
     * 
     * @param componentes
     */
    public void setComponentes(br.com.framew2.webservices.xsd.Componentes[] componentes) {
        this.componentes = componentes;
    }

    public br.com.framew2.webservices.xsd.Componentes getComponentes(int i) {
        return this.componentes[i];
    }

    public void setComponentes(int i, br.com.framew2.webservices.xsd.Componentes _value) {
        this.componentes[i] = _value;
    }


    /**
     * Gets the dataEquipamento value for this PosicaoEvento.
     * 
     * @return dataEquipamento
     */
    public java.lang.String getDataEquipamento() {
        return dataEquipamento;
    }


    /**
     * Sets the dataEquipamento value for this PosicaoEvento.
     * 
     * @param dataEquipamento
     */
    public void setDataEquipamento(java.lang.String dataEquipamento) {
        this.dataEquipamento = dataEquipamento;
    }


    /**
     * Gets the dataGps value for this PosicaoEvento.
     * 
     * @return dataGps
     */
    public java.lang.String getDataGps() {
        return dataGps;
    }


    /**
     * Sets the dataGps value for this PosicaoEvento.
     * 
     * @param dataGps
     */
    public void setDataGps(java.lang.String dataGps) {
        this.dataGps = dataGps;
    }


    /**
     * Gets the distanciaMetros value for this PosicaoEvento.
     * 
     * @return distanciaMetros
     */
    public java.lang.Double getDistanciaMetros() {
        return distanciaMetros;
    }


    /**
     * Sets the distanciaMetros value for this PosicaoEvento.
     * 
     * @param distanciaMetros
     */
    public void setDistanciaMetros(java.lang.Double distanciaMetros) {
        this.distanciaMetros = distanciaMetros;
    }


    /**
     * Gets the evento value for this PosicaoEvento.
     * 
     * @return evento
     */
    public java.lang.Integer getEvento() {
        return evento;
    }


    /**
     * Sets the evento value for this PosicaoEvento.
     * 
     * @param evento
     */
    public void setEvento(java.lang.Integer evento) {
        this.evento = evento;
    }


    /**
     * Gets the hdop value for this PosicaoEvento.
     * 
     * @return hdop
     */
    public java.lang.Short getHdop() {
        return hdop;
    }


    /**
     * Sets the hdop value for this PosicaoEvento.
     * 
     * @param hdop
     */
    public void setHdop(java.lang.Short hdop) {
        this.hdop = hdop;
    }


    /**
     * Gets the latitude value for this PosicaoEvento.
     * 
     * @return latitude
     */
    public java.lang.Double getLatitude() {
        return latitude;
    }


    /**
     * Sets the latitude value for this PosicaoEvento.
     * 
     * @param latitude
     */
    public void setLatitude(java.lang.Double latitude) {
        this.latitude = latitude;
    }


    /**
     * Gets the localizacao value for this PosicaoEvento.
     * 
     * @return localizacao
     */
    public java.lang.String getLocalizacao() {
        return localizacao;
    }


    /**
     * Sets the localizacao value for this PosicaoEvento.
     * 
     * @param localizacao
     */
    public void setLocalizacao(java.lang.String localizacao) {
        this.localizacao = localizacao;
    }


    /**
     * Gets the longitude value for this PosicaoEvento.
     * 
     * @return longitude
     */
    public java.lang.Double getLongitude() {
        return longitude;
    }


    /**
     * Sets the longitude value for this PosicaoEvento.
     * 
     * @param longitude
     */
    public void setLongitude(java.lang.Double longitude) {
        this.longitude = longitude;
    }


    /**
     * Gets the numero value for this PosicaoEvento.
     * 
     * @return numero
     */
    public java.lang.String getNumero() {
        return numero;
    }


    /**
     * Sets the numero value for this PosicaoEvento.
     * 
     * @param numero
     */
    public void setNumero(java.lang.String numero) {
        this.numero = numero;
    }


    /**
     * Gets the numeroSatelites value for this PosicaoEvento.
     * 
     * @return numeroSatelites
     */
    public java.lang.Short getNumeroSatelites() {
        return numeroSatelites;
    }


    /**
     * Sets the numeroSatelites value for this PosicaoEvento.
     * 
     * @param numeroSatelites
     */
    public void setNumeroSatelites(java.lang.Short numeroSatelites) {
        this.numeroSatelites = numeroSatelites;
    }


    /**
     * Gets the placa value for this PosicaoEvento.
     * 
     * @return placa
     */
    public java.lang.String getPlaca() {
        return placa;
    }


    /**
     * Sets the placa value for this PosicaoEvento.
     * 
     * @param placa
     */
    public void setPlaca(java.lang.String placa) {
        this.placa = placa;
    }


    /**
     * Gets the proa value for this PosicaoEvento.
     * 
     * @return proa
     */
    public java.lang.Short getProa() {
        return proa;
    }


    /**
     * Sets the proa value for this PosicaoEvento.
     * 
     * @param proa
     */
    public void setProa(java.lang.Short proa) {
        this.proa = proa;
    }


    /**
     * Gets the sequencia value for this PosicaoEvento.
     * 
     * @return sequencia
     */
    public java.lang.Integer getSequencia() {
        return sequencia;
    }


    /**
     * Sets the sequencia value for this PosicaoEvento.
     * 
     * @param sequencia
     */
    public void setSequencia(java.lang.Integer sequencia) {
        this.sequencia = sequencia;
    }


    /**
     * Gets the validade value for this PosicaoEvento.
     * 
     * @return validade
     */
    public java.lang.Short getValidade() {
        return validade;
    }


    /**
     * Sets the validade value for this PosicaoEvento.
     * 
     * @param validade
     */
    public void setValidade(java.lang.Short validade) {
        this.validade = validade;
    }


    /**
     * Gets the velocidade value for this PosicaoEvento.
     * 
     * @return velocidade
     */
    public java.lang.Short getVelocidade() {
        return velocidade;
    }


    /**
     * Sets the velocidade value for this PosicaoEvento.
     * 
     * @param velocidade
     */
    public void setVelocidade(java.lang.Short velocidade) {
        this.velocidade = velocidade;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PosicaoEvento)) return false;
        PosicaoEvento other = (PosicaoEvento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.altitude==null && other.getAltitude()==null) || 
             (this.altitude!=null &&
              this.altitude.equals(other.getAltitude()))) &&
            ((this.componentes==null && other.getComponentes()==null) || 
             (this.componentes!=null &&
              java.util.Arrays.equals(this.componentes, other.getComponentes()))) &&
            ((this.dataEquipamento==null && other.getDataEquipamento()==null) || 
             (this.dataEquipamento!=null &&
              this.dataEquipamento.equals(other.getDataEquipamento()))) &&
            ((this.dataGps==null && other.getDataGps()==null) || 
             (this.dataGps!=null &&
              this.dataGps.equals(other.getDataGps()))) &&
            ((this.distanciaMetros==null && other.getDistanciaMetros()==null) || 
             (this.distanciaMetros!=null &&
              this.distanciaMetros.equals(other.getDistanciaMetros()))) &&
            ((this.evento==null && other.getEvento()==null) || 
             (this.evento!=null &&
              this.evento.equals(other.getEvento()))) &&
            ((this.hdop==null && other.getHdop()==null) || 
             (this.hdop!=null &&
              this.hdop.equals(other.getHdop()))) &&
            ((this.latitude==null && other.getLatitude()==null) || 
             (this.latitude!=null &&
              this.latitude.equals(other.getLatitude()))) &&
            ((this.localizacao==null && other.getLocalizacao()==null) || 
             (this.localizacao!=null &&
              this.localizacao.equals(other.getLocalizacao()))) &&
            ((this.longitude==null && other.getLongitude()==null) || 
             (this.longitude!=null &&
              this.longitude.equals(other.getLongitude()))) &&
            ((this.numero==null && other.getNumero()==null) || 
             (this.numero!=null &&
              this.numero.equals(other.getNumero()))) &&
            ((this.numeroSatelites==null && other.getNumeroSatelites()==null) || 
             (this.numeroSatelites!=null &&
              this.numeroSatelites.equals(other.getNumeroSatelites()))) &&
            ((this.placa==null && other.getPlaca()==null) || 
             (this.placa!=null &&
              this.placa.equals(other.getPlaca()))) &&
            ((this.proa==null && other.getProa()==null) || 
             (this.proa!=null &&
              this.proa.equals(other.getProa()))) &&
            ((this.sequencia==null && other.getSequencia()==null) || 
             (this.sequencia!=null &&
              this.sequencia.equals(other.getSequencia()))) &&
            ((this.validade==null && other.getValidade()==null) || 
             (this.validade!=null &&
              this.validade.equals(other.getValidade()))) &&
            ((this.velocidade==null && other.getVelocidade()==null) || 
             (this.velocidade!=null &&
              this.velocidade.equals(other.getVelocidade())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAltitude() != null) {
            _hashCode += getAltitude().hashCode();
        }
        if (getComponentes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getComponentes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getComponentes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDataEquipamento() != null) {
            _hashCode += getDataEquipamento().hashCode();
        }
        if (getDataGps() != null) {
            _hashCode += getDataGps().hashCode();
        }
        if (getDistanciaMetros() != null) {
            _hashCode += getDistanciaMetros().hashCode();
        }
        if (getEvento() != null) {
            _hashCode += getEvento().hashCode();
        }
        if (getHdop() != null) {
            _hashCode += getHdop().hashCode();
        }
        if (getLatitude() != null) {
            _hashCode += getLatitude().hashCode();
        }
        if (getLocalizacao() != null) {
            _hashCode += getLocalizacao().hashCode();
        }
        if (getLongitude() != null) {
            _hashCode += getLongitude().hashCode();
        }
        if (getNumero() != null) {
            _hashCode += getNumero().hashCode();
        }
        if (getNumeroSatelites() != null) {
            _hashCode += getNumeroSatelites().hashCode();
        }
        if (getPlaca() != null) {
            _hashCode += getPlaca().hashCode();
        }
        if (getProa() != null) {
            _hashCode += getProa().hashCode();
        }
        if (getSequencia() != null) {
            _hashCode += getSequencia().hashCode();
        }
        if (getValidade() != null) {
            _hashCode += getValidade().hashCode();
        }
        if (getVelocidade() != null) {
            _hashCode += getVelocidade().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PosicaoEvento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "PosicaoEvento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("altitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "altitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("componentes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "componentes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "Componentes"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataEquipamento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "dataEquipamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataGps");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "dataGps"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distanciaMetros");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "distanciaMetros"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("evento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "evento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hdop");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "hdop"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("latitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "latitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("localizacao");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "localizacao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("longitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "longitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numero");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "numero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroSatelites");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "numeroSatelites"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("placa");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "placa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("proa");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "proa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequencia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "sequencia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("validade");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "validade"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("velocidade");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "velocidade"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
