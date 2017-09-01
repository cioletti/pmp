package bean
{
	[RemoteClass(alias="com.pmp.bean.InspecaoPmpBean")]
	public class InspecaoPmpBean
	{
		private var _id:Number;
		private var _tipoOperacao:String;
		private var _cliente:String;
		private var _contato:String;
		private var _telefone:String;
		private var _numeroOs:String;
		private var _emissao:String;
		private var _modelo:String;
		private var _serie:String;
		private var _horimetro:String;
		private var _tecnico:String;
		private var _filial:String;
		private var _tipoManutencao:String;
		private var _familia:String;
		private var _equipamento:String;
		private var _fileEt:String;
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id};
		
		public function get tipoOperacao(): String{return _tipoOperacao};
		public function set tipoOperacao(tipoOperacao: String): void{this._tipoOperacao = tipoOperacao};
		
		public function get cliente(): String{return _cliente};
		public function set cliente(cliente: String): void{this._cliente = cliente};
		
		public function get numeroOs(): String{return _numeroOs};
		public function set numeroOs(numeroOs: String): void{this._numeroOs = numeroOs};
						
		public function get modelo(): String{return _modelo};
		public function set modelo(modelo: String): void{this._modelo = modelo}; 
		
		public function get serie(): String{return _serie};
		public function set serie(serie: String): void{this._serie = serie};
		
		public function get tecnico(): String{return _tecnico};
		public function set tecnico(tecnico: String): void{this._tecnico = tecnico};
		
		public function get filial(): String{return _filial};
		public function set filial(filial: String): void{this._filial = filial};
		
		public function get tipoManutencao(): String{return _tipoManutencao};
		public function set tipoManutencao(tipoManutencao: String): void{this._tipoManutencao = tipoManutencao};
		
		public function get contato(): String{return _contato};
		public function set contato(contato: String): void{this._contato = contato};
		
		public function get telefone(): String{return _telefone};
		public function set telefone(telefone: String): void{this._telefone = telefone};
		
		public function get emissao(): String{return _emissao};
		public function set emissao(emissao: String): void{this._emissao = emissao};
		
		public function get horimetro(): String{return _horimetro};
		public function set horimetro(horimetro: String): void{this._horimetro = horimetro};
				
		public function get familia(): String{return _familia};
		public function set familia(familia: String): void{this._familia = familia};
		
		public function get equipamento(): String{return _equipamento};
		public function set equipamento(equipamento: String): void{this._equipamento = equipamento};

		public function get fileEt(): String{return _fileEt};
		public function set fileEt(fileEt: String): void{this._fileEt = fileEt};
		
		public function InspecaoPmpBean()
		{
		}
	}
}