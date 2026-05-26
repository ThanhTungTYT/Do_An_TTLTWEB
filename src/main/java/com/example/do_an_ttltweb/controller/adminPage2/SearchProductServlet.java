package com.example.do_an_ttltweb.controller.adminPage2;

import com.example.do_an_ttltweb.model.Category;
import com.example.do_an_ttltweb.model.Product;
import com.example.do_an_ttltweb.model.ProductImage;
import com.example.do_an_ttltweb.services.CategoryService;
import com.example.do_an_ttltweb.services.ImageService;
import com.example.do_an_ttltweb.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@WebServlet(name = "SearchProductServlet", value = "/admin/products/search")

public class SearchProductServlet extends HttpServlet {
    private ProductService productService = new ProductService();
    private CategoryService categoryService = new CategoryService();
    private ImageService imageService = new ImageService();

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

        Map<Integer, String[]> productImagesMap = new HashMap<>();
        for (Product p : products) {
            String[] urls = new String[3];
            for (ProductImage pi : imageService.getAllImageById(p.getId())) {
                int pos = pi.getPosition();
                if (pos >= 0 && pos < 3) urls[pos] = pi.getImage_url();
            }
            productImagesMap.put(p.getId(), urls);
        }

        List<Category> categories = categoryService.getAllCategories();
        req.setAttribute("categories", categories);

        req.setAttribute("products", products);
        req.setAttribute("productImagesMap", productImagesMap);
        req.setAttribute("searchKeyword", keyword);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        req.setAttribute("currentFilter", 0);

        req.getRequestDispatcher("/adminPage2.jsp").forward(req, resp);
    }
}
