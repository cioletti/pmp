package bean
{
	[RemoteClass(alias="com.pmp.bean.PrefixoBean")]
	public class PrefixoBean
	{
		private var _descricao:String;
		
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 
		
		public function PrefixoBean()
		{
		}
	}
}