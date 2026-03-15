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
    <title>Chăm sóc khách hàng</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
</head>
<body>
<div class="left-menu" id="left-menu">
    <div class="logo">
        <img src="${pageContext.request.contextPath}/assets/img/logo.png" onclick="location.href='${pageContext.request.contextPath}/'" width="300px" height="100px">
    </div>
    <div class="menu">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="menu-item">Tổng quan</a>
        <a href="${pageContext.request.contextPath}/admin/products" class="menu-item">Quản lí sản phẩm</a>
        <a href="${pageContext.request.contextPath}/admin/orders" class="menu-item">Quản lí đơn hàng</a>
        <a href="${pageContext.request.contextPath}/admin/users" class="menu-item">Quản lí tài khoản</a>
        <a href="${pageContext.request.contextPath}/admin/reviews" class="menu-item active">Quản lí đánh giá</a>
        <a href="${pageContext.request.contextPath}/admin/banner" class="menu-item">Quản lí banner</a>
        <a href="${pageContext.request.contextPath}/admin/promotion" class="menu-item">Quản lí mã giảm giá</a>
        <a href="${pageContext.request.contextPath}/admin/contact" class="menu-item">Chăm sóc khách hàng</a>
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
                <label>Start date</label>
                <input name="start" type="date">
            </div>
            <div class="end">
                <label>End date</label>
                <input name="end" type="date">
            </div>
            <button>Xác nhận</button>
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

<div class="detail-p" id="detail-p" style="display: none">
    <button id="close">X</button>
    <h3>CHI TIẾT ĐÁNH GIÁ</h3>
    <div></div>
    <div></div>
    <div></div>
    <div></div>
    <div class="detail-full">
        <p>Nội dung:</p>
        <p class="review-text"></p>
    </div>
</div>
<button class="slide-top" id="slide-top"><i class="fas fa-angle-up"></i></button>
<script>
    document.querySelectorAll(".detail").forEach(btn => {
        btn.onclick = function () {
            document.querySelector("#detail-p div:nth-child(3)").innerText =
                "Sản phẩm: " + btn.dataset.product;
            document.querySelector("#detail-p div:nth-child(4)").innerText =
                "Khách hàng: " + btn.dataset.user;
            document.querySelector("#detail-p div:nth-child(5)").innerText =
                "Ngày: " + btn.dataset.date;
            document.querySelector("#detail-p div:nth-child(6)").innerText =
                "Xếp hạng: " + btn.dataset.rating + "/5";
            document.querySelector(".review-text").innerText =
                btn.dataset.comment;
        }
    });
</script>
<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
