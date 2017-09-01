package com.pmp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobSendMailProximaRevisao implements Job {

	private static final String MANUTENCAO_PMP = "Dias próxima revisão";
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		EntityManager manager = null;
		EmailHelper emailHelper = new EmailHelper();
		try{

			try {
				manager = JpaUtil.getInstance();
				Query query = manager.createNativeQuery(" select C.id,"+//0
						" C.MEDIA_HORAS_MES,"+//1
						" co.FILIAL,"+//2									
						" (((select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N')  - (select max(horimetro) from pmp_maquina_pl pl where pl.numero_serie = C.Numero_Serie "+
						" and pl.horimetro is not null and pl.id = (select max(id) "+
						" from pmp_maquina_pl "+
						" where numero_serie = pl.numero_serie and horimetro is not null and HORIMETRO > 0) ))/C.MEDIA_HORAS_MES)*30 dias_pendentes," +//3
						"  C.NUMERO_SERIE," +//4
						"  C.NUMERO_CONTRATO "+//5
						" from pmp_contrato c, pmp_config_operacional co, pmp_tipo_contrato tc "+
						" where c.id_Status_Contrato = (select s.id from Pmp_Status_Contrato s where s.sigla = 'CA') "+
						" and  c.id in((select  distinct id_Contrato  from Pmp_Cont_Horas_Standard hs where id_Contrato = c.id and is_Executado = 'N'))"+
						" and co.num_os is not null "+
						" and co.id_contrato = c.id "+
				" and c.id_tipo_contrato = tc.id" +
				" and c.possui_horas_mes is not null" +
				" and c.MEDIA_HORAS_MES > 0");

				List<Object[]> obj = query.getResultList();
				for (Object[] objects : obj) {
					if(objects[3] == null){
						continue;
					}
					Double diasProxRevisao = ((BigDecimal)objects[3]).doubleValue();
					
					query = manager.createNativeQuery(" select DATEDIFF ( day , max(ag.data_agendamento), GETDATE())   from Pmp_Agendamento ag where  ag.id_cont_horas_standard in (select st.id from pmp_cont_horas_standard st where id_Contrato = "+((BigDecimal)objects[0]).longValue()+")");
					if(query.getResultList().size() > 0 && query.getResultList().get(0) != null){
						Integer dias = (Integer)query.getSingleResult();
						diasProxRevisao = diasProxRevisao - dias;
					}else{
						query = manager.createNativeQuery(" select DATEDIFF ( day , max(c.data_aceite), GETDATE())   from Pmp_contrato  c where  c.id = "+((BigDecimal)objects[0]).longValue());
						Integer dias = (Integer)query.getSingleResult();
						diasProxRevisao = diasProxRevisao - dias;
					}
					
					if(diasProxRevisao != null && diasProxRevisao.longValue() <= 10){
						query = manager.createNativeQuery("select EPLSNM, EMAIL  from tw_funcionario usu, adm_perfil_sistema_usuario psu"+
								" where psu.id_sistema = (select sis.id from adm_sistema sis where sis.sigla = 'RENPMP')"+
								" and psu.id_perfil in (select p.id from adm_perfil p where p.sigla in ('OPER', 'ADM') and p.tipo_sistema = 'RENPMP')"+
								" and usu.epidno = psu.id_tw_usuario"+
						" and usu.stn1 =:filial" );
						query.setParameter("filial", ((BigDecimal)objects[2]).intValue());
						List<Object[]> objList = query.getResultList();
						for (Object[] pair : objList) {
							emailHelper.sendSimpleMail(pair[0] +" gostariamos de informar que o equipamento de serie "+(String)objects[4]+", contrato "+(String)objects[5]+" falta apenas "+diasProxRevisao.intValue()+" dia(s) para a próxima revisão.\nFavor agendar a revisão.", MANUTENCAO_PMP, (String)pair[1]);
						}
					}
				}
				emailHelper.sendSimpleMail("Serviço de email próxima revisão PMP!", "Próxima revisão", "rodrigo@rdrsistemas.com.br");
			} catch (Exception e1) {
				File file = File.createTempFile("log", ".txt", new File("."));
				try {  
					PrintStream ps = new PrintStream(file);  
					e1.printStackTrace(ps);  
				} catch (FileNotFoundException e) {  
					e.printStackTrace();  
				}  
				emailHelper.sendSimpleMail("Erro ao executar serviço de enviar email próxima revisão PMP dias", "ERRO próxima revisão dias", "rodrigo@rdrsistemas.com.br", file);
			}
			//}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
	}

}
