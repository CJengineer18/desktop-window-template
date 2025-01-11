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
package io.github.cjengineer18.desktopwindowtemplate.async.task;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import io.github.cjengineer18.desktopwindowtemplate.async.AsyncProcessLoading;
import io.github.cjengineer18.desktopwindowtemplate.component.AcceptCancelDialogFooter;
import io.github.cjengineer18.desktopwindowtemplate.dialog.JModalDialog;
import io.github.cjengineer18.desktopwindowtemplate.util.constants.BundleConstants;

/**
 * An utility method for async tasks. This class support arguments and can
 * return a result. A dialog appears to show the progress.
 * 
 * @author Cristian Jimenez
 *
 * @param <Input>  The argument's class.
 * @param <Output> The result's class.
 */
public abstract class AsyncTask<Input, Output> extends AbstractAsyncTask<Input, Output> {

	// Private constants

	private static final String DEFAULT_TITLE = ResourceBundle.getBundle(BundleConstants.PANELS_LOCALE)
			.getString("loadingTitle");

	// Public constants

	public static final int NONE = 0x0;
	public static final int INDETERMINATE = 0x01;
	public static final int CANCEL = 0x10;

	// Fields

	private Window parent;
	private Dialog dialog;
	private String title;
	private boolean enableCancel;
	private boolean indeterminate;
	private long step;

	// Constructors

	/**
	 * Creates a new async task. With this constructor you can create indeterminate
	 * progress task. Note what this version of AsyncTask still be return something,
	 * otherwise you must use {@code AsyncProcessLoading}.
	 * 
	 * @param parent A window parent. If {@code null}, a default frame is used.
	 * 
	 * @see AsyncProcessLoading
	 */
	public AsyncTask(Window parent) {
		this(parent, DEFAULT_TITLE, 0, 17);
	}

	/**
	 * Creates a new async task.
	 * 
	 * @param parent A window parent. If {@code null}, a default frame is used.
	 * @param step   The progress step.
	 */
	public AsyncTask(Window parent, long step) {
		this(parent, DEFAULT_TITLE, step, 16);
	}

	/**
	 * Creates a new async task.
	 * 
	 * @param parent A window parent. If {@code null}, a default frame is used.
	 * @param title  A title for the dialog title.
	 */
	public AsyncTask(Window parent, String title) {
		this(parent, title, 0, 17);
	}

	/**
	 * Creates a new async task.
	 * 
	 * @param parent  A window parent. If {@code null}, a default frame is used.
	 * @param options The options for the task. Only this options are accepted:
	 *                {@code NONE}, {@code AT_CANCEL}, {@code AT_INDETERMINATE}. The
	 *                options can be combined.
	 * 
	 * @see #NONE
	 * @see #INDETERMINATE
	 * @see #CANCEL
	 */
	public AsyncTask(Window parent, int options) {
		this(parent, DEFAULT_TITLE, 0, options);
	}

	/**
	 * Creates a new async task.
	 * 
	 * @param parent  A window parent. If {@code null}, a default frame is used.
	 * @param title   A title for the dialog title.
	 * @param step    The progress step.
	 * @param options The options for the task. Only this options are accepted:
	 *                {@code NONE}, {@code AT_CANCEL}, {@code AT_INDETERMINATE}. The
	 *                options can be combined.
	 * 
	 * @see #NONE
	 * @see #INDETERMINATE
	 * @see #CANCEL
	 */
	public AsyncTask(Window parent, String title, long step, int options) {
		this.parent = parent;
		this.step = step;
		this.title = title;
		this.enableCancel = (options & CANCEL) != 0;
		this.indeterminate = (options & INDETERMINATE) != 0;
		this.result = null;
	}

	// Methods
	// For override methods, see parent's documentation.

	// Public methods

	@Override
	@SafeVarargs
	public final void execute(Input... inputs) {
		try {
			worker = new AsyncWorker<Input, Output>(this, inputs);
			dialog = new Dialog();

			worker.execute();
			dialog.setVisible(true);
		} catch (Exception exc) {
			handleError(exc);
		}
	}

	// Protected methods

	@Override
	protected final void finish(Output result) {
		dialog.dispose();

		if (worker.hasError()) {
			handleError(worker.getError());
		} else {
			this.result = result;

			done(result);
		}
	}

	/**
	 * Get the window parent
	 * 
	 * @return The parent.
	 */
	protected final Window getParent() {
		return parent;
	}

	/**
	 * Adds the step as progress.
	 */
	protected final void addStep() {
		addDelta(step);
	}

	/**
	 * Adds a progress delta.
	 * 
	 * @param delta The progress change.
	 */
	protected final void addDelta(long delta) {
		dialog.addDelta(delta);
	}

	/**
	 * Updates the dialog's message.
	 * 
	 * @param message The new message.
	 */
	protected final void updateMessage(String message) {
		dialog.updateMessage(message);
	}

	// Private classes

	private class Dialog extends JModalDialog {

		private static final long serialVersionUID = 1L;

		JProgressBar jpb;
		JLabel messageLabel;

		Dialog() throws Exception {
			super(parent, title, JModalDialog.CENTER);

			createNewInstance();
		}

		@Override
		protected void afterLoadArea() throws Exception {
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		}

		@Override
		protected void workArea() throws Exception {
			Container container = getContentPane();
			ResourceBundle panels = ResourceBundle.getBundle(BundleConstants.PANELS_LOCALE);
			AcceptCancelDialogFooter footer = new AcceptCancelDialogFooter(this,
					AcceptCancelDialogFooter.ACDF_CENTER_BUTTONS
							| (enableCancel ? AcceptCancelDialogFooter.ACDF_CANCEL : 0));
			GridBagConstraints gbc = new GridBagConstraints();
			int insetX = 6;
			int insetY = 2;

			messageLabel = new JLabel(panels.getString("loadingMessage"));

			if (enableCancel) {
				footer.onCancel(e -> worker.cancel(true));
			}

			jpb = new JProgressBar();

			jpb.setPreferredSize(new Dimension(getWidth() - 22, 14));

			if (indeterminate) {
				jpb.setIndeterminate(true);
			} else {
				jpb.setMinimum(0);
				jpb.setMaximum(100);
				jpb.setStringPainted(true);
			}

			container.setLayout(new GridBagLayout());

			gbc.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets = new Insets(insetX, insetY, insetX, insetY);
			gbc.fill = GridBagConstraints.BOTH;

			container.add(messageLabel, gbc);

			gbc.anchor = GridBagConstraints.BASELINE_LEADING;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, insetY, 0, insetY);

			container.add(jpb, gbc);

			gbc.anchor = GridBagConstraints.BELOW_BASELINE_LEADING;
			gbc.gridy = 2;
			gbc.insets = new Insets(insetX, insetY, insetX, 0);

			container.add(footer, gbc);
		}

		void addDelta(long delta) {
			if (!indeterminate) {
				jpb.setValue(jpb.getValue() + (int) delta);
			}
		}

		void updateMessage(String message) {
			if (!indeterminate) {
				messageLabel.setText(message);
			}
		}

		private void createNewInstance() throws Exception {
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			int w = (int) Math.round(screen.getWidth() / 5.464);

			w = w <= AsyncProcessLoading.DIALOG_MAX_WIDTH ? w : AsyncProcessLoading.DIALOG_MAX_WIDTH;

			loadWorkArea(w, 100);
		}
	}

}
