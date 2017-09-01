package com.pmp.business;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.pmp.bean.BusinessGroupBean;
import com.pmp.bean.ClassificacaoBean;
import com.pmp.bean.ClienteBean;
import com.pmp.bean.ConfigManutencaoBean;
import com.pmp.bean.ConfigManutencaoHorasBean;
import com.pmp.bean.ConfigManutencaoMesesBean;
import com.pmp.bean.ConfigurarCustomizacaoBean;
import com.pmp.bean.ContratoComercialBean;
import com.pmp.bean.ModeloBean;
import com.pmp.bean.MotNaoFecContratoBean;
import com.pmp.bean.PrecoBean;
import com.pmp.bean.PrefixoBean;
import com.pmp.bean.RangerBean;
import com.pmp.bean.StatusContratoBean;
import com.pmp.bean.TipoContratoBean;
import com.pmp.bean.UsuarioBean;
import com.pmp.entity.PmpClassificacaoContrato;
import com.pmp.entity.PmpCompCodePartner;
import com.pmp.entity.PmpConfigHorasStandard;
import com.pmp.entity.PmpConfigManutencao;
import com.pmp.entity.PmpConfigOperacional;
import com.pmp.entity.PmpConfigOperacionalMonitoramento;
import com.pmp.entity.PmpConfigTracao;
import com.pmp.entity.PmpConfiguracaoPrecos;
import com.pmp.entity.PmpContHorasStandard;
import com.pmp.entity.PmpContHorasStandardPlus;
import com.pmp.entity.PmpContMesesStandard;
import com.pmp.entity.PmpContrato;
import com.pmp.entity.PmpContratoCustomizacao;
import com.pmp.entity.PmpContratoCustomizacaoMonitoramento;
import com.pmp.entity.PmpContratoMonitoramento;
import com.pmp.entity.PmpHora;
import com.pmp.entity.PmpHoraPK;
import com.pmp.entity.PmpManutencao;
import com.pmp.entity.PmpManutencaoPrecoCusto;
import com.pmp.entity.PmpMotivosNaoFecContrato;
import com.pmp.entity.PmpRange;
import com.pmp.entity.PmpRangePK;
import com.pmp.entity.PmpStatusContrato;
import com.pmp.entity.PmpTipoContrato;
import com.pmp.entity.PmpTipoCustomizacao;
import com.pmp.entity.TwFilial;
import com.pmp.util.ConectionDbs;
import com.pmp.util.ConnectionZoho;
import com.pmp.util.EmailHelper;
import com.pmp.util.IConstantAccess;
import com.pmp.util.JpaUtil;
import com.pmp.util.ValorMonetarioHelper;

public class ContratoBusiness {

	private final String SQL_FIND_ALL_PMP = "SELECT TRIM(HO.BGRP) as BGRP, TRIM(HO.BEQMSN) as BEQMSN, TRIM(UNCS) as UNCS, TRIM(UNLS) as UNLS, TRIM(DLRQTY) as DLRQTY, TRIM(MD5.PANO20)as PANO20, TRIM(BECTYC) as BECTYC, TRIM(CPTCD) as CPTCD, TRIM(PTO.DS18) as DS18, TRIM(PTO.SOS1) SOS1," +
	" TRIM(HO.OCPTMD) as OCPTMD, TRIM(HO.OJBLOC) as OJBLOC, TRIM(HO.OWKAPP) as JWKAPP "+
	" FROM "+IConstantAccess.LIB_DBS+".PCPPIPT0 PTO, "+IConstantAccess.LIB_DBS+".SHLRBMD5 MD5 ,"+IConstantAccess.LIB_DBS+".SHLRBMH0 HO"+
	" WHERE PTO.PANO20 = MD5.PANO20"+
	" AND PTO.SOS1 = MD5.SOS1"+
	" AND MD5.RLTDHS = HO.RLTDHS"+
	" AND TRIM(HO.BGRP) IN ('PM')" +
	" AND MD5.DLRPCT <> '0.00000'";
	//" and substring(TRIM(HO.BEQMSN), 0, 5) = '0CBD'";

	private final String SQL_FIND_ALL_PMP_POR_FILIAL = "SELECT TRIM(HO.BGRP) as BGRP, TRIM(HO.BEQMSN) as BEQMSN, TRIM(PP.LANDCS) as LANDCS, TRIM(DLRQTY) as DLRQTY, TRIM(MD5.PANO20)as PANO20, TRIM(BECTYC) as BECTYC, TRIM(CPTCD) as CPTCD, TRIM(PTO.DS18) as DS18, TRIM(PP.STNO) as  STNO,"+
	" TRIM(HO.OCPTMD) as OCPTMD, TRIM(HO.OJBLOC) as OJBLOC, TRIM(PTO.SOS1) SOS1, TRIM(HO.OWKAPP) as JWKAPP "+
	" FROM "+IConstantAccess.LIB_DBS+".PCPPIPT0 PTO, "+IConstantAccess.LIB_DBS+".SHLRBMD5 MD5 ,"+IConstantAccess.LIB_DBS+".SHLRBMH0 HO,"+IConstantAccess.LIB_DBS+".PCPPIST0 PP"+
	" WHERE PP.PANO20 = PTO.PANO20" +
	" AND PP.SOS1 = PTO.SOS1" +
	" AND PTO.PANO20 = MD5.PANO20" +
	" AND TRIM(PP.STNO) = '0'"+
	" AND PTO.SOS1 = MD5.SOS1"+
	" AND MD5.RLTDHS = HO.RLTDHS"+
	" AND TRIM(HO.BGRP) IN ('PM')" +
	" AND MD5.DLRPCT <> '0.00000'";
	//" and substring(TRIM(HO.BEQMSN), 0, 5) = '0CBD'";
	
	
	
	
	private String SQL_HORAS_PMP = " SELECT TRIM(SH2.JBCD) as JBCD , TRIM(SH2.CPTCD) as CPTCD, TRIM(SH2.EQMFCD) as EQMFCD, TRIM(SH2.BEQMSN) as BEQMSN, TRIM(SH2.FRSDHR) as FRSDHR, TRIM(SH2.EQMFMD) as EQMFMD, TRIM(BGRP) as BGRP,trim(cptcd.CPTCDD) as TIPO_PM  FROM "+IConstantAccess.LIB_DBS+".SHLSTAN2 SH2, "+IConstantAccess.LIB_DBS+".SHLCMPC0 cptcd WHERE TRIM(SH2.BGRP)  IN ('PM') AND cptcd.cptcd = SH2.CPTCD";// and TRIM(SH2.EQMFMD) = '416E'";

	private String SQL_RANGE = "SELECT TRIM(SHL.EQMFCD) as EQMFCD, TRIM(SHL.BEGSN2) as BEGSN2, TRIM(SHL.ENDSN2) as ENDSN2, TRIM(SHL.EQMFM2) as EQMFM2, TRIM(SHL.BSERNO) as BSERNO from "+IConstantAccess.LIB_DBS+".SHLBSSN0 SHL WHERE TRIM(SHL.EQMFM2) <> ''";// and TRIM(SHL.EQMFM2) = '416E'";
	
	private String SQL_FIND_CUSTOMER_MACHINE = "SELECT TRIM(EMPEQPD0.EQMFS2) EQMFS2, TRIM(CIPNAME0.CUNO) CUNO, TRIM(CIPNAME0.CUNM) CUNM, TRIM(CIPNAME0.STN1) STN1"+
											    " FROM "+IConstantAccess.LIB_DBS+".CIPNAME0 CIPNAME0, "+IConstantAccess.LIB_DBS+".EMPEQPD0 EMPEQPD0"+
												" WHERE CIPNAME0.CUNO = EMPEQPD0.CUNO AND ((EMPEQPD0.EQMFCD='AA')) ";
	private String SQL_FIND_FILIAL = "SELECT WOLSTSQ0.STNO STNO, WOLSTSQ0.STNM STNM"+
									 " FROM B108F034.LIBU15.WOLSTSQ0 WOLSTSQ0"+
									 " WHERE (WOLSTSQ0.SQNOPF Like 'M%') ";
	private String ID_FUNCIONARIO;
	private String FILIAL;
	private UsuarioBean usuarioBean;
	
