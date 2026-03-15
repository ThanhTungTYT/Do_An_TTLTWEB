    document.addEventListener("DOMContentLoaded", function() {
    // 1. Khai báo các biến
    var promotionSelect = document.getElementById("promotionSelect");
    var totalEl = document.getElementById("total-price");
    var shippingEl = document.getElementById("shipping-fee");
    var discountEl = document.getElementById("discount-amount");
    var finalEl = document.getElementById("final-total");

    // 2. Hàm định dạng tiền tệ
    function formatVND(amount) {
    return amount.toLocaleString('vi-VN') + " VND";
}

    // 3. Hàm tính toán
    function calculateTotal() {
    // Lấy dữ liệu
    var total = parseFloat(totalEl.getAttribute("data-total")) || 0;
    var shipping = parseFloat(shippingEl.getAttribute("data-fee")) || 0;

    // Lấy option đang chọn
    var selectedOption = promotionSelect.options[promotionSelect.selectedIndex];
    var discountPercent = parseFloat(selectedOption.getAttribute("data-discount")) || 0;

    // Tính toán
    var discountValue = (total * discountPercent) / 100;
    var finalTotal = total + shipping - discountValue;

    console.log("Tổng gốc:", total);
    console.log("% Giảm:", discountPercent);
    console.log("Tiền giảm:", discountValue);

    // --- CẬP NHẬT GIAO DIỆN ---

    // Cập nhật dòng Giảm giá
    if (discountEl) {
    if (discountValue > 0) {
    discountEl.innerHTML = "- " + formatVND(discountValue);
} else {
    discountEl.innerHTML = "0 VND";
}
} else {
    console.error("LỖI: Không tìm thấy thẻ có id='discount-amount'");
}

    // Cập nhật dòng Tổng thanh toán
    if (finalEl) {
    finalEl.innerHTML = formatVND(finalTotal);
}
}

    // 4. Gắn sự kiện
    if (promotionSelect) {
    promotionSelect.addEventListener("change", calculateTotal);
}
});
