package com.bookstore.model;
import com.bookstore.util.IDGenerateUtil;

public class Books {
	    private String bookId;
	    private String title;
	    private String author;
	    private String description;
	    private double price;
	    private String bookImage;
	    private int stock = 10; // Default stock value
	    private boolean sale = false; // Default not on sale
	    private double discount_perc = 0.0; // Discount percentage from database
	    private double sale_price = 0.0; // Sale price from database
	    
	    public Books() {
	    
	    }
		public Books(String title, String author, String description, double price, String bookImage) {
			this.bookId = IDGenerateUtil.generateBookId();
			this.title = title;
			this.author = author;
			this.description = description;
			this.price = price;
			this.bookImage = bookImage;
		}
		public String getBookImage() {
			return bookImage;
		}
		public void setBookImage(String bookImage) {
			this.bookImage = bookImage;
		}
		public String getBookId() {
			return bookId;
		}
		public void setBookId(String bookId) {
			this.bookId = bookId;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getAuthor() {
			return author;
		}
		public void setAuthor(String author) {
			this.author = author;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		public int getStock() {
			return stock;
		}
		public void setStock(int stock) {
			this.stock = stock;
		}
		public boolean isSale() {
			return sale;
		}
		public void setSale(boolean sale) {
			this.sale = sale;
		}
		public double getDiscount_perc() {
	        return discount_perc;
	    }
	    
	    public void setDiscount_perc(double discount_perc) {
	        this.discount_perc = discount_perc;
	    }
	    
	    public double getSale_price() {
	        return sale_price;
	    }
	    
	    public void setSale_price(double sale_price) {
	        this.sale_price = sale_price;
	    }
	    
	    // Helper method to get the actual price (either sale price or regular price)
	    public double getActualPrice() {
	        if (sale) {
	            if (sale_price > 0) {
	                return sale_price;
	            } else if (discount_perc > 0) {
	                return price - (price * discount_perc / 100);
	            } else {
	                // Default discount if neither sale_price nor discount_perc is specified
	                return price * 0.8; // 20% off
	            }
	        }
	        return price;
	    }
	}


