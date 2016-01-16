package com.library.project.modules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.library.project.helper.DBHelper;

public class LoginScreen {

	private JFrame frmLibraryManagementSystem;
	private JTextField userTextField;
	private JPasswordField passwordField;

	/**
	 * Create the application.
	 */
	public LoginScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLibraryManagementSystem = new JFrame();
		frmLibraryManagementSystem.setResizable(false);
		frmLibraryManagementSystem.setTitle("Library Management System - Login");
		frmLibraryManagementSystem.setBounds(100, 100, 500, 226);
		frmLibraryManagementSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLibraryManagementSystem.getContentPane().setLayout(null);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(115, 45, 85, 37);
		frmLibraryManagementSystem.getContentPane().add(lblUsername);

		userTextField = new JTextField();
		userTextField.setBounds(212, 49, 134, 28);
		frmLibraryManagementSystem.getContentPane().add(userTextField);
		userTextField.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(115, 94, 85, 28);
		frmLibraryManagementSystem.getContentPane().add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(212, 94, 134, 28);
		passwordField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					LoginScreen.this.login();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frmLibraryManagementSystem.getContentPane().add(passwordField);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginScreen.this.login();
			}
		});
		btnLogin.setBounds(212, 148, 117, 29);
		frmLibraryManagementSystem.getContentPane().add(btnLogin);
		frmLibraryManagementSystem.setVisible(true);
	}

	private void login() {
		String username = this.userTextField.getText();
		String password = String.copyValueOf(this.passwordField.getPassword());

		if (DBHelper.getInstance().login(username, password)) {
			new HomeWindow();
			frmLibraryManagementSystem.dispose();
		} else {
			JOptionPane.showMessageDialog(frmLibraryManagementSystem, "Invalid credentials");
		}
	}
}
