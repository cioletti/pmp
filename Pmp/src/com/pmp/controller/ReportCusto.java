package com.pmp.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import org.apache.catalina.Manager;

import com.pmp.bean.PrecoDePecaMoBean;
import com.pmp.util.ConectionDbs;
import com.pmp.util.IConstantAccess;
import com.pmp.util.JpaUtil;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

public class ReportCusto extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportCusto() {
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
		//	String idFilial = request.getParameter("idFilial");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		JasperReport jasperReport = null;  
		JasperPrint pdfInspecao = null;   
		EntityManager manager = null;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement prstmt_ = null;
		String osAnterior = "";
		String dataAnterior= "";
		String dateformatada= "";

		//Obtem o caminho do .jasper  
		ServletContext servletContext = super.getServletContext();  
		String caminhoJasper = servletContext.getRealPath("WEB-INF/PrecoPecaMo/precos_pecas_mo.jasper"); 
		String pathSubreport = servletContext.getRealPath("WEB-INF/PrecoPecaMo/")+"/"; 

		//Carrega o arquivo com o jasperReport  
		try {  
			jasperReport = (JasperReport) JRLoader.loadObject( caminhoJasper );  
		} catch (Exception jre) {  
			jre.printStackTrace();  
		}  
		try {
			 
			SimpleDateFormat formatDbs = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
			Date date = format.parse(beginDate);
			beginDate = format2.format(date);
			date = format.parse(endDate);
			endDate = format2.format(date);

			manager = JpaUtil.getInstance();
	
			
			String SQL = "select distinct  op.NUM_OS OS_CONTRATO,(select distinct NUM_OS from PMP_AGENDAMENTO where ID_CONT_HORAS_STANDARD = hs.id) NUM_OS, C.NUMERO_SERIE, hs.IS_EXECUTADO, c.NUMERO_CONTRATO, c.VALO_CONTRATO, c.VALOR_NOTA, hs.TIPO_PM, hs.CUSTO_PECAS, ((hs.CUSTO_MO + (hs.CUSTO  -hs.CUSTO_MO)) -hs.CUSTO_PECAS) CUSTO_MO, CONVERT(varchar(10),(select max(data_agendamento) from PMP_AGENDAMENTO where ID_CONT_HORAS_STANDARD = hs.id),103) as DATA_AGENDAMENTO, (select distinct FILIAL from PMP_AGENDAMENTO  where id = (select max(id) from PMP_AGENDAMENTO where ID_CONT_HORAS_STANDARD = hs.id))  FILIAL, c.filial FILIALVENDA, hs.HORAS_MANUTENCAO, "+
			" CONVERT(varchar(10),c.DATA_NOTA,103) as DATA_NOTA, CONVERT(varchar(10),c.DATA_ACEITE,103) as DATA_ACEITE,"+
			"" + "contrato_ativo"+" = CASE c.IS_ATIVO when 'I' then 'Inativo' ELSE 'Ativo' END, (select COUNT(*) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_TA is not null) is_ta, cp.VALOR_TA, hs.IS_TA possui_ta, CONVERT(varchar(10),hs.DATA_FATURAMENTO,103) DATA_FATURAMENTO, c.NUMERO_NOTA"+
			" ,tpc.descricao tipoContrato"+
			"  from PMP_TIPO_CONTRATO tpc, PMP_CONFIGURACAO_PRECOS cp ,PMP_CONFIG_MANUTENCAO cm, PMP_CONTRATO c, PMP_CONFIG_OPERACIONAL op, PMP_AGENDAMENTO ag right join  PMP_CONT_HORAS_STANDARD hs on hs.ID = ag.ID_CONT_HORAS_STANDARD and ag.ID_STATUS_AGENDAMENTO = (select ID from PMP_STATUS_AGENDAMENTO where SIGLA = 'FIN')"+
			
			" where CONVERT(varchar(10),c.DATA_NOTA, 112) between '"+beginDate+"' and '"+endDate+"'"+
			" and c.ID_TIPO_CONTRATO = (select id from PMP_TIPO_CONTRATO where  SIGLA = 'VPG')"+
			"  and c.ID_STATUS_CONTRATO = (select id from PMP_STATUS_CONTRATO where SIGLA = 'CA')"+
			"  and hs.ID_CONTRATO = c.ID"+
			"  and op.id_contrato = c.id"+
			" and c.ID_CONFIG_MANUTENCAO = cm.ID"+
			"  and cp.ID = cm.ID_CONFIGURACAO_PRECO"+
			" and tpc.id = c.ID_TIPO_CONTRATO"+
			" order by c.NUMERO_CONTRATO, hs.HORAS_MANUTENCAO, hs.TIPO_PM";
			
			
			String SQL2 = "select distinct  op.NUM_OS OS_CONTRATO,(select distinct NUM_OS from PMP_AGENDAMENTO where ID_CONT_HORAS_STANDARD = hs.id) NUM_OS, C.NUMERO_SERIE, hs.IS_EXECUTADO, c.NUMERO_CONTRATO, c.VALO_CONTRATO, c.VALOR_NOTA, hs.TIPO_PM, hs.CUSTO_PECAS, ((hs.CUSTO_MO + (hs.CUSTO  -hs.CUSTO_MO)) -hs.CUSTO_PECAS) CUSTO_MO, CONVERT(varchar(10),(select max(data_agendamento) from PMP_AGENDAMENTO where ID_CONT_HORAS_STANDARD = hs.id),103) as DATA_AGENDAMENTO, (select distinct FILIAL from PMP_AGENDAMENTO  where id = (select max(id) from PMP_AGENDAMENTO where ID_CONT_HORAS_STANDARD = hs.id))  FILIAL, c.filial FILIALVENDA, hs.HORAS_MANUTENCAO, "+
			" CONVERT(varchar(10),c.DATA_NOTA,103) as DATA_NOTA, CONVERT(varchar(10),c.DATA_ACEITE,103) as DATA_ACEITE,"+
			"" + "contrato_ativo"+" = CASE c.IS_ATIVO when 'I' then 'Inativo' ELSE 'Ativo' END, (select COUNT(*) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_TA is not null) is_ta, cp.VALOR_TA, hs.IS_TA possui_ta, CONVERT(varchar(10),hs.DATA_FATURAMENTO,103) DATA_FATURAMENTO, c.NUMERO_NOTA"+
			" ,tpc.descricao tipoContrato"+
			"  from PMP_TIPO_CONTRATO tpc, PMP_CONFIGURACAO_PRECOS cp ,PMP_CONFIG_MANUTENCAO cm, PMP_CONTRATO c, PMP_CONFIG_OPERACIONAL op, PMP_AGENDAMENTO ag right join  PMP_CONT_HORAS_STANDARD hs on hs.ID = ag.ID_CONT_HORAS_STANDARD and ag.ID_STATUS_AGENDAMENTO = (select ID from PMP_STATUS_AGENDAMENTO where SIGLA = 'FIN')"+
			
			" where CONVERT(varchar(10),c.DATA_ACEITE, 112) between '"+beginDate+"' and '"+endDate+"'"+
			" and c.ID_TIPO_CONTRATO in (select id from PMP_TIPO_CONTRATO where  SIGLA in ('CON', 'VEPM'))"+
			"  and c.ID_STATUS_CONTRATO = (select id from PMP_STATUS_CONTRATO where SIGLA = 'CA')"+
			"  and hs.ID_CONTRATO = c.ID"+
			"  and op.id_contrato = c.id"+
			" and c.ID_CONFIG_MANUTENCAO = cm.ID"+
			"  and cp.ID = cm.ID_CONFIGURACAO_PRECO"+
			" and tpc.id = c.ID_TIPO_CONTRATO"+
			" order by c.NUMERO_CONTRATO, hs.HORAS_MANUTENCAO, hs.TIPO_PM";
			 
					

			Query query = manager.createNativeQuery(SQL);
			List<PrecoDePecaMoBean> precoList = new ArrayList<PrecoDePecaMoBean>();
			List<Object[]> resultObj = new ArrayList<Object[]>();
			if(query.getResultList().size()>0){
				resultObj = query.getResultList();
			}
			query = manager.createNativeQuery(SQL2);
			if(query.getResultList().size()>0){
				resultObj.addAll(query.getResultList());
			}
			
			ServletOutputStream servletOutputStream = response.getOutputStream();  
			for (Object[] precoDePecaMoBean : resultObj) {
				//con = ConectionDbs.getConnecton();
				PrecoDePecaMoBean preco = new PrecoDePecaMoBean();
//				SQL = "select nf.NRONTF  from "+IConstantAccess.PESARDRTRIBUTACAO+".NF020F nf, "+IConstantAccess.LIB_DBS+".WOPHDRS0 wo"+ 
//				 "	where nf.NRODOC = wo.WONO"+
//				 "	and wo.wono = '"+(String)precoDePecaMoBean[1]+"'"+
//				 "	and nf.VLRNTF is not null";
//				prstmt_ = con.prepareStatement(SQL);
//				rs = prstmt_.executeQuery();
//				if(rs.next()){
//					String NRONTF = rs.getString("NRONTF");
//					preco.setNotaFiscal(NRONTF);
//				}
				preco.setNumOsContrato((String)precoDePecaMoBean[0]);
				preco.setNumOs((String)precoDePecaMoBean[1]);
				preco.setNumSerie((String)precoDePecaMoBean[2]);
				preco.setIsExecutado((String)precoDePecaMoBean[3]);
				preco.setNumContrato((String)precoDePecaMoBean[4]);
				preco.setValorContrato((BigDecimal)precoDePecaMoBean[5]);
				if(precoDePecaMoBean[6] != null){
					preco.setValorNota((BigDecimal)precoDePecaMoBean[6]);
				}
				preco.setTipoPm((String)precoDePecaMoBean[7]);
				preco.setCustoPecas((BigDecimal)precoDePecaMoBean[8]);
				
				preco.setCustoMo((BigDecimal)precoDePecaMoBean[9]);
				if(precoDePecaMoBean[10]!= null){
					date = format.parse((String)precoDePecaMoBean[10]);
					dateformatada = format.format(date);
					preco.setDataAgendamento(dateformatada);
				}
				if(precoDePecaMoBean[11] != null){
					preco.setFilial(((BigDecimal)precoDePecaMoBean[11]).longValue());
				}
				preco.setFilialContrato(((BigDecimal)precoDePecaMoBean[12]).longValue());
				preco.setHorasManutencao(((BigDecimal)precoDePecaMoBean[13]).longValue());
				preco.setDataNota((String)precoDePecaMoBean[14]);
				preco.setDataAceite((String)precoDePecaMoBean[15]);

//				if(preco.getNumOsContrato().equals(osAnterior)){
//					preco.setDataFaturamento(dataAnterior);
//				}else{
//					osAnterior = preco.getNumOsContrato();
//					SQL= "select s.IVDT8 from "+IConstantAccess.LIB_DBS+".WOPHDRS0 s where s.wono = '"+preco.getNumOs()+"'";
//					prstmt_ = con.prepareStatement(SQL);
//					rs = prstmt_.executeQuery();
//					if(rs.next()){
//						if(!rs.getString("IVDT8").equals("00000000")){
//							date = formatDbs.parse(rs.getString("IVDT8"));
//							dateformatada = format.format(date);
//							preco.setDataFaturamento(dateformatada);
//							dataAnterior = dateformatada;
//						}else{
//							preco.setDataFaturamento("");
//							dataAnterior = "";
//						}
////					}
////
//				}
				preco.setContratoAtivo((String)precoDePecaMoBean[16]);
				if(((Integer)precoDePecaMoBean[17]).longValue() > 0){
					if(precoDePecaMoBean[19] != null){
						preco.setCustoMo((BigDecimal)precoDePecaMoBean[18]);
					}else{
						preco.setCustoMo(null);
					}
				}
				preco.setDataFaturamento((String)precoDePecaMoBean[20]);
				preco.setNotaFiscal((String)precoDePecaMoBean[21]);
				preco.setTipoContrato((String)precoDePecaMoBean[22]);
				//System.out.println(precoDePecaMoBean[22]);
				precoList.add(preco);
				//Para a conexão com o servlet não cair
				response.setContentType( "application/vnd.ms-excel" );
				servletOutputStream.print("");  
				servletOutputStream.flush();  
	            //System.out.println( "== ");  
				//con.close();
			}

			Map parametros = new HashMap();

			parametros.put("SUBREPORT_DIR", pathSubreport); 

			JRBeanCollectionDataSource result =  new JRBeanCollectionDataSource(precoList);

			byte buf[]=new byte[1024];
			int len;

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


				exporter.exportReport();     
				bytes = xlsReport.toByteArray();     
			} catch (Exception jre) {  
				jre.printStackTrace();  
			}  

