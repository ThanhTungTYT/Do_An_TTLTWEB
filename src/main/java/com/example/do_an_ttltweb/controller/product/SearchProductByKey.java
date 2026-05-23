package com.example.do_an_ttltweb.controller.product;

import com.example.do_an_ttltweb.model.Product;
import com.example.do_an_ttltweb.model.Category; // Import model Category
import com.example.do_an_ttltweb.services.ProductService;
import com.example.do_an_ttltweb.services.CategoryService; // Import Service Category
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchProductByKey", value = "/search-product")
public class SearchProductByKey extends HttpServlet {

    private ProductService productService = new ProductService();
    private CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("search");
        String sort = request.getParameter("sort");
        String price = request.getParameter("price");
        String pageStr = request.getParameter("page");

        int page = 1;
        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (Exception ignored) {}
        }

        List<Product> listProducts;
        int totalPages;
        int pageSize = 25;

        List<Category> listCategories = categoryService.getAllCategories();

        if (keyword != null && !keyword.trim().isEmpty()) {
            listProducts = productService.searchProducts(keyword, page, pageSize);
            totalPages = productService.getTotalPagesSearch(keyword, pageSize);
        } else {
return;
        }

        request.setAttribute("listProducts", listProducts);
        request.setAttribute("listCategories", listCategories);
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentSort", sort != null ? sort : "default");
        request.setAttribute("price", price != null ? price : "all");
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/catalog-search.jsp").forward(request, response);
    }
}