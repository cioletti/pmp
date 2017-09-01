package bean
{
	[RemoteClass(alias="com.pmp.bean.DashboardMaquinasBean")]
	public class DashboardMaquinasBean
	{
		
		private var _operacao:String;
		private var _totalRevisaoVencidas:Number;
		private var _totalMenor100:Number;
		private var _totalContratos:Number;
		private var _totalHorimetroZerado:Number;


		public function get operacao():String
		{
			return _operacao;
		}

		public function set operacao(value:String):void
		{
			_operacao = value;
		}

		public function get totalRevisaoVencidas():Number
		{
			return _totalRevisaoVencidas;
		}

		public function set totalRevisaoVencidas(value:Number):void
		{
			_totalRevisaoVencidas = value;
		}

		public function get totalMenor100():Number
		{
			return _totalMenor100;
		}

		public function set totalMenor100(value:Number):void
		{
			_totalMenor100 = value;
		}

		public function get totalContratos():Number
		{
			return _totalContratos;
		}

		public function set totalContratos(value:Number):void
		{
			_totalContratos = value;
		}

		public function get totalHorimetroZerado():Number
		{
			return _totalHorimetroZerado;
		}

		public function set totalHorimetroZerado(value:Number):void
		{
			_totalHorimetroZerado = value;
		}


	}
}