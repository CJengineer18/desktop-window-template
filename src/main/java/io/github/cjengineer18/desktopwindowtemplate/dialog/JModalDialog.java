/* 
 * Copyright (c) 2018-2023 Cristian José Jiménez Diazgranados
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
package io.github.cjengineer18.desktopwindowtemplate.dialog;

import java.awt.Dimension;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import io.github.cjengineer18.desktopwindowtemplate.exception.InvalidParameterException;
import io.github.cjengineer18.desktopwindowtemplate.util.Utilities;

/**
 * Generic modal dialog for desktop applications.
 * 
 * @author CJengineer18
 *
 */
public abstract class JModalDialog extends JDialog {

	/* Private Constants */

	private static final long serialVersionUID = 1311L;

	/* Public Constants */

	public static final int CENTER = 0;
	public static final int TOP_LEFT = 1;

	/* Protected Fields */

	protected Window window;

	/* Private Fields */

	private Dimension originalSize;
	private Dimension realSize;
	private int location;

	/**
	 * Builds the dialog.
	 * 
	 * @param window The window parent.
	 * @param title  The dialog title.
	 */
	public JModalDialog(Window window, String title) throws Exception {
		this(window, title, TOP_LEFT);
	}

	/**
	 * Builds the dialog.
	 * 
	 * @param window   The window parent.
	 * @param title    The dialog title.
	 * @param location The dialog location. Must be one of this constants:
	 *                 {@code JModalDialog.CENTER}, {@code JModalDialog.TOP_LEFT}
	 */
	public JModalDialog(Window window, String title, int location) throws Exception {
		super(window, title);

		this.window = window;
		this.location = location;
	}

	// Abstract methods
	// This methods must be implemented by the classes
	// which is used as windows for it's correct use in
	// the application.

	/**
	 * Here's all the window build. This method is invoked from
	 * {@code loadWorkArea(int, int)}.
	 * 
	 * @throws Exception If any error.
	 * 
	 * @see #loadWorkArea(int, int)
	 */
	protected abstract void workArea() throws Exception;

	// Empty methods
	// These methods, by default, do not make or return anything, but is the
	// developer's choice override them

	/**
	 * This method does any necessary action before create and show the window. This
	 * method is invoked from {@code loadWorkArea(int, int)}. By default, this
	 * method does nothing, but can be override.
	 * 
	 * @throws Exception If any error.
	 * 
	 * @see #loadWorkArea(int, int)
	 */
	protected void beforeLoadArea() throws Exception {
		// empty
	}

	/**
	 * This method does any necessary action after create and show the window. This
	 * method is invoked from {@code loadWorkArea(int, int)}. By default, this
	 * method does nothing, but can be override.
	 * 
	 * @throws Exception If any error.
	 * 
	 * @see #loadWorkArea(int, int)
	 */
	protected void afterLoadArea() throws Exception {
		// empty
	}

	// Common methods
	// This methods is common for all classes which
	// extends this class.

	/** Restore and show again the window. */
	public final void restore() {
		setSize(realSize);
		setPreferredSize(realSize);
		pack();
	}

	/**
	 * Invoke <b>only</b> inside the builder. Here an empty frame is created with
	 * the required specifications and filled as implemented by
	 * {@code beforeLoadArea()} and {@code workArea()}. The position of the dialog
	 * is +10px to the bottom and +10px to the right according to the parent's
	 * top-left corner.
	 * 
	 * @param width  The width.
	 * @param height The height
	 * 
	 * @throws Exception If any error.
	 */
	protected final void loadWorkArea(int width, int height) throws Exception {
		originalSize = new Dimension(width, height);
		realSize = Utilities.createWorkArea(originalSize);

		int x;
		int y;

		setSize(realSize);
		setPreferredSize(realSize);
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setIconImages(window.getIconImages());
		beforeLoadArea();

		switch (location) {
		case CENTER:
			x = window.getX() + (int) ((window.getWidth() / 2) - (realSize.getWidth() / 2));
			y = window.getY() + (int) ((window.getHeight() / 2) - (realSize.getHeight() / 2));
			break;
		case TOP_LEFT:
			x = window.getX() + 10;
			y = window.getY() + 10;
			break;
		default:
			throw new InvalidParameterException("Unknown parameter for dialog location");
		}

		setLocation(x, y);

		SwingUtilities.invokeLater(() -> {
			try {
				workArea();
				pack();
				executeAfterLoadArea();
			} catch (Exception e) {
				Logger.getLogger(JModalDialog.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		});
	}

	// Private Methods
	// Methods that work in this class.

	// Execute before the window loaded and show.
	private void executeAfterLoadArea() throws Exception {
		afterLoadArea();
		restore();
	}

}
