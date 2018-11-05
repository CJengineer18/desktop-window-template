package com.github.cjengineer18.desktop.window.template.exception;

public class InvalidParameterException extends Exception {

	private static final long serialVersionUID = 0xABCDL;

	public InvalidParameterException(Exception exc) {
		super("Invalid Parameter", exc);
	}

	public InvalidParameterException(String error) {
		super("Invalid Parameter" + error);
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Invalid Parameter!";
	}

}
