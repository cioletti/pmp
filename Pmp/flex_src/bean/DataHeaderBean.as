package bean
{
	[RemoteClass(alias="com.pmp.bean.DataHeaderBean")]
	public class DataHeaderBean
	{
		
		private var _dateString:String;
		private var _descricao:String;
		private var _data:Date;
		
		public function get dateString(): String{return _dateString};
		public function set dateString(dateString: String): void{this._dateString = dateString}; 

		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 
		
		public function get data(): Date{return _data};
		public function set data(data: Date): void{this._data = data}; 
		
		public function DataHeaderBean()
		{
		}
	}
}