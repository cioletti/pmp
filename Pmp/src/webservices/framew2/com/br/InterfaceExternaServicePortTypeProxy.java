package webservices.framew2.com.br;

public class InterfaceExternaServicePortTypeProxy implements webservices.framew2.com.br.InterfaceExternaServicePortType {
  private String _endpoint = null;
  private webservices.framew2.com.br.InterfaceExternaServicePortType interfaceExternaServicePortType = null;
  
  public InterfaceExternaServicePortTypeProxy() {
    _initInterfaceExternaServicePortTypeProxy();
  }
  
  public InterfaceExternaServicePortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initInterfaceExternaServicePortTypeProxy();
  }
  
  private void _initInterfaceExternaServicePortTypeProxy() {
    try {
      interfaceExternaServicePortType = (new webservices.framew2.com.br.InterfaceExternaServiceLocator()).getInterfaceExternaServiceSOAP11port_http();
      if (interfaceExternaServicePortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)interfaceExternaServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)interfaceExternaServicePortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (interfaceExternaServicePortType != null)
      ((javax.xml.rpc.Stub)interfaceExternaServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public webservices.framew2.com.br.InterfaceExternaServicePortType getInterfaceExternaServicePortType() {
    if (interfaceExternaServicePortType == null)
      _initInterfaceExternaServicePortTypeProxy();
    return interfaceExternaServicePortType;
  }
  
  public br.com.framew2.webservices.xsd.RetornoInterfacePosicaoExterna obterUltimaPosicaoEvento(java.lang.Integer id, java.lang.String senha) throws java.rmi.RemoteException{
    if (interfaceExternaServicePortType == null)
      _initInterfaceExternaServicePortTypeProxy();
    return interfaceExternaServicePortType.obterUltimaPosicaoEvento(id, senha);
  }
  
  public br.com.framew2.webservices.xsd.RetornoInterfaceExterna obterEventos(java.lang.Integer id, java.lang.String senha) throws java.rmi.RemoteException{
    if (interfaceExternaServicePortType == null)
      _initInterfaceExternaServicePortTypeProxy();
    return interfaceExternaServicePortType.obterEventos(id, senha);
  }
  
  public br.com.framew2.webservices.xsd.RetornoInterfaceExterna comandoSirene(java.lang.Integer id, java.lang.String senha, java.lang.String placa, java.lang.Integer situacao, java.lang.String numero) throws java.rmi.RemoteException{
    if (interfaceExternaServicePortType == null)
      _initInterfaceExternaServicePortTypeProxy();
    return interfaceExternaServicePortType.comandoSirene(id, senha, placa, situacao, numero);
  }
  
  public br.com.framew2.webservices.xsd.RetornoInterfaceExterna obterUltimaPosicao(java.lang.Integer id, java.lang.String senha) throws java.rmi.RemoteException{
    if (interfaceExternaServicePortType == null)
      _initInterfaceExternaServicePortTypeProxy();
    return interfaceExternaServicePortType.obterUltimaPosicao(id, senha);
  }
  
  public br.com.framew2.webservices.xsd.RetornoInterfaceExterna obterUltimaPosicaoNumero(java.lang.Integer id, java.lang.String senha, java.lang.Integer fabricanteId, java.lang.String numero) throws java.rmi.RemoteException{
    if (interfaceExternaServicePortType == null)
      _initInterfaceExternaServicePortTypeProxy();
    return interfaceExternaServicePortType.obterUltimaPosicaoNumero(id, senha, fabricanteId, numero);
  }
  
  public br.com.framew2.webservices.xsd.RetornoInterfaceExterna comandoBloqueio(java.lang.Integer id, java.lang.String senha, java.lang.String placa, java.lang.Integer situacao, java.lang.String numero) throws java.rmi.RemoteException{
    if (interfaceExternaServicePortType == null)
      _initInterfaceExternaServicePortTypeProxy();
    return interfaceExternaServicePortType.comandoBloqueio(id, senha, placa, situacao, numero);
  }
  
  public br.com.framew2.webservices.xsd.RetornoInterfaceExterna obterUltimaPosicaoPlaca(java.lang.Integer id, java.lang.String senha, java.lang.String placa) throws java.rmi.RemoteException{
    if (interfaceExternaServicePortType == null)
      _initInterfaceExternaServicePortTypeProxy();
    return interfaceExternaServicePortType.obterUltimaPosicaoPlaca(id, senha, placa);
  }
  
  public br.com.framew2.webservices.xsd.RetornoInterfacePosicaoExterna obterEventosPosicao(java.lang.Integer id, java.lang.String senha) throws java.rmi.RemoteException{
    if (interfaceExternaServicePortType == null)
      _initInterfaceExternaServicePortTypeProxy();
    return interfaceExternaServicePortType.obterEventosPosicao(id, senha);
  }
  
  public java.lang.String setLog(java.lang.String id, java.lang.String pass, java.lang.Integer log) throws java.rmi.RemoteException{
    if (interfaceExternaServicePortType == null)
      _initInterfaceExternaServicePortTypeProxy();
    return interfaceExternaServicePortType.setLog(id, pass, log);
  }
  
  public br.com.framew2.webservices.xsd.ComponentesDominio[] obterDominiosComponentes(java.lang.Integer id, java.lang.String senha) throws java.rmi.RemoteException{
    if (interfaceExternaServicePortType == null)
      _initInterfaceExternaServicePortTypeProxy();
    return interfaceExternaServicePortType.obterDominiosComponentes(id, senha);
  }
  
  
}