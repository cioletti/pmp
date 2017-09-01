package com.pmp.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.ClienteBean;
import com.pmp.bean.PlMaquinaBean;
import com.pmp.entity.PmpClientePl;
import com.pmp.util.JpaUtil;

public class PlMaquinaBusiness {

	public List<PlMaquinaBean> findAllMaquinaPl(){
		EntityManager manager = null;
		List<PlMaquinaBean> result = new ArrayList<PlMaquinaBean>();
		try{
			manager = JpaUtil.getInstance();
			String SQL = "select  distinct to_char(p.data_atualizacao, 'DD/MM/RRRR HH24:MI:SS') data_atualizacao, p.numero_serie, p.latitude, p.longitude," +
					" (select max(pl.horimetro) from pmp_maquina_pl pl where pl.numero_serie = p.numero_serie) horimetro, cpl.cod_cliente, cpl.nome_cliente," +
					" (select twf.stnm from tw_filial twf where twf.stno = to_Number(cpl.filial)) filial, p.modelo"+
					" from pmp_maquina_pl p left join pmp_cliente_pl cpl on p.numero_serie = cpl.serie,"+ 
					" (select  max(p.id) as id, max(p.data_atualizacao) as data_atualizacao, p.numero_serie"+
					" from pmp_maquina_pl p "+
					" group by p.numero_serie) t"+
					" where p.longitude is not null"+
					" and p.latitude is not null"+
					" and p.data_atualizacao in(select distinct max(mpl.data_atualizacao) from pmp_maquina_pl mpl where mpl.numero_serie = p.numero_serie)"+
					" and t.id = p.id";

			Query query = manager.createNativeQuery(SQL);
			List<Object[]> pairs = (List<Object[]>)query.getResultList();
			for (Object[] objects : pairs) {
				PlMaquinaBean bean = new PlMaquinaBean();
				bean.setDescricao("Dt. Atualização: "+objects[0]+"\nSérie "+objects[1]+"\nModelo: "+(String)objects[8]+"\n"+((objects[4] != null)?"Horímetro: "+objects[4]:"")
						+"\nCódigo Cliente: "+(String)objects[5]+"\nNome Cliente: "+(String)objects[6]
                        +"\nFilial: "+ (String)objects[7]);
				bean.setSerie((String)objects[1]);
				bean.setLatitude((String)objects[2]);
				bean.setLongitude((String)objects[3]);
				bean.setCodCliente((String)objects[5]);
				bean.setNomeCliente((String)objects[6]);
				bean.setFiial((String)objects[7]);
				bean.setModelo((String)objects[8]);
				result.add(bean);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	public PlMaquinaBean findAllMaquinaPl(String numSerie){
		EntityManager manager = null;
		List<PlMaquinaBean> result = new ArrayList<PlMaquinaBean>();
		try{
			manager = JpaUtil.getInstance();
			String SQL = "select  distinct to_char(p.data_atualizacao, 'DD/MM/RRRR HH24:MI:SS') data_atualizacao, p.numero_serie, p.latitude, p.longitude," +
			" (select max(pl.horimetro) from pmp_maquina_pl pl where pl.numero_serie = p.numero_serie) horimetro, cpl.cod_cliente, cpl.nome_cliente," +
			" (select twf.stnm from tw_filial twf where twf.stno = to_Number(cpl.filial)) filial, p.modelo"+
			" from pmp_maquina_pl p left join pmp_cliente_pl cpl on p.numero_serie = cpl.serie,"+ 
			" (select  max(p.id) as id, max(p.data_atualizacao) as data_atualizacao, p.numero_serie"+
			" from pmp_maquina_pl p where p.latitude is not null and p.longitude is not null"+
			" group by p.numero_serie) t"+
			" where p.longitude is not null"+
			" and p.latitude is not null"+
			" and p.data_atualizacao in(select distinct max(mpl.data_atualizacao) from pmp_maquina_pl mpl where mpl.numero_serie = p.numero_serie and mpl.latitude is not null and mpl.longitude is not null)"+
			" and t.id = p.id"+
			" and p.numero_serie = '"+numSerie.toUpperCase()+"'";
			
			Query query = manager.createNativeQuery(SQL);
			Object[] objects = (Object[])query.getSingleResult();
			
				PlMaquinaBean bean = new PlMaquinaBean();
				bean.setDescricao("Dt. Atualização: "+objects[0]+"\nSérie "+objects[1]+"\nModelo: "+(String)objects[8]+"\n"+((objects[4] != null)?"Horímetro: "+objects[4]:"")
						+"\nCódigo Cliente: "+(String)objects[5]+"\nNome Cliente: "+(String)objects[6]
                        +"\nFilial: "+ (String)objects[7]);
				bean.setSerie((String)objects[1]);
				bean.setLatitude((String)objects[2]);
				bean.setLongitude((String)objects[3]);
				bean.setCodCliente((String)objects[5]);
				bean.setNomeCliente((String)objects[6]);
				bean.setFiial((String)objects[7]);
				bean.setModelo((String)objects[8]);
				result.add(bean);
			return bean;
			
		}catch (Exception e) {
			return null;
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
	}
	public List<PlMaquinaBean> findAllPlMaquinaFilter(Long idFilial, List<ClienteBean> clienteList, String cliente, Boolean isCodCliente, Boolean isNomeCliente, Boolean isPmp){
		EntityManager manager = null;
		List<PlMaquinaBean> result = new ArrayList<PlMaquinaBean>();
		try{
			manager = JpaUtil.getInstance();
			String SQL = "select  distinct to_char(p.data_atualizacao, 'DD/MM/RRRR HH24:MI:SS') data_atualizacao, p.numero_serie, p.latitude, p.longitude," +
					" (select max(pl.horimetro) from pmp_maquina_pl pl where pl.numero_serie = p.numero_serie) horimetro, cpl.cod_cliente, cpl.nome_cliente," +
					" (select twf.stnm from tw_filial twf where twf.stno = to_Number(cpl.filial)) filial, p.modelo"+
					" from pmp_maquina_pl p left join pmp_cliente_pl cpl on p.numero_serie = cpl.serie,"+ 
					" (select  max(p.id) as id, max(p.data_atualizacao) as data_atualizacao, p.numero_serie"+
					" from pmp_maquina_pl p "+
					" group by p.numero_serie) t"+
					" where p.longitude is not null"+
					" and p.latitude is not null"+
					" and p.data_atualizacao in(select distinct max(mpl.data_atualizacao) from pmp_maquina_pl mpl where mpl.numero_serie = p.numero_serie)"+
					" and t.id = p.id";
			String codCliente = "";
			if(clienteList.size() > 0){
				for (ClienteBean bean : clienteList) {
					codCliente += "'"+bean.getCGCNUM()+"',";
				}
			}
			if(idFilial > -1){
				SQL += " and cpl.filial = "+idFilial;
			}
			if(codCliente.length() > 0){
				codCliente = codCliente.substring(0, codCliente.length() - 1);
				SQL += " and cpl.cod_cliente in ("+codCliente+")";
			}
			if(isCodCliente){
				SQL += " and cpl.cod_cliente = '"+cliente+"'";
			}
			if(isNomeCliente){
				SQL += " and cpl.nome_cliente like '"+cliente.toUpperCase()+"%'";
			}
			if(isPmp){
				SQL += " and p.numero_serie in (  " +
						" select con.numero_serie from pmp_contrato con" +
						"  where con.id in (select distinct hs.id_contrato from pmp_cont_horas_standard hs where hs.is_executado = 'N'))";
			}

			Query query = manager.createNativeQuery(SQL);
			List<Object[]> pairs = (List<Object[]>)query.getResultList();
			for (Object[] objects : pairs) {
				PlMaquinaBean bean = new PlMaquinaBean();
				bean.setDescricao("Dt. Atualização: "+objects[0]+"\nSérie "+objects[1]+"\nModelo: "+(String)objects[8]+"\n"+((objects[4] != null)?"Horímetro: "+objects[4]:"")
						+"\nCódigo Cliente: "+(String)objects[5]+"\nNome Cliente: "+(String)objects[6]
                        +"\nFilial: "+ (String)objects[7]);
				bean.setSerie((String)objects[1]);
				bean.setLatitude((String)objects[2]);
				bean.setLongitude((String)objects[3]);
				bean.setCodCliente((String)objects[5]);
				bean.setNomeCliente((String)objects[6]);
				bean.setFiial((String)objects[7]);
				bean.setModelo((String)objects[8]);
				result.add(bean);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	public List<ClienteBean> findClienteFilialPL(Long idFilial){
		EntityManager manager = null;
		List<ClienteBean> result = new ArrayList<ClienteBean>();
		
		try{
			manager = JpaUtil.getInstance();
			Query query = manager.createNativeQuery("select distinct nome_cliente, cod_cliente from pmp_cliente_pl where filial =:filial order by nome_cliente");
			query.setParameter("filial", idFilial);
			List<Object[]> clientePlList = query.getResultList();
			for (Object[] objects : clientePlList) {
				ClienteBean bean = new ClienteBean();
				bean.setRAZSOC((String)objects[0]);
				bean.setCGCNUM((String)objects[1]);
				result.add(bean);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}	
		return result;
	}
}
