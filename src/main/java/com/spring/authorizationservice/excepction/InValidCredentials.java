package com.spring.authorizationservice.excepction;

@SuppressWarnings("serial")
public class InValidCredentials extends Exception {
	
	public InValidCredentials(String msg) {
		super(msg);
	}
	
	

}
