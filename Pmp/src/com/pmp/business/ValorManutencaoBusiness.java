package com.pmp.business;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.ConfigManutencaoHorasBean;
import com.pmp.bean.PecaBean;
import com.pmp.bean.ValorManutencaoBean;
import com.pmp.entity.PmpContHorasStandard;
import com.pmp.entity.PmpManutencao;
import com.pmp.entity.PmpPecasOsOperacional;
import com.pmp.util.JpaUtil;
import com.pmp.util.ValorMonetarioHelper;

public class ValorManutencaoBusiness {
	
	public List<PecaBean> findAllPecas(ValorManutencaoBean bean){
		List<PecaBean> listPecas = new ArrayList<PecaBean>();
		ConfigManutencaoHorasBean osBean = bean.getConfigManutencaoHorasBeanList().get(0);
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createNativeQuery("SELECT m.bgrp, m.beqmsn, m.uncs, m.unls, m.dlrqty, m.pano20, m.bectyc, m.cptcd, m.ds18, m.sos FROM pmp_manutencao m " +
                            "WHERE m.cptcd = '"+osBean.getStandardJob()+"' AND " +
                            "m.bgrp = 'KIT' AND " +
                            "substr(m.beqmsn, 1, 4) = '"+bean.getPrefixo()+"' AND " + 
                            "substr(m.beqmsn, 5, 10) BETWEEN substr('"+bean.getBeginRanger()+"', 5, 10) AND substr('"+bean.getEndRanger()+"', 5, 10) " +
                            "ORDER BY m.beqmsn, m.ds18");
			
			 List<Object[]> list = query.getResultList();

	            for (Object[] obj : list) {
	                PecaBean pecaBean = new PecaBean();
	                
	                pecaBean.setBgrp((String) obj[0]);
	                pecaBean.setBeqmsn((String) obj[1]);
	                pecaBean.setUncs((String.valueOf(ValorMonetarioHelper.formata("###,###,##0.00",
	                		Double.valueOf(String.valueOf((BigDecimal) obj[2]))))));
	                pecaBean.setUnls((String.valueOf(ValorMonetarioHelper.formata("###,###,##0.00",
	                		Double.valueOf(String.valueOf((BigDecimal) obj[3]))))));
	                pecaBean.setDlrqty(((BigDecimal) obj[4]).intValue());
	                pecaBean.setPano20((String) obj[5]);
	                pecaBean.setBectyc((String) obj[6]);
	                pecaBean.setCptcd((String) obj[7]);
	                pecaBean.setDs18((String) obj[8]);
	                pecaBean.setSos((String) obj[9]);
	                
	                listPecas.add(pecaBean);
	                
	                
	            }
	                
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return listPecas;
	}
	
	public boolean removerPeca(PecaBean bean) {
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			Query query = manager.createNativeQuery("DELETE FROM pmp_manutencao man"+
					" WHERE man.beqmsn = '"+bean.getBeqmsn()+"'"+
					" and man.pano20 = '"+bean.getPano20()+"'"+
					" and man.bgrp = '"+bean.getBgrp()+"'"+
					" and man.cptcd = '"+bean.getCptcd()+"'");
			query.executeUpdate();
			manager.getTransaction().commit();				

		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return true;		
	}
	
