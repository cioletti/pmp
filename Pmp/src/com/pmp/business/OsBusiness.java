package com.pmp.business;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.ComponenteCodeBean;
import com.pmp.bean.CondicaoPagamentoBean;
import com.pmp.bean.FilialBean;
import com.pmp.bean.JobCodeBean;
import com.pmp.bean.JobControlBean;
import com.pmp.bean.MaquinaPlBean;
import com.pmp.bean.MinutaBean;
import com.pmp.bean.NotaFiscalBean;
import com.pmp.bean.OsEstimada;
import com.pmp.bean.PecaBean;
import com.pmp.bean.TotalAgendamentoChartBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.entity.CondicaoPagamento;
import com.pmp.entity.PmpConfigOperacional;
import com.pmp.entity.PmpContHorasStandard;
import com.pmp.entity.PmpContHorasStandardPlus;
import com.pmp.entity.PmpContrato;
import com.pmp.entity.PmpFinanceiro;
import com.pmp.entity.PmpOsOperacional;
import com.pmp.entity.PmpPecasConfigOperacional;
import com.pmp.entity.PmpPecasOsOperacional;
import com.pmp.entity.TwFilial;
import com.pmp.util.ConectionDbs;
import com.pmp.util.IConstantAccess;
import com.pmp.util.JpaUtil;
import com.pmp.util.ValorMonetarioHelper;

public class OsBusiness {

	private String FILIAL;
	private static String HQL_FIND_ALL_FILIAIS = "FROM TwFilial ORDER BY stnm";
	private SimpleDateFormat dateFormatHorimetroDbs = new SimpleDateFormat("ddMMyy");
	private UsuarioBean usuarioBean;
	public OsBusiness() {
		// TODO Auto-generated constructor stub
	}
    public OsBusiness(UsuarioBean bean) {
		FILIAL = bean.getFilial();
		this.usuarioBean = bean;
	}

