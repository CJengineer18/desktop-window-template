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
package io.github.cjengineer18.desktopwindowtemplate;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import io.github.cjengineer18.desktopwindowtemplate.exception.ComponentBuildException;
import io.github.cjengineer18.desktopwindowtemplate.util.Utilities;

/**
 * Generic window for any desktop application.
 * 
 * @version v1.3
 * 
 * @author Cristian Jimenez
 * 
 */
public abstract class JGenericWindow extends JFrame implements Serializable {

	/* Private Constants */

	private static final long serialVersionUID = 13;

	private static final Dimension MIN_WINDOW_SIZE = new Dimension(200, 200);

	/* Private Fields */

	private LinkedList<WindowListener> windowListeners;
	private LinkedList<WindowStateListener> windowStateListeners;
	private Dimension originalSize;
	private Dimension realSize;

	/* Public Constants */

	/**
	 * Main window only. Close the application directly.
	 */
	public static final int NO_CONFIRM_AT_CLOSE = 0;

	/**
	 * Main window only. You must show a confirmation message before close. The
	 * message depends of the implemented window's listener.
	 * 
	 * @see #enableListeners()
	 * @see #addListeners(WindowListener...)
	 * @see #addListeners(WindowStateListener...)
	 */
	public static final int CONFIRM_AT_CLOSE = 1;

	/** Closes the window normally, but without finish the application. */
	public static final int CLOSE_NOT_MAIN_WINDOW = 2;

	/* Default builder */

	/** Default builder. */
	public JGenericWindow() {
		super();

		windowListeners = new LinkedList<WindowListener>();
		windowStateListeners = new LinkedList<WindowStateListener>();
	}

	// Abstract methods
	// This methods must be implemented by the classes
	// which is used as windows for it's correct use in
	// the application.

	/**
	 * Here's all the window build. This method is invoked from
	 * {@code loadWorkArea(String, int, int, boolean, int)}.
	 * 
	 * @throws Exception
	 * 
	 * @see #loadWorkArea(String, int, int, boolean, int)
	 */
	protected abstract void workArea() throws Exception;

	// Empty methods
	// These methods, by default, do not make or return anything, but is the
	// developer's choice override them.

	/**
	 * This method does any necessary action before create and show the window. This
	 * method is invoked from {@code loadWorkArea(String, int, int, boolean, int)}.
	 * By default, this method does nothing, but can be override.
	 * 
	 * @throws Exception
	 * 
	 * @see #loadWorkArea(String, int, int, boolean, int)
	 */
	protected void beforeLoadArea() throws Exception {
		// empty
	}

	/**
	 * This method does any necessary action after create and show the window. This
	 * method is invoked from {@code loadWorkArea(String, int, int, boolean, int)}.
	 * By default, this method does nothing, but can be override.
	 * 
	 * @throws Exception
	 * 
	 * @see #loadWorkArea(String, int, int, boolean, int)
	 */
	protected void afterLoadArea() throws Exception {
		// empty
	}

	// Common methods
	// This methods is common for all classes which
	// extends this class.

	/**
	 * Gets the original window size (work area).
	 * 
	 * @return The window's original assigned size.
	 */
	public final Dimension getOriginalSize() {
		return originalSize;
	}

	/**
	 * Gets the real window size (work area + insets).
	 * 
	 * @return The window's real size.
	 */
	public final Dimension getRealSize() {
		return realSize;
	}

	/** Show the window (again). */
	public final void showWindow() {
		setVisible(true);
	}

	/** Hide the window without close it. */
	public final void hideWindow() {
		setVisible(false);
	}

	/** Restore and show again the window. */
	public final void restore() {
		setSize(realSize);
		setPreferredSize(realSize);
		pack();
		setVisible(true);
	}

	/**
	 * Maximize the window from outside. This only has an effect when the window
	 * doesn't have a fixed size.
	 * 
	 * @see #maximizeWindow()
	 * @see #loadWorkArea(String, int, int, boolean, int)
	 */
	public final void maximizeWindowFromOutside() {
		maximizeWindow();
	}

