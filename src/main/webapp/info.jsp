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
        color: #333;
    }
    .info-form label {
        font-weight: 600;
        color: #555;
        margin-bottom: 5px;
        display: block;
    }
    .info-form input {
        width: 100%;
        padding: 10px;
        margin-bottom: 15px;
        border: 1px solid #ddd;
        border-radius: 5px;
        font-size: 14px;
        transition: all 0.3s;
    }

    .info-form input[readonly] {
        background-color: #f9f9f9;
        cursor: default;
        color: #555;
        border-color: transparent;
    }

    .info-form input:not([readonly]) {
        background-color: #fff;
        border-color: #d2691e;
    }


    .input-fixed {
        background-color: #f9f9f9 !important;
        cursor: not-allowed !important;
    }

    .info-form input:focus {
        border-color: #d2691e;
        outline: none;
    }

    .btn-update {
        background-color: #d2691e;
        color: white;
        padding: 12px 25px;
        border: none;
        border-radius: 5px;
        font-size: 16px;
        cursor: pointer;
        transition: background 0.3s;
    }
    .btn-update:hover {
        background-color: #a0522d;
    }

    .btn-save-mode {
        background-color: #d2691e !important;
    }
    .btn-save-mode:hover {
        background-color: #a0522d !important;
    }
</style>

<div class="info-container">
    <h2>Thông tin cá nhân</h2>
    <form id="userForm" action="update-info" method="post" class="info-form">
        <input type="hidden" name="id" value="${user.id}">

        <label>Họ và tên:</label>
        <input type="text" name="fullname" class="editable" value="${user.full_name}" readonly required>
        <label>Email:</label>
        <input type="email" name="email" class="input-fixed" value="${user.email}" readonly>

        <label>Số điện thoại:</label>
        <input type="text" name="phone" class="editable" value="${user.phone}" readonly placeholder="Nhập số điện thoại">

        <label>Tỉnh/TP:</label>
        <input type="text" name="city" class="editable" placeholder="Tỉnh/TP" value="${addr != null ? addr.province : ''}" readonly>

        <label>Quận/Huyện, Xã/Phường:</label>
        <input type="text" name="district" class="editable" placeholder="Quận/Huyện" value="${addr != null ? addr.ward : ''}" readonly>

        <label>Đường, Số nhà:</label>
        <input type="text" name="address" class="editable" placeholder="Số nhà, tên đường" value="${addr != null ? addr.address : ''}" readonly>

        <input type="hidden" name="addressId" value="${addr != null ? addr.id : 0}">

        <button type="button" id="btnToggle" class="btn-update">Cập nhật thông tin</button>
    </form>
</div>
<script src="${pageContext.request.contextPath}/assets/js/info.js"></script>
