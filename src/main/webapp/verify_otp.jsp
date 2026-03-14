<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Xác thực OTP | Aroma Café</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/register.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">

    <style>
        .otp-wrapper {
            max-width: 450px;
            margin: 60px auto;
            background: #fff;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            text-align: center;
        }

        .otp-input {
            width: 100%;
            padding: 15px;
            margin: 20px 0;
            text-align: center;
            letter-spacing: 10px;
            font-size: 26px;
            font-weight: bold;
            border: 2px solid #eee;
            border-radius: 8px;
            outline: none;
            transition: 0.3s;
        }

        .otp-input:focus {
            border-color: #c76739;
            box-shadow: 0 0 8px rgba(199, 103, 57, 0.2);
        }

        .alert {
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 5px;
            font-size: 14px;
        }
        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .resend-box {
            margin-top: 15px;
            font-size: 14px;
        }
        .resend-link {
            color: #c76739;
            text-decoration: none;
            font-weight: 600;
            cursor: pointer;
        }
        .resend-link.disabled {
            color: #999;
            pointer-events: none;
            cursor: default;
        }

        .back-link {
            display: block;
            margin-top: 20px;
            color: #666;
            font-size: 13px;
            text-decoration: none;
        }
        .back-link:hover { text-decoration: underline; }
    </style>
</head>
<body>
<header>
    <div class="top">
        <div class="logo">
            <img src="${pageContext.request.contextPath}/assets/img/logo.png"
                 onclick="location.href='${pageContext.request.contextPath}/index.jsp'"
                 width="300px" height="100px" alt="Logo">
        </div>
    </div>
</header>

<div class="otp-wrapper">
    <h2 style="color: #333; margin-bottom: 10px;">XÁC THỰC TÀI KHOẢN</h2>

    <p style="color: #666; font-size: 14px; line-height: 1.5;">
        Mã OTP 6 số đã được gửi tới email:<br>
        <strong>${sessionScope.reg_temp.email}</strong>
    </p>

    <c:if test="${not empty error}">
        <div class="alert alert-error">${error}</div>
    </c:if>
    <c:if test="${not empty message}">
        <div class="alert alert-success">${message}</div>
    </c:if>

    <form action="verify-otp" method="post">
        <input type="text"
               name="otp"
               class="otp-input"
               placeholder="______"
               maxlength="6"
               required
               autofocus
               oninput="this.value = this.value.replace(/[^0-9]/g, '');">

        <div class="bt_regis" style="margin-top: 10px;">
            <button type="submit" id="register-btn" style="width: 100%;">Xác nhận</button>
        </div>
    </form>

    <div class="resend-box">
        <a href="resend-otp" id="resend-link" class="resend-link disabled">
            Gửi lại mã (120s)
        </a>
    </div>

    <a href="${pageContext.request.contextPath}/register.jsp" class="back-link">
        <i class="fas fa-arrow-left"></i> Quay lại trang đăng ký
    </a>
</div>

<footer class="footer">
    <div class="footer-top">
        <div class="foot-content left">
            <h3>Aroma Café</h3>
            <p>Địa chỉ: Trường đại học Nông Lâm TPHCM.</p>
            <p>Điện thoại: 0933652267.</p>
            <p>Email: nguyenhuybaolegit@gmail.com</p>
        </div>

        <div class="foot-content footer-links">
            <h3>Quy định & Chính sách</h3>
            <ul>
                <li><a href="${pageContext.request.contextPath}/policy?type=shipping">Chính sách vận chuyển</a></li>
                <li><a href="${pageContext.request.contextPath}/policy?type=warranty">Chính sách bảo hành, đổi trả</a></li>
                <li><a href="${pageContext.request.contextPath}/policy?type=terms">Điều khoản sử dụng</a></li>
            </ul>
        </div>

        <div class="foot-content right">
            <h3>Kết nối với chúng tôi</h3>
            <div class="social">
                <a href="#"><i class="fab fa-facebook-f"></i></a>
                <a href="#"><i class="fab fa-twitter"></i></a>
                <a href="#"><i class="fab fa-instagram"></i></a>
                <a href="#"><i class="fab fa-linkedin-in"></i></a>
            </div>
        </div>
    </div>
    <div class="footer-bottom">
        <p>&copy; 2024 Aroma Café. All rights reserved.</p>
    </div>
</footer>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const resendLink = document.getElementById('resend-link');


        let countdown = 120;
        const isResent = "${not empty message}" === "true";

        if (isResent) {
            const now = new Date().getTime();
            localStorage.setItem('otp_timer_end', (now + 120 * 1000).toString());
        }

        const storedTimestamp = localStorage.getItem('otp_timer_end');
        if (storedTimestamp) {
            const now = new Date().getTime();
            const remaining = Math.ceil((parseInt(storedTimestamp) - now) / 1000);
            countdown = remaining > 0 ? remaining : 0;
        } else {
            const now = new Date().getTime();
            localStorage.setItem('otp_timer_end', (now + countdown * 1000).toString());
        }

        const updateTimer = () => {
            if (countdown <= 0) {
                resendLink.textContent = "Chưa nhận được mã? Gửi lại";
                resendLink.classList.remove('disabled');
                resendLink.href = "resend-otp";
                localStorage.removeItem('otp_timer_end');
            } else {
                resendLink.textContent = "Gửi lại mã sau (" + countdown + "s)";
                resendLink.classList.add('disabled');
                resendLink.removeAttribute("href");
                countdown--;
                setTimeout(updateTimer, 1000);
            }
        };

        updateTimer();
    });
</script>
</body>
</html>