	/**
	 * Invoke <b>only</b> inside the builder. Here an empty frame is created with
	 * the required specifications and filled as implemented by
	 * {@code beforeLoadArea()} and {@code workArea()}. Now the window position will
	 * be the center of the screen.
	 * 
	 * @param title             Window title.
	 * @param width             Window width. The minimum must be 200px.
	 * @param height            Window height. The minimum must be 200px.
	 * @param fixedWindow       Will the window be fixed size?
	 * @param typeClosingWindow How should the window be closed?. Only one of three
	 *                          constants:<br>
	 *                          <ul>
	 *                          <li>{@code NO_CONFIRM_AT_CLOSE}</li>
	 *                          <li>{@code CONFIRM_AT_CLOSE}</li>
	 *                          <li>{@code CLOSE_NOT_MAIN_WINDOW}</li>
	 *                          </ul>
	 * 
	 * @throws Exception
	 * 
	 * @see #NO_CONFIRM_AT_CLOSE
	 * @see #CONFIRM_AT_CLOSE
	 * @see #CLOSE_NOT_MAIN_WINDOW
	 * 
	 * @see #beforeLoadArea()
	 * @see #workArea()
	 */
	protected final void loadWorkArea(String title, int width, int height, boolean fixedWindow, int typeClosingWindow)
			throws Exception {
		AtomicReference<Exception> asyncException = new AtomicReference<Exception>();

		Dimension screen;
		int x;
		int y;

		title = Objects.requireNonNull(title);

		if (((width >= MIN_WINDOW_SIZE.getWidth()) && (height >= MIN_WINDOW_SIZE.getHeight())) && (title != null)) {
			screen = Toolkit.getDefaultToolkit().getScreenSize();

			originalSize = new Dimension(width, height);
			realSize = Utilities.createWorkArea(originalSize);

			setTitle(title);
			setSize(realSize);
			setPreferredSize(realSize);

			x = (int) ((screen.getWidth() / 2) - (realSize.getWidth() / 2));
			y = (int) ((screen.getHeight() / 2) - (realSize.getHeight() / 2));

			setLocation(x, y);
			setResizable(!fixedWindow);
			tolerableMinimumSize();
			beforeLoadArea();

			switch (typeClosingWindow) {
			case JGenericWindow.NO_CONFIRM_AT_CLOSE:
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				break;
			case JGenericWindow.CONFIRM_AT_CLOSE:
				setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				break;
			case JGenericWindow.CLOSE_NOT_MAIN_WINDOW:
				setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				break;
			// ERROR! None of the 3!
			default:
				throw new ComponentBuildException(
						String.format(Locale.ENGLISH, "typeClosingWindow = %d?", typeClosingWindow));
			}

			SwingUtilities.invokeLater(() -> {
				try {
					workArea();
					pack();
					setVisible(true);
					executeAfterLoadArea();
				} catch (Exception exc) {
					asyncException.set(exc);
				}
			});

			if (asyncException.get() != null) {
				throw asyncException.get();
			}
		} else if ((width <= 0) || (height <= 0)) {
			throw new ComponentBuildException(
					String.format(Locale.ENGLISH, "Wrong size => (width = %d, height = %d)", width, height));
		}
	}

	/**
	 * Add the listener to the window.
	 * 
	 * @param listeners Objects which listen the window changes.
	 * 
	 * 
	 * @see #enableListeners()
	 */
	protected final void addListeners(WindowListener... listeners) {
		if (listeners != null && listeners.length > 0) {
			Arrays.asList(listeners).stream().filter(l -> l != null).forEach(l -> windowListeners.add(l));
		}
	}

	/**
	 * Add the listener to the window.
	 * 
	 * @param listeners Objects which listen the window changes.
	 * 
	 * @see #enableListeners()
	 */
	protected final void addListeners(WindowStateListener... listeners) {
		if (listeners != null && listeners.length > 0) {
			Arrays.asList(listeners).stream().filter(l -> l != null).forEach(l -> windowStateListeners.add(l));
		}
	}

	/**
	 * Enable the listeners added by {@code addListeners(WindowListener...)} and
	 * {@code addListeners(WindowStateListener...)}.
	 * 
	 * @see #addListeners(WindowListener...)
	 * @see #addListeners(WindowStateListener...)
	 */
	protected final void enableListeners() {
		SwingUtilities.invokeLater(() -> {
			windowListeners.forEach(l -> addWindowListener(l));
			windowStateListeners.forEach(l -> addWindowStateListener(l));
		});
	}

	/**
	 * Create and add a menu bar for the current window.
	 * 
	 * @param menus The menus which will be in the window.
	 */
	protected final void insertMainMenuBar(JMenu... menus) {
		if ((menus != null) && (menus.length > 0)) {
			JMenuBar jmb = new JMenuBar();

			Arrays.asList(menus).stream().filter(m -> m != null).forEach(m -> jmb.add(m));

			setJMenuBar(jmb);
		}
	}

	/**
	 * Maximize the window. This only has an effect when the window doesn't have a
	 * fixed size.
	 * 
	 * @see #maximizeWindowFromOutside()
	 * @see #loadWorkArea(String, int, int, boolean, int)
	 */
	protected final void maximizeWindow() {
		if (isResizable()) {
			setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
	}

	// Private Methods
	// Methods that work in this class.

	/*
	 * Adjust the minimum size to the minimum tolerable size.
	 */
	private void tolerableMinimumSize() {
		setMinimumSize(Utilities.createWorkArea(MIN_WINDOW_SIZE));
	}

	// Execute before the window loaded and show.
	private void executeAfterLoadArea() throws Exception {
		afterLoadArea();
		restore();
	}

}
