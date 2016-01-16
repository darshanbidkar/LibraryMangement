package com.library.project.modules;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.library.project.helper.DBHelper;

public class BorrowerManagement extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField fnameTextField;
	private JTextField lnameTextField;
	private JTextField addressTextField;
	private JTextField cityTextField;
	private JTextField stateTextField;
	private JTextField phoneTextField;

	/**
	 * Create the dialog.
	 */
	public BorrowerManagement() {
		setTitle("Add a New Borrower");
		this.initialize();

	}

	public BorrowerManagement(JFrame frame, boolean isModal) {
		super(frame, isModal);
		this.initialize();
	}

	private void initialize() {
		setBounds(100, 100, 702, 377);
		getContentPane().setLayout(null);

		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblFirstName.setBounds(61, 36, 107, 16);
		getContentPane().add(lblFirstName);

		fnameTextField = new JTextField();
		fnameTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		fnameTextField.setBounds(193, 31, 344, 28);
		getContentPane().add(fnameTextField);
		fnameTextField.setColumns(10);

		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblLastName.setBounds(61, 76, 107, 16);
		getContentPane().add(lblLastName);

		lnameTextField = new JTextField();
		lnameTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lnameTextField.setBounds(193, 71, 344, 28);
		getContentPane().add(lnameTextField);
		lnameTextField.setColumns(10);

		JLabel lblAddress = new JLabel("Address");
		lblAddress.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblAddress.setBounds(61, 116, 107, 16);
		getContentPane().add(lblAddress);

		addressTextField = new JTextField();
		addressTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		addressTextField.setBounds(193, 111, 344, 28);
		getContentPane().add(addressTextField);
		addressTextField.setColumns(10);

		JLabel lblCity = new JLabel("City");
		lblCity.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblCity.setBounds(61, 159, 107, 16);
		getContentPane().add(lblCity);

		cityTextField = new JTextField();
		cityTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		cityTextField.setBounds(193, 154, 344, 28);
		getContentPane().add(cityTextField);
		cityTextField.setColumns(10);

		JLabel lblState = new JLabel("State");
		lblState.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblState.setBounds(61, 203, 107, 16);
		getContentPane().add(lblState);

		stateTextField = new JTextField();
		stateTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		stateTextField.setBounds(193, 194, 344, 28);
		getContentPane().add(stateTextField);
		stateTextField.setColumns(10);

		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblPhone.setBounds(61, 246, 107, 16);
		getContentPane().add(lblPhone);

		phoneTextField = new JTextField();
		phoneTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		phoneTextField.setBounds(193, 240, 344, 28);
		getContentPane().add(phoneTextField);
		phoneTextField.setColumns(10);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!validateFields()) {
					JOptionPane.showMessageDialog(BorrowerManagement.this,
							"Enter all fields");
					return;
				}
				String fname, lname, phone, city, state, address;
				fname = fnameTextField.getText();
				lname = lnameTextField.getText();
				phone = phoneTextField.getText();
				city = cityTextField.getText();
				state = stateTextField.getText();
				address = addressTextField.getText();
				if (DBHelper.getInstance().isExisting(fname, lname, phone,
						city, state, address)) {
					JOptionPane.showMessageDialog(BorrowerManagement.this,
							"This is an existing user");
				} else {
					DBHelper.getInstance().createNewCardEntry(fname, lname,
							phone, city, state, address);
					JOptionPane.showMessageDialog(BorrowerManagement.this,
							"Borrower Added Successfully");
					fnameTextField.setText("");
					lnameTextField.setText("");
					addressTextField.setText("");
					cityTextField.setText("");
					stateTextField.setText("");
					phoneTextField.setText("");
				}
			}
		});
		btnAdd.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnAdd.setBounds(287, 300, 132, 37);
		getContentPane().add(btnAdd);
	}

	private boolean validateFields() {
		if (fnameTextField.getText().equalsIgnoreCase("")
				|| lnameTextField.getText().equalsIgnoreCase("")
				|| addressTextField.getText().equalsIgnoreCase("")
				|| cityTextField.getText().equalsIgnoreCase("")
				|| stateTextField.getText().equalsIgnoreCase("")
				|| phoneTextField.getText().equalsIgnoreCase(""))
			return false;
		return true;
	}
}
