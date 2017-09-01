package com.pmp.business;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.json.simple.JSONObject;

import com.pmp.bean.PlMaquinaBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.entity.PmpCriticidadeManutencao;
import com.pmp.entity.PmpMapaPl;
import com.pmp.util.DateHelper;
import com.pmp.util.JpaUtil;

public class LocalizacaoMaquinaBusiness {
	private UsuarioBean usuarioBean;
	private final int MES = 30;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public LocalizacaoMaquinaBusiness() {
	}
	public LocalizacaoMaquinaBusiness(UsuarioBean usuarioBean) {
		this.usuarioBean = usuarioBean;
	}
	
	//public List<JSONObject> findAllMaquinaPl(String nivel){
	public Map findAllMaquinaLocalPl(String nivel, String campoPesquisa){
		EntityManager manager = null;
		List<JSONObject> result = new ArrayList<JSONObject>();
		List<PlMaquinaBean> resultPlMaquinaBean = new ArrayList<PlMaquinaBean>();
		Map map = new HashMap();
		try{
			manager = JpaUtil.getInstance(); // Query para exibir as máquinas indenpedente se existir chamado ou não.
			
			List<PmpCriticidadeManutencao> criticidadeManutencao = null;
			
//			if(nivel.equals("")){
			criticidadeManutencao = findAllCriticidade(manager);				
//			}else{
//				criticidadeManutencao = findAllCriticidadeBy(nivel, manager);
//			}
			
			//Apenas seta marcadores no mapa se houver cadastro de criticidade (PMP_CRITICIDADE_MANUTENCAO).
			if(criticidadeManutencao != null){			
				
//			String SQL = "select  distinct CONVERT(VARCHAR(10), p.data_atualizacao, 103) data_atualizacao, p.numero_serie,"+ 
//						 "(select pll.LATITUDE from PMP_MAQUINA_PL pll where pll.NUMERO_SERIE = c.NUMERO_SERIE and pll.DATA_ATUALIZACAO in(select distinct max(mpl.data_atualizacao)"+
//						 " from pmp_maquina_pl mpl where mpl.numero_serie = c.NUMERO_SERIE and mpl.LATITUDE is not null ) ) latitude,"+
//						 " (select pll.longitude from PMP_MAQUINA_PL pll where pll.NUMERO_SERIE = c.NUMERO_SERIE and pll.DATA_ATUALIZACAO in(select distinct max(mpl.data_atualizacao)"+
//						 " from pmp_maquina_pl mpl where mpl.numero_serie = c.NUMERO_SERIE and mpl.longitude is not null ) ) longitude,"+
//						 " (select max(pl.horimetro) from pmp_maquina_pl pl"+
//						 " where pl.numero_serie = p.numero_serie) horimetro, c.CODIGO_CLIENTE, c.RAZAO_SOCIAL,"+ 
//						 " (select twf.stnm from tw_filial twf"+ 
//						 " where twf.stno = CONVERT(INT, c.filial)) filial, c.MODELO, c.id"+ 
//						 " from PMP_CONTRATO c,pmp_maquina_pl p left join pmp_cliente_pl cpl on p.numero_serie = cpl.serie,"+
//						 " (select  max(p.id) as id, max(p.data_atualizacao) as data_atualizacao, p.numero_serie"+ 
//						 " from pmp_maquina_pl p" +
//						 " where p.LATITUDE is not null"+
//						 " group by p.numero_serie) t"+
//						 " where p.data_atualizacao in(select distinct max(mpl.data_atualizacao)"+
//						 " from pmp_maquina_pl mpl where mpl.numero_serie = p.numero_serie and mpl.LATITUDE is not null)"+ 
//						 " and t.id = p.id"+ 
//						 " and c.filial in ("+usuarioBean.getFilial()+")"+
//						 " and c.NUMERO_SERIE = p.NUMERO_SERIE"+
//						 " and c.IS_ATIVO is null" +
//						 //" and p.horimetro is not null"+
//						 " and c.ID_STATUS_CONTRATO = (select id from PMP_STATUS_CONTRATO where SIGLA = 'CA')"+
//						 " and (select pll.longitude from PMP_MAQUINA_PL pll where pll.NUMERO_SERIE = c.NUMERO_SERIE and pll.DATA_ATUALIZACAO in(select distinct max(mpl.data_atualizacao)"+
//						 " from pmp_maquina_pl mpl where mpl.numero_serie = c.NUMERO_SERIE and mpl.longitude is not null ) ) is not null";
				
				
				String SQL = "SELECT SERIE serie"+//0
							 "     ,LATITUDE latitude"+//1
							 "    ,LOGITUDE longitude"+//2
							 "     ,CODIGO_CLIENTE codigoCliente"+//3
							 "     ,NOME_CLIENTE nomeCliente"+//4
							 "     ,NOME_FILIAL filial"+//5
							 "    ,MODELO modelo"+//6
							 "     ,HORIMETRO horimetro"+//7
							 "     ,DATA_ATUALIZACAO dtAtualizacao"+//8
							 "     ,COR cor"+//9
							 "     ,FILIAL codFilial"+//10
							 "     ,ID_CONTRATO idContrato"+//11
							 " FROM PMP_MAPA_PL c"+
							 " where c.filial in ("+usuarioBean.getFilial()+")";
				if(campoPesquisa != null){
					SQL += " and  (c.MODELO like '%"+campoPesquisa+"%' or c.SERIE like '%"+campoPesquisa+"%'"+  
					" or c.NOME_CLIENTE like '%"+campoPesquisa+"%')";
				}
				if("CRITICO".equals(nivel)){
					SQL += " and (cor = 'vermelho' or cor = 'azul')";
				}else if("CRITICIDADE MEDIA".equals(nivel)){
					SQL += " and cor = 'amarelo'";
				}else if("NAO CRITICO".equals(nivel)){
					SQL += " and cor = 'verde'";
				}
				
				
				Query query = manager.createNativeQuery(SQL);
				//query.setMaxResults(3);
				//query.setFirstResult(390);
				List<Object[]> pairs = (List<Object[]>)query.getResultList();
				
				for (Object[] objects : pairs) {
					JSONObject jsonObject = new JSONObject();

					
					jsonObject.put("serie", (String)objects[0]);
					jsonObject.put("latitude", (String)objects[1]);
					jsonObject.put("longitude", (String)objects[2]);
					jsonObject.put("codigoCliente", (String)objects[3]);
					jsonObject.put("nomeCliente", (String)objects[4]);
					jsonObject.put("filial", (String)objects[5]);
					jsonObject.put("modelo", (String)objects[6]);
					jsonObject.put("horimetro", ((objects[7] != null)?objects[7]:""));
					jsonObject.put("dtAtualizacao", (String)objects[8]);
					jsonObject.put("cor", (String)objects[9]);
					jsonObject.put("idContrato", ((BigDecimal)objects[11]).longValue());
					result.add(jsonObject);
					//resultPlMaquinaBean.add(bean);
					
				}
				
//				if(pairs.size() == 0){
//					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("serie", "N/A");
//					result.add(jsonObject);
//					jsonObject = new JSONObject();
//					jsonObject.put("serie", "N/A");
//					result.add(jsonObject);
//				}
			}
			map.put("json", result);
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}	
		}
		return map;
		
	}
	//public List<JSONObject> findAllMaquinaPl(String nivel){
	public Map findAllMaquinaPl(String nivel, String campoPesquisa){
		EntityManager manager = null;
		List<JSONObject> result = new ArrayList<JSONObject>();
		List<PlMaquinaBean> resultPlMaquinaBean = new ArrayList<PlMaquinaBean>();
		Map map = new HashMap();
		try{
			manager = JpaUtil.getInstance(); // Query para exibir as máquinas indenpedente se existir chamado ou não.
			
			List<PmpCriticidadeManutencao> criticidadeManutencao = null;
			
//			if(nivel.equals("")){
				criticidadeManutencao = findAllCriticidade(manager);				
//			}else{
//				criticidadeManutencao = findAllCriticidadeBy(nivel, manager);
//			}

			//Apenas seta marcadores no mapa se houver cadastro de criticidade (PMP_CRITICIDADE_MANUTENCAO).
			if(criticidadeManutencao != null){			
			
//			String SQL = "select  distinct CONVERT(VARCHAR(10), p.data_atualizacao, 103) data_atualizacao, p.numero_serie,"+ 
//						 "(select pll.LATITUDE from PMP_MAQUINA_PL pll where pll.NUMERO_SERIE = c.NUMERO_SERIE and pll.DATA_ATUALIZACAO in(select distinct max(mpl.data_atualizacao)"+
//						 " from pmp_maquina_pl mpl where mpl.numero_serie = c.NUMERO_SERIE and mpl.LATITUDE is not null ) ) latitude,"+
//						 " (select pll.longitude from PMP_MAQUINA_PL pll where pll.NUMERO_SERIE = c.NUMERO_SERIE and pll.DATA_ATUALIZACAO in(select distinct max(mpl.data_atualizacao)"+
//						 " from pmp_maquina_pl mpl where mpl.numero_serie = c.NUMERO_SERIE and mpl.longitude is not null ) ) longitude,"+
//						 " (select max(pl.horimetro) from pmp_maquina_pl pl"+
//						 " where pl.numero_serie = p.numero_serie) horimetro, c.CODIGO_CLIENTE, c.RAZAO_SOCIAL,"+ 
//						 " (select twf.stnm from tw_filial twf"+ 
//						 " where twf.stno = CONVERT(INT, c.filial)) filial, c.MODELO, c.id"+ 
//						 " from PMP_CONTRATO c,pmp_maquina_pl p left join pmp_cliente_pl cpl on p.numero_serie = cpl.serie,"+
//						 " (select  max(p.id) as id, max(p.data_atualizacao) as data_atualizacao, p.numero_serie"+ 
//						 " from pmp_maquina_pl p" +
//						 " where p.LATITUDE is not null"+
//						 " group by p.numero_serie) t"+
//						 " where p.data_atualizacao in(select distinct max(mpl.data_atualizacao)"+
//						 " from pmp_maquina_pl mpl where mpl.numero_serie = p.numero_serie and mpl.LATITUDE is not null)"+ 
//						 " and t.id = p.id"+ 
//						 " and c.filial in ("+usuarioBean.getFilial()+")"+
//						 " and c.NUMERO_SERIE = p.NUMERO_SERIE"+
//						 " and c.IS_ATIVO is null" +
//						 //" and p.horimetro is not null"+
//						 " and c.ID_STATUS_CONTRATO = (select id from PMP_STATUS_CONTRATO where SIGLA = 'CA')"+
//						 " and (select pll.longitude from PMP_MAQUINA_PL pll where pll.NUMERO_SERIE = c.NUMERO_SERIE and pll.DATA_ATUALIZACAO in(select distinct max(mpl.data_atualizacao)"+
//						 " from pmp_maquina_pl mpl where mpl.numero_serie = c.NUMERO_SERIE and mpl.longitude is not null ) ) is not null";
				
				
				String SQL = "select distinct CONVERT(VARCHAR(10), p.data_atualizacao, 103) data_atualizacao, p.numero_serie,"+ 
				 "  p.LATITUDE, p.LONGITUDE,"+
				 " ( select pl.horimetro from pmp_maquina_pl pl"+
				 " where pl.id = (select max(ID) from PMP_MAQUINA_PL pl1 where pl1.NUMERO_SERIE = p.numero_serie and HORIMETRO is not null and HORIMETRO > 0)"+
                 " ) horimetro, c.CODIGO_CLIENTE, c.RAZAO_SOCIAL,"+ 
				 " (select twf.stnm from tw_filial twf"+ 
				 " where twf.stno = CONVERT(INT, c.filial)) filial, c.MODELO, c.id, (select SIGLA from PMP_CLASSIFICACAO_CONTRATO where ID = c.id_classificacao_contrato) siglaClassificacao"+ 
				 " from PMP_CONTRATO c,pmp_maquina_pl p left join pmp_cliente_pl cpl on p.numero_serie = cpl.serie,"+
				 " (select  max(p.id) as id, p.numero_serie"+ 
				 " from pmp_maquina_pl p" +
				 " where p.LATITUDE is not null"+
				 " group by p.numero_serie) t, PMP_CONFIG_OPERACIONAL cm"+
				 " where p.id in(select distinct max(mpl.id)"+
				 " from pmp_maquina_pl mpl where mpl.numero_serie = p.numero_serie and mpl.LATITUDE is not null)"+ 
				 " and t.id = p.id"+ 
				 " and cm.id_contrato = c.id"+ 
				 " and cm.filial in ("+usuarioBean.getFilial()+")"+
				 " and (c.IS_ATIVO is null or  (select COUNT(*) from PMP_CONT_HORAS_STANDARD where IS_EXECUTADO = 'N' and ID_CONTRATO = c.ID) > 0)"+
				 " and c.NUMERO_SERIE = p.NUMERO_SERIE";
				if(campoPesquisa != null){
					SQL += " and  (c.MODELO like '%"+campoPesquisa+"%' or c.NUMERO_SERIE like '%"+campoPesquisa+"%'"+  
					" or c.RAZAO_SOCIAL like '%"+campoPesquisa+"%')";
				}
				SQL += " union " +
				"  select  distinct CONVERT(VARCHAR(10), p.data_atualizacao, 103) data_atualizacao, p.numero_serie,"+ 
				"  p.LATITUDE, p.LONGITUDE,"+
				" ( select pl.horimetro from pmp_maquina_pl pl"+
				" where pl.id = (select max(ID) from PMP_MAQUINA_PL pl1 where pl1.NUMERO_SERIE = p.numero_serie and HORIMETRO is not null and HORIMETRO > 0)"+
				" ) horimetro, c.CODIGO_CLIENTE, c.RAZAO_SOCIAL,"+ 
				" (select twf.stnm from tw_filial twf"+ 
				" where twf.stno = CONVERT(INT, c.filial)) filial, c.MODELO, c.id, (select SIGLA from PMP_CLASSIFICACAO_CONTRATO where ID = c.id_classificacao_contrato) siglaClassificacao"+ 
				" from PMP_CONTRATO c,pmp_maquina_pl p left join pmp_cliente_pl cpl on p.numero_serie = cpl.serie,"+
				" (select  max(p.id) as id, p.numero_serie"+ 
				" from pmp_maquina_pl p" +
				" where p.LATITUDE is not null"+
				" group by p.numero_serie) t, PMP_CONFIG_OPERACIONAL cm"+
				" where p.id in(select distinct max(mpl.id)"+
				" from pmp_maquina_pl mpl where mpl.numero_serie = p.numero_serie and mpl.LATITUDE is not null)"+ 
				" and t.id = p.id"+ 
				" and cm.id_contrato = c.id"+ 
				" and cm.filial in ("+usuarioBean.getFilial()+")"+
				" and (c.IS_ATIVO is null or  (select COUNT(*) from PMP_CONT_HORAS_STANDARD_PLUS where IS_EXECUTADO = 'N' and ID_CONTRATO = c.ID) > 0)"+
				" and c.NUMERO_SERIE = p.NUMERO_SERIE";
				if(campoPesquisa != null){
					SQL += " and  (c.MODELO like '%"+campoPesquisa+"%' or c.NUMERO_SERIE like '%"+campoPesquisa+"%'"+  
					" or c.RAZAO_SOCIAL like '%"+campoPesquisa+"%')";
				}
			
			
			Query query = manager.createNativeQuery(SQL);
			//query.setMaxResults(3);
			//query.setFirstResult(390);
			List<Object[]> pairs = (List<Object[]>)query.getResultList();
	
			for (Object[] objects : pairs) {
				JSONObject jsonObject = new JSONObject();
				PlMaquinaBean bean = new PlMaquinaBean();
				//bean.setDescricao("Dt. Atualizacao: "+objects[0]+"\nSérie "+objects[1]+"\nModelo: "+(String)objects[8]+"\n"+((objects[4] != null)?"Horimetro: "+objects[4]:"")
				//	+"\nCódigo Cliente: "+(String)objects[5]+"\nNome Cliente: "+(String)objects[6]
				//  +"\nFilial: "+ (String)objects[7]);

//				if(((String)objects[1]).equals("0GBB06357")){
//					System.out.println("teste");
//				}
				String cor = null;
				if(((String)objects[10]).equals("PLUS")){
					cor = setCriticidadePlus(objects, manager, criticidadeManutencao, nivel);
				}else{
					cor = setCriticidade(objects, manager, criticidadeManutencao, nivel);
				}
				if(cor == null){
					continue;
				}else if(!nivel.equals("")){
					cor = confirmColorByFilter(nivel, cor);
					if(cor == null){
						continue;
					}
				}

				bean.setSerie((String)objects[1]);
				jsonObject.put("serie", (String)objects[1]);
				bean.setLatitude((String)objects[2]);
				jsonObject.put("latitude", (String)objects[2]);
				bean.setLongitude((String)objects[3]);
				jsonObject.put("longitude", (String)objects[3]);
				bean.setCodCliente((String)objects[5]);
				jsonObject.put("codigoCliente", (String)objects[5]);
				bean.setNomeCliente((String)objects[6]);
				jsonObject.put("nomeCliente", (String)objects[6]);
				bean.setFiial((String)objects[7]);
				jsonObject.put("filial", (String)objects[7]);
				bean.setModelo((String)objects[8]);
				jsonObject.put("modelo", (String)objects[8]);
				bean.setHorimetro(((objects[4] != null)?"Horímetro: "+objects[4]:""));
				jsonObject.put("horimetro", ((objects[4] != null)?objects[4]:""));
				bean.setDataAtualizacao((String)objects[0]);
				jsonObject.put("dtAtualizacao", (String)objects[0]);
				bean.setCor(cor);
				bean.setIdContrato(((BigDecimal)objects[9]).longValue());
				jsonObject.put("cor", cor);
				result.add(jsonObject);
				resultPlMaquinaBean.add(bean);

			}

			}
			map.put("json", result);
			map.put("PlMaquinaBean", resultPlMaquinaBean);
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}	
		}
		return map;
		
	}
	//public List<JSONObject> findAllMaquinaPl(String nivel){
	public Map findAllMaquinaMapaPl(String nivel, String campoPesquisa){
		EntityManager manager = null;
		List<JSONObject> result = new ArrayList<JSONObject>();
		List<PlMaquinaBean> resultPlMaquinaBean = new ArrayList<PlMaquinaBean>();
		Map map = new HashMap();
		try{
			manager = JpaUtil.getInstance(); // Query para exibir as máquinas indenpedente se existir chamado ou não.
			
			List<PmpCriticidadeManutencao> criticidadeManutencao = null;
			
//			if(nivel.equals("")){
			criticidadeManutencao = findAllCriticidade(manager);				
//			}else{
//				criticidadeManutencao = findAllCriticidadeBy(nivel, manager);
//			}
			
			//Apenas seta marcadores no mapa se houver cadastro de criticidade (PMP_CRITICIDADE_MANUTENCAO).
			if(criticidadeManutencao != null){			
				
//			String SQL = "select  distinct CONVERT(VARCHAR(10), p.data_atualizacao, 103) data_atualizacao, p.numero_serie,"+ 
//						 "(select pll.LATITUDE from PMP_MAQUINA_PL pll where pll.NUMERO_SERIE = c.NUMERO_SERIE and pll.DATA_ATUALIZACAO in(select distinct max(mpl.data_atualizacao)"+
//						 " from pmp_maquina_pl mpl where mpl.numero_serie = c.NUMERO_SERIE and mpl.LATITUDE is not null ) ) latitude,"+
//						 " (select pll.longitude from PMP_MAQUINA_PL pll where pll.NUMERO_SERIE = c.NUMERO_SERIE and pll.DATA_ATUALIZACAO in(select distinct max(mpl.data_atualizacao)"+
//						 " from pmp_maquina_pl mpl where mpl.numero_serie = c.NUMERO_SERIE and mpl.longitude is not null ) ) longitude,"+
//						 " (select max(pl.horimetro) from pmp_maquina_pl pl"+
//						 " where pl.numero_serie = p.numero_serie) horimetro, c.CODIGO_CLIENTE, c.RAZAO_SOCIAL,"+ 
//						 " (select twf.stnm from tw_filial twf"+ 
//						 " where twf.stno = CONVERT(INT, c.filial)) filial, c.MODELO, c.id"+ 
//						 " from PMP_CONTRATO c,pmp_maquina_pl p left join pmp_cliente_pl cpl on p.numero_serie = cpl.serie,"+
//						 " (select  max(p.id) as id, max(p.data_atualizacao) as data_atualizacao, p.numero_serie"+ 
//						 " from pmp_maquina_pl p" +
//						 " where p.LATITUDE is not null"+
//						 " group by p.numero_serie) t"+
//						 " where p.data_atualizacao in(select distinct max(mpl.data_atualizacao)"+
//						 " from pmp_maquina_pl mpl where mpl.numero_serie = p.numero_serie and mpl.LATITUDE is not null)"+ 
//						 " and t.id = p.id"+ 
//						 " and c.filial in ("+usuarioBean.getFilial()+")"+
//						 " and c.NUMERO_SERIE = p.NUMERO_SERIE"+
//						 " and c.IS_ATIVO is null" +
//						 //" and p.horimetro is not null"+
//						 " and c.ID_STATUS_CONTRATO = (select id from PMP_STATUS_CONTRATO where SIGLA = 'CA')"+
//						 " and (select pll.longitude from PMP_MAQUINA_PL pll where pll.NUMERO_SERIE = c.NUMERO_SERIE and pll.DATA_ATUALIZACAO in(select distinct max(mpl.data_atualizacao)"+
//						 " from pmp_maquina_pl mpl where mpl.numero_serie = c.NUMERO_SERIE and mpl.longitude is not null ) ) is not null";
				
				
				String SQL = "select distinct CONVERT(VARCHAR(10), p.data_atualizacao, 103) data_atualizacao, p.numero_serie,"+ 
				"  p.LATITUDE, p.LONGITUDE,"+
				" ( select pl.horimetro from pmp_maquina_pl pl"+
				" where pl.id = (select max(ID) from PMP_MAQUINA_PL pl1 where pl1.NUMERO_SERIE = p.numero_serie and HORIMETRO is not null and HORIMETRO > 0)"+
				" ) horimetro, c.CODIGO_CLIENTE, c.RAZAO_SOCIAL,"+ 
				" (select twf.stnm from tw_filial twf"+ 
				" where twf.stno = CONVERT(INT, c.filial)) filial, c.MODELO, c.id, (select SIGLA from PMP_CLASSIFICACAO_CONTRATO where ID = c.id_classificacao_contrato) siglaClassificacao, cm.filial codFilial"+ 
				" from PMP_CONTRATO c,pmp_maquina_pl p left join pmp_cliente_pl cpl on p.numero_serie = cpl.serie,"+
				" (select  max(p.id) as id, p.numero_serie"+ 
				" from pmp_maquina_pl p" +
				" where p.LATITUDE is not null"+
				" group by p.numero_serie) t, PMP_CONFIG_OPERACIONAL cm"+
				" where p.id in(select distinct max(mpl.id)"+
				" from pmp_maquina_pl mpl where mpl.numero_serie = p.numero_serie and mpl.LATITUDE is not null)"+ 
				" and t.id = p.id"+ 
				" and cm.id_contrato = c.id"+ 
				//" and cm.filial in ("+usuarioBean.getFilial()+")"+
				" and (c.IS_ATIVO is null or  (select COUNT(*) from PMP_CONT_HORAS_STANDARD where IS_EXECUTADO = 'N' and ID_CONTRATO = c.ID) > 0)"+
				" and c.NUMERO_SERIE = p.NUMERO_SERIE";
				if(campoPesquisa != null){
					SQL += " and  (c.MODELO like '%"+campoPesquisa+"%' or c.NUMERO_SERIE like '%"+campoPesquisa+"%'"+  
					" or c.RAZAO_SOCIAL like '%"+campoPesquisa+"%')";
				}
				SQL += " union " +
				"  select  distinct CONVERT(VARCHAR(10), p.data_atualizacao, 103) data_atualizacao, p.numero_serie,"+ 
				"  p.LATITUDE, p.LONGITUDE,"+
				" ( select pl.horimetro from pmp_maquina_pl pl"+
				" where pl.id = (select max(ID) from PMP_MAQUINA_PL pl1 where pl1.NUMERO_SERIE = p.numero_serie and HORIMETRO is not null and HORIMETRO > 0)"+
				" ) horimetro, c.CODIGO_CLIENTE, c.RAZAO_SOCIAL,"+ 
				" (select twf.stnm from tw_filial twf"+ 
				" where twf.stno = CONVERT(INT, c.filial)) filial, c.MODELO, c.id, (select SIGLA from PMP_CLASSIFICACAO_CONTRATO where ID = c.id_classificacao_contrato) siglaClassificacao, cm.filial codFilial"+ 
				" from PMP_CONTRATO c,pmp_maquina_pl p left join pmp_cliente_pl cpl on p.numero_serie = cpl.serie,"+
				" (select  max(p.id) as id, p.numero_serie"+ 
				" from pmp_maquina_pl p" +
				" where p.LATITUDE is not null"+
				" group by p.numero_serie) t, PMP_CONFIG_OPERACIONAL cm"+
				" where p.id in(select distinct max(mpl.id)"+
				" from pmp_maquina_pl mpl where mpl.numero_serie = p.numero_serie and mpl.LATITUDE is not null)"+ 
				" and t.id = p.id"+ 
				" and cm.id_contrato = c.id"+ 
				//" and cm.filial in ("+usuarioBean.getFilial()+")"+
				" and (c.IS_ATIVO is null or  (select COUNT(*) from PMP_CONT_HORAS_STANDARD_PLUS where IS_EXECUTADO = 'N' and ID_CONTRATO = c.ID) > 0)"+
				" and c.NUMERO_SERIE = p.NUMERO_SERIE";
				if(campoPesquisa != null){
					SQL += " and  (c.MODELO like '%"+campoPesquisa+"%' or c.NUMERO_SERIE like '%"+campoPesquisa+"%'"+  
					" or c.RAZAO_SOCIAL like '%"+campoPesquisa+"%')";
				}
				
				
				Query query = manager.createNativeQuery(SQL);
				//query.setMaxResults(3);
				//query.setFirstResult(390);
				List<Object[]> pairs = (List<Object[]>)query.getResultList();
				
				for (Object[] objects : pairs) {
					JSONObject jsonObject = new JSONObject();
					PlMaquinaBean bean = new PlMaquinaBean();
					//bean.setDescricao("Dt. Atualizacao: "+objects[0]+"\nSérie "+objects[1]+"\nModelo: "+(String)objects[8]+"\n"+((objects[4] != null)?"Horimetro: "+objects[4]:"")
					//	+"\nCódigo Cliente: "+(String)objects[5]+"\nNome Cliente: "+(String)objects[6]
					//  +"\nFilial: "+ (String)objects[7]);
					
//				if(((String)objects[1]).equals("0GBB06357")){
//					System.out.println("teste");
//				}
					String cor = null;
					if(((String)objects[10]).equals("PLUS")){
						cor = setCriticidadePlus(objects, manager, criticidadeManutencao, nivel);
					}else{
						cor = setCriticidade(objects, manager, criticidadeManutencao, nivel);
					}
					if(cor == null){
						continue;
					}else if(nivel != null && !nivel.equals("")){
						cor = confirmColorByFilter(nivel, cor);
						if(cor == null){
							continue;
						}
					}
					
					bean.setSerie((String)objects[1]);
					jsonObject.put("serie", (String)objects[1]);
					bean.setLatitude((String)objects[2]);
					jsonObject.put("latitude", (String)objects[2]);
					bean.setLongitude((String)objects[3]);
					jsonObject.put("longitude", (String)objects[3]);
					bean.setCodCliente((String)objects[5]);
					jsonObject.put("codigoCliente", (String)objects[5]);
					bean.setNomeCliente((String)objects[6]);
					jsonObject.put("nomeCliente", (String)objects[6]);
					bean.setFiial((String)objects[7]);
					jsonObject.put("filial", (String)objects[7]);
					bean.setModelo((String)objects[8]);
					jsonObject.put("modelo", (String)objects[8]);
					bean.setHorimetro(((objects[4] != null)?"Horímetro: "+objects[4]:""));
					jsonObject.put("horimetro", ((objects[4] != null)?objects[4]:""));
					bean.setDataAtualizacao((String)objects[0]);
					jsonObject.put("dtAtualizacao", (String)objects[0]);
					bean.setCor(cor);
					bean.setIdContrato(((BigDecimal)objects[9]).longValue());
					jsonObject.put("cor", cor);
					Long filial = (((BigDecimal)objects[11]).longValue());
					result.add(jsonObject);
					resultPlMaquinaBean.add(bean);
					
					PmpMapaPl mapaPl = new PmpMapaPl();
					mapaPl.setSerie((String)objects[1]);
					mapaPl.setLatitude((String)objects[2]);
					mapaPl.setLogitude((String)objects[3]);
					mapaPl.setCodigoCliente((String)objects[5]);
					mapaPl.setNomeCliente((String)objects[6]);
					mapaPl.setNomeFilial((String)objects[7]);
					mapaPl.setModelo((String)objects[8]);
					mapaPl.setHorimetro(((objects[4] != null)?((BigDecimal)objects[4]).toString():""));
					mapaPl.setDataAtualização((String)objects[0]);
					mapaPl.setCor(cor);
					mapaPl.setFilial(filial);
					mapaPl.setIdContrato(((BigDecimal)objects[9]).longValue());
					manager.getTransaction().begin();
					manager.merge(mapaPl);
					manager.getTransaction().commit();
				}
				
			}
			manager.getTransaction().begin();
			Query query = manager.createNativeQuery("delete from PMP_MAPA_PL where SERIE in(select NUMERO_SERIE from PMP_CONTRATO"+ 
											" where IS_ATIVO = 'I'"+
											" and NUMERO_SERIE not in (select NUMERO_SERIE from PMP_CONTRATO where IS_ATIVO is null and ID_STATUS_CONTRATO = ("+
										    " select id from PMP_STATUS_CONTRATO where sigla = 'CA')))");
			query.executeUpdate();
			manager.getTransaction().commit();
			query = manager.createNativeQuery("select serie from PMP_MAPA_PL");
			List<String> serieList = query.getResultList();
			for (String serie : serieList) {
				query = manager.createNativeQuery("select  CASE WHEN count(*) = 0 THEN 'NÃO' ELSE 'SIM' END from pmp_maquina_pl pl"+
									" where pl.numero_serie ='"+serie+"'"+
									" and pl.latitude is not null"+
									" and pl.longitude is not null");
				String plAtivo = (String)query.getSingleResult();
				if(plAtivo.equals("NÃO")){
					manager.getTransaction().begin();
					query = manager.createNativeQuery("delete from PMP_MAPA_PL where SERIE = '"+serie+"'");
					query.executeUpdate();
					manager.getTransaction().commit();
				}
				
			}
			
			map.put("json", result);
			map.put("PlMaquinaBean", resultPlMaquinaBean);
		}catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}	
		}
		return map;
		
	}
	
	/**
	 * Verifica se existe alguma máquina com o nível de criticidade de revisão estabelecido para colorir o marcador do mapa.
	 * @param objects
	 * @param manager
	 * @return null se não existe registro para a série informada e a cor dependendo da situação do horimetro.
	 */
	private String setCriticidade(Object[] objects, EntityManager manager, List<PmpCriticidadeManutencao> criticidadeManutencao, String nivel) {
		
		String cor = null;
		
		try {
				cor = verificarManutencaoMeses((String)objects[1], manager, criticidadeManutencao);
				String corCriticidade = null;

//				if(cor == null){
				Query query = manager.createNativeQuery("select count(*) from PMP_AGENDAMENTO ag, PMP_CONTRATO c, PMP_CONFIG_OPERACIONAL op"+
						" where c.ID = op.ID_CONTRATO"+
						" and op.ID = ag.ID_CONG_OPERACIONAL"+
						" and c.NUMERO_SERIE = '"+(String)objects[1]+"'"+
				" and ag.ID_STATUS_AGENDAMENTO in (select ID from PMP_STATUS_AGENDAMENTO where SIGLA in ('EA','AT'))"); 
				Integer agendamentos = (Integer)query.getSingleResult();
				if(agendamentos > 0){
					return "azul";
				}

					 query = manager.createNativeQuery("select MIN(hs.HORAS_MANUTENCAO), hs.FREQUENCIA from PMP_CONTRATO c, PMP_CONFIG_OPERACIONAL op, PMP_CONT_HORAS_STANDARD hs"+
							" where c.NUMERO_SERIE = '"+(String)objects[1]+"'"+
							" and c.ID = op.ID_CONTRATO"+
							" and hs.ID_CONTRATO = c.id"+
							" and c.is_ativo is null"+
							" and IS_EXECUTADO = 'N'"+
					" group by hs.FREQUENCIA"); 

					if(query.getResultList().size() > 0){

						
						Object[] proxHorimetroFrequencia = (Object[])query.getResultList().get(0);
						
						Integer proxHorimetro = ((BigDecimal)proxHorimetroFrequencia[0]).intValue();
						Integer frequencia = ((BigDecimal)proxHorimetroFrequencia[1]).intValue();

						double horimetro = ((BigDecimal)objects[4]).doubleValue();
						
						if(horimetro > proxHorimetro || horimetro == proxHorimetro){
							if(!nivel.equals("") && !nivel.equals("CRITICO")){
								return null;
							}
							
							return "vermelho";
						}
						
						double result = 0;

						if(proxHorimetro < frequencia){
							result = 100 - (((proxHorimetro - horimetro) * 100) / proxHorimetro);
						}else{ 
							if(proxHorimetro > horimetro  && (proxHorimetro - frequencia) <= horimetro){
								result = 100 - (((proxHorimetro - horimetro) * 100) / frequencia);
							}
						}
						
						for(PmpCriticidadeManutencao manutencao : criticidadeManutencao){

							if(manutencao.getNivel().equals("CRITICO")){
								if(result >= manutencao.getMinPorcetagem() || result < 0){
									corCriticidade = "vermelho";
									break;
								}
							}else if(result >= manutencao.getMinPorcetagem() && result <= manutencao.getMaxPorcetagem()){
								if(manutencao.getNivel().equals("CRITICIDADE MEDIA")){
									corCriticidade = "amarelo";
									break;
								}else if(manutencao.getNivel().equals("NAO CRITICO")){
									corCriticidade = "verde";
									break;
								}					
							}				
						}
						
						if(corCriticidade == null && cor == null){
							return null;
						}else{
							if(cor == null){
								return cor = corCriticidade;
							}else if(corCriticidade == null){
								return cor;
							}
							if(cor.equals("vermelho") || corCriticidade.equals("vermelho")){
								cor = "vermelho";
							}else if(cor.equals("amarelo") || corCriticidade.equals("amarelo")){
								cor = "amarelo";
							}else {
								cor = "verde";
							}
						}
					}
					
			
		} catch (NoResultException nre) {
			// TODO: handle exception		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cor;
	}
	/**
	 * Verifica se existe alguma máquina com o nível de criticidade de revisão estabelecido para colorir o marcador do mapa.
	 * @param objects
	 * @param manager
	 * @return null se não existe registro para a série informada e a cor dependendo da situação do horimetro.
	 */
	private String setCriticidadePlus(Object[] objects, EntityManager manager, List<PmpCriticidadeManutencao> criticidadeManutencao, String nivel) {
		
		String cor = null;
		
		try {
			cor = verificarManutencaoMesesPlus((String)objects[1], manager, criticidadeManutencao);
			
//				if(cor == null){
			
			Query query = manager.createNativeQuery("select MIN(hs.data_revisao), hs.FREQUENCIA from PMP_CONTRATO c, PMP_CONFIG_OPERACIONAL op, PMP_CONT_HORAS_STANDARD_PLUS hs"+
					" where c.NUMERO_SERIE = '"+(String)objects[1]+"'"+
					" and c.ID = op.ID_CONTRATO"+
					" and hs.ID_CONTRATO = c.id"+
					" and c.is_ativo is null"+
					" and IS_EXECUTADO = 'N'"+
			" group by hs.FREQUENCIA"); 
			
			if(query.getResultList().size() > 0){
				
				Object[] proxHorimetroFrequencia = (Object[])query.getResultList().get(0);
				Date dataRevisao = dateFormat.parse((String)proxHorimetroFrequencia[0]);
				//Integer proxHorimetro = ((BigDecimal)proxHorimetroFrequencia[0]).intValue();
				//Integer frequencia = ((BigDecimal)proxHorimetroFrequencia[1]).intValue();
				int diasProximarevisao = DateHelper.diffDaysDate(new Date(), dataRevisao);
				
				
				
					query = manager.createNativeQuery("select count(*) from PMP_AGENDAMENTO ag, PMP_CONTRATO c, PMP_CONFIG_OPERACIONAL op"+
							" where c.ID = op.ID_CONTRATO"+
							" and op.ID = ag.ID_CONG_OPERACIONAL"+
							" and c.NUMERO_SERIE = '"+(String)objects[1]+"'"+
					" and ag.ID_STATUS_AGENDAMENTO in (select ID from PMP_STATUS_AGENDAMENTO where SIGLA in ('EA','AT'))"); 
					Integer agendamentos = (Integer)query.getSingleResult();
					if(agendamentos > 0){
						return "azul";
					}
					if(diasProximarevisao <= 15){
						return "vermelho";
					}
				}
				
				
			cor = "verde";
			
			
			
		} catch (NoResultException nre) {
			// TODO: handle exception		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return cor;
	}
	/**
	 * Busca todas as criticidades da tabela.
	 * @param manager
	 * @return lista de criticidade (PmpCriticidadeManutencao).
	 */
	private List<PmpCriticidadeManutencao> findAllCriticidade (EntityManager manager){
		
		try {
			Query query = manager.createQuery("FROM PmpCriticidadeManutencao");
			
			List<PmpCriticidadeManutencao> result = query.getResultList();
			
			return result;
			
		}catch (NoResultException nre) {
			// TODO: handle exception
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	/**
	 * Busca a criticidade do tipo escolhido no comboBox da tela.
	 * @param nivel
	 * @param manager
	 * @return List<PmpCriticidadeManutencao>
	 */
//	private List<PmpCriticidadeManutencao> findAllCriticidadeBy(String nivel,
//			EntityManager manager) {
//		
//		try {
//			Query query = manager.createQuery("FROM PmpCriticidadeManutencao WHERE nivel =:nivel");
//			query.setParameter("nivel", nivel);
//			
//			List<PmpCriticidadeManutencao> result = query.getResultList();
//			
//			return result;
//			
//		}catch (NoResultException nre) {
//			// TODO: handle exception
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;		
//	}
	
	/**
	 * Verifica se os dias da ultima revisão até hoje estouraram os dias(meses) da proxima revisão. 
	 * @param serie
	 * @param manager
	 * @return a cor depedendo da situação ou null se o tempo de manutenção não for igual ou maior o prazo de manutenção.
	 */
	private String verificarManutencaoMeses(String serie, EntityManager manager, List<PmpCriticidadeManutencao> criticidadeManutencao){
		
		try {			
		
		Query query = manager.createNativeQuery("select mm.MESES_MANUTENCAO from PMP_MESES_MANUTENCAO mm,"+
				"(select DISTINCT c.ID_FAMILIA, a.ID_ARV from PMP_CONTRATO c, ARV_INSPECAO a"+
				" where c.MODELO = a.DESCRICAO"+ 
				" and c.NUMERO_SERIE = '"+serie+"') fm"+
				" WHERE fm.ID_ARV = mm.ID_MODELO"+
				" and fm.ID_FAMILIA = mm.ID_FAMILIA");
		
		BigDecimal resultMeses = (BigDecimal) query.getSingleResult();
		
		if(resultMeses != BigDecimal.ZERO){			
			int diasMes = resultMeses.intValue() * MES;
			
			query = manager.createNativeQuery("select DATEDIFF(DD,MAX(ag.DATA_AGENDAMENTO), GETDATE()) from PMP_AGENDAMENTO ag,"+ 
						"(select hs.* from PMP_CONTRATO c, PMP_CONFIG_OPERACIONAL op, PMP_CONT_HORAS_STANDARD hs"+
						" where c.NUMERO_SERIE = '"+serie+"'"+
						" and c.ID = op.ID_CONTRATO"+
						" and hs.ID_CONTRATO = c.id"+
						" and c.is_ativo is null"+
						" and IS_EXECUTADO = 'S') hsaux"+
						" where ag.ID_CONT_HORAS_STANDARD = hsaux.ID");
			
			Integer resultDias = (Integer) query.getSingleResult();
			
			if(resultDias == null){
				query = manager.createNativeQuery("select DATEDIFF(DD,MAX(ag.DATA_AGENDAMENTO), GETDATE()) from PMP_AGENDAMENTO ag,"+
								" (select max(hs.id) id from PMP_CONTRATO c, PMP_CONFIG_OPERACIONAL op, PMP_CONT_HORAS_STANDARD hs"+
								" where c.NUMERO_SERIE = '"+serie+"'"+
								" and c.ID = op.ID_CONTRATO"+
								" and hs.ID_CONTRATO = c.id"+
								" and c.is_ativo = 'I'"+
								" and IS_EXECUTADO = 'S') hsaux"+
								" where ag.ID_CONT_HORAS_STANDARD = hsaux.ID");
				
				resultDias = (Integer) query.getSingleResult();
				
			}
			
									
			if(resultDias == null){ 
				
				query = manager.createNativeQuery("select DATEDIFF(DD,MAX(c.DATA_ACEITE), GETDATE()) from PMP_CONTRATO c where c.NUMERO_SERIE = '"+serie+"' and c.IS_ATIVO is null");
				
				resultDias = (Integer) query.getSingleResult();
				
			}if(resultDias != null && resultDias != 0){
				if(resultDias == 0){
					return "vermelho";
				}
				double result = (resultDias * 100) / diasMes;
				

				for(PmpCriticidadeManutencao manutencao : criticidadeManutencao){

					if(manutencao.getNivel().equals("CRITICO")){
						if(result >= manutencao.getMinPorcetagem()){
							return "vermelho";
						}
					}else if(result >= manutencao.getMinPorcetagem() && result <= manutencao.getMaxPorcetagem()){
						if(manutencao.getNivel().equals("CRITICIDADE MEDIA")){
							return "amarelo";
						}else if(manutencao.getNivel().equals("NAO CRITICO")){
							return "verde";
						}					
					}				
				}				
						
			}		
		}				
		}catch (NoResultException nre) {
			// TODO: handle exception
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Verifica se os dias da ultima revisão até hoje estouraram os dias(meses) da proxima revisão. 
	 * @param serie
	 * @param manager
	 * @return a cor depedendo da situação ou null se o tempo de manutenção não for igual ou maior o prazo de manutenção.
	 */
	private String verificarManutencaoMesesPlus(String serie, EntityManager manager, List<PmpCriticidadeManutencao> criticidadeManutencao){
		
		try {			
			
			Query query = manager.createNativeQuery("select mm.MESES_MANUTENCAO from PMP_MESES_MANUTENCAO mm,"+
					"(select DISTINCT c.ID_FAMILIA, a.ID_ARV from PMP_CONTRATO c, ARV_INSPECAO a"+
					" where c.MODELO = a.DESCRICAO"+ 
					" and c.NUMERO_SERIE = '"+serie+"') fm"+
					" WHERE fm.ID_ARV = mm.ID_MODELO"+
			" and fm.ID_FAMILIA = mm.ID_FAMILIA");
			
			BigDecimal resultMeses = (BigDecimal) query.getSingleResult();
			
			if(resultMeses != BigDecimal.ZERO){			
				int diasMes = resultMeses.intValue() * MES;
				
				query = manager.createNativeQuery("select DATEDIFF(DD,MAX(ag.DATA_AGENDAMENTO), GETDATE()) from PMP_AGENDAMENTO ag,"+ 
						"(select hs.* from PMP_CONTRATO c, PMP_CONFIG_OPERACIONAL op, PMP_CONT_HORAS_STANDARD_PLUS hs"+
						" where c.NUMERO_SERIE = '"+serie+"'"+
						" and c.ID = op.ID_CONTRATO"+
						" and hs.ID_CONTRATO = c.id"+
						" and c.is_ativo is null"+
						" and IS_EXECUTADO = 'S') hsaux"+
				" where ag.ID_CONT_HORAS_STANDARD = hsaux.ID");
				
				Integer resultDias = (Integer) query.getSingleResult();
				
				if(resultDias == null){ 
					
					query = manager.createNativeQuery("select DATEDIFF(DD,MAX(c.DATA_ACEITE), GETDATE()) from PMP_CONTRATO c where c.NUMERO_SERIE = '"+serie+"' and c.IS_ATIVO is null");
					
					resultDias = (Integer) query.getSingleResult();
					
				}if(resultDias != null && resultDias != 0){
					if(resultDias == 0){
						return "vermelho";
					}
					double result = (resultDias * 100) / diasMes;
					
					
					for(PmpCriticidadeManutencao manutencao : criticidadeManutencao){
						
						if(manutencao.getNivel().equals("CRITICO")){
							if(result >= manutencao.getMinPorcetagem()){
								return "vermelho";
							}
						}else if(result >= manutencao.getMinPorcetagem() && result <= manutencao.getMaxPorcetagem()){
							if(manutencao.getNivel().equals("CRITICIDADE MEDIA")){
								return "amarelo";
							}else if(manutencao.getNivel().equals("NAO CRITICO")){
								return "verde";
							}					
						}				
					}				
					
				}		
			}				
		}catch (NoResultException nre) {
			// TODO: handle exception
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Filtra as cores corretas para serem exibidas no mapa, dependendo da opção selecionada.
	 * @param nivel
	 * @param cor
	 * @return a cor ou null.
	 */
	private String confirmColorByFilter(String nivel, String cor){
		if(cor.equals("azul")){
			return cor;
		}
		if(nivel.equals("CRITICO") && !cor.equals("vermelho")){
			return null;
		}else if(nivel.equals("CRITICIDADE MEDIA") && !cor.equals("amarelo")){
			return null;
		}else if(nivel.equals("NAO CRITICO") && !cor.equals("verde")){
			return null;
		}
		
		return cor;
	}
	
	public List<JSONObject> findAllLocalizacaoCarroTecnico() {

		List<JSONObject> beanList = new ArrayList<JSONObject>();
        
		EntityManager manager = null;

		try {
			manager = JpaUtil.getInstance();
			

//			String SQL = "select distinct loc1.iipos_latitude, loc1.iipos_longitude, substr( replace (loc1.iipos_mctname, '*'),8,14) placa, loc1.iipos_landmark,loc1.iipos_mctname from  POSITIONHISTORY_IIPOS@autotrac loc1"+
//							" right join tw_funcionario f on substr( replace (loc1.iipos_mctname, '*'),8,14) = f.placa_veiculo"+
//							" , adm_perfil_sistema_usuario psu,"+
//							" (select iipos_mctaddress,  to_char( max(iipos_timeposition), 'DD/MM/YYYY HH24:MI:SS') dtAt from POSITIONHISTORY_IIPOS@autotrac"+ 
//							" group by iipos_mctaddress)loc2"+
//							" where loc1.iipos_mctaddress = loc2.iipos_mctaddress"+
//							" and to_char(loc1.iipos_timeposition, 'DD/MM/YYYY HH24:MI:SS')  = loc2.dtAt"+       
//							" and psu.id_perfil = (select id from adm_perfil where sigla = 'USUTEC' and tipo_sistema = 'SC')"+
//							" and f.epidno = psu.id_tw_usuario"+
//							" and f.stn1 = "+Integer.valueOf(usuarioBean.getFilial())+      
//							" and f.placa_veiculo is not null";
			String SQL = "select lv.latitude, lv.longitude, lv.placa, lv.localizacao, psu.ID_TW_USUARIO, f.EPLSNM, lv.velocidade"+
						 " from  adm_localizacao_veiculo lv inner join tw_funcionario f on lv.placa = f.placa_veiculo, adm_perfil_sistema_usuario psu"+
						 " where psu.id_perfil = (select id from adm_perfil where sigla = 'USUTEC' and tipo_sistema = 'PMP')"+
						 " and f.epidno = psu.id_tw_usuario"+
						 " and f.stn1 = "+Integer.valueOf(usuarioBean.getFilial())+
						 " and f.placa_veiculo is not null";

			Query query = manager.createNativeQuery(SQL);
			List<Object[]> list = (List<Object[]>) query.getResultList();			

			for(Object [] objects : list){
//				SQL = "FROM ScAgendamento ag, TwFuncionario tw, ScChamado ch WHERE ag.idStatusAgendamento in"+ 
//				" (select id from ScStatusAgendamento where sigla not in('FIN', 'RT', 'CN'))"+ 
//				" and  tw.placaVeiculo is not null"+
//				" and tw.epidno = ag.idFuncionario" +
//				" and tw.placaVeiculo like '"+objects[2]+"%'" +
//				" and ch.idOs.id = ag.idOs.id";
				SQL = "select ag.NUM_OS, c.RAZAO_SOCIAL from PMP_AGENDAMENTO ag, PMP_CONT_HORAS_STANDARD hs, PMP_CONTRATO c"+
					  "	 where ag.ID_CONT_HORAS_STANDARD = hs.ID"+
					  "	 and hs.ID_CONTRATO = c.ID"+
					  "	 and ag.ID_FUNCIONARIO = '"+((String)objects[4])+"'"+ 	
					  "	 and ag.ID_STATUS_AGENDAMENTO in(select id from PMP_STATUS_AGENDAMENTO where SIGLA in('AT'))";

				PlMaquinaBean bean = new PlMaquinaBean();
				JSONObject jsonObject = new JSONObject();
				bean.setLatitude((objects[0] != null)?objects[0].toString(): "");
				jsonObject.put("latitude", (objects[0] != null)?objects[0].toString(): "");
				bean.setLongitude((objects[1] != null)?objects[1].toString(): "");
				jsonObject.put("longitude", (objects[1] != null)?objects[1].toString(): "");
				bean.setPlaca((objects[2] != null)?objects[2].toString(): "");
				jsonObject.put("placa", (objects[2] != null)?objects[2].toString(): "");
				bean.setLocalizacao((objects[3] != null)?objects[3].toString(): "");
				jsonObject.put("localizacao", (objects[3] != null)?objects[3].toString(): "");
				bean.setFiial(usuarioBean.getFilial());
				jsonObject.put("filial", usuarioBean.getFilial());
				query = manager.createNativeQuery(SQL);
				List<Object[]> listAg = (List<Object[]>) query.getResultList();	
				String numOs = "";
								
				for (Object [] objectsAg : listAg) {
					String numeroOs = (String)objectsAg[0];
					String nomeCliente = (String)objectsAg[1];
					numOs += "Número OS: "+numeroOs+" "+nomeCliente+"#";
				}
				
				bean.setTecnico((String)objects[5]);
				jsonObject.put("tecnico", (String)objects[5]);
				jsonObject.put("velocidade", (BigDecimal)objects[6]);
				bean.setNumOS((numOs.length() > 1)?numOs.substring(0,numOs.length()-1):numOs);	
				jsonObject.put("numOs",(numOs.length() > 1)?numOs.substring(0,numOs.length()-1):numOs);
				beanList.add(jsonObject);	
			}			

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}	
		}

		return beanList;
	}

}
