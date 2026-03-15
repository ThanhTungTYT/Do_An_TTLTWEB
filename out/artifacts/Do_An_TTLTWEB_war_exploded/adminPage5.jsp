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
        <a href="${pageContext.request.contextPath}/admin/reviews" class="menu-item">Quản lí đánh giá</a>
        <a href="${pageContext.request.contextPath}/admin/banner" class="menu-item">Quản lí banner</a>
        <a href="${pageContext.request.contextPath}/admin/promotion" class="menu-item">Quản lí mã giảm giá</a>
        <a href="${pageContext.request.contextPath}/admin/contact" class="menu-item active">Chăm sóc khách hàng</a>
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
            <h3 class="contact-title">DANH SÁCH LIÊN HỆ</h3>
            <table>
                <thead>
                <tr>
                    <th style="width: 5%">ID</th>
                    <th style="width: 15%">Ngày gửi</th>
                    <th style="width: 20%">Họ và tên</th>
                    <th style="width: 25%">Email</th>
                    <th style="width: 20%">Nội dung (Rút gọn)</th>
                    <th style="width: 15%">Thao tác</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach items="${contactList}" var="c">
                    <tr>
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
                            <button class="detail" onclick="openDetail('${c.full_name}', '${c.email}', `${c.message}`)" title="Xem chi tiết">
                                <i class="fa-solid fa-message"></i>
                            </button>

                            <button class="btn-reply" onclick="openReply('${c.full_name}', '${c.email}')" title="Phản hồi">
                                <i class="fa-solid fa-reply"></i>
                            </button>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty contactList}">
                    <tr>
                        <td colspan="6">Chưa có liên hệ nào hoặc không tìm thấy kết quả phù hợp.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>

            <div class="pagination">
                <%-- Nút Previous --%>
                <a href="${currentPage > 1 ? pageContext.request.contextPath : ''}${currentPage > 1 ? '/admin/contact?page=' : '#'}${currentPage > 1 ? currentPage - 1 : ''}${currentPage > 1 ? '&startDate=' : ''}${currentPage > 1 ? startDate : ''}${currentPage > 1 ? '&endDate=' : ''}${currentPage > 1 ? endDate : ''}"
                   class="${currentPage <= 1 ? 'disabled' : ''}">
                    <i class="fa-solid fa-chevron-left"></i>
                </a>

                <%-- Số trang --%>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="${pageContext.request.contextPath}/admin/contact?page=${i}&startDate=${startDate}&endDate=${endDate}"
                       class="${currentPage == i ? 'active' : ''}">
                            ${i}
                    </a>
                </c:forEach>

                <%-- Nút Next --%>
                <a href="${currentPage < totalPages ? pageContext.request.contextPath : ''}${currentPage < totalPages ? '/admin/contact?page=' : '#'}${currentPage < totalPages ? currentPage + 1 : ''}${currentPage < totalPages ? '&startDate=' : ''}${currentPage < totalPages ? startDate : ''}${currentPage < totalPages ? '&endDate=' : ''}${currentPage < totalPages ? endDate : ''}"
                   class="${currentPage >= totalPages ? 'disabled' : ''}">
                    <i class="fa-solid fa-chevron-right"></i>
                </a>
            </div>
        </div>
    </div>
</div>

<div class="detail-p" id="detail-p">
    <button id="close" onclick="closePopup('detail-p')">X</button>
    <h3>Chi tiết tin nhắn</h3>

    <div class="info-customer">
        <div class="info" style="display: block;"> <div style="margin-bottom: 10px;"><b>Tên khách hàng:</b> <span id="d-name"></span></div>
            <div style="margin-bottom: 10px;"><b>Email:</b> <span id="d-email"></span></div>
        </div>
    </div>

    <div class="detail-content" style="margin-top: 20px; background: #f9f9f9; padding: 15px; border-radius: 8px;">
        <span style="font-weight: bold; display: block; margin-bottom: 10px;">Nội dung chi tiết:</span>
        <p id="d-msg" style="word-wrap: break-word;"></p>
    </div>
</div>

<div class="form-add" id="form-reply">
    <div class="form-title">
        Gửi phản hồi
        <button onclick="closePopup('form-reply')">X</button>
    </div>

    <form class="main-form" action="${pageContext.request.contextPath}/admin-send-mail" method="post">
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

        <button class="submit" type="submit">Gửi Email</button>
    </form>
</div>

<button class="slide-top" id="slide-top"><i class="fas fa-angle-up"></i></button>

<script>
    function openDetail(name, email, message) {
        document.getElementById('d-name').innerText = name;
        document.getElementById('d-email').innerText = email;
        document.getElementById('d-msg').innerText = message;
        document.getElementById('detail-p').style.display = 'flex';
        document.getElementById('form-reply').style.display = 'none';
        document.getElementById('right-content').style.filter = 'blur(5px)';
    }

    function openReply(name, email) {
        document.getElementById('r-name').value = name;
        document.getElementById('r-email').value = email;
        document.getElementById('form-reply').style.display = 'block';
        document.getElementById('detail-p').style.display = 'none';
        document.getElementById('right-content').style.filter = 'blur(5px)';
    }

    function closePopup(id) {
        document.getElementById(id).style.display = 'none';
        document.getElementById('right-content').style.filter = 'none';
    }
</script>

<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>