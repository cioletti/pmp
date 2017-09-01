package bean
{
	import mx.collections.ArrayCollection;
	
	[RemoteClass(alias="com.pmp.bean.UsuarioBean")]
	public class UsuarioBean
	{
		private var _idUsuario:Number;
		private var _sistemaList:ArrayCollection;
		private var _perfilIdPerfil:Number;
		private var _nome:String;
		private var _matricula:String;
		private var _login:String;
		private var _senha:String;
		private var _msg:String;
		private var _menu:String;
		private var _campo:String;
		private var _filial:String;
		private var _filialStr:String;
		private var _email:String;
		private var _telefone:String;
		private var _idSistema:String;
		private var _idPerfil:String;
		private var _centroDeCustoList:ArrayCollection;
		private var _siglaPerfil:String;
		private var _agendamentoList:ArrayCollection;
		private var _isAdm:Boolean;
		private var _estimateBy:String;
		private var _placa:String;
		private var _codVeiculo:String;
		
		public function UsuarioBean()
		{
			_centroDeCustoList = new ArrayCollection();
		}
		
		public function get sistemaList():ArrayCollection{return _sistemaList};
		public function set sistemaList(sistemaList:ArrayCollection):void{this._sistemaList = sistemaList}; 

		public function get idSistema():String{return _idSistema};
		public function set idSistema(idSistema:String):void{this._idSistema = idSistema}; 
		
		public function get idPerfil():String{return _idPerfil};
		public function set idPerfil(idPerfil:String):void{this._idPerfil = idPerfil}; 

		
		public function get telefone():String{return _telefone};
		public function set telefone(telefone:String):void{this._telefone = telefone}; 

		public function get email():String{return _email};
		public function set email(email:String):void{this._email = email}; 

		public function get filialStr():String{return _filialStr};
		public function set filialStr(filialStr:String):void{this._filialStr = filialStr}; 

		public function get filial():String{return _filial};
		public function set filial(filial:String):void{this._filial = filial}; 

		public function get campo():String{return _campo};
		public function set campo(campo:String):void{this._campo = campo}; 
		
		public function get idUsuario():Number{return _idUsuario};
	    public function set idUsuario(idUsuario:Number):void{this._idUsuario = idUsuario}; 

		public function get perfilIdPerfil():Number{return _perfilIdPerfil};
	    public function set perfilIdPerfil(perfilIdPerfil:Number):void{this._perfilIdPerfil = perfilIdPerfil}; 
		
		public function get nome():String{return _nome};
	    public function set nome(nome:String):void{this._nome = nome}; 
		
		public function get matricula():String{return _matricula};
	    public function set matricula(matricula:String):void{this._matricula = matricula}; 
		
		public function get login():String{return _login};
	    public function set login(login:String):void{this._login = login}; 

		public function get senha():String{return _senha};
	    public function set senha(senha:String):void{this._senha = senha}; 

		public function get msg():String{return _msg};
	    public function set msg(msg:String):void{this._msg = msg}; 

		public function get menu():String{return _menu};
	    public function set menu(menu:String):void{this._menu = menu}; 

		public function get centroDeCustoList():ArrayCollection{return _centroDeCustoList};
	    public function set centroDeCustoList(centroDeCustoList:ArrayCollection):void{this._centroDeCustoList = centroDeCustoList};
	    
	    public function get siglaPerfil():String{return _siglaPerfil};
	    public function set siglaPerfil(siglaPerfil:String):void{this._siglaPerfil = siglaPerfil}; 
		
		public function get agendamentoList(): ArrayCollection{return _agendamentoList};
		public function set agendamentoList(agendamentoList: ArrayCollection): void{this._agendamentoList = agendamentoList}; 
		
		public function get isAdm(): Boolean{return _isAdm};
		public function set isAdm(isAdm: Boolean): void{this._isAdm = isAdm}
		public function get estimateBy():String
		{
			return _estimateBy;
		}

		public function set estimateBy(value:String):void
		{
			_estimateBy = value;
		}

		public function get placa():String
		{
			return _placa;
		}

		public function set placa(value:String):void
		{
			_placa = value;
		}

		public function get codVeiculo():String
		{
			return _codVeiculo;
		}

		public function set codVeiculo(value:String):void
		{
			_codVeiculo = value;
		}


; 
 

	}
}