	public ContratoBusiness() {
		
	}
	public ContratoBusiness(UsuarioBean bean) {
		this.usuarioBean = bean;
		ID_FUNCIONARIO = bean.getMatricula();
		FILIAL = bean.getFilial();
	}
	public List<ModeloBean> findAllModelosContrato(String contExcessao, Long idFamilia, String isGerador){
		EntityManager manager = null;
		List<ModeloBean> modeloList = new ArrayList<ModeloBean>();
		try {
			manager = JpaUtil.getInstance();
			//String PMP_CONFIG_MANUTENCAO_SQL = "select distinct modelo from PmpConfigManutencao where contExcessao=:contExcessao and idFamilia.id = :id ";
			String PMP_CONFIG_MANUTENCAO_SQL_NEW = "select distinct MODELO, arv.ID_ARV from PMP_CONFIG_MANUTENCAO cm, ARV_INSPECAO arv where cm.ID_FAMILIA =:id and arv.DESCRICAO = cm.modelo and cont_Excessao=:contExcessao and cm.is_ativo is null";
			if(isGerador != null){
				PMP_CONFIG_MANUTENCAO_SQL_NEW += "  and is_Gerador_Standby = '"+isGerador+"' order by modelo";
			}else{
				PMP_CONFIG_MANUTENCAO_SQL_NEW += " order by modelo";
				
			}
			Query query = manager.createNativeQuery(PMP_CONFIG_MANUTENCAO_SQL_NEW);
			query.setParameter("contExcessao", contExcessao);
			query.setParameter("id", idFamilia);
			List<Object[]> modelos = query.getResultList();
			for (Object[] pair : modelos) {
				ModeloBean bean = new ModeloBean();
				bean.setDescricao((String)pair[0]);
				bean.setIdModelo(((BigDecimal)pair[1]).longValue());
				modeloList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return modeloList;
	}
	
	public List<PrefixoBean> findAllPrefixosContrato(String modelo, String contExcessao){
		EntityManager manager = null;
		List<PrefixoBean> prefixoList = new ArrayList<PrefixoBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("select distinct prefixo from PmpConfigManutencao where modelo = :modelo and contExcessao =:contExcessao and isAtivo is null order by prefixo");
			query.setParameter("modelo", modelo);
			query.setParameter("contExcessao", contExcessao);
			
			List<String> prefixos = query.getResultList();
			for (String string : prefixos) {
				PrefixoBean bean = new PrefixoBean();
				bean.setDescricao(string);
				prefixoList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return prefixoList;
	}
	public List<ConfigManutencaoBean> findAllPrecoEspecial(String modelo, String prefixo, String contExcessao, Long idFamilia){
		EntityManager manager = null;
		List<ConfigManutencaoBean> precoList = new ArrayList<ConfigManutencaoBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createNativeQuery("select distinct p.ID, p.DESCRICAO from PMP_CONFIG_MANUTENCAO m, PMP_CONFIGURACAO_PRECOS p where m.MODELO = '"+modelo+"' and m.PREFIXO = '"+prefixo+"' and m.CONT_EXCESSAO = '"+contExcessao+"' and ID_FAMILIA = "+idFamilia+" and p.ID = m.ID_CONFIGURACAO_PRECO and p.DESCRICAO not in('PROMOÇÃO RETRO') and m.is_ativo is null");
			
			List<Object[]> precos = query.getResultList();
			for (Object [] result : precos) {
				ConfigManutencaoBean bean = new ConfigManutencaoBean();
				bean.setIdPrecoConfig(((BigDecimal)result[0]).longValue());
				bean.setDescTipoPreco((String)result[1]);
				precoList.add(bean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return precoList;
	}
	public List<ConfigManutencaoBean> findAllPrecoEspecialPromocao(String modelo, String prefixo, String contExcessao, Long idFamilia){
		EntityManager manager = null;
		List<ConfigManutencaoBean> precoList = new ArrayList<ConfigManutencaoBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createNativeQuery("select distinct p.ID, p.DESCRICAO from PMP_CONFIG_MANUTENCAO m, PMP_CONFIGURACAO_PRECOS p where m.MODELO = '"+modelo+"' and m.PREFIXO = '"+prefixo+"' and m.CONT_EXCESSAO = '"+contExcessao+"' and ID_FAMILIA = "+idFamilia+" and p.ID = m.ID_CONFIGURACAO_PRECO and p.DESCRICAO in('PROMOÇÃO RETRO') and m.is_ativo is null");
			
			List<Object[]> precos = query.getResultList();
			for (Object [] result : precos) {
				ConfigManutencaoBean bean = new ConfigManutencaoBean();
				bean.setIdPrecoConfig(((BigDecimal)result[0]).longValue());
				bean.setDescTipoPreco((String)result[1]);
				precoList.add(bean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return precoList;
	}
	
	public List<BusinessGroupBean> findAllBusinessGroupContrato(){
		EntityManager manager = null;
		List<BusinessGroupBean> bgList = new ArrayList<BusinessGroupBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("select distinct bgrp from PmpConfigManutencao order by bgrp");
			List<String> bgs = query.getResultList();
			for (String string : bgs) {
				BusinessGroupBean bean = new BusinessGroupBean();
				bean.setDescricao(string);
				bgList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return bgList;
	}
	public List<RangerBean> findAllRangerContrato(String modelo, String prefixo, String contExcessao, Long idConfiguracaoPreco){
		EntityManager manager = null;
		List<RangerBean> rangerList = new ArrayList<RangerBean>();
		try {
			manager = JpaUtil.getInstance();
			String SQL = "select distinct beginrange, endrange from PmpConfigManutencao where modelo=:modelo and contExcessao=:contExcessao and prefixo=:prefixo ";
			if(idConfiguracaoPreco != null){
				SQL+= " and idConfiguracaoPreco.id="+idConfiguracaoPreco;
			} else {
				SQL += "  and isAtivo is null ";
			}
			Query query = manager.createQuery(SQL);
			query.setParameter("modelo", modelo);
			query.setParameter("prefixo", prefixo);
			query.setParameter("contExcessao", contExcessao);
			List<Object[]> rangers = query.getResultList();
			for (Object [] string : rangers) {
				RangerBean bean = new RangerBean();
				bean.setBeginRanger((String)string[0]);
				bean.setEndRanger((String)string[1]);
				bean.setDescricao(bean.getBeginRanger()+" - "+bean.getEndRanger());
				rangerList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return rangerList;
	}
	public List<ConfigurarCustomizacaoBean> findAllTipoCustomizacao(String modelo, Long idFamilia, Long idContrato){
		EntityManager manager = null;
		List<ConfigurarCustomizacaoBean> customizacaoList = new ArrayList<ConfigurarCustomizacaoBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createNativeQuery("select * from PMP_CONTRATO_CUSTOMIZACAO where ID_CONTRATO =:ID_CONTRATO");
			query.setParameter("ID_CONTRATO", idContrato);
			if(query.getResultList().size() == 0){
				return null;
			}
			
			query = manager.createNativeQuery("select ID_ARV from ARV_INSPECAO where ID_FAMILIA =:ID_FAMILIA and DESCRICAO =:MODELO");
			query.setParameter("MODELO", modelo);
			query.setParameter("ID_FAMILIA", idFamilia);
			BigDecimal ID_ARV = (BigDecimal)query.getSingleResult();
			query = manager.createNativeQuery(" select cc.ID_CONTRATO, tc.ID, tc.DESCRICAO, tc.ID_MODELO,cc.id id_contrato_customizacao from PMP_CONTRATO_CUSTOMIZACAO cc right join PMP_TIPO_CUSTOMIZACAO tc on cc.ID_TIPO_CUSTOMIZACAO = tc.ID and cc.ID_CONTRATO =:ID_CONTRATO" +
					" where tc.ID_MODELO =:ID_ARV  " +
					"order by tc.descricao");
			query.setParameter("ID_ARV", ID_ARV);
			query.setParameter("ID_CONTRATO", idContrato);
			
			List<Object[]> rangers = query.getResultList();
			for (Object [] string : rangers) {
				ConfigurarCustomizacaoBean bean = new ConfigurarCustomizacaoBean();
				bean.setIsSelected(false);
				if(string[4] != null){
					bean.setIsSelected(true);
				}
				bean.setIdTipoCustomizacao(((BigDecimal)string[1]).longValue());
				bean.setIdModelo(((BigDecimal)string[3]).longValue());
				bean.setDescricao((String)string[2]);
				bean.setIdFamilia(idFamilia);
				customizacaoList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return customizacaoList;
	}
	
	public List<TipoContratoBean> findAllTipoContrato(){
		EntityManager manager = null;
		List<TipoContratoBean> tipoContratoList = new ArrayList<TipoContratoBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("from PmpTipoContrato where sigla not in ('REN', 'CAN', 'PLUS')");
			List<PmpTipoContrato> tcList = query.getResultList();
			for (PmpTipoContrato tc : tcList) {
				TipoContratoBean bean = new TipoContratoBean();
				bean.setDescricao(tc.getDescricao());
				bean.setId(tc.getId().longValue());
				bean.setSigla(tc.getSigla());
				tipoContratoList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return tipoContratoList;
	}
	
	public List<TipoContratoBean> findAllTipoContratoRental(){
		EntityManager manager = null;
		List<TipoContratoBean> tipoContratoList = new ArrayList<TipoContratoBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("from PmpTipoContrato where sigla = 'REN'");
			List<PmpTipoContrato> tcList = query.getResultList();
			for (PmpTipoContrato tc : tcList) {
				TipoContratoBean bean = new TipoContratoBean();
				bean.setDescricao(tc.getDescricao());
				bean.setId(tc.getId().longValue());
				bean.setSigla(tc.getSigla());
				tipoContratoList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return tipoContratoList;
	}
	
	public List<TipoContratoBean> findAllTipoContratoAntigo(){
		EntityManager manager = null;
		List<TipoContratoBean> tipoContratoList = new ArrayList<TipoContratoBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("from PmpTipoContrato");
			List<PmpTipoContrato> tcList = query.getResultList();
			for (PmpTipoContrato tc : tcList) {
				TipoContratoBean bean = new TipoContratoBean();
				bean.setDescricao(tc.getDescricao());
				bean.setId(tc.getId().longValue());
				bean.setSigla(tc.getSigla());
				tipoContratoList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return tipoContratoList;
	}
	
	public List<StatusContratoBean> findAllStatusContrato(){
		EntityManager manager = null;
		List<StatusContratoBean> statusContratoList = new ArrayList<StatusContratoBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("from PmpStatusContrato");
			List<PmpStatusContrato> scList = query.getResultList();
			for (PmpStatusContrato tc : scList) {
				StatusContratoBean bean = new StatusContratoBean();
				bean.setDescricao(tc.getDescricao());
				bean.setId(tc.getId().longValue());
				bean.setSigla(tc.getSigla());
				statusContratoList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return statusContratoList;
	}
	
	public ClienteBean findDataCliente(String cuno){
		ResultSet rs = null;
		PreparedStatement prstmt_ = null;

		Connection con = null;

//		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
//		Properties prop = new Properties();
		ClienteBean bean = new ClienteBean();
		try {
//			prop.load(in);
//			String url = prop.getProperty("dbs.url");
//			String user = prop.getProperty("dbs.user");
//			String password =prop.getProperty("dbs.password");
//			Class.forName(prop.getProperty("dbs.driver")).newInstance();

			con = ConectionDbs.getConnecton(); 

			String SQL = "select c.FLGDLI,c.CUNM CLCHAVE, " +
			"c.CUNO,c.CUADD2 END2, c.CUADD3 BAIRRO,c.CUCYST CID,c.CUSTE EST, trim(SUBstring(c.TELXNO,0,15)) CPF, c.ZIPCD9 CEP " +
			"FROM "+com.pmp.util.IConstantAccess.LIB_DBS+".CIPNAME0 c "+
			" where c.CUNO = '"+cuno+"'";
			prstmt_ = con.prepareStatement(SQL);
			rs = prstmt_.executeQuery();
			if(rs.next()){
				if( rs.getString("FLGDLI").equals("Y")){
					bean.setMsg("O código do cliente não pode ser usuado!");
					return bean;
				}
				bean.setRAZSOC(rs.getString("CLCHAVE").trim());
				bean.setCLCHAVE(rs.getString("CUNO").trim());
				bean.setEND(rs.getString("END2").trim());
				bean.setBAIRRO(rs.getString("BAIRRO").trim());
				bean.setCID(rs.getString("CID").trim());
				bean.setEST(rs.getString("EST").trim());				
				//bean.setINSCEST(rs.getString("INSCEST").trim());
				//bean.setINSCMUN(rs.getString("INSCMUN").trim());
				//bean.setINDCONT(rs.getString("INDCONT").trim());
				bean.setEST(rs.getString("EST").trim());
				bean.setCEP(rs.getString("CEP").trim());
				bean.setCNPJ(rs.getString("CPF").trim());
				bean.setCNPJ(rs.getString("CPF").trim());
//				if(rs.getString("CONFIS").equals("J")){
//					String primeiraCasa = rs.getString("CGCNUM");
//					if(9 - primeiraCasa.length() > 0){
//						int pc = (9 - primeiraCasa.length());
//						for(int i = 0; i < pc ; i++){
//							primeiraCasa = "0"+primeiraCasa;
//						}
//					}			    	
//					String segundaCasa = rs.getString("CGCFIL");
//					if(4 - segundaCasa.length() > 0){
//						int sc = (4 - segundaCasa.length());
//						for(int i = 0; i < sc ; i++){
//							segundaCasa = "0"+segundaCasa;
//						}
//					}
//
//					String terceiraCasa = rs.getString("CGCDIG");
//					if(2 - terceiraCasa.length() > 0){
//						int tc = (2 - terceiraCasa.length());
//						for(int i = 0; i < tc ; i++){
//							terceiraCasa = "0"+terceiraCasa;
//						}
//					}
//					bean.setCNPJ(primeiraCasa+"/"+segundaCasa+"-"+terceiraCasa);
//				}else{
//					bean.setCNPJ(rs.getString("CPF").trim());
//				}
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
		return bean;
	}
	
	public String findFabricante (String prefixo){
		ResultSet rs = null;
		PreparedStatement prstmt_ = null;
		Connection con = null;
		String result = "";
		try{
			con = ConectionDbs.getConnecton(); 
			
			String SQL = "select distinct s.ds4 from LIBU17.SCPCODE0 s, LIBU17.EMPEQPD0 e"+ 
					" where s.keyda1 = e.EQMFCD"+ 
					" and s.rcdcd = 'F'"+
					" and substring(e.EQMFS2,0,5 ) = '"+prefixo+"'";
			prstmt_ = con.prepareStatement(SQL);
			rs = prstmt_.executeQuery();
			
			if(rs.next()){
				result = rs.getString("DS4").trim();
			}
			
			

		}catch(Exception e){
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
		return result;
		
	}
	
	public List<ClienteBean> findDataNomeCliente(String nomeCliente){
		ResultSet rs = null;
		PreparedStatement prstmt_ = null;

		Connection con = null;

//		InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
//		Properties prop = new Properties();

		List<ClienteBean> clienteList = new ArrayList<ClienteBean>();
		try {
//			prop.load(in);
//			String url = prop.getProperty("dbs.url");
//			String user = prop.getProperty("dbs.user");
//			String password =prop.getProperty("dbs.password");
//			Class.forName(prop.getProperty("dbs.driver")).newInstance();

			con = ConectionDbs.getConnecton();  

			String SQL = "select c.FLGDLI,c.CUNM CLCHAVE, " +
			"c.CUNO,c.CUADD2 END2, c.CUADD3 BAIRRO,c.CUCYST CID,c.CUSTE EST, trim(SUBstring(c.TELXNO,0,15)) CPF, c.ZIPCD9 CEP " +
			"FROM "+com.pmp.util.IConstantAccess.LIB_DBS+".CIPNAME0 c "+
			" where c.CUNM like '"+nomeCliente.toUpperCase()+"%'";
			//" and c.FLGDLI <> 'Y'";
			prstmt_ = con.prepareStatement(SQL);
			rs = prstmt_.executeQuery();
			while(rs.next()){
				ClienteBean bean = new ClienteBean();
				bean.setRAZSOC(rs.getString("CLCHAVE").trim());
				bean.setCLCHAVE(rs.getString("CUNO").trim());
				bean.setEND(rs.getString("END2").trim());
				bean.setBAIRRO(rs.getString("BAIRRO").trim());
				bean.setCID(rs.getString("CID").trim());
				bean.setEST(rs.getString("EST").trim());				
				//bean.setINSCEST(rs.getString("INSCEST").trim());
				//bean.setINSCMUN(rs.getString("INSCMUN").trim());
				//bean.setINDCONT(rs.getString("INDCONT").trim());
				bean.setEST(rs.getString("EST").trim());
				bean.setCEP(rs.getString("CEP").trim());
				bean.setCNPJ(rs.getString("CPF").trim());
				bean.setCNPJ(rs.getString("CPF").trim());
				bean.setFLAGDELETE(rs.getString("FLGDLI").trim());
//				if(rs.getString("CONFIS").equals("J")){
//					String primeiraCasa = rs.getString("CGCNUM");
//					if(9 - primeiraCasa.length() > 0){
//						int pc = (9 - primeiraCasa.length());
//						for(int i = 0; i < pc ; i++){
//							primeiraCasa = "0"+primeiraCasa;
//						}
//					}			    	
//					String segundaCasa = rs.getString("CGCFIL");
//					if(4 - segundaCasa.length() > 0){
//						int sc = (4 - segundaCasa.length());
//						for(int i = 0; i < sc ; i++){
//							segundaCasa = "0"+segundaCasa;
//						}
//					}
//
//					String terceiraCasa = rs.getString("CGCDIG");
//					if(2 - terceiraCasa.length() > 0){
//						int tc = (2 - terceiraCasa.length());
//						for(int i = 0; i < tc ; i++){
//							terceiraCasa = "0"+terceiraCasa;
//						}
//					}
//					bean.setCNPJ(primeiraCasa+"/"+segundaCasa+"-"+terceiraCasa);
//				}else{
//					bean.setCNPJ(rs.getString("CPF").trim());
//				}
				clienteList.add(bean);
			}


	
		} catch (Exception e) {
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
		return clienteList;
	}
	
	public List<ConfigManutencaoHorasBean> findAllManutencaoHoras(String modelo, String prefixo, String beginrange, String endrange, String bgrp, String contExcessao, Long idConfigPreco){
		EntityManager manager = null;
		List<ConfigManutencaoHorasBean> result = new ArrayList<ConfigManutencaoHorasBean>();
		try {
			manager = JpaUtil.getInstance();
			String SQL = "select h from PmpConfigManutencao m, PmpConfigHorasStandard h"+
				" where m.modelo = '"+modelo+"'"+
				" and m.prefixo = '"+prefixo+"'"+
				" and m.beginrange = '"+beginrange+"'"+
				" and m.endrange = '"+endrange+"'"+
				" and m.bgrp = '"+bgrp+"'"+
				" and m.id = h.idConfigManutencao.id " +
				" and m.isGeradorStandby = 'N' " +
				" and m.contExcessao = '"+contExcessao+"'" +
				" and m.isAtivo is null ";
			if(idConfigPreco != null){
				SQL+= " and m.idConfiguracaoPreco.id="+idConfigPreco;
			}
				
			SQL+=" order by h.horas";
			Query query = manager.createQuery(SQL);
			List<PmpConfigHorasStandard> list = query.getResultList();
			Long frequencia = 0l;
			Long frequenciaUltima = 0l;
			int cont = 0;
			while(cont < 60000){
				for (int i = 0; i < list.size(); i++) {
					PmpConfigHorasStandard horas = list.get(i);
					ConfigManutencaoHorasBean bean = new ConfigManutencaoHorasBean();
					
					if(i == 0 || i == 1 || i == 2){
						if((horas.getHoras()+cont) == 12200	|| (horas.getHoras()+cont) == 12400 || (horas.getHoras()+cont) == 12600
									|| (horas.getHoras()+cont) == 24200 || (horas.getHoras()+cont) == 24400 || (horas.getHoras()+cont) == 24600
									|| (horas.getHoras()+cont) == 36200 || (horas.getHoras()+cont) == 36400 || (horas.getHoras()+cont) == 36600
									|| (horas.getHoras()+cont) == 48200 || (horas.getHoras()+cont) == 48400 || (horas.getHoras()+cont) == 48600){
							continue;
						}
					}
					
					if(i == 0 || i == 1 || (i == 2 && horas.getHoras() == 500)){
						PmpConfigHorasStandard horasProximo = list.get(i+1);
						frequencia = horasProximo.getHoras() - horas.getHoras();
						
						PmpConfigHorasStandard horasFinal = list.get(list.size()-1);
						PmpConfigHorasStandard horasPenultima = list.get(list.size()-2);
						frequenciaUltima = horasFinal.getHoras() - horasPenultima.getHoras();
						
						if((cont == 12000 || cont == 24000 || cont == 36000 || cont == 48000) && frequencia.longValue() != frequenciaUltima.longValue()){
							continue;
						}
						if(cont == 12000 || cont == 24000 || cont == 36000 || cont == 48000){
							if(horas.getHoras() == 500 || horas.getHoras() == 250){
								continue;
							}
						}
						
					}else{
						PmpConfigHorasStandard horasFinal = list.get(list.size()-1);
						PmpConfigHorasStandard horasPenultima = list.get(list.size()-2);
						frequencia = horasFinal.getHoras() - horasPenultima.getHoras();
					}
					bean.setId(horas.getId());
					bean.setHoras(horas.getHoras());
					bean.setHorasManutencao(horas.getHoras()+cont);
					bean.setStandardJob(horas.getStandardJobCptcd());
					bean.setIdConfigManutencao(horas.getIdConfigManutencao().getId());
					bean.setIsExecutado("N");
					bean.setFrequencia(frequencia);
					bean.setHorasRevisao(horas.getHorasRevisao());
					result.add(bean);
				}
				cont = cont + 12000;
			}
			return result;
		} catch (Exception e) {			
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	public List<ConfigManutencaoMesesBean> findAllManutencaoMeses(String modelo, String prefixo, String beginrange, String endrange, String bgrp){
		EntityManager manager = null;
		List<ConfigManutencaoMesesBean> result = new ArrayList<ConfigManutencaoMesesBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("select h from PmpConfigManutencao m, PmpConfigHorasStandard h"+
					" where m.modelo = '"+modelo+"'"+
					" and m.prefixo = '"+prefixo+"'"+
					" and m.beginrange = '"+beginrange+"'"+
					" and m.endrange = '"+endrange+"'"+
					" and m.bgrp = '"+bgrp+"'"+
					" and m.id = h.idConfigManutencao.id " +
					" and m.isGeradorStandby = 'S' " +
					" and m.isAtivo is null " +
					" order by h.horas");
			List<PmpConfigHorasStandard> list = query.getResultList();
			
			Long frequencia = 30L;	//Em dias
			for (int i = 0; i < list.size(); i++) {
				PmpConfigHorasStandard horas = list.get(i);
				ConfigManutencaoMesesBean bean = new ConfigManutencaoMesesBean();
				
				bean.setId(horas.getId());
				bean.setMeses(horas.getHoras());
				bean.setMesManutencao(horas.getHoras());
				bean.setStandardJob(horas.getStandardJobCptcd());
				bean.setIdConfigManutencao(horas.getIdConfigManutencao().getId());
				bean.setIsExecutado("N");
				bean.setFrequencia(frequencia);
				result.add(bean);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	public ContratoComercialBean validarRenovacaoContrato(ContratoComercialBean contratoComercialBean){
		EntityManager manager = null;
		//SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		//SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");

		try {
			manager = JpaUtil.getInstance();
			String msg = validarRenovacaoContrato(contratoComercialBean, manager);
			if(msg != null){
				contratoComercialBean.setMsg(msg);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();		
			}
		}
		return contratoComercialBean;
	}
	
	public ContratoComercialBean saveOrUpdate(ContratoComercialBean contratoComercialBean, String isSaveHorasPosPago){
		EntityManager manager = null;
		Connection con = null;
		Statement prstmt = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			con = ConectionDbs.getConnecton();
			
			Query query = manager.createQuery("from PmpClassificacaoContrato where sigla =:sigla");
			query.setParameter("sigla", contratoComercialBean.getSiglaClassificacaoContrato());
			PmpClassificacaoContrato classificacaoContrato = (PmpClassificacaoContrato)query.getSingleResult(); 
			PmpTipoContrato pmpTipoContrato = manager.find(PmpTipoContrato.class, contratoComercialBean.getIdTipoContrato());
			PmpStatusContrato statusContrato = manager.find(PmpStatusContrato.class, contratoComercialBean.getStatusContrato());
			TwFilial filial = manager.find(TwFilial.class, Long.valueOf(FILIAL));
			PmpConfigTracao pmpConfigTracao = null;
			if(contratoComercialBean.getIdConfigTracao() != null && contratoComercialBean.getIdConfigTracao() != 0){
				pmpConfigTracao = manager.find(PmpConfigTracao.class, contratoComercialBean.getIdConfigTracao());
			}
			
			
			String SQL = "From PmpConfigManutencao where modelo =:modelo and prefixo =:prefixo and bgrp =:bgrp and beginrange =:beginrange and endrange =:endrange and contExcessao=:contExcessao ";
			if(contratoComercialBean.getIdConfigPreco() != null && contratoComercialBean.getIdConfigPreco() > 0){
				SQL += " and idConfiguracaoPreco.id = "+contratoComercialBean.getIdConfigPreco();
			}else{
				SQL += " and isAtivo is null ";
			}
			query = manager.createQuery(SQL);
			query.setParameter("modelo", contratoComercialBean.getModelo());
			query.setParameter("prefixo", contratoComercialBean.getPrefixo());
			query.setParameter("bgrp", contratoComercialBean.getBusinessGroup());
			query.setParameter("beginrange", contratoComercialBean.getBeginRanger());
			query.setParameter("endrange", contratoComercialBean.getEndRanger());
			query.setParameter("contExcessao", contratoComercialBean.getContExcessao());
			PmpConfigManutencao manutencao = (PmpConfigManutencao)query.getSingleResult();
			//contrato.setIdConfigManutencao(manutencao);
			
			PmpContrato contrato = null;
			PmpConfigOperacional operacional = null;
			
			if(contratoComercialBean.getId() == null || contratoComercialBean.getId() == 0){
				contrato = new PmpContrato();
				contrato.setIdTipoContrato(pmpTipoContrato);
				contrato.setIdStatusContrato(statusContrato);
				contrato.setIdFuncionario(ID_FUNCIONARIO);
				contrato.setFilial(Integer.valueOf(FILIAL));
				contrato.setIdConfigTracao(pmpConfigTracao);
				if(filial.getRegional() != null){
					contrato.setRegional(filial.getRegional().toString());
				}
				contratoComercialBean.toBean(contrato);
				contrato.setNumeroSerie(contratoComercialBean.getPrefixo()+contratoComercialBean.getNumeroSerie());
				
				if (contratoComercialBean.getDistanciaGerador() != null) {
					contrato.setDistanciaGerador(BigDecimal.valueOf(Double.valueOf(contratoComercialBean.getDistanciaGerador())));
				}
				
				if (statusContrato.getSigla().equals("CA")) {
					contrato.setDataAceite(new Date());
					contrato.setHasSendEmail("S");
				} else if (statusContrato.getSigla().equals("CNA")) {
					contrato.setDataRejeicao(new Date());
					contrato.setIdMotivoNaoFecContrato(manager.find(PmpMotivosNaoFecContrato.class, contratoComercialBean.getIdMotNaoFecContrato()));
				}else{
					contrato.setDataRejeicao(null);
					contrato.setIdMotivoNaoFecContrato(null);
				}
				
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy/dd/MM");
				String data = fmt.format(new Date());
				String tipoContrato = "G";
				if(pmpTipoContrato.getSigla().equals("REN")){
					tipoContrato = "R";
				}
				if(contratoComercialBean.getContExcessao() != null && contratoComercialBean.getContExcessao().equals("S")){
					tipoContrato = "E";
				}
				if(manutencao.getIdConfiguracaoPreco().getDescricao().equals("PROMOÇÃO RETRO")){
					tipoContrato = "P";
				}
				if(contratoComercialBean.getIsSpot() != null && contratoComercialBean.getIsSpot().equals("S")){
					tipoContrato = "S";
				}
//				String SQL = "From PmpConfigManutencao where modelo =:modelo and prefixo =:prefixo and bgrp =:bgrp and beginrange =:beginrange and endrange =:endrange and contExcessao=:contExcessao";
//				if(contratoComercialBean.getIdConfigPreco() != null && contratoComercialBean.getIdConfigPreco() > 0){
//					SQL += " and idConfiguracaoPreco.id = "+contratoComercialBean.getIdConfigPreco();
//				}
//				Query query = manager.createQuery(SQL);
//				query.setParameter("modelo", contratoComercialBean.getModelo());
//				query.setParameter("prefixo", contratoComercialBean.getPrefixo());
//				query.setParameter("bgrp", contratoComercialBean.getBusinessGroup());
//				query.setParameter("beginrange", contratoComercialBean.getBeginRanger());
//				query.setParameter("endrange", contratoComercialBean.getEndRanger());
//				query.setParameter("contExcessao", contratoComercialBean.getContExcessao());
//				PmpConfigManutencao manutencao = (PmpConfigManutencao)query.getSingleResult();
				contrato.setIdConfigManutencao(manutencao);
				manager.persist(contrato);
				contrato.setNumeroContrato(data.substring(2,4)+(Integer.valueOf(FILIAL) < 10?"0"+FILIAL:FILIAL)+tipoContrato+contrato.getId());
				if(statusContrato.getSigla().equals("CA")){
					operacional = new PmpConfigOperacional();
					operacional.setContato(contrato.getContatoComercial());
					operacional.setLocal(contrato.getBairro()+", "+contrato.getCidade()+", "+contrato.getEndereco());
					operacional.setIdFuncSupervisor(ID_FUNCIONARIO);
					operacional.setIdContrato(contrato);
					operacional.setTelefoneContato(contrato.getTelefoneComercial());
					operacional.setFilial(Long.valueOf(FILIAL));
					operacional.setObs(pmpTipoContrato.getDescricao()+" - "+classificacaoContrato.getDescricao());
					if(pmpTipoContrato.getSigla().equals("CON") || pmpTipoContrato.getSigla().equals("VEN") || pmpTipoContrato.getSigla().equals("REN") || pmpTipoContrato.getSigla().equals("VEPM")){//concessão ou contrato pós-pago (VEN)
						operacional.setCodErroDbs("00");
						operacional.setCompCode("8898");
						operacional.setJobCode("041");
						operacional.setCscc("SV");
						operacional.setInd("E");
						operacional.setNumeroSegmento("01");
					}
					manager.persist(operacional);
				}
				
			}else{
				contrato = manager.find(PmpContrato.class, contratoComercialBean.getId());
				contrato.setIdConfigManutencao(manutencao);
				contrato.setIdTipoContrato(pmpTipoContrato);
				contrato.setIdStatusContrato(statusContrato);
				contrato.setIdConfigTracao(pmpConfigTracao);
				if(filial.getRegional() != null){
					contrato.setRegional(filial.getRegional().toString());
				}
				//contrato.setIdFuncionario(ID_FUNCIONARIO);
				//contrato.setFilial(Integer.valueOf(FILIAL));
				contratoComercialBean.toBean(contrato);
				contrato.setNumeroSerie(contratoComercialBean.getPrefixo() + contratoComercialBean.getNumeroSerie());
				
				if (contratoComercialBean.getDistanciaGerador() != null) {
					contrato.setDistanciaGerador(BigDecimal.valueOf(Double.valueOf(contratoComercialBean.getDistanciaGerador())));
				}
				
				if(statusContrato.getSigla().equals("CA")){
					contrato.setDataAceite(new Date());
					contrato.setHasSendEmail("S");
					query = manager.createQuery("from PmpConfigOperacional where idContrato.id = :id_contrato");
					query.setParameter("id_contrato", contrato.getId());
					List<PmpConfigOperacional> list = query.getResultList();
					if(list.size() > 0){
						operacional = list.get(0);
					}else{
						operacional = new PmpConfigOperacional();
					}
					operacional.setContato(contrato.getContatoComercial());
					operacional.setLocal(contrato.getBairro()+", "+contrato.getCidade()+", "+contrato.getEndereco());
					operacional.setIdFuncSupervisor(ID_FUNCIONARIO);
					operacional.setIdContrato(contrato);
					operacional.setTelefoneContato(contrato.getTelefoneComercial());
					operacional.setFilial(Long.valueOf(FILIAL));
					operacional.setObs(pmpTipoContrato.getDescricao()+" - "+classificacaoContrato.getDescricao());
					manager.merge(operacional);
					
					if(pmpTipoContrato.getSigla().equals("CON") || pmpTipoContrato.getSigla().equals("VEN") || pmpTipoContrato.getSigla().equals("REN") || pmpTipoContrato.getSigla().equals("VEPM")){//concessão ou contrato pós-pago (VEN)
						query = manager.createQuery("from PmpConfigOperacional where idContrato.id =:idContrato");
						query.setParameter("idContrato", contrato.getId());
						PmpConfigOperacional configOperacional = new PmpConfigOperacional();
						if(query.getResultList().size() > 0){
							configOperacional = (PmpConfigOperacional)query.getSingleResult();
						}
						configOperacional.setIdContrato(contrato);
						configOperacional.setNumOs(contrato.getNumeroContrato());
						configOperacional.setIdFuncSupervisor(contrato.getIdFuncionario());
						configOperacional.setContato(contrato.getContatoServicos());
						configOperacional.setLocal(contrato.getEndereco());
						configOperacional.setTelefoneContato(contrato.getTelefoneServicos());
						configOperacional.setFilial(contrato.getFilial().longValue());
						configOperacional.setCodErroDbs("00");
						configOperacional.setCompCode("8898");
						configOperacional.setJobCode("041");
						configOperacional.setCscc("SV");
						configOperacional.setInd("E");
						configOperacional.setNumeroSegmento("01");
						configOperacional.setObs(pmpTipoContrato.getDescricao()+" - "+classificacaoContrato.getDescricao());
						manager.merge(configOperacional);
					}
					
					
				}else if(statusContrato.getSigla().equals("CNA")){
					contrato.setDataRejeicao(new Date());
					contrato.setIdMotivoNaoFecContrato(manager.find(PmpMotivosNaoFecContrato.class, contratoComercialBean.getIdMotNaoFecContrato()));
				}else{
					contrato.setDataRejeicao(null);
					contrato.setIdMotivoNaoFecContrato(null);
				}

				manager.merge(contrato);
		}
			contrato.setIdClassificacaoContrato(classificacaoContrato);
			contrato.setIsTerritorio(contratoComercialBean.getIsTerritorio());
			//Remove o tipo de customização
			String siglaCustomizacao = "";
			query = manager.createNativeQuery("delete from PMP_CONTRATO_CUSTOMIZACAO where id_contrato =:id_contrato");
			query.setParameter("id_contrato", contrato.getId());
			query.executeUpdate();
			if(contratoComercialBean.getTipoCustomizacaoList() != null){
				for (ConfigurarCustomizacaoBean bean : contratoComercialBean.getTipoCustomizacaoList()) {
					if(bean.getIsSelected()){
						PmpContratoCustomizacao contratoCustomizacao = new PmpContratoCustomizacao();
						contratoCustomizacao.setIdContrato(contrato);
						contratoCustomizacao.setIdTipoCustomizacao(manager.find(PmpTipoCustomizacao.class, bean.getIdTipoCustomizacao()));
						manager.persist(contratoCustomizacao);
						query = manager.createNativeQuery("select sc.sigla_customizacao from PMP_SIGLA_CUSTOMIZACAO sc, PMP_CONFIG_CUSTOMIZACAO cc"+
								" where cc.ID_TIPO_CUSTOMIZACAO =:ID_TIPO_CUSTOMIZACAO"+
								" and sc.id_config_customizacao = cc.id");
						query.setParameter("ID_TIPO_CUSTOMIZACAO", bean.getIdTipoCustomizacao());
						List<String> sgTipoCustList = query.getResultList();
						for (String string : sgTipoCustList) {
							siglaCustomizacao += "'"+string+"',";
						}
					}
				}
			}
			if(siglaCustomizacao.length() > 0){
				siglaCustomizacao = siglaCustomizacao.substring(0, siglaCustomizacao.length() - 1);
			}else{
				siglaCustomizacao = "'-null'";
			}
			if(contratoComercialBean.getFuncionarioIndicado() != null && contratoComercialBean.getMatriculaIndicado() != null){
				contrato.setIdFuncionarioIndicacao(contratoComercialBean.getMatriculaIndicado());
				contrato.setNomeIndicacao(contratoComercialBean.getFuncionarioIndicado());
				contrato.setComissaoindicacao(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getComissaoIndicacao());
			}else{
				contrato.setIdFuncionarioIndicacao(null);
				contrato.setNomeIndicacao(null);
				contrato.setComissaoConsultor(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getComissaoConsultor());
				//contrato.setComissaoConsultor(null);
				contrato.setComissaoindicacao(null);
			}
			//contrato.setIsFindSubTributaria(contratoComercialBean.getIsFindSubstuicaoTributaria());
			//contrato.setCodErroDbs(contratoComercialBean.getCodErroDbs());
			//contrato.setMsgErroDbs(contratoComercialBean.getMsgErroDbs());
			//contrato.setNumDocDbs(contratoComercialBean.getNumDocDbs());
			if(!contratoComercialBean.getIsGeradorStandby()) {
				this.insertManutHoras(contratoComercialBean.getConfigManutencaoHorasBeanList(), contrato, manager, isSaveHorasPosPago, siglaCustomizacao);
				contratoComercialBean.setPrintRevisaoPosPago(contrato.getPrintRevisaoPosPago());
			} else {
				this.insertManutMeses(contratoComercialBean.getConfigManutencaoMesesBeanList(), contrato, manager);
			}
			
			if(contratoComercialBean.getIsFindSubstuicaoTributaria() != null && "P".equals(contratoComercialBean.getIsFindSubstuicaoTributaria())){
				contrato.setIsFindSubTributaria("P");
				//envia as peças para gerar nova cotação
				prstmt = con.createStatement();
				String inCptcd = "";
				for (ConfigManutencaoHorasBean configManutHorasBean : contratoComercialBean.getConfigManutencaoHorasBeanList()) {
					inCptcd += "'"+configManutHorasBean.getStandardJob()+"',";
				}
				if(inCptcd.length() > 1){
					inCptcd = inCptcd.substring(0,inCptcd.length()-1);
				}
				new OsBusiness(this.usuarioBean).sendTotalPecasPecasDbs(contrato, manager, prstmt, inCptcd, siglaCustomizacao);
				contrato.setIsFindSubTributaria("P");
				contrato.setCodErroDbs("100");
				contrato.setNumDocDbs(null);
				contrato.setValoContrato(null);
				contratoComercialBean.setCodErroDbs("100");
				contrato.setMsgErroDbs("Aguarde o resultado da Cotação!");
				contratoComercialBean.setMsgErroDbs("Aguarde o resultado da Cotação!");

			}else if("R".equals(contratoComercialBean.getIsFindSubstuicaoTributaria())){//retira  a substituição tributária
				contrato.setIsFindSubTributaria(null);
				contrato.setCodErroDbs(null);
				contratoComercialBean.setCodErroDbs(null);
				contrato.setMsgErroDbs(null);
				contrato.setNumDocDbs(null);

			}
			
			
//			PmpMaquinaPl pl = new PmpMaquinaPl();
//			pl.setDataAtualizacao(new Date());
//			pl.setHorimetro(contratoComercialBean.getHorimetro());
//			pl.setNumeroSerie(contratoComercialBean.getPrefixo()+contratoComercialBean.getNumeroSerie());
//			manager.persist(pl);
			
			manager.getTransaction().commit();
			contratoComercialBean.setNumeroContrato(contrato.getNumeroContrato());
			contratoComercialBean.setId(contrato.getId().longValue());
			if(contrato.getIdTipoContrato().getSigla().equals("VEN")){
				List<PrecoBean> result = new ArrayList<PrecoBean>();
				if(contrato.getBgrp().equals("PM") || contrato.getBgrp().equals("SEV")){
					if(contrato.getIdClassificacaoContrato().getSigla().equals("PRE")){
						result = this.findAllParcelasPMPKIT3Custo(contrato.getId(), siglaCustomizacao);
					}else if(contrato.getIdClassificacaoContrato().getSigla().equals("CUS")){
						result = this.findAllParcelasCustoCustomer(contrato.getId(), siglaCustomizacao);
					}else if(contrato.getIdClassificacaoContrato().getSigla().equals("PART")){
						result = this.findAllParcelasCustoPartner(contrato.getId(), siglaCustomizacao);
					}else if(contrato.getIdClassificacaoContrato().getSigla().equals("CUSLIGHT")){
						result = this.findAllParcelasCustoCustomerLight(contrato.getId(), siglaCustomizacao);
					}
				}else if(contrato.getBgrp().equals("KIT2")){
					result = this.findAllParcelasKIT2Custo(contrato.getId(), siglaCustomizacao);
				}
				PrecoBean bean = result.get(0);
				manager.getTransaction().begin();
				contrato = manager.find(PmpContrato.class, contrato.getId());
				contrato.setValorCusto((BigDecimal.valueOf(Double.valueOf(bean.getPreco().replace(".", "").replace(",", ".").replace("R$", "")))));
				manager.merge(contrato);
				manager.getTransaction().commit();
			}
			if(statusContrato.getSigla().equals("CA")){
				AgendamentoBusiness business = new AgendamentoBusiness();
				business.saveAgendamentosPendentes(contratoComercialBean.getId());
			}
			return contratoComercialBean;
		} catch (Exception e) {
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
			if(prstmt != null){
				prstmt.close();
			}
			} catch (Exception e2) {
				e2.printStackTrace();
			}			
		}
		return null;
	}
	public ContratoComercialBean saveOrUpdatePlus(ContratoComercialBean contratoComercialBean, String isSaveHorasPosPago){
		EntityManager manager = null;
		Connection con = null;
		Statement prstmt = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			con = ConectionDbs.getConnecton();
			
			Query query = manager.createQuery("from PmpClassificacaoContrato where sigla =:sigla");
			query.setParameter("sigla", contratoComercialBean.getSiglaClassificacaoContrato());
			PmpClassificacaoContrato classificacaoContrato = (PmpClassificacaoContrato)query.getSingleResult();
			
			
			
			PmpTipoContrato pmpTipoContrato = manager.find(PmpTipoContrato.class, contratoComercialBean.getIdTipoContrato());
			PmpStatusContrato statusContrato = manager.find(PmpStatusContrato.class, contratoComercialBean.getStatusContrato());
			TwFilial filial = manager.find(TwFilial.class, Long.valueOf(FILIAL));
			PmpConfigTracao pmpConfigTracao = null;
			if(contratoComercialBean.getIdConfigTracao() != null && contratoComercialBean.getIdConfigTracao() != 0){
				pmpConfigTracao = manager.find(PmpConfigTracao.class, contratoComercialBean.getIdConfigTracao());
			}
			
			
			String SQL = "From PmpConfigManutencao where modelo =:modelo and prefixo =:prefixo and bgrp =:bgrp and beginrange =:beginrange and endrange =:endrange and contExcessao=:contExcessao and isAtivo is null";
			if(contratoComercialBean.getIdConfigPreco() != null && contratoComercialBean.getIdConfigPreco() > 0){
				SQL += " and idConfiguracaoPreco.id = "+contratoComercialBean.getIdConfigPreco();
			}
			query = manager.createQuery(SQL);
			query.setParameter("modelo", contratoComercialBean.getModelo());
			query.setParameter("prefixo", contratoComercialBean.getPrefixo());
			query.setParameter("bgrp", contratoComercialBean.getBusinessGroup());
			query.setParameter("beginrange", contratoComercialBean.getBeginRanger());
			query.setParameter("endrange", contratoComercialBean.getEndRanger());
			query.setParameter("contExcessao", contratoComercialBean.getContExcessao());
			PmpConfigManutencao manutencao = (PmpConfigManutencao)query.getSingleResult();
			//contrato.setIdConfigManutencao(manutencao);
			
			PmpContrato contrato = null;
			PmpConfigOperacional operacional = null;
			
			if(contratoComercialBean.getId() == null || contratoComercialBean.getId() == 0){
				contrato = new PmpContrato();
				contrato.setIdTipoContrato(pmpTipoContrato);
				contrato.setIdStatusContrato(statusContrato);
				contrato.setIdFuncionario(ID_FUNCIONARIO);
				contrato.setFilial(Integer.valueOf(FILIAL));
				contrato.setIdConfigTracao(pmpConfigTracao);
				if(filial.getRegional() != null){
					contrato.setRegional(filial.getRegional().toString());
				}
				contratoComercialBean.toBean(contrato);
				contrato.setNumeroSerie(contratoComercialBean.getPrefixo()+contratoComercialBean.getNumeroSerie());
				
				if (contratoComercialBean.getDistanciaGerador() != null) {
					contrato.setDistanciaGerador(BigDecimal.valueOf(Double.valueOf(contratoComercialBean.getDistanciaGerador())));
				}
				
				if (statusContrato.getSigla().equals("CA")) {
					contrato.setDataAceite(new Date());
					contrato.setHasSendEmail("S");
				} else if (statusContrato.getSigla().equals("CNA")) {
					contrato.setDataRejeicao(new Date());
					contrato.setIdMotivoNaoFecContrato(manager.find(PmpMotivosNaoFecContrato.class, contratoComercialBean.getIdMotNaoFecContrato()));
				}else{
					contrato.setDataRejeicao(null);
					contrato.setIdMotivoNaoFecContrato(null);
				}
				
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy/dd/MM");
				String data = fmt.format(new Date());
				String tipoContrato = "P";
				if(pmpTipoContrato.getSigla().equals("REN")){
					tipoContrato = "R";
				}
				if(contratoComercialBean.getContExcessao() != null && contratoComercialBean.getContExcessao().equals("S")){
					tipoContrato = "E";
				}
				if(manutencao.getIdConfiguracaoPreco().getDescricao().equals("PROMOÇÃO RETRO")){
					tipoContrato = "P";
				}
				if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
					tipoContrato = "S";
				}
//				String SQL = "From PmpConfigManutencao where modelo =:modelo and prefixo =:prefixo and bgrp =:bgrp and beginrange =:beginrange and endrange =:endrange and contExcessao=:contExcessao";
//				if(contratoComercialBean.getIdConfigPreco() != null && contratoComercialBean.getIdConfigPreco() > 0){
//					SQL += " and idConfiguracaoPreco.id = "+contratoComercialBean.getIdConfigPreco();
//				}
//				Query query = manager.createQuery(SQL);
//				query.setParameter("modelo", contratoComercialBean.getModelo());
//				query.setParameter("prefixo", contratoComercialBean.getPrefixo());
//				query.setParameter("bgrp", contratoComercialBean.getBusinessGroup());
//				query.setParameter("beginrange", contratoComercialBean.getBeginRanger());
//				query.setParameter("endrange", contratoComercialBean.getEndRanger());
//				query.setParameter("contExcessao", contratoComercialBean.getContExcessao());
//				PmpConfigManutencao manutencao = (PmpConfigManutencao)query.getSingleResult();
				contrato.setIdConfigManutencao(manutencao);
				manager.persist(contrato);
				contrato.setNumeroContrato(data.substring(2,4)+(Integer.valueOf(FILIAL) < 10?"0"+FILIAL:FILIAL)+tipoContrato+contrato.getId());
				if(statusContrato.getSigla().equals("CA")){
					operacional = new PmpConfigOperacional();
					operacional.setContato(contrato.getContatoComercial());
					operacional.setLocal(contrato.getBairro()+", "+contrato.getCidade()+", "+contrato.getEndereco());
					operacional.setIdFuncSupervisor(ID_FUNCIONARIO);
					operacional.setIdContrato(contrato);
					operacional.setTelefoneContato(contrato.getTelefoneComercial());
					operacional.setFilial(Long.valueOf(FILIAL));
					operacional.setObs(pmpTipoContrato.getDescricao());
					manager.persist(operacional);
				}
				
			}else{
				contrato = manager.find(PmpContrato.class, contratoComercialBean.getId());
				contrato.setIdConfigManutencao(manutencao);
				contrato.setIdTipoContrato(pmpTipoContrato);
				contrato.setIdStatusContrato(statusContrato);
				contrato.setIdConfigTracao(pmpConfigTracao);
				if(filial.getRegional() != null){
					contrato.setRegional(filial.getRegional().toString());
				}
				//contrato.setIdFuncionario(ID_FUNCIONARIO);
				//contrato.setFilial(Integer.valueOf(FILIAL));
				contratoComercialBean.toBean(contrato);
				contrato.setNumeroSerie(contratoComercialBean.getPrefixo() + contratoComercialBean.getNumeroSerie());
				
				if (contratoComercialBean.getDistanciaGerador() != null) {
					contrato.setDistanciaGerador(BigDecimal.valueOf(Double.valueOf(contratoComercialBean.getDistanciaGerador())));
				}
				
				if(statusContrato.getSigla().equals("CA")){
					contrato.setDataAceite(new Date());
					contrato.setHasSendEmail("S");
					query = manager.createQuery("from PmpConfigOperacional where idContrato.id = :id_contrato");
					query.setParameter("id_contrato", contrato.getId());
					List<PmpConfigOperacional> list = query.getResultList();
					if(list.size() > 0){
						operacional = list.get(0);
					}else{
						operacional = new PmpConfigOperacional();
					}
					operacional.setContato(contrato.getContatoComercial());
					operacional.setLocal(contrato.getBairro()+", "+contrato.getCidade()+", "+contrato.getEndereco());
					operacional.setIdFuncSupervisor(ID_FUNCIONARIO);
					operacional.setIdContrato(contrato);
					operacional.setTelefoneContato(contrato.getTelefoneComercial());
					operacional.setFilial(Long.valueOf(FILIAL));
					operacional.setObs(pmpTipoContrato.getDescricao());
					manager.merge(operacional);
					
					if(pmpTipoContrato.getSigla().equals("CON") || pmpTipoContrato.getSigla().equals("VEN") || pmpTipoContrato.getSigla().equals("REN") || pmpTipoContrato.getSigla().equals("VEPM")){//concessão ou contrato pós-pago (VEN)
						query = manager.createQuery("from PmpConfigOperacional where idContrato.id =:idContrato");
						query.setParameter("idContrato", contrato.getId());
						PmpConfigOperacional configOperacional = new PmpConfigOperacional();
						if(query.getResultList().size() > 0){
							configOperacional = (PmpConfigOperacional)query.getSingleResult();
						}
						configOperacional.setIdContrato(contrato);
						configOperacional.setNumOs(contrato.getNumeroContrato());
						configOperacional.setIdFuncSupervisor(contrato.getIdFuncionario());
						configOperacional.setContato(contrato.getContatoServicos());
						configOperacional.setLocal(contrato.getEndereco());
						configOperacional.setTelefoneContato(contrato.getTelefoneServicos());
						configOperacional.setFilial(contrato.getFilial().longValue());
						configOperacional.setCodErroDbs("00");
						configOperacional.setCompCode("8898");
						configOperacional.setJobCode("041");
						configOperacional.setCscc("SV");
						configOperacional.setInd("E");
						configOperacional.setNumeroSegmento("01");
						manager.merge(configOperacional);
					}
					
					
				}else if(statusContrato.getSigla().equals("CNA")){
					contrato.setDataRejeicao(new Date());
					contrato.setIdMotivoNaoFecContrato(manager.find(PmpMotivosNaoFecContrato.class, contratoComercialBean.getIdMotNaoFecContrato()));
				}else{
					contrato.setDataRejeicao(null);
					contrato.setIdMotivoNaoFecContrato(null);
				}
				manager.merge(contrato);
			}
			
			contrato.setIdClassificacaoContrato(classificacaoContrato);
			contrato.setIsTerritorio(contratoComercialBean.getIsTerritorio());
			//Remove o tipo de customização
			String siglaCustomizacao = "";
			query = manager.createNativeQuery("delete from PMP_CONTRATO_CUSTOMIZACAO where id_contrato =:id_contrato");
			query.setParameter("id_contrato", contrato.getId());
			query.executeUpdate();
			if(contratoComercialBean.getTipoCustomizacaoList() != null){
				for (ConfigurarCustomizacaoBean bean : contratoComercialBean.getTipoCustomizacaoList()) {
					if(bean.getIsSelected()){
						PmpContratoCustomizacao contratoCustomizacao = new PmpContratoCustomizacao();
						contratoCustomizacao.setIdContrato(contrato);
						contratoCustomizacao.setIdTipoCustomizacao(manager.find(PmpTipoCustomizacao.class, bean.getIdTipoCustomizacao()));
						manager.persist(contratoCustomizacao);
						query = manager.createNativeQuery("select sc.sigla_customizacao from PMP_SIGLA_CUSTOMIZACAO sc, PMP_CONFIG_CUSTOMIZACAO cc"+
								" where cc.ID_TIPO_CUSTOMIZACAO =:ID_TIPO_CUSTOMIZACAO"+
						" and sc.id_config_customizacao = cc.id");
						query.setParameter("ID_TIPO_CUSTOMIZACAO", bean.getIdTipoCustomizacao());
						List<String> sgTipoCustList = query.getResultList();
						for (String string : sgTipoCustList) {
							siglaCustomizacao += "'"+string+"',";
						}
					}
				}
			}
			if(siglaCustomizacao.length() > 0){
				siglaCustomizacao = siglaCustomizacao.substring(0, siglaCustomizacao.length() - 1);
			}else{
				siglaCustomizacao = "'-null'";
			}
			if(contratoComercialBean.getFuncionarioIndicado() != null && contratoComercialBean.getMatriculaIndicado() != null){
				contrato.setIdFuncionarioIndicacao(contratoComercialBean.getMatriculaIndicado());
				contrato.setNomeIndicacao(contratoComercialBean.getFuncionarioIndicado());
				contrato.setComissaoindicacao(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getComissaoIndicacao());
			}else{
				contrato.setIdFuncionarioIndicacao(null);
				contrato.setNomeIndicacao(null);
				contrato.setComissaoConsultor(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getComissaoConsultor());
				//contrato.setComissaoConsultor(null);
				contrato.setComissaoindicacao(null);
			}
			//contrato.setIsFindSubTributaria(contratoComercialBean.getIsFindSubstuicaoTributaria());
			//contrato.setCodErroDbs(contratoComercialBean.getCodErroDbs());
			//contrato.setMsgErroDbs(contratoComercialBean.getMsgErroDbs());
			//contrato.setNumDocDbs(contratoComercialBean.getNumDocDbs());
			if(!contratoComercialBean.getIsGeradorStandby()) {
				this.insertManutHorasPlus(contratoComercialBean.getConfigManutencaoHorasBeanList(), contrato, manager, isSaveHorasPosPago, siglaCustomizacao);
				contratoComercialBean.setPrintRevisaoPosPago(contrato.getPrintRevisaoPosPago());
			} else {
				this.insertManutMeses(contratoComercialBean.getConfigManutencaoMesesBeanList(), contrato, manager);
			}
			
			if(contratoComercialBean.getIsFindSubstuicaoTributaria() != null && "P".equals(contratoComercialBean.getIsFindSubstuicaoTributaria())){
				contrato.setIsFindSubTributaria("P");
				//envia as peças para gerar nova cotação
				prstmt = con.createStatement();
				String inCptcd = "";
				for (ConfigManutencaoHorasBean configManutHorasBean : contratoComercialBean.getConfigManutencaoHorasBeanList()) {
					inCptcd += "'"+configManutHorasBean.getStandardJob()+"',";
				}
				if(inCptcd.length() > 1){
					inCptcd = inCptcd.substring(0,inCptcd.length()-1);
				}
				new OsBusiness(this.usuarioBean).sendTotalPecasPecasDbs(contrato, manager, prstmt, inCptcd, siglaCustomizacao);
				contrato.setIsFindSubTributaria("P");
				contrato.setCodErroDbs("100");
				contrato.setNumDocDbs(null);
				contrato.setValoContrato(null);
				contratoComercialBean.setCodErroDbs("100");
				contrato.setMsgErroDbs("Aguarde o resultado da Cotação!");
				contratoComercialBean.setMsgErroDbs("Aguarde o resultado da Cotação!");
				
			}else if("R".equals(contratoComercialBean.getIsFindSubstuicaoTributaria())){//retira  a substituição tributária
				contrato.setIsFindSubTributaria(null);
				contrato.setCodErroDbs(null);
				contratoComercialBean.setCodErroDbs(null);
				contrato.setMsgErroDbs(null);
				contrato.setNumDocDbs(null);
				
			}
			
			
//			PmpMaquinaPl pl = new PmpMaquinaPl();
//			pl.setDataAtualizacao(new Date());
//			pl.setHorimetro(contratoComercialBean.getHorimetro());
//			pl.setNumeroSerie(contratoComercialBean.getPrefixo()+contratoComercialBean.getNumeroSerie());
//			manager.persist(pl);
			
			manager.getTransaction().commit();
			contratoComercialBean.setNumeroContrato(contrato.getNumeroContrato());
			contratoComercialBean.setId(contrato.getId().longValue());

			manager.getTransaction().begin();
			contrato = manager.find(PmpContrato.class, contrato.getId());
			contrato.setValorCusto(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorContratoPlus().multiply(BigDecimal.valueOf(contrato.getAnoContrato())));
			contrato.setValorSugerido(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorContratoPlus().multiply(BigDecimal.valueOf(contrato.getAnoContrato())));
			if(contrato.getValoContrato() == null){
				contrato.setValoContrato(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorContratoPlus().multiply(BigDecimal.valueOf(contrato.getAnoContrato())));
			}
			contratoComercialBean.setValorSugerido(ValorMonetarioHelper.formata("###,###.00", contrato.getValorSugerido().doubleValue()));
			contratoComercialBean.setValorContrato(ValorMonetarioHelper.formata("###,###.00", contrato.getValoContrato().doubleValue()));
			contratoComercialBean.setNumeroSerie(contrato.getNumeroSerie());
			manager.merge(contrato);
			manager.getTransaction().commit();
			if(statusContrato.getSigla().equals("CA")){
				AgendamentoBusiness business = new AgendamentoBusiness();
				business.saveAgendamentosPendentes(contratoComercialBean.getId());
			}

			return contratoComercialBean;
		} catch (Exception e) {
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
				if(prstmt != null){
					prstmt.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}			
		}
		return null;
	}
	public ContratoComercialBean saveOrUpdateMonitoramento(ContratoComercialBean contratoComercialBean, String isSaveHorasPosPago){
		EntityManager manager = null;
		Connection con = null;
		Statement prstmt = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			con = ConectionDbs.getConnecton();
			
			PmpTipoContrato pmpTipoContrato = manager.find(PmpTipoContrato.class, contratoComercialBean.getIdTipoContrato());
			PmpStatusContrato statusContrato = manager.find(PmpStatusContrato.class, contratoComercialBean.getStatusContrato());
			TwFilial filial = manager.find(TwFilial.class, Long.valueOf(FILIAL));
			PmpConfigTracao pmpConfigTracao = null;
			if(contratoComercialBean.getIdConfigTracao() != null && contratoComercialBean.getIdConfigTracao() != 0){
				pmpConfigTracao = manager.find(PmpConfigTracao.class, contratoComercialBean.getIdConfigTracao());
			}
			
			
//			String SQL = "From PmpConfigManutencao where modelo =:modelo and prefixo =:prefixo and bgrp =:bgrp and beginrange =:beginrange and endrange =:endrange and contExcessao=:contExcessao";
//			if(contratoComercialBean.getIdConfigPreco() != null && contratoComercialBean.getIdConfigPreco() > 0){
//				SQL += " and idConfiguracaoPreco.id = "+contratoComercialBean.getIdConfigPreco();
//			}
//			Query query = manager.createQuery(SQL);
//			query.setParameter("modelo", contratoComercialBean.getModelo());
//			query.setParameter("prefixo", contratoComercialBean.getPrefixo());
//			query.setParameter("bgrp", contratoComercialBean.getBusinessGroup());
//			query.setParameter("beginrange", contratoComercialBean.getBeginRanger());
//			query.setParameter("endrange", contratoComercialBean.getEndRanger());
//			query.setParameter("contExcessao", contratoComercialBean.getContExcessao());
//			PmpConfigManutencao manutencao = (PmpConfigManutencao)query.getSingleResult();
			//contrato.setIdConfigManutencao(manutencao);
			
			PmpContratoMonitoramento contrato = null;
			PmpConfigOperacionalMonitoramento operacional = null;
			
			if(contratoComercialBean.getId() == null || contratoComercialBean.getId() == 0){
				contrato = new PmpContratoMonitoramento();
				contrato.setIdTipoContrato(pmpTipoContrato);
				contrato.setIdStatusContrato(statusContrato);
				contrato.setIdFuncionario(ID_FUNCIONARIO);
				contrato.setFilial(Integer.valueOf(FILIAL));
				contrato.setIdConfigTracao(pmpConfigTracao);
				if(filial.getRegional() != null){
					contrato.setRegional(filial.getRegional().toString());
				}
				contratoComercialBean.toBean(contrato);
				contrato.setNumeroSerie(contratoComercialBean.getPrefixo()+contratoComercialBean.getNumeroSerie());
				
				if (contratoComercialBean.getDistanciaGerador() != null) {
					contrato.setDistanciaGerador(BigDecimal.valueOf(Double.valueOf(contratoComercialBean.getDistanciaGerador())));
				}
				
				if (statusContrato.getSigla().equals("CA")) {
					contrato.setDataAceite(new Date());
					contrato.setHasSendEmail("S");
				} else if (statusContrato.getSigla().equals("CNA")) {
					contrato.setDataRejeicao(new Date());
					contrato.setIdMotivoNaoFecContrato(manager.find(PmpMotivosNaoFecContrato.class, contratoComercialBean.getIdMotNaoFecContrato()));
				}else{
					contrato.setDataRejeicao(null);
					contrato.setIdMotivoNaoFecContrato(null);
				}
				
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy/dd/MM");
				String data = fmt.format(new Date());
				String tipoContrato = "M";
				if(pmpTipoContrato.getSigla().equals("REN")){
					tipoContrato = "R";
				}
//				String SQL = "From PmpConfigManutencao where modelo =:modelo and prefixo =:prefixo and bgrp =:bgrp and beginrange =:beginrange and endrange =:endrange and contExcessao=:contExcessao";
//				if(contratoComercialBean.getIdConfigPreco() != null && contratoComercialBean.getIdConfigPreco() > 0){
//					SQL += " and idConfiguracaoPreco.id = "+contratoComercialBean.getIdConfigPreco();
//				}
//				Query query = manager.createQuery(SQL);
//				query.setParameter("modelo", contratoComercialBean.getModelo());
//				query.setParameter("prefixo", contratoComercialBean.getPrefixo());
//				query.setParameter("bgrp", contratoComercialBean.getBusinessGroup());
//				query.setParameter("beginrange", contratoComercialBean.getBeginRanger());
//				query.setParameter("endrange", contratoComercialBean.getEndRanger());
//				query.setParameter("contExcessao", contratoComercialBean.getContExcessao());
//				PmpConfigManutencao manutencao = (PmpConfigManutencao)query.getSingleResult();
//				contrato.setIdConfigManutencao(manutencao);
				manager.persist(contrato);
				contrato.setNumeroContrato(data.substring(2,4)+(Integer.valueOf(FILIAL) < 10?"0"+FILIAL:FILIAL)+tipoContrato+contrato.getId());
				if(statusContrato.getSigla().equals("CA")){
					operacional = new PmpConfigOperacionalMonitoramento();
					operacional.setContato(contrato.getContatoComercial());
					operacional.setLocal(contrato.getBairro()+", "+contrato.getCidade()+", "+contrato.getEndereco());
					operacional.setIdFuncSupervisor(ID_FUNCIONARIO);
					operacional.setIdContrato(contrato);
					operacional.setTelefoneContato(contrato.getTelefoneComercial());
					operacional.setFilial(Long.valueOf(FILIAL));
					operacional.setObs(pmpTipoContrato.getDescricao());
					manager.persist(operacional);
				}
				
			}else{
				contrato = manager.find(PmpContratoMonitoramento.class, contratoComercialBean.getId());
				//contrato.setIdConfigManutencao(manutencao);
				contrato.setIdTipoContrato(pmpTipoContrato);
				contrato.setIdStatusContrato(statusContrato);
				contrato.setIdConfigTracao(pmpConfigTracao);
				if(filial.getRegional() != null){
					contrato.setRegional(filial.getRegional().toString());
				}
				//contrato.setIdFuncionario(ID_FUNCIONARIO);
				//contrato.setFilial(Integer.valueOf(FILIAL));
				contratoComercialBean.toBean(contrato);
				contrato.setNumeroSerie(contratoComercialBean.getPrefixo() + contratoComercialBean.getNumeroSerie());
				
				if (contratoComercialBean.getDistanciaGerador() != null) {
					contrato.setDistanciaGerador(BigDecimal.valueOf(Double.valueOf(contratoComercialBean.getDistanciaGerador())));
				}
				
				if(statusContrato.getSigla().equals("CA")){
					contrato.setDataAceite(new Date());
					contrato.setHasSendEmail("S");
					Query query = manager.createQuery("from PmpConfigOperacionalMonitoramento where idContrato.id = :id_contrato");
					query.setParameter("id_contrato", contrato.getId());
					List<PmpConfigOperacionalMonitoramento> list = query.getResultList();
					if(list.size() > 0){
						operacional = list.get(0);
					}else{
						operacional = new PmpConfigOperacionalMonitoramento();
					}
					operacional.setContato(contrato.getContatoComercial());
					operacional.setLocal(contrato.getBairro()+", "+contrato.getCidade()+", "+contrato.getEndereco());
					operacional.setIdFuncSupervisor(ID_FUNCIONARIO);
					operacional.setIdContrato(contrato);
					operacional.setTelefoneContato(contrato.getTelefoneComercial());
					operacional.setFilial(Long.valueOf(FILIAL));
					operacional.setObs(pmpTipoContrato.getDescricao());
					manager.merge(operacional);
					
					if(pmpTipoContrato.getSigla().equals("CON") || pmpTipoContrato.getSigla().equals("VEN") || pmpTipoContrato.getSigla().equals("REN")){//concessão ou contrato pós-pago (VEN)
						query = manager.createQuery("from PmpConfigOperacionalMonitoramento where idContrato.id =:idContrato");
						query.setParameter("idContrato", contrato.getId());
						PmpConfigOperacionalMonitoramento configOperacional = new PmpConfigOperacionalMonitoramento();
						if(query.getResultList().size() > 0){
							configOperacional = (PmpConfigOperacionalMonitoramento)query.getSingleResult();
						}
						configOperacional.setIdContrato(contrato);
						configOperacional.setNumOs(contrato.getNumeroContrato());
						configOperacional.setIdFuncSupervisor(contrato.getIdFuncionario());
						configOperacional.setContato(contrato.getContatoServicos());
						configOperacional.setLocal(contrato.getEndereco());
						configOperacional.setTelefoneContato(contrato.getTelefoneServicos());
						configOperacional.setFilial(contrato.getFilial().longValue());
						configOperacional.setCodErroDbs("00");
						configOperacional.setCompCode("8898");
						configOperacional.setJobCode("041");
						configOperacional.setCscc("SV");
						configOperacional.setInd("E");
						configOperacional.setNumeroSegmento("01");
						manager.merge(configOperacional);
					}
					
					
				}else if(statusContrato.getSigla().equals("CNA")){
					contrato.setDataRejeicao(new Date());
					contrato.setIdMotivoNaoFecContrato(manager.find(PmpMotivosNaoFecContrato.class, contratoComercialBean.getIdMotNaoFecContrato()));
				}else{
					contrato.setDataRejeicao(null);
					contrato.setIdMotivoNaoFecContrato(null);
				}
				manager.merge(contrato);
			}
			//Remove o tipo de customização
			//String siglaCustomizacao = "";
			Query query = manager.createNativeQuery("delete from PMP_CONTRATO_CUSTOMIZACAO_MONITORAMENTO where id_contrato =:id_contrato");
			query.setParameter("id_contrato", contrato.getId());
			query.executeUpdate();
			if(contratoComercialBean.getTipoCustomizacaoList() != null){
				for (ConfigurarCustomizacaoBean bean : contratoComercialBean.getTipoCustomizacaoList()) {
					if(bean.getIsSelected()){
						PmpContratoCustomizacaoMonitoramento contratoCustomizacao = new PmpContratoCustomizacaoMonitoramento();
						contratoCustomizacao.setIdContrato(contrato);
						contratoCustomizacao.setIdTipoCustomizacao(manager.find(PmpTipoCustomizacao.class, bean.getIdTipoCustomizacao()));
						manager.persist(contratoCustomizacao);
					}
				}
			}
//			if(siglaCustomizacao.length() > 0){
//				siglaCustomizacao = siglaCustomizacao.substring(0, siglaCustomizacao.length() - 1);
//			}else{
//				siglaCustomizacao = "'-null'";
//			}
			if(contratoComercialBean.getFuncionarioIndicado() != null && contratoComercialBean.getMatriculaIndicado() != null){
				contrato.setIdFuncionarioIndicacao(contratoComercialBean.getMatriculaIndicado());
				contrato.setNomeIndicacao(contratoComercialBean.getFuncionarioIndicado());
				contrato.setComissaoindicacao(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getComissaoIndicacao());
			}else{
				contrato.setIdFuncionarioIndicacao(null);
				contrato.setNomeIndicacao(null);
				contrato.setComissaoConsultor(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getComissaoConsultor());
				//contrato.setComissaoConsultor(null);
				contrato.setComissaoindicacao(null);
			}
			BigDecimal valorContrato = new BigDecimal(contratoComercialBean.getValorContrato());
			contrato.setValoContrato(valorContrato);

			//contrato.setIsFindSubTributaria(contratoComercialBean.getIsFindSubstuicaoTributaria());
			//contrato.setCodErroDbs(contratoComercialBean.getCodErroDbs());
			//contrato.setMsgErroDbs(contratoComercialBean.getMsgErroDbs());
			//contrato.setNumDocDbs(contratoComercialBean.getNumDocDbs());
//			if(!contratoComercialBean.getIsGeradorStandby()) {
//				this.insertManutHoras(contratoComercialBean.getConfigManutencaoHorasBeanList(), contrato, manager, isSaveHorasPosPago, siglaCustomizacao);
//				contratoComercialBean.setPrintRevisaoPosPago(contrato.getPrintRevisaoPosPago());
//			} else {
//				this.insertManutMeses(contratoComercialBean.getConfigManutencaoMesesBeanList(), contrato, manager);
//			}
			
//			if(contratoComercialBean.getIsFindSubstuicaoTributaria() != null && "P".equals(contratoComercialBean.getIsFindSubstuicaoTributaria())){
//				contrato.setIsFindSubTributaria("P");
//				//envia as peças para gerar nova cotação
//				prstmt = con.createStatement();
//				String inCptcd = "";
//				for (ConfigManutencaoHorasBean configManutHorasBean : contratoComercialBean.getConfigManutencaoHorasBeanList()) {
//					inCptcd += "'"+configManutHorasBean.getStandardJob()+"',";
//				}
//				if(inCptcd.length() > 1){
//					inCptcd = inCptcd.substring(0,inCptcd.length()-1);
//				}
//				new OsBusiness(this.usuarioBean).sendTotalPecasPecasDbs(contrato, manager, prstmt, inCptcd, siglaCustomizacao);
//				contrato.setIsFindSubTributaria("P");
//				contrato.setCodErroDbs("100");
//				contrato.setNumDocDbs(null);
//				contrato.setValoContrato(null);
//				contratoComercialBean.setCodErroDbs("100");
//				contrato.setMsgErroDbs("Aguarde o resultado da Cotação!");
//				contratoComercialBean.setMsgErroDbs("Aguarde o resultado da Cotação!");
//				
//			}else if("R".equals(contratoComercialBean.getIsFindSubstuicaoTributaria())){//retira  a substituição tributária
//				contrato.setIsFindSubTributaria(null);
//				contrato.setCodErroDbs(null);
//				contratoComercialBean.setCodErroDbs(null);
//				contrato.setMsgErroDbs(null);
//				contrato.setNumDocDbs(null);
//				
//			}
			
			
//			PmpMaquinaPl pl = new PmpMaquinaPl();
//			pl.setDataAtualizacao(new Date());
//			pl.setHorimetro(contratoComercialBean.getHorimetro());
//			pl.setNumeroSerie(contratoComercialBean.getPrefixo()+contratoComercialBean.getNumeroSerie());
//			manager.persist(pl);
			
			manager.getTransaction().commit();
			contratoComercialBean.setNumeroContrato(contrato.getNumeroContrato());
			contratoComercialBean.setId(contrato.getId().longValue());
			
//			if(contrato.getIdTipoContrato().getSigla().equals("VEN")){
//				List<PrecoBean> result = new ArrayList<PrecoBean>();
//				if(contrato.getBgrp().equals("PM") || contrato.getBgrp().equals("SEV")){
//					result = this.findAllParcelasPMPKIT3Custo(contrato.getId(), siglaCustomizacao);
//				}else if(contrato.getBgrp().equals("KIT2")){
//					result = this.findAllParcelasKIT2Custo(contrato.getId(), siglaCustomizacao);
//				}
//				PrecoBean bean = result.get(0);
//				manager.getTransaction().begin();
//				contrato = manager.find(PmpContrato.class, contrato.getId());
//				contrato.setValorCusto((BigDecimal.valueOf(Double.valueOf(bean.getPreco().replace(".", "").replace(",", ".").replace("R$", "")))));
//				manager.merge(contrato);
//				manager.getTransaction().commit();
//			}
			if(statusContrato.getSigla().equals("CA")){
				AgendamentoBusiness business = new AgendamentoBusiness();
				business.saveAgendamentosPendentes(contratoComercialBean.getId());
			}
			return contratoComercialBean;
		} catch (Exception e) {
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
				if(prstmt != null){
					prstmt.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}			
		}
		return null;
	}
	private String validarRenovacaoContrato(ContratoComercialBean contratoComercialBean, EntityManager manager) {
		
		contratoComercialBean.setMsg(null);
		String SQL = "select count(c.id) from pmp_contrato c"+
					 "	where c.id_status_contrato = ( select s.id from pmp_status_contrato s where s.sigla = 'CA')"+
					 "	and c.id = (select distinct hs.id_contrato from pmp_cont_horas_standard hs where  hs.is_executado = 'N' and hs.id_contrato = c.id)"+
					// "	and c.data_aceite between to_date('01/01/2011 00:00:00', 'DD/MM/YYYY HH24:MI:SS') and  to_date('01/01/2013 23:59:59', 'DD/MM/YYYY HH24:MI:SS')"+
					 "	and c.filial = "+ Integer.valueOf(usuarioBean.getFilial())+
					 "	and c.numero_serie = '"+contratoComercialBean.getPrefixo()+contratoComercialBean.getNumeroSerie()+"'";
		Query query =  manager.createNativeQuery(SQL);
		Integer totalContratos = (Integer)query.getSingleResult();
		if(totalContratos.intValue() > 0){
			SQL = "select count( hs.id_contrato) from pmp_cont_horas_standard hs where  hs.is_executado = 'N' and hs.id_contrato = (select min(c.id) from pmp_contrato c where c.numero_serie = '"+contratoComercialBean.getPrefixo()+contratoComercialBean.getNumeroSerie()+"' and c.filial = "+Integer.valueOf(usuarioBean.getFilial())+")";
			query =  manager.createNativeQuery(SQL);
			Integer totalManutencao = (Integer)query.getSingleResult();
			return "O equipamento já possui um contrato aberto com "+totalManutencao.intValue()+" manutenções aberta(s).\nTem certeza que deseja renová-lo ou fazer uma proposta";
		}
		return null;
	}
	
	public ContratoComercialBean saveOrUpdateContratoAntigo(ContratoComercialBean contratoComercialBean){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			
			Query query = manager.createQuery("from PmpClassificacaoContrato where sigla =:sigla");
			query.setParameter("sigla", contratoComercialBean.getSiglaClassificacaoContrato());
			PmpClassificacaoContrato classificacaoContrato = (PmpClassificacaoContrato)query.getSingleResult(); 
			PmpTipoContrato pmpTipoContrato = manager.find(PmpTipoContrato.class, contratoComercialBean.getIdTipoContrato());
			PmpStatusContrato statusContrato = manager.find(PmpStatusContrato.class, contratoComercialBean.getStatusContrato());
			TwFilial filial = manager.find(TwFilial.class, Long.valueOf(FILIAL));
			PmpConfigTracao pmpConfigTracao = null;
			if(contratoComercialBean.getIdConfigTracao() != null && contratoComercialBean.getIdConfigTracao() != 0){
				pmpConfigTracao = manager.find(PmpConfigTracao.class, contratoComercialBean.getIdConfigTracao());
			}
			
			
			PmpContrato contrato = null;
			
			if(contratoComercialBean.getId() == null || contratoComercialBean.getId() == 0){
				contrato = new PmpContrato();
				contrato.setIdTipoContrato(pmpTipoContrato);
				contrato.setIdStatusContrato(statusContrato);
				contrato.setIdFuncionario(ID_FUNCIONARIO);
				contrato.setFilial(Integer.valueOf(FILIAL));
				contrato.setIdConfigTracao(pmpConfigTracao);
				if(filial.getRegional() != null){
					contrato.setRegional(filial.getRegional().toString());
				}
				contratoComercialBean.toBean(contrato);
				contrato.setNumeroSerie(contratoComercialBean.getPrefixo()+contratoComercialBean.getNumeroSerie());
				if(statusContrato.getSigla().equals("CA")){
					contrato.setDataAceite(new Date());
					contrato.setHasSendEmail("S");
				}else if(statusContrato.getSigla().equals("CNA")){
					contrato.setDataRejeicao(new Date());
					contrato.setIdMotivoNaoFecContrato(manager.find(PmpMotivosNaoFecContrato.class, contratoComercialBean.getIdMotNaoFecContrato()));
				}
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy/dd/MM");
				String data = fmt.format(new Date());
				String tipoContrato = "G";
				if(pmpTipoContrato.getSigla().equals("REN")){
					tipoContrato = "R";
				}
				
				String SQL = "From PmpConfigManutencao where modelo =:modelo and prefixo =:prefixo and bgrp =:bgrp and beginrange =:beginrange and endrange =:endrange and contExcessao=:contExcessao ";
				if(contratoComercialBean.getIdConfigPreco() != null && contratoComercialBean.getIdConfigPreco() > 0){
					SQL += " and idConfiguracaoPreco.id = "+contratoComercialBean.getIdConfigPreco();
				}else{
					SQL += " and isAtivo is null ";
				}
				query = manager.createQuery(SQL);
				query.setParameter("modelo", contratoComercialBean.getModelo());
				query.setParameter("prefixo", contratoComercialBean.getPrefixo());
				query.setParameter("bgrp", contratoComercialBean.getBusinessGroup());
				query.setParameter("beginrange", contratoComercialBean.getBeginRanger());
				query.setParameter("endrange", contratoComercialBean.getEndRanger());
				query.setParameter("contExcessao", contratoComercialBean.getContExcessao());
				PmpConfigManutencao manutencao = (PmpConfigManutencao)query.getSingleResult();
				contrato.setIdConfigManutencao(manutencao);
				manager.persist(contrato);
				contrato.setNumeroContrato(data.substring(2,4)+(Integer.valueOf(FILIAL) < 10?"0"+FILIAL:FILIAL)+tipoContrato+contrato.getId());
				if(statusContrato.getSigla().equals("CA")){
					PmpConfigOperacional operacional = new PmpConfigOperacional();
					operacional.setContato(contrato.getContatoComercial());
					operacional.setLocal(contrato.getBairro()+", "+contrato.getCidade()+", "+contrato.getEndereco());
					operacional.setIdFuncSupervisor(ID_FUNCIONARIO);
					operacional.setIdContrato(contrato);
					operacional.setNumOs(contratoComercialBean.getNumOs().toUpperCase());
					operacional.setTelefoneContato(contrato.getTelefoneComercial());
					operacional.setFilial(Long.valueOf(FILIAL));
					operacional.setObs(pmpTipoContrato.getDescricao());
					manager.persist(operacional);
				}
				
			}else{
				contrato = manager.find(PmpContrato.class, contratoComercialBean.getId());
				contrato.setIdTipoContrato(pmpTipoContrato);
				contrato.setIdStatusContrato(statusContrato);
				contrato.setRegional(filial.getRegional().toString());
				contrato.setIdConfigTracao(pmpConfigTracao);
				//contrato.setIdFuncionario(ID_FUNCIONARIO);
				//contrato.setFilial(Integer.valueOf(FILIAL));
				contratoComercialBean.toBean(contrato);
				contrato.setNumeroSerie(contratoComercialBean.getPrefixo()+contratoComercialBean.getNumeroSerie());
				if(statusContrato.getSigla().equals("CA")){
					contrato.setDataAceite(new Date());
					contrato.setHasSendEmail("S");
					query = manager.createQuery("from PmpConfigOperacional where idContrato.id = :id_contrato");
					query.setParameter("id_contrato", contrato.getId());
					List<PmpConfigOperacional> list = query.getResultList();
					PmpConfigOperacional operacional = null;
					if(list.size() > 0){
						operacional = list.get(0);
					}else{
						operacional = new PmpConfigOperacional();
					}
					operacional.setContato(contrato.getContatoComercial());
					operacional.setLocal(contrato.getBairro()+", "+contrato.getCidade()+", "+contrato.getEndereco());
					operacional.setIdFuncSupervisor(ID_FUNCIONARIO);
					operacional.setIdContrato(contrato);
					operacional.setNumOs(contratoComercialBean.getNumOs().toUpperCase());
					operacional.setTelefoneContato(contrato.getTelefoneComercial());
					operacional.setFilial(Long.valueOf(FILIAL));
					operacional.setObs(pmpTipoContrato.getDescricao());
					manager.merge(operacional);
				}else if(statusContrato.getSigla().equals("CNA")){
					contrato.setDataRejeicao(new Date());
					contrato.setIdMotivoNaoFecContrato(manager.find(PmpMotivosNaoFecContrato.class, contratoComercialBean.getIdMotNaoFecContrato()));
				}
				manager.merge(contrato);
			}
			
			contrato.setIdClassificacaoContrato(classificacaoContrato);
			contrato.setIsTerritorio(contratoComercialBean.getIsTerritorio());
			//Remove o tipo de customização
			query = manager.createNativeQuery("delete from PMP_CONTRATO_CUSTOMIZACAO where id_contrato =:id_contrato");
			query.setParameter("id_contrato", contrato.getId());
			query.executeUpdate();
			String siglaCustomizacao = "";
			if(contratoComercialBean.getTipoCustomizacaoList() != null){
				for (ConfigurarCustomizacaoBean bean : contratoComercialBean.getTipoCustomizacaoList()) {
					if(bean.getIsSelected()){
						PmpContratoCustomizacao contratoCustomizacao = new PmpContratoCustomizacao();
						contratoCustomizacao.setIdContrato(contrato);
						contratoCustomizacao.setIdTipoCustomizacao(manager.find(PmpTipoCustomizacao.class, bean.getIdTipoCustomizacao()));
						manager.persist(contratoCustomizacao);
						query = manager.createNativeQuery("select sc.sigla_customizacao from PMP_SIGLA_CUSTOMIZACAO sc, PMP_CONFIG_CUSTOMIZACAO cc"+
													" where cc.ID_TIPO_CUSTOMIZACAO =:ID_TIPO_CUSTOMIZACAO"+
													" and sc.id_config_customizacao = cc.id");
						query.setParameter("ID_TIPO_CUSTOMIZACAO", bean.getIdTipoCustomizacao());
						List<String> sgTipoCustList = query.getResultList();
						for (String string : sgTipoCustList) {
							siglaCustomizacao += "'"+string+"',";
						}
					}
				}
			}
			if(siglaCustomizacao.length() > 0){
				siglaCustomizacao = siglaCustomizacao.substring(0, siglaCustomizacao.length() - 1);
			}else{
				siglaCustomizacao = "'-null'";
			}
			this.insertManutHoras(contratoComercialBean.getConfigManutencaoHorasBeanList(), contrato, manager, "N", siglaCustomizacao);
//			PmpMaquinaPl pl = new PmpMaquinaPl();
//			pl.setDataAtualizacao(new Date());
//			pl.setHorimetro(contratoComercialBean.getHorimetro());
//			pl.setNumeroSerie(contratoComercialBean.getPrefixo()+contratoComercialBean.getNumeroSerie());
//			manager.persist(pl);
			
			manager.getTransaction().commit();
			contratoComercialBean.setNumeroContrato(contrato.getNumeroContrato());
			contratoComercialBean.setId(contrato.getId().longValue());
			if(contrato.getIdTipoContrato().getSigla().equals("VEN")){
				List<PrecoBean> result = new ArrayList<PrecoBean>();
				if(contrato.getBgrp().equals("PM") || contrato.getBgrp().equals("KIT3")){
					result = this.findAllParcelasPMPKIT3Custo(contrato.getId(), siglaCustomizacao);
				}else if(contrato.getBgrp().equals("KIT2")){
					result = this.findAllParcelasKIT2Custo(contrato.getId(), siglaCustomizacao);
				}
				PrecoBean bean = result.get(0);
				manager.getTransaction().begin();
				contrato = manager.find(PmpContrato.class, contrato.getId());
				contrato.setValorCusto((BigDecimal.valueOf(Double.valueOf(bean.getPreco().replace(".", "").replace(",", ".").replace("R$", "")))));
				manager.merge(contrato);
				manager.getTransaction().commit();
			}
			if(statusContrato.getSigla().equals("CA")){
				AgendamentoBusiness business = new AgendamentoBusiness();
				business.saveAgendamentosPendentes(contratoComercialBean.getId());
			}
			return contratoComercialBean;
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
		return null;
	}
	
	public void insertManutHoras(List<ConfigManutencaoHorasBean> beanList, PmpContrato contrato, EntityManager manager, String isSaveHorasPosPago, String siglaCustomizacao)
	throws Exception{
		Query query = manager.createNativeQuery("delete from PMP_CONT_HORAS_STANDARD where id_contrato = "+contrato.getId().longValue());
		query.executeUpdate();
		query = manager.createNativeQuery("delete from PMP_CONT_HORAS_STANDARD_PLUS where id_contrato = "+contrato.getId().longValue());
		query.executeUpdate();
		if(contrato.getBgrp().equals("KIT2")){
			query = manager.createNativeQuery("select distinct(frequencia) from pmp_cont_horas_standard where id_contrato = "+contrato.getId().longValue());
			Long frequencia = (Long)query.getSingleResult();
			Long primeiraManutencao = 0l;
			while(primeiraManutencao < contrato.getHorimetro().longValue()){
				primeiraManutencao = primeiraManutencao + frequencia;
			}
			if(frequencia.equals(500)){
				for (int i = 1; i < 5; i++) {
					PmpContHorasStandard standard = new PmpContHorasStandard();
					String standardJob = null;
					switch (frequencia.intValue()*i) {
					case 500:
						standardJob = "7502";
						break;
					case 1000:
						standardJob = "7503";
						break;
					case 1500:
						standardJob = "7502";
						break;
					case 2000:
						standardJob = "7504";
						break;

					default:
						break;
					}
					standard.setHoras(frequencia*i);
					if(i > 1){
						primeiraManutencao = primeiraManutencao + frequencia;
					}
					standard.setHorasManutencao(primeiraManutencao);
					standard.setStandardJobCptcd(standardJob);
					standard.setIdContrato(contrato);
					standard.setFrequencia(frequencia);
					manager.persist(standard);
				}
		}else{
			for (int i = 1; i < 9; i++) {
				PmpContHorasStandard standard = new PmpContHorasStandard();
				String standardJob = null;
				switch (frequencia.intValue()*i) {
				case 250:
					standardJob = "7501";
					break;
				case 500:
					standardJob = "7502";
					break;
				case 750:
					standardJob = "7501";
					break;
				case 1000:
					standardJob = "7503";
					break;
				case 1250:
					standardJob = "7501";
					break;
				case 1500:
					standardJob = "7502";
					break;
				case 1750:
					standardJob = "7501";
					break;
				case 2000:
					standardJob = "7504";
					break;

				default:
					break;
				}
				standard.setHoras(frequencia*i);
				if(i > 1){
					primeiraManutencao = primeiraManutencao + frequencia;
				}
				standard.setHorasManutencao(primeiraManutencao);
				standard.setStandardJobCptcd(standardJob);
				standard.setIdContrato(contrato);
				standard.setFrequencia(frequencia);
				manager.persist(standard);
			}
		}
		}else{
			Double custo = 0D;
			Double valorParcelas = 0D;
			if(contrato.getValorSugerido() != null && contrato.getValorSugerido().doubleValue() < contrato.getValoContrato().doubleValue()){
				valorParcelas = contrato.getValoContrato().doubleValue() - contrato.getValorSugerido().doubleValue();
				valorParcelas = valorParcelas / beanList.size();
			}
			
			if((contrato.getIdClassificacaoContrato().getSigla().equals("CUS") || contrato.getIdClassificacaoContrato().getSigla().equals("CUSLIGHT")) && "S".equals(contrato.getIsTerritorio())){
				if(contrato.getIsSpot() == null){
					beanList = this.getTa(beanList);
				}
			}else if((contrato.getIdClassificacaoContrato().getSigla().equals("CUS") || contrato.getIdClassificacaoContrato().getSigla().equals("CUSLIGHT")) && "N".equals(contrato.getIsTerritorio())){
				if(contrato.getIsSpot() == null){
					beanList = this.getNotTa(beanList);
				}
			}
			if(contrato.getIdClassificacaoContrato().getSigla().equals("PART")){
				beanList = this.getPartner(beanList, manager);
			}
			BigDecimal valorMonitoramento = BigDecimal.ZERO;
			if(contrato.getIdClassificacaoContrato().getSigla().equals("CUSLIGHT") && contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getMonitoramento().doubleValue() > 0){
				valorMonitoramento = BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getMonitoramento().doubleValue() / beanList.size());
			}
			for (int i = 0; i < beanList.size(); i++) {
				ConfigManutencaoHorasBean bean = beanList.get(i);
				PmpContHorasStandard standard = new PmpContHorasStandard();
				if(bean.getIsSelected() && "S".equals(isSaveHorasPosPago)){
					contrato.setPrintRevisaoPosPago(BigDecimal.valueOf(bean.getHorasManutencao()));
				}
				standard.setHoras(bean.getHoras());
				standard.setHorasManutencao(bean.getHorasManutencao());
				standard.setStandardJobCptcd(bean.getStandardJob());
				standard.setIdContrato(contrato);
				standard.setIsExecutado(bean.getIsExecutado());
				standard.setFrequencia(bean.getFrequencia());
				standard.setHorasRevisao(bean.getHorasRevisao());
				standard.setIsTa(bean.getIsTa());
				standard.setIsPartner(bean.getIsPartner());
				if(contrato.getIsFindSubTributaria() != null && contrato.getIsFindSubTributaria().equals("S")){
					custo = this.calcularValorRevisaoSubTributaria(contrato, standard, manager);
					if(custo != null){
						standard.setCusto(BigDecimal.valueOf(custo + valorParcelas));
					}else{
						throw new Exception();
					}
				}else if(contrato.getIsFindSubTributaria() == null || contrato.getIsFindSubTributaria().equals("N")){
					if(contrato.getIdClassificacaoContrato().getSigla().equals("PRE")){
						custo = this.calcularValorRevisaoPremium(contrato, standard, manager, siglaCustomizacao);
						BigDecimal valorParcelasDiluido = BigDecimal.valueOf(valorParcelas).divide(BigDecimal.valueOf(2));
						
						standard.setCustoPecas(standard.getCustoPecas().add((valorParcelasDiluido)));
						standard.setCustoMo(standard.getCustoMo().add((valorParcelasDiluido)));
						
					} else if(contrato.getIdClassificacaoContrato().getSigla().equals("CUS")){
						custo = this.calcularValorRevisaoCustomer(contrato, standard, manager, siglaCustomizacao);
						standard.setCustoPecas(standard.getCustoPecas().add(BigDecimal.valueOf(valorParcelas)));
					} else if(contrato.getIdClassificacaoContrato().getSigla().equals("PART")){
						String retirarPeca = "";
						if(!bean.getIsPartner().equals("N")){
							query = manager.createNativeQuery("select descricao from Pmp_Comp_Code_Partner");
							List<String> descricao = (List<String>)query.getResultList();
							for (String cptcd : descricao) {
								if(bean.getStandardJob().equals(cptcd)){
									retirarPeca = " and m.OJBLOC <> 'CST'";
								}
							}
						}
						custo = this.calcularValorRevisaoPartner(contrato, standard, manager, siglaCustomizacao, retirarPeca);
						if(bean.getIsPartner().equals("S")){
							standard.setCustoPecas(standard.getCustoPecas().add(BigDecimal.valueOf(valorParcelas)));
						}else{
							BigDecimal valorParcelasDiluido = BigDecimal.valueOf(valorParcelas).divide(BigDecimal.valueOf(2));
							
							standard.setCustoPecas(standard.getCustoPecas().add((valorParcelasDiluido)));
							standard.setCustoMo(standard.getCustoMo().add((valorParcelasDiluido)));
						}
					}else if(contrato.getIdClassificacaoContrato().getSigla().equals("CUSLIGHT")){
						custo = this.calcularValorRevisaoCustomerLight(contrato, standard, manager, siglaCustomizacao)+valorMonitoramento.doubleValue();
						standard.setCustoMo(valorMonitoramento);
					}
					if(custo != null){
						standard.setCusto(BigDecimal.valueOf(custo + valorParcelas));
					}else{
						throw new Exception();
					}
				}
				manager.persist(standard);
			}
		}
		
	}
	public void insertManutHorasPlus(List<ConfigManutencaoHorasBean> beanList, PmpContrato contrato, EntityManager manager, String isSaveHorasPosPago, String siglaCustomizacao){
		Query query = manager.createNativeQuery("delete from PMP_CONT_HORAS_STANDARD_PLUS where id_contrato = "+contrato.getId().longValue());
		query.executeUpdate();
		query = manager.createNativeQuery("delete from PMP_CONT_HORAS_STANDARD where id_contrato = "+contrato.getId().longValue());
		query.executeUpdate();	

			
			for (int i = 1; i <= contrato.getAnoContrato(); i++) {
				Calendar calendar = Calendar.getInstance(new Locale("pt","BR"));
				PmpContHorasStandardPlus standard = new PmpContHorasStandardPlus();
				standard.setMesesRevisaoContrato(Long.valueOf(i));
				if(i == 1){
					calendar.add(Calendar.MONTH, 6);
					standard.setDataRevisao(calendar.getTime());
				}else{
					int meses = ((i-1) * 12)+6;
					calendar.add(Calendar.MONTH, meses);
					standard.setDataRevisao(calendar.getTime());
				}
				standard.setCusto(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorContratoPlus());
				standard.setIdContrato(contrato);
				standard.setIsExecutado("N");
				manager.persist(standard);
			
		}
		
	}
	
	public Double calcularValorRevisaoPremium(PmpContrato contrato, PmpContHorasStandard standard, EntityManager manager, String siglaCustomizacao){

		Double custoManutencao = 0D;
		try {
			String complemento = "";
			String complementoSigla="";
			if(contrato.getIdConfigTracao() != null){
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ocptmd is null)";
				complemento += " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+standard.getIdContrato().getIdConfigTracao().getId()+") or JWKAPP is null)"
				+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ojbloc is null)";
			}
			if(siglaCustomizacao.length() > 0){
				complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and JWKAPP not in ("+siglaCustomizacao+")";
			}
		
			if(contrato.getIdTipoContrato().getSigla().equals("REN")){
					Query query = manager.createNativeQuery("select sum(m.landcs * m.dlrqty) from pmp_manutencao_preco_custo m"+
							" where m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
							" and m.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							complemento + complementoSigla +
							" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
					BigDecimal custoPecas = (BigDecimal)query.getSingleResult();
					if(custoPecas != null){
						custoManutencao = custoPecas.doubleValue();
						standard.setCustoPecas(custoPecas);
					}else{
						standard.setCustoPecas(BigDecimal.ZERO);
					}

					//			if(contrato.getTa().equals("S")){
					//				Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
					//				taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
					//				custoManutencao = custoManutencao + taCusto;
					//			}


					//tipo manutenção
					query = manager.createNativeQuery("select TIPO_PM from pmp_hora h"+
							" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
							" and h.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
					String tipoPm = (String)query.getSingleResult();	
					standard.setTipoPm(tipoPm);
					//total horas para a manutenção
					query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
							" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
							" and h.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
					BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
					standard.setCustoMo((BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhRental().doubleValue() * totalHHManutencao.doubleValue())).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmRental().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmRental().doubleValue())));
					custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhRental().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
					custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmRental().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmRental().doubleValue());//valor de km

					return custoManutencao;

			}else if(contrato.getIdTipoContrato().getSigla().equals("VEN") || contrato.getIdTipoContrato().getSigla().equals("VPG") || contrato.getIdTipoContrato().getSigla().equals("VEPM") || contrato.getIdTipoContrato().getSigla().equals("CON") || contrato.getIdTipoContrato().getSigla().equals("CAN")){
							
				Query query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
							" where m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
							" and m.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and m.sos <> '400'"+
							" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
							complemento + complementoSigla+
					" and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
					BigDecimal custoPecas = (BigDecimal)query.getSingleResult();
					if(custoPecas != null){
						Double custoTotal = custoPecas.doubleValue();
						//custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
						if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
							custoTotal = custoTotal - ((custoTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
						}else if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
							//custoTotal = custoTotal - ((custoTotal * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getPdpSpot().doubleValue())/100);
							custoTotal = custoTotal - ((custoTotal * contrato.getDescontoPdpSpot().doubleValue())/100);
						}
						custoManutencao = custoManutencao + custoTotal;
					}
					
					//Query sem desconto pdp e sem desconto de peças com SOS 400
					query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
							" where m.cptcd = '"+standard.getStandardJobCptcd()+"'" +
							" and m.sos = '400'"+
							" and m.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
							complemento + complementoSigla);
					custoPecas = (BigDecimal)query.getSingleResult();
					//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
					if(custoPecas != null){
						custoManutencao = custoManutencao + custoPecas.doubleValue();
					}
					
					
					
					//Query sem desconto pdp e sem desconto de peças com SOS 000
					query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
							" where m.cptcd = '"+standard.getStandardJobCptcd()+"'" +
							" and m.sos <> '000'"+
							" and m.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
							complemento + complementoSigla +
					" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
							" or m.bectyc is null)");
					custoPecas = (BigDecimal)query.getSingleResult();
					//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
					if(custoPecas != null){
						custoManutencao = custoManutencao + custoPecas.doubleValue();
					}
					
					//Query sem desconto pdp e com desconto de peças com SOS = 000
					query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
							" where m.cptcd = '"+standard.getStandardJobCptcd()+"'" +
							" and m.sos = '000'"+
							" and m.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
							complemento + complementoSigla +
					" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
							" or m.bectyc is null)");
					custoPecas = (BigDecimal)query.getSingleResult();
					//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
					if(custoPecas != null){
						if(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas() != null && contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue() > 0){
							Double custoTotal = custoPecas.doubleValue();
							if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
								custoTotal = custoTotal - ((custoTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue()))/100);//desconto PDP
							}
							custoManutencao = custoManutencao + custoTotal;
						}else{
							custoManutencao = custoManutencao + custoPecas.doubleValue();
						}
					}

					
					if(custoManutencao != null){
						BigDecimal custP = BigDecimal.valueOf(custoManutencao);
						standard.setCustoPecas(custP);
					}
				
//				//custo do TA
//				if(contrato.getTa().equals("S")){
//					Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
//					taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
//					custoManutencao = custoManutencao + taCusto;
//				}
				
					
					//tipo manutenção
					query = manager.createNativeQuery("select TIPO_PM from pmp_hora h"+
							" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
							" and h.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
					String tipoPm = (String)query.getSingleResult();	
					standard.setTipoPm(tipoPm);
				
				//total horas para a manutenção
				query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
						" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and h.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
				if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
					Double valorHH = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue();
					valorHH = valorHH + ( (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorSpot().doubleValue() * valorHH) /100 );
					//standard.setCustoMo((BigDecimal.valueOf(valorHH)).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmpSpot().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())));
					String arraySpot []= contrato.getQtdHorasDeslSpot().split(":");
					BigDecimal minSpot = BigDecimal.valueOf((Double.valueOf(arraySpot[1])/60));
					BigDecimal horasPdpSpot = BigDecimal.valueOf(Double.valueOf(arraySpot[0]));
					horasPdpSpot = horasPdpSpot.add(minSpot);
					standard.setCustoMo((BigDecimal.valueOf(valorHH)).add(BigDecimal.valueOf(horasPdpSpot.doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHorasDesSpot().doubleValue())));
					custoManutencao = custoManutencao + valorHH;//valor de hh
					//custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmpSpot().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue());//valor de km
					custoManutencao = custoManutencao + (horasPdpSpot.doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHorasDesSpot().doubleValue());//valor de km
				}else{

					standard.setCustoMo((BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue())).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())));
					custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
					custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue());//valor de km
				}

				if(contrato.getIdTipoContrato().getSigla().equals("VPG") && (contrato.getIsSpot() == null || contrato.getIsSpot().equals("N"))){
					custoManutencao = custoManutencao - ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescontoPrePago().doubleValue())/100);
				}
				return custoManutencao;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<ConfigManutencaoHorasBean> getTa(List<ConfigManutencaoHorasBean> configManutencaoHorasBeansList){
		Long frequencia = 0L;
		if(configManutencaoHorasBeansList.size() == 1){
			frequencia = ((ConfigManutencaoHorasBean)configManutencaoHorasBeansList.get(0)).getFrequencia();
		}else{
			frequencia = ((ConfigManutencaoHorasBean)configManutencaoHorasBeansList.get(configManutencaoHorasBeansList.size() - 1)).getHorasManutencao() - ((ConfigManutencaoHorasBean)configManutencaoHorasBeansList.get(configManutencaoHorasBeansList.size() - 2)).getHorasManutencao(); 
			
		}
		Long horasManutencao = 0L;
		try {
			for (int i = 0; i < configManutencaoHorasBeansList.size(); i++) {
				ConfigManutencaoHorasBean bean = configManutencaoHorasBeansList.get(i);
				if(i == 0 && bean.getHorasManutencao() <= frequencia){
					horasManutencao = bean.getHorasManutencao();
				}else{
					if(i == 0){
						horasManutencao = horasManutencao + ((ConfigManutencaoHorasBean)configManutencaoHorasBeansList.get(i + 1)).getHorasManutencao() - bean.getHorasManutencao();
					}else{
						horasManutencao = horasManutencao + ((ConfigManutencaoHorasBean)configManutencaoHorasBeansList.get(i)).getHorasManutencao() - ((ConfigManutencaoHorasBean)configManutencaoHorasBeansList.get(i - 1)).getHorasManutencao();
					}
				}
				if(horasManutencao > 0 && horasManutencao == 2000){
					if((i + 1) == configManutencaoHorasBeansList.size()){
						ConfigManutencaoHorasBean horasBean = (ConfigManutencaoHorasBean)configManutencaoHorasBeansList.get(i -1);
						horasBean.setIsTa("S");
					}else{
						ConfigManutencaoHorasBean horasBean = (ConfigManutencaoHorasBean)configManutencaoHorasBeansList.get(i);
						horasBean.setIsTa("S");
					}
					horasManutencao = 0L;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return configManutencaoHorasBeansList;
	}
	private List<ConfigManutencaoHorasBean> getNotTa(List<ConfigManutencaoHorasBean> configManutencaoHorasBeansList){
		try {
			for (int i = 0; i < configManutencaoHorasBeansList.size(); i++) {
				ConfigManutencaoHorasBean bean = configManutencaoHorasBeansList.get(i);
				bean.setIsTa(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return configManutencaoHorasBeansList;
	}
	private List<ConfigManutencaoHorasBean> getPartner(List<ConfigManutencaoHorasBean> configManutencaoHorasBeansList, EntityManager manager){
		try {
			Query query = manager.createQuery("from PmpCompCodePartner");
			List<PmpCompCodePartner> codePartnerList = query.getResultList(); 
			for (int i = 0; i < configManutencaoHorasBeansList.size(); i++) {
				ConfigManutencaoHorasBean bean = configManutencaoHorasBeansList.get(i);
				bean.setIsPartner("N");
				for(PmpCompCodePartner partner : codePartnerList){
					if(partner.getDescricao().equals(bean.getStandardJob())){
						bean.setIsPartner("S");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return configManutencaoHorasBeansList;
	}
	
	public Double calcularValorRevisaoCustomer(PmpContrato contrato, PmpContHorasStandard standard, EntityManager manager, String siglaCustomizacao){
		
		Double custoManutencao = 0D;
		try {
			String complemento = "";
			String complementoSigla="";
			if(contrato.getIdConfigTracao() != null){
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ocptmd is null)";
				complemento += " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+standard.getIdContrato().getIdConfigTracao().getId()+") or JWKAPP is null)"
				+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ojbloc is null)";
			}
			if(siglaCustomizacao.length() > 0){
				complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and JWKAPP not in ("+siglaCustomizacao+")";
			}
			
			if(contrato.getIdTipoContrato().getSigla().equals("REN")){
				Query query = manager.createNativeQuery("select sum(m.landcs * m.dlrqty) from pmp_manutencao_preco_custo m"+
						" where m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'" +
						" and m.OJBLOC <> 'CST'"+
						complemento + complementoSigla +
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				BigDecimal custoPecas = (BigDecimal)query.getSingleResult();
				if(custoPecas != null){
					custoManutencao = custoPecas.doubleValue();
					standard.setCustoPecas(custoPecas);
				}else{
					standard.setCustoPecas(BigDecimal.ZERO);
				}
				
				//			if(contrato.getTa().equals("S")){
				//				Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
				//				taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
				//				custoManutencao = custoManutencao + taCusto;
				//			}
				
				
				//tipo manutenção
				query = manager.createNativeQuery("select TIPO_PM from pmp_hora h"+
						" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and h.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				String tipoPm = (String)query.getSingleResult();	
				standard.setTipoPm(tipoPm);
				//total horas para a manutenção
//				query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//						" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
//						" and h.bgrp = '"+contrato.getBgrp()+"'"+
//						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
//				standard.setCustoMo((BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhRental().doubleValue() * totalHHManutencao.doubleValue())).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmRental().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmRental().doubleValue())));
//				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhRental().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
//				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmRental().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmRental().doubleValue());//valor de km vezes a quantidade de manutenções
//				
				custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getMonitoramento().doubleValue();
				if(standard.getIsTa() != null && "S".equals(standard.getIsTa())){
					custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorTa().doubleValue() + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getSos().doubleValue();
				}
				
				return custoManutencao;
				
			}else if(contrato.getIdTipoContrato().getSigla().equals("VEN") || contrato.getIdTipoContrato().getSigla().equals("VPG") || contrato.getIdTipoContrato().getSigla().equals("VEPM") || contrato.getIdTipoContrato().getSigla().equals("CON") || contrato.getIdTipoContrato().getSigla().equals("CAN")){
				
				Query query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and m.OJBLOC <> 'CST'"+
						" and m.sos <> '400'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla+
				" and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
				BigDecimal custoPecas = (BigDecimal)query.getSingleResult();
				if(custoPecas != null){
					Double custoTotal = custoPecas.doubleValue();
					//custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
					if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
						custoTotal = custoTotal - ((custoTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
					}else if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
						//custoTotal = custoTotal - ((custoTotal * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getPdpSpot().doubleValue())/100);
						custoTotal = custoTotal - ((custoTotal * contrato.getDescontoPdpSpot().doubleValue())/100);
					}
					custoManutencao = custoManutencao + custoTotal;
				}
				//Query sem desconto pdp
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and m.sos <> '000'"+
						" and m.OJBLOC <> 'CST'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla +
						" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
				" or m.bectyc is null)");
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
				if(custoPecas != null){
					custoManutencao = custoManutencao + custoPecas.doubleValue();
				}
				
				
				//Query sem desconto pdp e sem desconto de peças com SOS 400
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+standard.getStandardJobCptcd()+"'" +
						" and m.sos = '400'"+
						" and m.OJBLOC <> 'CST'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla);
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
				if(custoPecas != null){
					custoManutencao = custoManutencao + custoPecas.doubleValue();
				}
				
				//Query sem desconto pdp e com desconto de peças com SOS = 000
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+standard.getStandardJobCptcd()+"'" +
						" and m.sos = '000'"+
						" and m.OJBLOC <> 'CST'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla +
				" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
						" or m.bectyc is null)");
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
				if(custoPecas != null){
					if(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas() != null && contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue() > 0){
						Double custoTotal = custoPecas.doubleValue();
						if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
							custoTotal = custoTotal - ((custoTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue()))/100);
						}
						custoManutencao = custoManutencao + custoTotal;
					}else{
						custoManutencao = custoManutencao + custoPecas.doubleValue();
					}
				}
				
				if(custoManutencao != null){
					BigDecimal custP = BigDecimal.valueOf(custoManutencao);
					standard.setCustoPecas(custP);
				}
				
//				//custo do TA
//				if(contrato.getTa().equals("S")){
//					Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
//					taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
//					custoManutencao = custoManutencao + taCusto;
//				}
				
				
				//tipo manutenção
				query = manager.createNativeQuery("select TIPO_PM from pmp_hora h"+
						" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and h.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				String tipoPm = (String)query.getSingleResult();	
				standard.setTipoPm(tipoPm);
				
				//total horas para a manutenção
//				query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//						" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
//						" and h.bgrp = '"+contrato.getBgrp()+"'"+
//						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
//				standard.setCustoMo((BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue())).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())));
//				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
//				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue());//valor de km vezes a quantidade de manutenções
				
				if(contrato.getIdTipoContrato().getSigla().equals("VPG") && (contrato.getIsSpot() == null || contrato.getIsSpot().equals("N"))){
					custoManutencao = custoManutencao - ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescontoPrePago().doubleValue())/100);
				}
				custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getMonitoramento().doubleValue();
				if(standard.getIsTa() != null && "S".equals(standard.getIsTa())){
					custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorTa().doubleValue() + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getSos().doubleValue();
				}
				return custoManutencao;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public Double calcularValorRevisaoCustomerLight(PmpContrato contrato, PmpContHorasStandard standard, EntityManager manager, String siglaCustomizacao){
		
		Double custoManutencao = 0D;
		try {
			String complemento = "";
			String complementoSigla="";
			if(contrato.getIdConfigTracao() != null){
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ocptmd is null)";
				complemento += " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+standard.getIdContrato().getIdConfigTracao().getId()+") or JWKAPP is null)"
						+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ojbloc is null)";
			}
			if(siglaCustomizacao.length() > 0){
				complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and JWKAPP not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
			}
			
			if(contrato.getIdTipoContrato().getSigla().equals("REN")){
				Query query = manager.createNativeQuery("select sum(m.landcs * m.dlrqty) from pmp_manutencao_preco_custo m"+
						" where m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and m.sos <> '050' "+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						complemento + complementoSigla +
						" and m.OJBLOC <> 'CST'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				BigDecimal custoPecas = (BigDecimal)query.getSingleResult();
				if(custoPecas != null){
					custoManutencao = custoPecas.doubleValue();
					standard.setCustoPecas(custoPecas);
				}else{
					standard.setCustoPecas(BigDecimal.ZERO);
				}
				
				//			if(contrato.getTa().equals("S")){
				//				Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
				//				taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
				//				custoManutencao = custoManutencao + taCusto;
				//			}
				
				
				//tipo manutenção
				query = manager.createNativeQuery("select TIPO_PM from pmp_hora h"+
						" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and h.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				String tipoPm = (String)query.getSingleResult();	
				standard.setTipoPm(tipoPm);
				//total horas para a manutenção
//				query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//						" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
//						" and h.bgrp = '"+contrato.getBgrp()+"'"+
//						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
//				standard.setCustoMo((BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhRental().doubleValue() * totalHHManutencao.doubleValue())).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmRental().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmRental().doubleValue())));
//				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhRental().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
//				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmRental().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmRental().doubleValue());//valor de km vezes a quantidade de manutenções
//				
				custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getMonitoramento().doubleValue();
				if(standard.getIsTa() != null && "S".equals(standard.getIsTa())){
					custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorTa().doubleValue() + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getSos().doubleValue();
				}
				
				return custoManutencao;
				
			}else if(contrato.getIdTipoContrato().getSigla().equals("VEN") || contrato.getIdTipoContrato().getSigla().equals("VPG") || contrato.getIdTipoContrato().getSigla().equals("VEPM") || contrato.getIdTipoContrato().getSigla().equals("CON") || contrato.getIdTipoContrato().getSigla().equals("CAN")){
				
				Query query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and m.sos <> '050' "+
						" and m.sos <> '400' "+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla+
						" and m.OJBLOC <> 'CST'"+
						" and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
				BigDecimal custoPecas = (BigDecimal)query.getSingleResult();
				if(custoPecas != null){
					Double custoTotal = custoPecas.doubleValue();
					//custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
					if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
						custoTotal = custoTotal - ((custoTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
					}else if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
						//custoTotal = custoTotal - ((custoTotal * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getPdpSpot().doubleValue())/100);
						custoTotal = custoTotal - ((custoTotal * contrato.getDescontoPdpSpot().doubleValue())/100);
					}
					custoManutencao = custoManutencao + custoTotal;
				}
				
				//Query sem desconto pdp e sem desconto de peças com SOS 400
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+standard.getStandardJobCptcd()+"'" +
						" and m.sos = '400'"+
						" and m.OJBLOC <> 'CST'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla);
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
				if(custoPecas != null){
					custoManutencao = custoManutencao + custoPecas.doubleValue();
				}
				
				
				//Query sem desconto pdp
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and m.sos <> '000'"+
						" and m.sos <> '400' "+
						" and m.sos <> '050' "+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla +
						" and m.OJBLOC <> 'CST'"+
						" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
						" or m.bectyc is null)");
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
				if(custoPecas != null){
					custoManutencao = custoManutencao + custoPecas.doubleValue();
				}
				
				//Query sem desconto pdp e com desconto de peças com SOS = 000
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+standard.getStandardJobCptcd()+"'" +
						" and m.sos = '000'"+
						" and m.sos <> '050' "+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla +
						" and m.OJBLOC <> 'CST'"+
						" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
						" or m.bectyc is null)");
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
				if(custoPecas != null){
					if(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas() != null && contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue() > 0){
						Double custoTotal = custoPecas.doubleValue();
						if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
							custoTotal = custoTotal - ((custoTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue()))/100);
						}
						custoManutencao = custoManutencao + custoTotal;
					}else{
						custoManutencao = custoManutencao + custoPecas.doubleValue();
					}
				}
				
				if(custoManutencao != null){
					BigDecimal custP = BigDecimal.valueOf(custoManutencao);
					standard.setCustoPecas(custP);
				}
				
//				//custo do TA
//				if(contrato.getTa().equals("S")){
//					Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
//					taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
//					custoManutencao = custoManutencao + taCusto;
//				}
				
				
				//tipo manutenção
				query = manager.createNativeQuery("select TIPO_PM from pmp_hora h"+
						" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and h.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				String tipoPm = (String)query.getSingleResult();	
				standard.setTipoPm(tipoPm);
				
				//total horas para a manutenção
//				query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//						" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
//						" and h.bgrp = '"+contrato.getBgrp()+"'"+
//						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
//				standard.setCustoMo((BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue())).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())));
//				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
//				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue());//valor de km vezes a quantidade de manutenções
				
				if(contrato.getIdTipoContrato().getSigla().equals("VPG") && (contrato.getIsSpot() == null || contrato.getIsSpot().equals("N"))){
					custoManutencao = custoManutencao - ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescontoPrePago().doubleValue())/100);
				}
				custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getMonitoramento().doubleValue();
				if(standard.getIsTa() != null && "S".equals(standard.getIsTa())){
					custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorTa().doubleValue() + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getSos().doubleValue();
				}
				return custoManutencao;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public Double calcularValorRevisaoPartner(PmpContrato contrato, PmpContHorasStandard standard, EntityManager manager, String siglaCustomizacao, String retirarPeca){
		
		Double custoManutencao = 0D;
		try {
			String complemento = "";
			String complementoSigla="";
			if(contrato.getIdConfigTracao() != null){
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ocptmd is null)";
				complemento += " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+standard.getIdContrato().getIdConfigTracao().getId()+") or JWKAPP is null)"
				+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ojbloc is null)";
			}
			if(siglaCustomizacao.length() > 0){
				complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and JWKAPP not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
			}
			
			if(contrato.getIdTipoContrato().getSigla().equals("REN")){
				Query query = manager.createNativeQuery("select sum(m.landcs * m.dlrqty) from pmp_manutencao_preco_custo m"+
						" where m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						retirarPeca+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						complemento + complementoSigla +
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				BigDecimal custoPecas = (BigDecimal)query.getSingleResult();
				if(custoPecas != null){
					custoManutencao = custoPecas.doubleValue();
					standard.setCustoPecas(custoPecas);
				}else{
					standard.setCustoPecas(BigDecimal.ZERO);
				}
				
				//			if(contrato.getTa().equals("S")){
				//				Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
				//				taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
				//				custoManutencao = custoManutencao + taCusto;
				//			}
				
				
				//tipo manutenção
				query = manager.createNativeQuery("select TIPO_PM from pmp_hora h"+
						" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and h.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				String tipoPm = (String)query.getSingleResult();	
				standard.setTipoPm(tipoPm);
				if((standard.getIsPartner() != null && "N".equals(standard.getIsPartner())) || ((standard.getStandardJobCptcd().equals("752A") || standard.getStandardJobCptcd().equals("752B")) && (standard.getIdContrato().getIdFamilia() == 41 ||  standard.getIdContrato().getIdFamilia() == 84))){
					//total horas para a manutenção
					query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
							" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
							" and h.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
					BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
					standard.setCustoMo((BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhRental().doubleValue() * totalHHManutencao.doubleValue())).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmRental().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmRental().doubleValue())));
					custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhRental().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
					custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmRental().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmRental().doubleValue());//valor de km vezes a quantidade de manutenções
				}
//				custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getMonitoramento().doubleValue();
//				if(standard.getIsTa() != null && "S".equals(standard.getIsTa())){
//					custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorTa().doubleValue() + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getSos().doubleValue();
//				}
				
				return custoManutencao;
				
			}else if(contrato.getIdTipoContrato().getSigla().equals("VEN") || contrato.getIdTipoContrato().getSigla().equals("VPG") || contrato.getIdTipoContrato().getSigla().equals("VEPM") || contrato.getIdTipoContrato().getSigla().equals("CON") || contrato.getIdTipoContrato().getSigla().equals("CAN")){
				
				Query query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and m.sos <> '400' "+
						retirarPeca+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla+
				" and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
				BigDecimal custoPecas = (BigDecimal)query.getSingleResult();
				if(custoPecas != null){
					Double custoTotal = custoPecas.doubleValue();
					//custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
					if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
						custoTotal = custoTotal - ((custoTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
					}else if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
						//custoTotal = custoTotal - ((custoTotal * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getPdpSpot().doubleValue())/100);
						custoTotal = custoTotal - ((custoTotal * contrato.getDescontoPdpSpot().doubleValue())/100);
					}
					custoManutencao = custoManutencao + custoTotal;
				}
				
				
				//Query sem desconto pdp e sem desconto de peças com SOS 400
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+standard.getStandardJobCptcd()+"'" +
						" and m.sos = '400'"+
						retirarPeca+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla);
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
				if(custoPecas != null){
					custoManutencao = custoManutencao + custoPecas.doubleValue();
				}
				
				//Query sem desconto pdp
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and m.sos <> '000'"+
						retirarPeca+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla +
						" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
				" or m.bectyc is null)");
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
				if(custoPecas != null){
					custoManutencao = custoManutencao + custoPecas.doubleValue();
				}
				//Query sem desconto pdp e com desconto de peças com SOS = 000
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+standard.getStandardJobCptcd()+"'" +
						" and m.sos = '000'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						retirarPeca+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla +
				" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
						" or m.bectyc is null)");
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
				if(custoPecas != null){
					if(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas() != null && contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue() > 0){
						Double custoTotal = custoPecas.doubleValue();
						if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
							custoTotal = custoTotal - ((custoTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue()))/100);
						}
						custoManutencao = custoManutencao + custoTotal;
					}else{
						custoManutencao = custoManutencao + custoPecas.doubleValue();
					}
				}
				
				if(custoManutencao != null){
					BigDecimal custP = BigDecimal.valueOf(custoManutencao);
					standard.setCustoPecas(custP);
				}
				
