package com.pmp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class JobSendBacklogSmartConnection implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		EntityManager manager = null;
		SimpleDateFormat smt = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
		
		try{
			manager = JpaUtil.getInstance();
			String SQL = "update"+
			" OS_PALM "+
			" set IS_SEND_SMART_CONNECTION = null"+
			" where  (MSG_SMART_CONNECTION like '%Erro%' or MSG_SMART_CONNECTION like '%PDF corrompido%') "; 
			Query query;
			try {
				manager.getTransaction().begin();
				query = manager.createNativeQuery(SQL);
				query.executeUpdate();
				manager.getTransaction().commit();
			} catch (Exception e) {
				if(manager != null && manager.getTransaction().isActive()){
					manager.getTransaction().rollback();
				}
				e.printStackTrace();
			}
			
			SQL = "select distinct os.IDOS_PALM, os.CLIENTE, c.CODIGO_CLIENTE ,co.CONTATO, co.TELEFONE_CONTATO, c.NUMERO_SERIE,"+
			"	co.LOCAL, f.DESCRICAO, CONVERT(varchar(10), os.EMISSAO, 103) as emissao, c.EMAIL_CONTATO_COMERCIAL, os.TECNICO "+
			"	from OS_PALM os, OS_PALM_DT dt, PMP_AGENDAMENTO a, PMP_CONFIG_OPERACIONAL co, PMP_CONTRATO c, PMP_FAMILIA f"+
			"	where os.IDOS_PALM = dt.OS_PALM_IDOS_PALM"+
			//"	and dt.STATUS = 'NC'"+
			"	and (os.is_send_smart_connection <> 'S' or os.is_send_smart_connection is null)"+
			"	and os.ID_AGENDAMENTO = a.id"+
			"	and a.ID_CONG_OPERACIONAL = co.ID"+
			"	and co.ID_CONTRATO = c.id" +
			"   and os.ID_FAMILIA = f.ID";
			query = manager.createNativeQuery(SQL);

			List<Object[]> result = query.getResultList();

			InputStream in = new ConectionDbs().getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
			Properties prop = new Properties();
			
			prop.load(in);
			String strURL = prop.getProperty("url.smart.connection");
			
			//String strURL = "http://54.235.103.56:8080/IntegradorPESA/rest/getRequisicao/XMLRequisicao/";

			//			 Map<String, String> cpf = new HashMap<String, String>();
			//			 cpf.put("cpf","002343234");	

			//URL url = new URL(strURL);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
			System.out.println("vou entrar no loop");
			for (Object[] objects : result) {
				System.out.println("Entrei no loop");
				String nomeArquivo = ((String)objects[1]).replace("/", "")+"_"+(String)objects[5]+"_"+smt.format(new Date());
				File importFile =  File.createTempFile(nomeArquivo,".pdf", new File("."));
				URL url = new URL("http://"+IConstantAccess.IP+":8080/Pmp/InspecaoReport?osPalmId="+(BigDecimal)objects[0]+"&equipamento="+((String)objects[7]).replace(" ", "%20"));   
				
				URLConnection connection = url.openConnection(); 
				OutputStream out=new FileOutputStream(importFile); 
				
				InputStream inputStream = connection.getInputStream(); 
				
				byte buf[]=new byte[1024];
				int len;
				while((len=inputStream.read(buf))>0){
					out.write(buf,0,len);
				}
				out.close();
				inputStream.close();
				//URLConnection connection = url.openConnection();
				//HttpURLConnection httpConn = (HttpURLConnection) connection;
				String strRequest ="<?xml version='1.0' encoding='utf-8'?><requests xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema'>" +
				"<row no='1'>" +
				"<fl val='Customer Name'><![CDATA["+(String)objects[1]+"]]></fl>" +
				"<fl val='Customer ID'><![CDATA["+(String)objects[2]+"]]></fl>" +
//				"<fl val='Customer ID'>10001</fl>" +
				"<fl val='Contact'><![CDATA["+(String)objects[3]+"]]></fl>" +
				"<fl val='Phone'><![CDATA["+(String)objects[4]+"]]></fl>" +
				"<fl val='Email'><![CDATA["+(String)objects[9]+"]]></fl>"+
				"<fl val='Machine Serial'><![CDATA["+(String)objects[5]+"]]></fl>" +
//				"<fl val='Machine Serial'>123456</fl>" +
				"<fl val='Subject'><![CDATA[Relatório PMP "+(String)objects[8]+"]]></fl>" +
				"<fl val='Location'><![CDATA["+(String)objects[6]+"]]></fl>" +
				"<fl val='Complete Date'><![CDATA["+dateFormat.format(new Date())+"]]></fl>" +
				"<fl val='linkRpm'></fl>" +
				"<fl val='relatorioPMP'><![CDATA["+nomeArquivo+".pdf]]></fl>"+
				"<fl val='tecnicoPMP'><![CDATA["+(String)objects[10]+"]]></fl>"+
				"<fl val='idPMP'><![CDATA["+(BigDecimal)objects[0]+"]]></fl>";

				SQL = "select arv.DESCRICAO, convert(varchar(4000),dt.OBS), arv.SMCS from OS_PALM_DT dt, ARV_INSPECAO arv"+
				" where dt.OS_PALM_IDOS_PALM ="+(BigDecimal)objects[0]+
				" and dt.STATUS = 'NC'" +
				" and arv.ID_ARV = dt.ID_IDARV";	
				query = manager.createNativeQuery(SQL);
				
				List<Object[]> resultBacklog = query.getResultList();
				int count = 1;
				for (Object[] objectsBack : resultBacklog) {

					strRequest += "<exception no='"+count+"'>" +
					"<fl val='Status'><![CDATA[Não Conforme]]></fl>" +
					"<fl val='Description'><![CDATA["+(String)objectsBack[0]+"]]></fl>" +
					"<fl val='Comments'><![CDATA["+(String)objectsBack[1]+"]]></fl>" +
					"<fl val='SMCS Code'><![CDATA["+(String)objectsBack[2]+"]]></fl>" +
					"</exception>";
					count++;

				}
				strRequest += "</row></requests>";
				//byte[] requestXML = strRequest.getBytes();
				//System.out.println(strRequest);
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(strURL);
				request.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				request.setHeader("Content-Type", "application/x-www-form-urlencoded");

				request.setHeader("XML",strRequest);
				MultipartEntity entity = new MultipartEntity();
				entity.addPart("myImageFile", new FileBody(importFile));
				request.setEntity(entity);



				HttpResponse response = client.execute(request);
				BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				String msgSmartConnection = "";
				//System.out.println("Vou imprimir a resposta do smart connection!");
				 while ((line = rd.readLine()) != null) {
				    System.out.println(line);
				    msgSmartConnection += line;
				 }
				 importFile.delete();
				 if(msgSmartConnection != null && msgSmartConnection.contains("null")){
					 continue;
				 }
				// Set the appropriate HTTP parameters.
//				httpConn.setRequestProperty( "Content-Length", String.valueOf( requestXML.length ) );
//				httpConn.setRequestProperty("Accept-Charset", "UTF-8");
//				httpConn.setRequestProperty("Content-Type","text/xml; charset=utf-8");
//				httpConn.setRequestMethod( "POST" );
//
//
//				httpConn.setDoOutput(true);
//				httpConn.setDoInput(true);

				// Send the String that was read into postByte.
//				OutputStream out = httpConn.getOutputStream();
//				out.write(requestXML);
//				out.close();

				// Read the response and write it to standard out.
//				InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
//				BufferedReader br = new BufferedReader(isr);
//				String temp;
//				String tempResponse = "";
//
//				//Create a string using response from web services
//				while ((temp = br.readLine()) != null)
//					tempResponse = tempResponse + temp;
//				String responseXML = tempResponse;
//				br.close();
//				isr.close();
//				httpConn.disconnect();
				 if(msgSmartConnection.length() > 1){
					 SQL = "update OS_PALM set IS_SEND_SMART_CONNECTION = 'S', MSG_SMART_CONNECTION = '"+msgSmartConnection+"'"+
					 "	where IDOS_PALM = "+(BigDecimal)objects[0];
					 try {
						manager.getTransaction().begin();
						 query = manager.createNativeQuery(SQL);
						 query.executeUpdate();
						 manager.getTransaction().commit();
					} catch (Exception e) {
						if(manager != null && manager.getTransaction().isActive()){
							manager.getTransaction().rollback();
						}
						e.printStackTrace();
					}
				 }
				
			}
		} catch (java.net.MalformedURLException e) {
			e.printStackTrace();
			new EmailHelper().sendSimpleMail("Error in postRequest(): Secure Service Required", "Erro Smart Connection", "rodrigo@rdrsistemas.com.br");
			//System.out.println("Error in postRequest(): Secure Service Required");
		} catch (Exception e) {
			e.printStackTrace();
			new EmailHelper().sendSimpleMail("Error in postRequest(): " + e.getMessage(), "Erro Smart Connection", "rodrigo@rdrsistemas.com.br");
			//System.out.println("Error in postRequest(): " + e.getMessage());
		} finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
	}
public static void main(String[] args) {
	try {
		String s = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><response>    <message>Erro ao verificar se a Empresa existe no Connexion: null</message>    <sucess>false</sucess></response>";
		System.out.println(s.contains("null"));
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}
}
