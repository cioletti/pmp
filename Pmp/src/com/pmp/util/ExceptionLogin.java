package com.pmp.util;

public class ExceptionLogin extends Exception{
	
	public ExceptionLogin(String message) {
		super(message);
	}
	private static final long serialVersionUID = -2717799058984565665L;
	public Boolean isLogin = true;

}
