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
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import io.github.cjengineer18.desktopwindowtemplate.component.staticpanel.WaitingPanel;
import io.github.cjengineer18.desktopwindowtemplate.dialog.JModalDialog;
import io.github.cjengineer18.desktopwindowtemplate.exception.AsyncProcessException;
import io.github.cjengineer18.desktopwindowtemplate.util.constants.BundleConstants;

/**
 * An utility method for async process (no return, no progress monitoring).
 * 
 * @author Cristian Jimenez
 */
public final class AsyncProcessLoading {

	// The thread exception reference
	private static final AtomicReference<Throwable> THREAD_ERROR = new AtomicReference<Throwable>();

	// The threads counter
	private static final AtomicLong COUNTER = new AtomicLong(0);

	// The thread pool
	private static final ExecutorService THE_POOL = Executors.newCachedThreadPool(new APLThreadFactory());

	// The dialog
	private static Dialog dialog;

	private AsyncProcessLoading() {
		throw new UnsupportedOperationException();
	}

	// Public methods

	/**
	 * Load asynchronously a process. A loading dialog will appear.
	 * 
	 * @param parent  A window parent. If {@code null}, a default frame is used.
	 * @param process The process.
	 * 
	 * @throws AsyncProcessException If an {@link Exception} is thrown during the
	 *                               async process.
	 */
	public static void loadAsyncProcess(Window parent, IProcess process) throws AsyncProcessException {
		ResourceBundle bundle = ResourceBundle.getBundle(BundleConstants.PANELS_LOCALE);

		loadAsyncProcess(parent, process, bundle.getString("loadingTitle"), bundle.getString("loadingMessage"));
	}

	/**
	 * Load asynchronously a process. A loading dialog will appear.
	 * 
	 * @param parent  A window parent. If {@code null}, a default frame is used.
	 * @param process The process.
	 * @param message A message that will appear in the loading dialog.
	 * 
	 * @throws AsyncProcessException If an {@link Exception} is thrown during the
	 *                               async process.
	 */
	public static void loadAsyncProcess(Window parent, IProcess process, String message) throws AsyncProcessException {
		ResourceBundle bundle = ResourceBundle.getBundle(BundleConstants.PANELS_LOCALE);

		loadAsyncProcess(parent, process, bundle.getString("loadingTitle"), message);
	}

	/**
	 * Load asynchronously a process. A loading dialog will appear.
	 * 
	 * @param parent  A window parent. If {@code null}, a default frame is used.
	 * @param process The process.
	 * @param title   A title for the dialog.
	 * @param message A message that will appear in the loading dialog.
	 * 
	 * @throws AsyncProcessException If an {@link Exception} is thrown during the
	 *                               async process.
	 */
	public static void loadAsyncProcess(Window parent, IProcess process, String title, String message)
			throws AsyncProcessException {
		loadAsyncProcess(parent, (Runnable) () -> {
			try {
				process.execute();
			} catch (Exception exc) {
				THREAD_ERROR.set(exc);
			} finally {
				dialog.dispose();
			}
		}, title, message);
	}

	// Private methods

	/**
	 * Load asynchronously a process. A loading dialog will appear.
	 * 
	 * @param parent  A window parent. If {@code null}, a default frame is used.
	 * @param invoker The runnable representing the process.
	 * @param title   A title for the dialog.
	 * @param message A message that will appear in the loading dialog.
	 * 
	 * @throws AsyncProcessException If an {@link Exception} is thrown during the
	 *                               async process.
	 */
	private static void loadAsyncProcess(Window parent, Runnable invoker, String title, String message)
			throws AsyncProcessException {
		try {
			dialog = new Dialog(parent, title, message);

			// Reset any older error
			resetThreadError();

			// Execute the process
			THE_POOL.execute(invoker);

			// Show the dialog
			dialog.setVisible(true);

			// If an exception was thrown in the main thread, throw as a cause.
			if (THREAD_ERROR.get() != null) {
				throw new AsyncProcessException(THREAD_ERROR.get());
			}
		} catch (AsyncProcessException ape) {
			throw ape;
		} catch (Exception exc) {
			throw new AsyncProcessException(exc);
		}
	}

	/**
	 * Reset the thread error reference.
	 */
	private static void resetThreadError() {
		THREAD_ERROR.set(null);
	}

	// Public classes

	/* The executable function */
	@FunctionalInterface
	public interface IProcess {

		void execute() throws Exception;

	}

	// Private classes

	/* The thread factory */
	private static class APLThreadFactory implements ThreadFactory {

		@Override
		public Thread newThread(Runnable run) {
			String id = String.format(Locale.ENGLISH, "%s-DaemonThread-%d", AsyncProcessLoading.class.getSimpleName(),
					COUNTER.incrementAndGet());
			Thread th = new Thread(run);

			th.setDaemon(true);
			th.setName(id);

			th.setUncaughtExceptionHandler((th0, tw0) -> {
				THREAD_ERROR.set(tw0);
			});

			return th;
		}

	}

	/* The dialog */
	private static class Dialog extends JModalDialog {

		private static final long serialVersionUID = 1L;

		private String message;

		Dialog(Window window, String title, String message) throws Exception {
			super(window, title, JModalDialog.CENTER);

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

			container.setLayout(new BorderLayout());
			container.add(BorderLayout.CENTER, new WaitingPanel(message));

			Arrays.asList(BorderLayout.SOUTH, BorderLayout.EAST, BorderLayout.WEST).forEach(cardinal -> {
				container.add(cardinal, new JPanel());
			});
		}

	}

}
