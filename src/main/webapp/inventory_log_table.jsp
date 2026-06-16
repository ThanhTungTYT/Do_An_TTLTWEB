<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<table style="width:100%; border-collapse: collapse; text-align:center;">
    <thead style="background-color: #f5f5f5; font-weight: bold;">
    <tr>
        <th style="padding:12px; border-bottom:2px solid #ddd;">Mã SP</th>
        <th style="padding:12px; border-bottom:2px solid #ddd;">Tên sản phẩm</th>
        <th style="padding:12px; border-bottom:2px solid #ddd;">Hành động</th>
        <th style="padding:12px; border-bottom:2px solid #ddd;">Số lượng</th>
        <th style="padding:12px; border-bottom:2px solid #ddd;">Thời gian</th>
    </tr>
    </thead>
    <tbody>
    <c:choose>
        <c:when test="${empty inventoryLogs}">
            <tr>
                <td colspan="5" style="padding:20px; color:#888;">Chưa có nhật ký biến động kho nào.</td>
            </tr>
        </c:when>
        <c:otherwise>
            <c:forEach items="${inventoryLogs}" var="log">
                <tr style="border-bottom: 1px solid #eee;">
                    <td style="padding:12px;">#${log.product_id}</td>
                    <td style="padding:12px; text-align:left; font-weight:bold;">${log.product_name}</td>

                    <td style="padding:12px;">
                        <c:choose>
                            <c:when test="${log.action_type eq 'IMPORT'}">
                                <span style="background:#4caf50; color:white; padding:4px 8px; border-radius:4px; font-size:12px;">Nhập kho</span>
                            </c:when>
                            <c:otherwise>
                                <span style="background:#f44336; color:white; padding:4px 8px; border-radius:4px; font-size:12px;">Xuất kho</span>
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <td style="padding:12px; font-weight:bold; color: ${log.action_type eq 'IMPORT' ? '#4caf50' : '#f44336'};">
                            ${log.quantity}
                    </td>

                    <td style="padding:12px; color:#666;">
                        <fmt:formatDate value="${log.created_at}" pattern="dd/MM/yyyy HH:mm"/>
                    </td>
                </tr>
            </c:forEach>
        </c:otherwise>
    </c:choose>
    </tbody>
</table>