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
package io.github.cjengineer18.desktopwindowtemplate.component;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import io.github.cjengineer18.desktopwindowtemplate.util.constants.BundleConstants;

/**
 * Creates an accept/cancel footer for a dialog or message.
 * 
 * @author CJengineer18
 */
public class AcceptCancelDialogFooter extends JPanel {

	private static final long serialVersionUID = 0x13C1;

	// Flags

	/**
	 * Indicates that the buttons will be centered.
	 */
	public static final int ACDF_CENTER_BUTTONS = 0x10;

	/**
	 * Add an "Accept" button.
	 */
	public static final int ACDF_ACCEPT = 0x1;

	/**
	 * Add a "Cancel" button.
	 */
	public static final int ACDF_CANCEL = 0x2;

	// Fields

	private JDialog dialog;
	private JButton acceptButton;
	private JButton cancelButton;
	private boolean showAcceptButton;
	private boolean showCancelButton;
	private ActionListener onAcceptAction;
	private ActionListener onCancelAction;

	// Constructors

	/**
	 * Creates a new component. By default both buttons are visible.
	 * 
	 * @param dialog The dialog host.
	 */
	public AcceptCancelDialogFooter(JDialog dialog) {
		this(dialog, 19);
	}

	/**
	 * Creates a new component. You can choose which button show and the positions.
	 * 
	 * @param dialog  The dialog host.
	 * @param options The options. Any of them: {@link #ACDF_ACCEPT},
	 *                {@link #ACDF_CANCEL} and {@link #ACDF_CENTER_BUTTONS}
	 */
	public AcceptCancelDialogFooter(JDialog dialog, int options) {
		super(new FlowLayout((options & ACDF_CENTER_BUTTONS) != 0 ? FlowLayout.CENTER : FlowLayout.TRAILING));

		this.dialog = dialog;
		this.showAcceptButton = (options & ACDF_ACCEPT) != 0;
		this.showCancelButton = (options & ACDF_CANCEL) != 0;

		createNewInstance();
	}

	// Public methods

	/**
	 * Executes an {@link ActionListener} when the 'Accept' button is pressed.
	 * 
	 * @param listener The listener.
	 */
	public void onAccept(ActionListener listener) {
		if (showAcceptButton) {
			this.onAcceptAction = listener;
		}
	}

	/**
	 * Executes an {@link ActionListener} when the 'Cancel' button is pressed.
	 * 
	 * @param listener The listener.
	 */
	public void onCancel(ActionListener listener) {
		if (showCancelButton) {
			this.onCancelAction = listener;
		}
	}

	// Private methods

	// Creates the new class instance.
	private void createNewInstance() {
		ResourceBundle buttonBundle = ResourceBundle.getBundle(BundleConstants.BUTTONS_LOCALE);
		ActionListener disposer = e -> {
			dialog.dispose();
		};

		if (showAcceptButton) {
			acceptButton = new JButton(buttonBundle.getString("acceptButton"));

			if (onAcceptAction != null) {
				acceptButton.addActionListener(onAcceptAction);
			}

			acceptButton.addActionListener(disposer);

			add(acceptButton);
		}

		if (showCancelButton) {
			cancelButton = new JButton(buttonBundle.getString("cancelButton"));

			if (onCancelAction != null) {
				cancelButton.addActionListener(onCancelAction);
			}

			cancelButton.addActionListener(disposer);

			add(cancelButton);
		}
	}

}
