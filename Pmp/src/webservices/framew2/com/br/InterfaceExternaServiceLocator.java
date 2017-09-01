/**
 * InterfaceExternaServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservices.framew2.com.br;

public class InterfaceExternaServiceLocator extends org.apache.axis.client.Service implements webservices.framew2.com.br.InterfaceExternaService {

    public InterfaceExternaServiceLocator() {
    }


    public InterfaceExternaServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public InterfaceExternaServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for InterfaceExternaServiceSOAP11port_http
    private java.lang.String InterfaceExternaServiceSOAP11port_http_address = "http://webmportal.dynalias.net:83/services/InterfaceExternaService";

    public java.lang.String getInterfaceExternaServiceSOAP11port_httpAddress() {
        return InterfaceExternaServiceSOAP11port_http_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String InterfaceExternaServiceSOAP11port_httpWSDDServiceName = "InterfaceExternaServiceSOAP11port_http";

    public java.lang.String getInterfaceExternaServiceSOAP11port_httpWSDDServiceName() {
        return InterfaceExternaServiceSOAP11port_httpWSDDServiceName;
    }

    public void setInterfaceExternaServiceSOAP11port_httpWSDDServiceName(java.lang.String name) {
        InterfaceExternaServiceSOAP11port_httpWSDDServiceName = name;
    }

    public webservices.framew2.com.br.InterfaceExternaServicePortType getInterfaceExternaServiceSOAP11port_http() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(InterfaceExternaServiceSOAP11port_http_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getInterfaceExternaServiceSOAP11port_http(endpoint);
    }

    public webservices.framew2.com.br.InterfaceExternaServicePortType getInterfaceExternaServiceSOAP11port_http(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            webservices.framew2.com.br.InterfaceExternaServiceSOAP11BindingStub _stub = new webservices.framew2.com.br.InterfaceExternaServiceSOAP11BindingStub(portAddress, this);
            _stub.setPortName(getInterfaceExternaServiceSOAP11port_httpWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setInterfaceExternaServiceSOAP11port_httpEndpointAddress(java.lang.String address) {
        InterfaceExternaServiceSOAP11port_http_address = address;
    }


    // Use to get a proxy class for InterfaceExternaServiceSOAP12port_http
    private java.lang.String InterfaceExternaServiceSOAP12port_http_address = "http://webmportal.dynalias.net:83/services/InterfaceExternaService";

    public java.lang.String getInterfaceExternaServiceSOAP12port_httpAddress() {
        return InterfaceExternaServiceSOAP12port_http_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String InterfaceExternaServiceSOAP12port_httpWSDDServiceName = "InterfaceExternaServiceSOAP12port_http";

    public java.lang.String getInterfaceExternaServiceSOAP12port_httpWSDDServiceName() {
        return InterfaceExternaServiceSOAP12port_httpWSDDServiceName;
    }

    public void setInterfaceExternaServiceSOAP12port_httpWSDDServiceName(java.lang.String name) {
        InterfaceExternaServiceSOAP12port_httpWSDDServiceName = name;
    }

    public webservices.framew2.com.br.InterfaceExternaServicePortType getInterfaceExternaServiceSOAP12port_http() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(InterfaceExternaServiceSOAP12port_http_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getInterfaceExternaServiceSOAP12port_http(endpoint);
    }

    public webservices.framew2.com.br.InterfaceExternaServicePortType getInterfaceExternaServiceSOAP12port_http(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            webservices.framew2.com.br.InterfaceExternaServiceSOAP12BindingStub _stub = new webservices.framew2.com.br.InterfaceExternaServiceSOAP12BindingStub(portAddress, this);
            _stub.setPortName(getInterfaceExternaServiceSOAP12port_httpWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setInterfaceExternaServiceSOAP12port_httpEndpointAddress(java.lang.String address) {
        InterfaceExternaServiceSOAP12port_http_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (webservices.framew2.com.br.InterfaceExternaServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                webservices.framew2.com.br.InterfaceExternaServiceSOAP11BindingStub _stub = new webservices.framew2.com.br.InterfaceExternaServiceSOAP11BindingStub(new java.net.URL(InterfaceExternaServiceSOAP11port_http_address), this);
                _stub.setPortName(getInterfaceExternaServiceSOAP11port_httpWSDDServiceName());
                return _stub;
            }
            if (webservices.framew2.com.br.InterfaceExternaServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                webservices.framew2.com.br.InterfaceExternaServiceSOAP12BindingStub _stub = new webservices.framew2.com.br.InterfaceExternaServiceSOAP12BindingStub(new java.net.URL(InterfaceExternaServiceSOAP12port_http_address), this);
                _stub.setPortName(getInterfaceExternaServiceSOAP12port_httpWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("InterfaceExternaServiceSOAP11port_http".equals(inputPortName)) {
            return getInterfaceExternaServiceSOAP11port_http();
        }
        else if ("InterfaceExternaServiceSOAP12port_http".equals(inputPortName)) {
            return getInterfaceExternaServiceSOAP12port_http();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("br.com.framew2.webservices", "InterfaceExternaService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("br.com.framew2.webservices", "InterfaceExternaServiceSOAP11port_http"));
            ports.add(new javax.xml.namespace.QName("br.com.framew2.webservices", "InterfaceExternaServiceSOAP12port_http"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("InterfaceExternaServiceSOAP11port_http".equals(portName)) {
            setInterfaceExternaServiceSOAP11port_httpEndpointAddress(address);
        }
        else 
if ("InterfaceExternaServiceSOAP12port_http".equals(portName)) {
            setInterfaceExternaServiceSOAP12port_httpEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
