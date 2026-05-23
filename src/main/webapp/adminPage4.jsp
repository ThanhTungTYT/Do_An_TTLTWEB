<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Trang Quản Trị Aroma Café</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/adminPage4.css">
    <style>
        body { display: flex; flex-direction: row; }
        .right-content { width: 80%; transition: width 0.3s ease; }
        .permission-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 10px;
            margin-top: 10px;
            background: #f9f9f9;
            padding: 10px;
            border-radius: 5px;
        }
        .permission-item { font-size: 0.9em; cursor: pointer; }
    </style>
</head>
<body>
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
            <a href="${pageContext.request.contextPath}/admin/users" class="menu-item active">Quản lí tài khoản</a>
        </c:if>

        <c:if test="${sessionScope.user.hasPermission('manage_review')}">
            <a href="${pageContext.request.contextPath}/admin/reviews" class="menu-item">Quản lí đánh giá</a>
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
        <p>2026 Aroma Café. All rights reserved.</p>
    </div>
</div>

<div class="right-content" id="right-content">
    <div class="title">
        <button class="slider-menu" id="slider-menu"><i class="fa-solid fa-bars"></i></button>
        <p>QUẢN LÍ TÀI KHOẢN</p>
    </div>

    <c:if test="${not empty sessionScope.auth_error}">
        <div style="background: #ffeded; color: #e74c3c; padding: 15px; margin: 10px; border-radius: 5px; border: 1px solid #e74c3c;">
            <i class="fa-solid fa-triangle-exclamation"></i> ${sessionScope.auth_error}
        </div>
        <script>
            setTimeout(() => { alert("${sessionScope.auth_error}"); }, 500);
        </script>
        <% session.removeAttribute("auth_error"); %>
    </c:if>

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
                <th>Thao tác</th>
                <th>Ban</th>
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
                            <%-- Lưu chuỗi quyền của User vào attribute để Javascript lấy dễ hơn --%>
                        <button class="remake" data-perms='${u.permissions}'><i class="fa-solid fa-pen"></i></button>
                    </td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/delete-user">
                            <input type="hidden" name="uid" value="${u.id}">
                            <button type="submit" style="background-color: ${u.status eq 'active' ? '#e74c3c' : '#7f8c8d'}; color: white;">
                                <i class="fa-solid fa-ban"></i>
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<div class="form-add" id="form-add" style="display: none">
    <div class="form-title"><p>THÊM TÀI KHOẢN</p><button id="take-off">X</button></div>
    <form method="post" action="${pageContext.request.contextPath}/add-user" class="main-form">
        <div class="p name-p"><label>Tên người dùng</label><input type="text" name="name"></div>
        <div class="price-p"><label>Email</label><input type="email" name="email" required></div>
        <div class="count-p"><label>Mật khẩu</label><input type="password" name="pass" required></div>
        <div class="type-p">
            <label>Quyền hạn chi tiết:</label>
            <div class="permission-grid">
                <c:forEach items="${allPermissions}" var="p">
                    <label class="permission-item">
                        <input type="checkbox" name="permissions" value="${p.id}"> ${p.permission_name}
                    </label>
                </c:forEach>
            </div>
        </div>
        <button class="submit" type="submit">Thêm</button>
    </form>
</div>

<div class="form-add" id="form-remake" style="display: none">
    <div class="form-title"><p>SỬA ACCOUNT & PHÂN QUYỀN</p><button id="close-remake">X</button></div>
    <form class="main-form" method="post" action="${pageContext.request.contextPath}/update-user">
        <input type="hidden" name="uid" id="up_uid">

        <div class="type-p">
            <label>Cấp quyền cho User này:</label>
            <div class="permission-grid">
                <c:forEach items="${allPermissions}" var="p">
                    <label class="permission-item">
                        <input type="checkbox" name="up_permissions" value="${p.id}" data-key="${p.permission_key}">
                            ${p.permission_name}
                    </label>
                </c:forEach>
            </div>
        </div>

        <div class="type-p" style="margin-top: 15px;">
            <label>Thay đổi Role chính:</label>
            <select name="up_role">
                <option value="admin">admin</option>
                <option value="customer">customer</option>
            </select>
        </div>

        <button class="submit" type="submit">Lưu Thay Đổi</button>
    </form>
</div>

<button class="slide-top" id="slide-top"><i class="fas fa-angle-up"></i></button>

<script>
    document.querySelectorAll(".remake").forEach(btn => {
        btn.onclick = function () {
            const row = btn.closest("tr");
            const uid = row.children[0].innerText.trim();
            const role = row.children[4].innerText.trim();

            const userPerms = btn.getAttribute("data-perms");

            document.getElementById("up_uid").value = uid;
            document.querySelector("select[name='up_role']").value = role;

            document.querySelectorAll("input[name='up_permissions']").forEach(cb => {
                const pKey = cb.getAttribute("data-key");
                cb.checked = userPerms.includes(pKey);
            });

            document.getElementById("form-remake").style.display = "block";
        }
    });

    document.getElementById("close-remake").onclick = () => document.getElementById("form-remake").style.display = "none";
    document.getElementById("add").onclick = () => document.getElementById("form-add").style.display = "block";
    document.getElementById("take-off").onclick = () => document.getElementById("form-add").style.display = "none";
</script>
<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
</body>
</html>