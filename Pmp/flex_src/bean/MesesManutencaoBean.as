package bean
{
	[RemoteClass(alias="com.pmp.bean.MesesManutencaoBean")]
	public class MesesManutencaoBean
	{
		private var _id:Number;
		private var _idFamilia:Number;
		private var _familiaStr:String;
		private var _idModelo:Number;
		private var _modeloStr:String;
		private var _mesesManutencao:Number;
		
		public function get modeloStr():String
		{
			return _modeloStr;
		}

		public function set modeloStr(value:String):void
		{
			_modeloStr = value;
		}

		public function get familiaStr():String
		{
			return _familiaStr;
		}

		public function set familiaStr(value:String):void
		{
			_familiaStr = value;
		}

		public function get idModelo():Number
		{
			return _idModelo;
		}

		public function set idModelo(value:Number):void
		{
			_idModelo = value;
		}

		public function get idFamilia():Number
		{
			return _idFamilia;
		}

		public function set idFamilia(value:Number):void
		{
			_idFamilia = value;
		}

		public function get mesesManutencao():Number
		{
			return _mesesManutencao;
		}

		public function set mesesManutencao(value:Number):void
		{
			_mesesManutencao = value;
		}

		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 

		
		
		public function MesesManutencaoBean()
		{
		}
	}
}