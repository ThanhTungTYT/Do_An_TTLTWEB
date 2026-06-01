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
        String pageStr = request.getParameter("page");
        int page = 1;
        int limit = 5;
        try {
            if (pageStr != null) {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            }
        } catch (NumberFormatException ignored) {}

        int totalBanners = bannerService.countBanners();
        int totalPages = (int) Math.ceil((double) totalBanners / limit);
        if (totalPages == 0) totalPages = 1;

        if (page > totalPages) page = totalPages;

        int offset = (page - 1) * limit;
        List<Banner> listBanner = bannerService.getBannersPaginated(limit, offset);

        request.setAttribute("listBanner", listBanner);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/adminPage7.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        String pageStr = request.getParameter("page");
        int page = 1;
        try {
            if (pageStr != null) page = Integer.parseInt(pageStr);
        } catch (NumberFormatException ignored) {}

        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/admin/banner?page=" + page);
            return;
        }

        switch (action) {
            case "add":
                handleAddBanner(request, response, page);
                break;
            case "update":
                handleUpdateBanner(request, response, page);
                break;
            case "delete":
                handleDeleteBanner(request, response, page);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/admin/banner?page=" + page);
                break;
        }
    }

    private void handleAddBanner(HttpServletRequest request, HttpServletResponse response, int page) throws IOException {
        HttpSession session = request.getSession();

        Timestamp start = parseDatetime(request.getParameter("start"));
        Timestamp end = parseDatetime(request.getParameter("end"));

        if (start == null || end == null) {
            session.setAttribute("error", "Thêm thất bại: Vui lòng nhập đầy đủ thời gian bắt đầu và kết thúc!");
            response.sendRedirect(request.getContextPath() + "/admin/banner?page=" + page);
            return;
        }
        if (start.after(end)) {
            session.setAttribute("error", "Thêm thất bại: Thời gian bắt đầu không được sau thời gian kết thúc!");
            response.sendRedirect(request.getContextPath() + "/admin/banner?page=" + page);
            return;
        }

        try {
            String status = request.getParameter("status");
            if (status == null || status.isBlank()) {
                session.setAttribute("error", "Vui lòng chọn trạng thái!");
                response.sendRedirect(request.getContextPath() + "/admin/banner?page=" + page);
                return;
            }

            Banner b = new Banner();
            b.setBanner_url(request.getParameter("banner_url"));
            b.setStatus(status);
            b.setStart_date(start);
            b.setEnd_date(end);

            if (bannerService.addBanner(b)) {
                session.setAttribute("success", "Thêm banner thành công");
                page = 1;
            } else {
                session.setAttribute("error", "Thêm banner thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Thêm banner thất bại: Lỗi định dạng dữ liệu");
        }
        response.sendRedirect(request.getContextPath() + "/admin/banner?page=" + page);
    }

    private void handleUpdateBanner(HttpServletRequest request, HttpServletResponse response, int page) throws IOException {
        HttpSession session = request.getSession();

        Timestamp upStart = parseDatetime(request.getParameter("up_start"));
        Timestamp upEnd = parseDatetime(request.getParameter("up_end"));

        if (upStart == null || upEnd == null) {
            session.setAttribute("error", "Cập nhật thất bại: Vui lòng nhập đầy đủ thời gian!");
            response.sendRedirect(request.getContextPath() + "/admin/banner?page=" + page);
            return;
        }
        if (upStart.after(upEnd)) {
            session.setAttribute("error", "Cập nhật thất bại: Thời gian bắt đầu không được sau thời gian kết thúc!");
            response.sendRedirect(request.getContextPath() + "/admin/banner?page=" + page);
            return;
        }

        try {
            int bid = Integer.parseInt(request.getParameter("bid"));
            String userStatus = request.getParameter("up_status");
            if (userStatus == null || userStatus.isBlank()) {
                session.setAttribute("error", "Vui lòng chọn trạng thái!");
                response.sendRedirect(request.getContextPath() + "/admin/banner?page=" + page);
                return;
            }

            Banner b = new Banner();
            b.setBanner_url(request.getParameter("up_url"));
            b.setStatus(userStatus);
            b.setStart_date(upStart);
            b.setEnd_date(upEnd);

            if (bannerService.updateBanner(bid, b)) {
                session.setAttribute("success", "Cập nhật banner thành công");
            } else {
                session.setAttribute("error", "Cập nhật banner không thành công");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("error", "ID Banner không hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Cập nhật banner thất bại: Lỗi định dạng");
        }
        response.sendRedirect(request.getContextPath() + "/admin/banner?page=" + page);
    }

    private void handleDeleteBanner(HttpServletRequest request, HttpServletResponse response, int page) throws IOException {
        HttpSession session = request.getSession();
        try {
            int bid = Integer.parseInt(request.getParameter("bid"));
            if (bannerService.deleteBanner(bid)) {
                session.setAttribute("success", "Xóa banner thành công");
            } else {
                session.setAttribute("error", "Xóa banner thất bại");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Lỗi: ID Banner không hợp lệ");
        }
        response.sendRedirect(request.getContextPath() + "/admin/banner?page=" + page);
    }

    private Timestamp parseDatetime(String datetimeLocal) {
        if (datetimeLocal == null || datetimeLocal.isEmpty()) return null;
        if (datetimeLocal.length() == 16) {
            datetimeLocal += ":00";
        }
        return Timestamp.valueOf(datetimeLocal.replace("T", " "));
    }
}