package bean
{
	[RemoteClass(alias="com.pmp.bean.OperacionalBean")]
	public class OperacionalBean
	{
		private var _contratoId:Number;
		private var _modelo:String;
		private var _numeroSerie:String;
		private var _horimetro:Number;
		private var _horasPendentes:String;
		private var _proximaRevisao:String;
		private var _dataAtualizacaoHorimetro:String;
		private var _numOs:String;
		private var _numeroContrato:String;
		private var _dataContrato:String;
		private var _situacao:String;
		private var _situacaoImagem:String;
		private var _codigoCliente:String;
		private var _valorContrato:String;
		private var _numeroParcela:String;		
		private var _nomeCliente:String;
		private var _revPendentes:String;
		private var _consultor:String;
		private var _origem:String;
		private var _destino:String;
		private var _plAtivo:String;
		private var _siglaTipoContrato:String;
		private var _filial:String;
		private var _ta:String;
		private var _codErroDbs:String;
		private var _numLinhas:Number;
		private var _diasProximaRevisao:String;
		private var _siglaClassificacaoContrato:String;
		private var _mediaDiasProximaRevisao:String;
		
		private var _prefixo:String;
		private var _contExcessao:String;
		private var _idConfiguracaoPreco:Number;
		private var _beginRanger:String;
		private var _endRanger:String;
		
		public function get codErroDbs():String
		{
			return _codErroDbs;
		}

		public function set codErroDbs(value:String):void
		{
			_codErroDbs = value;
		}

		public function get contratoId(): Number{return _contratoId};
		public function set contratoId(contratoId: Number): void{this._contratoId = contratoId};
						
		public function get modelo(): String{return _modelo};
		public function set modelo(modelo: String): void{this._modelo = modelo}; 
		
		public function get numeroSerie(): String{return _numeroSerie};
		public function set numeroSerie(numeroSerie: String): void{this._numeroSerie = numeroSerie};
		
		public function get horimetro(): Number{return _horimetro};
		public function set horimetro(horimetro: Number): void{this._horimetro = horimetro};
		
		public function get horasPendentes(): String{return _horasPendentes};
		public function set horasPendentes(horasPendentes: String): void{this._horasPendentes = horasPendentes};
		
		public function get proximaRevisao(): String{return _proximaRevisao};
		public function set proximaRevisao(proximaRevisao: String): void{this._proximaRevisao = proximaRevisao};
		
		public function get dataAtualizacaoHorimetro(): String{return _dataAtualizacaoHorimetro};
		public function set dataAtualizacaoHorimetro(dataAtualizacaoHorimetro: String): void{this._dataAtualizacaoHorimetro = dataAtualizacaoHorimetro};
		
		public function get numOs(): String{return _numOs};
		public function set numOs(numOs: String): void{this._numOs = numOs};
		
		public function get numeroContrato(): String{return _numeroContrato};
		public function set numeroContrato(numeroContrato: String): void{this._numeroContrato = numeroContrato};
		
		public function get dataContrato(): String{return _dataContrato};
		public function set dataContrato(dataContrato: String): void{this._dataContrato = dataContrato};
		
		public function get situacao(): String{return _situacao};
		public function set situacao(situacao: String): void{this._situacao = situacao}; 
		
		public function get situacaoImagem(): String{return _situacaoImagem};
		public function set situacaoImagem(situacaoImagem: String): void{this._situacaoImagem = situacaoImagem};

		public function get codigoCliente(): String{return _codigoCliente};
		public function set codigoCliente(codigoCliente: String): void{this._codigoCliente = codigoCliente};
		
		public function get valorContrato(): String{return _valorContrato};
		public function set valorContrato(valorContrato: String): void{this._valorContrato = valorContrato};
		
		public function get numeroParcela(): String{return _numeroParcela};
		public function set numeroParcela(numeroParcela: String): void{this._numeroParcela = numeroParcela};

		public function get nomeCliente(): String{return _nomeCliente};
		public function set nomeCliente(nomeCliente: String): void{this._nomeCliente = nomeCliente};
		
		public function get revPendentes(): String{return _revPendentes};
		public function set revPendentes(revPendentes: String): void{this._revPendentes = revPendentes};
		
		public function get consultor(): String{return _consultor};
		public function set consultor(consultor: String): void{this._consultor = consultor};
		
		public function get origem(): String{return _origem};
		public function set origem(origem: String): void{this._origem = origem};
		
		public function get destino(): String{return _destino};
		public function set destino(destino: String): void{this._destino = destino};
		
		public function get plAtivo(): String{return _plAtivo};
		public function set plAtivo(plAtivo: String): void{this._plAtivo = plAtivo};

		public function get siglaTipoContrato(): String{return _siglaTipoContrato};
		public function set siglaTipoContrato(siglaTipoContrato: String): void{this._siglaTipoContrato = siglaTipoContrato};

		public function get filial(): String{return _filial};
		public function set filial(filial: String): void{this._filial = filial};

		public function get ta(): String{return _ta};
		public function set ta(ta: String): void{this._ta = ta};
		
		public function get numLinhas():Number
		{
			return _numLinhas;
		}

		public function set numLinhas(value:Number):void
		{
			_numLinhas = value;
		}

		public function get diasProximaRevisao():String
		{
			return _diasProximaRevisao;
		}

		public function set diasProximaRevisao(value:String):void
		{
			_diasProximaRevisao = value;
		}

		public function get siglaClassificacaoContrato():String
		{
			return _siglaClassificacaoContrato;
		}

		public function set siglaClassificacaoContrato(value:String):void
		{
			_siglaClassificacaoContrato = value;
		}

		public function get mediaDiasProximaRevisao():String
		{
			return _mediaDiasProximaRevisao;
		}

		public function set mediaDiasProximaRevisao(value:String):void
		{
			_mediaDiasProximaRevisao = value;
		}

		public function get prefixo():String
		{
			return _prefixo;
		}

		public function set prefixo(value:String):void
		{
			_prefixo = value;
		}

		public function get contExcessao():String
		{
			return _contExcessao;
		}

		public function set contExcessao(value:String):void
		{
			_contExcessao = value;
		}

		public function get idConfiguracaoPreco():Number
		{
			return _idConfiguracaoPreco;
		}

		public function set idConfiguracaoPreco(value:Number):void
		{
			_idConfiguracaoPreco = value;
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

	

			
		public function OperacionalBean()
		{
		}
	}
}