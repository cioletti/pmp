package bean
{
	import mx.collections.ArrayCollection;

	[RemoteClass(alias="com.pmp.bean.ContratoComercialBean")]
	public class ContratoComercialBean
	{
		
		private var _id:Number;
		private var _numeroContrato:String;
		private var _contratoConcessao:String;
		private var _familia:String;
		private var _idFamilia:Number;
		private var _modelo:String;
		private var _prefixo:String;
		private var _businessGroup:String;
		private var _numeroSerie:String;
		private var _beginRanger:String;
		private var _endRanger:String;
		private var _horimetro:Number;
		private var _distanciaGerador:Number;
		private var _TA:String;
		private var _qtdParcelas:Number;
		private var _idTipoContrato:Number;
		private var _statusContrato:Number;
		private var _nomeCliente:String;
		private var _cidade:String;
		private var _endereco:String;
		private var _bairro:String;
		private var _razaoSocial:String;
		private var _cnpj:String;
		private var _inscEstadual:String;
		private var _inscMunicipal:String;
		private var _contribuinte:String;
		private var _procurador:String;
		private var _cpf:String;
		private var _contatoComercial:String;
		private var _telComercial:String;
		private var _emailComercial:String;
		private var _faxComercial:String;
		private var _contatoServicos:String;
		private var _telServicos:String;
		private var _emailServicos:String;
		private var _faxServicos:String;
		private var _rangerList:ArrayCollection
		private var _businessGroupList:ArrayCollection;
		private var _prefixoList:ArrayCollection;
		private var _modeloList:ArrayCollection;
		private var _tipoContrato:ArrayCollection;
		private var _statusContratoList:ArrayCollection;
		private var _configTracaoList:ArrayCollection;
		private var _cliente:ClienteBean;
		private var _financiado:String;
		private var _configManutencaoHorasBeanList:ArrayCollection;
		private var _configManutencaoMesesBeanList:ArrayCollection;
		private var _contExcessao:String;
		private var _condExcepcional:String;
		private var _descricaoStatusContrato:String;
		private var _descricaoTipoContrato:String;
		private var _idMotNaoFecContrato:Number;
		private var _hasSendEmail:String;
		private var _valorContrato:String;
		private var _valorSugerido:String;
		private var _codigoCliente:String;
		private var _dataCriacao:String
		private var _uf:String
		private var _cep:String
		private var _siglaStatusContrato:String
		private var _numOs:String;
		private var _serie:String;
		private var _valorContratoConcessao:String;
		private var _isDeletar:Boolean;
		private var _isGeradorStandby:Boolean;
		private var _msg:String;
		private var _nomeFuncionario:String;
		private var _isFindSubstuicaoTributaria:String;
		private var _numDocDbs:String;
		private var _codErroDbs:String;
		private var _msgErroDbs:String;
		private var _fabricante:String;
		private var _idConfigTracao:Number;
		private var _printRevisaoPosPago:Number;
		private var _funcionarioIndicado:String;
		private var _matriculaIndicado:String;
		private var _tipoCustomizacaoList:ArrayCollection;
		private var _idConfigPreco:Number;
		private var _emailCheckList:String;
		private var _precoEspecialList:ArrayCollection;
		private var _siglaClassificacaoContrato:String;
		private var _isTerritorio:String;
		private var _dataPreEntrega:String;
		private var _periodoGarantia:Number;
		private var _anosContrato:Number;
		private var _mediaHorasMes:Number;
		private var _possuiHorasMes:String;
		private var _isSpot:String;
		private var _descPdpSpot:String;
		private var _horasDeslocamentoSpot:String;
		


		public function get idFamilia():Number
		{
			return _idFamilia;
		}

		public function set idFamilia(value:Number):void
		{
			_idFamilia = value;
		}

		public function get hasSendEmail(): String{return _hasSendEmail}
		public function set hasSendEmail(hasSendEmail: String): void{this._hasSendEmail = hasSendEmail}
		
		public function get idMotNaoFecContrato(): Number{return _idMotNaoFecContrato}
		public function set idMotNaoFecContrato(idMotNaoFecContrato: Number): void{this._idMotNaoFecContrato = idMotNaoFecContrato}
		
		public function get descricaoTipoContrato(): String{return _descricaoTipoContrato}
		public function set descricaoTipoContrato(descricaoTipoContrato: String): void{this._descricaoTipoContrato = descricaoTipoContrato}

		public function get descricaoStatusContrato(): String{return _descricaoStatusContrato}
		public function set descricaoStatusContrato(descricaoStatusContrato: String): void{this._descricaoStatusContrato = descricaoStatusContrato}

		public function get financiado(): String{return _financiado}
		public function set financiado(financiado: String): void{this._financiado = financiado}
		
		public function get id(): Number{return _id}
		public function set id(id: Number): void{this._id = id}

		public function get numeroContrato(): String{return _numeroContrato}
		public function set numeroContrato(numeroContrato: String): void{this._numeroContrato = numeroContrato}
		
		public function get contratoConcessao(): String{return _contratoConcessao}
		public function set contratoConcessao(contratoConcessao: String): void{this._contratoConcessao = contratoConcessao}
		
		public function get familia(): String{return _familia}
		public function set familia(familia: String): void{this._familia = familia}
		
		public function get modelo(): String{return _modelo}
		public function set modelo(modelo: String): void{this._modelo = modelo}
		
		public function get prefixo(): String{return _prefixo}
		public function set prefixo(prefixo: String): void{this._prefixo = prefixo}
		
		public function get businessGroup(): String{return _businessGroup}
		public function set businessGroup(businessGroup: String): void{this._businessGroup = businessGroup}
		
		public function get numeroSerie(): String{return _numeroSerie}
		public function set numeroSerie(numeroSerie: String): void{this._numeroSerie = numeroSerie}
		
		public function get beginRanger(): String{return _beginRanger}
		public function set beginRanger(beginRanger: String): void{this._beginRanger = beginRanger}
		
		public function get endRanger(): String{return _endRanger}
		public function set endRanger(endRanger: String): void{this._endRanger = endRanger}
		
		public function get horimetro(): Number{return _horimetro}
		public function set horimetro(horimetro: Number): void{this._horimetro = horimetro}

		public function get distanciaGerador(): Number{return _distanciaGerador}
		public function set distanciaGerador(distanciaGerador: Number): void{this._distanciaGerador = distanciaGerador}
		
		public function get TA(): String{return _TA}
		public function set TA(TA: String): void{this._TA = TA}
		
		public function get qtdParcelas(): Number{return _qtdParcelas}
		public function set qtdParcelas(qtdParcelas: Number): void{this._qtdParcelas = qtdParcelas}
		
		public function get idTipoContrato(): Number{return _idTipoContrato}
		public function set idTipoContrato(idTipoContrato: Number): void{this._idTipoContrato = idTipoContrato}
		
		public function get statusContrato(): Number{return _statusContrato}
		public function set statusContrato(statusContrato: Number): void{this._statusContrato = statusContrato}
		
		public function get nomeCliente(): String{return _nomeCliente}
		public function set nomeCliente(nomeCliente: String): void{this._nomeCliente = nomeCliente}
		
		public function get cidade(): String{return _cidade}
		public function set cidade(cidade: String): void{this._cidade = cidade}
		
		public function get endereco(): String{return _endereco}
		public function set endereco(endereco: String): void{this._endereco = endereco}
		
		public function get bairro(): String{return _bairro}
		public function set bairro(bairro: String): void{this._bairro = bairro}
		
		public function get razaoSocial(): String{return _razaoSocial}
		public function set razaoSocial(razaoSocial: String): void{this._razaoSocial = razaoSocial}
		
		public function get cnpj(): String{return _cnpj}
		public function set cnpj(cnpj: String): void{this._cnpj = cnpj}
		
		public function get inscEstadual(): String{return _inscEstadual}
		public function set inscEstadual(inscEstadual: String): void{this._inscEstadual = inscEstadual}
		
		public function get inscMunicipal(): String{return _inscMunicipal}
		public function set inscMunicipal(inscMunicipal: String): void{this._inscMunicipal = inscMunicipal}
		
		public function get contribuinte(): String{return _contribuinte}
		public function set contribuinte(contribuinte: String): void{this._contribuinte = contribuinte}
		
		public function get procurador(): String{return _procurador}
		public function set procurador(procurador: String): void{this._procurador = procurador}
		
		public function get cpf(): String{return _cpf}
		public function set cpf(cpf: String): void{this._cpf = cpf}
		
		public function get contatoComercial(): String{return _contatoComercial}
		public function set contatoComercial(contatoComercial: String): void{this._contatoComercial = contatoComercial}
		
		public function get telComercial(): String{return _telComercial}
		public function set telComercial(telComercial: String): void{this._telComercial = telComercial}
		
		public function get emailComercial(): String{return _emailComercial}
		public function set emailComercial(emailComercial: String): void{this._emailComercial = emailComercial}

		public function get faxComercial(): String{return _faxComercial}
		public function set faxComercial(faxComercial: String): void{this._faxComercial = faxComercial}
		
		public function get contatoServicos(): String{return _contatoServicos}
		public function set contatoServicos(contatoServicos: String): void{this._contatoServicos = contatoServicos}
		
		public function get telServicos(): String{return _telServicos}
		public function set telServicos(telServicos: String): void{this._telServicos = telServicos}
		
		public function get emailServicos(): String{return _emailServicos}
		public function set emailServicos(emailServicos: String): void{this._emailServicos = emailServicos}
		
		public function get faxServicos(): String{return _faxServicos}
		public function set faxServicos(faxServicos: String): void{this._faxServicos = faxServicos}
		
		public function get rangerList(): ArrayCollection{return _rangerList}
		public function set rangerList(rangerList: ArrayCollection): void{this._rangerList = rangerList}
		
		public function get businessGroupList(): ArrayCollection{return _businessGroupList}
		public function set businessGroupList(businessGroupList: ArrayCollection): void{this._businessGroupList = businessGroupList}
		
		public function get prefixoList(): ArrayCollection{return _prefixoList}
		public function set prefixoList(prefixoList: ArrayCollection): void{this._prefixoList = prefixoList}
		
		public function get modeloList(): ArrayCollection{return _modeloList}
		public function set modeloList(modeloList: ArrayCollection): void{this._modeloList = modeloList}
		
		public function get tipoContrato(): ArrayCollection{return _tipoContrato}
		public function set tipoContrato(tipoContrato: ArrayCollection): void{this._tipoContrato = tipoContrato}

		public function get statusContratoList(): ArrayCollection{return _statusContratoList}
		public function set statusContratoList(statusContratoList: ArrayCollection): void{this._statusContratoList = statusContratoList}

		public function get cliente(): ClienteBean{return _cliente}
		public function set cliente(cliente: ClienteBean): void{this._cliente = cliente}

		public function get configManutencaoHorasBeanList(): ArrayCollection{return _configManutencaoHorasBeanList}
		public function set configManutencaoHorasBeanList(configManutencaoHorasBeanList: ArrayCollection): void{this._configManutencaoHorasBeanList = configManutencaoHorasBeanList}
		
		public function get configManutencaoMesesBeanList(): ArrayCollection{return _configManutencaoMesesBeanList}
		public function set configManutencaoMesesBeanList(configManutencaoMesesBeanList: ArrayCollection): void{this._configManutencaoMesesBeanList = configManutencaoMesesBeanList}
		
		public function get contExcessao(): String{return _contExcessao}
		public function set contExcessao(contExcessao: String): void{this._contExcessao = contExcessao}

		public function get condExcepcional(): String{return _condExcepcional}
		public function set condExcepcional(condExcepcional: String): void{this._condExcepcional = condExcepcional}

		public function get valorContrato(): String{return _valorContrato}
		public function set valorContrato(valorContrato: String): void{this._valorContrato = valorContrato}

		public function get codigoCliente(): String{return _codigoCliente}
		public function set codigoCliente(codigoCliente: String): void{this._codigoCliente = codigoCliente}

		public function get dataCriacao(): String{return _dataCriacao}
		public function set dataCriacao(dataCriacao: String): void{this._dataCriacao = dataCriacao}

		public function get uf(): String{return _uf}
		public function set uf(uf: String): void{this._uf = uf}

		public function get cep(): String{return _cep}
		public function set cep(cep: String): void{this._cep = cep}

		public function get siglaStatusContrato(): String{return _siglaStatusContrato}
		public function set siglaStatusContrato(siglaStatusContrato: String): void{this._siglaStatusContrato = siglaStatusContrato}

		public function get numOs(): String{return _numOs}
		public function set numOs(numOs: String): void{this._numOs = numOs}

		public function get serie(): String{return _serie}
		public function set serie(serie: String): void{this._serie = serie}

		public function get valorContratoConcessao(): String{return _valorContratoConcessao}
		public function set valorContratoConcessao(valorContratoConcessao: String): void{this._valorContratoConcessao = valorContratoConcessao}

		public function get isDeletar(): Boolean{return _isDeletar}
		public function set isDeletar(isDeletar: Boolean): void{this._isDeletar = isDeletar}

		public function get isGeradorStandby(): Boolean{return _isGeradorStandby}
		public function set isGeradorStandby(isGeradorStandby: Boolean): void{this._isGeradorStandby = isGeradorStandby}

		public function get msg(): String{return _msg}
		public function set msg(msg: String): void{this._msg = msg}
		
		public function get nomeFuncionario():String
		{
			return _nomeFuncionario;
		}

		public function set nomeFuncionario(value:String):void
		{
			_nomeFuncionario = value;
		}

		public function get isFindSubstuicaoTributaria()
		{
			return _isFindSubstuicaoTributaria;
		}

		public function set isFindSubstuicaoTributaria(value):void
		{
			_isFindSubstuicaoTributaria = value;
		}

		public function get numDocDbs():String
		{
			return _numDocDbs;
		}

		public function set numDocDbs(value:String):void
		{
			_numDocDbs = value;
		}

		public function get codErroDbs():String
		{
			return _codErroDbs;
		}

		public function set codErroDbs(value:String):void
		{
			_codErroDbs = value;
		}

		public function get msgErroDbs():String
		{
			return _msgErroDbs;
		}

		public function set msgErroDbs(value:String):void
		{
			_msgErroDbs = value;
		}

		public function get fabricante():String
		{
			return _fabricante;
		}

		public function set fabricante(value:String):void
		{
			_fabricante = value;
		}

		public function get idConfigTracao():Number
		{
			return _idConfigTracao;
		}

		public function set idConfigTracao(value:Number):void
		{
			_idConfigTracao = value;
		}

		public function get configTracaoList():ArrayCollection
		{
			return _configTracaoList;
		}

		public function set configTracaoList(value:ArrayCollection):void
		{
			_configTracaoList = value;
		}

		public function get valorSugerido():String
		{
			return _valorSugerido;
		}

		public function set valorSugerido(value:String):void
		{
			_valorSugerido = value;
		}

		public function get printRevisaoPosPago():Number
		{
			return _printRevisaoPosPago;
		}

		public function set printRevisaoPosPago(value:Number):void
		{
			_printRevisaoPosPago = value;
		}

		public function get funcionarioIndicado():String
		{
			return _funcionarioIndicado;
		}

		public function set funcionarioIndicado(value:String):void
		{
			_funcionarioIndicado = value;
		}

		public function get matriculaIndicado():String
		{
			return _matriculaIndicado;
		}

		public function set matriculaIndicado(value:String):void
		{
			_matriculaIndicado = value;
		}

		public function get tipoCustomizacaoList():ArrayCollection
		{
			return _tipoCustomizacaoList;
		}

		public function set tipoCustomizacaoList(value:ArrayCollection):void
		{
			_tipoCustomizacaoList = value;
		}

		public function get idConfigPreco():Number
		{
			return _idConfigPreco;
		}

		public function set idConfigPreco(value:Number):void
		{
			_idConfigPreco = value;
		}

		public function get emailCheckList():String
		{
			return _emailCheckList;
		}

		public function set emailCheckList(value:String):void
		{
			_emailCheckList = value;
		}

		public function get precoEspecialList():ArrayCollection
		{
			return _precoEspecialList;
		}

		public function set precoEspecialList(value:ArrayCollection):void
		{
			_precoEspecialList = value;
		}

		public function get siglaClassificacaoContrato():String
		{
			return _siglaClassificacaoContrato;
		}

		public function set siglaClassificacaoContrato(value:String):void
		{
			_siglaClassificacaoContrato = value;
		}

		public function get isTerritorio():String
		{
			return _isTerritorio;
		}

		public function set isTerritorio(value:String):void
		{
			_isTerritorio = value;
		}

		public function get dataPreEntrega():String
		{
			return _dataPreEntrega;
		}

		public function set dataPreEntrega(value:String):void
		{
			_dataPreEntrega = value;
		}

		public function get periodoGarantia():Number
		{
			return _periodoGarantia;
		}

		public function set periodoGarantia(value:Number):void
		{
			_periodoGarantia = value;
		}

		public function get anosContrato():Number
		{
			return _anosContrato;
		}

		public function set anosContrato(value:Number):void
		{
			_anosContrato = value;
		}

		public function get mediaHorasMes():Number
		{
			return _mediaHorasMes;
		}

		public function set mediaHorasMes(value:Number):void
		{
			_mediaHorasMes = value;
		}

		public function get possuiHorasMes():String
		{
			return _possuiHorasMes;
		}

		public function set possuiHorasMes(value:String):void
		{
			_possuiHorasMes = value;
		}

		public function get isSpot():String
		{
			return _isSpot;
		}

		public function set isSpot(value:String):void
		{
			_isSpot = value;
		}

		public function get descPdpSpot():String
		{
			return _descPdpSpot;
		}

		public function set descPdpSpot(value:String):void
		{
			_descPdpSpot = value;
		}

		public function get horasDeslocamentoSpot():String
		{
			return _horasDeslocamentoSpot;
		}

		public function set horasDeslocamentoSpot(value:String):void
		{
			_horasDeslocamentoSpot = value;
		}

		
		public function ContratoComercialBean()
		{
		}
	}
}