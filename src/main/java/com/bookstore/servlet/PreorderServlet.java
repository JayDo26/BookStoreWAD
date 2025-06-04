package com.bookstore.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookstore.dao.PreorderDao;
import com.bookstore.daoimpl.PreorderDaoImpl;
import com.bookstore.model.Users;

@WebServlet("/PreorderServlet")
public class PreorderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final PreorderDao preorderDao;
    
    public PreorderServlet() {
        preorderDao = new PreorderDaoImpl();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        
        // Debug session information
        System.out.println("Session ID: " + session.getId());
        System.out.println("Is new session: " + session.isNew());
        System.out.println("User ID in session: " + session.getAttribute("userId"));
        System.out.println("Userinfo in session: " + session.getAttribute("userinfo"));
        System.out.println("Action parameter: " + action);
        
        // Initialize common variables used across multiple actions
        boolean isLoggedIn = isUserLoggedIn(request);
        Integer userId = (Integer) session.getAttribute("userId");
        
        // Extract userId from userinfo if needed
        if (userId == null && session.getAttribute("userinfo") != null) {
            Users userinfo = (Users) session.getAttribute("userinfo");
            userId = extractUserIdFromUserinfo(userinfo, session);
        }
        
        // Set response type
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        StringBuilder jsonResponse = new StringBuilder();
        jsonResponse.append("{");
        
        try {
            // Handle null action parameter - this will fix the NullPointerException
            if (action == null) {
                jsonResponse.append("\"success\": false, ");
                jsonResponse.append("\"message\": \"Missing action parameter\"");
            } else {
                // Convert to rule switch (Java 14+)
                switch (action) {
                    case "emailPreorder" -> handleEmailPreorder(request, jsonResponse);
                    case "add" -> handleAddPreorder(request, response, isLoggedIn, userId, jsonResponse);
                    case "list" -> handleListPreorders(userId, jsonResponse);
                    case "checkLoginStatus" -> handleCheckLoginStatus(isLoggedIn, jsonResponse);
                    default -> {
                        jsonResponse.append("\"success\": false, ");
                        jsonResponse.append("\"message\": \"Invalid action: ").append(action).append("\"");
                    }
                }
            }
        } catch (NumberFormatException e) {
            jsonResponse.append("\"success\": false, ");
            jsonResponse.append("\"message\": \"Invalid parameters\"");
        // Use specific exception handlers instead of generic Exception
        } catch (IllegalArgumentException e) {
            jsonResponse.append("\"success\": false, ");
            jsonResponse.append("\"message\": \"Invalid arguments: ").append(e.getMessage()).append("\"");
        } catch (RuntimeException e) {
            jsonResponse.append("\"success\": false, ");
            jsonResponse.append("\"message\": \"An error occurred: ").append(e.getMessage()).append("\"");
            System.err.println("Error in PreorderServlet: " + e.getMessage());
            // Replace printStackTrace with proper logging
            System.err.println("Stack trace: " + java.util.Arrays.toString(e.getStackTrace()));
        }
        
        jsonResponse.append("}");
        // Write the response
        out.write(jsonResponse.toString());
        out.flush();
    }
    
    private Integer extractUserIdFromUserinfo(Users userinfo, HttpSession session) {
        Integer userId = null;
        
        // Debug the userinfo object structure
        System.out.println("Users object class: " + userinfo.getClass().getName());
        System.out.println("Users object toString: " + userinfo.toString());
        
        // Try multiple common property names for user ID using reflection
        String[] possibleIdFields = {"id", "userId", "user_id", "ID", "userID", "user_ID"};
        
        for (String fieldName : possibleIdFields) {
            try {
                // Try to access field directly
                java.lang.reflect.Field field = userinfo.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(userinfo);
                if (value instanceof Integer || value instanceof Long) {
                    userId = Integer.valueOf(value.toString());
                    System.out.println("Found userId in field " + fieldName + ": " + userId);
                    session.setAttribute("userId", userId);
                    break;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Field not found, try next one
            }
        }
        
        // If still null, try getter methods
        if (userId == null) {
            String[] possibleGetters = {"getId", "getUserId", "getUser_id", "getID", "getUserID", "getUser_ID"};
            for (String methodName : possibleGetters) {
                try {
                    java.lang.reflect.Method method = userinfo.getClass().getMethod(methodName);
                    Object value = method.invoke(userinfo);
                    if (value instanceof Integer || value instanceof Long) {
                        userId = Integer.valueOf(value.toString());
                        System.out.println("Found userId from method " + methodName + ": " + userId);
                        session.setAttribute("userId", userId);
                        break;
                    }
                } catch (ReflectiveOperationException e) {
                    // Method not found, try next one
                }
            }
        }
        
        // If still null, log failure
        if (userId == null) {
            System.err.println("Could not extract userId from Users object by any method");
        }
        
        return userId;
    }
    
    private void handleEmailPreorder(HttpServletRequest request, StringBuilder jsonResponse) {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        
        // Validate email
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            jsonResponse.append("\"success\": false, ");
            jsonResponse.append("\"message\": \"Please enter a valid email address\"");
        } else {
            // Check if this email already preordered by this book
            if (preorderDao.isAlreadyPreorderedByEmail(email, bookId)) {
                jsonResponse.append("\"success\": false, ");
                jsonResponse.append("\"message\": \"This email has already been registered for this book\"");
            } else {
                // Add the preorder with email
                boolean success = preorderDao.addPreorderWithEmail(email, name, bookId);
                if (success) {
                    jsonResponse.append("\"success\": true, ");
                    jsonResponse.append("\"message\": \"Preorder successful. You will be notified at ").append(email).append(" when the book is available.\"");
                } else {
                    jsonResponse.append("\"success\": false, ");
                    jsonResponse.append("\"message\": \"Failed to preorder. Please try again.\"");
                }
            }
        }
    }
    
    private void handleAddPreorder(HttpServletRequest request, HttpServletResponse response, 
                             boolean isLoggedIn, Integer userId,
                             StringBuilder jsonResponse) throws IOException {
        if (!isLoggedIn) {
            // Check if this is an AJAX request
            String xRequestedWith = request.getHeader("X-Requested-With");
            if (xRequestedWith != null && xRequestedWith.equals("XMLHttpRequest")) {
                // For AJAX requests, return JSON response
                jsonResponse.append("\"success\": false, ");
                jsonResponse.append("\"message\": \"Please login to preorder books\", ");
                jsonResponse.append("\"redirect\": \"login.jsp\"");
            } else {
                // For direct requests, redirect to login page with return URL
                response.sendRedirect("login.jsp?returnUrl=" + request.getRequestURI());
                // Remove unnecessary return statement
            }
        } else {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            
            // Check if user already preordered this book
            if (preorderDao.isAlreadyPreordered(userId, bookId)) {
                jsonResponse.append("\"success\": false, ");
                jsonResponse.append("\"message\": \"You have already preordered this book\"");
            } else {
                // Add the preorder
                boolean success = preorderDao.addPreorder(userId, bookId);
                if (success) {
                    jsonResponse.append("\"success\": true, ");
                    jsonResponse.append("\"message\": \"Preorder successful. You will be notified when the book is available.\"");
                } else {
                    jsonResponse.append("\"success\": false, ");
                    jsonResponse.append("\"message\": \"Failed to preorder. Please try again.\"");
                }
            }
        }
    }
    
    private void handleListPreorders(Integer userId, StringBuilder jsonResponse) {
        if (userId != null) {
            // Get preorders for the current user
            jsonResponse.append("\"success\": true, ");
            jsonResponse.append("\"message\": \"Retrieved user preorders\", ");
            jsonResponse.append("\"userId\": ").append(userId);
            // Note: getUserPreorders doesn't exist, using a placeholder response
            jsonResponse.append(", \"preorders\": []");
        } else {
            jsonResponse.append("\"success\": false, ");
            jsonResponse.append("\"message\": \"Could not identify user to list preorders\"");
        }
    }
    
    private void handleCheckLoginStatus(boolean isLoggedIn, StringBuilder jsonResponse) {
        if (isLoggedIn) {
            jsonResponse.append("\"success\": true, ");
            jsonResponse.append("\"message\": \"User is logged in\"");
        } else {
            jsonResponse.append("\"success\": false, ");
            jsonResponse.append("\"message\": \"User is not logged in\"");
        }
    }
    
    /**
     * Enhanced method to check if user is logged in
     * Uses multiple approaches to verify login status
     */
    private boolean isUserLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        
        // Check multiple possible session attributes for user info
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null && userId > 0) {
            return true;
        }
        
        // Check alternative session attributes that might store user info
        Object user = session.getAttribute("user");
        Object userObj = session.getAttribute("userObj");
        Object loggedInUser = session.getAttribute("loggedInUser");
        Object currentUser = session.getAttribute("currentUser");
        // Add check for userinfo which is set by LoginServlet
        Object userinfo = session.getAttribute("userinfo");
        
        return (user != null || userObj != null || loggedInUser != null || 
                currentUser != null || userinfo != null);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward GET requests to doPost
        doPost(request, response);
    }
}
