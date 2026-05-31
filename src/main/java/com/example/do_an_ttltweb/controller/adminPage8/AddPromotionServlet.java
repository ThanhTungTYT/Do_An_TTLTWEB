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
import java.time.LocalDate;

@WebServlet(name = "AddPromotionServlet", urlPatterns = {"/admin/promotion/add"})
public class AddPromotionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String error = null;

        String code = request.getParameter("code");
        String description = request.getParameter("description");
        String minOrderStr = request.getParameter("minOrderValue");
        String discountStr = request.getParameter("discountPercent");
        String quantityStr = request.getParameter("quantity");
        String startStr = request.getParameter("startDate");
        String endStr = request.getParameter("endDate");
        String state = request.getParameter("state");

        try {
            double minOrder = Double.parseDouble(minOrderStr);
            double discount = Double.parseDouble(discountStr);
            int quantity = Integer.parseInt(quantityStr);
            LocalDate startDate = LocalDate.parse(startStr);
            LocalDate endDate = LocalDate.parse(endStr);
            LocalDate today = LocalDate.now();

            // Validate cho ADD
            if (PromotionService.getInstance().isCodeExist(code)) {
                error = "Mã giảm giá '" + code + "' đã tồn tại!";
            } else if (minOrder < 1) error = "Đơn hàng tối thiểu phải từ 1đ trở lên!";
            else if (discount < 1 || discount > 100) error = "Mức giảm giá phải từ 1% đến 100%!";
            else if (quantity < 0 || quantity > 1000000) error = "Số lượng mã phải từ 0 đến 1.000.000!";
            else if (startDate.isBefore(today)) error = "Ngày bắt đầu không được ở quá khứ!";
            else if (!endDate.isAfter(today)) error = "Ngày kết thúc phải từ ngày mai trở đi!";
            else if (endDate.isBefore(startDate)) error = "Ngày kết thúc không được nhỏ hơn ngày bắt đầu!";

            if (error == null) {
                if (quantity == 0) state = "inactive";
                else if ("active".equals(state) && startDate.isAfter(today)) state = "inactive";
            }

            if (error != null) {
                request.setAttribute("error", error);
                request.setAttribute("listPromotions", PromotionService.getInstance().getAllPromotions());
                request.getRequestDispatcher("/adminPage8.jsp").forward(request, response);
                return;
            }

            Promotion p = new Promotion();
            p.setCode(code);
            p.setDescription(description);
            p.setMinOrderValue(minOrder);
            p.setDiscountPercent(discount);
            p.setQuantity(quantity);
            p.setStartDate(Timestamp.valueOf(startDate.atStartOfDay()));
            p.setEndDate(Timestamp.valueOf(endDate.atStartOfDay()));
            p.setState(state);

            PromotionService.getInstance().addPromotion(p);
            request.getSession().setAttribute("success", "Thêm mã giảm giá thành công!");
            response.sendRedirect(request.getContextPath() + "/admin/promotion");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi dữ liệu: " + e.getMessage());
            request.setAttribute("listPromotions", PromotionService.getInstance().getAllPromotions());
            request.getRequestDispatcher("/adminPage8.jsp").forward(request, response);
        }
    }
}