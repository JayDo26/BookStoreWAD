package com.bookstore.util;

import jakarta.mail.MessagingException;

public class EmailSender {
    
    /**
     * Gửi email thông báo sách có hàng
     */
    public static boolean sendBookAvailabilityNotification(String recipientEmail, String recipientName, String bookTitle) {
        try {
            // Tạo tiêu đề email
            String subject = "Thông báo: Sách \"" + bookTitle + "\" đã có hàng!";
            
            // Nội dung email
            String emailContent = 
                    "Xin chào " + recipientName + ",\n\n" +
                    "Chúng tôi vui mừng thông báo rằng cuốn sách \"" + bookTitle + "\" mà bạn đã đăng ký nhận thông báo " +
                    "hiện đã có hàng tại Pisces Bookstore.\n\n" +
                    "Hãy nhanh tay đặt sách trước khi hết hàng!\n\n" +
                    "Trân trọng,\n" +
                    "Đội ngũ Pisces Bookstore";
            
            // Sử dụng SendEmailUtil để gửi email
            SendEmailUtil.sendEmail(recipientEmail, subject, emailContent);
            
            return true;
            
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
