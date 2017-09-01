package bean
{
	import mx.collections.ArrayCollection;

	[RemoteClass(alias="com.pmp.bean.PecaBean")]
	public class PecaBean
	{
		private var _id:Number;
		private var _bgrp: String;
		private var _beqmsn: String;
		private var _uncs: String;
		private var _unls: String;
		private var _dlrqty: Number;
		private var _pano20: String;
		private var _bectyc: String;
		private var _cptcd: String;
		private var _ds18: String;
		private var _sos: String; 
		
		
		public function get id():Number
		{
			return _id;
		}

		public function set id(value:Number):void
		{
			_id = value;
		}

		public function get bgrp(): String{return _bgrp};
		public function set bgrp(bgrp: String): void{this._bgrp = bgrp}; 
		
		public function get beqmsn(): String{return _beqmsn};
		public function set beqmsn(beqmsn: String): void{this._beqmsn = beqmsn};
		
		public function get uncs(): String{return _uncs};
		public function set uncs(uncs: String): void{this._uncs = uncs};
		
		public function get unls(): String{return _unls};
		public function set unls(unls: String): void{this._unls = unls}; 
		
		public function get dlrqty(): Number{return _dlrqty};
		public function set dlrqty(dlrqty: Number): void{this._dlrqty = dlrqty};
		
		public function get pano20(): String{return _pano20};
		public function set pano20(pano20: String): void{this._pano20 = pano20}; 
		
		public function get bectyc(): String{return _bectyc};
		public function set bectyc(bectyc: String): void{this._bectyc = bectyc}; 
		
		public function get cptcd(): String{return _cptcd};
		public function set cptcd(cptcd: String): void{this._cptcd = cptcd}; 
		
		public function get ds18(): String{return _ds18};
		public function set ds18(ds18: String): void{this._ds18 = ds18};
		
		public function get sos(): String{return _sos};
		public function set sos(sos: String): void{this._sos = sos};

		
		public function PecaBean()
		{
		}
	}
}