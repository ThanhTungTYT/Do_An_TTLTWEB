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
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="menu-item">Tổng quan</a>
        </c:if>

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
        <div class="pagination">
            <a href="${currentPage > 1 ? pageContext.request.contextPath : ''}${currentPage > 1 ? '/admin/users?page=' : '#'}${currentPage > 1 ? currentPage - 1 : ''}${currentPage > 1 ? '&startDate=' : ''}${currentPage > 1 ? startDate : ''}${currentPage > 1 ? '&endDate=' : ''}${currentPage > 1 ? endDate : ''}"
               class="${currentPage <= 1 ? 'disabled' : ''}">
                <i class="fa-solid fa-chevron-left"></i>
            </a>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="${pageContext.request.contextPath}/admin/users?page=${i}&startDate=${startDate}&endDate=${endDate}"
                   class="${currentPage == i ? 'active' : ''}">
                        ${i}
                </a>
            </c:forEach>

            <a href="${currentPage < totalPages ? pageContext.request.contextPath : ''}${currentPage < totalPages ? '/admin/users?page=' : '#'}${currentPage < totalPages ? currentPage + 1 : ''}${currentPage < totalPages ? '&startDate=' : ''}${currentPage < totalPages ? startDate : ''}${currentPage < totalPages ? '&endDate=' : ''}${currentPage < totalPages ? endDate : ''}"
               class="${currentPage >= totalPages ? 'disabled' : ''}">
                <i class="fa-solid fa-chevron-right"></i>
            </a>
        </div>
    </div>
</div>

<div class="form-add" id="form-add" style="display: none; width: 500px; max-height: 90vh; overflow-y: auto; padding: 0; border-radius: 12px;">
    <div class="form-title" style="position: sticky; top: 0; background: #c76739; color: white; padding: 15px 20px; display: flex; justify-content: space-between; align-items: center; border-radius: 12px 12px 0 0; z-index: 1;">
        <p style="margin: 0; font-size: 1.1em; font-weight: bold;">THÊM TÀI KHOẢN</p>
        <button id="take-off" style="background: transparent; border: none; color: white; font-size: 1.3em; cursor: pointer;">✕</button>
    </div>

    <form method="post" action="${pageContext.request.contextPath}/add-user"
          style="padding: 20px; display: flex; flex-direction: column; gap: 20px;">
        <div>
            <p style="font-weight: bold; margin-bottom: 10px; color: #333; border-bottom: 2px solid #c76739; padding-bottom: 6px;">
                Thông tin tài khoản
            </p>
            <div style="display: flex; flex-direction: column; gap: 12px;">
                <div style="display: flex; flex-direction: column; gap: 5px;">
                    <label style="font-size: 0.88em; font-weight: 600; color: #555;">Tên người dùng</label>
                    <input type="text" name="name" placeholder="Nhập họ và tên"
                           style="padding: 10px 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 0.9em; background: #f9f9f9; outline: none;"
                           onfocus="this.style.borderColor='#c76739'" onblur="this.style.borderColor='#ddd'">
                </div>
                <div style="display: flex; flex-direction: column; gap: 5px;">
                    <label style="font-size: 0.88em; font-weight: 600; color: #555;">Email <span style="color:red">*</span></label>
                    <input type="email" name="email" placeholder="Nhập email" required
                           style="padding: 10px 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 0.9em; background: #f9f9f9; outline: none;"
                           onfocus="this.style.borderColor='#c76739'" onblur="this.style.borderColor='#ddd'">
                </div>
                <div style="display: flex; flex-direction: column; gap: 5px;">
                    <label style="font-size: 0.88em; font-weight: 600; color: #555;">Mật khẩu <span style="color:red">*</span></label>
                    <input type="password" name="pass" placeholder="Nhập mật khẩu" required
                           style="padding: 10px 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 0.9em; background: #f9f9f9; outline: none;"
                           onfocus="this.style.borderColor='#c76739'" onblur="this.style.borderColor='#ddd'">
                </div>
            </div>
        </div>
        <div>
            <p style="font-weight: bold; margin-bottom: 10px; color: #333; border-bottom: 2px solid #c76739; padding-bottom: 6px;">
                Phân quyền
            </p>
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 8px;">
                <c:forEach items="${allPermissions}" var="p">
                    <label style="display: flex; align-items: center; gap: 8px; padding: 8px 10px; background: #f9f9f9; border-radius: 8px; border: 1px solid #eee; cursor: pointer;"
                           onmouseover="this.style.background='#fff3ed'" onmouseout="this.style.background='#f9f9f9'">
                        <c:choose>
                            <c:when test="${p.permission_key eq 'shopping'}">
                                <input type="checkbox" name="permissions"
                                       value="${p.id}" checked disabled
                                       style="accent-color: #c76739; width: 16px; height: 16px; flex-shrink: 0;">
                                <input type="hidden" name="permissions" value="${p.id}">
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" name="permissions"
                                       value="${p.id}"
                                       style="accent-color: #c76739; width: 16px; height: 16px; flex-shrink: 0;">
                            </c:otherwise>
                        </c:choose>
                        <span style="font-size: 0.88em; color: #444; line-height: 1.3;">${p.permission_name}</span>
                        <c:if test="${p.permission_key eq 'shopping'}">
                            <span style="margin-left: auto; font-size: 0.75em; color: #aaa; white-space: nowrap;">mặc định</span>
                        </c:if>
                    </label>
                </c:forEach>
            </div>
        </div>
        <button type="submit"
                style="background: #c76739; color: white; border: none; padding: 12px; border-radius: 8px; font-size: 1em; font-weight: bold; cursor: pointer;"
                onmouseover="this.style.background='#a85530'" onmouseout="this.style.background='#c76739'">
            + Thêm tài khoản
        </button>
    </form>
