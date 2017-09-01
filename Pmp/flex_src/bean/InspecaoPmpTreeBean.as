package bean
{
	import mx.collections.ArrayCollection;

	[RemoteClass(alias="com.pmp.bean.InspecaoPmpTreeBean")]
	public class InspecaoPmpTreeBean
	{
		private var _idOsPalmDt:Number;
		private var _status:String;
		private var _statusUrlImage:String;
		private var _descricao:String;
		private var _obs:String;
		private var _tipoManutencao:String;
		private var _idIrdav:String;
		private var _simNao:String;
		private var _smcs:String;
		private var _fotoUrlImage:String
		private var _children:ArrayCollection;
		
		public function get idOsPalmDt(): Number{return _idOsPalmDt};
		public function set idOsPalmDt(idOsPalmDt: Number): void{this._idOsPalmDt = idOsPalmDt}; 
				
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 
		
		public function get status(): String{return _status};
		public function set status(status: String): void{this._status = status}; 
		
		public function get obs(): String{return _obs};
		public function set obs(obs: String): void{this._obs = obs}; 
		
		public function get tipoManutencao(): String{return _tipoManutencao};
		public function set tipoManutencao(tipoManutencao: String): void{this._tipoManutencao = tipoManutencao};
				
		public function get idIrdav(): String{return _idIrdav};
		public function set idIrdav(idIrdav: String): void{this._idIrdav = idIrdav}; 
		
		public function get simNao(): String{return _simNao};
		public function set simNao(simNao: String): void{this._simNao = simNao}; 
		
		public function get smcs(): String{return _smcs};
		public function set smcs(smcs: String): void{this._smcs = smcs}; 
		
		public function get statusUrlImage(): String{return _statusUrlImage};
		public function set statusUrlImage(statusUrlImage: String): void{this._statusUrlImage = statusUrlImage}; 
		
		public function get fotoUrlImage(): String{return _fotoUrlImage};
		public function set fotoUrlImage(fotoUrlImage: String): void{this._fotoUrlImage = fotoUrlImage}; 
		
		public function get children(): ArrayCollection{return _children};
		public function set children(children: ArrayCollection): void{this._children = children}; 		
		
		public function InspecaoPmpTreeBean(){
			_children = new ArrayCollection();
		}
	}
}