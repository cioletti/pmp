package bean
{
	[RemoteClass(alias="com.pmp.bean.DescontoPdpBean")]
	public class DescontoPdpBean
	{
		private var _id:Number;
		private var _descricao:String;
				
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 
		
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 
		
		public function DescontoPdpBean()
		{
		}
	}
}