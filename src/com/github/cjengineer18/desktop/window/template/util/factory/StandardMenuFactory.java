package com.github.cjengineer18.desktop.window.template.util.factory;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.github.cjengineer18.desktop.window.template.exception.InvalidParameterException;

/**
 * Factory for menus, either for a menu bar or for a sub-menu.
 * 
 * @author Cristian Jimenez
 */
public class StandardMenuFactory {

	/** Para indicar donde hay un separador. */
	public static final String SEPARATOR = "_separator";

	/**
	 * Create a menu ready to use in a menu bar or in a sub-menu. This menu will
	 * share the same listener for all options, whose functions will be defined by
	 * {@code listener}.
	 * 
	 * @param label    The menu's name.
	 * @param listener The object that will listen to the menu items.
	 * @param content  The items to add.
	 * 
	 * @return A {@link JMenu} with all the items added and with the listener
	 *         implemented.
	 * 
	 * @throws InvalidParameterException In case any of the parameters is null.
	 */
	public static JMenu createMenu(String label, ActionListener listener, Object... content)
			throws InvalidParameterException {
		try {
			JMenu jm = new JMenu(label);
			JMenuItem jmi;
			for (Object obj : content) {
				if (obj instanceof JMenu)
					jm.add((JMenu) obj);
				else {
					if (obj.toString().equals(SEPARATOR))
						jm.addSeparator();
					else {
						jmi = new JMenuItem(obj.toString());
						jmi.addActionListener(listener);
						jm.add(jmi);
					}
				}
			}
			return jm;
		} catch (NullPointerException npe) {
			throw new InvalidParameterException(npe);
		}
	}

	/**
	 * Create a menu ready to use in a menu bar or in a sub-menu. This menu will
	 * share the same listener for all the options, whose functions will be defined
	 * by {@code listener} and that will receive the commands defined in
	 * {@code commands}. You should be aware that if there is a submenu or a
	 * separator in {@code content [i]}, then the value in {@code commands [i]} must
	 * be {@code null}.
	 * 
	 * @param label    The menu's name.
	 * @param listener The object that will listen to the menu items.
	 * @param content  The items to add.
	 * @param commands The available commands.
	 * 
	 * @return A {@link JMenu} with all the items added and with the listener and
	 *         commands implemented.
	 * 
	 * @throws InvalidParameterException In case any of the parameters are null, not
	 *                                   valid or
	 *                                   {@code content.length != commands.length}.
	 * 
	 * @see #createMenu(String, ActionListener, Object...)
	 * @see #SEPARATOR
	 */
	public static JMenu createMenu(String label, ActionListener listener, Object[] content, String[] commands)
			throws InvalidParameterException {
		JMenu menu = createMenu(label, listener, content);
		try {
			for (int i = 0; i < content.length; i++) {
				if (testContent(content[i]) && (commands[i] != null))
					menu.getItem(i).setActionCommand(commands[i]);
			}
		} catch (ArrayIndexOutOfBoundsException | NullPointerException exc) {
			throw new InvalidParameterException(exc);
		}
		return menu;
	}

	/**
	 * Create a menu ready to use in a menu bar or in a sub-menu. Each option of the
	 * menu will have its own listener, so that each option performs a different
	 * function.
	 * 
	 * @param label     The menu's name.
	 * @param listeners An array with all the listeners available.
	 * @param content   The items to add.
	 * 
	 * @return A {@link JMenu} with all the items added and with the listener
	 *         implemented.
	 * 
	 * @throws InvalidParameterException In case one of the parameters is null or
	 *                                   that
	 *                                   {@code listeners.length != content.length}.
	 */
	public static JMenu createMenu(String label, ActionListener[] listeners, String[] content)
			throws InvalidParameterException {
		try {
			JMenu jm = new JMenu(label);
			JMenuItem jmi;
			for (int i = 0; i < listeners.length; i++) {
				jmi = new JMenuItem(content[i]);
				jmi.addActionListener(listeners[i]);
				jm.add(jmi);
			}
			return jm;
		} catch (NullPointerException | ArrayIndexOutOfBoundsException exc) {
			throw new InvalidParameterException(exc);
		}
	}

	private static boolean testContent(Object obj) {
		return (!((obj instanceof JMenu) || obj.toString().equals(SEPARATOR)));
	}

}
