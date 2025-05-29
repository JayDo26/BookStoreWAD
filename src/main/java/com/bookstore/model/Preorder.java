package com.bookstore.model;

import java.util.Date;

public class Preorder {
    private int preorderId;
    private int userId;
    private int bookId;
    private Date preorderDate;
    private boolean isNotified;
    
    // Thông tin bổ sung
    private String userEmail;
    private String userName;
    private String bookTitle;
    
    // Constructors
    public Preorder() {}
    
    public Preorder(int userId, int bookId, Date preorderDate, boolean isNotified) {
        this.userId = userId;
        this.bookId = bookId;
        this.preorderDate = preorderDate;
        this.isNotified = isNotified;
    }
    
    // Getters and Setters
    public int getPreorderId() {
        return preorderId;
    }
    
    public void setPreorderId(int preorderId) {
        this.preorderId = preorderId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getBookId() {
        return bookId;
    }
    
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    
    public Date getPreorderDate() {
        return preorderDate;
    }
    
    public void setPreorderDate(Date preorderDate) {
        this.preorderDate = preorderDate;
    }
    
    public boolean isNotified() {
        return isNotified;
    }
    
    public void setNotified(boolean isNotified) {
        this.isNotified = isNotified;
    }
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getBookTitle() {
        return bookTitle;
    }
    
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}
