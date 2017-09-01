package bean
{
	

	[RemoteClass(alias="com.pmp.bean.StatusOsBean")]
	public class StatusOsBean
	{
		private var _id:Number;
		private var _sigla:String; 
		private var _descricao:String; 


		public function get id():Number
		{
			return _id;
		}

		public function set id(value:Number):void
		{
			_id = value;
		}

		public function get sigla():String
		{
			return _sigla;
		}

		public function set sigla(value:String):void
		{
			_sigla = value;
		}

		public function get descricao():String
		{
			return _descricao;
		}

		public function set descricao(value:String):void
		{
			_descricao = value;
		}



		


	}
}