package bean
{
	[RemoteClass(alias="com.pmp.bean.FilialBean")]
	public class FilialBean
	{
		private var _stno: Number;
		private var _stnm: String;
		private var _descricao: String;
		private var _cnpj:String;
		private var _razaoSocial:String;
		private var _endereco:String;
		private var _cep:String;
		
		public function FilialBean(){}
		
		public function get endereco():String
		{
			return _endereco;
		}

		public function set endereco(value:String):void
		{
			_endereco = value;
		}

		public function get cep():String
		{
			return _cep;
		}

		public function set cep(value:String):void
		{
			_cep = value;
		}

		public function get razaoSocial():String
		{
			return _razaoSocial;
		}

		public function set razaoSocial(value:String):void
		{
			_razaoSocial = value;
		}

		public function get cnpj():String
		{
			return _cnpj;
		}

		public function set cnpj(value:String):void
		{
			_cnpj = value;
		}

		public function get stno(): Number{return _stno};
		public function set stno(stno: Number): void{this._stno = stno}; 
		
		public function get stnm(): String{return _stnm};
		public function set stnm(stnm: String): void{this._stnm = stnm}
		
		public function get descricao():String
		{
			return _descricao;
		}

		public function set descricao(value:String):void
		{
			_descricao = value;
		}

; 
	}
}