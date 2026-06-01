package com.example.do_an_ttltweb.controller.adminPage8;

import com.example.do_an_ttltweb.services.PromotionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AdminPage8Servlet", urlPatterns = {"/admin/promotion"})
public class AdminPage8Servlet extends HttpServlet {

    private static final int PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PromotionService promotionService = PromotionService.getInstance();
        // Bỏ autoUpdateStatus() — admin toàn quyền quyết định state, không auto override

        int page = 1;
        try {
            String pageStr = request.getParameter("page");
            if (pageStr != null) {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            }
        } catch (NumberFormatException ignored) {}

        int totalPromotions = promotionService.countPromotions();
        int totalPages = (int) Math.ceil((double) totalPromotions / PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;
        if (page > totalPages) page = totalPages;

        int offset = (page - 1) * PAGE_SIZE;

        request.setAttribute("listPromotions", promotionService.getPromotionsPaginated(PAGE_SIZE, offset));
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/adminPage8.jsp").forward(request, response);
    }
}