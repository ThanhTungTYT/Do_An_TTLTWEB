<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Danh sách sản phẩm | Aroma Café</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/catalog.css">
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

<input type="hidden" id="currentCid" value="${currentCid}">
<input type="hidden" id="currentSort" value="${currentSort}">

<div class="catalog">

    <div class="catalog-list" id="catalog-list">
        <p class="head-catalog">Danh mục sản phẩm</p>

        <ul class="list-catalog" id="list-catalog">
            <li>
                <a href="catalog?cid=0" class="catalog-item ${currentCid == 0 ? 'active' : ''}">Tất cả</a>
            </li>
            <c:forEach items="${listCategories}" var="c">
                <li>
                    <a href="catalog?cid=${c.id}" class="catalog-item ${currentCid == c.id ? 'active' : ''}">${c.name}</a>
                </li>
            </c:forEach>
        </ul>
        <hr>

        <div class="filter">
            <select name="sort" onchange="changeSort(this.value)">
                <option value="default" ${currentSort == 'default' ? 'selected' : ''}>-Chọn lựa-</option>
                <option value="price-desc" ${currentSort == 'price-desc' ? 'selected' : ''}>Giá cao đến thấp</option>
                <option value="price-asc" ${currentSort == 'price-asc' ? 'selected' : ''}>Giá thấp đến cao</option>
                <option value="sold" ${currentSort == 'sold' ? 'selected' : ''}>Lượt mua nhiều nhất</option>
                <option value="rating" ${currentSort == 'rating' ? 'selected' : ''}>Lượt đánh giá</option>
            </select>
        </div>
    </div>

    <div class="product-area" id="product-area">

        <div class="product-list" id="product-list">
            <c:if test="${empty listProducts}">
                <p style="width:100%; text-align:center; padding: 20px;">Không có sản phẩm nào.</p>
            </c:if>

            <c:forEach items="${listProducts}" var="p">
                <a href="product?pid=${p.id}" class="product">
                    <img src="${p.image_url}" alt="${p.name}">
                    <p>${p.name}</p>
                    <span><fmt:formatNumber value="${p.price}" type="number"/> VND</span>
                    <label>Loại: ${p.category_name}</label>
                    <div style="font-size: 12px; color: #666; margin-top: 5px;">
                        <span><i class="fas fa-star" style="color:gold"></i> ${String.format("%.1f", p.avg_rating)}</span>
                        <span style="margin-left: 10px;">Đã bán: ${p.sold}</span>
                    </div>
                </a>
            </c:forEach>
        </div>

        <div class="product-page" id="pagination" style="margin-top: 20px; text-align: center;">

            <c:set var="displayTotal" value="${totalPages > 0 ? totalPages : 1}" />

            <c:set var="startPage" value="${currentPage - 1}" />
            <c:set var="endPage" value="${currentPage + 1}" />

            <c:if test="${startPage < 1}">
                <c:set var="startPage" value="1" />
                <c:set var="endPage" value="3" />
            </c:if>

            <c:if test="${endPage > displayTotal}">
                <c:set var="endPage" value="${displayTotal}" />
                <c:set var="startPage" value="${displayTotal - 2}" />
                <c:if test="${startPage < 1}">
                    <c:set var="startPage" value="1" />
                </c:if>
            </c:if>

            <c:if test="${currentPage > 1}">
                <button onclick="changePage(1)" title="Trang đầu tiên">
                    <i class="fas fa-angle-double-left"></i>
                </button>
            </c:if>

            <c:if test="${currentPage > 1}">
                <button onclick="changePage(${currentPage - 1})" title="Trang trước">
                    <i class="fas fa-angle-left"></i>
                </button>
            </c:if>

            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                <c:choose>
                    <c:when test="${currentPage == i}">
                        <input type="number"
                               id="page-input"
                               min = '1'
                               max = '100'
                               value="${i}"
                               data-max="${displayTotal}"
                               oninput="if(this.value.length > 3) this.value = this.value.slice(0, 3);"
                               title="Nhập số trang và nhấn Enter" />
                    </c:when>
                    <c:otherwise>
                        <button onclick="changePage(${i})">${i}</button>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${currentPage < displayTotal}">
                <button onclick="changePage(${currentPage + 1})" title="Trang tiếp theo">
                    <i class="fas fa-angle-right"></i>
                </button>
            </c:if>

            <c:if test="${currentPage < displayTotal}">
                <button onclick="changePage(${displayTotal})" title="Trang cuối cùng">
                    <i class="fas fa-angle-double-right"></i>
                </button>
            </c:if>

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

<script src="${pageContext.request.contextPath}/assets/js/catalog.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>