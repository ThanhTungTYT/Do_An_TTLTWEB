<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán | Aroma Café</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/payment.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/toast.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/toast.jsp"/>
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

<form action="payment" method="post" id="checkout-form">
    <input type="hidden" name="action" value="process">

    <div class="checkout-container">
        <div class="back-link">
            <a href="cart.jsp"><i class="fas fa-arrow-left"></i> Quay lại giỏ hàng</a>
        </div>

        <div class="checkout-left">
            <section class="account-box">
                <h3>Tài khoản</h3>
                <div class="account-info">
                    <div class="avatar-icon" id="account-btn">
                        <i class="fas fa-user"></i>
                    </div>
                    <div>
                        <p class="name">${sessionScope.user.full_name}</p>
                        <p class="email">${sessionScope.user.email}</p>
                    </div>
                </div>
            </section>

            <section class="shipping-box">
                <h3>Thông tin giao hàng</h3>
                <div class="shipping-form">
                    <input type="text" id="fullname" name="fullname" placeholder="Họ và tên"
                           value="${sessionScope.user.full_name}">
                    <input type="text" id="phone" name="phone" placeholder="Số điện thoại"
                           value="${sessionScope.user.phone}">
                    <input type="hidden" name="country" value="Việt Nam">
                    <input type="hidden" id="hidden_province" name="province" value="${requestScope.userAddress.province}">
                    <input type="hidden" id="hidden_district" name="district" value="${requestScope.userAddress.district}">
                    <input type="hidden" id="hidden_ward"     name="ward"     value="${requestScope.userAddress.ward}">
                    <input type="hidden" id="hidden_district_id" name="district_id" value="">
                    <input type="hidden" id="hidden_ward_code" name="ward_code" value="">
                    <input type="hidden" id="hidden_shipping_fee" name="shippingFee" value="30000">
                    <select id="provinceSelect"
                            onchange="loadDistricts(this.value, this.options[this.selectedIndex].text)">
                        <option value="">-- Chọn Tỉnh/Thành phố --</option>
                    </select>
                    <select id="districtSelect" disabled
                            onchange="loadWards(this.value, this.options[this.selectedIndex].text)">
                        <option value="">-- Chọn Quận/Huyện --</option>
                    </select>
                    <select id="wardSelect" disabled
                            onchange="onWardChange(this.value, this.options[this.selectedIndex].text)">
                        <option value="">-- Chọn Phường/Xã --</option>
                    </select>
                    <input type="text" id="address" name="address" placeholder="Số nhà, tên đường"
                           value="${requestScope.userAddress.address}">
                </div>
            </section>

            <section class="payment-method">
                <h3>Phương thức thanh toán</h3>

                <label class="radio-item payment-option">
                    <input type="radio" name="paymentMethod" value="COD" checked>
                    <span class="payment-label">
                        <i class="fas fa-truck payment-icon"></i> Thanh toán khi nhận hàng (COD)
                    </span>
                </label>

                <label class="radio-item payment-option">
                    <input type="radio" name="paymentMethod" value="bank">
                    <span class="payment-label">
                        <i class="fas fa-university payment-icon"></i> Chuyển khoản / QR Banking
                    </span>
                </label>

                    <div id="bank-info-panel" class="bank-info-panel" style="display:none;">
                        <div class="bank-info-header">
                            <i class="fas fa-info-circle"></i> Thông tin chuyển khoản
                        </div>
                        <div class="bank-info-body">
                            <div class="bank-info-row">
                                <span class="bank-info-label">Ngân hàng:</span>
                                <span class="bank-info-value">BIDV</span>
                            </div>
                            <div class="bank-info-row">
                                <span class="bank-info-label">Số tài khoản:</span>
                                <span class="bank-info-value" id="bank-account-number">
                                    8800273817
                                    <button type="button" class="copy-btn" onclick="copyText('8800273817', this)">
                                        <i class="fas fa-copy"></i>
                                    </button>
                                </span>
                            </div>
                            <div class="bank-info-row">
                                <span class="bank-info-label">Chủ tài khoản:</span>
                                <span class="bank-info-value">AROMA CAFE</span>
                            </div>
                            <div class="bank-info-row">
                                <span class="bank-info-label">Nội dung CK:</span>
                                <span class="bank-info-value" id="transfer-content">
                                    <span id="transfer-content-text">AROMACAFE</span>
                                    <button type="button" class="copy-btn" id="copy-content-btn"
                                            onclick="copyTransferContent()">
                                        <i class="fas fa-copy"></i>
                                    </button>
                                </span>
                            </div>
                            <div id="qr-section" class="qr-section">
                                <p class="qr-label">Quét mã QR để thanh toán:</p>
                                <img id="qr-image"
                                     src="https://img.vietqr.io/image/BIDV-8800273817-compact2.png?amount=0&addInfo=AROMACAFE+${sessionScope.user.id}&accountName=AROMA+CAFE"
                                     alt="QR Code thanh toán"
                                     class="qr-img">
                                <p class="qr-note"><i class="fas fa-exclamation-circle"></i> QR sẽ tự động cập nhật số tiền khi đặt hàng</p>
                            </div>
                        </div>
                    </div>
                </section>

            <section class="note-box">
                <h3>Ghi chú</h3>
                <textarea name="note" id="order-note" maxlength="300"
                          placeholder="Nhập ghi chú cho đơn hàng (nếu có) — tối đa 300 ký tự"></textarea>
                <small id="note-counter" style="display:block; text-align:right; color:#888; font-size:13px; margin-top:4px;">0 / 300 ký tự</small>
            </section>
        </div>

        <div class="checkout-right">
            <section class="cart-box">
                <h3>Sản phẩm thanh toán</h3>
                <div id="cart-list">
                    <c:forEach items="${requestScope.cart.list}" var="item">
                        <div class="cart-item">
                            <div class="item-top">
                                <div class="product-img">
                                    <c:choose>
                                        <c:when test="${not empty item.product.image_url}">
                                            <img src="<c:url value='${item.product.image_url}'/>" alt="${item.product.name}">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/assets/img/about04.png" alt="Default">
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <p class="product-name">${item.product.name}</p>
                            </div>
                            <div class="item-bottom">
                                <div class="quantity">
                                    Số lượng: <strong>${item.quantity}</strong>
                                </div>
                                <div class="price-total">
                                    <fmt:formatNumber value="${item.price * item.quantity}" type="number" maxFractionDigits="0" />
                                    <span class="currency">VND</span>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </section>

            <section class="discount-box">
                <h3>Mã khuyến mãi</h3>
                <select name="promotionId" id="promotionSelect">
                    <option value="" data-discount="0">-- Chọn mã --</option>
                    <c:forEach var="p" items="${promotions}">
                        <option value="${p.id}" data-discount="${p.discountPercent}">
                                ${p.code} - Giảm ${p.discountPercent}%
                        </option>
                    </c:forEach>
                </select>
            </section>

            <section class="summary-box">
                <h3>Tóm tắt đơn hàng</h3>
                <div class="summary-row">
                    <span>Tổng tiền hàng</span>
                    <span id="total-price" data-total="${requestScope.cart.total}">
                        <fmt:formatNumber value="${requestScope.cart.total}" type="number" maxFractionDigits="0"/> VND
                    </span>
                </div>
                <div class="summary-row">
                    <span>Phí vận chuyển</span>
                    <span id="shipping-fee" data-fee="30000">30.000 VND</span>
                </div>
                <div class="summary-row">
                    <span>Giảm giá</span>
                    <span id="discount-amount">0 VND</span>
                </div>
                <hr>
                <div class="summary-row total">
                    <span>Tổng thanh toán</span>
                    <span id="final-total">
                        <fmt:formatNumber value="${requestScope.cart.total + 30000}" type="number" maxFractionDigits="0"/> VND
                    </span>
                </div>

                <button type="button" id="place-order-btn" class="checkout-btn">
                    <i class="fas fa-check-circle"></i> Đặt hàng
                </button>

                <button type="button" id="open-bank-modal-btn" class="checkout-btn" style="display:none;">
                    <i class="fas fa-check-circle"></i> Đặt hàng
                </button>
            </section>
        </div>
    </div>
