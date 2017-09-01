package bean
{
	[RemoteClass(alias="com.pmp.bean.JobCodeBean")]
	public class JobCodeBean
	{
		private var _id:String;
		private var _descricao:String;
		private var _label:String;
		
		public function get id(): String{return _id};
		public function set id(id: String): void{this._id = id}; 
		
		public function get descricao(): String{return _descricao};
		public function set descricao(descricao: String): void{this._descricao = descricao}; 
		
		public function get label(): String{return _label};
		public function set label(label: String): void{this._label = label}; 
		
		public function JobCodeBean()
		{
		}
	}
}