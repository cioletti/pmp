package com.pmp.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.InspecaoPmpTreeBean;
import com.pmp.util.JpaUtil;

public class InspencaoPmpTreeBusiness {

	
	public List<InspecaoPmpTreeBean> findAllInspencaoPmpTree(Long idOsPalm){
		
		EntityManager manager = null;
		List<InspecaoPmpTreeBean> inspecaoPmpTree = new ArrayList<InspecaoPmpTreeBean>();
		try {
			manager = JpaUtil.getInstance();

			Query query_pai = manager.createNativeQuery("select arv.id_arv, arv.descricao ,  arv.smcs, arv.id_pai " +
						"from arv_inspecao arv where arv.id_arv in(select distinct op.grupo " +
						"from os_palm_dt op where op.os_palm_idos_palm = :idOsPalm )order by arv.id_arv");
			
			query_pai.setParameter("idOsPalm", idOsPalm);
			
			List<Object[]> resultPai = query_pai.getResultList();
			
			for (Object [] inspecaoPai : resultPai) {
				InspecaoPmpTreeBean obj = new InspecaoPmpTreeBean();
				if(inspecaoPai[0] != null){obj.setIdIrdav(((BigDecimal)inspecaoPai[0]).longValue());}
				if(inspecaoPai[1] != null){obj.setDescricao(inspecaoPai[1].toString());}
				
				Query queryFilho = manager.createNativeQuery("select arv.id_arv,arv.descricao ,arv.smcs, dt.grupo, dt.status, dt.simnao, dt.tipo_manutencao, CONVERT (VARCHAR(1000), dt.obs), dt.idos_palm_dt " +
						"from arv_inspecao arv, os_palm_dt dt where dt.grupo = :grupo and dt.os_palm_idos_palm = :idOsPalm " +
						"and arv.id_arv = dt.id_idarv");
				
				queryFilho.setParameter("idOsPalm", idOsPalm);
				queryFilho.setParameter("grupo", obj.getIdIrdav());
				
				List<Object[]> resultFilho = queryFilho.getResultList();
				List<InspecaoPmpTreeBean> inspecaoPmpTreeFilhos = new ArrayList<InspecaoPmpTreeBean>();
				for (Object [] inspecaoFilho : resultFilho) {
					InspecaoPmpTreeBean objFilho = new InspecaoPmpTreeBean();
					if(inspecaoFilho[0] != null){objFilho.setIdIrdav(((BigDecimal)inspecaoFilho[0]).longValue());}
					if(inspecaoFilho[1] != null){objFilho.setDescricao(inspecaoFilho[1].toString());}
					if(inspecaoFilho[3] != null){objFilho.setGrupo(inspecaoFilho[3].toString());}
					
					if(inspecaoFilho[4] != null){
						if(inspecaoFilho[4].toString().equals("C")){
							objFilho.setStatus("Normal");	
							objFilho.setStatusUrlImage("img/CE.png");
						}else  if(inspecaoFilho[4].toString().equals("NC")){
							objFilho.setStatus("Não Conforme");	
							objFilho.setStatusUrlImage("img/CN.png");	
						}else  if(inspecaoFilho[4].toString().equals("NA")){
							objFilho.setStatus("Não Se Aplica");	
							objFilho.setStatusUrlImage("img/AM.png");	
						}		
					}
					
					if(inspecaoFilho[5] != null){objFilho.setSimNao(inspecaoFilho[5].toString());}	
					if(inspecaoFilho[2] != null){objFilho.setSmcs(inspecaoFilho[2].toString());}
					if(inspecaoFilho[6] != null){objFilho.setTipoManutencao(inspecaoFilho[6].toString());}
					if(inspecaoFilho[8] != null){objFilho.setIdOsPalmDt(((BigDecimal)inspecaoFilho[8]).longValue());}
					if(inspecaoFilho[7] != null){objFilho.setObs(inspecaoFilho[7].toString());}
					
										  
					Query queryFotoInspecao = manager.createNativeQuery("select count(id_foto_inspecao) from foto_inspecao " +
							"where id_os_palm = :idOsPalm and id_os_palm_dt = :idOsPalmDt ");
					queryFotoInspecao .setParameter("idOsPalm", idOsPalm);
					queryFotoInspecao .setParameter("idOsPalmDt", objFilho.getIdOsPalmDt());
					Long quantidadeFotos = ((Integer)queryFotoInspecao.getResultList().get(0)).longValue();
					
					if(quantidadeFotos > 0){
						objFilho.setFotoUrlImage("img/camera.png");
					}else{
						objFilho.setFotoUrlImage("");						
					}					
					inspecaoPmpTreeFilhos.add(objFilho);
				}
				obj.setChildren(inspecaoPmpTreeFilhos);			
				inspecaoPmpTree.add(obj);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return inspecaoPmpTree;
	}
	
	public List<Integer> findAllFotos(Integer idOsPalmDt){
		EntityManager manager = null;
		List<Integer> result = new ArrayList<Integer>();
		
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createNativeQuery("select f.id_foto_inspecao from foto_inspecao f where f.id_os_palm_dt =:id");
			query.setParameter("id", idOsPalmDt);
			List<BigDecimal> list = query.getResultList();
			for (BigDecimal bd : list) {
				result.add(bd.intValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
}
