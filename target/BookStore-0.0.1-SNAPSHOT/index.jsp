<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
	<title>Pisces Bookstore</title>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="format-detection" content="telephone=no">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="author" content="">
	<meta name="keywords" content="">
	<meta name="description" content="">

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9"
	crossorigin="anonymous">

<link rel="stylesheet" type="text/css" href="css/normalize.css">
<link rel="stylesheet" type="text/css" href="icomoon/icomoon.css">
<link rel="stylesheet" type="text/css" href="css/vendor.css">
<link rel="stylesheet" type="text/css" href="css/style.css">
<style>
    html, body {
        height: 100%;
        margin: 0;
        padding: 0;
        overflow-x: hidden;
    }
    
    body {
        background-image: url('images/bg-wave.png');
        background-repeat: repeat;
        background-attachment: fixed;
    }
    
    /* Base styles for content containers */
    #header-wrap, #header, #footer, #footer-bottom, .section-header, 
    .banner-content, .slider-item {
        background-color: rgba(255, 255, 255, 0.9);
    }
    
    /* Group elements with borders */
    .container, .container-fluid {
        padding: 20px;
        margin-bottom: 20px;
    }
    
    /* Style for sections */
    #billboard, #featured-books, #best-selling, #popular-books, #footer {
        border: 2px solid #d4e6f1;
        border-radius: 10px;
        box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
        padding: 15px;
        margin: 25px 0;
        background-color: rgba(255, 255, 255, 0.9);
    }
    
    /* Book item container style */
    .product-item {
        border: 1px solid #a9cce3;
        border-radius: 8px;
        padding: 15px;
        margin-bottom: 20px;
        transition: all 0.3s ease;
        background-color: rgba(255, 255, 255, 0.95);
    }
    
    .product-item:hover {
        transform: translateY(-5px);
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    }
    
    /* Header styles */
    #header-wrap {
        border-bottom: 3px solid #7fb3d5;
    }
    
    /* Footer styles */
    #footer, #footer-bottom {
        border-top: 2px solid #7fb3d5;
    }
    
    /* Section headers */
    .section-header {
        border-bottom: 2px solid #7fb3d5;
        margin-bottom: 25px;
        padding-bottom: 10px;
    }
    
    /* Fix for billboard slider */
    #billboard {
        position: relative;
        overflow: hidden;
        padding: 30px 0;
    }
    
    #billboard .slick-arrow {
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        z-index: 100;
        width: 40px;
        height: 40px;
        background-color: rgba(255, 255, 255, 0.7);
        border: 1px solid #7fb3d5;
        border-radius: 50%;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    
    #billboard .prev {
        left: 10px;
    }
    
    #billboard .next {
        right: 10px;
    }
    
    .main-slider {
        width: 100%;
        margin: 0 auto;
        position: relative;
    }
    
    .main-slider .slider-item {
        display: flex !important;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        padding: 20px;
        min-height: 400px;
    }
    
    .main-slider .banner-content {
        flex: 1;
        padding-right: 30px;
        max-width: 60%;
    }
    
    .main-slider .banner-image {
        max-width: 35%;
        height: 300px;
        object-fit: contain;
        margin-left: auto;
    }
    
    /* Fix the slick slider to show one item at a time */
    /* Hide all slides by default */
    .main-slider .slick-slide {
        visibility: hidden;
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        transform: translateX(100%);
        transition: transform 0.5s ease-in-out;
        opacity: 0;
    }
    
    /* Show only active slide */
    .main-slider .slick-current,
    .main-slider .slick-active {
        visibility: visible;
        position: relative;
        transform: translateX(0);
        opacity: 1;
    }
    
    /* Add custom JS to initialize slider properly */
    .slick-initialized .slick-slide {
        display: flex;
    }
    
    /* Give the slider container proper dimensions */
    .main-slider.slick-initialized {
        overflow: hidden;
        height: auto;
    }

    /* Sale tag styling */
    .sale-badge {
        position: absolute;
        top: 10px;
        right: 10px;
        background-color: #ff5722;
        color: white;
        padding: 5px 10px;
        border-radius: 4px;
        font-weight: bold;
        z-index: 10;
    }
    
    /* Price styling */
    .price-display {
        display: flex;
        align-items: center;
        gap: 8px;
    }
    
    .original-price {
        text-decoration: line-through;
        color: #999;
        font-size: 0.9em;
    }
    
    .sale-price {
        color: #ff5722;
        font-weight: bold;
    }
    
    /* Header elements to ensure proper alignment */
    .right-element {
        display: flex;
        align-items: center;
        justify-content: flex-end;
        z-index: 100; /* Higher z-index to keep cart above other elements */
    }
    
    .right-element > a {
        margin-right: 15px;
    }
    
    /* Sale banner styling */
    .offer-banner {
        background-color: #ff5722;
        color: white;
        text-align: center;
        padding: 10px 0;
        position: relative;
        border-radius: 8px;
        margin-bottom: 20px;
        font-size: 1.2em;
        font-weight: bold;
    }
    
    /* Discount badge styling */
    .discount-badge {
        background-color: #ff5722;
        color: white;
        display: inline-block;
        padding: 3px 8px;
        border-radius: 4px;
        font-size: 0.8em;
        font-weight: bold;
        margin-top: 8px;
    }
    
    /* Sale section styling */
    #special-offer {
        background-color: rgba(255, 245, 240, 0.7);
        border: 2px solid #ffccbc;
    }
    
    #special-offer .section-title {
        color: #d84315;
    }
    
    /* Enhanced product item styling for sale items */
    #special-offer .product-item {
        border: 2px solid #ffccbc;
        box-shadow: 0 4px 8px rgba(255, 87, 34, 0.1);
        transition: all 0.3s ease;
    }
    
    #special-offer .product-item:hover {
        transform: translateY(-8px);
        box-shadow: 0 8px 16px rgba(255, 87, 34, 0.2);
    }
