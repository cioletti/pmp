package bean
{
	import mx.collections.ArrayCollection;

	[RemoteClass(alias="com.pmp.bean.AgendamentoBean")]
	public class AgendamentoBean
	{
		private var _id:Number;
		private var _idContrato:Number;
		private var _numSerie:String;
		private var _horimetro:Number;
		private var _horasPendentes:Number;
		private var _codigoCliente:String;
		private var _modelo:String;
		private var _idStatusAgendamento:Number;
		private var _idContHorasStandard:Number;
		private var _idFuncionario:String;
		private var _idConfOperacional:Number;
		private var _horasRevisao:Number;
		private var _dataAgendamento:String;
		private var _dataAgendamentoFinal:String;
		private var _siglaStatus:String;
		private var _numOs:String;
		private var _agendamentoList:ArrayCollection;
		private var _local:String;
		private var _contato:String;
		private var _numContrato:String;
		private var _telefone:String;
		private var _statusAgendamento:String;
		private var _standardJob:String;
		private var _filial:String;
		private var _filialDestino:String;
		private var _siglaTipoContrato:String;
		private var _pecasList:ArrayCollection;
		private var _dataAtualizacaoHorimetro:String;
		private var _razaoSocial:String;
		private var _numDoc:String;
		private var _msg:String;
		private var _codErroOsDbs:String;
		private var _codErroDocDbs:String;
		private var _idOsOperacional:Number;
		private var _isFindTecnico:String;
		private var _obs:String;
		private var _totalRegistros:Number;
		private var _dataFaturamento:String;
		private var _obsOs:String;
		private var _idAjudante:String;
		private var _nomeAjudante:String;
		private var _horasTrabalhadas:String;
		private var _obsCheckList:String;
		private var _obsTecnico:String;
		private var _emailContato:String;
		private var _diasProximaRevisao:Number;
		private var _siglaClassificacaoContrato:String;
		private var _isPartner:String;
		private var _mediaDiasProximaRevisao:String;
		
		
		private var _moMiscDesl:String;
		private var _valorPecas:String;
		private var _valorTotal:String;
		private var _condicaoPagamento:String;
		private var _propNumero:String;
		private var _autPor:String;
		private var _ordemCompra:String;
		private var _preenchidoPor:String;
		private var _obsNf:String;
		
		
		private var _ordemCompraPeca:String;
		private var _ordemCompraServico:String;
		
		private var _estabelecimentoCredorPecas:String;
		private var _estabelecimentoCredorServicos:String;
		
		private var _condicaoPagamentoPecas:String;
		private var _condicaoPagamentoServicos:String;
		
		private var _descPorcPecas:String;
		private var _descPorcServicos:String;
		
		private var _descValorPecas:String;
		private var _descValorServicos:String;
		
		private var _automaticaFaturada:String;
		
		private var _obsPeca:String;
		private var _obsServico:String;
		
		private var _condEspecial:String;
		
		private var _credorPeca:Number;
		private var _credorServico:Number;
		private var _jobCode:String;
		private var _compCode:String;
		
		private var _horimetroUltimaRevisao:String;
		private var _horimetroProximaRevisao:String;
		private var _horimetroFaltantes:String;
		

		
		public function get horimetroFaltantes():String
		{
			return _horimetroFaltantes;
		}

		public function set horimetroFaltantes(value:String):void
		{
			_horimetroFaltantes = value;
		}

		public function get horimetroProximaRevisao():String
		{
			return _horimetroProximaRevisao;
		}

		public function set horimetroProximaRevisao(value:String):void
		{
			_horimetroProximaRevisao = value;
		}

		public function get horimetroUltimaRevisao():String
		{
			return _horimetroUltimaRevisao;
		}

		public function set horimetroUltimaRevisao(value:String):void
		{
			_horimetroUltimaRevisao = value;
		}

		public function get compCode():String
		{
			return _compCode;
		}

		public function set compCode(value:String):void
		{
			_compCode = value;
		}

		public function get jobCode():String
		{
			return _jobCode;
		}

		public function set jobCode(value:String):void
		{
			_jobCode = value;
		}

		public function get credorServico():Number
		{
			return _credorServico;
		}

		public function set credorServico(value:Number):void
		{
			_credorServico = value;
		}

		public function get credorPeca():Number
		{
			return _credorPeca;
		}

		public function set credorPeca(value:Number):void
		{
			_credorPeca = value;
		}

		public function get condEspecial():String
		{
			return _condEspecial;
		}

		public function set condEspecial(value:String):void
		{
			_condEspecial = value;
		}

		public function get obsServico():String
		{
			return _obsServico;
		}

		public function set obsServico(value:String):void
		{
			_obsServico = value;
		}

		public function get obsPeca():String
		{
			return _obsPeca;
		}

		public function set obsPeca(value:String):void
		{
			_obsPeca = value;
		}

		public function get automaticaFaturada():String
		{
			return _automaticaFaturada;
		}

		public function set automaticaFaturada(value:String):void
		{
			_automaticaFaturada = value;
		}

		public function get descValorServicos():String
		{
			return _descValorServicos;
		}

		public function set descValorServicos(value:String):void
		{
			_descValorServicos = value;
		}

		public function get descValorPecas():String
		{
			return _descValorPecas;
		}

		public function set descValorPecas(value:String):void
		{
			_descValorPecas = value;
		}

		public function get descPorcServicos():String
		{
			return _descPorcServicos;
		}

		public function set descPorcServicos(value:String):void
		{
			_descPorcServicos = value;
		}

		public function get descPorcPecas():String
		{
			return _descPorcPecas;
		}

		public function set descPorcPecas(value:String):void
		{
			_descPorcPecas = value;
		}

		public function get condicaoPagamentoServicos():String
		{
			return _condicaoPagamentoServicos;
		}

		public function set condicaoPagamentoServicos(value:String):void
		{
			_condicaoPagamentoServicos = value;
		}

		public function get condicaoPagamentoPecas():String
		{
			return _condicaoPagamentoPecas;
		}

		public function set condicaoPagamentoPecas(value:String):void
		{
			_condicaoPagamentoPecas = value;
		}

		public function get estabelecimentoCredorServicos():String
		{
			return _estabelecimentoCredorServicos;
		}

		public function set estabelecimentoCredorServicos(value:String):void
		{
			_estabelecimentoCredorServicos = value;
		}

		public function get estabelecimentoCredorPecas():String
		{
			return _estabelecimentoCredorPecas;
		}

		public function set estabelecimentoCredorPecas(value:String):void
		{
			_estabelecimentoCredorPecas = value;
		}

		public function get ordemCompraServico():String
		{
			return _ordemCompraServico;
		}

		public function set ordemCompraServico(value:String):void
		{
			_ordemCompraServico = value;
		}

		public function get ordemCompraPeca():String
		{
			return _ordemCompraPeca;
		}

		public function set ordemCompraPeca(value:String):void
		{
			_ordemCompraPeca = value;
		}

		public function get obsNf():String
		{
			return _obsNf;
		}

		public function set obsNf(value:String):void
		{
			_obsNf = value;
		}

		public function get preenchidoPor():String
		{
			return _preenchidoPor;
		}

		public function set preenchidoPor(value:String):void
		{
			_preenchidoPor = value;
		}

		public function get ordemCompra():String
		{
			return _ordemCompra;
		}

		public function set ordemCompra(value:String):void
		{
			_ordemCompra = value;
		}

		public function get autPor():String
		{
			return _autPor;
		}

		public function set autPor(value:String):void
		{
			_autPor = value;
		}

		public function get propNumero():String
		{
			return _propNumero;
		}

		public function set propNumero(value:String):void
		{
			_propNumero = value;
		}

		public function get condicaoPagamento():String
		{
			return _condicaoPagamento;
		}

		public function set condicaoPagamento(value:String):void
		{
			_condicaoPagamento = value;
		}

		public function get valorTotal():String
		{
			return _valorTotal;
		}

		public function set valorTotal(value:String):void
		{
			_valorTotal = value;
		}

		public function get valorPecas():String
		{
			return _valorPecas;
		}

		public function set valorPecas(value:String):void
		{
			_valorPecas = value;
		}

		public function get moMiscDesl():String
		{
			return _moMiscDesl;
		}

		public function set moMiscDesl(value:String):void
		{
			_moMiscDesl = value;
		}

		public function get idOsOperacional():Number
		{
			return _idOsOperacional;
		}

		public function set idOsOperacional(value:Number):void
		{
			_idOsOperacional = value;
		}

		public function get codErroDocDbs():String
		{
			return _codErroDocDbs;
		}

		public function set codErroDocDbs(value:String):void
		{
			_codErroDocDbs = value;
		}

		public function get codErroOsDbs():String
		{
			return _codErroOsDbs;
		}

		public function set codErroOsDbs(value:String):void
		{
			_codErroOsDbs = value;
		}

		public function get msg():String
		{
			return _msg;
		}

		public function set msg(value:String):void
		{
			_msg = value;
		}

		public function get numDoc():String
		{
			return _numDoc;
		}

		public function set numDoc(value:String):void
		{
			_numDoc = value;
		}

		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 
		
		public function get idContrato(): Number{return _idContrato};
		public function set idContrato(idContrato: Number): void{this._idContrato = idContrato}; 

		public function get numSerie(): String{return _numSerie};
		public function set numSerie(numSerie: String): void{this._numSerie = numSerie}; 

		public function get horimetro(): Number{return _horimetro};
		public function set horimetro(horimetro: Number): void{this._horimetro = horimetro}; 

		public function get horasPendentes(): Number{return _horasPendentes};
		public function set horasPendentes(horasPendentes: Number): void{this._horasPendentes = horasPendentes};	

		public function get codigoCliente(): String{return _codigoCliente};
		public function set codigoCliente(codigoCliente: String): void{this._codigoCliente = codigoCliente};	

		public function get idStatusAgendamento(): Number{return _idStatusAgendamento};
		public function set idStatusAgendamento(idStatusAgendamento: Number): void{this._idStatusAgendamento = idStatusAgendamento}; 

		public function get idContHorasStandard(): Number{return _idContHorasStandard};
		public function set idContHorasStandard(idContHorasStandard: Number): void{this._idContHorasStandard = idContHorasStandard}; 
		
		public function get idFuncionario(): String{return _idFuncionario};
		public function set idFuncionario(idFuncionario: String): void{this._idFuncionario = idFuncionario}; 

		public function get idConfOperacional(): Number{return _idConfOperacional};
		public function set idConfOperacional(idConfOperacional: Number): void{this._idConfOperacional = idConfOperacional}; 

		public function get horasRevisao(): Number{return _horasRevisao};
		public function set horasRevisao(horasRevisao: Number): void{this._horasRevisao = horasRevisao}; 
		
		public function get dataAgendamento(): String{return _dataAgendamento};
		public function set dataAgendamento(dataAgendamento: String): void{this._dataAgendamento = dataAgendamento}; 

		public function get dataAgendamentoFinal(): String{return _dataAgendamentoFinal};
		public function set dataAgendamentoFinal(dataAgendamentoFinal: String): void{this._dataAgendamentoFinal = dataAgendamentoFinal}; 

		public function get siglaStatus(): String{return _siglaStatus};
		public function set siglaStatus(siglaStatus: String): void{this._siglaStatus = siglaStatus}; 

		public function get numOs(): String{return _numOs};
		public function set numOs(numOs: String): void{this._numOs = numOs}; 

		public function get local(): String{return _local};
		public function set local(local: String): void{this._local = local}; 

		public function get contato(): String{return _contato};
		public function set contato(contato: String): void{this._contato = contato}; 

		public function get numContrato(): String{return _numContrato};
		public function set numContrato(numContrato: String): void{this._numContrato = numContrato}; 

		public function get telefone(): String{return _telefone};
		public function set telefone(telefone: String): void{this._telefone = telefone}; 

		public function get modelo(): String{return _modelo};
		public function set modelo(modelo: String): void{this._modelo = modelo}; 

		public function get statusAgendamento(): String{return _statusAgendamento};
		public function set statusAgendamento(statusAgendamento: String): void{this._statusAgendamento = statusAgendamento}; 

		public function get dataAtualizacaoHorimetro(): String{return _dataAtualizacaoHorimetro};
		public function set dataAtualizacaoHorimetro(dataAtualizacaoHorimetro: String): void{this._dataAtualizacaoHorimetro = dataAtualizacaoHorimetro}; 

		public function get standardJob(): String{return _standardJob};
		public function set standardJob(standardJob: String): void{this._standardJob = standardJob}; 

		public function get filial(): String{return _filial};
		public function set filial(filial: String): void{this._filial = filial}; 
		
		public function get filialDestino(): String{return _filialDestino};
		public function set filialDestino(filialDestino: String): void{this._filialDestino = filialDestino}; 


		public function get siglaTipoContrato(): String{return _siglaTipoContrato};
		public function set siglaTipoContrato(siglaTipoContrato: String): void{this._siglaTipoContrato = siglaTipoContrato}; 

		public function get agendamentoList(): ArrayCollection{return _agendamentoList};
		public function set agendamentoList(agendamentoList: ArrayCollection): void{this._agendamentoList = agendamentoList}; 

		public function get pecasList(): ArrayCollection{return _pecasList};
		public function set pecasList(pecasList: ArrayCollection): void{this._pecasList = pecasList}; 

		public function get razaoSocial(): String{return _razaoSocial};
		public function set razaoSocial(razaoSocial: String): void{this._razaoSocial = razaoSocial};
		
		public function get isFindTecnico():String
		{
			return _isFindTecnico;
		}

		public function set isFindTecnico(value:String):void
		{
			_isFindTecnico = value;
		}		

		public function get obs():String
		{
			return _obs;
		}

		public function set obs(value:String):void
		{
			_obs = value;
		}

		public function get totalRegistros():Number
		{
			return _totalRegistros;
		}

		public function set totalRegistros(value:Number):void
		{
			_totalRegistros = value;
		}
	
		public function get dataFaturamento():String
		{
			return _dataFaturamento;
		}
		
		public function set dataFaturamento(value:String):void
		{
			_dataFaturamento = value;
		}

		public function get obsOs():String
		{
			return _obsOs;
		}

		public function set obsOs(value:String):void
		{
			_obsOs = value;
		}

		public function get idAjudante():String
		{
			return _idAjudante;
		}

		public function set idAjudante(value:String):void
		{
			_idAjudante = value;
		}

		public function get nomeAjudante():String
		{
			return _nomeAjudante;
		}

		public function set nomeAjudante(value:String):void
		{
			_nomeAjudante = value;
		}

		public function get horasTrabalhadas():String
		{
			return _horasTrabalhadas;
		}

		public function set horasTrabalhadas(value:String):void
		{
			_horasTrabalhadas = value;
		}

		public function get obsCheckList():String
		{
			return _obsCheckList;
		}

		public function set obsCheckList(value:String):void
		{
			_obsCheckList = value;
		}

		public function get obsTecnico():String
		{
			return _obsTecnico;
		}

		public function set obsTecnico(value:String):void
		{
			_obsTecnico = value;
		}

		public function get emailContato():String
		{
			return _emailContato;
		}

		public function set emailContato(value:String):void
		{
			_emailContato = value;
		}

		public function get diasProximaRevisao():Number
		{
			return _diasProximaRevisao;
		}

		public function set diasProximaRevisao(value:Number):void
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

		public function get isPartner():String
		{
			return _isPartner;
		}

		public function set isPartner(value:String):void
		{
			_isPartner = value;
		}

		public function get mediaDiasProximaRevisao():String
		{
			return _mediaDiasProximaRevisao;
		}

		public function set mediaDiasProximaRevisao(value:String):void
		{
			_mediaDiasProximaRevisao = value;
		}

		
		public function AgendamentoBean()
		{
		}
	}
}