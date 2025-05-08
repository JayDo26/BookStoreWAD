package com.bookstore.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookstore.dao.BookDao;
import com.bookstore.model.Books;
import com.bookstore.util.DBUtil;

public class BookDaoImpl implements BookDao {
	@Override
	public List<Books> getRandomBooks(int limit) {
		List<Books> books = new ArrayList<>();
		String query = "SELECT * FROM Books ORDER BY RANDOM() LIMIT ?";

		try (Connection connection = DBUtil.openConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {

			ps.setInt(1, limit);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Books book = new Books();
					book.setBookId(rs.getString("book_id"));
					book.setAuthor(rs.getString("author"));
					book.setPrice(rs.getDouble("price"));
					book.setBookImage(rs.getString("coverimage"));
					book.setTitle(rs.getString("title"));
					book.setDescription(rs.getString("description"));


					try {
						boolean isOnSale = rs.getBoolean("is_on_sale");
						if (!rs.wasNull()) {
							book.setSale(isOnSale);
						}
					} catch (SQLException e) {
						// If the column doesn't exist, we'll determine sale status below
						// No need to do anything here
					}
					
					// Make sure to handle null values for discount_perc and sale_price
					boolean hasDiscount = false;
					try {
						double discountPerc = rs.getDouble("discount_percent");
						if (!rs.wasNull() && discountPerc > 0) {
							book.setDiscount_perc(discountPerc);
							hasDiscount = true;
						} else {
							book.setDiscount_perc(0.0);
						}
					} catch (SQLException e) {
						book.setDiscount_perc(0.0);
					}
					
					// Get sale price if available
					boolean hasSalePrice = false;
					try {
						double salePrice = rs.getDouble("sale_price");
						if (!rs.wasNull() && salePrice > 0 && salePrice < book.getPrice()) {
							book.setSale_price(salePrice);
							hasSalePrice = true;
						} else {
							book.setSale_price(0.0);
						}
					} catch (SQLException e) {
						book.setSale_price(0.0);
					}
					
					// If sale flag wasn't set from database but we have discount or sale price, mark as on sale
					if (!book.isSale() && (hasDiscount || hasSalePrice)) {
						book.setSale(true);
					}
					
					// Get stock if available
					try {
						int stock = rs.getInt("stock");
						if (!rs.wasNull()) {
							book.setStock(stock);
						}
					} catch (SQLException e) {
						book.setStock(10); // Default
					}
					
					books.add(book);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error in getRandomBooks: " + e.getMessage());
			e.printStackTrace();
		}

		return books;
	}

	@Override
	public List<Books> getAllBooks() {
		// Run diagnostic first to see what tables are available
		com.bookstore.util.DatabaseDiagnostic.runDiagnostic();

		List<Books> allBooks = new ArrayList<>();

		// Try with lowercase table name without quotes (PostgreSQL default)
		String query = "SELECT * FROM Books ORDER BY title";

		try (Connection connection = DBUtil.openConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Books book = new Books();
					book.setBookId(rs.getString("book_id"));
					book.setAuthor(rs.getString("author"));
					book.setPrice(rs.getFloat("price"));
					book.setBookImage(rs.getString("coverimage"));
					book.setTitle(rs.getString("title"));
					book.setDescription(rs.getString("description"));
					
					// Set sale flag based on is_on_sale column if it exists
					try {
						boolean isOnSale = rs.getBoolean("is_on_sale");
						if (!rs.wasNull()) {
							book.setSale(isOnSale);
						}
					} catch (SQLException e) {
						// If the column doesn't exist, we'll determine sale status below
						// No need to do anything here
					}
					
					// Make sure to handle null values for discount_perc and sale_price
					boolean hasDiscount = false;
					try {
						double discountPerc = rs.getDouble("discount_percent");
						if (!rs.wasNull() && discountPerc > 0) {
							book.setDiscount_perc(discountPerc);
							hasDiscount = true;
						} else {
							book.setDiscount_perc(0.0);
						}
					} catch (SQLException e) {
						book.setDiscount_perc(0.0);
					}
					
					// Get sale price if available
					boolean hasSalePrice = false;
					try {
						double salePrice = rs.getDouble("sale_price");
						if (!rs.wasNull() && salePrice > 0 && salePrice < book.getPrice()) {
							book.setSale_price(salePrice);
							hasSalePrice = true;
						} else {
							book.setSale_price(0.0);
						}
					} catch (SQLException e) {
						book.setSale_price(0.0);
					}
					
					// If sale flag wasn't set from database but we have discount or sale price, mark as on sale
					if (!book.isSale() && (hasDiscount || hasSalePrice)) {
						book.setSale(true);
					}
					
					// Get stock if available
					try {
						int stock = rs.getInt("stock");
						if (!rs.wasNull()) {
							book.setStock(stock);
						}
					} catch (SQLException e) {
						book.setStock(10); // Default
					}
					
					allBooks.add(book);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error with query: " + query);
			System.err.println("Message: " + e.getMessage());
		}

		return allBooks;
	}

	public double getBookPrice(String bookID) {
		double bookPrice = 0.0;
		String query = "SELECT price FROM Books WHERE book_id = ?";

		try (Connection con = DBUtil.openConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, bookID);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					bookPrice = rs.getDouble("price");
				}
			}
		} catch (SQLException e) {
			System.err.println("Error in getBookPrice: " + e.getMessage());
			e.printStackTrace();
		}

		return bookPrice;
	}

	@Override
	public Books getBookById(String bookID) {
		String query = "SELECT * FROM Books WHERE book_id = ?";
		Books book = null;

		try (Connection con = DBUtil.openConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, bookID);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					book = new Books();
					book.setBookId(rs.getString("book_id"));
					book.setTitle(rs.getString("title"));
					book.setAuthor(rs.getString("author"));
					book.setPrice(rs.getDouble("price"));
					book.setDescription(rs.getString("description"));
					book.setBookImage(rs.getString("coverimage"));
					
					// Try to get stock and sale fields
					try {
						double discountPerc = rs.getDouble("discount_percent");
						if (!rs.wasNull()) {
							book.setDiscount_perc(discountPerc);
						}
					} catch (SQLException e) {
						book.setDiscount_perc(0.0);
					}
					
					// Get sale price if available
					try {
						double salePrice = rs.getDouble("sale_price");
						if (!rs.wasNull()) {
							book.setSale_price(salePrice);
						}
					} catch (SQLException e) {
						book.setSale_price(0.0);
					}
					
					// Get stock if available
					try {
						int stock = rs.getInt("stock");
						if (!rs.wasNull()) {
							book.setStock(stock);
						}
					} catch (SQLException e) {
						book.setStock(10); // Default
					}
					
				}
			}
		} catch (SQLException e) {
			System.err.println("Error in getBookById: " + e.getMessage());
			e.printStackTrace();
		}

		return book;
	}

	@Override
	public List<Books> getBooksOnSale() {
		List<Books> saleBooks = new ArrayList<>();
		
		// Try different variations of the column name for sale flag
		List<String> queries = new ArrayList<>();
		queries.add("SELECT * FROM Books WHERE is_on_sale = true ORDER BY title");
		
		boolean foundBooks = false;
		
		for (String query : queries) {
			System.out.println("Trying query to fetch sale books: " + query);
			
			try (Connection connection = DBUtil.openConnection();
					PreparedStatement ps = connection.prepareStatement(query)) {
				
				try (ResultSet rs = ps.executeQuery()) {
					int count = 0;
					while (rs.next()) {
						count++;
						foundBooks = true;
						Books book = new Books();
						book.setBookId(rs.getString("book_id"));
						book.setAuthor(rs.getString("author"));
						book.setPrice(rs.getFloat("price"));
						book.setBookImage(rs.getString("coverimage"));
						book.setTitle(rs.getString("title"));
						book.setDescription(rs.getString("description"));
						book.setSale(true); // These are on sale
						
						// Get discount percentage if available
						try {
							double discountPerc = rs.getDouble("discount_percent");
							if (!rs.wasNull()) {
								book.setDiscount_perc(discountPerc);
							}
						} catch (SQLException e) {
							book.setDiscount_perc(0.0);
						}
						
						// Get sale price if available
						try {
							double salePrice = rs.getDouble("sale_price");
							if (!rs.wasNull()) {
								book.setSale_price(salePrice);
							}
						} catch (SQLException e) {
							book.setSale_price(0.0);
						}
						
						// Get stock if available
						try {
							int stock = rs.getInt("stock");
							if (!rs.wasNull()) {
								book.setStock(stock);
							}
						} catch (SQLException e) {
							book.setStock(10); // Default
						}
						
						saleBooks.add(book);
						System.out.println("Added sale book: " + book.getTitle());
					}
					System.out.println("Found " + count + " books on sale with query: " + query);
					
					if (foundBooks) {
						// If we found books with this query, no need to try other queries
						break;
					}
				}
			} catch (SQLException e) {
				// Simply try the next query if this one fails
				System.err.println("Query failed: " + query + " - Error: " + e.getMessage());
			}
		}

		return saleBooks;
	}
}

