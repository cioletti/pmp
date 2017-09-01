package bean
{
	[RemoteClass(alias="com.pmp.bean.ClienteBean")]
	public class ClienteBean
	{
		private var _RAZSOC:String;
		private var _CLCHAVE:String;
		private var _END:String;
		private var _BAIRRO:String;
		private var _CID:String;
		private var _EST:String;
		private var _INSCEST:String;
		private var _INSCMUN:String;
		private var _CONFINS:String;
		private var _INDCONT:String;
		private var _CGCNUM:String;
		private var _CGCFIL:String;
		private var _CGCDIG:String;
		private var _CPF:String;
		private var _CNPJ:String;
		private var _CEP:String;
		private var _FLAGDELETE:String;
		private var _msg:String;
		private var _isSelected:Boolean;
		
		public function get msg(): String{return _msg};
		public function set msg(msg: String): void{this._msg = msg};
		
		public function get RAZSOC(): String{return _RAZSOC};
		public function set RAZSOC(RAZSOC: String): void{this._RAZSOC = RAZSOC}; 
		
		public function get CLCHAVE(): String{return _CLCHAVE};
		public function set CLCHAVE(CLCHAVE: String): void{this._CLCHAVE = CLCHAVE}; 
		
		public function get END(): String{return _END};
		public function set END(END: String): void{this._END = END}; 
		
		public function get BAIRRO(): String{return _BAIRRO};
		public function set BAIRRO(BAIRRO: String): void{this._BAIRRO = BAIRRO}; 
		
		public function get CID(): String{return _CID};
		public function set CID(CID: String): void{this._CID = CID}; 
		
		public function get EST(): String{return _EST};
		public function set EST(EST: String): void{this._EST = EST}; 
		
		public function get INSCEST(): String{return _INSCEST};
		public function set INSCEST(INSCEST: String): void{this._INSCEST = INSCEST}; 
		
		public function get INSCMUN(): String{return _INSCMUN};
		public function set INSCMUN(INSCMUN: String): void{this._INSCMUN = INSCMUN}; 
		
		public function get CONFINS(): String{return _CONFINS};
		public function set CONFINS(CONFINS: String): void{this._CONFINS = CONFINS}; 
		
		public function get INDCONT(): String{return _INDCONT};
		public function set INDCONT(INDCONT: String): void{this._INDCONT = INDCONT}; 
		
		public function get CGCNUM(): String{return _CGCNUM};
		public function set CGCNUM(CGCNUM: String): void{this._CGCNUM = CGCNUM}; 
		
		public function get CGCFIL(): String{return _CGCFIL};
		public function set CGCFIL(CGCFIL: String): void{this._CGCFIL = CGCFIL}; 
		
		public function get CGCDIG(): String{return _CGCDIG};
		public function set CGCDIG(CGCDIG: String): void{this._CGCDIG = CGCDIG}; 
		
		public function get CPF(): String{return _CPF};
		public function set CPF(CPF: String): void{this._CPF = CPF}; 

		public function get CNPJ(): String{return _CNPJ};
		public function set CNPJ(CNPJ: String): void{this._CNPJ = CNPJ}; 

		public function get CEP(): String{return _CEP};
		public function set CEP(CEP: String): void{this._CEP = CEP}; 

		public function get FLAGDELETE(): String{return _FLAGDELETE};
		public function set FLAGDELETE(FLAGDELETE: String): void{this._FLAGDELETE = FLAGDELETE}; 

		public function get isSelected(): Boolean{return _isSelected};
		public function set isSelected(isSelected: Boolean): void{this._isSelected = isSelected}; 
	
		
		public function ClienteBean()
		{
		}
	}
}