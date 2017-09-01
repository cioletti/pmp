package com.pmp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;


public class ClienteSmart {

 public static void main(String[] args) throws ClientProtocolException, IOException {
		 
		 
		 HttpClient client = new DefaultHttpClient();
		 HttpPost request = new HttpPost("http://54.235.103.56:8080/IntegradorPESA/rest/getRequisicao/XMLRequisicao/");
		 File importFile = new File("C:\\teste.pdf");
		 String xmlRequisicao ="<?xml version='1.0' encoding='utf-8'?><requests xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema'><row no='1'><fl val='Customer Name'>Teste Wiki</fl><fl val='Customer ID'>10001</fl><fl val='Contact'>Contato teste</fl><fl val='Phone'></fl><fl val='Machine Serial'>123456</fl><fl val='Subject'>Requisicao de teste PMP</fl><fl val='Location'>MWM INTERNATIONAL IND. - AGNALDO 11 8420 7608 - SA</fl><fl val='Complete Date'>2013-04-19T14:52:23.000</fl><fl val='linkRpm'>https://pesa.rpmindustries.org/~/CSA/detail.aspx?WO=BS33272&amp;Customer=0907629&amp;Serial=AT17DR0471</fl><fl val='relatorioPMP'>arquivo.pdf</fl><exception no='1'><fl val='Status'>Ação</fl><fl val='Description'>LIMPAR RADIADOR</fl><fl val='Comments'>MWM INTERNATIONAL IND. - AGNALDO 11 8420 7608 - SA</fl><fl val='SMCS Code'>1000</fl></exception><exception no='2'><fl val='Status'>Monitorar</fl><fl val='Description'>Inspecionar Correias em (V)</fl><fl val='Comments'>MWM INTERNATIONAL IND. - AGNALDO 11 8420 7608 - SA</fl><fl val='SMCS Code'>1000</fl></exception></row></requests>";
		 
		 request.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		 request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		    request.setHeader("XML",xmlRequisicao);
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("myImageFile", new FileBody(importFile));
			request.setEntity(entity);
			
			
			
		 HttpResponse response = client.execute(request);
		 BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		 String line = "";
		 while ((line = rd.readLine()) != null) {
		    System.out.println(line);
		 }
		 
	 }
}
