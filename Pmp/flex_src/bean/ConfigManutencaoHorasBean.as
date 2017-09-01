package bean
{
	[RemoteClass(alias="com.pmp.bean.ConfigManutencaoHorasBean")]
	public class ConfigManutencaoHorasBean
	{
		private var _id:Number;
		private var _idConfigManutencao:Number;
		private var _horas:Number;
		private var _standardJob:String;
		private var _isSelected:Boolean;
		private var _frequencia:Number;
		private var _isExecutado:String;
		private var _horasManutencao:Number;
		private var _horasRevisao:Number;
		private var _tipoInspecao:String;
		private var _numContrato:String;
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 
		
		public function get idConfigManutencao(): Number{return _idConfigManutencao};
		public function set idConfigManutencao(idConfigManutencao: Number): void{this._idConfigManutencao = idConfigManutencao}; 
		
		public function get horas(): Number{return _horas};
		public function set horas(horas: Number): void{this._horas = horas}; 

		public function get horasManutencao(): Number{return _horasManutencao};
		public function set horasManutencao(horasManutencao: Number): void{this._horasManutencao = horasManutencao}; 
		
		public function get standardJob(): String{return _standardJob};
		public function set standardJob(standardJob: String): void{this._standardJob = standardJob}; 
	
		public function get isSelected(): Boolean{return _isSelected};
		public function set isSelected(isSelected: Boolean): void{this._isSelected = isSelected};
		
		public function get frequencia(): Number{return _frequencia};
		public function set frequencia(frequencia: Number): void{this._frequencia = frequencia}; 
		
		public function get isExecutado(): String{return _isExecutado};
		public function set isExecutado(isExecutado: String): void{this._isExecutado = isExecutado}
		public function get horasRevisao():Number
		{
			return _horasRevisao;
		}

		public function set horasRevisao(value:Number):void
		{
			_horasRevisao = value;
		}

		public function get tipoInspecao():String
		{
			return _tipoInspecao;
		}

		public function set tipoInspecao(value:String):void
		{
			_tipoInspecao = value;
		}

		public function get numContrato():String
		{
			return _numContrato;
		}

		public function set numContrato(value:String):void
		{
			_numContrato = value;
		}


;
		
		public function ConfigManutencaoHorasBean()
		{
		}
	}
}