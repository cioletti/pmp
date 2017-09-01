package bean
{
	[RemoteClass(alias="com.pmp.bean.ConfigManutencaoMesesBean")]
	public class ConfigManutencaoMesesBean
	{
		private var _id:Number;
		private var _idConfigManutencao:Number;
		private var _meses:Number;
		private var _standardJob:String;
		private var _isSelected:Boolean;
		private var _frequencia:Number;
		private var _isExecutado:String;
		private var _mesManutencao:Number;
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id}; 
		
		public function get idConfigManutencao(): Number{return _idConfigManutencao};
		public function set idConfigManutencao(idConfigManutencao: Number): void{this._idConfigManutencao = idConfigManutencao}; 
		
		public function get meses(): Number{return _meses};
		public function set meses(meses: Number): void{this._meses = meses}; 

		public function get mesManutencao(): Number{return _mesManutencao};
		public function set mesManutencao(mesManutencao: Number): void{this._mesManutencao = mesManutencao}; 
		
		public function get standardJob(): String{return _standardJob};
		public function set standardJob(standardJob: String): void{this._standardJob = standardJob}; 
	
		public function get isSelected(): Boolean{return _isSelected};
		public function set isSelected(isSelected: Boolean): void{this._isSelected = isSelected};
		
		public function get frequencia(): Number{return _frequencia};
		public function set frequencia(frequencia: Number): void{this._frequencia = frequencia}; 
		
		public function get isExecutado(): String{return _isExecutado};
		public function set isExecutado(isExecutado: String): void{this._isExecutado = isExecutado};
		
		public function ConfigManutencaoMesesBean()
		{
		}
	}
}