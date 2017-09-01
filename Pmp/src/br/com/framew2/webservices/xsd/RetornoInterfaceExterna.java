/**
 * RetornoInterfaceExterna.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.framew2.webservices.xsd;

public class RetornoInterfaceExterna  implements java.io.Serializable {
    private java.lang.Integer codigoRetorno;

    private br.com.framew2.webservices.xsd.Evento[] eventos;

    private java.lang.String mensagemRetorno;

    public RetornoInterfaceExterna() {
    }

    public RetornoInterfaceExterna(
           java.lang.Integer codigoRetorno,
           br.com.framew2.webservices.xsd.Evento[] eventos,
           java.lang.String mensagemRetorno) {
           this.codigoRetorno = codigoRetorno;
           this.eventos = eventos;
           this.mensagemRetorno = mensagemRetorno;
    }


    /**
     * Gets the codigoRetorno value for this RetornoInterfaceExterna.
     * 
     * @return codigoRetorno
     */
    public java.lang.Integer getCodigoRetorno() {
        return codigoRetorno;
    }


    /**
     * Sets the codigoRetorno value for this RetornoInterfaceExterna.
     * 
     * @param codigoRetorno
     */
    public void setCodigoRetorno(java.lang.Integer codigoRetorno) {
        this.codigoRetorno = codigoRetorno;
    }


    /**
     * Gets the eventos value for this RetornoInterfaceExterna.
     * 
     * @return eventos
     */
    public br.com.framew2.webservices.xsd.Evento[] getEventos() {
        return eventos;
    }


    /**
     * Sets the eventos value for this RetornoInterfaceExterna.
     * 
     * @param eventos
     */
    public void setEventos(br.com.framew2.webservices.xsd.Evento[] eventos) {
        this.eventos = eventos;
    }

    public br.com.framew2.webservices.xsd.Evento getEventos(int i) {
        return this.eventos[i];
    }

    public void setEventos(int i, br.com.framew2.webservices.xsd.Evento _value) {
        this.eventos[i] = _value;
    }


    /**
     * Gets the mensagemRetorno value for this RetornoInterfaceExterna.
     * 
     * @return mensagemRetorno
     */
    public java.lang.String getMensagemRetorno() {
        return mensagemRetorno;
    }


    /**
     * Sets the mensagemRetorno value for this RetornoInterfaceExterna.
     * 
     * @param mensagemRetorno
     */
    public void setMensagemRetorno(java.lang.String mensagemRetorno) {
        this.mensagemRetorno = mensagemRetorno;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RetornoInterfaceExterna)) return false;
        RetornoInterfaceExterna other = (RetornoInterfaceExterna) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigoRetorno==null && other.getCodigoRetorno()==null) || 
             (this.codigoRetorno!=null &&
              this.codigoRetorno.equals(other.getCodigoRetorno()))) &&
            ((this.eventos==null && other.getEventos()==null) || 
             (this.eventos!=null &&
              java.util.Arrays.equals(this.eventos, other.getEventos()))) &&
            ((this.mensagemRetorno==null && other.getMensagemRetorno()==null) || 
             (this.mensagemRetorno!=null &&
              this.mensagemRetorno.equals(other.getMensagemRetorno())));
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
        if (getCodigoRetorno() != null) {
            _hashCode += getCodigoRetorno().hashCode();
        }
        if (getEventos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEventos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEventos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMensagemRetorno() != null) {
            _hashCode += getMensagemRetorno().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RetornoInterfaceExterna.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "RetornoInterfaceExterna"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoRetorno");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "codigoRetorno"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eventos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "eventos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "Evento"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mensagemRetorno");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "mensagemRetorno"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
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
