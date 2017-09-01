package bean
{
	[RemoteClass(alias="com.pmp.bean.CondicaoPagamentoBean")]
	public class CondicaoPagamentoBean
	{
		private var _id:Number;
		private var _descricao:String;
		private var _sigla:String;
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 
		
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}
		public function get sigla():String
		{
			return _sigla;
		}

		public function set sigla(value:String):void
		{
			_sigla = value;
		}

; 
	}
}