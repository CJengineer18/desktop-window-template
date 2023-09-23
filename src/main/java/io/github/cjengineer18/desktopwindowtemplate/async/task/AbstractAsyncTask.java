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
package io.github.cjengineer18.desktopwindowtemplate.async.task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * An abstract async class for use in implementation on specialized async task
 * classes.
 * 
 * @author CJengineer18
 *
 * @param <Input>
 *            The argument's class.
 * @param <Output>
 *            The result's class.
 * 
 * @see AsyncTask
 * @see CustomAsyncTask
 */
abstract class AbstractAsyncTask<Input, Output> {

	// Fields

	// The worker
	protected AsyncWorker<Input, Output> worker;

	// The result
	protected Output result;

	// Methods

	// Public methods

	/**
	 * Calls the worker's {@code get()} to get the result of the process, or
	 * return it immediately if this task has been executed successfully. Return
	 * {@code null} if the task has cancelled. You must call {@code execute()}
	 * before call this.
	 * 
	 * @return The task's result.
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * 
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
	 * Calls the worker's {@code get(long, TimeUnit)} to get the result of the
	 * process, or return it immediately if this task has been executed
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

	// Protected methods

	/**
	 * This method is called when {@code doInBackground(Input[])} finish.
	 * Process the output result in the main thread.
	 * 
	 * @param output
	 *            The task's result.
	 * 
	 * @see #doInBackground(Object[])
	 */
	protected void done(Output output) {
		// empty
	};

	// Abstract methods

	/**
	 * Executes the process, passing the arguments if required.
	 * 
	 * @param inputs
	 *            The arguments
	 * 
	 */
	@SuppressWarnings("unchecked")
	public abstract void execute(Input... inputs);

	/**
	 * The task to execute in an async thread. Returns a result or throws an
	 * exception.
	 * 
	 * @param inputs
	 *            The parameters from {@code execute(Input...)}.
	 * 
	 * @return A result.
	 * 
	 * @throws Exception
	 *             If any error.
	 * 
	 * @see #execute(Object...)
	 * @see #done(Object)
	 */
	protected abstract Output doInBackground(Input[] inputs) throws Exception;

	/**
	 * Make the post-process to the result before call {@code done(Output)}.
	 * 
	 * @param result
	 *            The result.
	 */
	protected abstract void finish(Output result);
}
