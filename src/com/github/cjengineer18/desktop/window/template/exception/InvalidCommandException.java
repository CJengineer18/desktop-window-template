package com.github.cjengineer18.desktop.window.template.exception;

public class InvalidCommandException extends Exception {

	private static final long serialVersionUID = 0x1234L;

	public InvalidCommandException() {
		super("Invalid Command or Error in Command");
	}

	public InvalidCommandException(int invalidIndex) {
		super("Invalid Window Command" + invalidIndex);
	}

	public InvalidCommandException(int invalidIndex, Throwable consecuence) {
		super("Invalid Window Command" + invalidIndex, consecuence);
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Invalid Command or Error in Command!";
	}

}
