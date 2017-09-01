package bean
{
	[RemoteClass(alias="com.pmp.bean.ValorDescontoBean")]
	public class ValorDescontoBean
	{
		private var _totalComDesconto:String;
		private var _totalSemDesconto:String;
		private var _descontoMedio:String;
		

		public function get totalComDesconto(): String{return _totalComDesconto};
		public function set totalComDesconto(totalComDesconto: String): void{this._totalComDesconto = totalComDesconto};
		
		public function get totalSemDesconto(): String{return _totalSemDesconto};
		public function set totalSemDesconto(totalSemDesconto: String): void{this._totalSemDesconto = totalSemDesconto};
		
		public function get descontoMedio(): String{return _descontoMedio};
		public function set descontoMedio(descontoMedio: String): void{this._descontoMedio = descontoMedio};
		
		
		
		public function ValorDescontoBean()
		{
		}
	}
}