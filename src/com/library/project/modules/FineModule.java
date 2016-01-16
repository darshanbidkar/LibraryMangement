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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.library.project.constants.LibraryColumns;
import com.library.project.helper.DBHelper;

public class FineModule extends JDialog {

	JFrame parent;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField cardNoTextField;
	private JTable fineTable;
	private JButton btnPay;
	private JButton btnNewButton_1;

	/**
	 * Create the dialog.
	 */
	public FineModule() {
		setResizable(false);
		setTitle("Pay Fine");
		this.initialize();
	}

	public FineModule(JFrame parent, boolean isModal) {
		super(parent, isModal);
		this.parent = parent;
		this.initialize();
	}

	private void initialize() {
		setBounds(100, 100, 450, 341);
		getContentPane().setLayout(null);

		JLabel lblCardNo = new JLabel("Card No.");
		lblCardNo.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblCardNo.setBounds(33, 37, 98, 27);
		getContentPane().add(lblCardNo);

		cardNoTextField = new JTextField();
		cardNoTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		cardNoTextField.setBounds(143, 37, 104, 28);
		getContentPane().add(cardNoTextField);
		cardNoTextField.setColumns(10);

		JButton btnNewButton = new JButton("Search");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int cardNo = Integer.parseInt(cardNoTextField.getText());
					FineModule.this.populateData(cardNo);
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(FineModule.this,
							"Invalid input");
				}
			}
		});
		btnNewButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnNewButton.setBounds(282, 34, 134, 35);
		getContentPane().add(btnNewButton);

		fineTable = new JTable() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		fineTable.setFont(new Font("Lucida Grande", Font.PLAIN, 14));

		JScrollPane pane = new JScrollPane();
		pane.setBounds(33, 81, 383, 155);
		pane.setViewportView(fineTable);
		getContentPane().add(pane);

		btnPay = new JButton("Pay");
		btnPay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FineModule.this.payFine();
			}
		});
		btnPay.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnPay.setBounds(68, 260, 117, 41);
		getContentPane().add(btnPay);

		btnNewButton_1 = new JButton("Borrower Stats");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BorrowerHistory h = new BorrowerHistory(parent, true);
				h.setVisible(true);
			}
		});
		btnNewButton_1.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnNewButton_1.setBounds(230, 260, 150, 41);
		getContentPane().add(btnNewButton_1);
		DBHelper.getInstance().refreshFinesTable();
		this.populateData(-1);
	}

	private void populateData(int cardNo) {
		ResultSet rs = DBHelper.getInstance().getFinesData(cardNo);
		if (rs != null) {
			DefaultTableModel finesModel = new DefaultTableModel() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {

					return false;
				}
			};
			finesModel.addColumn(LibraryColumns.COL_CARD_NO);
			finesModel.addColumn(LibraryColumns.COL_SUM_AMOUNT);
			String cardNumber, amount;
			try {
				while (rs.next()) {

					cardNumber = rs.getInt(1) + "";
					amount = rs.getDouble(2) + "";
					finesModel.addRow(new Object[] { cardNumber, amount });
				}
				this.fineTable.setModel(finesModel);
				DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
				cellRenderer.setHorizontalAlignment(JLabel.CENTER);
				fineTable.getColumnModel().getColumn(1)
						.setCellRenderer(cellRenderer);
				fineTable.getTableHeader().setDefaultRenderer(cellRenderer);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void payFine() {
		int cardNo = Integer.parseInt(fineTable.getValueAt(
				fineTable.getSelectedRow(),
				fineTable.getColumnModel().getColumnIndex(
						LibraryColumns.COL_CARD_NO)).toString());
		if (DBHelper.getInstance().payFine(cardNo)) {
			JOptionPane.showMessageDialog(this, "Fine successfully paid");
			this.populateData(-1);
		} else {
			JOptionPane.showMessageDialog(this, "Unknown error has occurred");
		}
	}
}
