package bean
{
	[RemoteClass(alias="com.pmp.bean.DetalhesVeiculosBean")]
	public class DetalhesVeiculosBean
	{
		private var _id:Number;
		private var _frota:String;
		private var _idFilial:Number;
		private var _chassi:String;
		private var _placa:String;
		private var _modelo:String;
		private var _renavan:String;
		private var _anoVeiculo:String;
		private var _dataCompra:String;
		private var _kmAtualData:String;
		private var _viaFacil:String;
		private var _goodCar:String;
		private var _vistoriaCrono:String;
		private var _pi:Number;
		private var _trocaPneuDianteiro:String;
		private var _trocaPneuTraseiro:String;
		private var _notebook:String;
		private var _responsavelId:String;
		private var _responsavelNome:String;
		
		private var _filial;
		
		
		public function get id():Number
		{
			return _id;
		}

		public function set id(value:Number):void
		{
			_id = value;
		}

		public function get frota():String
		{
			return _frota;
		}

		public function set frota(value:String):void
		{
			_frota = value;
		}

		public function get idFilial():Number
		{
			return _idFilial;
		}

		public function set idFilial(value:Number):void
		{
			_idFilial = value;
		}

		public function get chassi():String
		{
			return _chassi;
		}

		public function set chassi(value:String):void
		{
			_chassi = value;
		}

		public function get placa():String
		{
			return _placa;
		}

		public function set placa(value:String):void
		{
			_placa = value;
		}

		public function get modelo():String
		{
			return _modelo;
		}

		public function set modelo(value:String):void
		{
			_modelo = value;
		}

		public function get renavan():String
		{
			return _renavan;
		}

		public function set renavan(value:String):void
		{
			_renavan = value;
		}

		public function get anoVeiculo():String
		{
			return _anoVeiculo;
		}

		public function set anoVeiculo(value:String):void
		{
			_anoVeiculo = value;
		}

		public function get dataCompra():String
		{
			return _dataCompra;
		}

		public function set dataCompra(value:String):void
		{
			_dataCompra = value;
		}

		public function get kmAtualData():String
		{
			return _kmAtualData;
		}

		public function set kmAtualData(value:String):void
		{
			_kmAtualData = value;
		}

		public function get viaFacil():String
		{
			return _viaFacil;
		}

		public function set viaFacil(value:String):void
		{
			_viaFacil = value;
		}

		public function get goodcar():String
		{
			return _goodCar;
		}

		public function set goodcar(value:String):void
		{
			_goodCar = value;
		}

		public function get vistoriaCrono():String
		{
			return _vistoriaCrono;
		}

		public function set vistoriaCrono(value:String):void
		{
			_vistoriaCrono = value;
		}

		public function get pi():Number
		{
			return _pi;
		}

		public function set pi(value:Number):void
		{
			_pi = value;
		}

		public function get trocaPneuDianteiro():String
		{
			return _trocaPneuDianteiro;
		}

		public function set trocaPneuDianteiro(value:String):void
		{
			_trocaPneuDianteiro = value;
		}

		public function get trocaPneuTraseiro():String
		{
			return _trocaPneuTraseiro;
		}

		public function set trocaPneuTraseiro(value:String):void
		{
			_trocaPneuTraseiro = value;
		}

		public function get notebook():String
		{
			return _notebook;
		}

		public function set notebook(value:String):void
		{
			_notebook = value;
		}


		public function get filial()
		{
			return _filial;
		}

		public function set filial(value):void
		{
			_filial = value;
		}

		public function get responsavelId():String
		{
			return _responsavelId;
		}

		public function set responsavelId(value:String):void
		{
			_responsavelId = value;
		}

		public function get responsavelNome():String
		{
			return _responsavelNome;
		}

		public function set responsavelNome(value:String):void
		{
			_responsavelNome = value;
		}


	}
}