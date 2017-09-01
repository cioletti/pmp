package bean
{
	[RemoteClass(alias="com.pmp.bean.ConfiguracaoPrecosBean")]
	public class ConfiguracaoPrecosBean
	{
		
		private var _id:Number;
		private var _descricao:String;
		private var _hhRental:String;
		private var _hhPmp:String;
		private var _kmRental:Number;
		private var _kmPmp:Number;
		private var _custoNordeste:String;
		private var _validadeContrato:Number;
		private var _jurosVendaContrato:String;
		private var _valorKmPmp:String;
		private var _valorKmRental:String;
		private var _hhTa:String;
		private var _horasTa:String;
		private var _descPdp:String;
		private var _valorHhPmpCusto:String;
		private var _valorKmPmpCusto:String;
		private var _valorHhTaCusto:String;
		private var _descontoPrePago:String;
		private var _comissaoConsultor:String;
		private var _comissaoIndicacao:String;
		private var _sos:String;
		private var _valorTa:String;
		private var _monitoramento:String;
		private var _descontoPecas:String;
		private var _valorContratoPlus:String;
		private var _valorSpot:String;
		private var _kmPmpSpot:Number;
		private var _descPdpSpot:String;
		private var _valorHorasDesSpot:String;
		
		
		public function get descPdpSpot():String
		{
			return _descPdpSpot;
		}

		public function set descPdpSpot(value:String):void
		{
			_descPdpSpot = value;
		}

		public function get valorHhPmpCusto(): String{return _valorHhPmpCusto};
		public function set valorHhPmpCusto(valorHhPmpCusto: String): void{this._valorHhPmpCusto = valorHhPmpCusto}; 
		
		public function get valorKmPmpCusto(): String{return _valorKmPmpCusto};
		public function set valorKmPmpCusto(valorKmPmpCusto: String): void{this._valorKmPmpCusto = valorKmPmpCusto}; 
		
		public function get valorHhTaCusto(): String{return _valorHhTaCusto};
		public function set valorHhTaCusto(valorHhTaCusto: String): void{this._valorHhTaCusto = valorHhTaCusto}; 
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 
		
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 

		public function get hhRental(): String{return _hhRental};
		public function set hhRental(hhRental: String): void{this._hhRental = hhRental}; 
		
		public function get hhPmp(): String{return _hhPmp};
		public function set hhPmp(hhPmp: String): void{this._hhPmp = hhPmp}; 
		
		public function get kmRental(): Number{return _kmRental};
		public function set kmRental(kmRental: Number): void{this._kmRental = kmRental}; 
		
		public function get kmPmp(): Number{return _kmPmp};
		public function set kmPmp(kmPmp: Number): void{this._kmPmp = kmPmp}; 
		
		public function get custoNordeste(): String{return _custoNordeste};
		public function set custoNordeste(custoNordeste: String): void{this._custoNordeste = custoNordeste}; 
		
		public function get validadeContrato(): Number{return _validadeContrato};
		public function set validadeContrato(validadeContrato: Number): void{this._validadeContrato = validadeContrato}; 
		
		public function get valorKmPmp(): String{return _valorKmPmp};
		public function set valorKmPmp(valorKmPmp: String): void{this._valorKmPmp = valorKmPmp}; 

		public function get valorKmRental(): String{return _valorKmRental};
		public function set valorKmRental(valorKmRental: String): void{this._valorKmRental = valorKmRental}; 

		public function get jurosVendaContrato(): String{return _jurosVendaContrato};
		public function set jurosVendaContrato(jurosVendaContrato: String): void{this._jurosVendaContrato = jurosVendaContrato}; 
		
		public function get horasTa(): String{return _horasTa};
		public function set horasTa(horasTa: String): void{this._horasTa = horasTa}; 
		
		public function get hhTa(): String{return _hhTa};
		public function set hhTa(hhTa: String): void{this._hhTa = hhTa}; 
		
		
		public function get descPdp(): String{return _descPdp};
		public function set descPdp(descPdp: String): void{this._descPdp = descPdp}
		public function get descontoPrePago():String{return _descontoPrePago;}
		public function set descontoPrePago(descontoPrePago:String):void{	this._descontoPrePago = descontoPrePago;} 

		public function get comissaoConsultor():String
		{
			return _comissaoConsultor;
		}

		public function set comissaoConsultor(value:String):void
		{
			_comissaoConsultor = value;
		}

		public function get comissaoIndicacao():String
		{
			return _comissaoIndicacao;
		}

		public function set comissaoIndicacao(value:String):void
		{
			_comissaoIndicacao = value;
		}

		public function get sos():String
		{
			return _sos;
		}

		public function set sos(value:String):void
		{
			_sos = value;
		}

		public function get valorTa():String
		{
			return _valorTa;
		}

		public function set valorTa(value:String):void
		{
			_valorTa = value;
		}

		public function get monitoramento():String
		{
			return _monitoramento;
		}

		public function set monitoramento(value:String):void
		{
			_monitoramento = value;
		}

		public function get descontoPecas():String
		{
			return _descontoPecas;
		}

		public function set descontoPecas(value:String):void
		{
			_descontoPecas = value;
		}

		public function get valorContratoPlus():String
		{
			return _valorContratoPlus;
		}

		public function set valorContratoPlus(value:String):void
		{
			_valorContratoPlus = value;
		}

		public function get valorSpot():String
		{
			return _valorSpot;
		}

		public function set valorSpot(value:String):void
		{
			_valorSpot = value;
		}

		public function get kmPmpSpot():Number
		{
			return _kmPmpSpot;
		}

		public function set kmPmpSpot(value:Number):void
		{
			_kmPmpSpot = value;
		}

		public function get valorHorasDesSpot():String
		{
			return _valorHorasDesSpot;
		}

		public function set valorHorasDesSpot(value:String):void
		{
			_valorHorasDesSpot = value;
		}


		
		public function ConfiguracaoPrecosBean()
		{
		}
	}
}