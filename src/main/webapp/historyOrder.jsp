
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
<h2>Lịch sử mua hàng</h2>
<p>Danh sách các đơn hàng gần đây của bạn:</p>

<c:if test="${empty orders}">
    <p>Bạn chưa có đơn hàng nào.</p>
</c:if>

<c:forEach items="${orders}" var="o">
    <div class="order-item" style="border:1px solid #ccc;padding:15px;margin-bottom:20px">

        <h3>Đơn hàng #DH${o.id}</h3>
        <p>
            Trạng thái:
            <span class="order-status ${o.status}">
                <c:choose>
                    <c:when test="${o.status == 'Đang xử lý'}">Đang xử lý</c:when>
                    <c:when test="${o.status == 'Đã giao'}">Đã giao</c:when>
                    <c:when test="${o.status == 'Đã hủy'}">Đã hủy</c:when>
                    <c:otherwise>Không xác định</c:otherwise>
                </c:choose>
            </span>
        </p>
        <p>Tổng tiền: <fmt:formatNumber value="${o.finalAmount}" type="number" maxFractionDigits="0"/> VND</p>

        <h4>Sản phẩm đã mua:</h4>

        <c:forEach items="${o.items}" var="it">
            <div style="display:flex;gap:15px;margin-bottom:10px">
                <img src="${it.product.image_url}"
                     width="80" height="80"
                     style="object-fit:cover;border-radius:5px">

                <div>
                    <strong>${it.product.name}</strong><br>
                    Số lượng: ${it.quantity}<br>
                    Giá: <fmt:formatNumber value="${it.price}" type="number" maxFractionDigits="0"/> VND
                </div>
            </div>
        </c:forEach>
        <c:if test="${o.status == 'Đang xử lý'}">
            <form action="${pageContext.request.contextPath}/cancel-order" method="post">
                <input type="hidden" name="orderId" value="${o.id}">
                <button type="submit">Hủy đơn</button>
            </form>

        </c:if>
    </div>
</c:forEach>
</body>
</html>
