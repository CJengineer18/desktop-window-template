/* 
 * Copyright (c) 2019-2020 Cristian José Jiménez Diazgranados
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import com.github.cjengineer18.desktopwindowtemplate.component.staticpanel.ProgressPanel;
import com.github.cjengineer18.desktopwindowtemplate.component.staticpanel.WaitingPanel;
import com.github.cjengineer18.desktopwindowtemplate.resources.constants.BundleConstants;
import com.github.cjengineer18.desktopwindowtemplate.util.factory.DialogMaker;

/**
 * An utility method for async tasks. This class support arguments and can
 * return a result. A dialog appears to show the progress. This class uses a
 * {@code SwingWorker} for execution.
 * 
 * @author Cristian Jimenez
 *
 * @param <Input>
 *            The argument's class.
 * @param <Output>
 *            The result's class.
 * 
 * @see SwingWorker
 */
public abstract class AsyncTask<Input, Output> {

	private Window parent;
	private JDialog dialog;
	private AsyncWorker worker;
	private JPanel panel;
	private Output result;
	private String title;
	private boolean enableCancel;
	private boolean indeterminate;
	private int step;

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

	/**
	 * Executes the process, passing the arguments if required. Unlike
	 * {@code SwingWorker.execute()}, this method can be used many times.
	 * 
	 * @param inputs
	 *            The arguments
	 * 
	 * @see SwingWorker#execute()
	 */
	@SafeVarargs
	public final void execute(Input... inputs) {
		ResourceBundle buttons = ResourceBundle.getBundle(BundleConstants.BUTTONS_LOCALE);
		ResourceBundle panels = ResourceBundle.getBundle(BundleConstants.PANELS_LOCALE);
		JButton cancelButton = new JButton(buttons.getString("cancelButton"));
		worker = new AsyncWorker(this, inputs);
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

	/**
	 * Calls the {@code SwingWorker.get()} to get the result of the process, or
	 * return it immediately if this task has been executed successfully. Return
	 * {@code null} if the task has cancelled. You must call {@code execute()}
	 * before call this.
	 * 
	 * @return The task's result.
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * 
	 * @see SwingWorker#get()
	 * @see #execute(Object...)
	 */
	public final Output get() throws InterruptedException, ExecutionException {
		if (!worker.isDone()) {
			return worker.get();
		} else {
			return worker.isCancelled() ? null : result;
		}
	}

	/**
	 * Calls the {@code SwingWorker.get(long, TimeUnit)} to get the result of
	 * the process, or return it immediately if this task has been executed
	 * successfully. Return {@code null} if the task has cancelled. You must
	 * call {@code execute()} before call this.
	 * 
	 * @param timeout
	 *            The time to wait.
	 * @param unit
	 *            The time unit.
	 * 
	 * @return The task's result.
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 * 
	 * @see SwingWorker#get(long, TimeUnit)
	 * @see #execute(Object...)
	 */
	public final Output get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		if (!worker.isDone()) {
			return worker.get(timeout, unit);
		} else {
			return worker.isCancelled() ? null : result;
		}
	}

	/**
	 * Checks if the task is done.
	 * 
	 * @return {@code true} if the task is done.
	 */
	public final boolean isDone() {
		return (worker == null) ? false : worker.isDone();
	}

	/**
	 * Checks if the task is cancelled.
	 * 
	 * @return {@code true} if the task is cancelled.
	 */
	public final boolean isCancelled() {
		return (worker == null) ? false : worker.isCancelled();
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

	/**
	 * This method is called when {@code doInBackground(Input[])} finish.
	 * Process the output result in the main thread.
	 * 
	 * @param output
	 *            The task's result.
	 * 
	 * @see #doInBackground(Object[])
	 */
	protected void done(Output output) {};

	/**
	 * The task to execute in an async thread. Returns a result or throws an
	 * exception. Unlike {@code SwingWorker.doInBackground()}, multiple calls of
	 * {@code execute()} invokes {@code doInBackground(Input[])} multiple times.
	 * 
	 * @param inputs
	 *            The parameters from {@code execute(Input...)}.
	 * 
	 * @return A result.
	 * 
	 * @throws Exception
	 *             If any error.
	 * 
	 * @see SwingWorker#doInBackground()
	 * @see #execute(Object...)
	 * @see #done(Object)
	 */
	protected abstract Output doInBackground(Input[] inputs) throws Exception;

	/*
	 * Closes the dialog and save the result before invoke done(Output).
	 * 
	 * @param result The result.
	 */
	private void finish(Output result) {
		dialog.dispose();
		this.result = result;
		done(result);
	}

	/*
	 * Private worker used to execute the assigned task.
	 */
	private class AsyncWorker extends SwingWorker<Output, Void> {

		private AsyncTask<Input, Output> task;
		private Input[] inputs;
		private Output result;

		private AsyncWorker(AsyncTask<Input, Output> task, Input[] inputs) {
			this.task = task;
			this.inputs = inputs;
		}

		@Override
		protected Output doInBackground() throws Exception {
			result = task.doInBackground(inputs);
			return result;
		}

		@Override
		protected void done() {
			task.finish(result);
		}

	}

}
