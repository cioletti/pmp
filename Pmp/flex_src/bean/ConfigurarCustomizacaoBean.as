package bean
{
	import mx.collections.ArrayCollection;

	[RemoteClass(alias="com.pmp.bean.ConfigurarCustomizacaoBean")]
	public class ConfigurarCustomizacaoBean
	{
		private var _id:Number;
		private var _idModelo:Number;
		private var _idFamilia:Number;
		private var _idTipoCustomizacao:Number;
		private var _descricao:String;
		private var _isSelected:Boolean;
		private var _siglaCustList:ArrayCollection;

		
		public function ConfigurarCustomizacaoBean()
		{
			_siglaCustList = new ArrayCollection();
		}
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 
		
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 
		
		

		public function get idModelo():Number
		{
			return _idModelo;
		}

		public function set idModelo(value:Number):void
		{
			_idModelo = value;
		}

		public function get siglaCustList():ArrayCollection
		{
			return _siglaCustList;
		}

		public function set siglaCustList(value:ArrayCollection):void
		{
			_siglaCustList = value;
		}

		public function get idFamilia():Number
		{
			return _idFamilia;
		}

		public function set idFamilia(value:Number):void
		{
			_idFamilia = value;
		}

		public function get idTipoCustomizacao():Number
		{
			return _idTipoCustomizacao;
		}

		public function set idTipoCustomizacao(value:Number):void
		{
			_idTipoCustomizacao = value;
		}

		public function get isSelected():Boolean
		{
			return _isSelected;
		}

		public function set isSelected(value:Boolean):void
		{
			_isSelected = value;
		}


	}
}