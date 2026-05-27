document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('f-contact');
    const nameInput = document.getElementById('name');
    const emailInput = document.getElementById('email');
    const messageInput = document.getElementById('message');
    const charCounter = document.getElementById('char-counter');

    function showError(input, message) {
        input.classList.add('input-error');

        let errLabel;
        if (input.tagName.toLowerCase() === 'textarea') {
            errLabel = input.nextElementSibling.querySelector('.error');
        } else {
            errLabel = input.nextElementSibling;
        }

        if (errLabel && errLabel.classList.contains('error')) {
            errLabel.textContent = message;
            errLabel.style.display = 'block';
        }
    }

    function clearError(input) {
        input.classList.remove('input-error');

        let errLabel;
        if (input.tagName.toLowerCase() === 'textarea') {
            errLabel = input.nextElementSibling.querySelector('.error');
        } else {
            errLabel = input.nextElementSibling;
        }

        if (errLabel && errLabel.classList.contains('error')) {
            errLabel.textContent = '';
            errLabel.style.display = 'none';
        }
    }

    function validateName() {
        const val = nameInput.value.trim();
        const regexName = /^[\p{L}\s]+$/u;

        if (val === '') {
            showError(nameInput, 'Họ và tên không được để trống!');
            return false;
        } else if (!regexName.test(val)) {
            showError(nameInput, 'Họ tên không được chứa số hay kí tự đặc biệt!');
            return false;
        }
        clearError(nameInput);
        return true;
    }

    function validateEmail() {
        const val = emailInput.value.trim();
        const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (val === '') {
            showError(emailInput, 'Email không được để trống!');
            return false;
        } else if (!regexEmail.test(val)) {
            showError(emailInput, 'Email không đúng định dạng (VD: abc@gmail.com)!');
            return false;
        }
        clearError(emailInput);
        return true;
    }

    function validateMessage() {
        const val = messageInput.value;
        const length = val.length;

        charCounter.textContent = `${length}/500`;

        if (length > 500) {
            charCounter.classList.add('limit-exceeded');
            showError(messageInput, 'Tin nhắn không được vượt quá 500 ký tự!');
            return false;
        } else {
            charCounter.classList.remove('limit-exceeded');
        }

        if (val.trim() === '') {
            showError(messageInput, 'Tin nhắn không được để trống!');
            return false;
        }

        clearError(messageInput);
        return true;
    }

    nameInput.addEventListener('input', validateName);
    emailInput.addEventListener('input', validateEmail);
    messageInput.addEventListener('input', validateMessage);

    form.addEventListener('submit', function (event) {
        const isNameValid = validateName();
        const isEmailValid = validateEmail();
        const isMessageValid = validateMessage();

        if (!isNameValid || !isEmailValid || !isMessageValid) {
            event.preventDefault();
        }
    });
});