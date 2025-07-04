package com.bookstore.admindaoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookstore.admindao.AdminBookDao;
import com.bookstore.model.Books;
import com.bookstore.util.DBUtil;

public class AdminBookDaoImpl implements AdminBookDao {
	public boolean addBook(Books book) {
		boolean f = false;
		String query = "INSERT INTO Books (book_ID, title, author, description, price, coverImage) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection connection = DBUtil.openConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, book.getBookId());
			ps.setString(2, book.getTitle());
			ps.setString(3, book.getAuthor());
			ps.setString(4, book.getDescription());
			ps.setDouble(5, book.getPrice());
			ps.setString(6, book.getBookImage());

			/* return ps.executeUpdate() > 0; */

			int i = ps.executeUpdate();
			if (i == 1) {
				f = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return f;
	}

	public Books getBookById(String id) {
		Books book = null;
		String query = "SELECT * FROM Books WHERE book_id = ? ";
		try (Connection connection = DBUtil.openConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {

			ps.setString(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					book = new Books();
					book.setBookId(rs.getString("book_ID"));
					book.setAuthor(rs.getString("author"));
					book.setPrice(rs.getDouble("price"));
					book.setBookImage(rs.getString("coverImage"));
					book.setTitle(rs.getString("title"));
					book.setDescription(rs.getString("description"));

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return book;

	}

	@Override
	public boolean updateEditBooks(Books book) {
		boolean f = false;
		String query = "UPDATE Books SET title = ?, author = ?, description = ?, price = ? WHERE book_ID = ?";
		try (Connection connection = DBUtil.openConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {

			ps.setString(1, book.getTitle());
			ps.setString(2, book.getAuthor());
			ps.setString(3, book.getDescription());
			ps.setDouble(4, book.getPrice());
			ps.setString(5, book.getBookId());

			/* return ps.executeUpdate() > 0; */

			int i = ps.executeUpdate();
			if (i == 1) {
				f = true;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return f;

	}

	@Override
	public boolean deleteBooks(String id) {
		boolean f = false;
		
		String query = "DELETE FROM Books WHERE book_ID = ?";
		try (Connection connection = DBUtil.openConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {

			ps.setString(1, id);
			/* return ps.executeUpdate() > 0; */

			int i = ps.executeUpdate();
			if (i == 1) {
				f = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return f;
	}
	
	/**
	 * Find a book by its title and author
	 * @param title The title of the book
	 * @param author The author of the book
	 * @return The Books object if found, null otherwise
	 */
	public Books getBookByTitleAndAuthor(String title, String author) {
	    Books book = null;
		try (Connection conn = DBUtil.openConnection()) {
			String sql = "SELECT * FROM books WHERE title = ? AND author = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, title);
				ps.setString(2, author);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						book = new Books();
						book.setBookId(rs.getString("book_id"));
						book.setTitle(rs.getString("title"));
						book.setAuthor(rs.getString("author"));
						book.setPrice(rs.getDouble("price"));
						book.setDescription(rs.getString("description"));
						book.setBookImage(rs.getString("coverImage"));
						book.setQuantity(rs.getInt("stock"));
						// Set other properties as needed
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return book;
	}

	/**
	 * Update the quantity of a book
	 * @param bookId The ID of the book to update
	 * @param quantityToAdd The quantity to add to the book's current quantity
	 * @return true if the update was successful, false otherwise
	 */
	public boolean updateBookQuantity(String bookId, int quantityToAdd) {
	    boolean success = false;
		try (Connection conn = DBUtil.openConnection()) {
			// First get current quantity
			int currentQuantity = 0;
			String selectSql = "SELECT stock FROM books WHERE book_id = ?";
			try (PreparedStatement selectPs = conn.prepareStatement(selectSql)) {
				selectPs.setString(1, bookId);
				try (ResultSet rs = selectPs.executeQuery()) {
					if (rs.next()) {
						currentQuantity = rs.getInt("stock");
					}
				}
			}
			
			// Update with new quantity
			String updateSql = "UPDATE books SET stock = ? WHERE book_id = ?";
			try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
				ps.setInt(1, currentQuantity + quantityToAdd);
				ps.setString(2, bookId);
				int rowsAffected = ps.executeUpdate();
				success = rowsAffected > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	/**
	 * Get all books from the database
	 * @return List of all books
	 */
	public List<Books> getAllBooks() {
		List<Books> bookList = new ArrayList<>();
		try (Connection conn = DBUtil.openConnection()) {
			String sql = "SELECT * FROM Books";
			try (PreparedStatement ps = conn.prepareStatement(sql);
				 ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Books book = new Books();
					book.setBookId(rs.getString("book_ID"));
					book.setTitle(rs.getString("title"));
					book.setAuthor(rs.getString("author"));
					book.setPrice(rs.getDouble("price"));
					book.setDescription(rs.getString("description"));
					book.setBookImage(rs.getString("coverImage"));
					book.setQuantity(rs.getInt("stock"));
					bookList.add(book);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookList;
	}

	/**
	 * Refill the stock of a book
	 * @param bookId The ID of the book to refill
	 * @param newStock The new stock value to set
	 * @return true if the refill was successful, false otherwise
	 */
	public boolean refillBookStock(String bookId, int newStock) {
		boolean success = false;
		try (Connection conn = DBUtil.openConnection()) {
			String updateSql = "UPDATE Books SET stock = ? WHERE book_ID = ?";
			try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
				ps.setInt(1, newStock);
				ps.setString(2, bookId);
				int rowsAffected = ps.executeUpdate();
				success = rowsAffected > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	/**
	 * Get the current stock of a book
	 * @param bookId The ID of the book
	 * @return The current stock quantity or -1 if an error occurs
	 */
	public int getBookCurrentStock(String bookId) {
		int stock = -1;
		try (Connection conn = DBUtil.openConnection()) {
			String sql = "SELECT stock FROM Books WHERE book_ID = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, bookId);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						stock = rs.getInt("stock");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stock;
	}
}
