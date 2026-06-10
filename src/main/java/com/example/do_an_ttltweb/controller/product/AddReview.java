package com.example.do_an_ttltweb.controller.product;

import com.example.do_an_ttltweb.model.ProductReview;
import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.services.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "AddReview", value = "/addReview")
public class AddReview extends HttpServlet {

    private ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        int productId = Integer.parseInt(request.getParameter("pid"));
        if (user == null) {
            session.setAttribute("error", "Bạn cần đăng nhập để gửi đánh giá");
            response.sendRedirect(
                    request.getContextPath() + "/product?pid=" + productId
            );
            return;
        }

        ProductReview review = new ProductReview();
        review.setProductId(productId);
        review.setUserId(user.getId());
        review.setRating(Integer.parseInt(request.getParameter("rating")));
        review.setComment(request.getParameter("comment"));

        if(!reviewService.isBuy(user.getId(), productId)){
            session.setAttribute("error", "Bạn cần mua sản phẩm để tiến hành đánh giá.");
            response.sendRedirect(
                    request.getContextPath() + "/product?pid=" + productId
            );
            return;
        }

        if(reviewService.isSpam(user.getId(), productId)){
            session.setAttribute("error", "Bạn đang đánh giá quá nhanh, vui lòng chờ 1 phút.");
            response.sendRedirect(
                    request.getContextPath() + "/product?pid=" + productId
            );
            return;
        }

        if (!reviewService.addReview(review)) {
            session.setAttribute("error", "Đã xảy ra lỗi khi gửi đánh giá!");
        } else {
            session.setAttribute("success", "Gửi đánh giá thành công");
        }

        response.sendRedirect(
                request.getContextPath() + "/product?pid=" + productId
        );
    }
}
