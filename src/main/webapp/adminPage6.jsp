<%--
  Created by IntelliJ IDEA.
  User: TDat
  Date: 27/12/2025
  Time: 15:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quản lí đánh giá</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/adminPage6.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/toast.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/toast.jsp"/>
<div class="left-menu" id="left-menu">
    <div class="logo">
        <img src="${pageContext.request.contextPath}/assets/img/logo.png" onclick="location.href='${pageContext.request.contextPath}/'" width="300px" height="100px">
    </div>
    <div class="menu">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="menu-item">Tổng quan</a>

        <c:if test="${sessionScope.user.hasPermission('manage_product')}">
            <a href="${pageContext.request.contextPath}/admin/products" class="menu-item">Quản lí sản phẩm</a>
        </c:if>

        <c:if test="${sessionScope.user.hasPermission('manage_order')}">
            <a href="${pageContext.request.contextPath}/admin/orders" class="menu-item">Quản lí đơn hàng</a>
        </c:if>

        <c:if test="${sessionScope.user.hasPermission('manage_user')}">
            <a href="${pageContext.request.contextPath}/admin/users" class="menu-item">Quản lí tài khoản</a>
        </c:if>

        <c:if test="${sessionScope.user.hasPermission('manage_review')}">
            <a href="${pageContext.request.contextPath}/admin/reviews" class="menu-item active">Quản lí đánh giá</a>
        </c:if>

        <c:if test="${sessionScope.user.hasPermission('manage_banner')}">
            <a href="${pageContext.request.contextPath}/admin/banner" class="menu-item">Quản lí banner</a>
        </c:if>

        <c:if test="${sessionScope.user.hasPermission('manage_promotion')}">
            <a href="${pageContext.request.contextPath}/admin/promotion" class="menu-item">Quản lí mã giảm giá</a>
        </c:if>

        <c:if test="${sessionScope.user.hasPermission('manage_contact')}">
            <a href="${pageContext.request.contextPath}/admin/contact" class="menu-item">Chăm sóc khách hàng</a>
        </c:if>

        <a href="#" class="menu-item" onclick="location.href='${pageContext.request.contextPath}/logout'">Đăng xuất</a>
    </div>
    <div class="footer">
        <p>2024 Aroma Café. All rights reserved.</p>
    </div>
</div>
<div class="right-content" id="right-content">
    <div class="title">
        <button class="slider-menu" id="slider-menu"><i class="fa-solid fa-bars"></i></button>
        <p>QUẢN LÍ ĐÁNH GIÁ</p>
    </div>
    <form class="search-bar" method="get" action="${pageContext.request.contextPath}/search-review">
        <input type="text" name="key" placeholder="Tìm kiếm người dùng hoặc sản phẩm">
        <button type="submit"><i class="fas fa-search"></i></button>
    </form>
    <div class="main-content">
        <form class="main-menu-date" method="get" action="${pageContext.request.contextPath}/filter-review">
            <div class="start">
                <label>Từ ngày</label>
                <input name="start" type="date" value="${startDate}">
            </div>
            <div class="end">
                <label>Đến ngày</label>
                <input name="end" type="date" value="${endDate}">
            </div>
            <div class="action-buttons">
                <button type="submit">Xác nhận</button>
                <a href="${pageContext.request.contextPath}/admin/reviews" class="btn-reset">Đặt lại</a>
            </div>
        </form>
        <div class="review">
            <h3 class="review-title">DANH SÁCH ĐÁNH GIÁ</h3>
            <table>
                <thead>
                <tr>
                    <th>Sản phẩm</th>
                    <th>Khách hàng</th>
                    <th>Xếp hạng</th>
                    <th>Ngày đánh giá</th>
                    <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${listReview}" var="r">
                    <tr>
                        <td>${r.productname}</td>
                        <td>${r.username}</td>
                        <td>
                            <span class="rating-stars">${r.rating}/5</span>
                        </td>
                        <td>${r.created_at}</td>
                        <td>
                            <button class="detail"
                                    data-product="${r.productname}"
                                    data-user="${r.username}"
                                    data-rating="${r.rating}"
                                    data-date="${r.created_at}"
                                    data-comment="${r.comment}">
                                <i class="fa-solid fa-eye"></i>
                            </button>

                            <form method="post" action="${pageContext.request.contextPath}/delete-review">
                                <input type="hidden" name="rid" value="${r.id}">
                                <button class="delete"><i class="fa-solid fa-trash"></i></button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="modal-backdrop" id="modal-backdrop" style="display: none;" onclick="closeReviewModal()"></div>
<div class="detail-p" id="detail-p" style="display: none">
    <button id="close" onclick="closeReviewModal()"><i class="fa-solid fa-xmark"></i></button>
    <h3>CHI TIẾT ĐÁNH GIÁ</h3>

    <div class="review-info-grid">
        <div class="info-item">
            <span class="info-label">Sản phẩm:</span>
            <span class="info-value review-product"></span>
        </div>
        <div class="info-item">
            <span class="info-label">Khách hàng:</span>
            <span class="info-value review-user"></span>
        </div>
        <div class="info-item">
            <span class="info-label">Ngày đánh giá:</span>
            <span class="info-value review-date"></span>
        </div>
        <div class="info-item">
            <span class="info-label">Xếp hạng:</span>
            <span class="info-value review-rating-badge"></span>
        </div>
    </div>

    <div class="detail-full">
        <p class="comment-label"><i class="fa-solid fa-comment-dots"></i> Nội dung bình luận:</p>
        <div class="review-text-box">
            <p class="review-text"></p>
        </div>
    </div>
</div>
<button class="slide-top" id="slide-top"><i class="fas fa-angle-up"></i></button>
<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
<script>
    document.querySelectorAll(".detail").forEach(btn => {
        btn.addEventListener("click", function () {
            document.getElementById("detail-p").style.display = "flex";
            document.getElementById("modal-backdrop").style.display = "block";
            const rating = this.dataset.rating ? this.dataset.rating.trim() : "5";
            console.log("Dữ liệu nhận được khi click:", this.dataset);
            document.querySelector(".review-product").innerText = this.dataset.product || "";
            document.querySelector(".review-user").innerText = this.dataset.user || "";
            document.querySelector(".review-date").innerText = this.dataset.date || "";
            document.querySelector(".review-rating-badge").innerHTML =
                `<span class="rating-badge">` + rating + `/5 <i class="fa-solid fa-star text-warning"></i></span>`;

            document.querySelector(".review-text").innerText = this.dataset.comment ? this.dataset.comment.trim() : "(Không có nội dung bình luận)";
        });
    });
    function closeReviewModal() {
        document.getElementById("detail-p").style.display = "none";
        document.getElementById("modal-backdrop").style.display = "none";
    }
</script>
</body>
</html>
