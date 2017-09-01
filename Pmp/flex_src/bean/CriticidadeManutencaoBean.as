package bean
{
	[RemoteClass(alias="com.pmp.bean.CriticidadeManutencaoBean")]
	public class CriticidadeManutencaoBean
	{
		private var _id:Number;
		private var _nivel:String;
		private var _minPorcetagem:Number;
		private var _maxPorcetagem:Number;	
		private var _msg:String;
		
		public function CriticidadeManutencaoBean()
		{
		}

		public function get id():Number
		{
			return _id;
		}

		public function set id(value:Number):void
		{
			_id = value;
		}

		public function get nivel():String
		{
			return _nivel;
		}

		public function set nivel(value:String):void
		{
			_nivel = value;
		}

		public function get minPorcetagem():Number
		{
			return _minPorcetagem;
		}

		public function set minPorcetagem(value:Number):void
		{
			_minPorcetagem = value;
		}

		public function get maxPorcetagem():Number
		{
			return _maxPorcetagem;
		}

		public function set maxPorcetagem(value:Number):void
		{
			_maxPorcetagem = value;
		}

		public function get msg():String
		{
			return _msg;
		}

		public function set msg(value:String):void
		{
			_msg = value;
		}


	}
}