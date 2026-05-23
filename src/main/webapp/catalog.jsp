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
        <a href="${pageContext.request.contextPath}/catalog" class="active">Sản phẩm</a>
        <a href="${pageContext.request.contextPath}/contact">Liên hệ</a>
        <a href="${pageContext.request.contextPath}/about">Giới thiệu</a>
    </div>
</header>

<input type="hidden" id="currentCid" value="${currentCid}">
<input type="hidden" id="currentSort" value="${currentSort}">

<main class="catalog-container">
    <div class="catalog-layout">

        <aside class="sidebar">
            <div class="filter-group">
                <h3 class="filter-title">Danh mục</h3>
                <ul class="list-catalog">
                    <li>
                        <a href="javascript:void(0)" onclick="changeCid(0)" class="catalog-item ${currentCid == 0 ? 'active' : ''}">Tất cả sản phẩm</a>
                    </li>
                    <c:forEach items="${listCategories}" var="c">
                        <li>
                            <a href="javascript:void(0)" onclick="changeCid(${c.id})" class="catalog-item ${currentCid == c.id ? 'active' : ''}">${c.name}</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>

            <div class="filter-group">
                <h3 class="filter-title">Sắp xếp theo</h3>
                <div class="filter-select-wrapper">
                    <select name="sort" class="sort-dropdown" onchange="changeSort(this.value)">
                        <option value="default" ${currentSort == 'default' ? 'selected' : ''}>Mặc định</option>
                        <option value="price-desc" ${currentSort == 'price-desc' ? 'selected' : ''}>Giá: Cao đến thấp</option>
                        <option value="price-asc" ${currentSort == 'price-asc' ? 'selected' : ''}>Giá: Thấp đến cao</option>
                        <option value="sold" ${currentSort == 'sold' ? 'selected' : ''}>Bán chạy nhất</option>
                        <option value="rating" ${currentSort == 'rating' ? 'selected' : ''}>Đánh giá tốt nhất</option>
                    </select>
                </div>
            </div>
            <div class="filter-group">
                <h3 class="filter-title">Khoảng giá</h3>
                <div class="price-radio-group" style="margin-bottom: 14px;">
                    <label class="radio-item">
                        <input type="radio" name="price" value="all"
                        ${(empty param.minPrice and empty param.maxPrice) ? 'checked' : ''}
                               onclick="changePrice('all')">
                        <span>Tất cả</span>
                    </label>
                    <label class="radio-item">
                        <input type="radio" name="price" value="0-100000"
                        ${param.minPrice == '0' and param.maxPrice == '100000' ? 'checked' : ''}
                               onclick="changePrice('0-100000')">
                        <span>Dưới 100.000đ</span>
                    </label>
                    <label class="radio-item">
                        <input type="radio" name="price" value="100000-500000"
                        ${param.minPrice == '100000' and param.maxPrice == '500000' ? 'checked' : ''}
                               onclick="changePrice('100000-500000')">
                        <span>100.000đ - 500.000đ</span>
                    </label>
                    <label class="radio-item">
                        <input type="radio" name="price" value="500000-10000000"
                        ${param.minPrice == '500000' ? 'checked' : ''}
                               onclick="changePrice('500000-10000000')">
                        <span>Trên 500.000đ</span>
                    </label>
                </div>
                <div class="price-range-wrap">
                    <div class="price-range-display">
                        <span id="price-min-label">0đ</span>
                        <span>—</span>
                        <span id="price-max-label">10.000.000đ</span>
                    </div>
                    <div class="price-slider-track">
                        <div class="price-slider-fill" id="slider-fill"></div>
                        <input type="range" id="range-min" class="price-range-input"
                               min="0" max="10000000" step="10000" value="${currentMin}">
                        <input type="range" id="range-max" class="price-range-input"
                               min="0" max="10000000" step="10000" value="${currentMax}">
                    </div>
                    <button class="price-apply-btn" onclick="applyPriceFilter()">Áp dụng</button>
                </div>
            </div>
        </aside>

        <section class="product-area">
            <div class="product-grid">
                <c:if test="${empty listProducts}">
                    <div class="no-product">
                        <i class="fas fa-box-open"></i>
                        <p>Không tìm thấy sản phẩm nào trong danh mục này.</p>
                    </div>
                </c:if>

                <c:forEach items="${listProducts}" var="p">
                    <div class="product-card">
                        <a href="product?pid=${p.id}">
                            <div class="product-img">
                                <img src="${p.image_url}" alt="${p.name}">
                                <c:if test="${p.sold > 20}">
                                    <span class="badge-hot">Bán chạy</span>
                                </c:if>
                            </div>
                            <div class="product-info">
                                <p class="p-category">${p.category_name}</p>
                                <h3 class="p-name">${p.name}</h3>
                                <p class="p-price"><fmt:formatNumber value="${p.price}" type="number"/> VND</p>
                                <div class="p-meta">
                                    <span class="p-rating"><i class="fas fa-star"></i> ${String.format("%.1f", p.avg_rating)}</span>
                                    <span class="p-sold">Đã bán: ${p.sold}</span>
                                </div>
                            </div>
                        </a>
                    </div>
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
                    <button onclick="changePage(${currentPage - 1})" title="Trang trước">
                        <i class="fas fa-angle-left"></i>
                    </button>
                </c:if>

                <c:forEach begin="${startPage}" end="${endPage}" var="i">
                    <c:choose>
                        <c:when test="${currentPage == i}">
                            <input type="number"
                                   id="page-input"
                                   value="${i}"
                                   data-max="${displayTotal}"
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
                    <button onclick="changePage(${displayTotal})" title="Trang cuối cùng">
                        <i class="fas fa-angle-double-right"></i>
                    </button>
                </c:if>

            </div>
        </section>
    </div>
</main>

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

<script src="${pageContext.request.contextPath}/assets/js/catalog.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>