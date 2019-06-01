/* 
 * Copyright (c) 2018 Cristian José Jiménez Diazgranados
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.cjengineer18.desktopwindowtemplate.util.factory;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.github.cjengineer18.desktopwindowtemplate.exception.InvalidParameterException;

/**
 * Factory to create popup menus.
 * 
 * @author Cristian Jimenez
 */
public class PopupMenuFactory {

	/** Indicates where's the separator. */
	public static final String SEPARATOR = "_separator";

	/**
	 * Create a contextual menu ready to use in some component. The menu will share
	 * the same listener for all options, whose functions will be defined by
	 * {@code listener}.
	 * 
	 * @param listener The object that will listen to the menu items.
	 * @param content  The items to add.
	 * 
	 * @return A {@link JPopupMenu} with all the items added and with the listener
	 *         implemented.
	 * 
	 * @throws InvalidParameterException In case any of the parameters is null.
	 */
	public static JPopupMenu createPopupMenu(ActionListener listener, Object... content)
			throws InvalidParameterException {
		try {
			JPopupMenu menu = new JPopupMenu();
			JMenuItem item;
			for (Object obj : content) {
				if (obj instanceof JMenu)
					menu.add((JMenu) obj);
				else {
					if (obj.toString().equals(SEPARATOR))
						menu.addSeparator();
					else {
						item = new JMenuItem(obj.toString());
						item.addActionListener(listener);
						menu.add(item);
					}
				}
			}
			return menu;
		} catch (NullPointerException npe) {
			throw new InvalidParameterException(npe);
		}
	}

	/**
	 * Create a context menu ready to use in a component. Each option of the menu
	 * will have its own listener, so that each option performs a different
	 * function.
	 * 
	 * @param listeners An array with all the listeners available.
	 * @param content   The items to add.
	 * 
	 * @return A {@link JPopupMenu} with all the items added and with the listener
	 *         implemented.
	 * 
	 * @throws InvalidParameterException In case any of the parameters is null or
	 *                                   {@code listeners.length != content.length}.
	 */
	public static JPopupMenu createPopupMenu(ActionListener[] listeners, String[] content)
			throws InvalidParameterException {
		try {
			JPopupMenu menu = new JPopupMenu();
			JMenuItem item;
			for (int i = 0; i < content.length; i++) {
				item = new JMenuItem(content[i].toString());
				item.addActionListener(listeners[i]);
				menu.add(item);
			}
			return menu;
		} catch (NullPointerException | ArrayIndexOutOfBoundsException exc) {
			throw new InvalidParameterException(exc);
		}
	}

	/**
	 * Create a context menu ready to use in a component. This menu will share the
	 * same listener for all the options, whose functions will be defined by
	 * {@code listener} and that will receive the commands defined in
	 * {@code commands}. You should be aware that if there is a sub-menu or a
	 * separator in {@code content [i]}, then the value in {@code commands [i]} must
	 * be {@code null}.
	 * 
	 * @param listener The object that will listen to the menu items.
	 * @param content  The items to add.
	 * @param commands The available commands.
	 * 
	 * @return A {@link JPopupMenu} with all the items added and with the listener
	 *         and commands implemented.
	 * 
	 * @throws InvalidParameterException In case any of the parameters is null or
	 *                                   {@code listeners.length != content.length}.
	 * 
	 * @see #createPopupMenu(ActionListener, Object...)
	 * @see #SEPARATOR
	 */
	public static JPopupMenu createPopupMenu(ActionListener listener, Object[] content, String[] commands)
			throws InvalidParameterException {
		JPopupMenu jpm = createPopupMenu(listener, content);
		try {
			for (int i = 0; i < content.length; i++) {
				if (testContent(content[i]) && (commands[i] != null))
					((JMenuItem) jpm.getComponent(i)).setActionCommand(commands[i]);
			}
		} catch (ArrayIndexOutOfBoundsException | NullPointerException exc) {
			throw new InvalidParameterException(exc);
		}
		return jpm;
	}
	
	/*
	 * Check if the content isn't a menu or a separator.
	 */
	private static boolean testContent(Object obj) {
		return (!((obj instanceof JMenu) || obj.toString().equals(SEPARATOR)));
	}

}
