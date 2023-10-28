/* 
 * Copyright (c) 2018-2023 Cristian José Jiménez Diazgranados
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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Window;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import io.github.cjengineer18.desktopwindowtemplate.component.staticpanel.WaitingPanel;
import io.github.cjengineer18.desktopwindowtemplate.dialog.JModalDialog;
import io.github.cjengineer18.desktopwindowtemplate.exception.AsyncProcessException;
import io.github.cjengineer18.desktopwindowtemplate.exception.UnknownAsyncProcessException;
import io.github.cjengineer18.desktopwindowtemplate.util.constants.BundleConstants;

/**
 * An utility method for async process (no return, no progress monitoring).
 * 
 * @author Cristian Jimenez
 */
public abstract class AsyncProcessLoading {

	// The Thread Pool
	private static final ExecutorService THE_POOL = Executors.newCachedThreadPool();

	// Public methods

	/**
	 * Load asynchronously a process. A loading dialog will appear.
	 * 
	 * @param parent   A window parent. If {@code null}, a default frame is used.
	 * @param runnable The process.
	 * 
	 * @throws AsyncProcessException If an {@link Exception} is thrown during the
	 *                               async process.
	 */
	public static void loadAsyncProcess(Window parent, Runnable runnable) throws AsyncProcessException {
		ResourceBundle bundle = ResourceBundle.getBundle(BundleConstants.PANELS_LOCALE);

		loadAsyncProcess(parent, runnable, bundle.getString("loadingTitle"), bundle.getString("loadingMessage"));
	}

	/**
	 * Load asynchronously a process. A loading dialog will appear.
	 * 
	 * @param parent   A window parent. If {@code null}, a default frame is used.
	 * @param runnable The process.
	 * @param message  A message that will appear in the loading dialog.
	 * 
	 * @throws AsyncProcessException If an {@link Exception} is thrown during the
	 *                               async process.
	 */
	public static void loadAsyncProcess(Window parent, Runnable runnable, String message) throws AsyncProcessException {
		ResourceBundle bundle = ResourceBundle.getBundle(BundleConstants.PANELS_LOCALE);

		loadAsyncProcess(parent, runnable, bundle.getString("loadingTitle"), message);
	}

	/**
	 * Load asynchronously a process. A loading dialog will appear.
	 * 
	 * @param parent   A window parent. If {@code null}, a default frame is used.
	 * @param runnable The process.
	 * @param title    A title for the dialog.
	 * @param message  A message that will appear in the loading dialog.
	 * 
	 * @throws AsyncProcessException If an {@link Exception} is thrown during the
	 *                               async process.
	 */
	public static void loadAsyncProcess(Window parent, Runnable runnable, String title, String message)
			throws AsyncProcessException {
		loadAsyncProcess(parent, Executors.callable(runnable, true), title, message);
	}

	// Private methods

	/**
	 * Load asynchronously a process. A loading dialog will appear.
	 * 
	 * @param parent  A window parent. If {@code null}, a default frame is used.
	 * @param call    The callable representing the process.
	 * @param title   A title for the dialog.
	 * @param message A message that will appear in the loading dialog.
	 * 
	 * @throws AsyncProcessException If an {@link Exception} is thrown during the
	 *                               async process.
	 */
	private static void loadAsyncProcess(Window parent, Callable<Boolean> call, String title, String message)
			throws AsyncProcessException {
		try {
			AtomicReference<Throwable> threadException = new AtomicReference<Throwable>();
			Dialog dialog = new Dialog(parent, title, message);

			// Call in a "daemon" thread for prevent the main thread to lock.
			THE_POOL.execute(() -> {
				try {
					THE_POOL.submit(call).get();

					dialog.dispose();
				} catch (InterruptedException | ExecutionException e) {
					Throwable thw = e.getCause();

					thw = thw instanceof RuntimeException ? thw.getCause() : thw;

					if (thw == null) {
						threadException.set(new UnknownAsyncProcessException());
					} else {
						threadException.set(thw);
					}
				}
			});

			// Show the dialog
			dialog.setVisible(true);

			// If an exception was thrown in the main thread, throw as a cause.
			if (threadException.get() != null) {
				throw new AsyncProcessException(threadException.get());
			}
		} catch (AsyncProcessException ape) {
			throw ape;
		} catch (Exception exc) {
			throw new AsyncProcessException(exc);
		}
	}

	// Private classes

	/* The Dialog */
	private static class Dialog extends JModalDialog {

		private static final long serialVersionUID = 1L;

		private String message;

		Dialog(Window window, String title, String message) throws Exception {
			super(window, title);

			this.message = message;

			loadWorkArea(250, 120);
		}

		@Override
		protected void afterLoadArea() throws Exception {
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		}

		@Override
		protected void workArea() throws Exception {
			Container container = getContentPane();
			String[] cardinals = { BorderLayout.NORTH, BorderLayout.SOUTH, BorderLayout.EAST, BorderLayout.WEST };

			container.setLayout(new BorderLayout());
			container.add(BorderLayout.CENTER, new WaitingPanel(message));

			for (String str : cardinals) {
				container.add(str, new JPanel());
			}
		}

	}

}