			//Parametros para nao fazer cache e o que será exibido..  
			response.setContentType( "application/vnd.ms-excel" );  
			//			response.setHeader("Content-disposition",
			//			"attachment; filename=transferencia" ); 
			response.setHeader("content-disposition", "inline; filename=PrecoPecasMo.xls");   

			//Envia para o navegador o pdf..  
			
			servletOutputStream.write( bytes );  
			servletOutputStream.flush();  
			servletOutputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				if(con != null){
					//prstmt_.close();
					//rs.close();
					if(!con.isClosed()){
						con.close();
					}
				}
				if(manager != null && manager.isOpen()){
					manager.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	


//	public static void main(String[] args) {
//		//COM.ibm.db2.app.Blob 
//		try {
//			Connection con = ConectionDbs.getConnecton();
//			Statement st = con.createStatement();
//			ResultSet viewTabela = st.executeQuery("select m.transp from PESA200ARQ.minuta01 m where m.NROMINUTA = 336399");
//
//			viewTabela.next();
//			byte[] id = viewTabela.getBytes(1);
//			
//			System.out.println(viewTabela.getString(1));
//			viewTabela.getBinaryStream(1);
//			viewTabela.getAsciiStream(1);
//			System.out.println(viewTabela.getCharacterStream(1));
//			
//			System.out.println((int)'A');
//			for(byte i : id){
//			System.out.println((char)i);
//			}
//		
//			
//			BufferedReader reader = new BufferedReader( new InputStreamReader(viewTabela.getBinaryStream(1)));
//			String line = "";
//			while ((line = reader.readLine()) != null) {
//				System.out.println(line);
//			}
//
//
//      
//            
//			//			Clob cpClob=viewTabela.getClob("transp"); 
//			
//			
////		    char[] buffer = new char[1024];  
////		    Reader instream = cpClob.getCharacterStream();  
////		    StringBuffer sb = new StringBuffer();  
////		    int length;  
////		    while ( ( length = instream.read( buffer ) ) != -1 ) {  
////		       sb.append( buffer, 0, length );  
////		    }  
////		    instream.close();  
////		    String strFinal =  sb.toString();  
//			
////			BufferedReader in = new BufferedReader(new InputStreamReader(blob.getBinaryStream()));            
////			String buf = in.readLine();           
////			System.out.println("-->"+buf);
////			//Blob blob = rs.getBlob(cloumnName[i]);
////	
//			//System.out.println(Integer.toBinaryString());
//			//System.out.println(s);
//			//System.out.println(s);
//			//System.out.println(s);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public static void main(String[] args) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
		
		Date date;
		try {
			date = format2.parse("20131102");
			String dateformatada = format.format(date);
			System.out.println(dateformatada);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	}


