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
package com.github.cjengineer18.desktopwindowtemplate;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.github.cjengineer18.desktopwindowtemplate.exception.InvalidCommandException;
import com.github.cjengineer18.desktopwindowtemplate.exception.InvalidParameterException;

/**
 * Generic window for any desktop application.
 * 
 * @version v1.0.2
 * 
 * @author Cristian Jimenez
 */
public abstract class JGenericWindow extends JFrame implements Serializable {

	/* Private Constants */

	private static final long serialVersionUID = 68482L;
	private static final Dimension minimumSize = new Dimension(200, 200);

	/* Private Attributes */

	private ArrayList<WindowListener> listeners1;
	private ArrayList<WindowStateListener> listeners2;
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
		listeners1 = new ArrayList<WindowListener>();
		listeners2 = new ArrayList<WindowStateListener>();
	}

	// Abstract methods
	// This methods must be implemented by the classes
	// which is used as windows for it's correct use in
	// the application.

	/**
	 * Universal getter for any window's object.
	 * 
	 * @param g
	 *            Object index. If the index doesn't match any object, this
	 *            method returns {@code null}.
	 * 
	 * @return The selected object.
	 */
	public abstract Object singleGetter(int g);

	/**
	 * Universal setter for any window's object.
	 * 
	 * @param s
	 *            Object index.
	 * @param newObject
	 *            The new object to assign.
	 * 
	 * @throws InvalidCommandException
	 *             If the index is invalid (object doesn's exists).
	 */
	public abstract void singleSetter(int s, Object newObject) throws InvalidCommandException;

	/**
	 * Here's all the window build. This method is invoked from
	 * {@code loadWorkArea(String, int, int, boolean, int)}
	 * 
	 * @throws Exception
	 *             If any error.
	 * 
	 * @see #loadWorkArea(String, int, int, boolean, int)
	 */
	protected abstract void workArea() throws Exception;

	// Empty methods
	// These methods, by default, do not make or return anything, but is the
	// developer's choice override them.

	/**
	 * Universal getter for any object inside an array. This is the equivalent
	 * to use {@code vectorGetter(v)[index]}. By default, returns {@code null},
	 * but can be override.
	 * 
	 * @param v
	 *            Array index. If the index doesn't match any array, this method
	 *            returns {@code null}.
	 * @param index
	 *            Index of the object within the array. If the index doesn't
	 *            match any object, this method returns {@code null}.
	 * 
	 * @return The selected object.
	 * 
	 * @see #vectorGetter(int)
	 */
	public Object singleGetterV(int v, int index) {
		return null;
	}

	/**
	 * Universal setter for any object inside an array. By default, this method
	 * does nothing, but can be override.
	 * 
	 * @param v
	 *            Array index.
	 * @param index
	 *            Index of the object within the array.
	 * @param newObject
	 *            The new object to assign.
	 * 
	 * @throws InvalidCommandException
	 *             If any index is invalid.
	 */
	public void singleSetterV(int v, int index, Object newObject) throws InvalidCommandException {
	}

	/**
	 * Universal getter for any array. By default, this method return
	 * {@code null}, but can be override.
	 * 
	 * @param g
	 *            Array index. If the index is doesn't match any array, this
	 *            method returns {@code null}.
	 * 
	 * @return The selected array.
	 * 
	 * @see #singleGetterV(int, int)
	 */
	public Object[] vectorGetter(int g) {
		return null;
	}

	/**
	 * Universal setter for any array. By default, this method does nothing, but
	 * can be override.
	 * 
	 * @param v
	 *            Array index.
	 * @param newVector
	 *            The new array to assign.
	 * 
	 * @throws InvalidCommandException
	 *             If the index is invalid (the array doesn't exists).
	 */
	public void vectorSetter(int v, Object[] newVector) throws InvalidCommandException {
	}

	/**
	 * This method does any necessary action before create and show the window.
	 * This method is invoked from
	 * {@code loadWorkArea(String, int, int, boolean, int)}. By default, this
	 * method does nothing, but can be override.
	 * 
	 * @throws Exception
	 *             If any error.
	 * 
	 * @see #loadWorkArea(String, int, int, boolean, int)
	 */
	protected void beforeLoadArea() throws Exception {
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
	 * Invoke <b>only</b> inside the builder. Here an empty frame is created
	 * with the required specifications and filled as implemented by
	 * {@code beforeLoadArea()} and {@code workArea()}. Now the window position
	 * will be the center of the screen.
	 * 
	 * @param title
	 *            Window title.
	 * @param width
	 *            Window width. The minimum must be 200px.
	 * @param height
	 *            Window height. The minimum must be 200px.
	 * @param fixedWindow
	 *            Will the window be fixed size?
	 * @param typeClosingWindow
	 *            How should the window be closed?. Only one of three
	 *            constants:<br>
	 *            <ul>
	 *            <li>{@code NO_CONFIRM_AT_CLOSE}</li>
	 *            <li>{@code CONFIRM_AT_CLOSE}</li>
	 *            <li>{@code CLOSE_NOT_MAIN_WINDOW}</li>
	 *            </ul>
	 * 
	 * @throws Exception
	 *             <ul>
	 *             <li>({@link InvalidParameterException}) If the width or
	 *             height are less than the minimal, the title is null or the
	 *             value in {@code typeClosingWindow} doesn't match any of the
	 *             constants.</li>
	 *             <li>({@link Exception}) Some other exception defined by the
	 *             developer.</li>
	 *             </ul>
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
		if (((width >= minimumSize.getWidth()) && (height >= minimumSize.getHeight())) && (title != null)) {
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			setTitle(title);
			originalSize = new Dimension(width, height);
			JFrame dummy = new JFrame();
			dummy.setLayout(null);
			dummy.pack();
			Insets windowInsets = (Insets) dummy.getInsets().clone();
			realSize = createWorkArea(originalSize, windowInsets);
			setSize(realSize);
			setPreferredSize(realSize);
			int x = (int) ((screen.getWidth() / 2) - (realSize.getWidth() / 2));
			int y = (int) ((screen.getHeight() / 2) - (realSize.getHeight() / 2));
			setLocation(x, y);
			setResizable(!fixedWindow);
			tolerableMinimumSize(windowInsets);
			beforeLoadArea();
			switch (typeClosingWindow) {
			// NO_CONFIRM_AT_CLOSE
			case 0:
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				break;
			// CONFIRM_AT_CLOSE
			case 1:
				setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				break;
			// CLOSE_NOT_MAIN_WINDOW
			case 2:
				setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				break;
			// ERROR! Neither of the 3!
			default:
				throw new InvalidParameterException(
						String.format(Locale.ENGLISH, "typeClosingWindow = %d?", typeClosingWindow));
			}
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						workArea();
						pack();
						setVisible(true);
					} catch (Exception exc) {
						Logger.getLogger(JGenericWindow.class.getName()).log(Level.SEVERE, exc.getMessage(), exc);
					}
				}

			});
		} else {
			if ((width <= 0) || (height <= 0))
				throw new InvalidParameterException(
						String.format(Locale.ENGLISH, "Wrong size => (width = %d, height = %d)", width, height));
			else if (title == null)
				throw new InvalidParameterException(new NullPointerException("title"));
		}
	}

	/**
	 * Add the listener to the window.
	 * 
	 * @param listeners
	 *            Objects which listen the window changes.
	 * 
	 * @throws InvalidParameterException
	 *             if {@code listeners} is {@code null}.
	 * 
	 * @see #enableListeners()
	 */
	protected final void addListeners(WindowListener... listeners) throws InvalidParameterException {
		if (listeners != null) {
			if (listeners.length > 0) {
				for (WindowListener wl : listeners) {
					if (wl != null)
						listeners1.add(wl);
					else {
						Logger.getLogger(JGenericWindow.class.getName()).log(Level.WARNING,
								"A listener is null. Ignoring...");
					}
				}
			} else
				throw new InvalidParameterException("No listeners to add!");
		} else
			throw new InvalidParameterException(new NullPointerException());
	}

	/**
	 * Add the listener to the window.
	 * 
	 * @param listeners
	 *            Objects which listen the window changes.
	 * 
	 * @throws InvalidParameterException
	 *             if {@code listeners} is {@code null}.
	 * 
	 * @see #enableListeners()
	 */
	protected final void addListeners(WindowStateListener... listeners) throws InvalidParameterException {
		if (listeners != null) {
			if (listeners.length != 0) {
				for (WindowStateListener wsl : listeners) {
					if (wsl != null)
						listeners2.add(wsl);
					else {
						Logger.getLogger(JGenericWindow.class.getName()).log(Level.WARNING,
								"An listener is null. Ignoring...");
					}
				}
			} else
				throw new InvalidParameterException("No listeners to add!");
		} else
			throw new InvalidParameterException(new NullPointerException());
	}

	/**
	 * Enable the listeners added by {@code addListeners(WindowListener...)} and
	 * {@code addListeners(WindowStateListener...)}.
	 * 
	 * @see #addListeners(WindowListener...)
	 * @see #addListeners(WindowStateListener...)
	 */
	protected final void enableListeners() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (!listeners1.isEmpty()) {
					for (WindowListener wl : listeners1)
						addWindowListener(wl);
				}
				if (!listeners2.isEmpty()) {
					for (WindowStateListener wsl : listeners2)
						addWindowStateListener(wsl);
				}
			}

		});
	}

	/**
	 * Create and add a menu bar for the current window.
	 * 
	 * @param menus
	 *            The menus which will be in the window.
	 * 
	 * @return {@code true} if the menu was added successfully, {@code false}
	 *         otherwise.
	 */
	protected final boolean insertMainMenuBar(JMenu... menus) {
		boolean success = true;
		if ((menus != null) && (menus.length > 0)) {
			JMenuBar jmb = new JMenuBar();
			for (JMenu jm : menus) {
				if (jm != null)
					jmb.add(jm);
			}
			if (success)
				setJMenuBar(jmb);
		} else
			success = false;
		return success;
	}

	/**
	 * Maximize the window. This only has an effect when the window doesn't have
	 * a fixed size.
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
	private void tolerableMinimumSize(Insets insets) {
		setMinimumSize(createWorkArea(minimumSize, insets));
	}

	/*
	 * Create the work area for the window.
	 */
	private Dimension createWorkArea(Dimension originalSize, Insets insets) {
		int[] insetValues = new int[2];
		String os = System.getProperty("os.name");

		// FIXME Some OS restrict the actual workspace to assign them to the
		// FIXME border and the title bar.
		// FIXME Depending on the OS, we compensate for the missing part by
		// FIXME adding the border size to the area.

		if (os.contains("Windows")) {
			// FIXME Windows adds 2 extra pixels to the borders, causing some
			// FIXME errors in custom graphics (games).
			// FIXME However, this can change between Windows versions.
			insetValues[0] = insets.top - 2;
			insetValues[1] = insets.left - 2;
		} else if (os.contains("Linux")) {
			// FIXME Untested on all distros
			insetValues[0] = 0;
			insetValues[1] = 0;
		}

		// FIXME Untested on Mac (OSX)

		int realWidth = ((int) originalSize.getWidth()) + insetValues[1];
		int realHeight = ((int) originalSize.getHeight()) + insetValues[0];
		return new Dimension(realWidth, realHeight);
	}

}
