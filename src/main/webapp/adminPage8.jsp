<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quản lý mã giảm giá</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/adminPage8.css">
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
            <a href="${pageContext.request.contextPath}/admin/reviews" class="menu-item">Quản lí đánh giá</a>
        </c:if>

        <c:if test="${sessionScope.user.hasPermission('manage_banner')}">
            <a href="${pageContext.request.contextPath}/admin/banner" class="menu-item">Quản lí banner</a>
        </c:if>

        <c:if test="${sessionScope.user.hasPermission('manage_promotion')}">
            <a href="${pageContext.request.contextPath}/admin/promotion" class="menu-item active">Quản lí mã giảm giá</a>
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
        <p>QUẢN LÍ MÃ GIẢM GIÁ</p>
    </div>

    <form class="search-bar" action="${pageContext.request.contextPath}/admin/promotion/search" method="GET">
        <input type="text" name="search" placeholder="Tìm kiếm mã giảm giá (ID hoặc Code)" value="${searchKeyword}">
        <button type="submit"><i class="fas fa-search"></i></button>
    </form>

    <div class="main-menu">
        <button id="btn-open-add">+ Thêm mã giảm giá</button>
    </div>

    <div class="list-discount">
        <h3 class="discount-title">DANH SÁCH MÃ GIẢM GIÁ</h3>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Mã Code</th>
                <th>Mô tả</th>
                <th>Điều kiện</th>
                <th>Mức giảm</th>
                <th>SL</th>
                <th>Bắt đầu</th>
                <th>Kết thúc</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${listPromotions}" var="p">
                <tr>
                    <td>#${p.id}</td>
                    <td><strong>${p.code}</strong></td>
                    <td>${p.description}</td>
                    <td><fmt:formatNumber value="${p.minOrderValue}" type="currency" currencySymbol="" maxFractionDigits="0"/> VND</td>
                    <td><fmt:formatNumber value="${p.discountPercent}" type="number" maxFractionDigits="0"/>%</td>
                    <td style="text-align: center;">${p.quantity}</td>
                    <td><fmt:formatDate value="${p.startDate}" pattern="dd/MM/yyyy"/></td>
                    <td><fmt:formatDate value="${p.endDate}" pattern="dd/MM/yyyy"/></td>

                    <td style="text-align: center;">
                        <span class="status-text ${p.state == 'active' ? 'active' : 'inactive'}">
                                ${p.state == 'active' ? 'Active' : 'Inactive'}
                        </span>
                    </td>

                    <td>
                        <button type="button" class="btn-action btn-edit"
                                data-id="${p.id}"
                                data-code="${p.code}"
                                data-desc="${p.description}"
                                data-min="<fmt:formatNumber value='${p.minOrderValue}' pattern='#' />"
                                data-discount="<fmt:formatNumber value='${p.discountPercent}' pattern='#' />"
                                data-quantity="${p.quantity}"
                                data-start="<fmt:formatDate value='${p.startDate}' pattern='yyyy-MM-dd'/>"
                                data-end="<fmt:formatDate value='${p.endDate}' pattern='yyyy-MM-dd'/>"
                                data-state="${p.state}">
                            <i class="fa-solid fa-pen"></i>
                        </button>

                        <a href="${pageContext.request.contextPath}/admin/promotion/delete?id=${p.id}"
                           class="btn-action btn-delete"
                           onclick="return confirm('Bạn có chắc chắn muốn xóa mã này không? Nếu mã đã được sử dụng, hệ thống sẽ chỉ tắt kích hoạt.');">
                            <i class="fa-solid fa-trash"></i>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<div class="form-add" id="form-promotion" style="display: none">
    <div class="form-title">
        <p id="popup-title">THÊM MÃ KHUYẾN MÃI</p>
        <button id="take-off">X</button>
    </div>

    <form class="main-form" id="main-form" method="POST" data-context="${pageContext.request.contextPath}/admin/promotion">
        <input type="hidden" name="id" id="input-id" value="">

        <div class="p name-p">
            <label>Mã Code</label>
            <input type="text" name="code" id="input-code" required>
        </div>

        <div class="price-p">
            <label>Mô tả</label>
            <textarea name="description" id="input-description" style="width: 60%; height: 50px; padding: 5px"></textarea>
        </div>

        <div class="price-p">
            <label>Đơn tối thiểu</label>
            <input type="number" name="minOrderValue" id="input-min" required>
        </div>

        <div class="price-p">
            <label>Mức giảm (%)</label>
            <input type="number" name="discountPercent" id="input-discount" required>
        </div>

        <div class="count-p">
            <label>Số lượng</label>
            <input type="number" name="quantity" id="input-quantity" required>
        </div>

        <div class="count-p">
            <label>Ngày bắt đầu</label>
            <input type="date" name="startDate" id="input-start" required>
        </div>

        <div class="count-p">
            <label>Hạn sử dụng</label>
            <input type="date" name="endDate" id="input-end" required>
        </div>

        <div class="type-p">
            <label>Trạng thái</label>
            <select name="state" id="input-state">
                <option value="active">Active (Hoạt động)</option>
                <option value="inactive">Inactive (Ngừng)</option>
            </select>
        </div>

        <button class="submit" type="submit">Xác nhận</button>

    </form>
</div>

<button class="slide-top" id="slide-top"><i class="fas fa-angle-up"></i></button>
<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/adminPage8.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>