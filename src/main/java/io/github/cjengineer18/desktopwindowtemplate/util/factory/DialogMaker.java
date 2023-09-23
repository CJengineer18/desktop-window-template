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
package io.github.cjengineer18.desktopwindowtemplate.util.factory;

import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * An utility for create dialogs.
 * 
 * @author Cristian Jimenez
 * 
 * @deprecated Dialogs can be build with new component {@code JModalDialog}.
 */
@Deprecated
public abstract class DialogMaker {

	/**
	 * Creates a dialog with the specified content.
	 * 
	 * @param owner
	 *            The dialog owner. If {@code null}, a default frame is used.
	 * @param title
	 *            The dialog's title.
	 * @param body
	 *            The dialog's body.
	 * @param buttons
	 *            An array of buttons. Each button must have their custom
	 *            listener implemented. If {@code null}, no buttons added.
	 * 
	 * @return A dialog.
	 */
	public static JDialog makeDialog(Window owner, String title, Object body, JButton[] buttons) {
		JOptionPane pane = new JOptionPane(body);
		if (buttons != null)
			pane.setOptions(buttons);
		else
			pane.setOptions(new Object[0]);
		return pane.createDialog(owner, title);
	}

}
