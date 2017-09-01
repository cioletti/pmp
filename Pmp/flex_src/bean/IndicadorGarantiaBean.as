package bean
{
	[RemoteClass(alias="com.pmp.bean.IndicadorGarantiaBean")]
	public class IndicadorGarantiaBean
	{
		private var _id:Number;
		private var _tipo:String;
		private var _ind:String;
		private var _descricao:String;
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 
		
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao};
		
		public function get tipo(): String{return _tipo};
		public function set tipo(tipo: String): void{this._tipo = tipo}; 
		
		public function get ind(): String{return _ind};
		public function set ind(ind: String): void{this._ind = ind}; 
		
		public function IndicadorGarantiaBean()
		{
		}
	}
}