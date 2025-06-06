package com.bookstore.dao;

import java.sql.SQLException;
import java.util.List;

import com.bookstore.model.Preorder;

public interface PreorderDao {
    boolean addPreorder(int userId, int bookId);
    boolean isAlreadyPreordered(int userId, int bookId);
    List<Preorder> getPreordersByBookId(int bookId) throws SQLException;
    boolean markAsNotified(int bookId);
    boolean isAlreadyPreorderedByEmail(String email, int bookId);
    boolean addPreorderWithEmail(String email, String name, int bookId);
}
