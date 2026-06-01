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
            </div>

            <form id="state-filter-form" action="${pageContext.request.contextPath}/admin/contact" method="get">
                <input type="hidden" name="startDate" value="${startDate}">
                <input type="hidden" name="endDate" value="${endDate}">
                <input type="hidden" name="page" value="1">
                <div class="state-filter-bar">
                    <label>Lọc theo trạng thái:</label>
                    <select name="state" onchange="document.getElementById('state-filter-form').submit()">
                        <option value=""           ${empty state           ? 'selected' : ''}>Tất cả</option>
                        <option value="PENDING"    ${'PENDING'    == state ? 'selected' : ''}>Chờ xử lý</option>
                        <option value="DONE"       ${'DONE'       == state ? 'selected' : ''}>Đã xử lý</option>
                    </select>
                </div>
            </form>

            <table>
                <thead>
                <tr>
                    <th style="width: 5%">ID</th>
                    <th style="width: 13%">Ngày gửi</th>
                    <th style="width: 17%">Họ và tên</th>
                    <th style="width: 20%">Email</th>
                    <th style="width: 20%">Nội dung (Rút gọn)</th>
                    <th style="width: 12%">Trạng thái</th>
                    <th style="width: 13%">Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${contactList}" var="c">
                    <tr id="row-${c.id}">
                        <td>#${c.id}</td>
                        <td><fmt:formatDate value="${c.sent_at}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td>${c.full_name}</td>
                        <td>${c.email}</td>
                        <td style="text-align: left; padding-left: 12px;">
                            <c:choose>
                                <c:when test="${c.message.length() > 30}">${c.message.substring(0, 30)}...</c:when>
                                <c:otherwise>${c.message}</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${c.state == 'PENDING'}">
                                    <span class="state-badge state-pending">Chờ xử lý</span>
                                </c:when>
                                <c:when test="${c.state == 'DONE'}">
                                    <span class="state-badge state-done">Đã xử lý</span>
                                </c:when>
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

            <div class="pagination">
                <a href="${currentPage > 1
                    ? pageContext.request.contextPath.concat('/admin/contact?page=').concat(String.valueOf(currentPage - 1)).concat('&startDate=').concat(startDate != null ? startDate : '').concat('&endDate=').concat(endDate != null ? endDate : '').concat('&state=').concat(state != null ? state : '')
                    : '#'}"
                   class="${currentPage <= 1 ? 'disabled' : ''}">
                    <i class="fa-solid fa-chevron-left"></i>
                </a>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="${pageContext.request.contextPath}/admin/contact?page=${i}&startDate=${startDate}&endDate=${endDate}&state=${state}"
                       class="${currentPage == i ? 'active' : ''}">${i}</a>
                </c:forEach>
                <a href="${currentPage < totalPages
                    ? pageContext.request.contextPath.concat('/admin/contact?page=').concat(String.valueOf(currentPage + 1)).concat('&startDate=').concat(startDate != null ? startDate : '').concat('&endDate=').concat(endDate != null ? endDate : '').concat('&state=').concat(state != null ? state : '')
                    : '#'}"
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

<div class="reply-popup" id="form-reply">
    <form id="reply-form" action="${pageContext.request.contextPath}/admin-send-mail" method="post">
        <input type="hidden" id="r-contact-id" name="contactId">
        <input type="hidden" name="page" value="${currentPage}">
        <input type="hidden" name="startDate" value="${startDate}">
        <input type="hidden" name="endDate" value="${endDate}">
        <input type="hidden" name="state" value="${state}">

        <div class="reply-popup-header">
            <i class="fa-solid fa-paper-plane reply-icon"></i>
            <h3>Gửi phản hồi</h3>
            <button type="button" id="close-reply" class="reply-close-btn" title="Đóng">
                <i class="fa-solid fa-xmark"></i>
            </button>
        </div>

        <div class="reply-info-grid">
            <div class="reply-info-item">
                <span class="detail-label"><i class="fa-solid fa-user"></i> Tên khách hàng</span>
                <span class="detail-value" id="r-name-display"></span>
                <input type="hidden" id="r-name" name="toName">
            </div>
            <div class="reply-info-item">
                <span class="detail-label"><i class="fa-solid fa-envelope"></i> Email người nhận</span>
                <span class="detail-value" id="r-email-display"></span>
                <input type="hidden" id="r-email" name="toEmail">
            </div>
        </div>

        <div class="reply-field">
            <span class="detail-label"><i class="fa-solid fa-heading"></i> Tiêu đề</span>
            <input class="reply-input" type="text" name="subject"
                   value="Phản hồi từ Aroma Café" required>
        </div>

        <div class="reply-field">
            <span class="detail-label"><i class="fa-solid fa-comment-dots"></i> Nội dung phản hồi</span>
            <textarea class="reply-textarea" id="reply-content" name="content"
                      required placeholder="Nhập nội dung phản hồi..."
                      oninput="autoResize(this)"></textarea>
        </div>

        <div id="reply-sending" style="display:none; text-align:center; padding: 8px 0; color:#c76739; font-weight:500;">
            <i class="fa-solid fa-spinner fa-spin"></i> Đang gửi email, vui lòng đợi...
        </div>

        <div class="reply-actions">
            <button class="btn-send-mail" type="submit" id="btn-send-reply">
                <i class="fa-solid fa-paper-plane"></i> Gửi Email
            </button>
        </div>
    </form>
</div>


<button class="slide-top" id="slide-top"><i class="fas fa-angle-up"></i></button>

<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/adminPage5.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>