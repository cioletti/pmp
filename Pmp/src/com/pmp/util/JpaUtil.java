/*
 * JpaUtil.java
 *
 * Created on 19 de Julho de 2007, 14:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.pmp.util;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *

 */
public class JpaUtil {
    
    /** Creates a new instance of JpaUtil */
    public JpaUtil() {
    }
     private static EntityManagerFactory factory;
     private static EntityManager manager;
     private static JpaUtil jpaUtil = new JpaUtil();  
 
    /**
     * Resgata EntityManager usando JNDI-NAME da arvore JNDI do jboss.
     * @return
     */
    public synchronized EntityManager getEntityManager(){
        try {
            if (factory==null || !factory.isOpen() ){
                factory = Persistence.createEntityManagerFactory("pesa");
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return factory.createEntityManager();
    }
   
    
    public synchronized static EntityManager getInstance(){
 //   	manager = verificarConexao();
//    	if(manager == null){
//    		boolean isConnection = true;
//    		int count = 0;
//    		while(isConnection){
    			try {
    				EntityManager entityManager = jpaUtil.getEntityManager();
//    				Query query = entityManager.createNativeQuery("select * from dual");
//    				query.getSingleResult();
    				//isConnection = false;
    				return entityManager;
    			} catch (Exception e1) {
//    				count++;
//    				if(count == 10){
//    					isConnection = false;
//    				}
    				e1.printStackTrace();
    			}
    		//}
    	//}
        return manager;
     }
    
    private static EntityManager verificarConexao(){
    	try {
			Query query = manager.createNativeQuery("select * from dual");
			query.getResultList();
			return manager;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    public static String preencheCom(String linha_a_preencher, String letra, int tamanho, int direcao){
        if (linha_a_preencher == null || linha_a_preencher.trim() == "" ) {linha_a_preencher = "";}
        while (linha_a_preencher.contains(" ")) {linha_a_preencher = linha_a_preencher.replaceAll(" "," ").trim();}
        linha_a_preencher = linha_a_preencher.replaceAll("[./-]","");
        StringBuffer sb = new StringBuffer(linha_a_preencher);
        if (direcao==1){ //a Esquerda
            for (int i=sb.length() ; i<tamanho ; i++){
                sb.insert(0,letra);
            }
        } else if (direcao==2) {//a Direita
            for (int i=sb.length() ; i<tamanho ; i++){
                sb.append(letra);
            }
        }
        return sb.toString();
    }
    
    
    public static String Sethoras(String Horai,String Horaf) {
		 Calendar  c1 = Calendar.getInstance();  
		 Calendar c2 = Calendar.getInstance();  
		 String [] hi = Horai.split(":"); 
		 String [] hf = Horaf.split(":"); 

		 c1.set(Calendar.YEAR, 2009);  
		 c1.set(Calendar.MONTH, Calendar.JANUARY);  
		 c1.set(Calendar.DAY_OF_MONTH, 1);  
		 c1.set(Calendar.HOUR, Integer.valueOf(hi[0]));  
		 c1.set(Calendar.MINUTE,Integer.valueOf(hi[1]));  
		 c1.set(Calendar.SECOND, 0);  
		 c1.set(Calendar.MILLISECOND, 0);  
		 c1.set(Calendar.AM_PM, Calendar.AM);  

		 c2.set(Calendar.YEAR, 2009);  
		 c2.set(Calendar.MONTH, Calendar.JANUARY);  
		 c2.set(Calendar.DAY_OF_MONTH, 1);  
		 c2.set(Calendar.HOUR, Integer.valueOf(hf[0]));  
		 c2.set(Calendar.MINUTE, Integer.valueOf(hf[1]));  
		 c2.set(Calendar.SECOND, 0);  
		 c2.set(Calendar.MILLISECOND, 0);  
		 c2.set(Calendar.AM_PM, Calendar.AM);  
		 //  System.out.println(c2.getTime());  
		 long dif = c2.getTime().getTime() - c1.getTime().getTime();  
		 //   long constante = 1000 * 60 * 60;  
		 double diferencahoras,diferencaminutos,diferencahoraminutos;     
		 diferencahoras = dif / (1000*60*60);  
		 diferencaminutos =  dif / (1000*60);    
		 diferencahoraminutos =  diferencaminutos % 60;    
		 BigDecimal hr = new BigDecimal(diferencahoras);  
		 BigDecimal mm = new BigDecimal(diferencahoraminutos);  
		 String hrs =preencheCom(String.valueOf(hr),"0",2,1)+ ":"+ preencheCom(String.valueOf(mm),"0",2,1);
		 return hrs;
	 }    

    
     
    
    public static void main(String[] args) {
    	String range = "0CBD00001";
    	System.out.println(range.substring(4, 9));
	}
    
    
    
    
}
