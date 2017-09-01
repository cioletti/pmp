package bean
{
	[RemoteClass(alias="com.pmp.bean.RegraDeNegocioBean")]
	public class RegraDeNegocioBean
	{
		private var _id:Number;
		private var _descricao:String;
		private var _filial:String;
		
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 
		
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 
		
		public function get filial(): String{return _filial};
		public function set filial(filial: String): void{this._filial = filial}; 
		
		public function RegraDeNegocioBean()
		{
		}
	}
}