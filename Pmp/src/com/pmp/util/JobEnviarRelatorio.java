package com.pmp.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pmp.business.ContratoBusiness;

public class JobEnviarRelatorio implements Job{

	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		EntityManager manager = null;
		EmailHelper emailHelper = new EmailHelper();
		File file = null;
		try{
			try {
				manager = JpaUtil.getInstance();

				JasperReport jasperReport = null;  
				JasperPrint pdfInspecao = null; 
				
				
				
				 String caminhoJasper =  getBasePath()+"/tabelaPreco.jasper";
				 String pathSubreport = getBasePath()+"./";
				 

				//Carrega o arquivo com o jasperReport  
				try {  
					jasperReport = (JasperReport) JRLoader.loadObject( caminhoJasper);  
				} catch (Exception jre) {  
					jre.printStackTrace();  
				}  

				try {

					Map parametros = new HashMap();  

					parametros.put("SUBREPORT_DIR", pathSubreport);  

					ContratoBusiness business = new ContratoBusiness();

					JRBeanCollectionDataSource result =  new JRBeanCollectionDataSource(business.findTabelaPreco());
					//Gera o excel para exibicao..
					byte[] bytes = null;
					file = new File(getBasePath()+"/tabelaPrecos.xls");
					file.createNewFile();
					try {  
						pdfInspecao = JasperFillManager.fillReport(jasperReport, parametros, result);  
						JRXlsExporter exporter = new JRXlsExporter();     
						ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();     
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, pdfInspecao);    
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, xlsReport);     
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);     
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);     
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE); 
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						//exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, getBasePath()+"/tabelaPrecos.xls"); 

						exporter.exportReport();     
						//bytes = xlsReport.toByteArray(); 
 
						FileOutputStream in = new FileOutputStream(file) ;    
						in.write(xlsReport.toByteArray());  
						in.close(); 
						
						
					} catch (Exception jre) {  
						jre.printStackTrace();  
					}  

				} catch (Exception e) {
					e.printStackTrace();
				}

			}catch (Exception e1) {
				e1.printStackTrace();
				//File file = File.createTempFile("TabelaPreco", ".xls", new File("."));
			}
			Query query = manager.createNativeQuery("select tw.email from tw_funcionario tw, adm_perfil_sistema_usuario u"+
					" where u.id_tw_usuario = tw.epidno"+
					" and u.id_sistema = (select s.id from adm_sistema s where s.sigla = 'PMP')"+
			" and u.id_perfil = (select p.id from adm_perfil p where p.tipo_sistema = 'PMP' and p.sigla = 'CON')");

			List<String> list = (List<String>) query.getResultList();
			for(String mail : list ){
				
				emailHelper.sendSimpleMail("Tabela de Preços", "Relatório: tabela de preços", mail, file);
			}

		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			file.delete();
			manager.close();
		}
	}
	public String getBasePath() { 
        String path = this.getClass().getClassLoader().getResource("com/pmp/util").getPath();
        return path; 
    } 
	public static void main(String[] args) {
		System.out.println(new JobEnviarRelatorio().getBasePath());
	}
}
