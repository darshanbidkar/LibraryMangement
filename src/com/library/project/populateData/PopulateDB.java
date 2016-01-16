/**
 * 
 */
package com.library.project.populateData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author darshanbidkar
 * 
 */
public class PopulateDB {

	private Connection mConnection;

	public PopulateDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.mConnection = DriverManager
					.getConnection("jdbc:mysql://localhost/library?user=root&password=");
			if (this.mConnection == null) {
				System.out.println("mConnection = null");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	private void insertBooks() {
		File booksFile = new File("DBData/books_authors.csv");
		if (booksFile.exists()) {
			try {
				FileReader fReader = new FileReader(booksFile);
				BufferedReader br = new BufferedReader(fReader);
				String currentRecord = br.readLine();
				System.out.println(currentRecord);
				PreparedStatement ps = this.mConnection
						.prepareStatement("insert into book values(?,?);");
				while ((currentRecord = br.readLine()) != null) {
					System.out.println(currentRecord);
					String[] splitOutput = currentRecord.split("\t");
					System.out.println("Split3: " + splitOutput[2]);
					String book_id = splitOutput[0].trim(); // Book_id
					String title = splitOutput[2].trim(); // Title
					ps.setString(1, book_id);
					ps.setString(2, title);
					ps.executeUpdate();
					System.out.println("Inserted: " + book_id + ": " + title);
				}
				br.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("booksFile doesn't exist!");
		}
	}

	private void insertBook_Authors() {

		File booksFile = new File("DBData/books_authors.csv");
		BufferedReader keyIn = new BufferedReader(new InputStreamReader(
				System.in));
		if (booksFile.exists()) {
			try {
				FileReader fReader = new FileReader(booksFile);
				BufferedReader br = new BufferedReader(fReader);
				br.readLine();
				try {
					String currentRecord;
					PreparedStatement ps = this.mConnection.prepareStatement(
							"insert into book_authors values(?,?,?);",
							PreparedStatement.RETURN_GENERATED_KEYS);

					while ((currentRecord = br.readLine()) != null) {

						String[] splitOutput = currentRecord.split("\t");
						String book_id = splitOutput[0].trim(); // Book_id
						String authorName = splitOutput[1].trim(); // Author

						ps.setString(1, book_id);
						String separateEntries[] = authorName.split(",");
						if (separateEntries.length > 1) {
							for (int i = 0; i < separateEntries.length; i++) {
								ps.setString(2, separateEntries[i].trim());
								ps.setInt(3, 1);
								// ps.addBatch();
								ps.executeUpdate();
							}
						} else {
							ps.setString(2, authorName.trim());
							if (authorName.equalsIgnoreCase("Various")
									|| authorName
											.equalsIgnoreCase("The Beatles")
									|| authorName
											.contains("Los Angeles"))
								ps.setInt(3, 2);
							else
								ps.setInt(3, 1);
							// ps.addBatch();
							System.out.println(currentRecord);
							ps.executeUpdate();
						}
					}
					// int j[] = ps.executeBatch();
					// for (int i = 0; i < j.length; i++)
					// System.out.println(j[i]);
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				br.close();
				keyIn.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("booksFile doesn't exist!");
		}

	}

	private void insertLibrary_Branch() {

		File booksFile = new File("DBData/library_branch.csv");

		if (booksFile.exists()) {
			try {
				FileReader fReader = new FileReader(booksFile);
				BufferedReader br = new BufferedReader(fReader);
				String currentRecord = br.readLine();
				PreparedStatement ps = null;
				try {
					ps = this.mConnection
							.prepareStatement("insert into library_branch values(?,?,?);");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				while ((currentRecord = br.readLine()) != null) {
					try {
						String[] splitOutput = currentRecord.split("\t");
						int branch_id = Integer.parseInt(splitOutput[0].trim()); // Branch_id
						String branch_Name = splitOutput[1].trim(); // Branch_Name
						String branch_Address = splitOutput[2].trim(); // Branch_Address
						ps.setInt(1, branch_id);
						ps.setString(2, branch_Name.trim());
						ps.setString(3, branch_Address.trim());
						ps.executeUpdate();
						System.out.println("Inserted");
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (SQLException e) {

					}
				}
				br.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("booksFile doesn't exist!");
		}

	}

	private void insertBook_Copies() {

		File booksFile = new File("DBData/book_copies.csv");

		if (booksFile.exists()) {
			try {
				FileReader fReader = new FileReader(booksFile);
				BufferedReader br = new BufferedReader(fReader);
				String currentRecord = br.readLine();
				PreparedStatement ps = null;
				try {
					ps = this.mConnection
							.prepareStatement("insert into book_copies values(?,?,?);");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				while ((currentRecord = br.readLine()) != null) {
					try {
						String[] splitOutput = currentRecord.split("\t");
						String book_id = splitOutput[0].trim(); // Book_id
						int branch_id = Integer.parseInt(splitOutput[1].trim()); // Branch_id
						int no_of_copies = Integer.parseInt(splitOutput[2]
								.trim()); // No_of_copies
						ps.setString(1, book_id.trim());
						ps.setInt(2, branch_id);
						ps.setInt(3, no_of_copies);
						ps.executeUpdate();
						System.out.println("Inserted");
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (SQLException e) {

					}
				}
				br.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("booksFile doesn't exist!");
		}

	}

	private void insertBorrower() {

		File booksFile = new File("DBData/borrowers.csv");

		if (booksFile.exists()) {
			try {
				FileReader fReader = new FileReader(booksFile);
				BufferedReader br = new BufferedReader(fReader);
				String currentRecord = br.readLine();
				PreparedStatement ps = null;
				try {
					ps = this.mConnection
							.prepareStatement("insert into borrower values(?,?,?,?,?,?,?);");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				while ((currentRecord = br.readLine()) != null) {
					try {
						String[] splitOutput = currentRecord.split("\t");
						int card_no = Integer.parseInt(splitOutput[0].trim());
						String fname = splitOutput[1].trim();
						String lname = splitOutput[2].trim();
						String address = splitOutput[3].trim();
						String city = splitOutput[4].trim();
						String state = splitOutput[5].trim();
						String phone = splitOutput[6].trim();

						ps.setInt(1, card_no);
						ps.setString(2, fname);
						ps.setString(3, lname);
						ps.setString(4, address);
						ps.setString(5, city);
						ps.setString(6, state);
						ps.setString(7, phone);

						ps.executeUpdate();
						System.out.println("Inserted");

					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (SQLException e) {

					}
				}
				br.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("booksFile doesn't exist!");
		}

	}

	private void insertBook_Loans() {

		File booksFile = new File("DBData/book_loans_data_F14.csv");

		if (booksFile.exists()) {
			try {
				FileReader fReader = new FileReader(booksFile);
				BufferedReader br = new BufferedReader(fReader);
				String currentRecord = br.readLine();
				PreparedStatement ps = null;
				try {
					ps = this.mConnection
							.prepareStatement("insert into book_loans values(?,?,?,?,?,?,?);");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				while ((currentRecord = br.readLine()) != null) {
					try {
						String[] splitOutput = currentRecord.split("\t");
						System.out.println(currentRecord + ":");
						int loan_id = Integer.parseInt(splitOutput[0].trim());
						if (loan_id == 12) {
							System.out.println("");
						}
						String book_id = splitOutput[1].trim();
						int branch_id = Integer.parseInt(splitOutput[2].trim());
						int card_no = Integer.parseInt(splitOutput[3].trim());
						String date_out = splitOutput[4].trim();
						String due_date = splitOutput[5].trim();
						String date_in = splitOutput[6].trim();

						ps.setInt(1, loan_id);
						ps.setString(2, book_id);
						ps.setInt(3, branch_id);
						ps.setInt(4, card_no);
						ps.setString(5, date_out);
						ps.setString(6, due_date);
						if (date_in.equalsIgnoreCase("NULL"))
							date_in = null;
						ps.setString(7, date_in);

						ps.executeUpdate();
						System.out.println("Inserted");

					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				br.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("booksFile doesn't exist!");
		}

	}

	private void insertNextValues() {
		try {
			this.mConnection.prepareStatement(
					"insert into nextvalue values('card_no', 9042);")
					.executeUpdate();
			this.mConnection.prepareStatement(
					"insert into nextvalue values('loan_id', 22);")
					.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void insertAdminUser() {

		try {
			this.mConnection.prepareStatement(
					"insert into userinfo values('admin', 'admin');")
					.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PopulateDB ib = new PopulateDB();
		if (ib.mConnection != null) {
			ib.insertBooks();
			ib.insertBook_Authors();
			ib.insertLibrary_Branch();
			ib.insertBook_Copies();
			ib.insertBorrower();
			ib.insertBook_Loans();
			ib.insertNextValues();
			ib.insertAdminUser();
			try {
				ib.mConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
