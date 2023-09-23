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
package io.github.cjengineer18.desktopwindowtemplate.async;

import java.awt.Window;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import io.github.cjengineer18.desktopwindowtemplate.component.staticpanel.WaitingPanel;
import io.github.cjengineer18.desktopwindowtemplate.exception.AsyncProcessException;
import io.github.cjengineer18.desktopwindowtemplate.exception.UnknownAsyncProcessException;
import io.github.cjengineer18.desktopwindowtemplate.util.constants.BundleConstants;
import io.github.cjengineer18.desktopwindowtemplate.util.factory.DialogMaker;

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
	 * @param runnable
	 *            The process. If the process is a {@code Thread} subclass, you
	 *            must implement it's own {@code UncaughtExceptionHandler}.
	 *            Otherwise a default {@code UncaughtExceptionHandler} will be
	 *            implemented in a new {@code Thread} object.
	 * 
	 * @throws AsyncProcessException
	 *             If an {@link Exception} is thrown during the async process.
	 * 
	 * @see UncaughtExceptionHandler
	 */
	public static void loadAsyncProcess(Window parent, Runnable runnable) throws AsyncProcessException {
		ResourceBundle bundle = ResourceBundle.getBundle(BundleConstants.PANELS_LOCALE);

		loadAsyncProcess(parent, runnable, bundle.getString("loadingTitle"), bundle.getString("loadingMessage"));
	}

	/**
	 * Load asynchronously a process. A loading dialog will appear.
	 * 
	 * @param parent
	 *            A window parent. If {@code null}, a default frame is used.
	 * @param runnable
	 *            The process. If the process is a {@code Thread} subclass, you
	 *            must implement it's own {@code UncaughtExceptionHandler}.
	 *            Otherwise a default {@code UncaughtExceptionHandler} will be
	 *            implemented in a new {@code Thread} object.
	 * @param message
	 *            A message that will appear in the loading dialog.
	 * 
	 * @throws AsyncProcessException
	 *             If an {@link Exception} is thrown during the async process.
	 * 
	 * @see UncaughtExceptionHandler
	 */
	public static void loadAsyncProcess(Window parent, Runnable runnable, String message) throws AsyncProcessException {
		ResourceBundle bundle = ResourceBundle.getBundle(BundleConstants.PANELS_LOCALE);

		loadAsyncProcess(parent, runnable, bundle.getString("loadingTitle"), message);
	}

	/**
	 * Load asynchronously a process. A loading dialog will appear.
	 * 
	 * @param parent
	 *            A window parent. If {@code null}, a default frame is used.
	 * @param runnable
	 *            The process. If the process is a {@code Thread} subclass, you
	 *            must implement it's own {@code UncaughtExceptionHandler}.
	 *            Otherwise a default {@code UncaughtExceptionHandler} will be
	 *            implemented in a new {@code Thread} object.
	 * @param title
	 *            A title for the dialog.
	 * @param message
	 *            A message that will appear in the loading dialog.
	 * 
	 * @throws AsyncProcessException
	 *             If an {@link Exception} is thrown during the async process.
	 * 
	 * @see UncaughtExceptionHandler
	 */
	public static void loadAsyncProcess(Window parent, Runnable runnable, String title, String message)
			throws AsyncProcessException {
		JDialog dialog = DialogMaker.makeDialog(parent, title, new WaitingPanel(message), null);
		AtomicReference<Throwable> threadException = new AtomicReference<Throwable>();

		Thread thread;
		Thread daemon;

		if (runnable instanceof Thread) {
			thread = (Thread) runnable;
		} else {
			thread = new Thread(runnable);

			thread.setUncaughtExceptionHandler((th, error) -> {
				if (error instanceof RuntimeException) {
					if (error.getCause() != null) {
						threadException.set(error.getCause());
					} else {
						threadException.set(new UnknownAsyncProcessException());
					}
				} else {
					threadException.set(error);
				}

				th.interrupt();
			});
		}

		// This daemon thread checks if the main thread was finish and close the
		// dialog when finished.
		daemon = new Thread(() -> {
			while (thread.isAlive()) {
				// Wait until main thread finish
			}

			dialog.dispose();
		});

		daemon.setDaemon(true);
		dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		thread.start();
		daemon.start();
		dialog.setVisible(true);

		// If an exception was thrown in the main thread, throw as a cause.
		if (threadException.get() != null) {
			throw new AsyncProcessException(threadException.get());
		}
	}

}
