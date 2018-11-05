package com.github.cjengineer18.desktop.window.template.exception;

public class UnavailableComponentException extends Exception {

	private static final long serialVersionUID = 14L;

	public UnavailableComponentException(Object error, Throwable exc) {
		super("Unavailable Component" + error.toString(), exc);
	}

	public UnavailableComponentException(Throwable exc) {
		super("Unavailable Component", exc);
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Unavailable Component";
	}

}
