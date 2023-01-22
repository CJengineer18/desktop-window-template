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

import javax.swing.SwingWorker;

/**
 * Package-scoped worker used to execute the assigned task.
 * 
 * @author CJengineer18
 *
 * @param <Input>
 *            The argument's class.
 * @param <Output>
 *            The result's class.
 * 
 * @see AbstractAsyncTask
 */
class AsyncWorker<Input, Output> extends SwingWorker<Output, Void> {

	private AbstractAsyncTask<Input, Output> task;
	private Input[] inputs;
	private Output result;

	AsyncWorker(AbstractAsyncTask<Input, Output> task, Input[] inputs) {
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
