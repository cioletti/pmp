package com.pmp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.pmp.bean.ApropriacaoHoras;
import com.pmp.util.Connection;
import com.pmp.util.JpaUtil;

/**
 * Servlet implementation class InspecaoReport
 */
public class InspecaoReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InspecaoReport() {
        super();
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
		String osPalmId  = request.getParameter("osPalmId");
		String equipamento = request.getParameter("equipamento");
		String printAPH = request.getParameter("printAPH");
		if(printAPH == null){
			printAPH = "S";
		}
		
		JasperReport jasperReport = null;  
		byte[] pdfInspecao = null;    

		//Obtem o caminho do .jasper  
		ServletContext servletContext = super.getServletContext();  
		String caminhoJasper = servletContext.getRealPath("WEB-INF/inspecao/inspecao.jasper"); 
		String pathSubreport = servletContext.getRealPath("WEB-INF/inspecao/")+"/"; 

		//Carrega o arquivo com o jasperReport  
		try {  
			jasperReport = (JasperReport) JRLoader.loadObject( caminhoJasper );  
		} catch (Exception jre) {  
			jre.printStackTrace();  
		}  
		EntityManager manager = null;
		java.sql.Connection conn = null;
		java.sql.PreparedStatement stm = null;
		ResultSet rs = null;
		try{

			Map parametros = new HashMap();  
			
			parametros.put("SUBREPORT_DIR", pathSubreport);  
			
			parametros.put("printAPH", printAPH);
			 
			parametros.put("equipamento", equipamento); 
			
			parametros.put("idos_palm", BigDecimal.valueOf(Integer.parseInt(osPalmId)));
		 
			
			InputStream inputStream =  getClass().getClassLoader().getResourceAsStream("Logo.jpg");
			File img = File.createTempFile("img", "jpg",new File("."));
			OutputStream out=new FileOutputStream(img);
			byte buf[]=new byte[1024];
			int len;
			while((len=inputStream.read(buf))>0)
				out.write(buf,0,len);
			out.close();
			inputStream.close();
			
			parametros.put("logo", img);			
			
			InputStream inputStreamCamera =  getClass().getClassLoader().getResourceAsStream("camera.GIF");
			File imgCamera = File.createTempFile("imgCamera", "GIF",new File("."));
			OutputStream outCamera = new FileOutputStream(imgCamera);
			byte bufCamera[] = new byte[1024];
			int lenCamera;
			while((lenCamera = inputStreamCamera.read(bufCamera))>0)
				outCamera.write(bufCamera,0,lenCamera);
			outCamera.close();
			inputStreamCamera.close();			
			
			parametros.put("foto_camera", imgCamera);
			
			manager = JpaUtil.getInstance();
			Query query = manager.createNativeQuery("select os.id_agendamento from os_palm os where os.idos_palm = :idos_palm");
			query.setParameter("idos_palm", BigDecimal.valueOf(Integer.parseInt(osPalmId)));
			BigDecimal idAgendameno = (BigDecimal)query.getSingleResult();
			
			conn = Connection.getConnection();
			stm = conn.prepareStatement("select distinct t.data, t.origem, t.destino, t.hora_saida, t.hora_chegada, t.horas_viagem, t.hora_inicio_servico, t.hora_termino_servico, t.horas_trabalho, t.km_inicial, t.km_final, t.km_final - t.km_inicial km_total from pmp_apropriacao_horas t"+
							" where t.id_agendamento = "+idAgendameno+" order by t.km_inicial");
			rs = stm.executeQuery();
			java.util.Collection<ApropriacaoHoras> list = new ArrayList<ApropriacaoHoras>();
			while(rs.next()){
				ApropriacaoHoras horas = new ApropriacaoHoras();
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
				
				String dateAux = rs.getString("data");
				
				if(dateAux != null){
					Date date = dateFormat.parse(dateAux);
					horas.setData(dateFormat2.format(date));					
				}				
				
				horas.setOrigem(rs.getString("origem"));
				horas.setDestino(rs.getString("destino"));
				String horas_saida = rs.getString("hora_saida");
				String [] aux = horas_saida.split(":");
				horas.setHoras_saida(aux[0]+":"+((aux[1].length() == 1)?"0"+aux[1]:aux[1]));

				String hora_chegada = rs.getString("hora_chegada");
				aux = hora_chegada.split(":");
				horas.setHora_chegada(aux[0]+":"+((aux[1].length() == 1)?"0"+aux[1]:aux[1]));

				String horas_viagem = rs.getString("horas_viagem");
				aux = horas_viagem.split(":");
				horas.setHoras_viagem(aux[0]+":"+((aux[1].length() == 1)?"0"+aux[1]:aux[1]));
				
				String hora_inicio_servico = rs.getString("hora_inicio_servico");
				aux = hora_inicio_servico.split(":");
				horas.setHora_inicio_servico(aux[0]+":"+((aux[1].length() == 1)?"0"+aux[1]:aux[1]));

				String hora_termino_servico = rs.getString("hora_termino_servico");
				aux = hora_termino_servico.split(":");
				horas.setHora_termino_servico(aux[0]+":"+((aux[1].length() == 1)?"0"+aux[1]:aux[1]));

				String horas_trabalho = rs.getString("horas_trabalho");
				aux = horas_trabalho.split(":");
				horas.setHoras_trabalho(aux[0]+":"+((aux[1].length() == 1)?"0"+aux[1]:aux[1]));
				horas.setKm_inicial(rs.getInt("km_inicial"));
				horas.setKm_final(rs.getInt("km_final"));
				horas.setKm_total(rs.getInt("km_total"));
				list.add(horas);
			}
			JRBeanCollectionDataSource horas =  new JRBeanCollectionDataSource(list);
			parametros.put("horas", horas);
			//Gera o pdf para exibicao..  
			try {  
				pdfInspecao = JasperRunManager.runReportToPdf( jasperReport, parametros, conn);  
			} catch (Exception jre) {  
				jre.printStackTrace();  
			}  

			//Parametros para nao fazer cache e o que será exibido..  
			response.setContentType( "application/pdf" );  
			response.setHeader("Content-disposition",
			"attachment; filename=inspecao.pdf" ); 

			//Envia para o navegador o pdf..  
			ServletOutputStream servletOutputStream = response.getOutputStream();  
			servletOutputStream.write( pdfInspecao );  
			servletOutputStream.flush();  
			servletOutputStream.close();
			img.delete();
			imgCamera.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
			if(conn != null){
				try {
					rs.close();
					stm.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}


}
