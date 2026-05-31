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

                if (result == 1) {
                    request.getSession().setAttribute("success", "Đã xóa vĩnh viễn mã giảm giá.");
                } else if (result == 2) {
                    request.getSession().setAttribute("success", "Mã đã có đơn hàng sử dụng. Đã chuyển sang trạng thái ngưng hoạt động.");
                } else {
                    request.getSession().setAttribute("error", "Xóa thất bại!");
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/promotion");
    }
}