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

/**
 * 
 * @author CJengineer18
 *
 * @param <Input>
 * @param <Output>
 */
public abstract class CustomAsyncTask<Input, Output> extends AbstractAsyncTask<Input, Output> {

	// Methods
	// For overrider methods, see parent's documentation.

	@SafeVarargs
	@Override
	public final void execute(Input... inputs) {
		worker = new AsyncWorker<Input, Output>(this, inputs);

		beforeExecution();
		worker.execute();
	}

	@Override
	protected final void finish(Output result) {
		done(result);
	}

	// Abstract methods

	/**
	 * 
	 */
	protected abstract void beforeExecution();

}
