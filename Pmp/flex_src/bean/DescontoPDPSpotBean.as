package bean
{
	[RemoteClass(alias="com.pmp.bean.DescontoPDPSpotBean")]
	public class DescontoPDPSpotBean
	{
		private var _id:Number;
		private var _descricao:String;
		private var _valor:String;

		public function get valor():String
		{
			return _valor;
		}

		public function set valor(value:String):void
		{
			_valor = value;
		}

		
		public function get id():Number{return _id};
		public function set id(id:Number):void{this._id = id}; 
		
		public function get descricao():String{return _descricao};
		public function set descricao(descricao:String):void{this._descricao = descricao}; 
	
		
		public function PerfilBean()
		{
		}
	}
}