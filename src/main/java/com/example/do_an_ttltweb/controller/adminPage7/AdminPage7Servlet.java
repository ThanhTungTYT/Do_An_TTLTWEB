package com.example.do_an_ttltweb.controller.adminPage7;

import com.example.do_an_ttltweb.model.Banner;
import com.example.do_an_ttltweb.services.BannerService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet(name = "AdminBannerServlet", value = "/admin/banner")
public class AdminPage7Servlet extends HttpServlet {

    private final BannerService bannerService = new BannerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Banner> listBanner = bannerService.getAllBanners();
        request.setAttribute("listBanner", listBanner);
        request.getRequestDispatcher("/adminPage7.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/admin/banner");
            return;
        }

        switch (action) {
            case "add":
                handleAddBanner(request, response);
                break;
            case "update":
                handleUpdateBanner(request, response);
                break;
            case "delete":
                handleDeleteBanner(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/admin/banner");
                break;
        }
    }

    private void handleAddBanner(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        try {
            Banner b = new Banner();
            b.setBanner_url(request.getParameter("banner_url"));
            b.setStatus(request.getParameter("status"));
            b.setStart_date(parseDatetime(request.getParameter("start")));
            b.setEnd_date(parseDatetime(request.getParameter("end")));

            if (bannerService.addBanner(b)) {
                session.setAttribute("notice", "Thêm thành công");
            } else {
                session.setAttribute("notice", "Thêm thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("notice", "Thêm thất bại: Lỗi định dạng dữ liệu");
        }
        response.sendRedirect(request.getContextPath() + "/admin/banner");
    }

    private void handleUpdateBanner(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        try {
            int bid = Integer.parseInt(request.getParameter("bid"));
            Banner b = new Banner();
            b.setBanner_url(request.getParameter("up_url"));
            b.setStatus(request.getParameter("up_status"));
            b.setStart_date(parseDatetime(request.getParameter("up_start")));
            b.setEnd_date(parseDatetime(request.getParameter("up_end")));

            if (bannerService.updateBanner(bid, b)) {
                session.setAttribute("notice_up", "Cập nhật thành công");
            } else {
                session.setAttribute("notice_up", "Cập nhật không thành công");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("notice_up", "ID Banner không hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("notice_up", "Cập nhật thất bại: Lỗi định dạng");
        }
        response.sendRedirect(request.getContextPath() + "/admin/banner");
    }

    private void handleDeleteBanner(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        try {
            int bid = Integer.parseInt(request.getParameter("bid"));
            if (bannerService.deleteBanner(bid)) {
                session.setAttribute("noticeDel", "Xóa thành công");
            } else {
                session.setAttribute("noticeDel", "Xóa thất bại");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("noticeDel", "Lỗi: ID Banner không hợp lệ");
        }
        response.sendRedirect(request.getContextPath() + "/admin/banner");
    }

    private Timestamp parseDatetime(String datetimeLocal) {
        if (datetimeLocal == null || datetimeLocal.isEmpty()) return null;
        if (datetimeLocal.length() == 16) {
            datetimeLocal += ":00";
        }
        return Timestamp.valueOf(datetimeLocal.replace("T", " "));
    }
}