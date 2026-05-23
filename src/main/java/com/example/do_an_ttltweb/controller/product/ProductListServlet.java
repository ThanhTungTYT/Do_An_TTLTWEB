package com.example.do_an_ttltweb.controller.product;

import com.example.do_an_ttltweb.model.Category;
import com.example.do_an_ttltweb.model.Product;
import com.example.do_an_ttltweb.services.CategoryService;
import com.example.do_an_ttltweb.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "catalog", value = "/catalog")
public class ProductListServlet extends HttpServlet {

    private CategoryService catalogService = new CategoryService();
    private ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> listCategories = catalogService.getAllCategories();
        request.setAttribute("listCategories", listCategories);

        String cidStr = request.getParameter("cid");
        String sort = request.getParameter("sort");
        String pageStr = request.getParameter("page");
        String minStr   = request.getParameter("minPrice");
        String maxStr   = request.getParameter("maxPrice");

        int cid = 0;
        int page = 1;
        double minPrice = 0;
        double maxPrice = 10_000_000;

        try { cid  = Integer.parseInt(cidStr);  } catch (Exception ignored) {}
        try { page = Integer.parseInt(pageStr); if (page < 1) page = 1; } catch (Exception ignored) {}
        try { minPrice = Double.parseDouble(minStr); } catch (Exception ignored) {}
        try { maxPrice = Double.parseDouble(maxStr); } catch (Exception ignored) {}

        int totalPages    = productService.getTotalPages(cid, minPrice, maxPrice);
        List<Product> listProducts = productService.getProductsForCatalog(cid, sort, page, minPrice, maxPrice);

        request.setAttribute("listProducts", listProducts);
        request.setAttribute("currentCid", cid);
        request.setAttribute("currentSort", sort);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentMin", (long) minPrice);
        request.setAttribute("currentMax", (long) maxPrice);

        request.getRequestDispatcher("catalog.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}