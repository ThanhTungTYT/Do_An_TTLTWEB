package com.example.do_an_ttltweb.controller.adminPage2;

import com.example.ltwebnhom23.model.Category;
import com.example.ltwebnhom23.model.Product;
import com.example.ltwebnhom23.services.CategoryService;
import com.example.ltwebnhom23.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
@WebServlet(name = "SearchProductServlet", value = "/admin/products/search")

public class SearchProductServlet extends HttpServlet {
    private ProductService productService = new ProductService();
    private CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("search");
        String pageStr = req.getParameter("page");

        int page = 1;
        int pageSize = 25;

        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        List<Product> products;
        int totalPages = 1;

        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productService.searchProducts(keyword.trim(), page, pageSize);
            totalPages = productService.getTotalPagesSearch(keyword.trim(), pageSize);
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/products");
            return;
        }

        List<Category> categories = categoryService.getAllCategories();
        req.setAttribute("categories", categories);

        req.setAttribute("products", products);
        req.setAttribute("searchKeyword", keyword);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        req.setAttribute("currentFilter", 0);

        req.getRequestDispatcher("/adminPage2.jsp").forward(req, resp);
    }
}
