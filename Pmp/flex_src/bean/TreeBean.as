package bean
{
	import mx.collections.ArrayCollection;

	[RemoteClass(alias="com.pmp.bean.TreeBean")]
	public class TreeBean
	{
		private var _idArv:Number;
		private var _descricao:String;
		private var _tipoManutencao:String;
		private var _sos:String;
		private var _idPai:Number;
		private var _idPaiRoot:Number;
		private var _children:ArrayCollection;
		private var _smcs:String;
		
		public function get idArv(): Number{return _idArv};
		public function set idArv(idArv: Number): void{this._idArv = idArv}; 
		
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 
		
		public function get tipoManutencao(): String{return _tipoManutencao};
		public function set tipoManutencao(tipoManutencao: String): void{this._tipoManutencao = tipoManutencao}; 

		public function get sos(): String{return _sos};
		public function set sos(sos: String): void{this._sos = sos}; 
		
		public function get idPai(): Number{return _idPai};
		public function set idPai(idPai: Number): void{this._idPai = idPai}; 
		
		public function get idPaiRoot(): Number{return _idPaiRoot};
		public function set idPaiRoot(idPaiRoot: Number): void{this._idPaiRoot = idPaiRoot}; 

		public function get children(): ArrayCollection{return _children};
		public function set children(children: ArrayCollection): void{this._children = children}; 

		public function get smcs(): String{return _smcs};
		public function set smcs(smcs: String): void{this._smcs = smcs}; 
		
		
		public function TreeBean()
		{
			children = new ArrayCollection();
		}
	}
}