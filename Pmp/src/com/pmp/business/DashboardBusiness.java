package com.pmp.business;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.DashboardBean;
import com.pmp.bean.DashboardMaquinasBean;
import com.pmp.bean.FamiliaBean;
import com.pmp.entity.PmpFamilia;
import com.pmp.entity.TwFilial;
import com.pmp.util.ConectionDbs;
import com.pmp.util.ConnectionStratec;
import com.pmp.util.JpaUtil;
import com.pmp.util.ValorMonetarioHelper;

public class DashboardBusiness {



	private static final int DIAS_DO_MES = 21;

	private  DashboardBean totalContratos = new DashboardBean();



	public List<DashboardBean> findAllIndicadoresCliente(Long filial){
		List<DashboardBean> result = new ArrayList<DashboardBean>();
		result.add(this.findContratosMda(filial));
		result.add(this.findContratosRental(filial));
		//result.add(this.findContratosCliente(filial));

		result.add(this.findContratosPartner(filial));
		result.add(this.findContratosCustomer(filial));
		result.add(this.findContratosPremium(filial));
		result.add(this.findContratosPlus(filial));
		this.totalIndicadores(result);

		return result;
	}


	public List<DashboardBean> findAllIndicadoresProdutividade(Long filial){
		List<DashboardBean> result = new ArrayList<DashboardBean>();
		result.add(this.findHorasAlocada(filial));
		result.add(this.findProdTecnico(filial));
		result.add(this.findMediaAtendimento(filial));
		result.add(this.findFaturamentoMensal(filial));
		result.add(this.findBacklog(filial));

		return result;
	}

	public List<DashboardBean> findAllIndicadores(Long filial){
		List<DashboardBean> result = new ArrayList<DashboardBean>();
		result.add(this.findStanderJobPerformance(filial));
		result.add(this.findStanderJobUsage());
		result.add(this.findPMTimeliness(filial));
		result.add(this.findRenewalRate(filial));
		result.add(this.findNovosAtivos(filial));
		result.add(this.findContratosAtivos(filial));
		result.add(this.findContratosRenovados(filial));
		result.add(this.findMediaDeslocamento(filial));
		return result;
	}

	public List<DashboardMaquinasBean> findAllIndicadoresMaquinas(Long filial){
		List<DashboardMaquinasBean> result = new ArrayList<DashboardMaquinasBean>();
		result.add(this.findAllIndicadoresMda(filial));
		result.add(this.findAllIndicadoresRental(filial));
		result.add(this.findAllIndicadoresPartner(filial));
		result.add(this.findAllIndicadoresCustomer(filial));
		result.add(this.findAllIndicadoresPremium(filial));
		result.add(this.findAllIndicadoresPlus(filial));

		return result;
	}

	private void totalIndicadores(List<DashboardBean> result){
		DashboardBean bean = new DashboardBean();
		bean.setOperacao("Total Contratos");
		for (int i = 0; i < result.size(); i++) {
			DashboardBean dashboardBean = result.get(i);
			if(i == 0){
				bean.setMes1(dashboardBean.getMes1());
				bean.setMes2(dashboardBean.getMes2());
				bean.setMes3(dashboardBean.getMes3());
				bean.setMes4(dashboardBean.getMes4());
				bean.setMes5(dashboardBean.getMes5());
				bean.setMes6(dashboardBean.getMes6());
				bean.setMes7(dashboardBean.getMes7());
			}else{

				bean.setMes1( Long.valueOf((Long.valueOf(dashboardBean.getMes1())+Long.valueOf(bean.getMes1()))).toString() );
				bean.setMes2(Long.valueOf((Long.valueOf(dashboardBean.getMes2())+Long.valueOf(bean.getMes2()))).toString());
				bean.setMes3(Long.valueOf((Long.valueOf(dashboardBean.getMes3())+Long.valueOf(bean.getMes3()))).toString());
				bean.setMes4(Long.valueOf((Long.valueOf(dashboardBean.getMes4())+Long.valueOf(bean.getMes4()))).toString());
				bean.setMes5(Long.valueOf((Long.valueOf(dashboardBean.getMes5())+Long.valueOf(bean.getMes5()))).toString());
				bean.setMes6(Long.valueOf((Long.valueOf(dashboardBean.getMes6())+Long.valueOf(bean.getMes6()))).toString());
				bean.setMes7(Long.valueOf((Long.valueOf(dashboardBean.getMes7())+Long.valueOf(bean.getMes7()))).toString());
			}
		}
		result.add(bean);
	}

	/**
	 * Apontado/potencial
	 * @param filial
	 * @return
	 */
	public DashboardBean findHorasAlocada(Long filial ) {
		EntityManager manager = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Horas Alocadas");
			bean.setTarget(" >= 95%");

			DecimalFormat df = new DecimalFormat("0.0");
			Calendar nomeMesCal = Calendar.getInstance();
			conn = ConectionDbs.getConnecton();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}

				String SQL = "select f.login from TW_FUNCIONARIO f, ADM_PERFIL_SISTEMA_USUARIO adm"+
						"	where f.EPIDNO = adm.ID_TW_USUARIO"+
						"	and adm.ID_PERFIL = (select ID from ADM_PERFIL where TIPO_SISTEMA = 'PMP' and SIGLA = 'USUTEC')";
				//"	and month(f.DATA_CADASTRO) = "+mes+""+
				//"	and year(f.DATA_CADASTRO) = " + calendar.get(Calendar.YEAR);


				if(filial != null){
					SQL += " and f.STN1 = "+filial;
				}

				Query query = manager.createNativeQuery(SQL);
				List<String> tecnicosList = (List<String>)query.getResultList();
				String complemento = "";
				for (String login : tecnicosList) {
					complemento += "'"+login+"',";
				}

				SQL = "select sum(ELPHR) as ELPHR from libu17.WOPLABR0"+
						"	where substring(DOCDT8,0,5) = " + nomeMesCal.get(Calendar.YEAR)+
						"	and substring(DOCDT8,5,2) = "+mes;

				if(complemento.length() > 1){
					complemento = complemento.substring(0, complemento.length()-1);
					SQL += " and epidno in ("+complemento+")";
				}
				stmt = conn.createStatement();
				rs = stmt.executeQuery(SQL);
				BigDecimal horasApontadas = BigDecimal.ZERO;
				if(rs.next()){
					horasApontadas = rs.getBigDecimal("ELPHR");
					if(horasApontadas == null){
						horasApontadas = BigDecimal.ZERO;
					}
				}

				Double totalHorasPotencial = 0d;
				SQL = "select count(*) from TW_FUNCIONARIO f, ADM_PERFIL_SISTEMA_USUARIO adm"+
						"	where f.EPIDNO = adm.ID_TW_USUARIO"+
						"	and adm.ID_PERFIL = (select ID from ADM_PERFIL where TIPO_SISTEMA = 'PMP' and SIGLA = 'USUTEC')";
				// "	and month(f.DATA_CADASTRO) = "+mes+""+
				// "	and year(f.DATA_CADASTRO) = " + calendar.get(Calendar.YEAR);


				if(filial != null){
					SQL += " and f.STN1 = "+filial;
				}

