
package com.pmp.business;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.InspecaoPmpBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.entity.PmpFileEt;
import com.pmp.util.JpaUtil;

public class InspencaoPmpBusiness {

	
	public List<InspecaoPmpBean> findAllInspecaoPmp(String dtEmissao, String trocarPecas, String filial, UsuarioBean bean, String campoPesquisa){
		
		EntityManager manager = null;
		List<InspecaoPmpBean> inspecaoPmp = new ArrayList<InspecaoPmpBean>();
		try {
			manager = JpaUtil.getInstance();

			String sql = "select op.tipo_operacao, op.cliente, op.numero_os, op.modelo, op.serie, op.tecnico, op.filial, " +
			"op.tipo_manutencao, op.contato, op.telefone, op.emissao, op.smu, f.descricao, op.equipamento, op.idos_palm," +
			" CASE (select et.id_os_palm from pmp_file_et et where et.id_os_palm = op.idos_palm)WHEN null THEN 'N' ELSE 'S' END as fileEt," +
			" CASE (select count(distinct (dt.status)) from OS_PALM_DT dt where dt.OS_PALM_IDOS_PALM = op.idos_palm)WHEN 1 THEN 'Não' ELSE 'Sim' END as backlog," +
			" hs.HORAS_MANUTENCAO, " +//17
			" hs.TIPO_PM  " +//18
			" from os_palm op " +
			" inner join pmp_familia f on f.id = op.id_familia, PMP_AGENDAMENTO ag, PMP_CONT_HORAS_STANDARD hs " +
			" where op.tipoinspecao like 'PM'" +
			" and ag.id = op.ID_AGENDAMENTO" +
			" and hs.ID = ag.ID_CONT_HORAS_STANDARD";
			if(!bean.getIsAdm()){
			 sql += " and op.filial =:filial";
			}
			
			sql += " AND (op.numero_os LIKE '%"+campoPesquisa.toUpperCase()+"%'";
			sql += " OR op.cliente like '%"+campoPesquisa.toUpperCase()+"%'";
			sql += " OR op.serie like '%"+campoPesquisa.toUpperCase()+"%'";
			sql += " OR op.tecnico like '%"+campoPesquisa.toUpperCase()+"%'";
			sql += " OR op.modelo like '%"+campoPesquisa.toUpperCase()+"%' )";
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			SimpleDateFormat dateFormatToday = new SimpleDateFormat("dd/MM/yyyy");
			
			if(dtEmissao == null){
				dtEmissao = dateFormatToday.format(new Date());
			}

			Date dataHoraZero = dateFormat.parse(dtEmissao + " 00:00");
			Date dataHora24 = dateFormat.parse(dtEmissao + " 23:59");
			Query query = null;
			
			if(trocarPecas.equals("Sim") && dtEmissao.equals("00/00/0000")){
				//Apenas Troca de Peças
				//sql = sql + " and op.trocar_pecas = 'S' ";
				sql =  sql + " and (select count(distinct (dt.status)) from OS_PALM_DT dt where dt.OS_PALM_IDOS_PALM = op.idos_palm) > 1 ";
				query = manager.createNativeQuery(sql);
			}else if(trocarPecas.equals("Sim") && !dtEmissao.equals("00/00/0000")){
				//Troca de Peças e Data Inicio
				//sql = sql + " and op.trocar_pecas = 'S' and op.emissao BETWEEN :dataHoraZero AND :dataHora24";				
				sql = sql + " and (select count(distinct (dt.status)) from OS_PALM_DT dt where dt.OS_PALM_IDOS_PALM = op.idos_palm) > 1 and op.emissao BETWEEN :dataHoraZero AND :dataHora24";				
				query = manager.createNativeQuery(sql);
				query.setParameter("dataHoraZero", dataHoraZero);
	            query.setParameter("dataHora24", dataHora24);	
			}else if(trocarPecas.equals("Não") && dtEmissao.equals("00/00/0000")){
				//Apenas Troca de Peças
				//sql = sql + " and op.trocar_pecas = 'N' ";
				sql = sql + " and (select count(distinct (dt.status)) from OS_PALM_DT dt where dt.OS_PALM_IDOS_PALM = op.idos_palm) = 1 ";
				query = manager.createNativeQuery(sql);
			}else if(trocarPecas.equals("Não") && !dtEmissao.equals("00/00/0000")){
				//Troca de Peças e Data Inicio
				//sql = sql + " and op.trocar_pecas = 'N' and op.emissao BETWEEN :dataHoraZero AND :dataHora24";				
				sql = sql + " and (select count(distinct (dt.status)) from OS_PALM_DT dt where dt.OS_PALM_IDOS_PALM = op.idos_palm) = 1 and op.emissao BETWEEN :dataHoraZero AND :dataHora24";				
				query = manager.createNativeQuery(sql);
				query.setParameter("dataHoraZero", dataHoraZero);
	            query.setParameter("dataHora24", dataHora24);	
			}else if(trocarPecas.equals("Selecione") && !dtEmissao.equals("00/00/0000")){
				//Apenas Troca de Peças
				sql = sql + " and op.emissao BETWEEN :dataHoraZero AND :dataHora24 ";
				query = manager.createNativeQuery(sql);
				query.setParameter("dataHoraZero", dataHoraZero);
	            query.setParameter("dataHora24", dataHora24);
				
			}else{
				query = manager.createNativeQuery(sql);
			}
			
			if(!bean.getIsAdm()){
				query.setParameter("filial", filial);
			}
			 			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
			
			List<Object[]> result = query.getResultList();
			
			for (Object [] inspecao : result) {
				InspecaoPmpBean insPmp = new InspecaoPmpBean();
				if(inspecao[0] != null){insPmp.setTipoOperacao(inspecao[0].toString());}
				insPmp.setCliente(inspecao[1].toString());
				insPmp.setNumeroOs(inspecao[2].toString());
				insPmp.setModelo(inspecao[3].toString());
				insPmp.setSerie(inspecao[4].toString());
				if(inspecao[5] != null){insPmp.setTecnico(inspecao[5].toString());}				
				insPmp.setFilial(inspecao[6].toString());
				insPmp.setTipoManutencao((String)inspecao[18]+" "+(BigDecimal)inspecao[17]);
				if(inspecao[8] != null){insPmp.setContato(inspecao[8].toString());}
				if(inspecao[9] != null){insPmp.setTelefone(inspecao[9].toString());}
				if(inspecao[10] != null){
					Date date = format.parse((String)inspecao[10]);
					insPmp.setEmissao((format2.format(date)).replaceAll("-", "/"));
					}
				if(inspecao[11] != null){insPmp.setHorimetro(inspecao[11].toString());}				
				if(inspecao[12] != null){insPmp.setFamilia(inspecao[12].toString());}
				if(inspecao[13] != null){insPmp.setEquipamento(inspecao[13].toString());}
				insPmp.setId(((BigDecimal)inspecao[14]).longValue());
				insPmp.setFileEt(((String)inspecao[15]));
				insPmp.setHaveBacklog(((String)inspecao[16]));
				inspecaoPmp.add(insPmp);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return inspecaoPmp;
	}
	
	public Boolean salvarFileEt(PmpFileEt file, Long idOsPalm) {
		
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			Query query = manager.createNativeQuery("Delete from pmp_file_et where id_os_palm = "+idOsPalm);
			query.executeUpdate();
			file.setIdOsPalm(idOsPalm);
			manager.persist(file);
			manager.getTransaction().commit();

			return true;

		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return false;
	}
}
