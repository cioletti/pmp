package com.pmp.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import com.pmp.bean.FilialBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.business.OsBusiness;
import com.pmp.util.DateHelper;

/**
 * Servlet implementation class RelatorioContratosFinalizadosSemRenovacao
 */
public class RelatorioContratosFinalizadosSemRenovacao extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public RelatorioContratosFinalizadosSemRenovacao() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getMediaAtendimentos(request, response);
	}
	private void getMediaAtendimentos(HttpServletRequest request, HttpServletResponse response) {

		String idFilial = request.getParameter("idFilial");
		//String beginDate = request.getParameter("beginDate");
		//String endDate = request.getParameter("endDate");
		JasperReport jasperReport = null;  
		JasperPrint pdfInspecao = null;    
		


		//Obtem o caminho do .jasper  
		ServletContext servletContext = super.getServletContext();  
		String caminhoJasper = servletContext.getRealPath("WEB-INF/relatorios/ContratosFinalizadosSemRenovacao.jasper"); 
		String pathSubreport = servletContext.getRealPath("WEB-INF/relatorios/")+"/"; 

		//Carrega o arquivo com o jasperReport  
		try {  
			jasperReport = (JasperReport) JRLoader.loadObject( caminhoJasper );  
		} catch (Exception jre) {  
			jre.printStackTrace();  
		}  
		Connection conn = null;

		try {
			conn = com.pmp.util.Connection.getConnection();

			OsBusiness business = new OsBusiness((UsuarioBean)request.getSession().getAttribute("usuario"));
			
			List<BigDecimal> listFilial = new ArrayList<BigDecimal>();
			
			if(!idFilial.equals("-1")) { //Se foi selecionada uma filial específica
				listFilial.add(BigDecimal.valueOf(Long.valueOf(idFilial)));
			} else { //Se foi selecionadas todas filiais
				for (FilialBean filial : business.findAllFiliais()) {
					listFilial.add(BigDecimal.valueOf(filial.getStno())); //Adiciona à lista o ID da filial
				}
			}
			
			
			Map parametros = new HashMap();  
			parametros.put("SUBREPORT_DIR", pathSubreport);  
			parametros.put("FILIAL", listFilial);  
	
			InputStream inputStream =  getClass().getClassLoader().getResourceAsStream("logo.gif");

			File img=File.createTempFile("img", "gif",new File("."));

			OutputStream out=new FileOutputStream(img);
			byte buf[]=new byte[1024];
			int len;
			while((len=inputStream.read(buf))>0)
				out.write(buf,0,len);
			out.close();
			inputStream.close();	
			parametros.put("logo", img);

			//Gera o excel para exibicao..
			byte[] bytes = null;
			try {  

				pdfInspecao = JasperFillManager.fillReport(jasperReport, parametros, conn);
				JRXlsExporter exporter = new JRXlsExporter();     
				ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();     
				exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, pdfInspecao);    
				exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, xlsReport);     
				exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);     
				exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);     
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);  
				exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE); 
				//exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, "c:/relatorio.xls");  

				exporter.exportReport();     
				bytes = xlsReport.toByteArray();     
			} catch (Exception jre) {  
				jre.printStackTrace();  
			}  

			//Parametros para nao fazer cache e o que será exibido..  
			response.setContentType( "application/vnd.ms-excel" );  
			//			response.setHeader("Content-disposition",
			//			"attachment; filename=transferencia" ); 
			response.setHeader("content-disposition", "inline; filename=totalRevisao.xls");   

			//Envia para o navegador o pdf..  
			ServletOutputStream servletOutputStream = response.getOutputStream();  
			servletOutputStream.write( bytes );  
			servletOutputStream.flush();  
			servletOutputStream.close();
			img.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}


