<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="toastSuccessMsg" value="${not empty success ? success : message}"/>
<c:set var="hasSuccess" value="${not empty success or not empty message}"/>
<c:if test="${hasSuccess or not empty error}">
    <div id="toast-notification" class="toast ${hasSuccess ? 'toast-success' : 'toast-error'}">
        <i class="${hasSuccess ? 'fas fa-check-circle' : 'fas fa-exclamation-triangle'}"></i>
        <span>${hasSuccess ? toastSuccessMsg : error}</span>
    </div>
</c:if>
