package com.pmp.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import com.pmp.bean.ApropriacaoHoras;
import com.pmp.bean.IntervencaoBean;
import com.pmp.bean.PecaBean;
import com.pmp.bean.PecasDbsBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.entity.PmpAgendamento;
import com.pmp.entity.PmpContHorasStandard;
import com.pmp.entity.PmpContrato;
import com.pmp.entity.PmpDescontoPdp;
import com.pmp.entity.TwFilial;
import com.pmp.entity.TwFuncionario;
import com.pmp.util.ConectionDbs;
import com.pmp.util.EmailHelper;
import com.pmp.util.IConstantAccess;
import com.pmp.util.JpaUtil;
import com.pmp.util.ValorMonetarioHelper;

/**
 * Servlet implementation class PropostaPdf
 */
public class PropostaPdf extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PropostaPdf() {
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
		String numeroOs = request.getParameter("numeroOs");
		numeroOs = numeroOs.toUpperCase();

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		java.sql.PreparedStatement stm = null;
		EntityManager manager = null;
		try {
			JasperReport jasperReport = null;  
			JasperPrint pdfProposta = null;    
			manager = JpaUtil.getInstance();
			String SQL = "select c.ID_CONTRATO from PMP_OS_OPERACIONAL os, PMP_CONFIG_OPERACIONAL c"+
			" where os.ID_CONFIG_OPERACIONAL = c.ID"+
			" and os.NUM_OS = '"+numeroOs+"'";
			Query query  = manager.createNativeQuery(SQL);
			Long idContrato = ((BigDecimal)query.getSingleResult()).longValue();
			//Long idContrato = id.longValue();
			PmpContrato contrato =  manager.find(PmpContrato.class, Long.valueOf(idContrato));
			//Obtem o caminho do .jasper  
			ServletContext servletContext = super.getServletContext(); 
			String caminhoJasper = "";
			String pathSubreport = "";
			//UsuarioBean bean = (UsuarioBean) request.getSession().getAttribute("usuario");


			caminhoJasper = servletContext.getRealPath("WEB-INF/PropostaAgendamento/PropostaPmp.jasper"); 
			pathSubreport = servletContext.getRealPath("WEB-INF/PropostaAgendamento/")+"/";
			query = manager.createQuery("From PmpAgendamento c where c.numOs=:numOs");
			query.setParameter("numOs", numeroOs);
			List<PmpAgendamento> agendamentoList = (List<PmpAgendamento>)query.getResultList();
			conn = com.pmp.util.Connection.getConnection();
			java.util.Collection<ApropriacaoHoras> list = new ArrayList<ApropriacaoHoras>();
			for (PmpAgendamento agendamento : agendamentoList) {
				stm = conn.prepareStatement("select distinct t.data, t.origem, t.destino, t.hora_saida, t.hora_chegada, t.horas_viagem, t.hora_inicio_servico, t.hora_termino_servico, t.horas_trabalho, t.km_inicial, t.km_final, t.km_final - t.km_inicial km_total from pmp_apropriacao_horas t"+
						" where t.id_agendamento = "+agendamento.getId()+" order by t.km_inicial");
				rs = stm.executeQuery();
				while(rs.next()){
					ApropriacaoHoras horas = new ApropriacaoHoras();

					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

					String dateAux = rs.getString("data");

					if(dateAux != null){
						Date date;
						try {
							date = dateFormat.parse(dateAux);
							horas.setData(dateFormat2.format(date));					
						} catch (ParseException e) {
							e.printStackTrace();
						}
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
			}
			JRBeanCollectionDataSource horas =  new JRBeanCollectionDataSource(list);

			try {  
				jasperReport = (JasperReport) JRLoader.loadObject( caminhoJasper );  
			} catch (Exception jre) {  
				jre.printStackTrace();  
			} finally{
				if(conn != null){
					stm.close();
					conn.close();
				}
			}

			//Double custoManutencao = 0d;
			//BigDecimal custoPecas = BigDecimal.ZERO;
			//TwFilial twFilial = new TwFilial();

			BigDecimal custoPecas = BigDecimal.ZERO;
			BigDecimal custoMo = BigDecimal.ZERO;

			query = manager.createNativeQuery("select horimetro From Os_Palm c where c.NUMERO_OS=:numOs");
			query.setParameter("numOs", numeroOs);
			BigDecimal horimetro = null;
			if(query.getResultList().size() > 0){
				horimetro = (BigDecimal)query.getSingleResult();
			}

			query = manager.createQuery("From PmpContHorasStandard c, PmpOsOperacional o where c.id = o.idContHorasStandard.id and o.numOs=:numOs");
			query.setParameter("numOs", numeroOs);
			Object[] pair = (Object[])query.getSingleResult();
			List<IntervencaoBean> intervencaoList = new ArrayList<IntervencaoBean>();

			PmpContHorasStandard pmpContHorasStandard = (PmpContHorasStandard) pair[0];
			IntervencaoBean intervencaoBean = new IntervencaoBean();
			intervencaoBean.setHorimetro(pmpContHorasStandard.getHorasManutencao().intValue());
			intervencaoBean.setIntervancaoRealizada(pmpContHorasStandard.getHorasRevisao().intValue());
			intervencaoBean.setIsTa(pmpContHorasStandard.getIsTa());
			if(pmpContHorasStandard.getCusto() != null){
				intervencaoBean.setCusto(ValorMonetarioHelper.formata("###,###.00", pmpContHorasStandard.getCusto().doubleValue()));
			}

			query = manager.createQuery("from PmpDescontoPdp");
			List<PmpDescontoPdp> pdpList = query.getResultList();
			String siglaDescontoPdp = "";
			for (PmpDescontoPdp pmpDescontoPdp : pdpList) {
				siglaDescontoPdp += "'"+pmpDescontoPdp.getDescricao()+"',";
			}
			if(siglaDescontoPdp.length() > 0){
				siglaDescontoPdp = siglaDescontoPdp.substring(0, siglaDescontoPdp.length() -1);
			}
			BigDecimal descontoPecas = pmpContHorasStandard.getIdContrato().getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas();

			SQL = "select s.LBAMT mo, s.WIPPAS pecas from "+IConstantAccess.LIB_DBS+".WOPHDRS0 w, "+IConstantAccess.LIB_DBS+".WOPSEGS0 s "+
			" where w.wono = '"+numeroOs+"'"+
			" and s.WONO = w.WONO";
			conn = ConectionDbs.getConnecton();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL);

			intervencaoList.add(intervencaoBean);
			if(rs.next()){
				custoPecas = rs.getBigDecimal(2);
				custoMo = rs.getBigDecimal(1);						
			}
			List<PecaBean> pecas = new ArrayList<PecaBean>();
			if(Long.valueOf(contrato.getCodigoCliente()) == 1008081 
					|| Long.valueOf(contrato.getCodigoCliente()) == 16854
					|| Long.valueOf(contrato.getCodigoCliente()) == 21835
					|| Long.valueOf(contrato.getCodigoCliente()) == 578053
					|| Long.valueOf(contrato.getCodigoCliente()) == 57216
					|| Long.valueOf(contrato.getCodigoCliente()) == 1008080
					|| Long.valueOf(contrato.getCodigoCliente()) == 1008079
					|| Long.valueOf(contrato.getCodigoCliente()) == 668516
					|| Long.valueOf(contrato.getCodigoCliente()) == 16853
					|| Long.valueOf(contrato.getCodigoCliente()) == 20685
					|| Long.valueOf(contrato.getCodigoCliente()) == 1013931
					|| Long.valueOf(contrato.getCodigoCliente()) == 1019028
					|| Long.valueOf(contrato.getCodigoCliente()) == 403199
					|| Long.valueOf(contrato.getCodigoCliente()) == 884531){
				intervencaoBean.setCusto(ValorMonetarioHelper.formata("###,###.00", (custoPecas.add(custoMo)).doubleValue()));


				SQL= "SELECT   a.toivqy, a.pano20, a.ds18, a.unsel, a.BECTYC"+
				" FROM     "+IConstantAccess.LIB_DBS+".woppart0 a"+
				" WHERE    wono= '"+numeroOs+"'";
				rs = stmt.executeQuery(SQL);
				while(rs.next()){
					PecaBean pecaBean = new PecaBean();
					pecaBean.setOrqy(rs.getInt(1));
					pecaBean.setPano20(rs.getString(2));
					pecaBean.setDs18(rs.getString(3));
					pecaBean.setPreco(rs.getBigDecimal(4));
					pecas.add(pecaBean);
				}

			}else{
				custoPecas = BigDecimal.ZERO;
				//Peças elegíveis ao desconto PDP
				SQL= "SELECT   a.orqy, a.pano20, a.ds18, a.unsel, a.BECTYC"+
				" FROM     "+IConstantAccess.LIB_DBS+".woppart0 a"+
				" WHERE    wono= '"+numeroOs+"'" +
				" and a.BECTYC in ("+siglaDescontoPdp+")";
				rs = stmt.executeQuery(SQL);
				while(rs.next()){
					PecaBean pecaBean = new PecaBean();
					pecaBean.setOrqy(rs.getInt(1));
					pecaBean.setPano20(rs.getString(2));
					pecaBean.setDs18(rs.getString(3));
					pecaBean.setPreco(rs.getBigDecimal(4));
					pecas.add(pecaBean);
					custoPecas = custoPecas.add(pecaBean.getPreco().multiply(BigDecimal.valueOf(rs.getInt(1))));
				}
				//Pecas não elegíveis a PDP
				SQL= "SELECT   a.orqy, a.pano20, a.ds18, a.unsel, a.BECTYC"+
				" FROM     "+IConstantAccess.LIB_DBS+".woppart0 a"+
				" WHERE    wono= '"+numeroOs+"'" +
				" and a.BECTYC not in ("+siglaDescontoPdp+")";
				rs = stmt.executeQuery(SQL);
				while(rs.next()){
					PecaBean pecaBean = new PecaBean();
					pecaBean.setOrqy(rs.getInt(1));
					pecaBean.setPano20(rs.getString(2));
					pecaBean.setDs18(rs.getString(3));
					pecaBean.setPreco(rs.getBigDecimal(4));
					pecas.add(pecaBean);
					if(descontoPecas.equals(0.00)){
						custoPecas = custoPecas.add(pecaBean.getPreco());
					}else{
						BigDecimal valorPeca = pecaBean.getPreco();
						pecaBean.setPreco(valorPeca.subtract(valorPeca.multiply(descontoPecas.divide(BigDecimal.valueOf(100.0)))));
						custoPecas = custoPecas.add( pecaBean.getPreco().multiply(BigDecimal.valueOf(rs.getInt(1))) );
					}
				}
				intervencaoBean.setCusto(ValorMonetarioHelper.formata("###,###.00", (custoPecas.add(custoMo)).doubleValue()));
			}
			JRBeanCollectionDataSource intervencaoListJRBaen = new JRBeanCollectionDataSource(intervencaoList);
			JRBeanCollectionDataSource pecasListJRBaen = new JRBeanCollectionDataSource(pecas);



			Map parametros = new HashMap();
			TwFilial filial = manager.find(TwFilial.class, contrato.getFilial().longValue());
			parametros.put("FORNECEDOR", filial.getRazaoSocial()); 
			parametros.put("ENDERECO_FILIAL", filial.getEndereco()); 
			parametros.put("CEP_FILIAL", filial.getCep()); 
			parametros.put("CNPJ_FILIAL", filial.getCnpj());
			
			parametros.put("horas", horas);
			parametros.put("SUBREPORT_DIR", pathSubreport); 
			parametros.put("NUMERO_CONTRATO", contrato.getNumeroContrato());  
			parametros.put("INTERVENCAO_LIST", intervencaoListJRBaen);
			parametros.put("PECAS_LIST", pecasListJRBaen);
			parametros.put("FABRICANTE", contrato.getFabricante());
			parametros.put("RAZAO_SOCIAL", contrato.getRazaoSocial()+" / "+contrato.getCodigoCliente());  
			parametros.put("ENDERECO", contrato.getEndereco()+" / "+contrato.getBairro());  
			parametros.put("CIDADE", contrato.getCidade()+" / "+contrato.getUf());  
			parametros.put("CEP", contrato.getCep());  
			parametros.put("CNPJ", contrato.getCnpj());  
			parametros.put("CPF", contrato.getCpf());  
			parametros.put("CONTATO_SERVICOS", contrato.getContatoServicos());  
			parametros.put("EMAIL_CONTATO_SERVICOS", contrato.getEmailContatoServicos());  
			parametros.put("TELEFONE_SERVICOS", contrato.getTelefoneServicos());  
			parametros.put("MODELO", contrato.getModelo());  
			parametros.put("FAMILIA", contrato.getFamilia());  
			parametros.put("MUM_SERIE", contrato.getNumeroSerie()); 
			if(horimetro != null){
				parametros.put("HORIMETRO", horimetro.toString());  
			}
			if(contrato.getValoContrato() != null){
				parametros.put("PRECO", "R$ "+ValorMonetarioHelper.formata("###,###.00", contrato.getValoContrato().doubleValue()));
				parametros.put("VALOR_PARCELA", "R$ "+ValorMonetarioHelper.formata("###,###.00", (contrato.getValoContrato().doubleValue()/contrato.getQtdParcelas())));

			}else{
				parametros.put("PRECO", "");
				parametros.put("VALOR_PARCELA", "");
			}
			parametros.put("CUSTO_PECAS",custoPecas);
			parametros.put("CUSTO_MO",custoMo);
			byte[] bytes = null;
			try {  
				//pdfProposta = JasperRunManager.runReportToPdf( jasperReport, parametros, new JREmptyDataSource()); 
				pdfProposta = JasperFillManager.fillReport(jasperReport, parametros,  new JREmptyDataSource());
				JROdtExporter docxExporter = new JROdtExporter();
				ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();  
				docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);  
				docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, pdfProposta);  
				//docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);  
				docxExporter.exportReport();
				bytes = xlsReport.toByteArray();
			} catch (Exception jre) {  
				jre.printStackTrace();  
			}  

			//Parametros para nao fazer cache e o que será exibido..  
			//response.setContentType( "application/pdf" );  
			response.addHeader("Content-disposition", "attachment; filename=orçamento.odt"); ; 

			//Envia para o navegador o pdf..  
			ServletOutputStream servletOutputStream = response.getOutputStream();  

			servletOutputStream.write( bytes );  
			servletOutputStream.flush();  
			servletOutputStream.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
			if(conn != null){
				try {
					rs.close();
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
