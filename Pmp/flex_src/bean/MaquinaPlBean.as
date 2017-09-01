package bean
{
	[RemoteClass(alias="com.pmp.bean.MaquinaPlBean")]
	public class MaquinaPlBean
	{
		private var _id:Number;
		private var _numeroSerie:String;
		private var _horimetro:Number;
		private var _latitude:String;
		private var _longitude:String;
		private var _dataAtualizacao:Date;
		private var _modelo:String;
		private var _idContrato:Number;
		
		public function get idContrato():Number
		{
			return _idContrato;
		}

		public function set idContrato(value:Number):void
		{
			_idContrato = value;
		}

		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 
		
		public function get numeroSerie(): String{return _numeroSerie};
		public function set numeroSerie(numeroSerie: String): void{this._numeroSerie = numeroSerie}; 
		
		public function get horimetro(): Number{return _horimetro};
		public function set horimetro(horimetro: Number): void{this._horimetro = horimetro}; 
		
		public function get latitude(): String{return _latitude};
		public function set latitude(latitude: String): void{this._latitude = latitude}; 
				
		public function get longitude(): String{return _longitude};
		public function set longitude(longitude: String): void{this._longitude = longitude}; 
		
		public function get dataAtualizacao(): Date{return _dataAtualizacao};
		public function set dataAtualizacao(dataAtualizacao: Date): void{this._dataAtualizacao = dataAtualizacao}; 
		
		public function get modelo(): String{return _modelo};
		public function set modelo(modelo: String): void{this._modelo = modelo}; 

		
		public function MaquinaPlBean()
		{
		}
	}
}