package bean
{
	[RemoteClass(alias="com.pmp.bean.TipoOleoBean")]
	public class TipoOleoBean
	{
		private var _id:Number;
		private var _fabricante:String;
		private var _viscosidade:String;
		private var _nomeComercial:String;
		
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 

		public function get fabricante(): String{return _fabricante};
		public function set fabricante(fabricante: String): void{this._fabricante = fabricante}; 
		
		public function get viscosidade(): String{return _viscosidade};
		public function set viscosidade(viscosidade: String): void{this._viscosidade = viscosidade}; 

		public function get nomeComercial(): String{return _nomeComercial};
		public function set nomeComercial(nomeComercial: String): void{this._nomeComercial = nomeComercial}; 
		
		public function TipoOleoBean()
		{
		}
	}
}