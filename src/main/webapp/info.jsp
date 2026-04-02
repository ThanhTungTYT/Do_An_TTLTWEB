<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<style>
    .info-container {
        background: #fff;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        max-width: 800px;
        margin: 0 auto;
    }
    .info-container h2 {
        border-bottom: 2px solid #eee;
        padding-bottom: 15px;
        margin-bottom: 25px;
        color: #c76739;
    }
    .info-form label {
        font-weight: 600;
        color: #555;
        margin-bottom: 8px;
        display: block;
    }
    .info-form input[type="text"],
    .info-form input[type="email"],
    .info-form select {
        width: 100% !important;
        box-sizing: border-box !important;
        padding: 10px;
        margin-bottom: 15px;
        border: 1px solid #ddd;
        border-radius: 5px;
        font-size: 15px;
        transition: all 0.3s;
    }
    .info-form input[readonly],
    .info-form select:disabled,
    .input-fixed {
        background-color: #f9f9f9 !important;
        cursor: not-allowed !important;
        color: #555;
        border-color: #ddd !important;
    }
    .info-form input:not([readonly]):focus,
    .info-form select:not(:disabled):focus {
        border-color: #d2691e;
        outline: none;
        box-shadow: 0 0 5px rgba(210, 105, 30, 0.3);
    }
    .btn-update {
        background-color: #c76739;
        color: white;
        padding: 12px 25px;
        border: none;
        border-radius: 5px;
        font-size: 16px;
        cursor: pointer;
        transition: background 0.3s;
        width: 100%;
        font-weight: bold;
    }
    .btn-update:hover {
        background-color: #a0522d;
    }
</style>

<div class="info-container">
    <h2>Thông tin cá nhân</h2>

    <c:if test="${not empty message}">
        <div style="color: #155724; background-color: #d4edda; padding: 12px; margin-bottom: 20px; border-radius: 4px; border: 1px solid #c3e6cb;">
            <i class="fas fa-check-circle"></i> ${message}
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div style="color: #721c24; background-color: #f8d7da; padding: 12px; margin-bottom: 20px; border-radius: 4px; border: 1px solid #f5c6cb;">
            <i class="fas fa-exclamation-triangle"></i> ${error}
        </div>
    </c:if>

    <form id="userForm" action="${pageContext.request.contextPath}/update-info" method="post" class="info-form">
        <input type="hidden" name="id" value="${user.id}">

        <label>Họ và tên:</label>
        <input type="text" name="fullname" class="editable input-fixed" value="${user.full_name}" readonly required>

        <label>Email (Không thể thay đổi):</label>
        <input type="email" name="email" class="input-fixed" value="${user.email}" readonly>

        <label>Số điện thoại:</label>
        <input type="text" name="phone" class="editable input-fixed" value="${user.phone}" readonly required placeholder="Nhập số điện thoại">

        <input type="hidden" name="city" id="hidden_city" value="${addr != null ? addr.province : ''}">
        <input type="hidden" name="district" id="hidden_district" value="${addr != null ? addr.ward : ''}">

        <label>Tỉnh/Thành phố:</label>
        <select id="citySelect" class="editable input-fixed" disabled required>
            <option value="">${addr != null && not empty addr.province ? addr.province : '-- Chọn Tỉnh/Thành phố --'}</option>
        </select>

        <label>Phường/Xã:</label>
        <select id="wardSelect" class="editable input-fixed" disabled required>
            <option value="">${addr != null && not empty addr.ward ? addr.ward : '-- Chọn Phường/Xã --'}</option>
        </select>

        <label>Đường, Số nhà:</label>
        <input type="text" name="address" class="editable input-fixed" placeholder="Số nhà, tên đường" value="${addr != null ? addr.address : ''}" readonly required>

        <input type="hidden" name="addressId" value="${addr != null ? addr.id : 0}">

        <button type="button" id="btnToggle" class="btn-update">Cập nhật thông tin</button>
    </form>
</div>

<script src="${pageContext.request.contextPath}/assets/js/info.js"></script>