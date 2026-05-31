<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="/WEB-INF/includes/toast.jsp"/>
<h2>Lịch sử mua hàng</h2>
<p>Danh sách các đơn hàng gần đây của bạn:</p>

<c:if test="${empty orders}">
    <p>Bạn chưa có đơn hàng nào.</p>
</c:if>

<div class="status-filter-wrapper">
    <p class="status-filter-title">Trạng thái đơn hàng</p>
    <div class="status-filter">
        <button class="filter-btn active" onclick="filterOrders('all', this)">Tất cả</button>
        <button class="filter-btn" onclick="filterOrders('Đang xử lý', this)">Đang xử lý</button>
        <button class="filter-btn" onclick="filterOrders('Chờ thanh toán', this)">Chờ thanh toán</button>
        <button class="filter-btn" onclick="filterOrders('Đang giao', this)">Đang giao</button>
        <button class="filter-btn" onclick="filterOrders('Đã nhận', this)">Đã nhận</button>
        <button class="filter-btn" onclick="filterOrders('Đã hủy', this)">Đã hủy</button>
        <button class="filter-btn" onclick="filterOrders('Đã hoàn trả', this)">Đã hoàn trả</button>
    </div>
</div>

<p id="empty-filter-msg" style="display:none; color:#999; text-align:center; padding:20px;">
    Không có đơn hàng nào trong trạng thái này.
</p>

<c:forEach items="${orders}" var="o">
    <div class="order-item" data-status="${o.status}" style="border:1px solid #ccc;padding:15px;margin-bottom:20px">
        <h3>Đơn hàng #DH${o.id}</h3>
        <p>Trạng thái:
            <span class="order-status ${o.status}">
                <c:choose>
                    <c:when test="${o.status == 'Chờ thanh toán'}"> Chờ thanh toán</c:when>
                    <c:when test="${o.status == 'Đang xử lý'}"> Đang xử lý</c:when>
                    <c:when test="${o.status == 'Đang giao'}"> Đang giao</c:when>
                    <c:when test="${o.status == 'Đã nhận'}"> Đã nhận</c:when>
                    <c:when test="${o.status == 'Đã hủy'}"> Đã hủy</c:when>
                    <c:when test="${o.status == 'Đã hoàn trả'}">↩️ Đã hoàn trả</c:when>
                    <c:otherwise>${o.status}</c:otherwise>
                </c:choose>
            </span>
        </p>

        <c:if test="${o.status == 'Đang giao' and not empty o.ghnOrderCode}">
            <div style="background:#f0f8ff;padding:10px;border-radius:8px;margin:10px 0;">
                <p>Mã vận đơn GHN: <strong>${o.ghnOrderCode}</strong></p>
            </div>
        </c:if>

        <p>Tổng tiền: <fmt:formatNumber value="${o.finalAmount}" type="number" maxFractionDigits="0"/> VND</p>

        <c:forEach items="${o.items}" var="it">
            <div style="display:flex;gap:15px;margin-bottom:10px">
                <img src="<c:url value='${it.product.image_url}'/>" width="80" height="80" style="object-fit:cover;border-radius:5px" onerror="this.src='https://placehold.co/80'">
                <div>
                    <strong>${it.product.name}</strong><br>
                    Số lượng: ${it.quantity}<br>
                    Giá: <fmt:formatNumber value="${it.price}" type="number" maxFractionDigits="0"/> VND
                </div>
            </div>
        </c:forEach>

        <c:if test="${o.status == 'Đang giao'}">
            <c:set var="itemsJson" value="[" />
            <c:forEach items="${o.items}" var="it" varStatus="its">
                <c:set var="pName" value="${fn:replace(it.product.name, '\"', '\\\"')}" />
                <c:url value="${it.product.image_url}" var="itemImgUrl"/>
                <c:set var="itemsJson" value="${itemsJson}{\"name\":\"${fn:escapeXml(pName)}\",\"image\":\"${itemImgUrl}\",\"quantity\":${empty it.quantity ? 0 : it.quantity},\"price\":${empty it.price ? 0 : it.price}}" />
                <c:if test="${!its.last}"><c:set var="itemsJson" value="${itemsJson}," /></c:if>
            </c:forEach>
            <c:set var="itemsJson" value="${itemsJson}]" />
            <button class="btn-detail"
                    data-id="${o.id}"
                    data-name="${fn:escapeXml(o.receiverName)}"
                    data-phone="${fn:escapeXml(o.receiverPhone)}"
                    data-province="${fn:escapeXml(orderAddressMap[o.id].province)}"
                    data-district="${fn:escapeXml(orderAddressMap[o.id].district)}"
                    data-ward="${fn:escapeXml(orderAddressMap[o.id].ward)}"
                    data-address="${fn:escapeXml(orderAddressMap[o.id].address)}"
                    data-ghn="${o.ghnOrderCode}"
                    data-district-id="${orderAddressMap[o.id].districtId}"
                    data-ward-code="${orderAddressMap[o.id].wardCode}"
                    onclick="openOrderModal(this)"
                    style="background:#A0522D;color:white;width:auto;padding:7px 14px;border-radius:6px;">
                Xem chi tiết vận chuyển
            </button>
        </c:if>

        <c:if test="${o.status == 'Đang giao'}">
            <form action="${pageContext.request.contextPath}/confirm-received" method="post" style="display:inline-block; margin-left:5px;">
                <input type="hidden" name="orderId" value="${o.id}">
                <button type="submit" onclick="return confirm('Xác nhận đã nhận hàng?')" style="background: cornflowerblue; color: white; width: auto; padding: 7px 14px; border-radius: 6px;">
                    Đã nhận hàng
                </button>
            </form>
        </c:if>

        <c:if test="${o.status == 'Đang xử lý'}">
            <form action="${pageContext.request.contextPath}/cancel-order" method="post">
                <input type="hidden" name="orderId" value="${o.id}">
                <button type="button" onclick="openCancelModal('${o.id}')" style="background: red; color: white; width: auto; padding: 7px 14px; border-radius: 6px;">
                    Hủy đơn
                </button>
            </form>
        </c:if>
    </div>
