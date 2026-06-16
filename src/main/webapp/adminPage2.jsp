<%--
  Created by IntelliJ IDEA.
  User: TDat
  Date: 27/12/2025
  Time: 8:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quản lí sản phẩm</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/adminPage2.css">
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
            <a href="${pageContext.request.contextPath}/admin/products" class="menu-item active">Quản lí sản phẩm</a>
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
        <p>QUẢN LÍ SẢN PHẨM</p>
    </div>
    <form class="search-bar" action="${pageContext.request.contextPath}/admin/products/search" method="GET">
        <input type="text" name="search" placeholder="Tìm kiếm (id hoặc tên sản phẩm)" value="${searchKeyword}">
        <button type="submit"><i class="fas fa-search"></i></button>
    </form>
    <div class="main-menu">
        <select onchange="changeCid(this.value)" style="float: left;">
            <option value="0" ${currentFilter == 0 ? 'selected' : ''}>Tất cả sản phẩm</option>
            <c:forEach items="${allCategories}" var="c">
                <option value="${c.id}" ${currentFilter == c.id ? 'selected' : ''}>${c.name}</option>
            </c:forEach>
        </select>
        <button type="button" id="open-inventory-btn" style="background-color: #e67e22; color: white;">
            <i class="fa-solid fa-clock-rotate-left"></i> Nhật ký kho
        </button>
        <button type="button" onclick="addCat()" id="add-cat-btn">
            + Thêm loại sản phẩm
        </button>
        <button id="add">+ Thêm sản phẩm</button>
    </div>
    <div class="cat-list">
        <h3>DANH SÁCH LOẠI SẢN PHẨM</h3>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Tên loại sản phẩm</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
            </tr>
            </thead>
            <tbody id="cat-tbody">
            <c:forEach items="${allCategories}" var="c" varStatus="status">
                <tr class="cat-row" style="${status.index >= 5 ? 'display:none;' : ''}">
                    <td>${c.id}</td>
                    <td>${c.name}</td>
                    <td>
                        <span class="status-text ${c.state eq 'Inactive' ? 'inactive' : 'active'}">${c.state}</span>
                    </td>
                    <td>
                        <button type="button"
                                onclick="openEditCat(${c.id}, '${c.name}', '${c.state}')"
                                title="Sửa loại">
                            <i class="fa-solid fa-pen"></i>
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div style="text-align: center; margin-top: 12px;">
            <button type="button" id="btn-show-more-cat" onclick="showMoreCat()" style="display:none;  width: 100px;font-size: ">
                <i class="fa-solid fa-chevron-down"></i> Xem thêm
            </button>
        </div>
    </div>
    <div class="list-product">
        <div class="list-header">
            <h3>DANH SÁCH SẢN PHẨM</h3>
            <button type="button" class="btn-delete-checked" onclick="toggleCheckedProducts()">
                Ẩn/Hiện sản phẩm đã chọn
            </button>
        </div>        <table>
            <thead>
            <tr>
                <th style="width: 40px; text-align: center;">
                    <input type="checkbox" onclick="toggleSelectAll(this)" style="cursor: pointer;">
                </th>
                <th>ID</th>
                <th>Tên sản phẩm</th>
                <th>Loại sản phẩm</th>
                <th>Khối lượng</th>
                <th>Giá (đ)</th>
                <th>Số lượng còn</th>
                <th>Số lượng bán</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${products}" var="p">
                <c:set var="imgs" value="${productImagesMap[p.id]}"/>
                <c:set var="img0url" value=""/>
                <c:set var="img1url" value=""/>
                <c:set var="img2url" value=""/>
                <c:if test="${not empty imgs[0]}"><c:url value="${imgs[0]}" var="img0url"/></c:if>
                <c:if test="${not empty imgs[1]}"><c:url value="${imgs[1]}" var="img1url"/></c:if>
                <c:if test="${not empty imgs[2]}"><c:url value="${imgs[2]}" var="img2url"/></c:if>
                <tr>
                    <td style="text-align: center;">
                        <input type="checkbox" name="productIds" value="${p.id}" style="cursor: pointer;">
                    </td>

                    <td>#${p.id}</td>
                    <td style="font-weight: bold; text-align: left;">${p.name}</td>
                    <td>${p.category_name != null ? p.category_name : 'Khác'}</td>
                    <td>${p.weight_grams}</td>

                    <td style="color: #d32f2f; font-weight: bold;">
                        <fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/> VND
                    </td>

                    <td style="${p.stock == 0 ? 'color:red; font-weight:bold;' : ''}">${p.stock}</td>
                    <td>${p.sold}</td>

                    <td>
                        <c:choose>
                            <c:when test="${p.state eq 'inactive'}">
                                <span class="status-text inactive">Inactive</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-text active">Active</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <button class="remake"
                                type="button"
                                data-id="${p.id}"
                                data-name="${p.name}"
                                data-category="${p.category_id}"
                                data-weight="${p.weight_grams}"
                                data-price="${p.price}"
                                data-stock="${p.stock}"
                                data-state="${p.state}"
                                data-desc="${p.description}"
                                data-img-main="${img0url}"
                                data-img-sub1="${img1url}"
                                data-img-sub2="${img2url}"
                                onclick="openEditModal(this)">
                            <i class="fa-solid fa-pen"></i>
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="product-page" id="pagination" style="margin-top: 20px; text-align: center;">

            <c:set var="displayTotal" value="${totalPages > 0 ? totalPages : 1}" />
            <c:set var="startPage" value="${currentPage - 1}" />
            <c:set var="endPage" value="${currentPage + 1}" />

            <c:if test="${startPage < 1}">
                <c:set var="startPage" value="1" />
                <c:set var="endPage" value="3" />
            </c:if>

            <c:if test="${endPage > displayTotal}">
                <c:set var="endPage" value="${displayTotal}" />
                <c:set var="startPage" value="${displayTotal - 2}" />
                <c:if test="${startPage < 1}">
                    <c:set var="startPage" value="1" />
                </c:if>
            </c:if>

            <c:if test="${currentPage > 1}">
                <button type="button" onclick="changePage(1)" title="Trang đầu tiên">
                    <i class="fas fa-angle-double-left"></i>
                </button>
                <button type="button" onclick="changePage(${currentPage - 1})" title="Trang trước">
                    <i class="fas fa-angle-left"></i>
                </button>
            </c:if>

            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                <c:choose>
                    <c:when test="${currentPage == i}">
                        <input type="number"
                               id="page-input"
                               value="${i}"
                               data-max="${displayTotal}"
                               title="Nhập số trang và nhấn Enter" />
                    </c:when>
                    <c:otherwise>
                        <button type="button" onclick="changePage(${i})">${i}</button>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${currentPage < displayTotal}">
                <button type="button" onclick="changePage(${currentPage + 1})" title="Trang tiếp theo">
                    <i class="fas fa-angle-right"></i>
                </button>
                <button type="button" onclick="changePage(${displayTotal})" title="Trang cuối cùng">
                    <i class="fas fa-angle-double-right"></i>
                </button>
            </c:if>
        </div>

    </div>
