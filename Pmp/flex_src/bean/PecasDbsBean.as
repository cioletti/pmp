package bean
{
	[RemoteClass(alias="com.pmp.bean.PecasDbsBean")]
	public class PecasDbsBean
	{
		private var _codigo:String;
		private var _qtd:Number;
		private var _numPeca:String;
		private var _nomePeca:String;
		
		public function get codigo(): String{return _codigo};
		public function set codigo(codigo: String): void{this._codigo = codigo}; 
		
		public function get qtd(): Number{return _qtd};
		public function set qtd(qtd: Number): void{this._qtd = qtd}; 
		
		public function get numPeca(): String{return _numPeca};
		public function set numPeca(numPeca: String): void{this._numPeca = numPeca}; 
		
		public function get nomePeca(): String{return _nomePeca};
		public function set nomePeca(nomePeca: String): void{this._nomePeca = nomePeca}; 
		
		public function PecasDbsBean()
		{
		}
	}
}