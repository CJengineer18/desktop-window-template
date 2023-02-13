package com.github.cjengineer18.desktopwindowtemplate.component;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.github.cjengineer18.desktopwindowtemplate.util.constants.BundleConstants;

/**
 * Creates an accept/cancel footer for a dialog or message.
 * 
 * @author CJengineer18
 *
 */
public class AcceptCancelDialogFooter extends JPanel {

	private static final long serialVersionUID = 205L;

	private JDialog dialog;
	private JButton acceptButton;
	private JButton cancelButton;

	public AcceptCancelDialogFooter(JDialog dialog) {
		super(new FlowLayout(FlowLayout.TRAILING));

		this.dialog = dialog;

		createNewInstance();
	}

	public void onAccept(ActionListener listener) {
		acceptButton.addActionListener(listener);
	}

	private void createNewInstance() {
		acceptButton = new JButton("Aceptar");
		cancelButton = new JButton(ResourceBundle.getBundle(BundleConstants.BUTTONS_LOCALE).getString("cancelButton"));

		cancelButton.addActionListener(e -> {
			dialog.dispose();
		});

		add(acceptButton);
		add(cancelButton);
	}

}
