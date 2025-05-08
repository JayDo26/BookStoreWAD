<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ attribute name="books" type="java.util.List" required="true" %>
<%@ attribute name="title" type="java.lang.String" required="true" %>
<%@ attribute name="subtitle" type="java.lang.String" required="false" %>

<div class="sale-books-component">
    <div class="section-header align-center">
        <div class="title">
            <span>${subtitle}</span>
        </div>
        <h2 class="section-title">${title}</h2>
    </div>
    
    <c:if test="${empty books}">
        <div class="alert alert-info">No special offers available at this time.</div>
    </c:if>
    
    <c:if test="${not empty books}">
        <div class="offer-banner mb-4">
            Limited Time Deals - Save up to 20% on selected books!
        </div>
        
        <div class="sale-books-carousel">
            <div class="row">
                <c:forEach var="book" items="${books}">
                    <div class="col-md-3">
                        <div class="product-item">
                            <figure class="product-style">
                                <img src="${book.bookImage}" alt="${book.title}" class="product-item">
                                <span class="sale-badge">ON SALE</span>
                                <button type="button" class="add-to-cart" 
                                    data-book-title="${book.title}"  
                                    data-book-price="${book.sale_price > 0 ? book.sale_price : book.price - (book.price * book.discount_perc/100)}"
                                    data-book-id="${book.bookId}">Add to Cart</button>
                            </figure>
                            <figcaption>
                                <h3>${book.title}</h3>
                                <span>${book.author}</span>
                                <div class="price-display">
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
                                </div>
                                <div class="discount-badge">
                                    <c:choose>
                                        <c:when test="${book.discount_perc > 0}">
                                            ${book.discount_perc}% OFF
                                        </c:when>
                                        <c:otherwise>
                                            20% OFF
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </figcaption>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:if>
</div>