</style>
</head>
<body data-bs-spy="scroll" data-bs-target="#header" tabindex="0">
    
	<div id="header-wrap">
	    <!-- Add sale notification banner at the top -->
		<div class="top-content">
			<div class="container-fluid">
				<div class="row">
					<div class="col-md-6">
					</div>
					<div class="col-md-6">
					    <div class="right-element">
					        <!-- Check if the user is logged in -->
							<c:choose>
							    <c:when test="${sessionScope.userinfo != null}">
							        <!-- User is logged in -->
							        <a href="userProfile.jsp" class="user-account for-buy">
							            <i class="icon icon-user"></i>
							            <span>Account</span>
							        </a>
							    </c:when>
							    <c:otherwise>
							        <!-- User is not logged in -->
							        <a href="login.jsp" class="user-account for-buy">
							            <i class="icon icon-user"></i>
							            <span>Account</span>
							        </a>
							    </c:otherwise>
							</c:choose>
							<c:choose>
                <c:when test="${not empty sessionScope.userinfo}">
                    <!-- User is logged in, show cart -->
                    <a href="ViewCartServlet" class="cart for-buy">
                        <i class="icon icon-clipboard"></i>
                        <span id="cartInfo">
                            Cart: 
                            <c:if test="${not empty sessionScope.cartTotalItems}">
                                (${sessionScope.cartTotalItems} items - 
                                <fmt:formatNumber value="${sessionScope.cartTotalPrice}" type="currency" />
                                )
                            </c:if>
                            <c:if test="${empty sessionScope.cartTotalItems}">
                                (0 items - $0.00)
                            </c:if>
                        </span>
                    </a>
                </c:when>
                <c:otherwise>
                    <!-- User is not logged in -->
                    <a href="login.jsp" class="cart for-buy">
                        <i class="icon icon-clipboard"></i>
                        <span>Please log in to view your cart.</span>
                    </a>
                </c:otherwise>
            </c:choose>
					    </div>
					</div>
				</div><!--top-right-->
			</div>
		</div>
	</div><!--header-wrap-->
	<header id="header">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-2">
				    <div class="main-logo">
				        <a href=" "><img src="images/main-logo-pisces.png"
				            alt="logo"></a>
				    </div>
				</div>
				<div class="col-md-8 d-flex align-items-center">
				    <nav id="navbar" class="w-100">
				        <div class="main-menu stellarnav d-flex justify-content-center align-items-center">
				            <ul class="menu-list mx-auto my-0">
				                <li class="menu-item active"><a href="#home">Home</a></li>
				                <li class="menu-item"><a href="#featured-books" class="nav-link">Featured</a></li>
				                <li class="menu-item"><a href="#popular-books" class="nav-link">Popular</a></li>
				                <li class="menu-item"><a href="#special-offer" class="nav-link">Offer</a></li>
				            </ul>
				        </div>
				    </nav>
				</div>
				<div class="col-md-2">
				</div>
			</div>
		</div>
	</header>
	<section id="billboard">
    <div class="container">
        <div class="row">
            <div class="col-md-12 position-relative">
                <button class="prev slick-arrow">
                    <i class="icon icon-arrow-left"></i>
                </button>
                <div class="main-slider pattern-overlay">
                    <c:forEach var="book" items="${randomBooks}">
                        <div class="slider-item">
                            <div class="banner-content">
                                <h2 class="banner-title">${book.title}</h2>
                                <p>${book.description}</p>
                                <div class="btn-wrap">
                                    <a href="#popular-books" class="btn btn-outline-accent btn-accent-arrow">Read
                                        More <i class="icon icon-ns-arrow-right"></i>
                                    </a>
                                </div>
                            </div>
                            <img src="${pageContext.request.contextPath}/${book.bookImage}"
                                alt="Book Cover" class="banner-image">
                        </div>
                    </c:forEach>
                </div>
                <button class="next slick-arrow">
                    <i class="icon icon-arrow-right"></i>
                </button>
            </div>
        </div>
    </div>
