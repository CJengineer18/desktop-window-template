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
package io.github.cjengineer18.desktopwindowtemplate.factories;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import io.github.cjengineer18.desktopwindowtemplate.component.JStatusBar;
import io.github.cjengineer18.desktopwindowtemplate.exception.ComponentBuildException;
import io.github.cjengineer18.desktopwindowtemplate.exception.UnavailableComponentException;

/**
 * Factory for create predefined status bars.
 * 
 * @author cjengineer18
 */
public final class StatusBarFactory {

	private StatusBarFactory() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Create a status bar with the width, height, orientation and assigned content.
	 * 
	 * @param width
	 * @param height
	 * @param rightToLeft {@code true} so that the order of the components is from
	 *                    right to left.
	 * @param components
	 * @return A {@link JStatusBar} ready to use.
	 * 
	 * @throws ComponentBuildException
	 * @throws UnavailableComponentException
	 * 
	 * @see #createStatusBar(int, Component...)
	 * @see #createStatusBar(int, int, Component...)
	 * @see #createStatusBar(int, boolean, Component...)
	 */
	public static JStatusBar createStatusBar(int width, int height, boolean rightToLeft, Component... components)
			throws ComponentBuildException, UnavailableComponentException {
		JStatusBar statusBar = createStatusBar(width, height, components);

		if (rightToLeft) {
			JPanel content = statusBar.getContentSection();

			content.setLayout(new BoxLayout(content, BoxLayout.LINE_AXIS));
			content.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			statusBar.setContentSection(content);
		}

		return statusBar;
	}

	/**
	 * Create a status bar with the width, height and assigned content. The
	 * orientation will be from left to right.
	 * 
	 * @param width
	 * @param height
	 * @param components .
	 * 
	 * @return A {@link JStatusBar} ready to use.
	 * 
	 * @throws ComponentBuildException
	 * 
	 * @see #createStatusBar(int, Component...)
	 * @see #createStatusBar(int, boolean, Component...)
	 * @see #createStatusBar(int, int, boolean, Component...)
	 */
	public static JStatusBar createStatusBar(int width, int height, Component... components)
			throws ComponentBuildException {
		return new JStatusBar(width, height, components);
	}

	/**
	 * Create a status bar with the width, orientation and content assigned. The
	 * default height is 20px.
	 * 
	 * @param width
	 * @param rightToLeft {@code true} so that the order of the components is from
	 *                    right to left.
	 * @param components
	 * 
	 * @return A {@link JStatusBar} ready to use.
	 * 
	 * @throws ComponentBuildException
	 * @throws UnavailableComponentException
	 * 
	 * @see #createStatusBar(int, Component...)
	 * @see #createStatusBar(int, int, Component...)
	 * @see #createStatusBar(int, int, boolean, Component...)
	 */
	public static JStatusBar createStatusBar(int width, boolean rightToLeft, Component... components)
			throws ComponentBuildException, UnavailableComponentException {
		return createStatusBar(width, 20, rightToLeft, components);
	}

	/**
	 * Create a status bar with the width and content assigned. The default height
	 * is 20px. The orientation will be from left to right.
	 * 
	 * @param width
	 * @param components
	 * 
	 * @return A {@link JStatusBar} ready to use.
	 * 
	 * @throws ComponentBuildException
	 * 
	 * @see #createStatusBar(int, int, Component...)
	 * @see #createStatusBar(int, boolean, Component...)
	 * @see #createStatusBar(int, int, boolean, Component...)
	 */
	public static JStatusBar createStatusBar(int width, Component... components) throws ComponentBuildException {
		return createStatusBar(width, 20, components);
	}

	/**
	 * Create an empty status bar with the width and height assigned ready to use.
	 * 
	 * @param width
	 * @param height
	 * 
	 * @return A empty {@link JStatusBar}.
	 * 
	 * @throws ComponentBuildException
	 * 
	 * @see #createEmptyStatusBar(int)
	 */
	public static JStatusBar createEmptyStatusBar(int width, int height) throws ComponentBuildException {
		return createStatusBar(width, height, new Component[0]);
	}

	/**
	 * Create an empty status bar with the assigned width ready to use. The default
	 * height is 20px.
	 * 
	 * @param width
	 * 
	 * @return A empty {@link JStatusBar}.
	 * 
	 * @throws ComponentBuildException
	 * 
	 * @see #createEmptyStatusBar(int, int)
	 */
	public static JStatusBar createEmptyStatusBar(int width) throws ComponentBuildException {
		return createStatusBar(width, 20, new Component[0]);
	}

	/**
	 * Insert a component to the last panel of the status bar to modify.
	 * 
	 * @param statusBar Status bar to modify.
	 * @param component The component to add.
	 * 
	 * @return The same status bar.
	 * 
	 * @throws ComponentBuildException
	 * @throws UnavailableComponentException
	 */
	public static JStatusBar insertComponentToLastPanel(JStatusBar statusBar, Component component)
			throws ComponentBuildException, UnavailableComponentException {
		JPanel lastPanel = statusBar.extractLastPanel();

		lastPanel.setLayout(new BorderLayout());

		if (statusBar.getComponentOrientation() == ComponentOrientation.LEFT_TO_RIGHT) {
			lastPanel.add(new JPanel(), BorderLayout.WEST);
		} else {
			lastPanel.add(new JPanel(), BorderLayout.EAST);
		}

		lastPanel.add(component, BorderLayout.CENTER);

		return statusBar;
	}

	/**
	 * Create an empty component for the status bar of the assigned width. The
	 * default height is 20px.
	 * 
	 * @param width The width
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
	 * @param width  The width
	 * @param height The height
	 * 
	 * @return A {@link Component} with the new space.
	 * 
	 * @see #createRigidSpace(int)
	 */
	public static Component createRigidSpace(int width, int height) {
		return Box.createRigidArea(new Dimension(width, height));
	}

}
