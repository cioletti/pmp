package com.pmp.business;

import java.util.List;

import com.pmp.bean.CriticidadeHorasBean;

public class AprovalHelper {

	public static void popularArrayCriticidadeHS(List<CriticidadeHorasBean> result){
		for(int i = 0; i < 6; i++){
			CriticidadeHorasBean bean = new CriticidadeHorasBean();
			bean.setHoras(i);
			if(i == 5){
				bean.setLABOTIINFERIOR("2");
				bean.setLABOTISUPERIOR("1");
				bean.setIsDivisorHoraExtra(true);
			} else {
				bean.setLABOTIINFERIOR("2");
				bean.setLABOTISUPERIOR("1");
				bean.setIsDivisorHoraExtra(false);
			}
			result.add(bean);
		}
		for(int i = 6; i < 9; i++){
			CriticidadeHorasBean bean = new CriticidadeHorasBean();
			bean.setHoras(i);
			if(i == 8){
				bean.setLABOTIINFERIOR("1");
				bean.setLABOTISUPERIOR("");
				bean.setIsDivisorHoraExtra(true);
			} else {
				bean.setLABOTIINFERIOR("1");
				bean.setLABOTISUPERIOR("");
				bean.setIsDivisorHoraExtra(false);
			}
			result.add(bean);
		}
		for(int i = 18; i < 23; i++){
			CriticidadeHorasBean bean = new CriticidadeHorasBean();
			bean.setHoras(i);
			if(i == 18 || i == 22){
				bean.setLABOTIINFERIOR("1");
				bean.setLABOTISUPERIOR("2");
				bean.setIsDivisorHoraExtra(true);
			} else {
				bean.setLABOTIINFERIOR("1");
				bean.setIsDivisorHoraExtra(false);
			}
			result.add(bean);
		}
		for(int i = 23; i < 24; i++){
			CriticidadeHorasBean bean = new CriticidadeHorasBean();
			bean.setHoras(i);
	
			bean.setLABOTIINFERIOR("2");
			bean.setLABOTISUPERIOR("2");
			bean.setIsDivisorHoraExtra(false);
	
			result.add(bean);
		}
	}

	//	private void popularArrayCriticidadeHS(){
	//		for(int i = 0; i < 6; i++){
	//			CriticidadeHorasBean bean = new CriticidadeHorasBean();
	//			bean.setHoras(i);
	//			if(i == 5){
	//				bean.setLABOTIINFERIOR("2");
	//				bean.setLABOTISUPERIOR("");
	//				bean.setIsDivisorHoraExtra(true);
	//			} else {
	//				bean.setLABOTIINFERIOR("2");
	//				bean.setLABOTISUPERIOR("");
	//				bean.setIsDivisorHoraExtra(false);
	//			}
	//			result.add(bean);
	//		}
	//		for(int i = 18; i < 23; i++){
	//			CriticidadeHorasBean bean = new CriticidadeHorasBean();
	//			bean.setHoras(i);
	//			if(i == 18 || i == 22){
	//				bean.setLABOTIINFERIOR("1");
	//				bean.setLABOTISUPERIOR("2");
	//				bean.setIsDivisorHoraExtra(true);
	//			} else {
	//				bean.setLABOTIINFERIOR("1");
	//				bean.setIsDivisorHoraExtra(false);
	//			}
	//			result.add(bean);
	//		}
	//		for(int i = 23; i < 24; i++){
	//			CriticidadeHorasBean bean = new CriticidadeHorasBean();
	//			bean.setHoras(i);
	//
	//			bean.setLABOTIINFERIOR("2");
	//			bean.setLABOTISUPERIOR("2");
	//			bean.setIsDivisorHoraExtra(false);
	//
	//			result.add(bean);
	//		}
	//	}
	//	
		public static CriticidadeHorasBean verificarCritidadeHoraExtra(int horas, List<CriticidadeHorasBean> result){
			for (CriticidadeHorasBean bean : result) {
				if(bean.getHoras() == horas){
					return bean;
				}
			}
			return null;
		}

}
