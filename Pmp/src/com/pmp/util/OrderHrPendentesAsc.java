package com.pmp.util;

import java.util.Comparator;

import com.pmp.bean.OperacionalBean;

public class OrderHrPendentesAsc implements Comparator{

	public int compare(Object o1, Object o2) {
		return (Integer.valueOf(((OperacionalBean)o1).getHorasPendentes())).compareTo(Integer.valueOf(((OperacionalBean)o2).getHorasPendentes()));
	}

}
