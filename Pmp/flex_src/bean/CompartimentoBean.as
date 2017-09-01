package bean
{
	[RemoteClass(alias="com.pmp.bean.CompartimentoBean")]
	public class CompartimentoBean
	{
		private var _id:Number;
		private var _descricao:String;
		private var _codigo:String;
		private var _modelo:String;
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 

		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 
		
		public function get codigo(): String{return _codigo};
		public function set codigo(codigo: String): void{this._codigo = codigo}; 

		public function get modelo(): String{return _modelo};
		public function set modelo(modelo: String): void{this._modelo = modelo}; 
		
		public function CompartimentoBean()
		{
		}
	}
}