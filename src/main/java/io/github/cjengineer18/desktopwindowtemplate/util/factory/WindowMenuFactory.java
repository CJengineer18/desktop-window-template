/* 
 * Copyright (c) 2018-2024 Cristian José Jiménez Diazgranados
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
package io.github.cjengineer18.desktopwindowtemplate.util.factory;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Factory for menus, either for a menu bar or for a sub-menu.
 * 
 * @author Cristian Jimenez
 */
public final class WindowMenuFactory {

	private WindowMenuFactory() {
		throw new UnsupportedOperationException();
	}

	/** Indicates where's the separator. */
	public static final String SEPARATOR = "_separator";

	/**
	 * Create a menu ready to use in a menu bar or in a sub-menu. This menu will
	 * share the same listener for all options, whose functions will be defined by
	 * {@code listener}.
	 * 
	 * @param label            The menu's name.
	 * @param menuOptionLabels The menu's list. {@code null} elements will be
	 *                         ignored.
	 * @param listener         The menu's global listener.
	 * 
	 * @return The new menu.
	 */
	public static JMenu createMenu(String label, List<Object> menuOptionLabels, ActionListener listener) {
		JMenu jm = new JMenu(label);

		Objects.requireNonNull(menuOptionLabels, "The options must not be null");
		Objects.requireNonNull(listener, "A global listener is required");

		menuOptionLabels.stream().filter(e -> e != null).forEach(obj -> {
			process(jm, obj, item -> {
				JMenuItem jmi = new JMenuItem(item.toString());

				jmi.addActionListener(listener);

				return jmi;
			});
		});

		return jm;
	}

	/**
	 * Create a menu ready to use in a menu bar or in a sub-menu. This menu will
	 * share the same listener for all the options, whose functions will be defined
	 * by {@code listener} and that will receive the commands defined in the
	 * {@code menuOptions} values for each key.
	 * 
	 * @param label       The menu's name.
	 * @param menuOptions The menu's list, each key represent a menu's element and
	 *                    each value their command.
	 * @param listener    The menu's global listener.
	 * 
	 * @return The new menu.
	 */
	public static JMenu createMenu(String label, Map<Object, String> menuOptions, ActionListener listener) {
		JMenu jm = new JMenu(label);

		Objects.requireNonNull(menuOptions, "The options must not be null");
		Objects.requireNonNull(listener, "A global listener is required");

		menuOptions.forEach((optionLabel, command) -> {
			process(jm, optionLabel, item -> {
				JMenuItem jmi = new JMenuItem(item.toString());

				jmi.setActionCommand(command);
				jmi.addActionListener(listener);

				return jmi;
			});
		});

		return jm;
	}

	/**
	 * Create a menu ready to use in a menu bar or in a sub-menu. This menu will
	 * share the same listener for all the options, whose functions will be defined
	 * by {@code listener} and that will have it's own listeners defined in the
	 * {@code menuOptions} values for each key.
	 * 
	 * @param label          The menu's name.
	 * @param menuOptionsThe menu's list, each key represent a menu's element and
	 *                       each value their listener
	 * 
	 * @return The new menu.
	 */
	public static JMenu createMenu(String label, Map<Object, ActionListener> menuOptions) {
		JMenu jm = new JMenu(label);

		Objects.requireNonNull(menuOptions, "The options must not be null");

		menuOptions.forEach((optionLabel, listener) -> {
			process(jm, optionLabel, item -> {
				JMenuItem jmi = new JMenuItem(item.toString());

				jmi.addActionListener(listener);

				return jmi;
			});
		});

		return jm;
	}

	/**
	 * Process the menu.
	 * 
	 * @param menu            The menu.
	 * @param menuItem        The menu item.
	 * @param menuItemFactory An custom action if {@code menuItem} is not a
	 *                        separator or an instance of {@code JMenu}. It must
	 *                        create a new {@code JMenuItem}.
	 */
	private static void process(JMenu menu, Object menuItem, Function<Object, JMenuItem> menuItemFactory) {
		JMenuItem jmi = null;

		if (menuItem instanceof JMenu) {
			menu.add((JMenu) menuItem);
		} else if (menuItem.equals(SEPARATOR)) {
			menu.addSeparator();
		} else {
			jmi = menuItemFactory.apply(menuItem);

			menu.add(jmi);
		}
	}

}