//				//custo do TA
//				if(contrato.getTa().equals("S")){
//					Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
//					taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
//					custoManutencao = custoManutencao + taCusto;
//				}
				
				
				//tipo manutenção
				query = manager.createNativeQuery("select TIPO_PM from pmp_hora h"+
						" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and h.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				String tipoPm = (String)query.getSingleResult();	
				standard.setTipoPm(tipoPm);
				if((standard.getIsPartner() != null && "N".equals(standard.getIsPartner())) || ((standard.getStandardJobCptcd().equals("752A") || standard.getStandardJobCptcd().equals("752B")) && (standard.getIdContrato().getIdFamilia() == 41 ||  standard.getIdContrato().getIdFamilia() == 84))){
					//total horas para a manutenção
					query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
							" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
							" and h.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
					BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
					
					if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
						Double valorHH = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue();
						valorHH = valorHH + ( (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorSpot().doubleValue() * valorHH) /100 );
						
						String arraySpot []= contrato.getQtdHorasDeslSpot().split(":");
						BigDecimal minSpot = BigDecimal.valueOf((Double.valueOf(arraySpot[1])/60));
						BigDecimal horasPdpSpot = BigDecimal.valueOf(Double.valueOf(arraySpot[0]));
						horasPdpSpot = horasPdpSpot.add(minSpot);
						//standard.setCustoMo((BigDecimal.valueOf(valorHH)).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmpSpot().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())));
						standard.setCustoMo((BigDecimal.valueOf(valorHH)).add(BigDecimal.valueOf(horasPdpSpot.doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHorasDesSpot().doubleValue())));
						custoManutencao = custoManutencao + valorHH;//valor de hh
						//custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmpSpot().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue());//valor de km
						custoManutencao = custoManutencao + (horasPdpSpot.doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHorasDesSpot().doubleValue());//valor de km
					}else{
						standard.setCustoMo((BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue())).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())));
						custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
						custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue());//valor de km
					}
				}
				if(contrato.getIdTipoContrato().getSigla().equals("VPG") && (contrato.getIsSpot() == null || contrato.getIsSpot().equals("N"))){
					custoManutencao = custoManutencao - ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescontoPrePago().doubleValue())/100);
				}