</section>

<section id="best-selling" class="leaf-pattern-overlay">
    <div class="corner-pattern-overlay"></div>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="row">
                    <c:forEach var="book" items="${bestSellingBook}">
                        <div class="col-md-6">
                            <figure class="products-thumb">
                                <img src="${book.bookImage}" alt="${book.title}"
                                    class="single-image">
                            </figure>
                        </div>
                        <div class="col-md-6">
                            <div class="product-entry">
                                <h2 class="section-title divider">Best Selling Book</h2>
                                <div class="products-content">
                                    <div class="author-name">By ${book.author}</div>
                                    <h3 class="item-title">${book.title}</h3>
                                    <p>${book.description}</p>
                                    <div class="price-display">
                                        <c:choose>
                                            <c:when test="${book.sale}">
                                                <div class="original-price">$<fmt:formatNumber value="${book.price}" pattern="0.00"/></div>
                                                <div class="sale-price">
                                                    <c:choose>
                                                        <c:when test="${book.sale_price > 0}">
                                                            $<fmt:formatNumber value="${book.sale_price}" pattern="0.00"/>
                                                        </c:when>
                                                        <c:when test="${book.discount_perc > 0}">
                                                            $<fmt:formatNumber value="${book.price - (book.price * book.discount_perc/100)}" pattern="0.00"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            $<fmt:formatNumber value="${book.price * 0.8}" pattern="0.00"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="item-price">$<fmt:formatNumber value="${book.price}" pattern="0.00"/></div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="btn-wrap">
                                        <button type="button" class="add-to-cart"
                                            data-book-title="${book.title}"
                                            data-book-price="${book.sale ? (book.sale_price > 0 ? book.sale_price : book.price - (book.price * book.discount_perc/100)) : book.price}"
                                            data-book-id="${book.bookId}">Add to Cart</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div><!-- / row -->
            </div>
        </div>
    </div>
