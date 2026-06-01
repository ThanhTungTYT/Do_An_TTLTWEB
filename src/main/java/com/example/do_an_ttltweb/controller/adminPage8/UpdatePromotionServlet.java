package com.example.do_an_ttltweb.controller.adminPage8;

import com.example.do_an_ttltweb.model.Promotion;
import com.example.do_an_ttltweb.services.PromotionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebServlet(name = "UpdatePromotionServlet", urlPatterns = {"/admin/promotion/update"})
public class UpdatePromotionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String error = null;

        String idStr = request.getParameter("id");
        String description = request.getParameter("description");
        String minOrderStr = request.getParameter("minOrderValue");
        String discountStr = request.getParameter("discountPercent");
        String quantityStr = request.getParameter("quantity");
        String startStr = request.getParameter("startDate");
        String endStr = request.getParameter("endDate");
        String state = request.getParameter("state");
        String pageStr = request.getParameter("page");

        try {
            double minOrder = Double.parseDouble(minOrderStr);
            double discount = Double.parseDouble(discountStr);
            int quantity = Integer.parseInt(quantityStr);
            LocalDateTime startDate = LocalDateTime.parse(startStr);
            LocalDateTime endDate = LocalDateTime.parse(endStr);

            if (minOrder < 1) error = "Đơn hàng tối thiểu phải từ 1đ trở lên!";
            else if (discount < 1 || discount > 100) error = "Mức giảm giá phải từ 1% đến 100%!";
            else if (quantity < 0 || quantity > 1000000) error = "Số lượng mã phải từ 0 đến 1.000.000!";
            else if (endDate.isBefore(startDate)) error = "Ngày kết thúc không được nhỏ hơn ngày bắt đầu!";
            else if (state == null || state.isBlank()) error = "Vui lòng chọn trạng thái!";

            if (error != null) {
                request.setAttribute("error", error);
                forwardWithList(request, response, pageStr);
                return;
            }

            Promotion p = new Promotion();
            p.setId(Integer.parseInt(idStr));
            // p.setCode(code); // Không set Code
            p.setDescription(description);
            p.setMinOrderValue(minOrder);
            p.setDiscountPercent(discount);
            p.setQuantity(quantity);
            p.setStartDate(Timestamp.valueOf(startDate));
            p.setEndDate(Timestamp.valueOf(endDate));
            p.setState(state);

            PromotionService.getInstance().updatePromotion(p);
            request.getSession().setAttribute("success", "Cập nhật mã giảm giá thành công!");
            String pageParam = (pageStr != null && !pageStr.isBlank()) ? "?page=" + pageStr : "";
            response.sendRedirect(request.getContextPath() + "/admin/promotion" + pageParam);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi dữ liệu: " + e.getMessage());
            forwardWithList(request, response, pageStr);
        }
    }

    private void forwardWithList(HttpServletRequest request, HttpServletResponse response, String pageStr)
            throws ServletException, IOException {
        int page = 1;
        try {
            if (pageStr != null) {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            }
        } catch (NumberFormatException ignored) {}

        int total = PromotionService.getInstance().countPromotions();
        int totalPages = (int) Math.ceil((double) total / 5);
        if (totalPages == 0) totalPages = 1;
        if (page > totalPages) page = totalPages;
        int offset = (page - 1) * 5;

        request.setAttribute("listPromotions", PromotionService.getInstance().getPromotionsPaginated(5, offset));
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/adminPage8.jsp").forward(request, response);
    }
}