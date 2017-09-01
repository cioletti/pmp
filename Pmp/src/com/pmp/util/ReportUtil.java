package com.pmp.util;

public class ReportUtil {

	public String getBasePath() { 
        String path = this.getClass().getClassLoader().getResource("com/pmp/util").getPath();
        return path; 
    } 
	
	public static void main(String[] args) {
		System.out.println(new ReportUtil().getBasePath());
	}
}