</div>

<div class="form-add" id="form-remake" style="display: none; width: 500px; max-height: 90vh; overflow-y: auto; padding: 0; border-radius: 12px;">
    <div class="form-title" style="position: sticky; top: 0; background: #c76739; color: white; padding: 15px 20px; display: flex; justify-content: space-between; align-items: center; border-radius: 12px 12px 0 0; z-index: 1;">
        <p style="margin: 0; font-size: 1.1em; font-weight: bold;">SỬA ACCOUNT & PHÂN QUYỀN</p>
        <button id="close-remake" style="background: transparent; border: none; color: white; font-size: 1.3em; cursor: pointer;">✕</button>
    </div>

    <form class="main-form" method="post" action="${pageContext.request.contextPath}/update-user" style="padding: 20px; display: flex; flex-direction: column; gap: 20px;">
        <input type="hidden" name="uid" id="up_uid">
        <div>
            <p style="font-weight: bold; margin-bottom: 10px; color: #333; border-bottom: 2px solid #c76739; padding-bottom: 6px;">
                Phân quyền
            </p>
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 8px;">
                <c:forEach items="${allPermissions}" var="p">
                    <label style="display: flex; align-items: center; gap: 8px; padding: 8px 10px; background: #f9f9f9; border-radius: 8px; border: 1px solid #eee; cursor: pointer;"
                           onmouseover="this.style.background='#fff3ed'" onmouseout="this.style.background='#f9f9f9'">
                        <c:choose>
                            <c:when test="${p.permission_key eq 'shopping'}">
                                <input type="checkbox" name="up_permissions"
                                       value="${p.id}" data-key="${p.permission_key}"
                                       checked disabled
                                       style="accent-color: #c76739; width: 16px; height: 16px; flex-shrink: 0;">
                                <input type="hidden" name="up_permissions" value="${p.id}">
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" name="up_permissions"
                                       value="${p.id}" data-key="${p.permission_key}"
                                       style="accent-color: #c76739; width: 16px; height: 16px; flex-shrink: 0;">
                            </c:otherwise>
                        </c:choose>
                        <span style="font-size: 0.88em; color: #444; line-height: 1.3;">${p.permission_name}</span>
                        <c:if test="${p.permission_key eq 'shopping'}">
                            <span style="margin-left: auto; font-size: 0.75em; color: #aaa; white-space: nowrap;">mặc định</span>
                        </c:if>
                    </label>
                </c:forEach>
            </div>
        </div>
        <div>
            <p style="font-weight: bold; margin-bottom: 10px; color: #333; border-bottom: 2px solid #c76739; padding-bottom: 6px;">
                Role chính
            </p>
            <select name="up_role" style="width: 100%; padding: 10px 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 0.95em; background: #f9f9f9; cursor: pointer;">
                <option value="admin">Admin</option>
                <option value="customer">Customer</option>
            </select>
        </div>
        <button type="submit"
                style="background: #c76739; color: white; border: none; padding: 12px; border-radius: 8px; font-size: 1em; font-weight: bold; cursor: pointer;"
                onmouseover="this.style.background='#a85530'" onmouseout="this.style.background='#c76739'">
            Lưu Thay Đổi
        </button>
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
    function changePage(page) {
        let urlParams = new URLSearchParams(window.location.search);
        urlParams.set('page', page);
        window.location.search = urlParams.toString();
    }

    document.getElementById("close-remake").onclick = () => document.getElementById("form-remake").style.display = "none";
    document.getElementById("add").onclick = () => document.getElementById("form-add").style.display = "block";
    document.getElementById("take-off").onclick = () => document.getElementById("form-add").style.display = "none";
</script>
<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
</body>
</html>