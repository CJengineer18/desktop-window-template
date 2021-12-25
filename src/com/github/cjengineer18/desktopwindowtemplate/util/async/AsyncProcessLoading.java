/* 
 * Copyright (c) 2019-2021 Cristian José Jiménez Diazgranados
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
package com.github.cjengineer18.desktopwindowtemplate.util.async;

import java.awt.Window;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import com.github.cjengineer18.desktopwindowtemplate.component.staticpanel.WaitingPanel;
import com.github.cjengineer18.desktopwindowtemplate.exception.AsyncProcessException;
import com.github.cjengineer18.desktopwindowtemplate.exception.UnknownAsyncProcessException;
import com.github.cjengineer18.desktopwindowtemplate.resources.constants.BundleConstants;
import com.github.cjengineer18.desktopwindowtemplate.util.factory.DialogMaker;

/**
 * An utility method for async process (no return, no progress monitoring).
 * 
 * @author Cristian Jimenez
 */
public abstract class AsyncProcessLoading {

	/**
	 * Load asynchronously a process. A loading dialog will appear.
	 * 
	 * @param parent
	 *            A window parent. If {@code null}, a default frame is used.
	 * @param run
	 *            The process.
	 * 
	 * @throws AsyncProcessException
	 *             If an Exception is thrown during the thread process.
	 */
	public static void loadAsyncProcess(Window parent, Runnable run) throws AsyncProcessException {
		ResourceBundle b = ResourceBundle.getBundle(BundleConstants.PANELS_LOCALE);
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
	 * 
	 * @throws AsyncProcessException
	 *             If an Exception is thrown during the thread process.
	 */
	public static void loadAsyncProcess(Window parent, Runnable run, String text) throws AsyncProcessException {
		ResourceBundle b = ResourceBundle.getBundle(BundleConstants.PANELS_LOCALE);
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
	 * 
	 * @throws AsyncProcessException
	 *             If an Exception is thrown during the thread process.
	 */
	public static void loadAsyncProcess(Window parent, Runnable run, String title, String text)
			throws AsyncProcessException {
		JDialog d = DialogMaker.makeDialog(parent, title, new WaitingPanel(text), null);
		AtomicReference<Throwable> threadException = new AtomicReference<Throwable>();

		Thread thread;
		Thread daemon;

		if (run instanceof Thread) {
			thread = (Thread) run;
		} else {
			thread = new Thread(run);
			thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

				@Override
				public void uncaughtException(Thread thread, Throwable throwable) {
					// TODO Auto-generated method stub
					thread.interrupt();

					if (throwable instanceof RuntimeException) {
						if (throwable.getCause() != null) {
							threadException.set(throwable.getCause());
						} else {
							threadException.set(new UnknownAsyncProcessException());
						}
					} else {
						threadException.set(throwable);
					}
				}

			});
		}

		daemon = new Thread(new Runnable() {

			@Override
			public void run() {
				while (thread.isAlive()) {
					// Wait until main thread finish
				}

				if (threadException.get() != null) {
					JOptionPane.showMessageDialog(parent, threadException.get().getMessage(), "Error in process",
							JOptionPane.ERROR_MESSAGE);
				}

				d.dispose();
			}

		});

		daemon.setDaemon(true);
		d.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		thread.start();
		daemon.start();
		d.setVisible(true);

		if (threadException.get() != null) {
			throw new AsyncProcessException(threadException.get());
		}
	}

}
