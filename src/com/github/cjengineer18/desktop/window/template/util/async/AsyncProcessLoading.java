/* 
 * Copyright (c) 2019 Cristian José Jiménez Diazgranados
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
package com.github.cjengineer18.desktop.window.template.util.async;

import java.awt.Window;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import com.github.cjengineer18.desktop.window.template.component.WaitingPanel;
import com.github.cjengineer18.desktop.window.template.util.factory.DialogMaker;

/**
 * An utility method for async process (no return).
 * 
 * @author Cristian Jimenez
 */
public abstract class AsyncProcessLoading {

	private static final String BUNDLE_LOCALE = "com.github.cjengineer18.desktop.window.template.resources.bundle.loading";

	/**
	 * Load asynchronously a process. A loading dialog will appear.
	 * 
	 * @param parent
	 *            A window parent. If {@code null}, a default frame is used.
	 * @param run
	 *            The process.
	 */
	public static void loadAsyncProcess(Window parent, Runnable run) {
		ResourceBundle b = ResourceBundle.getBundle(BUNDLE_LOCALE);
		loadAsyncProcess(parent, run, b.getString("loadingTitle"), b.getString("loadingMessage"));
	}

	/**
	 * Load asynchronously a process. A loading dialog will appear.
	 * 
	 * @param parent
	 *            A window parent. If {@code null}, a default frame is used.
	 * @param run
	 *            The process.
	 * @param text
	 *            A text that will appear in the loading dialog.
	 */
	public static void loadAsyncProcess(Window parent, Runnable run, String text) {
		ResourceBundle b = ResourceBundle.getBundle(BUNDLE_LOCALE);
		loadAsyncProcess(parent, run, b.getString("loadingTitle"), text);
	}

	/**
	 * Load asynchronously a process. A loading dialog will appear.
	 * 
	 * @param parent
	 *            A window parent. If {@code null}, a default frame is used.
	 * @param run
	 *            The process.
	 * @param title
	 *            An title for the dialog.
	 * @param text
	 *            A text that will appear in the loading dialog.
	 */
	public static void loadAsyncProcess(Window parent, Runnable run, String title, String text) {
		JDialog d = DialogMaker.makeDialog(parent, title, new WaitingPanel(text), null);

		Thread thread;
		Thread daemon;

		if (run instanceof Thread) {
			thread = (Thread) run;
		} else {
			thread = new Thread(run);
		}

		daemon = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (thread.isAlive())
					;
				d.dispose();
			}

		});

		daemon.setDaemon(true);
		d.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		thread.start();
		daemon.start();
		d.setVisible(true);
	}

}
