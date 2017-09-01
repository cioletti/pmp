package com.pmp.util;

import java.math.BigDecimal;
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

import com.pmp.bean.UsuarioBean;
import com.pmp.business.AgendamentoBusiness;
import com.pmp.business.OsBusiness;
import com.pmp.entity.PmpConfigOperacional;
import com.pmp.entity.PmpContHorasStandard;
import com.pmp.entity.TwFuncionario;

public class FindOsEstimadaContrato implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Connection con = null;
        Statement prstmt = null;
        Statement prstmtSegmento = null;
        EntityManager manager = null;
        ResultSet rs = null;
        try {
        	con = com.pmp.util.ConectionDbs.getConnecton(); 
        	prstmt = con.createStatement();
        	prstmtSegmento = con.createStatement();
        	manager = JpaUtil.getInstance();

        	Query query = manager.createQuery("From PmpConfigOperacional where codErroDbs = '100'");
        	List<PmpConfigOperacional> osOperacionalList = query.getResultList();
        	String pair = "";
        	for (PmpConfigOperacional operacional : osOperacionalList) {
				pair += "'"+operacional.getId().toString()+"-P',";
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
        			PmpConfigOperacional osOperacional = manager.find(PmpConfigOperacional.class, Long.valueOf(PEDSM));
        		       		        		
        			if(osOperacional != null){
        				
        				if(CODERR.equals("00")){
        					//osOperacional.setMsg("OS Criada com sucesso!");
        					//Coloca a OS como Open
        					UsuarioBean bean = new UsuarioBean();
        					bean.setFilial(osOperacional.getFilial().toString());
        					
        					osOperacional.setCodErroDbs("00");
        					osOperacional.setNumOs(WONO);
        					BigDecimal valorContrato = osOperacional.getIdContrato().getValoContrato();
        					int decimalPlace = 2;
        					valorContrato = valorContrato.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
        					String total = valorContrato.toString().replace(".", "");
        					for (int i = total.length();  i < 13; i++) {
        						total = "0"+total;
        					}
        					String horas = "";
        					if(osOperacional.getIdContrato().getIdClassificacaoContrato().getSigla().equals("PLUS")){
        						query = manager.createQuery("From PmpContHorasStandardPlus where idContrato.id =:idContrato");
        						query.setParameter("idContrato", osOperacional.getIdContrato().getId());
        						Integer horasAux = IConstantAccess.horasManutPlus * query.getResultList().size();
        						if(horasAux.toString().length() == 3){
        							horas = horasAux+"00";
        						}else if(horasAux.toString().length() == 1){
        							horas = "00"+horasAux+"00";
        						}else if(horasAux.toString().length() == 2){
        							horas = "0"+horasAux+"00";
        						}
        						//horas = "00000";// tem que multiplicar pela quantidade de vezes que será feita a revisão
        					}else{
        						query = manager.createQuery("From PmpContHorasStandard where idContrato.id =:idContrato");
        						query.setParameter("idContrato", osOperacional.getIdContrato().getId());

        						List<PmpContHorasStandard> contHorasStandardList = query.getResultList();
        						BigDecimal totalHHManutencao = BigDecimal.ZERO;
        						for (PmpContHorasStandard standard : contHorasStandardList) {
        							query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
        									" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
        									" and h.bgrp = '"+osOperacional.getIdContrato().getBgrp()+"'"+
        									" and substring(h.beqmsn,1,4) = '"+osOperacional.getIdContrato().getPrefixo()+"'"+
        									" and substring(h.beqmsn,5,10) between '"+osOperacional.getIdContrato().getBeginRanger().substring(4, 9)+"' and '"+osOperacional.getIdContrato().getEndRanger().substring(4, 9)+"'");
        							totalHHManutencao = totalHHManutencao.add((BigDecimal)query.getSingleResult());
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
        					}
        					//Enviar os segmentos para o DBS
        					
        					pair = "'"+osOperacional.getId().toString()+"-P','"+osOperacional.getNumeroSegmento()+"','"+osOperacional.getCscc()+"','"+osOperacional.getJobCode()+"','"+osOperacional.getCompCode()+"','"+osOperacional.getInd()+"','F','F','"+total+"','"+horas+"','00001'";
        					if(osOperacional.getIdContrato().getIdTipoContrato().getSigla().equals("VPG")){
        						pair += ",'CPRE'";										      
        						SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPSGSM0 (wonosm, wosgno, cscc, jbcd, cptcd, ind, shpfld, lbrate, lbamt, frsthr ,qty, mecr) values("+pair+")";
        					}else{
        						//con = DriverManager.getConnection(url, user, password);  
        						SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPSGSM0 (wonosm, wosgno, cscc, jbcd, cptcd, ind, shpfld, lbrate, lbamt, frsthr ,qty) values("+pair+")";
        						//prstmt_ = con.prepareStatement(SQL);
        					}
        					try {
								prstmtSegmento.executeUpdate(SQL);
							} catch (Exception e) {
								e.printStackTrace();
							}
        					//        					prstmtSegmento = con.createStatement();
        					//        					//Envia as peças para o DBS
        					//        					new OsBusiness(null).sendPecasDbs(osOperacional, manager, prstmtSegmento);
        					//        					osOperacional.setMsg(null);
        					//        					osOperacional.setCodErroDocDbs("100");
        					Thread.sleep(15000);
        					new OsBusiness(bean).openOs(osOperacional.getId(), WONO, "P");

        				}else{
        					osOperacional.setNumOs(DESCERR);
        					//osOperacional.setNumOs(DESCERR);
        					osOperacional.setCodErroDbs("99");
        				}	
        			} else {
        				manager.getTransaction().commit();
        				continue;   				
        			}
        			manager.getTransaction().commit();
        			if(osOperacional.getIdContrato().getIdTipoContrato().getSigla().equals("VPG") && CODERR.equals("00")){
        				if(osOperacional.getIdContrato().getIdClassificacaoContrato().getSigla().equals("PLUS")){
        					query = manager.createQuery("From PmpContHorasStandardPlus where idContrato.id =:id");
        					query.setParameter("id", osOperacional.getIdContrato().getId());
        					ConectionDbs.setNotesFluxoOSDBS("Visitas "+query.getResultList().size(), con, osOperacional.getNumOs(), "028").close();
        				}else{
        					query = manager.createQuery("from PmpContHorasStandard where idContrato.id =:id");
        					query.setParameter("id", osOperacional.getIdContrato().getId());
        					List<PmpContHorasStandard> horasStandards = query.getResultList();
        					PmpContHorasStandard contHorasStandardRevisao1 = horasStandards.get(0);
        					PmpContHorasStandard contHorasStandardRevisao2 = horasStandards.get(horasStandards.size() - 1);
        					ConectionDbs.setNotesFluxoOSDBS(contHorasStandardRevisao1.getHorasManutencao()+" a "+contHorasStandardRevisao2.getHorasManutencao(), con, osOperacional.getNumOs(), "27").close();
        					ConectionDbs.setNotesFluxoOSDBS("Visitas "+horasStandards.size(), con, osOperacional.getNumOs(), "028").close();
        				}

        				ConectionDbs.setNotesFluxoOSDBS(osOperacional.getIdContrato().getNumeroContrato(), con, osOperacional.getNumOs(), "024").close();
        				ConectionDbs.setNotesFluxoOSDBS(osOperacional.getIdContrato().getModelo(), con, osOperacional.getNumOs(), "025").close();
        				ConectionDbs.setNotesFluxoOSDBS(osOperacional.getIdContrato().getNumeroSerie(), con, osOperacional.getNumOs(), "026").close();
        				ConectionDbs.setNotesFluxoOSDBS(osOperacional.getIdContrato().getIdTipoContrato().getDescricao(), con, osOperacional.getNumOs(), "029").close();
        				
        				AgendamentoBusiness business = new AgendamentoBusiness();
        				String nomeContato = "NOME DO CONTATO ........:"+osOperacional.getIdContrato().getContatoServicos();
        				prstmt = business.setNotesOSDBS( con, osOperacional.getNumOs(), "010", (nomeContato.length() > 50)?nomeContato.substring(0, 50):nomeContato);
        				String telefoneContato = "TELEFONE DO CONTATO ....:"+osOperacional.getIdContrato().getTelefoneServicos();
        				prstmt = business.setNotesOSDBS( con, osOperacional.getNumOs(), "011", (telefoneContato.length() > 50)?telefoneContato.substring(0, 50):telefoneContato);
        				String emailContato = "EMAIL CONTATO..:"+osOperacional.getIdContrato().getEmailContatoServicos();
        				prstmt = business.setNotesOSDBS( con, osOperacional.getNumOs(), "012", (emailContato.length() > 50)?emailContato.substring(0, 50):emailContato);
        				String maoDeObra = "VALOR MOBRA/MISC/DESLOC.: R$"+osOperacional.getIdContrato().getValoContrato();
        				prstmt = business.setNotesOSDBS( con, osOperacional.getNumOs(), "014", (maoDeObra.length() > 50)?maoDeObra.substring(0, 50):maoDeObra);
//        				String pecas = "VALOR DE PECAS .........: R$"+valorPecas;
//        				prstmt = business.setNotesOSDBS( con, osOperacional.getIdContrato().getNumeroContrato(), "015", (pecas.length() > 50)?pecas.substring(0, 50):pecas);
        				String total = "VALOR TOTAL ............: R$"+osOperacional.getIdContrato().getValoContrato();
        				prstmt = business.setNotesOSDBS( con, osOperacional.getNumOs(), "016", (total.length() > 50)?total.substring(0, 50):total);
        				String condicaoPagamento = "CONDICAO DE PAGAMENTO ..: "+osOperacional.getIdContrato().getQtdParcelas()+" parcela(s)";
        				prstmt = business.setNotesOSDBS( con, osOperacional.getNumOs(), "017", (condicaoPagamento.length() > 50)?condicaoPagamento.substring(0, 50):condicaoPagamento);
        				prstmt = business.setNotesOSDBS( con, osOperacional.getNumOs(), "018", "PROPOSTA NUMERO ........: "+osOperacional.getIdContrato().getNumeroContrato());
        				TwFuncionario funcionario = manager.find(TwFuncionario.class, osOperacional.getIdFuncionarioCriadorOs());
//        				prstmt = business.setNotesOSDBS( con, osOperacional.getNumOs(), "019", "AUTORIZADO POR .........: "+funcionario.getEstimateBy());
        				
        				nomeContato =  "AUTORIZADO POR .........: "+osOperacional.getIdContrato().getContatoServicos();
        				prstmt = business.setNotesOSDBS(con, osOperacional.getNumOs(), "019", ((nomeContato.length() > 50)?nomeContato.substring(0, 50):nomeContato));
        				prstmt = business.setNotesOSDBS(con, osOperacional.getNumOs(), "021", "NOTES PREENCHIDO POR .........: "+funcionario.getEstimateBy());

        				
        				
        			}
        		}
        	}
        } catch (Exception e) {
        	if(manager != null && manager.getTransaction().isActive()){
        		manager.getTransaction().rollback();
        	}
        	EmailHelper emailHelper = new EmailHelper();
        	emailHelper.sendSimpleMail("Erro ao recuperar OS de Agendamento!", "Erro ao Buscar OS Agendamento", "rodrigo@rdrsistemas.com.br");
        	e.printStackTrace();
        	
        }finally{
        	try {
        		if(manager != null && manager.isOpen()){
        			manager.close();
        		}
        		if(rs != null){
        			rs.close();
        		}
				if(prstmtSegmento != null){
					prstmtSegmento.close();
				}
				if(prstmt != null){
					prstmt.close();
				}
				if(con != null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	
        }
		
	}

}
