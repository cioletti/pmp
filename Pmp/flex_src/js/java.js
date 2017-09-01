function funcao()
{
	loginVar = document.getElementById("mainForm").login.value;
	senhaVar = document.getElementById("mainForm").senha.value;
	//alert(loginVar);		
  if(document.getElementById("mainForm").login.value=="" || document.getElementById("mainForm").senha.value=="")
   {
	  //alert("O login e a senha s�o obrigat�rios");
	 
		  return false;
	   }  else{
		  //document.getElementById('tabela').innerHTML = "none";
		  document.getElementById('tabela').style.display = "none";

		  document.getElementById('load').style.display = "block";
		  loginAjax();
	   	return true;
	   }
}
var loginVar;
var senhaVar;
function loginAjax(){
	//alert("vou chamar o ajax");
	
	new Ajax.Request('./Login',
			  {
		
			    method:'get',
			    parameters: {login:  loginVar, senha: senhaVar},
			    onSuccess: function(transport){
			      var response = transport.responseText || "no response text";
			     // document.getElementById('tabela').innerHTML = "block";
				  document.getElementById('tabela').style.display = "block";

				  document.getElementById('load').style.display = "none";
				  //document.getElementById('load').innerHTML = "none";
			      alert("Success! \n\n" + response);
			      
			    },
			    onFailure: function(){ 
			    	alert('Erro ao tentar logar'); 
			    	//document.getElementById('tabela').innerHTML = "block";
			  	  	document.getElementById('tabela').style.display = "block";

			  	  document.getElementById('load').style.display = "none";
			    }
			  });
}

function ajax(){
	alert("vou chamar o ajax");
	
	 geocoder = new google.maps.Geocoder();
     var mapOptions = {
      center: new google.maps.LatLng(-3.875946,-38.627930),	
      zoom: 4,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map_canvas"),
        mapOptions);
	
	new Ajax.Request('./MapaServlet',
			  {
		
			    method:'get',
			    parameters: {login:  loginVar, senha: senhaVar},
			    onSuccess: function(transport){
			      var response =   (transport.responseText).evalJSON(true);
			      alert(response.length)
			      for(var i = 0; i < response.length; i++){
			    	  alert(response[i]);
			    	  alert(response[i].localizacao);
			      }
			     // document.getElementById('tabela').innerHTML = "block";
				  //document.getElementById('tabela').style.display = "block";

				 // document.getElementById('load').style.display = "none";
				  //document.getElementById('load').innerHTML = "none";
			      alert("Success! \n\n" + response);
			      
			    },
			    onFailure: function(){ 
			    	alert('Erro ao tentar logar'); 
			    	//document.getElementById('tabela').innerHTML = "block";
			  	  //	document.getElementById('tabela').style.display = "block";

			  	//  document.getElementById('load').style.display = "none";
			    }
			  });
}