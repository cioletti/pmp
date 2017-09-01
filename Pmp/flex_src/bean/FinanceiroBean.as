package bean
{
	import mx.collections.ArrayCollection;

	[RemoteClass(alias="com.pmp.bean.FinanceiroBean")]
	public class FinanceiroBean
	{
		private var _id:Number;
		private var _cliente:String; 
		private var _codCliente:String; 
		private var _creditoCliente:String; 
		private var _idFuncionario:String;
		private var _status:String; 
		private var _idChamado:Number;
		private var _idContrato:Number;
		private var _numeroOs:String;
		private var _observacao:String;
		private var _observacaoLiberacao:String;
		private var _dataLiberacao:String;
		private var _valorLiberado:String;
		private var _funcionarioLiberacao:String;
		private var _dataRejeicao:String;
		private var _idFuncionarioCriador:String;
		private var _data:String;
		private var _nomeFilial:String;
		private var _nomeFuncionario:String;
		private var _vlrEstimado:String;
		private var _idContHorasStandard:Number;
		private var _nomeFuncionarioResponsavel:String;
		private var _idStatusFinanceiro:Number;
		private var _valorManutencaoStd:String;
		
		private var _depositoCliente:String;
		
		private var _siglaCondicaoPagamento:String;
		private var _crdDbs:String;
		private var _siglaMotivoRequisicao:String;

		public function get siglaMotivoRequisicao():String
		{
			return _siglaMotivoRequisicao;
		}

		public function set siglaMotivoRequisicao(value:String):void
		{
			_siglaMotivoRequisicao = value;
		}

		public function get crdDbs():String
		{
			return _crdDbs;
		}

		public function set crdDbs(value:String):void
		{
			_crdDbs = value;
		}

		public function get siglaCondicaoPagamento():String
		{
			return _siglaCondicaoPagamento;
		}

		public function set siglaCondicaoPagamento(value:String):void
		{
			_siglaCondicaoPagamento = value;
		}

		public function get depositoCliente():String
		{
			return _depositoCliente;
		}

		public function set depositoCliente(value:String):void
		{
			_depositoCliente = value;
		}

		public function get id():Number
		{
			return _id;
		}

		public function set id(value:Number):void
		{
			_id = value;
		}

		public function get cliente():String
		{
			return _cliente;
		}

		public function set cliente(value:String):void
		{
			_cliente = value;
		}

		public function get codCliente():String
		{
			return _codCliente;
		}

		public function set codCliente(value:String):void
		{
			_codCliente = value;
		}

		public function get creditoCliente():String
		{
			return _creditoCliente;
		}

		public function set creditoCliente(value:String):void
		{
			_creditoCliente = value;
		}

		public function get idFuncionario():String
		{
			return _idFuncionario;
		}

		public function set idFuncionario(value:String):void
		{
			_idFuncionario = value;
		}

		public function get status():String
		{
			return _status;
		}

		public function set status(value:String):void
		{
			_status = value;
		}

		public function get idChamado():Number
		{
			return _idChamado;
		}

		public function set idChamado(value:Number):void
		{
			_idChamado = value;
		}

		public function get numeroOs():String
		{
			return _numeroOs;
		}

		public function set numeroOs(value:String):void
		{
			_numeroOs = value;
		}

		public function get observacao():String
		{
			return _observacao;
		}

		public function set observacao(value:String):void
		{
			_observacao = value;
		}

		public function get observacaoLiberacao():String
		{
			return _observacaoLiberacao;
		}

		public function set observacaoLiberacao(value:String):void
		{
			_observacaoLiberacao = value;
		}

		public function get dataLiberacao():String
		{
			return _dataLiberacao;
		}

		public function set dataLiberacao(value:String):void
		{
			_dataLiberacao = value;
		}

		public function get valorLiberado():String
		{
			return _valorLiberado;
		}

		public function set valorLiberado(value:String):void
		{
			_valorLiberado = value;
		}

		public function get funcionarioLiberacao():String
		{
			return _funcionarioLiberacao;
		}

		public function set funcionarioLiberacao(value:String):void
		{
			_funcionarioLiberacao = value;
		}

		public function get dataRejeicao():String
		{
			return _dataRejeicao;
		}

		public function set dataRejeicao(value:String):void
		{
			_dataRejeicao = value;
		}

		public function get idFuncionarioCriador():String
		{
			return _idFuncionarioCriador;
		}

		public function set idFuncionarioCriador(value:String):void
		{
			_idFuncionarioCriador = value;
		}

		public function get data():String
		{
			return _data;
		}

		public function set data(value:String):void
		{
			_data = value;
		}

		public function get nomeFilial():String
		{
			return _nomeFilial;
		}

		public function set nomeFilial(value:String):void
		{
			_nomeFilial = value;
		}

		public function get nomeFuncionario():String
		{
			return _nomeFuncionario;
		}

		public function set nomeFuncionario(value:String):void
		{
			_nomeFuncionario = value;
		}

		public function get vlrEstimado():String
		{
			return _vlrEstimado;
		}

		public function set vlrEstimado(value:String):void
		{
			_vlrEstimado = value;
		}

		public function get idContHorasStandard():Number
		{
			return _idContHorasStandard;
		}

		public function set idContHorasStandard(value:Number):void
		{
			_idContHorasStandard = value;
		}

		public function get idContrato():Number
		{
			return _idContrato;
		}

		public function set idContrato(value:Number):void
		{
			_idContrato = value;
		}

		public function get nomeFuncionarioResponsavel():String
		{
			return _nomeFuncionarioResponsavel;
		}

		public function set nomeFuncionarioResponsavel(value:String):void
		{
			_nomeFuncionarioResponsavel = value;
		}

		public function get idStatusFinanceiro():Number
		{
			return _idStatusFinanceiro;
		}

		public function set idStatusFinanceiro(value:Number):void
		{
			_idStatusFinanceiro = value;
		}

		public function get valorManutencaoStd():String
		{
			return _valorManutencaoStd;
		}

		public function set valorManutencaoStd(value:String):void
		{
			_valorManutencaoStd = value;
		}


		


	}
}