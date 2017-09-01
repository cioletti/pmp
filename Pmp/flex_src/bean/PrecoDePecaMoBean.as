package bean
{
	[RemoteClass(alias="com.pmp.bean.ClassificacaoBean")]
	public class ClassificacaoBean
	{
		
		private var _osContrato:String;
		private var _numOs:String;
		private var _numSerie:String;
		private var _numContrato:String;
		private var _horasManutencao:Number;
		private var _tipoPm:String;
		private var _custoPecas:Number;
		private var _custoMo:Number;
		private var _dataAgendamento:String;
		private var _filial:Number;
		private var _filialContrato:Number;
		private var _valorContrato:Number;
		private var _valorNota:Number;
		private var _isExecutado:String;
		private var _dataEncerramento:String;

		public function get osContrato():String
		{
			return _osContrato;
		}

		public function set osContrato(value:String):void
		{
			_osContrato = value;
		}

		public function get numOs():String
		{
			return _numOs;
		}

		public function set numOs(value:String):void
		{
			_numOs = value;
		}

		public function get numSerie():String
		{
			return _numSerie;
		}

		public function set numSerie(value:String):void
		{
			_numSerie = value;
		}

		public function get numContrato():String
		{
			return _numContrato;
		}

		public function set numContrato(value:String):void
		{
			_numContrato = value;
		}

		public function get horasManutencao():Number
		{
			return _horasManutencao;
		}

		public function set horasManutencao(value:Number):void
		{
			_horasManutencao = value;
		}

		public function get tipoPm():String
		{
			return _tipoPm;
		}

		public function set tipoPm(value:String):void
		{
			_tipoPm = value;
		}

		public function get custoPecas():Number
		{
			return _custoPecas;
		}

		public function set custoPecas(value:Number):void
		{
			_custoPecas = value;
		}

		public function get custoMo():Number
		{
			return _custoMo;
		}

		public function set custoMo(value:Number):void
		{
			_custoMo = value;
		}

		public function get dataAgendamento():String
		{
			return _dataAgendamento;
		}

		public function set dataAgendamento(value:String):void
		{
			_dataAgendamento = value;
		}

		public function get filial():Number
		{
			return _filial;
		}

		public function set filial(value:Number):void
		{
			_filial = value;
		}

		public function get filialContrato():Number
		{
			return _filialContrato;
		}

		public function set filialContrato(value:Number):void
		{
			_filialContrato = value;
		}

		public function get valorContrato():Number
		{
			return _valorContrato;
		}

		public function set valorContrato(value:Number):void
		{
			_valorContrato = value;
		}

		public function get valorNota():Number
		{
			return _valorNota;
		}

		public function set valorNota(value:Number):void
		{
			_valorNota = value;
		}

		public function get isExecutado():String
		{
			return _isExecutado;
		}

		public function set isExecutado(value:String):void
		{
			_isExecutado = value;
		}

		public function get dataEncerramento():String
		{
			return _dataEncerramento;
		}

		public function set dataEncerramento(value:String):void
		{
			_dataEncerramento = value;
		}


	}
}