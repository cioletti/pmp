package bean
{
	[RemoteClass(alias="com.pmp.bean.ClienteInterBean")]
	public class ClienteInterBean
	{
		private var _id:Number;
		private var _fkFilial:String;
		private var _customerNum:String;
		private var _searchKey:String;
		private var _descricao:String;
		private var _cod:String;
		private var _filialDesc:String;		
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 
		
		public function get fkFilial(): String{return _fkFilial};
		public function set fkFilial(fkFilial: String): void{this._fkFilial = fkFilial}; 
		
		public function get customerNum(): String{return _customerNum};
		public function set customerNum(customerNum: String): void{this._customerNum = customerNum};
		
		public function get searchKey(): String{return _searchKey};
		public function set searchKey(searchKey: String): void{this._searchKey = searchKey};
		
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao};
		
		public function get cod(): String{return _cod};
		public function set cod(cod: String): void{this._cod = cod};
		
		public function get filialDesc(): String{return _filialDesc};
		public function set filialDesc(filialDesc: String): void{this._filialDesc = filialDesc};
		
		public function ClienteInterBean()
		{
		}
	}
}