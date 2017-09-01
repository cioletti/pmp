package com.pmp.business;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.pmp.bean.OperacionalBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.util.JpaUtil;
import com.pmp.util.OrderDateAsc;
import com.pmp.util.OrderDateDesc;
import com.pmp.util.OrderHorimetroDesc;
import com.pmp.util.OrderHrPendentesAsc;
import com.pmp.util.ValorMonetarioHelper;

import edu.emory.mathcs.backport.java.util.Collections;

public class OperacionalBusiness {

	private String FILIAL;
	private Boolean ADM;
	private final int MES = 30;
	
	public OperacionalBusiness(UsuarioBean bean) {
		FILIAL = bean.getFilial();
		ADM = bean.getIsAdm();
	}
	
//	public static void main(String[] args) {
//		//String date = "2013/06/04 10:46:03";
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
//
//		Date date;
//		try {
//			date = sdf.parse("2013-06-04 10:46:03");
//			
//			String aff = sdf2.format(date);
//			
//			System.out.println(date);
//			System.out.println(aff.replaceAll("-", "/"));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		//String d = format.format(date);
//		
//		
//	}

	public List<OperacionalBean> findAllOperacionalByFiltro(String filtroUmaPendente, String filtroPendentes, String filtroEncerradas, String filtroHoras50,
			String ordenarHorimetro, String ordenarData, Long inicial, Long numRegistros, String campoPesquisa){
		
		String SQL = "select C.id, C.modelo, C.prefixo, C.numero_Serie, C.horimetro, C.cidade, " +
		" C.numero_Contrato, C.data_Aceite, C.codigo_cliente, C.valo_contrato, C.qtd_parcelas,C.Razao_Social, c.id_funcionario, " +
		" (select f.stnm from tw_filial f where f.stno = c.filial) origem,"+     
		" (select f.stnm from tw_filial f where f.stno = co.filial) destino," +
		" C.filial," +
		" (select t.sigla from pmp_tipo_contrato t where t.id = C.id_tipo_contrato)," +
		" CASE WHEN c.ta = 'S' THEN 'SIM' ELSE 'NÃO' END ta," +
		"(select count(id) from Pmp_Cont_Horas_Standard where id_Contrato = C.id) situacao, "+
		"(select count(id) from Pmp_Cont_Horas_Standard where id_Contrato = C.id and is_Executado = 'S') situacaoExe, " +
		" CO.cod_Erro_Dbs," +
		" (select sigla from PMP_CLASSIFICACAO_CONTRATO where ID = C.id_classificacao_contrato ) siglaClassificacaoContrato, C.MEDIA_HORAS_MES, C.POSSUI_HORAS_MES, cm.CONT_EXCESSAO," +
		" cm.ID_CONFIGURACAO_PRECO, C.PREFIXO, C.BEGIN_RANGER, C.END_RANGER " +
		"from PMP_CONFIG_MANUTENCAO cm, Pmp_Contrato C inner join Pmp_Config_Operacional CO on CO.id_Contrato = C.id " +
		"where UPPER(c.familia) NOT LIKE '%STANDBY%' " +
		" AND C.id_Status_Contrato = (select S.id from Pmp_Status_Contrato S where S.sigla = 'CA') " +
		" and c.id_classificacao_contrato not in (select id from PMP_CLASSIFICACAO_CONTRATO where sigla = 'PLUS')" +
		" and cm.id = c.ID_CONFIG_MANUTENCAO ";
		
		//if(!ADM){
			SQL +="and C.filial = :filial ";
		//}
			
			if(campoPesquisa.equals("")){
				if ((filtroUmaPendente.equals("S")) && (filtroPendentes.equals("S")) && (filtroEncerradas.equals("N")) && (filtroHoras50.equals("S"))){
					SQL +=" and (select count(id) from Pmp_Cont_Horas_Standard where id_Contrato = C.id and is_Executado = 'S') >= 0 " +
					" and (select count(id) from Pmp_Cont_Horas_Standard where id_Contrato = C.id and is_Executado = 'N') > 0 ";
				}else if (filtroUmaPendente.equals("S")){
					SQL +=" and (select count(id) from Pmp_Cont_Horas_Standard where id_Contrato = C.id and is_Executado = 'N') = 1";
				}else if (filtroPendentes.equals("S")){
					SQL +=" and (select count(id) from Pmp_Cont_Horas_Standard where id_Contrato = C.id and is_Executado = 'N') >= 1";
				}else if (filtroEncerradas.equals("S")){
					SQL +=" and (select count(id) from Pmp_Cont_Horas_Standard where id_Contrato = C.id and is_Executado = 'N') = 0";
				} else if(filtroHoras50.equals("S")){
					SQL +=" and  (((select min(horas_Manutencao) from Pmp_Cont_Horas_Standard hs where hs.id_Contrato = C.id and is_Executado = 'N')  - (select horimetro from pmp_maquina_pl pl where pl.numero_serie = C.numero_Serie "+
					" and pl.horimetro is not null and pl.id = (select max(id) from pmp_maquina_pl "+
					" where numero_serie = pl.numero_serie and horimetro is not null and horimetro > 0))) <= 50 or (select horimetro from pmp_maquina_pl pl where pl.numero_serie = C.numero_Serie "+
					" and pl.horimetro is not null and pl.id = (select max(id) from pmp_maquina_pl "+
					" where numero_serie = pl.numero_serie and horimetro is not null and horimetro > 0)) IS null) ";
				}
			}
			
			if(!campoPesquisa.equals("")){
				SQL += "and  (C.MODELO like '%"+campoPesquisa+"%' or C.NUMERO_SERIE like '%"+campoPesquisa+"%'"+  
					" or C.RAZAO_SOCIAL like '%"+campoPesquisa+"%' or CO.NUM_OS like '%"+campoPesquisa+"%' or C.NUMERO_CONTRATO like '%"+campoPesquisa+"%')";
			}
		EntityManager manager = null;
		List<OperacionalBean> op = new ArrayList<OperacionalBean>();
		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createNativeQuery(SQL);
			//if(!ADM){
				query.setParameter("filial", Integer.parseInt(FILIAL));
			//}
				Integer tamanhoTotalLista = query.getResultList().size(); // Total de itens da consulta para a paginação.
				
				query.setFirstResult(inicial.intValue());
				query.setMaxResults(numRegistros.intValue());
			
			List<Object[]> result = query.getResultList();
			
			for (Object [] operacional : result) {
				if(operacional[9] == null){
					continue;
				}
				OperacionalBean bean = new OperacionalBean();
				
				bean.setContratoId(((BigDecimal)operacional[0]).longValue());
				bean.setNumLinhas(tamanhoTotalLista.longValue());
				bean.setNumeroSerie((String)operacional[3]);
				bean.setCodErroDbs((String)operacional[20]);
				bean.setSiglaClassificacaoContrato((String)operacional[21]);
				bean.setDiasProximaRevisao(findDiasProxRevisao(bean.getNumeroSerie(), manager));
				
				bean.setContExcessao((String)operacional[24]);				
				bean.setIdConfiguracaoPreco(((BigDecimal)operacional[25]).longValue());
				bean.setPrefixo((String)operacional[26]);
				bean.setBeginRanger((String)operacional[27]);
				bean.setEndRanger((String)operacional[28]);
				//Define a situação do Contrato
//				String sqlSituacao = "select count(id) from PmpContHorasStandard where idContrato.id = :contratoId " ;					  
//				Query querySituacao = manager.createQuery(sqlSituacao);
//				querySituacao.setParameter("contratoId", bean.getContratoId());
//				Long situacao = (Long)querySituacao.getResultList().get(0);
				Long situacao = (((Integer)operacional[18]).longValue());
				
//				String sqlSituacaoExe = "select count(id) from PmpContHorasStandard where idContrato.id = :contratoId and isExecutado = 'S'" ;					  
//				Query querySituacaoExe = manager.createQuery(sqlSituacaoExe);
//				querySituacaoExe.setParameter("contratoId", bean.getContratoId());
//				Long situacaoExe = (Long)querySituacaoExe.getResultList().get(0);
				Long situacaoExe = ((Integer)operacional[19]).longValue();
				
				if(situacao.longValue() == situacaoExe.longValue()){					
					bean.setSituacao("Vermelho");
					bean.setSituacaoImagem("img/CN.png");
				}else if((situacao - situacaoExe) == 1){
					bean.setSituacao("Amarelo");
					bean.setSituacaoImagem("img/AM.png");
				}else{
					bean.setSituacao("Verde");
					bean.setSituacaoImagem("img/CE.png");
				}
				
				//Busca o Horímetro da Máquina
				String sqlHorimetro =  " select distinct horimetro, data_atualizacao from pmp_maquina_pl pl where pl.numero_serie = '"+bean.getNumeroSerie()+"'"+
                                       " and pl.horimetro is not null and pl.horimetro = (select max(horimetro) from pmp_maquina_pl" + 
                                       " where numero_serie = pl.numero_serie and horimetro is not null and horimetro > 0)" ;
				Query queryHorimetro  = manager.createNativeQuery(sqlHorimetro );
				//queryHorimetro.setParameter("numeroSerie", bean.getNumeroSerie());
				List<Object[]> resultHorimetro = queryHorimetro.getResultList();
				
				//SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
				
				if(resultHorimetro.size() >= 1){
					
					Object [] par = resultHorimetro.get(0);
					
					if(par[1] != null){
						Date date = format.parse((String)par[1]);
						
						bean.setDataAtualizacaoHorimetro((format2.format(date)).replaceAll("-", "/"));
						bean.setDataAtualizacaoHori(date);						
					}
					bean.setHorimetro(((BigDecimal)par[0]).intValue());
					
					//Horas p/ revisões		
					String sqlMaiores = "select horas_Manutencao from Pmp_Cont_Horas_Standard where id_Contrato = :contratoId and is_Executado = 'N'order by horas_Manutencao " ;					  
					Query queryHorasMaiores = manager.createNativeQuery(sqlMaiores);
					queryHorasMaiores.setParameter("contratoId", bean.getContratoId());
					Long proximaManutencao = 0l;
					if(queryHorasMaiores.getResultList().size() > 0){
						proximaManutencao = ((BigDecimal)queryHorasMaiores.getResultList().get(0)).longValue();
					}
					
					if(operacional[23] != null && ((String)operacional[23]).equals("S")){
						if(queryHorasMaiores.getResultList().size() > 0){
							proximaManutencao = ((BigDecimal)queryHorasMaiores.getResultList().get(0)).longValue();
							Double mediaDiasProximaRevisao = ((proximaManutencao.doubleValue() - ((BigDecimal)par[0]).doubleValue())/((BigDecimal)operacional[22]).doubleValue())*30;//média de dias da próxima revisão

							query = manager.createNativeQuery(" select DATEDIFF ( day , max(palm.EMISSAO), GETDATE())   from Pmp_Agendamento ag, os_palm palm where  ag.id_cont_horas_standard in (select st.id from pmp_cont_horas_standard st where id_Contrato = "+bean.getContratoId()+") and palm.ID_AGENDAMENTO = ag.ID");
							if(query.getResultList().size() > 0 && query.getResultList().get(0) != null){
								Integer dias = (Integer)query.getSingleResult();
								mediaDiasProximaRevisao = mediaDiasProximaRevisao - dias;
							}else{
								query = manager.createNativeQuery(" select DATEDIFF ( day , max(c.data_aceite), GETDATE())   from Pmp_contrato  c where  c.id = "+bean.getContratoId());
								Integer dias = (Integer)query.getSingleResult();
								mediaDiasProximaRevisao = mediaDiasProximaRevisao -dias;
							}

							Long diasProximaRevisao = Math.round(mediaDiasProximaRevisao);
							Long proximaRevisao = Math.round(mediaDiasProximaRevisao);
							bean.setMediaDiasProximaRevisao(proximaRevisao.toString());
						}
					}
					
					
					
					
					//Set Horas Pendentes e Proxima Revisão
					Long horasPendentes = proximaManutencao - bean.getHorimetro().longValue();
					bean.setHorasPendentes(horasPendentes.toString());	
					bean.setProximaRevisao(proximaManutencao.toString());	
					
					if(!bean.getSituacao().equals("Vermelho")){
						if(bean.getHorimetro() > proximaManutencao || Integer.parseInt(bean.getHorasPendentes()) <= 50){
							bean.setSituacaoImagem("img/CN.png");
						}else{
							String resultDiasRevisao = findDiasProxRevisao(bean.getNumeroSerie(), manager);
							if(resultDiasRevisao != ""){
								if(Integer.valueOf(resultDiasRevisao) < 0){
									bean.setSituacaoImagem("img/CN.png");									
								}
							}
						}
					}
					
				}else{
					bean.setHorasPendentes("0");					
				}

				if((Integer.parseInt(bean.getHorasPendentes()) <= 50) && filtroHoras50.equals("S") && !bean.getSituacao().equals("Vermelho")){
					op.add(bean);						
				} else if ((filtroUmaPendente.equals("N")) && (filtroPendentes.equals("N")) && (filtroEncerradas.equals("N")) && (filtroHoras50.equals("S"))){
					continue;
				}else {
					op.add(bean);
				}
				
				
				bean.setNomeCliente((String)operacional[11]);				
				bean.setOrigem((String)operacional[13]);				
				bean.setDestino((String)operacional[14]);
				bean.setFilial(((BigDecimal)operacional[15]).toString());
				bean.setSiglaTipoContrato((String)operacional[16]);
				bean.setModelo((String)operacional[1]);				
				
				bean.setTa((String)operacional[17]);
				//Verifica se PL está ativo
				String sqlPlAtivo = "select CASE WHEN count(*) = 0 THEN 'NÃO' ELSE 'SIM' END from pmp_maquina_pl pl"+
									" where pl.numero_serie =:numerSerie"+
									" and pl.latitude is not null"+
									" and pl.longitude is not null";
				Query queryMPlAtivo = manager.createNativeQuery(sqlPlAtivo);
				queryMPlAtivo.setParameter("numerSerie", bean.getNumeroSerie().trim());
				String plAtivo = (String)queryMPlAtivo.getSingleResult();
				bean.setPlAtivo(plAtivo);
				
				//Recupera as manuteções pendentes
				String sqlManutPendentes = "select count(*) from pmp_cont_horas_standard hs where hs.id_contrato =:idContrato and hs.is_executado = 'N'";
				Query queryManutPendentes = manager.createNativeQuery(sqlManutPendentes);
				queryManutPendentes.setParameter("idContrato", bean.getContratoId());
				Integer totalRevPendentes = (Integer)queryManutPendentes.getSingleResult();
				bean.setRevPendentes(totalRevPendentes.toString());
				
				//Recupera o consultor dono do contrato
				String sqlConsultor = "select tw.eplsnm from tw_funcionario tw where  tw.epidno =:idFuncionario ";
				Query queryConsultor = manager.createNativeQuery(sqlConsultor);
				queryConsultor.setParameter("idFuncionario",(String)operacional[12]);
				String consultor = "";
				try {
					consultor = (String)queryConsultor.getSingleResult();
				} catch (Exception e) {
					e.printStackTrace();
				}
				bean.setConsultor(consultor);
				
				bean.setCodigoCliente((String)operacional[8]);					
//				//Busca o Horímetro da Máquina
//				String sqlHorimetro =  " select horimetro, data_atualizacao from pmp_maquina_pl pl where pl.numero_serie = :numeroSerie" +
//                                       " and pl.horimetro is not null and pl.data_atualizacao = (select max(data_atualizacao) from pmp_maquina_pl" + 
//                                       " where numero_serie = pl.numero_serie and horimetro is not null)" ;
//				Query queryHorimetro  = manager.createNativeQuery(sqlHorimetro );
//				queryHorimetro.setParameter("numeroSerie", bean.getNumeroSerie());
//				List<Object[]> resultHorimetro = queryHorimetro.getResultList();
//				
//				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//				
//				if(resultHorimetro.size() >= 1){
//					Object [] par = resultHorimetro.get(0);
//					bean.setHorimetro(((BigDecimal)par[0]).intValue());
//					bean.setDataAtualizacaoHorimetro(format.format((Date)par[1]));
//					bean.setDataAtualizacaoHori((Date)par[1]);
//					//Horas p/ revisões		
//					String sqlMaiores = "select horasManutencao from PmpContHorasStandard where idContrato.id = :contratoId and isExecutado = 'N'order by horasManutencao " ;					  
//					Query queryHorasMaiores = manager.createQuery(sqlMaiores);
//					queryHorasMaiores.setParameter("contratoId", bean.getContratoId());
//					Long proximaManutencao = 0l;
//					if(queryHorasMaiores.getResultList().size() > 0){
//						proximaManutencao = (Long)queryHorasMaiores.getResultList().get(0);
//					}
//					
//					//Set Horas Pendentes e Proxima Revisão
//					Long horasPendentes = proximaManutencao - bean.getHorimetro().longValue();
//					bean.setHorasPendentes(horasPendentes.toString());	
//					bean.setProximaRevisao(proximaManutencao.toString());												
//				}else{
//					bean.setHorasPendentes("0");					
//				}
				
				//Busca a OS se existir				  
				Query queryNumOs = manager.createQuery("select numOs from PmpConfigOperacional Where idContrato.id = :idContrato");
				queryNumOs.setParameter("idContrato", bean.getContratoId());
				List<String> resultNumOs = queryNumOs .getResultList();
				if(resultNumOs.size() > 0){
					bean.setNumOs(resultNumOs.get(0));					
				}				
				
				bean.setNumeroContrato((String)operacional[6]);
				if(operacional[7] != null){
					
					Date date = format.parse((String)operacional[7]);					
					bean.setDataContrato((format2.format(date)).replaceAll("-", "/"));					
				}		
				
//				//Define a situação do Contrato
//				String sqlSituacao = "select count(id) from PmpContHorasStandard where idContrato.id = :contratoId " ;					  
//				Query querySituacao = manager.createQuery(sqlSituacao);
//				querySituacao.setParameter("contratoId", bean.getContratoId());
//				Long situacao = (Long)querySituacao.getResultList().get(0);
//				
//				String sqlSituacaoExe = "select count(id) from PmpContHorasStandard where idContrato.id = :contratoId and isExecutado = 'S'" ;					  
//				Query querySituacaoExe = manager.createQuery(sqlSituacaoExe);
//				querySituacaoExe.setParameter("contratoId", bean.getContratoId());
//				Long situacaoExe = (Long)querySituacaoExe.getResultList().get(0);
//				
//				if(situacao.longValue() == situacaoExe.longValue()){					
//					bean.setSituacao("Vermelho");
//					bean.setSituacaoImagem("img/CN.png");
//				}else if((situacao - situacaoExe) == 1){
//					bean.setSituacao("Amarelo");
//					bean.setSituacaoImagem("img/AM.png");
//				}else{
//					bean.setSituacao("Verde");
//					bean.setSituacaoImagem("img/CE.png");
//				}
//				if(operacional[9] == null){
//					continue;
//				}
				
				bean.setValorContrato((String.valueOf(ValorMonetarioHelper.formata("###,###,##0.00", Double.valueOf(String.valueOf(((BigDecimal)operacional[9])))))));
				bean.setNumeroParcela(((BigDecimal)operacional[10]).toString());
				
//				if(filtroEncerradas.equals("N" )&& filtroPendentes.equals("N") && filtroUmaPendente.equals("N") && filtroHoras50.equals("N") ){
//					op.add(bean);				
//				}else{
//					if(bean.getSituacao().equals("Vermelho") && filtroEncerradas.equals("S")){
//						op.add(bean);					
//					}
//					if(bean.getSituacao().equals("Amarelo") && filtroUmaPendente.equals("S")){
//						op.add(bean);					
//					}
//					if(bean.getSituacao().equals("Verde") && filtroPendentes.equals("S")){
//						op.add(bean);
//					}
//					if((Integer.parseInt(bean.getHorasPendentes()) <= 50) && filtroHoras50.equals("S")){
//						op.add(bean);						
//					}
//				}				
			}	

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		//Collections.sort(op);
		if(ordenarHorimetro != null){
			if(ordenarHorimetro.equals("C")){
				Collections.sort(op, new OrderHrPendentesAsc());
			}else{
				Collections.sort(op, new OrderHorimetroDesc());
			}
		}
		if(ordenarData != null){
			if(ordenarData.equals("C")){
				Collections.sort(op, new OrderDateAsc());
			}else{
				Collections.sort(op, new OrderDateDesc());
			}
		}
//		for (OperacionalBean operacionalBean : op) {
//			operacionalBean.setNumLinhas(Long.valueOf(op.size()));
//		}
		
		return op;
	}
	public List<OperacionalBean> findAllOperacionalByFiltroPlus(String filtroUmaPendente, String filtroPendentes, String filtroEncerradas, String filtroHoras50,
			String ordenarHorimetro, String ordenarData, Long inicial, Long numRegistros, String campoPesquisa){
		
		String SQL = "select C.id, C.modelo, C.prefixo, C.numero_Serie, C.horimetro, C.cidade, " +
		" C.numero_Contrato, C.data_Aceite, C.codigo_cliente, C.valo_contrato, C.qtd_parcelas,C.Razao_Social, c.id_funcionario, " +
		" (select f.stnm from tw_filial f where f.stno = c.filial) origem,"+     
		" (select f.stnm from tw_filial f where f.stno = co.filial) destino," +
		" C.filial," +
		" (select t.sigla from pmp_tipo_contrato t where t.id = C.id_tipo_contrato)," +
		" CASE WHEN c.ta = 'S' THEN 'SIM' ELSE 'NÃO' END ta," +
		"(select count(id) from Pmp_Cont_Horas_Standard_plus where id_Contrato = C.id) situacao, "+
		"(select count(id) from Pmp_Cont_Horas_Standard_plus where id_Contrato = C.id and is_Executado = 'S') situacaoExe, " +
		" CO.cod_Erro_Dbs, " +
		" (select DATEDIFF(DAY, GETDATE(), DATA_REVISAO) from Pmp_Cont_Horas_Standard_plus where id = (select min(id) from Pmp_Cont_Horas_Standard_plus where id_Contrato = C.id and is_Executado = 'N')) diasProxRevisao," +
		" (select sigla from PMP_CLASSIFICACAO_CONTRATO where ID = C.id_classificacao_contrato ) siglaClassificacaoContrato, C.MEDIA_HORAS_MES, C.POSSUI_HORAS_MES  " +
		"from Pmp_Contrato C inner join Pmp_Config_Operacional CO on CO.id_Contrato = C.id " +
		"where UPPER(c.familia) NOT LIKE '%STANDBY%' AND C.id_Status_Contrato = (select S.id from Pmp_Status_Contrato S where S.sigla = 'CA') " +
		" and c.id_classificacao_contrato in (select id from PMP_CLASSIFICACAO_CONTRATO where sigla = 'PLUS')";
		
		//if(!ADM){
		SQL +="and C.filial = :filial ";
		//}
		
		if(campoPesquisa.equals("")){
			if ((filtroUmaPendente.equals("S")) && (filtroPendentes.equals("S")) && (filtroEncerradas.equals("N")) && (filtroHoras50.equals("S"))){
				SQL +=" and (select count(id) from Pmp_Cont_Horas_Standard_plus where id_Contrato = C.id and is_Executado = 'S') >= 0 " +
				" and (select count(id) from Pmp_Cont_Horas_Standard_plus where id_Contrato = C.id and is_Executado = 'N') > 0 ";
			}else if (filtroUmaPendente.equals("S")){
				SQL +=" and (select count(id) from Pmp_Cont_Horas_Standard_plus where id_Contrato = C.id and is_Executado = 'N') = 1";
			}else if (filtroPendentes.equals("S")){
				SQL +=" and (select count(id) from Pmp_Cont_Horas_Standard_plus where id_Contrato = C.id and is_Executado = 'N') >= 1";
			}else if (filtroEncerradas.equals("S")){
				SQL +=" and (select count(id) from Pmp_Cont_Horas_Standard_plus where id_Contrato = C.id and is_Executado = 'N') = 0";
			} 
		}
		
		if(!campoPesquisa.equals("")){
			SQL += "and  (C.MODELO like '%"+campoPesquisa+"%' or C.NUMERO_SERIE like '%"+campoPesquisa+"%'"+  
			" or C.RAZAO_SOCIAL like '%"+campoPesquisa+"%' or CO.NUM_OS like '%"+campoPesquisa+"%' or C.NUMERO_CONTRATO like '%"+campoPesquisa+"%')";
		}
		EntityManager manager = null;
		List<OperacionalBean> op = new ArrayList<OperacionalBean>();
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createNativeQuery(SQL);
			//if(!ADM){
			query.setParameter("filial", Integer.parseInt(FILIAL));
			//}
			Integer tamanhoTotalLista = query.getResultList().size(); // Total de itens da consulta para a paginação.
			
			query.setFirstResult(inicial.intValue());
			query.setMaxResults(numRegistros.intValue());
			
			List<Object[]> result = query.getResultList();
			
			for (Object [] operacional : result) {
				if(operacional[9] == null){
					continue;
				}
				OperacionalBean bean = new OperacionalBean();
				
				bean.setContratoId(((BigDecimal)operacional[0]).longValue());
				bean.setNumLinhas(tamanhoTotalLista.longValue());
				bean.setNumeroSerie((String)operacional[3]);
				bean.setCodErroDbs((String)operacional[20]);
				//bean.setDiasProximaRevisao(findDiasProxRevisao(bean.getNumeroSerie(), manager));
				bean.setDiasProximaRevisao(((Integer)operacional[21]).toString());
				bean.setSiglaClassificacaoContrato((String)operacional[22]);
				//Define a situação do Contrato
//				String sqlSituacao = "select count(id) from PmpContHorasStandard where idContrato.id = :contratoId " ;					  
//				Query querySituacao = manager.createQuery(sqlSituacao);
//				querySituacao.setParameter("contratoId", bean.getContratoId());
//				Long situacao = (Long)querySituacao.getResultList().get(0);
				Long situacao = (((Integer)operacional[18]).longValue());
				
//				String sqlSituacaoExe = "select count(id) from PmpContHorasStandard where idContrato.id = :contratoId and isExecutado = 'S'" ;					  
//				Query querySituacaoExe = manager.createQuery(sqlSituacaoExe);
//				querySituacaoExe.setParameter("contratoId", bean.getContratoId());
//				Long situacaoExe = (Long)querySituacaoExe.getResultList().get(0);
				Long situacaoExe = ((Integer)operacional[19]).longValue();
				
				if(situacao.longValue() == situacaoExe.longValue()){					
					bean.setSituacao("Vermelho");
					bean.setSituacaoImagem("img/CN.png");
				}else if((situacao - situacaoExe) == 1){
					bean.setSituacao("Amarelo");
					bean.setSituacaoImagem("img/AM.png");
				}else{
					bean.setSituacao("Verde");
					bean.setSituacaoImagem("img/CE.png");
				}
				
				//Busca o Horímetro da Máquina
				String sqlHorimetro =  " select horimetro, data_atualizacao from pmp_maquina_pl pl where pl.numero_serie = '"+bean.getNumeroSerie()+"'"+
				" and pl.horimetro is not null and pl.id = (select max(id) from pmp_maquina_pl" + 
				" where numero_serie = pl.numero_serie and horimetro is not null and horimetro > 0)" ;
				Query queryHorimetro  = manager.createNativeQuery(sqlHorimetro );
				//queryHorimetro.setParameter("numeroSerie", bean.getNumeroSerie());
				List<Object[]> resultHorimetro = queryHorimetro.getResultList();
				
				//SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
				
				if(resultHorimetro.size() >= 1){
					
					Object [] par = resultHorimetro.get(0);
					
					if(par[1] != null){
						Date date = format.parse((String)par[1]);
						
						bean.setDataAtualizacaoHorimetro((format2.format(date)).replaceAll("-", "/"));
						bean.setDataAtualizacaoHori(date);						
					}
					bean.setHorimetro(((BigDecimal)par[0]).intValue());
					
//					//Horas p/ revisões		
//					String sqlMaiores = "select horas_Manutencao from Pmp_Cont_Horas_Standard where id_Contrato = :contratoId and is_Executado = 'N'order by horas_Manutencao " ;					  
//					Query queryHorasMaiores = manager.createNativeQuery(sqlMaiores);
//					queryHorasMaiores.setParameter("contratoId", bean.getContratoId());
//					Long proximaManutencao = 0l;
//					if(queryHorasMaiores.getResultList().size() > 0){
//						proximaManutencao = ((BigDecimal)queryHorasMaiores.getResultList().get(0)).longValue();
//					}
					
					//Set Horas Pendentes e Proxima Revisão
//					Long horasPendentes = proximaManutencao - bean.getHorimetro().longValue();
//					bean.setHorasPendentes(horasPendentes.toString());	
//					bean.setProximaRevisao(proximaManutencao.toString());	
//					
//					if(!bean.getSituacao().equals("Vermelho")){
//						if(bean.getHorimetro() > proximaManutencao || Integer.parseInt(bean.getHorasPendentes()) <= 50){
//							bean.setSituacaoImagem("img/CN.png");
//						}else{
//							String resultDiasRevisao = findDiasProxRevisao(bean.getNumeroSerie(), manager);
//							if(resultDiasRevisao != ""){
//								if(Integer.valueOf(resultDiasRevisao) < 0){
//									bean.setSituacaoImagem("img/CN.png");									
//								}
//							}
//						}
//					}
					
				}else{
					bean.setHorasPendentes("0");					
				}
				
//				if((Integer.parseInt(bean.getHorasPendentes()) <= 50) && filtroHoras50.equals("S") && !bean.getSituacao().equals("Vermelho")){
//					op.add(bean);						
//				} else if ((filtroUmaPendente.equals("N")) && (filtroPendentes.equals("N")) && (filtroEncerradas.equals("N")) && (filtroHoras50.equals("S"))){
//					continue;
//				}else {
//					op.add(bean);
//				}
				
				
				bean.setNomeCliente((String)operacional[11]);				
				bean.setOrigem((String)operacional[13]);				
				bean.setDestino((String)operacional[14]);
				bean.setFilial(((BigDecimal)operacional[15]).toString());
				bean.setSiglaTipoContrato((String)operacional[16]);
				bean.setModelo((String)operacional[1]);				
				
				bean.setTa((String)operacional[17]);
				//Verifica se PL está ativo
				String sqlPlAtivo = "select CASE WHEN count(*) = 0 THEN 'NÃO' ELSE 'SIM' END from pmp_maquina_pl pl"+
				" where pl.numero_serie =:numerSerie"+
				" and pl.latitude is not null"+
				" and pl.longitude is not null";
				Query queryMPlAtivo = manager.createNativeQuery(sqlPlAtivo);
				queryMPlAtivo.setParameter("numerSerie", bean.getNumeroSerie().trim());
				String plAtivo = (String)queryMPlAtivo.getSingleResult();
				bean.setPlAtivo(plAtivo);
				
				//Recupera as manuteções pendentes
				String sqlManutPendentes = "select count(*) from pmp_cont_horas_standard_plus hs where hs.id_contrato =:idContrato and hs.is_executado = 'N'";
				Query queryManutPendentes = manager.createNativeQuery(sqlManutPendentes);
				queryManutPendentes.setParameter("idContrato", bean.getContratoId());
				Integer totalRevPendentes = (Integer)queryManutPendentes.getSingleResult();
				bean.setRevPendentes(totalRevPendentes.toString());
				
				//Recupera o consultor dono do contrato
				String sqlConsultor = "select tw.eplsnm from tw_funcionario tw where  tw.epidno =:idFuncionario ";
				Query queryConsultor = manager.createNativeQuery(sqlConsultor);
				queryConsultor.setParameter("idFuncionario",(String)operacional[12]);
				String consultor = "";
				try {
					consultor = (String)queryConsultor.getSingleResult();
				} catch (Exception e) {
					e.printStackTrace();
				}
				bean.setConsultor(consultor);
				
				bean.setCodigoCliente((String)operacional[8]);					
//				//Busca o Horímetro da Máquina
//				String sqlHorimetro =  " select horimetro, data_atualizacao from pmp_maquina_pl pl where pl.numero_serie = :numeroSerie" +
//                                       " and pl.horimetro is not null and pl.data_atualizacao = (select max(data_atualizacao) from pmp_maquina_pl" + 
//                                       " where numero_serie = pl.numero_serie and horimetro is not null)" ;
//				Query queryHorimetro  = manager.createNativeQuery(sqlHorimetro );
//				queryHorimetro.setParameter("numeroSerie", bean.getNumeroSerie());
//				List<Object[]> resultHorimetro = queryHorimetro.getResultList();
//				
//				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//				
//				if(resultHorimetro.size() >= 1){
//					Object [] par = resultHorimetro.get(0);
//					bean.setHorimetro(((BigDecimal)par[0]).intValue());
//					bean.setDataAtualizacaoHorimetro(format.format((Date)par[1]));
//					bean.setDataAtualizacaoHori((Date)par[1]);
//					//Horas p/ revisões		
//					String sqlMaiores = "select horasManutencao from PmpContHorasStandard where idContrato.id = :contratoId and isExecutado = 'N'order by horasManutencao " ;					  
//					Query queryHorasMaiores = manager.createQuery(sqlMaiores);
//					queryHorasMaiores.setParameter("contratoId", bean.getContratoId());
//					Long proximaManutencao = 0l;
//					if(queryHorasMaiores.getResultList().size() > 0){
//						proximaManutencao = (Long)queryHorasMaiores.getResultList().get(0);
//					}
//					
//					//Set Horas Pendentes e Proxima Revisão
//					Long horasPendentes = proximaManutencao - bean.getHorimetro().longValue();
//					bean.setHorasPendentes(horasPendentes.toString());	
//					bean.setProximaRevisao(proximaManutencao.toString());												
//				}else{
//					bean.setHorasPendentes("0");					
//				}
				
				//Busca a OS se existir				  
				Query queryNumOs = manager.createQuery("select numOs from PmpConfigOperacional Where idContrato.id = :idContrato");
				queryNumOs.setParameter("idContrato", bean.getContratoId());
				List<String> resultNumOs = queryNumOs .getResultList();
				if(resultNumOs.size() > 0){
					bean.setNumOs(resultNumOs.get(0));					
				}				
				
				bean.setNumeroContrato((String)operacional[6]);
				if(operacional[7] != null){
					
					Date date = format.parse((String)operacional[7]);					
					bean.setDataContrato((format2.format(date)).replaceAll("-", "/"));					
				}		
				
//				//Define a situação do Contrato
//				String sqlSituacao = "select count(id) from PmpContHorasStandard where idContrato.id = :contratoId " ;					  
//				Query querySituacao = manager.createQuery(sqlSituacao);
//				querySituacao.setParameter("contratoId", bean.getContratoId());
//				Long situacao = (Long)querySituacao.getResultList().get(0);
//				
//				String sqlSituacaoExe = "select count(id) from PmpContHorasStandard where idContrato.id = :contratoId and isExecutado = 'S'" ;					  
//				Query querySituacaoExe = manager.createQuery(sqlSituacaoExe);
//				querySituacaoExe.setParameter("contratoId", bean.getContratoId());
//				Long situacaoExe = (Long)querySituacaoExe.getResultList().get(0);
//				
//				if(situacao.longValue() == situacaoExe.longValue()){					
//					bean.setSituacao("Vermelho");
//					bean.setSituacaoImagem("img/CN.png");
//				}else if((situacao - situacaoExe) == 1){
//					bean.setSituacao("Amarelo");
//					bean.setSituacaoImagem("img/AM.png");
//				}else{
//					bean.setSituacao("Verde");
//					bean.setSituacaoImagem("img/CE.png");
//				}
//				if(operacional[9] == null){
//					continue;
//				}
				
				bean.setValorContrato((String.valueOf(ValorMonetarioHelper.formata("###,###,##0.00", Double.valueOf(String.valueOf(((BigDecimal)operacional[9])))))));
				bean.setNumeroParcela(((BigDecimal)operacional[10]).toString());
				op.add(bean);
//				if(filtroEncerradas.equals("N" )&& filtroPendentes.equals("N") && filtroUmaPendente.equals("N") && filtroHoras50.equals("N") ){
//					op.add(bean);				
//				}else{
//					if(bean.getSituacao().equals("Vermelho") && filtroEncerradas.equals("S")){
//						op.add(bean);					
//					}
//					if(bean.getSituacao().equals("Amarelo") && filtroUmaPendente.equals("S")){
//						op.add(bean);					
//					}
//					if(bean.getSituacao().equals("Verde") && filtroPendentes.equals("S")){
//						op.add(bean);
//					}
//					if((Integer.parseInt(bean.getHorasPendentes()) <= 50) && filtroHoras50.equals("S")){
//						op.add(bean);						
//					}
//				}				
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		//Collections.sort(op);
		if(ordenarHorimetro != null){
			if(ordenarHorimetro.equals("C")){
				Collections.sort(op, new OrderHrPendentesAsc());
			}else{
				Collections.sort(op, new OrderHorimetroDesc());
			}
		}
		if(ordenarData != null){
			if(ordenarData.equals("C")){
				Collections.sort(op, new OrderDateAsc());
			}else{
				Collections.sort(op, new OrderDateDesc());
			}
		}
//		for (OperacionalBean operacionalBean : op) {
//			operacionalBean.setNumLinhas(Long.valueOf(op.size()));
//		}
		
		return op;
	}
	
	/**
	 * Retorna quantos dias faltam para a proxíma revisão.
	 * @param numSerie
	 * @param manager
	 * @return
	 */	
	public String findDiasProxRevisao(String numSerie, EntityManager manager){

		try {

			Query query = manager.createNativeQuery("select mm.MESES_MANUTENCAO from PMP_MESES_MANUTENCAO mm,"+
					"(select DISTINCT a.ID_FAMILIA, a.ID_ARV from PMP_CONTRATO c, ARV_INSPECAO a"+
					" where c.MODELO = a.DESCRICAO"+ 
					" and c.NUMERO_SERIE = '"+numSerie+"') fm"+
					" WHERE fm.ID_ARV = mm.ID_MODELO"+
					" and fm.ID_FAMILIA = mm.ID_FAMILIA");
			
			BigDecimal resultMeses = (BigDecimal) query.getSingleResult();			
			
			if(resultMeses != BigDecimal.ZERO){			
				int diasMes = resultMeses.intValue() * MES;
				
				query = manager.createNativeQuery("select DATEDIFF(DD,MAX(ag.DATA_AGENDAMENTO), GETDATE()) from PMP_AGENDAMENTO ag,"+ 
						"(select hs.* from PMP_CONTRATO c, PMP_CONFIG_OPERACIONAL op, PMP_CONT_HORAS_STANDARD hs"+
						" where c.NUMERO_SERIE = '"+numSerie+"'"+
						" and c.ID = op.ID_CONTRATO"+
						" and hs.ID_CONTRATO = c.id"+
						" and c.is_ativo is null"+
						" and IS_EXECUTADO = 'S') hsaux"+
						" where ag.ID_CONT_HORAS_STANDARD = hsaux.ID");
			
			Integer resultDias = (Integer) query.getSingleResult();
			
				if(resultDias == null){
				
				query = manager.createNativeQuery("select DATEDIFF(DD,MAX(c.DATA_ACEITE), GETDATE()) from PMP_CONTRATO c where c.NUMERO_SERIE = '"+numSerie+"' and c.IS_ATIVO is null");
				
				resultDias = (Integer) query.getSingleResult();
				
				}
				
				if(resultDias != null){
					return String.valueOf(resultDias = diasMes - resultDias);					
				}
				
			}
		} catch (NoResultException nre) {
			// TODO: handle exception					
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public List<OperacionalBean> findAllOperacionalByFiltroGerador(String filtroUmaPendente, String filtroPendentes, String filtroEncerradas, String filtroHoras50, String ordenarHorimetro, String ordenarData){
		
		String SQL = "SELECT c.id, c.modelo, c.prefixo, c.numero_serie, c.horimetro, c.cidade, c.numero_contrato, c.data_aceite, c.codigo_cliente, c.valo_contrato, c.qtd_parcelas,c.razao_social, c.id_funcionario, " +
				" (SELECT f.stnm from tw_filial f WHERE f.stno = c.filial) origem,"+     
				" (SELECT f.stnm from tw_filial f WHERE f.stno = co.filial) destino," +
				" c.filial," +
				" (SELECT t.sigla FROM pmp_tipo_contrato t WHERE t.id = c.id_tipo_contrato)," +
				" DECODE(c.ta, 'S', 'SIM','NÃO') ta " +
				"FROM pmp_contrato c LEFT JOIN pmp_config_operacional co ON co.id_contrato = c.id " +
				"WHERE UPPER(c.familia) LIKE '%STANDBY%' AND c.id_status_contrato = (SELECT s.id FROM pmp_status_contrato s WHERE s.sigla = 'CA') ";
		
		//if(!ADM){
		SQL +="and C.filial = :filial ";
		//}
		
		
		EntityManager manager = null;
		List<OperacionalBean> op = new ArrayList<OperacionalBean>();
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createNativeQuery(SQL);
			//if(!ADM){
			query.setParameter("filial", Integer.parseInt(FILIAL));
			//}
			
			List<Object[]> result = query.getResultList();
			
			for (Object [] operacional : result) {
				OperacionalBean bean = new OperacionalBean();
				bean.setContratoId(((BigDecimal)operacional[0]).longValue());
				bean.setNomeCliente((String)operacional[11]);				
				bean.setOrigem((String)operacional[13]);				
				bean.setDestino((String)operacional[14]);
				bean.setFilial(((BigDecimal)operacional[15]).toString());
				bean.setSiglaTipoContrato((String)operacional[16]);
				bean.setModelo((String)operacional[1]);				
				bean.setNumeroSerie((String)operacional[3]);
				bean.setTa((String)operacional[17]);
				//Verifica se PL está ativo
				String sqlPlAtivo = "select decode(count(*),0,'NÃO','SIM') from pmp_maquina_pl pl"+
						" where pl.numero_serie =:numerSerie"+
						" and pl.latitude is not null"+
						" and pl.longitude is not null";
				Query queryMPlAtivo = manager.createNativeQuery(sqlPlAtivo);
				queryMPlAtivo.setParameter("numerSerie", bean.getNumeroSerie().trim());
				String plAtivo = (String)queryMPlAtivo.getSingleResult();
				bean.setPlAtivo(plAtivo);
				
				//Recupera as manuteções pendentes
				String sqlManutPendentes = "select count(*) from pmp_cont_meses_standard hs where hs.id_contrato =:idContrato and hs.is_executado = 'N'";
				Query queryManutPendentes = manager.createNativeQuery(sqlManutPendentes);
				queryManutPendentes.setParameter("idContrato", bean.getContratoId());
				BigDecimal totalRevPendentes = (BigDecimal)queryManutPendentes.getSingleResult();
				bean.setRevPendentes(totalRevPendentes.toString());
				
				//Recupera o consultor dono do contrato
				String sqlConsultor = "select tw.eplsnm from tw_funcionario tw where  tw.epidno =:idFuncionario ";
				Query queryConsultor = manager.createNativeQuery(sqlConsultor);
				queryConsultor.setParameter("idFuncionario",(String)operacional[12]);
				String consultor = "";
				try {
					consultor = (String)queryConsultor.getSingleResult();
				} catch (Exception e) {
					e.printStackTrace();
				}
				bean.setConsultor(consultor);
				
				bean.setCodigoCliente((String)operacional[8]);					
				//Busca o Horímetro da Máquina
				String sqlHorimetro =  " select horimetro, data_atualizacao from pmp_maquina_pl pl where pl.numero_serie = :numeroSerie" +
						" and pl.horimetro is not null and pl.data_atualizacao = (select max(data_atualizacao) from pmp_maquina_pl" + 
						" where numero_serie = pl.numero_serie and horimetro is not null)" ;
				Query queryHorimetro  = manager.createNativeQuery(sqlHorimetro );
				queryHorimetro.setParameter("numeroSerie", bean.getNumeroSerie());
				List<Object[]> resultHorimetro = queryHorimetro.getResultList();
				
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				
				if(resultHorimetro.size() >= 1){
					Object [] par = resultHorimetro.get(0);
					bean.setHorimetro(((BigDecimal)par[0]).intValue());
					bean.setDataAtualizacaoHorimetro(format.format((Date)par[1]));
					bean.setDataAtualizacaoHori((Date)par[1]);
					//Horas p/ revisões		
					//String sqlMaiores = "select mesManutencao from PmpContMesesStandard where idContrato.id = :contratoId and isExecutado = 'N'order by mesManutencao " ;					  
					
					String sql = "SELECT cms.mes manutencao, TO_DATE(((cms.mes * cms.frequencia) + c.data_aceite), 'DD/MM/YYYY') - (TO_DATE(SYSDATE, 'DD/MM/YYYY')) AS dias FROM pmp_contrato c " +
								"INNER JOIN pmp_cont_meses_standard cms ON cms.id_contrato = c.id " +
								"WHERE cms.id_contrato = " + bean.getContratoId() + " AND cms.is_executado = 'N' AND ROWNUM = 1 " +
								"ORDER BY cms.mes " ;
					
					Query query1 = manager.createNativeQuery(sql);
					
					Object[] rs = (Object[]) query1.getSingleResult();
					
					//Set Horas Pendentes e Proxima Revisão
					bean.setHorasPendentes(rs[1].toString());	
					bean.setProximaRevisao(rs[0].toString());												
				}else{
					bean.setHorasPendentes("0");					
				}
				
				//Busca a OS se existir				  
				Query queryNumOs = manager.createQuery("select numOs from PmpConfigOperacional Where idContrato.id = :idContrato");
				queryNumOs.setParameter("idContrato", bean.getContratoId());
				List<String> resultNumOs = queryNumOs .getResultList();
				if(resultNumOs.size() > 0){
					bean.setNumOs(resultNumOs.get(0));					
				}				
				
				bean.setNumeroContrato((String)operacional[6]);
				if(operacional[7] != null){
					bean.setDataContrato(format.format((Date)operacional[7]));					
				}		
				
				//Define a situação do Contrato
				String sqlSituacao = "select count(id) from PmpContMesesStandard where idContrato.id = :contratoId " ;					  
				Query querySituacao = manager.createQuery(sqlSituacao);
				querySituacao.setParameter("contratoId", bean.getContratoId());
				Long situacao = (Long)querySituacao.getResultList().get(0);
				
				String sqlSituacaoExe = "select count(id) from PmpContMesesStandard where idContrato.id = :contratoId and isExecutado = 'S'" ;					  
				Query querySituacaoExe = manager.createQuery(sqlSituacaoExe);
				querySituacaoExe.setParameter("contratoId", bean.getContratoId());
				Long situacaoExe = (Long)querySituacaoExe.getResultList().get(0);
				
				if(situacao.longValue() == situacaoExe.longValue()){					
					bean.setSituacao("Vermelho");
					bean.setSituacaoImagem("img/CN.png");
				}else if((situacao - situacaoExe) == 1){
					bean.setSituacao("Amarelo");
					bean.setSituacaoImagem("img/AM.png");
				}else{
					bean.setSituacao("Verde");
					bean.setSituacaoImagem("img/CE.png");
				}
				if(operacional[9] == null){
					continue;
				}
				
				bean.setValorContrato((String.valueOf(ValorMonetarioHelper.formata("###,###,##0.00", Double.valueOf(String.valueOf(((BigDecimal)operacional[9])))))));
				bean.setNumeroParcela(((BigDecimal)operacional[10]).toString());
				
				if(filtroEncerradas.equals("N" )&& filtroPendentes.equals("N") && filtroUmaPendente.equals("N") ){
					op.add(bean);				
				}else{
					if(bean.getSituacao().equals("Vermelho") && filtroEncerradas.equals("S")){
						op.add(bean);					
					}
					if(bean.getSituacao().equals("Amarelo") && filtroUmaPendente.equals("S")){
						op.add(bean);					
					}
					if(bean.getSituacao().equals("Verde") && filtroPendentes.equals("S")){
						op.add(bean);
					}
				}				
				/*if(filtroEncerradas.equals("N" )&& filtroPendentes.equals("N") && filtroUmaPendente.equals("N") && filtroHoras50.equals("N") ){
					op.add(bean);				
				}else{
					if(bean.getSituacao().equals("Vermelho") && filtroEncerradas.equals("S")){
						op.add(bean);					
					}
					if(bean.getSituacao().equals("Amarelo") && filtroUmaPendente.equals("S")){
						op.add(bean);					
					}
					if(bean.getSituacao().equals("Verde") && filtroPendentes.equals("S")){
						op.add(bean);
					}
					if((Integer.parseInt(bean.getHorasPendentes()) <= 50) && filtroHoras50.equals("S")){
						op.add(bean);						
					}
				}*/			
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		Collections.sort(op);
		if(ordenarHorimetro != null){
			if(ordenarHorimetro.equals("C")){
				Collections.sort(op, new OrderHrPendentesAsc());
			}else{
				Collections.sort(op, new OrderHorimetroDesc());
			}
		}
		if(ordenarData != null){
			if(ordenarData.equals("C")){
				Collections.sort(op, new OrderDateAsc());
			}else{
				Collections.sort(op, new OrderDateDesc());
			}
		}
		
		return op;
	}
}
