package com.bookstore.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstore.daoimpl.BookDaoImpl;
import com.bookstore.model.Books;

@WebServlet("/GetBookDetailsServlet")
public class GetBookDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String bookId = request.getParameter("bookId");
        
        if (bookId != null && !bookId.isEmpty()) {
            BookDaoImpl bookDao = new BookDaoImpl();
            Books book = bookDao.getBookById(bookId);
            
            if (book != null) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                
                // Manual JSON serialization
                String jsonBook = createJsonFromBook(book);
                
                PrintWriter out = response.getWriter();
                out.print(jsonBook);
                out.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    /**
     * Manually create a JSON string from a Book object
     */
    private String createJsonFromBook(Books book) {
        StringBuilder json = new StringBuilder("{");
        
        // Add book properties
        appendJsonProperty(json, "bookId", book.getBookId(), true);
        appendJsonProperty(json, "title", book.getTitle(), true);
        appendJsonProperty(json, "author", book.getAuthor(), true);
        appendJsonProperty(json, "price", String.valueOf(book.getPrice()), false);
        appendJsonProperty(json, "description", book.getDescription(), true);
        appendJsonProperty(json, "bookImage", book.getBookImage(), true);
        appendJsonProperty(json, "stock", String.valueOf(book.getStock()), false);
        appendJsonProperty(json, "sale", String.valueOf(book.isSale()), false);
        appendJsonProperty(json, "discount_perc", String.valueOf(book.getDiscount_perc()), false);
        appendJsonProperty(json, "sale_price", String.valueOf(book.getSale_price()), false);
        appendJsonProperty(json, "actualPrice", String.valueOf(book.getActualPrice()), false);
        
        // Close JSON object
        if (json.charAt(json.length() - 1) == ',') {
            json.deleteCharAt(json.length() - 1);
        }
        json.append("}");
        
        return json.toString();
    }
    
    /**
     * Helper method to append a property to the JSON string
     */
    private void appendJsonProperty(StringBuilder json, String key, String value, boolean isString) {
        if (value == null) value = "";
        
        json.append("\"").append(key).append("\":");
        if (isString) {
            json.append("\"").append(escapeJsonString(value)).append("\"");
        } else {
            json.append(value);
        }
        json.append(",");
    }
    
    /**
     * Helper method to escape special characters in JSON strings
     */
    private String escapeJsonString(String input) {
        if (input == null) return "";
        
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
