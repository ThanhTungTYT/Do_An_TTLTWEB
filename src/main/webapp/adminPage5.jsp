<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chăm sóc khách hàng</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/adminPage5.css">
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
            <a href="${pageContext.request.contextPath}/admin/contact" class="menu-item active">Chăm sóc khách hàng</a>
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
        <p>CHĂM SÓC KHÁCH HÀNG</p>
    </div>

    <div class="main-content">
        <c:if test="${not empty sessionScope.mailSuccess}">
            <div class="alert alert-success"><i class="fa-solid fa-circle-check"></i> ${sessionScope.mailSuccess}</div>
            <c:remove var="mailSuccess" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.mailError}">
            <div class="alert alert-error"><i class="fa-solid fa-circle-xmark"></i> ${sessionScope.mailError}</div>
            <c:remove var="mailError" scope="session"/>
        </c:if>

        <form class="main-menu-date" action="${pageContext.request.contextPath}/admin/contact" method="get">
            <div class="start">
                <label>Từ ngày</label>
                <input type="date" name="startDate" value="${startDate}">
            </div>
            <div class="end">
                <label>Đến ngày</label>
                <input type="date" name="endDate" value="${endDate}">
            </div>
            <div class="action-buttons">
                <button type="submit">Lọc</button>
                <a href="${pageContext.request.contextPath}/admin/contact" class="btn-reset">Đặt lại</a>
            </div>
        </form>

        <div class="contact">
            <div class="contact-header">
                <h3 class="contact-title">DANH SÁCH LIÊN HỆ</h3>
                <button class="btn-delete-selected" id="btn-delete-selected" onclick="deleteSelected()" style="display:none;">
                    <i class="fa-solid fa-trash"></i> Xóa đã chọn (<span id="selected-count">0</span>)
                </button>
            </div>

            <form id="bulk-delete-form" action="${pageContext.request.contextPath}/admin/contact/delete-bulk" method="post">
                <input type="hidden" name="page" value="${currentPage}">
                <input type="hidden" name="startDate" value="${startDate}">
                <input type="hidden" name="endDate" value="${endDate}">

                <table>
                    <thead>
                    <tr>
                        <th style="width: 4%">
                            <input type="checkbox" id="check-all" title="Chọn tất cả" onchange="toggleAll(this)">
                        </th>
                        <th style="width: 5%">ID</th>
                        <th style="width: 14%">Ngày gửi</th>
                        <th style="width: 18%">Họ và tên</th>
                        <th style="width: 23%">Email</th>
                        <th style="width: 21%">Nội dung (Rút gọn)</th>
                        <th style="width: 15%">Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${contactList}" var="c">
                        <tr id="row-${c.id}">
                            <td>
                                <input type="checkbox" class="row-check" name="ids" value="${c.id}" onchange="updateDeleteBtn()">
                            </td>
                            <td>#${c.id}</td>
                            <td><fmt:formatDate value="${c.sent_at}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td>${c.full_name}</td>
                            <td>${c.email}</td>
                            <td style="text-align: left; padding-left: 20px;">
                                <c:choose>
                                    <c:when test="${c.message.length() > 30}">
                                        ${c.message.substring(0, 30)}...
                                    </c:when>
                                    <c:otherwise>
                                        ${c.message}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <button type="button" class="btn-action detail"
                                        data-id="${c.id}"
                                        data-name="${c.full_name}"
                                        data-email="${c.email}"
                                        data-message="${c.message}"
                                        title="Xem chi tiết">
                                    <i class="fa-solid fa-message"></i>
                                </button>
                                <button type="button" class="btn-action btn-reply"
                                        onclick="openReply(${c.id}, '${c.full_name}', '${c.email}')"
                                        title="Phản hồi">
                                    <i class="fa-solid fa-reply"></i>
                                </button>
                                <button type="button" class="btn-action btn-delete-single"
                                        onclick="deleteSingle(${c.id})"
                                        title="Xóa liên hệ">
                                    <i class="fa-solid fa-trash"></i>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty contactList}">
                        <tr>
                            <td colspan="7">Chưa có liên hệ nào hoặc không tìm thấy kết quả phù hợp.</td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </form>

            <div class="pagination">
                <a href="${currentPage > 1 ? pageContext.request.contextPath : ''}${currentPage > 1 ? '/admin/contact?page=' : '#'}${currentPage > 1 ? currentPage - 1 : ''}${currentPage > 1 ? '&startDate=' : ''}${currentPage > 1 ? startDate : ''}${currentPage > 1 ? '&endDate=' : ''}${currentPage > 1 ? endDate : ''}"
                   class="${currentPage <= 1 ? 'disabled' : ''}">
                    <i class="fa-solid fa-chevron-left"></i>
                </a>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="${pageContext.request.contextPath}/admin/contact?page=${i}&startDate=${startDate}&endDate=${endDate}"
                       class="${currentPage == i ? 'active' : ''}">${i}</a>
                </c:forEach>
                <a href="${currentPage < totalPages ? pageContext.request.contextPath : ''}${currentPage < totalPages ? '/admin/contact?page=' : '#'}${currentPage < totalPages ? currentPage + 1 : ''}${currentPage < totalPages ? '&startDate=' : ''}${currentPage < totalPages ? startDate : ''}${currentPage < totalPages ? '&endDate=' : ''}${currentPage < totalPages ? endDate : ''}"
                   class="${currentPage >= totalPages ? 'disabled' : ''}">
                    <i class="fa-solid fa-chevron-right"></i>
                </a>
            </div>
        </div>
    </div>
