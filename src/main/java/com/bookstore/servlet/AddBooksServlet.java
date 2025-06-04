package com.bookstore.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.bookstore.admindaoimpl.AdminBookDaoImpl;
import com.bookstore.daoimpl.PreorderDaoImpl;
import com.bookstore.model.Books;
import com.bookstore.model.Preorder;
import com.bookstore.util.EmailSender;
import com.bookstore.util.IDGenerateUtil;

@WebServlet("/AddBooksServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
		maxFileSize = 1024 * 1024 * 10, // 10MB
		maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class AddBooksServlet extends HttpServlet {
	@SuppressWarnings("unused")
	private static final String UPLOAD_DIR = "book_images";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();

		if (session.getAttribute("succMsg") == null) {
			session.setAttribute("succMsg", ""); // Empty initialization
		}
		if (session.getAttribute("failedMsg") == null) {
			session.setAttribute("failedMsg", ""); // Empty initialization
		}

		try {
			String bookType = req.getParameter("bookType"); // "new" or "old"
			String title = req.getParameter("bname");
			String author = req.getParameter("author");
			double price = Double.parseDouble(req.getParameter("price"));
			String description = req.getParameter("bdes");
			int quantity = Integer.parseInt(req.getParameter("quantity"));
			boolean notifyPreorders = req.getParameter("notifyPreorders") != null;
			
			AdminBookDaoImpl dao = new AdminBookDaoImpl();
			
			// Check if we're adding a new book or refilling an existing one
			if ("new".equals(bookType)) {
				// Check if the book already exists
				Books existingBook = dao.getBookByTitleAndAuthor(title, author);
				
				if (existingBook != null) {
					// Book already exists, inform the user to use refill option
					session.setAttribute("failedMsg", "Book with same title and author already exists. Please use the refill option instead.");
					resp.sendRedirect("admin/addBooks.jsp");
					return;
				}
				
				// Process new book
				Part filePart = req.getPart("bimg");
				String fileName = extractFileName(filePart);

				// Define the upload directory and create it if it doesn't exist
				String uploadPath = getServletContext().getRealPath("") + File.separator + "images";
				File uploadDir = new File(uploadPath);
				if (!uploadDir.exists()) {
					uploadDir.mkdir();
				}

				// Save the uploaded file to the target directory
				String filePath = uploadPath + File.separator + fileName;
				filePart.write(filePath);

				// Save the file path relative to the application context
				String relativePath = "images/" + fileName;

				// Create and save the book object
				Books b = new Books();
				b.setBookId(IDGenerateUtil.generateBookId());
				b.setAuthor(author);
				b.setTitle(title);
				b.setPrice(price);
				b.setDescription(description);
				b.setBookImage(relativePath);
				b.setQuantity(quantity); // Set initial quantity

				boolean f = dao.addBook(b);

				if (f) {
					session.setAttribute("succMsg", "Book added successfully");
					
					// Notify preorder users if requested
					if (notifyPreorders) {
						notifyPreorderUsers(Integer.parseInt(b.getBookId()), title);
					}
				} else {
					session.setAttribute("failedMsg", "Error while adding book");
				}
			} else {
				// Handle refilling existing book
				String bookId = req.getParameter("bookId");
				
				if (bookId == null || bookId.isEmpty()) {
					// Try to find the book by title and author
					Books existingBook = dao.getBookByTitleAndAuthor(title, author);
					
					if (existingBook == null) {
						session.setAttribute("failedMsg", "Could not find existing book with this title and author. Please add as a new book.");
						resp.sendRedirect("admin/addBooks.jsp");
						return;
					}
					
					bookId = existingBook.getBookId();
				}
				
				boolean updated = dao.updateBookQuantity(bookId, quantity);
				
				if (updated) {
					session.setAttribute("succMsg", "Book quantity updated successfully");
					
					// Notify preorder users if requested and if the book was out of stock before
					if (notifyPreorders) {
						notifyPreorderUsers(Integer.parseInt(bookId), title);
					}
				} else {
					session.setAttribute("failedMsg", "Error while updating book quantity");
				}
			}

			resp.sendRedirect("admin/addBooks.jsp");

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("failedMsg", "Error: " + e.getMessage());
			resp.sendRedirect("admin/addBooks.jsp");
		}
	}

	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		for (String content : contentDisp.split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf("=") + 2, content.length() - 1);
			}
		}
		return "default.png";
	}
	
	/**
	 * Notify users who have preordered a book that is now available
	 * @param bookId the ID of the book that is now available
	 * @param bookTitle the title of the book
	 */
	private void notifyPreorderUsers(int bookId, String bookTitle) {
		try {
			PreorderDaoImpl preorderDao = new PreorderDaoImpl();
			List<Preorder> preorders = preorderDao.getPreordersByBookId(bookId);
			
			if (preorders != null && !preorders.isEmpty()) {
				int successCount = 0;
				
				for (Preorder preorder : preorders) {
					boolean emailSent = EmailSender.sendBookAvailabilityNotification(
							preorder.getUserEmail(), 
							preorder.getUserName(), 
							preorder.getBookTitle());
					
					if (emailSent) {
						successCount++;
					}
				}
				
				// Mark all preorders as notified
				if (successCount > 0) {
					preorderDao.markAsNotified(bookId);
				}
				
				// You could log this information or add it to the success message
				System.out.println("Notified " + successCount + " users about book availability");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Log error but don't stop the process
			System.err.println("Error notifying preorder users: " + e.getMessage());
		}
	}
}
