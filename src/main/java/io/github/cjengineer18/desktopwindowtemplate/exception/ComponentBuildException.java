package io.github.cjengineer18.desktopwindowtemplate.exception;

public class ComponentBuildException extends Exception {

	private static final long serialVersionUID = 0x13E2;

	public ComponentBuildException(Exception exc) {
		super("Failed to build component", exc);
	}

	public ComponentBuildException(String error) {
		super("Failed to build component: " + error);
	}
}
