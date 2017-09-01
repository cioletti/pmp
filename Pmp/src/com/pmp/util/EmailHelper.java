package com.pmp.util;
import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;


public class EmailHelper {
	 
			// private static final String SMTP_HOST_NAME = "mail.pesa.com.br";//192.168.2.12//dominio pesa.local
			// private static final String SMTP_HOST_NAME = "192.168.1.200";//192.168.2.12//dominio pesa.local
			 private static final String SMTP_HOST_NAME = "172.18.1.38";//192.168.2.12//dominio pesa.local
			 private static final String SMTP_AUTH_USER = "control_dse@pesa.com.br";
			 private static final String SMTP_AUTH_PWD  = "@11caeha";
//			 private static final String SMTP_HOST_NAME = "192.168.2.12";
//			 private static final String SMTP_AUTH_USER = "control_dse@pesa.com.br";
//			 private static final String SMTP_AUTH_PWD  = "w8o#ay3*";

			public boolean sendSimpleMail (String msg, String subject, String to, File file){
				try{
					Properties props = new Properties();
					props.put("mail.transport.protocol", "smtp");
					props.put("mail.smtp.host", SMTP_HOST_NAME);
					//props.put("mail.smtp.starttls.enable", "true");
					//props.put("mail.smtp.auth", "true");
					//props.put("mail.smtp.port", "587");

					
					//Authenticator auth = new SMTPAuthenticator();
					//Session mailSession = Session.getDefaultInstance(props, auth);
					Session mailSession = Session.getDefaultInstance(props);
					// uncomment for debugging infos to stdout
					// mailSession.setDebug(true);
					Transport transport = mailSession.getTransport();

					MimeMessage message = new MimeMessage(mailSession);
					//message.setContent(msg, "text/plain");
					message.setSubject(MimeUtility.encodeText(subject, "ISO-8859-1", null));
					message.setFrom(new InternetAddress("control_dse@pesa.com.br"));
					message.addRecipient(Message.RecipientType.TO,
							new InternetAddress(to));
//				     Cria a primeira parte do email (com o corpo do texto)  
					MimeBodyPart mbp1 = new MimeBodyPart();  
					mbp1.setText(new String(msg.getBytes(), "ISO-8859-1"));
					
					//message.setContent(new String(msg.getBytes(), "ISO-8859-1"), "text/html");
					//message.setHeader("Content-Type", "text/html; charset=ISO-8859-1");
					//Cria a segunda parte do email  
					MimeBodyPart mbp2 = new MimeBodyPart(); 
					//Atacha o arquivo  
					FileDataSource fds = new FileDataSource(file);  
					mbp2.setDataHandler(new DataHandler(fds));  
					mbp2.setFileName("Checklist.pdf");
					
					//Cria um multipart adicionando aquelas 2 primeiras partes  
					Multipart mp = new MimeMultipart();  
					mp.addBodyPart(mbp1);  
					mp.addBodyPart(mbp2);  
					message.setContent(mp);
					transport.connect();

					transport.sendMessage(message,
							message.getRecipients(Message.RecipientType.TO));
					transport.close();
					return true;

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			
			}

			private class SMTPAuthenticator extends javax.mail.Authenticator {
				public PasswordAuthentication getPasswordAuthentication() {
					String username = SMTP_AUTH_USER;
					String password = SMTP_AUTH_PWD;
					return new PasswordAuthentication(username, password);
				}
			}
				
			public boolean sendSimpleMail(String msg, String subject, String to) {
				try {
					Properties props = new Properties();
					props.put("mail.transport.protocol", "smtp");
					props.put("mail.smtp.host", SMTP_HOST_NAME);
					//props.put("mail.smtp.auth", "true");
					//props.put("mail.smtp.starttls.enable", "true");
					// props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
					//props.put("mail.smtp.port", "587");

					//Authenticator auth = new SMTPAuthenticator();
					Session mailSession = Session.getDefaultInstance(props);
					// uncomment for debugging infos to stdout
					// mailSession.setDebug(true);
					Transport transport = mailSession.getTransport();

					MimeMessage message = new MimeMessage(mailSession);
					message.setSubject(MimeUtility.encodeText(subject, "ISO-8859-1", null));
					message.setContent(new String(msg.getBytes(), "ISO-8859-1"), "text/html");
					message.setFrom(new InternetAddress("control_dse@pesa.com.br"));
					//message.setHeader("Content-Type", "text/html; charset=ISO-8859-1");
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

					transport.connect();

					transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
					transport.close();
					return true;

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}

			}
			
			public static void main(String[] args) {
				for (int i = 0; i < 1; i++) {
					String msg = " Flademir informamos que o equipamento com:" +
							"<br> horímetro: 1400 <br>serie: XXXXXX <br>contrato: YYYYYYYYY<br> Entrou no período crítico, para realizar a revisão de 1500 horas\n";
					new EmailHelper().sendSimpleMail(msg, "Teste", "Costa_Flademir@pesa.com.br");
					new EmailHelper().sendSimpleMail(msg, "Teste", "cioletti.rodrigo@gmail.com");
					System.out.println("enviei");
					
				}
			}
}
