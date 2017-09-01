package bean
{
	[RemoteClass(alias="com.pmp.bean.TipoPm")]
	public class TipoPm
	{
		private var _label:String;
		private var _valor:String;
		private var _ordem:Number;
		public function TipoPm()
		{
		}

		public function get ordem():Number
		{
			return _ordem;
		}

		public function set ordem(value:Number):void
		{
			_ordem = value;
		}

		public function get valor():String
		{
			return _valor;
		}

		public function set valor(value:String):void
		{
			_valor = value;
		}

		public function get label():String
		{
			return _label;
		}

		public function set label(value:String):void
		{
			_label = value;
		}


	}
}