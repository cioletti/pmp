package bean
{
	import mx.collections.ArrayCollection;

	[RemoteClass(alias="com.pmp.bean.ValorManutencaoBean")]
	public class ValorManutencaoBean
	{
		private var _configManutencaoHorasBeanList: ArrayCollection;
		private var _beginRanger: String;
		private var _endRanger: String;
		private var _prefixo: String;
		private var _cptcd: String;
		private var _bgrp: String;
		private var _tipoPreco: String;
		private var _modelo: String;
		
		public function ValorManutencaoBean(){}
		
		public function get configManutencaoHorasBeanList(): ArrayCollection{return _configManutencaoHorasBeanList};
		public function set configManutencaoHorasBeanList(configManutencaoHorasBeanList: ArrayCollection): void{this._configManutencaoHorasBeanList = configManutencaoHorasBeanList}; 
		
		public function get beginRanger(): String{return _beginRanger};
		public function set beginRanger(beginRanger: String): void{this._beginRanger = beginRanger};
		
		public function get endRanger(): String{return _endRanger};
		public function set endRanger(endRanger: String): void{this._endRanger = endRanger};
		
		public function get prefixo(): String{return _prefixo};
		public function set prefixo(prefixo: String): void{this._prefixo = prefixo};
		
		public function get cptcd(): String{return _cptcd};
		public function set cptcd(cptcd: String): void{this._cptcd = cptcd};
		
		public function get bgrp(): String{return _bgrp};
		public function set bgrp(bgrp: String): void{this._bgrp = bgrp}; 
		
		public function get tipoPreco(): String{return _tipoPreco};
		public function set tipoPreco(tipoPreco: String): void{this._tipoPreco = tipoPreco}; 
		
		public function get modelo(): String{return _modelo};
		public function set modelo(modelo: String): void{this._modelo = modelo}; 
	}
}