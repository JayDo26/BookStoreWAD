<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page isELIgnored="false"%>
<%@ page import="com.bookstore.admindaoimpl.AdminBookDaoImpl" %>
<%@ page import="com.bookstore.model.Books" %>
<%@ page import="java.util.List" %>

<%
// Fetch all books for the dropdown
AdminBookDaoImpl dao = new AdminBookDaoImpl();
List<Books> bookList = dao.getAllBooks();
request.setAttribute("bookList", bookList);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>BS Admin: Add Books</title>
<%@include file="admin.jsp"%>
</head>
<body style="background-color: #F3F2EC;">
	<%@include file="adminNavBar.jsp"%>

	<div class="container">
		<div class="row">
			<div class="col-md-4 offset-md-4">
				<div class="cord">
					<div class="card-body"></div>

					<h4 class="text-center">Add Books</h4>

					<c:if test="${not empty succMsg}">
						<p class="text-center text-success">${succMsg}</p>
						<c:remove var="succMsg" scope="session" />
						<!-- Remove the message after it is displayed -->
					</c:if>

					<c:if test="${not empty failedMsg}">
						<p class="text-center text-danger">${failedMsg}</p>
						<c:remove var="failedMsg" scope="session" />
						<!-- Remove the message after it is displayed -->
					</c:if>

					<form action="${pageContext.request.contextPath}/AddBooksServlet"
						method="post" enctype="multipart/form-data">
						
						<div class="form-group">
							<label>Book Type:</label>
							<div class="form-check">
								<input class="form-check-input" type="radio" name="bookType" id="newBook" 
									value="new" checked onchange="toggleBookForm()">
								<label class="form-check-label" for="newBook">
									New Book
								</label>
							</div>
							<div class="form-check">
								<input class="form-check-input" type="radio" name="bookType" id="oldBook" 
									value="old" onchange="toggleBookForm()">
								<label class="form-check-label" for="oldBook">
									Refill Existing Book
								</label>
							</div>
						</div>
						
						<!-- Book selection dropdown for refilling -->
						<div id="bookSelectField" class="form-group" style="display: none;">
							<label for="bookSelect">Select Book to Refill</label>
							<select name="bookSelect" class="form-control" id="bookSelect" onchange="fillBookDetails()">
								<option value="">-- Select a book --</option>
								<c:forEach var="book" items="${bookList}">
									<option value="${book.bookId}" 
										data-title="${book.title}" 
										data-author="${book.author}" 
										data-price="${book.price}"
										data-current-stock="${book.quantity}">
										${book.title} by ${book.author} (Current Stock: ${book.quantity})
									</option>
								</c:forEach>
							</select>
						</div>
						
						<!-- Book ID field (only shown for refilling) -->
						<div id="bookIdField" class="form-group" style="display: none;">
							<label for="bookId">Book ID</label>
							<input name="bookId" type="text" class="form-control" id="bookId" readonly>
						</div>

						<div class="form-group">
							<label for="bookName">Book Name</label> <input
								name="bname" type="text" class="form-control"
								id="bookName" required>
						</div>

						<div class="form-group">
							<label for="authorName">Author Name</label> <input
								name="author" type="text" class="form-control"
								id="authorName" required>
						</div>
						
						<div class="form-group">
							<label for="price">Price</label> <input
								name="price" type="number" step="0.01" class="form-control"
								id="price" required>
						</div>
						
						<div class="form-group">
							<label for="quantity" id="quantityLabel">Quantity</label> <input
								name="quantity" type="number" class="form-control"
								id="quantity" min="1" value="1" required>
							<small id="currentStockInfo" class="form-text text-muted" style="display: none;">
								Current stock: <span id="currentStockValue">0</span>
							</small>
						</div>
						
						<div id="descriptionField" class="form-group">
							<label for="description">Description</label> <input
								name="bdes" type="text" class="form-control"
								id="description">
						</div>

						<div id="imageField" class="form-group">
							<label for="bookImage">Book Image</label> 
							<input name="bimg" type="file" class="form-control-file" 
							   id="bookImage">
						</div>
		
						<div class="form-check mb-4">
						    <input class="form-check-input" type="checkbox" id="notifyPreorders" name="notifyPreorders">
						    <label class="form-check-label" for="notifyPreorders">
						        Notify users who preordered this book
						    </label>
						</div>

						<button type="submit" class="btn btn-primary">Submit</button>
					</form>

				</div>
			</div>
		</div>
	</div>

	<script>
		function toggleBookForm() {
			const isNewBook = document.getElementById('newBook').checked;
			const isOldBook = document.getElementById('oldBook').checked;
			
			// Toggle visibility based on selection
			document.getElementById('bookSelectField').style.display = isOldBook ? 'block' : 'none';
			document.getElementById('bookIdField').style.display = isOldBook ? 'block' : 'none';
			document.getElementById('descriptionField').style.display = isNewBook ? 'block' : 'none';
			document.getElementById('imageField').style.display = isNewBook ? 'block' : 'none';
			document.getElementById('currentStockInfo').style.display = isOldBook ? 'block' : 'none';
			
			// Update required attributes
			document.getElementById('bookImage').required = isNewBook;
			document.getElementById('description').required = isNewBook;
			
			// Update labels and input states
			if (isOldBook) {
				document.getElementById('quantityLabel').textContent = 'Additional Quantity to Add';
				document.getElementById('bookName').readOnly = true;
				document.getElementById('authorName').readOnly = true;
				document.getElementById('price').readOnly = true;
			} else {
				document.getElementById('quantityLabel').textContent = 'Quantity';
				document.getElementById('bookName').readOnly = false;
				document.getElementById('authorName').readOnly = false;
				document.getElementById('price').readOnly = false;
				
				// Clear fields if switching back to new book
				document.getElementById('bookId').value = '';
				document.getElementById('currentStockValue').textContent = '0';
			}
			
			// Update button text
			const submitBtn = document.querySelector('button[type="submit"]');
			submitBtn.textContent = isNewBook ? 'Add Book' : 'Update Stock';
		}
		
		function fillBookDetails() {
			const select = document.getElementById('bookSelect');
			const selectedOption = select.options[select.selectedIndex];
			
			if (select.value) {
				// Set values from data attributes
				document.getElementById('bookId').value = select.value;
				document.getElementById('bookName').value = selectedOption.getAttribute('data-title');
				document.getElementById('authorName').value = selectedOption.getAttribute('data-author');
				document.getElementById('price').value = selectedOption.getAttribute('data-price');
				document.getElementById('currentStockValue').textContent = selectedOption.getAttribute('data-current-stock');
			} else {
				// Clear fields if no book selected
				document.getElementById('bookId').value = '';
				document.getElementById('bookName').value = '';
				document.getElementById('authorName').value = '';
				document.getElementById('price').value = '';
				document.getElementById('currentStockValue').textContent = '0';
			}
		}
		
		// Initialize form state on page load
		document.addEventListener('DOMContentLoaded', function() {
			toggleBookForm();
		});
	</script>

</body>
</html>