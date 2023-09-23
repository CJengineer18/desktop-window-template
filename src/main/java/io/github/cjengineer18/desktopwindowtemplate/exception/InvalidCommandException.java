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
package io.github.cjengineer18.desktopwindowtemplate.exception;

import io.github.cjengineer18.desktopwindowtemplate.JGenericWindow;

/**
 * This exception is thrown when an invalid command parameter was passed.
 * 
 * @see JGenericWindow#singleSetter(int, Object)
 * @see JGenericWindow#singleSetterV(int, int, Object)
 * @see JGenericWindow#vectorSetter(int, Object[])
 * 
 * @author Cristian Jimenez Dzg
 *
 */
public class InvalidCommandException extends Exception {

	private static final long serialVersionUID = 0x1234L;

	public InvalidCommandException() {
		super("Invalid Command or Error in Command");
	}

	public InvalidCommandException(int invalidIndex) {
		super("Invalid Window Command" + invalidIndex);
	}

	public InvalidCommandException(int invalidIndex, Throwable consecuence) {
		super("Invalid Window Command" + invalidIndex, consecuence);
	}

	@Override
	public String getMessage() {
		return "Invalid Command or Error in Command!";
	}

}
