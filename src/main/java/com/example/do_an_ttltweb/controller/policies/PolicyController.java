package com.example.do_an_ttltweb.controller.policies;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/policy")
public class PolicyController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("type");

        if (type == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String title;
        String content;

        switch (type) {

            /* ================= CHÍNH SÁCH VẬN CHUYỂN ================= */
            case "shipping":
                title = "Chính Sách Vận Chuyển";
                content = """
                    <h2 class="fade-in">1. Phí giao hàng</h2>
                    <ul class="fade-in">
                        <li>Miễn phí vận chuyển toàn quốc cho đơn hàng từ 299.000 VNĐ.</li>
                        <li>Đơn hàng dưới 299.000 VNĐ áp dụng phí giao hàng cố định 25.000 VNĐ.</li>
                    </ul>

                    <h2 class="fade-in">2. Thời gian giao hàng</h2>
                    <ul class="fade-in">
                        <li>Khu vực TP. HCM: 1–2 ngày làm việc.</li>
                        <li>Các tỉnh thành khác: 3–5 ngày làm việc.</li>
                    </ul>

                    <h2 class="fade-in">3. Lưu ý khi nhận hàng</h2>
                    <p class="fade-in">
                        Khách hàng cần kiểm tra tình trạng và số lượng sản phẩm trước khi ký nhận.
                        Mọi phản hồi về sai sót cần được báo lại trong vòng 24h kể từ khi nhận hàng.
                    </p>
                """;
                break;

            /* ================= ĐIỀU KHOẢN SỬ DỤNG ================= */
            case "terms":
                title = "Điều Khoản Sử Dụng";
                content = """
                    <h2 class="fade-in">1. Giới thiệu</h2>
                    <p class="fade-in">
                        Khi truy cập website của Aroma Café, người dùng đồng ý tuân thủ các điều khoản
                        được nêu dưới đây. Chúng tôi khuyến nghị khách hàng đọc kỹ trước khi sử dụng.
                    </p>

                    <h2 class="fade-in">2. Quyền và nghĩa vụ</h2>
                    <ul class="fade-in">
                        <li>Không sử dụng website vào mục đích phi pháp hoặc gây hại.</li>
                        <li>Không sao chép, sửa đổi hay phân phối nội dung mà không được phép.</li>
                    </ul>

                    <h2 class="fade-in">3. Bảo mật thông tin</h2>
                    <p class="fade-in">
                        Thông tin cá nhân của khách hàng được bảo vệ tuyệt đối và chỉ sử dụng
                        cho mục đích giao dịch mua hàng.
                    </p>

                    <h2 class="fade-in">4. Sửa đổi điều khoản</h2>
                    <p class="fade-in">
                        Aroma Café có quyền cập nhật, thay đổi các điều khoản khi cần thiết.
                        Những thay đổi sẽ có hiệu lực ngay khi được đăng tải.
                    </p>
                """;
                break;

            /* ================= CHÍNH SÁCH BẢO HÀNH & ĐỔI TRẢ ================= */
            case "warranty":
                title = "Chính Sách Bảo Hành & Đổi Trả";
                content = """
                    <h2 class="fade-in">1. Mục đích</h2>
                    <p class="fade-in">
                        Nhằm đảm bảo quyền lợi tối đa cho khách hàng khi mua sản phẩm tại Aroma Café,
                        chúng tôi áp dụng chính sách bảo hành và đổi trả minh bạch, nhanh chóng.
                    </p>

                    <h2 class="fade-in">2. Thời hạn bảo hành</h2>
                    <ul class="fade-in">
                        <li>Thời gian bảo hành: 30 ngày kể từ ngày nhận hàng.</li>
                        <li>Áp dụng cho các sản phẩm có lỗi kỹ thuật do nhà sản xuất.</li>
                    </ul>

                    <h2 class="fade-in">3. Điều kiện đổi trả</h2>
                    <ul class="fade-in">
                        <li>Sản phẩm còn nguyên tem, nhãn mác và chưa qua sử dụng.</li>
                        <li>Có hóa đơn hoặc bằng chứng mua hàng hợp lệ.</li>
                    </ul>

                    <h2 class="fade-in">4. Quy trình đổi trả</h2>
                    <p class="fade-in">
                        Khách hàng liên hệ với bộ phận chăm sóc qua email hoặc hotline để được
                        hướng dẫn cụ thể. Quá trình xử lý đổi trả được hoàn tất trong vòng
                        3–5 ngày làm việc.
                    </p>
                """;
                break;

            default:
                response.sendRedirect("index.jsp");
                return;
        }

        request.setAttribute("title", title);
        request.setAttribute("content", content);
        request.getRequestDispatcher("/policy.jsp").forward(request, response);
    }
}


