<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quản lí banner</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/adminPage7.css">
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
            <a href="${pageContext.request.contextPath}/admin/banner" class="menu-item active">Quản lí banner</a>
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
        <p>QUẢN LÍ BANNER</p>
    </div>

    <div class="main-menu">
        <button id="add">+ Thêm banner</button>
    </div>

    <div class="list-banner">
        <h3>DANH SÁCH BANNER</h3>
        <table>
            <thead>
            <tr>
                <th>Hình ảnh</th>
                <th>Trạng thái</th>
                <th>Ngày bắt đầu</th>
                <th>Ngày kết thúc</th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${listBanner}" var="b">
                <c:set var="startDt"><fmt:formatDate value="${b.start_date}" pattern="yyyy-MM-dd'T'HH:mm"/></c:set>
                <c:set var="endDt"><fmt:formatDate value="${b.end_date}" pattern="yyyy-MM-dd'T'HH:mm"/></c:set>
                <tr>
                    <td><img src="${b.banner_url}" width="200"></td>
                    <td>
                        <span class="status-text ${b.status == 'active' ? 'active' : 'inactive'}">
                                ${b.status == 'active' ? 'Active' : 'Inactive'}
                        </span>
                    </td>
                    <td><fmt:formatDate value="${b.start_date}" pattern="dd/MM/yyyy HH:mm"/></td>
                    <td><fmt:formatDate value="${b.end_date}" pattern="dd/MM/yyyy HH:mm"/></td>
                    <td>
                        <button class="remake"
                                data-bid="${b.id}"
                                data-url="${b.banner_url}"
                                data-status="${b.status}"
                                data-start="${startDt}"
                                data-end="${endDt}">
                            <i class="fa-solid fa-pen"></i>
                        </button>
                    </td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/admin/banner">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="bid" value="${b.id}">
                            <input type="hidden" name="page" value="${currentPage}">
                            <button type="submit" class="delete-banner"><i class="fa-solid fa-trash"></i></button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="pagination">
            <a href="${currentPage > 1 ? pageContext.request.contextPath : ''}${currentPage > 1 ? '/admin/banner?page=' : '#'}${currentPage > 1 ? currentPage - 1 : ''}${currentPage > 1 ? '&startDate=' : ''}${currentPage > 1 ? startDate : ''}${currentPage > 1 ? '&endDate=' : ''}${currentPage > 1 ? endDate : ''}"
               class="${currentPage <= 1 ? 'disabled' : ''}">
                <i class="fa-solid fa-chevron-left"></i>
            </a>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="${pageContext.request.contextPath}/admin/banner?page=${i}&startDate=${startDate}&endDate=${endDate}"
                   class="${currentPage == i ? 'active' : ''}">
                        ${i}
                </a>
            </c:forEach>

            <a href="${currentPage < totalPages ? pageContext.request.contextPath : ''}${currentPage < totalPages ? '/admin/banner?page=' : '#'}${currentPage < totalPages ? currentPage + 1 : ''}${currentPage < totalPages ? '&startDate=' : ''}${currentPage < totalPages ? startDate : ''}${currentPage < totalPages ? '&endDate=' : ''}${currentPage < totalPages ? endDate : ''}"
               class="${currentPage >= totalPages ? 'disabled' : ''}">
                <i class="fa-solid fa-chevron-right"></i>
            </a>
        </div>
    </div>
    </div>
    </div>
</div>

<div class="form-add-custom" id="form-add">
    <div class="form-title-custom">
        <p>THÊM BANNER MỚI</p>
        <button id="take-off" class="btn-close">✕</button>
    </div>

    <form class="main-form-custom" method="post" action="${pageContext.request.contextPath}/admin/banner">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="page" value="${currentPage}">

        <div>
            <p class="form-section-title">Thông tin hình ảnh</p>
            <div class="input-group-vertical">
                <div class="input-field">
                    <label>URL Banner <span style="color:red">*</span></label>
                    <input type="text" name="banner_url" placeholder="Nhập link ảnh banner" required>
                </div>
                <div class="input-field">
                    <label>Trạng thái</label>
                    <select name="status" required>
                        <option value="" disabled selected>-- Chọn trạng thái --</option>
                        <option value="active">Active</option>
                        <option value="inactive">Inactive</option>
                    </select>
                </div>
            </div>
        </div>

        <div>
            <p class="form-section-title">Thời gian hiển thị</p>
            <div class="input-grid-horizontal">
                <div class="input-field">
                    <label>Ngày bắt đầu</label>
                    <input type="datetime-local" name="start">
                </div>
                <div class="input-field">
                    <label>Ngày kết thúc</label>
                    <input type="datetime-local" name="end">
                </div>
            </div>
        </div>

        <button class="btn-submit-custom" type="submit">+ Thêm Banner</button>
    </form>
</div>

<div class="form-add-custom" id="form-remake">
    <div class="form-title-custom">
        <p>SỬA THÔNG TIN BANNER</p>
        <button id="close-remake" class="btn-close">✕</button>
    </div>

    <form class="main-form-custom" method="post" action="${pageContext.request.contextPath}/admin/banner">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="bid" id="up_bid">
        <input type="hidden" name="page" value="${currentPage}">

        <div>
            <p class="form-section-title">Cập nhật hình ảnh</p>
            <div class="input-group-vertical">
                <div class="input-field">
                    <label>URL Banner</label>
                    <input type="text" name="up_url">
                </div>
                <div class="input-field">
                    <label>Trạng thái</label>
                    <select name="up_status" id="up_status_select" required>
                        <option value="active">Active</option>
                        <option value="inactive">Inactive</option>
                    </select>
                </div>
            </div>
        </div>

        <div>
            <p class="form-section-title">Cập nhật thời gian</p>
            <div class="input-grid-horizontal">
                <div class="input-field">
                    <label>Ngày bắt đầu</label>
                    <input type="datetime-local" name="up_start">
                </div>
                <div class="input-field">
                    <label>Ngày kết thúc</label>
                    <input type="datetime-local" name="up_end">
                </div>
            </div>
        </div>

        <button class="btn-submit-custom" type="submit">Lưu Thay Đổi</button>
    </form>
</div>
<button class="slide-top" id="slide-top"><i class="fas fa-angle-up"></i></button>

<script>
    document.querySelectorAll(".remake").forEach(btn => {
        btn.onclick = function () {
            const data = btn.dataset;

            document.getElementById("up_bid").value = data.bid;
            document.querySelector("input[name='up_url']").value = data.url;
            document.querySelector("input[name='up_start']").value = data.start;
            document.querySelector("input[name='up_end']").value = data.end;

            document.querySelector("select[name='up_status']").value = "";

            document.getElementById("form-remake").display = "block";
        }
    });
    function changePage(page) {
        let urlParams = new URLSearchParams(window.location.search);
        urlParams.set('page', page);
        window.location.search = urlParams.toString();
    }
</script>
<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>