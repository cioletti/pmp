package bean
{
	[RemoteClass(alias="com.pmp.bean.ModeloBean")]
	public class ModeloBean
	{		
		private var _idModelo:Number;
		private var _descricao:String;
		
		public function get idModelo():Number
		{
			return _idModelo;
		}

		public function set idModelo(value:Number):void
		{
			_idModelo = value;
		}

		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 
		
		public function ModeloBean()
		{
		}
	}
}