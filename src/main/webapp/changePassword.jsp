<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/includes/toast.jsp"/>
<div class="change-password-container">
    <h2>Đổi mật khẩu</h2>

    <form id="change-pass-form" action="${pageContext.request.contextPath}/change-password" method="post">

        <label for="old_pass">Mật khẩu hiện tại:</label>
        <div class="password-wrapper">
            <input type="password" id="old_pass" name="old_pass" required placeholder="Nhập mật khẩu cũ">
            <button type="button" title="Hiện/ẩn mật khẩu">
                <i class="fa-regular fa-eye"></i>
                <i class="fa-regular fa-eye-slash" style="display:none;"></i>
            </button>
        </div>

        <label for="new_pass">Mật khẩu mới:</label>
        <div class="password-wrapper">
            <input type="password" id="new_pass" name="new_pass" required placeholder="Nhập mật khẩu mới">
            <button type="button" title="Hiện/ẩn mật khẩu">
                <i class="fa-regular fa-eye"></i>
                <i class="fa-regular fa-eye-slash" style="display:none;"></i>
            </button>
        </div>

        <label for="confirm_pass">Xác nhận mật khẩu mới:</label>
        <div class="password-wrapper">
            <input type="password" id="confirm_pass" name="confirm_pass" required placeholder="Nhập lại mật khẩu mới">
            <button type="button" title="Hiện/ẩn mật khẩu">
                <i class="fa-regular fa-eye"></i>
                <i class="fa-regular fa-eye-slash" style="display:none;"></i>
            </button>
        </div>

        <button type="submit">Đổi mật khẩu</button>
    </form>
</div>

<script>
    document.removeEventListener('click', window.togglePasswordHandler);

    window.togglePasswordHandler = function(e) {
        const button = e.target.closest('.password-wrapper button');

        if (button) {
            const wrapper = button.closest('.password-wrapper');
            const input = wrapper.querySelector('input');
            const iconEye = wrapper.querySelector('.fa-eye');
            const iconEyeOff = wrapper.querySelector('.fa-eye-slash');

            if (input.type === 'password') {
                input.type = 'text';
                iconEye.style.display = 'none';
                iconEyeOff.style.display = 'inline-block';
            } else {
                input.type = 'password';
                iconEye.style.display = 'inline-block';
                iconEyeOff.style.display = 'none';
            }
        }
    };

    document.addEventListener('click', window.togglePasswordHandler);
</script>