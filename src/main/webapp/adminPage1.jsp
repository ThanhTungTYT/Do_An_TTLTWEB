
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Trang Quản Trị Aroma Café</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/adminPage1.css?v=3">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/toast.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/toast.jsp"/>
<div class="left-menu" id="left-menu">
    <div class="logo">
        <img src="${pageContext.request.contextPath}/assets/img/logo.png" onclick="location.href='${pageContext.request.contextPath}/'" width="300px" height="100px">
    </div>
    <div class="menu">
        <c:if test="${sessionScope.user.hasPermission('view_dashboard')}">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="menu-item active">Tổng quan</a>
        </c:if>

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
        <p>TỔNG QUAN</p>
    </div>
    <div class="top-bar">
        <form method="post" action="${pageContext.request.contextPath}/admin/dashboard" class="main-menu-date">
            <div class="start">
                <label>Từ ngày</label>
                <input type="date" name="startDate" value="${startDate}">
            </div>
            <div class="end">
                <label>Đến ngày</label>
                <input type="date" name="endDate" value="${endDate}">
            </div>
            <button>Xác nhận</button>
        </form>

        <div class="filter-section">
            <form method="get" action="${pageContext.request.contextPath}/admin/dashboard">
                <select name="filter" onchange="this.form.submit()">
                    <option value="all" ${filter == 'all' ? 'selected' : ''}>Tất cả</option>
                    <option value="today" ${filter == 'today' ? 'selected' : ''}>Hôm nay</option>
                    <option value="week" ${filter == 'week' ? 'selected' : ''}>7 ngày</option>
                    <option value="month" ${filter == 'month' ? 'selected' : ''}>30 ngày</option>
                    <option value="quarter" ${filter == 'quarter' ? 'selected' : ''}>1 quý</option>
                </select>
            </form>
            <button class="reset">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="reset-link">Đặt lại</a>
            </button>
        </div>
    </div>

    <div class="kpi-grid">

            <div class="kpi-card">
                <div class="card-icon blue"><i class="fa-solid fa-chart-line"></i></div>
                <div class="card-info">
                    <h3>
                    <fmt:formatNumber value="${totalRevenue}" type="number" groupingUsed="true"/> VND
                   </h3>
                    <span>Tổng doanh thu</span>
                </div>
            </div>

            <div class="kpi-card">
                <div class="card-icon green"><i class="fa-solid fa-box"></i></div>
                <div class="card-info">
                    <h3>${totalOrders}</h3>
                    <span>Đơn hàng</span>
                </div>
            </div>

            <div class="kpi-card">
                <div class="card-icon orange"><i class="fa-solid fa-clock"></i></div>
                <div class="card-info">
                    <h3>${pendingOrders}</h3>
                    <span>Đơn chờ xử lý</span>
                </div>
            </div>

            <div class="kpi-card">
                <div class="card-icon red"><i class="fa-solid fa-user-plus"></i></div>
                <div class="card-info">
                    <h3>${newCustomers}</h3>
                    <span>Khách mới</span>
                </div>
            </div>

        </div>
    <div class="product-stats-container">

        <div class="table-card margin-zero">
            <h3 class="title-top-products">
                <i class="fa-solid fa-fire icon-margin"></i>Top 10 sản phẩm bán chạy
            </h3>
            <table>
                <thead>
                <tr>
                    <th class="col-id">Mã SP</th>
                    <th class="col-name">Tên sản phẩm</th>
                    <th class="col-qty">Số lượng bán</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${topProducts}" var="row">
                    <tr>
                        <td class="text-center">#${row.product.id}</td>
                        <td class="text-left font-bold">${row.product.name}</td>
                        <td class="text-center">
                        <span class="status completed badge-qty">
                                ${row.totalSold}
                        </span>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty topProducts}">
                    <tr>
                        <td colspan="3" class="text-center">Không có dữ liệu</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>

        <div class="table-card margin-zero">
            <h3 class="title-worst-products">
                <i class="fa-solid fa-snowflake icon-margin"></i>Top 10 sản phẩm bán ế
            </h3>
            <table>
                <thead>
                <tr>
                    <th class="col-id">Mã SP</th>
                    <th class="col-name">Tên sản phẩm</th>
                    <th class="col-days">Thời gian tồn</th>
                    <th class="col-qty">Số lượng bán</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${worstProducts}" var="row">
                    <tr>
                        <td class="text-center">#${row.product.id}</td>
                        <td class="text-left font-bold">${row.product.name}</td>
                        <td class="text-center text-muted font-small">${row.daysInStock} ngày</td>
                        <td class="text-center">
                        <span class="status cancelled badge-qty">
                                ${row.totalSold}
                        </span>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty worstProducts}">
                    <tr>
                        <td colspan="4" class="text-center">Không có dữ liệu</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>

    </div>
    <div class="table-card">
            <h3>Đơn hàng</h3>

            <table>
                <thead>
                <tr>
                    <th>Mã ĐH</th>
                    <th>Người nhận</th>
                    <th>Ngày đặt</th>
                    <th>Trạng thái</th>
                    <th>Tổng tiền</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach items="${orders}" var="o">
                    <tr>
                        <td>#${o.id}</td>
                        <td>${o.receiverName}</td>
                        <td>${o.createdAt}</td>
                        <td>
                        <span class="status ${o.status}">
                                <c:choose>
                                    <c:when test="${o.status == 'Đang xử lý'}">Đang xử lý</c:when>
                                    <c:when test="${o.status == 'Chờ thanh toán'}">Chờ thanh toán</c:when>
                                    <c:when test="${o.status == 'Đang giao'}">Đang giao</c:when>
                                    <c:when test="${o.status == 'Yêu cầu hủy'}">Yêu cầu hủy</c:when>
                                    <c:when test="${o.status == 'Đã giao'}">Đã giao</c:when>
                                    <c:when test="${o.status == 'Đã hủy'}">Đã huỷ</c:when>
                                </c:choose>
                            </span>
                        </td>
                        <td><fmt:formatNumber value="${o.finalAmount} " type="number" groupingUsed="true"/> VND</td>
                    </tr>
                </c:forEach>

                <c:if test="${empty orders}">
                    <tr>
                        <td colspan="5" style="text-align:center">Không có đơn hàng</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
</div>
<button class="slide-top" id="slide-top"><i class="fas fa-angle-up"></i></button>
<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>

</body>
</html>
