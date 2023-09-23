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
package io.github.cjengineer18.desktopwindowtemplate.util;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JFrame;

/**
 * Utility class.
 * 
 * @author CJengineer18
 */
public abstract class Utilities {

	/**
	 * Create the work area for the window according to the current OS.
	 * 
	 * @param originalSize
	 *            The original size of the window.
	 * 
	 * @return An {@code Dimension} object with the calculated size based on the
	 *         OS.
	 */
	public static Dimension createWorkArea(Dimension originalSize) {
		JFrame dummy = new JFrame();
		int[] insetValues = new int[2];
		String os = System.getProperty("os.name");

		int realWidth;
		int realHeight;
		Insets insets;

		dummy.setLayout(null);
		dummy.pack();

		insets = (Insets) dummy.getInsets().clone();

		// FIXME Insets Management
		/*
		 * Some OS restrict the actual workspace to assign them to the border
		 * and the title bar. Depending on the OS, we compensate for the missing
		 * part by adding the border size to the area.
		 */

		if (os.contains("Windows")) {
			// FIXME Windows insets
			/*
			 * Windows adds 2 extra pixels to the borders, causing some errors
			 * in custom graphics (like games). However, this can change between
			 * Windows versions.
			 */
			insetValues[0] = insets.top - 2;
			insetValues[1] = insets.left - 2;
		} else if (os.contains("Linux")) {
			// FIXME Untested on all distros / environments
			insetValues[0] = 0;
			insetValues[1] = 0;
		}

		// FIXME Untested on MacOS

		realWidth = ((int) originalSize.getWidth()) + insetValues[1];
		realHeight = ((int) originalSize.getHeight()) + insetValues[0];

		return new Dimension(realWidth, realHeight);
	}

}
