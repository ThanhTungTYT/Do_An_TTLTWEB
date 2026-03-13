<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="change-password-container">
    <h2>Đổi mật khẩu</h2>

    <c:if test="${not empty error}">
        <div style="color: #721c24; background-color: #f8d7da; padding: 12px; margin-bottom: 20px; border-radius: 4px;">
            <i class="fas fa-exclamation-triangle"></i> ${error}
        </div>
    </c:if>

    <c:if test="${not empty success}">
        <div style="color: #155724; background-color: #d4edda; padding: 12px; margin-bottom: 20px; border-radius: 4px;">
            <i class="fas fa-check-circle"></i> ${success}
        </div>
    </c:if>

    <form id="change-pass-form" action="${pageContext.request.contextPath}/change-password" method="post">

        <label for="old_pass">Mật khẩu hiện tại:</label>
        <input type="password" id="old_pass" name="old_pass" required placeholder="Nhập mật khẩu cũ">

        <label for="new_pass">Mật khẩu mới:</label>
        <input type="password" id="new_pass" name="new_pass" required placeholder="Nhập mật khẩu mới">

        <label for="confirm_pass">Xác nhận mật khẩu mới:</label>
        <input type="password" id="confirm_pass" name="confirm_pass" required placeholder="Nhập lại mật khẩu mới">

        <button type="submit">Đổi mật khẩu</button>
    </form>
</div>
