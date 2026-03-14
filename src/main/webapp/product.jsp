<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fomt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>${product.name} | Aroma Café</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/product.css">
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
        <div class="search-bar">
            <input type="text" id="search-input" placeholder="Tìm kiếm...">
            <button id="search-button"><i class="fas fa-search"></i></button>
        </div>
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

<div class="product-body">
    <div class="product-main">
        <div class="product-title">
            <span>Sản phẩm</span>
            <p>${product.name}</p>
        </div>
        <div class="product-content">
            <div class="product-img">
                <img src="${product.image_url}" id="img-main">
                <div class="thumbnail-gallery">
                    <c:forEach items="${listImage}" var="i">
                        <img src="${i.image_url}" alt="${product.name}" class="thumbnail-item ${i.image_url == product.image_url ? 'active' : ''}"
                             data-full-image="${i.image_url}">
                    </c:forEach>
                </div>
            </div>
            <div class="content">
                <p><fmt:formatNumber value="${product.price}" type="number" maxFractionDigits="0"/> VND</p>

                <div class="sub-content">
                    <p>Thương hiệu: <span>Aroma Cafe</span></p>
                </div>
                <form method="post" action="${pageContext.request.contextPath}/add-to-cart">
                    <input type="hidden" name="pid" value="${product.id}">
                    <div class="weight">
                        <label>Khối lượng</label>
                        <p>${product.weight_grams} gram</p>
                    </div>
                    <div class="type">
                        <label>Loại</label>
                        <p>${product.category_name}</p>
                    </div>
                    <div class="count-num">
                        <label>Số lượng</label>
                        <button id="count-minus" type="button">-</button>
                        <input type="hidden" name="q" id="q" value="1">
                        <span id="num-count">1</span>
                        <button id="count-add" type="button">+</button>
                    </div>
                    <button type="submit">Thêm vào giỏ hàng</button>
                </form>
            </div>
            <div class="des-right">
                <p class="title">Thông tin liên hệ tư vấn</p>
                <div class="list-call">
                    <p><i class="fab fa-facebook-f"></i> Facebook: <a href="#">AromaCafe</a></p>
                    <p><i class="fab fa-twitter"></i>Twitter: <a href="#">AromaCafe</a></p>
                    <p><i class="fab fa-instagram"></i>Instagram: <a href="#">AromaCafe</a></p>
                    <p><i class="fab fa-linkedin-in"></i>Linkedin: <a href="#">AromaCafe</a></p>
                    <p><i class="fa-solid fa-phone"></i>Số hotline: XXXXXXXXXX</p>
                </div>
            </div>
        </div>
    </div>
    <div class="product-details">
        <div class="detail-main" id="productDescription">
            <div class="main-title">
                <span class="line"></span>
                <h2>MÔ TẢ SẢN PHẨM</h2>
                <span class="line"></span>
            </div>
            <div class="detail-content" id="contentToCollapse">
                ${product.description}
            </div>
            <button class="toggle-button" id="readMoreBtn">
                Xem thêm <span class="arrow"></span>
            </button>
        </div>
    </div>
    <div class="product-comment">
        <div class="main-section-title">
            <span class="line"></span>
            <h2>ĐÁNH GIÁ</h2>
            <span class="line"></span>
        </div>
        <div class="product-comment-wrapper">
            <div class="review-summary">
                <div class="average-rating">
                    <span class="rating-value">${avg}/5</span>
                    <div class="stars" style="--rating: ${avg}" aria-label="Đánh giá trung bình là 0/5 sao"></div>
                    <span class="review-count">(${count} đánh giá)</span>
                </div>
            </div>

            <div class="review-list">
                <div class="review-item">
                    <c:forEach items="${review}" var="r">
                        <div class="review-item">
                            <div class="review-author">${r.username} - <span class="review-date"><fomt:formatDate value="${r.created_at}" pattern="dd/MM/yyyy"/></span></div>
                            <div class="review-meta">
                                <div class="stars" style="--rating: ${r.rating};" aria-label="Đánh giá ${r.rating}/5 sao"></div>
                            </div>
                            <p class="review-body">
                                ${r.comment}
                            </p>
                            <div class="review-images">
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <form class="review-form" method="post"
                      action="${pageContext.request.contextPath}/addReview">

                    <input type="hidden" name="pid" value="${product.id}">

                    <h4>Viết đánh giá của bạn</h4>

                    <div class="form-group rating-group">
                        <label>Bạn đánh giá sản phẩm này bao nhiêu sao?</label>
                        <div class="star-rating-input">
                            <input type="radio" id="star5" name="rating" value="5" required><label for="star5"></label>
                            <input type="radio" id="star4" name="rating" value="4"><label for="star4"></label>
                            <input type="radio" id="star3" name="rating" value="3"><label for="star3"></label>
                            <input type="radio" id="star2" name="rating" value="2"><label for="star2"></label>
                            <input type="radio" id="star1" name="rating" value="1"><label for="star1"></label>
                        </div>
                    </div>

                    <div class="form-group">
        <textarea name="comment" rows="5"
                  placeholder="Hãy chia sẻ cảm nhận của bạn..."></textarea>
                    </div>
                    <button type="submit">Gửi đánh giá</button>
                </form>
                <c:if test="${not empty sessionScope.reviewNotice}">
                    <div class="notice" style="text-align: center;">
                        <p style="padding: 5px; margin-top: 5px">${sessionScope.reviewNotice}</p>
                    </div>
                    <c:remove var="reviewNotice" scope="session"/>
                </c:if>
            </div>
        </div>
        <div class="product-relative">
            <div class="relative-title">
                <span class="line"></span>
                <h2>SẢN PHẨM LIÊN QUAN</h2>
                <span class="line"></span>
            </div>
            <div class="product-catalog" id="product-catalog">
                <c:forEach items="${relative}" var="p">
                    <a class="product" href="product?pid=${p.id}">
                        <img src="${p.image_url}" alt="${p.name}">
                        <p>${p.name}</p>
                        <span><fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/> VND</span>
                        <label>Loại: ${p.category_name}</label>
                    </a>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

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

<script src="${pageContext.request.contextPath}/assets/js/product.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
<script>
    const numCount = document.getElementById("num-count");
    const qInput = document.getElementById("q");

    document.getElementById("count-add").onclick = () => {
        let v = parseInt(numCount.innerText) + 1;
        numCount.innerText = v;
        qInput.value = v;
    };

    document.getElementById("count-minus").onclick = () => {
        let v = Math.max(1, parseInt(numCount.innerText) - 1);
        numCount.innerText = v;
        qInput.value = v;
    };

</script>
</body>
</html>