package bean
{
	[RemoteClass(alias="com.pmp.bean.TotalAgendamentoChartBean")]
	public class TotalAgendamentoChartBean
	{
		private var _valor: Number;
		private var _descricao: String;
		private var _descricaoColumn: String;
		
		public function FilialBean(){}
		
		public function get valor(): Number{return _valor};
		public function set valor(valor: Number): void{this._valor = valor}; 
		
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 

		public function get descricaoColumn(): String{return _descricaoColumn};
		public function set descricaoColumn(descricaoColumn: String): void{this._descricaoColumn = descricaoColumn}; 
	}
}