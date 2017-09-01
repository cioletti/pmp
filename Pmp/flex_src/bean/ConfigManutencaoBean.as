package bean
{
	import mx.collections.ArrayCollection;

	[RemoteClass(alias="com.pmp.bean.ConfigManutencaoBean")]
	public class ConfigManutencaoBean
	{
		private var _id:Number;
		private var _modelo:String;
		private var _prefixo:String;
		private var _businessGroup:String;
		private var _beginRanger:String;
		private var _rangerDescription:String;
		private var _endRanger:String;
		private var _standardJobList:ArrayCollection;
		private var _rangerList:ArrayCollection;
		private var _businessGroupList:ArrayCollection;
		private var _prefixoList:ArrayCollection;
		private var _modeloList:ArrayCollection;
		private var _contExcessao:String;
		private var _tipoPreco:String;
		private var _idPrecoConfig:Number;
		private var _idFamilia:Number;
		private var _idTipoTracao:Number;
		private var _possuiArCondicionado:String;
		private var _descTipoPreco:String;
		private var _descPreco:String;
		
		
		
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 

		public function get modelo(): String{return _modelo};
		public function set modelo(modelo: String): void{this._modelo = modelo}; 
		
		public function get prefixo(): String{return _prefixo};
		public function set prefixo(prefixo: String): void{this._prefixo = prefixo}; 
		
		public function get businessGroup(): String{return _businessGroup};
		public function set businessGroup(businessGroup: String): void{this._businessGroup = businessGroup}; 
		
		public function get beginRanger(): String{return _beginRanger};
		public function set beginRanger(beginRanger: String): void{this._beginRanger = beginRanger}; 

		public function get rangerDescription(): String{return _rangerDescription};
		public function set rangerDescription(rangerDescription: String): void{this._rangerDescription = rangerDescription}; 
		
		public function get endRanger(): String{return _endRanger};
		public function set endRanger(endRanger: String): void{this._endRanger = endRanger}; 

		public function get contExcessao(): String{return _contExcessao};
		public function set contExcessao(contExcessao: String): void{this._contExcessao = contExcessao}; 
		
		public function get standardJobList(): ArrayCollection{return _standardJobList};
		public function set standardJobList(standardJobList: ArrayCollection): void{this._standardJobList = standardJobList}; 

		public function get rangerList(): ArrayCollection{return _rangerList};
		public function set rangerList(rangerList: ArrayCollection): void{this._rangerList = rangerList}; 

		public function get businessGroupList(): ArrayCollection{return _businessGroupList};
		public function set businessGroupList(businessGroupList: ArrayCollection): void{this._businessGroupList = businessGroupList}; 

		public function get prefixoList(): ArrayCollection{return _prefixoList};
		public function set prefixoList(prefixoList: ArrayCollection): void{this._prefixoList = prefixoList}; 

		public function get modeloList(): ArrayCollection{return _modeloList};
		public function set modeloList(modeloList: ArrayCollection): void{this._modeloList = modeloList}; 

		public function get idPrecoConfig(): Number{return _idPrecoConfig};
		public function set idPrecoConfig(idPrecoConfig: Number): void{this._idPrecoConfig = idPrecoConfig}; 

		public function get idFamilia(): Number{return _idFamilia};
		public function set idFamilia(idFamilia: Number): void{this._idFamilia = idFamilia}
		
		public function get tipoPreco():String
		{
			return _tipoPreco;
		}

		public function set tipoPreco(value:String):void
		{
			_tipoPreco = value;
		}

		public function get idTipoTracao():Number
		{
			return _idTipoTracao;
		}

		public function set idTipoTracao(value:Number):void
		{
			_idTipoTracao = value;
		}

		public function get possuiArCondicionado():String
		{
			return _possuiArCondicionado;
		}

		public function set possuiArCondicionado(value:String):void
		{
			_possuiArCondicionado = value;
		}

		public function get descTipoPreco():String
		{
			return _descTipoPreco;
		}

		public function set descTipoPreco(value:String):void
		{
			_descTipoPreco = value;
		}

		public function get descPreco():String
		{
			return _descPreco;
		}

		public function set descPreco(value:String):void
		{
			_descPreco = value;
		}
		
		
		public function ConfigManutencaoBean()
		{
			_standardJobList = new ArrayCollection();
		}
	}
}