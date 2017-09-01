package bean
{
	import mx.collections.ArrayCollection;

	[RemoteClass(alias="com.pmp.bean.ConfigOperacionalBean")]
	public class ConfigOperacionalBean
	{
		private var _id:Number;
		private var _idContrato:Number;
	    private var _contrato:ContratoComercialBean;
		private var _numOs:String;
		private var _idFuncSupervisor:String;
		private var _contato:String;
		private var _local:String;
		private var _telefoneContato:String;
		private var _filial:Number;
		private var _email:String;
		private var _numeroSerie:String;
		private var _obsCliente:String;
		private var _filialList:ArrayCollection;
		
		private var _beginRanger:String;
		private var _endRanger:String;
		
		private var _tipoCustomizacaoList:ArrayCollection;
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 
		
		public function get idContrato(): Number{return _idContrato};
		public function set idContrato(idContrato: Number): void{this._idContrato = idContrato}; 
		
		public function get contrato(): ContratoComercialBean{return _contrato};
		public function set contrato(contrato: ContratoComercialBean): void{this._contrato = contrato}; 
		
		public function get numOs(): String{return _numOs};
		public function set numOs(numOs: String): void{this._numOs = numOs}; 
		
		public function get idFuncSupervisor(): String{return _idFuncSupervisor};
		public function set idFuncSupervisor(idFuncSupervisor: String): void{this._idFuncSupervisor = idFuncSupervisor}; 
		
		public function get contato(): String{return _contato};
		public function set contato(contato: String): void{this._contato = contato}; 
		
		public function get local(): String{return _local};
		public function set local(local: String): void{this._local = local}; 
		
		public function get telefoneContato(): String{return _telefoneContato};
		public function set telefoneContato(telefoneContato: String): void{this._telefoneContato = telefoneContato}; 
		
		public function get filial(): Number{return _filial};
		public function set filial(filial: Number): void{this._filial = filial}; 
		
		public function get filialList(): ArrayCollection{return _filialList};
		public function set filialList(filialList: ArrayCollection): void{this._filialList = filialList};

		public function get email(): String{return _email};
		public function set email(email: String): void{this._email= email};

		public function get numeroSerie(): String{return _numeroSerie};
		public function set numeroSerie(numeroSerie: String): void{this._numeroSerie= numeroSerie};
		
		public function get obsCliente():String
		{
			return _obsCliente;
		}

		public function set obsCliente(value:String):void
		{
			_obsCliente = value;
		}

		public function get beginRanger():String
		{
			return _beginRanger;
		}

		public function set beginRanger(value:String):void
		{
			_beginRanger = value;
		}

		public function get endRanger():String
		{
			return _endRanger;
		}

		public function set endRanger(value:String):void
		{
			_endRanger = value;
		}

		public function get tipoCustomizacaoList():ArrayCollection
		{
			return _tipoCustomizacaoList;
		}

		public function set tipoCustomizacaoList(value:ArrayCollection):void
		{
			_tipoCustomizacaoList = value;
		}

		
		public function ConfigOperacionalBean()
		{
			tipoCustomizacaoList = new ArrayCollection();
		}
	}
}