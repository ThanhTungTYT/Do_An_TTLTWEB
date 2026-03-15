document.addEventListener("DOMContentLoaded", function () {

    const checkboxes = document.querySelectorAll(".item-checkbox");
    const totalDisplay = document.getElementById("cart-total");
    const selectAllBtn = document.querySelector(".select-all-cart");

    function updateDisplayTotal() {
        let total = 0;
        document.querySelectorAll(".item-checkbox:checked").forEach(box => {
            total += parseFloat(box.dataset.subtotal);
        });
        totalDisplay.textContent = total.toLocaleString("vi-VN") + " VND";
    }

    checkboxes.forEach(cb => {
        cb.addEventListener("change", updateDisplayTotal);
    });

    if (selectAllBtn) {
        selectAllBtn.addEventListener("click", function (e) {
            e.preventDefault();
            const allChecked = [...checkboxes].every(cb => cb.checked);
            checkboxes.forEach(cb => cb.checked = !allChecked);
            this.textContent = !allChecked ? "Bỏ chọn tất cả" : "Chọn tất cả";
            updateDisplayTotal();
        });
    }

    updateDisplayTotal();
});