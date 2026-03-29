<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Trang chủ | Aroma Café</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
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
                    <c:choose>
                        <c:when test="${sessionScope.user.role eq 'admin'}">
                            <a href="${pageContext.request.contextPath}/admin/dashboard">
                                <i class="fas fa-user-shield"></i>
                                <span style="font-size: 14px; margin-left: 5px">
                                    <c:set var="nameParts" value="${fn:split(sessionScope.user.full_name, ' ')}" />
                                    Hi, ${nameParts[fn:length(nameParts) - 1]}!
                                </span>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/account">
                                <i class="fas fa-user"></i>
                                <span style="font-size: 14px; margin-left: 5px">
                                    <c:set var="nameParts" value="${fn:split(sessionScope.user.full_name, ' ')}" />
                                    Hi, ${nameParts[fn:length(nameParts) - 1]}!
                                </span>
                            </a>
                        </c:otherwise>
                    </c:choose>
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

<div class="img-home" id="img-home">
    <button class="left" onclick="showPreviousImage()"><</button>
    <button class="right" onclick="showNextImage()">></button>
</div>

<div class="hot-item slide-up">
    <div class="hot-title">
        <span class="line"></span>
        <h2>SẢN PHẨM NỔI BẬT <i class="fa-solid fa-fire"></i></h2>
        <span class="line"></span>
    </div>
    <div class="product-slider">
        <button class="nav left" onclick="scrollProduct(-1)">❮</button>
        <div class="product-list" id="product-list">
            <c:forEach items="${listProduct}" var="p">
                <a class="product" href="product?pid=${p.id}" data-name="${p.name}"
                    data-price="<fmt:formatNumber value='${p.price}' type='number' maxFractionDigits='0'/> VND"
                    data-image="${p.image_url}"
                    data-sold="${p.sold}"
                    data-des="${p.description}"
                    data-avgr = "${p.avg_rating}">
                    <img src="${p.image_url}" alt="${p.name}">
                    <p>${p.name}</p>
                    <span><fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/> VND</span>
                    <label><i class="fa-solid fa-fire"></i> ${p.sold}</label>
                </a>
            </c:forEach>
        </div>
        <button class="nav right" onclick="scrollProduct(1)">❯</button>
    </div>
    <div class="product-preview" id="productPreview">
        <img id="previewImg">
        <div class="info">
            <h3 id="previewName"></h3>
            <p class="price" id="previewPrice"></p>
            <p class="sold" id="previewSold"></p>
            <p class="description" id="previewDes"></p>
            <p class="rating" id="previewRating"></p>
        </div>
    </div>
</div>

<div class="video-1 fade-in">
    <div class="intro-video-1">
        <p>QUY TRÌNH SẢN XUẤT</p>
        <p class="content">
            Các sản phẩm của chúng tôi được sản xuất theo quy trình sản xuất chuẩn thế giới. Được đóng gói kĩ càng và
            máy móc đạt chuẩn.
        </p>
        <button onclick="location.href='${pageContext.request.contextPath}/contact'">Liên hệ báo giá</button>
    </div>
    <video class="video-bg fill visible" preload playsinline autoplay muted loop>
        <source src="https://90scoffee.vn/wp-content/uploads/2022/07/videos-cat-ngan-full-vien.mp4" type="video/mp4">
    </video>
</div>

<section class="why-choose-us slide-up">
    <div class="section-title">
        <span class="line"></span>
        <h2>TẠI SAO NÊN CHỌN CHÚNG TÔI?</h2>
        <span class="line"></span>
    </div>
    <div class="features-container">
        <div class="feature-item">
            <img src="https://90scoffee.vn/wp-content/uploads/2025/10/natural-ingredients-25x25.png"
                 alt="Icon Vùng nguyên liệu" class="feature-icon">
            <h3>Vùng nguyên liệu cà phê độc quyền</h3>
            <p>Mọi hạt cà phê của 90S Coffee đều được tuyển chọn từ nông trại riêng tại Gia Lai, Đắk Lắk và Lâm Đồng –
                nơi khởi nguồn chất lượng đích thực.</p>
        </div>
        <div class="feature-item">
            <img src="https://90scoffee.vn/wp-content/uploads/2025/10/management-25x25.png" alt="Icon Quy trình ISO"
                 class="feature-icon">
            <h3>Quy trình sản xuất đạt chuẩn ISO</h3>
            <p>Tuân thủ nghiêm ngặt theo hệ thống quản lý an toàn thực phẩm ISO 22000:2018</p>
        </div>
        <div class="feature-item">
            <img src="https://90scoffee.vn/wp-content/uploads/2025/10/handshake-25x25.png" alt="Icon Chính sách"
                 class="feature-icon">
            <h3>Chính sách sỉ linh hoạt – Hậu mãi tận tâm</h3>
            <p>Chúng tôi có chính sách bán hàng sỉ & lẻ. Chế độ hậu mãi tốt, mang lại lợi ích – bảo vệ lợi ích cho khách
                hàng của 90S Coffee.</p>
        </div>
    </div>
</section>

<div class="approach-section fade-in">
    <div class="approach-container">
        <div class="text-content-box">
            <h2>CÁCH TIẾP CẬN CỦA CHÚNG TÔI VỚI CÀ PHÊ</h2>
            <p>Là một trong những doanh nghiệp hàng đầu trong hệ thống cà phê, AROMA CAFÉ là một trong những cầu nối uy
                tín, vững chắc giữa những người nông dân với thị trường cà phê Việt Nam và thế giới.</p>
            <button onclick="location.href='${pageContext.request.contextPath}/contact'">Liên hệ báo giá</button>
        </div>
        <div class="video-content-box">
            <div class="video-container">
                <iframe
                        src="https://www.youtube-nocookie.com/embed/OKdyYY6tShA?"
                        title="YouTube video player"
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                        allowfullscreen>
                </iframe>
            </div>
        </div>
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
                <a href="#"><i class="fab fa-facebook-f"></i></a>
                <a href="#"><i class="fab fa-twitter"></i></a>
                <a href="#"><i class="fab fa-instagram"></i></a>
                <a href="#"><i class="fab fa-linkedin-in"></i></a>
            </div>
        </div>
    </div>
    <div class="footer-bottom">
        <p>&copy; 2024 Aroma Café. All rights reserved.</p>
    </div>
</footer>
<button class="slide-top" id="slide-top"><i class="fas fa-angle-up"></i></button>

<script>
    window.BANNERS = [
        <c:forEach items="${listBanner}" var="b" varStatus="st">
        "${b.banner_url}"<c:if test="${!st.last}">,</c:if>
        </c:forEach>
    ];
</script>

<script src="${pageContext.request.contextPath}/assets/js/index.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>