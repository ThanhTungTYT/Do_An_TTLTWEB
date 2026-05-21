
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
        <p>Trạng thái:
            <span class="order-status ${o.status}">
                <c:choose>
                    <c:when test="${o.status == 'Đang xử lý'}">Đang xử lý</c:when>
                    <c:when test="${o.status == 'Đang giao'}">Đang giao</c:when>
                    <c:when test="${o.status == 'Đã nhận'}">Đã nhận</c:when>
                    <c:when test="${o.status == 'Đã hủy'}">Đã hủy</c:when>
                    <c:otherwise>Không xác định</c:otherwise>
                </c:choose>
            </span>
        </p>
        <c:if test="${o.status == 'Đang giao' and not empty o.ghnOrderCode}">
            <div style="background:#f0f8ff;padding:10px;border-radius:8px;margin:10px 0;">
                <p>Trạng thái vận chuyển GHN:
                    <strong id="ghn-status-${o.id}">Đang tải...</strong>
                </p>
                <p>Mã vận đơn: <strong>${o.ghnOrderCode}</strong></p>
            </div>
            <script>
                fetch('${pageContext.request.contextPath}/api/ghn/tracking?code=${o.ghnOrderCode}')
                    .then(r => r.json())
                    .then(data => {
                        document.getElementById('ghn-status-${o.id}').textContent =
                            data.statusVi || 'Không xác định';
                    })
                    .catch(() => {
                        document.getElementById('ghn-status-${o.id}').textContent = 'Không thể tải';
                    });
            </script>
        </c:if>
        <p>Tổng tiền: <fmt:formatNumber value="${o.finalAmount}" type="number" maxFractionDigits="0"/> VND</p>
        <c:forEach items="${o.items}" var="it">
            <div style="display:flex;gap:15px;margin-bottom:10px">
                <img src="${it.product.image_url}" width="80" height="80"
                     style="object-fit:cover;border-radius:5px">
                <div>
                    <strong>${it.product.name}</strong><br>
                    Số lượng: ${it.quantity}<br>
                    Giá: <fmt:formatNumber value="${it.price}" type="number" maxFractionDigits="0"/> VND
                </div>
            </div>
        </c:forEach>
        <c:if test="${o.status == 'Đang giao'}">
            <form action="${pageContext.request.contextPath}/confirm-received" method="post">
                <input type="hidden" name="orderId" value="${o.id}">
                <button type="submit"
                        onclick="return confirm('Xác nhận đã nhận hàng?')" style="background: cornflowerblue">
                    Đã nhận hàng
                </button>
            </form>
        </c:if>
        <c:if test="${o.status == 'Đang xử lý'}">
            <form action="${pageContext.request.contextPath}/cancel-order" method="post">
                <input type="hidden" name="orderId" value="${o.id}">
                <button type="submit"
                        onclick="return confirm('Bạn có chắc muốn hủy đơn hàng này?')">
                    Hủy đơn
                </button>
            </form>
        </c:if>
    </div>
</c:forEach>
</body>
</html>
