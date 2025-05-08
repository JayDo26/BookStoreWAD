package com.bookstore.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.daoimpl.BookDaoImpl;
import com.bookstore.model.Books;

/**
 
 */
@WebServlet(name = "HomeServlet", urlPatterns = "/home")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    BookDaoImpl bookService = new BookDaoImpl();
	    List<Books> randomBooks = bookService.getRandomBooks(2);
	    List<Books> bestSellingBook = bookService.getRandomBooks(1);
	    List<Books> allBooks = bookService.getAllBooks();
	    
	    // Get books on sale directly from the database
	    List<Books> saleBooks = bookService.getBooksOnSale();
	    
	    System.out.println("HomeServlet - Books on sale: " + saleBooks.size());
	    
	    // Instead of forcing books to be on sale, only display actual sale books
	    // No need to create fake sales if none exist
	    
	    // Log each sale book to confirm they exist
	    for (Books book : saleBooks) {
	        System.out.println("Sale book: " + book.getTitle() + " (Sale: " + book.isSale() + 
	                ", Discount: " + book.getDiscount_perc() + "%, Sale price: $" + book.getSale_price() + ")");
	    }
	    
	    request.setAttribute("bestSellingBook", bestSellingBook);
	    request.setAttribute("allBooks", allBooks);
	    request.setAttribute("randomBooks", randomBooks);
	    request.setAttribute("saleBooks", saleBooks); // Only truly discounted books
	    request.getRequestDispatcher("/index.jsp").forward(request, response);  
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