</div>

<div class="detail-p" id="detail-p">
    <button id="close" title="Đóng"><i class="fa-solid fa-xmark"></i></button>
    <div class="detail-popup-header">
        <i class="fa-solid fa-envelope-open-text detail-icon"></i>
        <h3>Chi tiết liên hệ</h3>
    </div>

    <div class="detail-info-grid">
        <div class="detail-info-item">
            <span class="detail-label"><i class="fa-solid fa-user"></i> Tên khách hàng</span>
            <span class="detail-value" id="d-name"></span>
        </div>
        <div class="detail-info-item">
            <span class="detail-label"><i class="fa-solid fa-envelope"></i> Email</span>
            <span class="detail-value" id="d-email"></span>
        </div>
    </div>

    <div class="detail-message-box">
        <div class="detail-message-label">
            <i class="fa-solid fa-comment-dots"></i> Nội dung tin nhắn
        </div>
        <p id="d-msg"></p>
    </div>

    <div class="detail-actions">
        <button class="btn-reply-from-detail" onclick="openReplyFromDetail()">
            <i class="fa-solid fa-reply"></i> Phản hồi ngay
        </button>
    </div>
</div>

<div class="form-add" id="form-reply">
    <div class="form-title">
        Gửi phản hồi
        <button id="close-reply" type="button">X</button>
    </div>

    <form class="main-form" id="reply-form" action="${pageContext.request.contextPath}/admin-send-mail" method="post">
        <input type="hidden" id="r-contact-id" name="contactId">
        <input type="hidden" name="page" value="${currentPage}">
        <input type="hidden" name="startDate" value="${startDate}">
        <input type="hidden" name="endDate" value="${endDate}">

        <div>
            <label>Người nhận:</label>
            <input type="email" id="r-email" name="toEmail" readonly style="background-color: #eee;">
        </div>
        <div>
            <label>Tên khách:</label>
            <input type="text" id="r-name" name="toName" readonly style="background-color: #eee;">
        </div>
        <div>
            <label>Tiêu đề:</label>
            <input type="text" name="subject" value="Phản hồi từ Aroma Café" required>
        </div>
        <div>
            <label>Nội dung:</label>
            <textarea name="content" required placeholder="Nhập nội dung phản hồi..."></textarea>
        </div>

        <div id="reply-sending" style="display:none; text-align:center; padding: 10px 0; color: #c76739; font-weight: 500;">
            <i class="fa-solid fa-spinner fa-spin"></i> Đang gửi email, vui lòng đợi...
        </div>

        <button class="submit" type="submit" id="btn-send-reply">Gửi Email</button>
    </form>
</div>

<form id="delete-single-form" action="${pageContext.request.contextPath}/admin/contact/delete" method="post" style="display:none;">
    <input type="hidden" name="id" id="delete-single-id">
    <input type="hidden" name="page" value="${currentPage}">
    <input type="hidden" name="startDate" value="${startDate}">
    <input type="hidden" name="endDate" value="${endDate}">
</form>

<button class="slide-top" id="slide-top"><i class="fas fa-angle-up"></i></button>

<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/adminPage5.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>