</c:forEach>

<div class="overlay" id="detail-overlay" onclick="closeDetailIfOutside(event)">
    <div class="detail-modal">
        <button class="close-modal-btn" onclick="closeDetail()">✕</button>

        <h3>Chi tiết đơn hàng #<span id="d-order-id"></span></h3>

        <div class="detail-section">
            <h4>Thông tin người nhận</h4>
            <div class="detail-row"><span class="label">Họ và tên:</span><span id="d-name"></span></div>
            <div class="detail-row"><span class="label">Số điện thoại:</span><span id="d-phone"></span></div>
        </div>

        <div class="detail-section">
            <h4>Địa chỉ giao hàng</h4>
            <div class="detail-row"><span class="label">Tỉnh/Thành:</span><span id="d-province"></span></div>
            <div class="detail-row"><span class="label">Quận/Huyện:</span><span id="d-district"></span></div>
            <div class="detail-row"><span class="label">Phường/Xã:</span><span id="d-ward"></span></div>
            <div class="detail-row"><span class="label">Địa chỉ:</span><span id="d-address"></span></div>
        </div>

        <div class="detail-section">
            <h4>Trạng thái vận chuyển GHN</h4>
            <div class="ghn-box">
                <div class="detail-row"><span class="label">Mã vận đơn:</span><strong id="d-ghn-code"></strong></div>
                <div class="detail-row"><span class="label">Trạng thái:</span><strong id="d-ghn-status">Đang tải<span class="spinner"></span></strong></div>
                <div class="detail-row">
                    <span class="label">Dự kiến giao:</span>
                    <strong id="d-leadtime" style="color:#27ae60;">
                        Đang tải<span class="spinner"></span>
                    </strong>
                </div>
            </div>
        </div>
    </div>
</div>

<form id="cancel-order-form"
      action="${pageContext.request.contextPath}/cancel-order"
      method="post" style="display:none;">
    <input type="hidden" name="orderId" id="cancel-order-id">
    <input type="hidden" name="cancelReason" id="cancel-reason-value">
</form>

<div class="cancel-overlay" id="cancel-overlay" onclick="closeCancelIfOutside(event)">
    <div class="cancel-modal">

        <h3>Hủy đơn hàng #<span id="cancel-order-display-id"></span></h3>
        <p>Vui lòng chọn lý do hủy đơn hàng của bạn:</p>

        <div class="reason-list">
            <label class="reason-item">
                <input type="radio" name="cancelReason" value="Sản phẩm không còn cần thiết">
                Sản phẩm không còn cần thiết
            </label>
            <label class="reason-item">
                <input type="radio" name="cancelReason" value="Đổi thông tin nhận hàng">
                Đổi thông tin nhận hàng
            </label>
            <label class="reason-item">
                <input type="radio" name="cancelReason" value="Đặt nhầm sản phẩm">
                Đặt nhầm sản phẩm
            </label>
            <label class="reason-item">
                <input type="radio" name="cancelReason" value="Đổi phương thức thanh toán">
                Đổi phương thức thanh toán
            </label>
        </div>

        <div class="cancel-modal-actions">
            <button class="btn-back" onclick="closeCancelModal()">
                Quay lại
            </button>
            <button class="btn-confirm-cancel" id="btn-confirm-cancel"
                    onclick="submitCancel()" disabled>
                Xác nhận hủy đơn
            </button>
        </div>
    </div>
</div>

