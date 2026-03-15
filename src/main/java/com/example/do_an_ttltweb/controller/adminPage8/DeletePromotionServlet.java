package com.example.do_an_ttltweb.controller.adminPage8;

import com.example.do_an_ttltweb.services.PromotionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "DeletePromotionServlet", urlPatterns = {"/admin/promotion/delete"})
public class DeletePromotionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                int result = PromotionService.getInstance().deletePromotionSafe(id);

                String msg;
                if (result == 1) {
                    msg = "Đã xóa vĩnh viễn mã giảm giá.";
                } else if (result == 2) {
                    msg = "Mã đã có đơn hàng sử dụng. Đã chuyển sang trạng thái ngưng hoạt động.";
                } else {
                    msg = "Xóa thất bại!";
                }
                request.getSession().setAttribute("sessionMessage", msg);

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/promotion");
    }
}