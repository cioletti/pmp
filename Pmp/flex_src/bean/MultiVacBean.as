package bean
{
	[RemoteClass(alias="com.pmp.bean.MultiVacBean")]
	public class MultiVacBean
	{
		private var _valorDesconto: Number;
		
		public function MultiVacBean(){}
		
		public function get valorDesconto():Number
		{
			return _valorDesconto;
		}

		public function set valorDesconto(value:Number):void
		{
			_valorDesconto = value;
		}

	}
}