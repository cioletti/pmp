package bean
{
	[RemoteClass(alias="com.pmp.bean.PrecoBean")]
	public class PrecoBean
	{
		private var _parcela:Number;
		private var _preco:String;
		private var _precoConcessao:String;
		
		public function get parcela(): Number{return _parcela};
		public function set parcela(parcela: Number): void{this._parcela = parcela}; 
		
		public function get preco(): String{return _preco};
		public function set preco(preco: String): void{this._preco = preco}; 

		public function get precoConcessao(): String{return _precoConcessao};
		public function set precoConcessao(precoConcessao: String): void{this._precoConcessao = precoConcessao}; 
		
		public function PrecoBean()
		{
		}
	}
}