	public Collection<FilialBean> findAllFiliais() {
		Collection<FilialBean> listForm = new ArrayList<FilialBean>();
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery(HQL_FIND_ALL_FILIAIS);
			List<TwFilial> list = (List<TwFilial>) query.getResultList();
			for (TwFilial twFil : list) {
				FilialBean filialBean = new FilialBean();
				filialBean.fromBean(twFil);
				listForm.add(filialBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return listForm;
	}
	
	public Collection<JobControlBean> findAllJobControl() {
		Collection<JobControlBean> listForm = new ArrayList<JobControlBean>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createNativeQuery("select respar from jobcontrol order by respar");
			List<String> list = (List<String>) query.getResultList();
			for (String jbctr : list) {
				JobControlBean bean = new JobControlBean();
				bean.setDescricao(jbctr);
				listForm.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return listForm;
	}
	
	
	public Boolean  verificarOs(String numeroOs) {
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createNativeQuery("select * from PMP_OS_OPERACIONAL o, PMP_CONT_HORAS_STANDARD c where NUM_OS = '"+numeroOs+"'" +
					" and c.IS_EXECUTADO = 'S'" +
					" and c.ID = o.ID_CONT_HORAS_STANDARD");
			if(query.getResultList().size() > 0){
				return true;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		
		return false;
	}
	
	public Collection<JobCodeBean> findAllJobCode() {
		Collection<JobCodeBean> listForm = new ArrayList<JobCodeBean>();
		EntityManager manager = null;
		
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createNativeQuery("select jbcd, jbcdds from jbcd order by jbcd");
			List<Object[]> list = (List<Object[]>) query.getResultList();
			for (Object[] jbcd : list) {
				JobCodeBean bean = new JobCodeBean();
				bean.setId((String)jbcd[0]);
				bean.setDescricao((String)jbcd[1]);
				bean.setLabel((String)jbcd[0]+" - "+(String)jbcd[1]);
				listForm.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return listForm;
	}

	public Collection<ComponenteCodeBean> findAllCompCode(String caracter) {
		Collection<ComponenteCodeBean> listForm = new ArrayList<ComponenteCodeBean>();
		
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			
			Query query = manager.createNativeQuery("select cptcd, cptcdd from cptcd where cptcdd like '"+caracter.toUpperCase()+"%'");
			List<Object[]> list = (List<Object[]>) query.getResultList();
			for (Object[] jbcd : list) {
				ComponenteCodeBean bean = new ComponenteCodeBean();
				bean.setId((String)jbcd[0]);
				bean.setDescricao((String)jbcd[1]);
				bean.setLabel((String)jbcd[0]+" - "+(String)jbcd[1]);
				listForm.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return listForm;
	}
	
	 private final Timer timer = new Timer();
	  
	    public void startNewOsEstimada(final OsEstimada bean, final PmpOsOperacional osOperacional, final String prefixo, final String beginRanger, final String endRanger) {
	        timer.schedule(new TimerTask() {
	        	public void run() {
	        		if(createOsEstimadaThread(bean, osOperacional, prefixo, beginRanger, endRanger)){
	        			timer.cancel();
	        		}
	        	}
	        },  30 * 1000);
	    }
	    public void startNewOsEstimadaContrato(final OsEstimada bean, final PmpConfigOperacional operacional, final PmpContrato contrato) {
	    	timer.schedule(new TimerTask() {
	    		public void run() {
	    			if(createOsEstimadaContratoThread(bean, operacional, contrato)){
	    				timer.cancel();
	    			}
	    		}
	    	
	    	},  30 * 1000);
	    }
	
		public Boolean validarNumSerie(String numSerie){
			
			ResultSet rs = null;
			PreparedStatement prstmt_ = null;

			Connection con = null;

//			InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
//			Properties prop = new Properties();

			try {
//				prop.load(in);
//				String url = prop.getProperty("dbs.url");
//				String user = prop.getProperty("dbs.user");
//				String password =prop.getProperty("dbs.password");
//				Class.forName(prop.getProperty("dbs.driver")).newInstance();

				con = com.pmp.util.ConectionDbs.getConnecton();
				String SQL = "select * from "+IConstantAccess.LIB_DBS+".empeqpd0 f where (trim(f.eqmfs2) = '"+numSerie+"' or trim(f.RDMSR1) = '"+numSerie+"')";
				prstmt_ = con.prepareStatement(SQL);
				rs = prstmt_.executeQuery();
				if(rs.next()){
					return true;
				}
				SQL = "select * from "+IConstantAccess.LIB_DBS+".EMPATCH0 f where trim(f.ATSLN1) = '"+numSerie+"'";
				
				prstmt_ = con.prepareStatement(SQL);
				rs = prstmt_.executeQuery();
				if(rs.next()){
					return true;
				}
				
				SQL = "select * from "+IConstantAccess.LIB_DBS+".EMPORDH0 where eqmfs2 = '"+numSerie+"'";
				prstmt_ = con.prepareStatement(SQL);
				rs = prstmt_.executeQuery();
				if(rs.next()){
					return true;
				}
				
				return false;
			}catch (Exception e) {
				e.printStackTrace();
				return false;
			}finally{
				try {
					if(con != null){
						con.close();						
					}
					if(prstmt_ != null){
						prstmt_.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		public Boolean openOs(Long id, String numOs, String letra){
			Connection con = null;
			Statement statement = null;
			try {
				con = ConectionDbs.getConnecton();
				String SQL = "insert into  "+IConstantAccess.AMBIENTE_DBS+".USPPPSM0 (WONOSM, WONO) values('"+id+"-"+letra+"','"+numOs+"')";
				statement = con.createStatement();
				statement.executeUpdate(SQL);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(con != null){
						con.close();
					}
					if(statement != null){
						statement.close();						
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		
		public OsEstimada newOsEstimada(OsEstimada bean){
			bean.setMsg("");
			EntityManager manager = null;
			Connection con = null;
			Statement statement = null;
			try {
				manager = JpaUtil.getInstance();
				
				PmpContrato contrato = manager.find(PmpContrato.class, bean.getAgendamentoBean().getIdContrato());
				MaquinaPlBusiness maquinaPlBusiness = new MaquinaPlBusiness();
				MaquinaPlBean maquinaPlNovo = new MaquinaPlBean();
				maquinaPlNovo.setNumeroSerie (contrato.getNumeroSerie());
				maquinaPlNovo.setModelo(contrato.getModelo());
				maquinaPlNovo.setHorimetro(bean.getHorimetro());
				maquinaPlNovo.setIdContrato(contrato.getId());
				maquinaPlBusiness.saveOrUpdate(maquinaPlNovo);
				
				manager.getTransaction().begin();
				contrato.setHorimetroUltimaRevisao(bean.getHorimetro());
				manager.merge(contrato);
				Query query = manager.createQuery("From PmpConfigOperacional where idContrato.id = :id");
				query.setParameter("id", bean.getAgendamentoBean().getIdContrato());
				PmpConfigOperacional operacional = (PmpConfigOperacional)query.getSingleResult();

				PmpOsOperacional osOperacional = new PmpOsOperacional();
				if(bean.getAgendamentoBean().getIdContHorasStandard() != null && bean.getAgendamentoBean().getIdContHorasStandard() > 0
						&& !operacional.getIdContrato().getIdClassificacaoContrato().getSigla().equals("PLUS")){
					query  = manager.createQuery("From PmpOsOperacional where idContHorasStandard.id = :id");
					query.setParameter("id", bean.getAgendamentoBean().getIdContHorasStandard());
					if(query.getResultList().size() > 0){
						osOperacional = (PmpOsOperacional)query.getSingleResult();
						////	    		if(osOperacional.getNumOs() != null && !"".equals(osOperacional.getNumOs())){
						////	    			return bean;
						////	    		}
						//	    		if(this.verifyCreateOs(osOperacional.getId().toString())){
						//	    			this.startNewOsEstimada(bean, osOperacional, osOperacional.getIdConfigOperacional().getIdContrato().getPrefixo(), osOperacional.getIdConfigOperacional().getIdContrato().getBeginRanger(), osOperacional.getIdConfigOperacional().getIdContrato().getEndRanger());
						//	    			return bean;
						//	    		}
						//					if(osOperacional.getCodErroOsDbs() != null && osOperacional.getCodErroOsDbs().equals("100")){
						//						bean.setMsg("Aguarde o retorno do DBS, pois, a OS já foi enviada!");
						//						return bean;
						//					}
					}
				}else 	if(bean.getAgendamentoBean().getIdContHorasStandard() != null && bean.getAgendamentoBean().getIdContHorasStandard() > 0
						&& operacional.getIdContrato().getIdClassificacaoContrato().getSigla().equals("PLUS")){
					query  = manager.createQuery("From PmpOsOperacional where idContHorasStandardPlus.id = :id");
					query.setParameter("id", bean.getAgendamentoBean().getIdContHorasStandard());
					if(query.getResultList().size() > 0){
						osOperacional = (PmpOsOperacional)query.getSingleResult();
						////	    		if(osOperacional.getNumOs() != null && !"".equals(osOperacional.getNumOs())){
						////	    			return bean;
						////	    		}
						//	    		if(this.verifyCreateOs(osOperacional.getId().toString())){
						//	    			this.startNewOsEstimada(bean, osOperacional, osOperacional.getIdConfigOperacional().getIdContrato().getPrefixo(), osOperacional.getIdConfigOperacional().getIdContrato().getBeginRanger(), osOperacional.getIdConfigOperacional().getIdContrato().getEndRanger());
						//	    			return bean;
						//	    		}
						//					if(osOperacional.getCodErroOsDbs() != null && osOperacional.getCodErroOsDbs().equals("100")){
						//						bean.setMsg("Aguarde o retorno do DBS, pois, a OS já foi enviada!");
						//						return bean;
						//					}
					}
				}
				if(bean.getEndereco() != null && !bean.getEndereco().equals("")){
					operacional.getIdContrato().setEndereco(bean.getEndereco());
				}
				//osOperacional = new PmpOsOperacional();
				osOperacional.setNumOs("Aguarde o retorno do DBS!");
				osOperacional.setIdConfigOperacional(operacional);
				osOperacional.setCodErroOsDbs("100");
				osOperacional.setFilial(Long.valueOf(FILIAL));
				osOperacional.setNumeroSegmento(bean.getSegmento());
				osOperacional.setCscc("SV");
				osOperacional.setJobCode(bean.getJobCode());
				osOperacional.setCompCode(bean.getComponenteCode());
				osOperacional.setInd("E");
				osOperacional.setIdFuncionarioCriadorOs(this.usuarioBean.getMatricula());
				if(operacional.getIdContrato().getIdClassificacaoContrato().getSigla().equals("PLUS")){
					osOperacional.setIdContHorasStandardPlus(manager.find(PmpContHorasStandardPlus.class, bean.getAgendamentoBean().getIdContHorasStandard()));
				}else{
					osOperacional.setIdContHorasStandard(manager.find(PmpContHorasStandard.class, bean.getAgendamentoBean().getIdContHorasStandard()));
				}
				manager.persist(osOperacional);
				
				if(this.verifyCreateOsAgendamento(osOperacional.getId().toString())){
					manager.getTransaction().commit();
					if(operacional.getNumOs() != null && !"".equals(operacional.getNumOs())){
						return bean;
					}
					//this.startNewOsEstimadaContrato(bean, operacional, contrato);
					return bean;
				}
				
				
				query = manager.createNativeQuery("select max(HORIMETRO) from  PMP_MAQUINA_PL where NUMERO_SERIE = '"+operacional.getIdContrato().getNumeroSerie()+"' and HORIMETRO is not null"+
												  " and DATA_ATUALIZACAO = (select MAX(DATA_ATUALIZACAO) from PMP_MAQUINA_PL where NUMERO_SERIE = '"+operacional.getIdContrato().getNumeroSerie()+"' and HORIMETRO is not null)");

				BigDecimal smu = (BigDecimal)query.getSingleResult();
				/*Formata Horimetro e data atual para enviar para o DBS*/
				String horimetro = smu.toString();			
				horimetro += "0";		
				
				while (horimetro.length() < 8){
					horimetro = "0"+ horimetro;
				}		
//				Integer filial = -100;
//				if(operacional.getIdContrato().getIdTipoContrato().getSigla().equals("VEN") || operacional.getIdContrato().getIdTipoContrato().getSigla().equals("REN")){
//					filial = operacional.getFilial().intValue();//filial de destino
//				}else{
//					filial = operacional.getIdContrato().getFilial();//filial de origem
//				}
				String dataHorimetroDbs = dateFormatHorimetroDbs.format(new Date());
				//Cria uma os estimada no dbs
				String filialSessao = this.FILIAL;
				if(this.FILIAL.equals("23") || this.FILIAL.equals("21")){
					this.FILIAL = "0";
				}
				if(!operacional.getIdContrato().getIdClassificacaoContrato().getSigla().equals("PLUS")){
					query = manager.createQuery("from PmpFinanceiro where idContHorasStandard.id =:idContHorasStandard and status = 'L'");
					
				}else{
					query = manager.createQuery("from PmpFinanceiro where idContHorasStandardPlus.id =:idContHorasStandard and status = 'L'");
				}
				query.setParameter("idContHorasStandard", bean.getAgendamentoBean().getIdContHorasStandard());
				List<PmpFinanceiro> financeiroList = (List<PmpFinanceiro>)query.getResultList();
				PmpFinanceiro financeiro = new PmpFinanceiro();
				financeiro.setTermsCod("");
				financeiro.setCrdDbs("");
				for (PmpFinanceiro scFinanceiro : financeiroList) {
					financeiro = scFinanceiro;
					financeiro.setTermsCod("2");
				}
				
				Integer pdp = 0;
				String descPdp = "";
				if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
					pdp = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().intValue();//desconto PDP
				}else if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
					//descPdp = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getPdpSpot().intValue();
					pdp = contrato.getDescontoPdpSpot().intValue();
				}
				if(pdp.toString().length() == 1 && pdp.intValue() > 0){
					descPdp = "0"+pdp;
				}
				if(this.createOsEstimada(osOperacional.getId().toString(), bean.getSegmento(), osOperacional.getCscc(), 
						bean.getJobCode(), bean.getComponenteCode(), osOperacional.getInd(), bean.getJobControl(), ((new Integer(this.FILIAL) < 10)?"0"+this.FILIAL:this.FILIAL).toString(), bean.getCodigoCliente()
								, bean.getEstimateBy(), bean.getMake(), bean.getNumeroSerie(), null, operacional.getIdContrato().getBgrp(), horimetro, dataHorimetroDbs, "A", ((descPdp.equals(""))?"":IConstantAccess.SIGLA_PDP), ((descPdp.equals(""))?"":descPdp),"PE",
								financeiro.getCrdDbs(), financeiro.getTermsCod())){
					//this.startNewOsEstimada(bean, osOperacional, osOperacional.getIdConfigOperacional().getIdContrato().getPrefixo(), osOperacional.getIdConfigOperacional().getIdContrato().getBeginRanger(), osOperacional.getIdConfigOperacional().getIdContrato().getEndRanger());
					con = ConectionDbs.getConnecton();
					
					ResultSet rs = null;

					String SQL = "SELECT WCLPIPS0.EQMFCD, WCLPIPS0.EQMFSN, WCLPIPS0.PIPNO, WCLPIPS0.OPNDT8"+
					" FROM "+IConstantAccess.LIB_DBS+".WCLPIPS0 WCLPIPS0"+
					" WHERE (WCLPIPS0.OPNDT8=0) "+
					" AND WCLPIPS0.EQMFSN = '"+bean.getNumeroSerie()+"'";
					statement = con.createStatement();
					rs = statement.executeQuery(SQL);
					if(rs.next()){
						bean.setMsg("Os estimada enviada com sucesso.\nPOSSIBILIDADE DE PIP/PSP!");
					}
					SQL = "select trim(t.CUNO) CUNO, trim(t.CRLMT) CRLMT, trim(t.TERMCD) TERMCD from "+IConstantAccess.LIB_DBS+".cipname0 t where t.CUNO = '"+bean.getCodigoCliente()+"'";
					statement = con.createStatement();
					rs = statement.executeQuery(SQL);
					if(rs.next()){
						String msg = "";
						if("1".equals(rs.getString("TERMCD"))){
							msg = "O Pagamento deve ser À VISTA, pois o cliente não possui crédito!";
						}else if("2".equals(rs.getString("TERMCD"))){
							msg = "O cliente possui crédito no valor de "+(String.valueOf(ValorMonetarioHelper.formata("###,###,##0.00", Double.valueOf(String.valueOf(rs.getString("CRLMT"))))))+"!";
						}else if("4".equals(rs.getString("TERMCD"))){
							msg = "O cliente está sem movimentação e possui um crédito de "+(String.valueOf(ValorMonetarioHelper.formata("###,###,##0.00", Double.valueOf(String.valueOf(rs.getString("CRLMT"))))))+"!\nFavor providenciar a atualização do cadastro do cliente!";
						}
						if(bean.getMsg() != null){
							bean.setMsg(bean.getMsg()+"\n"+msg);
						}
					}
					manager.getTransaction().commit();
					
					
					return bean;
				}else{
					manager.getTransaction().rollback();
				}
				this.FILIAL = filialSessao;
			} catch (Exception e) {
				if(manager != null && manager.getTransaction().isActive()){
					manager.getTransaction().rollback();
				}
				e.printStackTrace();
			}finally{
				if(manager != null && manager.isOpen()){
					manager.close();
				}
				if(con != null){
					try {
						if(statement != null){
							statement.close();
						}
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			bean.setMsg("Não foi possível criar OS estimada!");
			return bean;
	}
		
	private Boolean createOsEstimadaThread(OsEstimada bean, PmpOsOperacional osOperacional, String prefixo, String beginRanger, String endRanger) {
		
		EntityManager manager = null;
		Connection con = ConectionDbs.getConnecton();
		try {
			Statement stmt = con.createStatement();
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();

			String wono = this.findOsEstimada(osOperacional.getId().toString());
			
			if(wono == null){
				try {
					PmpOsOperacional operacional = manager.find(PmpOsOperacional.class, osOperacional.getId());
					if(operacional.getNumOs() == null || "".equals(operacional.getNumOs())){
						manager.remove(manager.find(PmpOsOperacional.class, osOperacional.getId()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
			if("".equals(wono)){
				return false;
			}else{

				osOperacional.setNumOs(wono.trim());			
				manager.merge(osOperacional);
				this.sendPecasDbs(osOperacional,manager, stmt);	
				//Coloca a OS como Open
				//this.openOs(osOperacional.getId(), wono.trim());
				
			}
			manager.getTransaction().commit();	

			if(bean.getVcc().getTipoCliente() != null && !bean.getVcc().getTipoCliente().equals("")){
				if(bean.getVcc().getTipoCliente().equals("INT")){
					if(!this.updateIntoClienteInterOrExcecaoGarantiaDbs(wono,bean.getVcc().getClienteInter(), bean.getVcc().getContaContabilSigla(), bean.getVcc().getCentroDeCustoSigla(),bean.getSegmento(), osOperacional.getId().toString())){
						bean.setMsg("A Ordem de serviço extimada foi criada, mas não foi possível cadastrar centro de custo e conta contábil!");
					}
				}else if(bean.getVcc().getTipoCliente().equals("EXT")){
					if(!this.updateGarantiaClienteExternoDbs(wono, bean.getVcc().getContaContabilSigla(), bean.getVcc().getCentroDeCustoSigla(),bean.getSegmento(),osOperacional.getId().toString())){
						bean.setMsg("A Ordem de serviço extimada foi criada, mas não foi possível cadastrar centro de custo e conta contábil!");
					}
				}
			}else{
				//significa que é garantica com a regra de excessão
				if(!("").equals(bean.getVcc().getClienteInter())){
					if(!this.updateIntoClienteInterOrExcecaoGarantiaDbs(wono,bean.getVcc().getClienteInter(), bean.getVcc().getContaContabilSigla(), bean.getVcc().getCentroDeCustoSigla(),bean.getSegmento(),osOperacional.getId().toString())){
						bean.setMsg("A Ordem de serviço extimada foi criada, mas não foi possível cadastrar centro de custo e conta contábil!");
					}
				}else{//grantia sem regra de excessão
					if(!this.updateGarantiaClienteExternoDbs(wono, bean.getVcc().getContaContabilSigla(), bean.getVcc().getCentroDeCustoSigla(),bean.getSegmento(),osOperacional.getId().toString())){
						bean.setMsg("A Ordem de serviço extimada foi criada, mas não foi possível cadastrar centro de custo e conta contábil!");
					}
				}
			}

		}catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		}finally{
			try {
				if(manager != null && manager.isOpen()){
					manager.close();
				}
				if(con != null){
					con.close();					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public Boolean sendPecasDbs(Long idContHorasStandard){
		EntityManager manager = null;
		Connection conn = null;
		Statement stament = null;
		try{
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpContHorasStandard chs = manager.find(PmpContHorasStandard.class, idContHorasStandard);
			chs.getIdOsOperacional().setCodErroDocDbs("100");
			chs.getIdOsOperacional().setMsg("Aguarde o retorno da cotação!");
			conn = com.pmp.util.ConectionDbs.getConnecton();
			stament = conn.createStatement();
			this.sendPecasReviewDbs(chs.getIdOsOperacional(), manager, stament);
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
				try {
					if(conn != null){
						conn.close();
					}						
					if(stament != null){
						stament.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		
		return false;
	}

	public void sendPecasDbs(PmpOsOperacional osOperacional,
			EntityManager manager, Statement stmt)
			throws SQLException {
		try {
			
			Query query = manager.createNativeQuery("select ID_TIPO_CUSTOMIZACAO from PMP_CONTRATO_CUSTOMIZACAO where id_contrato =:id_contrato");
			query.setParameter("id_contrato", osOperacional.getIdContHorasStandard().getIdContrato().getId());
			List<BigDecimal> contratoCustList =  query.getResultList();
			String siglaCustomizacao = "";
			String complementoSigla = "";
			for (BigDecimal idTipoCustomizacao : contratoCustList) {
						query = manager.createNativeQuery("select sc.sigla_customizacao from PMP_SIGLA_CUSTOMIZACAO sc, PMP_CONFIG_CUSTOMIZACAO cc"+
													" where cc.ID_TIPO_CUSTOMIZACAO =:ID_TIPO_CUSTOMIZACAO"+
													" and sc.id_config_customizacao = cc.id");
						query.setParameter("ID_TIPO_CUSTOMIZACAO", idTipoCustomizacao);
						List<String> sgTipoCustList = query.getResultList();
						for (String string : sgTipoCustList) {
							siglaCustomizacao += "'"+string+"',";
						}
				}
			if(siglaCustomizacao.length() > 0){
				siglaCustomizacao = siglaCustomizacao.substring(0, siglaCustomizacao.length() - 1);
			}else{
				siglaCustomizacao = "'-null'";
			}
			
			String complemento = "";
			if(osOperacional.getIdConfigOperacional().getIdContrato().getIdConfigTracao() != null){
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+osOperacional.getIdConfigOperacional().getIdContrato().getIdConfigTracao().getId()+") or ocptmd is null)";
				complemento += " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+osOperacional.getIdConfigOperacional().getIdContrato().getIdConfigTracao().getId()+") or JWKAPP is null)"+			
				 " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+osOperacional.getIdConfigOperacional().getIdContrato().getIdConfigTracao().getId()+") or ojbloc is null)";
			}
			
			String complementoCustPart = "";
			if(osOperacional.getIdConfigOperacional().getIdContrato().getIdClassificacaoContrato().getSigla().equals("CUS") || osOperacional.getIdConfigOperacional().getIdContrato().getIdClassificacaoContrato().getSigla().equals("CUSLIGHT") || osOperacional.getIdConfigOperacional().getIdContrato().getIdClassificacaoContrato().getSigla().equals("PART")){
				complementoCustPart = " and m.OJBLOC <> 'CST'";
			}
			
			if(osOperacional.getIdContHorasStandard().getIsPartner() != null && !osOperacional.getIdContHorasStandard().getIsPartner().equals("N")){
				
				query = manager.createNativeQuery("select descricao from Pmp_Comp_Code_Partner");
				List<String> descricao = (List<String>)query.getResultList();
				for (String cptcd : descricao) {
					if(osOperacional.getIdContHorasStandard().getStandardJobCptcd().equals(cptcd)){
						complementoCustPart = " and m.OJBLOC <> 'CST'";
					}
				}
				//complementoCustPart = " and m.OJBLOC <> 'CST'";
			}else if(osOperacional.getIdContHorasStandard().getIdContrato().getIdClassificacaoContrato().getSigla().equals("PART")){
				if(osOperacional.getIdContHorasStandard().getIsPartner() == null || (osOperacional.getIdContHorasStandard().getIsPartner().equals("N"))){
					//complementoCustPart = " and m.OJBLOC <> 'CST'";
					complementoCustPart = "";
				}
			}
			
			if(siglaCustomizacao.length() > 0){
				complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and JWKAPP not in ("+siglaCustomizacao+")";
			}
			
			PmpContHorasStandard horasStandard = manager.find(PmpContHorasStandard.class, osOperacional.getIdContHorasStandard().getId());
			horasStandard.setIdOsOperacional(osOperacional);
			manager.merge(horasStandard);
			String SQL = "select m.pano20,  m.dlrqty, m.sos, m.ds18  from pmp_manutencao m"+
					" where m.cptcd = '"+osOperacional.getCompCode()+"'"+
					" and m.bgrp = '"+horasStandard.getIdContrato().getBgrp()+"'"+
					" and substring(m.beqmsn,1,4) = '"+osOperacional.getIdConfigOperacional().getIdContrato().getPrefixo()+"'"+
					 complemento + complementoSigla + complementoCustPart+
					" and substring(m.beqmsn,5,10) between '"+osOperacional.getIdConfigOperacional().getIdContrato().getBeginRanger().substring(4, 9)+"' and '"+osOperacional.getIdConfigOperacional().getIdContrato().getEndRanger().substring(4, 9)+"'";
			if(horasStandard.getIdContrato().getIdClassificacaoContrato().getSigla()
					.equals("CUSLIGHT")){
				SQL += " and m.sos <> '050' ";
			}
			query = manager.createNativeQuery(SQL);
			List<Object[]> list = query.getResultList();
			//manager.getTransaction().begin();
			stmt.executeUpdate("delete from "+IConstantAccess.AMBIENTE_DBS+".USPPLSM0 where PEDSM = '"+osOperacional.getId()+"-P'");
			stmt.executeUpdate("delete from "+IConstantAccess.AMBIENTE_DBS+".USPPWSM0 where PEDSM = '"+osOperacional.getId()+"-P'");
			for (Object[] pair : list) {
				SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPPLSM0 (PEDSM, SOS, PANO20, QTDE) values('"+osOperacional.getId()+"-P', '"+(String)pair[2]+"', '"+((String)pair[0]).replace("-", "")+"', '"+((pair[1] != null)?((BigDecimal)pair[1]).intValue():0)+"')";
				stmt.executeUpdate(SQL);
				PmpPecasOsOperacional operacional = new PmpPecasOsOperacional();
				operacional.setIdOsOperacional(osOperacional);
				operacional.setSos((String)pair[2]);
				operacional.setNomePeca((String)pair[3]);
				operacional.setNumPeca((String)pair[0]);
				operacional.setQtd(((pair[1] != null)?((BigDecimal)pair[1]).longValue():0));
				manager.persist(operacional);
			}
			//manager.getTransaction().commit();
			
			if((horasStandard.getIdContrato().getIdClassificacaoContrato().getSigla().equals("PART") && horasStandard.getIsPartner().equals("S")) || horasStandard.getIdContrato().getIdClassificacaoContrato().getSigla().equals("CUS")){
				this.enviarEnderecoNotesDbs(osOperacional, stmt);
			}
			SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPPWSM0 (PEDSM, WONOSM, SGNOSM, OPERSM) values('"+osOperacional.getId()+"-P', '"+osOperacional.getNumOs()+"', '01', '')";
			stmt.executeUpdate(SQL);
		} catch (Exception e) {
//			if(manager != null && manager.getTransaction().isActive()){
//				manager.getTransaction().rollback();
//			}
			e.printStackTrace();
		}
	}
	
	private void enviarEnderecoNotesDbs(PmpOsOperacional osOperacional, Statement stmt){
		try {
			stmt.executeUpdate("delete from "+IConstantAccess.AMBIENTE_DBS+".USPNTSM0 where PEDSM = '"+osOperacional.getId()+"-P'");
			String obsOS = osOperacional.getIdConfigOperacional().getIdContrato().getEndereco();
			String aux = "";
			int initIndex = 0;
			int lengthIndex = 48;
			Integer linha = 1;
			for (initIndex = 0; initIndex < obsOS.length() ; initIndex++) {
				aux += obsOS.charAt(initIndex);
				if(initIndex -1 == lengthIndex){
					//prstmt = ConectionDbs.setNotesFluxoOSDBS(aux.toUpperCase(), conn, numeroOs, (linha < 100)?"0"+linha:linha+"");
					String SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPNTSM0 (PEDSM, SEQ, COD, NOTES) values('"+osOperacional.getId()+"-P', '"+((linha < 10)?"0"+linha:linha)+"', '6', '"+aux.replaceAll("'", "")+"')";
					stmt.executeUpdate(SQL);
					lengthIndex += 50;
					linha += 1;
					aux = "";
				}
			}
			if(aux.length() > 0){
				String SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPNTSM0 (PEDSM, SEQ, COD, NOTES) values('"+osOperacional.getId()+"-P', '"+((linha < 10)?"0"+linha:linha)+"', '6', '"+aux.replaceAll("'", "")+"')";
				stmt.executeUpdate(SQL);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void sendPecasDbsPlus(PmpOsOperacional osOperacional,
			EntityManager manager, Statement stmt, List<PecaBean> list)
	throws SQLException {
		try {
			
			Query query = manager.createNativeQuery("select ID_TIPO_CUSTOMIZACAO from PMP_CONTRATO_CUSTOMIZACAO where id_contrato =:id_contrato");
			query.setParameter("id_contrato", osOperacional.getIdContHorasStandardPlus().getIdContrato().getId());
			List<BigDecimal> contratoCustList =  query.getResultList();
			String siglaCustomizacao = "";
			String complementoSigla = "";
			for (BigDecimal idTipoCustomizacao : contratoCustList) {
				query = manager.createNativeQuery("select sc.sigla_customizacao from PMP_SIGLA_CUSTOMIZACAO sc, PMP_CONFIG_CUSTOMIZACAO cc"+
						" where cc.ID_TIPO_CUSTOMIZACAO =:ID_TIPO_CUSTOMIZACAO"+
				" and sc.id_config_customizacao = cc.id");
				query.setParameter("ID_TIPO_CUSTOMIZACAO", idTipoCustomizacao);
				List<String> sgTipoCustList = query.getResultList();
				for (String string : sgTipoCustList) {
					siglaCustomizacao += "'"+string+"',";
				}
			}
			if(siglaCustomizacao.length() > 0){
				siglaCustomizacao = siglaCustomizacao.substring(0, siglaCustomizacao.length() - 1);
			}else{
				siglaCustomizacao = "'-null'";
			}
			
			String complemento = "";
			if(osOperacional.getIdConfigOperacional().getIdContrato().getIdConfigTracao() != null){
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+osOperacional.getIdConfigOperacional().getIdContrato().getIdConfigTracao().getId()+") or ocptmd is null)"
				+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+osOperacional.getIdConfigOperacional().getIdContrato().getIdConfigTracao().getId()+") or ojbloc is null)";
			}
			
			if(siglaCustomizacao.length() > 0){
				complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
			}
			
			PmpContHorasStandardPlus horasStandard = manager.find(PmpContHorasStandardPlus.class, osOperacional.getIdContHorasStandardPlus().getId());
			horasStandard.setIdOsOperacional(osOperacional);
//			manager.merge(horasStandard);
//			String SQL = "select m.pano20,  m.dlrqty, m.sos, m.ds18  from pmp_manutencao m"+
//			" where m.cptcd = '"+osOperacional.getCompCode()+"'"+
//			" and m.bgrp = '"+horasStandard.getIdContrato().getBgrp()+"'"+
//			" and substring(m.beqmsn,1,4) = '"+osOperacional.getIdConfigOperacional().getIdContrato().getPrefixo()+"'"+
//			complemento + complementoSigla +
//			" and substring(m.beqmsn,5,10) between '"+osOperacional.getIdConfigOperacional().getIdContrato().getBeginRanger().substring(4, 9)+"' and '"+osOperacional.getIdConfigOperacional().getIdContrato().getEndRanger().substring(4, 9)+"'";
//			
//			query = manager.createNativeQuery(SQL);
//			List<Object[]> list = query.getResultList();
			//manager.getTransaction().begin();
			stmt.executeUpdate("delete from "+IConstantAccess.AMBIENTE_DBS+".USPPLSM0 where PEDSM = '"+osOperacional.getId()+"-P'");
			stmt.executeUpdate("delete from "+IConstantAccess.AMBIENTE_DBS+".USPPWSM0 where PEDSM = '"+osOperacional.getId()+"-P'");
			for (PecaBean pair : list) {
				String SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPPLSM0 (PEDSM, SOS, PANO20, QTDE) values('"+osOperacional.getId()+"-P', '"+pair.getSos()+"', '"+(pair.getPano20()).replace("-", "")+"', '"+((pair.getDlrqty() > 0)?pair.getDlrqty():0)+"')";
				stmt.executeUpdate(SQL);
				PmpPecasOsOperacional operacional = new PmpPecasOsOperacional();
				operacional.setIdOsOperacional(osOperacional);
				operacional.setSos(pair.getSos());
				operacional.setNomePeca(pair.getDs18());
				operacional.setNumPeca(pair.getPano20());
				operacional.setQtd(Long.valueOf(pair.getDlrqty()));
				manager.persist(operacional);
			}
			//manager.getTransaction().commit();
			
			String SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPPWSM0 (PEDSM, WONOSM, SGNOSM, OPERSM) values('"+osOperacional.getId()+"-P', '"+osOperacional.getNumOs()+"', '01', '')";
			stmt.executeUpdate(SQL);
		} catch (Exception e) {
//			if(manager != null && manager.getTransaction().isActive()){
//				manager.getTransaction().rollback();
//			}
			e.printStackTrace();
		}
	}

	public void sendTotalPecasPecasDbs(PmpContrato contrato,
			EntityManager manager, Statement stmt, String inCptcd, String siglaCustomizacao)
	throws SQLException {
		try {

			String complemento = "";
			String complementoSigla = "";
			if(contrato.getIdConfigTracao() != null){
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ocptmd is null)"
							+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ojbloc is null)";
			}
			if(siglaCustomizacao.length() > 0){
				complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
			}
			Query query = manager.createNativeQuery("delete from Pmp_Pecas_Config_Operacional where id_contrato = "+contrato.getId());
			query.executeUpdate();
			String SQL = "select m.pano20,  m.dlrqty, m.sos, m.ds18, m.bectyc, m.cptcd  from pmp_manutencao m"+
			" where m.cptcd in ("+inCptcd+")"+
			" and m.bgrp = '"+contrato.getBgrp()+"'"+
			" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
			  complemento + complementoSigla +
			" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'";
			
			query = manager.createNativeQuery(SQL);
			List<Object[]> list = query.getResultList();
			//manager.getTransaction().begin();
			stmt.executeUpdate("delete from "+IConstantAccess.AMBIENTE_DBS+".USPPLSM0 where PEDSM = 'C-"+contrato.getId()+"'");
			stmt.executeUpdate("delete from "+IConstantAccess.AMBIENTE_DBS+".USPPHSM0 where PEDSM = 'C-"+contrato.getId()+"'");
			for (Object[] pair : list) {
				SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPPLSM0 (PEDSM, SOS, PANO20, QTDE) values('C-"+contrato.getId()+"', '"+(String)pair[2]+"', '"+((String)pair[0]).replace("-", "")+"', '"+((pair[1] != null)?((BigDecimal)pair[1]).intValue():0)+"')";
				stmt.executeUpdate(SQL);
				PmpPecasConfigOperacional operacional = new PmpPecasConfigOperacional();
				operacional.setIdContrato(contrato);
				operacional.setSos((String)pair[2]);
				operacional.setNomePeca((String)pair[3]);
				operacional.setNumPeca((String)pair[0]);
				operacional.setQtd(((pair[1] != null)?((BigDecimal)pair[1]).longValue():0));
				operacional.setBectyc((String)pair[4]);
				operacional.setCptcd((String)pair[5]);
				manager.persist(operacional);
			}
			//manager.getTransaction().commit();
			
			//SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPPWSM0 (PEDSM, WONOSM, SGNOSM, OPERSM) values('"+osOperacional.getId()+"-C', '"+osOperacional.getNumOs()+"', '01', '')";
			SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPPHSM0 (PEDSM, STNSM, CUNOSM) values('C-"+contrato.getId()+"', '"+((contrato.getFilial()< 10)?"0"+contrato.getFilial():contrato.getFilial())+"', '"+contrato.getCodigoCliente()+"')";
			stmt.executeUpdate(SQL);
		} catch (Exception e) {
//			if(manager != null && manager.getTransaction().isActive()){
//				manager.getTransaction().rollback();
//			}
			e.printStackTrace();
		}
	}
	
	public void sendPecasReviewDbs(PmpOsOperacional osOperacional,
			EntityManager manager, Statement stmt)
	throws SQLException {
					
			String SQL = "from PmpPecasOsOperacional where idOsOperacional.id =:idOsOperacional";			
			Query query = manager.createQuery(SQL);
			query.setParameter("idOsOperacional", osOperacional.getId());
			List<PmpPecasOsOperacional> list = query.getResultList();
			//manager.getTransaction().begin();
			stmt.executeUpdate("delete from "+IConstantAccess.AMBIENTE_DBS+".USPPLSM0 where PEDSM = '"+osOperacional.getId()+"-P'");
			stmt.executeUpdate("delete from "+IConstantAccess.AMBIENTE_DBS+".USPPWSM0 where PEDSM = '"+osOperacional.getId()+"-P'");
			for (PmpPecasOsOperacional operacional : list) {
				SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPPLSM0 (PEDSM, SOS, PANO20, QTDE) values('"+osOperacional.getId()+"-P', '"+operacional.getSos()+"', '"+operacional.getNumPeca()+"', '"+operacional.getQtd()+"')";
				stmt.executeUpdate(SQL);
			}
			//manager.getTransaction().commit();
			
			SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPPWSM0 (PEDSM, WONOSM, SGNOSM, OPERSM) values('"+osOperacional.getId()+"-P', '"+osOperacional.getNumOs()+"', '01', '')";
			stmt.executeUpdate(SQL);
		
	}
	
	public OsEstimada newOsEstimadaContrato(OsEstimada bean){
		EntityManager manager = null;
		try {
			bean.setMsg("");
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			Query query = manager.createQuery("From PmpContrato where numeroContrato = '"+bean.getNumContrato()+"'");
			PmpContrato contrato = (PmpContrato)query.getSingleResult();
			query = manager.createQuery("From PmpConfigOperacional where idContrato.id = :id");
			query.setParameter("id", contrato.getId());
			PmpConfigOperacional operacional = (PmpConfigOperacional)query.getSingleResult();
			operacional.setCodErroDbs("100");
			operacional.setNumOs("Aguarde o retorno do DBS!");
			operacional.setNumeroSegmento(bean.getSegmento());
			operacional.setCscc(bean.getJobControl());
			operacional.setJobCode(bean.getJobCode());
			operacional.setCompCode(bean.getComponenteCode());
			operacional.setIdFuncionarioCriadorOs(this.usuarioBean.getMatricula());
			operacional.setInd("E");
			
			if(this.verifyCreateOs(operacional.getId().toString())){
				if(operacional.getNumOs() != null && !"".equals(operacional.getNumOs())){
					return bean;
				}
				//this.startNewOsEstimadaContrato(bean, operacional, contrato);
				return bean;
			}

			/*Formata Horimetro e data atual para enviar para o DBS*/
			String horimetro = contrato.getHorimetro().toString();			
			horimetro += "0";		
			
			while (horimetro.length() < 8){
				horimetro = "0"+ horimetro;
			}		
			String dataHorimetroDbs = dateFormatHorimetroDbs.format(new Date());
			//Cria uma os estimada no dbs
			String filialSessao = this.FILIAL;
			if(this.FILIAL.equals("23") || this.FILIAL.equals("21")){
				this.FILIAL = "0";
			}
			Integer descPdp = 0;
			if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
				descPdp = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().intValue();//desconto PDP
			}else if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
				//descPdp = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getPdpSpot().intValue();
				descPdp = contrato.getDescontoPdpSpot().intValue();
			}
			if(this.createOsEstimada(operacional.getId().toString(), bean.getSegmento(),"SV", 
					bean.getJobCode(), bean.getComponenteCode(), "E", bean.getJobControl(), (Integer.valueOf(this.FILIAL) < 10)?"0"+this.FILIAL:this.FILIAL, bean.getCodigoCliente()
							, bean.getEstimateBy(), bean.getMake(), bean.getNumeroSerie(), null, contrato.getBgrp(), horimetro, dataHorimetroDbs, "P", IConstantAccess.SIGLA_PDP, 
							descPdp.toString(),"","","")){
				
				//this.startNewOsEstimadaContrato(bean, operacional, contrato);
				Connection con = ConectionDbs.getConnecton();
				Statement statement = null;
				ResultSet rs = null;
				try {
					String SQL = "SELECT WCLPIPS0.EQMFCD, WCLPIPS0.EQMFSN, WCLPIPS0.PIPNO, WCLPIPS0.OPNDT8"+
									" FROM "+IConstantAccess.LIB_DBS+".WCLPIPS0 WCLPIPS0"+
									" WHERE (WCLPIPS0.OPNDT8=0) "+
									" AND WCLPIPS0.EQMFSN = '"+bean.getNumeroSerie()+"'";
					statement = con.createStatement();
					rs = statement.executeQuery(SQL);
					if(rs.next()){
						bean.setMsg("OS enviada com sucesso!\nPOSSIBILIDADE DE PIP/PSP!");
					}
					SQL = "select trim(t.CUNO) CUNO, trim(t.CRLMT) CRLMT, trim(t.TERMCD) TERMCD from "+IConstantAccess.LIB_DBS+".cipname0 t where t.CUNO = '"+bean.getCodigoCliente()+"'";
					statement = con.createStatement();
					rs = statement.executeQuery(SQL);
					if(rs.next()){
						String msg = "";
						if("1".equals(rs.getString("TERMCD"))){
							msg = "O Pagamento deve ser À VISTA, pois o cliente não possui crédito!";
						}else if("2".equals(rs.getString("TERMCD"))){
							msg = "O cliente possui crédito no valor de "+(String.valueOf(ValorMonetarioHelper.formata("###,###,##0.00", Double.valueOf(String.valueOf(rs.getString("CRLMT"))))))+"!";
						}else if("4".equals(rs.getString("TERMCD"))){
							msg = "O cliente está sem movimentação e possui um crédito de "+(String.valueOf(ValorMonetarioHelper.formata("###,###,##0.00", Double.valueOf(String.valueOf(rs.getString("CRLMT"))))))+"!\nFavor providenciar a atualização do cadastro do cliente!";
						}
						if(bean.getMsg() != null){
							bean.setMsg(bean.getMsg()+"\n"+msg);
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(con != null){
						try {
							if(statement != null){
								statement.close();								
							}
							con.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
				this.FILIAL = filialSessao;
				manager.getTransaction().commit();
				return bean;
			}else{
				if(manager != null && manager.getTransaction().isActive()){
					manager.getTransaction().rollback();
				}
			}
			
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
    	bean.setMsg("Não foi possível criar OS estimada!");
    	return bean;
		
	}

	private Boolean createOsEstimadaContratoThread(OsEstimada bean, PmpConfigOperacional operacional, PmpContrato contrato) {
		
		EntityManager manager = null;

		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			
	
				String wono = this.findOsEstimada(operacional.getId().toString());
				if(wono == null){
					return true;
				}
				if("".equals(wono.trim())){
					return false;
				}
				
				operacional.setNumOs(wono);
				manager.merge(operacional);
				contrato.setNumOs(wono);
				manager.merge(contrato);
				manager.getTransaction().commit();	
				//Coloca a OS como Open
				//this.openOs(operacional.getId(), wono.trim());
				if(bean.getVcc().getTipoCliente() != null && !bean.getVcc().getTipoCliente().equals("")){
					if(bean.getVcc().getTipoCliente().equals("INT")){
						if(!this.updateIntoClienteInterOrExcecaoGarantiaDbs(wono,bean.getVcc().getClienteInter(), bean.getVcc().getContaContabilSigla(), bean.getVcc().getCentroDeCustoSigla(), bean.getSegmento(), operacional.getId().toString())){
							bean.setMsg("A Ordem de serviço extimada foi criada, mas não foi possível cadastrar centro de custo e conta contábil!");
						}
					}else if(bean.getVcc().getTipoCliente().equals("EXT")){
						if(!this.updateGarantiaClienteExternoDbs(wono, bean.getVcc().getContaContabilSigla(), bean.getVcc().getCentroDeCustoSigla(), bean.getSegmento(), operacional.getId().toString())){
							bean.setMsg("A Ordem de serviço extimada foi criada, mas não foi possível cadastrar centro de custo e conta contábil!");
						}
					}
				}else{
					//significa que é garantica com a regra de excessão
					if(!("").equals(bean.getVcc().getClienteInter())){
						if(!this.updateIntoClienteInterOrExcecaoGarantiaDbs(wono,bean.getVcc().getClienteInter(), bean.getVcc().getContaContabilSigla(), bean.getVcc().getCentroDeCustoSigla(),bean.getSegmento(), operacional.getId().toString())){
							bean.setMsg("A Ordem de serviço extimada foi criada, mas não foi possível cadastrar centro de custo e conta contábil!");
						}
					}else{//grantia sem regra de excessão
						if(!this.updateGarantiaClienteExternoDbs(wono, bean.getVcc().getContaContabilSigla(), bean.getVcc().getCentroDeCustoSigla(), bean.getSegmento(), operacional.getId().toString())){
							bean.setMsg("A Ordem de serviço extimada foi criada, mas não foi possível cadastrar centro de custo e conta contábil!");
						}
					}
				}
			
		}catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return true;
	}
	
	private boolean updateIntoClienteInterOrExcecaoGarantiaDbs(String wono, String clienteInter, String contaContabilSigla, String centroDeCustoSigla, String segmento, String wonosm){
		Connection con = null;

//		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
//		Properties prop = new Properties();
		try {
//			prop.load(in);
//			String url = prop.getProperty("dbs.url");
//			String user = prop.getProperty("dbs.user");
//			String password =prop.getProperty("dbs.password");
//			Class.forName(prop.getProperty("dbs.driver")).newInstance();

			//			String url = "jdbc:as400://192.168.128.146";
			//			String user = "XUPU15PSS";
			//			String password = "marcosa";
			//			Class.forName("com.ibm.as400.access.AS400JDBCDriver").newInstance();
			con = com.pmp.util.ConectionDbs.getConnecton();
			Statement statement = con.createStatement();
			String SQL = "insert into LIBU15FTP.USPIFSM0 (LBCUNO, MSCUNO, PTCUNO, PTDOLP, LBDOLP, MSDOLP, WONO, WOSGNO, WONOSM) values('"+clienteInter+"', '"+clienteInter+"', '"+clienteInter+"','100', '100', '100','"+wono+"','"+segmento+"','"+wonosm+"PM')";
							//" where GS.WONO = '"+wono+"'";

			statement.executeUpdate(SQL);
//			statement = con.createStatement();
//			Thread.sleep(30000);
//			statement.executeUpdate("delete from LIBU15FTP.USPIFSM0 where WONOSM = "+wonosm);
			statement = con.createStatement();
			SQL = "insert into LIBU15FTP.USPIFSM0 (CONTA, WONO, WOSGNO, WONOSM) values( '"+contaContabilSigla+"          "+centroDeCustoSigla+"', '"+wono+"','"+segmento+"','"+wonosm+"PMCC')";
					//" where WONO = '"+wono+"'";
			statement.executeUpdate(SQL);	
//			statement = con.createStatement();
//			Thread.sleep(30000);
//			statement.executeUpdate("delete from LIBU15FTP.USPIFSM0 where WONOSM = "+wonosm);		
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			try {
				if(con != null){
					con.close();					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	private boolean updateGarantiaClienteExternoDbs(String wono, String contaContabilSigla, String centroDeCustoSigla, String segmento, String wonosm){
		Connection con = null;
		
//		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
//		Properties prop = new Properties();
		try {
//			prop.load(in);
//			String url = prop.getProperty("dbs.url");
//			String user = prop.getProperty("dbs.user");
//			String password =prop.getProperty("dbs.password");
//			Class.forName(prop.getProperty("dbs.driver")).newInstance();
			
			//			String url = "jdbc:as400://192.168.128.146";
			//			String user = "XUPU15PSS";
			//			String password = "marcosa";
			//			Class.forName("com.ibm.as400.access.AS400JDBCDriver").newInstance();
			con = com.pmp.util.ConectionDbs.getConnecton(); 
			Statement statement = con.createStatement();
		
			String SQL = "insert into LIBU15FTP.USPIFSM0 (CONTA, WONO, WOSGNO, WONOSM) values( '"+contaContabilSigla+"          "+centroDeCustoSigla+"', '"+wono+"','"+segmento+"','"+wonosm+"PM')";;
			//" where WONO like '"+wono+"%'";
			statement.executeUpdate(SQL);
//			statement = con.createStatement();
//			Thread.sleep(30000);
//			statement.executeUpdate("delete from LIBU15FTP.USPIFSM0 where WONOSM = "+wonosm);
			con.commit();
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			try {
				if(con != null){
					con.close();					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Boolean createOsEstimada(String wonosm, String wosgno, String cscc, String jbcd, String cptcd, String ind,
			String respar, String stn1, String cuno, String estby, String eqmfcd, String eqmfsn, String shpfld, String bgrp, String horimetro, 
			String dataHorimetroDbs, String sufixo, String tax2, String tax3, String tax5, String NROREQ, String TERMSCD){

		Connection con = null;

//		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
//		Properties prop = new Properties();

		try {
//			prop.load(in);
//			String url = prop.getProperty("dbs.url");
//			String user = prop.getProperty("dbs.user");
//			String password =prop.getProperty("dbs.password");
//			Class.forName(prop.getProperty("dbs.driver")).newInstance();

			String pair = "'"+wonosm+"-"+sufixo+"','"+cuno+"','"+respar+"','"+stn1+"','"+estby+"','"+eqmfcd+"','"+eqmfsn+"','"+horimetro+"','"+dataHorimetroDbs+"','"+tax2+"','"+tax3+"','"+tax5+"','"+NROREQ+"','"+TERMSCD+"'";
			con = com.pmp.util.ConectionDbs.getConnecton();
			con.setAutoCommit(true);
			Statement statement = con.createStatement();

			String SQL = "delete from "+IConstantAccess.AMBIENTE_DBS+".USPWOSM0 where wonosm = '"+wonosm+"-"+sufixo+"'";// (wonosm, cuno, respar, stn1, estby, eqmfcd, eqmfsn, bgrp) values("+pair+")";
			statement.executeUpdate(SQL);
			
			statement = con.createStatement();
			SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPWOSM0 (wonosm, cuno, respar, stn1, estby, eqmfcd, eqmfsn, smu, datal, tax2,tax3,tax5, NROREQ, TERMSCD) values("+pair+")";
			statement.executeUpdate(SQL);
			//prstmt_ = con.prepareStatement(SQL);
			//rs = prstmt_.executeQuery();
           //s con.commit();

//			pair = "'"+wonosm+"-P','"+wosgno+"','"+cscc+"','"+jbcd+"','"+cptcd+"','"+ind+"','F'";
//			//con = DriverManager.getConnection(url, user, password);  
//			SQL = "insert into "+IConstantAccess.AMBIENTE_DBS+".USPSGSM0 (wonosm, wosgno, cscc, jbcd, cptcd, ind, shpfld) values("+pair+")";
//			//prstmt_ = con.prepareStatement(SQL);
//			statement.executeUpdate(SQL);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			try {
				if(con != null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean verifyCreateOs(String idAgenda){
		ResultSet rs = null;
		PreparedStatement prstmt_ = null;
//		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
//		Properties prop = new Properties();
		Connection con = null;
		try {
//			prop.load(in);
//			String url = prop.getProperty("dbs.url");
//			String user = prop.getProperty("dbs.user");
//			String password =prop.getProperty("dbs.password");
//			Class.forName(prop.getProperty("dbs.driver")).newInstance();

			con = com.pmp.util.ConectionDbs.getConnecton(); 
			//Class.forName("com.ibm.as400.access.AS400JDBCDriver").newInstance();
			//con = DriverManager.getConnection("jdbc:as400://192.168.128.146", "XUPU15PSS", "marcosa"); 
			String SQL = "select w.wono, w.wonosm, w.descerr, w.coderr from "+IConstantAccess.AMBIENTE_DBS+".USPWOSM0 w where w.wonosm = '"+idAgenda+"-P' and w.wono <> ''";
			prstmt_ = con.prepareStatement(SQL);
			rs = prstmt_.executeQuery();

			if(rs.next()){
				String wono = rs.getString("wono");
				String coderr = rs.getString("coderr");
				if(wono == null || "".equals(wono.trim())){
					if(coderr != null && !"".equals(coderr)){
						return false;
					}
					return true;
				}else{
					return true;
				}
			}
		}catch (Exception e) { 
			e.printStackTrace();
		}finally{
			try {
				if(con != null){
					con.close();
				}
				if(prstmt_ != null){
					prstmt_.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	private boolean verifyCreateOsAgendamento(String idAgenda){
		ResultSet rs = null;
		PreparedStatement prstmt_ = null;
//		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
//		Properties prop = new Properties();
		Connection con = null;
		try {
//			prop.load(in);
//			String url = prop.getProperty("dbs.url");
//			String user = prop.getProperty("dbs.user");
//			String password =prop.getProperty("dbs.password");
//			Class.forName(prop.getProperty("dbs.driver")).newInstance();
			
			con = com.pmp.util.ConectionDbs.getConnecton(); 
			//Class.forName("com.ibm.as400.access.AS400JDBCDriver").newInstance();
			//con = DriverManager.getConnection("jdbc:as400://192.168.128.146", "XUPU15PSS", "marcosa"); 
			String SQL = "select w.wono, w.wonosm, w.descerr, w.coderr from "+IConstantAccess.AMBIENTE_DBS+".USPWOSM0 w where w.wonosm = '"+idAgenda+"-A' and w.wono <> ''";
			prstmt_ = con.prepareStatement(SQL);
			rs = prstmt_.executeQuery();
			
			if(rs.next()){
				String wono = rs.getString("wono");
				String coderr = rs.getString("coderr");
				if(wono == null || "".equals(wono.trim())){
					if(coderr != null && !"".equals(coderr)){
						return false;
					}
					return true;
				}else{
					return true;
				}
			}
		}catch (Exception e) { 
			e.printStackTrace();
		}finally{
			try {
				if(con != null){
					con.close();
				}
				if(prstmt_ != null){
					prstmt_.close();						
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public String findOsEstimada(String idAgenda) {
		ResultSet rs = null;
		PreparedStatement prstmt_ = null;

		Connection con = null;
//		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
//		Properties prop = new Properties();

		String wono = "";
		try {

//			prop.load(in);
//			String url = prop.getProperty("dbs.url");
//			String user = prop.getProperty("dbs.user");
//			String password =prop.getProperty("dbs.password");
//			Class.forName(prop.getProperty("dbs.driver")).newInstance();

			con = com.pmp.util.ConectionDbs.getConnecton(); 
			//Class.forName("com.ibm.as400.access.AS400JDBCDriver").newInstance();
			//con = DriverManager.getConnection("jdbc:as400://192.168.128.146", "XUPU15PSS", "marcosa"); 
			String SQL = "select w.wono, w.wonosm, descerr from LIBU15FTP.USPWOSM0 w where w.wonosm = "+idAgenda+" and w.bgrp is not null and( w.bgrp = 'PM' or w.bgrp = 'SEV') and w.wono <> ''";
			prstmt_ = con.prepareStatement(SQL);
			rs = prstmt_.executeQuery();
			
			if(rs.next()){
				wono = rs.getString("wono");
				if(wono == null || "".equals(wono.trim())){
					if(!"".equals(rs.getString("descerr"))){
						prstmt_ = con.prepareStatement("delete from LIBU15FTP.USPWOSM0 where wonosm = '"+idAgenda+"'");
						prstmt_.executeUpdate();
						prstmt_ = con.prepareStatement("delete from LIBU15FTP.USPSGSM0 where wonosm = '"+idAgenda+"'");
						prstmt_.executeUpdate();
						return null;
					}
				}
				prstmt_ = con.prepareStatement("delete from LIBU15FTP.USPWOSM0 where wonosm = '"+idAgenda+"'");
				prstmt_.executeUpdate();
				prstmt_ = con.prepareStatement("delete from LIBU15FTP.USPSGSM0 where wonosm = '"+idAgenda+"'");
				prstmt_.executeUpdate();
			}else{
				return "";
			}

		}catch (Exception e) { 
			e.printStackTrace();
		}finally{
			try {
				if(con != null){
					con.close();
				}
				if(prstmt_ != null){
					prstmt_.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return wono.trim();
	}
	
	public Boolean verificaOsFaturada(Integer idContrato) {
//		ResultSet rs = null;
//		PreparedStatement prstmt_ = null;
//		EntityManager manager = null;
//		Connection con = null;
//
//		try {
//			manager = JpaUtil.getInstance();
//			Query query = manager.createNativeQuery("select co.NUM_OS from PMP_CONFIG_OPERACIONAL co, PMP_CONTRATO c"+
//					" where c.ID = co.ID_CONTRATO"+
//					" and c.ID_TIPO_CONTRATO in (select id from PMP_TIPO_CONTRATO where SIGLA in ('VPG','VEPM'))"+
//					//" and co.NUM_OS is not null"+
//					" and c.ID = "+idContrato);
//
//			if(query.getResultList().size() > 0){
//				String numOs = (String) query.getSingleResult();
//				if(numOs == null){
//					return false;
//				}
//				con = com.pmp.util.ConectionDbs.getConnecton(); 
//				String SQL = "select w.ACTI from LIBU17.WOPHDRS0 w where w.WONO = '"+numOs+"' and w.ACTI = 'I'";
//				prstmt_ = con.prepareStatement(SQL);
//				rs = prstmt_.executeQuery();
//				if(rs.next()){
//					return true;
//				}else{
//					return false;
//				}
//
//			}else{
//				return true;
//			}
//		}catch (Exception e) { 
//			e.printStackTrace();
//		}finally{
//			try {
//				if(con != null){
//					con.close();
//				}
//				if(prstmt_ != null){
//					prstmt_.close();
//				}
//				if(manager != null && manager.isOpen()){
//					manager.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return false;
		return true;
	}
	
	
	
	public List<TotalAgendamentoChartBean> findTotalAgendamento(Long filial) {
		
		EntityManager manager = null;
		List<TotalAgendamentoChartBean> result = new ArrayList<TotalAgendamentoChartBean>();
		try {
			manager = JpaUtil.getInstance();
			String SQL = "select ISNULL(count(distinct(ag.id_cong_operacional)),0) from pmp_agendamento ag"+
					" where ag.id_status_agendamento in (select st.id from pmp_status_agendamento st"+
					" where st.sigla in('EA','AT'))";

			if(filial.intValue() > -1){
					SQL += " and ag.filial = filial";
			}
			Query query = manager.createNativeQuery(SQL);
			TotalAgendamentoChartBean bean = new TotalAgendamentoChartBean();
			bean.setDescricao("Total Agendamentos");
			bean.setValor(((Integer)query.getSingleResult()).intValue());
			result.add(bean);
			
			if(filial.intValue() > -1){
				SQL = "select count(*) from pmp_os_operacional os, pmp_cont_horas_standard hs"+
						"	where os.num_os is not null"+
						"	and hs.id_os_operacional = os.id"+
						"	and hs.is_executado = 'N'"+
						"	and os.filial = "+filial+
						"	and hs.id not in("+
						"	select ag.id_cont_horas_standard from pmp_agendamento ag"+
						"	where ag.id_status_agendamento in (select st.id from pmp_status_agendamento st"+
						"	where st.sigla in ('EA','AT'))"+
						"	and ag.filial = "+filial+")";
			}else{
				SQL = "select count(*) from pmp_os_operacional os, pmp_cont_horas_standard hs"+
						"	where os.num_os is not null"+
						"	and hs.id_os_operacional = os.id"+
						"	and hs.is_executado = 'N'"+
						"	and hs.id not in("+
						"	select ag.id_cont_horas_standard from pmp_agendamento ag"+
						"	where ag.id_status_agendamento in (select st.id from pmp_status_agendamento st"+
						"	where st.sigla in ('EA','AT'))"+
						"	)";
			}
			query = manager.createNativeQuery(SQL);
			bean = new TotalAgendamentoChartBean();
			bean.setDescricao("Não Agendedados com OS");
			bean.setValor(((Integer)query.getSingleResult()).intValue());
			result.add(bean);
			
			if(filial.intValue() > -1){
//				SQL = "select count(*) from(select "+
//						" ( select op.num_os from pmp_os_operacional op where  op.id = (select st.id_os_operacional from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as num_os,"+ 
//					
//						" ((select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N')  - (select max(horimetro) from pmp_maquina_pl pl where pl.numero_serie = C.Numero_Serie "+
//						"  and pl.horimetro is not null and pl.data_atualizacao = (select max(data_atualizacao) "+
//						"   from pmp_maquina_pl"+
//						" where numero_serie = pl.numero_serie and horimetro is not null) )) horas_pendentes"+
//					
//						"  from pmp_contrato c, pmp_config_operacional co"+
//						" where c.id_Status_Contrato = (select s.id from Pmp_Status_Contrato s where s.sigla = 'CA')"+ 
//						" and  c.id in((select  distinct id_Contrato  from Pmp_Cont_Horas_Standard hs where id_Contrato = c.id and is_Executado = 'N'))"+ 
//						" and co.num_os is not null "+
//						" and co.id_contrato = c.id"+
//						" and co.filial = "+filial+") tab"+
//						" where tab.horas_pendentes <= 50"+ 
//						" and tab.num_os is null";
				SQL = " select count(c.id)"+
						" from PMP_OS_OPERACIONAL oso, PMP_CONTRATO c, PMP_CONFIG_OPERACIONAL op left join PMP_AGENDAMENTO ag on op.ID = ag.ID_CONG_OPERACIONAL"+ 
						" where op.ID_CONTRATO = c.ID"+
						" and ((select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N')  -"+ 
						" (select max(horimetro)  from pmp_maquina_pl pl "+
						" where pl.numero_serie = C.Numero_Serie   "+
						" and pl.horimetro is not null "+
						" and pl.id = (select max(id) from pmp_maquina_pl"+  
						" where numero_serie = pl.numero_serie and horimetro is not null and HORIMETRO > 0) )) <=50"+
						" and c.IS_ATIVO is null "+
						" and oso.ID_CONT_HORAS_STANDARD not in (select s.ID_CONT_HORAS_STANDARD from PMP_AGENDAMENTO s where s.ID_CONG_OPERACIONAL = op.id)"+
						" and oso.ID_CONFIG_OPERACIONAL = op.id" +
						" and op.filial = "+filial;
			}else{
//				SQL = 	" select count(*) from(select  "+
//						  " ( select op.num_os from pmp_os_operacional op where  op.id = (select st.id_os_operacional from pmp_cont_horas_standard st where st.horas_manutencao = (select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.Id and is_Executado = 'N') and id_Contrato = C.id))  as num_os,"+
//						  " ((select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N')  - (select max(horimetro) from pmp_maquina_pl pl where pl.numero_serie = C.Numero_Serie  "+
//						   " and pl.horimetro is not null and pl.data_atualizacao = (select max(data_atualizacao)  "+
//						   " from pmp_maquina_pl "+
//						  " where numero_serie = pl.numero_serie and horimetro is not null) )) horas_pendentes "+
//						  " from pmp_contrato c, pmp_config_operacional co "+
//						  " where c.id_Status_Contrato = (select s.id from Pmp_Status_Contrato s where s.sigla = 'CA')  "+
//						  " and  c.id in((select  distinct id_Contrato  from Pmp_Cont_Horas_Standard hs where id_Contrato = c.id and is_Executado = 'N'))  "+
//						  " and co.num_os is not null  "+
//						  " and co.id_contrato = c.id) tab "+
//						  " where tab.horas_pendentes <= 50  "+
//						  " and tab.num_os is null ";
				SQL = " select count(c.id)"+
						" from PMP_OS_OPERACIONAL oso, PMP_CONTRATO c, PMP_CONFIG_OPERACIONAL op left join PMP_AGENDAMENTO ag on op.ID = ag.ID_CONG_OPERACIONAL"+ 
						" where op.ID_CONTRATO = c.ID"+
						" and ((select  min(horas_Manutencao) from Pmp_Cont_Horas_Standard  where id_Contrato = C.id and is_Executado = 'N')  -"+ 
						" (select max(horimetro)  from pmp_maquina_pl pl "+
						" where pl.numero_serie = C.Numero_Serie   "+
						" and pl.horimetro is not null "+
						" and pl.id = (select max(id) from pmp_maquina_pl"+  
						" where numero_serie = pl.numero_serie and horimetro is not null and HORIMETRO > 0) )) <=50"+
						" and c.IS_ATIVO is null "+
						" and oso.ID_CONT_HORAS_STANDARD not in (select s.ID_CONT_HORAS_STANDARD from PMP_AGENDAMENTO s where s.ID_CONG_OPERACIONAL = op.id)"+
						" and oso.ID_CONFIG_OPERACIONAL = op.id";
			}
			

			query = manager.createNativeQuery(SQL);
			bean = new TotalAgendamentoChartBean();
			bean.setDescricao("Não Agendamentos sem OS\n com a prox. manut. menor ou igual\n a 50 horas");
			bean.setValor(((Integer)query.getSingleResult()).intValue());
			result.add(bean);
			
		}catch (Exception e) {
			//manager.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	public List<MinutaBean> findAllMinuta(String pesquisa){
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
		ResultSet rs = null;
		PreparedStatement prstmt_ = null;

		Connection con = null;

		List<MinutaBean> listaMinuta = new ArrayList<MinutaBean>();
		try {

			con = ConectionDbs.getConnecton(); 

			String SQL ="select f.NRODOC, f.NOMCONSUM, s.eqmfsn, s.eqmfmd, f.dtasys, m.data, m.NROMINUTA, m.VOLUMES   from PESA200ARQ.minuta01 m, PESA200ARQ.NF020F f, LIBU17.WOPHDRS0 s"+ 
					" where (f.NRONTF = m.nf01 or f.NRONTF = m.nf02 or f.NRONTF = m.nf03 or f.NRONTF = m.nf04 or f.NRONTF = m.nf05 or f.NRONTF = m.nf06 or f.NRONTF = m.nf07 or f.NRONTF = m.nf08"+
					" or f.NRONTF = m.nf09 or f.NRONTF = m.nf10 or f.NRONTF = m.nf11 or f.NRONTF = m.nf12 or f.NRONTF = m.nf13 or f.NRONTF = m.nf14 or f.NRONTF = m.nf15 or f.NRONTF = m.nf16"+
					" or f.NRONTF = m.nf17 or f.NRONTF = m.nf18 or f.NRONTF = m.nf19 or f.NRONTF = m.nf20 or f.NRONTF = m.nf21 or f.NRONTF = m.nf22 or f.NRONTF = m.nf23 or f.NRONTF = m.nf24"+
					" or f.NRONTF = m.nf25 or f.NRONTF = m.nf26 or f.NRONTF = m.nf27 or f.NRONTF = m.nf28 or f.NRONTF = m.nf29 or f.NRONTF = m.nf30 or f.NRONTF = m.nf31 or f.NRONTF = m.nf32)"+
					" and s.WONO = f.NRODOC"+
					" and (s.WONO like '"+pesquisa.toUpperCase()+"%' or f.NOMCONSUM like '"+pesquisa.toUpperCase()+"%' or  s.eqmfsn like '"+pesquisa.toUpperCase()+"%' or s.eqmfmd like '"+pesquisa.toUpperCase()+"%')";

			prstmt_ = con.prepareStatement(SQL);
			rs = prstmt_.executeQuery();
			while(rs.next()){
				MinutaBean bean = new MinutaBean();
				
				bean.setNumOs(rs.getString(1));
				bean.setCliente(rs.getString(2));
				bean.setSerie(rs.getString(3));
				bean.setModelo(rs.getString(4));
				String dataNota = rs.getString(5);
				Date date = format2.parse(dataNota);
				dataNota = format.format(date);
				bean.setDataNota(dataNota);
				String dataTransportadora = rs.getString(6);
				date = format2.parse(dataTransportadora);
				dataTransportadora = format.format(date);
				bean.setDataTransp(dataTransportadora);
				bean.setMinuta(rs.getString(7));
				bean.setVolume(rs.getString(8));
				listaMinuta.add(bean);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(con != null){
					prstmt_.close();
					rs.close();
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listaMinuta;
	}
	
	public NotaFiscalBean findAllNotaFiscal(String numMinuta){
		ResultSet rs = null;
		PreparedStatement prstmt_ = null;

		Connection con = null;

		//List<NotaFiscalBean> listaNotaFiscal = new ArrayList<NotaFiscalBean>();
		try {

			con = ConectionDbs.getConnecton(); 

			String SQL ="select m.NF01, m.NF02, m.NF03, m.NF04, m.NF05, m.NF06, m.NF07, m.NF08, m.NF09, m.NF10, m.NF11, m.NF12, m.NF13, m.NF14, m.NF15, m.NF16, m.NF17, m.NF18, m.NF19, m.NF20"+
					" , m.NF21, m.NF22, m.NF23, m.NF24, m.NF25, m.NF26, m.NF27, m.NF28, m.NF29, m.NF30, m.NF31, m.NF31    from PESA200ARQ.minuta01 m, PESA200ARQ.NF020F f, LIBU17.WOPHDRS0 s"+
					" where (f.NRONTF = m.nf01 or f.NRONTF = m.nf02 or f.NRONTF = m.nf03 or f.NRONTF = m.nf04 or f.NRONTF = m.nf05 or f.NRONTF = m.nf06 or f.NRONTF = m.nf07 or f.NRONTF = m.nf08"+
					" or f.NRONTF = m.nf09 or f.NRONTF = m.nf10 or f.NRONTF = m.nf11 or f.NRONTF = m.nf12 or f.NRONTF = m.nf13 or f.NRONTF = m.nf14 or f.NRONTF = m.nf15 or f.NRONTF = m.nf16"+
					" or f.NRONTF = m.nf17 or f.NRONTF = m.nf18 or f.NRONTF = m.nf19 or f.NRONTF = m.nf20 or f.NRONTF = m.nf21 or f.NRONTF = m.nf22 or f.NRONTF = m.nf23 or f.NRONTF = m.nf24"+
					" or f.NRONTF = m.nf25 or f.NRONTF = m.nf26 or f.NRONTF = m.nf27 or f.NRONTF = m.nf28 or f .NRONTF = m.nf29 or f.NRONTF = m.nf30 or f.NRONTF = m.nf31 or f.NRONTF = m.nf32)"+
					" and s.WONO = f.NRODOC"+
					" and m.NROMINUTA = "+numMinuta;

			prstmt_ = con.prepareStatement(SQL);
			rs = prstmt_.executeQuery();
			if(rs.next()){
				NotaFiscalBean bean = new NotaFiscalBean();
				
				bean.setNF1(rs.getString(1));
				bean.setNF2(rs.getString(2));
				bean.setNF3(rs.getString(3));
				bean.setNF4(rs.getString(4));
				bean.setNF5(rs.getString(5));
				bean.setNF6(rs.getString(6));
				bean.setNF7(rs.getString(7));
				bean.setNF8(rs.getString(8));
				bean.setNF9(rs.getString(9));
				bean.setNF10(rs.getString(10));
				bean.setNF11(rs.getString(11));
				bean.setNF12(rs.getString(12));
				bean.setNF13(rs.getString(13));
				bean.setNF14(rs.getString(14));
				bean.setNF15(rs.getString(15));
				bean.setNF16(rs.getString(16));
				bean.setNF17(rs.getString(17));
				bean.setNF18(rs.getString(18));
				bean.setNF19(rs.getString(19));
				bean.setNF20(rs.getString(20));
				bean.setNF21(rs.getString(21));
				bean.setNF22(rs.getString(22));
				bean.setNF23(rs.getString(23));
				bean.setNF24(rs.getString(24));
				bean.setNF25(rs.getString(25));
				bean.setNF26(rs.getString(26));
				bean.setNF27(rs.getString(27));
				bean.setNF28(rs.getString(28));
				bean.setNF29(rs.getString(29));
				bean.setNF30(rs.getString(30));
				bean.setNF31(rs.getString(31));
				bean.setNF32(rs.getString(32));
				return bean;
				//listaNotaFiscal.add(bean);
			}
	
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(con != null){
					prstmt_.close();
					rs.close();
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public List<CondicaoPagamentoBean> findAllCondicaoPagamento(){
		List<CondicaoPagamentoBean> listForm = new ArrayList<CondicaoPagamentoBean>();
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("from CondicaoPagamento");
			List<CondicaoPagamento> list = (List<CondicaoPagamento>) query.getResultList();
			for (CondicaoPagamento pagamento : list) {
				CondicaoPagamentoBean bean = new CondicaoPagamentoBean();
				bean.setId(pagamento.getId());
				bean.setDescricao(pagamento.getDescricao()+" - "+pagamento.getSigla());
				bean.setSigla(pagamento.getSigla());
				listForm.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}	
		}
		return listForm;
	}
	
	public List<CondicaoPagamentoBean> findAllCondicaoPagamento(String campo){
		List<CondicaoPagamentoBean> listForm = new ArrayList<CondicaoPagamentoBean>();
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();

			Query query = manager.createQuery("from CondicaoPagamento where (descricao  like '"+campo+"%' or sigla like '"+campo+"%')");
			List<CondicaoPagamento> list = (List<CondicaoPagamento>) query.getResultList();
			for (CondicaoPagamento pagamento : list) {
				CondicaoPagamentoBean bean = new CondicaoPagamentoBean();
				bean.setId(pagamento.getId());
				bean.setDescricao(pagamento.getDescricao()+" - "+pagamento.getSigla());
				bean.setSigla(pagamento.getSigla());
				listForm.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}	
		}
		return listForm;
	}
}
