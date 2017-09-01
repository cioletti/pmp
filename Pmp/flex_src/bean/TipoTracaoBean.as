package bean
{
	[RemoteClass(alias="com.pmp.bean.TipoTracaoBean")]
	public class TipoTracaoBean
	{
		private var _id:Number;
		private var _descricao:String;
		private var _sigla:String;
		private var _idModelo:Number;
		
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 
		
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 
		
		public function get sigla(): String{return _sigla};
		public function set sigla(sigla: String): void{this._sigla = sigla}; 
		
		public function CentroDeCustoBean()
		{
		}

		public function get idModelo():Number
		{
			return _idModelo;
		}

		public function set idModelo(value:Number):void
		{
			_idModelo = value;
		}

	}
}