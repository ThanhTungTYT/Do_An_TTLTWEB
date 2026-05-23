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
        String minStr = request.getParameter("minPrice");
        String maxStr = request.getParameter("maxPrice");
        String cidStr = request.getParameter("cid");

        int cid = 0;
        int page = 1;
        double minPrice = 0;
        double maxPrice = 10_000_000;

        try { cid  = Integer.parseInt(cidStr);  } catch (Exception ignored) {}
        try { page = Integer.parseInt(pageStr); if (page < 1) page = 1; } catch (Exception ignored) {}
        try { minPrice = Double.parseDouble(minStr); } catch (Exception ignored) {}
        try { maxPrice = Double.parseDouble(maxStr); } catch (Exception ignored) {}

        List<Product> listProducts;
        int totalPages;
        int pageSize = 25;

        List<Category> listCategories = categoryService.getAllCategories();

        if (keyword != null && !keyword.trim().isEmpty()) {
            listProducts = productService.searchProductsByPrice(keyword, cid, sort, page, pageSize, minPrice, maxPrice);
            totalPages   = productService.getTotalPagesSearchByPrice(keyword, cid, pageSize, minPrice, maxPrice);
        } else {
            listProducts = productService.getProductsForCatalog(0, sort, page, minPrice, maxPrice);
            totalPages   = productService.getTotalPages(0, minPrice, maxPrice);
        }
        request.setAttribute("currentMin", (long) minPrice);
        request.setAttribute("currentMax", (long) maxPrice);

        request.setAttribute("listProducts", listProducts);
        request.setAttribute("listCategories", listCategories);
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentSort", sort != null ? sort : "default");
        request.setAttribute("price", price != null ? price : "all");
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentCid", cid);

        request.getRequestDispatcher("/catalog-search.jsp").forward(request, response);
    }
}