package com.library.project.modules;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.library.project.helper.DBHelper;
import javax.swing.SwingConstants;

public class BorrowerHistory extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField cardNoTextField;
	private JLabel unPaidAmountLabel, lblPaidAmount, lblCheckedOutBooks,
			lblHistoryBookCount;

	/**
	 * Create the dialog.
	 */
	public BorrowerHistory() {
		setResizable(false);
		setTitle("Borrower Statistics");
		getContentPane().setFont(new Font("Lucida Grande", Font.PLAIN, 16));

		this.initialize();
	}

	public BorrowerHistory(JFrame parent, boolean isModal) {
		super(parent, isModal);
		this.initialize();
	}

	private void initialize() {
		setBounds(100, 100, 450, 351);
		getContentPane().setLayout(null);

		JLabel lblCardNo = new JLabel("Card No.");
		lblCardNo.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblCardNo.setBounds(56, 17, 92, 28);
		getContentPane().add(lblCardNo);

		cardNoTextField = new JTextField();
		cardNoTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		cardNoTextField.setBounds(253, 17, 134, 28);
		getContentPane().add(cardNoTextField);
		cardNoTextField.setColumns(10);

		JButton btnCheck = new JButton("Check");
		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BorrowerHistory.this.checkStats();
			}
		});
		btnCheck.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnCheck.setBounds(159, 271, 117, 37);
		getContentPane().add(btnCheck);

		JLabel lblUnpaidAmount = new JLabel("Unpaid Amount");
		lblUnpaidAmount.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblUnpaidAmount.setBounds(56, 80, 134, 28);
		getContentPane().add(lblUnpaidAmount);

		JLabel lblPreviousFines = new JLabel("Previous Fines");
		lblPreviousFines.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblPreviousFines.setBounds(56, 120, 134, 32);
		getContentPane().add(lblPreviousFines);

		unPaidAmountLabel = new JLabel("");
		unPaidAmountLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		unPaidAmountLabel.setBounds(263, 80, 124, 28);
		getContentPane().add(unPaidAmountLabel);

		lblPaidAmount = new JLabel("");
		lblPaidAmount.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblPaidAmount.setBounds(263, 120, 124, 32);
		getContentPane().add(lblPaidAmount);

		JLabel lblBooksCheckedOut = new JLabel("Books Checked Out");
		lblBooksCheckedOut.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblBooksCheckedOut.setBounds(56, 164, 176, 35);
		getContentPane().add(lblBooksCheckedOut);

		lblCheckedOutBooks = new JLabel("");
		lblCheckedOutBooks.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblCheckedOutBooks.setBounds(263, 164, 124, 35);
		getContentPane().add(lblCheckedOutBooks);

		JLabel lblNewLabel = new JLabel("Checked In Book Count");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblNewLabel.setBounds(56, 211, 203, 26);
		getContentPane().add(lblNewLabel);

		lblHistoryBookCount = new JLabel("");
		lblHistoryBookCount.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblHistoryBookCount.setBounds(263, 211, 124, 26);
		getContentPane().add(lblHistoryBookCount);
	}

	private void checkStats() {
		int cardNo = 0;
		try {
			String cardNumber = cardNoTextField.getText();
			if (cardNumber.equalsIgnoreCase(""))
				return;
			cardNo = Integer.parseInt(cardNumber);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(BorrowerHistory.this, "Invalid input");
			return;
		}
		ResultSet rs = DBHelper.getInstance().getUnPaidStats(cardNo);
		try {
			if (rs.next()) {
				this.unPaidAmountLabel.setText("$" + rs.getDouble(1));
			} else {
				this.unPaidAmountLabel.setText(0 + "");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs = DBHelper.getInstance().getPaidStats(cardNo);
		try {
			if (rs.next()) {
				this.lblPaidAmount.setText("$" + rs.getDouble(1));
			} else {
				this.lblPaidAmount.setText(0 + "");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs = DBHelper.getInstance().getNumberOfCheckedOutBooks(cardNo);
		try {
			if (rs.next()) {
				this.lblCheckedOutBooks.setText("" + rs.getInt(1));
			} else {
				this.lblCheckedOutBooks.setText(0 + "");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		rs = DBHelper.getInstance().getHistoryBookCount(cardNo);
		try {
			if (rs.next()) {
				this.lblHistoryBookCount.setText(rs.getInt(1) + "");
			} else {
				this.lblHistoryBookCount.setText("0");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
