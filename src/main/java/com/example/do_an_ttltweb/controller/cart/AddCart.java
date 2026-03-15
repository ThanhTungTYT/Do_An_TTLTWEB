package com.example.do_an_ttltweb.controller.cart;

import com.example.do_an_ttltweb.model.cart.Cart;
import com.example.do_an_ttltweb.model.Product;
import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "AddCart", value = "/add-to-cart")
public class AddCart extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("pid"));
        int q = Integer.parseInt(request.getParameter("q"));
        ProductService ps = new ProductService();
        Product product = ps.getProduct(id);
        if(product == null){
            response.sendRedirect(request.getContextPath() + "/product?pid=" + id);
            return;
        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null){
            response.sendRedirect(request.getContextPath()+"/login");
            return;
        }
        Cart c = (Cart) session.getAttribute("cart");
        if(c == null) c = new Cart();
        c.addProduct(product, q);
        session.setAttribute("cart", c);
        response.sendRedirect(request.getContextPath() + "/product?pid=" + id);
        return;
    }
}