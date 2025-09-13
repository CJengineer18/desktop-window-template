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
package io.github.cjengineer18.desktopwindowtemplate.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import io.github.cjengineer18.desktopwindowtemplate.exception.ComponentBuildException;
import io.github.cjengineer18.desktopwindowtemplate.exception.UnavailableComponentException;

/**
 * Create a status bar that can be placed at the end of the window.
 * 
 * @author cjengineer18
 */
public class JStatusBar extends JPanel {

	private static final long serialVersionUID = 0x13C2;

	/**
	 * Create a status bar with the width, height and the components to add.
	 * 
	 * @param width      The width.
	 * @param height     The height.
	 * @param components The components which will be in the bar.
	 * 
	 * @throws ComponentBuildException
	 */
	public JStatusBar(int width, int height, Component[] components) throws ComponentBuildException {
		super(new BorderLayout());

		createDefaultStatusBar(width, height, components);
	}

	/**
	 * Gets the panel where are all the content of the bar.
	 * 
	 * @return The {@link JPanel} with the content of the bar.
	 * 
	 * @throws UnavailableComponentException
	 * 
	 * @see #setContentSection(JPanel)
	 */
	public final JPanel getContentSection() throws UnavailableComponentException {
		try {
			return (JPanel) getComponent(4);
		} catch (Exception exc) {
			throw new UnavailableComponentException(exc);
		}
	}

	/**
	 * Replace the content panel with the new one.
	 * 
	 * @param contentSection The new content.
	 * 
	 * @throws UnavailableComponentException
	 * 
	 * @see #getContentSection()
	 */
	public final void setContentSection(JPanel contentSection) throws UnavailableComponentException {
		try {
			remove(4);
			add(contentSection);
		} catch (Exception exc) {
			throw new UnavailableComponentException(exc);
		}
	}

	/**
	 * Extracts and get the last empty panel that remains in the bar.
	 * 
	 * @return The last panel.
	 * 
	 * @throws UnavailableComponentException
	 */
	public final JPanel extractLastPanel() throws UnavailableComponentException {
		try {
			JPanel content = getContentSection();

			return (JPanel) content.getComponent(content.getComponentCount() - 1);
		} catch (Exception exc) {
			throw new UnavailableComponentException(exc);
		}
	}

	private final void createDefaultStatusBar(int width, int height, Component[] components)
			throws ComponentBuildException {
		if ((components != null) && ((width > 0) && (height > 0))) {
			JPanel statusBar = new JPanel();
			JPanel lastPanel = new JPanel();

			statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
			statusBar.setPreferredSize(new Dimension(width, height));
			lastPanel.setBorder(BorderFactory.createLoweredBevelBorder());

			setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));

			Arrays.asList(components).stream().filter(c -> c != null).forEach(c -> {
				JPanel fragment = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

				fragment.setBorder(BorderFactory.createLoweredBevelBorder());
				fragment.add(c);

				statusBar.add(fragment);
				statusBar.add(Box.createRigidArea(new Dimension(4, height)));
			});

			statusBar.add(lastPanel);

			Arrays.asList(BorderLayout.NORTH, BorderLayout.SOUTH, BorderLayout.EAST, BorderLayout.WEST)
					.forEach(cardinal -> {
						add(cardinal, padding());
					});

			add(statusBar, BorderLayout.CENTER);
		} else if (components == null) {
			throw new ComponentBuildException("Components cannot be null");
		} else if ((width <= 0) || (height <= 0)) {
			throw new ComponentBuildException(
					String.format(Locale.ENGLISH, "Wrong size => (width = %d, height = %d)", width, height));
		} else {
			throw new ComponentBuildException("Unknown error");
		}
	}

	// Private methods

	private static JPanel padding() {
		JPanel panel = new JPanel();

		panel.setPreferredSize(new Dimension(2, 2));

		return panel;
	}

}
