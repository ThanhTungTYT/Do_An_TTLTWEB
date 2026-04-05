document.addEventListener("DOMContentLoaded", function() {

    var promotionSelect = document.getElementById("promotionSelect");
    var totalEl = document.getElementById("total-price");
    var shippingEl = document.getElementById("shipping-fee");
    var discountEl = document.getElementById("discount-amount");
    var finalEl = document.getElementById("final-total");

    function formatVND(amount) {
        return amount.toLocaleString('vi-VN') + " VND";
    }

    function calculateTotal() {
        var total = parseFloat(totalEl.getAttribute("data-total")) || 0;
        var shipping = parseFloat(shippingEl.getAttribute("data-fee")) || 0;

        var selectedOption = promotionSelect.options[promotionSelect.selectedIndex];
        var discountPercent = parseFloat(selectedOption.getAttribute("data-discount")) || 0;

        var discountValue = (total * discountPercent) / 100;
        var finalTotal = total + shipping - discountValue;

        if (discountEl) {
            if (discountValue > 0) {
                discountEl.innerHTML = "- " + formatVND(discountValue);
            } else {
                discountEl.innerHTML = "0 VND";
            }
        }

        if (finalEl) {
            finalEl.innerHTML = formatVND(finalTotal);
        }
    }

    if (promotionSelect) {
        promotionSelect.addEventListener("change", calculateTotal);
    }

    const host = "https://provinces.open-api.vn/api/v2/";

    const citySelect = document.getElementById("citySelect");
    const wardSelect = document.getElementById("wardSelect");
    const hiddenProvince = document.getElementById("hidden_province");
    const hiddenWard = document.getElementById("hidden_ward");

    let savedProvinceName = hiddenProvince ? hiddenProvince.value.trim() : "";
    let savedWardName = hiddenWard ? hiddenWard.value.trim() : "";

    function loadProvinces() {
        fetch(host + "p/")
            .then(res => res.json())
            .then(data => {
                let options = '<option value="">-- Chọn Tỉnh/Thành phố --</option>';
                let matchedCode = "";

                data.forEach(tinh => {
                    options += `<option value="${tinh.code}" data-name="${tinh.name}">${tinh.name}</option>`;
                    if (tinh.name === savedProvinceName) {
                        matchedCode = tinh.code;
                    }
                });

                citySelect.innerHTML = options;

                if (matchedCode) {
                    citySelect.value = matchedCode;
                    loadWards(matchedCode, savedWardName);
                }
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
                        if (xa.name === wardNameToSelect) {
                            matchedCode = xa.code;
                        }
                    });
                }

                wardSelect.innerHTML = options;

                if (matchedCode) {
                    wardSelect.value = matchedCode;
                }
            });
    }

    if (citySelect) {
        citySelect.addEventListener("change", function() {
            let pCode = this.value;
            let pName = this.options[this.selectedIndex].getAttribute("data-name");

            if (pName) hiddenProvince.value = pName;
            else hiddenProvince.value = "";

            wardSelect.innerHTML = '<option value="">-- Đang tải... --</option>';
            hiddenWard.value = "";

            if (pCode) {
                loadWards(pCode);
            } else {
                wardSelect.innerHTML = '<option value="">-- Chọn Phường/Xã --</option>';
            }
        });
    }

    if (wardSelect) {
        wardSelect.addEventListener("change", function() {
            let wName = this.options[this.selectedIndex].getAttribute("data-name");
            if (wName) {
                hiddenWard.value = wName;
            } else {
                hiddenWard.value = "";
            }
        });
    }

    if (citySelect) {
        loadProvinces();
    }
});