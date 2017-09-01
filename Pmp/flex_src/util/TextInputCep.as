package util
{
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.ui.Keyboard;
	
	import mx.controls.TextInput;
	import mx.events.FlexEvent;
	import mx.managers.IFocusManager;
	import mx.managers.IFocusManagerComponent;
	
	public class TextInputCep extends TextInput
	{
		public function TextInputCep()
		{
			super();
			init();
		}
		
		/**
		 *  Inicializa os componentes
		 */
		private function init():void {
			editable = true; //O componente é editável
			restrict = "0-9"; //Serão aceitos apenas números
			maxChars = 9; //O componente suportará apenas 9 caracteres
			addEventListener(FlexEvent.ENTER, moveFoco);
		}       
		
		/**
		 * Esse método é sobrescrito para tratar cada tecla pressionada
		 *
		 */
		override protected function keyDownHandler(event:KeyboardEvent):void{
			var codigoTecla:int = event.keyCode; //Pega o código da tecla pressionada
			var quantidadeCaracteres:int = textField.text.length; //Pega a quantidade de caracteres
			//dentro do componente
			if(codigoTecla == Keyboard.ENTER){ //Se pressionar ENTER, o foco do componente será mudado
				dispatchEvent(new FlexEvent(FlexEvent.ENTER));
			}else if( codigoTecla != 8 ){
				if(quantidadeCaracteres == 5){
					//Caso já tenha sido digitado 5 caracteres, o próximo a ser inserido será o hífem "-"
					textField.replaceText(quantidadeCaracteres, quantidadeCaracteres, "-");
					//Move o cursor dentro do componente para a última posição
					textField.setSelection(textField.text.length, textField.text.length);
				}
			}
		}
		
		/**
		 * Esse método move o foco para o próximo componente
		 */
		private function moveFoco(event:Event):void {
			var fm:IFocusManager = event.target.focusManager;
			var next:IFocusManagerComponent = fm.getNextFocusManagerComponent();
			fm.setFocus(next);
		}       
		
	}
}