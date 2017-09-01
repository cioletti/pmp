package bean
{
	import mx.collections.ArrayCollection;

	[RemoteClass(alias="com.pmp.bean.MinutaBean")]
	public class MinutaBean{
		
		private var _numOs:String;
		private var _cliente:String;
		private var _serie:String;
		private var _modelo:String;
		private var _dataNota:String;
		private var _numNota:String;
		private var _dataTransp:String;
		private var _minuta:String;
		

		public function get numOs():String
		{
			return _numOs;
		}

		public function set numOs(value:String):void
		{
			_numOs = value;
		}

		public function get cliente():String
		{
			return _cliente;
		}

		public function set cliente(value:String):void
		{
			_cliente = value;
		}

		public function get serie():String
		{
			return _serie;
		}

		public function set serie(value:String):void
		{
			_serie = value;
		}

		public function get modelo():String
		{
			return _modelo;
		}

		public function set modelo(value:String):void
		{
			_modelo = value;
		}

		public function get dataNota():String
		{
			return _dataNota;
		}

		public function set dataNota(value:String):void
		{
			_dataNota = value;
		}

		public function get numNota():String
		{
			return _numNota;
		}

		public function set numNota(value:String):void
		{
			_numNota = value;
		}

		public function get dataTransp():String
		{
			return _dataTransp;
		}

		public function set dataTransp(value:String):void
		{
			_dataTransp = value;
		}

		public function get minuta():String
		{
			return _minuta;
		}

		public function set minuta(value:String):void
		{
			_minuta = value;
		}


	}
	
	

}