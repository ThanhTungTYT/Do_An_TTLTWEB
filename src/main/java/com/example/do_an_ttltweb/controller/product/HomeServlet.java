package com.example.do_an_ttltweb.controller.product;

import com.example.do_an_ttltweb.model.Banner;
import com.example.do_an_ttltweb.model.Product;
import com.example.do_an_ttltweb.services.BannerService;
import com.example.do_an_ttltweb.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomeServlet", value = "")
public class HomeServlet extends HttpServlet {

    private ProductService productService = new ProductService();
    private BannerService bannerService = new BannerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> listProduct = productService.getProductsBySold();
        List<Banner> listBanner = bannerService.getBannerActive();

        request.setAttribute("listProduct", listProduct);
        request.setAttribute("listBanner", listBanner);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}