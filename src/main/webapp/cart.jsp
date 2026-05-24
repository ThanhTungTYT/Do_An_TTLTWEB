<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ hàng | Aroma Café</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cart.css">
</head>
<body>

<header>
    <div class="top">
        <div class="logo">
            <img src="${pageContext.request.contextPath}/assets/img/logo.png"
                 onclick="location.href='${pageContext.request.contextPath}/'"
                 width="300px" height="100px" alt="Logo">
        </div>
        <form class="search-bar" method="get" action="${pageContext.request.contextPath}/search-product">
            <input type="text" name="search" id="search-input" placeholder="Tìm kiếm..." value="${keyword}">
            <button type="submit" id="search-button"><i class="fas fa-search"></i></button>
        </form>
        <div class="mini-menu">
            <div class="cart">
                <a href="${pageContext.request.contextPath}/cart"><i class="fas fa-shopping-cart"></i></a>
                <span id="num-cart-label">${sessionScope.cart.totalQuantity}</span>
            </div>
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <a href="${pageContext.request.contextPath}/account">
                        <i class="fas fa-user"></i>
                        <span style="font-size: 14px; margin-left: 5px">
                <c:set var="nameParts" value="${fn:split(sessionScope.user.full_name, ' ')}" />
                Hi, ${nameParts[fn:length(nameParts) - 1]}!
            </span>
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login">
                        <i class="fas fa-user"></i>
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="bottom">
        <a href="${pageContext.request.contextPath}/">Trang chủ</a>
        <a href="${pageContext.request.contextPath}/catalog">Sản phẩm</a>
        <a href="${pageContext.request.contextPath}/contact">Liên hệ</a>
        <a href="${pageContext.request.contextPath}/about">Giới thiệu</a>
    </div>
</header>

<div class="cart-container">
    <h2>Giỏ hàng của bạn</h2>

    <c:if test="${not empty sessionScope.error}">
        <div style="color: red; text-align: center; margin-bottom: 10px;">
            <i class="fas fa-exclamation-circle"></i> ${sessionScope.error}
        </div>
        <% session.removeAttribute("error"); %>
    </c:if>

    <c:if test="${empty sessionScope.cart or empty sessionScope.cart.list}">
        <div style="text-align: center; margin: 50px 0;">
            <p>Giỏ hàng của bạn đang trống.</p>
            <a href="${pageContext.request.contextPath}/catalog" style="color: #c76739; font-weight: bold;">Quay lại mua sắm</a>
        </div>
    </c:if>

    <c:if test="${not empty sessionScope.cart and not empty sessionScope.cart.list}">
        <form action="${pageContext.request.contextPath}/payment" method="post" id="cart-form">
            <input type="hidden" name="action" value="prepare">

            <div class="clear-all-container" style="display: flex; justify-content: space-between;">
                <a href="#" class="select-all-cart">Chọn tất cả</a>
                <a href="#" class="clear-all-cart" id="clear-all-btn"
                   data-href="${pageContext.request.contextPath}/remove-all">Xóa tất cả</a>
            </div>

            <div id="cart-list">
                <c:forEach items="${sessionScope.cart.list}" var="item">
                    <div class="cart-item">
                        <input type="checkbox"
                               class="product-select item-checkbox"
                               id="checkbox-${item.product.id}"
                               name="selectedIds"
                               value="${item.product.id}"
                               checked
                               data-subtotal="${item.price * item.quantity}">


                        <div class="product-thumbnail">
                            <c:choose>
                                <c:when test="${not empty item.product.image_url}">
                                    <img src="<c:url value='${item.product.image_url}'/>" alt="${item.product.name}"/>
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/assets/img/about05.png" alt="Default Image"/>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="product-details">
                            <p class="product-name">${item.product.name}</p>
                            <p class="product-type">Loại: ${item.product.category_name}</p>
                            <p class="product-weight">
                                Khối lượng: <span>${item.product.weight_grams} gr</span>
                            </p>
                        </div>

                        <div class="product-price">
                            <fmt:formatNumber value="${item.price}" type="number"/> VND
                        </div>

                        <div class="product-quantity">
                            <button type="button" class="btn-decrease" data-pid="${item.product.id}">-</button>

                            <input type="number" class="qty-input" data-pid="${item.product.id}" value="${item.quantity}" min="1"/>

                            <button type="button" class="btn-increase" data-pid="${item.product.id}">+</button>
                        </div>

                        <div class="product-subtotal" id="subtotal-${item.product.id}">
                            <fmt:formatNumber value="${item.price * item.quantity}" type="number" maxFractionDigits="0"/> VND
                        </div>
                        <a href="${pageContext.request.contextPath}/remove-item?pid=${item.product.id}" class="product-remove" title="Xóa sản phẩm" onclick="return">
                            <i class="fas fa-times"></i>
                        </a>
                    </div>
                </c:forEach>
            </div>

            <div class="cart-totals" style="display:none;">
                <h3>TỔNG CỘNG</h3>
                <span id="cart-total">0đ</span>
                <p>(Chỉ tính các sản phẩm được chọn)</p>
            </div>

        </form>
    </c:if>

    <div class="payment-methods">
        <h4>Phương thức thanh toán chấp nhận</h4>
        <i class="fa-solid fa-boxes-stacked"></i>
        <span>Thanh Toán Khi Nhận Hàng</span>
        <i class="fa-brands fa-cc-visa"></i>
        <span>Chuyển Khoản Ngân Hàng</span>
    </div>