//				custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getMonitoramento().doubleValue();
//				if(standard.getIsTa() != null && "S".equals(standard.getIsTa())){
//					custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorTa().doubleValue() + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getSos().doubleValue();
//				}
				return custoManutencao;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Double calcularValorRevisaoSubTributaria(PmpContrato contrato, PmpContHorasStandard standard, EntityManager manager){
		Double custoManutencao = 0d;
		BigDecimal custoPecas = BigDecimal.ZERO;
		
		try{
			//manager = JpaUtil.getInstance();
			
				Query query = manager.createNativeQuery("select sum(m.VLSUB) from PMP_PECAS_CONFIG_OPERACIONAL m"+
						" where ID_CONTRATO = "+contrato.getId()+
						" and m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
				" and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
				custoPecas = (BigDecimal)query.getSingleResult();
				if(custoPecas != null){
					Double custoPecasTotal = custoPecas.doubleValue();
					if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
						custoPecasTotal = custoPecasTotal - ((custoPecasTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
					}else if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
						//custoPecasTotal = custoPecasTotal - ((custoPecasTotal * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getPdpSpot().doubleValue())/100);
						custoPecasTotal = custoPecasTotal - ((custoPecasTotal * contrato.getDescontoPdpSpot().doubleValue())/100);
					}
					custoManutencao = custoManutencao + custoPecasTotal;
				}
				//Query sem desconto pdp
				query = manager.createNativeQuery("select sum(m.VLSUB) from PMP_PECAS_CONFIG_OPERACIONAL m"+
						" where ID_CONTRATO = "+contrato.getId()+
						" and m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
				" or m.bectyc is null)");
				custoPecas = (BigDecimal)query.getSingleResult();
				custoManutencao = custoManutencao + custoPecas.doubleValue();
			
				//tipo manutenção
				query = manager.createNativeQuery("select TIPO_PM from pmp_hora h"+
						" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
						" and h.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				String tipoPm = (String)query.getSingleResult();	
				standard.setTipoPm(tipoPm);
				
				
			//total horas para a manutenção
			query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
					" where h.cptcd  = '"+standard.getStandardJobCptcd()+"'"+
					" and h.bgrp = '"+contrato.getBgrp()+"'"+
					" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
					" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
			BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
			
			if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
				Double valorHH = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue();
				valorHH = valorHH + ( (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorSpot().doubleValue() * valorHH) /100 );
				//standard.setCustoMo((BigDecimal.valueOf(valorHH)).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())));
				custoManutencao = custoManutencao + valorHH;//valor de hh
				//custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmpSpot().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue());//valor de km vezes a quantidade de manutenções
				String arraySpot []= contrato.getQtdHorasDeslSpot().split(":");
				BigDecimal minSpot = BigDecimal.valueOf((Double.valueOf(arraySpot[1])/60));
				BigDecimal horasPdpSpot = BigDecimal.valueOf(Double.valueOf(arraySpot[0]));
				horasPdpSpot = horasPdpSpot.add(minSpot);
				custoManutencao = custoManutencao + (horasPdpSpot.doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHorasDesSpot().doubleValue());//valor de km vezes a quantidade de manutenções
			}else{
				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue());//valor de km vezes a quantidade de manutenções
			}
			if(contrato.getIdTipoContrato().getSigla().equals("VPG") && (contrato.getIsSpot() == null || contrato.getIsSpot().equals("N"))){
				custoManutencao = custoManutencao - ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescontoPrePago().doubleValue())/100);
			}
			return custoManutencao;		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void insertManutMeses(List<ConfigManutencaoMesesBean> beanList, PmpContrato contrato, EntityManager manager){
		Query query = manager.createNativeQuery("DELETE FROM pmp_cont_meses_standard WHERE id_contrato = " + contrato.getId().longValue());
		query.executeUpdate();
		
		for (int i = 0; i < beanList.size(); i++) {
			ConfigManutencaoMesesBean bean = beanList.get(i);
			PmpContMesesStandard standard = new PmpContMesesStandard();

			standard.setMes(bean.getMeses());
			standard.setMesManutencao(bean.getMesManutencao());
			standard.setStandardJobCptcd(bean.getStandardJob());
			standard.setIdContrato(contrato);
			standard.setIsExecutado(bean.getIsExecutado());
			standard.setFrequencia(bean.getFrequencia());

			manager.persist(standard);
		}
	}

	public List<ContratoComercialBean> findAllContratoComercial(String nomeCliente, Long idStatusContrato, String isGerador, String contExcessao){
		EntityManager manager = null;
		List<ContratoComercialBean> result = new ArrayList<ContratoComercialBean>();
		try {
			manager = JpaUtil.getInstance();
			
			String sql = "SELECT DISTINCT c FROM PmpContrato c, PmpContHorasStandard chs " +
					" WHERE c.id = chs.idContrato AND (c.razaoSocial LIKE '" + nomeCliente.toUpperCase() + "%' or c.numeroSerie LIKE '" + nomeCliente.toUpperCase() + "%' or c.modelo LIKE '" + nomeCliente.toUpperCase() + "%') AND c.idFuncionario =:idFuncionario AND c.idTipoContrato.sigla NOT IN ('REN', 'CAN') and c.contExcessao =:contExcessao and (c.isSpot <> 'S' or c.isSpot is null)";
			if(usuarioBean.getIsAdm()){
				sql = " SELECT DISTINCT c FROM PmpContrato c, PmpContHorasStandard chs " +
						" WHERE c.id = chs.idContrato AND (c.razaoSocial LIKE '" + nomeCliente.toUpperCase() + "%' or c.numeroSerie LIKE '" + nomeCliente.toUpperCase() + "%' or c.modelo LIKE '" + nomeCliente.toUpperCase() + "%') AND c.idTipoContrato.sigla NOT IN ('REN', 'CAN') and c.contExcessao =:contExcessao and (c.isSpot <> 'S' or c.isSpot is null)";
			}
			if(idStatusContrato > 0){
				sql += " AND c.idStatusContrato.id ="+idStatusContrato;
			}
			sql += "AND c.idConfigManutencao.idConfiguracaoPreco.descricao not in('PROMOÇÃO RETRO')";
			Query query = manager.createQuery(sql);
			if(!usuarioBean.getIsAdm()){
				query.setParameter("idFuncionario", ID_FUNCIONARIO);
			}
			query.setParameter("contExcessao", contExcessao);
			List<PmpContrato> contratoList = query.getResultList();
			TracaoBusiness business = new TracaoBusiness();
			for (int i = 0; i < contratoList.size(); i++) {
				PmpContrato contrato = contratoList.get(i);
				ContratoComercialBean bean = new ContratoComercialBean();
				bean.fromBean(contrato, usuarioBean);
				bean.setNumeroSerie(contrato.getNumeroSerie());
				bean.setModeloList(this.findAllModelosContrato(contrato.getContExcessao(), contrato.getIdConfigManutencao().getIdFamilia().getId(), isGerador));
				bean.setPrefixoList(this.findAllPrefixosContrato(contrato.getModelo(),contrato.getIdConfigManutencao().getContExcessao()));
				bean.setRangerList(this.findAllRangerContrato(contrato.getModelo(), contrato.getPrefixo(), contrato.getIdConfigManutencao().getContExcessao(), contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getId()));
				bean.setTipoCustomizacaoList(this.findAllTipoCustomizacao(contrato.getModelo(), contrato.getIdFamilia(), contrato.getId()));
				bean.setPrecoEspecialList(this.findAllPrecoEspecial(contrato.getModelo(), contrato.getPrefixo(),contrato.getContExcessao(),contrato.getIdFamilia()));
				bean.setFuncionarioIndicado(contrato.getNomeIndicacao());
				bean.setMatriculaIndicado(contrato.getIdFuncionarioIndicacao());
				
				
				if(contrato.getIdConfigTracao() != null){
					query = manager.createNativeQuery("select ID_ARV from ARV_INSPECAO where DESCRICAO =:modelo");
					query.setParameter("modelo", contrato.getModelo());
					BigDecimal idModelo = (BigDecimal)query.getResultList().get(0);
					bean.setConfigTracaoList(business.findAllConfigTracao(idModelo.longValue()));
				}
				query = manager.createNativeQuery("select hm.horas ,hm.horas_manutencao, hm.standard_job_cptcd, 'true', hm.is_executado, hm.horas_revisao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato = "+contrato.getId()+
						" union"+
						" select h.horas,h.horas, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and h.horas not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")"+
						" union"+
						" select h.horas,h.horas + 12000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 12000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" union"+
						" select h.horas,h.horas + 24000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 24000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" union"+
						" select h.horas,h.horas + 36000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 36000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" union"+
						" select h.horas,h.horas + 48000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 48000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" order by 2 ");
				
				
			   List<ConfigManutencaoHorasBean> horasList = new ArrayList<ConfigManutencaoHorasBean>();
			   List<Object[]> manutHorasList = query.getResultList();
			   Long frequencia = 0l;
			   for (int j = 0; j < manutHorasList.size(); j++){ 
				   Object[] objects  = (Object[]) manutHorasList.get(j);
				   if(j == 0 || j == 1 && manutHorasList.size() > 2){
					   Object[] objectsProximo  = (Object[]) manutHorasList.get(j+1);
					   frequencia = ((BigDecimal)objectsProximo[1]).longValue() - ((BigDecimal)objects[1]).longValue();
				   }else{
					   Object[] objectsPenultimo  = (Object[]) manutHorasList.get(manutHorasList.size()-2);
					   Object[] objectsUltimo  = (Object[]) manutHorasList.get(manutHorasList.size()-1);
					   frequencia = ((BigDecimal)objectsUltimo[1]).longValue() - ((BigDecimal)objectsPenultimo[1]).longValue();
				   }
				   if(((BigDecimal)objects[1]).longValue() == 12600 || ((BigDecimal)objects[1]).longValue() == 12400 || ((BigDecimal)objects[1]).longValue() == 12200 || ((BigDecimal)objects[1]).longValue() == 12000 || 
						   ((BigDecimal)objects[1]).longValue() == 24600 || ((BigDecimal)objects[1]).longValue() == 24400 || ((BigDecimal)objects[1]).longValue() == 24200 || ((BigDecimal)objects[1]).longValue() == 24000 || 
						   ((BigDecimal)objects[1]).longValue() == 36600 || ((BigDecimal)objects[1]).longValue() == 36400 || ((BigDecimal)objects[1]).longValue() == 36200 || ((BigDecimal)objects[1]).longValue() == 36000 ||
					   	   ((BigDecimal)objects[1]).longValue() == 48600 || ((BigDecimal)objects[1]).longValue() == 48400 || ((BigDecimal)objects[1]).longValue() == 48200 
					   	   
					   	|| ((BigDecimal)objects[1]).longValue() == 12800  || 
						   ((BigDecimal)objects[1]).longValue() == 24800 || 
						   ((BigDecimal)objects[1]).longValue() == 36800 || 
					   	   ((BigDecimal)objects[1]).longValue() == 48800 ){
					   int k = j;
					   Object[] objectsProximo  = (Object[]) manutHorasList.get(j+1);
					   while(((BigDecimal)objectsProximo[1]).longValue() != (((BigDecimal)objects[1]).longValue() + frequencia)){
						   manutHorasList.remove(k+1);
						   objectsProximo  = (Object[]) manutHorasList.get(k+1);
					   }
					}
				   ConfigManutencaoHorasBean horasBean = new ConfigManutencaoHorasBean();
				   horasBean.setHoras(((BigDecimal)objects[0]).longValue());
				   horasBean.setHorasManutencao(((BigDecimal)objects[1]).longValue());
				   horasBean.setStandardJob((String)objects[2]);
				   horasBean.setIsSelected(Boolean.valueOf((String)objects[3]));
				   horasBean.setIsExecutado((String)objects[4]);
				   horasBean.setFrequencia(frequencia);
				   if(objects[5] != null){
					   horasBean.setHorasRevisao(((BigDecimal)objects[5]).longValue());
				   }
				   horasList.add(horasBean);
			   }
			   Collections.sort(horasList);
			   bean.setConfigManutencaoHorasBeanList(horasList);
			   result.add(bean);
			}			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		
		return result;
	}
	public List<ContratoComercialBean> findAllContratoComercialSpot(String nomeCliente, Long idStatusContrato, String isGerador, String contExcessao){
		EntityManager manager = null;
		List<ContratoComercialBean> result = new ArrayList<ContratoComercialBean>();
		try {
			manager = JpaUtil.getInstance();
			
			String sql = "SELECT DISTINCT c FROM PmpContrato c, PmpContHorasStandard chs " +
			" WHERE c.id = chs.idContrato AND (c.razaoSocial LIKE '" + nomeCliente.toUpperCase() + "%' or c.numeroSerie LIKE '" + nomeCliente.toUpperCase() + "%' or c.modelo LIKE '" + nomeCliente.toUpperCase() + "%') AND c.idFuncionario =:idFuncionario AND c.idTipoContrato.sigla NOT IN ('REN', 'CAN') and c.contExcessao =:contExcessao and c.isSpot = 'S'";
			if(usuarioBean.getIsAdm() || usuarioBean.getSiglaPerfil().equals("OPER")){
				sql = " SELECT DISTINCT c FROM PmpContrato c, PmpContHorasStandard chs " +
				" WHERE c.id = chs.idContrato AND (c.razaoSocial LIKE '" + nomeCliente.toUpperCase() + "%' or c.numeroSerie LIKE '" + nomeCliente.toUpperCase() + "%' or c.modelo LIKE '" + nomeCliente.toUpperCase() + "%') AND c.idTipoContrato.sigla NOT IN ('REN', 'CAN') and c.contExcessao =:contExcessao and c.isSpot = 'S'";
			}
			if(idStatusContrato > 0){
				sql += " AND c.idStatusContrato.id ="+idStatusContrato;
			}
			sql += "AND c.idConfigManutencao.idConfiguracaoPreco.descricao not in('PROMOÇÃO RETRO')";
			Query query = manager.createQuery(sql);
			if(!usuarioBean.getIsAdm() && !usuarioBean.getSiglaPerfil().equals("OPER")){
				query.setParameter("idFuncionario", ID_FUNCIONARIO);
			}
			query.setParameter("contExcessao", contExcessao);
			List<PmpContrato> contratoList = query.getResultList();
			TracaoBusiness business = new TracaoBusiness();
			for (int i = 0; i < contratoList.size(); i++) {
				PmpContrato contrato = contratoList.get(i);
				ContratoComercialBean bean = new ContratoComercialBean();
				bean.fromBean(contrato, usuarioBean);
				bean.setNumeroSerie(contrato.getNumeroSerie());
				bean.setModeloList(this.findAllModelosContrato(contrato.getContExcessao(), contrato.getIdConfigManutencao().getIdFamilia().getId(), isGerador));
				bean.setPrefixoList(this.findAllPrefixosContrato(contrato.getModelo(),contrato.getIdConfigManutencao().getContExcessao()));
				bean.setRangerList(this.findAllRangerContrato(contrato.getModelo(), contrato.getPrefixo(), contrato.getIdConfigManutencao().getContExcessao(), contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getId()));
				bean.setTipoCustomizacaoList(this.findAllTipoCustomizacao(contrato.getModelo(), contrato.getIdFamilia(), contrato.getId()));
				bean.setPrecoEspecialList(this.findAllPrecoEspecial(contrato.getModelo(), contrato.getPrefixo(),contrato.getContExcessao(),contrato.getIdFamilia()));
				bean.setFuncionarioIndicado(contrato.getNomeIndicacao());
				bean.setMatriculaIndicado(contrato.getIdFuncionarioIndicacao());
				
				
				if(contrato.getIdConfigTracao() != null){
					query = manager.createNativeQuery("select ID_ARV from ARV_INSPECAO where DESCRICAO =:modelo");
					query.setParameter("modelo", contrato.getModelo());
					BigDecimal idModelo = (BigDecimal)query.getResultList().get(0);
					bean.setConfigTracaoList(business.findAllConfigTracao(idModelo.longValue()));
				}
				query = manager.createNativeQuery("select hm.horas ,hm.horas_manutencao, hm.standard_job_cptcd, 'true', hm.is_executado, hm.horas_revisao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato = "+contrato.getId()+
						" union"+
						" select h.horas,h.horas, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and h.horas not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")"+
						" union"+
						" select h.horas,h.horas + 12000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 12000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" union"+
						" select h.horas,h.horas + 24000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 24000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" union"+
						" select h.horas,h.horas + 36000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 36000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" union"+
						" select h.horas,h.horas + 48000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 48000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
				" order by 2 ");
				
				
				List<ConfigManutencaoHorasBean> horasList = new ArrayList<ConfigManutencaoHorasBean>();
				List<Object[]> manutHorasList = query.getResultList();
				Long frequencia = 0l;
				for (int j = 0; j < manutHorasList.size(); j++){ 
					Object[] objects  = (Object[]) manutHorasList.get(j);
					if(j == 0 || j == 1 && manutHorasList.size() > 2){
						Object[] objectsProximo  = (Object[]) manutHorasList.get(j+1);
						frequencia = ((BigDecimal)objectsProximo[1]).longValue() - ((BigDecimal)objects[1]).longValue();
					}else{
						Object[] objectsPenultimo  = (Object[]) manutHorasList.get(manutHorasList.size()-2);
						Object[] objectsUltimo  = (Object[]) manutHorasList.get(manutHorasList.size()-1);
						frequencia = ((BigDecimal)objectsUltimo[1]).longValue() - ((BigDecimal)objectsPenultimo[1]).longValue();
					}
					if(((BigDecimal)objects[1]).longValue() == 12600 || ((BigDecimal)objects[1]).longValue() == 12400 || ((BigDecimal)objects[1]).longValue() == 12200 || ((BigDecimal)objects[1]).longValue() == 12000 || 
							((BigDecimal)objects[1]).longValue() == 24600 || ((BigDecimal)objects[1]).longValue() == 24400 || ((BigDecimal)objects[1]).longValue() == 24200 || ((BigDecimal)objects[1]).longValue() == 24000 || 
							((BigDecimal)objects[1]).longValue() == 36600 || ((BigDecimal)objects[1]).longValue() == 36400 || ((BigDecimal)objects[1]).longValue() == 36200 || ((BigDecimal)objects[1]).longValue() == 36000 ||
							((BigDecimal)objects[1]).longValue() == 48600 || ((BigDecimal)objects[1]).longValue() == 48400 || ((BigDecimal)objects[1]).longValue() == 48200 
							|| ((BigDecimal)objects[1]).longValue() == 12800  || 
							   ((BigDecimal)objects[1]).longValue() == 24800 || 
							   ((BigDecimal)objects[1]).longValue() == 36800 || 
						   	   ((BigDecimal)objects[1]).longValue() == 48800 ){
						int k = j;
						Object[] objectsProximo  = (Object[]) manutHorasList.get(j+1);
						while(((BigDecimal)objectsProximo[1]).longValue() != (((BigDecimal)objects[1]).longValue() + frequencia)){
							manutHorasList.remove(k+1);
							objectsProximo  = (Object[]) manutHorasList.get(k+1);
						}
					}
					ConfigManutencaoHorasBean horasBean = new ConfigManutencaoHorasBean();
					horasBean.setHoras(((BigDecimal)objects[0]).longValue());
					horasBean.setHorasManutencao(((BigDecimal)objects[1]).longValue());
					horasBean.setStandardJob((String)objects[2]);
					horasBean.setIsSelected(Boolean.valueOf((String)objects[3]));
					horasBean.setIsExecutado((String)objects[4]);
					horasBean.setFrequencia(frequencia);
					if(objects[5] != null){
						horasBean.setHorasRevisao(((BigDecimal)objects[5]).longValue());
					}
					horasList.add(horasBean);
				}
				Collections.sort(horasList);
				bean.setConfigManutencaoHorasBeanList(horasList);
				result.add(bean);
			}			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		
		return result;
	}
	public List<ContratoComercialBean> findAllContratoComercialPromocao(String nomeCliente, Long idStatusContrato, String isGerador, String contExcessao){
		EntityManager manager = null;
		List<ContratoComercialBean> result = new ArrayList<ContratoComercialBean>();
		try {
			manager = JpaUtil.getInstance();
			
			String sql = "SELECT DISTINCT c FROM PmpContrato c, PmpContHorasStandard chs " +
			" WHERE c.id = chs.idContrato AND (c.razaoSocial LIKE '" + nomeCliente.toUpperCase() + "%' or c.numeroSerie LIKE '" + nomeCliente.toUpperCase() + "%' or c.modelo LIKE '" + nomeCliente.toUpperCase() + "%') AND c.idFuncionario =:idFuncionario AND c.idTipoContrato.sigla NOT IN ('REN', 'CAN') and c.contExcessao =:contExcessao and (c.isSpot <> 'S' or c.isSpot is null)";
			if(usuarioBean.getIsAdm()){
				sql = " SELECT DISTINCT c FROM PmpContrato c, PmpContHorasStandard chs " +
				" WHERE c.id = chs.idContrato AND (c.razaoSocial LIKE '" + nomeCliente.toUpperCase() + "%' or c.numeroSerie LIKE '" + nomeCliente.toUpperCase() + "%' or c.modelo LIKE '" + nomeCliente.toUpperCase() + "%') AND c.idTipoContrato.sigla NOT IN ('REN', 'CAN') and c.contExcessao =:contExcessao and (c.isSpot <> 'S' or c.isSpot is null)";
			}
			if(idStatusContrato > 0){
				sql += " AND c.idStatusContrato.id ="+idStatusContrato;
			}
			sql += "AND c.idConfigManutencao.idConfiguracaoPreco.descricao in('PROMOÇÃO RETRO')";
			Query query = manager.createQuery(sql);
			if(!usuarioBean.getIsAdm()){
				query.setParameter("idFuncionario", ID_FUNCIONARIO);
			}
			query.setParameter("contExcessao", contExcessao);
			List<PmpContrato> contratoList = query.getResultList();
			TracaoBusiness business = new TracaoBusiness();
			for (int i = 0; i < contratoList.size(); i++) {
				PmpContrato contrato = contratoList.get(i);
				ContratoComercialBean bean = new ContratoComercialBean();
				bean.fromBean(contrato, usuarioBean);
				bean.setNumeroSerie(contrato.getNumeroSerie());
				bean.setModeloList(this.findAllModelosContrato(contrato.getContExcessao(), contrato.getIdConfigManutencao().getIdFamilia().getId(), isGerador));
				bean.setPrefixoList(this.findAllPrefixosContrato(contrato.getModelo(),contrato.getIdConfigManutencao().getContExcessao()));
				bean.setRangerList(this.findAllRangerContrato(contrato.getModelo(), contrato.getPrefixo(), contrato.getIdConfigManutencao().getContExcessao(), contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getId()));
				bean.setTipoCustomizacaoList(this.findAllTipoCustomizacao(contrato.getModelo(), contrato.getIdFamilia(), contrato.getId()));
				bean.setPrecoEspecialList(this.findAllPrecoEspecialPromocao(contrato.getModelo(), contrato.getPrefixo(),contrato.getContExcessao(),contrato.getIdFamilia()));
				bean.setFuncionarioIndicado(contrato.getNomeIndicacao());
				bean.setMatriculaIndicado(contrato.getIdFuncionarioIndicacao());
				
				if(contrato.getIdConfigTracao() != null){
					query = manager.createNativeQuery("select ID_ARV from ARV_INSPECAO where DESCRICAO =:modelo");
					query.setParameter("modelo", contrato.getModelo());
					BigDecimal idModelo = (BigDecimal)query.getResultList().get(0);
					bean.setConfigTracaoList(business.findAllConfigTracao(idModelo.longValue()));
				}
				query = manager.createNativeQuery("select hm.horas ,hm.horas_manutencao, hm.standard_job_cptcd, 'true', hm.is_executado, hm.horas_revisao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato = "+contrato.getId()+
						" union"+
						" select h.horas,h.horas, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and h.horas not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")"+
						" union"+
						" select h.horas,h.horas + 12000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 12000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" union"+
						" select h.horas,h.horas + 24000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 24000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" union"+
						" select h.horas,h.horas + 36000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 36000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" union"+
						" select h.horas,h.horas + 48000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 48000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
				" order by 2 ");
				
				
				List<ConfigManutencaoHorasBean> horasList = new ArrayList<ConfigManutencaoHorasBean>();
				List<Object[]> manutHorasList = query.getResultList();
				Long frequencia = 0l;
				for (int j = 0; j < manutHorasList.size(); j++){ 
					Object[] objects  = (Object[]) manutHorasList.get(j);
					if(j == 0 || j == 1 && manutHorasList.size() > 2){
						Object[] objectsProximo  = (Object[]) manutHorasList.get(j+1);
						frequencia = ((BigDecimal)objectsProximo[1]).longValue() - ((BigDecimal)objects[1]).longValue();
					}else{
						Object[] objectsPenultimo  = (Object[]) manutHorasList.get(manutHorasList.size()-2);
						Object[] objectsUltimo  = (Object[]) manutHorasList.get(manutHorasList.size()-1);
						frequencia = ((BigDecimal)objectsUltimo[1]).longValue() - ((BigDecimal)objectsPenultimo[1]).longValue();
					}
					if(((BigDecimal)objects[1]).longValue() == 12600 || ((BigDecimal)objects[1]).longValue() == 12400 || ((BigDecimal)objects[1]).longValue() == 12200 || ((BigDecimal)objects[1]).longValue() == 12000 || 
							   ((BigDecimal)objects[1]).longValue() == 24600 || ((BigDecimal)objects[1]).longValue() == 24400 || ((BigDecimal)objects[1]).longValue() == 24200 || ((BigDecimal)objects[1]).longValue() == 24000 || 
							   ((BigDecimal)objects[1]).longValue() == 36600 || ((BigDecimal)objects[1]).longValue() == 36400 || ((BigDecimal)objects[1]).longValue() == 36200 || ((BigDecimal)objects[1]).longValue() == 36000 ||
						   	   ((BigDecimal)objects[1]).longValue() == 48600 || ((BigDecimal)objects[1]).longValue() == 48400 || ((BigDecimal)objects[1]).longValue() == 48200 
						   	|| ((BigDecimal)objects[1]).longValue() == 12800  || 
							   ((BigDecimal)objects[1]).longValue() == 24800 || 
							   ((BigDecimal)objects[1]).longValue() == 36800 || 
						   	   ((BigDecimal)objects[1]).longValue() == 48800 ){
						int k = j;
						Object[] objectsProximo  = (Object[]) manutHorasList.get(j+1);
						while(((BigDecimal)objectsProximo[1]).longValue() != (((BigDecimal)objects[1]).longValue() + frequencia)){
							manutHorasList.remove(k+1);
							objectsProximo  = (Object[]) manutHorasList.get(k+1);
						}
					}
					ConfigManutencaoHorasBean horasBean = new ConfigManutencaoHorasBean();
					horasBean.setHoras(((BigDecimal)objects[0]).longValue());
					horasBean.setHorasManutencao(((BigDecimal)objects[1]).longValue());
					horasBean.setStandardJob((String)objects[2]);
					horasBean.setIsSelected(Boolean.valueOf((String)objects[3]));
					horasBean.setIsExecutado((String)objects[4]);
					horasBean.setFrequencia(frequencia);
					if(objects[5] != null){
						horasBean.setHorasRevisao(((BigDecimal)objects[5]).longValue());
					}
					horasList.add(horasBean);
				}
				Collections.sort(horasList);
				bean.setConfigManutencaoHorasBeanList(horasList);
				result.add(bean);
			}			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		
		return result;
	}
	public List<ContratoComercialBean> findAllContratoComercialPlus(String nomeCliente, Long idStatusContrato, String isGerador, String contExcessao){
		EntityManager manager = null;
		List<ContratoComercialBean> result = new ArrayList<ContratoComercialBean>();
		try {
			manager = JpaUtil.getInstance();
			
			String sql = "SELECT DISTINCT c FROM PmpContrato c, PmpContHorasStandardPlus chs " +
			" WHERE c.id = chs.idContrato AND (c.razaoSocial LIKE '" + nomeCliente.toUpperCase() + "%' or c.numeroSerie LIKE '" + nomeCliente.toUpperCase() + "%' or c.modelo LIKE '" + nomeCliente.toUpperCase() + "%') AND c.idFuncionario =:idFuncionario AND c.idTipoContrato.sigla NOT IN ('REN', 'CAN') and c.contExcessao =:contExcessao";
			if(usuarioBean.getIsAdm()){
				sql = " SELECT DISTINCT c FROM PmpContrato c, PmpContHorasStandardPlus chs " +
				" WHERE c.id = chs.idContrato AND (c.razaoSocial LIKE '" + nomeCliente.toUpperCase() + "%' or c.numeroSerie LIKE '" + nomeCliente.toUpperCase() + "%' or c.modelo LIKE '" + nomeCliente.toUpperCase() + "%') AND c.idTipoContrato.sigla NOT IN ('REN', 'CAN') and c.contExcessao =:contExcessao";
			}
			if(idStatusContrato > 0){
				sql += " AND c.idStatusContrato.id ="+idStatusContrato;
			}
			Query query = manager.createQuery(sql);
			if(!usuarioBean.getIsAdm()){
				query.setParameter("idFuncionario", ID_FUNCIONARIO);
			}
			query.setParameter("contExcessao", contExcessao);
			List<PmpContrato> contratoList = query.getResultList();
			TracaoBusiness business = new TracaoBusiness();
			for (int i = 0; i < contratoList.size(); i++) {
				PmpContrato contrato = contratoList.get(i);
				ContratoComercialBean bean = new ContratoComercialBean();
				bean.fromBean(contrato, usuarioBean);
				bean.setNumeroSerie(contrato.getNumeroSerie());
				bean.setModeloList(this.findAllModelosContrato(contrato.getContExcessao(), contrato.getIdConfigManutencao().getIdFamilia().getId(), isGerador));
				bean.setPrefixoList(this.findAllPrefixosContrato(contrato.getModelo(),contrato.getIdConfigManutencao().getContExcessao()));
				bean.setRangerList(this.findAllRangerContrato(contrato.getModelo(), contrato.getPrefixo(), contrato.getIdConfigManutencao().getContExcessao(), contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getId()));
				bean.setTipoCustomizacaoList(this.findAllTipoCustomizacao(contrato.getModelo(), contrato.getIdFamilia(), contrato.getId()));
				bean.setPrecoEspecialList(this.findAllPrecoEspecial(contrato.getModelo(), contrato.getPrefixo(),contrato.getContExcessao(),contrato.getIdFamilia()));
				bean.setFuncionarioIndicado(contrato.getNomeIndicacao());
				bean.setMatriculaIndicado(contrato.getIdFuncionarioIndicacao());
				bean.setAnosContrato(contrato.getAnoContrato());
				
				if(contrato.getIdConfigTracao() != null){
					query = manager.createNativeQuery("select ID_ARV from ARV_INSPECAO where DESCRICAO =:modelo");
					query.setParameter("modelo", contrato.getModelo());
					BigDecimal idModelo = (BigDecimal)query.getResultList().get(0);
					bean.setConfigTracaoList(business.findAllConfigTracao(idModelo.longValue()));
				}
				result.add(bean);
			}			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		
		return result;
	}
	public List<ContratoComercialBean> findAllContratoComercialPlusPromocao(String nomeCliente, Long idStatusContrato, String isGerador, String contExcessao){
		EntityManager manager = null;
		List<ContratoComercialBean> result = new ArrayList<ContratoComercialBean>();
		try {
			manager = JpaUtil.getInstance();
			
			String sql = "SELECT DISTINCT c FROM PmpContrato c, PmpContHorasStandardPlus chs " +
			" WHERE c.id = chs.idContrato AND (c.razaoSocial LIKE '" + nomeCliente.toUpperCase() + "%' or c.numeroSerie LIKE '" + nomeCliente.toUpperCase() + "%' or c.modelo LIKE '" + nomeCliente.toUpperCase() + "%') AND c.idFuncionario =:idFuncionario AND c.idTipoContrato.sigla NOT IN ('REN', 'CAN') and c.contExcessao =:contExcessao";
			if(usuarioBean.getIsAdm()){
				sql = " SELECT DISTINCT c FROM PmpContrato c, PmpContHorasStandardPlus chs " +
				" WHERE c.id = chs.idContrato AND (c.razaoSocial LIKE '" + nomeCliente.toUpperCase() + "%' or c.numeroSerie LIKE '" + nomeCliente.toUpperCase() + "%' or c.modelo LIKE '" + nomeCliente.toUpperCase() + "%') AND c.idTipoContrato.sigla NOT IN ('REN', 'CAN') and c.contExcessao =:contExcessao";
			}
			if(idStatusContrato > 0){
				sql += " AND c.idStatusContrato.id ="+idStatusContrato;
			}
			Query query = manager.createQuery(sql);
			if(!usuarioBean.getIsAdm()){
				query.setParameter("idFuncionario", ID_FUNCIONARIO);
			}
			query.setParameter("contExcessao", contExcessao);
			List<PmpContrato> contratoList = query.getResultList();
			TracaoBusiness business = new TracaoBusiness();
			for (int i = 0; i < contratoList.size(); i++) {
				PmpContrato contrato = contratoList.get(i);
				ContratoComercialBean bean = new ContratoComercialBean();
				bean.fromBean(contrato, usuarioBean);
				bean.setNumeroSerie(contrato.getNumeroSerie());
				bean.setModeloList(this.findAllModelosContrato(contrato.getContExcessao(), contrato.getIdConfigManutencao().getIdFamilia().getId(), isGerador));
				bean.setPrefixoList(this.findAllPrefixosContrato(contrato.getModelo(),contrato.getIdConfigManutencao().getContExcessao()));
				bean.setRangerList(this.findAllRangerContrato(contrato.getModelo(), contrato.getPrefixo(), contrato.getIdConfigManutencao().getContExcessao(), contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getId()));
				bean.setTipoCustomizacaoList(this.findAllTipoCustomizacao(contrato.getModelo(), contrato.getIdFamilia(), contrato.getId()));
				bean.setPrecoEspecialList(this.findAllPrecoEspecialPromocao(contrato.getModelo(), contrato.getPrefixo(),contrato.getContExcessao(),contrato.getIdFamilia()));
				bean.setFuncionarioIndicado(contrato.getNomeIndicacao());
				bean.setMatriculaIndicado(contrato.getIdFuncionarioIndicacao());
				bean.setAnosContrato(contrato.getAnoContrato());
				
				if(contrato.getIdConfigTracao() != null){
					query = manager.createNativeQuery("select ID_ARV from ARV_INSPECAO where DESCRICAO =:modelo");
					query.setParameter("modelo", contrato.getModelo());
					BigDecimal idModelo = (BigDecimal)query.getResultList().get(0);
					bean.setConfigTracaoList(business.findAllConfigTracao(idModelo.longValue()));
				}
				result.add(bean);
			}			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		
		return result;
	}

	
	
	public ContratoComercialBean findAllContratoComercial(Long idContrato){
		EntityManager manager = null;
		ContratoComercialBean bean = new ContratoComercialBean();
		try {
			manager = JpaUtil.getInstance();
			String sql = "SELECT DISTINCT c FROM PmpContrato c, PmpContHorasStandard chs " +
			" WHERE c.id = chs.idContrato AND c.id = "+idContrato;

			Query query = manager.createQuery(sql);
			PmpContrato contrato = (PmpContrato)query.getSingleResult();
			if(contrato.getIdTipoContrato().getSigla().equals("CAN")){
				query = manager.createQuery("from PmpConfigOperacional where idContrato.id =:idContrato");
				query.setParameter("idContrato", contrato.getId());
				PmpConfigOperacional operacional = (PmpConfigOperacional)query.getSingleResult();
				bean.setNumOs(operacional.getNumOs());
			}
			bean.fromBean(contrato, usuarioBean);
			bean.setNumeroSerie(contrato.getNumeroSerie());
			bean.setModeloList(this.findAllModelosContrato(contrato.getContExcessao(), contrato.getIdConfigManutencao().getIdFamilia().getId(), "N"));
			bean.setPrefixoList(this.findAllPrefixosContrato(contrato.getModelo(),contrato.getIdConfigManutencao().getContExcessao()));
			bean.setRangerList(this.findAllRangerContrato(contrato.getModelo(), contrato.getPrefixo(), contrato.getIdConfigManutencao().getContExcessao(), contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getId()));
			bean.setTipoCustomizacaoList(this.findAllTipoCustomizacao(contrato.getModelo(), contrato.getIdFamilia(), contrato.getId()));
			bean.setFuncionarioIndicado(contrato.getNomeIndicacao());
			bean.setMatriculaIndicado(contrato.getIdFuncionarioIndicacao());

			query = manager.createNativeQuery("select hm.horas ,hm.horas_manutencao, hm.standard_job_cptcd, 'true', hm.is_executado, hm.horas_revisao, hm.FREQUENCIA, hm.is_ta from pmp_cont_horas_standard hm"+
					" where hm.id_contrato = "+contrato.getId());
//					" union"+
//					" select h.horas,h.horas, h.standard_job_cptcd, 'false', 'N' from  pmp_config_horas_standard h " +
//					" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
//					" and h.horas not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
//					" where hm.id_contrato =  "+contrato.getId()+")"+
//					" union"+
//					" select h.horas,h.horas + 12000, h.standard_job_cptcd, 'false', 'N' from  pmp_config_horas_standard h " +
//					" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
//					" and (h.horas + 12000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
//					" where hm.id_contrato =  "+contrato.getId()+")" +
//			" order by 2 ");


			List<ConfigManutencaoHorasBean> horasList = new ArrayList<ConfigManutencaoHorasBean>();
			List<Object[]> manutHorasList = query.getResultList();
			Long frequencia = 0l;
			for (int j = 0; j < manutHorasList.size(); j++){ 
				Object[] objects  = (Object[]) manutHorasList.get(j);
				if(manutHorasList.size() > 1){
					if(j == 0 || j == 1 && manutHorasList.size() > 2){
						Object[] objectsProximo  = (Object[]) manutHorasList.get(j+1);
						frequencia = ((BigDecimal)objectsProximo[1]).longValue() - ((BigDecimal)objects[1]).longValue();
					}else{
						Object[] objectsPenultimo  = (Object[]) manutHorasList.get(manutHorasList.size()-2);
						Object[] objectsUltimo  = (Object[]) manutHorasList.get(manutHorasList.size()-1);
						frequencia = ((BigDecimal)objectsUltimo[1]).longValue() - ((BigDecimal)objectsPenultimo[1]).longValue();
					}
				}else{
					frequencia = ((BigDecimal)objects[6]).longValue();
				}
				ConfigManutencaoHorasBean horasBean = new ConfigManutencaoHorasBean();
				horasBean.setHoras(((BigDecimal)objects[0]).longValue());
				horasBean.setHorasManutencao(((BigDecimal)objects[1]).longValue());
				horasBean.setStandardJob((String)objects[2]);
				horasBean.setIsSelected(Boolean.valueOf((String)objects[3]));
				horasBean.setIsExecutado((String)objects[4]);
				horasBean.setFrequencia(frequencia);
				horasBean.setHorasRevisao(((BigDecimal)objects[5]).longValue());
				horasBean.setIsTa((String)objects[7]);
				horasList.add(horasBean);
			}
			bean.setConfigManutencaoHorasBeanList(horasList);
			return bean;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}

		return bean;
	}
	
	public List<ContratoComercialBean> findAllContratoComercialGerador(String nomeCliente, Long idStatusContrato, String isGerador) {
		EntityManager manager = null;
		List<ContratoComercialBean> result = new ArrayList<ContratoComercialBean>();
		try {
			manager = JpaUtil.getInstance();
			String sql = "SELECT DISTINCT c FROM PmpContrato c, PmpContMesesStandard cms " +
					" WHERE c.id = cms.idContrato AND c.razaoSocial LIKE '"+nomeCliente.toUpperCase()+"%' AND c.idFuncionario =:idFuncionario AND c.idTipoContrato.sigla NOT IN('REN', 'CAN')";
			if(usuarioBean.getIsAdm()){
				sql = "SELECT DISTINCT c FROM PmpContrato c, PmpContMesesStandard cms " +
						" WHERE c.id = cms.idContrato AND c.razaoSocial LIKE '"+nomeCliente.toUpperCase()+"%' AND c.idTipoContrato.sigla NOT IN ('REN', 'CAN')";
			}
			if(idStatusContrato > 0){
				sql += " and idStatusContrato.id ="+idStatusContrato;
			}
			Query query = manager.createQuery(sql);
			if(!usuarioBean.getIsAdm()){
				query.setParameter("idFuncionario", ID_FUNCIONARIO);
			}
			List<PmpContrato> contratoList = query.getResultList();
			for (int i = 0; i < contratoList.size(); i++) {
				PmpContrato contrato = contratoList.get(i);
				ContratoComercialBean bean = new ContratoComercialBean();
				bean.fromBean(contrato, usuarioBean);
				bean.setNumeroSerie(contrato.getNumeroSerie());
				bean.setModeloList(this.findAllModelosContrato(contrato.getContExcessao(), contrato.getIdConfigManutencao().getIdFamilia().getId(), isGerador));
				bean.setPrefixoList(this.findAllPrefixosContrato(contrato.getModelo(),contrato.getIdConfigManutencao().getContExcessao()));
				bean.setRangerList(this.findAllRangerContrato(contrato.getModelo(), contrato.getPrefixo(), contrato.getIdConfigManutencao().getContExcessao(), contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getId()));
				bean.setDistanciaGerador(Double.valueOf(contrato.getDistanciaGerador().doubleValue()));

				query = manager.createNativeQuery("SELECT cms.mes, cms.mes_manutencao, cms.standard_job_cptcd, 'true' AS selected, cms.is_executado FROM pmp_cont_meses_standard cms"+
						" WHERE cms.id_contrato = " + contrato.getId() +
						" UNION "+
						" SELECT cms.horas AS mes, cms.horas AS mes_manutencao, cms.standard_job_cptcd, 'false' AS selected, 'N' FROM pmp_config_horas_standard cms " +
						" WHERE cms.id_config_manutencao =" + contrato.getIdConfigManutencao().getId() +
						" AND cms.horas NOT IN (SELECT chs.mes_manutencao FROM pmp_cont_meses_standard chs"+
						" WHERE chs.id_contrato = " + contrato.getId() + ")"+
						" ORDER BY 2");
				
				
				List<ConfigManutencaoMesesBean> mesesList = new ArrayList<ConfigManutencaoMesesBean>();
				List<Object[]> manutMesesList = query.getResultList();
				Long frequencia = 30l;
				for (int j = 0; j < manutMesesList.size(); j++) {
					Object[] objects  = (Object[]) manutMesesList.get(j);
//					if(j == 0){
//						Object[] objectsProximo  = (Object[]) manutMesesList.get(j+1);
//						//frequencia = ((BigDecimal)objectsProximo[1]).longValue() - ((BigDecimal)objects[1]).longValue();
//					}else{
//						Object[] objectsPenultimo  = (Object[]) manutMesesList.get(manutMesesList.size()-2);
//						Object[] objectsUltimo  = (Object[]) manutMesesList.get(manutMesesList.size()-1);
//						//frequencia = ((BigDecimal)objectsUltimo[1]).longValue() - ((BigDecimal)objectsPenultimo[1]).longValue();
//					}
					ConfigManutencaoMesesBean mesesBean = new ConfigManutencaoMesesBean();
					mesesBean.setMeses(((BigDecimal)objects[0]).longValue());
					mesesBean.setMesManutencao(((BigDecimal)objects[1]).longValue());
					mesesBean.setStandardJob((String)objects[2]);
					mesesBean.setIsSelected(Boolean.valueOf((String)objects[3]));
					mesesBean.setIsExecutado((String)objects[4]);
					mesesBean.setFrequencia(frequencia);
					mesesList.add(mesesBean);
				}
				Collections.sort(mesesList);
				bean.setConfigManutencaoMesesBeanList(mesesList);
				result.add(bean);
			}			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		
		return result;
	}
	
	public List<ContratoComercialBean> findAllContratoComercialAVM(String nomeCliente){
		EntityManager manager = null;
		List<ContratoComercialBean> result = new ArrayList<ContratoComercialBean>();
		try {
			manager = JpaUtil.getInstance();
			//String sql = "FROM PmpContrato WHERE razaoSocial LIKE'"+nomeCliente.toUpperCase()+"%'  AND idFuncionario =:idFuncionario and idTipoContrato.sigla NOT IN('REN', 'CAN')";
			//if(usuarioBean.getIsAdm()){
			String sql = "FROM PmpContrato WHERE razaoSocial LIKE'"+nomeCliente.toUpperCase()+"%' AND idTipoContrato.sigla NOT IN ('REN', 'CAN')";
			//}
			
				sql += " AND idTipoContrato.id in (SELECT id FROM PmpTipoContrato WHERE (sigla = 'VEPM' or sigla = 'CON'))";
			
			Query query = manager.createQuery(sql);
/*			if(!usuarioBean.getIsAdm()){
				query.setParameter("idFuncionario", ID_FUNCIONARIO);
			}*/
			List<PmpContrato> contratoList = query.getResultList();
			for (int i = 0; i < contratoList.size(); i++) {
				PmpContrato contrato = contratoList.get(i);
				ContratoComercialBean bean = new ContratoComercialBean();
				
				
				query = manager.createNativeQuery("select f.eplsnm from tw_funcionario f where f.epidno = '"+contrato.getIdFuncionario()+"'"); 
				
				bean.setNomeFuncionario((String) query.getSingleResult());
				
				bean.fromBean(contrato, usuarioBean);
				bean.setNumeroSerie(contrato.getNumeroSerie());
				bean.setModeloList(this.findAllModelosContrato(contrato.getContExcessao(), contrato.getIdConfigManutencao().getIdFamilia().getId(), null));
				bean.setPrefixoList(this.findAllPrefixosContrato(contrato.getModelo(),contrato.getIdConfigManutencao().getContExcessao()));
				bean.setRangerList(this.findAllRangerContrato(contrato.getModelo(), contrato.getPrefixo(), contrato.getIdConfigManutencao().getContExcessao(), contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getId()));
				
				query = manager.createNativeQuery("select hm.horas ,hm.horas_manutencao, hm.standard_job_cptcd, 'true', hm.is_executado from pmp_cont_horas_standard hm"+
						" where hm.id_contrato = "+contrato.getId()+
						" union"+
						" select h.horas,h.horas, h.standard_job_cptcd, 'false', 'N' from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and h.horas not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")"+
						" union"+
						" select h.horas,h.horas + 12000, h.standard_job_cptcd, 'false', 'N' from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 12000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
				" order by 2 ");	
				
				
				List<ConfigManutencaoHorasBean> horasList = new ArrayList<ConfigManutencaoHorasBean>();
				List<Object[]> manutHorasList = query.getResultList();
				Long frequencia = 0l;
				for (int j = 0; j < manutHorasList.size(); j++){ 
					Object[] objects  = (Object[]) manutHorasList.get(j);
					if(j == 0 || j == 1 && manutHorasList.size() > 2){
						Object[] objectsProximo  = (Object[]) manutHorasList.get(j+1);
						frequencia = ((BigDecimal)objectsProximo[1]).longValue() - ((BigDecimal)objects[1]).longValue();
					}else{
						Object[] objectsPenultimo  = (Object[]) manutHorasList.get(manutHorasList.size()-2);
						Object[] objectsUltimo  = (Object[]) manutHorasList.get(manutHorasList.size()-1);
						frequencia = ((BigDecimal)objectsUltimo[1]).longValue() - ((BigDecimal)objectsPenultimo[1]).longValue();
					}
					ConfigManutencaoHorasBean horasBean = new ConfigManutencaoHorasBean();
					horasBean.setHoras(((BigDecimal)objects[0]).longValue());
					horasBean.setHorasManutencao(((BigDecimal)objects[1]).longValue());
					horasBean.setStandardJob((String)objects[2]);
					horasBean.setIsSelected(Boolean.valueOf((String)objects[3]));
					horasBean.setIsExecutado((String)objects[4]);
					horasBean.setFrequencia(frequencia);
					horasList.add(horasBean);
				}
				Collections.sort(horasList);
				bean.setConfigManutencaoHorasBeanList(horasList);
				result.add(bean);
			}			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		
		return result;
	}
	
	public List<ContratoComercialBean> findAllContratoComercialRental(String nomeCliente, Long idStatusContrato, String contExcessao){
		EntityManager manager = null;
		List<ContratoComercialBean> result = new ArrayList<ContratoComercialBean>();
		try {
			manager = JpaUtil.getInstance();
			String sql = "From PmpContrato where (razaoSocial LIKE '" + nomeCliente.toUpperCase() + "%' or numeroSerie LIKE '" + nomeCliente.toUpperCase() + "%' or modelo LIKE '" + nomeCliente.toUpperCase() + "%') and idTipoContrato.sigla = 'REN' and filial =:filial and contExcessao =:contExcessao";
			if(idStatusContrato > 0){
				sql += " and idStatusContrato.id ="+idStatusContrato;
			}
			Query query = manager.createQuery(sql);
			query.setParameter("filial", Integer.valueOf(usuarioBean.getFilial()));
			query.setParameter("contExcessao", contExcessao);
			List<PmpContrato> contratoList = query.getResultList();
			TracaoBusiness business = new TracaoBusiness();
			for (int i = 0; i < contratoList.size(); i++) {
				PmpContrato contrato = contratoList.get(i);
				ContratoComercialBean bean = new ContratoComercialBean();
				bean.fromBean(contrato, usuarioBean);
				bean.setNumeroSerie(contrato.getNumeroSerie());
				bean.setModeloList(this.findAllModelosContrato(contrato.getContExcessao(), contrato.getIdConfigManutencao().getIdFamilia().getId(), "N"));
				bean.setPrefixoList(this.findAllPrefixosContrato(contrato.getModelo(),contrato.getIdConfigManutencao().getContExcessao()));
				bean.setRangerList(this.findAllRangerContrato(contrato.getModelo(), contrato.getPrefixo(), contrato.getIdConfigManutencao().getContExcessao(), contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getId()));
				bean.setTipoCustomizacaoList(this.findAllTipoCustomizacao(contrato.getModelo(), contrato.getIdFamilia(), contrato.getId()));
				if(contrato.getIdConfigTracao() != null){
					query = manager.createNativeQuery("select ID_ARV from ARV_INSPECAO where DESCRICAO =:modelo");
					query.setParameter("modelo", contrato.getModelo());
					BigDecimal idModelo = (BigDecimal)query.getResultList().get(0);
					bean.setConfigTracaoList(business.findAllConfigTracao(idModelo.longValue()));
				}
				
				query = manager.createNativeQuery("select hm.horas ,hm.horas_manutencao, hm.standard_job_cptcd, 'true', hm.is_executado, hm.horas_revisao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato = "+contrato.getId()+
						" union"+
						" select h.horas,h.horas, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and h.horas not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")"+
						" union"+
						" select h.horas,h.horas + 12000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 12000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" union"+
						" select h.horas,h.horas + 24000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 24000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" union"+
						" select h.horas,h.horas + 36000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 36000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" union"+
						" select h.horas,h.horas + 48000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 48000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" order by 2 ");	
				
				
				List<ConfigManutencaoHorasBean> horasList = new ArrayList<ConfigManutencaoHorasBean>();
				List<Object[]> manutHorasList = query.getResultList();
				Long frequencia = 0l;
				for (int j = 0; j < manutHorasList.size(); j++){ 
					Object[] objects  = (Object[]) manutHorasList.get(j);
					if(j == 0 || j == 1 && manutHorasList.size() > 2){
						Object[] objectsProximo  = (Object[]) manutHorasList.get(j+1);
						frequencia = ((BigDecimal)objectsProximo[1]).longValue() - ((BigDecimal)objects[1]).longValue();
					 }else{
						   Object[] objectsPenultimo  = (Object[]) manutHorasList.get(manutHorasList.size()-2);
						   Object[] objectsUltimo  = (Object[]) manutHorasList.get(manutHorasList.size()-1);
						   frequencia = ((BigDecimal)objectsUltimo[1]).longValue() - ((BigDecimal)objectsPenultimo[1]).longValue();
					 }
					if(((BigDecimal)objects[1]).longValue() == 12600 || ((BigDecimal)objects[1]).longValue() == 12400 || ((BigDecimal)objects[1]).longValue() == 12200 || ((BigDecimal)objects[1]).longValue() == 12000 || 
							   ((BigDecimal)objects[1]).longValue() == 24600 || ((BigDecimal)objects[1]).longValue() == 24400 || ((BigDecimal)objects[1]).longValue() == 24200 || ((BigDecimal)objects[1]).longValue() == 24000 || 
							   ((BigDecimal)objects[1]).longValue() == 36600 || ((BigDecimal)objects[1]).longValue() == 36400 || ((BigDecimal)objects[1]).longValue() == 36200 || ((BigDecimal)objects[1]).longValue() == 36000 ||
						   	   ((BigDecimal)objects[1]).longValue() == 48600 || ((BigDecimal)objects[1]).longValue() == 48400 || ((BigDecimal)objects[1]).longValue() == 48200 

						   	|| ((BigDecimal)objects[1]).longValue() == 12800  || 
							   ((BigDecimal)objects[1]).longValue() == 24800 || 
							   ((BigDecimal)objects[1]).longValue() == 36800 || 
						   	   ((BigDecimal)objects[1]).longValue() == 48800 ){
						   int k = j;
						   Object[] objectsProximo  = (Object[]) manutHorasList.get(j+1);
						   while(((BigDecimal)objectsProximo[1]).longValue() != (((BigDecimal)objects[1]).longValue() + frequencia)){
							   manutHorasList.remove(k+1);
							   objectsProximo  = (Object[]) manutHorasList.get(k+1);
						   }
						}
					ConfigManutencaoHorasBean horasBean = new ConfigManutencaoHorasBean();
					horasBean.setHoras(((BigDecimal)objects[0]).longValue());
					horasBean.setHorasManutencao(((BigDecimal)objects[1]).longValue());
					horasBean.setStandardJob((String)objects[2]);
					horasBean.setIsSelected(Boolean.valueOf((String)objects[3]));
					horasBean.setIsExecutado((String)objects[4]);
					horasBean.setFrequencia(frequencia);
					if(objects[5] != null){
						horasBean.setHorasRevisao(((BigDecimal)objects[5]).longValue());
					}
					horasList.add(horasBean);
				}
				Collections.sort(horasList);
				bean.setConfigManutencaoHorasBeanList(horasList);
				result.add(bean);
			}			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		
		return result;
	}
	
	public List<ContratoComercialBean> findAllContratoComercialAntigo(String nomeCliente, Long idStatusContrato, String contExcessao){
		EntityManager manager = null;
		List<ContratoComercialBean> result = new ArrayList<ContratoComercialBean>();
		try {
			manager = JpaUtil.getInstance();
			String sql = "From PmpContrato c, PmpConfigOperacional pco  where (c.razaoSocial LIKE '" + nomeCliente.toUpperCase() + "%' or c.numeroSerie LIKE '" + nomeCliente.toUpperCase() + "%' or c.modelo LIKE '" + nomeCliente.toUpperCase() + "%') and c.id = pco.idContrato.id  and c.filial =:filial and c.idTipoContrato.sigla = 'CAN' and c.contExcessao =:contExcessao";
			if(idStatusContrato > 0){
				sql += " and idStatusContrato.id ="+idStatusContrato;
			}
			Query query = manager.createQuery(sql);
			query.setParameter("filial", Integer.valueOf(usuarioBean.getFilial()));
			query.setParameter("contExcessao", contExcessao);
			List<Object[]> contratoList = query.getResultList();
			TracaoBusiness business = new TracaoBusiness();
			for (int i = 0; i < contratoList.size(); i++) {
				Object [] pair = (Object[])contratoList.get(i);
				PmpContrato contrato = (PmpContrato)pair[0];
				PmpConfigOperacional cop = (PmpConfigOperacional)pair[1];
				ContratoComercialBean bean = new ContratoComercialBean();
				bean.fromBean(contrato, usuarioBean);
				bean.setNumOs(cop.getNumOs());
				bean.setNumeroSerie(contrato.getNumeroSerie());
				bean.setModeloList(this.findAllModelosContrato(contrato.getContExcessao(), contrato.getIdConfigManutencao().getIdFamilia().getId(), "N"));
				bean.setPrefixoList(this.findAllPrefixosContrato(contrato.getModelo(),contrato.getIdConfigManutencao().getContExcessao()));
				bean.setRangerList(this.findAllRangerContrato(contrato.getModelo(), contrato.getPrefixo(), contrato.getIdConfigManutencao().getContExcessao(), contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getId()));
				bean.setTipoCustomizacaoList(this.findAllTipoCustomizacao(contrato.getModelo(), contrato.getIdFamilia(), contrato.getId()));
				
				if(contrato.getIdConfigTracao() != null){
					query = manager.createNativeQuery("select ID_ARV from ARV_INSPECAO where DESCRICAO =:modelo");
					query.setParameter("modelo", contrato.getModelo());
					BigDecimal idModelo = (BigDecimal)query.getResultList().get(0);
					bean.setConfigTracaoList(business.findAllConfigTracao(idModelo.longValue()));
				}
				
				query = manager.createNativeQuery("select hm.horas ,hm.horas_manutencao, hm.standard_job_cptcd, 'true', hm.is_executado, hm.horas_revisao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato = "+contrato.getId()+
						" union"+
						" select h.horas,h.horas, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and h.horas not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")"+
						" union"+
						" select h.horas,h.horas + 12000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 12000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" union"+
						" select h.horas,h.horas + 24000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 24000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" union"+
						" select h.horas,h.horas + 36000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 36000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" union"+
						" select h.horas,h.horas + 48000, h.standard_job_cptcd, 'false', 'N', h.horas_revisao from  pmp_config_horas_standard h " +
						" where h.id_config_manutencao ="+contrato.getIdConfigManutencao().getId()+
						" and (h.horas + 48000) not in (select hm.horas_manutencao from pmp_cont_horas_standard hm"+
						" where hm.id_contrato =  "+contrato.getId()+")" +
						" and h.horas <> 500" +
						" and h.horas <> 250" +
						" and h.horas <> 100" +
						" order by 2 ");	
				
				
				List<ConfigManutencaoHorasBean> horasList = new ArrayList<ConfigManutencaoHorasBean>();
				List<Object[]> manutHorasList = query.getResultList();
				Long frequencia = 0l;
				for (int j = 0; j < manutHorasList.size(); j++){ 
					Object[] objects  = (Object[]) manutHorasList.get(j);
					if(j == 0 || j == 1 && manutHorasList.size() > 2){
						Object[] objectsProximo  = (Object[]) manutHorasList.get(j+1);
						frequencia = ((BigDecimal)objectsProximo[1]).longValue() - ((BigDecimal)objects[1]).longValue();
					 }else{
						   Object[] objectsPenultimo  = (Object[]) manutHorasList.get(manutHorasList.size()-2);
						   Object[] objectsUltimo  = (Object[]) manutHorasList.get(manutHorasList.size()-1);
						   frequencia = ((BigDecimal)objectsUltimo[1]).longValue() - ((BigDecimal)objectsPenultimo[1]).longValue();
					 }
					if(((BigDecimal)objects[1]).longValue() == 12600 || ((BigDecimal)objects[1]).longValue() == 12400 || ((BigDecimal)objects[1]).longValue() == 12200 || ((BigDecimal)objects[1]).longValue() == 12000 || 
							   ((BigDecimal)objects[1]).longValue() == 24600 || ((BigDecimal)objects[1]).longValue() == 24400 || ((BigDecimal)objects[1]).longValue() == 24200 || ((BigDecimal)objects[1]).longValue() == 24000 || 
							   ((BigDecimal)objects[1]).longValue() == 36600 || ((BigDecimal)objects[1]).longValue() == 36400 || ((BigDecimal)objects[1]).longValue() == 36200 || ((BigDecimal)objects[1]).longValue() == 36000 ||
						   	   ((BigDecimal)objects[1]).longValue() == 48600 || ((BigDecimal)objects[1]).longValue() == 48400 || ((BigDecimal)objects[1]).longValue() == 48200 
						   	   
						   	|| ((BigDecimal)objects[1]).longValue() == 12800  || 
							   ((BigDecimal)objects[1]).longValue() == 24800 || 
							   ((BigDecimal)objects[1]).longValue() == 36800 || 
						   	   ((BigDecimal)objects[1]).longValue() == 48800 ){
						   int k = j;
						   Object[] objectsProximo  = (Object[]) manutHorasList.get(j+1);
						   while(((BigDecimal)objectsProximo[1]).longValue() != (((BigDecimal)objects[1]).longValue() + frequencia)){
							   manutHorasList.remove(k+1);
							   objectsProximo  = (Object[]) manutHorasList.get(k+1);
						   }
						}
					ConfigManutencaoHorasBean horasBean = new ConfigManutencaoHorasBean();
					horasBean.setHoras(((BigDecimal)objects[0]).longValue());
					horasBean.setHorasManutencao(((BigDecimal)objects[1]).longValue());
					horasBean.setStandardJob((String)objects[2]);
					horasBean.setIsSelected(Boolean.valueOf((String)objects[3]));
					horasBean.setIsExecutado((String)objects[4]);
					horasBean.setFrequencia(frequencia);
					if(objects[5] != null){
						horasBean.setHorasRevisao(((BigDecimal)objects[5]).longValue());
					}
					horasList.add(horasBean);
				}
				Collections.sort(horasList);
				bean.setConfigManutencaoHorasBeanList(horasList);
				result.add(bean);
			}			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		
		return result;
	}

	public List<PrecoBean> findAllParcelas(Long idContrato){

		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			//Recupera o tipo de customização
			Query query = manager.createNativeQuery("select ID_TIPO_CUSTOMIZACAO from PMP_CONTRATO_CUSTOMIZACAO where id_contrato =:id_contrato");
			query.setParameter("id_contrato", contrato.getId());
			List<BigDecimal> contratoCustList =  query.getResultList();
			String siglaCustomizacao = "";
			
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
			if(contrato != null){
				if(!contrato.getIdTipoContrato().getSigla().equals("REN")){
					if(contrato.getBgrp().equals("PM") || contrato.getBgrp().equals("KIT3")){
						if("PRE".equals(contrato.getIdClassificacaoContrato().getSigla())){
							result = this.findAllParcelasPMPKIT3(idContrato, siglaCustomizacao);
						} else if("CUS".equals(contrato.getIdClassificacaoContrato().getSigla())){
							result = this.findAllParcelasPMPCustomer(idContrato, siglaCustomizacao);
						} else if("PART".equals(contrato.getIdClassificacaoContrato().getSigla())){
							result = this.findAllParcelasPMPPartner(idContrato, siglaCustomizacao);
						} else if("CUSLIGHT".equals(contrato.getIdClassificacaoContrato().getSigla())){
							result = this.findAllParcelasPMPCustomerLight(idContrato, siglaCustomizacao);
						}
						//					}else if(contrato.getBgrp().equals("KIT2")){
						//						result = this.findAllParcelasKIT2(idContrato);
					}else if(contrato.getBgrp().equals("SEV")){
						if("PRE".equals(contrato.getIdClassificacaoContrato().getSigla())){
							result = this.findAllParcelasPMPKIT3(idContrato, siglaCustomizacao);
						} else if("CUS".equals(contrato.getIdClassificacaoContrato().getSigla())){
							result = this.findAllParcelasPMPCustomer(idContrato, siglaCustomizacao);
						} else if("PART".equals(contrato.getIdClassificacaoContrato().getSigla())){
							result = this.findAllParcelasPMPPartner(idContrato, siglaCustomizacao);
						} else if("CUSLIGHT".equals(contrato.getIdClassificacaoContrato().getSigla())){
							result = this.findAllParcelasPMPCustomerLight(idContrato, siglaCustomizacao);
						}
					}
					//				}else if (!contrato.getIdTipoContrato().getSigla().equals("REN")){
					//					if(contrato.getBgrp().equals("PM") || contrato.getBgrp().equals("KIT3")){
					//						result = this.findAllParcelasPMPKIT3Custo(idContrato);
					//					}
					//					}else if(contrato.getBgrp().equals("KIT2")){
					//						result = this.findAllParcelasKIT2Custo(idContrato);
					//					}
				}else{
					if(contrato.getBgrp().equals("PM") || contrato.getBgrp().equals("KIT3")){
						result = this.findAllParcelasRentalCustoPMKIT3(idContrato, siglaCustomizacao);
					}
//					else if(contrato.getBgrp().equals("KIT2")){
//						result = this.findAllParcelasRentalKIT2Custo(idContrato);
//					}
				}
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
	
	public List<PrecoBean> findAllParcelasSubstituicaoTributaria(Long idContrato){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			if(contrato != null){
				if(!contrato.getIdTipoContrato().getSigla().equals("REN")){
					if(contrato.getBgrp().equals("PM") || contrato.getBgrp().equals("KIT3")){
						result = this.findAllParcelasSubstituicaoTributariaPMPKIT3(idContrato);
//					}else if(contrato.getBgrp().equals("KIT2")){
//						result = this.findAllParcelasKIT2(idContrato);
					}else if(contrato.getBgrp().equals("SEV")){
						result = this.findAllParcelasSubstituicaoTributariaPMPKIT3(idContrato);
					}
//				}else if (!contrato.getIdTipoContrato().getSigla().equals("REN")){
//					if(contrato.getBgrp().equals("PM") || contrato.getBgrp().equals("KIT3")){
//						result = this.findAllParcelasPMPKIT3Custo(idContrato);
//					}
//					}else if(contrato.getBgrp().equals("KIT2")){
//						result = this.findAllParcelasKIT2Custo(idContrato);
//					}
				}else{
					if(contrato.getBgrp().equals("PM") || contrato.getBgrp().equals("KIT3")){
						contrato = manager.find(PmpContrato.class, idContrato);
						//Recupera o tipo de customização
						Query query = manager.createNativeQuery("select ID_TIPO_CUSTOMIZACAO from PMP_CONTRATO_CUSTOMIZACAO where id_contrato =:id_contrato");
						query.setParameter("id_contrato", contrato.getId());
						List<BigDecimal> contratoCustList =  query.getResultList();
						String siglaCustomizacao = "";
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
						result = this.findAllParcelasRentalCustoPMKIT3(idContrato, siglaCustomizacao);
					}
					//					else if(contrato.getBgrp().equals("KIT2")){
					//						result = this.findAllParcelasRentalKIT2Custo(idContrato);
					//					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	public List<PrecoBean> findAllParcelasGerador(Long idContrato){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			if(contrato != null){
				if(!contrato.getIdTipoContrato().getSigla().equals("REN")){
					if(contrato.getBgrp().equals("PM") || contrato.getBgrp().equals("KIT3")){
						result = this.findAllParcelasPMPKIT3Gerador(idContrato);
//					}else if(contrato.getBgrp().equals("KIT2")){
//						result = this.findAllParcelasKIT2(idContrato);
					}else if(contrato.getBgrp().equals("SEV")){
						result = this.findAllParcelasPMPKIT3Gerador(idContrato);
					}
//				}else if (!contrato.getIdTipoContrato().getSigla().equals("REN")){
//					if(contrato.getBgrp().equals("PM") || contrato.getBgrp().equals("KIT3")){
//						result = this.findAllParcelasPMPKIT3Custo(idContrato);
//					}
//					}else if(contrato.getBgrp().equals("KIT2")){
//						result = this.findAllParcelasKIT2Custo(idContrato);
//					}
				}else{
					if(contrato.getBgrp().equals("PM") || contrato.getBgrp().equals("KIT3")){
						result = this.findAllParcelasRentalCustoPMKIT3Gerador(idContrato);
					}
//					else if(contrato.getBgrp().equals("KIT2")){
//						result = this.findAllParcelasRentalKIT2Custo(idContrato);
//					}
				}
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

	public List<PrecoBean> findAllParcelasPMPKIT3(Long idContrato, String siglaCustomizacao){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		BigDecimal totalHHManutencao = BigDecimal.ZERO;
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			
			String complemento = "";
			String complementoSigla = "";
			if(contrato.getIdConfigTracao() != null){
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ocptmd is null)";
				complemento += " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or JWKAPP is null)"
							+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ojbloc is null)";
			}
			
			if(siglaCustomizacao.length() > 0){
				complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and JWKAPP not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
			}
			
			//Long frequencia = 0l;
			
				//BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
				Query query = manager.createQuery("From PmpContHorasStandard where idContrato.id = :id order by horasManutencao");
				query.setParameter("id", contrato.getId());
				List<PmpContHorasStandard> horasStandardList = query.getResultList();
				for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
					totalHorasManutencao += pmpContHorasStandard.getFrequencia();
				}
				int quantidadeManutencoes = 0;
				for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
					quantidadeManutencoes++;
					//frequencia = pmpContHorasStandard.getFrequencia();
					//Query para peças com desconto PDP
					query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
							" and m.bgrp = '"+contrato.getBgrp()+"'"+
							" and m.sos <> '400'"+
							" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
							complemento + complementoSigla +
					" and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
					custoPecas = (BigDecimal)query.getSingleResult();
					if(custoPecas != null){
						Double custoTotal = custoPecas.doubleValue();
						//custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
						if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
							custoTotal = custoTotal - ((custoTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
						}else if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
							//custoTotal = custoTotal - ((custoTotal * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getPdpSpot().doubleValue())/100);
							custoTotal = custoTotal - ((custoTotal * contrato.getDescontoPdpSpot().doubleValue())/100);
						}
						custoManutencao = custoManutencao + custoTotal;
					}
					
					//Query sem desconto pdp e sem desconto de peças com SOS 400
					query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'" +
							" and m.sos = '400'"+
							" and m.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
							complemento + complementoSigla);
					custoPecas = (BigDecimal)query.getSingleResult();
					//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
					if(custoPecas != null){
						custoManutencao = custoManutencao + custoPecas.doubleValue();
					}
					
					
					//Query sem desconto pdp
					query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
							" and m.sos <> '000'"+
							" and m.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
							complemento + complementoSigla +
					" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
							" or m.bectyc is null)");
					custoPecas = (BigDecimal)query.getSingleResult();
					//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
					if(custoPecas != null){
						custoManutencao = custoManutencao + custoPecas.doubleValue();
					}
					//Query sem desconto pdp e com desconto de peças com SOS = 000
					query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'" +
							" and m.sos = '000'"+
							" and m.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
							complemento + complementoSigla +
					" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
							" or m.bectyc is null)");
					custoPecas = (BigDecimal)query.getSingleResult();
					//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
					
					if(custoPecas != null){
						if(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas() != null && contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue() > 0){
							Double custoTotal = custoPecas.doubleValue();
							if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
								custoTotal = custoTotal - ((custoTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue()))/100);
							}
							custoManutencao = custoManutencao + custoTotal;
						}else{
							custoManutencao = custoManutencao + custoPecas.doubleValue();
						}
					}
					query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
							" where h.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
							" and h.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
					totalHHManutencao = totalHHManutencao.add((BigDecimal)query.getSingleResult());
					
					if(contrato.getIdTipoContrato().getSigla().equals("VEN") && contrato.getIdStatusContrato().getSigla().equals("CA")){//contrato pos pago e aberto para na revisão que o consultor marcou
						if(contrato.getPrintRevisaoPosPago() != null  && contrato.getPrintRevisaoPosPago().longValue() <= pmpContHorasStandard.getHorasManutencao().longValue()){
							break;
						}
							
					}
					
				}
				//int totalManutencoes = 0;
				
				//totalHorasManutencao = frequencia * horasStandardList.size();
				
				//custo do TA
				if(contrato.getTa().equals("S")){
					Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
					taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
					custoManutencao = custoManutencao + taCusto;
				}
				
				
				//total horas para a manutenção
				//corrigir o erro das horas
//				query = manager.createNativeQuery("select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId());
//				List<String> cptcdList = query.getResultList();
//				BigDecimal totalHHManutencao = BigDecimal.ZERO;
//				for (String cptcd : cptcdList) {
//
//					query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//							" where h.cptcd = '"+cptcd+"'"+
//							" and h.bgrp = '"+contrato.getBgrp()+"'"+
//							" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//							" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//					totalHHManutencao = totalHHManutencao.add((BigDecimal)query.getSingleResult());
//				}
				if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
					Double valorHH = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue();
					valorHH = valorHH + ( (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorSpot().doubleValue() * valorHH) /100 );
					//standard.setCustoMo((BigDecimal.valueOf(valorHH)).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())));
					custoManutencao = custoManutencao + valorHH;//valor de hh
					//custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmpSpot().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
					String arraySpot []= contrato.getQtdHorasDeslSpot().split(":");
					BigDecimal minSpot = BigDecimal.valueOf((Double.valueOf(arraySpot[1])/60));
					BigDecimal horasPdpSpot = BigDecimal.valueOf(Double.valueOf(arraySpot[0]));
					horasPdpSpot = horasPdpSpot.add(minSpot);
					custoManutencao = custoManutencao + ((horasPdpSpot.doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHorasDesSpot().doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
				}else{
					custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
					custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
				}
				
				//Contrato pré-pago da desconto
				if(contrato.getIdTipoContrato().getSigla().equals("VPG") && (contrato.getIsSpot() == null || contrato.getIsSpot().equals("N"))){
					custoManutencao = custoManutencao - ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescontoPrePago().doubleValue())/100);
				}
				PrecoBean bean = new PrecoBean();
//				bean.setParcela(1);
//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
//				result.add(bean);
				PrecoBean precoConcessaoBean = this.findPrecoConcessao(idContrato);
				for(int i = 0; i < 12; i++){
					//custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
					bean = new PrecoBean();
					bean.setParcela(i+1);
					if(i == 0){
						bean.setPrecoConcessao(precoConcessaoBean.getPrecoConcessao());
					}
					bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
					result.add(bean);
					
				}
				return result;
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	public List<PrecoBean> findAllParcelasPMPCustomer(Long idContrato, String siglaCustomizacao){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		//BigDecimal totalHHManutencao = BigDecimal.ZERO;
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			String complemento = "";
			String complementoSigla = "";
			if(contrato.getIdConfigTracao() != null){
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ocptmd is null)";
				complemento += " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or JWKAPP is null)"
				+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ojbloc is null)";
			}
			if(siglaCustomizacao.length() > 0){
				complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and JWKAPP not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
			}
			//Long frequencia = 0l;
			
			//BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager.createQuery("From PmpContHorasStandard where idContrato.id = :id order by horasManutencao");
			query.setParameter("id", contrato.getId());
			List<PmpContHorasStandard> horasStandardList = query.getResultList();
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				totalHorasManutencao += pmpContHorasStandard.getFrequencia();
			}
			int quantidadeManutencoes = 0;
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				quantidadeManutencoes++;
				//frequencia = pmpContHorasStandard.getFrequencia();
				//Query para peças com desconto PDP
		
				
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and m.OJBLOC <> 'CST'"+
						" and m.sos <> '400'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla+
				" and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");		
				
				
				//" and m.stno ="+Integer.valueOf(FILIAL));
				custoPecas = (BigDecimal)query.getSingleResult();
				
				
				if(custoPecas != null){
					Double custoTotal = custoPecas.doubleValue();
					//custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
					if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
						custoTotal = custoTotal - ((custoTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
					}else if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
						//custoTotal = custoTotal - ((custoTotal * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getPdpSpot().doubleValue())/100);
						custoTotal = custoTotal - ((custoTotal * contrato.getDescontoPdpSpot().doubleValue())/100);
					}
					custoManutencao = custoManutencao + custoTotal;
				}
				
				//Query sem desconto pdp
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
						" and m.sos <> '000'"+
						" and m.OJBLOC <> 'CST'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla +
						" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
				" or m.bectyc is null)");
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
				if(custoPecas != null){
					custoManutencao = custoManutencao + custoPecas.doubleValue();
				}
				
				
				//Query sem desconto pdp e sem desconto de peças com SOS 400
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'" +
						" and m.sos = '400'"+
						" and m.OJBLOC <> 'CST'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla);
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
				if(custoPecas != null){
					custoManutencao = custoManutencao + custoPecas.doubleValue();
				}
				
				//Query sem desconto pdp e com desconto de peças com SOS = 000
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'" +
						" and m.sos = '000'"+
						" and m.OJBLOC <> 'CST'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla +
				" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
						" or m.bectyc is null)");
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
				if(custoPecas != null){
					if(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas() != null && contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue() > 0){
						Double custoTotal = custoPecas.doubleValue();
						if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
							custoTotal = custoTotal - ((custoTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue()))/100);
						}
						custoManutencao = custoManutencao + custoTotal;
					}else{
						custoManutencao = custoManutencao + custoPecas.doubleValue();
					}
				}
				
				//Double custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
				//custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
				
