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
package com.github.cjengineer18.desktopwindowtemplate.component.staticpanel;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * A waiting panel.
 * 
 * @see com.github.cjengineer18.desktopwindowtemplate.util.async.AsyncProcessLoading
 * 
 * @author Cristian Jimenez
 */
public class WaitingPanel extends JPanel {

	private static final long serialVersionUID = 40L;

	private String message;

	public WaitingPanel(String message) {
		super(new BorderLayout());
		this.message = message;
		createNewInstance();
	}

	private void createNewInstance() {
		add(BorderLayout.CENTER, new JLabel(message));
		JProgressBar bar = new JProgressBar();
		bar.setIndeterminate(true);
		add(BorderLayout.SOUTH, bar);
	}

}
