package bean
{
	import mx.collections.ArrayCollection;

	[RemoteClass(alias="com.pmp.bean.TipoFrequenciaBean")]
	public class TipoFrequenciaBean
	{
		private var _id:Number;
		private var _descricao:String;
		private var _frequenciaList:ArrayCollection;
		
		
		public function get id(): Number{return _id};
		public function set id(id: Number): void{this._id = id};
		
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 

		public function get frequenciaList(): ArrayCollection{return _frequenciaList};
		public function set frequenciaList(frequenciaList: ArrayCollection): void{this._frequenciaList = frequenciaList}; 
		
		public function TipoFrequenciaBean()
		{
		}
	}
}