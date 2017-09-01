package bean
{
	[RemoteClass(alias="com.pmp.bean.RangerBean")]
	public class RangerBean
	{
		private var _beginRanger:String;
		private var _endRanger:String;
		private var _descricao:String;
		
		public function get beginRanger(): String{return _beginRanger};
		public function set beginRanger(beginRanger: String): void{this._beginRanger = beginRanger}; 
		
		public function get endRanger(): String{return _endRanger};
		public function set endRanger(endRanger: String): void{this._endRanger = endRanger}; 
		
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 
		
		public function RangerBean()
		{
		}
	}
}