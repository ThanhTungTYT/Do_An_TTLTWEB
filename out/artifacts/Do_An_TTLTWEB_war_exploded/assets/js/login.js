document.addEventListener('DOMContentLoaded', function () {
    const loginButton = document.getElementById('b-login'); // Nút Đăng nhập (button)
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password'); // Thêm để kiểm tra mật khẩu

    if (loginButton && usernameInput && passwordInput) {
        loginButton.addEventListener('click', function (event) {
            if (usernameInput.value.trim() === '' || passwordInput.value.trim() === '') {
                alert('Vui lòng điền đầy đủ Tên đăng nhập và Mật khẩu.');
                return; // Dừng nếu chưa điền đủ
            }

            const username = usernameInput.value.trim().toLowerCase();

            // 2. Logic kiểm tra TÊN ĐĂNG NHẬP
            if (username === 'admin@gmail.com') {
                // Chuyển hướng đến trang quản trị
                window.location.href = 'adminPage1.html';
            } else {
                // Chuyển hướng đến trang tài khoản người dùng hoặc trang chủ
                window.location.href = 'account.html';
            }
        });
    }
});