/**
 * 
 */
package com.library.project.helper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import com.library.project.constants.LibraryConstants;

/**
 * @author darshanbidkar
 * 
 */
public class DBHelper {

	private Connection mConnection;
	private static DBHelper mDBHelper;

	/**
	 * 
	 */
	private DBHelper() {

		try {
			Class.forName(LibraryConstants.DATABSE_DRIVER);
			this.mConnection = DriverManager
					.getConnection(LibraryConstants.DATABASE_PATH);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (this.mConnection == null) {
			System.out.println("Connection = null");
		}

	}

	public synchronized static DBHelper getInstance() {

		if (DBHelper.mDBHelper == null) {
			DBHelper.mDBHelper = new DBHelper();

		}
		return DBHelper.mDBHelper;
	}

	private void closeDBConnection() {
		if (DBHelper.mDBHelper.mConnection != null)
			try {
				DBHelper.mDBHelper.mConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public ResultSet getBookSearchData(String bookId, String author,
			String title, boolean defaultSearch) {
		// TODO try removing if condition
		try {
			PreparedStatement ps;
			if (defaultSearch) {
				String sqlQuery = "Select bc.book_id, " + "b.title, "
						+ "GROUP_CONCAT(distinct ba.author_name) authors, "
						+ "bc.branch_id, " + "bc.no_of_copies, "
						+ "bc.no_of_copies - " + "("
						+ "select count(*) from book_loans bl "
						+ "where b.book_id=bl.book_id "
						+ "AND bl.branch_id=bc.branch_id "
						+ "AND isnull(bl.date_in)) as available "
						+ "from book b NATURAL JOIN book_copies bc "
						+ "NATURAL JOIN book_authors ba "
						+ "group by b.book_id, bc.branch_id;";
				ps = this.mConnection.prepareStatement(sqlQuery);
			} else {
				ps = this.mConnection.prepareStatement("Select bc.book_id, "
						+ "b.title, "
						+ "GROUP_CONCAT(distinct ba.author_name) authors, "
						+ "bc.branch_id, " + "bc.no_of_copies, "
						+ "bc.no_of_copies - " + "("
						+ "select count(*) from book_loans bl "
						+ "where b.book_id=bl.book_id "
						+ "AND bl.branch_id=bc.branch_id "
						+ "AND isnull(bl.date_in)) as available "
						+ "from book b NATURAL JOIN book_copies bc "
						+ "NATURAL JOIN book_authors ba "
						+ "where b.book_id LIKE ? " + "AND b.title LIKE ? "
						+ "AND ba.author_name LIKE ? "
						+ "group by b.book_id, bc.branch_id;");
				ps.setString(1, "%" + bookId + "%");
				ps.setString(3, "%" + author + "%");
				ps.setString(2, "%" + title + "%");
			}
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Thread sShutDownThread = new Thread() {
		@Override
		public void run() {
			DBHelper.getInstance().closeDBConnection();
		}
	};

	public int getBorrowerCount(int cardNumber) {
		try {
			PreparedStatement ps = this.mConnection
					.prepareStatement("select count(*) from book_loans where card_no = ? and date_in is NULL");
			ps.setInt(1, cardNumber);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int getNextLoanId() {
		int retVal = -1;
		try {
			PreparedStatement ps = this.mConnection
					.prepareStatement("update nextvalue set nextval= nextval+1 where name='loan_id';");
			ps.executeUpdate();
			ps = this.mConnection
					.prepareStatement("select nextval from nextvalue where name='loan_id';");
			ResultSet rs = ps.executeQuery();
			rs.next();
			retVal = rs.getInt(1);
			rs.close();
			return retVal;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retVal;
	}

	public void createNewLoanEntry(int nextLoanId, String bookId,
			int branch_id, int cardNo) {
		try {
			PreparedStatement ps = this.mConnection
					.prepareStatement("insert into book_loans values(?,?,?,?,?,?,?);");
			ps.setInt(1, nextLoanId);
			ps.setString(2, bookId);
			ps.setInt(3, branch_id);
			ps.setInt(4, cardNo);
			Calendar c = Calendar.getInstance();
			ps.setDate(5, new java.sql.Date(c.getTimeInMillis()));
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 14);
			ps.setDate(6, new java.sql.Date(c.getTimeInMillis()));
			ps.setDate(7, null);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public ResultSet getBookCheckInData(String book_id, String cardNo,
			String borrowerName, boolean defaultSearch) {
		try {
			PreparedStatement ps;
			if (defaultSearch)
				ps = this.mConnection
						.prepareStatement("select BL.book_id , BL.card_no, BL.branch_id, BO.fname, BO.lname"
								+ " from BOOK_LOANS BL NATURAL JOIN borrower BO where BL.date_in is NULL;");
			else {
				ps = this.mConnection
						.prepareStatement("select BL.book_id , BL.card_no, BL.branch_id, BO.fname, BO.lname"
								+ " from BOOK_LOANS BL NATURAL JOIN borrower BO"
								+ " where BL.date_in is NULL AND BL.book_id LIKE ? AND"
								+ " BL.card_no LIKE ? AND "
								+ "(BO.fname LIKE ? OR " + "BO.lname LIKE ?);");
				ps.setString(1, "%" + book_id + "%");
				ps.setString(2, "%" + cardNo + "%");
				ps.setString(3, "%" + borrowerName + "%");
				ps.setString(4, "%" + borrowerName + "%");
			}
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean checkIn(int branch_id, String book_id, int card_no) {
		try {
			PreparedStatement ps = this.mConnection
					.prepareStatement("update book_loans set date_in = ? where "
							+ "book_id = ? and branch_id = ? and card_no = ?;");
			ps.setDate(1, new Date(Calendar.getInstance().getTimeInMillis()));
			ps.setString(2, book_id);
			ps.setInt(3, branch_id);
			ps.setInt(4, card_no);
			int q = ps.executeUpdate();
			this.refreshFinesTable();
			if (q > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean checkExistingFines(int cardNo) {
		try {
			this.refreshFinesTable();
			PreparedStatement ps = this.mConnection
					.prepareStatement("select * from fineview where card_no = ? and paid = false;");
			ps.setInt(1, cardNo);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) // No fine
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void refreshFinesTable() {
		try {
			PreparedStatement ps = this.mConnection
					.prepareStatement("select loan_id, fineamount from fineview where (paid is NULL or paid = false);");

			ResultSet rs = ps.executeQuery();
			PreparedStatement finesPS = this.mConnection
					.prepareStatement("replace into fines values(?,?,?);");
			while (rs.next()) {
				int loan_id = rs.getInt(1);
				double fineAmt = rs.getDouble(2);
				finesPS.setInt(1, loan_id);
				finesPS.setDouble(2, fineAmt);
				finesPS.setBoolean(3, false);
				finesPS.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isExisting(String fname, String lname, String phone,
			String city, String state, String address) {
		ResultSet rs = null;
		try {
			PreparedStatement ps = this.mConnection
					.prepareStatement("select * from borrower where "
							+ "fname like ? and " + "lname like ? and "
							+ "phone like ? and " + "state like ? and "
							+ "city like ? and " + "address like ?;");
			ps.setString(1, fname);
			ps.setString(2, lname);
			ps.setString(3, phone);
			ps.setString(4, state);
			ps.setString(5, city);
			ps.setString(6, address);
			rs = ps.executeQuery();
			if (rs.next())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (NullPointerException e2) {
				e2.printStackTrace();
			}
		}
		return false;
	}

	public void createNewCardEntry(String fname, String lname, String phone,
			String city, String state, String address) {
		try {
			int cardNo = this.getNextCardNo();
			PreparedStatement ps = this.mConnection
					.prepareStatement("insert into borrower values("
							+ "?, ?, ?, ?, ?, ?, ?);");
			ps.setInt(1, cardNo);
			ps.setString(2, fname);
			ps.setString(3, lname);
			ps.setString(4, address);
			ps.setString(5, city);
			ps.setString(6, state);
			ps.setString(7, phone);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private int getNextCardNo() {
		int i = -1;
		try {
			PreparedStatement ps = this.mConnection
					.prepareStatement("update nextvalue set nextval= nextval+1 where name='card_no';");
			ps.executeUpdate();
			ps = this.mConnection
					.prepareStatement("select nextval from nextvalue where name='card_no';");
			ResultSet rs = ps.executeQuery();
			rs.next();
			i = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	public boolean isLateReturn(String branch_id, String book_id, int cardNo) {
		try {
			PreparedStatement ps = this.mConnection
					.prepareStatement("Select * from fineview where loan_id = (select loan_id from book_loans where card_no = ? and book_id = ? and branch_id = ? and date_in is null);");
			ps.setInt(1, cardNo);
			ps.setString(2, book_id);
			ps.setString(3, branch_id);
			return ps.executeQuery().next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ResultSet getFinesData(int cardNo) {
		try {
			PreparedStatement ps;
			if (cardNo == -1) {
				ps = this.mConnection
						.prepareStatement("select card_no, sum(fineamount) from fineview where date_in is not null and paid = false group by card_no;");
			} else {
				ps = this.mConnection
						.prepareStatement("select card_no, sum(fineamount) from fineview where date_in is not null and card_no like ? and paid = false group by card_no");
				ps.setString(1, "%" + cardNo + "%");
			}
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean payFine(int cardNo) {
		try {
			PreparedStatement ps = this.mConnection
					.prepareStatement("update fines set paid = true where loan_id in (select loan_id from fineview where card_no = ? and date_in is not null and paid = false);");
			ps.setInt(1, cardNo);
			int q = ps.executeUpdate();
			if (q > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean login(String usernmae, String password) {
		try {
			PreparedStatement ps = this.mConnection
					.prepareStatement("Select * from userinfo where userid = ? and password = ?");
			ps.setString(1, usernmae);
			ps.setString(2, password);
			return ps.executeQuery().next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ResultSet getUnPaidStats(int cardNo) {
		try {
			PreparedStatement ps = this.mConnection
					.prepareStatement("select sum(fineamount) from fineview where paid = false and card_no = ?");
			ps.setInt(1, cardNo);
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet getPaidStats(int cardNo) {
		try {
			PreparedStatement ps = this.mConnection
					.prepareStatement("select sum(fineamount) from fineview where paid = true and card_no = ?");
			ps.setInt(1, cardNo);
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public ResultSet getNumberOfCheckedOutBooks(int cardNo) {

		try {
			PreparedStatement ps = this.mConnection
					.prepareStatement("select count(*) from fineview where date_in is null and card_no = ?");
			ps.setInt(1, cardNo);
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public ResultSet getHistoryBookCount(int cardNo) {
		try {
			PreparedStatement ps = this.mConnection
					.prepareStatement("select count(*) from fineview where date_in is not null and card_no = ?");
			ps.setInt(1, cardNo);
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
