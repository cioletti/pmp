package bean
{
	[RemoteClass(alias="com.pmp.bean.StandardJobBean")]
	public class StandardJobBean
	{
		private var _id:Number;
		private var _horas:Number;
		private var _standardJob:String;
		private var _sequencia:Number;
		private var _jobCode:Number;
		private var _horasRevisao:Number;
		
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 
		
		public function get horas(): Number{return _horas};
		public function set horas(horas: Number): void{this._horas = horas}; 
		
		public function get standardJob(): String{return _standardJob};
		public function set standardJob(standardJob: String): void{this._standardJob = standardJob}; 
		
		public function get sequencia(): Number{return _sequencia};
		public function set sequencia(sequencia: Number): void{this._sequencia = sequencia}; 
		
		public function get jobCode(): Number{return _jobCode};
		public function set jobCode(jobCode: Number): void{this._jobCode = jobCode}
		public function get horasRevisao():Number
		{
			return _horasRevisao;
		}

		public function set horasRevisao(value:Number):void
		{
			_horasRevisao = value;
		}

;
		

		
		
		public function StandardJobBean()
		{
		}
	}
}