</section>
	<section id="popular-books" class="bookshelf py-5 my-5">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="section-header align-center">
                    <div class="title">
                        <span>All quality items</span>
                    </div>
                    <h2 class="section-title">All Books</h2>
                </div>
                <div class="row">
                    <c:forEach var="book" items="${allBooks}">
                        <div class="col-md-3">
                            <div class="product-item">
                                <figure class="product-style" style="position: relative;">
                                    <img src="${book.bookImage}" alt="Books" class="product-item">
                                    <c:if test="${book.sale && ((book.sale_price > 0 && book.sale_price < book.price) || book.discount_perc > 0)}">
                                        <div class="sale-badge">ON SALE</div>
                                    </c:if>
                                    <button type="button" class="add-to-cart" 
                                        data-book-title="${book.title}"  
                                        data-book-price="${book.sale && ((book.sale_price > 0 && book.sale_price < book.price) || book.discount_perc > 0) ? (book.sale_price > 0 ? book.sale_price : book.price - (book.price * book.discount_perc/100)) : book.price}"
                                        data-book-id="${book.bookId}">Add to Cart</button>
                                </figure>
                                <figcaption>
                                    <h3>${book.title}</h3>
                                    <span>${book.author}</span>
                                    <div class="price-display">
                                        <c:choose>
                                            <c:when test="${book.sale && ((book.sale_price > 0 && book.sale_price < book.price) || book.discount_perc > 0)}">
                                                <div class="original-price">$<fmt:formatNumber value="${book.price}" pattern="0.00"/></div>
                                                <div class="sale-price">
                                                    $<fmt:formatNumber value="${book.sale_price > 0 ? book.sale_price : book.price - (book.price * book.discount_perc/100)}" pattern="0.00"/>
                                                </div>
                                                <c:choose>
                                                    <c:when test="${book.sale_price > 0 && book.sale_price < book.price}">
                                                        <div class="discount-badge">
                                                            <fmt:formatNumber value="${100 - (book.sale_price / book.price * 100)}" maxFractionDigits="0"/>% OFF
                                                        </div>
                                                    </c:when>
                                                    <c:when test="${book.discount_perc > 0}">
                                                        <div class="discount-badge">${book.discount_perc}% OFF</div>
                                                    </c:when>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="item-price">$<fmt:formatNumber value="${book.price}" pattern="0.00"/></div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </figcaption>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</section>
    <!-- Replace the current sale section with our new component -->
    <section id="special-offer" class="bookshelf py-5 my-5">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <tags:saleBooks books="${saleBooks}" title="Special Offers" subtitle="Limited Time Deals" />
                </div>
            </div>
        </div>
    </section>
    
    <!-- Book Detail Modal -->
    <div class="modal fade" id="bookDetailModal" tabindex="-1" aria-labelledby="bookDetailModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="bookDetailModalLabel">Book Details</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-4 text-center">
                            <img id="modalBookImage" src="" alt="Book Cover" class="img-fluid mb-3">
                            <span id="modalSaleTag" style="display:none;" class="badge bg-danger">On Sale</span>
                            <div class="mt-3">
                                <!-- Add to Cart button - shown for in-stock items -->
                                <button type="button" id="modalAddToCart" class="btn btn-primary">
                                    Add to Cart
                                </button>
                                <!-- Preorder button - shown for out-of-stock items -->
                                <button type="button" id="modalPreorder" class="btn btn-warning" style="display:none;">
                                    Preorder
                                </button>
                            </div>
                        </div>
                        <div class="col-md-8">
                            <h3 id="modalBookTitle"></h3>
                            <p><strong>Author: </strong><span id="modalBookAuthor"></span></p>
                            <div class="price-container">
                                <p class="mb-0">
                                    <strong>Price: </strong>
                                    <!-- Original price for sale items -->
                                    <span id="modalOriginalPrice" style="display:none;">
                                        <del>$<span id="modalOriginalPriceValue"></span></del>
                                    </span>
                                    <!-- Current price -->
                                    $<span id="modalBookPrice"></span>
                                </p>
                            </div>
                            <p><strong>Stock: </strong><span id="modalBookStock"></span></p>
                            <hr>
                            <h5>Description</h5>
                            <p id="modalBookDescription"></p>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
	<footer id="footer">
		<div class="container">
			<div class="row">
				<div class="col-md-4">
					<div class="footer-item">
						<div class="company-brand">
							<img src="images/main-logo-pisces.png" alt="logo" class="footer-logo">
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="footer-menu">
						<p>Whenever needing some new books or pencils, you will think about a book shop. With students having thin pockets, book shops are the place they can read new and interesting books without paying a cost.</p>
					</div>
				</div>
				<div class="col-md-2">
					<div class="footer-menu">
						<h5>Discover</h5>
						<ul class="menu-list">
							<li class="menu-item"><a href="#">Home</a></li>
							<li class="menu-item"><a href="#">Featured</a></li>
							<li class="menu-item"><a href="#">All Books</a></li>
							<li class="menu-item"><a href="#">Sale</a></li>
						</ul>
					</div>
				</div>
			</div><!-- / row -->
		</div>
	</footer>
	<div id="footer-bottom">
		<div class="container">
			<div class="row">
				<div class="col-md-12">
					<div class="copyright">
						<div class="row">
							<div class="col-md-6">
								<p>
									© 2022 All rights reserved. Free HTML Template by <a
										href="https://www.templatesjungle.com/" target="_blank">TemplatesJungle</a>
								</p>
							</div>
							<div class="col-md-6">
								<div class="social-links align-right">
									<ul>
										<li><a href="#"><i class="icon icon-facebook"></i></a></li>
										<li><a href="#"><i class="icon icon-twitter"></i></a></li>
										<li><a href="#"><i class="icon icon-youtube-play"></i></a></li>
										<li><a href="#"><i class="icon icon-behance-square"></i></a></li>
									</ul>
								</div>
							</div>
						</div><!--footer-bottom-content-->
					</div>
				</div>
			</div><!--grid-->
		</div>
	</div>
	<script src="js/jquery-1.11.0.min.js"></script>
	<script 
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js" 
		integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm" 
		crossorigin="anonymous"></script>
	<script src="js/plugins.js"></script>
	<script src="js/script.js"></script>
	<script src="js/addToCart.js"></script>
	<script>
	    $(document).ready(function(){
	        $('.main-slider').slick({
	            arrows: true,
	            slidesToShow: 1,
	            slidesToScroll: 1,
	            prevArrow: $('#billboard .prev'),
	            nextArrow: $('#billboard .next'),
	            infinite: true,
	            speed: 500,
	            fade: true,
	            cssEase: 'linear',
	            adaptiveHeight: true
	        });
	        // Add smooth scrolling to all links
	        $("a[href^='#']").on('click', function(event) {
	            if (this.hash !== "") {
	                event.preventDefault();
	                var hash = this.hash;
	                $('html, body').animate({
	                    scrollTop: $(hash).offset().top
	                }, 800, function(){
	                    window.location.hash = hash;
	                });
	            }
	        });
	        // Book Detail Modal
	        $('.product-item').on('click', function(e) {
	            // Don't trigger if add-to-cart button is clicked
	            if($(e.target).hasClass('add-to-cart')) {
	                return;
	            }
	            const $figure = $(this).find('figure.product-style');
	            const $figcaption = $(this).find('figcaption');
	            // Get data from data attributes
	            const bookId = $figure.find('.add-to-cart').data('book-id');
	            const bookTitle = $figure.find('.add-to-cart').data('book-title');
	            const bookPrice = $figure.find('.add-to-cart').data('book-price');
	            const bookAuthor = $figcaption.find('span').text();
	            const bookImage = $figure.find('img').attr('src');
	            // Set values in modal
	            $('#modalBookTitle').text(bookTitle);
	            $('#modalBookPrice').text(bookPrice);
	            $('#modalBookAuthor').text(bookAuthor);
	            $('#modalBookImage').attr('src', bookImage);
	            $('#modalAddToCart').data('book-id', bookId);
	            $('#modalAddToCart').data('book-title', bookTitle);
	            $('#modalAddToCart').data('book-price', bookPrice);
	            // Hide sale tag and original price by default
	            $('#modalSaleTag').hide();
	            $('#modalOriginalPrice').hide();
	            // Default values until AJAX returns
	            $('#modalBookDescription').text("Loading...");
	            $('#modalBookStock').text("Loading...");
	            // Show the modal
	            $('#bookDetailModal').modal('show');
	            // Fetch additional book details via AJAX
	            $.ajax({
	                url: 'GetBookDetailsServlet',
	                type: 'GET',
	                data: { bookId: bookId },
	                dataType: 'json',
	                success: function(book) {
	                    // Update modal with additional details
	                    $('#modalBookDescription').text(book.description || "No description available.");
	                    // Handle stock availability
	                    if (book.stock > 0) {
	                        $('#modalBookStock').text(book.stock + " available");
	                        $('#modalAddToCart').show();
	                        $('#modalPreorder').hide();
	                    } else {
	                        $('#modalBookStock').text("Out of stock");
	                        $('#modalAddToCart').hide();
	                        $('#modalPreorder').show();
	                    }
	                    // Handle sale items
	                    if(book.sale) {
	                        $('#modalSaleTag').show();
	                        // Show original price with strikethrough
	                        const originalPrice = parseFloat(book.price);
	                        let discountedPrice;
	                        // Use sale_price if available, otherwise calculate from discount percentages
	                        if (book.sale_price > 0) {
	                            discountedPrice = parseFloat(book.sale_price);
	                        } else if (book.discount_perc > 0) {
	                            discountedPrice = originalPrice - (originalPrice * book.discount_perc/100);
	                        } else {
	                            discountedPrice = originalPrice * 0.8; // fallback to 20% discount
	                        }
	                        $('#modalOriginalPriceValue').text(originalPrice.toFixed(2));
	                        $('#modalOriginalPrice').show();
	                        $('#modalBookPrice').text(discountedPrice.toFixed(2));
	                        // Update add to cart button with new price
	                        $('#modalAddToCart').data('book-price', discountedPrice.toFixed(2));
	                    } else {
	                        // Regular price
	                        $('#modalSaleTag').hide();
	                        $('#modalOriginalPrice').hide();
	                    }
	                },
	                error: function() {
	                    // Fallback if AJAX fails
	                    $('#modalBookDescription').text("Description not available");
	                    $('#modalBookStock').text("Stock information not available");
	                    $('#modalAddToCart').show();
	                    $('#modalPreorder').hide();
	                }
	            });
	        });
	        // Also make the best selling book clickable to show details
	        $('#best-selling .products-thumb, #best-selling .product-entry').click(function() {
	            const $addToCartBtn = $('#best-selling .add-to-cart');
	            const bookId = $addToCartBtn.data('book-id');
	            const bookTitle = $addToCartBtn.data('book-title');
	            const bookPrice = $addToCartBtn.data('book-price');
	            const bookAuthor = $('#best-selling .author-name').text().replace('By', '').trim();
	            const bookImage = $('#best-selling .products-thumb img').attr('src');
	            // Set modal content
	            $('#modalBookTitle').text(bookTitle);
	            $('#modalBookAuthor').text(bookAuthor);
	            $('#modalBookPrice').text(bookPrice);
	            $('#modalBookImage').attr('src', bookImage);
	            $('#modalAddToCart').data('book-id', bookId);
	            $('#modalAddToCart').data('book-title', bookTitle);
	            $('#modalAddToCart').data('book-price', bookPrice);
	            // Hide sale tag and original price by default
	            $('#modalSaleTag').hide();
	            $('#modalOriginalPrice').hide();
	            // Default values until AJAX returns
	            $('#modalBookDescription').text("Loading...");
	            $('#modalBookStock').text("Loading...");
	            // Show the modal
	            $('#bookDetailModal').modal('show');
	            // Fetch additional book details via AJAX
	            if (bookId) {
	                $.ajax({
	                    url: 'GetBookDetailsServlet',
	                    type: 'GET',
	                    data: { bookId: bookId },
	                    dataType: 'json',
	                    success: function(book) {
	                        $('#modalBookDescription').text(book.description || "No description available.");
	                        // Handle stock availability
	                        if (book.stock > 0) {
	                            $('#modalBookStock').text(book.stock + " available");
	                            $('#modalAddToCart').show();
	                            $('#modalPreorder').hide();
	                        } else {
	                            $('#modalBookStock').text("Out of stock");
	                            $('#modalAddToCart').hide();
	                            $('#modalPreorder').show();
	                        }
	                        // Handle sale items
	                        if(book.sale) {
	                            $('#modalSaleTag').show();
	                            // Show original price with strikethrough
	                            const originalPrice = parseFloat(book.price);
	                            const discountedPrice = (originalPrice * 0.8).toFixed(2);
	                            $('#modalOriginalPriceValue').text(originalPrice.toFixed(2));
	                            $('#modalOriginalPrice').show();
	                            $('#modalBookPrice').text(discountedPrice);
	                            // Update add to cart button with new price
	                            $('#modalAddToCart').data('book-price', discountedPrice);
	                        } else {
	                            // Regular price
	                            $('#modalSaleTag').hide();
	                            $('#modalOriginalPrice').hide();
	                        }
	                    },
	                    error: function() {
	                        const description = $('#best-selling .products-content p').text();
	                        $('#modalBookDescription').text(description || "Description not available");
	                        $('#modalBookStock').text("Stock information not available");
	                        $('#modalAddToCart').show();
	                        $('#modalPreorder').hide();
	                    }
	                });
	            } else {
	                const description = $('#best-selling .products-content p').text();
	                $('#modalBookDescription').text(description || "Description not available");
	                $('#modalBookStock').text("Stock information not available");
	            }
	        });
	        // Handle modal add to cart button
	        $('#modalAddToCart').click(function() {
	            const bookId = $(this).data('book-id');
	            const bookTitle = $(this).data('book-title');
	            const bookPrice = $(this).data('book-price');
	            
	            // Use the same add to cart functionality
	            if (typeof addToCart === 'function') {
	                addToCart(bookId, bookTitle, bookPrice);
	                $('#bookDetailModal').modal('hide');
	            }
	        });
	        
	        // Handle preorder button
	        $('#modalPreorder').click(function() {
	            const bookId = $('#modalAddToCart').data('book-id');
	            const bookTitle = $('#modalAddToCart').data('book-title');
	            
	            // Gửi yêu cầu preorder đến server
	            $.ajax({
                url: 'PreorderServlet',
                type: 'POST',
                data: { bookId: bookId },
                success: function(response) {
                    if (response.status === 'success') {
                        alert('Cảm ơn bạn đã đăng ký nhận thông báo khi sách "' + bookTitle + '" có hàng! Chúng tôi sẽ gửi email thông báo cho bạn ngay khi sách được bổ sung.');
                    } else if (response.status === 'login_required') {
                        alert('Vui lòng đăng nhập để đăng ký nhận thông báo khi sách có hàng.');
                        window.location.href = 'login.jsp';
                    } else if (response.status === 'already_registered') {
                        alert('Bạn đã đăng ký nhận thông báo cho sách này rồi. Chúng tôi sẽ thông báo ngay khi sách có hàng.');
                    } else {
                        alert('Đã xảy ra lỗi. Vui lòng thử lại sau.');
                    }
                },
                error: function() {
                    alert('Đã xảy ra lỗi khi đăng ký nhận thông báo. Vui lòng thử lại sau.');
                }
            });
	            
	            $('#bookDetailModal').modal('hide');
	        });

            // Initialize the chatbot
            initChatbot();
	    });

        // Chatbot functionality
        function initChatbot() {
            const chatbotHtml = `
                <div id="chatbot-container">
                    <div id="chatbot-button">
                        <i class="icon icon-bubble"></i>
                    </div>
                    <div id="chatbot-window" class="hidden">
                        <div id="chatbot-header">
                            <div class="chatbot-title">Pisces Book Assistant</div>
                            <div id="chatbot-close"><i class="icon icon-cross"></i></div>
                        </div>
                        <div id="chatbot-messages">
                            <div class="bot-message">Hello! I'm your book assistant. How can I help you today?</div>
                        </div>
                        <div id="chatbot-input-container">
                            <input type="text" id="chatbot-input" placeholder="Type your message...">
                            <button id="chatbot-send">
                                <i class="icon icon-arrow-right"></i>
                            </button>
                        </div>
                        <div id="chatbot-typing" class="hidden">
                            <span class="dot"></span>
                            <span class="dot"></span>
                            <span class="dot"></span>
                        </div>
                    </div>
                </div>
            `;
            
            // Append chatbot HTML to body
            $('body').append(chatbotHtml);
            
            // Add chatbot styling
            const chatbotStyles = `
                <style>
                    #chatbot-container {
                        position: fixed;
                        bottom: 20px;
                        right: 20px;
                        z-index: 1000;
                        font-family: Arial, sans-serif;
                    }
                    
                    #chatbot-button {
                        width: 60px;
                        height: 60px;
                        border-radius: 50%;
                        background-color: #7fb3d5;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        cursor: pointer;
                        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
                        transition: all 0.3s ease;
                    }
                    
                    #chatbot-button:hover {
                        background-color: #5499c7;
                        transform: scale(1.05);
                    }
                    
                    #chatbot-button i {
                        color: white;
                        font-size: 24px;
                    }
                    
                    #chatbot-window {
                        position: absolute;
                        bottom: 80px;
                        right: 0;
                        width: 320px;
                        height: 400px;
                        background-color: white;
                        border-radius: 10px;
                        box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
                        display: flex;
                        flex-direction: column;
                        transition: all 0.3s ease;
                        overflow: hidden;
                    }
                    
                    #chatbot-window.hidden {
                        opacity: 0;
                        visibility: hidden;
                        transform: translateY(20px);
                    }
                    
                    #chatbot-header {
                        padding: 15px;
                        background-color: #7fb3d5;
                        color: white;
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        font-weight: bold;
                    }
                    
                    #chatbot-close {
                        cursor: pointer;
                    }
                    
                    #chatbot-messages {
                        flex: 1;
                        padding: 15px;
                        overflow-y: auto;
                        display: flex;
                        flex-direction: column;
                    }
                    
                    .user-message, .bot-message {
                        max-width: 80%;
                        padding: 10px 15px;
                        margin: 5px 0;
                        border-radius: 18px;
                        word-wrap: break-word;
                    }
                    
                    .user-message {
                        background-color: #7fb3d5;
                        color: white;
                        align-self: flex-end;
                        border-bottom-right-radius: 5px;
                    }
                    
                    .bot-message {
                        background-color: #f0f0f0;
                        color: #333;
                        align-self: flex-start;
                        border-bottom-left-radius: 5px;
                    }
                    
                    #chatbot-input-container {
                        display: flex;
                        padding: 10px;
                        background-color: #f8f9fa;
                        border-top: 1px solid #e9ecef;
                    }
                    
                    #chatbot-input {
                        flex: 1;
                        padding: 10px 15px;
                        border: 1px solid #ced4da;
                        border-radius: 20px;
                        outline: none;
                    }
                    
                    #chatbot-input:focus {
                        border-color: #7fb3d5;
                    }
                    
                    #chatbot-send {
                        width: 40px;
                        height: 40px;
                        margin-left: 10px;
                        background-color: #7fb3d5;
                        color: white;
                        border: none;
                        border-radius: 50%;
                        cursor: pointer;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        transition: background-color 0.3s;
                    }
                    
                    #chatbot-send:hover {
                        background-color: #5499c7;
                    }
                    
                    #chatbot-typing {
                        padding: 10px 15px;
                        background-color: #f0f0f0;
                        border-radius: 18px;
                        margin: 5px 0;
                        align-self: flex-start;
                        display: flex;
                        align-items: center;
                        width: 60px;
                        position: absolute;
                        bottom: 60px;
                        left: 15px;
                    }
                    
                    #chatbot-typing.hidden {
                        display: none;
                    }
                    
                    .dot {
                        height: 8px;
                        width: 8px;
                        background-color: #999;
                        border-radius: 50%;
                        margin: 0 2px;
                        display: inline-block;
                        animation: typing 1.4s infinite ease-in-out both;
                    }
                    
                    .dot:nth-child(1) {
                        animation-delay: 0s;
                    }
                    
                    .dot:nth-child(2) {
                        animation-delay: 0.2s;
                    }
                    
                    .dot:nth-child(3) {
                        animation-delay: 0.4s;
                    }
                    
                    @keyframes typing {
                        0%, 80%, 100% {
                            transform: scale(0);
                        }
                        40% {
                            transform: scale(1);
                        }
                    }
                </style>
            `;
            
            // Add styles to head
            $('head').append(chatbotStyles);
            
            // Handle chat toggle
            $('#chatbot-button').on('click', function() {
                $('#chatbot-window').toggleClass('hidden');
                // If opening the chat, scroll to bottom and focus input
                if (!$('#chatbot-window').hasClass('hidden')) {
                    scrollToBottom();
                    $('#chatbot-input').focus();
                }
            });
            
            // Close chat window
            $('#chatbot-close').on('click', function() {
                $('#chatbot-window').addClass('hidden');
            });
            
            // Function to add message to chat
            function addMessage(message, isUser = false) {
                const messageClass = isUser ? 'user-message' : 'bot-message';
                const messageHtml = `<div class="${messageClass}">${message}</div>`;
                $('#chatbot-messages').append(messageHtml);
                scrollToBottom();
            }
            
            // Function to scroll chat to bottom
            function scrollToBottom() {
                const chatMessages = document.getElementById('chatbot-messages');
                chatMessages.scrollTop = chatMessages.scrollHeight;
            }
            
            // Show typing indicator
            function showTyping() {
                $('#chatbot-typing').removeClass('hidden');
                scrollToBottom();
            }
            
            // Hide typing indicator
            function hideTyping() {
                $('#chatbot-typing').addClass('hidden');
            }
            
            // Send message on button click
            $('#chatbot-send').on('click', sendMessage);
            
            // Send message on Enter key
            $('#chatbot-input').on('keypress', function(event) {
                if (event.which === 13) {  // Enter key
                    sendMessage();
                }
            });
            
            // Function to send message to chatbot API
            function sendMessage() {
                const userMessage = $('#chatbot-input').val().trim();
                
                // Don't send empty messages
                if (userMessage === '') return;
                
                // Add user message to chat
                addMessage(userMessage, true);
                
                // Clear input
                $('#chatbot-input').val('');
                
                // Show typing indicator
                showTyping();
                
                console.log("Sending message to API:", userMessage);
                
                // Send request directly to the external API - using HTTPS
                $.ajax({
                    url: 'https://logically-exact-phoenix.ngrok-free.app/chat',  // Use HTTPS instead of HTTP
                    type: 'POST',
                    contentType: 'application/json',
                    dataType: 'json',
                    crossDomain: true,  // Explicitly indicate cross-domain request
                    xhrFields: {
                        withCredentials: false  // Disable credentials for cross-domain requests
                    },
                    data: JSON.stringify({ query: userMessage }),
                    success: function(response) {
                        console.log("Received API response:", response);
                        // Hide typing indicator
                        hideTyping();
                        
                        // Add bot response to chat
                        if (response && response.reply) {
                            addMessage(response.reply);
                        } else {
                            addMessage("Sorry, I couldn't understand the response. Please try again.");
                        }
                    },
                    error: function(xhr, status, error) {
                        console.error("Error in API request:", status, error);
                        console.log("Response status:", xhr.status);
                        
                        // Hide typing indicator
                        hideTyping();
                        
                        // Show specific error message based on the error type
                        if (xhr.status === 0) {
                            addMessage("I can't connect to the server. This might be due to CORS restrictions or network issues. Please try again later.");
                        } else {
                            try {
                                const errorResponse = JSON.parse(xhr.responseText);
                                addMessage(errorResponse.reply || "Sorry, I encountered an error. Please try again.");
                            } catch (e) {
                                addMessage("I'm having trouble connecting to my knowledge base. Please try again later.");
                            }
                        }
                    }
                });
            }
        }
	</script>

</body>

</html>