//					//Query sem desconto pdp
//					query = manager.createNativeQuery("select sum(m.unls) from pmp_manutencao_preco_custo m"+
//							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
//							" and m.bgrp = '"+contrato.getBgrp()+"'"+
//							" and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//							" and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
//					" and m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)");
//					custoPecas = (BigDecimal)query.getSingleResult();
//					custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
//					custoManutencao = custoManutencao + custoNordesteTotal;
				
				//total horas para a manutenção
//				query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//						" where h.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
//						" and h.bgrp = '"+contrato.getBgrp()+"'"+
//						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//				totalHHManutencao = totalHHManutencao.add((BigDecimal)query.getSingleResult());
//				
				custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getMonitoramento().doubleValue();
				if(pmpContHorasStandard.getIsTa() != null && "S".equals(pmpContHorasStandard.getIsTa())){
					custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorTa().doubleValue() + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getSos().doubleValue();
				}
				
				if(contrato.getIdTipoContrato().getSigla().equals("VPG") && (contrato.getIsSpot() == null || contrato.getIsSpot().equals("N"))){
					custoManutencao = custoManutencao - ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescontoPrePago().doubleValue())/100);
				}

				if(contrato.getIdTipoContrato().getSigla().equals("VEN") && contrato.getIdStatusContrato().getSigla().equals("CA")){//contrato pos pago e aberto para na revisão que o consultor marcou
					if(contrato.getPrintRevisaoPosPago() != null  && contrato.getPrintRevisaoPosPago().longValue() <= pmpContHorasStandard.getHorasManutencao().longValue()){
						break;
					}
					
				}
			}
			//int totalManutencoes = 0;
			
			//totalHorasManutencao = frequencia * horasStandardList.size();
			
			//custo do TA
