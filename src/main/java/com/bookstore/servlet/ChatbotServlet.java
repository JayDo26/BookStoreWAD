package com.bookstore.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/chatbot")
public class ChatbotServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String API_URL = "http://logically-exact-phoenix.ngrok-free.app/chat";
    private static final Logger LOGGER = Logger.getLogger(ChatbotServlet.class.getName());
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Option 1: Respond with a JSON message explaining the endpoint requires POST
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"This endpoint requires a POST request\", \"reply\": \"Please use a POST request to interact with the chatbot.\"}");
        
        // Option 2 (alternative): Forward GET requests to doPost method
        // doPost(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        LOGGER.log(Level.INFO, "Received POST request to /chatbot endpoint");
        
        // Read the request body
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        
        // Log the request body
        LOGGER.log(Level.INFO, "Request body: " + requestBody.toString());
        
        // Check if request body is empty
        if (requestBody.length() == 0) {
            LOGGER.log(Level.WARNING, "Empty request body received");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Empty request body\", \"reply\": \"Please provide a message.\"}");
            return;
        }
        
        // Set up connection to external API
        URL url = URI.create(API_URL).toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        
        // Send request to external API
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        // Get response from external API
        StringBuilder responseBody = new StringBuilder();
        int statusCode = 200;
        
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                responseBody.append(responseLine);
            }
            LOGGER.log(Level.INFO, "API response: " + responseBody.toString());
        } catch (IOException e) {
            statusCode = con.getResponseCode();
            LOGGER.log(Level.WARNING, "API error: " + e.getMessage() + ", status code: " + statusCode);
            // In case of error, try to read the error stream
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getErrorStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    responseBody.append(responseLine);
                }
                LOGGER.log(Level.INFO, "API error response: " + responseBody.toString());
            }
            // If we can't read anything, provide a default error message in JSON format
            if (responseBody.length() == 0) {
                responseBody.append("{\"error\": \"Sorry, I couldn't process your request.\", \"reply\": \"Sorry, I couldn't process your request.\"}");
            }
        }
        
        // Set response headers for JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        
        // Write JSON response back to client
        response.getWriter().write(responseBody.toString());
        LOGGER.log(Level.INFO, "Response sent to client: " + responseBody.toString());
    }
}