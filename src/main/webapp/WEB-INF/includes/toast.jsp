<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="toastSuccessMsg" value="${not empty success ? success : message}"/>
<c:set var="hasSuccess" value="${not empty success or not empty message}"/>
<c:if test="${hasSuccess or not empty error}">
    <div id="toast-notification" class="toast ${hasSuccess ? 'toast-success' : 'toast-error'}">
        <i class="${hasSuccess ? 'fas fa-check-circle' : 'fas fa-exclamation-triangle'}"></i>
        <span>${hasSuccess ? toastSuccessMsg : error}</span>
    </div>
    <script>
        (function () {
            var t = document.getElementById('toast-notification');
            if (t) setTimeout(function () { t.remove(); }, 3000);
            if (window.history && window.history.replaceState) {
                try {
                    var url = new URL(window.location.href);
                    var changed = false;
                    ['success', 'msg', 'error'].forEach(function (k) {
                        if (url.searchParams.has(k)) {
                            url.searchParams.delete(k);
                            changed = true;
                        }
                    });
                    if (changed) {
                        window.history.replaceState({}, document.title, url.pathname + url.search + url.hash);
                    }
                } catch (e) {}
            }
        })();
    </script>
</c:if>
<c:remove var="success" scope="session"/>
<c:remove var="message" scope="session"/>
<c:remove var="error" scope="session"/>