//			if(contrato.getTa().equals("S")){
//				Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhTaCusto().doubleValue());
//				taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
//				custoManutencao = custoManutencao + taCusto;
//			}
			
			
			//total horas para a manutenção
//				query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//						" where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId()+")"+
//						" and h.bgrp = '"+contrato.getBgrp()+"'"+
//						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
			
//			custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhPmpCusto().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
//			custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmpCusto().doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
			
			PrecoBean bean = new PrecoBean();
//				bean.setParcela(1);
//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
//				result.add(bean);
			//for(int i = 0; i < 10; i++){
			//custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
			bean = new PrecoBean();
			bean.setParcela(1);
			bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
			result.add(bean);
			
			//}
			return result;
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	public List<PrecoBean> findAllParcelasPMPCustomerLight(Long idContrato, String siglaCustomizacao){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		//BigDecimal totalHHManutencao = BigDecimal.ZERO;
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			
			String complemento = "";
			String complementoSigla = "";
			if(contrato.getIdConfigTracao() != null){
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ocptmd is null)";
				complemento += " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or JWKAPP is null)"
						+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ojbloc is null)";
			}
			
			if(siglaCustomizacao.length() > 0){
				complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and JWKAPP not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
			}
			
			//Long frequencia = 0l;
			
			//BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager.createQuery("From PmpContHorasStandard where idContrato.id = :id order by horasManutencao");
			query.setParameter("id", contrato.getId());
			List<PmpContHorasStandard> horasStandardList = query.getResultList();
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				totalHorasManutencao += pmpContHorasStandard.getFrequencia();
			}
			int quantidadeManutencoes = 0;
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				quantidadeManutencoes++;
				//frequencia = pmpContHorasStandard.getFrequencia();
				//Query para peças com desconto PDP
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and m.sos <> '050' "+
						" and m.sos <> '400' "+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla+
						" and m.OJBLOC <> 'CST'"+
						" and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
				custoPecas = (BigDecimal)query.getSingleResult();
				if(custoPecas != null){
					Double custoTotal = custoPecas.doubleValue();
					//custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
					if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
						custoTotal = custoTotal - ((custoTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
					}else if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
						//custoTotal = custoTotal - ((custoTotal * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getPdpSpot().doubleValue())/100);
						custoTotal = custoTotal - ((custoTotal * contrato.getDescontoPdpSpot().doubleValue())/100);
					}
					custoManutencao = custoManutencao + custoTotal;
				}
				
				//Query sem desconto pdp e sem desconto de peças com SOS 400
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'" +
						" and m.sos = '400'"+
						" and m.OJBLOC <> 'CST'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla);
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
				if(custoPecas != null){
					custoManutencao = custoManutencao + custoPecas.doubleValue();
				}
				
				
				//Query sem desconto pdp
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
						" and m.sos <> '000'"+
						" and m.sos <> '400'"+
						" and m.sos <> '050' "+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla +
						" and m.OJBLOC <> 'CST'"+
						" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
						" or m.bectyc is null)");
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
				if(custoPecas != null){
					custoManutencao = custoManutencao + custoPecas.doubleValue();
				}
				
				//Query sem desconto pdp e com desconto de peças com SOS = 000
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'" +
						" and m.sos = '000'"+
						" and m.sos <> '050' "+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla +
						" and m.OJBLOC <> 'CST'"+
						" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
						" or m.bectyc is null)");
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
				if(custoPecas != null){
					if(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas() != null && contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue() > 0){
						Double custoTotal = custoPecas.doubleValue();
						if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
							custoTotal = custoTotal - ((custoTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue()))/100);
						}
						custoManutencao = custoManutencao + custoTotal;
					}else{
						custoManutencao = custoManutencao + custoPecas.doubleValue();
					}
				}
				
				
				
//				
				
				//total horas para a manutenção
//				query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//						" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
//						" and h.bgrp = '"+contrato.getBgrp()+"'"+
//						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
//				standard.setCustoMo((BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue())).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())));
//				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
//				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue());//valor de km vezes a quantidade de manutenções
				
				if(contrato.getIdTipoContrato().getSigla().equals("VPG") && (contrato.getIsSpot() == null || contrato.getIsSpot().equals("N"))){
					custoManutencao = custoManutencao - ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescontoPrePago().doubleValue())/100);
				}
				custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getMonitoramento().doubleValue();
				if(pmpContHorasStandard.getIsTa() != null && "S".equals(pmpContHorasStandard.getIsTa())){
					custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorTa().doubleValue() + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getSos().doubleValue();
				}
				
				if(contrato.getIdTipoContrato().getSigla().equals("VEN") && contrato.getIdStatusContrato().getSigla().equals("CA")){//contrato pos pago e aberto para na revisão que o consultor marcou
					if(contrato.getPrintRevisaoPosPago() != null  && contrato.getPrintRevisaoPosPago().longValue() <= pmpContHorasStandard.getHorasManutencao().longValue()){
						break;
					}
					
				}
			}
			PrecoBean bean = new PrecoBean();
//				bean.setParcela(1);
//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
//				result.add(bean);
			PrecoBean precoConcessaoBean = this.findPrecoConcessaoCustomer(idContrato);
			for(int i = 0; i < 12; i++){
				//custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
				bean = new PrecoBean();
				bean.setParcela(i+1);
				if(i == 0){
					bean.setPrecoConcessao(precoConcessaoBean.getPrecoConcessao());
				}
				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
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
	
	public List<PrecoBean> findAllParcelasPMPPartner(Long idContrato, String siglaCustomizacao){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		BigDecimal totalHHManutencao = BigDecimal.ZERO;
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			
			String complemento = "";
			String complementoSigla = "";
			if(contrato.getIdConfigTracao() != null){
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ocptmd is null)";
				complemento += " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or JWKAPP is null)"
				+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ojbloc is null)";
			}
			
			if(siglaCustomizacao.length() > 0){
				complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and JWKAPP not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
			}
			
			//Long frequencia = 0l;
			
			//BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager.createQuery("From PmpContHorasStandard where idContrato.id = :id order by horasManutencao");
			query.setParameter("id", contrato.getId());
			List<PmpContHorasStandard> horasStandardList = query.getResultList();
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				totalHorasManutencao += pmpContHorasStandard.getFrequencia();
			}
			int quantidadeManutencoes = 0;
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				String retirarPeca = "";
				if(!pmpContHorasStandard.getIsPartner().equals("N")){
					query = manager.createNativeQuery("select descricao from Pmp_Comp_Code_Partner");
					List<String> descricao = (List<String>)query.getResultList();
					for (String cptcd : descricao) {
						if(pmpContHorasStandard.getStandardJobCptcd().equals(cptcd)){
							retirarPeca = " and m.OJBLOC <> 'CST'";
						}
					}
				}
				//frequencia = pmpContHorasStandard.getFrequencia();
				//Query para peças com desconto PDP
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and m.sos <> '400'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla + retirarPeca+
				" and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
				custoPecas = (BigDecimal)query.getSingleResult();
				if(custoPecas != null){
					Double custoTotal = custoPecas.doubleValue();
					//custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
					if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
						custoTotal = custoTotal - ((custoTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
					}else if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
						//custoTotal = custoTotal - ((custoTotal * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getPdpSpot().doubleValue())/100);
						custoTotal = custoTotal - ((custoTotal * contrato.getDescontoPdpSpot().doubleValue())/100);
					}
					custoManutencao = custoManutencao + custoTotal;
				}
				
				//Query sem desconto pdp e sem desconto de peças com SOS 400
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'" +
						" and m.sos = '400'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla+retirarPeca);
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
				if(custoPecas != null){
					custoManutencao = custoManutencao + custoPecas.doubleValue();
				}
				
				//Query sem desconto pdp
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
						" and m.sos <> '000'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla + retirarPeca+
						" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
				" or m.bectyc is null)");
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
				if(custoPecas != null){
					custoManutencao = custoManutencao + custoPecas.doubleValue();
				}
				
				//Query sem desconto pdp e com desconto de peças com SOS = 000
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'" +
						" and m.sos = '000'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						complemento + complementoSigla + retirarPeca+
				" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
						" or m.bectyc is null)");
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
				if(custoPecas != null){
					if(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas() != null && contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue() > 0){
						Double custoTotal = custoPecas.doubleValue();
						if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
							custoTotal = custoTotal - ((custoTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPecas().doubleValue()))/100);//desconto peças
						}
						custoManutencao = custoManutencao + custoTotal;
					}else{
						custoManutencao = custoManutencao + custoPecas.doubleValue();
					}
				}

				if((pmpContHorasStandard.getIsPartner() != null && "N".equals(pmpContHorasStandard.getIsPartner())) || ((pmpContHorasStandard.getStandardJobCptcd().equals("752A") || pmpContHorasStandard.getStandardJobCptcd().equals("752B")) && (pmpContHorasStandard.getIdContrato().getIdFamilia() == 41 ||  pmpContHorasStandard.getIdContrato().getIdFamilia() == 84))){	
					query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
							" where h.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
							" and h.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
					totalHHManutencao = totalHHManutencao.add((BigDecimal)query.getSingleResult());
					quantidadeManutencoes++;
				}
				
//				custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getMonitoramento().doubleValue();
//				if(pmpContHorasStandard.getIsTa() != null && "S".equals(pmpContHorasStandard.getIsTa())){
//					custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorTa().doubleValue() + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getSos().doubleValue();
//				}
				
				if(contrato.getIdTipoContrato().getSigla().equals("VEN") && contrato.getIdStatusContrato().getSigla().equals("CA")){//contrato pos pago e aberto para na revisão que o consultor marcou
					if(contrato.getPrintRevisaoPosPago() != null  && contrato.getPrintRevisaoPosPago().longValue() <= pmpContHorasStandard.getHorasManutencao().longValue()){
						break;
					}
					
				}
				
			}
			//int totalManutencoes = 0;
			
			//totalHorasManutencao = frequencia * horasStandardList.size();
			
			//custo do TA
//			if(contrato.getTa().equals("S")){
//				Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
//				taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
//				custoManutencao = custoManutencao + taCusto;
//			}
//			
			
			//total horas para a manutenção
			//corrigir o erro das horas
//				query = manager.createNativeQuery("select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId());
//				List<String> cptcdList = query.getResultList();
//				BigDecimal totalHHManutencao = BigDecimal.ZERO;
//				for (String cptcd : cptcdList) {
//
//					query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//							" where h.cptcd = '"+cptcd+"'"+
//							" and h.bgrp = '"+contrato.getBgrp()+"'"+
//							" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//							" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//					totalHHManutencao = totalHHManutencao.add((BigDecimal)query.getSingleResult());
//				}
			if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
				Double valorHH = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue();
				valorHH = valorHH + ( (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorSpot().doubleValue() * valorHH) /100 );
				//standard.setCustoMo((BigDecimal.valueOf(valorHH)).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())));
				custoManutencao = custoManutencao + valorHH;//valor de hh
				//custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmpSpot().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
				String arraySpot []= contrato.getQtdHorasDeslSpot().split(":");
				BigDecimal minSpot = BigDecimal.valueOf((Double.valueOf(arraySpot[1])/60));
				BigDecimal horasPdpSpot = BigDecimal.valueOf(Double.valueOf(arraySpot[0]));
				horasPdpSpot = horasPdpSpot.add(minSpot);
				custoManutencao = custoManutencao + ((horasPdpSpot.doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHorasDesSpot().doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
			}else{
				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
				custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
			}
			
			//Contrato pré-pago da desconto
			if(contrato.getIdTipoContrato().getSigla().equals("VPG") && (contrato.getIsSpot() == null || contrato.getIsSpot().equals("N"))){
				custoManutencao = custoManutencao - ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescontoPrePago().doubleValue())/100);
			}
			PrecoBean bean = new PrecoBean();
//				bean.setParcela(1);
//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
//				result.add(bean);
			PrecoBean precoConcessaoBean = this.findPrecoConcessaoCustomer(idContrato);
			for(int i = 0; i < 12; i++){
				//custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
				bean = new PrecoBean();
				bean.setParcela(i+1);
				if(i == 0){
					bean.setPrecoConcessao(precoConcessaoBean.getPrecoConcessao());
				}
				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
				result.add(bean);
				
			}
			return result;
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	public List<PrecoBean> findAllParcelasSubstituicaoTributariaPMPKIT3(Long idContrato){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			
//			Query query = manager.createQuery("From PmpConfigOperacional where idContrato.id = :id");
//			query.setParameter("id", contrato.getId());
//			PmpConfigOperacional configOperacional = (PmpConfigOperacional)query.getSingleResult();
			//Long frequencia = 0l;
			
			//BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager.createQuery("From PmpContHorasStandard where idContrato.id = :id");
			query.setParameter("id", contrato.getId());
			List<PmpContHorasStandard> horasStandardList = query.getResultList();
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				totalHorasManutencao += pmpContHorasStandard.getFrequencia();
			}
			//for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				//frequencia = pmpContHorasStandard.getFrequencia();
				//Query para peças com desconto PDP
				query = manager.createNativeQuery("select sum(m.VLSUB) from PMP_PECAS_CONFIG_OPERACIONAL m"+
						" where ID_CONTRATO = "+contrato.getId()+
				" and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
				custoPecas = (BigDecimal)query.getSingleResult();
				Double custoPecasTotal = 0.0;
				if(custoPecas != null){
					//custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
					custoPecasTotal = custoPecas.doubleValue();
					if(contrato.getIsSpot() == null || contrato.getIsSpot().equals("N")){
						custoPecasTotal = custoPecasTotal - ((custoPecasTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
					}else if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
						//custoPecasTotal = custoPecasTotal - ((custoPecasTotal * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getPdpSpot().doubleValue())/100);
						custoPecasTotal = custoPecasTotal - ((custoPecasTotal * contrato.getDescontoPdpSpot().doubleValue())/100);
					}
					custoManutencao = custoManutencao + custoPecasTotal;
				}
				//Query sem desconto pdp
				query = manager.createNativeQuery("select sum(m.VLSUB) from PMP_PECAS_CONFIG_OPERACIONAL m"+
						" where ID_CONTRATO = "+contrato.getId()+
						" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
				" or m.bectyc is null)");
				custoPecas = (BigDecimal)query.getSingleResult();
				//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
				custoManutencao = custoManutencao + custoPecas.doubleValue();
			//}
			//int totalManutencoes = 0;
			
			//totalHorasManutencao = frequencia * horasStandardList.size();
			
			//custo do TA
			if(contrato.getTa().equals("S")){
				Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
				taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
				custoManutencao = custoManutencao + taCusto;
			}
			
			
			//total horas para a manutenção
			query = manager.createNativeQuery("select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId());
			List<String> cptcdList = query.getResultList();
			BigDecimal totalHHManutencao = BigDecimal.ZERO;
			for (String cptcd : cptcdList) {

				query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
						" where h.cptcd = '"+cptcd+"'"+
						" and h.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				totalHHManutencao = totalHHManutencao.add((BigDecimal)query.getSingleResult());
			}
			if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
				Double valorHH = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue();
				valorHH = valorHH + ( (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorSpot().doubleValue() * valorHH) /100 );
				//standard.setCustoMo((BigDecimal.valueOf(valorHH)).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())));
				custoManutencao = custoManutencao + valorHH;//valor de hh
				//custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmpSpot().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())*horasStandardList.size());//valor de km vezes a quantidade de manutenções
				
				String arraySpot []= contrato.getQtdHorasDeslSpot().split(":");
				BigDecimal minSpot = BigDecimal.valueOf((Double.valueOf(arraySpot[1])/60));
				BigDecimal horasPdpSpot = BigDecimal.valueOf(Double.valueOf(arraySpot[0]));
				horasPdpSpot = horasPdpSpot.add(minSpot);
				custoManutencao = custoManutencao + ((horasPdpSpot.doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHorasDesSpot().doubleValue())*horasStandardList.size());//valor de km vezes a quantidade de manutenções
				
			}else{
				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
				custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())*horasStandardList.size());//valor de km vezes a quantidade de manutenções
			}
			
			if(contrato.getIdTipoContrato().getSigla().equals("VPG") && (contrato.getIsSpot() == null || contrato.getIsSpot().equals("N"))){
				custoManutencao = custoManutencao - ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescontoPrePago().doubleValue())/100);
			}
			PrecoBean bean = new PrecoBean();
