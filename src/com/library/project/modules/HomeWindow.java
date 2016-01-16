package com.library.project.modules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.library.project.constants.LibraryButtons;
import com.library.project.constants.LibraryColumns;
import com.library.project.helper.DBHelper;

import java.awt.Font;

public class HomeWindow {

	private JFrame frmLibraryManagementSystem;
	private JTable table;
	private JTextField searchBookIdField;
	private JButton btnSearch;
	private JTextField searchAuthorField;
	private JTextField searchTitleField;

	/**
	 * Create the application.
	 */
	public HomeWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLibraryManagementSystem = new JFrame("Library Management System");
		frmLibraryManagementSystem
				.setTitle("Library Management System - Search");
		frmLibraryManagementSystem.getContentPane().setFont(
				new Font("Lucida Grande", Font.PLAIN, 13));
		frmLibraryManagementSystem.setBounds(100, 100, 885, 612);
		frmLibraryManagementSystem
				.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLibraryManagementSystem.setResizable(false);
		frmLibraryManagementSystem.getContentPane().setLayout(null);
		frmLibraryManagementSystem.setVisible(true);

		JPanel contentPanel = (JPanel) frmLibraryManagementSystem
				.getContentPane();

		btnSearch = new JButton(LibraryButtons.BTN_SEARCH);
		btnSearch.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnSearch.setBounds(669, 82, 128, 41);
		contentPanel.add(btnSearch);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HomeWindow.this.search();
			}
		});

		KeyListener keyListener = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				HomeWindow.this.search();

			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		};

		searchBookIdField = new JTextField();
		searchBookIdField.addKeyListener(keyListener);
		searchBookIdField.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		searchBookIdField.setBounds(211, 47, 412, 28);
		contentPanel.add(searchBookIdField);
		searchBookIdField.setColumns(10);

		final JButton btnCheckOut = new JButton("Check Out");
		btnCheckOut.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnCheckOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HomeWindow.this.checkOut();
			}
		});
		btnCheckOut.setBounds(27, 533, 192, 35);
		frmLibraryManagementSystem.getContentPane().add(btnCheckOut);
		btnCheckOut.setEnabled(false);

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
		table.setBounds(27, 100, 742, 345);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Add row selection listener
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (table.getSelectedRow() >= 0)
							btnCheckOut.setEnabled(true);
						table.getSelectionModel()
								.addListSelectionListener(null);
					}
				});
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(27, 183, 834, 331);
		scrollPane.setViewportView(table);
		contentPanel.add(scrollPane);

		JLabel lblSearchByBook = new JLabel("Search By Book ID");
		lblSearchByBook.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		lblSearchByBook.setBounds(27, 46, 185, 30);
		frmLibraryManagementSystem.getContentPane().add(lblSearchByBook);

		JLabel lblSearchByAuthor = new JLabel("Search By Author");
		lblSearchByAuthor.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		lblSearchByAuthor.setBounds(27, 88, 172, 27);
		frmLibraryManagementSystem.getContentPane().add(lblSearchByAuthor);

		searchAuthorField = new JTextField();
		searchAuthorField.addKeyListener(keyListener);
		searchAuthorField.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		searchAuthorField.setBounds(211, 87, 412, 28);
		frmLibraryManagementSystem.getContentPane().add(searchAuthorField);
		searchAuthorField.setColumns(10);

		JLabel lblSearchByTitle = new JLabel("Search By Title");
		lblSearchByTitle.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		lblSearchByTitle.setBounds(27, 127, 172, 30);
		frmLibraryManagementSystem.getContentPane().add(lblSearchByTitle);

		searchTitleField = new JTextField();
		searchTitleField.addKeyListener(keyListener);
		searchTitleField.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		searchTitleField.setBounds(211, 129, 412, 28);
		frmLibraryManagementSystem.getContentPane().add(searchTitleField);
		searchTitleField.setColumns(10);

		JButton btnCheckInA = new JButton("Check in a Book");
		btnCheckInA.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnCheckInA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HomeWindow.this.checkIn();
			}
		});
		btnCheckInA.setBounds(246, 533, 192, 35);
		frmLibraryManagementSystem.getContentPane().add(btnCheckInA);

		JButton btnAddABorrower = new JButton("Add a Borrower");
		btnAddABorrower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HomeWindow.this.showBorrowerManagement();
			}
		});
		btnAddABorrower.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnAddABorrower.setBounds(450, 533, 192, 35);
		frmLibraryManagementSystem.getContentPane().add(btnAddABorrower);

		JButton btnPayAFine = new JButton("Pay a Fine");
		btnPayAFine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FineModule fineModule = new FineModule(
						HomeWindow.this.frmLibraryManagementSystem, true);
				fineModule.setVisible(true);
			}
		});
		btnPayAFine.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnPayAFine.setBounds(669, 533, 192, 35);
		frmLibraryManagementSystem.getContentPane().add(btnPayAFine);

		this.getBookSearchData("", "", "", true);
		frmLibraryManagementSystem.repaint();
	}

	private void getBookSearchData(String bookId, String authorName,
			String bookTitle, boolean defaultSearch) {
		ResultSet rs = DBHelper.getInstance().getBookSearchData(bookId,
				authorName, bookTitle, defaultSearch);

		DefaultTableModel bookSearchModel = new DefaultTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {

				return false;
			}
		};
		bookSearchModel.addColumn(LibraryColumns.COL_BOOK_ID);
		bookSearchModel.addColumn(LibraryColumns.COL_TITLE);
		bookSearchModel.addColumn(LibraryColumns.COL_AUTHORS);
		bookSearchModel.addColumn(LibraryColumns.COL_BRANCH_ID);
		bookSearchModel.addColumn(LibraryColumns.COL_COPIES_TOTAL);
		bookSearchModel.addColumn(LibraryColumns.COL_COPIES_AVAILABLE);
		String book_id, title, author, branch_id, copies_total, copies_available;
		try {
			while (rs.next()) {
				book_id = rs.getString(1);
				title = rs.getString(2);
				author = rs.getString(3);
				branch_id = rs.getInt(4) + "";
				copies_total = rs.getInt(5) + "";
				copies_available = rs.getInt(6) + "";
				bookSearchModel.addRow(new Object[] { book_id, title, author,
						branch_id, copies_total, copies_available });
			}
			this.table.setModel(bookSearchModel);
			DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
			cellRenderer.setHorizontalAlignment(JLabel.CENTER);
			table.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
			table.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);
			table.getColumnModel().getColumn(5).setCellRenderer(cellRenderer);
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

	private void checkOut() {
		this.getCardNo();
	}

	private void getCardNo() {
		try {

			if (Integer.parseInt(table.getValueAt(
					table.getSelectedRow(),
					table.getColumnModel().getColumnIndex(
							LibraryColumns.COL_COPIES_AVAILABLE))
					+ "") > 0) {
				String cardNo = this.createDialog();
				if (cardNo == null)
					return;
				int cardNumber = Integer.parseInt(cardNo);
				if (!DBHelper.getInstance().checkExistingFines(cardNumber)) {
					System.out.println("True");
					JOptionPane
							.showMessageDialog(
									frmLibraryManagementSystem,
									"You already have existing fines. Please pay your fines by navigating to borrower management to check out more books");
				} else if (DBHelper.getInstance().getBorrowerCount(cardNumber) < 3) {

					int nextLoanId = DBHelper.getInstance().getNextLoanId();
					if (nextLoanId == -1) {
						JOptionPane.showMessageDialog(
								frmLibraryManagementSystem,
								"Some unknown error has occurred");
					} else {
						// book id
						String book_id = table.getValueAt(
								table.getSelectedRow(),
								table.getColumnModel().getColumnIndex(
										LibraryColumns.COL_BOOK_ID)).toString();
						// branch id
						int branch_id = Integer.parseInt(table.getValueAt(
								table.getSelectedRow(),
								table.getColumnModel().getColumnIndex(
										LibraryColumns.COL_BRANCH_ID))
								.toString());
						DBHelper.getInstance().createNewLoanEntry(nextLoanId,
								book_id, branch_id, cardNumber);
						JOptionPane.showMessageDialog(
								frmLibraryManagementSystem,
								"Successully checked out!");
						this.getBookSearchData("", "", "", true);
					}
				} else {
					JOptionPane.showMessageDialog(frmLibraryManagementSystem,
							"This borrower has already borrowed 3 books!");
				}
			} else {
				JOptionPane.showMessageDialog(frmLibraryManagementSystem,
						"Insufficient number of copies");

			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frmLibraryManagementSystem,
					"Invalid Input");
		}
	}

	private String createDialog() {
		return JOptionPane.showInputDialog(frmLibraryManagementSystem,
				"Enter Card No.", "");
	}

	protected void checkIn() {
		CheckInDialog ckd = new CheckInDialog(this.frmLibraryManagementSystem,
				true);
		ckd.setVisible(true);
	}

	private void showBorrowerManagement() {
		BorrowerManagement bDialog = new BorrowerManagement(
				this.frmLibraryManagementSystem, true);
		bDialog.setVisible(true);
	}

	private void search() {

		boolean searchCondition = (searchBookIdField.getText().trim()
				.equalsIgnoreCase("")
				&& searchAuthorField.getText().trim().equalsIgnoreCase("") && searchTitleField
				.getText().trim().equalsIgnoreCase(""));
		HomeWindow.this.getBookSearchData(searchBookIdField.getText(),
				searchAuthorField.getText(), searchTitleField.getText(),
				searchCondition);
	}
}