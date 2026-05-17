function togglePassword() {
    const input1 = document.getElementById('password');
    const input2 = document.getElementById('confirmpassword');
    const checkbox = document.getElementById('toggle-password');

    if (checkbox.checked) {
        input1.type = 'text';
        input2.type = 'text';
    } else {
        input1.type = 'password';
        input2.type = 'password';
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('register-form');
    const nameInput = document.getElementById('yourname');
    const emailInput = document.getElementById('email');
    const phoneInput = document.getElementById('phone');
    const passwordInput = document.getElementById('password');
    const confirmInput = document.getElementById('confirmpassword');

    const errName = document.getElementById('err-name');
    const errEmail = document.getElementById('err-email');
    const errPhone = document.getElementById('err-phone');
    const errPassword = document.getElementById('err-password');
    const errConfirm = document.getElementById('err-confirm');

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

    function validateName() {
        const val = nameInput.value.trim();
        const regexName = /^[\p{L}\s]+$/u;
        if (val === '') {
            showError(nameInput, errName, 'Họ và tên không được để trống!');
            return false;
        } else if (!regexName.test(val)) {
            showError(nameInput, errName, 'Họ tên không được chứa số hay ký tự đặc biệt!');
            return false;
        }
        clearError(nameInput, errName);
        return true;
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

    function validatePhone() {
        const val = phoneInput.value.trim();
        const regexPhone = /^(03|05|07|08|09)\d{8}$/;
        if (val === '') {
            showError(phoneInput, errPhone, 'Số điện thoại không được để trống!');
            return false;
        } else if (!regexPhone.test(val)) {
            showError(phoneInput, errPhone, 'Số điện thoại không hợp lệ (10 số, bắt đầu bằng 03, 05, 07, 08, 09)!');
            return false;
        }
        clearError(phoneInput, errPhone);
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
        if (confirmInput.value !== '') validateConfirm();
        return true;
    }

    function validateConfirm() {
        const val = confirmInput.value;
        if (val === '') {
            showError(confirmInput, errConfirm, 'Vui lòng xác nhận mật khẩu!');
            return false;
        } else if (val !== passwordInput.value) {
            showError(confirmInput, errConfirm, 'Mật khẩu xác nhận không khớp!');
            return false;
        }
        clearError(confirmInput, errConfirm);
        return true;
    }

    nameInput.addEventListener('input', validateName);
    emailInput.addEventListener('input', validateEmail);
    phoneInput.addEventListener('input', validatePhone);
    passwordInput.addEventListener('input', validatePassword);
    confirmInput.addEventListener('input', validateConfirm);

    form.addEventListener('submit', function (event) {
        const isNameValid = validateName();
        const isEmailValid = validateEmail();
        const isPhoneValid = validatePhone();
        const isPasswordValid = validatePassword();
        const isConfirmValid = validateConfirm();

        if (!isNameValid || !isEmailValid || !isPhoneValid || !isPasswordValid || !isConfirmValid) {
            event.preventDefault();
        }
    });
});
