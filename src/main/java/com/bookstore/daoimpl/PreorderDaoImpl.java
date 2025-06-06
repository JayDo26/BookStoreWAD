package com.bookstore.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bookstore.dao.PreorderDao;
import com.bookstore.model.Preorder;
import com.bookstore.util.DBUtil;

public class PreorderDaoImpl implements PreorderDao {
    
    private static final Logger logger = Logger.getLogger(PreorderDaoImpl.class.getName());
    
    /**
     * Thêm một yêu cầu preorder mới vào cơ sở dữ liệu
     */
    @Override
    public boolean addPreorder(int userId, int bookId) {
        String sql = "INSERT INTO preorders (user_id, book_id, preorder_date, is_notified) VALUES (?, ?, NOW(), false)";

        try (Connection conn = DBUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding preorder", e);
            return false;
        }
    }
    
    /**
     * Kiểm tra xem người dùng đã đăng ký preorder cho sách này chưa
     */
    @Override
    public boolean isAlreadyPreordered(int userId, int bookId) {
        String sql = "SELECT * FROM preorders WHERE user_id = ? AND book_id = ? AND is_notified = false";
        
        try (Connection conn = DBUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking if already preordered", e);
            return false;
        }
    }
    
    /**
     * Lấy danh sách người dùng đã đăng ký preorder cho một sách
     */
    @Override
    public List<Preorder> getPreordersByBookId(int bookId) throws SQLException {
        List<Preorder> preorders = new ArrayList<>();
        String sql = "SELECT p.*, u.email, u.name, b.title FROM preorders p " +
                     "JOIN users u ON p.user_id = u.user_id " +
                     "JOIN books b ON p.book_id = b.book_id " +
                     "WHERE p.book_id = ? AND p.is_notified = false";
        
        try (Connection conn = DBUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Preorder preorder = new Preorder();
                preorder.setPreorderId(rs.getInt("preorder_id"));
                preorder.setUserId(rs.getInt("user_id"));
                preorder.setBookId(rs.getInt("book_id"));
                preorder.setPreorderDate(rs.getTimestamp("preorder_date"));
                preorder.setNotified(rs.getBoolean("is_notified"));
                preorder.setUserEmail(rs.getString("email"));
                preorder.setUserName(rs.getString("name"));
                preorder.setBookTitle(rs.getString("title"));
                preorders.add(preorder);
            }
        }
        
        return preorders;
    }
    
    /**
     * Cập nhật trạng thái đã thông báo cho các preorder
     */
    @Override
    public boolean markAsNotified(int bookId) {
        String sql = "UPDATE preorders SET is_notified = true WHERE book_id = ? AND is_notified = false";
        
        try (Connection conn = DBUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error marking preorders as notified", e);
            return false;
        }
    }

    @Override
    public boolean isAlreadyPreorderedByEmail(String email, int bookId) {
        // Implementation to check if email has already preordered the book
        // This is a placeholder implementation - adjust according to your database structure
        try (Connection conn = DBUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT COUNT(*) FROM preorders WHERE email = ? AND book_id = ?")) {
            
            stmt.setString(1, email);
            stmt.setInt(2, bookId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking if email has preordered", e);
        }
        return false;
    }

    @Override
    public boolean addPreorderWithEmail(String email, String name, int bookId) {
        // Implementation to add a preorder with email
        // This is a placeholder implementation - adjust according to your database structure
        try (Connection conn = DBUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO preorders (email, name, book_id, preorder_date) VALUES (?, ?, ?, NOW())")) {
            
            stmt.setString(1, email);
            stmt.setString(2, name);
            stmt.setInt(3, bookId);
            
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding preorder with email", e);
        }
        return false;
    }
}
