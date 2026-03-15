<%--
  Created by IntelliJ IDEA.
  User: TDat
  Date: 27/12/2025
  Time: 15:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Trang Quản Trị Aroma Café</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <style>
        body {
            display: flex;
            flex-direction: row;
        }

        .right-content {
            width: 80%;
            transition: width 0.3s ease;
        }
    </style>
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
        <a href="${pageContext.request.contextPath}/admin/users" class="menu-item active">Quản lí tài khoản</a>
        <a href="${pageContext.request.contextPath}/admin/reviews" class="menu-item">Quản lí đánh giá</a>
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
        <p>QUẢN LÍ TÀI KHOẢN</p>
    </div>
    <div class="notify">
        <h3>THÔNG BÁO</h3>
        <div class="notify-main">
            <c:forEach items="${listNew}" var="n">
                <p><span>${n.full_name}</span> vừa tạo tài khoản.</p>
            </c:forEach>
        </div>
    </div>
    <form class="search-bar" method="get" action="${pageContext.request.contextPath}/search-user">
        <input type="text" name="keyword" placeholder="Tìm kiếm người dùng">
        <button type="submit"><i class="fas fa-search"></i></button>
    </form>
    <div class="main-menu">
        <button id="add">+ Thêm tài khoản</button>
    </div>
    <div class="list-account">
        <h3>DANH SÁCH TÀI KHOẢN</h3>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Họ và tên</th>
                <th>Email</th>
                <th>Số điện thoại</th>
                <th>Role</th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
                <c:forEach items="${listUsers}" var="u">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.full_name}</td>
                        <td>${u.email}</td>
                        <td>${u.phone}</td>
                        <td>${u.role}</td>
                        <td>
                            <button class="remake" ><i class="fa-solid fa-pen"></i></button>
                        </td>
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/delete-user">
                                <input type="hidden" name="uid" value="${u.id}">
                                <c:choose>
                                    <c:when test="${u.status eq 'active'}">
                                        <button type="submit" style="background-color: #e74c3c; color: white;" title="Ban user">
                                            <i class="fa-solid fa-ban"></i>
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="submit" style="background-color: #7f8c8d; color: #ddd;" title="User đã bị ban">
                                            <i class="fa-solid fa-ban"></i>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<div class="form-add" id="form-add" style="display: none">
    <div class="form-title">
        <p>THÊM TÀI KHOẢN</p>
        <button id="take-off">X</button>
    </div>
    <form method="post" action="${pageContext.request.contextPath}/add-user" class="main-form">
        <div class="p name-p">
            <label>Tên người dùng</label>
            <input type="text" name="name" placeholder="Tên người dùng">
        </div>
        <div class="price-p">
            <label>Email</label>
            <input type="email" name="email" placeholder="Email" required>
        </div>
        <div class="count-p">
            <label>Mật khẩu</label>
            <input type="text" name="pass" placeholder="Mật khẩu" required>
        </div>
        <div class="count-p">
            <label>Số điện thoại</label>
            <input type="text" name="phone" placeholder="Số điện thoại">
        </div>
        <div class="type-p">
            <label>Role</label>
            <select name="role">
                <option>admin</option>
                <option>customer</option>
            </select>
        </div>
        <button class="submit" type="submit">Thêm</button>
    </form>
</div>
<!-- POPUP SỬA ACCOUNT -->
<div class="form-add" id="form-remake" style="display: none">
    <div class="form-title">
        <p>SỬA ACCOUNT</p>
        <button id="close-remake">X</button>
    </div>

    <form class="main-form" method="post" action="${pageContext.request.contextPath}/update-user">
        <input type="hidden" name="uid" id="up_uid">
        <div class="type-p">
            <label>Role</label>
            <select name="up_role">
                <option>admin</option>
                <option>customer</option>
            </select>
        </div>
        <button class="submit" type="submit">Lưu Thay Đổi</button>
        <c:if test="${not empty notice_up}">
            <p>${notice_up}</p>
        </c:if>
    </form>
</div>
<button class="slide-top" id="slide-top"><i class="fas fa-angle-up"></i></button>
<script>
        document.querySelectorAll(".remake").forEach(btn => {
        btn.onclick = function () {
            const row = btn.closest("tr");

            const uid = row.children[0].innerText.trim();
            const role = row.children[4].innerText.trim();

            document.getElementById("up_uid").value = uid;
            document.querySelector("select[name='up_role']").value = role;

            document.getElementById("form-remake").style.display = "block";
        }
    });
</script>
<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>