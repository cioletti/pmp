package bean
{
	[RemoteClass(alias="com.pmp.bean.PlMaquinaBean")]
	public class PlMaquinaBean
	{
		
		private var _serie:String;
		private var _latitude:String;
		private var _longitude:String;
		private var _descricao:String;
		private var _codCliente:String;
		private var _nomeCliente:String;
		private var _fiial:String;
		private var _modelo:String;
		private var _localizacao:String;
		private var _placa:String;
		private var _numOS:String;
		private var _tecnico:String;
		private var _horimetro:String;
		private var _dataAtualizacao:String;
		private var _cor:String;
		
		public function get serie():String{return _serie};
		public function set serie(serie:String):void{this._serie = serie}; 
		
		public function get latitude():String{return _latitude};
		public function set latitude(latitude:String):void{this._latitude = latitude}; 
		
		public function get longitude():String{return _longitude};
		public function set longitude(longitude:String):void{this._longitude = longitude}; 
		
		public function get descricao():String{return _descricao};
		public function set descricao(descricao:String):void{this._descricao = descricao}; 
		
		public function get codCliente():String{return _codCliente};
		public function set codCliente(codCliente:String):void{this._codCliente = codCliente}; 
		
		public function get nomeCliente():String{return _nomeCliente};
		public function set nomeCliente(nomeCliente:String):void{this._nomeCliente = nomeCliente}; 
		
		public function get fiial():String{return _fiial};
		public function set fiial(fiial:String):void{this._fiial = fiial}; 
		
		public function get modelo():String{return _modelo};
		public function set modelo(modelo:String):void{this._modelo = modelo};
		
		public function get localizacao():String
		{
			return _localizacao;
		}
		
		public function set localizacao(value:String):void
		{
			_localizacao = value;
		}
		
		public function get placa():String
		{
			return _placa;
		}
		
		public function set placa(value:String):void
		{
			_placa = value;
		}
		
		public function get numOS():String
		{
			return _numOS;
		}
		
		public function set numOS(value:String):void
		{
			_numOS = value;
		}
		
		public function get tecnico():String
		{
			return _tecnico;
		}
		
		public function set tecnico(value:String):void
		{
			_tecnico = value;
		}
		
		public function get horimetro():String
		{
			return _horimetro;
		}
		
		public function set horimetro(value:String):void
		{
			_horimetro = value;
		}
		
		public function get dataAtualizacao():String
		{
			return _dataAtualizacao;
		}
		
		public function set dataAtualizacao(value:String):void
		{
			_dataAtualizacao = value;
		}

		public function get cor():String
		{
			return _cor;
		}

		public function set cor(value:String):void
		{
			_cor = value;
		}
		
		
		public function PlMaquinaBean()
		{
		}
	}
}