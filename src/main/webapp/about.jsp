<%--
  Created by IntelliJ IDEA.
  User: MyPC
  Date: 21/12/2025
  Time: 3:47 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Về chúng tôi | Aroma Café</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/aboutUs.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
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

<main class="about-wrapper">

    <section class="about-banner">
        <img src="${pageContext.request.contextPath}/assets/img/about06.png" alt="Banner" class="banner-img"/>
        <div class="banner-text">
            <h1>VỀ CHÚNG TÔI</h1>
        </div>
    </section>

    <section class="about-section fade-in">
        <h2>ĐỒNG HÀNH ĐỂ ĐI XA</h2>
        <p>
            Trong hành trình phát triển của mình, Aroma Café luôn xem trọng mối quan hệ hợp tác bền vững cùng đối tác,
            đồng hành cùng khách hàng và nông dân trồng cà phê để lan tỏa tinh thần cà phê sạch, chất lượng và khác
            biệt.
        </p>
        <div class="about-imgs">
            <img src="${pageContext.request.contextPath}/assets/img/about01.png" alt="Hình ảnh 1"/>
        </div>
    </section>


    <section class="about-section slide-up">
        <div class="about-content reverse">
            <div class="about-text">
                <h2>TÔN VINH GIÁ TRỊ THẬT CỦA CÀ PHÊ VIỆT</h2>
                <p>
                    Aroma Café được tạo ra từ niềm đam mê “Tận hưởng cà phê Việt” – nơi mọi người được thưởng thức hương
                    vị nguyên bản,
                    phong phú và chân thật nhất. Chúng tôi không ngừng tìm kiếm, chọn lọc và hợp tác cùng những nông hộ
                    để xây dựng vùng trồng bền vững.
                </p>
            </div>
            <div class="about-imgs">
                <img src="${pageContext.request.contextPath}/assets/img/about02.png" alt="Hình ảnh 2"/>
            </div>
        </div>
    </section>


    <section class="about-section fade-in">
        <div class="about-content">
            <div class="about-imgs">
                <img src="${pageContext.request.contextPath}/assets/img/about04.png" alt="Hình ảnh 3"/>
            </div>
            <div class="about-text">
                <h2>NHỮNG HẠT CÀ PHÊ NGUYÊN CHẤT</h2>
                <p>
                    Từ những hạt cà phê được chọn lọc kỹ lưỡng, quy trình khép kín và rang xay chuẩn mực,
                    Aroma Café mang đến trải nghiệm tinh tế trong từng tách cà phê – để mỗi giây phút bên ly cà phê là
                    một niềm vui nhỏ của cuộc sống.
                </p>
            </div>
        </div>
    </section>


    <section class="about-section slide-up">
        <div class="about-content reverse">
            <div class="about-text">
                <h2>CHÚNG TÔI LUÔN HƯỚNG ĐẾN TƯƠNG LAI BỀN VỮNG</h2>
                <p>
                    Mục tiêu của chúng tôi không chỉ là sản xuất cà phê ngon mà còn góp phần phát triển cộng đồng và bảo
                    vệ môi trường.
                    Mỗi sản phẩm của Aroma Café đều là cam kết về chất lượng và trách nhiệm xã hội.
                </p>
            </div>
            <div class="about-imgs">
                <img src="${pageContext.request.contextPath}/assets/img/about05.png" alt="Hình ảnh 4"/>
            </div>
        </div>
    </section>
</main>

<footer class="footer">
    <div class="footer-top">
        <div class="foot-content left">
            <h3>Aroma Café</h3>
            <p>Địa chỉ: Trường đại học Nông Lâm TPHCM.</p>
            <p>Điện thoại: 0933652267.</p>
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
<script src="${pageContext.request.contextPath}/assets/js/aboutUs.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>

</body>
</html>

