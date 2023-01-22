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
 * A progress panel.
 * 
 * @see com.github.cjengineer18.desktopwindowtemplate.util.async.AsyncTask
 * 
 * @author Cristian Jimenez
 */
public class ProgressPanel extends JPanel {

	private static final long serialVersionUID = 42L;

	private JLabel message;
	private JProgressBar bar;

	public ProgressPanel(String message) {
		super(new BorderLayout());
		this.message = new JLabel(message);
		createNewInstance();
	}

	public void setMessage(String newMessage) {
		message.setText(newMessage);
	}

	public void setProgress(int progress) {
		bar.setValue(progress);
	}

	public void grow(int delta) {
		bar.setValue(bar.getValue() + delta);
	}

	public void restart() {
		bar.setValue(0);
	}

	private void createNewInstance() {
		add(BorderLayout.CENTER, message);
		bar = new JProgressBar();
		bar.setValue(0);
		bar.setStringPainted(true);
		add(BorderLayout.SOUTH, bar);
	}

}
