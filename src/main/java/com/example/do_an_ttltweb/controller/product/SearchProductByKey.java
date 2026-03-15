package com.example.do_an_ttltweb.controller.product;

import com.example.do_an_ttltweb.model.Product;
import com.example.do_an_ttltweb.services.ProductService;
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("search");
        String sort = request.getParameter("sort");
        String pageStr = request.getParameter("page");

        int page = 1;
        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (Exception ignored) {}
        }

        List<Product> listProducts;
        int totalPages;

        if (keyword != null && !keyword.trim().isEmpty()) {
            int pageSize = 25;
            listProducts = productService.searchProducts(keyword, page, pageSize);
            totalPages = productService.getTotalPagesSearch(keyword, pageSize);
        } else {
            listProducts = productService.getProductsForCatalog(0, sort, page);
            totalPages = productService.getTotalPages(0);
        }

        request.setAttribute("listProducts", listProducts);
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentSort", sort);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/catalog-search.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}