</div>

<%-- STICKY CHECKOUT BAR --%>
<c:if test="${not empty sessionScope.cart and not empty sessionScope.cart.list}">
    <div class="sticky-checkout-bar" id="sticky-checkout-bar">
        <div class="sticky-bar-left">
            <label class="sticky-select-all-label">
                <input type="checkbox" id="sticky-select-all-cb">
                <span>Chọn tất cả</span>
            </label>
            <span class="sticky-bar-selected-count" id="sticky-selected-count">0 sản phẩm</span>
        </div>
        <div class="sticky-bar-total-wrap">
            <span class="sticky-bar-total-label">Tạm tính:</span>
            <span class="sticky-bar-total-amount" id="sticky-total">0 VND</span>
        </div>
        <button class="sticky-bar-btn" id="sticky-checkout-btn" disabled
                onclick="document.getElementById('cart-form').submit()">
            Thanh toán&nbsp;<span class="sticky-btn-count" id="sticky-count-btn">(0)</span>
        </button>
    </div>
</c:if>

<footer class="footer">
    <div class="footer-top">
        <div class="foot-content left">
            <h3>Aroma Café</h3>
            <p>Địa chỉ: Trường đại học Nông Lâm TPHCM.</p>
            <p>Điện thoại: 0933652267</p>
            <p>Email: nguyenhuybaolegit@gmail.com</p>
        </div>

        <div class="foot-content footer-links">
            <h3>Quy định & Chính sách</h3>
            <ul>
                <li><a href="${pageContext.request.contextPath}/policy?type=shipping">Chính sách vận chuyển</a></li>
                <li><a href="${pageContext.request.contextPath}/policy?type=warranty">Chính sách bảo hành, đổi trả</a></li>
                <li><a href="${pageContext.request.contextPath}/policy?type=terms">Điều khoản sử dụng</a></li>
            </ul>
        </div>

        <div class="foot-content right">
            <h3>Kết nối với chúng tôi</h3>
            <div class="social">
                <a href="https://www.facebook.com/atnguyen.75640/"><i class="fab fa-facebook-f"></i></a>
                <a href="https://www.youtube.com/@nguyenlechannel4855/featured"><i class="fa-brands fa-youtube"></i></a>
                <a href="https://www.instagram.com/nguyendat6159/"><i class="fab fa-instagram"></i></a>
            </div>
        </div>
    </div>
    <div class="footer-bottom">
        <p>&copy; 2024 Aroma Café. All rights reserved.</p>
    </div>
</footer>

<button class="slide-top" id="slide-top"><i class="fas fa-angle-up"></i></button>
<script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
