package com.pmp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

	public static boolean verificarDatasVencimento(Date data) throws ParseException {  
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");        
		Date dataAtual = format.parse(format.format(new Date()));  
		if(data == null)  
			return false;  
		if(data.compareTo(dataAtual) == -1){
			return false;
		}
		return true;
			  
	}

	public static String converteHoraMinuto(float valorFinalEmHoras){  
	    float tempoM= valorFinalEmHoras*60;  
	    int hora=0;  
	    int minutos=0;  
	    String hora_minutos="00:00";  
	    while(tempoM>=60){  
	        hora++;  
	        tempoM=tempoM-60;           
	    }  
	    minutos=Math.round(tempoM);  
	    hora_minutos = hora+":"+((minutos < 10)?"0"+minutos:minutos);  
	    return hora_minutos.replace(":", "");  
	}  
	
	public static Integer diffDaysDate(Date beginDate, Date endDate){
		Long diff = endDate.getTime() - beginDate.getTime();
		
		// Quantidade de milissegundos em um dia
		Long tempoDia = 1000L * 60L * 60L * 24L;
		 
		Long diasDiferenca = diff / tempoDia;
		
		return diasDiferenca.intValue();
	}
	
	//faz a pesquisa, dado um inteiro de 1 a 7  
	  public static String pesquisarDiaSemana(int _diaSemana)  
	  {  
	    String diaSemana = null;  
	  
	    switch (_diaSemana)  
	    {  
	  
	    case 1:  
	    {  
	      diaSemana = "Domingo";  
	      break;  
	    }  
	    case 2:  
	    {  
	      diaSemana = "Segunda";  
	      break;  
	    }  
	    case 3:  
	    {  
	      diaSemana = "Terça";  
	      break;  
	    }  
	    case 4:  
	    {  
	      diaSemana = "Quarta";  
	      break;  
	    }  
	    case 5:  
	    {  
	      diaSemana = "Quinta";  
	      break;  
	    }  
	    case 6:  
	    {  
	      diaSemana = "Sexta";  
	      break;  
	    }  
	    case 7:  
	    {  
	      diaSemana = "Sabado";  
	      break;  
	    }  
	  
	    }  
	    return diaSemana; 
	  }
}
