package com.pmp.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pmp.bean.PecaBean;
import com.pmp.bean.PecasDbsBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.business.OsBusiness;
import com.pmp.entity.PmpContHorasStandard;
import com.pmp.entity.PmpDescontoMultVac;
import com.pmp.entity.PmpOsOperacional;

import flex.messaging.io.ArrayList;

public class FindOsEstimadaAgendamento implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Connection con = null;
        Statement prstmt = null;
        Statement prstmtSegmento = null;
        Statement prstmtMulti = null;
        EntityManager manager = null;
        ResultSet rs = null;
        ResultSet rsMulti = null;
        try {
        	con = com.pmp.util.ConectionDbs.getConnecton(); 
        	prstmt = con.createStatement();
        	prstmtSegmento = con.createStatement();
        	prstmtMulti = con.createStatement();
        	manager = JpaUtil.getInstance();

        	Query query = manager.createQuery("From PmpOsOperacional where codErroOsDbs in ('100', '99')");
        	List<PmpOsOperacional> osOperacionalList = query.getResultList();
        	String pair = "";
        	for (PmpOsOperacional operacional : osOperacionalList) {
				pair += "'"+operacional.getId().toString()+"-A',";
			}
        	pair = (pair.length() > 0)?pair.substring(0, pair.length()-1):"''";
        	String SQL = "select TRIM(DESCERR) DESCERR, TRIM(CODERR) CODERR, TRIM(WONO) WONO, TRIM(WONOSM) WONOSM from "+IConstantAccess.AMBIENTE_DBS+".USPWOSM0 where LENGTH(TRIM(CODERR)) > 0 and TRIM(WONOSM) in("+pair+")";
        	rs = prstmt.executeQuery(SQL);
        	while (rs.next()){		            	 
        		String CODERR = rs.getString("CODERR").trim();				
        		String WONO = rs.getString("WONO").trim();				
        		String DESCERR = rs.getString("DESCERR").trim();
        		String [] aux = rs.getString("WONOSM").trim().split("-");
        		if(aux.length > 1){
        			String PEDSM = aux[0];
        			manager.getTransaction().begin();
        			PmpOsOperacional osOperacional = manager.find(PmpOsOperacional.class, Long.valueOf(PEDSM));
        		       		        		
        			if(osOperacional != null){
        				
        				if(CODERR.equals("00")){
        					//Coloca a OS como Open
        					UsuarioBean bean = new UsuarioBean();
        					bean.setFilial(osOperacional.getFilial().toString());
        					
        					//osOperacional.setMsg("OS Criada com sucesso!");
        					osOperacional.setCodErroOsDbs("00");
        					osOperacional.setNumOs(WONO);
        					String horas = "";
        					String totalMO = "";
        					if(osOperacional.getIdConfigOperacional().getIdContrato().getIdClassificacaoContrato().getSigla().equals("PLUS")){
        						query = manager.createQuery("From PmpContHorasStandardPlus where idContrato.id =:idContrato");
        						query.setParameter("idContrato", osOperacional.getIdConfigOperacional().getIdContrato().getId());
        						Integer horasAux = IConstantAccess.horasManutPlus;
        						if(horasAux.toString().length() == 3){
        							horas = horasAux+"00";
        						}else if(horasAux.toString().length() == 1){
        							horas = "00"+horasAux+"00";
        						}else if(horasAux.toString().length() == 2){
        							horas = "0"+horasAux+"00";
        						}
        						
        						totalMO = osOperacional.getIdContHorasStandardPlus().getCusto().toString().replace(".", "");
        						for (int i = totalMO.length();  i < 13; i++) {
        							totalMO = "0"+totalMO;
        						}
        					}else{
        						query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
        								" where h.cptcd = '"+osOperacional.getCompCode()+"'"+
        								" and h.bgrp = '"+osOperacional.getIdConfigOperacional().getIdContrato().getBgrp()+"'"+
        								" and substring(h.beqmsn,1,4) = '"+osOperacional.getIdConfigOperacional().getIdContrato().getPrefixo()+"'"+
        								" and substring(h.beqmsn,5,10) between '"+osOperacional.getIdConfigOperacional().getIdContrato().getBeginRanger().substring(4, 9)+"' and '"+osOperacional.getIdConfigOperacional().getIdContrato().getEndRanger().substring(4, 9)+"'");

        						BigDecimal totalHHManutencao = BigDecimal.ZERO;
        						BigDecimal valorMO = BigDecimal.ZERO;
        						if(query.getResultList().size() > 0 && query.getResultList().get(0) != null){
        							totalHHManutencao = (BigDecimal)query.getSingleResult();
        							valorMO =  osOperacional.getIdConfigOperacional().getIdContrato().getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().multiply(totalHHManutencao);//valor de hh
        							valorMO = valorMO.add(osOperacional.getIdConfigOperacional().getIdContrato().getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().multiply(BigDecimal.valueOf(osOperacional.getIdConfigOperacional().getIdContrato().getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue())));
        						}

        						if(osOperacional.getIdConfigOperacional().getIdContrato().getIsSpot() != null && osOperacional.getIdConfigOperacional().getIdContrato().getIsSpot().equals("S")){
        							valorMO = osOperacional.getIdContHorasStandard().getCustoMo();
        						}
        						int decimalPlace = 2;
        						if(valorMO == null){
        							valorMO = BigDecimal.ZERO;
        						}
        						valorMO = valorMO.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
        						totalMO = valorMO.toString().replace(".", "");
        						
        						
        						for (int i = totalMO.length();  i < 13; i++) {
        							totalMO = "0"+totalMO;
        						}
        						
        						try{
        							SQL = "select * from "+IConstantAccess.LIB_DBS+".EMPTEXT0 where NTDA like '%MULTI%' and EQMFS2 = '"+osOperacional.getIdConfigOperacional().getIdContrato().getNumeroSerie()+"'";
        							prstmtMulti = con.createStatement();
        							rsMulti = prstmtMulti.executeQuery(SQL);
        							if(rsMulti.next()){
        								query = manager.createQuery("from PmpDescontoMultVac");
        								if(query.getResultList().size() > 0){
        									PmpDescontoMultVac multVac = (PmpDescontoMultVac)query.getResultList().get(0);
        									if(multVac.getDescontoMultiVac() > 0 && totalHHManutencao.longValue() > 0){
        										BigDecimal descontoMultVac = BigDecimal.valueOf(multVac.getDescontoMultiVac()).divide(BigDecimal.valueOf(100));
        										totalHHManutencao = totalHHManutencao.subtract(totalHHManutencao.multiply(descontoMultVac));
        										totalHHManutencao = totalHHManutencao.setScale(2,RoundingMode.HALF_UP);
        										osOperacional.getIdContHorasStandard().setIsMultVac("S");
        									}
        								}
        							}
        						}catch (Exception e) {
									e.printStackTrace();
								}
        						
        						horas = totalHHManutencao.toString().replace(".", "");
        						//        					horas = new Integer(Integer.valueOf(horas) * segmentoBean.getQtdComp()).toString();
        						if(horas.length() == 3){
        							horas = "00"+horas;
        						}else if(horas.length() == 4){
        							horas = "0"+horas;
        						}else if(horas.length() == 1){
        							horas = "0000"+horas;
        						}else if(horas.length() == 2){
        							horas = "000"+horas;
        						}
        						if("PART".equals(osOperacional.getIdConfigOperacional().getIdContrato().getIdClassificacaoContrato().getSigla())){
        							PmpContHorasStandard standard = manager.find(PmpContHorasStandard.class, osOperacional.getIdContHorasStandard().getId());
        							if("S".equals(standard.getIsPartner())){
        								osOperacional.getIdContHorasStandard().setIsMultVac(null);
        								horas = "00000";
        								totalMO = "0000000000000";
        							}
        						} else if("CUS".equals(osOperacional.getIdConfigOperacional().getIdContrato().getIdClassificacaoContrato().getSigla())
        								|| osOperacional.getIdConfigOperacional().getIdContrato().getIdClassificacaoContrato().getSigla()
        								.equals("CUSLIGHT")){
        							osOperacional.getIdContHorasStandard().setIsMultVac(null);
        							horas = "00000";
        							totalMO = "0000000000000";
        						}
        					}
        					//Enviar os segmentos para o DBS
//        					if(osOperacional.getIdConfigOperacional().getIdContrato().getIdClassificacaoContrato().getSigla().equals("PLUS")){
//        						pair = "'"+osOperacional.getId().toString()+"-A','"+osOperacional.getNumeroSegmento()+"','"+osOperacional.getCscc()+"','"+osOperacional.getJobCode()+"','"+osOperacional.getCompCode()+"','"+osOperacional.getInd()+"','F','"+horas+"','F','"+totalMO+"','00001'";
//        					}else{
        						pair = "'"+osOperacional.getId().toString()+"-A','"+osOperacional.getNumeroSegmento()+"','"+osOperacional.getCscc()+"','"+osOperacional.getJobCode()+"','"+osOperacional.getCompCode()+"','"+osOperacional.getInd()+"','F','"+horas+"','F','"+totalMO+"','00001'";
        				//	}
        					//con = DriverManager.getConnection(url, user, password);  
        					try {
        						
        						SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPSGSM0 (wonosm, wosgno, cscc, jbcd, cptcd, ind, shpfld, frsthr, lbrate, lbamt, qty) values("+pair+")";
        						
        						//prstmt_ = con.prepareStatement(SQL);
        						try {
									prstmtSegmento.executeUpdate(SQL);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

        						prstmtSegmento = con.createStatement();
        						Thread.sleep(10000);
        						if(osOperacional.getIdConfigOperacional().getIdContrato().getIdClassificacaoContrato().getSigla().equals("PLUS")){
        							List<PecaBean> result = new ArrayList();
        							for(int i = 0; i < 3; i++){
        								PecaBean pecaBean = new PecaBean();
        								pecaBean.setSos("700");
        								pecaBean.setPano20("SOS");
        								pecaBean.setDs18("KIT SOS          #");
        								pecaBean.setDlrqty(1);
        								result.add(pecaBean);
        							}
        							new OsBusiness(bean).sendPecasDbsPlus(osOperacional, manager, prstmtSegmento, result);
        						}else{
        							//Envia as peças para o DBS
        							new OsBusiness(bean).sendPecasDbs(osOperacional, manager, prstmtSegmento);
        						}
        						osOperacional.setMsg("Aguarde o retorno da cotação!");
        						osOperacional.setCodErroDocDbs("100");
        						new OsBusiness(bean).openOs(osOperacional.getId(), WONO, "A");
        					} catch (Exception e) {
        						e.printStackTrace();
        					}
        					
        				}else{
        					osOperacional.setMsg(DESCERR);
        					osOperacional.setNumOs(DESCERR);
        					//osOperacional.setNumOs(DESCERR);
        					osOperacional.setCodErroOsDbs("99");
        				}	
        			} else {
        				manager.getTransaction().commit();
        				continue;   	
        			}
        			manager.getTransaction().commit();
        		
        		}
        	}
        } catch (Exception e) {
        	if(manager != null && manager.getTransaction().isActive()){
        		manager.getTransaction().rollback();
        	}
        	EmailHelper emailHelper = new EmailHelper();
        	StringWriter writer = new StringWriter();
        	e.printStackTrace(new PrintWriter(writer));
        	emailHelper.sendSimpleMail(writer.toString(), "Erro ao Buscar OS Agendamento", "rodrigo@rdrsistemas.com.br");
        	e.printStackTrace();
        	
        }finally{
        	try {
        		if(manager != null && manager.isOpen()){
        			manager.close();
        		}
        		if(rs != null){
        			rs.close();
        		}
        		if(rsMulti != null){
        			rsMulti.close();
        		}
				if(prstmtSegmento != null){
					prstmtSegmento.close();
				}
				if(prstmt != null){
					prstmt.close();
				}
				if(prstmtMulti != null){
					prstmtMulti.close();
				}
				if(con != null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	
        }
		
	}
	
	public static void main(String[] args) {
		BigDecimal valor = BigDecimal.valueOf(3.0);
		valor = 
	          valor.setScale(2,RoundingMode.HALF_UP);
		System.out.println(valor);
	}

}
