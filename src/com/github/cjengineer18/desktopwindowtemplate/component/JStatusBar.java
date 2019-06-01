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
package com.github.cjengineer18.desktopwindowtemplate.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.github.cjengineer18.desktopwindowtemplate.exception.InvalidParameterException;
import com.github.cjengineer18.desktopwindowtemplate.exception.UnavailableComponentException;

/**
 * Create a status bar that can be placed at the end of the window.
 * 
 * @author cjengineer18
 */
public class JStatusBar extends JPanel {

	private static final long serialVersionUID = 86442222L;

	/**
	 * Create a status bar with the width, height and the components to add.
	 * 
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @param components
	 *            The components which will be in the bar.
	 * 
	 * @throws InvalidParameterException
	 *             If some of the parameters are invalid.
	 */
	public JStatusBar(int width, int height, Component[] components) throws InvalidParameterException {
		super();
		createDefaultStatusBar(width, height, components);
	}

	/**
	 * Gets the panel where are all the content of the bar.
	 * 
	 * @return The {@link JPanel} with the content of the bar.
	 * 
	 * @throws UnavailableComponentException
	 *             If the component can't be extracted.
	 * 
	 * @see #setContentSection(JPanel)
	 */
	public final JPanel getContentSection() throws UnavailableComponentException {
		try {
			return (JPanel) getComponent(4);
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			throw new UnavailableComponentException(aioobe);
		}
	}

	/**
	 * Replace the content panel with the new one.
	 * 
	 * @param contentSection
	 *            The new content.
	 * 
	 * @throws InvalidParameterException
	 *             If {@code contentSection} don't meet to be added the bar or
	 *             some other internal error.
	 * 
	 * @see #getContentSection()
	 */
	public final void setContentSection(JPanel contentSection) throws InvalidParameterException {
		try {
			remove(4);
			add(contentSection);
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			throw new InvalidParameterException(aioobe);
		}
	}

	/**
	 * Extracts and get the last empty panel that remains in the bar.
	 * 
	 * @return The last panel.
	 * 
	 * @throws UnavailableComponentException
	 *             If the panel can't be extracted.
	 */
	public final JPanel extractLastPanel() throws UnavailableComponentException {
		try {
			JPanel content = getContentSection();
			return (JPanel) content.getComponent(content.getComponentCount() - 1);
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			throw new UnavailableComponentException("This status bar doesn't have a empty last panel!", aioobe);
		} catch (NullPointerException npe) {
			throw new UnavailableComponentException(npe);
		}
	}

	private final void createDefaultStatusBar(int width, int height, Component[] components)
			throws InvalidParameterException {
		if ((components != null) && ((width > 0) && (height > 0))) {
			JPanel statusBar = new JPanel();
			JPanel lastPanel = new JPanel();
			JPanel[] fragments = new JPanel[components.length];
			statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
			statusBar.setPreferredSize(new Dimension(width, height));
			lastPanel.setBorder(BorderFactory.createLoweredBevelBorder());
			setLayout(new BorderLayout());
			setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
			for (int i = 0; i < components.length; i++) {
				if (components[i] != null) {
					fragments[i] = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
					fragments[i].setBorder(BorderFactory.createLoweredBevelBorder());
					fragments[i].add(components[i]);
					statusBar.add(fragments[i]);
					statusBar.add(Box.createRigidArea(new Dimension(4, height)));
				} else
					throw new NullPointerException(String.format(Locale.ENGLISH, "components[%d]", i));
			}
			statusBar.add(lastPanel);
			add(padding(), BorderLayout.NORTH); // 0
			add(padding(), BorderLayout.SOUTH); // 1
			add(padding(), BorderLayout.EAST); // 2
			add(padding(), BorderLayout.WEST); // 3
			add(statusBar, BorderLayout.CENTER); // 4
		} else {
			if (components == null)
				throw new InvalidParameterException(new NullPointerException());
			else if ((width <= 0) || (height <= 0))
				throw new InvalidParameterException(
						String.format(Locale.ENGLISH, "Wrong size => (width = %d, height = %d)", width, height));
			else
				throw new InvalidParameterException("Unknown error");
		}
	}

	private static JPanel padding() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(2, 2));
		return panel;
	}

}
