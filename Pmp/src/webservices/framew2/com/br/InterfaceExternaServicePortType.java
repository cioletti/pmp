/**
 * InterfaceExternaServicePortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservices.framew2.com.br;

public interface InterfaceExternaServicePortType extends java.rmi.Remote {
    public br.com.framew2.webservices.xsd.RetornoInterfacePosicaoExterna obterUltimaPosicaoEvento(java.lang.Integer id, java.lang.String senha) throws java.rmi.RemoteException;
    public br.com.framew2.webservices.xsd.RetornoInterfaceExterna obterEventos(java.lang.Integer id, java.lang.String senha) throws java.rmi.RemoteException;
    public br.com.framew2.webservices.xsd.RetornoInterfaceExterna comandoSirene(java.lang.Integer id, java.lang.String senha, java.lang.String placa, java.lang.Integer situacao, java.lang.String numero) throws java.rmi.RemoteException;
    public br.com.framew2.webservices.xsd.RetornoInterfaceExterna obterUltimaPosicao(java.lang.Integer id, java.lang.String senha) throws java.rmi.RemoteException;
    public br.com.framew2.webservices.xsd.RetornoInterfaceExterna obterUltimaPosicaoNumero(java.lang.Integer id, java.lang.String senha, java.lang.Integer fabricanteId, java.lang.String numero) throws java.rmi.RemoteException;
    public br.com.framew2.webservices.xsd.RetornoInterfaceExterna comandoBloqueio(java.lang.Integer id, java.lang.String senha, java.lang.String placa, java.lang.Integer situacao, java.lang.String numero) throws java.rmi.RemoteException;
    public br.com.framew2.webservices.xsd.RetornoInterfaceExterna obterUltimaPosicaoPlaca(java.lang.Integer id, java.lang.String senha, java.lang.String placa) throws java.rmi.RemoteException;
    public br.com.framew2.webservices.xsd.RetornoInterfacePosicaoExterna obterEventosPosicao(java.lang.Integer id, java.lang.String senha) throws java.rmi.RemoteException;
    public java.lang.String setLog(java.lang.String id, java.lang.String pass, java.lang.Integer log) throws java.rmi.RemoteException;
    public br.com.framew2.webservices.xsd.ComponentesDominio[] obterDominiosComponentes(java.lang.Integer id, java.lang.String senha) throws java.rmi.RemoteException;
}
