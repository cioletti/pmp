package com.pmp.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pmp.entity.PmpClientePl;
import com.pmp.entity.PmpHora;
import com.pmp.entity.PmpHoraPK;
import com.pmp.entity.PmpManutencao;
import com.pmp.entity.PmpManutencaoPrecoCusto;
import com.pmp.entity.PmpRange;
import com.pmp.entity.PmpRangePK;
import com.pmp.entity.TwFilial;
import com.pmp.util.EmailHelper;
import com.pmp.util.JpaUtil;

/**
 * Servlet implementation class ServicoImportacao
 */
public class ServicoImportacao extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String SQL_FIND_ALL_PMP = "SELECT TRIM(HO.BGRP) as BGRP, TRIM(HO.BEQMSN) as BEQMSN, TRIM(UNCS) as UNCS, TRIM(UNLS) as UNLS, TRIM(DLRQTY) as DLRQTY, TRIM(MD5.PANO20)as PANO20, TRIM(BECTYC) as BECTYC, TRIM(CPTCD) as CPTCD, TRIM(PTO.DS18) as DS18, TRIM(PTO.SOS1) SOS1 "+
			" FROM LIBU15.PCPPIPT0 PTO, LIBU15.SHLRBMD5 MD5 ,LIBU15.SHLRBMH0 HO"+
			" WHERE PTO.PANO20 = MD5.PANO20"+
			" AND PTO.SOS1 = MD5.SOS1"+
			" AND MD5.RLTDHS = HO.RLTDHS"+
			" AND TRIM(HO.BGRP) NOT IN ('BLDK', 'CAP', 'CSA', 'PMP', 'SPBT', 'BLDR','', 'STJ')";

			private final String SQL_FIND_ALL_PMP_POR_FILIAL = "SELECT TRIM(HO.BGRP) as BGRP, TRIM(HO.BEQMSN) as BEQMSN, TRIM(PP.LANDCS) as LANDCS, TRIM(DLRQTY) as DLRQTY, TRIM(MD5.PANO20)as PANO20, TRIM(BECTYC) as BECTYC, TRIM(CPTCD) as CPTCD, TRIM(PTO.DS18) as DS18, TRIM(PP.STNO) as  STNO"+
			" FROM LIBU15.PCPPIPT0 PTO, LIBU15.SHLRBMD5 MD5 ,LIBU15.SHLRBMH0 HO,LIBU15.PCPPIST0 PP"+
			" WHERE PP.PANO20 = PTO.PANO20" +
			" AND PP.SOS1 = PTO.SOS1" +
			" AND PTO.PANO20 = MD5.PANO20" +
			" AND TRIM(PP.STNO) = '0'"+
			" AND PTO.SOS1 = MD5.SOS1"+
			" AND MD5.RLTDHS = HO.RLTDHS"+
			" AND TRIM(HO.BGRP) NOT IN ('BLDK', 'CAP', 'CSA', 'PMP', 'SPBT', 'BLDR','', 'STJ')";
			
			
			
			
			private String SQL_HORAS_PMP = " SELECT TRIM(SH2.JBCD) as JBCD , TRIM(SH2.CPTCD) as CPTCD, TRIM(SH2.EQMFCD) as EQMFCD, TRIM(SH2.BEQMSN) as BEQMSN, TRIM(SH2.FRSDHR) as FRSDHR, TRIM(SH2.EQMFMD) as EQMFMD, TRIM(BGRP) as BGRP  FROM LIBU15.SHLSTAN2 SH2 WHERE TRIM(SH2.BGRP) NOT IN ('BLDK', 'CAP', 'CSA', 'PMP', 'SPBT','BLDR', '', 'STJ')";

			private String SQL_RANGE = "SELECT TRIM(SHL.EQMFCD) as EQMFCD, TRIM(SHL.BEGSN2) as BEGSN2, TRIM(SHL.ENDSN2) as ENDSN2, TRIM(SHL.EQMFM2) as EQMFM2, TRIM(SHL.BSERNO) as BSERNO from LIBU15.SHLBSSN0 SHL WHERE TRIM(SHL.EQMFM2) <> ''";
			
			private String SQL_FIND_CUSTOMER_MACHINE = "SELECT TRIM(EMPEQPD0.EQMFS2) EQMFS2, TRIM(CIPNAME0.CUNO) CUNO, TRIM(CIPNAME0.CUNM) CUNM, TRIM(CIPNAME0.STN1) STN1"+
													    " FROM B108F034.LIBU15.CIPNAME0 CIPNAME0, B108F034.LIBU15.EMPEQPD0 EMPEQPD0"+
														" WHERE CIPNAME0.CUNO = EMPEQPD0.CUNO AND ((EMPEQPD0.EQMFCD='AA')) ";
			private String SQL_FIND_FILIAL = "SELECT WOLSTSQ0.STNO STNO, WOLSTSQ0.STNM STNM"+
											 " FROM B108F034.LIBU15.WOLSTSQ0 WOLSTSQ0"+
											 " WHERE (WOLSTSQ0.SQNOPF Like 'M%') ";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServicoImportacao() {
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
		ResultSet rs = null;
		PreparedStatement prstmt = null;

		Connection con = null;
		//while(isLoadService){
			try {
//				InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
//				Properties prop = new Properties();
//				prop.load(in);
//				String url = prop.getProperty("dbs.url");
//				String user = prop.getProperty("dbs.user");
//				String password =prop.getProperty("dbs.password");
//				Class.forName(prop.getProperty("dbs.driver")).newInstance();

				con = com.pmp.util.ConectionDbs.getConnecton(); 

				prstmt = con.prepareStatement(SQL_FIND_ALL_PMP);
				rs = prstmt.executeQuery();
				EntityManager manager = null;
				//boolean isConnection = true;
				//while(isConnection){
					try {
						manager = JpaUtil.getInstance();

						manager.getTransaction().begin();
						Query query = manager.createNativeQuery("delete from pmp_manutencao");
						query.executeUpdate();
						query = manager.createNativeQuery("delete from pmp_manutencao_preco_custo");
						query.executeUpdate();
						query = manager.createNativeQuery("delete from pmp_hora");
						query.executeUpdate();
						query = manager.createNativeQuery("delete from pmp_range");
						query.executeUpdate();
						query = manager.createNativeQuery("delete from pmp_cliente_pl");
						query.executeUpdate();
						//manager.getTransaction().commit();
						while(rs.next()){
							//manager.getTransaction().begin();
							PmpManutencao pManutencao = new PmpManutencao();
							
							pManutencao.setBectyc(rs.getString("BECTYC"));
							pManutencao.setDlrqty(Long.valueOf((rs.getString("DLRQTY"))));
							pManutencao.setDs18(rs.getString("DS18"));
							pManutencao.setUncs(new BigDecimal(rs.getString("UNCS")));
							pManutencao.setUnls(new BigDecimal(rs.getString("UNLS")));
							pManutencao.setSos(rs.getString("SOS1"));
							pManutencao.setOcptmd(rs.getString("OCPTMD"));
							pManutencao.setOjbloc(rs.getString("OJBLOC"));
							pManutencao.setBgrp(rs.getString("BGRP"));
							pManutencao.setBeqmsn(rs.getString("BEQMSN"));
							pManutencao.setPano20(rs.getString("PANO20"));
							pManutencao.setCptcd(rs.getString("CPTCD"));
							manager.persist(pManutencao);
							//manager.getTransaction().commit();
						}
						
						prstmt = con.prepareStatement(SQL_FIND_ALL_PMP_POR_FILIAL);
						rs = prstmt.executeQuery();
						while(rs.next()){
							//manager.getTransaction().begin();
							PmpManutencaoPrecoCusto pManutencao = new PmpManutencaoPrecoCusto();
							pManutencao.setBectyc(rs.getString("BECTYC"));
							pManutencao.setDlrqty(Long.valueOf((rs.getString("DLRQTY"))));
							pManutencao.setDs18(rs.getString("DS18"));
							pManutencao.setLandcs(new BigDecimal(rs.getString("LANDCS")));
							pManutencao.setOcptmd(rs.getString("OCPTMD"));
							pManutencao.setOjbloc(rs.getString("OJBLOC"));
							pManutencao.setBgrp(rs.getString("BGRP"));
							pManutencao.setBeqmsn(rs.getString("BEQMSN"));
							pManutencao.setPano20(rs.getString("PANO20"));
							pManutencao.setCptcd(rs.getString("CPTCD"));
							manager.persist(pManutencao);
							//manager.getTransaction().commit();
						}
				
						prstmt = con.prepareStatement(SQL_HORAS_PMP);
						rs = prstmt.executeQuery();
						while(rs.next()){
							//manager.getTransaction().begin();
							PmpHora pmpHora = new PmpHora();
							PmpHoraPK pmpHoraPK = new PmpHoraPK(rs.getString("JBCD"), rs.getString("CPTCD"),rs.getString("BEQMSN"), rs.getString("BGRP") );
							pmpHora.setEqmfcd(rs.getString("EQMFCD"));
							pmpHora.setEqmfmd(rs.getString("EQMFMD"));
							pmpHora.setFrsdhr(rs.getString("FRSDHR"));
							pmpHora.setPmpHoraPK(pmpHoraPK);
							manager.merge(pmpHora);
							//manager.getTransaction().commit();
						}
						
						prstmt = con.prepareStatement(SQL_RANGE);
						rs = prstmt.executeQuery();
						while(rs.next()){
							//manager.getTransaction().begin();
							PmpRange pmpRange = new PmpRange();
							PmpRangePK pmpHoraPK = new PmpRangePK(rs.getString("BEGSN2"), rs.getString("ENDSN2"),rs.getString("EQMFM2") );
							pmpRange.setBserno(rs.getString("BSERNO"));
							pmpRange.setEqmfcd(rs.getString("EQMFCD"));
							pmpRange.setPmpRangePK(pmpHoraPK);
							manager.merge(pmpRange);
							//manager.getTransaction().commit();
						}


						
						prstmt = con.prepareStatement(SQL_FIND_CUSTOMER_MACHINE);
						rs = prstmt.executeQuery();
						while(rs.next()){
							//manager.getTransaction().begin();
							PmpClientePl clientePl = new PmpClientePl();
							clientePl.setSerie(rs.getString("EQMFS2"));
							clientePl.setCodCliente(rs.getString("CUNO"));
							clientePl.setNomeCliente(rs.getString("CUNM"));
							clientePl.setFilial(Long.valueOf(rs.getString("STN1")));
							manager.merge(clientePl);
							//manager.getTransaction().commit();
						}
						prstmt = con.prepareStatement(SQL_FIND_FILIAL);
						rs = prstmt.executeQuery();
						while(rs.next()){
							//manager.getTransaction().begin();
							TwFilial twFilial = new TwFilial();
							twFilial.setStno(new Long(rs.getInt("STNO")));
							twFilial.setStnm(rs.getString("STNM"));
							try {
								TwFilial filial = manager.find(TwFilial.class, twFilial.getStno());
								filial.setStnm(twFilial.getStnm());
								manager.merge(filial);
							} catch (Exception e) {
								e.printStackTrace();
								manager.merge(twFilial);
							}
							//manager.getTransaction().commit();
						}
						
						manager.getTransaction().commit();
						//isLoadService = false;
						EmailHelper emailHelper = new EmailHelper();
						emailHelper.sendSimpleMail("Serviço de importação de Standard Job executado", "Standard Job", "cioletti.rodrigo@gmail.com");
						//System.out.println("Importação realizada com sucesso");
						//isConnection = false;
					} catch (Exception e1) {
						if(manager != null && manager.getTransaction().isActive()){
							manager.getTransaction().rollback();
						}
					e1.printStackTrace();
						EmailHelper emailHelper = new EmailHelper();
						//emailHelper.sendSimpleMail("Erro ao executar a busca do STANDARD JOB no DBS", "ERRO STANDARD JOB", "ti.monitoramento@marcosa.com.br");
						emailHelper.sendSimpleMail("Erro ao executar a busca do STANDARD JOB no DBS", "ERRO STANDARD JOB", "cioletti.rodrigo@gmail.com");
					} finally {
						if(manager != null && manager.isOpen()){
							manager.close();
						}
					}
				//}
			}catch (Exception e) {
				
				e.printStackTrace();
				EmailHelper emailHelper = new EmailHelper();
				//emailHelper.sendSimpleMail("Erro ao tentar conectar no DBS (STANDARD JOB)", "ERRO STANDARD JOB", "ti.monitoramento@marcosa.com.br");
				emailHelper.sendSimpleMail("Erro ao tentar conectar no DBS (STANDARD JOB)", "ERRO STANDARD JOB", "cioletti.rodrigo@gmail.com");
			}finally{
				try {
					
					rs.close();
					prstmt.close();
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
	}

}