<script>
    window.openOrderModal = function(btn) {
        try {
            const orderId    = btn.getAttribute('data-id');
            const name       = btn.getAttribute('data-name');
            const phone      = btn.getAttribute('data-phone');
            const province   = btn.getAttribute('data-province') || '—';
            const district   = btn.getAttribute('data-district') || '—';
            const ward       = btn.getAttribute('data-ward') || '—';
            const address    = btn.getAttribute('data-address') || '—';
            const ghnCode    = btn.getAttribute('data-ghn');
            const districtId = btn.getAttribute('data-district-id');  // ✅
            const wardCode   = btn.getAttribute('data-ward-code');    // ✅

            document.getElementById('d-order-id').textContent = orderId;
            document.getElementById('d-name').textContent = name;
            document.getElementById('d-phone').textContent = phone;
            document.getElementById('d-province').textContent = province;
            document.getElementById('d-district').textContent = district;
            document.getElementById('d-ward').textContent = ward;
            document.getElementById('d-address').textContent = address;
            document.getElementById('d-ghn-code').textContent = ghnCode || 'Chưa có';
            document.getElementById('d-ghn-status').innerHTML =
                'Đang tải<span class="spinner"></span>';
            document.getElementById('d-leadtime').innerHTML =
                'Đang tải<span class="spinner"></span>';

            document.getElementById('detail-overlay').classList.add('show');
            const contextPath = '${pageContext.request.contextPath}';
            if (ghnCode && ghnCode.trim() !== '') {
                fetch(contextPath + '/api/ghn/tracking?code=' + ghnCode)
                    .then(r => r.ok ? r.json() : Promise.reject())
                    .then(data => {
                        document.getElementById('d-ghn-status').textContent =
                            data.statusVi || 'Không xác định';
                    })
                    .catch(() => {
                        document.getElementById('d-ghn-status').textContent = 'Không thể tải';
                    });
            } else {
                document.getElementById('d-ghn-status').textContent = 'Chưa có mã vận đơn';
            }
            if (districtId && wardCode) {
                fetch(contextPath + '/api/ghn/leadtime?district_id=' + districtId +
                    '&ward_code=' + wardCode)
                    .then(r => r.ok ? r.json() : Promise.reject())
                    .then(data => {
                        document.getElementById('d-leadtime').textContent =
                            data.leadtime ? '' + data.leadtime : 'Không xác định';
                    })
                    .catch(() => {
                        document.getElementById('d-leadtime').textContent = 'Không thể tải';
                    });
            } else {
                document.getElementById('d-leadtime').textContent = 'Không có thông tin';
            }

        } catch (error) {
            console.error('Lỗi xử lý popup:', error);
        }
    };

    window.closeDetail = function() {
        document.getElementById('detail-overlay').classList.remove('show');
    };

    window.closeDetailIfOutside = function(event) {
        if (event.target === document.getElementById('detail-overlay')) {
            window.closeDetail();
        }
    };
    document.querySelectorAll('input[name="cancelReason"]').forEach(radio => {
        radio.addEventListener('change', function() {
            document.querySelectorAll('.reason-item').forEach(item => {
                item.classList.remove('selected');
            });
            this.closest('.reason-item').classList.add('selected');
            document.getElementById('btn-confirm-cancel').disabled = false;
        });
    });

    function openCancelModal(orderId) {
        document.getElementById('cancel-order-id').value = orderId;
        document.getElementById('cancel-order-display-id').textContent = orderId;

        document.querySelectorAll('input[name="cancelReason"]').forEach(r => r.checked = false);
        document.querySelectorAll('.reason-item').forEach(item => item.classList.remove('selected'));
        document.getElementById('btn-confirm-cancel').disabled = true;

        document.getElementById('cancel-overlay').classList.add('show');
    }

    function closeCancelModal() {
        document.getElementById('cancel-overlay').classList.remove('show');
    }

    function closeCancelIfOutside(event) {
        if (event.target === document.getElementById('cancel-overlay')) {
            closeCancelModal();
        }
    }

    function submitCancel() {
        const selected = document.querySelector('input[name="cancelReason"]:checked');
        if (!selected) {
            alert('Vui lòng chọn lý do hủy đơn!');
            return;
        }
        document.getElementById('cancel-reason-value').value = selected.value;
        document.getElementById('cancel-order-form').submit();
    }

    function filterOrders(status, btn) {
        document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');

        document.querySelectorAll('.order-item').forEach(item => {
            if (status === 'all' || item.getAttribute('data-status') === status) {
                item.style.display = 'block';
            } else {
                item.style.display = 'none';
            }
        });

        const visible = [...document.querySelectorAll('.order-item')]
            .filter(item => item.style.display !== 'none');

        const emptyMsg = document.getElementById('empty-filter-msg');
        if (visible.length === 0) {
            emptyMsg.style.display = 'block';
        } else {
            emptyMsg.style.display = 'none';
        }
    }
</script>
