package com.library.project.modules;

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
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.library.project.constants.LibraryColumns;
import com.library.project.helper.DBHelper;
import java.awt.Font;

public class CheckInDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField searchBookId;
	private JTextField searchCardNo;
	private JTextField searchBorrowerName;
	private JTable table;

	/**
	 * Create the dialog.
	 */
	public CheckInDialog() {
		this.initialize();
	}

	public CheckInDialog(JFrame frame, boolean modal) {
		super(frame, modal);
		this.initialize();
	}

	private void initialize() {
		setTitle("Check In a Book");
		setModal(true);
		setBounds(100, 100, 657, 403);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		getContentPane().setLayout(null);

		JLabel lblSearchByBook = new JLabel("Search by Book ID");
		lblSearchByBook.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblSearchByBook.setBounds(25, 27, 165, 16);
		getContentPane().add(lblSearchByBook);

		searchBookId = new JTextField();
		searchBookId.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		searchBookId.setBounds(238, 21, 258, 28);
		getContentPane().add(searchBookId);
		searchBookId.setColumns(10);

		JLabel lblSearchBy = new JLabel("Search by Card No.");
		lblSearchBy.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblSearchBy.setBounds(25, 68, 165, 16);
		getContentPane().add(lblSearchBy);

		searchCardNo = new JTextField();
		searchCardNo.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		searchCardNo.setBounds(238, 61, 258, 28);
		getContentPane().add(searchCardNo);
		searchCardNo.setColumns(10);

		JLabel lblSearchByBorrower = new JLabel("Search by Borrower Name");
		lblSearchByBorrower.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblSearchByBorrower.setBounds(25, 107, 213, 16);
		getContentPane().add(lblSearchByBorrower);

		searchBorrowerName = new JTextField();
		searchBorrowerName.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		searchBorrowerName.setBounds(238, 102, 258, 28);
		getContentPane().add(searchBorrowerName);
		searchBorrowerName.setColumns(10);

		JButton btnSearch = new JButton("Search");
		btnSearch.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				CheckInDialog.this.populateData(searchBookId.getText(),
						searchCardNo.getText(), searchBorrowerName.getText(),
						false);

			}
		});
		btnSearch.setBounds(511, 56, 117, 42);
		getContentPane().add(btnSearch);

		final JButton btnCheckIn = new JButton("Check In");
		btnCheckIn.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnCheckIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int branch_id = Integer.parseInt(table.getValueAt(
						table.getSelectedRow(),
						table.getColumnModel().getColumnIndex(
								LibraryColumns.COL_BRANCH_ID)).toString());
				String book_id = table.getValueAt(
						table.getSelectedRow(),
						table.getColumnModel().getColumnIndex(
								LibraryColumns.COL_BOOK_ID)).toString();
				int cardNo = Integer.parseInt(table.getValueAt(
						table.getSelectedRow(),
						table.getColumnModel().getColumnIndex(
								LibraryColumns.COL_CARD_NO)).toString());
				if (!DBHelper.getInstance().checkIn(branch_id, book_id, cardNo))
					JOptionPane.showMessageDialog(CheckInDialog.this,
							"Operation unsuccessful");
				else {
					JOptionPane.showMessageDialog(CheckInDialog.this,
							"Book checked in");
					if (CheckInDialog.this.checkForFine(branch_id + "",
							book_id, cardNo)) {
						JOptionPane
								.showMessageDialog(
										CheckInDialog.this,
										"This book has been charged for late check in.\nPlease pay the fine by navigating to Borrower Management");
					}
					CheckInDialog.this.populateData("", "", "", true);
				}

			}

		});
		btnCheckIn.setEnabled(false);
		btnCheckIn.setBounds(270, 325, 136, 50);
		getContentPane().add(btnCheckIn);

		table = new JTable() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		table.setBounds(24, 161, 749, 152);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Add row selection listener
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (table.getSelectedRow() >= 0)
							btnCheckIn.setEnabled(true);
						table.getSelectionModel()
								.addListSelectionListener(null);
					}
				});
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 161, 603, 152);
		scrollPane.setViewportView(table);
		getContentPane().add(scrollPane);
		this.populateData("", "", "", true);
		super.repaint();

	}

	private void populateData(String book_id, String cardNo,
			String borrowerName, boolean defaultSearch) {
		ResultSet rs = DBHelper.getInstance().getBookCheckInData(book_id,
				cardNo, borrowerName, defaultSearch);
		DefaultTableModel checkInSearchModel = new DefaultTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {

				return false;
			}
		};
		checkInSearchModel.addColumn(LibraryColumns.COL_BOOK_ID);
		checkInSearchModel.addColumn(LibraryColumns.COL_CARD_NO);
		checkInSearchModel.addColumn(LibraryColumns.COL_BRANCH_ID);
		checkInSearchModel.addColumn(LibraryColumns.COL_FIRST_NAME);
		checkInSearchModel.addColumn(LibraryColumns.COL_LAST_NAME);

		try {
			while (rs.next()) {
				book_id = rs.getString(1);
				String cardNumber = rs.getInt(2) + "";
				String branch_id = rs.getInt(3) + "";
				String fname = rs.getString(4);
				String lname = rs.getString(5);
				checkInSearchModel.addRow(new Object[] { book_id, cardNumber,
						branch_id, fname, lname });
			}
			this.table.setModel(checkInSearchModel);
			DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
			cellRenderer.setHorizontalAlignment(JLabel.CENTER);
			table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
			table.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
			table.getTableHeader().setDefaultRenderer(cellRenderer);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean checkForFine(String branch_id, String book_id, int cardNo) {
		return DBHelper.getInstance().isLateReturn(branch_id, book_id, cardNo);
	}
}
