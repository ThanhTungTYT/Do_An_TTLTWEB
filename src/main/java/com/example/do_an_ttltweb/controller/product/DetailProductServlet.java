package com.example.do_an_ttltweb.controller.product;

import com.example.do_an_ttltweb.model.Product;
import com.example.do_an_ttltweb.model.ProductImage;
import com.example.do_an_ttltweb.model.ProductReview;
import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.services.ImageService;
import com.example.do_an_ttltweb.services.ProductService;
import com.example.do_an_ttltweb.services.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "product", value = "/product")
public class DetailProductServlet extends HttpServlet {

    private ProductService productService = new ProductService();
    private ImageService imageService = new ImageService();
    private ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pid = request.getParameter("pid");
        if (pid == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        int productId = Integer.parseInt(pid);
        Product product = productService.getProduct(productId);
        request.setAttribute("product", product);

        if (product != null) {
            List<Product> relative = productService.getProductsByRelative(
                    product.getCategory_id(), product.getName(), product.getId());
            request.setAttribute("relative", relative);
            
            User currentUser = (User) request.getSession().getAttribute("user");
            int currentUserId = (currentUser != null) ? currentUser.getId() : -1;

            List<ProductReview> allReviews = reviewService.getReviewForProduct(productId);

            List<ProductReview> sortedReviews = new java.util.ArrayList<>();
            ProductReview myReview = null;

            for (ProductReview r : allReviews) {
                if (r.getUserId() == currentUserId) {
                    myReview = r; // review của user hiện tại
                }
            }
            if (myReview != null) sortedReviews.add(myReview);

            for (ProductReview r : allReviews) {
                if (r.getUserId() != currentUserId) {
                    sortedReviews.add(r);
                }
            }

            int totalCount = allReviews.size();
            double avg = 0;
            if (totalCount > 0) {
                int sum = 0;
                for (ProductReview r : allReviews) sum += r.getRating();
                avg = (double) sum / totalCount;
                avg = Math.round(avg * 10.0) / 10.0;
            }
            request.setAttribute("review", sortedReviews);
            request.setAttribute("count", totalCount);
            request.setAttribute("avg", avg);
            request.setAttribute("currentUserId", currentUserId);
            List<ProductImage> listImage = imageService.getAllImageById(productId);
            request.setAttribute("listImage", listImage);
        }
        request.getRequestDispatcher("product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}