				query = manager.createNativeQuery(SQL);
				Integer totalTecnicos = (Integer)query.getSingleResult();
				totalHorasPotencial = totalTecnicos * 8.8 * DIAS_DO_MES;

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasPotencial != null && totalHorasPotencial > 0){
						bean.setMes7(df.format((horasApontadas.doubleValue()/totalHorasPotencial) * 100).replace(",", ".")+"%");
						if(((horasApontadas.doubleValue()/totalHorasPotencial) * 100) >= 95){
							bean.setCor7("green");
						}
						else{
							bean.setCor7("#d81010");
						}
					}else{
						bean.setMes7("0%");
						bean.setCor7("#d81010");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasPotencial != null && totalHorasPotencial > 0){
						bean.setMes6(df.format((horasApontadas.doubleValue()/totalHorasPotencial) * 100).replace(",", ".")+"%");
						if(((horasApontadas.doubleValue()/totalHorasPotencial) * 100) >= 95){
							bean.setCor6("green");
						}
						else{
							bean.setCor6("#cb0000");
						}

					}else{
						bean.setMes6("0%");
						bean.setCor6("#cb0000");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasPotencial != null && totalHorasPotencial > 0){
						bean.setMes5(df.format((horasApontadas.doubleValue()/totalHorasPotencial) * 100).replace(",", ".")+"%");
						if(((horasApontadas.doubleValue()/totalHorasPotencial) * 100) >= 95){
							bean.setCor5("green");
						}
						else{
							bean.setCor5("#cb0000");
						}


					}else{
						bean.setMes5("0%");
						bean.setCor5("#cb0000");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasPotencial != null && totalHorasPotencial > 0){
						bean.setMes4(df.format((horasApontadas.doubleValue()/totalHorasPotencial) * 100).replace(",", ".")+"%");
						if(((horasApontadas.doubleValue()/totalHorasPotencial) * 100) >= 95){
							bean.setCor4("green");
						}
						else{
							bean.setCor4("#cb0000");
						}

					}else{
						bean.setMes4("0%");
						bean.setCor4("#cb0000");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasPotencial != null && totalHorasPotencial > 0){
						bean.setMes3(df.format((horasApontadas.doubleValue()/totalHorasPotencial) * 100).replace(",", ".")+"%");
						if(((horasApontadas.doubleValue()/totalHorasPotencial) * 100) >= 95){
							bean.setCor3("green");
						}
						else{
							bean.setCor3("#cb0000");
						}
					}else{
						bean.setMes3("0%");
						bean.setCor3("#cb0000");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasPotencial != null && totalHorasPotencial > 0){
						bean.setMes2(df.format((horasApontadas.doubleValue()/totalHorasPotencial) * 100).replace(",", ".")+"%");
						if(((horasApontadas.doubleValue()/totalHorasPotencial) * 100) >= 95){
							bean.setCor2("green");
						}
						else{
							bean.setCor2("#cb0000");
						}
					}else{
						bean.setMes2("0%");
						bean.setCor2("#cb0000");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasPotencial != null && totalHorasPotencial > 0){
						bean.setMes1(df.format((horasApontadas.doubleValue()/totalHorasPotencial) * 100).replace(",", ".")+"%");
						if(((horasApontadas.doubleValue()/totalHorasPotencial) * 100) >= 95){
							bean.setCor1("green");
						}
						else{
							bean.setCor1("#cb0000");
						}

					}else{
						bean.setMes1("0%");
						bean.setCor1("#cb0000");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
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
		return bean;
	}

	/**
	 * Produtiva/potencial
	 * @param filial
	 * @return
	 */
	public DashboardBean findProdTecnico(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Prod. Técnico");
			bean.setTarget(" >= 70%");

			DecimalFormat df = new DecimalFormat("0.0");
			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				Double totalHorasTrabalhadas = 0d;
				String SQL = "select HORAS_TRABALHO from PMP_APROPRIACAO_HORAS aph, PMP_AGENDAMENTO ag, PMP_CONFIG_OPERACIONAL op, PMP_CONTRATO c"+
						"	where aph.HORAS_TRABALHO <> '0:00'"+
						"	and  month(aph.DATA) = "+mes+
						"	and aph.ID_AGENDAMENTO = ag.ID" +
						" and ag.ID_CONG_OPERACIONAL = op.ID" +
						" and op.ID_CONTRATO = c.ID"+
						" and year(aph.DATA) = " + nomeMesCal.get(Calendar.YEAR);
				if(filial != null){
					SQL += " and ag.FILIAL = "+filial;
				}

				Query query = manager.createNativeQuery(SQL);
				List<String> listHorasTrab = (List<String>)query.getResultList();
				for (String horasTrab : listHorasTrab) {
					String [] horasTrabAux = horasTrab.split(":");
					if(Integer.valueOf(horasTrabAux[1]) > 0){
						Double horasMinutos = Double.valueOf(horasTrabAux[1])/60;
						totalHorasTrabalhadas += Double.valueOf(horasTrabAux[0]) + horasMinutos;
					}else if(Integer.valueOf(horasTrabAux[0]) > 0){
						totalHorasTrabalhadas += Double.valueOf(horasTrabAux[0] +"."+horasTrabAux[1]);
					}
				}


				SQL = "select HORAS_VIAGEM from PMP_APROPRIACAO_HORAS aph, PMP_AGENDAMENTO ag, PMP_CONFIG_OPERACIONAL op, PMP_CONTRATO c"+
						"	where aph.HORAS_VIAGEM <> '0:00'"+
						"	and  month(aph.DATA) = "+mes+
						"	and aph.ID_AGENDAMENTO = ag.ID" +
						" and ag.ID_CONG_OPERACIONAL = op.ID" +
						" and op.ID_CONTRATO = c.ID"+
						" and year(aph.DATA) = " + nomeMesCal.get(Calendar.YEAR);


				if(filial != null){
					SQL += " and ag.FILIAL  = "+filial;
				}

				query = manager.createNativeQuery(SQL);
				listHorasTrab = (List<String>)query.getResultList();
				for (String horasTrab : listHorasTrab) {
					String [] horasTrabAux = horasTrab.split(":");
					if(Integer.valueOf(horasTrabAux[1]) > 0){
						Double horasMinutos = Double.valueOf(horasTrabAux[1])/60;
						totalHorasTrabalhadas += Double.valueOf(horasTrabAux[0]) + horasMinutos;
					}else if(Integer.valueOf(horasTrabAux[0]) > 0){
						totalHorasTrabalhadas += Double.valueOf(horasTrabAux[0] +"."+horasTrabAux[1]);
					}
				}


				Double totalHorasPotencial = 0d;
				SQL = "select count(*) from TW_FUNCIONARIO f, ADM_PERFIL_SISTEMA_USUARIO adm"+
						"	where f.EPIDNO = adm.ID_TW_USUARIO"+
						"	and adm.ID_PERFIL = (select ID from ADM_PERFIL where TIPO_SISTEMA = 'PMP' and SIGLA = 'USUTEC')";
				//"	and month(f.DATA_CADASTRO) = "+mes+""+
				//"	and year(f.DATA_CADASTRO) = " + calendar.get(Calendar.YEAR);


				if(filial != null){
					SQL += " and f.STN1 = "+filial;
				}

				query = manager.createNativeQuery(SQL);
				Integer totalTecnicos = (Integer)query.getSingleResult();
				totalHorasPotencial = totalTecnicos * 8.8 * DIAS_DO_MES;

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasPotencial != null && totalHorasPotencial > 0){
						bean.setMes7(df.format((totalHorasTrabalhadas.doubleValue()/totalHorasPotencial) * 100).replace(",", ".")+"%");
						if(((totalHorasTrabalhadas.doubleValue()/totalHorasPotencial) * 100) >= 70){
							bean.setCor7("green");
						}
						else{
							bean.setCor7("#d81010");
						}
					}else{
						bean.setMes7("0%");
						bean.setCor7("#d81010");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasPotencial != null && totalHorasPotencial > 0){
						bean.setMes6(df.format((totalHorasTrabalhadas.doubleValue()/totalHorasPotencial) * 100).replace(",", ".")+"%");
						if(((totalHorasTrabalhadas.doubleValue()/totalHorasPotencial) * 100) >= 70){
							bean.setCor6("green");
						}
						else{
							bean.setCor6("#cb0000");
						}

					}else{
						bean.setMes6("0%");
						bean.setCor6("#cb0000");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasPotencial != null && totalHorasPotencial > 0){
						bean.setMes5(df.format((totalHorasTrabalhadas.doubleValue()/totalHorasPotencial) * 100).replace(",", ".")+"%");
						if(((totalHorasTrabalhadas.doubleValue()/totalHorasPotencial) * 100) >= 70){
							bean.setCor5("green");
						}
						else{
							bean.setCor5("#cb0000");
						}


					}else{
						bean.setMes5("0%");
						bean.setCor5("#cb0000");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasPotencial != null && totalHorasPotencial > 0){
						bean.setMes4(df.format((totalHorasTrabalhadas.doubleValue()/totalHorasPotencial) * 100).replace(",", ".")+"%");
						if(((totalHorasTrabalhadas.doubleValue()/totalHorasPotencial) * 100) >= 70){
							bean.setCor4("green");
						}
						else{
							bean.setCor4("#cb0000");
						}

					}else{
						bean.setMes4("0%");
						bean.setCor4("#cb0000");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasPotencial != null && totalHorasPotencial > 0){
						bean.setMes3(df.format((totalHorasTrabalhadas.doubleValue()/totalHorasPotencial) * 100).replace(",", ".")+"%");
						if(((totalHorasTrabalhadas.doubleValue()/totalHorasPotencial) * 100) >= 70){
							bean.setCor3("green");
						}
						else{
							bean.setCor3("#cb0000");
						}
					}else{
						bean.setMes3("0%");
						bean.setCor3("#cb0000");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasPotencial != null && totalHorasPotencial > 0){
						bean.setMes2(df.format((totalHorasTrabalhadas.doubleValue()/totalHorasPotencial) * 100).replace(",", ".")+"%");
						if(((totalHorasTrabalhadas.doubleValue()/totalHorasPotencial) * 100) >= 70){
							bean.setCor2("green");
						}
						else{
							bean.setCor2("#cb0000");
						}
					}else{
						bean.setMes2("0%");
						bean.setCor2("#cb0000");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasPotencial != null && totalHorasPotencial > 0){
						bean.setMes1(df.format((totalHorasTrabalhadas.doubleValue()/totalHorasPotencial) * 100).replace(",", ".")+"%");
						if(((totalHorasTrabalhadas.doubleValue()/totalHorasPotencial) * 100) >= 70){
							bean.setCor1("green");
						}
						else{
							bean.setCor1("#cb0000");
						}

					}else{
						bean.setMes1("0%");
						bean.setCor1("#cb0000");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}

	/**
	 * somatório de número de atendimentos por mês/(número de mecânicos X 2)
	 * @param filial
	 * @return
	 */
	public DashboardBean findMediaAtendimento(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Média Atend.");
			bean.setTarget("");

			DecimalFormat df = new DecimalFormat("0.0");
			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				Double totalAtendimentos = 0d;
				String SQL = "select COUNT(*) from PMP_AGENDAMENTO ag"+
						" where ag.ID_STATUS_AGENDAMENTO = (select ID from PMP_STATUS_AGENDAMENTO where SIGLA = 'FIN')"+
						" and month(ag.DATA_AGENDAMENTO) = "+mes+""+
						" and year(ag.DATA_AGENDAMENTO) = " + nomeMesCal.get(Calendar.YEAR);
				if(filial != null){
					SQL += " and ag.FILIAL = "+filial;
				}

				Query query = manager.createNativeQuery(SQL);
				totalAtendimentos = Double.valueOf((Integer)query.getSingleResult());



				Double totalMecanicos = 0d;
				SQL = "select count(*) from TW_FUNCIONARIO f, ADM_PERFIL_SISTEMA_USUARIO adm"+
						"	where f.EPIDNO = adm.ID_TW_USUARIO"+
						"	and adm.ID_PERFIL = (select ID from ADM_PERFIL where TIPO_SISTEMA = 'PMP' and SIGLA = 'USUTEC')";
				//"	and month(f.DATA_CADASTRO) = "+mes+""+
				//"	and year(f.DATA_CADASTRO) = " + calendar.get(Calendar.YEAR);


				if(filial != null){
					SQL += " and f.STN1 = "+filial;
				}

				query = manager.createNativeQuery(SQL);
				Integer totalTecnicos = (Integer)query.getSingleResult();
				totalMecanicos = totalTecnicos.doubleValue() * DIAS_DO_MES;

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalMecanicos != null && totalMecanicos > 0){
						//bean.setMes7(df.format((totalAtendimentos.doubleValue()/totalMecanicos) * 100).replace(",", ".")+"%");
						bean.setMes7(df.format((totalAtendimentos.doubleValue()/totalMecanicos)).replace(",", "."));
						//						if(((totalAtendimentos.doubleValue()/totalMecanicos) * 100) >= 70){
						//							bean.setCor7("green");
						//						}
						//						else{
						//							bean.setCor7("#d81010");
						//						}
					}else{
						//bean.setMes7("0%");
						bean.setMes7("0");
						//bean.setCor7("#d81010");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalMecanicos != null && totalMecanicos > 0){
						//bean.setMes6(df.format((totalAtendimentos.doubleValue()/totalMecanicos) * 100).replace(",", ".")+"%");
						bean.setMes6(df.format((totalAtendimentos.doubleValue()/totalMecanicos)).replace(",", "."));
						//						if(((totalAtendimentos.doubleValue()/totalMecanicos) * 100) >= 70){
						//							bean.setCor6("green");
						//						}
						//						else{
						//							bean.setCor6("#cb0000");
						//						}

					}else{
						//bean.setMes6("0%");
						bean.setMes6("0");
						//bean.setCor6("#cb0000");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalMecanicos != null && totalMecanicos > 0){
						//bean.setMes5(df.format((totalAtendimentos.doubleValue()/totalMecanicos) * 100).replace(",", ".")+"%");
						bean.setMes5(df.format((totalAtendimentos.doubleValue()/totalMecanicos)).replace(",", "."));
						//						if(((totalAtendimentos.doubleValue()/totalMecanicos) * 100) >= 70){
						//							bean.setCor5("green");
						//						}
						//						else{
						//							bean.setCor5("#cb0000");
						//						}


					}else{
						//bean.setMes5("0%");
						bean.setMes5("0");
						//bean.setCor5("#cb0000");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalMecanicos != null && totalMecanicos > 0){
						//bean.setMes4(df.format((totalAtendimentos.doubleValue()/totalMecanicos) * 100).replace(",", ".")+"%");
						bean.setMes4(df.format((totalAtendimentos.doubleValue()/totalMecanicos)).replace(",", "."));
						//						if(((totalAtendimentos.doubleValue()/totalMecanicos) * 100) >= 70){
						//							bean.setCor4("green");
						//						}
						//						else{
						//							bean.setCor4("#cb0000");
						//						}

					}else{
						//bean.setMes4("0%");
						bean.setMes4("0");
						//bean.setCor4("#cb0000");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalMecanicos != null && totalMecanicos > 0){
						//bean.setMes3(df.format((totalAtendimentos.doubleValue()/totalMecanicos) * 100).replace(",", ".")+"%");
						bean.setMes3(df.format((totalAtendimentos.doubleValue()/totalMecanicos)).replace(",", "."));
						//						if(((totalAtendimentos.doubleValue()/totalMecanicos) * 100) >= 70){
						//							bean.setCor3("green");
						//						}
						//						else{
						//							bean.setCor3("#cb0000");
						//						}
					}else{
						//bean.setMes3("0%");
						bean.setMes3("0");
						//bean.setCor3("#cb0000");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalMecanicos != null && totalMecanicos > 0){
						//bean.setMes2(df.format((totalAtendimentos.doubleValue()/totalMecanicos) * 100).replace(",", ".")+"%");
						bean.setMes2(df.format((totalAtendimentos.doubleValue()/totalMecanicos)).replace(",", "."));
						//						if(((totalAtendimentos.doubleValue()/totalMecanicos) * 100) >= 70){
						//							bean.setCor2("green");
						//						}
						//						else{
						//							bean.setCor2("#cb0000");
						//						}
					}else{
						//bean.setMes2("0%");
						bean.setMes2("0");
						//bean.setCor2("#cb0000");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalMecanicos != null && totalMecanicos > 0){
						//bean.setMes1(df.format((totalAtendimentos.doubleValue()/totalMecanicos) * 100).replace(",", ".")+"%");
						bean.setMes1(df.format((totalAtendimentos.doubleValue()/totalMecanicos)).replace(",", "."));
						//						if(((totalAtendimentos.doubleValue()/totalMecanicos) * 100) >= 70){
						//							bean.setCor1("green");
						//						}
						//						else{
						//							bean.setCor1("#cb0000");
						//						}

					}else{
						//bean.setMes1("0%");
						bean.setMes1("0");
						//bean.setCor1("#cb0000");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	/**
	 * 
	 * @param filial
	 * @return
	 */
	public DashboardBean findFaturamentoMensal(Long filial ) {
		EntityManager manager = null;
		Connection conn = null;
		DashboardBean bean = new DashboardBean();
		Statement statement = null;
		ResultSet rs = null;
		try {
			conn = ConnectionStratec.getConnection();
			statement = conn.createStatement();
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Faturamento Mensal");
			bean.setTarget("R$");

			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				String SQL = "select SUM(realizado) as realizado from FaturamentoPMP"+
						" where ano = " + nomeMesCal.get(Calendar.YEAR)+
						" and periodo = "+mes;
				if(filial != null){
					TwFilial fl = manager.find(TwFilial.class, filial);
					SQL += " and LTRIM(RTRIM(descricao)) = '"+fl.getDescricaoStratec()+"'";
				}

				rs = statement.executeQuery(SQL);
				String valorFaturamento = "0,00";
				if(rs.next()){
					if(rs.getDouble("realizado") > 0){
						valorFaturamento = ValorMonetarioHelper.formata("###,###.00", rs.getDouble("realizado"));
					}
				}


				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					bean.setMes7(valorFaturamento);
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					bean.setMes6(valorFaturamento);
				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					bean.setMes5(valorFaturamento);
				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					bean.setMes4(valorFaturamento);
				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					bean.setMes3(valorFaturamento);
				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					bean.setMes2(valorFaturamento);
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					bean.setMes1(valorFaturamento);
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
			if(conn != null){
				try {
					rs.close();
					statement.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return bean;
	}
	/**
	 * (número de mecânicos X 2 x dias úteis do mês)/Número de backlogs no período
	 * @param filial
	 * @return
	 */
	public DashboardBean findBacklog(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Backlog");
			bean.setTarget(" >= 50%");

			DecimalFormat df = new DecimalFormat("0.0");
			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}

				Double backlog = 0d;
				String SQL = "select  count(distinct NUMERO_OS) from OS_PALM palm,( select dt.OS_PALM_IDOS_PALM from OS_PALM_DT dt"+
						" where dt.STATUS = 'NC') as opdt"+
						"	where palm.IDOS_PALM = opdt.OS_PALM_IDOS_PALM"+ 
						" and month(palm.EMISSAO) = "+mes+""+
						"	and year(palm.EMISSAO) = " + nomeMesCal.get(Calendar.YEAR);


				if(filial != null){
					SQL += " and palm.FILIAL = "+filial;
				}

				Query query = manager.createNativeQuery(SQL);
				backlog = Double.valueOf((Integer)query.getSingleResult());



				Double totalAtendimentos = 0d;
//				SQL = "select count(*) from TW_FUNCIONARIO f, ADM_PERFIL_SISTEMA_USUARIO adm"+
//						"	where f.EPIDNO = adm.ID_TW_USUARIO"+
//						"	and adm.ID_PERFIL = (select ID from ADM_PERFIL where TIPO_SISTEMA = 'PMP' and SIGLA = 'USUTEC')" +
//						"   and f.EPLSNM not in ('CUSTOMER', 'PARTNER')";
				
				SQL = "select  count(distinct NUMERO_OS) from OS_PALM palm"+
				"	where  month(palm.EMISSAO) = "+mes+""+
				"	and year(palm.EMISSAO) = " + nomeMesCal.get(Calendar.YEAR);
				//"	and month(f.DATA_CADASTRO) = "+mes+""+
				//"	and year(f.DATA_CADASTRO) = " + calendar.get(Calendar.YEAR);
				if(filial != null){
					//SQL += " and f.STN1 = "+filial;
					SQL += " and palm.FILIAL = "+filial;
				}

				query = manager.createNativeQuery(SQL);
				totalAtendimentos = Double.valueOf((Integer)query.getSingleResult());
//				totalAtendimentos = totalAtendimentos * 2 * DIAS_DO_MES;


				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalAtendimentos != null && totalAtendimentos > 0){
						bean.setMes7(df.format((backlog.doubleValue()/totalAtendimentos) * 100).replace(",", ".")+"%");
						if(((backlog.doubleValue()/totalAtendimentos) * 100) >= 50){
							bean.setCor7("green");
						}
						else{
							bean.setCor7("#d81010");
						}
					}else{
						bean.setMes7("0%");
						bean.setCor7("#d81010");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalAtendimentos != null && totalAtendimentos > 0){
						bean.setMes6(df.format((backlog.doubleValue()/totalAtendimentos) * 100).replace(",", ".")+"%");
						if(((backlog.doubleValue()/totalAtendimentos) * 100) >= 50){
							bean.setCor6("green");
						}
						else{
							bean.setCor6("#cb0000");
						}

					}else{
						bean.setMes6("0%");
						bean.setCor6("#cb0000");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalAtendimentos != null && totalAtendimentos > 0){
						bean.setMes5(df.format((backlog.doubleValue()/totalAtendimentos) * 100).replace(",", ".")+"%");
						if(((backlog.doubleValue()/totalAtendimentos) * 100) >= 50){
							bean.setCor5("green");
						}
						else{
							bean.setCor5("#cb0000");
						}


					}else{
						bean.setMes5("0%");
						bean.setCor5("#cb0000");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalAtendimentos != null && totalAtendimentos > 0){
						bean.setMes4(df.format((backlog.doubleValue()/totalAtendimentos) * 100).replace(",", ".")+"%");
						if(((backlog.doubleValue()/totalAtendimentos) * 100) >= 50){
							bean.setCor4("green");
						}
						else{
							bean.setCor4("#cb0000");
						}

					}else{
						bean.setMes4("0%");
						bean.setCor4("#cb0000");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalAtendimentos != null && totalAtendimentos > 0){
						bean.setMes3(df.format((backlog.doubleValue()/totalAtendimentos) * 100).replace(",", ".")+"%");
						if(((backlog.doubleValue()/totalAtendimentos) * 100) >= 50){
							bean.setCor3("green");
						}
						else{
							bean.setCor3("#cb0000");
						}
					}else{
						bean.setMes3("0%");
						bean.setCor3("#cb0000");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalAtendimentos != null && totalAtendimentos > 0){
						bean.setMes2(df.format((backlog.doubleValue()/totalAtendimentos) * 100).replace(",", ".")+"%");
						if(((backlog.doubleValue()/totalAtendimentos) * 100) >= 50){
							bean.setCor2("green");
						}
						else{
							bean.setCor2("#cb0000");
						}
					}else{
						bean.setMes2("0%");
						bean.setCor2("#cb0000");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalAtendimentos != null && totalAtendimentos > 0){
						bean.setMes1(df.format((backlog.doubleValue()/totalAtendimentos) * 100).replace(",", ".")+"%");
						if(((backlog.doubleValue()/totalAtendimentos) * 100) >= 50){
							bean.setCor1("green");
						}
						else{
							bean.setCor1("#cb0000");
						}

					}else{
						bean.setMes1("0%");
						bean.setCor1("#cb0000");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}



	public DashboardBean findStanderJobPerformance(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Standard job Performance");
			bean.setTarget(" <= 100%");

			DecimalFormat df = new DecimalFormat("0.0");
			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				String SQL = "select SUM(horas_agendada) from pmp_agendamento a,"+
						" (select  NUM_OS, min(id) as id from PMP_AGENDAMENTO"+
						"	where horas_agendada is not null"+
						"	group by NUM_OS) ag, PMP_CONFIG_OPERACIONAL op, PMP_CONTRATO c"+				 
						"	where a.ID = ag.id" +
						"   and a.ID_CONG_OPERACIONAL = op.ID" +
						"   and op.ID_CONTRATO = c.ID"+
						"	and month(a.DATA_AGENDAMENTO) = "+mes+""+
						" 	and year(a.DATA_AGENDAMENTO) = " + nomeMesCal.get(Calendar.YEAR);
				if(filial != null){
					SQL += " and c.FILIAL = "+filial;
				}
				Query query = manager.createNativeQuery(SQL);
				BigDecimal horasAgendada = (BigDecimal)query.getSingleResult();
				if(horasAgendada == null){
					horasAgendada = BigDecimal.ZERO;
				}
				Double totalHorasTrabalhadas = 0d;
				SQL = "select HORAS_TRABALHO from PMP_APROPRIACAO_HORAS aph, PMP_AGENDAMENTO ag, PMP_CONFIG_OPERACIONAL op, PMP_CONTRATO c"+
						"	where aph.HORAS_TRABALHO <> '0:00'"+
						"	and  month(aph.DATA) = "+mes+
						"	and aph.ID_AGENDAMENTO = ag.ID" +
						" and ag.ID_CONG_OPERACIONAL = op.ID" +
						" and op.ID_CONTRATO = c.ID"+
						" and year(aph.DATA) = " + nomeMesCal.get(Calendar.YEAR);


				if(filial != null){
					SQL += " and ag.FILIAL  = "+filial;
				}

				query = manager.createNativeQuery(SQL);
				List<String> listHorasTrab = (List<String>)query.getResultList();
				for (String horasTrab : listHorasTrab) {
					String [] horasTrabAux = horasTrab.split(":");
					if(Integer.valueOf(horasTrabAux[1]) > 0){
						Double horasMinutos = Double.valueOf(horasTrabAux[1])/60;
						totalHorasTrabalhadas += Double.valueOf(horasTrabAux[0]) + horasMinutos;
					}else if(Integer.valueOf(horasTrabAux[0]) > 0){
						totalHorasTrabalhadas += Double.valueOf(horasTrabAux[0] +"."+horasTrabAux[1]);
					}
				}


//				SQL = "select HORAS_VIAGEM from PMP_APROPRIACAO_HORAS aph, PMP_AGENDAMENTO ag, PMP_CONFIG_OPERACIONAL op, PMP_CONTRATO c"+
//						"	where aph.HORAS_VIAGEM <> '0:00'"+
//						"	and  month(aph.DATA) = "+mes+
//						"	and aph.ID_AGENDAMENTO = ag.ID" +
//						" and ag.ID_CONG_OPERACIONAL = op.ID" +
//						" and op.ID_CONTRATO = c.ID"+
//						" and year(aph.DATA) = " + nomeMesCal.get(Calendar.YEAR);
//
//
//				if(filial != null){
//					SQL += " and ag.FILIAL  = "+filial;
//				}
//
//				query = manager.createNativeQuery(SQL);
//				listHorasTrab = (List<String>)query.getResultList();
//				for (String horasTrab : listHorasTrab) {
//					String [] horasTrabAux = horasTrab.split(":");
//					if(Integer.valueOf(horasTrabAux[1]) > 0){
//						Double horasMinutos = Double.valueOf(horasTrabAux[1])/60;
//						totalHorasTrabalhadas += Double.valueOf(horasTrabAux[0]) + horasMinutos;
//					}else if(Integer.valueOf(horasTrabAux[0]) > 0){
//						totalHorasTrabalhadas += Double.valueOf(horasTrabAux[0] +"."+horasTrabAux[1]);
//					}
//				}


				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasTrabalhadas != null && totalHorasTrabalhadas > 0){
						bean.setMes7(df.format((totalHorasTrabalhadas/horasAgendada.doubleValue()) * 100).replace(",", ".")+"%");
						if(((totalHorasTrabalhadas/horasAgendada.doubleValue()) * 100)  <= 100){
							bean.setCor7("green");
						}
						else{
							bean.setCor7("#d81010");
						}
					}else{
						bean.setMes7("0%");
						bean.setCor7("#d81010");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasTrabalhadas != null && totalHorasTrabalhadas > 0){
						bean.setMes6(df.format((totalHorasTrabalhadas/horasAgendada.doubleValue()) * 100).replace(",", ".")+"%");
						if(((totalHorasTrabalhadas/horasAgendada.doubleValue()) * 100)  <= 100){
							bean.setCor6("green");
						}
						else{
							bean.setCor6("#cb0000");
						}

					}else{
						bean.setMes6("0%");
						bean.setCor6("#cb0000");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasTrabalhadas != null && totalHorasTrabalhadas > 0){
						bean.setMes5(df.format((totalHorasTrabalhadas/horasAgendada.doubleValue()) * 100).replace(",", ".")+"%");
						if(((totalHorasTrabalhadas/horasAgendada.doubleValue()) * 100)  <= 100){
							bean.setCor5("green");
						}
						else{
							bean.setCor5("#cb0000");
						}


					}else{
						bean.setMes5("0%");
						bean.setCor5("#cb0000");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasTrabalhadas != null && totalHorasTrabalhadas > 0){
						bean.setMes4(df.format((totalHorasTrabalhadas/horasAgendada.doubleValue()) * 100).replace(",", ".")+"%");
						if(((totalHorasTrabalhadas/horasAgendada.doubleValue()) * 100)  <= 100){
							bean.setCor4("green");
						}
						else{
							bean.setCor4("#cb0000");
						}

					}else{
						bean.setMes4("0%");
						bean.setCor4("#cb0000");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasTrabalhadas != null && totalHorasTrabalhadas > 0){
						bean.setMes3(df.format((totalHorasTrabalhadas/horasAgendada.doubleValue()) * 100).replace(",", ".")+"%");
						if(((totalHorasTrabalhadas/horasAgendada.doubleValue()) * 100)  <= 100){
							bean.setCor3("green");
						}
						else{
							bean.setCor3("#cb0000");
						}
					}else{
						bean.setMes3("0%");
						bean.setCor3("#cb0000");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasTrabalhadas != null && totalHorasTrabalhadas > 0){
						bean.setMes2(df.format((totalHorasTrabalhadas/horasAgendada.doubleValue()) * 100).replace(",", ".")+"%");
						if(((totalHorasTrabalhadas/horasAgendada.doubleValue()) * 100)  <= 100){
							bean.setCor2("green");
						}
						else{
							bean.setCor2("#cb0000");
						}
					}else{
						bean.setMes2("0%");
						bean.setCor2("#cb0000");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalHorasTrabalhadas != null && totalHorasTrabalhadas > 0){
						bean.setMes1(df.format((totalHorasTrabalhadas/horasAgendada.doubleValue()) * 100).replace(",", ".")+"%");
						if(((totalHorasTrabalhadas/horasAgendada.doubleValue()) * 100)  <= 100){
							bean.setCor1("green");
						}
						else{
							bean.setCor1("#cb0000");
						}

					}else{
						bean.setMes1("0%");
						bean.setCor1("#cb0000");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	public DashboardBean findPMTimeliness(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("PM Timeliness");
			bean.setTarget("85% +/-50H");

			DecimalFormat df = new DecimalFormat("0.0");
			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				String SQL = "select COUNT(*) from PMP_AGENDAMENTO ag, OS_PALM palm, PMP_CONFIG_OPERACIONAL op, PMP_CONTRATO c"+
						"	where ag.ID = palm.ID_AGENDAMENTO"+
						"	and ag.HORAS_REVISAO - palm.HORIMETRO <= 50"+
						"	and ag.HORAS_REVISAO - palm.HORIMETRO >= -50"+
						"	and  month(ag.DATA_AGENDAMENTO) = "+mes+
						"   and ag.ID_CONG_OPERACIONAL = op.ID" +
						"   and op.ID_CONTRATO = c.ID"+
						"   and year(ag.DATA_AGENDAMENTO) = " + nomeMesCal.get(Calendar.YEAR)+
						"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
						"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%'))";
				if(filial != null){
					SQL += " and c.FILIAL = "+filial;
				}
				Query query = manager.createNativeQuery(SQL);
				Integer totalTimelines = (Integer)query.getSingleResult();
				if(totalTimelines == null){
					totalTimelines = 0;
				}
				Integer totalRealizadoPeriodo = 0;
				SQL = "select COUNT(*) from PMP_AGENDAMENTO ag, OS_PALM palm, PMP_CONFIG_OPERACIONAL op, PMP_CONTRATO c"+
						" where ag.ID = palm.ID_AGENDAMENTO"+
						" and  month(ag.DATA_AGENDAMENTO) = "+mes+
						" and ag.ID_CONG_OPERACIONAL = op.ID" +
						" and op.ID_CONTRATO = c.ID"+
						" and year(ag.DATA_AGENDAMENTO) = " + nomeMesCal.get(Calendar.YEAR)+
						"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
						"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%'))";
				if(filial != null){
					SQL += " and c.FILIAL  = "+filial;
				}

				query = manager.createNativeQuery(SQL);
				List<BigDecimal> listTotalRealizadoPeriodo = (List<BigDecimal>)query.getResultList();
				if(listTotalRealizadoPeriodo.size() > 0){
					totalRealizadoPeriodo = (Integer)query.getSingleResult();
				}

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalRealizadoPeriodo != null && totalRealizadoPeriodo > 0){
						bean.setMes7(df.format((totalTimelines.doubleValue()/totalRealizadoPeriodo) * 100).replace(",", ".")+"%");
						if(((totalTimelines.doubleValue()/totalRealizadoPeriodo) * 100) > 85){
							bean.setCor7("green");
						}
						else{
							bean.setCor7("#cb0000");
						}
					}else{
						bean.setMes7("0%");
						bean.setCor7("#cb0000");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalRealizadoPeriodo != null && totalRealizadoPeriodo > 0){
						bean.setMes6(df.format((totalTimelines.doubleValue()/totalRealizadoPeriodo) * 100).replace(",", ".")+"%");
						if(((totalTimelines.doubleValue()/totalRealizadoPeriodo) * 100) > 85){
							bean.setCor6("green");
						}
						else{
							bean.setCor6("#cb0000");
						}
					}else{
						bean.setMes6("0%");
						bean.setCor6("#cb0000");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalRealizadoPeriodo != null && totalRealizadoPeriodo > 0){
						bean.setMes5(df.format((totalTimelines.doubleValue()/totalRealizadoPeriodo) * 100).replace(",", ".")+"%");
						if(((totalTimelines.doubleValue()/totalRealizadoPeriodo) * 100) > 85){
							bean.setCor5("green");
						}
						else{
							bean.setCor5("#cb0000");
						}
					}else{
						bean.setMes5("0%");
						bean.setCor5("#cb0000");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalRealizadoPeriodo != null && totalRealizadoPeriodo > 0){
						bean.setMes4(df.format((totalTimelines.doubleValue()/totalRealizadoPeriodo) * 100).replace(",", ".")+"%");
						if(((totalTimelines.doubleValue()/totalRealizadoPeriodo) * 100) > 85){
							bean.setCor4("green");
						}
						else{
							bean.setCor4("#cb0000");
						}
					}else{
						bean.setMes4("0%");
						bean.setCor4("#cb0000");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalRealizadoPeriodo != null && totalRealizadoPeriodo > 0){
						bean.setMes3(df.format((totalTimelines.doubleValue()/totalRealizadoPeriodo) * 100).replace(",", ".")+"%");
						if(((totalTimelines.doubleValue()/totalRealizadoPeriodo) * 100) > 85){
							bean.setCor3("green");
						}
						else{
							bean.setCor3("#cb0000");
						}
					}else{
						bean.setMes3("0%");
						bean.setCor3("#cb0000");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalRealizadoPeriodo != null && totalRealizadoPeriodo > 0){
						bean.setMes2(df.format((totalTimelines.doubleValue()/totalRealizadoPeriodo) * 100).replace(",", ".")+"%");
						if(((totalTimelines.doubleValue()/totalRealizadoPeriodo) * 100) > 85){
							bean.setCor2("green");
						}
						else{
							bean.setCor2("#cb0000");
						}
					}else{
						bean.setMes2("0%");
						bean.setCor2("#cb0000");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalRealizadoPeriodo != null && totalRealizadoPeriodo > 0){
						bean.setMes1(df.format((totalTimelines.doubleValue()/totalRealizadoPeriodo) * 100).replace(",", ".")+"%");
						if(((totalTimelines.doubleValue()/totalRealizadoPeriodo) * 100) > 85){
							bean.setCor1("green");
						}
						else{
							bean.setCor1("#cb0000");
						}
					}else{
						bean.setMes1("0%");
						bean.setCor1("#cb0000");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	public DashboardBean findNovosAtivos(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Contratos Novos Ativos");
			bean.setTarget("");

			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				String SQL = "select COUNT(*) FROM PMP_CONTRATO p WHERE"+
						" IS_ATIVO is null"+
						" and month(p.DATA_ACEITE) = "+mes+""+
						" and year(p.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+
						" and p.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') ";
				if(filial != null){
					SQL += " and p.FILIAL = "+filial;
				}
				Query query = manager.createNativeQuery(SQL);
				Integer totalNovosAtivos = (Integer)query.getSingleResult();
				if(totalNovosAtivos == null){
					totalNovosAtivos = 0;
				}

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalNovosAtivos != null && totalNovosAtivos > 0){
						bean.setMes7(totalNovosAtivos.toString());
					}else{
						bean.setMes7("0");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalNovosAtivos != null && totalNovosAtivos > 0){
						bean.setMes6(totalNovosAtivos.toString());
					}else{
						bean.setMes6("0");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalNovosAtivos != null && totalNovosAtivos > 0){
						bean.setMes5(totalNovosAtivos.toString());
					}else{
						bean.setMes5("0");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalNovosAtivos != null && totalNovosAtivos > 0){
						bean.setMes4(totalNovosAtivos.toString());
					}else{
						bean.setMes4("0");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalNovosAtivos != null && totalNovosAtivos > 0){
						bean.setMes3(totalNovosAtivos.toString());
					}else{
						bean.setMes3("0");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalNovosAtivos != null && totalNovosAtivos > 0){
						bean.setMes2(totalNovosAtivos.toString());
					}else{
						bean.setMes2("0");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalNovosAtivos != null && totalNovosAtivos > 0){
						bean.setMes1(totalNovosAtivos.toString());
					}else{
						bean.setMes1("0");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	public DashboardBean findContratosAtivos(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Contratos Ativos");
			bean.setTarget("");

			DecimalFormat df = new DecimalFormat("0.0");
			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				String SQL = "select COUNT(*) FROM PMP_CONTRATO p WHERE"+
						" IS_ATIVO is null"+
						" and month(p.DATA_ACEITE) <= "+mes+""+
						" and year(p.DATA_ACEITE) <= " + nomeMesCal.get(Calendar.YEAR)+
						" and p.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') ";
				if(filial != null){
					SQL += " and p.FILIAL = "+filial;
				}
				Query query = manager.createNativeQuery(SQL);
				Integer totalContratosAtivos = (Integer)query.getSingleResult();
				if(totalContratosAtivos == null){
					totalContratosAtivos = 0;
				}


				SQL = "select COUNT(*) FROM PMP_CONTRATO p WHERE"+
						" IS_ATIVO is null"+
						//" and month(p.DATA_ACEITE) <= "+mes+""+
						" and year(p.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+
						" and p.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') ";
				if(filial != null){
					SQL += " and p.FILIAL = "+filial;
				}
				query = manager.createNativeQuery(SQL);
				Integer totalContratosAtivosAnterior = (Integer)query.getSingleResult();
				if(totalContratosAtivosAnterior != null){
					totalContratosAtivos += totalContratosAtivosAnterior;
				}

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosAtivos != null && totalContratosAtivos > 0){
						bean.setMes7(totalContratosAtivos.toString());
					}else{
						bean.setMes7("0");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosAtivos != null && totalContratosAtivos > 0){
						bean.setMes6(totalContratosAtivos.toString());
					}else{
						bean.setMes6("0");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosAtivos != null && totalContratosAtivos > 0){
						bean.setMes5(totalContratosAtivos.toString());
					}else{
						bean.setMes5("0");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosAtivos != null && totalContratosAtivos > 0){
						bean.setMes4(totalContratosAtivos.toString());
					}else{
						bean.setMes4("0");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosAtivos != null && totalContratosAtivos > 0){
						bean.setMes3(totalContratosAtivos.toString());
					}else{
						bean.setMes3("0");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosAtivos != null && totalContratosAtivos > 0){
						bean.setMes2(totalContratosAtivos.toString());
					}else{
						bean.setMes2("0");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosAtivos != null && totalContratosAtivos > 0){
						bean.setMes1(totalContratosAtivos.toString());
					}else{
						bean.setMes1("0");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	public DashboardBean findContratosRenovados(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Contratos Renovados");
			bean.setTarget("");

			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				String SQL = "select COUNT(*) from PMP_CONTRATO ca,("+
						" select c.NUMERO_SERIE, COUNT(c.id) num_contrato from PMP_CONTRATO c" +
						" where c.ID_STATUS_CONTRATO = (select id from PMP_STATUS_CONTRATO where SIGLA = 'CA')"+
						" group by c.NUMERO_SERIE) t"+
						" where ca.NUMERO_SERIE = t.NUMERO_SERIE"+
						" and t.num_contrato > 1"+
						" and ca.IS_ATIVO is null"+
						" and month(ca.DATA_ACEITE) = "+mes+""+
						" and year(ca.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR);
				if(filial != null){
					SQL += " and ca.FILIAL = "+filial;
				}
				Query query = manager.createNativeQuery(SQL);
				Integer totalContratosRenovados = (Integer)query.getSingleResult();
				if(totalContratosRenovados == null){
					totalContratosRenovados = 0;
				}

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosRenovados != null && totalContratosRenovados > 0){
						bean.setMes7(totalContratosRenovados.toString());
					}else{
						bean.setMes7("0");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosRenovados != null && totalContratosRenovados > 0){
						bean.setMes6(totalContratosRenovados.toString());
					}else{
						bean.setMes6("0");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosRenovados != null && totalContratosRenovados > 0){
						bean.setMes5(totalContratosRenovados.toString());
					}else{
						bean.setMes5("0");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosRenovados != null && totalContratosRenovados > 0){
						bean.setMes4(totalContratosRenovados.toString());
					}else{
						bean.setMes4("0");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosRenovados != null && totalContratosRenovados > 0){
						bean.setMes3(totalContratosRenovados.toString());
					}else{
						bean.setMes3("0");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosRenovados != null && totalContratosRenovados > 0){
						bean.setMes2(totalContratosRenovados.toString());
					}else{
						bean.setMes2("0");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosRenovados != null && totalContratosRenovados > 0){
						bean.setMes1(totalContratosRenovados.toString());
					}else{
						bean.setMes1("0");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	public DashboardBean findRenewalRate(Long filial ) {
		EntityManager manager = null;
		
		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Renewal Rate");
			
			
			DecimalFormat df = new DecimalFormat("0.0");
			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			Integer totalContratosExpiradosGeral = 0;
			Integer totalRenewalRateGeral = 0;
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				String yearMonth = nomeMesCal.get(Calendar.YEAR)+""+mes;
				//				String SQL = "select COUNT(*) from PMP_CONTRATO ca,("+
				//				"	select c.NUMERO_SERIE, COUNT(c.id) num_contrato from PMP_CONTRATO c"+
				//				"	group by c.NUMERO_SERIE) t"+
				//				"	where ca.NUMERO_SERIE = t.NUMERO_SERIE"+
				//				"	and t.num_contrato > 1"+
				//				"	and ca.IS_ATIVO is null"+
				//				"	and month(ca.DATA_ACEITE) = "+mes+""+
				//				" 	and year(ca.DATA_ACEITE) = " + calendar.get(Calendar.YEAR);
										String SQL = "select distinct (hs.ID_CONTRATO), month(c.DATA_ACEITE), (select COUNT(*) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.ID and IS_EXECUTADO = 'S' and STANDARD_JOB_CPTCD <> '752A' and STANDARD_JOB_CPTCD <> '7511' and STANDARD_JOB_CPTCD <> '7522' and STANDARD_JOB_CPTCD <> '752B') totalRev,"+
										" (select MAX(frequencia) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.ID) frequencia, 'A' status, c.NUMERO_CONTRATO "+
										" from PMP_CONT_HORAS_STANDARD hs, PMP_CONTRATO c, PMP_AGENDAMENTO ag "+
											" where year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+
											" and month(ag.DATA_AGENDAMENTO) = "+mes+""+
											" and c.ID_STATUS_CONTRATO = (select id from PMP_STATUS_CONTRATO where SIGLA = 'CA')"+
											" and hs.ID_CONTRATO = c.id"+
											" and c.IS_ATIVO is null"+
											" and ag.id_cont_horas_standard = hs.id"+
											" and ag.ID_CONT_HORAS_STANDARD = (select MAX(id) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.ID and IS_EXECUTADO = 'S')"+
											" and cast(year(ag.DATA_AGENDAMENTO) as varchar)+cast(month(ag.DATA_AGENDAMENTO) as varchar) <="+Integer.valueOf(yearMonth)+
											
											" union"+
						
											" select distinct (hs.ID_CONTRATO), month(c.DATA_ACEITE), (select COUNT(*) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.ID and IS_EXECUTADO = 'S' and STANDARD_JOB_CPTCD <> '752A' and STANDARD_JOB_CPTCD <> '7511' and STANDARD_JOB_CPTCD <> '7522' and STANDARD_JOB_CPTCD <> '752B') totalRev"+  
											" , (select MAX(frequencia) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.ID) frequencia, 'A' status, c.NUMERO_CONTRATO"+
											" from PMP_CONT_HORAS_STANDARD hs, PMP_CONTRATO c, PMP_AGENDAMENTO ag "+
											" where month(c.DATA_ACEITE) <= "+mes+""+
											" and year(c.DATA_ACEITE) <= " + nomeMesCal.get(Calendar.YEAR)+
											" and month(ag.DATA_AGENDAMENTO) = "+mes+""+
											" and c.ID_STATUS_CONTRATO = (select id from PMP_STATUS_CONTRATO where SIGLA = 'CA')"+
											" and hs.ID_CONTRATO = c.id"+
											" and c.IS_ATIVO is null" +
											" and ag.id_cont_horas_standard = hs.id"+
											" and ag.ID_CONT_HORAS_STANDARD = (select MAX(id) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.ID and IS_EXECUTADO = 'S')"+
											" and cast(year(ag.DATA_AGENDAMENTO) as varchar)+cast(month(ag.DATA_AGENDAMENTO) as varchar) <="+Integer.valueOf(yearMonth)+
											
											" union "+
											
											" select distinct (hs.ID_CONTRATO), month(c.DATA_ACEITE), (select COUNT(*) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.ID and IS_EXECUTADO = 'S' and STANDARD_JOB_CPTCD <> '752A' and STANDARD_JOB_CPTCD <> '7511' and STANDARD_JOB_CPTCD <> '7522' and STANDARD_JOB_CPTCD <> '752B') totalRev"+  
											" , (select MAX(frequencia) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.ID) frequencia, 'I' status, c.NUMERO_CONTRATO"+
											" from PMP_CONT_HORAS_STANDARD hs, PMP_CONTRATO c, PMP_AGENDAMENTO ag"+
											" where month(ag.DATA_AGENDAMENTO) = "+mes+""+
											" and year(ag.DATA_AGENDAMENTO) = " + nomeMesCal.get(Calendar.YEAR)+
											" and c.ID_STATUS_CONTRATO = (select id from PMP_STATUS_CONTRATO where SIGLA = 'CA')"+
											" and hs.ID_CONTRATO = c.id"+
											" and ag.ID_CONT_HORAS_STANDARD = (select MAX(id) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.ID)"+
											" and c.IS_ATIVO is not null";
											
									

	
//				String SQL = "select COUNT(*) from PMP_CONTRATO ca,("+
//						" select c.NUMERO_SERIE, COUNT(c.id) num_contrato from PMP_CONTRATO c" +
//						" where c.ID_STATUS_CONTRATO = (select id from PMP_STATUS_CONTRATO where SIGLA = 'CA')"+
//						" group by c.NUMERO_SERIE) t"+
//						" where ca.NUMERO_SERIE = t.NUMERO_SERIE"+
//						" and t.num_contrato > 1"+
//						" and ca.IS_ATIVO is null"+
//						" and month(ca.DATA_ACEITE) = "+mes+""+
//						" and year(ca.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+
//						" and ca.NUMERO_SERIE in (select c.NUMERO_SERIE from PMP_CONTRATO c"+
//						"  where c.IS_ATIVO is not null"+
//						"  and month(c.DATA_ACEITE) = "+mes+""+
//						"  and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+")";
				if(filial != null){
					SQL += " and ca.FILIAL = "+filial;
				}
				Integer totalRenewalRate = 0;
				Integer totalAtivos = 0;
				Query query = manager.createNativeQuery(SQL);
				Integer totalContratosExpirados = 0;
				List<Object[]> pair = (List<Object[]>)query.getResultList();
				int count = 0;
				for (Object[] objects : pair) {
					//BigDecimal horasManutenção = (BigDecimal)objects[1];
					BigDecimal frequencia = (BigDecimal)objects[3];
					Integer totalRev = (Integer)objects[2];
					String status = (String)objects[4];
					if(totalRev.longValue() >= 4 && frequencia.longValue() == 500){
						
						//totalRenewalRate += (totalRev.intValue() / 4);
						if(totalRev.doubleValue()%4 == 0){
							totalContratosExpirados += 1;
							totalContratosExpiradosGeral += 1;
							
							if(status.equals("A")){
								totalRenewalRate += 1;
								System.out.println(mes+" - "+objects[5]);
								if(count <= 5){
									totalRenewalRateGeral += 1;
								}
							}
							
						}
					}else if(totalRev.longValue() >= 8 && frequencia.longValue() != 500){
						//totalRenewalRate += (totalRev.intValue() / 8);
						if(totalRev.doubleValue()%8 == 0){
							totalContratosExpirados += 1;
							totalContratosExpiradosGeral += 1;
							
							if(status.equals("A")){
								totalRenewalRate += 1;
								System.out.println(mes+" - "+objects[5]);
								if(count <= 5){
									totalRenewalRateGeral += 1;
								}
							}
						}
					}
				}
				count++;
////				Integer totalRenewalRate = (Integer)query.getSingleResult();
////				if(totalRenewalRate == null){
////					totalRenewalRate = 0;
////				}
//				Integer totalContratosExpirados = 0;
////				SQL = "select COUNT(*) from PMP_CONTRATO c"+
////						"  where c.IS_ATIVO is not null"+
////						"  and month(c.DATA_ACEITE) = "+mes+""+
////						"  and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR);
//				SQL = "select hs.ID_CONTRATO, HORAS_MANUTENCAO  from PMP_CONT_HORAS_STANDARD hs, PMP_CONTRATO c where hs.ID in ("+
//				  
//				"	select ID_CONT_HORAS_STANDARD from PMP_AGENDAMENTO ag"+
//				"	where month(ag.DATA_AGENDAMENTO) = "+mes+""+
//				"	and year(ag.DATA_AGENDAMENTO) = " + nomeMesCal.get(Calendar.YEAR)+
//				"	and ID_CONT_HORAS_STANDARD is not null)"+
//				"	and c.ID_STATUS_CONTRATO = (select id from PMP_STATUS_CONTRATO where SIGLA = 'CA')"+
//				"	and hs.ID_CONTRATO = c.id" +
//				"   and c.IS_ATIVO is not null"+
//					
//				"	union"+
//												
//				"	select hs.ID_CONTRATO, HORAS_MANUTENCAO  from PMP_CONT_HORAS_STANDARD_PLUS hs, PMP_CONTRATO c where hs.ID in ("+
//											  
//				"	select ID_CONT_HORAS_STANDARD_PLUS from PMP_AGENDAMENTO ag"+
//				"	where month(ag.DATA_AGENDAMENTO) = "+mes+""+
//				"	and year(ag.DATA_AGENDAMENTO) = " + nomeMesCal.get(Calendar.YEAR)+
//				"	and ID_CONT_HORAS_STANDARD is not null)"+
//				"	and c.ID_STATUS_CONTRATO = (select id from PMP_STATUS_CONTRATO where SIGLA = 'CA')"+
//				"	and hs.ID_CONTRATO = c.id"+
//				"   and c.IS_ATIVO is not null";	
//				
//				if(filial != null){
//					SQL += " and c.FILIAL  = "+filial;
//				}
//				
//				//Integer totalContratosExpirados = 0;
//				query = manager.createNativeQuery(SQL);
//				pair = (List<Object[]>)query.getResultList();
//				for (Object[] objects : pair) {
//					BigDecimal horasManutenção = (BigDecimal)objects[1];
//					if(horasManutenção.longValue() >= 2000){
//						totalContratosExpirados += (horasManutenção.intValue() / 2000);
//					}
//				}

//				query = manager.createNativeQuery(SQL);
//				List<BigDecimal> listTotalContratosExpirados = (List<BigDecimal>)query.getResultList();
//				if(listTotalContratosExpirados.size() > 0){
//					totalContratosExpirados = (Integer)query.getSingleResult();
//				}

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosExpirados != null && totalContratosExpirados > 0){
						bean.setMes7(df.format((totalRenewalRate.doubleValue()/totalContratosExpirados) * 100).replace(",", ".")+"%");
						if(((totalRenewalRate.doubleValue()/totalContratosExpirados) * 100) > 50){
							bean.setCor7("green");
						}
						else{
							bean.setCor7("#cb0000");
						}
					}else{
						bean.setMes7("0%");
						bean.setCor7("#cb0000");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosExpirados != null && totalContratosExpirados > 0){
						bean.setMes6(df.format((totalRenewalRate.doubleValue()/totalContratosExpirados) * 100).replace(",", ".")+"%");
						if(((totalRenewalRate.doubleValue()/totalContratosExpirados) * 100) > 50){
							bean.setCor6("green");
						}
						else{
							bean.setCor6("#cb0000");
						}
					}else{
						bean.setMes6("0%");
						bean.setCor6("#cb0000");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosExpirados != null && totalContratosExpirados > 0){
						bean.setMes5(df.format((totalRenewalRate.doubleValue()/totalContratosExpirados) * 100).replace(",", ".")+"%");
						if(((totalRenewalRate.doubleValue()/totalContratosExpirados) * 100) > 50){
							bean.setCor5("green");
						}
						else{
							bean.setCor5("#cb0000");
						}
					}else{
						bean.setMes5("0%");
						bean.setCor5("#cb0000");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosExpirados != null && totalContratosExpirados > 0){
						bean.setMes4(df.format((totalRenewalRate.doubleValue()/totalContratosExpirados) * 100).replace(",", ".")+"%");
						if(((totalRenewalRate.doubleValue()/totalContratosExpirados) * 100) > 50){
							bean.setCor4("green");
						}
						else{
							bean.setCor4("#cb0000");
						}
					}else{
						bean.setMes4("0%");
						bean.setCor4("#cb0000");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosExpirados != null && totalContratosExpirados > 0){
						bean.setMes3(df.format((totalRenewalRate.doubleValue()/totalContratosExpirados) * 100).replace(",", ".")+"%");
						if(((totalRenewalRate.doubleValue()/totalContratosExpirados) * 100) > 50){
							bean.setCor3("green");
						}
						else{
							bean.setCor3("#cb0000");
						}
					}else{
						bean.setMes3("0%");
						bean.setCor3("#cb0000");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosExpirados != null && totalContratosExpirados > 0){
						bean.setMes2(df.format((totalRenewalRate.doubleValue()/totalContratosExpirados) * 100).replace(",", ".")+"%");
						if(((totalRenewalRate.doubleValue()/totalContratosExpirados) * 100) > 50){
							bean.setCor2("green");
						}
						else{
							bean.setCor2("#cb0000");
						}
					}else{
						bean.setMes2("0%");
						bean.setCor2("#cb0000");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosExpirados != null && totalContratosExpirados > 0){
						bean.setMes1(df.format((totalRenewalRate.doubleValue()/totalContratosExpirados) * 100).replace(",", ".")+"%");
						if(((totalRenewalRate.doubleValue()/totalContratosExpirados) * 100) > 50){
							bean.setCor1("green");
						}
						else{
							bean.setCor1("#cb0000");
						}
					}else{
						bean.setMes1("0%");
						bean.setCor1("#cb0000");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
			bean.setTarget(">50% "+df.format(((totalRenewalRateGeral.doubleValue()/totalContratosExpiradosGeral) * 100)/6 ));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	public DashboardBean findMediaDeslocamento(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Média Deslocamento");
			bean.setTarget("");

			DecimalFormat df = new DecimalFormat("0.0");
			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				String SQL = "select  sum(d.METROS_FIM - d.METROS_INICIO) from ADM_DESLOCAMENTO d, TW_FUNCIONARIO f"+
						" where d.PLACA in (select f.placa_veiculo from  TW_FUNCIONARIO f, ADM_PERFIL_SISTEMA_USUARIO adm"+  
						" where f.EPIDNO = adm.ID_TW_USUARIO"+
						" and adm.ID_PERFIL = (select adms.ID from ADM_PERFIL adms where adms.SIGLA = 'USUTEC' and adms.TIPO_SISTEMA = 'PMP'))"+
						" and d.METROS_FIM is not null" +
						" and f.PLACA_VEICULO = d.PLACA"+
						" and MONTH(d.DATA_REPORT_INICIO) = "+mes+""+
						" and YEAR(d.DATA_REPORT_INICIO) = " + nomeMesCal.get(Calendar.YEAR);
				if(filial != null){
					SQL += " and f.STN1 = "+filial;
				}
				Query query = manager.createNativeQuery(SQL);
				BigDecimal deslocamento = (BigDecimal)query.getSingleResult();
				if(deslocamento == null){
					deslocamento = BigDecimal.ZERO;
				}
				Integer numeroAtendimento = 0;
				SQL = "select COUNT(*) from PMP_AGENDAMENTO a"+
						" where a.ID_STATUS_AGENDAMENTO = (select ID from PMP_STATUS_AGENDAMENTO where SIGLA = 'FIN')" +
						" and MONTH(a.DATA_AGENDAMENTO) = "+mes+""+
						" and YEAR(a.DATA_AGENDAMENTO) = " + nomeMesCal.get(Calendar.YEAR);	
				if(filial != null){
					SQL += " and a.FILIAL  = "+filial;
				}

				query = manager.createNativeQuery(SQL);
				List<BigDecimal> listTotalContratosExpirados = (List<BigDecimal>)query.getResultList();
				if(listTotalContratosExpirados.size() > 0){
					numeroAtendimento = (Integer)query.getSingleResult();
				}

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(numeroAtendimento != null && numeroAtendimento > 0){
						bean.setMes7(df.format((deslocamento.doubleValue()/numeroAtendimento)).replace(",", "."));
					}else{
						bean.setMes7("0.0");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(numeroAtendimento != null && numeroAtendimento > 0){
						bean.setMes6(df.format((deslocamento.doubleValue()/numeroAtendimento)).replace(",", "."));
					}else{
						bean.setMes6("0.0");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(numeroAtendimento != null && numeroAtendimento > 0){
						bean.setMes5(df.format((deslocamento.doubleValue()/numeroAtendimento)).replace(",", "."));
					}else{
						bean.setMes5("0.0");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(numeroAtendimento != null && numeroAtendimento > 0){
						bean.setMes4(df.format((deslocamento.doubleValue()/numeroAtendimento)).replace(",", "."));
					}else{
						bean.setMes4("0.0");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(numeroAtendimento != null && numeroAtendimento > 0){
						bean.setMes3(df.format((deslocamento.doubleValue()/numeroAtendimento)).replace(",", "."));
					}else{
						bean.setMes3("0.0");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(numeroAtendimento != null && numeroAtendimento > 0){
						bean.setMes2(df.format((deslocamento.doubleValue()/numeroAtendimento)).replace(",", "."));
					}else{
						bean.setMes2("0.0");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(numeroAtendimento != null && numeroAtendimento > 0){
						bean.setMes1(df.format((deslocamento.doubleValue()/numeroAtendimento)).replace(",", "."));
					}else{
						bean.setMes1("0.0");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}

	//	public List<FamiliaBean> findAllFamiliaGerador(String contExcessao, String descricao) {
	//		List<FamiliaBean> familiaList = new ArrayList<FamiliaBean>();
	//		EntityManager manager = null;
	//		
	//		try {
	//			manager = JpaUtil.getInstance();
	//			
	//			Query query = manager.createQuery("From PmpFamilia where id in( select cm.idFamilia.id from PmpConfigManutencao cm where cm.contExcessao = '"+contExcessao+"' and cm.isGeradorStandby = 'S') order by descricao");
	//			List<PmpFamilia> result = query.getResultList();
	//			for (PmpFamilia familia : result) {
	//				FamiliaBean bean = new FamiliaBean();
	//				bean.setId(familia.getId());
	//				bean.setDescricao(familia.getDescricao());
	//				familiaList.add(bean);
	//			}
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}finally {
	//			if(manager != null && manager.isOpen()){
	//				manager.close();
	//			}
	//		}
	//		return familiaList;
	//	}

	public List<FamiliaBean> findAllFamilia() {
		List<FamiliaBean> familiaList = new ArrayList<FamiliaBean>();
		EntityManager manager = null;

		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("FROM PmpFamilia ORDER BY descricao");
			List<PmpFamilia> result = query.getResultList();
			for (PmpFamilia familia : result) {
				FamiliaBean bean = new FamiliaBean();
				bean.setId(familia.getId());
				bean.setDescricao(familia.getDescricao());
				familiaList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return familiaList;
	}

	private DashboardBean findStanderJobUsage(){
		DashboardBean bean = new DashboardBean();
		bean.setOperacao("Standard Job Usage PM");
		bean.setTarget("100%");
		bean.setMes1("100%");
		bean.setMes2("100%");
		bean.setMes3("100%");
		bean.setMes4("100%");
		bean.setMes5("100%");
		bean.setMes6("100%");
		bean.setMes7("100%");
		bean.setCor1("green");
		bean.setCor2("green");
		bean.setCor3("green");
		bean.setCor4("green");
		bean.setCor5("green");
		bean.setCor6("green");
		bean.setCor7("green");
		return bean;
	}

	public List<FamiliaBean> findAllFamiliaGerador(String descricao) {
		List<FamiliaBean> familiaList = new ArrayList<FamiliaBean>();
		EntityManager manager = null;

		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("FROM PmpFamilia WHERE descricao LIKE '%" + descricao.toUpperCase() + "%' ORDER BY descricao");
			List<PmpFamilia> result = query.getResultList();
			for (PmpFamilia familia : result) {
				FamiliaBean bean = new FamiliaBean();
				bean.setId(familia.getId());
				bean.setDescricao(familia.getDescricao());
				familiaList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return familiaList;
	}

	public FamiliaBean saveOrUpdate(FamiliaBean bean) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpFamilia familia = null;
			if (bean.getId() == null || bean.getId() == 0) {
				familia = new PmpFamilia();
				familia.setDescricao(bean.getDescricao().toUpperCase());
				manager.persist(familia);
			} else {
				familia = manager.find(PmpFamilia.class, bean.getId());
				familia.setDescricao(bean.getDescricao().toUpperCase());
				manager.merge(familia);
			}
			manager.getTransaction().commit();
			bean.setId(familia.getId());
			return bean;
		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return null;
	}

	public Boolean remover(FamiliaBean bean) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpFamilia.class, bean.getId()));
			manager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return false;
	}

	public DashboardBean findContratosMda(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Contratos MDA");
			bean.setTarget("");

			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				String SQL = "";
				//if(i == 0){//mês corrente
					SQL = "select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
					"	and c.ID = cc.ID_CONTRATO" +
					"  and c.IS_ATIVO is null"+
					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') ";
					//"	and month(c.DATA_ACEITE) <= "+mes+""+
					//"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR);

//				}else{
//					SQL = 
//							"select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO" +
//					//"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					"	and month(c.DATA_ACEITE) <= "+mes+""+
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR);
				//}

				if(filial != null){
					SQL += " and c.FILIAL = "+filial;
				}
				Integer totalContratosMda = 0;
				Query query = null;
				if(i == 0){	
					query = manager.createNativeQuery(SQL);
					totalContratosMda = query.getResultList().size();
					if(totalContratosMda == null){
						totalContratosMda = 0;
					}
				}
				if(i > 0){
					//Sql ativos mes posterior com MDA
					//int mesPosterior = mes+1;
					SQL = "select SUM(quantidade) from pmp_dashboard_contratos where ano = "+nomeMesCal.get(Calendar.YEAR)+" and mes = "+mes+" and sigla = 'MDA'";
					if(filial != null){
						SQL += " and FILIAL = "+filial;
					}
					query = manager.createNativeQuery(SQL);
					totalContratosMda = 0;
					if(query.getSingleResult() != null){
						totalContratosMda = ((BigDecimal)query.getSingleResult()).intValue();
					}
				}
				
//				if(i == 0){//mês corrente
//					SQL = "select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO" +
//					"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					//"	and month(c.DATA_ACEITE) <= "+mes+""+
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR) -1);
//				}else{
//					SQL = "select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO" +
//					//"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					//"	and month(c.DATA_ACEITE) <= "+mes+""+
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR) -1);
//				}
//				if(filial != null){
//					SQL += " and c.FILIAL = "+filial;
//				}
//				query = manager.createNativeQuery(SQL);
//				Integer totalContratosMdaAnterior = query.getResultList().size();
//				if(totalContratosMdaAnterior != null){
//					totalContratosMda += totalContratosMdaAnterior;
//				}

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes7(totalContratosMda.toString());
					}else{
						bean.setMes7("0");
					}
					totalContratos.setMes7(bean.getMes7());
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes6(totalContratosMda.toString());
					}else{
						bean.setMes6("0");
					}


				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes5(totalContratosMda.toString());
					}else{
						bean.setMes5("0");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes4(totalContratosMda.toString());
					}else{
						bean.setMes4("0");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes3(totalContratosMda.toString());
					}else{
						bean.setMes3("0");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes2(totalContratosMda.toString());
					}else{
						bean.setMes2("0");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes1(totalContratosMda.toString());
					}else{
						bean.setMes1("0");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}



		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}



	public DashboardBean findContratosRental(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Contratos Rental");
			bean.setTarget("");

			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				String SQL = "";
				//if(i == 0){//mês corrente
				SQL = "select distinct c.id from PMP_CONTRATO c"+
				"	where c.ID_TIPO_CONTRATO = (select ID from PMP_TIPO_CONTRATO where SIGLA = 'REN')"+
				" and DATA_FINALIZACAO is null "+
				"   and c.IS_ATIVO is null"+
				" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') ";
				//"	and month(c.DATA_ACEITE) <= "+mes+""+
				//"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR);
				//				}else{
				//					SQL = " select c.id from PMP_CONTRATO c"+
				//					"	where c.ID_TIPO_CONTRATO = (select ID from PMP_TIPO_CONTRATO where SIGLA = 'REN')"+
				//					//"   and c.IS_ATIVO is null"+
				//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
				//					"	and month(c.DATA_ACEITE) <= "+mes+""+
				//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR);
				//				}
				if(filial != null){
					SQL += " and c.FILIAL = "+filial;
				}
				Integer totalContratosMda = 0;
				Query query = null;
				if(i == 0){	
					query = manager.createNativeQuery(SQL);
					totalContratosMda = query.getResultList().size();
					if(totalContratosMda == null){
						totalContratosMda = 0;
					}
				}

				if(i > 0){
					//Sql ativos mes posterior com MDA
					//int mesPosterior = mes+1;
					SQL = "select SUM(quantidade) from pmp_dashboard_contratos where ano = "+nomeMesCal.get(Calendar.YEAR)+" and mes = "+mes+" and sigla = 'REN'";
					if(filial != null){
						SQL += " and FILIAL = "+filial;
					}
					query = manager.createNativeQuery(SQL);
					totalContratosMda = 0;
					if(query.getSingleResult() != null){
						totalContratosMda = ((BigDecimal)query.getSingleResult()).intValue();
					}

				}
				
//				if(i == 0){//mês corrente
//				SQL = "select c.id from PMP_CONTRATO c"+
//						"	where c.ID_TIPO_CONTRATO = (select ID from PMP_TIPO_CONTRATO where SIGLA = 'REN')"+
//						"   and c.IS_ATIVO is null"+
//						" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//						//"	and month(c.DATA_ACEITE) <= "+mes+""+
//						"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR) - 1);
//				}else{
//					SQL = " select c.id from PMP_CONTRATO c"+
//					"	where c.ID_TIPO_CONTRATO = (select ID from PMP_TIPO_CONTRATO where SIGLA = 'REN')"+
//					//"   and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					//"	and month(c.DATA_ACEITE) <= "+mes+""+
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR) - 1);
//				}
//				if(filial != null){
//					SQL += " and c.FILIAL = "+filial;
//				}
//				query = manager.createNativeQuery(SQL);
//				Integer totalContratosMdaAnterior = query.getResultList().size();
//				if(totalContratosMdaAnterior != null){
//					totalContratosMda += totalContratosMdaAnterior;
//				}

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes7(totalContratosMda.toString());
					}else{
						bean.setMes7("0");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes6(totalContratosMda.toString());
					}else{
						bean.setMes6("0");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes5(totalContratosMda.toString());
					}else{
						bean.setMes5("0");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes4(totalContratosMda.toString());
					}else{
						bean.setMes4("0");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes3(totalContratosMda.toString());
					}else{
						bean.setMes3("0");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes2(totalContratosMda.toString());
					}else{
						bean.setMes2("0");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes1(totalContratosMda.toString());
					}else{
						bean.setMes1("0");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}

	public DashboardBean findContratosCliente(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Contratos Cliente");
			bean.setTarget("");

			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				String SQL = "select COUNT(*) from PMP_CONTRATO c"+
						"	where c.IS_ATIVO is null"+
						" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
						"	and month(c.DATA_ACEITE) <= "+mes+
						"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+
						"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
						"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
						"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
						"	and c.ID = cc.ID_CONTRATO"+
						"  and c.IS_ATIVO is null"+
						" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
						" 	and month(c.DATA_ACEITE) <= "+mes+
						"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+")";		 


				if(filial != null){
					SQL += " and c.FILIAL = "+filial;
				}
				Query query = manager.createNativeQuery(SQL);
				Integer totalContratosMda = (Integer)query.getSingleResult();
				if(totalContratosMda == null){
					totalContratosMda = 0;
				}

				SQL = "select COUNT(*) from PMP_CONTRATO c"+
						"	where c.IS_ATIVO is null"+
						" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
						//"	and month(c.DATA_ACEITE) <= "+mes+
						"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+
						"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
						"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
						"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
						"	and c.ID = cc.ID_CONTRATO"+
						"  and c.IS_ATIVO is null"+
						" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
						//" 	and month(c.DATA_ACEITE) <= "+mes+
						"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+")";		 


				if(filial != null){
					SQL += " and c.FILIAL = "+filial;
				}
				query = manager.createNativeQuery(SQL);
				Integer totalContratosMdaAnterior = (Integer)query.getSingleResult();
				if(totalContratosMdaAnterior != null){
					totalContratosMda += totalContratosMdaAnterior;
				}

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes7(totalContratosMda.toString());
					}else{
						bean.setMes7("0");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes6(totalContratosMda.toString());
					}else{
						bean.setMes6("0");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes5(totalContratosMda.toString());
					}else{
						bean.setMes5("0");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes4(totalContratosMda.toString());
					}else{
						bean.setMes4("0");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes3(totalContratosMda.toString());
					}else{
						bean.setMes3("0");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes2(totalContratosMda.toString());
					}else{
						bean.setMes2("0");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes1(totalContratosMda.toString());
					}else{
						bean.setMes1("0");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	public DashboardBean findContratosPartner(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Contratos Partner");
			bean.setTarget("");

			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				String SQL = "";
				//if(i == 0){//mês corrente
					
					SQL = "select distinct c.id from PMP_CONTRATO c, PMP_CLASSIFICACAO_CONTRATO cc"+
						" where DATA_FINALIZACAO is null"+
						" and c.IS_ATIVO is null"+
						" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA')"+ 
						" and c.ID_CLASSIFICACAO_CONTRATO = cc.ID"+
						" and cc.SIGLA in ('PART')"+
						" and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
						" and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
						" where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
						" and c.ID = cc.ID_CONTRATO)";
					
					
//					SQL = "select c.id from PMP_CONTRATO c"+
//					"	where c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PART')"+
//					"	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+
//					"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
//					"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO"+
//					"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" 	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+")";
					if(filial != null){
						SQL += " and c.FILIAL = "+filial+"";
					}
//				}else{
//					SQL = "select op.ID_CONTRATO, max(ag.DATA_AGENDAMENTO) from PMP_AGENDAMENTO ag, PMP_CONFIG_OPERACIONAL op"+ 
//						" where ID_CONG_OPERACIONAL = op.ID and op.ID_CONTRATO in (" +
//							"select c.id from PMP_CONTRATO c"+
//					//"	where c.IS_ATIVO is null"+
//					" where c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PART')"+
//					"	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+
//					"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
//					"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO"+
//					//"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" 	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+"))" +
//					" and month(ag.DATA_AGENDAMENTO) <= "+mes;
//					if(filial != null){
//						SQL += " and ag.FILIAL = "+filial+"";
//					}
//					SQL +=" group by op.ID_CONTRATO	";
//					
//				}

					Integer totalContratosMda = 0;
					Query query = null;
					if(i == 0){	
						query = manager.createNativeQuery(SQL);
						totalContratosMda = query.getResultList().size();
						if(totalContratosMda == null){
							totalContratosMda = 0;
						}
					}
				
				
				if(i > 0){
					//Sql ativos mes posterior com MDA
					//int mesPosterior = mes+1;
					SQL = "select SUM(quantidade) from pmp_dashboard_contratos where ano = "+nomeMesCal.get(Calendar.YEAR)+" and mes = "+mes+" and sigla = 'PART'";
					if(filial != null){
						SQL += " and FILIAL = "+filial;
					}
					query = manager.createNativeQuery(SQL);
					totalContratosMda = 0;
					if(query.getSingleResult() != null){
						totalContratosMda = ((BigDecimal)query.getSingleResult()).intValue();
					}
				}
				
//				if(i == 0){//mês corrente
//					SQL = "select c.id from PMP_CONTRATO c"+
//					"	where c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PART')"+
//					//"	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+
//					"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
//					"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO"+
//					"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					//" 	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+")";	
//					if(filial != null){
//						SQL += " and c.FILIAL = "+filial+"";
//					}
//				}else{
//					SQL = "select op.ID_CONTRATO, max(ag.DATA_AGENDAMENTO) from PMP_AGENDAMENTO ag, PMP_CONFIG_OPERACIONAL op"+ 
//					" where ID_CONG_OPERACIONAL = op.ID and op.ID_CONTRATO in (" +
//							"select c.id from PMP_CONTRATO c"+
//					//"	where c.IS_ATIVO is null"+
//					" where c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PART')"+
//					//"	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+
//					"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
//					"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO"+
//					//"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					//" 	and month(c.DATA_ACEITE) <= "+mes+
//					
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+"))" +
//						" and year(ag.DATA_AGENDAMENTO) <= " + (nomeMesCal.get(Calendar.YEAR)-1);
//						if(filial != null){
//							SQL += " and ag.FILIAL = "+filial+"";
//						}
//						SQL +=" group by op.ID_CONTRATO	";
//				}

//				query = manager.createNativeQuery(SQL);
//				Integer totalContratosMdaAnterior = query.getResultList().size();
//				if(totalContratosMdaAnterior != null){
//					totalContratosMda += totalContratosMdaAnterior;
//				}

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes7(totalContratosMda.toString());
					}else{
						bean.setMes7("0");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes6(totalContratosMda.toString());
					}else{
						bean.setMes6("0");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes5(totalContratosMda.toString());
					}else{
						bean.setMes5("0");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes4(totalContratosMda.toString());
					}else{
						bean.setMes4("0");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes3(totalContratosMda.toString());
					}else{
						bean.setMes3("0");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes2(totalContratosMda.toString());
					}else{
						bean.setMes2("0");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes1(totalContratosMda.toString());
					}else{
						bean.setMes1("0");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	public DashboardBean findContratosCustomer(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Contratos Customer");
			bean.setTarget("");

			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				String SQL = "";
				//if(i == 0){//mês corrente
					SQL = "select distinct c.id from PMP_CONTRATO c, PMP_CLASSIFICACAO_CONTRATO cc"+
					" where DATA_FINALIZACAO is null"+
					" and c.IS_ATIVO is null"+
					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA')"+ 
					" and c.ID_CLASSIFICACAO_CONTRATO = cc.ID"+
					" and cc.SIGLA in ('CUS', 'CUSLIGHT')"+
					" and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
					" and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
					" where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
					" and c.ID = cc.ID_CONTRATO)";
//					SQL = "select c.id from PMP_CONTRATO c"+
//					"	where c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA in ('CUS', 'CUSLIGHT'))"+
//					"	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+
//					"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
//					"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO"+
//					"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" 	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+")";	
					if(filial != null){
						SQL += " and c.FILIAL = "+filial+"";
					}
//				}else{
//					SQL = "select op.ID_CONTRATO, max(ag.DATA_AGENDAMENTO) from PMP_AGENDAMENTO ag, PMP_CONFIG_OPERACIONAL op"+ 
//						"	where ID_CONG_OPERACIONAL = op.ID and op.ID_CONTRATO in ("+
//						"	select c.id from PMP_CONTRATO c"+
//					//"	where c.IS_ATIVO is null"+
//					" where c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'CUS')"+
//					"	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+
//					"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
//					"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO"+
//					//"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" 	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+"))" +
//					" and month(ag.DATA_AGENDAMENTO) <= "+mes;
//
//					if(filial != null){
//						SQL += " and ag.FILIAL = "+filial+"";
//					}
//					SQL +=" group by op.ID_CONTRATO	";
//				}

					Integer totalContratosMda = 0;
					Query query = null;
					if(i == 0){	
						query = manager.createNativeQuery(SQL);
						totalContratosMda = query.getResultList().size();
						if(totalContratosMda == null){
							totalContratosMda = 0;
						}
					}
				if(i > 0){
					//Sql ativos mes posterior com MDA
					//int mesPosterior = mes+1;
					SQL = "select SUM(quantidade) from pmp_dashboard_contratos where ano = "+nomeMesCal.get(Calendar.YEAR)+" and mes = "+mes+" and sigla = 'CUS'";
					if(filial != null){
						SQL += " and FILIAL = "+filial;
					}
					query = manager.createNativeQuery(SQL);
					totalContratosMda = 0;
					if(query.getSingleResult() != null){
						totalContratosMda = ((BigDecimal)query.getSingleResult()).intValue();
					}
				}
//				if(i == 0){//mês corrente
//					SQL = "select c.id from PMP_CONTRATO c"+
//					"	where c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA in ('CUS', 'CUSLIGHT'))"+
//					//"	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+
//					"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
//					"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO"+
//					"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					//" 	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+")";
//					if(filial != null){
//						SQL += " and c.FILIAL = "+filial+"";
//					}
//				}else{
//					SQL = "select op.ID_CONTRATO, max(ag.DATA_AGENDAMENTO) from PMP_AGENDAMENTO ag, PMP_CONFIG_OPERACIONAL op "+
//					" where ID_CONG_OPERACIONAL = op.ID and op.ID_CONTRATO in ("+
//					"		select c.id from PMP_CONTRATO c"+
//					//"	where c.IS_ATIVO is null"+
//					" where c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'CUS')"+
//					//"	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+
//					"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
//					"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO"+
//					//"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					//" 	and month(c.DATA_ACEITE) <= "+mes+
//					
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+"))" +
//					" and year(ag.DATA_AGENDAMENTO) <= " + (nomeMesCal.get(Calendar.YEAR)-1);
//					if(filial != null){
//						SQL += " and ag.FILIAL = "+filial+"";
//					}
//					SQL +=" group by op.ID_CONTRATO	";
//					
//				}
//
//				query = manager.createNativeQuery(SQL);
//				Integer totalContratosMdaAnterior = query.getResultList().size();
//				if(totalContratosMdaAnterior != null){
//					totalContratosMda += totalContratosMdaAnterior;
//				}

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes7(totalContratosMda.toString());
					}else{
						bean.setMes7("0");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes6(totalContratosMda.toString());
					}else{
						bean.setMes6("0");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes5(totalContratosMda.toString());
					}else{
						bean.setMes5("0");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes4(totalContratosMda.toString());
					}else{
						bean.setMes4("0");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes3(totalContratosMda.toString());
					}else{
						bean.setMes3("0");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes2(totalContratosMda.toString());
					}else{
						bean.setMes2("0");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes1(totalContratosMda.toString());
					}else{
						bean.setMes1("0");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	public DashboardBean findContratosPremium(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Contratos Premium");
			bean.setTarget("");

			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				String SQL = "";
				//if(i == 0){//mês corrente
					
					SQL = "select distinct c.id from PMP_CONTRATO c, PMP_CLASSIFICACAO_CONTRATO cc"+
						" where DATA_FINALIZACAO is null"+
						" and c.IS_ATIVO is null"+
						" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA')"+ 
						" and c.ID_CLASSIFICACAO_CONTRATO = cc.ID"+
						" and cc.SIGLA in ('PRE')"+
						" and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
						" where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
						" and c.ID = cc.ID_CONTRATO)"+
						" and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))";
					
//					SQL = "select c.id from PMP_CONTRATO c"+
//					"	where c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PRE')"+
//					"	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+
//					"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
//					"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO"+
//					"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" 	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+")";		
					if(filial != null){
						SQL += " and c.FILIAL = "+filial+"";
					}
//				}else{
//					SQL = "select op.ID_CONTRATO, max(ag.DATA_AGENDAMENTO) from PMP_AGENDAMENTO ag, PMP_CONFIG_OPERACIONAL op"+ 
//					"	where ID_CONG_OPERACIONAL = op.ID and op.ID_CONTRATO in ("+
//					"	select c.id from PMP_CONTRATO c"+
//					//"	where c.IS_ATIVO is null"+
//					" where c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PRE')"+
//					"	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+
//					"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
//					"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO"+
//					//"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" 	and month(c.DATA_ACEITE) <= "+mes+					
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+"))" +
//					" and month(ag.DATA_AGENDAMENTO) <= "+mes;
//					if(filial != null){
//						SQL += " and ag.FILIAL = "+filial+"";
//					}
//					SQL +=" group by op.ID_CONTRATO	";
//				}

					Integer totalContratosMda = 0;
					Query query = null;
					if(i == 0){	
						query = manager.createNativeQuery(SQL);
						totalContratosMda = query.getResultList().size();
						if(totalContratosMda == null){
							totalContratosMda = 0;
						}
					}
				
				if(i > 0){
					//Sql ativos mes posterior com MDA
					//int mesPosterior = mes+1;
					SQL = "select SUM(quantidade) from pmp_dashboard_contratos where ano = "+nomeMesCal.get(Calendar.YEAR)+" and mes = "+mes+" and sigla = 'PRE'";
					if(filial != null){
						SQL += " and FILIAL = "+filial;
					}
					query = manager.createNativeQuery(SQL);
					totalContratosMda = 0;
					if(query.getSingleResult() != null){
						totalContratosMda = ((BigDecimal)query.getSingleResult()).intValue();
					}
				}
//				if(i == 0){//mês corrente
//				SQL = "select c.id from PMP_CONTRATO c"+
//						"	where c.IS_ATIVO is null"+
//						" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//						" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PRE')"+
//						//"	and month(c.DATA_ACEITE) <= "+mes+
//						"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+
//						"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
//						"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//						"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//						"	and c.ID = cc.ID_CONTRATO"+
//						"  and c.IS_ATIVO is null"+
//						" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//						//" 	and month(c.DATA_ACEITE) <= "+mes+
//						"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+")";
//				if(filial != null){
//					SQL += " and c.FILIAL = "+filial;
//				}
//
//				}else{
//					SQL = "select op.ID_CONTRATO, max(ag.DATA_AGENDAMENTO) from PMP_AGENDAMENTO ag, PMP_CONFIG_OPERACIONAL op"+ 
//					"	where ID_CONG_OPERACIONAL = op.ID and op.ID_CONTRATO in ("+
//					"	select c.id from PMP_CONTRATO c"+
//					//"	where c.IS_ATIVO is null"+
//					" where c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PRE')"+
//					//"	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+
//					"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
//					"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO"+
//					//"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					//" 	and month(c.DATA_ACEITE) <= "+mes+					
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+"))" +
//					" and year(ag.DATA_AGENDAMENTO) <=" + (nomeMesCal.get(Calendar.YEAR)-1);
//					if(filial != null){
//						SQL += " and ag.FILIAL = "+filial+"";
//					}
//					SQL +=" group by op.ID_CONTRATO	";
//				}
//				
//				query = manager.createNativeQuery(SQL);
//				Integer totalContratosMdaAnterior = query.getResultList().size();
//				if(totalContratosMdaAnterior != null){
//					totalContratosMda += totalContratosMdaAnterior;
//				}

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes7(totalContratosMda.toString());
					}else{
						bean.setMes7("0");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes6(totalContratosMda.toString());
					}else{
						bean.setMes6("0");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes5(totalContratosMda.toString());
					}else{
						bean.setMes5("0");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes4(totalContratosMda.toString());
					}else{
						bean.setMes4("0");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes3(totalContratosMda.toString());
					}else{
						bean.setMes3("0");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes2(totalContratosMda.toString());
					}else{
						bean.setMes2("0");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes1(totalContratosMda.toString());
					}else{
						bean.setMes1("0");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	public DashboardBean findContratosPlus(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Contratos Plus");
			bean.setTarget("");

			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				String SQL = "";
				SQL = "select distinct c.id from PMP_CONTRATO c, PMP_CLASSIFICACAO_CONTRATO cc"+
				" where DATA_FINALIZACAO is null"+
				" and c.IS_ATIVO is null"+
				" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA')"+ 
				" and c.ID_CLASSIFICACAO_CONTRATO = cc.ID"+
				" and cc.SIGLA in ('PLUS')"+
				" and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
				" where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
				" and c.ID = cc.ID_CONTRATO)"+
				" and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))";
//				if(i == 0){//mês corrente
//					SQL = "select c.id from PMP_CONTRATO c"+
//					"	where c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PLUS')"+
//					"	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+
//					"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
//					"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO"+
//					"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" 	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+")";
//					if(filial != null){
//						SQL += " and c.FILIAL = "+filial+"";
//					}
//
//				}else{
//					SQL = "select op.ID_CONTRATO, max(ag.DATA_AGENDAMENTO) from PMP_AGENDAMENTO ag, PMP_CONFIG_OPERACIONAL op" +
//					"		where ID_CONG_OPERACIONAL = op.ID and op.ID_CONTRATO in ("+
//					"		select c.id from PMP_CONTRATO c"+
//					//"	where c.IS_ATIVO is null"+
//					" where c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PLUS')"+
//					"	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+
//					"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
//					"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO"+
//					//"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" 	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+"))" +
//					"  and month(ag.DATA_AGENDAMENTO) <= "+mes;
					if(filial != null){
						SQL += " and c.FILIAL = "+filial+"";
					}
//					SQL +=" group by op.ID_CONTRATO	";
//				}
					Integer totalContratosMda = 0;
					Query query = null;
					if(i == 0){	
						query = manager.createNativeQuery(SQL);
						totalContratosMda = query.getResultList().size();
						if(totalContratosMda == null){
							totalContratosMda = 0;
						}
					}

				if(i > 0){
					//Sql ativos mes posterior com MDA
					//int mesPosterior = mes+1;
					SQL = "select SUM(quantidade) from pmp_dashboard_contratos where ano = "+nomeMesCal.get(Calendar.YEAR)+" and mes = "+mes+" and sigla = 'PLUS'";
					if(filial != null){
						SQL += " and FILIAL = "+filial;
					}
					query = manager.createNativeQuery(SQL);
					totalContratosMda = 0;
					if(query.getSingleResult() != null){
						totalContratosMda = ((BigDecimal)query.getSingleResult()).intValue();
					}
				}
//				if(i == 0){//mês corrente
//					SQL = "select c.id from PMP_CONTRATO c"+
//					"	where c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PLUS')"+
//					//"	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+
//					"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
//					"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO"+
//					"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					//" 	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+")";	
//					if(filial != null){
//						SQL += " and c.FILIAL = "+filial+"";
//					}
//				}else{
//					SQL = "select op.ID_CONTRATO, max(ag.DATA_AGENDAMENTO) from PMP_AGENDAMENTO ag, PMP_CONFIG_OPERACIONAL op" +
//					"		where ID_CONG_OPERACIONAL = op.ID and op.ID_CONTRATO in ("+
//					"		select c.id from PMP_CONTRATO c"+
//					//"	where c.IS_ATIVO is null"+
//					" where c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PLUS')"+
//					//"	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+
//					"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
//					"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
//					"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
//					"	and c.ID = cc.ID_CONTRATO"+
//					//"  and c.IS_ATIVO is null"+
//					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
//					//" 	and month(c.DATA_ACEITE) <= "+mes+
//					"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+"))" +
//					" and year(ag.DATA_AGENDAMENTO) <= "+ (nomeMesCal.get(Calendar.YEAR)-1);
//					
//					if(filial != null){
//						SQL += " and ag.FILIAL = "+filial+"";
//					}
//					SQL +=" group by op.ID_CONTRATO	";
//				}

//				query = manager.createNativeQuery(SQL);
//				Integer totalContratosMdaAnterior = query.getResultList().size();
//				if(totalContratosMdaAnterior != null){
//					totalContratosMda += totalContratosMdaAnterior;
//				}

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes7(totalContratosMda.toString());
					}else{
						bean.setMes7("0");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes6(totalContratosMda.toString());
					}else{
						bean.setMes6("0");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes5(totalContratosMda.toString());
					}else{
						bean.setMes5("0");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes4(totalContratosMda.toString());
					}else{
						bean.setMes4("0");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes3(totalContratosMda.toString());
					}else{
						bean.setMes3("0");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes2(totalContratosMda.toString());
					}else{
						bean.setMes2("0");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes1(totalContratosMda.toString());
					}else{
						bean.setMes1("0");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	public DashboardBean findContratosTotal(Long filial ) {
		EntityManager manager = null;

		DashboardBean bean = new DashboardBean();
		try {
			Calendar calendar = Calendar.getInstance();
			int mes = calendar.get(Calendar.MONTH)+1;
			bean.setOperacao("Contratos Plus");
			bean.setTarget("");

			Calendar nomeMesCal = Calendar.getInstance();
			manager = JpaUtil.getInstance();
			for(int i = 0; i < 7; i++){
				nomeMesCal = Calendar.getInstance();
				if(i > 0){
					nomeMesCal.add(Calendar.MONTH, -i);
				}
				String SQL = "select COUNT(*) from PMP_CONTRATO c"+
						"	where c.IS_ATIVO is null"+
						" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
						" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PLUS')"+
						"	and month(c.DATA_ACEITE) <= "+mes+
						"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+
						"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
						"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
						"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
						"	and c.ID = cc.ID_CONTRATO"+
						"  and c.IS_ATIVO is null"+
						" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
						" 	and month(c.DATA_ACEITE) <= "+mes+
						"	and year(c.DATA_ACEITE) = " + nomeMesCal.get(Calendar.YEAR)+")";		 


				if(filial != null){
					SQL += " and c.FILIAL = "+filial;
				}
				Query query = manager.createNativeQuery(SQL);
				Integer totalContratosMda = (Integer)query.getSingleResult();
				if(totalContratosMda == null){
					totalContratosMda = 0;
				}

				SQL = "select COUNT(*) from PMP_CONTRATO c"+
						"	where c.IS_ATIVO is null"+
						" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
						" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PLUS')"+
						//"	and month(c.DATA_ACEITE) <= "+mes+
						"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+
						"	and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
						"	and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
						"	where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
						"	and c.ID = cc.ID_CONTRATO"+
						"  and c.IS_ATIVO is null"+
						" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
						//" 	and month(c.DATA_ACEITE) <= "+mes+
						"	and year(c.DATA_ACEITE) <= " + (nomeMesCal.get(Calendar.YEAR)-1)+")";		 


				if(filial != null){
					SQL += " and c.FILIAL = "+filial;
				}
				query = manager.createNativeQuery(SQL);
				Integer totalContratosMdaAnterior = (Integer)query.getSingleResult();
				if(totalContratosMdaAnterior != null){
					totalContratosMda += totalContratosMdaAnterior;
				}

				//Faz os cálculos
				if(i == 0){
					bean.setNomeMes7((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes7(totalContratosMda.toString());
					}else{
						bean.setMes7("0");
					}
				}
				if(i == 1){
					bean.setNomeMes6((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes6(totalContratosMda.toString());
					}else{
						bean.setMes6("0");
					}

				}
				if(i == 2){
					bean.setNomeMes5((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes5(totalContratosMda.toString());
					}else{
						bean.setMes5("0");
					}

				}
				if(i == 3){
					bean.setNomeMes4((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes4(totalContratosMda.toString());
					}else{
						bean.setMes4("0");
					}

				}
				if(i == 4){
					bean.setNomeMes3((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes3(totalContratosMda.toString());
					}else{
						bean.setMes3("0");
					}

				}
				if(i == 5){
					bean.setNomeMes2((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes2(totalContratosMda.toString());
					}else{
						bean.setMes2("0");
					}
				}
				if(i == 6){
					bean.setNomeMes1((nomeMesCal.get(Calendar.MONTH)+1)+"/"+nomeMesCal.get(Calendar.YEAR));
					if(totalContratosMda != null && totalContratosMda > 0){
						bean.setMes1(totalContratosMda.toString());
					}else{
						bean.setMes1("0");
					}
				}
				if((mes - 1) > 0){
					mes = mes - 1;
				} else {
					mes = 12;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}

	public DashboardMaquinasBean findAllIndicadoresMda(Long filial ) {
		EntityManager manager = null;

		DashboardMaquinasBean bean = new DashboardMaquinasBean();
		try {
			bean.setOperacao("Contratos MDA");

			manager = JpaUtil.getInstance();
			String SQL = "select ID from PMP_TIPO_CUSTOMIZACAO tc where tc.DESCRICAO like  '%COM MDA%'";
			Query query = manager.createNativeQuery(SQL);

			List<Long> idMdaList = (List<Long>)query.getResultList();
			String inList = idMdaList.toString().replace("[", "").replace("]", "");


			SQL = "select c.NUMERO_SERIE, (((select MIN(HORAS_MANUTENCAO) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_EXECUTADO = 'N')  "+
					" - (select horimetro from PMP_MAQUINA_PL pl1 where ID = (select MAX(id) from PMP_MAQUINA_PL where NUMERO_SERIE =c.NUMERO_SERIE and HORIMETRO is not null)))) as horas"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s, PMP_CONTRATO_CUSTOMIZACAO cc, PMP_TIPO_CUSTOMIZACAO tc"+
					" where c.IS_ATIVO is null "+
					" and tc.ID in ("+inList+")"+
					" and  c.ID = cc.ID_CONTRATO"+
					" and tc.ID = cc.ID_TIPO_CUSTOMIZACAO"+
					" and c.ID_STATUS_CONTRATO = s.ID"+
					" and s.SIGLA = 'CA'";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			SQL+= " order by horas";

			query = manager.createNativeQuery(SQL);
			List<Object[]> horasMdaList = (List<Object[]>)query.getResultList();

			for (Object[] objects : horasMdaList) {

				if((BigDecimal)objects[1]==null){
					bean.setTotalHorimetroZerado(bean.getTotalHorimetroZerado()+1);
				}else if(((BigDecimal)objects[1]).longValue()< 0){
					bean.setTotalRevisaoVencidas(bean.getTotalRevisaoVencidas()+1);
					if(((BigDecimal)objects[1]).longValue() < -100){
						bean.setTotalMenor100(bean.getTotalMenor100()+1);
					}
				}else if(((BigDecimal)objects[1]).longValue() >0){
					break;
				}
			}

			SQL = "	select  count(c.NUMERO_SERIE)from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s, PMP_CONTRATO_CUSTOMIZACAO cc, PMP_TIPO_CUSTOMIZACAO tc"+
					" where c.IS_ATIVO is null "+
					" and tc.ID in ("+inList+")"+
					" and  c.ID = cc.ID_CONTRATO"+
					" and tc.ID = cc.ID_TIPO_CUSTOMIZACAO"+
					" and c.ID_STATUS_CONTRATO = s.ID"+
					" and s.SIGLA = 'CA'";
			query = manager.createNativeQuery(SQL);
			Integer totalContratos = (Integer)query.getSingleResult();
			bean.setTotalContratos(totalContratos);

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}

	public DashboardMaquinasBean findAllIndicadoresPartner(Long filial ) {
		EntityManager manager = null;

		DashboardMaquinasBean bean = new DashboardMaquinasBean();
		try {
			bean.setOperacao("Contratos Partner");

			manager = JpaUtil.getInstance();
			String SQL = "select COUNT(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
					" where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
					" and c.ID = cc.ID_CONTRATO"+
					" and c.IS_ATIVO is null)"+
					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
					" and c.ID_STATUS_CONTRATO = s.ID"+
					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PART')"+
					" and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
					" and s.SIGLA = 'CA'"+
					" and (((select MIN(HORAS_MANUTENCAO) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_EXECUTADO = 'N')  "+
					" - (select horimetro from PMP_MAQUINA_PL pl1 where ID = (select MAX(id) from PMP_MAQUINA_PL where NUMERO_SERIE =c.NUMERO_SERIE and HORIMETRO is not null)))) < 0";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			Query query = manager.createNativeQuery(SQL);

			bean.setTotalRevisaoVencidas((Integer)query.getSingleResult());

			SQL = "select COUNT(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
					" where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
					" and c.ID = cc.ID_CONTRATO"+
					" and c.IS_ATIVO is null)"+
					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
					" and c.ID_STATUS_CONTRATO = s.ID"+
					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PART')"+
					" and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
					" and s.SIGLA = 'CA'"+
					" and (((select MIN(HORAS_MANUTENCAO) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_EXECUTADO = 'N')  "+
					" - (select horimetro from PMP_MAQUINA_PL pl1 where ID = (select MAX(id) from PMP_MAQUINA_PL where NUMERO_SERIE =c.NUMERO_SERIE and HORIMETRO is not null)))) < -100";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			query = manager.createNativeQuery(SQL);
			bean.setTotalMenor100((Integer)query.getSingleResult());

			SQL = "select COUNT(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
					" where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
					" and c.ID = cc.ID_CONTRATO"+
					" and c.IS_ATIVO is null)"+
					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
					" and c.ID_STATUS_CONTRATO = s.ID"+
					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PART')"+
					" and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
					" and s.SIGLA = 'CA'"+
					" and (((select MIN(HORAS_MANUTENCAO) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_EXECUTADO = 'N')  "+
					" - (select horimetro from PMP_MAQUINA_PL pl1 where ID = (select MAX(id) from PMP_MAQUINA_PL where NUMERO_SERIE =c.NUMERO_SERIE and HORIMETRO is not null)))) is null";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			query = manager.createNativeQuery(SQL);
			bean.setTotalHorimetroZerado((Integer)query.getSingleResult());

			SQL = "select COUNT(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
					" where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
					" and c.ID = cc.ID_CONTRATO"+
					" and c.IS_ATIVO is null)"+
					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
					" and c.ID_STATUS_CONTRATO = s.ID"+
					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PART')"+
					" and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
					" and s.SIGLA = 'CA'";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			query = manager.createNativeQuery(SQL);
			bean.setTotalContratos((Integer)query.getSingleResult());



		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}

	public DashboardMaquinasBean findAllIndicadoresRental(Long filial ) {
		EntityManager manager = null;

		DashboardMaquinasBean bean = new DashboardMaquinasBean();
		try {
			bean.setOperacao("Contratos Rental");

			manager = JpaUtil.getInstance();
			String SQL = "select count(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID_TIPO_CONTRATO = (select ID from PMP_TIPO_CONTRATO where SIGLA = 'REN')"+
					" and (((select MIN(HORAS_MANUTENCAO) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_EXECUTADO = 'N')  "+
					" - (select horimetro from PMP_MAQUINA_PL pl1 where ID = (select MAX(id) from PMP_MAQUINA_PL where NUMERO_SERIE =c.NUMERO_SERIE and HORIMETRO is not null)))) < 0"+
					" and s.SIGLA = 'CA'"+
					" and s.ID = c.ID_STATUS_CONTRATO";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			Query query = manager.createNativeQuery(SQL);

			bean.setTotalRevisaoVencidas((Integer)query.getSingleResult());

			SQL = "select count(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID_TIPO_CONTRATO = (select ID from PMP_TIPO_CONTRATO where SIGLA = 'REN')"+
					" and (((select MIN(horas_manutencao) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_EXECUTADO = 'N')  "+
					" - (select horimetro from PMP_MAQUINA_PL pl1 where ID = (select MAX(id) from PMP_MAQUINA_PL where NUMERO_SERIE =c.NUMERO_SERIE and HORIMETRO is not null)))) < -100"+
					" and s.SIGLA = 'CA'"+
					" and s.ID = c.ID_STATUS_CONTRATO";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}



			query = manager.createNativeQuery(SQL);
			bean.setTotalMenor100((Integer)query.getSingleResult());

			SQL = "select count(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID_TIPO_CONTRATO = (select ID from PMP_TIPO_CONTRATO where SIGLA = 'REN')"+
					" and (((select MIN(HORAS_MANUTENCAO) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_EXECUTADO = 'N')  "+
					" - (select horimetro from PMP_MAQUINA_PL pl1 where ID = (select MAX(id) from PMP_MAQUINA_PL where NUMERO_SERIE =c.NUMERO_SERIE and HORIMETRO is not null)))) is null"+
					" and s.SIGLA = 'CA'"+
					" and s.ID = c.ID_STATUS_CONTRATO";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}



			query = manager.createNativeQuery(SQL);
			bean.setTotalHorimetroZerado((Integer)query.getSingleResult());

			SQL = "	select count(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID_TIPO_CONTRATO = (select ID from PMP_TIPO_CONTRATO where SIGLA = 'REN')"+
					" and s.SIGLA = 'CA'"+
					" and s.ID = c.ID_STATUS_CONTRATO";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}



			query = manager.createNativeQuery(SQL);
			bean.setTotalContratos((Integer)query.getSingleResult());



		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	
	public DashboardMaquinasBean findAllIndicadoresCustomer(Long filial ) {
		EntityManager manager = null;

		DashboardMaquinasBean bean = new DashboardMaquinasBean();
		try {
			bean.setOperacao("Contratos Customer");

			manager = JpaUtil.getInstance();
			String SQL = "select COUNT(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
					" where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
					" and c.ID = cc.ID_CONTRATO"+
					" and c.IS_ATIVO is null)"+
					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
					" and c.ID_STATUS_CONTRATO = s.ID"+
					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'CUS')"+
					" and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
					" and s.SIGLA = 'CA'"+
					" and (((select MIN(HORAS_MANUTENCAO) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_EXECUTADO = 'N')  "+
					" - (select horimetro from PMP_MAQUINA_PL pl1 where ID = (select MAX(id) from PMP_MAQUINA_PL where NUMERO_SERIE =c.NUMERO_SERIE and HORIMETRO is not null)))) < 0";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			Query query = manager.createNativeQuery(SQL);

			bean.setTotalRevisaoVencidas((Integer)query.getSingleResult());

			SQL = "select COUNT(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
					" where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
					" and c.ID = cc.ID_CONTRATO"+
					" and c.IS_ATIVO is null)"+
					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
					" and c.ID_STATUS_CONTRATO = s.ID"+
					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'CUS')"+
					" and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
					" and s.SIGLA = 'CA'"+
					" and (((select MIN(HORAS_MANUTENCAO) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_EXECUTADO = 'N')  "+
					" - (select horimetro from PMP_MAQUINA_PL pl1 where ID = (select MAX(id) from PMP_MAQUINA_PL where NUMERO_SERIE =c.NUMERO_SERIE and HORIMETRO is not null)))) < -100";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			query = manager.createNativeQuery(SQL);
			bean.setTotalMenor100((Integer)query.getSingleResult());

			SQL = "select COUNT(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
					" where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
					" and c.ID = cc.ID_CONTRATO"+
					" and c.IS_ATIVO is null)"+
					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
					" and c.ID_STATUS_CONTRATO = s.ID"+
					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'CUS')"+
					" and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
					" and s.SIGLA = 'CA'"+
					" and (((select MIN(HORAS_MANUTENCAO) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_EXECUTADO = 'N')  "+
					" - (select horimetro from PMP_MAQUINA_PL pl1 where ID = (select MAX(id) from PMP_MAQUINA_PL where NUMERO_SERIE =c.NUMERO_SERIE and HORIMETRO is not null)))) is null";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			query = manager.createNativeQuery(SQL);
			bean.setTotalHorimetroZerado((Integer)query.getSingleResult());

			SQL = "select COUNT(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
					" where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
					" and c.ID = cc.ID_CONTRATO"+
					" and c.IS_ATIVO is null)"+
					" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
					" and c.ID_STATUS_CONTRATO = s.ID"+
					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'CUS')"+
					" and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
					" and s.SIGLA = 'CA'";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			query = manager.createNativeQuery(SQL);
			bean.setTotalContratos((Integer)query.getSingleResult());



		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	
	public DashboardMaquinasBean findAllIndicadoresPremium(Long filial ) {
		EntityManager manager = null;

		DashboardMaquinasBean bean = new DashboardMaquinasBean();
		try {
			bean.setOperacao("Contratos Premium");

			manager = JpaUtil.getInstance();
			String SQL = "select COUNT(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
					" where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
					" and c.ID = cc.ID_CONTRATO"+
					" and c.IS_ATIVO is null)"+
					//" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
					" and c.ID_STATUS_CONTRATO = s.ID"+
					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PRE')"+
					" and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
					" and s.SIGLA = 'CA'"+
					" and (((select MIN(HORAS_MANUTENCAO) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_EXECUTADO = 'N')  "+
					" - (select horimetro from PMP_MAQUINA_PL pl1 where ID = (select MAX(id) from PMP_MAQUINA_PL where NUMERO_SERIE =c.NUMERO_SERIE and HORIMETRO is not null)))) < 0";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			Query query = manager.createNativeQuery(SQL);

			bean.setTotalRevisaoVencidas((Integer)query.getSingleResult());

			SQL = "select COUNT(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
					" where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
					" and c.ID = cc.ID_CONTRATO"+
					" and c.IS_ATIVO is null)"+
					//" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
					" and c.ID_STATUS_CONTRATO = s.ID"+
					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PRE')"+
					" and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
					" and s.SIGLA = 'CA'"+
					" and (((select MIN(HORAS_MANUTENCAO) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_EXECUTADO = 'N')  "+
					" - (select horimetro from PMP_MAQUINA_PL pl1 where ID = (select MAX(id) from PMP_MAQUINA_PL where NUMERO_SERIE =c.NUMERO_SERIE and HORIMETRO is not null)))) < -100";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			query = manager.createNativeQuery(SQL);
			bean.setTotalMenor100((Integer)query.getSingleResult());

			SQL = "select COUNT(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
					" where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
					" and c.ID = cc.ID_CONTRATO"+
					" and c.IS_ATIVO is null)"+
					//" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
					" and c.ID_STATUS_CONTRATO = s.ID"+
					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PRE')"+
					" and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
					" and s.SIGLA = 'CA'"+
					" and (((select MIN(HORAS_MANUTENCAO) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_EXECUTADO = 'N')  "+
					" - (select horimetro from PMP_MAQUINA_PL pl1 where ID = (select MAX(id) from PMP_MAQUINA_PL where NUMERO_SERIE =c.NUMERO_SERIE and HORIMETRO is not null)))) is null";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			query = manager.createNativeQuery(SQL);
			bean.setTotalHorimetroZerado((Integer)query.getSingleResult());

			SQL = "select COUNT(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID not in (select distinct ID_CONTRATO from PMP_CONTRATO_CUSTOMIZACAO cc, PMP_CONTRATO c"+
					" where cc.ID_TIPO_CUSTOMIZACAO in (select ID from PMP_TIPO_CUSTOMIZACAO where DESCRICAO like '%COM MDA%')"+
					" and c.ID = cc.ID_CONTRATO"+
					" and c.IS_ATIVO is null)"+
					//" and c.ID_STATUS_CONTRATO = (select s.id from pmp_status_contrato s where s.sigla = 'CA') "+
					" and c.ID_STATUS_CONTRATO = s.ID"+
					" and c.ID_CLASSIFICACAO_CONTRATO = (select id from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PRE')"+
					" and c.ID_TIPO_CONTRATO not in (select ID from PMP_TIPO_CONTRATO where SIGLA in ('REN'))"+
					" and s.SIGLA = 'CA'";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			query = manager.createNativeQuery(SQL);
			bean.setTotalContratos((Integer)query.getSingleResult());



		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}

	public DashboardMaquinasBean findAllIndicadoresPlus(Long filial ) {
		EntityManager manager = null;
		
		DashboardMaquinasBean bean = new DashboardMaquinasBean();
		try {
			bean.setOperacao("Contratos Plus");
			
			manager = JpaUtil.getInstance();
			String SQL = "select count(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID_CLASSIFICACAO_CONTRATO = (select ID from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PLUS')"+
					" and (((select MIN(HORAS_MANUTENCAO) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_EXECUTADO = 'N')  "+
					" - (select horimetro from PMP_MAQUINA_PL pl1 where ID = (select MAX(id) from PMP_MAQUINA_PL where NUMERO_SERIE =c.NUMERO_SERIE and HORIMETRO is not null)))) < 0"+
					" and s.SIGLA = 'CA'" +
					" and s.ID = c.ID_STATUS_CONTRATO";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			Query query = manager.createNativeQuery(SQL);
			
			bean.setTotalRevisaoVencidas((Integer)query.getSingleResult());
			
			SQL = "select count(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID_CLASSIFICACAO_CONTRATO = (select ID from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PLUS')"+
					" and (((select MIN(HORAS_MANUTENCAO) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_EXECUTADO = 'N')  "+
					" - (select horimetro from PMP_MAQUINA_PL pl1 where ID = (select MAX(id) from PMP_MAQUINA_PL where NUMERO_SERIE =c.NUMERO_SERIE and HORIMETRO is not null)))) < -100"+
					" and s.SIGLA = 'CA'" +
					" and s.ID = c.ID_STATUS_CONTRATO";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			
			
			
			query = manager.createNativeQuery(SQL);
			bean.setTotalMenor100((Integer)query.getSingleResult());
			
			SQL = "select count(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID_CLASSIFICACAO_CONTRATO = (select ID from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PLUS')"+
					" and (((select MIN(HORAS_MANUTENCAO) from PMP_CONT_HORAS_STANDARD where ID_CONTRATO = c.id and IS_EXECUTADO = 'N')  "+
					" - (select horimetro from PMP_MAQUINA_PL pl1 where ID = (select MAX(id) from PMP_MAQUINA_PL where NUMERO_SERIE =c.NUMERO_SERIE and HORIMETRO is not null)))) is null"+
					" and s.SIGLA = 'CA'" +
					" and s.ID = c.ID_STATUS_CONTRATO";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			
			
			
			query = manager.createNativeQuery(SQL);
			bean.setTotalHorimetroZerado((Integer)query.getSingleResult());
			
			SQL = "	select count(*)"+
					" from  PMP_CONTRATO c, PMP_STATUS_CONTRATO s"+
					" where c.IS_ATIVO is null "+
					" and c.ID_CLASSIFICACAO_CONTRATO = (select ID from PMP_CLASSIFICACAO_CONTRATO where SIGLA = 'PLUS')"+
					" and s.SIGLA = 'CA'" +
					" and s.ID = c.ID_STATUS_CONTRATO";
			if(filial != null){
				SQL += " and c.FILIAL = "+filial;
			}
			
			
			
			query = manager.createNativeQuery(SQL);
			bean.setTotalContratos((Integer)query.getSingleResult());
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bean;
	}
	
	


	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 4);
		System.out.println(calendar.get(Calendar.YEAR));
	}
}