	public boolean saveOrUpdate (PecaBean bean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			
			Query query = manager.createNativeQuery("SELECT * FROM pmp_manutencao man"+
					" WHERE man.beqmsn = '"+bean.getBeqmsn()+"'"+ 
					" AND man.pano20 = '"+bean.getPano20()+"'"+
					" AND man.bgrp = '"+bean.getBgrp()+"'"+ 
					" AND man.cptcd = '"+bean.getCptcd()+"'");
			
			if (query.getResultList().size() > 0) {
				query = manager.createNativeQuery("UPDATE pmp_manutencao man"+
						" SET man.ds18 = '"+bean.getDs18()+"' , man.unls = "+BigDecimal.valueOf(Double.valueOf((bean.getUnls().replace(".", "").replace(",", "."))))+","+
						" man.dlrqty = "+bean.getDlrqty()+" , man.bectyc = '"+bean.getBectyc()+"', man.sos = '"+bean.getSos()+"'"+
						" WHERE man.beqmsn = '"+bean.getBeqmsn()+"'"+ 
						" AND man.pano20 = '"+bean.getPano20()+"'"+
						" AND man.bgrp = '"+bean.getBgrp()+"'"+ 
						" AND man.cptcd = '"+bean.getCptcd()+"'");
				query.executeUpdate();
				manager.getTransaction().commit();					
			} else {
				PmpManutencao manutencao = new PmpManutencao();
				manutencao.setUncs(BigDecimal.valueOf(Double.valueOf((bean.getUncs().replace(".", "").replace(",", ".")))));
				manutencao.setUnls(BigDecimal.valueOf(Double.valueOf((bean.getUnls().replace(".", "").replace(",", ".")))));
				manutencao.setDlrqty (Long.valueOf(String.valueOf(bean.getDlrqty())));
				manutencao.setDs18(bean.getDs18());
				manutencao.setBectyc(bean.getBectyc());
				manutencao.setSos(bean.getSos());
				manutencao.setBgrp(bean.getBgrp());
				manutencao.setBeqmsn(bean.getBeqmsn());
				manutencao.setPano20(bean.getPano20());
				manutencao.setCptcd(bean.getCptcd());
				//precoCongelado.setSos(bean.g)
				manager.persist(manutencao);
				manager.getTransaction().commit();
			}
		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return true;
	}
	
	public List<PecaBean> findAllOsOperacionalPecas(Long idContHorasStandard){
		List<PecaBean> listPecas = new ArrayList<PecaBean>();
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			PmpContHorasStandard chs = manager.find(PmpContHorasStandard.class, idContHorasStandard);
			String SQL = "from PmpPecasOsOperacional where idOsOperacional.id =:idOsOperacional";			
			Query query = manager.createQuery(SQL);
			query.setParameter("idOsOperacional", chs.getIdOsOperacional().getId());
			
			 List<PmpPecasOsOperacional> list = query.getResultList();

	            for (PmpPecasOsOperacional obj : list) {
	                PecaBean pecaBean = new PecaBean();
	                pecaBean.setId(obj.getId());
	                pecaBean.setDlrqty(obj.getQtd().intValue());
	                pecaBean.setPano20(obj.getNumPeca());
	                pecaBean.setDs18(obj.getNumPeca());
	                pecaBean.setSos(obj.getSos());
	                
	                listPecas.add(pecaBean);
	            }
	                
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return listPecas;
	}
	public Boolean removerOsOperacionalPecas(Long idPecaOsOperacional){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			Query query = manager.createNativeQuery("delete from PMP_PECAS_OS_OPERACIONAL where ID =:ID");
			query.setParameter("ID", idPecaOsOperacional);
			query.executeUpdate();
			manager.getTransaction().commit();
		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}finally{
				if(manager != null && manager.isOpen()){
					manager.close();
				}
			}
		return true;
	}
	public Long saveOrUpdateOsOperacionalPecas(Long idContHorasStandard, Long idPecaOsOperacional, String partNumber, Long qtd, String sos){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			PmpContHorasStandard horasStandard = manager.find(PmpContHorasStandard.class, idContHorasStandard);
			manager.getTransaction().begin();
			PmpPecasOsOperacional operacional = new PmpPecasOsOperacional();
			if(idPecaOsOperacional != null && idPecaOsOperacional > 0){
				operacional = manager.find(PmpPecasOsOperacional.class, idPecaOsOperacional);
			}
			operacional.setIdOsOperacional(horasStandard.getIdOsOperacional());
			operacional.setNumPeca(partNumber);
			operacional.setQtd(qtd);
			operacional.setSos(sos);
			if(idPecaOsOperacional != null && idPecaOsOperacional > 0){
				manager.merge(operacional);
			}else{
				manager.persist(operacional);
			}
			manager.getTransaction().commit();
			return operacional.getId();
		} catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
			return null;
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
	}
}
