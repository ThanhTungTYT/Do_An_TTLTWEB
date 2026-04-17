// Hàm ẩn / hiện mật khẩu
function togglePassword() {
    const input = document.getElementById('password');
    const iconEye = document.getElementById('icon-eye');
    const iconEyeOff = document.getElementById('icon-eye-off');

    if (input.type === 'password') {
        input.type = 'text';
        iconEye.style.display = 'none';
        iconEyeOff.style.display = 'block';
    } else {
        input.type = 'password';
        iconEye.style.display = 'block';
        iconEyeOff.style.display = 'none';
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('f-login');
    const emailInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    const errEmail = document.getElementById('err-email');
    const errPassword = document.getElementById('err-password');

    function showError(input, errLabel, message) {
        input.classList.add('input-error');
        errLabel.textContent = message;
        errLabel.style.display = 'block';
    }

    function clearError(input, errLabel) {
        input.classList.remove('input-error');
        errLabel.textContent = '';
        errLabel.style.display = 'none';
    }

    function validateEmail() {
        const val = emailInput.value.trim();
        const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (val === '') {
            showError(emailInput, errEmail, 'Email không được để trống!');
            return false;
        } else if (!regexEmail.test(val)) {
            showError(emailInput, errEmail, 'Email không đúng định dạng (VD: abc@gmail.com)!');
            return false;
        }
        clearError(emailInput, errEmail);
        return true;
    }

    function validatePassword() {
        const val = passwordInput.value;

        if (val === '') {
            showError(passwordInput, errPassword, 'Mật khẩu không được để trống!');
            return false;
        } else if (val.length < 8) {
            showError(passwordInput, errPassword, 'Mật khẩu phải chứa ít nhất 8 ký tự!');
            return false;
        }
        clearError(passwordInput, errPassword);
        return true;
    }

    emailInput.addEventListener('input', validateEmail);
    passwordInput.addEventListener('input', validatePassword);

    form.addEventListener('submit', function (event) {
        const isEmailValid = validateEmail();
        const isPasswordValid = validatePassword();

        if (!isEmailValid || !isPasswordValid) {
            event.preventDefault();
        }
    });
});