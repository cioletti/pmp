/**
 * ComponentesDominio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.framew2.webservices.xsd;

public class ComponentesDominio  implements java.io.Serializable {
    private java.lang.String descricaoComponente;

    private java.lang.String descricaoDominio;

    private java.lang.Integer id;

    private java.lang.String nomeCampo;

    private java.lang.String unidade;

    private java.lang.Integer valor;

    public ComponentesDominio() {
    }

    public ComponentesDominio(
           java.lang.String descricaoComponente,
           java.lang.String descricaoDominio,
           java.lang.Integer id,
           java.lang.String nomeCampo,
           java.lang.String unidade,
           java.lang.Integer valor) {
           this.descricaoComponente = descricaoComponente;
           this.descricaoDominio = descricaoDominio;
           this.id = id;
           this.nomeCampo = nomeCampo;
           this.unidade = unidade;
           this.valor = valor;
    }


    /**
     * Gets the descricaoComponente value for this ComponentesDominio.
     * 
     * @return descricaoComponente
     */
    public java.lang.String getDescricaoComponente() {
        return descricaoComponente;
    }


    /**
     * Sets the descricaoComponente value for this ComponentesDominio.
     * 
     * @param descricaoComponente
     */
    public void setDescricaoComponente(java.lang.String descricaoComponente) {
        this.descricaoComponente = descricaoComponente;
    }


    /**
     * Gets the descricaoDominio value for this ComponentesDominio.
     * 
     * @return descricaoDominio
     */
    public java.lang.String getDescricaoDominio() {
        return descricaoDominio;
    }


    /**
     * Sets the descricaoDominio value for this ComponentesDominio.
     * 
     * @param descricaoDominio
     */
    public void setDescricaoDominio(java.lang.String descricaoDominio) {
        this.descricaoDominio = descricaoDominio;
    }


    /**
     * Gets the id value for this ComponentesDominio.
     * 
     * @return id
     */
    public java.lang.Integer getId() {
        return id;
    }


    /**
     * Sets the id value for this ComponentesDominio.
     * 
     * @param id
     */
    public void setId(java.lang.Integer id) {
        this.id = id;
    }


    /**
     * Gets the nomeCampo value for this ComponentesDominio.
     * 
     * @return nomeCampo
     */
    public java.lang.String getNomeCampo() {
        return nomeCampo;
    }


    /**
     * Sets the nomeCampo value for this ComponentesDominio.
     * 
     * @param nomeCampo
     */
    public void setNomeCampo(java.lang.String nomeCampo) {
        this.nomeCampo = nomeCampo;
    }


    /**
     * Gets the unidade value for this ComponentesDominio.
     * 
     * @return unidade
     */
    public java.lang.String getUnidade() {
        return unidade;
    }


    /**
     * Sets the unidade value for this ComponentesDominio.
     * 
     * @param unidade
     */
    public void setUnidade(java.lang.String unidade) {
        this.unidade = unidade;
    }


    /**
     * Gets the valor value for this ComponentesDominio.
     * 
     * @return valor
     */
    public java.lang.Integer getValor() {
        return valor;
    }


    /**
     * Sets the valor value for this ComponentesDominio.
     * 
     * @param valor
     */
    public void setValor(java.lang.Integer valor) {
        this.valor = valor;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ComponentesDominio)) return false;
        ComponentesDominio other = (ComponentesDominio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.descricaoComponente==null && other.getDescricaoComponente()==null) || 
             (this.descricaoComponente!=null &&
              this.descricaoComponente.equals(other.getDescricaoComponente()))) &&
            ((this.descricaoDominio==null && other.getDescricaoDominio()==null) || 
             (this.descricaoDominio!=null &&
              this.descricaoDominio.equals(other.getDescricaoDominio()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.nomeCampo==null && other.getNomeCampo()==null) || 
             (this.nomeCampo!=null &&
              this.nomeCampo.equals(other.getNomeCampo()))) &&
            ((this.unidade==null && other.getUnidade()==null) || 
             (this.unidade!=null &&
              this.unidade.equals(other.getUnidade()))) &&
            ((this.valor==null && other.getValor()==null) || 
             (this.valor!=null &&
              this.valor.equals(other.getValor())));
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
        if (getDescricaoComponente() != null) {
            _hashCode += getDescricaoComponente().hashCode();
        }
        if (getDescricaoDominio() != null) {
            _hashCode += getDescricaoDominio().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getNomeCampo() != null) {
            _hashCode += getNomeCampo().hashCode();
        }
        if (getUnidade() != null) {
            _hashCode += getUnidade().hashCode();
        }
        if (getValor() != null) {
            _hashCode += getValor().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ComponentesDominio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "ComponentesDominio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descricaoComponente");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "descricaoComponente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descricaoDominio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "descricaoDominio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeCampo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "nomeCampo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unidade");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "unidade"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.framew2.com.br/xsd", "valor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
