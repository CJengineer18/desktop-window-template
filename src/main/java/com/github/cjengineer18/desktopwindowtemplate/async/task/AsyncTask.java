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
package com.github.cjengineer18.desktopwindowtemplate.async.task;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.github.cjengineer18.desktopwindowtemplate.async.AsyncProcessLoading;
import com.github.cjengineer18.desktopwindowtemplate.component.staticpanel.ProgressPanel;
import com.github.cjengineer18.desktopwindowtemplate.component.staticpanel.WaitingPanel;
import com.github.cjengineer18.desktopwindowtemplate.util.constants.BundleConstants;
import com.github.cjengineer18.desktopwindowtemplate.util.factory.DialogMaker;

/**
 * An utility method for async tasks. This class support arguments and can
 * return a result. A dialog appears to show the progress.
 * 
 * @author Cristian Jimenez
 *
 * @param <Input>
 *            The argument's class.
 * @param <Output>
 *            The result's class.
 */
public abstract class AsyncTask<Input, Output> extends AbstractAsyncTask<Input, Output> {

	// Fields

	private Window parent;
	private JDialog dialog;
	private JPanel panel;
	private String title;
	private boolean enableCancel;
	private boolean indeterminate;
	private int step;

	// Constructors

	/**
	 * Creates a new async task. With this constructor you can create
	 * indeterminate progress task. Note what this version of AsyncTask still be
	 * return something, otherwise you must use {@code AsyncProcessLoading}.
	 * 
	 * @param parent
	 *            A window parent. If {@code null}, a default frame is used.
	 * 
	 * @see AsyncProcessLoading
	 */
	public AsyncTask(Window parent) {
		this(parent, ResourceBundle.getBundle(BundleConstants.PANELS_LOCALE).getString("loadingTitle"), 0, true, true);
	}

	/**
	 * Creates a new async task.
	 * 
	 * @param parent
	 *            A window parent. If {@code null}, a default frame is used.
	 * @param step
	 *            The progress step.
	 */
	public AsyncTask(Window parent, int step) {
		this(parent, ResourceBundle.getBundle(BundleConstants.PANELS_LOCALE).getString("progressTitle"), step, false,
				true);
	}

	/**
	 * Creates a new async task.
	 * 
	 * @param parent
	 *            A window parent. If {@code null}, a default frame is used.
	 * @param title
	 *            A title for the dialog title.
	 * @param step
	 *            The progress step.
	 * @param indeterminate
	 *            If {@code true}, disables progress monitoring.
	 * @param enableCancel
	 *            If this process can be cancelled.
	 */
	public AsyncTask(Window parent, String title, int step, boolean indeterminate, boolean enableCancel) {
		this.parent = parent;
		this.step = step;
		this.title = title;
		this.enableCancel = enableCancel;
		this.indeterminate = indeterminate;
	}

	// Methods
	// For overrider methods, see parent's documentation.

	// Public methods

	@Override
	@SafeVarargs
	public final void execute(Input... inputs) {
		ResourceBundle buttons = ResourceBundle.getBundle(BundleConstants.BUTTONS_LOCALE);
		ResourceBundle panels = ResourceBundle.getBundle(BundleConstants.PANELS_LOCALE);
		JButton cancelButton = new JButton(buttons.getString("cancelButton"));

		worker = new AsyncWorker<Input, Output>(this, inputs);
		panel = indeterminate ? new WaitingPanel(panels.getString("loadingMessage")) : new ProgressPanel(new String());
		dialog = DialogMaker.makeDialog(parent, title, panel, enableCancel ? new JButton[] { cancelButton } : null);

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				worker.cancel(true);
			}

		});
		worker.execute();
		dialog.setVisible(true);
	}

	// Protected methods

	@Override
	protected final void finish(Output result) {
		dialog.dispose();
		this.result = result;
		done(result);
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
	 * @param delta
	 *            The progress change.
	 */
	protected final void addDelta(int delta) {
		if (!indeterminate) {
			((ProgressPanel) panel).grow(delta);
		}
	}

	/**
	 * Updates the dialog's message.
	 * 
	 * @param message
	 *            The new message.
	 */
	protected final void updateMessage(String message) {
		if (!indeterminate) {
			((ProgressPanel) panel).setMessage(message);
		}
	}

}