//				bean.setParcela(1);
//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
//				result.add(bean);
			PrecoBean precoConcessaoBean = this.findPrecoConcessao(idContrato);
			for(int i = 0; i < 10; i++){
				//custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
				bean = new PrecoBean();
				bean.setParcela(i+1);
				if(i == 0){
					bean.setPrecoConcessao(precoConcessaoBean.getPrecoConcessao());
				}
				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
				result.add(bean);
				
			}
			return result;			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}

	public List<PrecoBean> findAllParcelasPMPKIT3Gerador(Long idContrato){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		//Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			
			//Long frequencia = 0l;
			
			BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager.createQuery("From PmpContMesesStandard where idContrato.id = :id");
			query.setParameter("id", contrato.getId());
			List<PmpContMesesStandard> mesesStandardList = query.getResultList();
//			for (PmpContMesesStandard pmpContMesesStandard : mesesStandardList) {
//				totalHorasManutencao += pmpContMesesStandard.getFrequencia();
//			}
			for (PmpContMesesStandard pmpContMesesStandard : mesesStandardList) {
				//frequencia = pmpContHorasStandard.getFrequencia();
				//Query para peças com desconto PDP
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContMesesStandard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						" and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
				custoPecas = (BigDecimal)query.getSingleResult();
				Double custoNordesteTotal = 0.0;
				if(custoPecas != null){
					custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
					custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
					custoManutencao = custoManutencao + custoNordesteTotal;
				}
				//Query sem desconto pdp
				query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContMesesStandard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
						" or m.bectyc is null)");
				custoPecas = (BigDecimal)query.getSingleResult();
				if(custoPecas != null){
					custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
					custoManutencao = custoManutencao + custoNordesteTotal;
				}
			}
			//int totalManutencoes = 0;
			
			//totalHorasManutencao = frequencia * horasStandardList.size();
			
			//custo do TA
			//if(contrato.getTa().equals("S")){
				Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
				//taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
				custoManutencao = custoManutencao + taCusto;
			//}
			
			
			//total horas para a manutenção
			query = manager.createNativeQuery("select sum (to_number(replace(h.frsdhr,'.',','))) from pmp_hora h"+
					" where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_meses_standard hs where id_contrato = "+contrato.getId()+")"+
					" and h.bgrp = '"+contrato.getBgrp()+"'"+
					" and substr(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
					" and substr(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
			BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
			
			if (totalHHManutencao != null) {
				if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
					Double valorHH = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue();
					//valorHH = valorHH + ( (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorSpot().doubleValue() * valorHH) /100 );
					valorHH = valorHH + ( (contrato.getDescontoPdpSpot().doubleValue() * valorHH) /100 );
					//standard.setCustoMo((BigDecimal.valueOf(valorHH)).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())));
					custoManutencao = custoManutencao + valorHH;//valor de hh
				}else{
					custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
				}
				custoManutencao = custoManutencao + ((contrato.getDistanciaGerador().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())*mesesStandardList.size());//valor de km vezes a quantidade de manutenções
				
				
				PrecoBean bean = new PrecoBean();
	//				bean.setParcela(1);
	//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
	//				result.add(bean);
				PrecoBean precoConcessaoBean = this.findPrecoConcessaoGerador(idContrato);
				for(int i = 0; i < 10; i++){
					//custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
					bean = new PrecoBean();
					bean.setParcela(i+1);
					if(i == 0){
						bean.setPrecoConcessao(precoConcessaoBean.getPrecoConcessao());
					}
					bean.setPreco("R$ " + ValorMonetarioHelper.formata("###,###.00", custoManutencao));
					result.add(bean);
					
				}
				return result;
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
	
	public List<ContratoComercialBean> findTabelaPreco(){
		
		EntityManager manager = null;
		
		Long totalHorasManutencao = 2000l;
		
		List<ContratoComercialBean> result = new ArrayList<ContratoComercialBean>();
		try{
			manager = JpaUtil.getInstance();
			
			//Long frequencia = 0l;
			
			Query query = manager.createQuery("select distinct m, p from PmpConfigManutencao m, PmpConfiguracaoPrecos p where m.idConfiguracaoPreco.id = p.id and m.bgrp = 'PM' and m.contExcessao = 'N' and m.isAtivo is null order by m.modelo, m.prefixo, m.beginrange ");
			
			List<Object[]> list = query.getResultList();
			for (Object[] objects : list) {
				BigDecimal totalHHManutencao = BigDecimal.ZERO;
				Double custoManutencao = 0d;
				BigDecimal custoPecas = BigDecimal.ZERO;
				ContratoComercialBean bean = new ContratoComercialBean();
				String cptcd = "";
				PmpConfigManutencao manutencao = (PmpConfigManutencao)objects[0];
				PmpConfiguracaoPrecos precos = (PmpConfiguracaoPrecos)objects[1];
				bean.setModelo(manutencao.getModelo());
				bean.setPrefixo(manutencao.getPrefixo());
				bean.setQtdParcelas(1);
				bean.setBeginRanger(manutencao.getBeginrange());
				bean.setEndRanger(manutencao.getEndrange());
				//BigDecimal custoNordeste = precos.getCustoNordeste();

				query = manager.createQuery("From  PmpConfigManutencao pc, PmpConfigHorasStandard hs where pc.id = hs.idConfigManutencao.id " +
						" and hs.horas <=:horas" +
						" and pc.bgrp =:bgrp" +
						" and pc.modelo =:modelo" +
						" and pc.prefixo =:prefixo" +
						" and pc.beginrange =:beginrange" +
						" and pc.endrange =:endrange" +
						" and pc.contExcessao = 'N'" +
						" and pc.isAtivo is null " +
				" order by hs.horas");
				query.setParameter("horas", 2000L);
				query.setParameter("bgrp", manutencao.getBgrp());
				query.setParameter("modelo", manutencao.getModelo());
				query.setParameter("prefixo", manutencao.getPrefixo());
				query.setParameter("beginrange", manutencao.getBeginrange());
				query.setParameter("endrange", manutencao.getEndrange());
				List<Object[]> manutencaoList = query.getResultList();
				boolean isPreco = true;
				for (Object[] pair : manutencaoList) {
					//PmpConfigManutencao pmpConfigManutencao = (PmpConfigManutencao)pair[0];
					PmpConfigHorasStandard standard = (PmpConfigHorasStandard)pair[1];
					cptcd += "'"+standard.getStandardJobCptcd()+"',";
					//frequencia = pmpContHorasStandard.getFrequencia();
					//Query para peças com desconto PDP
					query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
							" where m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
							" and m.bgrp = '"+manutencao.getBgrp()+"'"+
							" and substring(m.beqmsn,1,4) = '"+manutencao.getPrefixo()+"'"+
							" and substring(m.beqmsn,5,10) between '"+manutencao.getBeginrange().substring(4, 9)+"' and '"+manutencao.getEndrange().substring(4, 9)+"'"+
					" and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
					
					custoPecas = (BigDecimal)query.getSingleResult();
					Double custoNordesteTotal = 0.0;
					if(custoPecas != null){
						//custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
						custoNordesteTotal = custoPecas.doubleValue();
						custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal * (precos.getDescPdp().doubleValue()))/100);//desconto PDP
						custoManutencao = custoManutencao + custoNordesteTotal;
					}
					//Query sem desconto pdp
					query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
							" where m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
							" and m.bgrp = '"+manutencao.getBgrp()+"'"+
							" and substring(m.beqmsn,1,4) = '"+manutencao.getPrefixo()+"'"+
							" and substring(m.beqmsn,5,10) between '"+manutencao.getBeginrange().substring(4, 9)+"' and '"+manutencao.getEndrange().substring(4, 9)+"'"+
							" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
					" or m.bectyc is null)");
					custoPecas = (BigDecimal)query.getSingleResult();
					if(custoPecas != null){
					//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
					custoManutencao = custoManutencao + custoPecas.doubleValue();
					}
					
					query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
							" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
							" and h.bgrp = '"+manutencao.getBgrp()+"'"+
							" and substring(h.beqmsn,1,4) = '"+manutencao.getPrefixo()+"'"+
							" and substring(h.beqmsn,5,10) between '"+manutencao.getBeginrange().substring(4, 9)+"' and '"+manutencao.getEndrange().substring(4, 9)+"'");
					if((BigDecimal)query.getSingleResult() != null){
						totalHHManutencao = totalHHManutencao.add((BigDecimal)query.getSingleResult());
					}else{
						isPreco = false;
					}
				}
				if(isPreco == false){
					continue;
				}
				//int totalManutencoes = 0;

				//totalHorasManutencao = frequencia * horasStandardList.size();

				//custo do TA
				//if(contrato.getTa().equals("S")){
//				Double taCusto = (precos.getTaHoras().doubleValue() * precos.getHhTa().doubleValue());
//				taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
//				custoManutencao = custoManutencao + taCusto;
				//}


				//total horas para a manutenção
//				query = manager.createNativeQuery("select sum(cast(h.FRSDHR as decimal(18,2))) from pmp_hora h"+
//						" where h.cptcd in ("+cptcd.substring(0,cptcd.length()-1)+")"+
//						" and h.bgrp = '"+manutencao.getBgrp()+"'"+
//						" and substring(h.beqmsn,1,4) = '"+manutencao.getPrefixo()+"'"+
//						" and substring(h.beqmsn,5,10) between '"+manutencao.getBeginrange().substring(4, 9)+"' and '"+manutencao.getEndrange().substring(4, 9)+"'");
//				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
				if(totalHHManutencao != null){
					custoManutencao = custoManutencao + (precos.getHhPmp().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
					custoManutencao = custoManutencao + ((precos.getKmPmp().doubleValue() * precos.getValorKmPmp().doubleValue())*manutencaoList.size());//valor de km vezes a quantidade de manutenções
				}

				//				bean.setParcela(1);
				//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
				//				result.add(bean);


				//custoManutencao = custoManutencao + ((custoManutencao * precos.getJurosVenda().doubleValue())/100);
				if(custoManutencao == null){
					custoManutencao = 0d;
					
				}

				bean.setValorContrato("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
				result.add(bean);
			}
			return result;		
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	/**
	 * Recupera os preços do para disponibilizar para o zoho
	 * @param request
	 * @param response
	 */
	public void savePrecoZoho(){
		
		EntityManager manager = null;
		Connection conn = null;
		Statement stmt = null;
		//Long totalHorasManutencao = 2000l;
		
		//List<ContratoComercialBean> result = new ArrayList<ContratoComercialBean>();
		try{
			conn = ConnectionZoho.getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate("delete from PrecosPmpCat");
			manager = JpaUtil.getInstance();
			
			//Long frequencia = 0l;
			
			Query query = manager.createQuery("select distinct m, p from PmpConfigManutencao m, PmpConfiguracaoPrecos p where m.idConfiguracaoPreco.id = p.id and m.bgrp = 'PM' and m.contExcessao = 'N' and m.isAtivo is null order by m.modelo, m.prefixo, m.beginrange");
			
			List<Object[]> list = query.getResultList();
			Long horas = 2000L;
			while(horas <= 12000){
				for (Object[] objects : list) {
					BigDecimal totalHHManutencao = BigDecimal.ZERO;
					Double custoManutencao = 0d;
					BigDecimal custoPecas = BigDecimal.ZERO;
					ContratoComercialBean bean = new ContratoComercialBean();
					String cptcd = "";
					PmpConfigManutencao manutencao = (PmpConfigManutencao)objects[0];
					PmpConfiguracaoPrecos precos = (PmpConfiguracaoPrecos)objects[1];
					bean.setModelo(manutencao.getModelo());
					bean.setPrefixo(manutencao.getPrefixo());
					bean.setQtdParcelas(1);
					bean.setBeginRanger(manutencao.getBeginrange());
					bean.setEndRanger(manutencao.getEndrange());
					//BigDecimal custoNordeste = precos.getCustoNordeste();

					query = manager.createQuery("From  PmpConfigManutencao pc, PmpConfigHorasStandard hs where pc.id = hs.idConfigManutencao.id " +
							" and hs.horas <=:horas" +
							" and pc.bgrp =:bgrp" +
							" and pc.modelo =:modelo" +
							" and pc.prefixo =:prefixo" +
							" and pc.beginrange =:beginrange" +
							" and pc.endrange =:endrange" +
							" and pc.contExcessao = 'N'" +
							" and pc.isAtivo is null " +
					" order by hs.horas");
					query.setParameter("horas", horas);
					query.setParameter("bgrp", manutencao.getBgrp());
					query.setParameter("modelo", manutencao.getModelo());
					query.setParameter("prefixo", manutencao.getPrefixo());
					query.setParameter("beginrange", manutencao.getBeginrange());
					query.setParameter("endrange", manutencao.getEndrange());
					List<Object[]> manutencaoList = query.getResultList();
					boolean isPreco = true;
					for (Object[] pair : manutencaoList) {
						//PmpConfigManutencao pmpConfigManutencao = (PmpConfigManutencao)pair[0];
						PmpConfigHorasStandard standard = (PmpConfigHorasStandard)pair[1];
						cptcd += "'"+standard.getStandardJobCptcd()+"',";
						//frequencia = pmpContHorasStandard.getFrequencia();
						//Query para peças com desconto PDP
						query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
								" where m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
								" and m.bgrp = '"+manutencao.getBgrp()+"'"+
								" and substring(m.beqmsn,1,4) = '"+manutencao.getPrefixo()+"'"+
								" and substring(m.beqmsn,5,10) between '"+manutencao.getBeginrange().substring(4, 9)+"' and '"+manutencao.getEndrange().substring(4, 9)+"'"+
						" and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");

						custoPecas = (BigDecimal)query.getSingleResult();
						Double custoNordesteTotal = 0.0;
						if(custoPecas != null){
							//custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
							custoNordesteTotal = custoPecas.doubleValue();
							custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal * (precos.getDescPdp().doubleValue()))/100);//desconto PDP
							custoManutencao = custoManutencao + custoNordesteTotal;
						}
						//Query sem desconto pdp
						query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
								" where m.cptcd = '"+standard.getStandardJobCptcd()+"'"+
								" and m.bgrp = '"+manutencao.getBgrp()+"'"+
								" and substring(m.beqmsn,1,4) = '"+manutencao.getPrefixo()+"'"+
								" and substring(m.beqmsn,5,10) between '"+manutencao.getBeginrange().substring(4, 9)+"' and '"+manutencao.getEndrange().substring(4, 9)+"'"+
								" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
						" or m.bectyc is null)");
						custoPecas = (BigDecimal)query.getSingleResult();
						if(custoPecas != null){
							//custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
							custoManutencao = custoManutencao + custoPecas.doubleValue();
						}

						query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
								" where h.cptcd = '"+standard.getStandardJobCptcd()+"'"+
								" and h.bgrp = '"+manutencao.getBgrp()+"'"+
								" and substring(h.beqmsn,1,4) = '"+manutencao.getPrefixo()+"'"+
								" and substring(h.beqmsn,5,10) between '"+manutencao.getBeginrange().substring(4, 9)+"' and '"+manutencao.getEndrange().substring(4, 9)+"'");
						if((BigDecimal)query.getSingleResult() != null){
							totalHHManutencao = totalHHManutencao.add((BigDecimal)query.getSingleResult());
						}else{
							isPreco = false;
						}
					}
					if(isPreco == false){
						continue;
					}
					//int totalManutencoes = 0;

					//totalHorasManutencao = frequencia * horasStandardList.size();

					//custo do TA
					//if(contrato.getTa().equals("S")){
					//				Double taCusto = (precos.getTaHoras().doubleValue() * precos.getHhTa().doubleValue());
					//				taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
					//				custoManutencao = custoManutencao + taCusto;
					//}


					//total horas para a manutenção
					//				query = manager.createNativeQuery("select sum(cast(h.FRSDHR as decimal(18,2))) from pmp_hora h"+
					//						" where h.cptcd in ("+cptcd.substring(0,cptcd.length()-1)+")"+
					//						" and h.bgrp = '"+manutencao.getBgrp()+"'"+
					//						" and substring(h.beqmsn,1,4) = '"+manutencao.getPrefixo()+"'"+
					//						" and substring(h.beqmsn,5,10) between '"+manutencao.getBeginrange().substring(4, 9)+"' and '"+manutencao.getEndrange().substring(4, 9)+"'");
					//				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
					if(totalHHManutencao != null){
						custoManutencao = custoManutencao + (precos.getHhPmp().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
						custoManutencao = custoManutencao + ((precos.getKmPmp().doubleValue() * precos.getValorKmPmp().doubleValue())*manutencaoList.size());//valor de km vezes a quantidade de manutenções
					}

					//				bean.setParcela(1);
					//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
					//				result.add(bean);


					//custoManutencao = custoManutencao + ((custoManutencao * precos.getJurosVenda().doubleValue())/100);
					if(custoManutencao == null){
						custoManutencao = 0d;

					}
					
					bean.setValorContrato("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
					//result.add(bean);
					stmt.executeUpdate("insert into PrecosPmpCat (modelo, prefixo, beginRanger, endRanger, dataImportacao, preco, horas) values ('"+bean.getModelo()+"','"+bean.getPrefixo()+"','"+bean.getBeginRanger()+"','"+bean.getEndRanger()+"',NOW(),"+custoManutencao+","+horas+")");
					
					
				}
				horas += 1000;
			}
			//return result;		
			EmailHelper emailHelper = new EmailHelper();
			//emailHelper.sendSimpleMail("Erro ao executar a busca do STANDARD JOB no DBS", "ERRO STANDARD JOB", "ti.monitoramento@marcosa.com.br");
			emailHelper.sendSimpleMail("Rotina de importação para o ZOHO executada com sucesso", "SUCESSO IMPORTAÇÃO ZOHO", "rodrigo@rdrsistemas.com.br");
		}catch (Exception e) {
			e.printStackTrace();
			EmailHelper emailHelper = new EmailHelper();
			//emailHelper.sendSimpleMail("Erro ao executar a busca do STANDARD JOB no DBS", "ERRO STANDARD JOB", "ti.monitoramento@marcosa.com.br");
			emailHelper.sendSimpleMail("Erro ao executar importação para o ZOHO", "ERRO IMPORTAÇÃO ZOHO", "rodrigo@rdrsistemas.com.br");
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
			if(conn != null){
				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//return result;
	}
	
	public PrecoBean findPrecoConcessao(Long idContrato){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		BigDecimal custoPecasFrasco = BigDecimal.ZERO;
		BigDecimal totalHHManutencao = BigDecimal.ZERO;
		
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			
			//Long frequencia = 0l;
			
			//BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager.createQuery("From PmpContHorasStandard where idContrato.id = :id order by horasManutencao");
			query.setParameter("id", contrato.getId());
			List<PmpContHorasStandard> horasStandardList = query.getResultList();
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				totalHorasManutencao += pmpContHorasStandard.getFrequencia();
			}
			int quantidadeManutencoes = 0;
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				quantidadeManutencoes++;
				//frequencia = pmpContHorasStandard.getFrequencia();
				//Query para peças com desconto PDP
				query = manager.createNativeQuery("select sum(m.landcs * m.dlrqty) from pmp_manutencao_preco_custo m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
						//" and m.stno = "+Integer.valueOf(usuarioBean.getFilial())+
						//" and m.pano20 <> '1Z0212'");
				custoPecas = (BigDecimal)query.getSingleResult(); 
				
				if(custoPecas != null){
					custoPecas = BigDecimal.ZERO;
				}
				
				query = manager.createNativeQuery("select sum(m.uncs * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
						//" and m.pano20 = '1Z0212'");
				custoPecasFrasco = (BigDecimal)query.getSingleResult(); 
				
				if(custoPecasFrasco != null){
					custoPecas = custoPecas.add(custoPecasFrasco);
				}
				//Double custoNordesteTotal = 0.0;
				//if(custoPecas != null){
					//custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
					//custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
					custoManutencao = custoManutencao + custoPecas.doubleValue();
				//}
					
					//total horas para a manutenção
					query = manager.createNativeQuery(" select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
							" where h.cptcd ='"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
							" and h.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
					totalHHManutencao = totalHHManutencao.add((BigDecimal)query.getSingleResult());	
					if(contrato.getIdTipoContrato().getSigla().equals("VEN") && contrato.getIdStatusContrato().getSigla().equals("CA")){//contrato pos pago e aberto para na revisão que o consultor marcou
						if(contrato.getPrintRevisaoPosPago() != null  && contrato.getPrintRevisaoPosPago().longValue() <= pmpContHorasStandard.getHorasManutencao().longValue()){
							break;
						}
							
					}
			}
			//int totalManutencoes = 0;
			
			//totalHorasManutencao = frequencia * horasStandardList.size();
			
			//custo do TA
			if(contrato.getTa().equals("S")){
				Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhTaCusto().doubleValue());
				taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
				custoManutencao = custoManutencao + taCusto;
			}
			
			
//			//total horas para a manutenção
//			query = manager.createNativeQuery(" select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//					" where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId()+")"+
//					" and h.bgrp = '"+contrato.getBgrp()+"'"+
//					" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//					" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//			BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
			custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhPmpCusto().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
			if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
				//custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmpSpot().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmpCusto().doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
				String arraySpot []= contrato.getQtdHorasDeslSpot().split(":");
				BigDecimal minSpot = BigDecimal.valueOf((Double.valueOf(arraySpot[1])/60));
				BigDecimal horasPdpSpot = BigDecimal.valueOf(Double.valueOf(arraySpot[0]));
				horasPdpSpot = horasPdpSpot.add(minSpot);
				
				custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHorasDesSpot().doubleValue() * horasPdpSpot.doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
			}else{
				custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmpCusto().doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
			}
			
			
			PrecoBean bean = new PrecoBean();
//				bean.setParcela(1);
//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
//				result.add(bean);
			//for(int i = 0; i < 10; i++){
				//custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
				//bean = new PrecoBean();
				bean.setParcela(1);
				bean.setPrecoConcessao("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
				//result.add(bean);
				
			//}
			return bean;
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return null;
	}
	public PrecoBean findPrecoConcessaoCustomer(Long idContrato){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		BigDecimal custoPecasFrasco = BigDecimal.ZERO;
		//BigDecimal totalHHManutencao = BigDecimal.ZERO;
		
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			
			//Long frequencia = 0l;
			
			//BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager.createQuery("From PmpContHorasStandard where idContrato.id = :id order by horasManutencao");
			query.setParameter("id", contrato.getId());
			List<PmpContHorasStandard> horasStandardList = query.getResultList();
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				totalHorasManutencao += pmpContHorasStandard.getFrequencia();
			}
			int quantidadeManutencoes = 0;
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				quantidadeManutencoes++;
				//frequencia = pmpContHorasStandard.getFrequencia();
				//Query para peças com desconto PDP
				query = manager.createNativeQuery("select sum(m.landcs * m.dlrqty) from pmp_manutencao_preco_custo m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				//" and m.stno = "+Integer.valueOf(usuarioBean.getFilial())+
				//" and m.pano20 <> '1Z0212'");
				custoPecas = (BigDecimal)query.getSingleResult(); 
				
				if(custoPecas != null){
					custoPecas = BigDecimal.ZERO;
				}
				
				query = manager.createNativeQuery("select sum(m.uncs * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				//" and m.pano20 = '1Z0212'");
				custoPecasFrasco = (BigDecimal)query.getSingleResult(); 
				
				if(custoPecasFrasco != null){
					custoPecas = custoPecas.add(custoPecasFrasco);
				}
				//Double custoNordesteTotal = 0.0;
				//if(custoPecas != null){
				//custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
				//custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
				custoManutencao = custoManutencao + custoPecas.doubleValue();
				//}
				
				//total horas para a manutenção
//				query = manager.createNativeQuery(" select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//						" where h.cptcd ='"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
//						" and h.bgrp = '"+contrato.getBgrp()+"'"+
//						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//				totalHHManutencao = totalHHManutencao.add((BigDecimal)query.getSingleResult());	
				
				custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getMonitoramento().doubleValue();
				if(pmpContHorasStandard != null && "S".equals(pmpContHorasStandard.getIsTa())){
					custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorTa().doubleValue() + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getSos().doubleValue();
				}
				if(contrato.getIdTipoContrato().getSigla().equals("VEN") && contrato.getIdStatusContrato().getSigla().equals("CA")){//contrato pos pago e aberto para na revisão que o consultor marcou
					if(contrato.getPrintRevisaoPosPago() != null  && contrato.getPrintRevisaoPosPago().longValue() <= pmpContHorasStandard.getHorasManutencao().longValue()){
						break;
					}
					
				}
			}
			//int totalManutencoes = 0;
			
			//totalHorasManutencao = frequencia * horasStandardList.size();
			
			//custo do TA
//			if(contrato.getTa().equals("S")){
//				Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhTaCusto().doubleValue());
//				taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
//				custoManutencao = custoManutencao + taCusto;
//			}
			
			
//			//total horas para a manutenção
//			query = manager.createNativeQuery(" select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//					" where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId()+")"+
//					" and h.bgrp = '"+contrato.getBgrp()+"'"+
//					" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//					" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//			BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
//			custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhPmpCusto().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
//			custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmpCusto().doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
//			
			
			PrecoBean bean = new PrecoBean();
//				bean.setParcela(1);
//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
//				result.add(bean);
			//for(int i = 0; i < 10; i++){
			//custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
			//bean = new PrecoBean();
			bean.setParcela(1);
			bean.setPrecoConcessao("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
			//result.add(bean);
			
			//}
			return bean;
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return null;
	}
	
	public PrecoBean findPrecoConcessaoGerador(Long idContrato){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		//Long totalMesesManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		BigDecimal custoPecasFrasco = BigDecimal.ZERO;
		
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			
			//Long frequencia = 0l;
			
			//BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager.createQuery("From PmpContMesesStandard where idContrato.id = :id");
			query.setParameter("id", contrato.getId());
			List<PmpContMesesStandard> mesesStandardList = query.getResultList();
//			for (PmpContMesesStandard pmpContMesesStandard : mesesStandardList) {
//				totalMesesManutencao += pmpContMesesStandard.getFrequencia();
//			}
			for (PmpContMesesStandard pmpContHorasStandard : mesesStandardList) {
				//frequencia = pmpContHorasStandard.getFrequencia();
				//Query para peças com desconto PDP
				query = manager.createNativeQuery("select sum(m.landcs * m.dlrqty) from pmp_manutencao_preco_custo m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						//" and m.stno = "+Integer.valueOf(usuarioBean.getFilial())+
						" and m.pano20 <> '1Z0212'");
				custoPecas = (BigDecimal)query.getSingleResult();
				if(custoPecas == null){
					custoPecas = BigDecimal.ZERO;
				}
				query = manager.createNativeQuery("select sum(m.uncs * m.dlrqty) from pmp_manutencao m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
						" and m.pano20 = '1Z0212'");
				custoPecasFrasco = (BigDecimal)query.getSingleResult(); 
				if(custoPecasFrasco == null){
					custoPecasFrasco = BigDecimal.ZERO;
				}
				if(custoPecasFrasco != null){
					custoPecas = custoPecas.add(custoPecasFrasco);
				}
				//Double custoNordesteTotal = 0.0;
				//if(custoPecas != null){
				//custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
				//custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
				custoManutencao = custoManutencao + custoPecas.doubleValue();
				//}
			}
			//int totalManutencoes = 0;
			
			//totalHorasManutencao = frequencia * horasStandardList.size();
			
			//custo do TA
			//if(contrato.getTa().equals("S")){
				Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhTaCusto().doubleValue());
				//taCusto = (totalMesesManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
				custoManutencao = custoManutencao + taCusto;
			//}
			
			
			//total horas para a manutenção
			query = manager.createNativeQuery("select sum (to_number(replace(h.frsdhr,'.',','))) from pmp_hora h"+
					" where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_meses_standard hs where id_contrato = "+contrato.getId()+")"+
					" and h.bgrp = '"+contrato.getBgrp()+"'"+
					" and substr(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
					" and substr(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
			BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
			custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhPmpCusto().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
			custoManutencao = custoManutencao + ((contrato.getDistanciaGerador().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmpCusto().doubleValue())*mesesStandardList.size());//valor de km vezes a quantidade de manutenções
			
			
			PrecoBean bean = new PrecoBean();
//				bean.setParcela(1);
//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
//				result.add(bean);
			//for(int i = 0; i < 10; i++){
			//custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
			//bean = new PrecoBean();
			bean.setParcela(1);
			bean.setPrecoConcessao("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
			//result.add(bean);
			
			//}
			return bean;
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return null;
	}
	
	
	public List<PrecoBean> findAllParcelasPMPKIT3Custo(Long idContrato, String  siglaCustomizacao){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		BigDecimal totalHHManutencao = BigDecimal.ZERO;
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			String complemento = "";
			String complementoSigla = "";
			if(contrato.getIdConfigTracao() != null){
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ocptmd is null)";
				complemento += " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or JWKAPP is null)"
				+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ojbloc is null)";
			}
			if(siglaCustomizacao.length() > 0){
				complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and JWKAPP not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
			}
			//Long frequencia = 0l;
			
				//BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
				Query query = manager.createQuery("From PmpContHorasStandard where idContrato.id = :id order by horasManutencao");
				query.setParameter("id", contrato.getId());
				List<PmpContHorasStandard> horasStandardList = query.getResultList();
				for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
					totalHorasManutencao += pmpContHorasStandard.getFrequencia();
				}
				int quantidadeManutencoes = 0;
				for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
					quantidadeManutencoes++;
					//frequencia = pmpContHorasStandard.getFrequencia();
					//Query para peças com desconto PDP
					query = manager.createNativeQuery("select sum(m.landcs * m.dlrqty) from pmp_manutencao_preco_custo m"+
							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
							" and m.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							complemento + complementoSigla +
							" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
					//" and m.stno ="+Integer.valueOf(FILIAL));
					custoPecas = (BigDecimal)query.getSingleResult();
					//Double custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
					//custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
					if(custoPecas != null){
						custoManutencao = custoManutencao + custoPecas.doubleValue();
					}
//					//Query sem desconto pdp
//					query = manager.createNativeQuery("select sum(m.unls) from pmp_manutencao_preco_custo m"+
//							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
//							" and m.bgrp = '"+contrato.getBgrp()+"'"+
//							" and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//							" and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
//					" and m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)");
//					custoPecas = (BigDecimal)query.getSingleResult();
//					custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
//					custoManutencao = custoManutencao + custoNordesteTotal;
					
					//total horas para a manutenção
					query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
							" where h.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
							" and h.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
					totalHHManutencao = totalHHManutencao.add((BigDecimal)query.getSingleResult());
					
					
					if(contrato.getIdTipoContrato().getSigla().equals("VEN") && contrato.getIdStatusContrato().getSigla().equals("CA")){//contrato pos pago e aberto para na revisão que o consultor marcou
						if(contrato.getPrintRevisaoPosPago() != null  && contrato.getPrintRevisaoPosPago().longValue() <= pmpContHorasStandard.getHorasManutencao().longValue()){
							break;
						}
							
					}
				}
				//int totalManutencoes = 0;
				
				//totalHorasManutencao = frequencia * horasStandardList.size();
				
				//custo do TA
				if(contrato.getTa().equals("S")){
					Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhTaCusto().doubleValue());
					taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
					custoManutencao = custoManutencao + taCusto;
				}
				
				
				//total horas para a manutenção
//				query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//						" where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId()+")"+
//						" and h.bgrp = '"+contrato.getBgrp()+"'"+
//						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
			
					custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhPmpCusto().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
					custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmpCusto().doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
				
				PrecoBean bean = new PrecoBean();
//				bean.setParcela(1);
//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
//				result.add(bean);
				//for(int i = 0; i < 10; i++){
					//custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
					bean = new PrecoBean();
					bean.setParcela(1);
					bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
					result.add(bean);
					
				//}
				return result;
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	public List<PrecoBean> findAllParcelasCustoPartner(Long idContrato, String  siglaCustomizacao){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		BigDecimal totalHHManutencao = BigDecimal.ZERO;
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			String complemento = "";
			String complementoSigla = "";
			if(contrato.getIdConfigTracao() != null){
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ocptmd is null)";
				complemento += " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or JWKAPP is null)"
				+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ojbloc is null)";
			}
			if(siglaCustomizacao.length() > 0){
				complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and JWKAPP not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
			}
			//Long frequencia = 0l;
			
			//BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager.createQuery("From PmpContHorasStandard where idContrato.id = :id order by horasManutencao");
			query.setParameter("id", contrato.getId());
			List<PmpContHorasStandard> horasStandardList = query.getResultList();
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				totalHorasManutencao += pmpContHorasStandard.getFrequencia();
			}
			int quantidadeManutencoes = 0;
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				String retirarPeca = "";
				if(!pmpContHorasStandard.getIsPartner().equals("N") && (pmpContHorasStandard.getStandardJobCptcd().equals("7502") || pmpContHorasStandard.getStandardJobCptcd().equals("7501"))){
					query = manager.createNativeQuery("select descricao from Pmp_Comp_Code_Partner");
					List<String> descricao = (List<String>)query.getResultList();
					for (String cptcd : descricao) {
						if(pmpContHorasStandard.getStandardJobCptcd().equals(cptcd)){
							retirarPeca = " and m.OJBLOC <> 'CST'";
						}
					}
					
				}
				//frequencia = pmpContHorasStandard.getFrequencia();
				//Query para peças com desconto PDP
				query = manager.createNativeQuery("select sum(m.landcs * m.dlrqty) from pmp_manutencao_preco_custo m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						retirarPeca+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						complemento + complementoSigla +
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				//" and m.stno ="+Integer.valueOf(FILIAL));
				custoPecas = (BigDecimal)query.getSingleResult();
				//Double custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
				//custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
				if(custoPecas != null){
					custoManutencao = custoManutencao + custoPecas.doubleValue();
				}
//					//Query sem desconto pdp
//					query = manager.createNativeQuery("select sum(m.unls) from pmp_manutencao_preco_custo m"+
//							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
//							" and m.bgrp = '"+contrato.getBgrp()+"'"+
//							" and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//							" and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
//					" and m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)");
//					custoPecas = (BigDecimal)query.getSingleResult();
//					custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
//					custoManutencao = custoManutencao + custoNordesteTotal;
				if((pmpContHorasStandard.getIsPartner() != null && "N".equals(pmpContHorasStandard.getIsPartner())) || ((pmpContHorasStandard.getStandardJobCptcd().equals("752A") || pmpContHorasStandard.getStandardJobCptcd().equals("752B")) && (pmpContHorasStandard.getIdContrato().getIdFamilia() == 41 ||  pmpContHorasStandard.getIdContrato().getIdFamilia() == 84))){
					quantidadeManutencoes++;
					//total horas para a manutenção
					query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
							" where h.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
							" and h.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
					totalHHManutencao = totalHHManutencao.add((BigDecimal)query.getSingleResult());
				}
				
				if(contrato.getIdTipoContrato().getSigla().equals("VEN") && contrato.getIdStatusContrato().getSigla().equals("CA")){//contrato pos pago e aberto para na revisão que o consultor marcou
					if(contrato.getPrintRevisaoPosPago() != null  && contrato.getPrintRevisaoPosPago().longValue() <= pmpContHorasStandard.getHorasManutencao().longValue()){
						break;
					}
					
				}
			}
			//int totalManutencoes = 0;
			
			//totalHorasManutencao = frequencia * horasStandardList.size();
			
			//custo do TA
			if(contrato.getTa().equals("S")){
				Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhTaCusto().doubleValue());
				taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
				custoManutencao = custoManutencao + taCusto;
			}
			
			
			//total horas para a manutenção
//				query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//						" where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId()+")"+
//						" and h.bgrp = '"+contrato.getBgrp()+"'"+
//						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
			
			custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhPmpCusto().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
			if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
				//custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmpSpot().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmpCusto().doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
				String arraySpot []= contrato.getQtdHorasDeslSpot().split(":");
				BigDecimal minSpot = BigDecimal.valueOf((Double.valueOf(arraySpot[1])/60));
				BigDecimal horasPdpSpot = BigDecimal.valueOf(Double.valueOf(arraySpot[0]));
				horasPdpSpot = horasPdpSpot.add(minSpot);
				custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHorasDesSpot().doubleValue() * horasPdpSpot.doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
			}else{
				custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmpCusto().doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
			}
			PrecoBean bean = new PrecoBean();
//				bean.setParcela(1);
//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
//				result.add(bean);
			//for(int i = 0; i < 10; i++){
			//custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
			bean = new PrecoBean();
			bean.setParcela(1);
			bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
			result.add(bean);
			
			//}
			return result;
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	public List<PrecoBean> findAllParcelasCustoCustomer(Long idContrato, String  siglaCustomizacao){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		//BigDecimal totalHHManutencao = BigDecimal.ZERO;
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			String complemento = "";
			String complementoSigla = "";
			if(contrato.getIdConfigTracao() != null){
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ocptmd is null)";
				complemento += " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or JWKAPP is null)"
				+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ojbloc is null)";
			}
			if(siglaCustomizacao.length() > 0){
				complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and JWKAPP not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
			}
			//Long frequencia = 0l;
			
			//BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager.createQuery("From PmpContHorasStandard where idContrato.id = :id order by horasManutencao");
			query.setParameter("id", contrato.getId());
			List<PmpContHorasStandard> horasStandardList = query.getResultList();
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				totalHorasManutencao += pmpContHorasStandard.getFrequencia();
			}
			int quantidadeManutencoes = 0;
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				quantidadeManutencoes++;
				//frequencia = pmpContHorasStandard.getFrequencia();
				//Query para peças com desconto PDP
				query = manager.createNativeQuery("select sum(m.landcs * m.dlrqty) from pmp_manutencao_preco_custo m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and m.OJBLOC <> 'CST'"+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						complemento + complementoSigla +
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				//" and m.stno ="+Integer.valueOf(FILIAL));
				custoPecas = (BigDecimal)query.getSingleResult();
				//Double custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
				//custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
				if(custoPecas != null){
					custoManutencao = custoManutencao + custoPecas.doubleValue();
				}
//					//Query sem desconto pdp
//					query = manager.createNativeQuery("select sum(m.unls) from pmp_manutencao_preco_custo m"+
//							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
//							" and m.bgrp = '"+contrato.getBgrp()+"'"+
//							" and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//							" and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
//					" and m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)");
//					custoPecas = (BigDecimal)query.getSingleResult();
//					custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
//					custoManutencao = custoManutencao + custoNordesteTotal;
				
				//total horas para a manutenção
//				query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//						" where h.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
//						" and h.bgrp = '"+contrato.getBgrp()+"'"+
//						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//				totalHHManutencao = totalHHManutencao.add((BigDecimal)query.getSingleResult());
//				
				custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getMonitoramento().doubleValue();
				if(pmpContHorasStandard.getIsTa() != null && "S".equals(pmpContHorasStandard.getIsTa())){
					custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorTa().doubleValue() + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getSos().doubleValue();
				}
				if(contrato.getIdTipoContrato().getSigla().equals("VEN") && contrato.getIdStatusContrato().getSigla().equals("CA")){//contrato pos pago e aberto para na revisão que o consultor marcou
					if(contrato.getPrintRevisaoPosPago() != null  && contrato.getPrintRevisaoPosPago().longValue() <= pmpContHorasStandard.getHorasManutencao().longValue()){
						break;
					}
					
				}
			}
			//int totalManutencoes = 0;
			
			//totalHorasManutencao = frequencia * horasStandardList.size();
			
			//custo do TA
