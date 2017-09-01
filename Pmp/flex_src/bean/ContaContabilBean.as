package bean
{
	[RemoteClass(alias="com.pmp.bean.ContaContabilBean")]
	public class ContaContabilBean
	{
		private var _id:Number;
		private var _descricao:String;
		private var _sigla:String;
		private var _siglaDescricao:String;		
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 
		
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 
		
		public function get sigla(): String{return _sigla};
		public function set sigla(sigla: String): void{this._sigla = sigla}; 
		
		public function get siglaDescricao(): String{return _siglaDescricao};
		public function set siglaDescricao(siglaDescricao: String): void{this._siglaDescricao = siglaDescricao}; 
		
		public function ContaContabilBean()
		{
		}
	}
}