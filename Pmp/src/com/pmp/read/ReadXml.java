package com.pmp.read;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.pmp.entity.PmpMaquinaPl;
import com.pmp.util.JpaUtil;

public class ReadXml {

	/**
	 * @param args
	 */
	public static void readXmlPL(String response) {
		//String xml = "C:\\Documents and Settings\\RDR\\Desktop\\xml.xml";
		response = "<messages>"+ response.substring(response.indexOf("</nextBuffer>")+13, response.indexOf("</smuLoc:topic>"))+"</messages>";
		//System.out.println(response);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		DocumentBuilder db;
		List<message> messageList = new ArrayList<message>();
		EntityManager manager = null;
		try {
			File xml = File.createTempFile("xml", "xml", new File("."));
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(xml));
			bufferedWriter.write(response);
			bufferedWriter.flush();
			bufferedWriter.close();
			db = dbf.newDocumentBuilder();
			Document doc = db.parse( xml );  
			Element elem = doc.getDocumentElement();  
			// pega todos os elementos usuario do XML  
			NodeList nl = elem.getElementsByTagName( "message" ); 
			// percorre cada elemento usuario encontrado  
			manager = JpaUtil.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Query query = null;
			for( int i=0; i<nl.getLength(); i++ ) {  
				PmpMaquinaPl maquinaPl = new PmpMaquinaPl();
				try {
					manager.getTransaction().begin();
					message message = new message();
					Element tagMessage = (Element) nl.item( i );  
					NodeList messageChidrenList = tagMessage.getChildNodes();
					header header = new header();
					Element headerEl = (Element)messageChidrenList.item(0);
					equipament equipament = new equipament();
					Element equipamentEl = (Element)headerEl.getChildNodes().item(2);
//					if(getChildTagValue( equipamentEl, "serialNumber" ).equals("JKM03144") || getChildTagValue( equipamentEl, "serialNumber" ).equals("RTB02829")){
//						System.out.println("entrei");
//						manager.getTransaction().begin();
//					}else{
//						continue;
//					}
					header.setMessageId(getChildTagValue( headerEl, "messageId" ));   
					header.setMasterMsgId(getChildTagValue( headerEl, "masterMsgId" )); 
					header.setModuleCode(getChildTagValue( headerEl, "moduleCode" )); 
					header.setModuleTime(getChildTagValue( headerEl, "moduleTime" )); 
					header.setReceivedTime(getChildTagValue( headerEl, "receivedTime" )); 
					header.setOwner(getChildTagValue( headerEl, "owner" ));

					equipament.setSerialNumber(getChildTagValue( equipamentEl, "serialNumber" )); 
					equipament.setMake(getChildTagValue( equipamentEl, "make" )); 
					equipament.setModel(getChildTagValue( equipamentEl, "model" )); 
					equipament.setNickname(getChildTagValue( equipamentEl, "nickname" )); 
					header.setEquipament(equipament);
					message.setHeader(header);
					if(messageChidrenList.getLength() == 3){
						serviceMeter meter = new serviceMeter();
						Element serviceMeterEl = (Element)messageChidrenList.item(1);
						meter.setValue(getChildTagValue( serviceMeterEl, "value" ));   
						meter.setUom(getChildTagValue( serviceMeterEl, "uom" ));
						message.setSerMeter(meter);
						location location = new location();
						Element locationEl = (Element)messageChidrenList.item(2);
						location.setLatitude(getChildTagValue( locationEl, "latitude" ));   
						location.setLongitude(getChildTagValue( locationEl, "longitude" ));   
						location.setValidity(getChildTagValue( locationEl, "validity" )); 
						message.setLocation(location);

						maquinaPl.setNumeroSerie("0"+message.getHeader().getEquipament().getSerialNumber());
						
						maquinaPl.setHorimetro(new Double(message.getSerMeter().getValue()).intValue());

						maquinaPl.setLatitude(message.getLocation().getLatitude());
						maquinaPl.setLongitude(message.getLocation().getLongitude());
						maquinaPl.setDataAtualizacao(df.parse(message.getHeader().getReceivedTime()));
						maquinaPl.setModelo(message.getHeader().getEquipament().getModel());
						maquinaPl.setMake(message.getHeader().getEquipament().getMake());
						maquinaPl.setModuleCode(message.getHeader().getModuleCode());
						maquinaPl.setModuleTime(df.parse(message.getHeader().getModuleTime()));
						maquinaPl.setReceivedTime(df.parse(message.getHeader().getReceivedTime()));
						maquinaPl.setMessageId(Long.valueOf(message.getHeader().getMessageId()));
						maquinaPl.setMasterMsgId(Long.valueOf(message.getHeader().getMasterMsgId()));
						maquinaPl.setNickname(message.getHeader().getEquipament().getNickname());
						query = manager.createNativeQuery("update EMS_DIAGNOSTIC set DATA_ATUALIZACAO_HORIMETRO ='"+message.getHeader().getReceivedTime()+"', HORIMETRO = "+maquinaPl.getHorimetro()+" where SERIAL_NUMBER = '"+maquinaPl.getNumeroSerie()+"'");
						query.executeUpdate();
					}else{
						location location = new location();
						Element locationEl = (Element)messageChidrenList.item(1);
						location.setLatitude(getChildTagValue( locationEl, "latitude" ));   
						location.setLongitude(getChildTagValue( locationEl, "longitude" ));   
						location.setValidity(getChildTagValue( locationEl, "validity" )); 
						message.setLocation(location);

						maquinaPl.setNumeroSerie("0"+message.getHeader().getEquipament().getSerialNumber());
						maquinaPl.setLatitude(message.getLocation().getLatitude());
						maquinaPl.setLongitude(message.getLocation().getLongitude());
						maquinaPl.setDataAtualizacao(df.parse(message.getHeader().getReceivedTime()));
						maquinaPl.setModelo(message.getHeader().getEquipament().getModel());
						maquinaPl.setMake(message.getHeader().getEquipament().getMake());
						maquinaPl.setModuleCode(message.getHeader().getModuleCode());
						maquinaPl.setModuleTime(df.parse(message.getHeader().getModuleTime()));
						maquinaPl.setReceivedTime(df.parse(message.getHeader().getReceivedTime()));
						maquinaPl.setMessageId(Long.valueOf(message.getHeader().getMessageId()));
						maquinaPl.setMasterMsgId(Long.valueOf(message.getHeader().getMasterMsgId()));
						maquinaPl.setNickname(message.getHeader().getEquipament().getNickname());
					}
					manager.persist(maquinaPl);
					manager.getTransaction().commit();
				} catch (Exception e) {
					if(manager.getTransaction().isActive()){
						manager.getTransaction().rollback();
					}
					e.printStackTrace();
				} 			        
			}  
			xml.delete();
		
		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		} 
	}
	 // este método lê e retorna o conteúdo (texto) de uma tag (elemento)  
    // filho da tag informada como parâmetro. A tag filho a ser pesquisada  
    // é a tag informada pelo nome (string)  
    private static String getChildTagValue( Element elem, String tagName ) {  
    	String nodo = null;
    	try {
    		if(tagName == null){
    			return "";
    		}
    		NodeList children = elem.getElementsByTagName( tagName );  
    		Element child = null;
    		if( children == null ) return null;  
    		child = (Element) children.item(0);  
    		if( child == null ) return null;

    		nodo = child.getFirstChild().getNodeValue();
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}  
    	return nodo;  
    } 
public static void main(String[] args) {
	System.out.println(Double.valueOf("254.60020368000002").intValue());
}
}
