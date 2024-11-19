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
package io.github.cjengineer18.desktopwindowtemplate.component.staticpanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * A waiting panel.
 * 
 * @see com.github.cjengineer18.desktopwindowtemplate.util.async.AsyncProcessLoading
 * @see com.github.cjengineer18.desktopwindowtemplate.util.async.task.AsyncTask
 * 
 * @author Cristian Jimenez
 */
public class WaitingPanel extends JPanel {

	private static final long serialVersionUID = 40L;

	private String message;

	public WaitingPanel(String message, int width) {
		this(message, width, 0, 8);
	}

	public WaitingPanel(String message, int width, int topMargin, int bottomMargin) {
		super(new GridBagLayout());

		this.message = message;

		createNewInstance(width, topMargin, bottomMargin);
	}

	private void createNewInstance(int width, int topMargin, int bottomMargin) {
		JProgressBar bar = new JProgressBar();
		GridBagConstraints gbc = new GridBagConstraints();
		Insets insets = new Insets(topMargin, 4, bottomMargin, 4);

		bar.setIndeterminate(true);
		bar.setPreferredSize(new Dimension(width - 12, 14));

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = insets;

		add(new JLabel(message), gbc);

		gbc.gridy = 1;

		add(bar, gbc);
	}

}
