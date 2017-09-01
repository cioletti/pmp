package util
{
	public class DateValidator
	{
		public function DateValidator()
		{
		}
		public static  function validateDate(s:String, delimitador: String):Boolean {
			var d:Array = s.split(delimitador);
			
			if (d.length!=3) return false;
			for (var i:Number = 0; i<d.length; i++) d[i] = parseInt(d[i]);
			
			d[1] = d[1]-1;	
			var dt:Date = new Date(d[2], d[1], d[0]);
			
			return ((dt.getFullYear()==d[2]) && (dt.getMonth()==d[1]) && (dt.getDate()==d[0]));
		}
	}
}