</form>

<div id="bank-payment-modal" class="modal-overlay" style="display:none;">
    <div class="modal-box">
        <div class="modal-header">
            <h3><i class="fas fa-university"></i> Xác nhận thanh toán chuyển khoản</h3>
            <button type="button" class="modal-close-btn" id="modal-close-x">&times;</button>
        </div>
        <div class="modal-body">

            <div class="modal-order-summary">
                <p class="modal-label">Mã đơn hàng</p>
                <p class="modal-order-id" id="modal-order-ref">AROMACAFE-${sessionScope.user.id}-<span id="modal-timestamp"></span></p>
            </div>

            <div class="modal-bank-details">
                <div class="modal-bank-row">
                    <span><i class="fas fa-university"></i> Ngân hàng</span>
                    <strong>BIDV</strong>
                </div>
                <div class="modal-bank-row">
                    <span><i class="fas fa-credit-card"></i> Số tài khoản</span>
                    <strong>
                        8800273817
                        <button type="button" class="copy-btn" onclick="copyText('8800273817', this)">
                            <i class="fas fa-copy"></i>
                        </button>
                    </strong>
                </div>
                <div class="modal-bank-row">
                    <span><i class="fas fa-user"></i> Chủ tài khoản</span>
                    <strong>AROMA CAFE</strong>
                </div>
                <div class="modal-bank-row">
                    <span><i class="fas fa-money-bill-wave"></i> Số tiền</span>
                    <strong class="modal-amount" id="modal-amount-display">0 VND</strong>
                </div>
                <div class="modal-bank-row">
                    <span><i class="fas fa-comment-dots"></i> Nội dung CK</span>
                    <strong>
                        <span id="modal-transfer-content">AROMACAFE ${sessionScope.user.id} ${sessionScope.user.full_name}</span>
                        <button type="button" class="copy-btn" onclick="copyModalContent()">
                            <i class="fas fa-copy"></i>
                        </button>
                    </strong>
                </div>
            </div>

            <div class="modal-qr-section">
                <p class="qr-label"><i class="fas fa-qrcode"></i> Quét mã QR thanh toán:</p>
                <img id="modal-qr-image"
                     src="https://img.vietqr.io/image/BIDV-8800273817-compact2.png?amount=0&addInfo=AROMACAFE+${sessionScope.user.id}&accountName=AROMA+CAFE"
                     alt="QR Code"
                     class="qr-img modal-qr-img">
                <p class="qr-note">
                    <i class="fas fa-exclamation-circle"></i>
                    Vui lòng chuyển khoản đúng số tiền và nội dung, sau đó nhấn "Xác nhận đã chuyển khoản".
                </p>
            </div>

            <div class="modal-actions">
                <button type="button" id="confirm-payment-btn" class="checkout-btn modal-confirm-btn">
                    <i class="fas fa-check"></i> Xác nhận đã chuyển khoản
                </button>
                <button type="button" id="cancel-payment-btn" class="cancel-btn">
                    <i class="fas fa-times"></i> Hủy giao dịch
                </button>
            </div>
        </div>
    </div>
</div>

<div id="success-modal" class="modal-overlay" style="display:none;">
    <div class="modal-box success-modal-box">
        <div class="success-icon-wrap">
            <i class="fas fa-check-circle success-icon"></i>
        </div>
        <h3 class="success-title">Đặt hàng thành công!</h3>
        <p class="success-desc">Đơn hàng của bạn đã được ghi nhận. Chúng tôi sẽ xác nhận và liên hệ với bạn sớm nhất.</p>
        <a href="${pageContext.request.contextPath}/account" class="checkout-btn" style="display:inline-block; text-decoration:none; text-align:center; margin-top:10px;">
            <i class="fas fa-list-alt"></i> Xem đơn hàng của tôi
        </a>
    </div>
</div>

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

<script>
    window.contextPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/assets/js/payment.js?v=4"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>