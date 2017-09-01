package bean
{
	import mx.collections.ArrayCollection;

	[RemoteClass(alias="com.pmp.bean.ConfigurarTracaoBean")]
	public class ConfigurarTracaoBean
	{
		private var _id:Number;
		private var _idModelo:Number;
		private var _idFamilia:Number;
		private var _idTracao:Number;
		private var _possuiArCondicionado:String;
		private var _descricaoAC:String;
		private var _descricao:String;
		private var _siglaAcList:ArrayCollection;
		private var _siglaTracaoList:ArrayCollection;
		
		public function CentroDeCustoBean()
		{
			_siglaAcList = new ArrayCollection();
			_siglaTracaoList = new ArrayCollection();
		}
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 
		
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 
		
		

		public function get idModelo():Number
		{
			return _idModelo;
		}

		public function set idModelo(value:Number):void
		{
			_idModelo = value;
		}

		public function get idTracao():Number
		{
			return _idTracao;
		}

		public function set idTracao(value:Number):void
		{
			_idTracao = value;
		}

		public function get descricaoAC():String
		{
			return _descricaoAC;
		}

		public function set descricaoAC(value:String):void
		{
			_descricaoAC = value;
		}

		public function get possuiArCondicionado():String
		{
			return _possuiArCondicionado;
		}

		public function set possuiArCondicionado(value:String):void
		{
			_possuiArCondicionado = value;
		}

		public function get siglaTracaoList():ArrayCollection
		{
			return _siglaTracaoList;
		}

		public function set siglaTracaoList(value:ArrayCollection):void
		{
			_siglaTracaoList = value;
		}

		public function get siglaAcList():ArrayCollection
		{
			return _siglaAcList;
		}

		public function set siglaAcList(value:ArrayCollection):void
		{
			_siglaAcList = value;
		}

		public function get idFamilia():Number
		{
			return _idFamilia;
		}

		public function set idFamilia(value:Number):void
		{
			_idFamilia = value;
		}


	}
}