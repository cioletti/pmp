package com.pmp.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pmp.entity.TrCargo;
import com.pmp.entity.TwFilial;
import com.pmp.entity.TwFuncionario;
import com.zehon.FileTransferStatus;
import com.zehon.exception.FileTransferException;
import com.zehon.sftp.SFTP;

public class JobImportarFuncionario implements Job{

	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		EntityManager manager = null;
		try{
			try {
				manager = JpaUtil.getInstance();
				String host = "sftp2.deloitte.com.br";
				String username = "paranaequip";
				String password = "3Quip@DTTct8";
				String sftpFolder = "/Cioleti/";		

				String writeToLocalFolder = "/opt/rdr/";
				//String writeToLocalFolder = "C:\\teste";
				String nameOfFile = "PESA.TXT";
				try {
					int status = SFTP.getFile(nameOfFile, sftpFolder, host, username, password, writeToLocalFolder);
					if(FileTransferStatus.SUCCESS == status){
						//System.out.println(nameOfFile + " got downloaded successfully to  folder "+writeToLocalFolder);
						new EmailHelper().sendSimpleMail("Início de importação de funcionário", "Início de importação de funcionário", "rodrigo@rdrsistemas.com.br");
						FileInputStream stream = new FileInputStream(writeToLocalFolder+"/PESA.TXT");
						InputStreamReader reader = new InputStreamReader(stream);
						BufferedReader br = new BufferedReader(reader);
						br.readLine();
						String linha = br.readLine();
						while(linha != null) {
							String aux = linha;
							String [] dados = aux.split("	"); 
							try {
								//TwFuncionario funcionario = manager.find(TwFuncionario.class, dados[1]);
								TwFuncionario twFuncionario = new TwFuncionario();
								Query query = manager.createQuery("from TwFuncionario where epidno = '"+dados[1]+"'");
								
								
								
								if(query.getResultList().size() > 0){
									twFuncionario = (TwFuncionario)query.getSingleResult();
									//filialFunc = (TwFilial)query.getSingleResult();
									//twFuncionario.setStn1(String.valueOf(filialFunc.getStno()));
								}else{
									try {
										TwFilial filialFunc = new TwFilial();
										String [] filial = dados[2].split(" "); 
										query = manager.createQuery("from TwFilial where filialImportacao =:filial");
										if(filial.length == 5){
											query.setParameter("filial", filial[3]+" "+filial[4]);
										}else{
											query.setParameter("filial", filial[3]);
										}
										if(query.getResultList().size() > 0){
											filialFunc = (TwFilial)query.getSingleResult();
											twFuncionario.setStn1(String.valueOf(filialFunc.getStno()));
										}
										String [] horas = dados[7].split(":");
										if(Long.valueOf(horas[0]) <= 8){
											twFuncionario.setTurno(1L);
										}else{
											twFuncionario.setTurno(2L);
										}
										if(twFuncionario.getEpidno() == null || twFuncionario.getEpidno().equals("")){
											if(dados[4] != null && !("").equals(dados[4])){
												twFuncionario.setEmail(dados[4]);
											}
										}
										twFuncionario.setTelefone(dados[5]);
										twFuncionario.setCentroCusto(dados[9]);
										twFuncionario.setDescricaoCentroCusto(dados[10]);
										
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								//if(funcionario != null){
									if(!dados[8].equals("ATIVO") && !dados[8].equals("AFASTADO") && !dados[8].equals("FERIAS")){
										query = manager.createQuery("delete from AdmPerfilSistemaUsuario where idTwUsuario.epidno =:epidno");
										query.setParameter("epidno", dados[1].trim());
										manager.getTransaction().begin();
										query.executeUpdate();
										manager.getTransaction().commit();
									}
//									String [] filial = dados[2].split(" "); 
									twFuncionario.setEpidno(dados[1].trim());
									twFuncionario.setEplsnm(dados[0]);
									twFuncionario.setCentroCusto(dados[9]);
									twFuncionario.setDescricaoCentroCusto(dados[10]);
//									query = manager.createQuery("from TwFilial where filialImportacao =:filial");
//									if(filial.length == 5){
//										query.setParameter("filial", filial[3]+" "+filial[4]);
//									}else{
//										query.setParameter("filial", filial[3]);
//									}
//									TwFilial filialFunc = (TwFilial)query.getSingleResult();
//								twFuncionario.setStn1(String.valueOf(filialFunc.getStno()));
									query = manager.createQuery("from TrCargo where descricao =:descricao");
									query.setParameter("descricao", dados[3]);
									if(query.getResultList().size() > 0){
										TrCargo cargo = (TrCargo)query.getSingleResult();
										if(cargo != null){
											twFuncionario.setIdTrCargo(cargo);
										}
									}
									//twFuncionario.setEmail(dados[4]);
									//twFuncionario.setTelefone(dados[5]);
									String cpf = dados[6];
									twFuncionario.setCpf(cpf);
									
//									if(twFuncionario.getEpidno().equals("338226")){
//										System.out.println(twFuncionario.getEpidno());
//										System.out.println(dados[1].trim());
//									}
									manager.getTransaction().begin();
									manager.merge(twFuncionario);
									manager.getTransaction().commit();
									
								//}
								//System.out.println(dados[8]);
							} catch (Exception e) {
								if(manager != null && manager.getTransaction().isActive()){
									manager.getTransaction().rollback();
								}
								e.printStackTrace();
							}
							linha = br.readLine();
						}
						
						Query query = manager.createNativeQuery("select distinct CENTRO_CUSTO from Tw_Funcionario");
						List<String> centroCustoList = query.getResultList();
						for (String centroCusto : centroCustoList) {
							String SQL = "select f.EPIDNO, f.EPLSNM, c.DESCRICAO, c.GRADE from TW_FUNCIONARIO f, TR_CARGO c where CENTRO_CUSTO = '"+centroCusto+"'"+
										 "	 and c.ID = f.ID_CARGO"+
										 "	 order by c.GRADE desc"	;	
							query = manager.createNativeQuery(SQL);
							List<Object[]> funcPair = (List<Object[]>)query.getResultList();
							if(funcPair.size() > 1){
								String epidnoSuper = (String)funcPair.get(0)[0];
								Integer gardeSuper = (Integer)funcPair.get(0)[3];
								for (int i = 1; i < funcPair.size(); i++) {
									Object[] pair = funcPair.get(i);
									Integer gradeFunc = (Integer)pair[3];
									if(gradeFunc == null || gardeSuper > gradeFunc){
										SQL = " update TW_FUNCIONARIO set EPIDNO_SUPERVISOR = '"+epidnoSuper+"' where EPIDNO = '"+(String)pair[0]+"'";
										query = manager.createNativeQuery(SQL);
										manager.getTransaction().begin();
										query.executeUpdate();
										manager.getTransaction().commit();
									}
									
								}
							}
						}
						
						
						new EmailHelper().sendSimpleMail("Término de importação de funcionário", "Término de importação de funcionário", "rodrigo@rdrsistemas.com.br");
					}
					else if(FileTransferStatus.FAILURE == status){
						System.out.println("Fail to download  to  folder "+writeToLocalFolder);
						new EmailHelper().sendSimpleMail("Falha na importação de funcionário", "Falha na importação de funcionáro", "rodrigo@rdrsistemas.com.br");
					}
					
					
				} catch (Exception e) {
					if(manager != null && manager.getTransaction().isActive()){
						manager.getTransaction().rollback();
					}
					StringWriter sw = new StringWriter();
				      PrintWriter pw = new PrintWriter(sw);
				      e.printStackTrace(pw);
					new EmailHelper().sendSimpleMail("ERRO AO EXECUTAR IMPORTAÇÃO DE FUNCIONARIO "+sw.toString(), "ERRO AO EXECUTAR IMPORTAÇÃO DE FUNCIONARIO", "rodrigo@rdrsistemas.com.br");
					//e.printStackTrace();
				}
				

			}catch (Exception e1) {
				e1.printStackTrace();
			}

		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
	}
//	public void execute(JobExecutionContext arg0) throws JobExecutionException {
//		EntityManager manager = null;
//		try{
//			try {
//				manager = JpaUtil.getInstance();
//				String host = "sftp2.deloitte.com.br";
//				String username = "paranaequip";
//				String password = "3Quip@DTTct8";
//				String sftpFolder = "/Cioleti/";		
//				
//				//String writeToLocalFolder = "/home/rdr/";
//				String writeToLocalFolder = "C:\\";
//				String nameOfFile = "PESA.TXT";
//				try {
//					//int status = SFTP.getFile(nameOfFile, sftpFolder, host, username, password, writeToLocalFolder);
//					if(FileTransferStatus.SUCCESS == 1){
//						System.out.println(nameOfFile + " got downloaded successfully to  folder "+writeToLocalFolder);
//						FileInputStream stream = new FileInputStream(writeToLocalFolder+"\\PESA.TXT");
//						InputStreamReader reader = new InputStreamReader(stream);
//						BufferedReader br = new BufferedReader(reader);
//						br.readLine();
//						String linha = br.readLine();
//						while(linha != null) {
//							String aux = linha;
//							String [] dados = aux.split("	"); 
//							if("CARENA SOLUCOES ACUSTICAS LTDA".equals(dados[2]) ||
//									"TRANSPORTES TORINO LTDA PR".equals(dados[2]) ||
//									"TRANSPORTES TORINO LTDA SP".equals(dados[2]) ||
//									"BR GERADORES LTDA".equals(dados[2]) ||
//									"SINERGIA EMPREENDIMENTOS E PARTICIPACOES SA".equals(dados[2]) ||
//									"SUPERTEK COM E SERV DE IMP E EXPORT SA PR".equals(dados[2]) ||
//									"SUPERTEK COM E SERV DE IMP E EXPORT SA SC".equals(dados[2]) ||
//									"SUPERTEK COM E SERV DE IMP E EXPORT SA RS".equals(dados[2]) ||
//									"CURIPECAS COM DE PECAS TRATORES USADOS LTDA".equals(dados[2])){
//							try {
//									//TwFuncionario funcionario = manager.find(TwFuncionario.class, dados[1]);
//									TwFuncionario twFuncionario = new TwFuncionario();
//									Query query = manager.createQuery("from TwFuncionario where epidno = '"+dados[1]+"'");
//								
//
//
//									if(!"ATIV EXTERNA ART 62".contains(dados[7])){
//										linha = br.readLine();
//										continue;
//										//twFuncionario = (TwFuncionario)query.getSingleResult();
//										//filialFunc = (TwFilial)query.getSingleResult();
//										//twFuncionario.setStn1(String.valueOf(filialFunc.getStno()));
//									}else{
//										try {
//											TwFilial filialFunc = new TwFilial();
//
//
//
//
//											//String [] filial = dados[2].split(" "); 
//											query = manager.createQuery("from TwFilial where filialImportacao =:filial");
//											//										if(filial.length == 5){
//											//											query.setParameter("filial", filial[3]+" "+filial[4]);
//											//										}else{
//											query.setParameter("filial", dados[2]);
//											//}
//											if(query.getResultList().size() > 0){
//												filialFunc = (TwFilial)query.getSingleResult();
//												twFuncionario.setStn1(String.valueOf(filialFunc.getStno()));
//											}
//											String [] horas = dados[7].split(":");
//											//if(Long.valueOf(horas[0]) <= 8){
//												twFuncionario.setTurno(1L);
//											//}else{
//												//twFuncionario.setTurno(2L);
//											//}
//											twFuncionario.setLogin(dados[1]);
//											twFuncionario.setSenha(dados[1]);
//											twFuncionario.setEmail(dados[4]);
//											twFuncionario.setTelefone(dados[5]);
//										} catch (Exception e) {
//											e.printStackTrace();
//										}
//									}
//									//if(funcionario != null){
//									if(!dados[8].equals("ATIVO") && !dados[8].equals("AFASTADO") && !dados[8].equals("FERIAS")){
//										query = manager.createQuery("delete from AdmPerfilSistemaUsuario where idTwUsuario.epidno =:epidno");
//										query.executeUpdate();
//									}
//									//									String [] filial = dados[2].split(" "); 
//									twFuncionario.setEpidno(dados[1].trim());
//									twFuncionario.setEplsnm(dados[0]);
//									//									query = manager.createQuery("from TwFilial where filialImportacao =:filial");
//									//									if(filial.length == 5){
//									//										query.setParameter("filial", filial[3]+" "+filial[4]);
//									//									}else{
//									//										query.setParameter("filial", filial[3]);
//									//									}
//									//									TwFilial filialFunc = (TwFilial)query.getSingleResult();
//									//								twFuncionario.setStn1(String.valueOf(filialFunc.getStno()));
//									query = manager.createQuery("from TrCargo where descricao =:descricao");
//									query.setParameter("descricao", dados[3]);
//									if(query.getResultList().size() > 0){
//										TrCargo cargo = (TrCargo)query.getSingleResult();
//										if(cargo != null){
//											manager.persist(cargo);
//										}
//										twFuncionario.setIdTrCargo(cargo);
//									}else{
//										TrCargo cargo = new TrCargo();
//										cargo.setDescricao(dados[3]);
//										manager.getTransaction().begin();
//										manager.persist(cargo);
//										manager.getTransaction().commit();
//										twFuncionario.setIdTrCargo(cargo);
//									}
//									//twFuncionario.setEmail(dados[4]);
//									//twFuncionario.setTelefone(dados[5]);
//									String cpf = dados[6];
//									twFuncionario.setCpf(cpf);
//
//									//									if(twFuncionario.getEpidno().equals("338226")){
//									//										System.out.println(twFuncionario.getEpidno());
//									//										System.out.println(dados[1].trim());
//									//									}
//									manager.getTransaction().begin();
//									if(twFuncionario.getEpidno() != null){
//										manager.merge(twFuncionario);
//									}else{
//										manager.persist(twFuncionario);
//									}
//									
//									manager.getTransaction().commit();
//									query = manager.createNativeQuery("select * from ADM_PERFIL_SISTEMA_USUARIO where ID_TW_USUARIO = '"+dados[1]+"' and ID_PERFIL = 2 and ID_SISTEMA = 1");
//									if(query.getResultList().size() == 0){
//
//										if(dados[8].equals("ATIVO")){
//											manager.getTransaction().begin();
//											query = manager.createNativeQuery("  insert into ADM_PERFIL_SISTEMA_USUARIO (ID_PERFIL, ID_SISTEMA, ID_TW_USUARIO) values (2, 1, '"+twFuncionario.getEpidno()+"')");
//											query.executeUpdate();
//											manager.getTransaction().commit();
//										}
//									}
//								//}
//								//System.out.println(dados[8]);
//									linha = br.readLine();
//							} catch (Exception e) {
//								if(manager != null && manager.getTransaction().isActive()){
//									manager.getTransaction().rollback();
//								}
//								e.printStackTrace();
//							}
//							}else{
//								linha = br.readLine();
//								continue;
//							}
//						}
//					}
//					else if(FileTransferStatus.FAILURE == 0){
//						System.out.println("Fail to download  to  folder "+writeToLocalFolder);
//					}
//
//
//				} catch (Exception e) {
//
//					StringWriter sw = new StringWriter();
//					PrintWriter pw = new PrintWriter(sw);
//					e.printStackTrace(pw);
//					new EmailHelper().sendSimpleMail("ERRO AO EXECUTAR IMPORTAÇÃO DE FUNCIONARIO "+sw.toString(), "ERRO AO EXECUTAR IMPORTAÇÃO DE FUNCIONARIO", "rodrigo@rdrsistemas.com.br");
//					//e.printStackTrace();
//				}
//
//
//			}catch (Exception e1) {
//				e1.printStackTrace();
//			}
//
//		}catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			if(manager != null && manager.isOpen()){
//				manager.close();
//			}
//		}
//	}
	public String getBasePath() { 
        String path = this.getClass().getClassLoader().getResource("com/pmp/util").getPath();
        return path; 
    } 
	public static void main(String[] args) {
		System.out.println(new JobImportarFuncionario().getBasePath());
	}
}
