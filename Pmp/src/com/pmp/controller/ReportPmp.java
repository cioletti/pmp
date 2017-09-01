package com.pmp.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import com.pmp.bean.EquipamentosPmpBean;
import com.pmp.bean.FilialBean;
import com.pmp.bean.OperacionalBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.business.ContratoBusiness;
import com.pmp.business.OperacionalBusiness;
import com.pmp.business.OsBusiness;
import com.pmp.util.Connection;
import com.pmp.util.JpaUtil;

/**
 * Servlet implementation class ReportPmp
 */
public class ReportPmp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReportPmp() {
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
		String tipoRelatorio = request.getParameter("tipoRelatorio");

		if (tipoRelatorio.equals("GE")) {
			getGestaoEquipamentos(request, response);
		} else if (tipoRelatorio.equals("TP")){
			getTabelaPreco(request, response);
		} else if (tipoRelatorio.equals("TR")) {
			getTotalRevisao(request, response);
		} else if (tipoRelatorio.equals("IR")) {
			getTotalContratosAtivos(request, response);
		} else if (tipoRelatorio.equals("TA")) {
			getTotalAgendamentos(request, response);
		}else if (tipoRelatorio.equals("TC")) {
			getTotalComissao(request, response);
		}
	}

	private void getGestaoEquipamentos(HttpServletRequest request,
			HttpServletResponse response) {
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		
		
		EntityManager manager = null;
		JasperReport jasperReport = null;  
		JasperPrint pdfInspecao = null;    

		//Obtem o caminho do .jasper  
		ServletContext servletContext = super.getServletContext();  
		String caminhoJasper = servletContext.getRealPath("WEB-INF/relatorios/equipamentosPmp.jasper"); 
		String pathSubreport = servletContext.getRealPath("WEB-INF/relatorios/")+"/"; 

		//Carrega o arquivo com o jasperReport  
		try {  
			jasperReport = (JasperReport) JRLoader.loadObject( caminhoJasper );  
		} catch (Exception jre) {  
			jre.printStackTrace();  
		}  

		try {
			SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
			Date date = format.parse(beginDate);
			beginDate = format2.format(date);
			date = format.parse(endDate);
			endDate = format2.format(date);


			Map parametros = new HashMap();  
			parametros.put("SUBREPORT_DIR", pathSubreport);  
			List<EquipamentosPmpBean> euipList = new ArrayList<EquipamentosPmpBean>();
			manager = JpaUtil.getInstance();

			String SQL = "select c.modelo, c.numero_serie, CAST( DATEPART(DD,c.data_aceite ) AS VARCHAR ) dia, CAST( DATEPART(MM,c.data_aceite )AS VARCHAR) mes, CAST( DATEPART(yy,c.data_aceite )AS VARCHAR) ano, c.numero_contrato numeroContrato, c.codigo_cliente codigoCliente, c.razao_social razaoSocial, "+
					  " (select case when (count(*) = 0)"+
					" then 'NÃO'"+
					" else 'SIM'"+
					" end from pmp_maquina_pl pl"+
					" where pl.latitude is not null"+
					" and pl.longitude is not null" +
					" and pl.NUMERO_SERIE = c.numero_serie) pl,"+  
					"  CAST(DATEPART(MM,DATEADD(MM,(select count (hs1.id) from pmp_cont_horas_standard hs1 where hs1.id_contrato = c.id), c.data_aceite )) AS VARCHAR) mesFim,  CAST(DATEPART(YY,DATEADD(MM,(select count (hs1.id) from pmp_cont_horas_standard hs1 where hs1.id_contrato = c.id), c.data_aceite)) AS VARCHAR) anoFim,"+
					"  (select num_os from pmp_config_operacional where id_contrato = c.id) numOs, c.qtd_parcelas parcelas, c.valo_contrato valor, c.data_aceite, c.filial,f.eplsnm,   "+
					"   (select case when (c.ta = 'S')"+
					"   then 'SIM' ELSE 'NÃO'"+
					"   end) ta,"+
					"   (select t.descricao from pmp_tipo_contrato t where t.id = c.id_tipo_contrato) tipoContrato,"+   
					"   (select f.stnm from tw_filial f where f.stno = c.filial) origem,   "+
					"   (select f.stnm from tw_filial f where f.stno = (select co.filial from pmp_config_operacional co where id_contrato = c.id)) destino,"+   
					"   (select count(*) from pmp_cont_horas_standard hs where hs.id_contrato = c.id and hs.is_executado = 'N') revPendentes,   "+
					"   (select max(mp.horimetro) from pmp_maquina_pl mp where mp.id = (select max(pm.id) from PMP_MAQUINA_PL pm where pm.NUMERO_SERIE = c.NUMERO_SERIE and pm.HORIMETRO is not null)) horimetro,  "+
					"   CONVERT(varchar(8),(select max(mp.data_atualizacao) from pmp_maquina_pl mp where mp.id = (select max(pm.id) from PMP_MAQUINA_PL pm where pm.NUMERO_SERIE = c.NUMERO_SERIE and pm.HORIMETRO is not null)), 112) at_horimetro," +
					" cc.DESCRICAO  tipo_contrato, c.id id_contrato, c.possui_horas_mes, C.MEDIA_HORAS_MES," +
					" ap.horimetro_ultima_revisao, ap.horimetro_proxima_revisao, ap.horimetro_faltante "+            
					"   from tw_funcionario f, PMP_CLASSIFICACAO_CONTRATO cc, pmp_contrato c left join pmp_agendamento_pendente ap on ap.serie = c.numero_serie "+
					"   where c.id in (  "+
					"   select distinct hs.id_contrato from pmp_cont_horas_standard hs"+  
					"   where c.id = hs.id_contrato  "+
					"   and hs.is_executado = 'N'  "+
					"   )  "+
					"   and c.id_status_contrato = (select s.id from pmp_status_contrato s where s.sigla = 'CA')"+   
					"   and CONVERT(varchar(8),c.data_aceite, 112) between "+beginDate+" and  "+endDate+""+
					"   and c.id_funcionario = f.epidno   " +
					"   and c.IS_ATIVO is null " +
					"   and c.ID_CLASSIFICACAO_CONTRATO = cc.ID" +
					"   order by c.data_aceite ";
			
			Query query = manager.createNativeQuery(SQL);
			List<Object[]> list = query.getResultList();
			UsuarioBean usuarioBean = (UsuarioBean) request.getSession().getAttribute(
			"usuario");
			OperacionalBusiness business = new OperacionalBusiness(usuarioBean);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			for (Object[] objects : list) {
				
				EquipamentosPmpBean bean = new EquipamentosPmpBean();
				bean.setMODELO((String)objects[0]);
				bean.setNUMERO_SERIE((String)objects[1]);
				bean.setDIA((String)objects[2]);
				bean.setMES((String)objects[3]);
				bean.setANO((String)objects[4]);
				bean.setNUMEROCONTRATO((String)objects[5]);
				bean.setCODIGOCLIENTE((String)objects[6]);
				bean.setRAZAOSOCIAL((String)objects[7]);
				bean.setPL((String)objects[8]);
				bean.setMESFIM((String)objects[9]);
				bean.setANOFIM((String)objects[10]);
				bean.setNUMOS((String)objects[11]);
				bean.setPARCELAS((BigDecimal)objects[12]);
				bean.setVALOR((BigDecimal)objects[13]);
				Date date2 = format3.parse((String)objects[14]);
				bean.setDATAACEITE(date2);
				bean.setFILIAL((BigDecimal)objects[15]);
				bean.setCONSULTOR((String)objects[16]);
				bean.setTA((String)objects[17]);
				bean.setTIPOCONTRATO((String)objects[18]);
				bean.setORIGEM((String)objects[19]);
				bean.setDESTINO((String)objects[20]);
				BigDecimal revPendentes = new BigDecimal((Integer)objects[21]);
				bean.setREVPENDENTES(revPendentes);
				bean.setHORIMETRO((BigDecimal)objects[22]);
				if((String)objects[23] != null){
					date2 = format2.parse((String)objects[23]);
					bean.setAT_HORIMETRO(date2);
				}
				bean.setTIPO_CONTRATO((String)objects[24]);
				bean.setHORIMETRO_ULTIMA_REVISAO((String)objects[28]);
				bean.setHORIMETRO_PROXIMA_REVISAO((String)objects[29]);
				bean.setHORIMETRO_FALTANTE((String)objects[30]);
				
				//Horas p/ revisões		
				String sqlMaiores = "select horas_Manutencao from Pmp_Cont_Horas_Standard where id_Contrato = :contratoId and is_Executado = 'N'order by horas_Manutencao " ;					  
				Query queryHorasMaiores = manager.createNativeQuery(sqlMaiores);
				queryHorasMaiores.setParameter("contratoId", ((BigDecimal)objects[25]).longValue());
				Long proximaManutencao = 0l;
				if(queryHorasMaiores.getResultList().size() > 0){
					proximaManutencao = ((BigDecimal)queryHorasMaiores.getResultList().get(0)).longValue();
					bean.setPROXIMA_REVISAO(proximaManutencao.toString());
				}
				
//				if(objects[26] != null && ((String)objects[26]).equals("S")){
//					if(queryHorasMaiores.getResultList().size() > 0){
//						proximaManutencao = ((BigDecimal)queryHorasMaiores.getResultList().get(0)).longValue();
//						Double mediaDiasProximaRevisao = ((proximaManutencao.doubleValue() - bean.getHORIMETRO().doubleValue())/((BigDecimal)objects[27]).doubleValue())*30;//média de dias da próxima revisão
//
//						query = manager.createNativeQuery(" select DATEDIFF ( day , max(palm.EMISSAO), GETDATE())   from Pmp_Agendamento ag, os_palm palm where  ag.id_cont_horas_standard in (select st.id from pmp_cont_horas_standard st where id_Contrato = "+((BigDecimal)objects[25]).longValue()+") and palm.ID_AGENDAMENTO = ag.ID");
//						if(query.getResultList().size() > 0 && query.getResultList().get(0) != null){
//							Integer dias = (Integer)query.getSingleResult();
//							mediaDiasProximaRevisao = mediaDiasProximaRevisao - dias;
//						}else{
//							query = manager.createNativeQuery(" select DATEDIFF ( day , max(c.data_aceite), GETDATE())   from Pmp_contrato  c where  c.id = "+((BigDecimal)objects[25]).longValue());
//							Integer dias = (Integer)query.getSingleResult();
//							mediaDiasProximaRevisao = mediaDiasProximaRevisao -dias;
//						}
//
//						Long proximaRevisao = Math.round(mediaDiasProximaRevisao);
//					}
//				}
				bean.setDIAS_PENDENTES(business.findDiasProxRevisao(bean.getNUMERO_SERIE(), manager));
				if(bean.getHORIMETRO() != null){
					Long horasPendentes = proximaManutencao - bean.getHORIMETRO().longValue();
					bean.setHORIMETRO_PENDENTE(horasPendentes.toString());
				}else{
					bean.setHORIMETRO_PENDENTE("0");
				}
				
				euipList.add(bean);
				response.setContentType( "application/vnd.ms-excel" );
				servletOutputStream.print("");  
				servletOutputStream.flush(); 
			}
			
			JRBeanCollectionDataSource result =  new JRBeanCollectionDataSource(euipList);
			//Gera o excel para exibicao..
			byte[] bytes = null;
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
			response.setHeader("content-disposition", "inline; filename=gestaEquipamentos.xls");   

			//Envia para o navegador o pdf..  
			  
			servletOutputStream.write( bytes );  
			servletOutputStream.flush();  
			servletOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
	}
	private void getTabelaPreco(HttpServletRequest request,
			HttpServletResponse response) {

		JasperReport jasperReport = null;  
		JasperPrint pdfInspecao = null;    

		//Obtem o caminho do .jasper  
		ServletContext servletContext = super.getServletContext();  
		String caminhoJasper = servletContext.getRealPath("WEB-INF/relatorios/tabelaPreco.jasper"); 
		String pathSubreport = servletContext.getRealPath("WEB-INF/relatorios/")+"/"; 

		//Carrega o arquivo com o jasperReport  
		try {  
			jasperReport = (JasperReport) JRLoader.loadObject( caminhoJasper );  
		} catch (Exception jre) {  
			jre.printStackTrace();  
		}  

		try {

			Map parametros = new HashMap();  

			parametros.put("SUBREPORT_DIR", pathSubreport);  

			ContratoBusiness business = new ContratoBusiness((UsuarioBean)request.getSession().getAttribute("usuario"));

			JRBeanCollectionDataSource result =  new JRBeanCollectionDataSource(business.findTabelaPreco());
			//Gera o excel para exibicao..
			byte[] bytes = null;
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
			response.setHeader("content-disposition", "inline; filename=tabelaPrecos.xls");   

			//Envia para o navegador o pdf..  
			ServletOutputStream servletOutputStream = response.getOutputStream();  
			servletOutputStream.write( bytes );  
			servletOutputStream.flush();  
			servletOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getTotalRevisao(HttpServletRequest request, HttpServletResponse response) {
		
		String idFilial = request.getParameter("idFilial");
		JasperReport jasperReport = null;  
		JasperPrint pdfInspecao = null;    
		
		//Obtem o caminho do .jasper  
		ServletContext servletContext = super.getServletContext();  
		String caminhoJasper = servletContext.getRealPath("WEB-INF/relatorios/totalRevisao.jasper"); 
		String pathSubreport = servletContext.getRealPath("WEB-INF/relatorios/")+"/"; 
		
		//Carrega o arquivo com o jasperReport  
		try {  
			jasperReport = (JasperReport) JRLoader.loadObject( caminhoJasper );  
		} catch (Exception jre) {  
			jre.printStackTrace();  
		}  
		java.sql.Connection conn = null;

		try {
			conn = Connection.getConnection();
			Map parametros = new HashMap();
			
			OsBusiness business = new OsBusiness((UsuarioBean)request.getSession().getAttribute("usuario"));
			
			List<BigDecimal> listFilial = new ArrayList<BigDecimal>();
			
			if(!idFilial.equals("-1")) { //Se foi selecionada uma filial específica
				listFilial.add(BigDecimal.valueOf(Long.valueOf(idFilial)));
			} else { //Se foi selecionadas todas filiais
				for (FilialBean filial : business.findAllFiliais()) {
					listFilial.add(BigDecimal.valueOf(filial.getStno())); //Adiciona à lista o ID da filial
				}
			}

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
	private void getTotalContratosAtivos(HttpServletRequest request, HttpServletResponse response) {
		
		String idFilial = request.getParameter("idFilial");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		JasperReport jasperReport = null;  
		JasperPrint pdfInspecao = null;    
		
		//Obtem o caminho do .jasper  
		ServletContext servletContext = super.getServletContext();  
		String caminhoJasper = servletContext.getRealPath("WEB-INF/relatorios/totalIndiceRenovacao.jasper"); 
		String pathSubreport = servletContext.getRealPath("WEB-INF/relatorios/")+"/"; 
		
		//Carrega o arquivo com o jasperReport  
		try {  
			jasperReport = (JasperReport) JRLoader.loadObject( caminhoJasper );  
		} catch (Exception jre) {  
			jre.printStackTrace();  
		}  
		java.sql.Connection conn = null;
		
		try {
			conn = Connection.getConnection();
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
			Date date = format.parse(beginDate);
			beginDate = format2.format(date);
			date = format.parse(endDate);
			endDate = format2.format(date);

			
			Map parametros = new HashMap();
			
			OsBusiness business = new OsBusiness((UsuarioBean)request.getSession().getAttribute("usuario"));
			
			List<BigDecimal> listFilial = new ArrayList<BigDecimal>();
			
			if(!idFilial.equals("-1")) { //Se foi selecionada uma filial específica
				listFilial.add(BigDecimal.valueOf(Long.valueOf(idFilial)));
			} else { //Se foi selecionadas todas filiais
				for (FilialBean filial : business.findAllFiliais()) {
					listFilial.add(BigDecimal.valueOf(filial.getStno())); //Adiciona à lista o ID da filial
				}
			}
			
			parametros.put("SUBREPORT_DIR", pathSubreport);
			parametros.put("FILIAL", listFilial);
			parametros.put("DATA_INICIO", beginDate);
			parametros.put("DATA_FIM", endDate);
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
			response.setHeader("content-disposition", "inline; filename=totalContratosAtivos.xls");   
			
			//Envia para o navegador o pdf..  
			ServletOutputStream servletOutputStream = response.getOutputStream();  
			servletOutputStream.write( bytes );  
			servletOutputStream.flush();  
			servletOutputStream.close();
			img.delete();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	private void getTotalComissao(HttpServletRequest request, HttpServletResponse response) {
		
		String idFilial = request.getParameter("idFilial");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		JasperReport jasperReport = null;  
		JasperPrint pdfInspecao = null;    
		
		//Obtem o caminho do .jasper  
		ServletContext servletContext = super.getServletContext();  
		String caminhoJasper = servletContext.getRealPath("WEB-INF/relatorios/tabelaComissao.jasper"); 
		String pathSubreport = servletContext.getRealPath("WEB-INF/relatorios/")+"/"; 
		
		//Carrega o arquivo com o jasperReport  
		try {  
			jasperReport = (JasperReport) JRLoader.loadObject( caminhoJasper );  
		} catch (Exception jre) {  
			jre.printStackTrace();  
		}
		java.sql.Connection conn = null;
		
			
		try {
			conn = Connection.getConnection();
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
			Date date = format.parse(beginDate);
			beginDate = format2.format(date);
			date = format.parse(endDate);
			endDate = format2.format(date);
			
			
			Map parametros = new HashMap();
			
			OsBusiness business = new OsBusiness((UsuarioBean)request.getSession().getAttribute("usuario"));
			
			List<BigDecimal> listFilial = new ArrayList<BigDecimal>();
			
			if(!idFilial.equals("-1")) { //Se foi selecionada uma filial específica
				listFilial.add(BigDecimal.valueOf(Long.valueOf(idFilial)));
			} else { //Se foi selecionadas todas filiais
				for (FilialBean filial : business.findAllFiliais()) {
					listFilial.add(BigDecimal.valueOf(filial.getStno())); //Adiciona à lista o ID da filial
				}
			}
			
			parametros.put("SUBREPORT_DIR", pathSubreport);
			parametros.put("FILIAL", listFilial);
			parametros.put("DATA_INICIO", beginDate);
			parametros.put("DATA_FIM", endDate);
			
			
			byte buf[]=new byte[1024];
			int len;
			
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
			response.setHeader("content-disposition", "inline; filename=totalComissao.xls");   
			
			//Envia para o navegador o pdf..  
			ServletOutputStream servletOutputStream = response.getOutputStream();  
			servletOutputStream.write( bytes );  
			servletOutputStream.flush();  
			servletOutputStream.close();
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
	private void getTotalAgendamentos(HttpServletRequest request, HttpServletResponse response) {
		
		String idFilial = request.getParameter("idFilial");

		JasperReport jasperReport = null;  
		JasperPrint pdfInspecao = null;    
		
		//Obtem o caminho do .jasper  
		ServletContext servletContext = super.getServletContext();  
		String caminhoJasper = servletContext.getRealPath("WEB-INF/relatorios/totalIndiceRenovacao.jasper"); 
		String pathSubreport = servletContext.getRealPath("WEB-INF/relatorios/")+"/"; 
		
		//Carrega o arquivo com o jasperReport  
		try {  
			jasperReport = (JasperReport) JRLoader.loadObject( caminhoJasper );  
		} catch (Exception jre) {  
			jre.printStackTrace();  
		}  
		java.sql.Connection conn = null;
		
		try {
			conn = Connection.getConnection();
			Map parametros = new HashMap();
			
			OsBusiness business = new OsBusiness((UsuarioBean)request.getSession().getAttribute("usuario"));
			
			List<BigDecimal> listFilial = new ArrayList<BigDecimal>();
			
			if(!idFilial.equals("-1")) { //Se foi selecionada uma filial específica
				listFilial.add(BigDecimal.valueOf(Long.valueOf(idFilial)));
			} else { //Se foi selecionadas todas filiais
				for (FilialBean filial : business.findAllFiliais()) {
					listFilial.add(BigDecimal.valueOf(filial.getStno())); //Adiciona à lista o ID da filial
				}
			}
			
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
