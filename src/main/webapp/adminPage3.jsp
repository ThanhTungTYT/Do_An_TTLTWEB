<%--
  Created by IntelliJ IDEA.
  User: TDat
  Date: 27/12/2025
  Time: 15:47
  To change this template use File | Settings | File Templates.
--%>
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
        <a href="${pageContext.request.contextPath}/admin/orders" class="menu-item active">Quản lí đơn hàng</a>
        <a href="${pageContext.request.contextPath}/admin/users" class="menu-item">Quản lí tài khoản</a>
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
        <p>ĐƠN HÀNG</p>
    </div>
    <div class="main-content">
        <form class="search-bar" action="${pageContext.request.contextPath}/admin/orders/search" method="GET">
            <input type="text" name="search" placeholder="Tìm kiếm (Mã ĐH hoặc Tên khách hàng)" value="${searchKeyword}">
            <button type="submit"><i class="fas fa-search"></i></button>
        </form>
        <form class="main-menu-date" action="${pageContext.request.contextPath}/admin/orders" method="get">
            <div class="start">
                <label>Start date</label>
                <input type="date" name="startDate" value="${startDate}">
            </div>
            <div class="end">
                <label>End date</label>
                <input type="date" name="endDate" value="${endDate}">
            </div>

            <div class="action-buttons">
                <button type="submit">Xác nhận</button>
                <a href="${pageContext.request.contextPath}/admin/orders" class="btn-reset">Đặt lại</a>
            </div>
        </form>
        <div class="list-order">
            <h3>DANH SÁCH ĐƠN HÀNG</h3>
            <table>
                <thead>
                <tr>
                    <th></th>
                    <th>ID</th>
                    <th>Khách hàng</th>
                    <th>Ngày đặt</th>
                    <th>Trạng thái</th>
                    <th>Tổng tiền</th>
                    <th>Phương thức thanh toán</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach items="${orders}" var="o">
                    <tr>
                        <td>
                            <button class="detail"
                                    onclick="showDetail(${o.id})">
                                <i class="fa-solid fa-list"></i>
                            </button>
                        </td>

                        <td>#${o.id}</td>

                        <td>
                            <c:out value="${userMap[o.userId]['full_name']}" />
                        </td>

                        <td>
                            <fmt:formatDate value="${o.createdAt}" pattern="dd/MM/yyyy"/>
                        </td>

                        <td>
                            <span class="status ${o.status}">
                                <c:choose>
                                    <c:when test="${o.status == 'Đang xử lý'}">Đang xử lý</c:when>
                                    <c:when test="${o.status == 'Đã giao'}">Đã giao</c:when>
                                    <c:when test="${o.status == 'Đã hủy'}">Đã huỷ</c:when>
                                </c:choose>
                            </span>
                        </td>
                        <td>
                            <fmt:formatNumber value="${o.finalAmount}" type="number"/> VND
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${o.paymentMethodId == 1}">
                                    Thanh toán khi nhận hàng
                                </c:when>
                                <c:otherwise>
                                    Chuyển khoản ngân hàng
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="pagination">
                <%-- Nút Previous --%>
                <a href="${currentPage > 1 ? pageContext.request.contextPath : ''}${currentPage > 1 ? '/admin/orders?page=' : '#'}${currentPage > 1 ? currentPage - 1 : ''}${currentPage > 1 ? '&startDate=' : ''}${currentPage > 1 ? startDate : ''}${currentPage > 1 ? '&endDate=' : ''}${currentPage > 1 ? endDate : ''}"
                   class="${currentPage <= 1 ? 'disabled' : ''}">
                    <i class="fa-solid fa-chevron-left"></i>
                </a>

                <%-- Số trang --%>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="${pageContext.request.contextPath}/admin/orders?page=${i}&startDate=${startDate}&endDate=${endDate}"
                       class="${currentPage == i ? 'active' : ''}">
                            ${i}
                    </a>
                </c:forEach>

                <%-- Nút Next --%>
                <a href="${currentPage < totalPages ? pageContext.request.contextPath : ''}${currentPage < totalPages ? '/admin/orders?page=' : '#'}${currentPage < totalPages ? currentPage + 1 : ''}${currentPage < totalPages ? '&startDate=' : ''}${currentPage < totalPages ? startDate : ''}${currentPage < totalPages ? '&endDate=' : ''}${currentPage < totalPages ? endDate : ''}"
                   class="${currentPage >= totalPages ? 'disabled' : ''}">
                    <i class="fa-solid fa-chevron-right"></i>
                </a>
            </div>
        </div>
    </div>
</div>
<c:forEach items="${orders}" var="o">
    <div class="detail-p order-detail"
         id="detail-${o.id}"
         style="display:none">

        <button id="close" onclick="closeDetail()">X</button>

        <!-- DANH SÁCH SẢN PHẨM -->
        <div class="order">
            <h3>DANH SÁCH SẢN PHẨM</h3>
            <table>
                <thead>
                <tr>
                    <th>Tên sản phẩm</th>
                    <th>Giá (VND)</th>
                    <th>Số lượng</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${orderItemsMap[o.id]}" var="i">
                    <tr>
                        <td>${i.product.name}</td>
                        <td>
                            <fmt:formatNumber value="${i.price}" type="number"/>đ
                        </td>
                        <td>${i.quantity}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <!-- THÔNG TIN KHÁCH -->
        <div class="info-customer">
            <h3>THÔNG TIN KHÁCH HÀNG</h3>
            <div class="info">
                <div class="info-left">
                    <p>Họ và tên: ${userMap[o.userId].full_name}</p>
                    <p>SĐT: ${o.receiverPhone}</p>
                </div>
            </div>
        </div>

        <!-- TỔNG TIỀN -->
        <div class="totalSum">
            <h3>
                TỔNG TIỀN SẢN PHẨM:
                <span>
                <fmt:formatNumber value="${o.totalAmount}" type="number"/> đ
            </span>
            </h3>
            <h3>
                GIẢM GIÁ (Tổng tiền sản phẩm):
                <span>
                <fmt:formatNumber value="${o.discountPercent * o.totalAmount /100}" type="number"/> đ
            </span>
            </h3>
            <h3>
                PHÍ VẬN CHUYỂN:
                <span>
                <fmt:formatNumber value="${o.shippingFee}" type="number"/> đ
            </span>
            </h3>
            <h3>
                TỔNG TIỀN ĐƠN HÀNG:
                <span>
                <fmt:formatNumber value="${o.finalAmount}" type="number"/> đ
            </span>
            </h3>
        </div>
        <c:if test="${o.status == 'Đang xử lý'}">
            <form action="${pageContext.request.contextPath}/admin/orders" method="post">
                <input type="hidden" name="orderId" value="${o.id}">
                <button type="submit">Xuất hóa đơn</button>
            </form>
        </c:if>
    </div>
</c:forEach>
<button class="slide-top" id="slide-top"><i class="fas fa-angle-up"></i></button>
<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/adminPage3.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>