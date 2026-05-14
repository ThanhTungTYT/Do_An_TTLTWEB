document.addEventListener("DOMContentLoaded", function () {

    var promotionSelect = document.getElementById("promotionSelect");
    var totalEl         = document.getElementById("total-price");
    var shippingEl      = document.getElementById("shipping-fee");
    var discountEl      = document.getElementById("discount-amount");
    var finalEl         = document.getElementById("final-total");

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
    const citySelect      = document.getElementById("citySelect");
    const wardSelect      = document.getElementById("wardSelect");
    const hiddenProvince  = document.getElementById("hidden_province");
    const hiddenWard      = document.getElementById("hidden_ward");

    function showError(input, message) {
        input.classList.add('input-error');
        let errLabel = input.nextElementSibling;
        if (errLabel && errLabel.classList.contains('error')) {
            errLabel.textContent = message;
            errLabel.style.display = 'block';
        }
    }

    function clearError(input) {
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
        if (citySelect.value === '') { showError(citySelect, 'Vui lòng chọn Tỉnh/Thành phố!'); isValid = false; }
        else clearError(citySelect);
        if (wardSelect.value === '') { showError(wardSelect, 'Vui lòng chọn Phường/Xã!'); isValid = false; }
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

    fullnameInput.addEventListener('input', validateFullName);
    phoneInput.addEventListener('input', validatePhone);
    addressInput.addEventListener('input', validateAddress);

    const host = "https://provinces.open-api.vn/api/v2/";
    let savedProvinceName = hiddenProvince ? hiddenProvince.value.trim() : "";
    let savedWardName     = hiddenWard     ? hiddenWard.value.trim()     : "";

    function loadProvinces() {
        fetch(host + "p/")
            .then(res => res.json())
            .then(data => {
                let options = '<option value="">-- Chọn Tỉnh/Thành phố --</option>';
                let matchedCode = "";
                data.forEach(tinh => {
                    options += `<option value="${tinh.code}" data-name="${tinh.name}">${tinh.name}</option>`;
                    if (tinh.name === savedProvinceName) matchedCode = tinh.code;
                });
                citySelect.innerHTML = options;
                if (matchedCode) { citySelect.value = matchedCode; loadWards(matchedCode, savedWardName); }
            })
            .catch(err => console.error("Lỗi API Tỉnh/Thành:", err));
    }

    function loadWards(provinceCode, wardNameToSelect = "") {
        fetch(host + "p/" + provinceCode + "?depth=2")
            .then(res => res.json())
            .then(data => {
                let options = '<option value="">-- Chọn Phường/Xã --</option>';
                let matchedCode = "";
                if (data.wards) {
                    data.wards.forEach(xa => {
                        options += `<option value="${xa.code}" data-name="${xa.name}">${xa.name}</option>`;
                        if (xa.name === wardNameToSelect) matchedCode = xa.code;
                    });
                }
                wardSelect.innerHTML = options;
                if (matchedCode) wardSelect.value = matchedCode;
            });
    }

    if (citySelect) {
        citySelect.addEventListener("change", function () {
            let pName = this.options[this.selectedIndex].getAttribute("data-name");
            hiddenProvince.value = pName || "";
            wardSelect.innerHTML = '<option value="">-- Đang tải... --</option>';
            hiddenWard.value = "";
            if (this.value) loadWards(this.value);
            else wardSelect.innerHTML = '<option value="">-- Chọn Phường/Xã --</option>';
            validateLocation();
        });
        loadProvinces();
    }

    if (wardSelect) {
        wardSelect.addEventListener("change", function () {
            let wName = this.options[this.selectedIndex].getAttribute("data-name");
            hiddenWard.value = wName || "";
            validateLocation();
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

        bankInfoPanel.style.display    = "none";
        ewalletInfoPanel.style.display = "none";
        placeOrderBtn.style.display    = "block";
        openBankModalBtn.style.display = "none";

        if (BANK_METHODS.includes(val)) {
            bankInfoPanel.style.display   = "block";
            placeOrderBtn.style.display   = "none";
            openBankModalBtn.style.display = "block";
        } else if (EWALLET_METHODS.includes(val)) {
            ewalletInfoPanel.style.display = "block";
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

    // ===== CẬP NHẬT QR CODE THEO SỐ TIỀN =====
    function updateQrAmount() {
        const selected = document.querySelector('input[name="paymentMethod"]:checked');
        if (!selected || !BANK_METHODS.includes(selected.value)) return;

        const { finalTotal } = getFinalAmount();
        const amountInt = Math.round(finalTotal);
        const contentEl = document.getElementById("transfer-content-text");
        const content   = contentEl ? contentEl.textContent.trim().replace(/\s+/g, '+') : "AROMACAFE";

        const qrImg = document.getElementById("qr-image");
        if (qrImg) {
            qrImg.src = `https://img.vietqr.io/image/BIDV-1234567890123456-compact2.png?amount=${amountInt}&addInfo=${content}&accountName=AROMA+CAFE`;
        }
    }

    placeOrderBtn.addEventListener("click", function () {
        if (!validateAll()) {
            const firstError = document.querySelector('.input-error');
            if (firstError) firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
            return;
        }
        setButtonLoading(placeOrderBtn, true);
        checkoutForm.submit();
    });

    openBankModalBtn.addEventListener("click", function () {
        if (!validateAll()) {
            const firstError = document.querySelector('.input-error');
            if (firstError) firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
            return;
        }
        openBankModal();
    });

    const modal             = document.getElementById("bank-payment-modal");
    const modalCloseX       = document.getElementById("modal-close-x");
    const confirmPaymentBtn = document.getElementById("confirm-payment-btn");
    const cancelPaymentBtn  = document.getElementById("cancel-payment-btn");
    const modalAmountEl     = document.getElementById("modal-amount-display");
    const modalQrImg        = document.getElementById("modal-qr-image");
    const modalTimestamp    = document.getElementById("modal-timestamp");

    function openBankModal() {
        const { finalTotal } = getFinalAmount();
        const amountInt = Math.round(finalTotal);

        if (modalAmountEl) modalAmountEl.textContent = formatVND(amountInt);

        if (modalTimestamp) {
            modalTimestamp.textContent = Date.now();
        }

        const contentEl = document.getElementById("transfer-content-text");
        const content   = contentEl ? contentEl.textContent.trim().replace(/\s+/g, '+') : "AROMACAFE";
        if (modalQrImg) {
            modalQrImg.src = `https://img.vietqr.io/image/BIDV-1234567890123456-compact2.png?amount=${amountInt}&addInfo=${content}&accountName=AROMA+CAFE`;
        }

        modal.style.display = "flex";
        document.body.style.overflow = "hidden";
    }

    function closeModal() {
        modal.style.display = "none";
        document.body.style.overflow = "";
    }

    modalCloseX.addEventListener("click", closeModal);
    cancelPaymentBtn.addEventListener("click", closeModal);

    modal.addEventListener("click", function (e) {
        if (e.target === modal) closeModal();
    });

    confirmPaymentBtn.addEventListener("click", function () {
        setButtonLoading(confirmPaymentBtn, true);
        checkoutForm.submit();
    });

    function setButtonLoading(btn, loading) {
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
        const src = document.getElementById(imgId).src;
        document.getElementById("qr-zoom-img").src = src;
        document.getElementById("qr-zoom-modal").style.display = "flex";
        document.body.style.overflow = "hidden";
    };

    window.closeQrZoom = function () {
        document.getElementById("qr-zoom-modal").style.display = "none";
        document.body.style.overflow = "";
    };

    calculateTotal();
    onPaymentMethodChange();
});