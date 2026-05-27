document.addEventListener("DOMContentLoaded", function () {

    const ctx = window.contextPath || "/Do_An_TTLTWEB";

    var promotionSelect = document.getElementById("promotionSelect");
    var totalEl         = document.getElementById("total-price");
    var shippingEl      = document.getElementById("shipping-fee");
    var discountEl      = document.getElementById("discount-amount");
    var finalEl         = document.getElementById("final-total");

    let paymentPollingInterval = null;

    function formatVND(amount) {
        return amount.toLocaleString('vi-VN') + " VND";
    }

    function getFinalAmount() {
        var total    = parseFloat(totalEl.getAttribute("data-total"))  || 0;
        var shipping = parseFloat(shippingEl.getAttribute("data-fee")) || 0;
        var selectedOption  = promotionSelect.options[promotionSelect.selectedIndex];
        var discountPercent = parseFloat(selectedOption.getAttribute("data-discount")) || 0;
        var discountValue   = (total * discountPercent) / 100;
        return { total, shipping, discountValue, finalTotal: total + shipping - discountValue };
    }

    function calculateTotal() {
        var { discountValue, finalTotal } = getFinalAmount();
        if (discountEl) discountEl.innerHTML = discountValue > 0 ? "- " + formatVND(discountValue) : "0 VND";
        if (finalEl)    finalEl.innerHTML    = formatVND(finalTotal);
        updateQrAmount();
    }

    if (promotionSelect) {
        promotionSelect.addEventListener("change", calculateTotal);
    }

    const checkoutForm    = document.getElementById('checkout-form');
    const fullnameInput   = document.getElementById('fullname');
    const phoneInput      = document.getElementById('phone');
    const addressInput    = document.getElementById('address');
    const citySelect      = document.getElementById("provinceSelect");
    const wardSelect      = document.getElementById("wardSelect");
    const districtSelect  = document.getElementById("districtSelect");
    const hiddenProvince  = document.getElementById("hidden_province");
    const hiddenDistrict  = document.getElementById("hidden_district");
    const hiddenWard      = document.getElementById("hidden_ward");

    function showError(input, message) {
        if (!input) return;
        input.classList.add('input-error');
        let errLabel = input.nextElementSibling;
        if (errLabel && errLabel.classList.contains('error')) {
            errLabel.textContent = message;
            errLabel.style.display = 'block';
        }
    }

    function clearError(input) {
        if (!input) return;
        input.classList.remove('input-error');
        let errLabel = input.nextElementSibling;
        if (errLabel && errLabel.classList.contains('error')) {
            errLabel.textContent = '';
            errLabel.style.display = 'none';
        }
    }

    function validateFullName() {
        const val = fullnameInput.value.trim();
        const regexName = /^[\p{L}\s]+$/u;
        if (val === '') { showError(fullnameInput, 'Họ và tên không được để trống!'); return false; }
        if (!regexName.test(val)) { showError(fullnameInput, 'Họ tên không được chứa số hay kí tự đặc biệt!'); return false; }
        clearError(fullnameInput); return true;
    }

    function validatePhone() {
        const val = phoneInput.value.trim();
        const regexPhone = /^(03|05|07|08|09)\d{8}$/;
        if (val === '') { showError(phoneInput, 'Số điện thoại không được để trống!'); return false; }
        if (!regexPhone.test(val)) { showError(phoneInput, 'Số điện thoại không hợp lệ (Gồm 10 số, bắt đầu bằng 03,05,07,08,09)!'); return false; }
        clearError(phoneInput); return true;
    }

    function validateAddress() {
        const val = addressInput.value.trim();
        const regexAddress = /^[\p{L}0-9\s,.\-\/]+$/u;
        if (val === '') { showError(addressInput, 'Địa chỉ không được để trống!'); return false; }
        if (!regexAddress.test(val)) { showError(addressInput, 'Địa chỉ chứa kí tự đặc biệt không hợp lệ!'); return false; }
        clearError(addressInput); return true;
    }

    function validateLocation() {
        let isValid = true;

        if (!citySelect || citySelect.value === '') { showError(citySelect, 'Vui lòng chọn Tỉnh/Thành phố!'); isValid = false; }
        else clearError(citySelect);

        if (!districtSelect || districtSelect.value === '') { showError(districtSelect, 'Vui lòng chọn Quận/Huyện!'); isValid = false; }
        else clearError(districtSelect);

        if (!wardSelect || wardSelect.value === '') { showError(wardSelect, 'Vui lòng chọn Phường/Xã!'); isValid = false; }
        else clearError(wardSelect);

        return isValid;
    }

    function validateAll() {
        const a = validateFullName();
        const b = validatePhone();
        const c = validateAddress();
        const d = validateLocation();
        return a && b && c && d;
    }

    if (fullnameInput) fullnameInput.addEventListener('input', validateFullName);
    if (phoneInput)    phoneInput.addEventListener('input', validatePhone);
    if (addressInput)  addressInput.addEventListener('input', validateAddress);

    // ==========================================================
    // KHỐI HÀM CALL API NỘI BỘ GHN CHUẨN (FIX KHỚP VALUE ID)
    // ==========================================================
    async function loadProvinces() {
        try {
            const res = await fetch(`${ctx}/api/ghn/provinces`);
            const data = await res.json();
            if (citySelect) {
                citySelect.innerHTML = '<option value="">-- Chọn Tỉnh/Thành phố --</option>';
                data.forEach(p => {
                    const opt = document.createElement('option');
                    opt.value = p.ProvinceID; // Trả về ID số của GHN (VD: 214)
                    opt.text = p.ProvinceName;
                    citySelect.appendChild(opt);
                });
            }
        } catch (e) { console.error('Lỗi tải danh sách Tỉnh:', e); }
    }

    async function loadDistricts(provinceId, provinceName) {
        if (!provinceId) return;
        if (hiddenProvince) hiddenProvince.value = provinceName;

        if (districtSelect) {
            districtSelect.innerHTML = '<option value="">-- Chọn Quận/Huyện --</option>';
            districtSelect.disabled = true;
        }
        if (wardSelect) {
            wardSelect.innerHTML = '<option value="">-- Chọn Phường/Xã --</option>';
            wardSelect.disabled = true;
        }

        try {
            const res = await fetch(`${ctx}/api/ghn/districts?province_id=${provinceId}`);
            const data = await res.json();
            if (districtSelect) {
                data.forEach(d => {
                    const opt = document.createElement('option');
                    opt.value = d.DistrictID;
                    opt.text = d.DistrictName;
                    districtSelect.appendChild(opt);
                });
                districtSelect.disabled = false;
            }
        } catch (e) { console.error('Lỗi tải danh sách Huyện:', e); }
        validateLocation();
    }

    async function loadWards(districtId, districtName) {
        if (!districtId) return;
        if (hiddenDistrict) hiddenDistrict.value = districtName;

        const hiddenDistrictId = document.getElementById('hidden_district_id');
        if (hiddenDistrictId) hiddenDistrictId.value = districtId;

        if (wardSelect) {
            wardSelect.innerHTML = '<option value="">-- Chọn Phường/Xã --</option>';
            wardSelect.disabled = true;
        }

        try {
            const res = await fetch(`${ctx}/api/ghn/wards?district_id=${districtId}`);
            const data = await res.json();
            if (wardSelect) {
                data.forEach(w => {
                    const opt = document.createElement('option');
                    opt.value = w.WardCode;
                    opt.text = w.WardName;
                    wardSelect.appendChild(opt);
                });
                wardSelect.disabled = false;
            }
        } catch (e) { console.error('Lỗi tải danh sách Xã:', e); }
        validateLocation();
    }

    async function onWardChange(wardCode, wardName) {
        if (!wardCode) return;
        if (hiddenWard) hiddenWard.value = wardName;

        const hiddenWardCode = document.getElementById('hidden_ward_code');
        if (hiddenWardCode) hiddenWardCode.value = wardCode;

        const hiddenDistrictId = document.getElementById('hidden_district_id');
        const districtId = hiddenDistrictId ? hiddenDistrictId.value : "";
        const weight = 500;

        if (!districtId) return;

        try {
            const res = await fetch(`${ctx}/api/ghn/fee?district_id=${districtId}&ward_code=${wardCode}&weight=${weight}`);
            const data = await res.json();
            const fee = data.fee || 30000;

            const hiddenShippingFee = document.getElementById('hidden_shipping_fee');
            if (hiddenShippingFee) hiddenShippingFee.value = fee;

            if (shippingEl) {
                shippingEl.textContent = fee.toLocaleString('vi-VN') + ' VND';
                shippingEl.setAttribute('data-fee', fee);
            }
            calculateTotal();
        } catch (e) { console.error('Lỗi tính phí ship:', e); }
        validateLocation();
    }

    // Gắn sự kiện lắng nghe thay đổi ô select
    if (citySelect) {
        citySelect.addEventListener("change", function () {
            if (this.value) {
                loadDistricts(this.value, this.options[this.selectedIndex].text);
            }
        });
    }

    if (districtSelect) {
        districtSelect.addEventListener("change", function () {
            if (this.value) {
                loadWards(this.value, this.options[this.selectedIndex].text);
            }
        });
    }

    if (wardSelect) {
        wardSelect.addEventListener("change", function () {
            if (this.value) {
                onWardChange(this.value, this.options[this.selectedIndex].text);
            }
        });
    }

    // ===== PHƯƠNG THỨC THANH TOÁN =====
    const paymentRadios     = document.querySelectorAll('input[name="paymentMethod"]');
    const bankInfoPanel     = document.getElementById("bank-info-panel");
    const ewalletInfoPanel  = document.getElementById("ewallet-info-panel");
    const placeOrderBtn     = document.getElementById("place-order-btn");
    const openBankModalBtn  = document.getElementById("open-bank-modal-btn");

    const BANK_METHODS = ["bank"];
    const EWALLET_METHODS = ["Ví điện tử"];

    function onPaymentMethodChange() {
        const selected = document.querySelector('input[name="paymentMethod"]:checked');
        if (!selected) return;
        const val = selected.value;

        if(bankInfoPanel) bankInfoPanel.style.display    = "none";
        if(ewalletInfoPanel) ewalletInfoPanel.style.display = "none";
        if(placeOrderBtn) placeOrderBtn.style.display    = "block";
        if(openBankModalBtn) openBankModalBtn.style.display = "none";

        if (BANK_METHODS.includes(val)) {
            if(bankInfoPanel) bankInfoPanel.style.display   = "block";
            if(placeOrderBtn) placeOrderBtn.style.display   = "none";
            if(openBankModalBtn) openBankModalBtn.style.display = "block";
        } else if (EWALLET_METHODS.includes(val)) {
            if(ewalletInfoPanel) ewalletInfoPanel.style.display = "block";
        }

        updateQrAmount();
        highlightSelectedPayment();
    }

    function highlightSelectedPayment() {
        document.querySelectorAll('.payment-option').forEach(opt => {
            opt.style.borderColor = '#eee';
            opt.style.background  = '';
        });
        const checked = document.querySelector('input[name="paymentMethod"]:checked');
        if (checked) {
            const label = checked.closest('.payment-option');
            if (label) {
                label.style.borderColor = '#c76739';
                label.style.background  = '#fff8f4';
            }
        }
    }

    paymentRadios.forEach(r => r.addEventListener("change", onPaymentMethodChange));
    highlightSelectedPayment();

    function updateQrAmount() {
        const selected = document.querySelector('input[name="paymentMethod"]:checked');
        if (!selected || !BANK_METHODS.includes(selected.value)) return;

        const { finalTotal } = getFinalAmount();
        const amountInt = Math.round(finalTotal);
        const contentEl = document.getElementById("transfer-content-text");
        const content   = contentEl ? contentEl.textContent.trim().replace(/\s+/g, '+') : "AROMACAFE";

        const qrImg = document.getElementById("qr-image");
        if (qrImg) {
            qrImg.src = `https://img.vietqr.io/image/BIDV-8800273817-compact2.png?amount=${amountInt}&addInfo=${content}&accountName=AROMA+CAFE`;
        }
    }

    if (placeOrderBtn) {
        placeOrderBtn.addEventListener("click", function () {
            if (!validateAll()) {
                const firstError = document.querySelector('.input-error');
                if (firstError) firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
                return;
            }
            setButtonLoading(placeOrderBtn, true);
            checkoutForm.submit();
        });
    }

    if (openBankModalBtn) {
        openBankModalBtn.addEventListener("click", function () {
            if (!validateAll()) {
                const firstError = document.querySelector('.input-error');
                if (firstError) firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
                return;
            }
            handleBankTransferCheckout();
        });
    }

    const modal             = document.getElementById("bank-payment-modal");
    const modalCloseX       = document.getElementById("modal-close-x");
    const confirmPaymentBtn = document.getElementById("confirm-payment-btn");
    const cancelPaymentBtn  = document.getElementById("cancel-payment-btn");
    const modalAmountEl     = document.getElementById("modal-amount-display");
    const modalQrImg        = document.getElementById("modal-qr-image");
    const modalTimestamp    = document.getElementById("modal-timestamp");

    async function handleBankTransferCheckout() {
        setButtonLoading(openBankModalBtn, true);

        const formData = new FormData(checkoutForm);
        const searchParams = new URLSearchParams(formData);

        try {
            const actionUrl = checkoutForm.getAttribute("action") || `${ctx}/payment`;
            const res = await fetch(actionUrl, {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: searchParams.toString()
            });
            const result = await res.json();

            if (result.success) {
                openBankModal(result.orderId, result.finalAmount);
            } else {
                alert("Lỗi đặt hàng: " + (result.message || "Vui lòng thử lại sau."));
            }
        } catch (e) {
            console.error("Lỗi gửi đơn hàng qua AJAX:", e);
            alert("Đã xảy ra lỗi hệ thống khi tạo đơn hàng. Vui lòng thử lại!");
        } finally {
            setButtonLoading(openBankModalBtn, false);
        }
    }

    function openBankModal(orderId, finalAmount) {
        if (!modal) return;
        const amountInt = Math.round(finalAmount);

        if (modalAmountEl) modalAmountEl.textContent = formatVND(amountInt);
        if (modalTimestamp) modalTimestamp.textContent = Date.now();

        const orderRef = document.getElementById("modal-order-ref");
        if (orderRef) orderRef.textContent = `AROMACAFE-${orderId}`;

        const transferContent = document.getElementById("modal-transfer-content");
        if (transferContent) transferContent.textContent = `AROMACAFE ${orderId}`;

        if (modalQrImg) {
            modalQrImg.src = `https://img.vietqr.io/image/BIDV-8800273817-compact2.png?amount=${amountInt}&addInfo=AROMACAFE+${orderId}&accountName=AROMA+CAFE`;
        }

        modal.style.display = "flex";
        document.body.style.overflow = "hidden";

        startSepayPolling(orderId);
    }

    function startSepayPolling(orderId) {
        if (paymentPollingInterval) clearInterval(paymentPollingInterval);
        paymentPollingInterval = setInterval(async () => {
            await checkOrderStatus(orderId);
        }, 3000);
    }

    async function checkOrderStatus(orderId) {
        try {
            const res = await fetch(`${ctx}/api/check-order-status?orderId=${orderId}`);
            const data = await res.json();

            if (data.status === "Đã thanh toán" || data.status === "Đang xử lý") {
                handlePaymentSuccess();
            }
        } catch (e) {
            console.error("Lỗi khi kiểm tra trạng thái đơn hàng từ polling:", e);
        }
    }

    function handlePaymentSuccess() {
        if (paymentPollingInterval) {
            clearInterval(paymentPollingInterval);
            paymentPollingInterval = null;
        }

        const modalBody = document.querySelector(".modal-box .modal-body");
        if (modalBody) {
            modalBody.innerHTML = `
                <div style="text-align: center; padding: 40px 20px;">
                    <i class="fas fa-check-circle" style="color: #28a745; font-size: 64px; margin-bottom: 20px;"></i>
                    <h3 style="color: #28a745; margin-bottom: 10px;">Thanh toán thành công!</h3>
                    <p>Hệ thống Aroma Café đã nhận được tiền của bạn.</p>
                    <p style="font-size: 14px; color: #666; margin-top: 8px;">Đang tự động chuyển hướng về trang tài khoản của bạn...</p>
                </div>
            `;
        }

        setTimeout(() => {
            window.location.href = `${ctx}/account?success=1`;
        }, 2500);
    }

    function closeModal() {
        if (!modal) return;
        modal.style.display = "none";
        document.body.style.overflow = "";

        if (paymentPollingInterval) {
            clearInterval(paymentPollingInterval);
            paymentPollingInterval = null;
        }
    }

    if (modalCloseX) modalCloseX.addEventListener("click", closeModal);
    if (cancelPaymentBtn) cancelPaymentBtn.addEventListener("click", closeModal);

    if (modal) {
        modal.addEventListener("click", function (e) {
            if (e.target === modal) closeModal();
        });
    }

    if (confirmPaymentBtn) {
        confirmPaymentBtn.addEventListener("click", async function () {
            setButtonLoading(confirmPaymentBtn, true);
            const orderRefText = document.getElementById("modal-order-ref")?.textContent || "";
            const orderId = orderRefText.replace("AROMACAFE-", "").trim();
            if (orderId) {
                await checkOrderStatus(orderId);
            }
            setButtonLoading(confirmPaymentBtn, false);
        });
    }

    function setButtonLoading(btn, loading) {
        if (!btn) return;
        if (loading) {
            btn.dataset.originalHtml = btn.innerHTML;
            btn.innerHTML = '<span class="spinner"></span> Đang xử lý...';
            btn.classList.add('btn-loading');
        } else {
            btn.innerHTML = btn.dataset.originalHtml || btn.innerHTML;
            btn.classList.remove('btn-loading');
        }
    }

    window.copyText = function (text, btnEl) {
        navigator.clipboard.writeText(text).then(() => {
            if (btnEl) {
                const original = btnEl.innerHTML;
                btnEl.innerHTML = '<i class="fas fa-check"></i>';
                btnEl.classList.add('copied');
                setTimeout(() => {
                    btnEl.innerHTML = original;
                    btnEl.classList.remove('copied');
                }, 1800);
            }
        }).catch(() => {
            const ta = document.createElement('textarea');
            ta.value = text;
            document.body.appendChild(ta);
            ta.select();
            document.execCommand('copy');
            document.body.removeChild(ta);
        });
    };

    window.copyTransferContent = function () {
        const el = document.getElementById("transfer-content-text");
        if (el) copyText(el.textContent.trim(), document.getElementById("copy-content-btn"));
    };

    window.copyModalContent = function () {
        const el = document.getElementById("modal-transfer-content");
        if (el) copyText(el.textContent.trim(), null);
    };

    window.openQrZoom = function (imgId) {
        const targetImg = document.getElementById(imgId);
        if (!targetImg) return;
        const src = targetImg.src;
        document.getElementById("qr-zoom-img").src = src;
        document.getElementById("qr-zoom-modal").style.display = "flex";
        document.body.style.overflow = "hidden";
    };

    window.closeQrZoom = function () {
        document.getElementById("qr-zoom-modal").style.display = "none";
        document.body.style.overflow = "";
    };

    // Khởi chạy nạp dữ liệu ban đầu một cách duy nhất
    loadProvinces();
    calculateTotal();
    onPaymentMethodChange();
});