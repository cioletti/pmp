package bean
{
	[RemoteClass(alias="com.pmp.bean.SistemaBean")]
	public class SistemaBean
	{
		private var _id:Number;
		private var _descricao:String;
		private var _descricaoPerfil:String;
		private var _sigla:String;
		private var _img:String;
		private var _context:String;
		private var _url:String;
		private var _perfilBean:PerfilBean;
		
		public function get id():Number{return _id};
		public function set id(id:Number):void{this._id = id}; 
		
		public function get descricao():String{return _descricao};
		public function set descricao(descricao:String):void{this._descricao = descricao}; 

		public function get descricaoPerfil():String{return _descricaoPerfil};
		public function set descricaoPerfil(descricaoPerfil:String):void{this._descricaoPerfil = descricaoPerfil}; 
		
		public function get sigla():String{return _sigla};
		public function set sigla(sigla:String):void{this._sigla = sigla}; 
		
		public function get img():String{return _img};
		public function set img(img:String):void{this._img = img}; 
		
		public function get context():String{return _context};
		public function set context(context:String):void{this._context = context}; 
		
		public function get url():String{return _url};
		public function set url(url:String):void{this._url = url}; 

		public function get perfilBean():PerfilBean{return _perfilBean};
		public function set perfilBean(perfilBean:PerfilBean):void{this._perfilBean = perfilBean}; 
		
		public function SistemaBean()
		{
		}
	}
}