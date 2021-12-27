/* 
 * Copyright (c) 2021 Cristian José Jiménez Diazgranados
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
package com.github.cjengineer18.desktopwindowtemplate.exception;

import com.github.cjengineer18.desktopwindowtemplate.util.async.AsyncProcessLoading;

/**
 * This exception is thrown when an {@link Exception} was thrown during an async
 * process.
 * 
 * @see AsyncProcessLoading#loadAsyncProcess(java.awt.Window, Runnable)
 * @see AsyncProcessLoading#loadAsyncProcess(java.awt.Window, Runnable, String)
 * @see AsyncProcessLoading#loadAsyncProcess(java.awt.Window, Runnable, String,
 *      String)
 * 
 * @author Cristian Jimenez Dzg
 *
 */
public class AsyncProcessException extends Exception {

	private static final long serialVersionUID = 8402899429558462504L;

	public AsyncProcessException(Throwable throwable) {
		super(String.format("An error was thrown in an async process: %s", throwable.getMessage()), throwable);
	}

}