</div>
<div class="form-add" id="form-add" style="display: none">
    <div class="form-title">
        <p>THÊM SẢN PHẨM</p>
        <button id="take-off" type="button" >X</button>
    </div>
    <form class="main-form" action="${pageContext.request.contextPath}/admin/products" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="add_product">
        <div class="p name-p">
            <label>Tên sản phẩm</label>
            <input type="text" name="name" placeholder="Tên sản phẩm" required>
        </div>
        <div class="type-p">
            <label>Loại sản phẩm</label>
            <select name="category_id">
                <option value="">-- Chọn loại --</option>
                <c:forEach items="${categories}" var="cat">
                    <option value="${cat.id}">${cat.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="price-p">
            <label>Giá sản phẩm</label>
            <input type="number" name="price" placeholder="Giá sản phẩm" required>
        </div>
        <div class="p name-p">
            <label>Số lượng</label>
            <input type="number" name="stock" placeholder="Số lượng">
        </div>
        <div class="weight-p">
            <label>Khối lượng</label>
            <input type="number" name="weight" placeholder="Khối lượng" required>
        </div>
        <div class="img-p">
            <label>Ảnh sản phẩm</label>
            <div class="upload-boxes">
                <div class="upload-box">
                    <input type="file" name="main_image" id="mainImage"
                           accept="image/jpeg,image/png,image/webp" hidden
                           onchange="previewImage(this)">
                    <label for="mainImage" class="upload-label">
                        <span class="upload-placeholder">
                            <i class="fa-solid fa-plus"></i>
                            <small>Ảnh chính</small>
                        </span>
                        <img class="upload-preview" alt="">
                    </label>
                </div>
                <div class="upload-box">
                    <input type="file" name="sub_image_1" id="subImage1"
                           accept="image/jpeg,image/png,image/webp" hidden
                           onchange="previewImage(this)">
                    <label for="subImage1" class="upload-label">
                        <span class="upload-placeholder">
                            <i class="fa-solid fa-plus"></i>
                            <small>Ảnh phụ 1</small>
                        </span>
                        <img class="upload-preview" alt="">
                    </label>
                </div>
                <div class="upload-box">
                    <input type="file" name="sub_image_2" id="subImage2"
                           accept="image/jpeg,image/png,image/webp" hidden
                           onchange="previewImage(this)">
                    <label for="subImage2" class="upload-label">
                        <span class="upload-placeholder">
                            <i class="fa-solid fa-plus"></i>
                            <small>Ảnh phụ 2</small>
                        </span>
                        <img class="upload-preview" alt="">
                    </label>
                </div>
            </div>
        </div>
        <div class="des-p">
            <label>Mô tả</label>
            <textarea name="description" placeholder="Mô tả"></textarea>
        </div>
        <button class="submit" type="submit">Thêm</button>
    </form>
</div>
<div class="form-add" id="form-remake" style="display: none">
    <div class="form-title">
        <p>SỬA SẢN PHẨM</p>
        <button id="close-remake" type="button" onclick="closeEditModal()">X</button>
    </div>

    <form class="main-form" action="${pageContext.request.contextPath}/admin/products" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="edit_product">

        <input type="hidden" name="id" id="edit-id-hidden">

        <div class="p id-p">
            <label>ID</label>
            <input type="text" id="edit-id-display" readonly style="background-color: #f0f0f0;">
        </div>

        <div class="p name-p">
            <label>Tên sản phẩm</label>
            <input type="text" name="name" id="edit-name" required>
        </div>

        <div class="type-p">
            <label>Loại sản phẩm</label>
            <select name="category_id" id="edit-category">
                <option value="">-- Chọn loại --</option>
                <c:forEach items="${categories}" var="cat">
                    <option value="${cat.id}">${cat.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="state-p">
            <label>Trạng thái</label>
            <select name="state" id="edit-state" required>
                <option value="active">Active</option>
                <option value="inactive">Inactive</option>
            </select>
        </div>
        <div class="price-p">
            <label>Giá sản phẩm</label>
            <input type="number" name="price" id="edit-price" required>
        </div>
        <div class="weight-p">
            <label>Khối lượng</label>
            <input type="number" name="weight" id="edit-weight" required>
        </div>
        <div class="p name-p">
            <label>Số lượng</label>
            <input type="number" name="stock" id="edit-stock" required>
        </div>
        <div class="img-p">
            <label>Ảnh sản phẩm</label>
            <div class="upload-boxes">
                <div class="upload-box" id="edit-box-main">
                    <input type="file" name="main_image" id="editMainImage"
                           accept="image/jpeg,image/png,image/webp" hidden
                           onchange="previewImage(this)">
                    <label for="editMainImage" class="upload-label">
                        <span class="upload-placeholder">
                            <i class="fa-solid fa-plus"></i>
                            <small>Ảnh chính</small>
                        </span>
                        <img class="upload-preview" alt="">
                    </label>
                </div>
                <div class="upload-box" id="edit-box-sub1">
                    <input type="file" name="sub_image_1" id="editSubImage1"
                           accept="image/jpeg,image/png,image/webp" hidden
                           onchange="previewImage(this)">
                    <label for="editSubImage1" class="upload-label">
                        <span class="upload-placeholder">
                            <i class="fa-solid fa-plus"></i>
                            <small>Ảnh phụ 1</small>
                        </span>
                        <img class="upload-preview" alt="">
                    </label>
                </div>
                <div class="upload-box" id="edit-box-sub2">
                    <input type="file" name="sub_image_2" id="editSubImage2"
                           accept="image/jpeg,image/png,image/webp" hidden
                           onchange="previewImage(this)">
                    <label for="editSubImage2" class="upload-label">
                        <span class="upload-placeholder">
                            <i class="fa-solid fa-plus"></i>
                            <small>Ảnh phụ 2</small>
                        </span>
                        <img class="upload-preview" alt="">
                    </label>
                </div>
            </div>
        </div>

        <div class="des-p">
            <label>Mô tả</label>
            <textarea name="description" id="edit-desc"></textarea>
        </div>

        <button class="submit" type="submit">Cập nhật</button>
    </form>
</div>
<div id="form-add-cat" class="form-add">

    <div class="form-title">
        <p >THÊM LOẠI SẢN PHẨM</p>
        <button type="button" id="close-cat">X</button>
    </div>

    <form class="main-form" action="${pageContext.request.contextPath}/admin/products" method="post">
        <input type="hidden" name="action" value="add_category">
        <div class="p name-p" >
            <label>Tên loại sản phẩm</label>
            <input type="text" name="category_name" required placeholder="Nhập tên loại..." >
        </div>
        <button class="submit" type="submit">Thêm</button>
    </form>
</div>
<form id="delete-form" action="${pageContext.request.contextPath}/admin/products" method="post" style="display: none;">
    <input type="hidden" name="action" id="delete-action">
    <input type="hidden" name="ids" id="delete-ids-multi">
</form>
<div id="form-edit-cat" class="form-add" style="display:none;">
    <div class="form-title">
        <p>SỬA LOẠI SẢN PHẨM</p>
        <button type="button" id="close-edit-cat">X</button>
    </div>
    <form class="main-form" action="${pageContext.request.contextPath}/admin/products" method="post">
        <input type="hidden" name="action" value="edit_category">
        <input type="hidden" name="id" id="edit-cat-id">
        <div class="p name-p">
            <label>Tên loại sản phẩm</label>
            <input type="text" name="category_name" id="edit-cat-name" required>
        </div>
        <div class="state-p">
            <label>Trạng thái</label>
            <select name="state" id="edit-cat-state">
                <option value="Active">Active</option>
                <option value="Inactive">Inactive</option>
            </select>
        </div>
        <button class="submit" type="submit">Cập nhật</button>
    </form>
</div>
<div class="form-add" id="form-inventory-log"
     style="display: none; max-width: 850px; width: 80%;
            position: fixed; z-index: 9999;
            box-shadow: 0px 0px 15px rgba(0,0,0,0.3);
            left: 60%;
            top: 50%;
            transform: translate(-50%, -50%);">    <div class="form-title">
        <p>NHẬT KÝ BIẾN ĐỘNG KHO (XUẤT / NHẬP)</p>
        <button id="close-inventory-log" type="button">X</button>
    </div>
    <div id="inventory-log-body" style="padding: 20px; max-height: 450px; overflow-y: auto;">
        <p>Đang tải dữ liệu lịch sử...</p>
    </div>
</div>
<button class="slide-top" id="slide-top"><i class="fas fa-angle-up"></i></button>
<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/adminPage2.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
<script>
    function repositionInventoryLog() {
        var logForm = document.getElementById('form-inventory-log');
        if (logForm && logForm.style.display === 'block') {
            var rightContent = document.getElementById('right-content');
            if (rightContent) {
                var rect = rightContent.getBoundingClientRect();
                var centerOfRight = rect.left + (rect.width / 2);
                logForm.style.left = centerOfRight + 'px';
            }
        }
    }

    document.getElementById('open-inventory-btn').addEventListener('click', function() {
        var logForm = document.getElementById('form-inventory-log');
        var logBody = document.getElementById('inventory-log-body');

        logForm.style.display = 'block';

        repositionInventoryLog();

        logBody.innerHTML = '<p style="text-align:center;">Đang truy vấn dữ liệu từ kho hàng...</p>';

        fetch('${pageContext.request.contextPath}/admin/products?action=get_inventory_log')
            .then(response => response.text())
            .then(htmlResult => {
                logBody.innerHTML = htmlResult;
                repositionInventoryLog();
            })
            .catch(err => {
                logBody.innerHTML = '<p style="color:red; text-align:center;">Lỗi! Không thể tải dữ liệu nhật ký.</p>';
            });
    });

    document.getElementById('close-inventory-log').addEventListener('click', function() {
        document.getElementById('form-inventory-log').style.display = 'none';
    });

    window.addEventListener('resize', repositionInventoryLog);

    var sliderMenuBtn = document.getElementById('slider-menu');
    if (sliderMenuBtn) {
        sliderMenuBtn.addEventListener('click', function() {
            setTimeout(repositionInventoryLog, 100);
        });
    }
</script>
</body>
</html>