//			if(contrato.getTa().equals("S")){
//				Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhTaCusto().doubleValue());
//				taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
//				custoManutencao = custoManutencao + taCusto;
//			}
			
			
			//total horas para a manutenção
//				query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//						" where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId()+")"+
//						" and h.bgrp = '"+contrato.getBgrp()+"'"+
//						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
			
//			custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhPmpCusto().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
//			custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmpCusto().doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
			
			PrecoBean bean = new PrecoBean();
//				bean.setParcela(1);
//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
//				result.add(bean);
			//for(int i = 0; i < 10; i++){
			//custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
			bean = new PrecoBean();
			bean.setParcela(1);
			bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
			result.add(bean);
			
			//}
			return result;
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	public List<PrecoBean> findAllParcelasCustoCustomerLight(Long idContrato, String  siglaCustomizacao){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		//BigDecimal totalHHManutencao = BigDecimal.ZERO;
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			String complemento = "";
			String complementoSigla = "";
			if(contrato.getIdConfigTracao() != null){
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ocptmd is null)";
				complemento += " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or JWKAPP is null)"
						+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ojbloc is null)";
			}
			if(siglaCustomizacao.length() > 0){
				complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and JWKAPP not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
			}
			//Long frequencia = 0l;
			
			//BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager.createQuery("From PmpContHorasStandard where idContrato.id = :id order by horasManutencao");
			query.setParameter("id", contrato.getId());
			List<PmpContHorasStandard> horasStandardList = query.getResultList();
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				totalHorasManutencao += pmpContHorasStandard.getFrequencia();
			}
			int quantidadeManutencoes = 0;
			for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
				quantidadeManutencoes++;
				//frequencia = pmpContHorasStandard.getFrequencia();
				//Query para peças com desconto PDP
				query = manager.createNativeQuery("select sum(m.landcs * m.dlrqty) from pmp_manutencao_preco_custo m"+
						" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and m.sos <> '050' "+
						" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						complemento + complementoSigla +
						" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				//" and m.stno ="+Integer.valueOf(FILIAL));
				custoPecas = (BigDecimal)query.getSingleResult();
				//Double custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
				//custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
				if(custoPecas != null){
					custoManutencao = custoManutencao + custoPecas.doubleValue();
				}
//					//Query sem desconto pdp
//					query = manager.createNativeQuery("select sum(m.unls) from pmp_manutencao_preco_custo m"+
//							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
//							" and m.bgrp = '"+contrato.getBgrp()+"'"+
//							" and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//							" and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
//					" and m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)");
//					custoPecas = (BigDecimal)query.getSingleResult();
//					custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
//					custoManutencao = custoManutencao + custoNordesteTotal;
				
				//total horas para a manutenção
//				query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//						" where h.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
//						" and h.bgrp = '"+contrato.getBgrp()+"'"+
//						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//				totalHHManutencao = totalHHManutencao.add((BigDecimal)query.getSingleResult());
//				
				custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getMonitoramento().doubleValue();
				if(pmpContHorasStandard.getIsTa() != null && "S".equals(pmpContHorasStandard.getIsTa())){
					custoManutencao = custoManutencao + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorTa().doubleValue() + contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getSos().doubleValue();
				}
				if(contrato.getIdTipoContrato().getSigla().equals("VEN") && contrato.getIdStatusContrato().getSigla().equals("CA")){//contrato pos pago e aberto para na revisão que o consultor marcou
					if(contrato.getPrintRevisaoPosPago() != null  && contrato.getPrintRevisaoPosPago().longValue() <= pmpContHorasStandard.getHorasManutencao().longValue()){
						break;
					}
					
				}
			}
			//int totalManutencoes = 0;
			
			//totalHorasManutencao = frequencia * horasStandardList.size();
			
			//custo do TA
//			if(contrato.getTa().equals("S")){
//				Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhTaCusto().doubleValue());
//				taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
//				custoManutencao = custoManutencao + taCusto;
//			}
			
			
			//total horas para a manutenção
//				query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//						" where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId()+")"+
//						" and h.bgrp = '"+contrato.getBgrp()+"'"+
//						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
			
//			custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhPmpCusto().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
//			custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmpCusto().doubleValue())*quantidadeManutencoes);//valor de km vezes a quantidade de manutenções
			
			PrecoBean bean = new PrecoBean();
//				bean.setParcela(1);
//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
//				result.add(bean);
			//for(int i = 0; i < 10; i++){
			//custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
			bean = new PrecoBean();
			bean.setParcela(1);
			bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
			result.add(bean);
			
			//}
			return result;
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	
	public List<PrecoBean> findAllParcelasRentalCustoPMKIT3(Long idContrato, String siglaCustomizacao){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		BigDecimal totalHHManutencao = BigDecimal.ZERO;
		
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			
			//Long frequencia = 0l;
			String complemento = "";
			String complementoSigla ="";
				if(contrato.getIdConfigTracao() != null){
					complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ocptmd is null)";
					complemento += " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or JWKAPP is null)"
								+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ojbloc is null)";
				}
				
				if(siglaCustomizacao.length() > 0){
					complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
					complementoSigla +=  " and JWKAPP not in ("+siglaCustomizacao+")";
					complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
				}
				//BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
				Query query = manager.createQuery("From PmpContHorasStandard where idContrato.id = :id");
				query.setParameter("id", contrato.getId());
				List<PmpContHorasStandard> horasStandardList = query.getResultList();
				for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
					totalHorasManutencao += pmpContHorasStandard.getFrequencia();
				}
				for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
					//frequencia = pmpContHorasStandard.getFrequencia();
					//Query para peças com desconto PDP
					query = manager.createNativeQuery("select sum(m.landcs * m.dlrqty) from pmp_manutencao_preco_custo m"+
							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
							" and m.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							complemento + complementoSigla +
							" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
							//" and m.stno ="+Integer.valueOf(FILIAL));
					custoPecas = (BigDecimal)query.getSingleResult();
					//Double custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
					//custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
					custoManutencao = custoManutencao + custoPecas.doubleValue();
					
//					//Query sem desconto pdp
//					query = manager.createNativeQuery("select sum(m.unls) from pmp_manutencao_preco_custo m"+
//							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
//							" and m.bgrp = '"+contrato.getBgrp()+"'"+
//							" and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//							" and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
//					" and m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)");
//					custoPecas = (BigDecimal)query.getSingleResult();
//					custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
//					custoManutencao = custoManutencao + custoNordesteTotal;
					query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
							" where h.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
							" and h.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
					totalHHManutencao = totalHHManutencao.add((BigDecimal)query.getSingleResult());
				}
				//int totalManutencoes = 0;
				
				//totalHorasManutencao = frequencia * horasStandardList.size();
				
				//custo do TA
				if(contrato.getTa().equals("S")){
					Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
					taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
					custoManutencao = custoManutencao + taCusto;
				}
				
				
//				//total horas para a manutenção
//				query = manager.createNativeQuery("select sum(cast(FRSDHR as decimal(18,2))) from pmp_hora h"+
//						" where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId()+")"+
//						" and h.bgrp = '"+contrato.getBgrp()+"'"+
//						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
//				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhRental().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
				custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmRental().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmRental().doubleValue())*horasStandardList.size());//valor de km vezes a quantidade de manutenções
				
				
				PrecoBean bean = new PrecoBean();
//				bean.setParcela(1);
//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
//				result.add(bean);
			//	for(int i = 0; i < 10; i++){
					//custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
					bean = new PrecoBean();
					bean.setParcela(1);
					bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
					result.add(bean);
					
				//}
				return result;
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}

	
	public List<PrecoBean> findAllParcelasRentalCustoPMKIT3Gerador(Long idContrato){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			
			//Long frequencia = 0l;
			
			//BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
			Query query = manager.createQuery("From PmpContMesesStandard where idContrato.id = :id");
			query.setParameter("id", contrato.getId());
			List<PmpContMesesStandard> mesesStandardList = query.getResultList();
			for (PmpContMesesStandard pmpContMesesStandard : mesesStandardList) {
				totalHorasManutencao += pmpContMesesStandard.getFrequencia();
			}
			for (PmpContMesesStandard pmpContMesesStandard : mesesStandardList) {
				//frequencia = pmpContHorasStandard.getFrequencia();
				//Query para peças com desconto PDP
				query = manager.createNativeQuery("select sum(m.landcs * m.dlrqty) from pmp_manutencao_preco_custo m"+
						" where m.cptcd = '"+pmpContMesesStandard.getStandardJobCptcd()+"'"+
						" and m.bgrp = '"+contrato.getBgrp()+"'"+
						" and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
				//" and m.stno ="+Integer.valueOf(FILIAL));
				custoPecas = (BigDecimal)query.getSingleResult();
				//Double custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
				//custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
				custoManutencao = custoManutencao + custoPecas.doubleValue();
				
			}
			
			//custo do TA
			//if(contrato.getTa().equals("S")){
				Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
				//taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
				custoManutencao = custoManutencao + taCusto;
			//}
			
			
			//total horas para a manutenção
			query = manager.createNativeQuery("select sum (to_number(replace(h.frsdhr,'.',','))) from pmp_hora h"+
					" where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_meses_standard hs where id_contrato = "+contrato.getId()+")"+
					" and h.bgrp = '"+contrato.getBgrp()+"'"+
					" and substr(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
					" and substr(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
			BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
			custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhRental().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
			custoManutencao = custoManutencao + ((contrato.getDistanciaGerador().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmRental().doubleValue())*mesesStandardList.size());//valor de km vezes a quantidade de manutenções
			
			
			PrecoBean bean = new PrecoBean();
			bean = new PrecoBean();
			bean.setParcela(1);
			bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
			result.add(bean);
			
			//}
			return result;
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	public List<PrecoBean> findAllParcelasKIT2(Long idContrato){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			
			//Long frequencia = 0l;
	
				BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
				Query query = manager.createQuery("From PmpContHorasStandard where idContrato.id = :id");
				query.setParameter("id", contrato.getId());
				List<PmpContHorasStandard> horasStandardList = query.getResultList();
				for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
					totalHorasManutencao += pmpContHorasStandard.getFrequencia();
				}
				for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
					//frequencia = pmpContHorasStandard.getFrequencia();
					//Query para peças com desconto PDP
					query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
							" and m.bgrp = '"+contrato.getBgrp()+"'"+
							" and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
					" and m.bectyc in (select pdp.descricao from pmp_desconto_pdp pdp)");
					custoPecas = (BigDecimal)query.getSingleResult();
					Double custoNordesteTotal = 0.0;
					if(custoPecas != null){
						custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
						custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
						custoManutencao = custoManutencao + custoNordesteTotal;
					}
					//Query sem desconto pdp
					query = manager.createNativeQuery("select sum(m.unls * m.dlrqty) from pmp_manutencao m"+
							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
							" and m.bgrp = '"+contrato.getBgrp()+"'"+
							" and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
							" and (m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)"+
					" or m.bectyc is null)");
					custoPecas = (BigDecimal)query.getSingleResult();
					custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
					custoManutencao = custoManutencao + custoNordesteTotal;
				}
				//int totalManutencoes = 0;
				
				//totalHorasManutencao = frequencia * horasStandardList.size();
			
				
				//custo do TA
				if(contrato.getTa().equals("S")){
					Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
					taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
					custoManutencao = custoManutencao + taCusto;
				}
				
				
				//total horas para a manutenção
				query = manager.createNativeQuery("select sum (to_number(replace(h.frsdhr,'.',','))) from pmp_hora h"+
						" where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId()+")"+
						" and h.bgrp = '"+contrato.getBgrp()+"'"+
						" and substr(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substr(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'" +
						" and h.cptcd = 7504");
				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
				
				if(contrato.getIsSpot() != null && contrato.getIsSpot().equals("S")){
					Double valorHH = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue();
					valorHH = valorHH + ( (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorSpot().doubleValue() * valorHH) /100 );
					//standard.setCustoMo((BigDecimal.valueOf(valorHH)).add(BigDecimal.valueOf(contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())));
					custoManutencao = custoManutencao + valorHH;//valor de hh
					//custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmpSpot().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())*1);//valor de km vezes a quantidade de manutenções
					
					String arraySpot []= contrato.getQtdHorasDeslSpot().split(":");
					BigDecimal minSpot = BigDecimal.valueOf((Double.valueOf(arraySpot[1])/60));
					BigDecimal horasPdpSpot = BigDecimal.valueOf(Double.valueOf(arraySpot[0]));
					horasPdpSpot = horasPdpSpot.add(minSpot);
					custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHorasDesSpot().doubleValue() * horasPdpSpot.doubleValue())*1);//valor de km vezes a quantidade de manutenções
					
					
				}else{
					custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhPmp().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
					custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmp().doubleValue())*1);//valor de km vezes a quantidade de manutenções
				}
				
				PrecoBean bean = new PrecoBean();
//				bean.setParcela(1);
//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
//				result.add(bean);
				for(int i = 0; i < 10; i++){
					custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
					bean = new PrecoBean();
					bean.setParcela(i+1);
					bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
					result.add(bean);
					
				}
				return result;
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	public List<PrecoBean> findAllParcelasKIT2Custo(Long idContrato, String siglaCustomizacao){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			String complemento = "";
			String complementoSigla="";
			if(contrato.getIdConfigTracao() != null){
				complemento = " and (ocptmd  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ocptmd is null)";
				complemento += " and (JWKAPP  not in (select SIGLA_AC from PMP_SIGLA_AC where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or JWKAPP is null)"
				+ " and (ojbloc not in (select SIGLA_TRACAO from PMP_SIGLA_TRACAO where ID_CONFIG_TRACAO = "+contrato.getIdConfigTracao().getId()+") or ojbloc is null)";
			}
			if(siglaCustomizacao.length() > 0){
				complementoSigla =  " and ojbloc not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and JWKAPP not in ("+siglaCustomizacao+")";
				complementoSigla +=  " and ocptmd not in ("+siglaCustomizacao+")";
			}
			//Long frequencia = 0l;
			
				//BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
				Query query = manager.createQuery("From PmpContHorasStandard where idContrato.id = :id order by horasManutencao");
				query.setParameter("id", contrato.getId());
				List<PmpContHorasStandard> horasStandardList = query.getResultList();
				for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
					totalHorasManutencao += pmpContHorasStandard.getFrequencia();
				}
				for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
					//frequencia = pmpContHorasStandard.getFrequencia();
					//Query para peças com desconto PDP
					query = manager.createNativeQuery("select sum(m.landcs * m.dlrqty) from pmp_manutencao_preco_custo m"+
							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
							" and m.bgrp = '"+contrato.getBgrp()+"'"+
							" and substring(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							complemento + complementoSigla +
							" and substring(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
							//" and m.stno ="+Integer.valueOf(FILIAL));
					custoPecas = (BigDecimal)query.getSingleResult();
					//Double custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
					//custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
					custoManutencao = custoManutencao + custoPecas.doubleValue();
					
//					//Query sem desconto pdp
//					query = manager.createNativeQuery("select sum(m.unls) from pmp_manutencao m"+
//							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
//							" and m.bgrp = '"+contrato.getBgrp()+"'"+
//							" and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//							" and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
//					" and m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)");
//					custoPecas = (BigDecimal)query.getSingleResult();
//					custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
//					custoManutencao = custoManutencao + custoNordesteTotal;
				}
				//int totalManutencoes = 0;
				
				//totalHorasManutencao = frequencia * horasStandardList.size();
				
				//custo do TA
				if(contrato.getTa().equals("S")){
					Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhTaCusto().doubleValue());
					taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
					custoManutencao = custoManutencao + taCusto;
				}
				
				
				//total horas para a manutenção
				query = manager.createNativeQuery("select sum (to_number(replace(h.frsdhr,'.',','))) from pmp_hora h"+
						" where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId()+")"+
						" and h.bgrp = '"+contrato.getBgrp()+"'"+
						" and substring(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substring(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'" +
				" and h.cptcd = 7504");
				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorHhPmpCusto().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
				custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmPmp().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmPmpCusto().doubleValue())*1);//valor de km vezes a quantidade de manutenções
				
				
				PrecoBean bean = new PrecoBean();
//				bean.setParcela(1);
//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
//				result.add(bean);
				//for(int i = 0; i < 10; i++){
					//custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
					bean = new PrecoBean();
					bean.setParcela(1);
					bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
					result.add(bean);
					
				//}
				return result;
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	public List<PrecoBean> findAllParcelasRentalKIT2Custo(Long idContrato){
		
		EntityManager manager = null;
		PmpContrato contrato = new PmpContrato();
		Double custoManutencao = 0d;
		Long totalHorasManutencao = 0l;
		BigDecimal custoPecas = BigDecimal.ZERO;
		
		List<PrecoBean> result = new ArrayList<PrecoBean>();
		try{
			manager = JpaUtil.getInstance();
			contrato = manager.find(PmpContrato.class, idContrato);
			
			//Long frequencia = 0l;
		
				//BigDecimal custoNordeste = contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getCustoNordeste();
				Query query = manager.createQuery("From PmpContHorasStandard where idContrato.id = :id");
				query.setParameter("id", contrato.getId());
				List<PmpContHorasStandard> horasStandardList = query.getResultList();
				for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
					totalHorasManutencao += pmpContHorasStandard.getFrequencia();
				}
				for (PmpContHorasStandard pmpContHorasStandard : horasStandardList) {
					//frequencia = pmpContHorasStandard.getFrequencia();
					//Query para peças com desconto PDP
					query = manager.createNativeQuery("select sum(m.landcs * m.dlrqty) from pmp_manutencao_preco_custo m"+
							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
							" and m.bgrp = '"+contrato.getBgrp()+"'"+
							" and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
							" and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'");
							//" and m.stno ="+Integer.valueOf(FILIAL));
					custoPecas = (BigDecimal)query.getSingleResult();
					//Double custoNordesteTotal =  custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste
					//custoNordesteTotal = custoNordesteTotal - ((custoNordesteTotal * (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getDescPdp().doubleValue()))/100);//desconto PDP
					custoManutencao = custoManutencao + custoPecas.doubleValue();
					
//					//Query sem desconto pdp
//					query = manager.createNativeQuery("select sum(m.unls) from pmp_manutencao m"+
//							" where m.cptcd = '"+pmpContHorasStandard.getStandardJobCptcd()+"'"+
//							" and m.bgrp = '"+contrato.getBgrp()+"'"+
//							" and substr(m.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
//							" and substr(m.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'"+
//					" and m.bectyc not in (select pdp.descricao from pmp_desconto_pdp pdp)");
//					custoPecas = (BigDecimal)query.getSingleResult();
//					custoNordesteTotal = custoPecas.doubleValue() + ((custoPecas.doubleValue() * custoNordeste.doubleValue())/100);//aplicar custo nordeste	
//					custoManutencao = custoManutencao + custoNordesteTotal;
				}
				//int totalManutencoes = 0;
				
				//totalHorasManutencao = frequencia * horasStandardList.size();
				
				//custo do TA
				if(contrato.getTa().equals("S")){
					Double taCusto = (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getTaHoras().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhTa().doubleValue());
					taCusto = (totalHorasManutencao.intValue() / 2000) * taCusto;// total de revisoes de 2000 horas * custo do TA
					custoManutencao = custoManutencao + taCusto;
				}
				
				
				//total horas para a manutenção
				query = manager.createNativeQuery("select sum (to_number(replace(h.frsdhr,'.',','))) from pmp_hora h"+
						" where h.cptcd in (select hs.standard_job_cptcd from pmp_cont_horas_standard hs where id_contrato = "+contrato.getId()+")"+
						" and h.bgrp = '"+contrato.getBgrp()+"'"+
						" and substr(h.beqmsn,1,4) = '"+contrato.getPrefixo()+"'"+
						" and substr(h.beqmsn,5,10) between '"+contrato.getBeginRanger().substring(4, 9)+"' and '"+contrato.getEndRanger().substring(4, 9)+"'" +
				" and h.cptcd = 7504");
				BigDecimal totalHHManutencao = (BigDecimal)query.getSingleResult();
			
				custoManutencao = custoManutencao + (contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getHhRental().doubleValue() * totalHHManutencao.doubleValue());//valor de hh
				custoManutencao = custoManutencao + ((contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getKmRental().doubleValue() * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getValorKmRental().doubleValue())*1);//valor de km vezes a quantidade de manutenções
				
				
				PrecoBean bean = new PrecoBean();
//				bean.setParcela(1);
//				bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
//				result.add(bean);
				//for(int i = 0; i < 10; i++){
					//custoManutencao = custoManutencao + ((custoManutencao * contrato.getIdConfigManutencao().getIdConfiguracaoPreco().getJurosVenda().doubleValue())/100);
					bean = new PrecoBean();
					bean.setParcela(1);
					bean.setPreco("R$ "+ValorMonetarioHelper.formata("###,###.00", custoManutencao));
					result.add(bean);
					
				//}
				return result;
		
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	public List<MotNaoFecContratoBean> findAllMotNaoFecContrato(){
		
		EntityManager manager = null;
		List<MotNaoFecContratoBean> mnfcResultList = new ArrayList<MotNaoFecContratoBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("from PmpMotivosNaoFecContrato");
			List<PmpMotivosNaoFecContrato> mnfcList = query.getResultList();
			for (PmpMotivosNaoFecContrato mnfc : mnfcList) {
				MotNaoFecContratoBean bean = new MotNaoFecContratoBean();
				bean.setDescricao(mnfc.getDescricao());
				bean.setId(mnfc.getId().longValue());
				bean.setSigla(mnfc.getSigla());
				mnfcResultList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return mnfcResultList;
	}

	public List<ContratoComercialBean> 	findAllContratosAbertos(){
		
		EntityManager manager = null;
		List<ContratoComercialBean> result = new ArrayList<ContratoComercialBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("from PmpContrato where idStatusContrato.id in(select id from PmpStatusContrato where sigla = 'CA')");
			List<PmpContrato> mnfcList = query.getResultList();
			for (PmpContrato contrato : mnfcList) {
				ContratoComercialBean bean = new ContratoComercialBean();
				bean.fromBean(contrato, usuarioBean);
				result.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return result;
	}
	
	public Boolean removerContrato(Long idContrato){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			PmpContrato contrato = manager.find(PmpContrato.class, idContrato);
			if(contrato.getIdStatusContrato().getSigla().equals("CA")){
				return false;
			}
			manager.getTransaction().begin();
			manager.remove(manager.find(PmpContrato.class, idContrato));
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
	
	public Boolean buscarStanderJob() {
		ResultSet rs = null;
		PreparedStatement prstmt = null;

		Connection con = null;
		//while(isLoadService){
			try {
//				InputStream in = getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
//				Properties prop = new Properties();
//				prop.load(in);
//				String url = prop.getProperty("dbs.url");
//				String user = prop.getProperty("dbs.user");
//				String password =prop.getProperty("dbs.password");
//				Class.forName(prop.getProperty("dbs.driver")).newInstance();

				con = com.pmp.util.ConectionDbs.getConnecton(); 

				prstmt = con.prepareStatement(SQL_FIND_ALL_PMP);
				rs = prstmt.executeQuery();
				EntityManager manager = null;
				//boolean isConnection = true;
				//while(isConnection){
					try {
						manager = JpaUtil.getInstance();

						manager.getTransaction().begin();
						Query query = manager.createNativeQuery("delete from pmp_manutencao");
						query.executeUpdate();
						query = manager.createNativeQuery("delete from pmp_manutencao_preco_custo");
						query.executeUpdate();
						query = manager.createNativeQuery("delete from pmp_hora");
						query.executeUpdate();
						query = manager.createNativeQuery("delete from pmp_range");
						query.executeUpdate();
//						query = manager.createNativeQuery("delete from pmp_cliente_pl");
//						query.executeUpdate();
						//manager.getTransaction().commit();
						while(rs.next()){
							//manager.getTransaction().begin();
							PmpManutencao pManutencao = new PmpManutencao();
					
							pManutencao.setBectyc(rs.getString("BECTYC"));
							pManutencao.setDlrqty(Long.valueOf((rs.getString("DLRQTY"))));
							pManutencao.setDs18(rs.getString("DS18"));
							pManutencao.setUncs(new BigDecimal(rs.getString("UNCS")));
							pManutencao.setUnls(new BigDecimal(rs.getString("UNLS")));
							pManutencao.setSos(rs.getString("SOS1"));
							pManutencao.setOcptmd(rs.getString("OCPTMD"));
							pManutencao.setOjbloc(rs.getString("OJBLOC"));
							pManutencao.setJwapp(rs.getString("JWKAPP"));
							pManutencao.setBgrp(rs.getString("BGRP"));
							pManutencao.setBeqmsn(rs.getString("BEQMSN"));
							pManutencao.setPano20(rs.getString("PANO20"));
							pManutencao.setCptcd(rs.getString("CPTCD"));
							manager.persist(pManutencao);
							//manager.getTransaction().commit();
						}
//						
						prstmt = con.prepareStatement(SQL_FIND_ALL_PMP_POR_FILIAL);
						rs = prstmt.executeQuery();
						while(rs.next()){
							//manager.getTransaction().begin();
							PmpManutencaoPrecoCusto pManutencao = new PmpManutencaoPrecoCusto();
							pManutencao.setBectyc(rs.getString("BECTYC"));
							pManutencao.setDlrqty(Long.valueOf((rs.getString("DLRQTY"))));
							pManutencao.setDs18(rs.getString("DS18"));
							pManutencao.setLandcs(new BigDecimal(rs.getString("LANDCS")));
							pManutencao.setOcptmd(rs.getString("OCPTMD"));
							pManutencao.setOjbloc(rs.getString("OJBLOC"));
							pManutencao.setJwapp(rs.getString("JWKAPP"));
							pManutencao.setBgrp(rs.getString("BGRP"));
							pManutencao.setBeqmsn(rs.getString("BEQMSN"));
							pManutencao.setPano20(rs.getString("PANO20"));
							pManutencao.setCptcd(rs.getString("CPTCD"));
							pManutencao.setSos(rs.getString("SOS1"));
							manager.persist(pManutencao);
							//manager.getTransaction().commit();
						}
				
						prstmt = con.prepareStatement(SQL_HORAS_PMP);
						rs = prstmt.executeQuery();
						while(rs.next()){
							//manager.getTransaction().begin();
							PmpHora pmpHora = new PmpHora();
							PmpHoraPK pmpHoraPK = new PmpHoraPK(rs.getString("JBCD"), rs.getString("CPTCD"),rs.getString("BEQMSN"), rs.getString("BGRP") );
							pmpHora.setEqmfcd(rs.getString("EQMFCD"));
							pmpHora.setEqmfmd(rs.getString("EQMFMD"));
							pmpHora.setFrsdhr(rs.getString("FRSDHR"));
							pmpHora.setTipoPm(rs.getString("TIPO_PM"));
							pmpHora.setPmpHoraPK(pmpHoraPK);
							manager.merge(pmpHora);
							//manager.getTransaction().commit();
						}
						
						prstmt = con.prepareStatement(SQL_RANGE);
						rs = prstmt.executeQuery();
						while(rs.next()){
							//manager.getTransaction().begin();
							PmpRange pmpRange = new PmpRange();
							PmpRangePK pmpHoraPK = new PmpRangePK(rs.getString("BEGSN2"), rs.getString("ENDSN2"),rs.getString("EQMFM2") );
							pmpRange.setBserno(rs.getString("BSERNO"));
							pmpRange.setEqmfcd(rs.getString("EQMFCD"));
							pmpRange.setPmpRangePK(pmpHoraPK);
							manager.merge(pmpRange);
							//manager.getTransaction().commit();
						}


						
//						prstmt = con.prepareStatement(SQL_FIND_CUSTOMER_MACHINE);
//						rs = prstmt.executeQuery();
//						while(rs.next()){
//							//manager.getTransaction().begin();
//							PmpClientePl clientePl = new PmpClientePl();
//							clientePl.setSerie(rs.getString("EQMFS2"));
//							clientePl.setCodCliente(rs.getString("CUNO"));
//							clientePl.setNomeCliente(rs.getString("CUNM"));
//							clientePl.setFilial(Long.valueOf(rs.getString("STN1")));
//							manager.merge(clientePl);
//							//manager.getTransaction().commit();
//						}
//						prstmt = con.prepareStatement(SQL_FIND_FILIAL);
//						rs = prstmt.executeQuery();
//						while(rs.next()){
//							//manager.getTransaction().begin();
//							TwFilial twFilial = new TwFilial();
//							twFilial.setStno(new Long(rs.getInt("STNO")));
//							twFilial.setStnm(rs.getString("STNM"));
//							try {
//								TwFilial filial = manager.find(TwFilial.class, twFilial.getStno());
//								filial.setStnm(twFilial.getStnm());
//								manager.merge(filial);
//							} catch (Exception e) {
//								e.printStackTrace();
//								manager.merge(twFilial);
//							}
//							//manager.getTransaction().commit();
//						}
						
						manager.getTransaction().commit();
						//isLoadService = false;
						EmailHelper emailHelper = new EmailHelper();
						emailHelper.sendSimpleMail("Serviço de importação de Standard Job executado", "Standard Job", "rodrigo@rdrsistemas.com.br");
						//System.out.println("ImportaÃ§Ã£o realizada com sucesso");
						//isConnection = false;
						return true;
					} catch (Exception e1) {
						if(manager != null && manager.getTransaction().isActive()){
							manager.getTransaction().rollback();
						}
						e1.printStackTrace();
						EmailHelper emailHelper = new EmailHelper();
						//emailHelper.sendSimpleMail("Erro ao executar a busca do STANDARD JOB no DBS", "ERRO STANDARD JOB", "ti.monitoramento@marcosa.com.br");
						emailHelper.sendSimpleMail("Erro ao executar a busca do STANDARD JOB no DBS da PESA", "ERRO STANDARD JOB", "rodrigo@rdrsistemas.com.br");
					} finally {
						if(manager != null && manager.isOpen()){
							manager.close();
						}
					}
				//}
			}catch (Exception e) {
				e.printStackTrace();
				EmailHelper emailHelper = new EmailHelper();
				//emailHelper.sendSimpleMail("Erro ao tentar conectar no DBS (STANDARD JOB)", "ERRO STANDARD JOB", "ti.monitoramento@marcosa.com.br");
				emailHelper.sendSimpleMail("Erro ao tentar conectar no DBS (STANDARD JOB) da PESA", "ERRO STANDARD JOB", "rodrigo@rdrsistemas.com.br");
			}finally{
				
				try {
					rs.close();
					prstmt.close();
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return false;
	}
	
	public Boolean sincronizarStanderJob(){
		boolean standerJob = this.buscarStanderJob();
		this.savePrecoZoho();
		return standerJob;

	}
	
	public Boolean terminarContrato(Long idContrato){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			Query query = manager.createNativeQuery("delete from PMP_CONT_HORAS_STANDARD where IS_EXECUTADO = 'N' and ID_OS_OPERACIONAL is null and ID_CONTRATO =:ID_CONTRATO");
			query.setParameter("ID_CONTRATO", idContrato);
			query.executeUpdate();
			query = manager.createNativeQuery("delete from PMP_CONT_HORAS_STANDARD_PLUS where IS_EXECUTADO = 'N' and ID_OS_OPERACIONAL is null and ID_CONTRATO =:ID_CONTRATO");
			query.setParameter("ID_CONTRATO", idContrato);
			query.executeUpdate();
			PmpContrato contrato = manager.find(PmpContrato.class, idContrato);
			contrato.setIsAtivo("I");
			contrato.setIdFuncionarioFinalizacao(this.usuarioBean.getMatricula());
			contrato.setDataFinalizacao(new Date());
			manager.merge(contrato);
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
		return true;
	}
	public String findIdEquipamento(String numeroSerie)throws Exception{
		ResultSet rs = null;
		PreparedStatement prstmt = null;

		Connection con = null;
		//while(isLoadService){
			try {
				con = com.pmp.util.ConectionDbs.getConnecton(); 
				String SQL = "select f.IDNO1 from "+IConstantAccess.LIB_DBS+".empeqpd0 f where f.EQMFS2 = '"+numeroSerie+"'";

				prstmt = con.prepareStatement(SQL);
				rs = prstmt.executeQuery();
				if(rs.next()){
					return rs.getString("IDNO1");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				prstmt.close();
				con.close();
			}
			return null;
	}
	public List<ClassificacaoBean> findAllClassificacao()throws Exception{
		EntityManager manager = null;
		List<ClassificacaoBean> classificacaoBeans = new ArrayList<ClassificacaoBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("from PmpClassificacaoContrato where sigla not in ('PLUS') order by descricao");
			List<PmpClassificacaoContrato> classificacaoContratos = query.getResultList();
			for (PmpClassificacaoContrato pmpClassificacaoContrato : classificacaoContratos) {
				ClassificacaoBean bean = new ClassificacaoBean();
				bean.formBean(pmpClassificacaoContrato);
				classificacaoBeans.add(bean);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return classificacaoBeans;
	}
	public List<ClassificacaoBean> findAllClassificacaoVendas()throws Exception{
		EntityManager manager = null;
		List<ClassificacaoBean> classificacaoBeans = new ArrayList<ClassificacaoBean>();
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("from PmpClassificacaoContrato order by descricao");
			List<PmpClassificacaoContrato> classificacaoContratos = query.getResultList();
			for (PmpClassificacaoContrato pmpClassificacaoContrato : classificacaoContratos) {
				ClassificacaoBean bean = new ClassificacaoBean();
				bean.formBean(pmpClassificacaoContrato);
				classificacaoBeans.add(bean);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return classificacaoBeans;
	}
	
	public Long validarRevisaoProxima(String numerContrato){
		EntityManager manager = null;
		try {
			manager = JpaUtil.getInstance();
			Query query = manager.createQuery("from PmpContrato where numeroContrato =:numeroContrato");
			query.setParameter("numeroContrato", numerContrato);
			if(query.getResultList().size() > 0){
				PmpContrato contrato = (PmpContrato)query.getSingleResult();
				query = manager.createNativeQuery("select id from Pmp_Cont_horas_standard where id_contrato =:id_contrato and is_executado = 'N' order by horas_Manutencao asc");
				query.setParameter("id_contrato", contrato.getId());
				if(query.getResultList().size() > 0){
					BigDecimal idContHorasStandard = (BigDecimal)query.getResultList().get(0);
					return idContHorasStandard.longValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
		return null;
	}
	
	public String verificarChamadoCampo(String serie){
		EntityManager manager = null;
		String chamado = "";
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			manager = JpaUtil.getInstance();

			String sqlPlAtivo = "select CASE WHEN count(*) = 0 THEN 'NÃO' ELSE 'SIM' END from pmp_maquina_pl pl"+
			" where pl.numero_serie =:numerSerie"+
			" and pl.latitude is not null"+
			" and pl.longitude is not null";
			Query queryMPlAtivo = manager.createNativeQuery(sqlPlAtivo);
			queryMPlAtivo.setParameter("numerSerie", serie.trim());
			String plAtivo = (String)queryMPlAtivo.getSingleResult();
			if("SIM".equals(plAtivo)){
				chamado = "Favor programar o técnico para conserta o PL.\n";
			}
			
			Query query = manager.createNativeQuery("select ch.ID from SC_STATUS_CHAMADO sa, SC_CHAMADO ch"+
													"	where ch.ID_STATUS_CHAMADO = sa.id"+
													"	and sa.SIGLA not in('TR')"+
													"   and ch.SERIE =:serie"+
													"	and ch.ID_OS is null");
			query.setParameter("serie", serie);
			if(query.getResultList().size() > 0){
				BigDecimal idChamado = (BigDecimal)query.getSingleResult();
				chamado += "A máquina possui chamado de campo número "+idChamado;
				
				query = manager.createNativeQuery("select CONVERT(varchar(10), ag.DATA_AGENDAMENTO, 103) DATA_AGENDAMENTO"+ 
												  "	from SC_AGENDAMENTO ag, SC_OS os, SC_CHAMADO ch, SC_STATUS_AGENDAMENTO sa"+
												  "	where os.ID = ag.ID_OS"+
												  "	and ch.ID_OS = ag.ID_OS"+
												  "	and ag.ID_STATUS_AGENDAMENTO = sa.ID"+
												  "	and sa.SIGLA in ('ET', 'AT', 'PA')"+
												  "	and ch.ID =:idChamado");
				query.setParameter("idChamado",idChamado);
				List<String> result = query.getResultList();
				String dataAg = "";
				for (String dataAgendamento : result) {
					dataAg += dataAgendamento+",";
				}
				if(dataAg.length() > 0){
					dataAg = dataAg.substring(0, dataAg.length() -1);
					chamado = chamado+" com data agendamento "+dataAg;
				}
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyMMdd");
			
			String SQL = "SELECT  WCLPIPS0.PIPNO  FROM "+IConstantAccess.LIB_DBS+".WCLPIPS0 WCLPIPS0"+
                    "  where WCLPIPS0.EQMFSN = '"+serie+"'"+
                    " and WCLPIPS0.wono = ''"+
                    " and WCLPIPS0.endd8 > "+dateFormat.format(new Date());
			conn = ConectionDbs.getConnecton();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL);
			String cartaCredito = "";
			while(rs.next()){
				cartaCredito += rs.getString("PIPNO")+",";
			}
			if(cartaCredito.length() > 0){
				cartaCredito = cartaCredito.substring(0, cartaCredito.length() - 1);
				chamado = chamado +". Possui carta de serviço "+cartaCredito;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(manager != null && manager.isOpen()){
				manager.close();
			}
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return chamado;
	}
public static void main(String[] args) {
	System.out.println("054500001".substring(4, 9));
}
}
