/* 
 * Copyright (c) 2018-2022 Cristian José Jiménez Diazgranados
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.github.cjengineer18.desktopwindowtemplate.component.JStatusBar;
import com.github.cjengineer18.desktopwindowtemplate.exception.InvalidParameterException;
import com.github.cjengineer18.desktopwindowtemplate.exception.UnavailableComponentException;

/**
 * Factory for create predefined status bars.
 * 
 * @author cjengineer18
 */
public abstract class StatusBarFactory {

	/**
	 * Create a status bar with the width, height, orientation and assigned
	 * content.
	 * 
	 * @param width
	 * @param height
	 * @param rightToLeft
	 *            {@code true} so that the order of the components is from right
	 *            to left.
	 * @param components
	 * @return A {@link JStatusBar} ready to use.
	 * 
	 * @throws InvalidParameterException
	 *             In case some parameter does not comply and / or some internal
	 *             error.
	 * 
	 * @see #createStatusBar(int, Component...)
	 * @see #createStatusBar(int, int, Component...)
	 * @see #createStatusBar(int, boolean, Component...)
	 */
	public static JStatusBar createStatusBar(int width, int height, boolean rightToLeft, Component... components)
			throws InvalidParameterException {
		try {
			JStatusBar statusBar = createStatusBar(width, height, components);
			if (rightToLeft) {
				JPanel content = statusBar.getContentSection();
				content.setLayout(new BoxLayout(content, BoxLayout.LINE_AXIS));
				content.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
				statusBar.setContentSection(content);
			}
			return statusBar;
		} catch (UnavailableComponentException uce) {
			throw new InvalidParameterException(uce);
		}
	}

	/**
	 * Create a status bar with the width, height and assigned content. The
	 * orientation will be from left to right.
	 * 
	 * @param width
	 * @param height
	 * @param components
	 *            .
	 * 
	 * @return A {@link JStatusBar} ready to use.
	 * 
	 * @throws InvalidParameterException
	 *             In case some parameter does not comply and / or some internal
	 *             error.
	 * 
	 * @see #createStatusBar(int, Component...)
	 * @see #createStatusBar(int, boolean, Component...)
	 * @see #createStatusBar(int, int, boolean, Component...)
	 */
	public static JStatusBar createStatusBar(int width, int height, Component... components)
			throws InvalidParameterException {
		return new JStatusBar(width, height, components);
	}

	/**
	 * Create a status bar with the width, orientation and content assigned. The
	 * default height is 20px.
	 * 
	 * @param width
	 * @param rightToLeft
	 *            {@code true} so that the order of the components is from right
	 *            to left.
	 * @param components
	 * 
	 * @return A {@link JStatusBar} ready to use.
	 * 
	 * @throws InvalidParameterException
	 *             In case some parameter does not comply and / or some internal
	 *             error.
	 * 
	 * @see #createStatusBar(int, Component...)
	 * @see #createStatusBar(int, int, Component...)
	 * @see #createStatusBar(int, int, boolean, Component...)
	 */
	public static JStatusBar createStatusBar(int width, boolean rightToLeft, Component... components)
			throws InvalidParameterException {
		return createStatusBar(width, 20, rightToLeft, components);
	}

	/**
	 * Create a status bar with the width and content assigned. The default
	 * height is 20px. The orientation will be from left to right.
	 * 
	 * @param width
	 * @param components
	 * 
	 * @return A {@link JStatusBar} ready to use.
	 * 
	 * @throws InvalidParameterException
	 *             In case some parameter does not comply and / or some internal
	 *             error.
	 * 
	 * @see #createStatusBar(int, int, Component...)
	 * @see #createStatusBar(int, boolean, Component...)
	 * @see #createStatusBar(int, int, boolean, Component...)
	 */
	public static JStatusBar createStatusBar(int width, Component... components) throws InvalidParameterException {
		return createStatusBar(width, 20, components);
	}

	/**
	 * Create an empty status bar with the width and height assigned ready to
	 * use.
	 * 
	 * @param width
	 * @param height
	 * 
	 * @return A empty {@link JStatusBar}.
	 * 
	 * @throws InvalidParameterException
	 *             In case of internal error.
	 * 
	 * @see #createEmptyStatusBar(int)
	 */
	public static JStatusBar createEmptyStatusBar(int width, int height) throws InvalidParameterException {
		return createStatusBar(width, height, new Component[0]);
	}

	/**
	 * Create an empty status bar with the assigned width ready to use. The
	 * default height is 20px.
	 * 
	 * @param width
	 * 
	 * @return A empty {@link JStatusBar}.
	 * 
	 * @throws InvalidParameterException
	 *             In case of internal error.
	 * 
	 * @see #createEmptyStatusBar(int, int)
	 */
	public static JStatusBar createEmptyStatusBar(int width) throws InvalidParameterException {
		return createStatusBar(width, 20, new Component[0]);
	}

	/**
	 * Insert a component to the last panel of the status bar to modify.
	 * 
	 * @param statusBar
	 *            Status bar to modify.
	 * @param component
	 *            The component to add.
	 * 
	 * @return The same status bar.
	 * 
	 * @throws InvalidParameterException
	 *             In case {@code statusBar} or {@code component} are
	 *             {@code null} or if the component to be modified is not
	 *             available in the bar.
	 */
	public static JStatusBar insertComponentToLastPanel(JStatusBar statusBar, Component component)
			throws InvalidParameterException {
		try {
			JPanel lastPanel = statusBar.extractLastPanel();
			lastPanel.setLayout(new BorderLayout());
			if (statusBar.getComponentOrientation() == ComponentOrientation.LEFT_TO_RIGHT)
				lastPanel.add(new JPanel(), BorderLayout.WEST);
			else
				lastPanel.add(new JPanel(), BorderLayout.EAST);
			lastPanel.add(component, BorderLayout.CENTER);
			return statusBar;
		} catch (UnavailableComponentException | NullPointerException exc) {
			throw new InvalidParameterException(exc);
		}
	}

	/**
	 * Create an empty component for the status bar of the assigned width. The
	 * default height is 20px.
	 * 
	 * @param width.
	 * 
	 * @return A {@link Component} with the new space.
	 * 
	 * @see #createRigidSpace(int, int)
	 */
	public static Component createRigidSpace(int width) {
		return createRigidSpace(width, 20);
	}

	/**
	 * Create an empty component for the status bar of the assigned width and
	 * height.
	 * 
	 * @param width
	 * @param height
	 * 
	 * @return A {@link Component} with the new space.
	 * 
	 * @see #createRigidSpace(int)
	 */
	public static Component createRigidSpace(int width, int height) {
		return Box.createRigidArea(new Dimension(width, height));
	}

}
